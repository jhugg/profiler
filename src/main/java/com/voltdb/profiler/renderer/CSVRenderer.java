package com.voltdb.profiler.renderer;


public class CSVRenderer extends PrintRenderer {

    @Override
    public void printf(String formatString, Object... items) {
        String value = String.format(formatString, items).replace("\"", "\"\"");
        super.print("\"");
        super.printf(value);
        super.print("\"");
        super.print(",");
    }

    @Override
    public void print(String string) {
        
        super.print("\"" + string.replace("\"", "\"\"") + "\"");
        super.print(",");
    }

    @Override
    public void printUnderline(int width) {
    }

}
