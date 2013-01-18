package com.voltdb.profiler.info.catalog;

import java.util.ArrayList;
import java.util.TreeSet;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;

import com.voltdb.profiler.info.system.SystemInformationDeployment;
import com.voltdb.profiler.renderer.Renderer;
import com.voltdb.profiler.table.Column;
import com.voltdb.profiler.table.Table;

public class SystemCatalogColumnsTable extends Table {

    private Column table;
    private Column columnName;
    private Column typeName;
    private Column size;
    private Column remarks;
    private Column nullable;

    private ArrayList<SystemCatalogColumns> list = new ArrayList<SystemCatalogColumns>();

    public SystemCatalogColumnsTable(Client client, Renderer renderer) {
        super(client, renderer, "Table Definitions", "@SystemCatalog",
                "columns");

        this.table = new Column(40, "Table Name", "TABLE_NAME", this.renderer,
                true);
        this.columnName = new Column(40, "Column Name", "COLUMN_NAME",
                this.renderer, true);
        this.typeName = new Column(15, "Type", "TYPE_NAME", this.renderer);
        this.size = new Column(10, "Size", "COLUMN_SIZE", this.renderer);
        this.remarks = new Column(25, "Remarks", "REMARKS", this.renderer);
        this.nullable = new Column(7, "Nullable", "IS_NULLABLE", this.renderer);

        this.setColumns(new Column[] { this.table, this.columnName,
                this.typeName, this.size, this.nullable, this.remarks });
    }

    public void printStatistics() {
        for (SystemCatalogColumns column : this.list) {
            this.table.writeColumn(column.getTable());
            this.columnName.writeColumn(column.getColumnName());
            this.typeName.writeColumn(column.getTypeName());
            this.size.writeColumn(column.getSize());
            this.nullable.writeColumn(column.getNullable());
            this.remarks.writeColumn(column.getRemarks());
            this.renderer.println();
        }
    }

    @Override
    protected void convertRows(VoltTable[] tables) {
        String table = tables[0].getString(this.table.getDataMapping());
        String columnName = tables[0].getString(this.columnName.getDataMapping());
        String typeName = tables[0].getString(this.typeName.getDataMapping());
        long size = tables[0].getLong(this.size.getDataMapping());
        String nullable = tables[0].getString(this.nullable.getDataMapping());
        String remarks = tables[0].getString(this.remarks.getDataMapping());

        SystemCatalogColumns column = new SystemCatalogColumns();
        column.setTable(table);
        column.setColumnName(columnName);
        column.setTypeName(typeName);
        column.setSize("" + size);
        column.setNullable(nullable);
        column.setRemarks(remarks);

        this.list.add(column);
    }
}
