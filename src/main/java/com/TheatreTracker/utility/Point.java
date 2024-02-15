package com.TheatreTracker.utility;


import lombok.Getter;

@Getter
public class Point
{
    private final int x;
    private final int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
