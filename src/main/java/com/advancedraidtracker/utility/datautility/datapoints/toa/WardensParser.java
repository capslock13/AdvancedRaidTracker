package com.advancedraidtracker.utility.datautility.datapoints.toa;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.List;
import java.util.Map;

public class WardensParser extends RoomParser
{
    @Override
    public Map<Integer, String> getLines()
    {
        addLinesFromCollection(data.getList(DataPoint.WARDENS_P2_CORE_SPAWNS), "Core Spawned");
        addLinesFromCollection(data.getList(DataPoint.WARDENS_P2_CORE_DESPAWNS), "Core Despawned");
        lines.put(data.get(DataPoint.WARDENS_ENRAGED_SPLIT), "Enraged");
        int p3Start = data.get(DataPoint.WARDENS_P3_SPLIT);
        for(int i = 1; i < 5; i++)
        {
            int skullStart = data.get(DataPoint.getValue("Wardens Skull " + i + " Split")); //todo does not work
            int skullDuration = data.get(DataPoint.getValue("Wardens Skull " + i + " Duration")); //todo does not work
            lines.put(skullStart, "Skull Start"); //todo does not work
            lines.put(skullStart+skullDuration, "Skull End"); //todo does not work
        }
        return lines;
    }
    public void init()
    {
        data.init(RaidRoom.WARDENS);
    }
}
