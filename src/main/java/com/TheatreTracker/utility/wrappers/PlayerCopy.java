package com.TheatreTracker.utility.wrappers;

import net.runelite.api.Actor;

public class PlayerCopy
{
    public String name;
    public int interactingIndex;
    public int interactingID;
    public String interactingName;
    public int animation;
    public String wornItems;
    public String weapon;
    public PlayerCopy(String name, int interactingIndex, int interactingID, String interactingName, int animation, String wornItems, String weapon)
    {
        this.name = name;
        this.interactingIndex = interactingIndex;
        this.interactingID = interactingID;
        this.interactingName = interactingName;
        this.animation = animation;
        this.wornItems = wornItems;
        this.weapon = weapon;
    }
}
