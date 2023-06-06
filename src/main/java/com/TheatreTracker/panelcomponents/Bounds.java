package com.TheatreTracker.panelcomponents;

public class Bounds
{
    private int left;
    private int right;
    private int bottom;
    private int top;

    public Bounds(int l, int r, int b, int t)
    {
        left = l;
        right = r;
        bottom = b;
        top = t;
    }

    public void reset()
    {
        left = -1;
        bottom = -1;
        top = -1;
        right = -1;
    }

    public boolean matches(Bounds match)
    {
        return (match.getLeft() == left && match.getRight() == right && match.getTop() == top && match.getBottom() == bottom);
    }

    public int getLeft()
    {
        return left;
    }

    public int getRight()
    {
        return right;
    }

    public int getBottom()
    {
        return bottom;
    }

    public int getTop()
    {
        return top;
    }
}
