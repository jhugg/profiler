package com.voltdb.profiler.renderer;

import com.voltdb.profiler.configuration.SampleConfigurationFactory;

public abstract class Renderer {

    public boolean ignoreTruncation() {
        return SampleConfigurationFactory.getConfiguration().c;
    }
    public abstract void printf(String formatString, Object... items);
    public abstract void print(String string);
    public abstract void println();
    
    public abstract void printUnderline(int width);

}
