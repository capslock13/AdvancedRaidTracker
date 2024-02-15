package com.TheatreTracker.utility;

import lombok.Getter;
import lombok.Setter;

public class DawnSpec
{
    public String player;
    public int tick;

    public DawnSpec(String player, int tick)
    {
        this.player = player;
        this.tick = tick;
    }

    @Getter
    @Setter
    int damage = -1;

}
