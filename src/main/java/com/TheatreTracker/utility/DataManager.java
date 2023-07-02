package com.TheatreTracker.utility;

public class DataManager
{

    private DataPointWrapper data[];
    public DataManager()
    {
        data = new DataPointWrapper[DataPoint.values().length];
        for(int i = 0; i < DataPoint.values().length; i++)
        {
            data[i] = new DataPointWrapper(DataPoint.values()[i]);
        }
    }

    public void set(DataPoint point, int value)
    {
        data[point.ordinal()].setValue(value);
    }

    public int get(DataPoint point)
    {
        return data[point.ordinal()].value;
    }

    public int get(String point)
    {
        DataPoint dataPoint = DataPoint.getValue(point);
        if(dataPoint != null)
        {
            return data[dataPoint.ordinal()].value;
        }
        return 0;
    }

    public void increment(DataPoint point, int valueAdded)
    {
        data[point.ordinal()].increment(valueAdded);
    }

    public void increment(DataPoint point)
    {
        data[point.ordinal()].increment();
    }

    public void hammer(DataPoint point)
    {
        data[point.ordinal()].setValue((int)(data[point.ordinal()].value * 0.7));
    }

    public void bgs(DataPoint point, int damage)
    {
        data[point.ordinal()].setValue(Math.max(0, data[point.ordinal()].value-damage));
    }
}
