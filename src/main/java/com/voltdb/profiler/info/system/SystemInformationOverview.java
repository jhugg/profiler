package com.voltdb.profiler.info.system;


public class SystemInformationOverview  implements Comparable<SystemInformationOverview>{

    String hostID;
    String property;
    String value;
    /**
     * @return the hostID
     */
    public String getHostID() {
        return hostID;
    }
    /**
     * @param hostID the hostID to set
     */
    public void setHostID(String hostID) {
        this.hostID = hostID;
    }
    /**
     * @return the property
     */
    public String getProperty() {
        return property;
    }
    /**
     * @param property the property to set
     */
    public void setProperty(String property) {
        this.property = property;
    }
    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    @Override
    public int compareTo(SystemInformationOverview that) {
        return (this.getHostID() + this.getProperty()).compareToIgnoreCase(that.getHostID() + that.getProperty());
    }

}