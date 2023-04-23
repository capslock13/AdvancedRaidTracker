package com.cTimers;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("cTimers")

public interface cTimersConfig extends Config
{
    @ConfigItem(
            position = 0,
            keyName = "chatSplts",
            name = "Chat Splits",
            description = "Display room splits in chatbox."
    )
    default boolean chatSplits()
    {
        return false;
    }

    @ConfigItem(
            position = 1,
            keyName = "writeToLog",
            name = "Track Raid Data",
            description = "Tracks data about the raids you spectate and participate in."
    )
    default boolean writeToLog() { return true; }
}
