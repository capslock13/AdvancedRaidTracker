package com.TheatreTracker.utility.wrappers;

import java.util.ArrayList;

public class PlayerHitsWrapper
{
    public ArrayList<Integer> hitsplats;
    public String name;
    public int hits;

    public PlayerHitsWrapper(String name, int initial)
    {
        this.name = name;
        hitsplats = new ArrayList<>();
        hitsplats.add(initial);
        this.hits = 0;
    }
}
