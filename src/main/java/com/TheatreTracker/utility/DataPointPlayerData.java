package com.TheatreTracker.utility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataPointPlayerData
{
    public Map<String, Integer> specificPlayerData;
    public DataPoint datapoint;

    public DataPointPlayerData(DataPoint datapoint)
    {
        this.datapoint = datapoint;
        specificPlayerData = new LinkedHashMap<>();
    }

    public void increment(String player)
    {
        if(specificPlayerData.containsKey(player))
        {
            specificPlayerData.put(player, specificPlayerData.get(player)+1);
        }
        else
        {
            specificPlayerData.put(player, 1);
        }
    }

    public void increment(String player, int valueAdded)
    {
        if(specificPlayerData.containsKey(player))
        {
            specificPlayerData.put(player, specificPlayerData.get(player)+valueAdded);
        }
        else
        {
            specificPlayerData.put(player, valueAdded);
        }
    }
}
