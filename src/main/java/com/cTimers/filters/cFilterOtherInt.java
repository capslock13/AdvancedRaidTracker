package com.cTimers.filters;

import com.cTimers.cRoomData;
import com.cTimers.utility.cDataPoint;

public class cFilterOtherInt extends cFilterCondition
{
    cDataPoint param;
    int qualifier;
    int value;
    public String stringValue;
    /*public cFilterOtherInt(int param, int qualifier, int value)
    {
        this.param = param;
        this.qualifier = qualifier;
        this.value = value;
    }

    /*public cFilterOtherInt(int param, int qualifier, int value, String val)
    {
        this.param = param;
        this.qualifier = qualifier;
        this.value = value;
        stringValue = val;
    }*/

    public cFilterOtherInt(cDataPoint param, int qualifier, int value, String val)
    {
        this.param = param;
        this.qualifier = qualifier;
        this.value = value;
        stringValue = val;
    }

    @Override
    public boolean evaluate(cRoomData data)
    {
        int checkValue = data.getValue(param);
        if(data.getTimeAccurate(param) && value != -1)
        {
                switch(qualifier)
                {
                    case 0:
                        return checkValue == value;
                    case 1:
                        return checkValue < value;
                    case 2:
                        return checkValue > value;
                    case 3:
                        return checkValue <= value;
                    case 4:
                        return checkValue >= value;
                    default:
                        return false;
                }
        }
        else
        {
            return false;
        }
    }
}
