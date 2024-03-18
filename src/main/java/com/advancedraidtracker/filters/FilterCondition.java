package com.advancedraidtracker.filters;


import com.advancedraidtracker.utility.datautility.datapoints.Raid;

public abstract class FilterCondition
{
    public abstract boolean evaluate(Raid data);

    public abstract String getFilterCSV();
}
