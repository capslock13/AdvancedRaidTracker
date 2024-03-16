package com.advancedraidtracker.filters;

import com.advancedraidtracker.SimpleTOBData;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;

public class FilterOtherInt extends FilterCondition
{
    DataPoint param;
    int qualifier;
    int value;
    public String stringValue;

    public FilterOtherInt(DataPoint param, int qualifier, int value, String val)
    {
        this.param = param;
        this.qualifier = qualifier;
        this.value = value;
        stringValue = val;
    }

    @Override
    public String toString()
    {
        return stringValue;
    }

    @Override
    public boolean evaluate(Raid data)
    {
        int checkValue = data.get(param);
        if(data.getRoomAccurate(param.room) && value != -1)
        {
            return FilterUtil.compare(qualifier, value, checkValue);
        }
        else
        {
            return false;
        }
    }

    public String getFilterCSV()
    {
        return "1-" + param.name + "-" + qualifier + "-" + value + "-" + stringValue;
    }
}
