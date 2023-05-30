package com.cTimers.utility;

public class cDataManager
{

    private cDataPointWrapper data[];
    public cDataManager()
    {
        data = new cDataPointWrapper[cDataPoint.values().length];
        for(int i = 0; i < cDataPoint.values().length; i++)
        {
            data[i] = new cDataPointWrapper(cDataPoint.values()[i]);
        }
    }

    public void set(cDataPoint point, int value)
    {
        data[point.ordinal()].setValue(value);
    }

    public int get(cDataPoint point)
    {
        return data[point.ordinal()].value;
    }

    public int get(String point)
    {
        return data[cDataPoint.getValue(point).ordinal()].value;
    }

    public void increment(cDataPoint point, int valueAdded)
    {
        data[point.ordinal()].increment(valueAdded);
    }

    public void increment(cDataPoint point)
    {
        data[point.ordinal()].increment();
    }

    public void hammer(cDataPoint point)
    {
        data[point.ordinal()].setValue((int)(data[point.ordinal()].value * 0.7));
    }

    public void bgs(cDataPoint point, int damage)
    {
        data[point.ordinal()].setValue(Math.max(0, data[point.ordinal()].value-damage));
    }
}
