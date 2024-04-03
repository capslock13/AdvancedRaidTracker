package com.advancedraidtracker.utility;


import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Point
{
    private int x;
    private int y;
    private int hashCode;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.hashCode = Objects.hash(x, y);
    }

    @Override
    public String toString()
    {
        return "(" + x +"," + y + ")";
    }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Point)
        {
            Point p = (Point) o;
            return p.x == x && p.y == y;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return this.hashCode;
    }
}
