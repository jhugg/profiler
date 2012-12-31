package com.voltdb.profiler.info.catalog;

import java.util.TreeSet;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;

import com.voltdb.profiler.renderer.Renderer;
import com.voltdb.profiler.table.Column;
import com.voltdb.profiler.table.Table;

public class SystemCatalogProcedureStatsTable extends Table {

    private Column procedure;
    private Column remarks;

    private TreeSet<SystemCatalogProcedureStatistics> set = new TreeSet<SystemCatalogProcedureStatistics>();

    public SystemCatalogProcedureStatsTable(Client client, Renderer renderer) {
        super(client, renderer, "Procedure Statistics", "@SystemCatalog",
                "procedures");

        this.procedure = new Column(40, "Procedure", "PROCEDURE_NAME",
                this.renderer, true);
        this.remarks = new Column(80, "Info", "REMARKS", this.renderer);

        this.setColumns(new Column[] { this.procedure, this.remarks });
    }

    public void printStatistics() {
        for (SystemCatalogProcedureStatistics stats : this.set) {
            this.procedure.writeColumn(stats.getProcedure());
            this.remarks.writeColumn(stats.getRemarks());
            this.renderer.println();
        }
    }

    @Override
    protected void convertRows(VoltTable[] tables) {
        String procedure = tables[0].getString(this.procedure.getDataMapping());
        String remarks = tables[0].getString(this.remarks.getDataMapping());

        SystemCatalogProcedureStatistics procStats = new SystemCatalogProcedureStatistics();
        procStats.setProcedure(procedure);
        procStats.setRemarks(remarks);

        this.set.add(procStats);
    }
}
