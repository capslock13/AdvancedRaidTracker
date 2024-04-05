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
    public Map<Integer, String> getLines()
    {
        lines.put(data.get(DataPoint.XARP_SCREECH), "Screech");
        for(int i = data.get(DataPoint.XARP_SCREECH) + 8; i < data.get(DataPoint.XARPUS_TIME); i += 8)
        {
            lines.put(i, "Turn");
        }
        return lines;
    }
}
