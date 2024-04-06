package com.advancedraidtracker;

import com.advancedraidtracker.ui.charts.ChartTheme;
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
            description = "Replaces letter/colors with icons"
    )
    default boolean useIconsOnChart()
    {
        return true;
    }

    @ConfigItem(
            position = 6,
            keyName = "useTimeOnChart",
            name = "Use time on chart",
            description = "Replaces ticks with time"
    )
    default boolean useTimeOnChart()
    {
        return false;
    }

    @ConfigItem(
            position = 9,
            keyName = "theme",
            name = "Theme",
            description = "Theme to apply to UI"
    )
    default ChartTheme chartTheme()
    {
        return ChartTheme.DEFAULT;
    }

    @ConfigItem(
            position = 10,
            keyName = "primaryDark",
            name = "Theme Dark Color",
            description = "Color to use as darkest on chart"
    )
    default Color primaryDark()
    {
        return new Color(20, 20, 20);
    }

    @ConfigItem(
            position = 20,
            keyName = "primaryMiddle",
            name = "Theme Middle Color",
            description = "Color to use as Middle on chart"
    )
    default Color primaryMiddle()
    {
        return new Color(30, 30, 30);
    }

    @ConfigItem(
            position = 30,
            keyName = "primaryLight",
            name = "Theme Light Color",
            description = "Color to use as lightest on chart"
    )
    default Color primaryLight()
    {
        return new Color(40, 40, 40);
    }

    @ConfigItem(
            position = 40,
            keyName = "idleColor",
            name = "Idle Tick Color",
            description = "Color to use for idle ticks on chart"
    )
    default Color idleColor()
    {
        return new Color(38, 38, 38);
    }

    @ConfigItem(
            position = 44,
            keyName = "fontColor",
            name = "Font Color",
            description = "Color to use for font on chart"
    )
    default Color fontColor()
    {
        return new Color(220, 220, 220);
    }

    @ConfigItem(
            position = 45,
            keyName = "boxColor",
            name = "Box Color",
            description = "Color to use for boxes on chart"
    )
    default Color boxColor()
    {
        return new Color(120, 120, 120);
    }

    @ConfigItem(
            position = 46,
            keyName = "markerColor",
            name = "Marker Color",
            description = "Color to use for markers on chart"
    )
    default Color markerColor()
    {
        return new Color(255, 0, 0);
    }

    @ConfigItem(
            position = 47,
            keyName = "attackColor",
            name = "Attack Box Color",
            description = "Color of attack boxes. #FFFFFF will use defined colors per weapon"
    )
    default Color attackBoxColor()
    {
        return new Color(255, 255, 255);
    }

    @ConfigItem(
            position = 48,
            keyName = "useRounded",
            name = "Use Rounded Boxes",
            description = "Chart will use rounded boxes"
    )
    default boolean useRounded()
    {
        return true;
    }

    @ConfigItem(
            position = 49,
            keyName = "wrapAllBoxes",
            name = "Wrap All Boxes",
            description = "Chart will wrap all boxes"
    )
    default boolean wrapAllBoxes()
    {
        return false;
    }

    @ConfigItem(
            position = 50,
            keyName = "showBoldTick",
            name = "Show Bold Tick",
            description = "Chart will show bold 'Tick'"
    )
    default boolean showBoldTick()
    {
        return false;
    }

    @ConfigItem(
            position = 51,
            keyName = "rightAlignTicks",
            name = "Right Align Ticks",
            description = "Chart will right align ticks"
    )
    default boolean rightAlignTicks()
    {
        return false;
    }

    @ConfigItem(
            position = 52,
            keyName = "useAlternateFont",
            name = "Use Alternate Font",
            description = "Chart will use alternate font"
    )
    default boolean useAlternateFont()
    {
        return false;
    }

    @ConfigItem(
            position = 57,
            keyName = "letterBackgroundOpacity",
            name = "Letter BG Opacity",
            description = "Opacity of letter background color on chart"
    )
    default int letterBackgroundOpacity()
    {
        return 180;
    }

    @ConfigItem(
            position = 60,
            keyName = "iconBackgroundOpacity",
            name = "Icon BG Opacity",
            description = "Opacity of icon background color on chart"
    )
    default int iconBackgroundOpacity()
    {
        return 180;
    }

    @ConfigItem(
            position = 70,
            keyName = "useUnkitted",
            name = "Use unkitted icons",
            description = "Replaces kitted items with unkitted variants on chart icons"
    )
    default boolean useUnkitted()
    {
        return false;
    }

    @ConfigItem(
            position = 75,
            keyName = "showConfigOnChart",
            name = "Show config options on chart",
            description = "Show config options on chart"
    )
    default boolean showConfigOnChart()
    {
        return true;
    }
}
