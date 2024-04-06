package com.advancedraidtracker.constants;

public enum RaidType
{
    ALL(-1, "All", ""),
    TOB(1, "ToB", "<html><font color='#FF0000'>"),
    TOA(2, "ToA", "<html><font color='#FFFF33'>"),
    COX(0, "CoX", ""),
    INFERNO(5, "Inferno", "<html><font color='#ffa500'>"),
    COLOSSEUM(4, "Colosseum", "<html><font color='#77BBFF'>"),
    ;

    public final int value;
    public final String name;
    public final String color;

    public String colorName()
    {
        return color + name;
    }

    RaidType(int value, String name, String color)
    {
        this.value = value;
        this.name = name;
        this.color = color;
    }
}
