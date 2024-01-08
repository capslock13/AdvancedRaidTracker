package com.TheatreTracker.utility;

import java.util.ArrayList;

public class PlayerHitsWrapper {
    public ArrayList<Integer> hitsplats;
    public String name;
    public int hits; //reused for tracking count

    public PlayerHitsWrapper(String name, int initial) {
        this.name = name;
        hitsplats = new ArrayList<>();
        hitsplats.add(initial);
        this.hits = 0;
    }

    public PlayerHitsWrapper(String name) {
        this.name = name;
        hitsplats = new ArrayList<>();
        this.hits = 1;
    }
}
