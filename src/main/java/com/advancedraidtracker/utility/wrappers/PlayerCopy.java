package com.advancedraidtracker.utility.wrappers;

import net.runelite.api.coords.WorldPoint;

public class PlayerCopy
{
    public String name;
    public int interactingIndex;
    public int interactingID;
    public String interactingName;
    public int animation;
    public String wornItems;
    public int weapon;
    public WorldPoint worldPoint;

    public PlayerCopy(String name, int interactingIndex, int interactingID, String interactingName, int animation, String wornItems, int weapon, WorldPoint worldPoint)
    {
        this.name = name;
        this.interactingIndex = interactingIndex;
        this.interactingID = interactingID;
        this.interactingName = interactingName;
        this.animation = animation;
        this.wornItems = wornItems;
        this.weapon = weapon;
        this.worldPoint = worldPoint;
    }
}
