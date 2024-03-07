package com.TheatreTracker.rooms.toa;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.TheatreTrackerPlugin;
import com.TheatreTracker.rooms.tob.RoomHandler;
import com.TheatreTracker.utility.datautility.DataWriter;
import net.runelite.api.Client;

public class TOARoomHandler extends RoomHandler
{

    TheatreTrackerPlugin plugin;
    public TOAHandler handler;
    public TOARoomHandler(Client client, DataWriter clog, TheatreTrackerConfig config, TheatreTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config);
        this.plugin = plugin;
        this.handler = handler;
    }
}
