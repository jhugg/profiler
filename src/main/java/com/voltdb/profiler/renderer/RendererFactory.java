package com.voltdb.profiler;

public class RendererFactory {

    public final static String TYPE_SYSTEM = "system";
    
    public static Renderer get(String type) {
        Renderer result = new PrintRenderer();
        return result;
    }

}
