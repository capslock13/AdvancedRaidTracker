package com.TheatreTracker.utility.thrallvengtracking;

public class VengDamageQueue {
    public int appliedTick;
    public int damage;
    public String target;

    public VengDamageQueue(String target, int damage, int appliedTick) {
        this.appliedTick = appliedTick;
        this.damage = damage;
        this.target = target;
    }
}
