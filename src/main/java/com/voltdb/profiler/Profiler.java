package com.voltdb.profiler;

import com.voltdb.profiler.configuration.SampleConfiguration;
import com.voltdb.profiler.configuration.SampleConfigurationFactory;
import com.voltdb.profiler.info.catalog.SystemCatalogProcedureStatsTable;
import com.voltdb.profiler.info.procedure.ProcedureStatsTable;
import com.voltdb.profiler.info.system.SystemInformationDeploymentTable;
import com.voltdb.profiler.info.system.SystemInformationOverviewTable;
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
        showSystemInformationOverview(renderer);
        showSystemInformationDeployment(renderer);
        showProcedureStatistics(renderer);
        showSystemCatalogProcedure(renderer);
    }

    protected void showProcedureStatistics(Renderer renderer) {
        ProcedureStatsTable procStatsTable = new ProcedureStatsTable(
                this.client, renderer);
        procStatsTable.show();
        renderer.println();
    }
    
    protected void showSystemInformationDeployment(Renderer renderer) {
        SystemInformationDeploymentTable deploymentTable = new SystemInformationDeploymentTable(
                this.client, renderer);
        deploymentTable.show();
        renderer.println();
    }
    
    protected void showSystemInformationOverview(Renderer renderer) {
        SystemInformationOverviewTable deploymentTable = new SystemInformationOverviewTable(
                this.client, renderer);
        deploymentTable.show();
        renderer.println();
    }
    
    protected void showSystemCatalogProcedure(Renderer renderer) {
        SystemCatalogProcedureStatsTable procStatsTable = new SystemCatalogProcedureStatsTable(
                this.client, renderer);
        procStatsTable.show();
        renderer.println();
    }
}
