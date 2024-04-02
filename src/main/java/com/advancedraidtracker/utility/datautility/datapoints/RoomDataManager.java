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
    Map<String, Integer> map;
    Map<String, Map<String, Integer>> playerSpecificMap;
    Map<String, Integer> tickMap;
    Multimap<DataPoint, Integer> intList = ArrayListMultimap.create();

    public RoomDataManager()
    {
        map = new HashMap<>();
        playerSpecificMap = new HashMap<>();
        tickMap = new HashMap<>();
    }


    public void increment(DataPoint point)
    {
        increment(point, "");
    }

    public void increment(DataPoint point, String player)
    {
        incrementBy(point, 1, player);
    }

    public void increment(DataPoint point, RaidRoom room)
    {
        incrementBy(point, 1, room);
    }

    public void increment(DataPoint point, String player, RaidRoom room)
    {
        incrementBy(point, 1, player, room);
    }

    public void incrementBy(DataPoint point, int value)
    {
        map.merge(point.name, value, Integer::sum);
    }

    public void incrementBy(DataPoint point, int value, String player)
    {
        if(player.isEmpty())
        {
            incrementBy(point, value);
            return;
        }
        Map<String, Integer> playerMap = playerSpecificMap.getOrDefault(point.name, new HashMap<>());
        playerMap.merge(player,value, Integer::sum);
        playerSpecificMap.put(point.name, playerMap);
    }

    public void incrementBy(DataPoint point, int value, RaidRoom room)
    {
        map.merge("Total " + point.name, value, Integer::sum);
        map.merge(room.name + " " + point.name, value, Integer::sum);
    }

    public void incrementBy(DataPoint point, int value, String player, RaidRoom room)
    {
        if(player.isEmpty())
        {
            if(point.room.equals(RaidRoom.ALL))
            {
                incrementBy(point, value, room);
            }
            else
            {
                incrementBy(point, value);
            }
            return;
        }
        Map<String, Integer> playerMapTotal = playerSpecificMap.getOrDefault("Total " + point.name, new HashMap<>());
        playerMapTotal.merge(player,value, Integer::sum);
        playerSpecificMap.put("Total " + point.name, playerMapTotal);

        Map<String, Integer> playerMapRoom = playerSpecificMap.getOrDefault(room.name + " " + point.name, new HashMap<>());
        playerMapRoom.merge(player,value, Integer::sum);
        playerSpecificMap.put(room.name + " " + point.name, playerMapRoom);
    }

    public void set(DataPoint point, int value)
    {
        map.put(point.name, value);
    }

    public void set(DataPoint point, int value, String player)
    {
        Map<String, Integer> playerMap = playerSpecificMap.getOrDefault(point.name, new HashMap<>());
        playerMap.put(player, value);
        playerSpecificMap.put(point.name, playerMap);
    }

    public void set(DataPoint point, int value, String player, RaidRoom room)
    {
        Map<String, Integer> playerMap = playerSpecificMap.getOrDefault("Total " + point.name, new HashMap<>());
        playerMap.merge(player, value, Integer::sum);
        playerSpecificMap.put("Total " + point.name, playerMap);

        Map<String, Integer> playerRoomMap = playerSpecificMap.getOrDefault(room.name + " " + point.name, new HashMap<>());
        playerRoomMap.merge(player, value, Integer::sum);
        playerSpecificMap.put(room.name + " " + point.name, playerRoomMap);
    }

    public void set(DataPoint point, int value, RaidRoom room)
    {
        map.merge("Total " + point.name, value, Integer::sum);
        map.put(room.name + " " + point.name, value);
    }

    public int get(String point)
    {
        int val = map.getOrDefault(point, 0);
        if(val == -1)
        {
            if(playerSpecificMap.containsKey(point))
            {
                int sum = 0;
                for(String player : playerSpecificMap.get(point).keySet())
                {
                    sum += playerSpecificMap.get(point).getOrDefault(player, 0);
                }
                if(sum != 0)
                {
                    return sum;
                }
            }
        }
        return map.getOrDefault(point, 0);
    }

    public int get(DataPoint point)
    {
        if(point.playerSpecific)
        {
            int sum = 0;
            for(String name : playerSpecificMap.get(point.name).keySet())
            {
                sum += playerSpecificMap.get(point.name).get(name);
            }
            return sum;
        }
        return map.getOrDefault(point.name, 0);
    }

    public int get(DataPoint point, String player)
    {
        return playerSpecificMap.getOrDefault(point.name, new HashMap<>()).getOrDefault(player, 0);
    }

    public int get(DataPoint point, RaidRoom room)
    {
        if(point.playerSpecific)
        {
            int sum = 0;
            for(String name : playerSpecificMap.getOrDefault(room.name + " " + point.name, new HashMap<>()).keySet())
            {
                sum += playerSpecificMap.get(room.name + " " + point.name).get(name);
            }
            return sum;
        }
        return map.getOrDefault(room.name + " " + point.name, 0);
    }

    public void dwh(DataPoint point)
    {
        double defense = map.getOrDefault(point.name, 0); //fix defense todo
        defense *= .7;
        map.put(point.name, (int)defense);
    }

    public void bgs(DataPoint point, int damage)
    {
        int defense = map.getOrDefault(point.name, 0); //todo fix defense
        defense = Math.max(defense-damage, 0);
        map.put(point.name, defense);
    }

    public void dumpValues() //used for testing only
    {
        log.info("DataPoint: ");
        for(String point : map.keySet())
        {
            log.info(point + ": " + map.get(point));
        }
        log.info("Player Specific DataPoint: ");
        for(String point : playerSpecificMap.keySet())
        {
            for(String name : playerSpecificMap.get(point).keySet())
            {
                log.info(name + ", " + point + ": " + playerSpecificMap.get(point).get(name));
            }
        }
        log.info("Mapped: ");
        for(DataPoint point : intList.keySet())
        {
            log.info(point.name + ": " + intList.get(point).toString());
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
