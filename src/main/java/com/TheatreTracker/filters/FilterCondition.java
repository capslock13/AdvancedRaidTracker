package com.TheatreTracker.filters;

import com.TheatreTracker.SimpleRaidData;

public abstract class FilterCondition
{
    public abstract boolean evaluate(SimpleRaidData data);

    public abstract String getFilterCSV();
}
