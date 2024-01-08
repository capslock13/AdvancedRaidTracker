package com.TheatreTracker.utility;

import net.runelite.api.coords.WorldPoint;

public class BloodPositionWrapper {
    public int finalTick;
    public int initialTick;
    public WorldPoint location;

    public BloodPositionWrapper(WorldPoint location, int initialTick) {
        this.initialTick = initialTick;
        this.location = location;
        this.finalTick = initialTick + 10;
    }
}
