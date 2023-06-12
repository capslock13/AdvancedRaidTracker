package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.RoomData;

import java.util.ArrayList;

public class GraphInternalDataContainer
{
    public ArrayList<RoomData> fullData;
    public ArrayList<Integer> intData;

    public GraphInternalDataContainer(ArrayList<RoomData> fullData, ArrayList<Integer> intData)
    {
        this.fullData = fullData;
        this.intData = intData;
    }
}
