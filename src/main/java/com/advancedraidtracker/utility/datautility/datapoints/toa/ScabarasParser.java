package com.advancedraidtracker.utility.datautility.datapoints.toa;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.Map;

public class ScabarasParser extends RoomParser
{
    public ScabarasParser(Raid data)
    {
        super(data);
    }

    @Override
    public Map<Integer, String> getLines()
    {
        return lines;
    }
}
