package com.voltdb.profiler.table;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;

import com.voltdb.profiler.renderer.Renderer;

public abstract class Table {

    protected Client client;
    protected Renderer renderer;
    protected String tableName;
    protected String sysProc;
    protected Object[] sysProcParameters;
    protected Column[] columns;

    public Table(Client client, Renderer renderer, String tableName, String sysProc, Object...parameters ) {
        this.client = client;
        this.renderer = renderer;
        this.tableName = tableName;
        this.sysProc = sysProc;
        this.sysProcParameters = parameters;
    }
    
    protected void setColumns(Column[] columns) {
        this.columns = columns;
    }

    public void show() {
        this.collectStatistics(this.sysProc, this.sysProcParameters);
        this.drawHeader();
        this.printStatistics();
        this.drawFooter();
    }
    
    protected void drawHeader() {
        renderer.printf("%s", this.tableName);
        renderer.println();
        
        for ( Column col: this.columns ) {
            col.writeHeader();
        }
        renderer.println();

        for ( Column col: this.columns ) {
            col.writeUnderline();
        }
        renderer.println();
    }
    
    public void collectStatistics(String name, Object... parameters ) {
        try {
            ClientResponse cr = this.client.callProcedure(name,parameters);
            if (cr.getStatus() == ClientResponse.SUCCESS) {
                VoltTable[] tables = cr.getResults();
                extractTables(tables);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void extractTables(VoltTable[] tables) {
        if (tables != null && tables.length > 0) {
            while (tables[0].advanceRow()) {
                convertRows(tables);
            }
        }
    }
    
    protected void drawFooter() {
        this.renderer.println();
    }

    protected abstract void printStatistics();
    
    protected abstract void convertRows(VoltTable[] tables);

}