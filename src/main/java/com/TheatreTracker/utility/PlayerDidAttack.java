package com.TheatreTracker.utility;

public class PlayerDidAttack
{
    public String player;
    public int animation;
    public int tick;
    public String weapon;

    public PlayerDidAttack(String player, int animation, int tick, String weapon)
    {
        this.player = player;
        this.animation = animation;
        this.tick = tick;
        this.weapon = weapon;
    }
}
