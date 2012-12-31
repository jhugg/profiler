package com.voltdb.profiler.renderer;


public class PrintRenderer extends Renderer {

    @Override
    public void printf(String formatString, Object... items) {
        System.out.printf(formatString, items);
    }

    @Override
    public void print(String string) {
        System.out.print(string);
    }

    @Override
    public void println() {
        System.out.println();
    }

}
