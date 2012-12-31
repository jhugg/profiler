package com.voltdb.profiler.info.catalog;

import java.util.HashMap;
import java.util.TreeSet;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;

import com.voltdb.profiler.renderer.Renderer;
import com.voltdb.profiler.table.Column;
import com.voltdb.profiler.table.Table;

public class SystemCatalogProcedureStatsTable extends Table {

    private  Column procedure;
    private  Column remarks;

    Client client;
    private TreeSet<SystemCatalogProcedureStatistics> set = new TreeSet<SystemCatalogProcedureStatistics>();
    private Renderer renderer;

    public SystemCatalogProcedureStatsTable(Client client, Renderer renderer) {
        this.client = client;
        this.renderer = renderer;
        
        procedure = new Column(40, "Procedure",this.renderer, true);
        remarks = new Column(80, "Info",this.renderer);
    }

    public void drawHeader() {
        renderer.printf("Procedure Statistics%n");
        
        this.procedure.writeHeader();
        this.remarks.writeHeader();
        renderer.println();

        this.procedure.writeUnderline();
        this.remarks.writeUnderline();
        renderer.println();
    }

    public void collectStatistics() {
        try {
            ClientResponse cr = this.client.callProcedure("@SystemCatalog",
                    "procedures");
            if (cr.getStatus() == ClientResponse.SUCCESS) {
                VoltTable[] tables = cr.getResults();
                if (tables != null && tables.length > 0) {
                    while (tables[0].advanceRow()) {
                        String procedure = tables[0].getString("PROCEDURE_NAME");
                        String remarks = tables[0].getString("REMARKS");

                        SystemCatalogProcedureStatistics procStats = new SystemCatalogProcedureStatistics();
                        procStats.setProcedure(procedure);
                        procStats.setRemarks(remarks);
                        
                        this.set.add(procStats);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printStatistics() {
        for (SystemCatalogProcedureStatistics stats : this.set) {
            this.procedure.writeColumn(stats.getProcedure());
            this.remarks.writeColumn(stats.getRemarks());
            this.renderer.println();
        }
    }
}
