package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.wrappers.PlayerDidAttack;
import lombok.Getter;

import java.util.*;

public abstract class RoomParser
{
    public abstract Map<Integer, String> getLines();

    public Map<Integer, String> getRoomSpecificMarkers()
    {
        return markers;
    }
    public String getRoomSpecificMarkerName()
    {
        return "";
    }

    public List<Integer> getRoomAutos()
    {
        return autos;
    }

    protected List<Integer> autos = new ArrayList<>();

    protected Map<Integer, String> lines = new LinkedHashMap<>(); //todo hashmap?
    protected Map<Integer, String> markers = new LinkedHashMap<>();
    protected RaidRoom room;
    protected Raid data;

    public RoomParser(Raid data)
    {
        this.data = data;
        room = RaidRoom.ANY;
    }

    protected void addLinesFromCollection(List<Integer> list, String description)
    {
        for(Integer i : list)
        {
            lines.put(i, description);
        }
    }

    protected void addMarkersFromCollection(List<Integer> list, String description)
    {
        for(Integer i : list)
        {
            markers.put(i, description);
        }
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
