package com.advancedraidtracker.constants;

public enum RaidType
{
    UNASSIGNED(-1, "unassigned"),
    COX(0, "CoX"),
    TOB(1, "ToB"),
    TOA(2, "ToA")
    ;

    public final int value;
    public final String name;

    RaidType(int value, String name)
    {
        this.value = value;
        this.name = name;
    }
}
