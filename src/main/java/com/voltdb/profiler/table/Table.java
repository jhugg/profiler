package com.voltdb.profiler.table;

public abstract class Table {


    public void show() {
        this.collectStatistics();
        this.drawHeader();
        this.printStatistics();
    }

    protected abstract void printStatistics() ;

    protected abstract void drawHeader();

    protected abstract void collectStatistics();

}