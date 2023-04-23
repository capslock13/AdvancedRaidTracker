package com.cTimers;

import com.cTimers.cTimersPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class cTimersPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(cTimersPlugin.class);
		RuneLite.main(args);
	}
}