package com.voltdb.profiler.info.system;

import java.util.TreeSet;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;

import com.voltdb.profiler.renderer.Renderer;
import com.voltdb.profiler.table.Column;
import com.voltdb.profiler.table.Table;

public class SystemInformationOverviewTable extends Table {

    private  Column hostID;
    private  Column property;
    private  Column value;

    Client client;
    private TreeSet<SystemInformationOverview> set = new TreeSet<SystemInformationOverview>();
    private Renderer renderer;

    public SystemInformationOverviewTable(Client client, Renderer renderer) {
        this.client = client;
        this.renderer = renderer;
        
        hostID = new Column(10, "Host ID",this.renderer);
        property = new Column(35, "Property",this.renderer);
        value = new Column(75, "Value",this.renderer, true);
    }

    public void drawHeader() {
        renderer.printf("System Information Overview%n");
        
        this.hostID.writeHeader();
        this.property.writeHeader();
        this.value.writeHeader();
        renderer.println();

        this.hostID.writeUnderline();
        this.property.writeUnderline();
        this.value.writeUnderline();
        renderer.println();
    }

    public void collectStatistics() {
        try {
            ClientResponse cr = this.client.callProcedure("@SystemInformation",
                    "overview");
            if (cr.getStatus() == ClientResponse.SUCCESS) {
                VoltTable[] tables = cr.getResults();
                if (tables != null && tables.length > 0) {
                    while (tables[0].advanceRow()) {
                        String host = "" + tables[0].getLong("HOST_ID");
                        String property = tables[0].getString("KEY");
                        String value = tables[0].getString("VALUE");

                        SystemInformationOverview overview = new SystemInformationOverview();
                        overview.setHostID(host);
                        overview.setProperty(property);
                        overview.setValue(value);
                        
                        this.set.add(overview);
                       
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printStatistics() {
        for (SystemInformationOverview stats : this.set) {
            this.hostID.writeColumn(stats.getHostID());
            this.property.writeColumn(stats.getProperty());
            this.value.writeColumn(stats.getValue());
            renderer.println();
        }
    }
}
