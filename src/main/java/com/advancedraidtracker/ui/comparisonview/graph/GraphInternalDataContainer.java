package com.advancedraidtracker.ui.comparisonview.graph;

import com.advancedraidtracker.SimpleRaidDataBase;

import java.util.ArrayList;

public class GraphInternalDataContainer
{
    public ArrayList<SimpleRaidDataBase> fullData;
    public ArrayList<Integer> intData;

    public GraphInternalDataContainer(ArrayList<SimpleRaidDataBase> fullData, ArrayList<Integer> intData)
    {
        this.fullData = fullData;
        this.intData = intData;
    }
}
