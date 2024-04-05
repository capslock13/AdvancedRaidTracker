package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
@Slf4j
public class VerzikParser extends RoomParser
{
    public VerzikParser(Raid data)
    {
        super(data);
    }

    @Override
    public Map<Integer, String> getRoomSpecificMarkers()
    {
        addMarkersFromCollection(data.getList(DataPoint.DAWN_DROPS), "X");
        return markers;
    }

    @Override
    public String getRoomSpecificMarkerName()
    {
        return "Dawn Appears";
    }
    @Override
    public List<Integer> getRoomAutos()
    {
        if(autos.isEmpty())
        {
            for (int i = 19; i < data.get(DataPoint.VERZIK_P2_SPLIT); i++)
            {
                if (i == 19 || (i - 19) % 14 == 0)
                {
                    autos.add(i);
                }
            }
            int lastreset = data.get(DataPoint.VERZIK_P2_SPLIT) + 15; //time from p1 end to first auto p2
            for (int i = lastreset; i < data.get(DataPoint.VERZIK_P3_SPLIT); i++)
            {
                boolean wasNextTick = false;
                for (Integer j : data.getList(DataPoint.VERZIK_REDS_SETS))
                {
                    if (i == j) //current tick is red proc, so we set the next check to be after shield ends
                    {
                        lastreset = i + 11;
                    } else if (i == (j - 5) || i == (j - 1)) //was auto or 2 autos before reds
                    {
                        if (!data.getList(DataPoint.VERZIK_REDS_SETS).get(0).equals(j) || i != (j - 5)) //if first reds, only auto before is skipped not the one before that too
                        {
                            wasNextTick = true;
                        }
                    }
                }
                if ((i - lastreset) % 4 == 0 && i >= lastreset && !wasNextTick) //add auto every 4t if not during shield and not before shield if not first auto
                {
                    autos.add(i);
                }
            }
        }
        return autos;
    }

    @Override
    public Map<Integer, String> getLines()
    {
        //todo dawn appears text
        for(Integer i : data.getList(DataPoint.VERZIK_REDS_SETS))
        {
            lines.put(i, "Reds");
            lines.put(i+10, "Shield End");
        }
        addLinesFromCollection(data.getList(DataPoint.VERZIK_CRABS_SPAWNED), "Crabs");
        for(Integer i : data.getList(DataPoint.WEBS_THROWN))
        {
            if(i%2==0)
            {
                lines.put(i, "Webs");
            }
        }
        return lines;
    }
}
