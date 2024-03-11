package com.advancedraidtracker.filters;


import com.advancedraidtracker.SimpleTOBData;

public abstract class FilterCondition
{
    public abstract boolean evaluate(SimpleTOBData data);

    public abstract String getFilterCSV();
}
