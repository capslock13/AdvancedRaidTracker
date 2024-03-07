package com.TheatreTracker;

import com.TheatreTracker.constants.RaidType;
import com.TheatreTracker.utility.datautility.DataPoint;
import com.TheatreTracker.utility.wrappers.PlayerCorrelatedPointData;

import java.util.*;

import static com.TheatreTracker.utility.datautility.DataPoint.PARTY_SIZE;
import static com.TheatreTracker.utility.datautility.DataPoint.RAID_INDEX;

public abstract class SimpleRaidData
{
    String red = "<html><font color='#FF0000'>";
    String green = "<html><font color='#33FF33'>";
    String orange = "<html><font color='#FF7733'>";
    String yellow = "<html><font color='#FFFF33'>";
    public abstract String getRaidType();
    public RaidType raidType = RaidType.UNASSIGNED;
    public Date raidStarted = new Date(System.currentTimeMillis());
    public boolean spectated = false;
    public boolean raidCompleted = false;
    public boolean hardMode = false;
    public boolean storyMode = false;
    public LinkedHashMap<String, Integer> players = new LinkedHashMap<>();

    public String activeValue = "";
    public String fileName;
    public String filePath;

    public abstract String getFileName();
    public abstract String getFilePath();

    public abstract int getScale();

    public abstract Date getDate();

    public abstract int getIndex();

    public abstract int getValue(String name);

    public abstract int getValue(DataPoint point);

    public abstract String getScaleString();

    public abstract String getRoomStatus();

    public abstract void setIndex(int index);

    public abstract String getPlayers();

    public abstract ArrayList<String> getPlayersArray();

    public abstract String getPlayerList(ArrayList<Map<String, ArrayList<String>>> aliases);

    public abstract String getPlayerList();

    public abstract int getSpecificTime();

    public abstract boolean getOverallTimeAccurate();

    public abstract PlayerCorrelatedPointData getSpecificTimeInactiveCorrelated(String inactive);

    public abstract int getSpecificTimeInactive(String inactive);

    public abstract boolean getTimeAccurate(DataPoint key);

    public abstract int getTimeSum();

}
