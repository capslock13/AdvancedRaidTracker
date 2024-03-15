package com.advancedraidtracker.utility.datautility.datapoints.toa;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.List;
import java.util.Map;

public class HetParser extends RoomParser
{
    @Override
    public Map<Integer, String> getLines()
    {
        addLinesFromCollection(data.getList(DataPoint.HET_DOWNS), "Obelisk Weakened");
        return lines;
    }
    public void init()
    {
        data.init(RaidRoom.HET);
    }
}
