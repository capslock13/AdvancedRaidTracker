package com.TheatreTracker;

import com.TheatreTracker.constants.RaidType;
import com.TheatreTracker.utility.datautility.DataPoint;
import com.TheatreTracker.utility.wrappers.PlayerCorrelatedPointData;

import java.util.*;

import static com.TheatreTracker.utility.datautility.DataPoint.PARTY_SIZE;
import static com.TheatreTracker.utility.datautility.DataPoint.RAID_INDEX;

public class SimpleRaidData
{
    public RaidType raidType = RaidType.UNASSIGNED;
    public Date raidStarted = new Date(System.currentTimeMillis());
    public boolean spectated = false;
    public boolean raidCompleted = false;
    public boolean hardMode = false;
    public boolean storyMode = false;
    public LinkedHashMap<String, Integer> players = new LinkedHashMap<>();

    public String activeValue = "";
    public String fileName = "";
    public String filePath = "";

    public int getScale()
    {
        return 1;
    }

    public Date getDate()
    {
        return new Date(System.currentTimeMillis());
    }

    public int getValue(String name)
    {
        return -1;
    }

    public int getValue(DataPoint point)
    {
        return -1;
    }

    public String getScaleString()
    {
        return "";
    }

    public String getRoomStatus()
    {
        return "";
    }

    public void setIndex(int index)
    {

    }

    public String getPlayers()
    {
        return "";
    }

    public String getPlayerList(ArrayList<Map<String, ArrayList<String>>> aliases)
    {
        return "";
    }

    public String getPlayerList()
    {
        return "";
    }

    public int getSpecificTime()
    {
        return -1;
    }

    public boolean getOverallTimeAccurate()
    {
        return true;
    }

    public PlayerCorrelatedPointData getSpecificTimeInactiveCorrelated(String inactive)
    {
        return null;
    }

    public int getSpecificTimeInactive(String inactive)
    {
        return -1;
    }

    public boolean getTimeAccurate(DataPoint key)
    {
        return false;
    }

    public int getTimeSum()
    {
        return 0;
    }

}
