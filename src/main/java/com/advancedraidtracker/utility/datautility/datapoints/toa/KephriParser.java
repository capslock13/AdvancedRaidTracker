package com.advancedraidtracker.utility.datautility.datapoints.toa;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.List;
import java.util.Map;

public class KephriParser extends RoomParser
{
    @Override
    public Map<Integer, String> getLines()
    {
       /* addLinesFromCollection(data.getList(DataPoint.KEPHRI_DUNG_THROWN), "Dung");
        lines.put(data.get(DataPoint.KEPHRI_P1_DURATION), "Swarm1 Start");
        lines.put(data.get(DataPoint.KEPHRI_P2_SPLIT), "Swarm1 End");
        lines.put(data.get(DataPoint.KEPHRI_SWARM2_SPLIT), "Swarm2 Start");
        lines.put(data.get(DataPoint.KEPHRI_P3_SPLIT), "Swarm2 End");
        lines.put(data.get(DataPoint.KEPHRI_P3_SPLIT), "Final Phase");
        lines.put(data.get(DataPoint.KEPHRI_TIME), "End");*/
        return lines;
    }
}
