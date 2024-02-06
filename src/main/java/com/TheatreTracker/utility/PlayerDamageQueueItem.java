package com.TheatreTracker.utility;

public class PlayerDamageQueueItem
{
    public int arrivalTick;
    public String playerName;

    public PlayerDamageQueueItem(int arrivalTick, String playerName)
    {
        this.arrivalTick = arrivalTick;
        this.playerName = playerName;
    }
}
