package com.voltdb.profiler;

import java.util.Calendar;

public class ProcedureStatistics  {

    String procedure;
    long timedInvocations;
    long invocations;
    int partitions;
    long minExecutionTime;
    long maxExecutionTime;
    long reportedAverageExecutionTime;
    

    /**
     * @return the procedure
     */
    public String getProcedure() {
        return procedure;
    }

    /**
     * @param procedure
     *            the procedure to set
     */
    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    /**
     * @return the timedInvocations
     */
    public long getTimedInvocations() {
        return timedInvocations;
    }

    /**
     * @param timedInvocations
     *            the timedInvocations to set
     */
    public void setTimedInvocations(long timedInvocations) {
        this.timedInvocations = timedInvocations;
    }

    /**
     * @return the invocations
     */
    public long getInvocations() {
        return invocations;
    }

    /**
     * @param invocations
     *            the invocations to set
     */
    public void setInvocations(long invocations) {
        this.invocations = invocations;
    }

    /**
     * @return the partitions
     */
    public int getPartitions() {
        return partitions;
    }

    /**
     * @param partitions
     *            the partitions to set
     */
    public void setPartitions(int partitions) {
        this.partitions = partitions;
    }

    /**
     * @return the minExecutionTime
     */
    public long getMinExecutionTime() {
        return minExecutionTime;
    }

    /**
     * @param minExecutionTime
     *            the minExecutionTime to set
     */
    public void setMinExecutionTime(long minExecutionTime) {
        this.minExecutionTime = minExecutionTime;
    }

    /**
     * @return the maxExecutionTime
     */
    public long getMaxExecutionTime() {
        return maxExecutionTime;
    }

    /**
     * @param maxExecutionTime
     *            the maxExecutionTime to set
     */
    public void setMaxExecutionTime(long maxExecutionTime) {
        this.maxExecutionTime = maxExecutionTime;
    }

    /**
     * @return the averageExecutionTime
     */
    public long getReportedAverageExecutionTime() {
        return reportedAverageExecutionTime;
    }

    /**
     * @param averageExecutionTime
     *            the averageExecutionTime to set
     */
    public void setReportedAverageExecutionTime(long averageExecutionTime) {
        this.reportedAverageExecutionTime = averageExecutionTime;
    }
    
    public float getAverageExecutionTime(float timeConversion) {
        return (this.reportedAverageExecutionTime/this.partitions)/timeConversion;
    }
    
    public float getTotalExecutionTime(float timeConversion) {
        return this.getInvocations() * this.getAverageExecutionTime(timeConversion);
    }
    
    public float getPercentTotalExecutionTime(float totalTime) {
        return (this.getTotalExecutionTime(1)/totalTime)*100f;
    }
    
    public void merge(ProcedureStatistics newProcStats) {
        long tmpTimedInvocations= this.timedInvocations + newProcStats.getTimedInvocations();
        long tmpInvocations = this.invocations + newProcStats.getInvocations();
        int tmpPartitions = this.partitions +  newProcStats.getPartitions();
        long tmpMinExecutionTime = Math.min(this.minExecutionTime, newProcStats.getMinExecutionTime());
        long tmpMaxExecutionTime = Math.max(this.maxExecutionTime, newProcStats.getMaxExecutionTime());
        long tmpReportedAverageExecutionTime = this.reportedAverageExecutionTime + newProcStats.getReportedAverageExecutionTime();
        
        this.timedInvocations = tmpTimedInvocations;
        this.invocations = tmpInvocations;
        this.partitions = tmpPartitions;
        this.minExecutionTime = tmpMinExecutionTime;
        this.maxExecutionTime = tmpMaxExecutionTime;
        this.reportedAverageExecutionTime = tmpReportedAverageExecutionTime;
        
        newProcStats.setInvocations(tmpInvocations);
        newProcStats.setTimedInvocations(tmpTimedInvocations);
        newProcStats.setPartitions(tmpPartitions);
        newProcStats.setMinExecutionTime(tmpMinExecutionTime);
        newProcStats.setMaxExecutionTime(tmpMaxExecutionTime);
        newProcStats.setReportedAverageExecutionTime(tmpReportedAverageExecutionTime);
        
    }

    

}