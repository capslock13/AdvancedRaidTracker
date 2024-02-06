package com.TheatreTracker.utility;

import net.runelite.api.NPC;

public class ThrallWrapper
{
    public NPC thrall;
    public boolean isAnimating;
    public int hitAppliedTick;

    public ThrallWrapper(NPC thrall)
    {
        this.thrall = thrall;
        isAnimating = false;
        hitAppliedTick = -1;
    }
}
