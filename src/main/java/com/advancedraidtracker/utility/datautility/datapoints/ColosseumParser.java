package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.RaidRoom;

import java.util.HashMap;
import java.util.Map;

public class ColosseumParser extends RoomParser
{
    @Override
    public Map<Integer, String> getLines()
    {
        return new HashMap<>();
    }
    public void init()
    {
        data.init(RaidRoom.COLOSSEUM);
    }
}
