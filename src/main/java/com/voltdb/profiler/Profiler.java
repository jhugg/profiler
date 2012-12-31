package com.voltdb.profiler;

import com.voltdb.profiler.configuration.SampleConfiguration;
import com.voltdb.profiler.configuration.SampleConfigurationFactory;
import com.voltdb.profiler.info.procedure.ProcedureStatsTable;
import com.voltdb.profiler.renderer.Renderer;
import com.voltdb.profiler.renderer.RendererFactory;

/**
 * Profiler
 * 
 * @author andrewwilson
 * 
 */
public class Profiler extends BaseVoltApp {

    public static void main(String[] args) {
        SampleConfiguration config = SampleConfigurationFactory
                .getConfiguration(args);
        Profiler profiler = new Profiler(config);
        try {
            profiler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Profiler(SampleConfiguration config) {
        super(config);
    }

    @Override
    protected void execute() throws Exception {
        Renderer renderer = RendererFactory.get(RendererFactory.TYPE_SYSTEM);
        showProcedureStatistics(renderer);
    }

    protected void showProcedureStatistics(Renderer renderer) {
        ProcedureStatsTable procStatsTable = new ProcedureStatsTable(
                this.client, renderer);
        procStatsTable.show();
    }
}
