package com.TheatreTracker.utility;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class DataManager
{

    private DataPointWrapper data[];
    private DataPointPlayerData playerSpecificData[];


    public DataManager()
    {
        data = new DataPointWrapper[DataPoint.values().length];
        for (int i = 0; i < DataPoint.values().length; i++)
        {
            data[i] = new DataPointWrapper(DataPoint.values()[i]);
        }

        playerSpecificData = new DataPointPlayerData[DataPoint.getPlayerSpecific().length];
        for (int i = 0; i < DataPoint.getPlayerSpecific().length; i++)
        {
            playerSpecificData[i] = new DataPointPlayerData(DataPoint.getValue(DataPoint.getPlayerSpecific()[i]));
        }
    }

    public PlayerCorrelatedPointData getHighest(DataPoint point)
    {
        int highest = 0;
        String name = "";
        int index = getPlayerSpecificIndex(point);
        for (String player : playerSpecificData[index].specificPlayerData.keySet())
        {
            if (playerSpecificData[index].specificPlayerData.get(player) > highest)
            {
                highest = playerSpecificData[index].specificPlayerData.get(player);
                name = player;
            }
        }
        return new PlayerCorrelatedPointData(name, highest);
    }

    int getPlayerSpecificIndex(DataPoint point)
    {
        int index = 0;
        for (String datapoint : DataPoint.getPlayerSpecific())
        {
            if (datapoint.equalsIgnoreCase(point.name))
            {
                return index;
            }
            index++;
        }
        return index;
    }

    public void incrementPlayerSpecific(DataPoint dataPoint, String player)
    {
        playerSpecificData[getPlayerSpecificIndex(dataPoint)].increment(player);
    }

    public void incrementPlayerSpecific(DataPoint dataPoint, String player, int valueAdded)
    {
        playerSpecificData[getPlayerSpecificIndex(dataPoint)].increment(player, valueAdded);
    }

    public int getPlayerSpecific(DataPoint point, String player)
    {
        return playerSpecificData[getPlayerSpecificIndex(point)].specificPlayerData.get(player);
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
        if (dataPoint != null)
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
        data[point.ordinal()].setValue((int) (data[point.ordinal()].value * 0.7));
    }

    public void bgs(DataPoint point, int damage)
    {
        data[point.ordinal()].setValue(Math.max(0, data[point.ordinal()].value - damage));
    }
}
