package com.TheatreTracker.ui.comparisonview.graph;

import com.TheatreTracker.RoomData;

import java.util.ArrayList;

public class GraphInternalBoundMatchedContainer
{
    public ArrayList<ArrayList<RoomData>> fullData;
    public ArrayList<Integer> intData;

    public GraphInternalBoundMatchedContainer(ArrayList<ArrayList<RoomData>> fullData, ArrayList<Integer> intData)
    {
        this.fullData = fullData;
        this.intData = intData;
    }
}
