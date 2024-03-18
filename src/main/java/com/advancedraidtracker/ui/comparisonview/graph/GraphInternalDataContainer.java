package com.advancedraidtracker.ui.comparisonview.graph;

import com.advancedraidtracker.utility.datautility.datapoints.Raid;

import java.util.List;

public class GraphInternalDataContainer
{
    public List<Raid> fullData;
    public List<Integer> intData;

    public GraphInternalDataContainer(List<Raid> fullData, List<Integer> intData)
    {
        this.fullData = fullData;
        this.intData = intData;
    }
}
