package com.advancedraidtracker.ui.comparisonview.graph;

import com.advancedraidtracker.SimpleTOBData;

import java.util.ArrayList;

public class GraphInternalDataContainer
{
    public ArrayList<SimpleTOBData> fullData;
    public ArrayList<Integer> intData;

    public GraphInternalDataContainer(ArrayList<SimpleTOBData> fullData, ArrayList<Integer> intData)
    {
        this.fullData = fullData;
        this.intData = intData;
    }
}
