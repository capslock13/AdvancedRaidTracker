package com.TheatreTracker.ui.comparisonview.graph;

import com.TheatreTracker.SimpleRaidData;
import lombok.Getter;

import java.util.ArrayList;

public class Bounds
{
    @Getter
    private int left;
    @Getter
    private int right;
    @Getter
    private int bottom;
    @Getter
    private int top;

    public ArrayList<SimpleRaidData> raids;

    public Bounds(int l, int r, int b, int t, ArrayList<SimpleRaidData> raids)
    {
        this.raids = raids;
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

}
