package com.cTimers.utility;

public class cDataPointWrapper
{
    int value = 0;
    cDataPoint dataPoint;
    public cDataPointWrapper(cDataPoint point)
    {
        dataPoint = point;
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
