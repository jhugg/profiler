package com.voltdb.profiler.info.catalog;

public class SystemCatalogProcedureStatistics implements Comparable<SystemCatalogProcedureStatistics>{

    String procedure;
    String remarks;
    
    @Override
    public int compareTo(SystemCatalogProcedureStatistics that) {
        // TODO Auto-generated method stub
        return this.procedure.compareToIgnoreCase(that.procedure);
    }

    /**
     * @return the procedure
     */
    public String getProcedure() {
        return procedure;
    }

    /**
     * @param procedure the procedure to set
     */
    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}