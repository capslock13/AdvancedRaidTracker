package com.TheatreTracker.utility;

public class DataPointWrapper
{
    int value = 0;
    DataPoint dataPoint;

    public DataPointWrapper(DataPoint point)
    {
        dataPoint = point;
        if (point.equals(DataPoint.MAIDEN_DEFENSE))
        {
            value = 200;
        } else if (point.equals(DataPoint.BLOAT_DEFENSE))
        {
            value = 100;
        } else if (point.equals(DataPoint.NYLO_DEFENSE))
        {
            value = 50;
        } else if (point.equals(DataPoint.XARP_DEFENSE))
        {
            value = 250;
        }
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return this.value;
    }

    public void increment(int valueAdded)
    {
        value += valueAdded;
    }

    public void increment()
    {
        value++;
    }
}
