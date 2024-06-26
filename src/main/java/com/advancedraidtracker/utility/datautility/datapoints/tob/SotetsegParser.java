package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.Map;

public class SotetsegParser extends RoomParser
{
    public SotetsegParser(Raid data)
    {
        super(data);
    }

    @Override
    public int getFirstPossibleNonIdleTick()
    {
        return 5;
    }

    @Override
    public Map<Integer, String> getLines()
    {
        lines.put(data.get(DataPoint.SOTE_M1_SPLIT), "Maze1 Start");
        lines.put(data.get(DataPoint.SOTE_P2_SPLIT), "Maze1 End");
        lines.put(data.get(DataPoint.SOTE_M2_SPLIT), "Maze2 Start");
        lines.put(data.get(DataPoint.SOTE_P3_SPLIT), "Maze2 End");
        return lines;
    }
}
