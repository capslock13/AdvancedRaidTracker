package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.Map;

public class XarpusParser extends RoomParser
{
    public XarpusParser(Raid data)
    {
        super(data);
    }

    @Override
    public int getFirstPossibleNonIdleTick()
    {
        switch(data.getScale())
        {
            case 5:
                return 97;
            case 4:
                return 85;
            case 3:
                return 117;
            case 2:
                return 93;
            case 1:
                return 1; //todo hmm
            default:
                return 1;
        }
    }

    @Override
    public Map<Integer, String> getLines()
    {
        lines.put(data.get(DataPoint.XARP_SCREECH), "Screech");
        for (int i = data.get(DataPoint.XARP_SCREECH) + 8; i < data.get(DataPoint.XARPUS_TIME); i += 8)
        {
            lines.put(i, "Turn");
        }
        return lines;
    }
}
