package com.cTimers.filters;

import com.cTimers.utility.cDataPoint;
import lombok.extern.slf4j.Slf4j;
import com.cTimers.cRoomData;

@Slf4j
public class cFilterTime extends cFilterCondition
{
    private cDataPoint split;
    private int compare;
    private int time;

    private String stringValue;

    public cFilterTime(cDataPoint split, int compare1, int time1, String str)
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
    public boolean evaluate(cRoomData data)
    {
        int checkValue = data.getValue(split);
        switch(compare)
        {
            case 0:
                return time == checkValue;
            case 1:
                return checkValue < time;
            case 2:
                return checkValue > time;
            case 3:
                return checkValue <= time;
            case 4:
                return checkValue >= time;
            default:
                return false;
        }
    }
}

