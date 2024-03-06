package com.advancedraidtracker.filters;

import com.advancedraidtracker.SimpleRaidData;

public abstract class FilterCondition
{
    public abstract boolean evaluate(SimpleRaidData data);

    public abstract String getFilterCSV();
}
