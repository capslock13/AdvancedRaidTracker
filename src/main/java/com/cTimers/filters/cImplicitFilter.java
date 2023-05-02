package com.cTimers.filters;

import com.cTimers.cRoomData;

public class cImplicitFilter
{
    private final cFilterCondition filter;

    public cImplicitFilter(cFilterDate filterDate)
    {
        filter = filterDate;
    }
    public cImplicitFilter(cFilterTime filterTime)
    {
        filter = filterTime;
    }
    public cImplicitFilter(cFilterPlayers filterPlayers)
    {
        filter = filterPlayers;
    }
    public cImplicitFilter(cFilterOtherBool filterBool)
    {
        filter = filterBool;
    }
    public cImplicitFilter(cFilterOtherInt filterInt)
    {
        filter = filterInt;
    }

    public String getFilterDescription()
    {
        return filter.toString();
    }

    public boolean evaluate(cRoomData data)
    {
        return filter.evaluate(data);
    }
}
