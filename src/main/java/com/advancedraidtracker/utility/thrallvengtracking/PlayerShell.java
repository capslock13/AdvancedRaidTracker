package com.advancedraidtracker.utility.thrallvengtracking;

import net.runelite.api.coords.WorldPoint;

public class PlayerShell
{
    public WorldPoint worldLocation;
    public String name;

    public PlayerShell(WorldPoint location, String name)
    {
        this.worldLocation = location;
        this.name = name;
    }
}
