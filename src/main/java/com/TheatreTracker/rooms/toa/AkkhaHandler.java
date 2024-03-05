package com.TheatreTracker.rooms.toa;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.TheatreTrackerPlugin;
import com.TheatreTracker.utility.datautility.DataWriter;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;

public class AkkhaHandler extends TOARoomHandler
{
    public AkkhaHandler(Client client, DataWriter clog, TheatreTrackerConfig config, TheatreTrackerPlugin plugin)
    {
        super(client, clog, config, plugin);
    }

    @Override
    public void updateGameTick(GameTick event)
    {
    }
}
