package com.advancedraidtracker.utility.datautility.datapoints.toa;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.List;
import java.util.Map;

public class ZebakParser extends RoomParser
{
    @Override
    public Map<Integer, String> getLines()
    {
        addLinesFromCollection(data.getList(DataPoint.ZEBAK_BOULDER_ATTACKS), "Jug");
        addLinesFromCollection(data.getList(DataPoint.ZEBAK_WATERFALL_ATTACKS), "Tsunami");
        lines.put(data.get(DataPoint.ZEBAK_ENRAGED_SPLIT), "Enraged");
        return lines;
    }
    public void init()
    {
        data.init(RaidRoom.ZEBAK);
    }
}
