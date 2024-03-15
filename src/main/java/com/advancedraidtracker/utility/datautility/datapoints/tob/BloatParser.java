package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.Map;

public class BloatParser extends RoomParser
{
    @Override
    public Map<Integer, String> getLines()
    {
        for(Integer i : data.getList(DataPoint.BLOAT_DOWNS))
        {
            lines.put(i, "Down");
            lines.put(i+33, "Moving");
            lines.put(i+30, "Stomp"); //todo verify
        }
        return lines;
    }

    public void init()
    {
        data.init(RaidRoom.BLOAT);
    }
}
