package com.advancedraidtracker.ui.comparisonview.graph;

public class PieChartData
{
    public int value;
    public int occurrences;
    public int sections;

    public PieChartData(int value, int occurrences, int total)
    {
        this.value = value;
        this.occurrences = occurrences;
        double percent = (double) occurrences / total;
        this.sections = (int) (percent * 360);
    }
}
