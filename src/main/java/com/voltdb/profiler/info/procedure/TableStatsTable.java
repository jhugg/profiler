package com.voltdb.profiler.info.procedure;

import java.util.ArrayList;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;

import com.voltdb.profiler.renderer.Renderer;
import com.voltdb.profiler.table.Column;
import com.voltdb.profiler.table.Table;

public class TableStatsTable extends Table {

    private Column partition;
    private Column table;
    private Column type;
    private Column tuples;
    private Column allocatedMemory;
    private Column dataMemory;
    private Column stringMemory;

    private ArrayList<TableStatistics> list = new ArrayList<TableStatistics>();

    public TableStatsTable(Client client, Renderer renderer) {
        super(client, renderer, "Table Statistics", "@Statistics", "table", 0);

        this.partition = new Column(10, "Partition", "PARTITION_ID",
                this.renderer);
        this.table = new Column(45, "Table", "TABLE_NAME", this.renderer);
        this.type = new Column(25, "Type", "TABLE_TYPE", this.renderer);
        this.tuples = new Column(15, "Tuples", "TUPLE_COUNT", this.renderer);
        this.allocatedMemory = new Column(17, "Allocated Memory",
                "TUPLE_ALLOCATED_MEMORY", this.renderer);
        this.dataMemory = new Column(15, "Data Memory", "TUPLE_DATA_MEMORY",
                this.renderer);
        this.stringMemory = new Column(15, "String Memory",
                "String_DATA_MEMORY", this.renderer);

        this.setColumns(new Column[] { this.partition, this.table, this.type,
                this.tuples, this.allocatedMemory, this.dataMemory,
                this.stringMemory });
    }

    protected void convertRows(VoltTable[] tables) {
        long partition = tables[0].getLong(this.partition.getDataMapping());
        String table = tables[0].getString(this.table.getDataMapping());
        String type = tables[0].getString(this.type.getDataMapping());
        long tuples = tables[0].getLong(this.tuples.getDataMapping());
        long allocatedMemory = tables[0].getLong(this.allocatedMemory
                .getDataMapping());
        long dataMemory = tables[0].getLong(this.dataMemory.getDataMapping());
        long stringMemory = tables[0].getLong(this.stringMemory
                .getDataMapping());

        TableStatistics tableStats = new TableStatistics();
        tableStats.setTableName(table);
        tableStats.setTableType(type);
        tableStats.setPartitionId(partition);
        tableStats.setTupleCount(tuples);
        tableStats.setTupleAllocatedMemory(allocatedMemory);
        tableStats.setTupleDataMemory(dataMemory);
        tableStats.setStringDataMemory(stringMemory);

        list.add(tableStats);
    }

    public void printStatistics() {
        for (TableStatistics stats : this.list) {
            this.partition.writeColumn(stats.getPartitionId());
            this.table.writeColumn(stats.getTableName());
            this.type.writeColumn(stats.getTableType());
            this.tuples.writeColumn(stats.getTupleCount());
            this.allocatedMemory.writeColumn(stats.getTupleAllocatedMemory());
            this.dataMemory.writeColumn(stats.getTupleDataMemory());
            this.stringMemory.writeColumn(stats.getStringDataMemory());
            
            this.renderer.println();
        }
    }
}
