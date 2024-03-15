package com.advancedraidtracker.utility.datautility.datapoints.toa;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.Map;

public class BabaParser extends RoomParser
{
    @Override
    public Map<Integer, String> getLines()
    {
        lines.put(data.get(DataPoint.BABA_P1_DURATION), "Boulder 1 Start");
        lines.put(data.get(DataPoint.BABA_P2_SPLIT), "Boulder 1 End");
        lines.put(data.get(DataPoint.BABA_BOULDER_2_SPLIT), "Boulder 2 Start");
        lines.put(data.get(DataPoint.BABA_P3_SPLIT), "Boulder 2 End");
        lines.put(data.get(DataPoint.BABA_TIME), "End");
        return lines;
    }
    public void init()
    {
        data.init(RaidRoom.BABA);
    }
}
