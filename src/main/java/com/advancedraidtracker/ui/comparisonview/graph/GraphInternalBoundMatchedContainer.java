package com.advancedraidtracker.ui.comparisonview.graph;

import com.advancedraidtracker.SimpleTOBData;

import java.util.ArrayList;

public class GraphInternalBoundMatchedContainer
{
    public ArrayList<ArrayList<SimpleTOBData>> fullData;
    public ArrayList<Integer> intData;

    public GraphInternalBoundMatchedContainer(ArrayList<ArrayList<SimpleTOBData>> fullData, ArrayList<Integer> intData)
    {
        this.fullData = fullData;
        this.intData = intData;
    }
}
