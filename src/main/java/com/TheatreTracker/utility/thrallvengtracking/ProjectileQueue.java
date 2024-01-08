package com.TheatreTracker.utility.thrallvengtracking;

public class ProjectileQueue {
    public int originTick;
    public int finalTick;
    public int targetIndex;

    public ProjectileQueue(int originTick, int finalTick, int targetIndex) {
        this.originTick = originTick;
        this.finalTick = finalTick;
        this.targetIndex = targetIndex;
    }
}
