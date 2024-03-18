package com.advancedraidtracker.filters;

import com.advancedraidtracker.utility.datautility.datapoints.Raid;

public class FilterOtherBool extends FilterCondition
{
    boolean value;
    int param;
    String stringValue;

    public FilterOtherBool(int param, boolean value, String val)
    {
        this.param = param;
        this.value = value;
        stringValue = val;
    }

    @Override
    public String toString()
    {
        return stringValue;
    }

    @Override
    public boolean evaluate(Raid data) //TODO
    { /*
        switch (param)
        {
            case 0:
                return data.maidenSkip == value;
            case 1:
                return data.maidenReset == value;
            case 2:
                return data.maidenWipe == value;
            case 3:
                return data.bloatReset == value;
            case 4:
                return data.bloatWipe == value;
            case 5:
                return data.nyloReset == value;
            case 6:
                return data.nyloWipe == value;
            case 7:
                return data.soteReset == value;
            case 8:
                return data.soteWipe == value;
            case 9:
                return data.xarpReset == value;
            case 10:
                return data.xarpWipe == value;
            case 11:
                return data.verzikWipe == value;
            case 12:
                return data.maidenScuffed == value;
        }*/
        return false;
    }

    public String getFilterCSV()
    {
        return "3-" + param + "-" + ((value) ? 1 : 0) + "-" + stringValue;
    }
}
