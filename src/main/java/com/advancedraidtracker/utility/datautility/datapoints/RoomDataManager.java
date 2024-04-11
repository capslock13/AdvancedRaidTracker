package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
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
    Map<Integer, Map<Integer, Integer>> playerSpecificMap;
    Map<String, Integer> tickMap;
    Multimap<DataPoint, Integer> intList = ArrayListMultimap.create();

    //Making the change to map player names and datapoints as ints reduced retained memory by ~20%. Shorts were the same due to padding.
    //2068 raids went from 33.8MB to 27.5MB retained size in heap dump. Not sure if worth but leaving for now

    public static BiMap<String, Integer> playerIntegerMap = HashBiMap.create();
    public static Integer highestPlayerInteger = Integer.MIN_VALUE;
    public static BiMap<String, Integer> dataPointIntegerMap = HashBiMap.create();
    public static Integer highestDataPointInteger = Integer.MIN_VALUE;
    public static String getIntegerAsDataPointString(Integer val)
    {
        return dataPointIntegerMap.inverse().getOrDefault(val, "Unknown Data Point");
    }
    public static Integer getDataPointStringAsInteger(String point)
    {
        if(dataPointIntegerMap.containsKey(point))
        {
            return dataPointIntegerMap.get(point);
        }
        else
        {
            dataPointIntegerMap.put(point, highestDataPointInteger);
            return highestDataPointInteger++;
        }
    }
    public static String getIntegerAsPlayer(Integer val)
    {
        return playerIntegerMap.inverse().getOrDefault(val, "Unknown Player");
    }
    public static Integer getPlayerAsInteger(String player)
    {
        if(playerIntegerMap.containsKey(player))
        {
            return playerIntegerMap.get(player);
        }
        else
        {
            playerIntegerMap.put(player, highestPlayerInteger);
            return highestPlayerInteger++;
        }
    }

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
        if (player.isEmpty())
        {
            incrementBy(point, value);
            return;
        }
        Map<Integer, Integer> playerMap = playerSpecificMap.getOrDefault(getDataPointStringAsInteger(point.name), new HashMap<>());
        playerMap.merge(getPlayerAsInteger(player), value, Integer::sum);
        playerSpecificMap.put(getDataPointStringAsInteger(point.name), playerMap);
    }

    public void incrementBy(DataPoint point, int value, RaidRoom room)
    {
        map.merge("Total " + point.name, value, Integer::sum);
        map.merge(room.name + " " + point.name, value, Integer::sum);
    }

    public void incrementBy(DataPoint point, int value, String player, RaidRoom room)
    {
        if (player.isEmpty())
        {
            if (point.room.equals(RaidRoom.ALL))
            {
                incrementBy(point, value, room);
            } else
            {
                incrementBy(point, value);
            }
            return;
        }
        applyToMap(point, value, player, room);
    }

    private void applyToMap(DataPoint point, int value, String player, RaidRoom room)
    {
        Map<Integer, Integer> playerMapTotal = playerSpecificMap.getOrDefault(getDataPointStringAsInteger("Total " + point.name), new HashMap<>());
        playerMapTotal.merge(getPlayerAsInteger(player), value, Integer::sum);
        playerSpecificMap.put(getDataPointStringAsInteger("Total " + point.name), playerMapTotal);

        Map<Integer, Integer> playerMapRoom = playerSpecificMap.getOrDefault(getDataPointStringAsInteger(room.name + " " + point.name), new HashMap<>());
        playerMapRoom.merge(getPlayerAsInteger(player), value, Integer::sum);
        playerSpecificMap.put(getDataPointStringAsInteger(room.name + " " + point.name), playerMapRoom);
    }

    public void set(DataPoint point, int value)
    {
        map.put(point.name, value);
    }

    public void set(String point, int value)
    {
        map.put(point, value);
    }

    public void set(DataPoint point, int value, String player)
    {
        Map<Integer, Integer> playerMap = playerSpecificMap.getOrDefault(getDataPointStringAsInteger(point.name), new HashMap<>());
        playerMap.put(getPlayerAsInteger(player), value);
        playerSpecificMap.put(getDataPointStringAsInteger(point.name), playerMap);
    }

    public void set(DataPoint point, int value, String player, RaidRoom room)
    {
        applyToMap(point, value, player, room);
    }

    public void set(DataPoint point, int value, RaidRoom room)
    {
        map.merge("Total " + point.name, value, Integer::sum);
        map.put(room.name + " " + point.name, value);
    }

    public int get(String point)
    {
        int val = map.getOrDefault(point, 0);
        if (val == 0)
        {
            if (playerSpecificMap.containsKey(getDataPointStringAsInteger(point)))
            {
                int sum = 0;
                for (Integer player : playerSpecificMap.get(getDataPointStringAsInteger(point)).keySet())
                {
                    sum += playerSpecificMap.get(getDataPointStringAsInteger(point)).getOrDefault(player, 0);
                }
                if (sum != 0)
                {
                    return sum;
                }
            }
        }
        return map.getOrDefault(point, 0);
    }

    public int get(DataPoint point)
    {
        if (point.playerSpecific)
        {
            int sum = 0;
            for (Integer name : playerSpecificMap.get(getDataPointStringAsInteger(point.name)).keySet())
            {
                sum += playerSpecificMap.get(getDataPointStringAsInteger(point.name)).get(name);
            }
            return sum;
        }
        return map.getOrDefault(point.name, 0);
    }

    public int get(DataPoint point, String player)
    {
        if (point.room.equals(RaidRoom.ALL))
        {
            int sum = 0;
            for (RaidRoom room : RaidRoom.values())
            {
                sum += playerSpecificMap.getOrDefault(getDataPointStringAsInteger(room.name + " " + point.name), new HashMap<>()).getOrDefault(getPlayerAsInteger(player), 0);
            }
            if (sum > 0)
            {
                return sum;
            }
        }
        return playerSpecificMap.getOrDefault(getDataPointStringAsInteger(point.name), new HashMap<>()).getOrDefault(getPlayerAsInteger(player), 0);
    }

    public int get(DataPoint point, RaidRoom room)
    {
        if (point.playerSpecific)
        {
            int sum = 0;
            for (Integer name : playerSpecificMap.getOrDefault(getDataPointStringAsInteger(room.name + " " + point.name), new HashMap<>()).keySet())
            {
                sum += playerSpecificMap.get(getDataPointStringAsInteger(room.name + " " + point.name)).get(name);
            }
            return sum;
        }
        return map.getOrDefault(room.name + " " + point.name, 0);
    }

    public void dwh(DataPoint point)
    {
        double defense = map.getOrDefault(point.name, 0); //fix defense todo
        defense *= .7;
        map.put(point.name, (int) defense);
    }

    public void bgs(DataPoint point, int damage)
    {
        int defense = map.getOrDefault(point.name, 0); //todo fix defense
        defense = Math.max(defense - damage, 0);
        map.put(point.name, defense);
    }

    public void dumpValues() //used for testing only
    {
        log.info("DataPoint: ");
        for (String point : map.keySet())
        {
            log.info(point + ": " + map.get(point));
        }
        log.info("Player Specific DataPoint: ");
        for (Integer point : playerSpecificMap.keySet())
        {
            for (Integer name : playerSpecificMap.get(point).keySet())
            {
                log.info(getIntegerAsPlayer(name) + ", " + getIntegerAsDataPointString(point) + ": " + playerSpecificMap.get(point).get(name));
            }
        }
        log.info("Mapped: ");
        for (DataPoint point : intList.keySet())
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
