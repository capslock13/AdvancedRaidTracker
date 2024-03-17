package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public class RoomDataManager
{
    Map<DataPoint, Integer> map;
    Map<DataPoint, Map<String, Integer>> playerSpecificMap;
    Map<DataPoint, Integer> tickMap;
    Multimap<DataPoint, Integer> intList = ArrayListMultimap.create();
    RaidRoom room = null;

    public RoomDataManager()
    {
        map = new HashMap<>();
        playerSpecificMap = new HashMap<>();
        tickMap = new HashMap<>();
    }

    public void init(RaidRoom room)
    {
        this.room = room;
    }

    public void increment(DataPoint point)
    {
        increment(point, "");
    }

    public void increment(DataPoint point, String player)
    {
        incrementBy(point, 1, player);
    }

    public void incrementBy(DataPoint point, int value)
    {
        map.merge(point, value, Integer::sum);
    }

    public void incrementBy(DataPoint point, int value, String player)
    {
        if(player.isEmpty())
        {
            incrementBy(point, value);
            return;
        }
        Map<String, Integer> playerMap = playerSpecificMap.getOrDefault(point, new HashMap<>());
        playerMap.merge(player,value, Integer::sum);
        playerSpecificMap.put(point, playerMap);
    }

    public void set(DataPoint point, int value)
    {
        map.put(point, value);
    }

    public void set(DataPoint point, int value, String player)
    {
        map.put(point, value);
    }

    public int get(DataPoint point)
    {
        if(point.playerSpecific)
        {
            int sum = 0;
            for(String name : playerSpecificMap.get(point).keySet())
            {
                sum += playerSpecificMap.get(point).get(name);
            }
            return sum;
        }
        return map.getOrDefault(point, point.isTime() ? -1 : 0 );
    }

    public int get(DataPoint point, String player)
    {
        return playerSpecificMap.getOrDefault(point, new HashMap<>()).getOrDefault(player, 0);
    }

    public void dwh(DataPoint point)
    {
        double defense = map.getOrDefault(point, 0); //fix defense todo
        defense *= .7;
        map.put(point, (int)defense);
    }

    public void bgs(DataPoint point, int damage)
    {
        int defense = map.getOrDefault(point, 0); //todo fix defense
        defense = Math.max(defense-damage, 0);
        map.put(point, defense);
    }

    public void dumpValues() //used for testing only
    {
        log.info("DataPoint: ");
        for(DataPoint point : map.keySet())
        {
            log.info(point.name + ": " + map.get(point));
        }
        log.info("Player Specific DataPoint: ");
        for(DataPoint point : playerSpecificMap.keySet())
        {
            for(String name : playerSpecificMap.get(point).keySet())
            {
                log.info(name + ", " + point.name + ": " + playerSpecificMap.get(point).get(name));
            }
        }
        log.info("Mapped: ");
        for(DataPoint point : intList.keySet())
        {
            log.info(intList.get(point).toString());
        }
    }

    public void addToList(DataPoint point, Integer value)
    {
        intList.put(point, value);
    }

    public List<Integer> getList(DataPoint point)
    {
        return new ArrayList<>(intList.get(point));
    }

}
