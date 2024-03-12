package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.wrappers.PlayerDidAttack;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public abstract class RoomParser
{
    public abstract Map<Integer, String> getLines();


    public RoomDataManager data;
    private RaidRoom room;

    public RoomParser()
    {
        room = RaidRoom.ANY;
        data = new RoomDataManager();
    }

    public int getRoomTime()
    {
        return 1; //hmm
    }
    public int getStartTick()
    {
        return 1;
    }
    public int getEndTick()
    {
        return getRoomTime();
    }
}
