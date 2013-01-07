package com.voltdb.profiler.renderer;

public class RendererFactory {

    public static enum RendererType {
        CSV("csv"), 
        SYSTEM("system");

        String name;

        private RendererType(String name) {
            this.name = name;
        }
    }

    public static Renderer get(RendererType type) {
        Renderer result = null;
        switch (type) {
        case CSV:
            result = new CSVRenderer();
            break;
        default:
            result = new PrintRenderer();
        }
        return result;
    }

}
