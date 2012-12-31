package com.voltdb.profiler.info.system;

import java.util.ArrayList;
import java.util.HashMap;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;

import com.voltdb.profiler.renderer.Renderer;
import com.voltdb.profiler.table.Column;
import com.voltdb.profiler.table.Table;

public class SystemInformationDeploymentTable extends Table {

    private Column property;
    private Column value;

    Client client;
    private ArrayList<SystemInformationDeployment> list = new ArrayList<SystemInformationDeployment>();
    private Renderer renderer;

    public SystemInformationDeploymentTable(Client client, Renderer renderer) {
        this.client = client;
        this.renderer = renderer;

        property = new Column(25, "Property", this.renderer, true);
        value = new Column(95, "Value", this.renderer, true);
    }

    public void drawHeader() {
        renderer.printf("System Information Deployment Configuration%n");

        this.property.writeHeader();
        this.value.writeHeader();
        renderer.println();

        this.property.writeUnderline();
        this.value.writeUnderline();
        renderer.println();
    }

    public void collectStatistics() {
        try {
            ClientResponse cr = this.client.callProcedure("@SystemInformation",
                    "deployment");
            if (cr.getStatus() == ClientResponse.SUCCESS) {
                VoltTable[] tables = cr.getResults();
                if (tables != null && tables.length > 0) {
                    while (tables[0].advanceRow()) {
                        String property = tables[0].getString("PROPERTY");
                        String value = tables[0].getString("VALUE");

                        SystemInformationDeployment deploymentInfo = new SystemInformationDeployment();
                        deploymentInfo.setProperty(property);
                        deploymentInfo.setValue(value);
                        this.list.add(deploymentInfo);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printStatistics() {
        for (SystemInformationDeployment stats : this.list) {
            this.property.writeColumn(stats.getProperty());
            this.value.writeColumn(stats.getValue());
            this.renderer.println();
        }
    }
}
