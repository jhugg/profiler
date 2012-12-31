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
    
    private HashMap<String, ProcedureStatistics> map = new HashMap<String, ProcedureStatistics>();

    private float totalExecutionTime;

    public ProcedureStatsTable(Client client, Renderer renderer) {
        super(client,renderer, "Procedure Statistics", "@Statistics", "procedure", 0);
        
        this.procedure = new Column(40, "Procedure","PROCEDURE",this.renderer, true);
        this.calls = new Column(15, "Calls","INVOCATIONS",this.renderer);
        this.min = new Column(15, "Min(ms)","MIN_EXECUTION_TIME",this.renderer);
        this.max = new Column(15, "Max(ms)","MAX_EXECUTION_TIME",this.renderer);
        this.average = new Column(15, "Average(ms)","AVG_EXECUTION_TIME", this.renderer);
        this.total = new Column(20, "Total Time(ms)", null, this.renderer);
        this.percent = new Column(15, "Percent", null, this.renderer);
        
        this.totalExecutionTime = 0f;
        
        this.setColumns(new Column[] { this.procedure, this.calls, this.min, this.max, this.average, this.total, this.percent});
    }
    
    protected void convertRows(VoltTable[] tables) {
        String procedure = tables[0].getString(this.procedure.getDataMapping());
        long invocations = tables[0].getLong(this.calls.getDataMapping());
        long timedInvocations = tables[0]
                .getLong("TIMED_INVOCATIONS");
        long min = (timedInvocations > 0 ? tables[0]
                .getLong(this.min.getDataMapping()) : -1);
        long max = (timedInvocations > 0 ? tables[0]
                .getLong(this.max.getDataMapping()) : -1);
        long average = (timedInvocations > 0 ? tables[0]
                .getLong(this.average.getDataMapping()) : -1);

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
            this.renderer.println();
        }
    }
}
