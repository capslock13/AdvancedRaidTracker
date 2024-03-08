package com.advancedraidtracker.utility.maidenbloodtracking;

public class BloodDamageToBeApplied
{
    public String playerName;
    public int bloodTicksAlive;

    public BloodDamageToBeApplied(String playerName, int bloodTicksAlive)
    {
        this.playerName = playerName;
        this.bloodTicksAlive = bloodTicksAlive;
    }
}
