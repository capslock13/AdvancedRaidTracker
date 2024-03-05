package com.TheatreTracker.filters;

import com.TheatreTracker.SimpleTOBData;

public abstract class FilterCondition
{
    public abstract boolean evaluate(SimpleTOBData data);

    public abstract String getFilterCSV();
}
