package com.advancedraidtracker.utility.datautility;

import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.wrappers.PlayerCorrelatedPointData;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class DataManager
{

    private final DataPointIntWrapper[] data;
    private final DataPointPlayerData[] playerSpecificData;
    public final RaidType raidType;


    public DataManager(RaidType raidType)
    {
        this.raidType = raidType;
        ArrayList<DataPoint> dataPoints = (raidType.equals(RaidType.TOB)) ? DataPoint.getTOBValues() : DataPoint.getTOAValues();
        data = new DataPointIntWrapper[dataPoints.size()];
        for (int i = 0; i < dataPoints.size(); i++)
        {
            data[i] = new DataPointIntWrapper(dataPoints.get(i));
        }

        String[] playerSpecificRaidSpecific = DataPoint.getPlayerSpecific(raidType);
        playerSpecificData = new DataPointPlayerData[playerSpecificRaidSpecific.length];
        for (int i = 0; i < playerSpecificRaidSpecific.length; i++)
        {
            playerSpecificData[i] = new DataPointPlayerData(DataPoint.getValue(playerSpecificRaidSpecific[i]));
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
        for (String datapoint : DataPoint.getPlayerSpecific(raidType))
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

    public void set(DataPoint point, int value) //todo revisit these, map(?)
    {
        for (DataPointIntWrapper pointWrapper : data)
        {
            if (pointWrapper.dataPoint.equals(point))
            {
                pointWrapper.setValue(value);
            }
        }
    }

    public int get(DataPoint point)
    {
        if (point == null)
        {
            return -1;
        }
        for (DataPointIntWrapper pointWrapper : data)
        {
            if (pointWrapper.dataPoint.equals(point))
            {
                return pointWrapper.value;
            }
        }
        return -1;
    }

    public int get(String point)
    {
        return get(DataPoint.getValue(point));
    }

    public void increment(DataPoint point, int valueAdded) //todo also revisit etc etc
    {
        for (DataPointIntWrapper pointWrapper : data)
        {
            if (pointWrapper.dataPoint.equals(point))
            {
                pointWrapper.increment(valueAdded);
            }
        }
    }

    public void increment(DataPoint point)
    {
        for (DataPointIntWrapper pointWrapper : data)
        {
            if (pointWrapper.dataPoint.equals(point))
            {
                pointWrapper.increment();
            }
        }
    }

    public void decrement(DataPoint point)
    {
        for (DataPointIntWrapper pointWrapper : data)
        {
            if (pointWrapper.dataPoint.equals(point))
            {
                pointWrapper.decrement();
            }
        }
    }

    public void hammer(DataPoint point)
    {
        for (DataPointIntWrapper pointWrapper : data)
        {
            if (pointWrapper.dataPoint.equals(point))
            {
                pointWrapper.setValue((int) (data[point.ordinal()].value * 0.7));
            }
        }
    }

    public void bgs(DataPoint point, int damage)
    {
        for (DataPointIntWrapper pointWrapper : data)
        {
            if (pointWrapper.dataPoint.equals(point))
            {
                pointWrapper.setValue(Math.max(0, data[point.ordinal()].value - damage));
            }
        }
    }
}