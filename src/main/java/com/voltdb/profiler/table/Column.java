package com.voltdb.profiler.table;

import java.util.Arrays;
import java.util.Calendar;

import com.voltdb.profiler.renderer.Renderer;

public class Column {

    private int width;
    private String label;
    private String dataMapping;
    private String formatString;
    private Renderer renderer;
    private boolean truncate = false;

    private final static String TRUNCATE_PREFIX = "...";

    public Column(int width, String label, String dataMapping, Renderer renderer) {
        this(width, label, dataMapping, renderer, false);
    }

    public Column(int width, String label, String dataMapping,
            Renderer renderer, boolean truncate) {
        this.width = width;
        this.label = label;
        this.setDataMapping(dataMapping);
        this.formatString = "%" + (renderer.ignoreTruncation() ? "" : width)
                + "s";
        this.renderer = renderer;
        this.truncate = truncate && !renderer.ignoreTruncation();
    }

    public String getDataMapping() {
        return dataMapping;
    }

    public void setDataMapping(String dataMapping) {
        this.dataMapping = dataMapping;
    }

    public void writeColumn(long value) {
        String tmpValue = (value > -1 ? "" + value : "NA");
        this.writeColumn(tmpValue);
    }

    public void writeColumn(float value) {
        String tmpValue = (value > -1 ? "" + value : "NA");
        this.writeColumn(tmpValue);
    }

    public void writeColumn(long value, float denomenator) {
        String tmpValue = "NA";
        if (value > -1) {
            float f = (float) value / denomenator;
            tmpValue = "" + f;
        }
        this.writeColumn(tmpValue);
    }

    public void writeColumn(Calendar calendar) {
        this.writeColumn(String.format("%1$tD %1$tT", calendar));
    }

    public void writeHeader() {
        renderer.printf(this.formatString, label);
    }

    public void writeUnderline() {
        renderer.printUnderline(this.width);
    }

    public void writeColumn(String value) {
        if (value != null) {
            String tmpValue = new String(value);
            if (value.length() > this.width && this.truncate == true) {
                tmpValue = TRUNCATE_PREFIX
                        + value.substring(
                                (value.length() + TRUNCATE_PREFIX.length())
                                        - this.width, value.length());
            }
            renderer.printf(this.formatString, tmpValue);
        }
    }

    public void writeColumnPercent(float percentTotalExecutionTime,
            int precision) {
        String tmpFormatString = "%." + precision + "f";
        renderer.printf(this.formatString,
                String.format(tmpFormatString, percentTotalExecutionTime));

    }

    public void newLine() {
        renderer.println();
    }
}
