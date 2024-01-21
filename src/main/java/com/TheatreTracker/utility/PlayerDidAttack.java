package com.TheatreTracker.utility;

public class PlayerDidAttack
{
    public String player;
    public String animation;
    public int tick;
    public String weapon;
    public String projectile;
    public String spotAnims;

    public PlayerDidAttack(String player, String animation, int tick, String weapon, String projectile, String spotAnims)
    {
        this.player = player;
        this.animation = animation;
        this.tick = tick;
        this.weapon = weapon;
        this.projectile = projectile;
        this.spotAnims = spotAnims;
    }
}
