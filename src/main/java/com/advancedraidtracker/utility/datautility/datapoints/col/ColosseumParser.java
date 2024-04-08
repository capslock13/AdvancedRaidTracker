package com.advancedraidtracker.utility.datautility.datapoints.col;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.HashMap;
import java.util.Map;

public class ColosseumParser extends RoomParser
{
    public ColosseumParser(Raid data)
    {
        super(data);
    }

    @Override
    public int getFirstPossibleNonIdleTick()
    {
        return 2;
    }

    @Override
    public Map<Integer, String> getLines()
    {
        return new HashMap<>();
    }
}
