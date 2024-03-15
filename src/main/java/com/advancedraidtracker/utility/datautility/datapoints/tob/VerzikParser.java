package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;

import java.util.Map;

public class VerzikParser extends RoomParser
{
    @Override
    public Map<Integer, String> getLines()
    {
        addLinesFromCollection(data.getList(DataPoint.DAWN_DROPS), "Dawn Appears");
        //todo dawn dmg?
        for(int i = 19; i < data.get(DataPoint.VERZIK_P2_SPLIT); i++)
        {
            if(i == 19 || (i-19) % 14 == 0)
            {
                //todo autos
            }
        }
        //todo dawn appears text
        for(Integer i : data.getList(DataPoint.VERZIK_REDS_SETS))
        {
            lines.put(i, "Reds");
            lines.put(i+10, "Shield End");
        }
        addLinesFromCollection(data.getList(DataPoint.VERZIK_CRABS_SPAWNED), "Crabs");
        int lastReset = data.get(DataPoint.VERZIK_P2_SPLIT) + 11;
        for(int i = lastReset; i < data.get(DataPoint.VERZIK_P3_SPLIT); i++)
        {
            boolean wasNextTick = false;
            //todo autos?
        }
        for(Integer i : data.getList(DataPoint.WEBS_THROWN))
        {
            if(i%2==0)
            {
                lines.put(i, "Webs");
            }
        }
        return lines;
    }
    public void init()
    {
        data.init(RaidRoom.VERZIK);
    }
}
