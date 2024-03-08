package com.advancedraidtracker.utility.wrappers;

public class PlayerTick
{
    public String player;
    public int tick;
    public String action;
    public String[] wornItems;

    public PlayerTick(String player, int tick, String action, String[] wornItems)
    {
        this.player = player;
        this.tick = tick;
        this.action = action;
        this.wornItems = wornItems;
    }
}
