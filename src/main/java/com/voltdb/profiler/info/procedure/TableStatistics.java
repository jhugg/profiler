package com.voltdb.profiler.info.procedure;


public class TableStatistics  {

    long partitionId;
    String tableName;
    String tableType;
    long tupleCount;
    long tupleAllocatedMemory;
    long tupleDataMemory;
    long StringDataMemory;
    /**
     * @return the partitionId
     */
    public long getPartitionId() {
        return partitionId;
    }
    /**
     * @param partitionId the partitionId to set
     */
    public void setPartitionId(long partitionId) {
        this.partitionId = partitionId;
    }
    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }
    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    /**
     * @return the tupleCount
     */
    public long getTupleCount() {
        return tupleCount;
    }
    /**
     * @param tupleCount the tupleCount to set
     */
    public void setTupleCount(long tupleCount) {
        this.tupleCount = tupleCount;
    }
    /**
     * @return the tupleAllocatedMemory
     */
    public long getTupleAllocatedMemory() {
        return tupleAllocatedMemory;
    }
    /**
     * @param tupleAllocatedMemory the tupleAllocatedMemory to set
     */
    public void setTupleAllocatedMemory(long tupleAllocatedMemory) {
        this.tupleAllocatedMemory = tupleAllocatedMemory;
    }
    /**
     * @return the tupleDataMemory
     */
    public long getTupleDataMemory() {
        return tupleDataMemory;
    }
    /**
     * @param tupleDataMemory the tupleDataMemory to set
     */
    public void setTupleDataMemory(long tupleDataMemory) {
        this.tupleDataMemory = tupleDataMemory;
    }
    /**
     * @return the stringDataMemory
     */
    public long getStringDataMemory() {
        return StringDataMemory;
    }
    /**
     * @param stringDataMemory the stringDataMemory to set
     */
    public void setStringDataMemory(long stringDataMemory) {
        StringDataMemory = stringDataMemory;
    }
    /**
     * @return the tableType
     */
    public String getTableType() {
        return tableType;
    }
    /**
     * @param tableType the tableType to set
     */
    public void setTableType(String tableType) {
        this.tableType = tableType;
    }
    

}