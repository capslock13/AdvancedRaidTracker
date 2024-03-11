package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.TOBRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.MultiRoomDataPoint;
import com.advancedraidtracker.utility.datautility.MultiRoomPlayerDataPoint;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class RaidDataManager
{
    Map<DataPoint, Integer> map;
    Map<MultiRoomDataPoint, Map<TOBRoom, Integer>> roomMap;
    Map<MultiRoomPlayerDataPoint, Map<TOBRoom, Map<String, Integer>>> roomPlayerMap;
    public RaidDataManager()
    {
        map = new HashMap<>();
        roomMap = new HashMap<>();
        roomPlayerMap = new HashMap<>();
    }

    public void increment(Object point, String player, TOBRoom room)
    {
        incrementBy(point, 1, player, room);
    }

    public void incrementBy(Object point, int value, String player, TOBRoom room)
    {
        if(point instanceof DataPoint)
        {
            map.merge((DataPoint)point, value, Integer::sum);
        }
        else if(point instanceof MultiRoomPlayerDataPoint)
        {
            roomPlayerMap.getOrDefault((MultiRoomPlayerDataPoint) point, new HashMap<>()).getOrDefault(room, new HashMap<>()).merge(player, value, Integer::sum);
        }
    }

    public void set(DataPoint point, int value)
    {
        map.put(point, value);
    }

    public void set(MultiRoomDataPoint point, int value, TOBRoom room)
    {
        roomMap.getOrDefault(point, new HashMap<>()).merge(room, value, Integer::sum);
    }

    public int get(DataPoint point)
    {
        if(!map.containsKey(point))
        {
            return -1;
        }
        return map.get(point);
    }

    public int get(String point)
    {
        return get(DataPoint.getValue(point)); //TODO make more efficient
    }

    private static int getDefenseFromRoom(TOBRoom room)
    {
        switch (room)
        {
            case MAIDEN:
                return 200;
            case BLOAT:
                return 100;
            case NYLOCAS:
                return 50;
            case SOTETSEG:
                return 200;
            case XARPUS:
                return 250;
            case VERZIK:
                return 200;
            default:
                return 0;
        }
    }

    public void dwh(MultiRoomDataPoint point, TOBRoom room)
    {
        double defense = roomMap.getOrDefault(point, new HashMap<>()).getOrDefault(room, getDefenseFromRoom(room));
        defense *= .7;
        roomMap.getOrDefault(point, new HashMap<>()).put(room, (int)defense);
    }

    public void bgs(MultiRoomDataPoint point, int damage, TOBRoom room)
    {
        int defense = roomMap.getOrDefault(point, new HashMap<>()).getOrDefault(room, getDefenseFromRoom(room));
        defense = Math.max(defense-damage, 0);
        roomMap.getOrDefault(point, new HashMap<>()).put(room, defense);
    }

    public void dumpValues()
    {
        for(DataPoint point : map.keySet())
        {
            log.info(point.name + ": " + map.get(point));
        }
    }

    public int get(MultiRoomDataPoint arg, TOBRoom room)
    {
        return roomMap.getOrDefault(arg, new HashMap<>()).getOrDefault(room, 0);
    }
}
