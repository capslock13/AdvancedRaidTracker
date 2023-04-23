package com.cTimers;


import lombok.Getter;
//TODO replace with runelite Point
public class Point
{
    @Getter
    private int x;
    @Getter
    private int y;
    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
