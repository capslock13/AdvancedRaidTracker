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
    Map<Short, Map<Short, Integer>> playerSpecificMap;
    Map<String, Integer> tickMap;
    Multimap<DataPoint, Integer> intList = ArrayListMultimap.create();

    //Making the change to map player names and datapoints as shorts reduced retained memory by ~20%
    //2068 raids went from 33.8MB to 27.5MB in memory. Not sure if worth but leaving for now
    //Really hope no one raids with more than 65k different people....

    public static BiMap<String, Short> playerShortMap = HashBiMap.create();
    public static short highestPlayerShort = Short.MIN_VALUE;
    public static BiMap<String, Short> dataPointShortMap = HashBiMap.create();
    public static short highestDataPointShort = Short.MIN_VALUE;
    public static String getShortAsDataPointString(Short val)
    {
        return dataPointShortMap.inverse().getOrDefault(val, "Unknown Data Point");
    }
    public static Short getDataPointStringAsShort(String point)
    {
        if(dataPointShortMap.containsKey(point))
        {
            return dataPointShortMap.get(point);
        }
        else
        {
            dataPointShortMap.put(point, highestDataPointShort);
            return highestDataPointShort++;
        }
    }
    public static String getShortAsPlayer(short val)
    {
        return playerShortMap.inverse().getOrDefault(val, "Unknown Player");
    }
    public static Short getPlayerAsShort(String player)
    {
        if(playerShortMap.containsKey(player))
        {
            return playerShortMap.get(player);
        }
        else
        {
            playerShortMap.put(player, highestPlayerShort);
            return highestPlayerShort++;
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
        Map<Short, Integer> playerMap = playerSpecificMap.getOrDefault(getDataPointStringAsShort(point.name), new HashMap<>());
        playerMap.merge(getPlayerAsShort(player), value, Integer::sum);
        playerSpecificMap.put(getDataPointStringAsShort(point.name), playerMap);
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
        Map<Short, Integer> playerMapTotal = playerSpecificMap.getOrDefault(getDataPointStringAsShort("Total " + point.name), new HashMap<>());
        playerMapTotal.merge(getPlayerAsShort(player), value, Integer::sum);
        playerSpecificMap.put(getDataPointStringAsShort("Total " + point.name), playerMapTotal);

        Map<Short, Integer> playerMapRoom = playerSpecificMap.getOrDefault(getDataPointStringAsShort(room.name + " " + point.name), new HashMap<>());
        playerMapRoom.merge(getPlayerAsShort(player), value, Integer::sum);
        playerSpecificMap.put(getDataPointStringAsShort(room.name + " " + point.name), playerMapRoom);
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
        Map<Short, Integer> playerMap = playerSpecificMap.getOrDefault(getDataPointStringAsShort(point.name), new HashMap<>());
        playerMap.put(getPlayerAsShort(player), value);
        playerSpecificMap.put(getDataPointStringAsShort(point.name), playerMap);
    }

    public void set(DataPoint point, int value, String player, RaidRoom room)
    {
        Map<Short, Integer> playerMap = playerSpecificMap.getOrDefault(getDataPointStringAsShort("Total " + point.name), new HashMap<>());
        playerMap.merge(getPlayerAsShort(player), value, Integer::sum);
        playerSpecificMap.put(getDataPointStringAsShort("Total " + point.name), playerMap);

        Map<Short, Integer> playerRoomMap = playerSpecificMap.getOrDefault(getDataPointStringAsShort(room.name + " " + point.name), new HashMap<>());
        playerRoomMap.merge(getPlayerAsShort(player), value, Integer::sum);
        playerSpecificMap.put(getDataPointStringAsShort(room.name + " " + point.name), playerRoomMap);
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
            if (playerSpecificMap.containsKey(getDataPointStringAsShort(point)))
            {
                int sum = 0;
                for (Short player : playerSpecificMap.get(getDataPointStringAsShort(point)).keySet())
                {
                    sum += playerSpecificMap.get(getDataPointStringAsShort(point)).getOrDefault(player, 0);
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
            for (Short name : playerSpecificMap.get(getDataPointStringAsShort(point.name)).keySet())
            {
                sum += playerSpecificMap.get(getDataPointStringAsShort(point.name)).get(name);
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
                sum += playerSpecificMap.getOrDefault(getDataPointStringAsShort(room.name + " " + point.name), new HashMap<>()).getOrDefault(getPlayerAsShort(player), 0);
            }
            if (sum > 0)
            {
                return sum;
            }
        }
        return playerSpecificMap.getOrDefault(getDataPointStringAsShort(point.name), new HashMap<>()).getOrDefault(getPlayerAsShort(player), 0);
    }

    public int get(DataPoint point, RaidRoom room)
    {
        if (point.playerSpecific)
        {
            int sum = 0;
            for (Short name : playerSpecificMap.getOrDefault(getDataPointStringAsShort(room.name + " " + point.name), new HashMap<>()).keySet())
            {
                sum += playerSpecificMap.get(getDataPointStringAsShort(room.name + " " + point.name)).get(name);
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
        for (Short point : playerSpecificMap.keySet())
        {
            for (Short name : playerSpecificMap.get(point).keySet())
            {
                log.info(getShortAsPlayer(name) + ", " + getShortAsDataPointString(point) + ": " + playerSpecificMap.get(point).get(name));
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
