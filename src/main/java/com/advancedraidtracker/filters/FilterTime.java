package com.advancedraidtracker.filters;

import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import lombok.extern.slf4j.Slf4j;
import com.advancedraidtracker.SimpleTOBData;

@Slf4j
public class FilterTime extends FilterCondition
{
    private final DataPoint split;
    private final int compare;
    private final int time;
    private final String stringValue;

    public FilterTime(DataPoint split, int compare1, int time1, String str)
    {
        this.split = split;
        this.compare = compare1;
        this.time = time1;
        stringValue = str;
    }

    @Override
    public String toString()
    {
        return stringValue;
    }

    @Override
    public boolean evaluate(Raid data)
    {
        int checkValue = data.get(split);
        return FilterUtil.compare(compare, time, checkValue);
    }

    public String getFilterCSV()
    {
        return "0-" + split.name + "-" + compare + "-" + time + "-" + stringValue;
    }
}

