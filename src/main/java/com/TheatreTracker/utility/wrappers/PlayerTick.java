package com.TheatreTracker.utility.wrappers;

public class PlayerTick
{
    public String player;
    public int tick;
    public String action;

    public PlayerTick(String player, int tick, String action)
    {
        this.player = player;
        this.tick = tick;
        this.action = action;
    }
}
