package com.advancedraidtracker.constants;

public enum RaidType
{
    TOB(1, "ToB", "<html><font color='#FF0000'>"),
    TOA(2, "ToA", "<html><font color='#FFFF33'>"),
    COX(0, "CoX", ""),
    COLOSSEUM(4, "Col", "<html><font color='#88DDDD'>"),
    UNASSIGNED(-1, "All", ""),
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
