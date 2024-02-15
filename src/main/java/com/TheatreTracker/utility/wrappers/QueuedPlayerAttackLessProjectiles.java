package com.TheatreTracker.utility.wrappers;

import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;

public class QueuedPlayerAttackLessProjectiles
{
    public Player player;
    public String animation;
    public int tick;
    public String spotAnims;
    public String weapon;

    public WorldPoint location;

    public QueuedPlayerAttackLessProjectiles(Player player, WorldPoint location, int tick, String spotAnims, String weapon, String animation)
    {
        this.player = player;
        this.animation = animation;
        this.tick = tick;
        this.spotAnims = spotAnims;
        this.weapon = weapon;
        this.location = location;
    }
}
