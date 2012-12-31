package com.voltdb.profiler.renderer;


public class RendererFactory {

    public static enum RendererType {
        SYSTEM("system");
        
        String name;
        
        private RendererType(String name) {
            this.name = name;
        }
    }
    
    public static Renderer get(RendererType type) {
        Renderer result = new PrintRenderer();
        return result;
    }

}
