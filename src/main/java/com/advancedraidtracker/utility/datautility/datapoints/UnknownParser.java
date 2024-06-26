package com.advancedraidtracker.utility.datautility.datapoints;

import java.util.Map;

public class UnknownParser extends RoomParser
{
    public UnknownParser(Raid data)
    {
        super(data);
    }

    @Override
    public int getFirstPossibleNonIdleTick()
    {
        return 0;
    }

    @Override
    public Map<Integer, String> getLines()
    {
        return lines;
    }
}
