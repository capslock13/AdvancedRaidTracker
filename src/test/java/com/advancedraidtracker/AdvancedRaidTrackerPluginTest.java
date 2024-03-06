package com.advancedraidtracker;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class AdvancedRaidTrackerPluginTest
{
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(AdvancedRaidTrackerPlugin.class);
        RuneLite.main(args);
    }
}