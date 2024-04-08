package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.Map;

public class NylocasParser extends RoomParser
{
    public NylocasParser(Raid data)
    {
        super(data);
    }

    @Override
    public int getFirstPossibleNonIdleTick()
    {
        return 6;
    }

    @Override
    public Map<Integer, String> getLines()
    {
        addLinesFromCollection(data.getList(DataPoint.NYLO_STALLS_TOTAL), "Stall");
        lines.put(data.get(DataPoint.NYLO_LAST_WAVE), "Last Wave");
        lines.put(data.get(DataPoint.NYLO_BOSS_SPAWN) + 2, "Boss Spawn");
        for (int i = data.get(DataPoint.NYLO_BOSS_SPAWN) + 11; i < data.get(DataPoint.NYLOCAS_TIME); i += 10)
        {
            lines.put(i, "Phase");
        }
        int wave = 1;
        for (Integer i : data.getList(DataPoint.NYLO_WAVES))
        {
            lines.put(i, "W" + wave);
            wave++;
        }
        return lines;
    }
}
