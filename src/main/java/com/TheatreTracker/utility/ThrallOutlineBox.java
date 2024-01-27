package com.TheatreTracker.utility;

import java.awt.*;

import static com.TheatreTracker.constants.NpcIDs.*;

public class ThrallOutlineBox
{
    public int spawnTick;
    public String owner;
    public int id;
    public ThrallOutlineBox(String owner, int spawnTick, int id)
    {
        this.spawnTick = spawnTick;
        this.owner = owner;
        this.id = id;
    }

    public Color getColor()
    {
        switch(id)
        {
            case MELEE_THRALL:
                return new Color(240, 30, 30);
            case RANGE_THRALL:
                return new Color(30, 240, 30);
            case MAGE_THRALL:
                return new Color(30, 30, 240);
            default:
                return new Color(30, 30, 30);
        }
    }
}
