package com.TheatreTracker;

import com.TheatreTracker.constants.RaidType;

import java.util.Date;

public class SimpleTOAData extends SimpleRaidData
{
    public SimpleTOAData(String[] parameters, String filePath, String fileName)
    {
        raidType = RaidType.TOA;
    }
}
