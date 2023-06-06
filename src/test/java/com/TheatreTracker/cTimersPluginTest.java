package com.TheatreTracker;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class cTimersPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TheatreTrackerPlugin.class);
		RuneLite.main(args);
	}
}