package com.voltdb.profiler.info.system;

import java.util.TreeSet;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import com.voltdb.profiler.renderer.Renderer;
import com.voltdb.profiler.table.Column;
import com.voltdb.profiler.table.Table;

public class SystemInformationOverviewTable extends Table {

    private Column hostID;
    private Column property;
    private Column value;

    private TreeSet<SystemInformationOverview> set = new TreeSet<SystemInformationOverview>();

    public SystemInformationOverviewTable(Client client, Renderer renderer) {
        super(client, renderer, "System Information Overview", "@SystemInformation",
                "overview");

        this.hostID = new Column(10, "Host ID", "HOST_ID", this.renderer);
        this.property = new Column(35, "Property", "KEY", this.renderer);
        this.value = new Column(75, "Value","VALUE", this.renderer, true);
        
        this.setColumns(new Column[] { this.hostID, this.property, this.value });
    }

    public void printStatistics() {
        for (SystemInformationOverview stats : this.set) {
            this.hostID.writeColumn(stats.getHostID());
            this.property.writeColumn(stats.getProperty());
            this.value.writeColumn(stats.getValue());
            renderer.println();
        }
    }
    
    @Override
    protected void convertRows(VoltTable[] tables) {
        String host = "" + tables[0].getLong(this.hostID.getDataMapping());
        String property = tables[0].getString(this.property.getDataMapping());
        String value = tables[0].getString(this.value.getDataMapping());

        SystemInformationOverview overview = new SystemInformationOverview();
        overview.setHostID(host);
        overview.setProperty(property);
        overview.setValue(value);

        this.set.add(overview);
    }
}
