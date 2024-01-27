package com.TheatreTracker.utility.nyloutility;

public class DawnSpec
{
    public String player;
    public int tick;
    public DawnSpec(String player, int tick)
    {
        this.player = player;
        this.tick = tick;
    }

    int damage = -1;
    public void setDamage(int damage)
    {
        this.damage = damage;
    }

    public int getDamage()
    {
        return damage;
    }
}
