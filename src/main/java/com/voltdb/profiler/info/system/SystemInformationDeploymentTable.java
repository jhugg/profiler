package com.voltdb.profiler.info.system;

import java.util.ArrayList;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;

import com.voltdb.profiler.renderer.Renderer;
import com.voltdb.profiler.table.Column;
import com.voltdb.profiler.table.Table;

public class SystemInformationDeploymentTable extends Table {

    private Column property;
    private Column value;

    private ArrayList<SystemInformationDeployment> list = new ArrayList<SystemInformationDeployment>();

    public SystemInformationDeploymentTable(Client client, Renderer renderer) {
        super(client, renderer, "System Information Deployment Configuration", "@SystemInformation", "deployment");
        
        this.property = new Column(45, "Property", "PROPERTY", this.renderer, true);
        this.value = new Column(65, "Value", "VALUE", this.renderer, true);
        
        this.setColumns(new Column[] { this.property, this.value});
    }
    
    @Override
    protected void convertRows(VoltTable[] tables) {
        String property = tables[0].getString(this.property.getDataMapping());
        String value = tables[0].getString(this.value.getDataMapping());

        SystemInformationDeployment deploymentInfo = new SystemInformationDeployment();
        deploymentInfo.setProperty(property);
        deploymentInfo.setValue(value);
        this.list.add(deploymentInfo);
    }

    public void printStatistics() {
        for (SystemInformationDeployment stats : this.list) {
            this.property.writeColumn(stats.getProperty());
            this.value.writeColumn(stats.getValue());
            this.renderer.println();
        }
    }
}
