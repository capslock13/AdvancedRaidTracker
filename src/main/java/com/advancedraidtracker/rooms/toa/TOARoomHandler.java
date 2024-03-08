package com.advancedraidtracker.rooms.toa;

import com.advancedraidtracker.TheatreTrackerConfig;
import com.advancedraidtracker.TheatreTrackerPlugin;
import com.advancedraidtracker.rooms.tob.RoomHandler;
import com.advancedraidtracker.utility.datautility.DataWriter;
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
