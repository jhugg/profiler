package com.voltdb.profiler;

import com.voltdb.profiler.configuration.SampleConfiguration;
import com.voltdb.profiler.configuration.SampleConfigurationFactory;
import com.voltdb.profiler.info.catalog.SystemCatalogColumnsTable;
import com.voltdb.profiler.info.catalog.SystemCatalogProcedureStatsTable;
import com.voltdb.profiler.info.procedure.ProcedureStatsTable;
import com.voltdb.profiler.info.procedure.TableStatsTable;
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
        System.out.println(config.c);
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
        RendererFactory.RendererType rendererType = (this.config.c ? RendererFactory.RendererType.CSV
                : RendererFactory.RendererType.SYSTEM);
        Renderer renderer = RendererFactory.get(rendererType);

        if (this.config.verbose || this.config.i) {
            showSystemInformationOverview(renderer);
            showSystemInformationDeployment(renderer);
        }

        if (this.config.verbose || this.config.p) {
            showProcedureStatistics(renderer);
        }

        if (this.config.verbose || this.config.o) {
            showSystemCatalogProcedure(renderer);
        }

        if (this.config.verbose || this.config.t) {
            showSystemCatalogProcedureColumns(renderer);
        }

        if (this.config.verbose || this.config.s) {
            showTableStatistics(renderer);
        }
    }

    protected void showProcedureStatistics(Renderer renderer) {
        ProcedureStatsTable procStatsTable = new ProcedureStatsTable(
                this.client, renderer);
        procStatsTable.show();
    }

    protected void showSystemInformationDeployment(Renderer renderer) {
        SystemInformationDeploymentTable deploymentTable = new SystemInformationDeploymentTable(
                this.client, renderer);
        deploymentTable.show();
    }

    protected void showSystemInformationOverview(Renderer renderer) {
        SystemInformationOverviewTable deploymentTable = new SystemInformationOverviewTable(
                this.client, renderer);
        deploymentTable.show();
    }

    protected void showSystemCatalogProcedure(Renderer renderer) {
        SystemCatalogProcedureStatsTable procStatsTable = new SystemCatalogProcedureStatsTable(
                this.client, renderer);
        procStatsTable.show();
    }

    protected void showSystemCatalogProcedureColumns(Renderer renderer) {
        SystemCatalogColumnsTable columnStatsTable = new SystemCatalogColumnsTable(
                this.client, renderer);
        columnStatsTable.show();
    }

    protected void showTableStatistics(Renderer renderer) {
        TableStatsTable tableStats = new TableStatsTable(this.client,
                renderer);
        tableStats.show();
    }
}
