package com.advancedraidtracker.ui.comparisonview.graph;

import com.advancedraidtracker.SimpleRaidDataBase;

import java.util.ArrayList;

public class GraphInternalBoundMatchedContainer
{
    public ArrayList<ArrayList<SimpleRaidDataBase>> fullData;
    public ArrayList<Integer> intData;

    public GraphInternalBoundMatchedContainer(ArrayList<ArrayList<SimpleRaidDataBase>> fullData, ArrayList<Integer> intData)
    {
        this.fullData = fullData;
        this.intData = intData;
    }
}
