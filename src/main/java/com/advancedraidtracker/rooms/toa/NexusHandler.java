package com.advancedraidtracker.rooms.toa;

import com.advancedraidtracker.TheatreTrackerConfig;
import com.advancedraidtracker.TheatreTrackerPlugin;
import com.advancedraidtracker.utility.datautility.DataWriter;
import net.runelite.api.Client;

public class NexusHandler extends TOARoomHandler
{
    public NexusHandler(Client client, DataWriter clog, TheatreTrackerConfig config, TheatreTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config, plugin, handler);
    }
}
