package com.voltdb.profiler.renderer;

public abstract class Renderer {

    public abstract void printf(String formatString, Object... items);
    public abstract void print(String string);
    public abstract void println() ;

}
