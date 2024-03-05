package com.TheatreTracker.constants;

public enum RaidType
{
    UNASSIGNED(-1, "unassigned"), COX(0, "cox"), TOB(1, "tob"), TOA(2, "toa");

    public final int value;
    public final String name;
    RaidType(int value, String name)
    {
        this.value = value;
        this.name = name;
    }
}
