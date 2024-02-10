package com.TheatreTracker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("Theatre Statistic Tracker")

public interface TheatreTrackerConfig extends Config
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
    default boolean writeToLog()
    {
        return true;
    }

    @ConfigItem(
            position = 2,
            keyName = "showMistakesInChat",
            name = "Show mistakes in chat?",
            description = "Shows mistakes (hammer bop, etc) in chat box when they occur."
    )
    default boolean showMistakesInChat()
    {
        return true;
    }

    @ConfigItem(
            position = 3,
            keyName = "chartScaleSize",
            name = "Chart Scale Size",
            description = "Adjusts box size in chart"
    )
    default int chartScaleSize() { return 26;}
}
