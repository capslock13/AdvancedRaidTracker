package com.advancedraidtracker.utility.datautility;

public enum SingleRoomMappedDataPoint
{
    DAWN_DROPS("Dawn Dropped"),
    WEBS_THROWN("Webs Thrown"),
    BLOAT_DOWNS("Bloat Downs"),
    VERZIK_BOUNCES("Verzik Bounces"),

    ;
    public String name;
    SingleRoomMappedDataPoint(String name)
    {
        this.name = name;
    }
}
