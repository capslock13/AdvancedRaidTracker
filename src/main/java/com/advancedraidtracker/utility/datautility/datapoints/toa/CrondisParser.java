package com.advancedraidtracker.utility.datautility.datapoints.toa;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.Map;

public class CrondisParser extends RoomParser
{
    public CrondisParser(Raid data)
    {
        super(data);
    }

    @Override
    public int getFirstPossibleNonIdleTick()
    {
        return 8;
    }

    @Override
    public Map<Integer, String> getLines()
    {
        lines.put(8, "Pickup water");
        if (data.get(DataPoint.PARTY_SIZE) == 1)
        {
            lines.put(19, "First Fill");
            lines.put(30, "First Watering");
            lines.put(43, "Second Fill");
            lines.put(54, "Second Watering");
        } else
        {
            lines.put(20, "First Fill");
            lines.put(31, "First Watering");
            lines.put(45, "Second Fill");
            lines.put(56, "Second Watering");
        }
        return lines;
    }
}
