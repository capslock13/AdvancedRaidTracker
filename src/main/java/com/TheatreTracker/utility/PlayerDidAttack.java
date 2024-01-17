package com.TheatreTracker.utility;

public class PlayerDidAttack
{
    public String player;
    public int animation;
    public int tick;

    public PlayerDidAttack(String player, int animation, int tick)
    {
        this.player = player;
        this.animation = animation;
        this.tick = tick;
    }
}
