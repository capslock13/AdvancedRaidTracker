package com.TheatreTracker.utility.thrallvengtracking;

public class NPCHits
{
    public int hits;
    public int index;
    public NPCHits(int hits, int index)
    {
        this.hits = hits;
        this.index = index;
    }

    public void increment()
    {
        hits++;
    }
}
