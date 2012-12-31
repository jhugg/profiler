package com.voltdb.profiler;

import java.util.Arrays;
import java.util.Calendar;

public class Column {

    int width;
    String text;
    String formatString;
    Renderer renderer;

    protected Column(int width, String string, Renderer renderer) {
        this.width = width;
        text = string;
        formatString = "%" + width + "s";
        this.renderer = renderer;
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
        renderer.printf(this.formatString, text);
    }

    public void writeUnderline() {
        char[] tmp = new char[width];
        Arrays.fill(tmp, '=');
        renderer.print(new String(tmp));
    }

    public void writeColumn(String value) {
        renderer.printf(this.formatString, value);
    }

    public void writeColumnPercent(float percentTotalExecutionTime,
            int precision) {
        String tmpFormatString = "%." + precision + "f";
        renderer.printf(this.formatString, String.format(tmpFormatString, percentTotalExecutionTime));

    }

    public void newLine() {
        renderer.println();
    }
}
