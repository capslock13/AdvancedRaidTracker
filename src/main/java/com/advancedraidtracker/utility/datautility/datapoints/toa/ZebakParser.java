package com.advancedraidtracker.utility.datautility.datapoints.toa;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.Map;

public class ZebakParser extends RoomParser
{
    @Override
    public Map<Integer, String> getLines()
    {
        return null;
    }
    public void init()
    {
        data.init(RaidRoom.ZEBAK);
    }
}
