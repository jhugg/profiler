package com.voltdb.profiler.info.system;

import java.util.HashMap;
import java.util.TreeSet;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import com.voltdb.profiler.renderer.Renderer;
import com.voltdb.profiler.table.Column;
import com.voltdb.profiler.table.Table;

public class SystemInformationOverviewTable extends Table {

    private Column addressColumn;
    private Column hostIDColumn;
    private Column propertyColumn;
    private Column valueColumn;

    private TreeSet<SystemInformationOverview> rowSet = new TreeSet<SystemInformationOverview>();
    private HashMap<String, String> map = new HashMap<String, String>();

    public SystemInformationOverviewTable(Client client, Renderer renderer) {
        super(client, renderer, "System Information Overview",
                "@SystemInformation", "overview");

        this.addressColumn = new Column(15, "Host IP", null, this.renderer);
        this.hostIDColumn = new Column(10, "Host ID", "HOST_ID", this.renderer);
        this.propertyColumn = new Column(35, "Property", "KEY", this.renderer);
        this.valueColumn = new Column(80, "Value", "VALUE", this.renderer, true);

        this.setColumns(new Column[] { this.addressColumn, this.hostIDColumn,
                this.propertyColumn, this.valueColumn });
    }

    public void printStatistics() {
        for (SystemInformationOverview stats : this.rowSet) {
            this.addressColumn.writeColumn(stats.getAddress());
            this.hostIDColumn.writeColumn(stats.getHostID());
            this.propertyColumn.writeColumn(stats.getProperty());
            this.valueColumn.writeColumn(stats.getValue());
            renderer.println();
        }
    }

    @Override
    protected void convertRows(VoltTable[] tables) {
        if (this.valueColumn.getDataMapping() != null) {
            String host = ""
                    + tables[0].getLong(this.hostIDColumn.getDataMapping());
            String property = tables[0].getString(this.propertyColumn
                    .getDataMapping());
            String value = tables[0].getString(this.valueColumn
                    .getDataMapping());

            SystemInformationOverview overview = new SystemInformationOverview();
            overview.setHostID(host);
            overview.setProperty(property);
            overview.setValue(value);
            this.rowSet.add(overview);

            String address = this.map.get(overview.getHostID());
            if (address == null) {
                if (property.equalsIgnoreCase("IPADDRESS")) {
                    address = overview.getValue();
                    this.map.put(overview.getHostID(), address);
                    updateAddress(overview.getHostID(), address);
                }
            } else {
                overview.setAddress(address);
            }
        }
    }

    private void updateAddress(String hostID, String address) {
        for (SystemInformationOverview row : this.rowSet) {
            if (row.getAddress() == null
                    && row.getHostID().equalsIgnoreCase(hostID)) {
                row.setAddress(address);
            }
        }

    }
}
