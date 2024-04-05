package com.advancedraidtracker.utility.datautility.datapoints.inf;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.HashMap;
import java.util.Map;

public class InfernoParser extends RoomParser
{
    public InfernoParser(Raid data)
    {
        super(data);
    }

    @Override
    public Map<Integer, String> getLines()
    {
        return new HashMap<>();
    }

}
