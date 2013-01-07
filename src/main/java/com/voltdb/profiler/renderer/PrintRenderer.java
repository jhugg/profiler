package com.voltdb.profiler.renderer;

import java.util.Arrays;


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

    @Override
    public void printUnderline(int width) {
        char[] tmp = new char[width];
        Arrays.fill(tmp, '=');
        this.print(new String(tmp));
    }
    
    

}
