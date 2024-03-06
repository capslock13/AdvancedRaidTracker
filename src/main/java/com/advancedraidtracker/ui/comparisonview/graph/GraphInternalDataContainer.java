package com.advancedraidtracker.ui.comparisonview.graph;

import com.advancedraidtracker.SimpleRaidData;

import java.util.ArrayList;

public class GraphInternalDataContainer
{
    public ArrayList<SimpleRaidData> fullData;
    public ArrayList<Integer> intData;

    public GraphInternalDataContainer(ArrayList<SimpleRaidData> fullData, ArrayList<Integer> intData)
    {
        this.fullData = fullData;
        this.intData = intData;
    }
}
