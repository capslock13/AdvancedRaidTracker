package com.advancedraidtracker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("Advanced Raid Tracker")

public interface AdvancedRaidTrackerConfig extends Config
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
    default int chartScaleSize()
    {
        return 26;
    }

    @ConfigItem(
            position = 5,
            keyName = "useIconsOnChart",
            name = "Use icons on chart",
            description = "Replaced letter/colors with icons"
    )
    default boolean useIconsOnChart()
    {
        return true;
    }

    @ConfigItem(
            position = 6,
            keyName = "primaryDark",
            name = "Theme Dark Color",
            description = "Color to use as darkest on chart"
    )
    default Color primaryDark()
    {
        return new Color(20, 20, 20);
    }

    @ConfigItem(
            position = 7,
            keyName = "primaryMiddle",
            name = "Theme Middle Color",
            description = "Color to use as Middle on chart"
    )
    default Color primaryMiddle()
    {
        return new Color(30, 30, 30);
    }

    @ConfigItem(
            position = 8,
            keyName = "primaryLight",
            name = "Theme Light Color",
            description = "Color to use as lightest on chart"
    )
    default Color primaryLight()
    {
        return new Color(40, 40, 40);
    }

    @ConfigItem(
            position = 9,
            keyName = "letterBackgroundOpacity",
            name = "Letter BG Opacity",
            description = "Opacity of letter background color on chart"
    )
    default int letterBackgroundOpacity()
    {
        return 180;
    }

    @ConfigItem(
            position = 9,
            keyName = "iconBackgroundOpacity",
            name = "Icon BG Opacity",
            description = "Opacity of icon background color on chart"
    )
    default int iconBackgroundOpacity()
    {
        return 180;
    }

    @ConfigItem(
            position = 10,
            keyName = "useUnkitted",
            name = "Use unkitted icons",
            description = "Replaces kitted items with unkitted variants on chart icons"
    )
    default boolean useUnkitted()
    {
        return false;
    }
}
