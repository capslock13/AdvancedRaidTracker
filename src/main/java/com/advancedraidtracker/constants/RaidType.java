package com.advancedraidtracker.constants;

public enum RaidType
{
    UNASSIGNED(-1, "unassigned", ""),
    COX(0, "CoX", ""),
    TOB(1, "ToB", "<html><font color='#FF0000'>"),
    TOA(2, "ToA", "<html><font color='#FFFF33'>")
    ;

    public final int value;
    public final String name;
    public final String color;

    public String colorName()
    {
        return color+name;
    }

    RaidType(int value, String name, String color)
    {
        this.value = value;
        this.name = name;
        this.color = color;
    }
}
