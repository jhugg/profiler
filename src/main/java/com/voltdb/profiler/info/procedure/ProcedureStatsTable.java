package com.voltdb.profiler.info.procedure;

import java.util.HashMap;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;

import com.voltdb.profiler.renderer.Renderer;
import com.voltdb.profiler.table.Column;
import com.voltdb.profiler.table.Table;

public class ProcedureStatsTable extends Table {
    private static final int NANOSECONDS = 1000 * 1000;

    private  Column procedure;
    private  Column calls;
    private  Column min;
    private  Column max;
    private  Column average;
    private  Column total;
    private  Column percent;

    Client client;
    private HashMap<String, ProcedureStatistics> map = new HashMap<String, ProcedureStatistics>();
    private Renderer renderer;

    private float totalExecutionTime;

    public ProcedureStatsTable(Client client, Renderer renderer) {
        this.client = client;
        this.renderer = renderer;
        
        procedure = new Column(40, "Procedure",this.renderer, true);
        calls = new Column(15, "Calls",this.renderer);
        min = new Column(15, "Min(ms)",this.renderer);
        max = new Column(15, "Max(ms)",this.renderer);
        average = new Column(15, "Average(ms)",this.renderer);
        total = new Column(20, "Total Time(ms)",this.renderer);
        percent = new Column(15, "Percent",this.renderer);
        
        this.totalExecutionTime = 0f;
    }

    public void drawHeader() {
        renderer.printf("Procedure Statistics%n");
        
        this.procedure.writeHeader();
        this.calls.writeHeader();
        this.min.writeHeader();
        this.max.writeHeader();
        this.average.writeHeader();
        this.total.writeHeader();
        this.percent.writeHeader();
        renderer.println();

        this.procedure.writeUnderline();
        this.calls.writeUnderline();
        this.min.writeUnderline();
        this.max.writeUnderline();
        this.average.writeUnderline();
        this.total.writeUnderline();
        this.percent.writeUnderline();
        renderer.println();
    }

    public void collectStatistics() {
        try {
            ClientResponse cr = this.client.callProcedure("@Statistics",
                    "procedure", 0);
            if (cr.getStatus() == ClientResponse.SUCCESS) {
                VoltTable[] tables = cr.getResults();
                if (tables != null && tables.length > 0) {
                    while (tables[0].advanceRow()) {
                        String procedure = tables[0].getString("PROCEDURE");
                        long invocations = tables[0].getLong("INVOCATIONS");
                        long timedInvocations = tables[0]
                                .getLong("TIMED_INVOCATIONS");
                        long min = (timedInvocations > 0 ? tables[0]
                                .getLong("MIN_EXECUTION_TIME") : -1);
                        long max = (timedInvocations > 0 ? tables[0]
                                .getLong("MAX_EXECUTION_TIME") : -1);
                        long average = (timedInvocations > 0 ? tables[0]
                                .getLong("AVG_EXECUTION_TIME") : -1);

                        ProcedureStatistics procStats = new ProcedureStatistics();
                        procStats.setProcedure(procedure);
                        procStats.setInvocations(invocations);
                        procStats.setTimedInvocations(timedInvocations);
                        procStats.setMinExecutionTime(min);
                        procStats.setMaxExecutionTime(max);
                        procStats.setReportedAverageExecutionTime(average);
                        procStats.setPartitions(1);

                        String normalizedProcName = procedure.toUpperCase();
                        this.totalExecutionTime += invocations * average;

                        ProcedureStatistics tmpStats = this.map
                                .get(normalizedProcName);
                        if (tmpStats != null) {
                            tmpStats.merge(procStats);
                        } else {
                            this.map.put(normalizedProcName, procStats);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printStatistics() {
        for (ProcedureStatistics stats : this.map.values()) {
            this.procedure.writeColumn(stats.getProcedure());
            this.calls.writeColumn(stats.getInvocations());
            this.min.writeColumn(stats.getMinExecutionTime(),
                    NANOSECONDS);
            this.max.writeColumn(stats.getMaxExecutionTime(),
                    NANOSECONDS);
            this.average.writeColumn(stats
                    .getAverageExecutionTime(NANOSECONDS));
            this.total.writeColumn(stats
                    .getTotalExecutionTime(NANOSECONDS));
            this.percent.writeColumnPercent(stats
                    .getPercentTotalExecutionTime(this.totalExecutionTime), 2);
            this.total.newLine();
        }
    }
}
