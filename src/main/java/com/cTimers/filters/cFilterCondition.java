package com.cTimers.filters;

import com.cTimers.cRoomData;

public abstract class cFilterCondition
{
    public abstract boolean evaluate(cRoomData data);

    public abstract String getFilterCSV();
}
