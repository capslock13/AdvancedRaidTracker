package com.advancedraidtracker.ui.comparisonview.graph;

import com.advancedraidtracker.utility.datautility.datapoints.Raid;

import java.util.ArrayList;

public class GraphInternalBoundMatchedContainer
{
    public ArrayList<ArrayList<Raid>> fullData;
    public ArrayList<Integer> intData;

    public GraphInternalBoundMatchedContainer(ArrayList<ArrayList<Raid>> fullData, ArrayList<Integer> intData)
    {
        this.fullData = fullData;
        this.intData = intData;
    }
}
