package com.TheatreTracker.utility;

public class DataPointWrapper
{
    int value = 0;
    DataPoint dataPoint;
    public DataPointWrapper(DataPoint point)
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
