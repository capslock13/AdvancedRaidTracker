package com.advancedraidtracker.rooms.cox;

import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;

import java.nio.file.Path;
import java.util.List;

public class Cox extends Raid
{
    protected Cox(Path filepath, List<LogEntry> raidData)
    {
        super(filepath, raidData);
    }

    @Override
    public int getTimeSum()
    {
        return 0;
    }

    @Override
    public String getRoomStatus()
    {
        return null;
    }

    @Override
    public RaidType getRaidType()
    {
        return null;
    }

    @Override
    public int getChallengeTime()
    {
        return 0;
    }
}
