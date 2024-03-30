package com.advancedraidtracker.rooms.toa;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.rooms.tob.RoomHandler;
import com.advancedraidtracker.utility.datautility.DataWriter;
import net.runelite.api.Client;

public class TOARoomHandler extends RoomHandler
{

    AdvancedRaidTrackerPlugin plugin;
    public TOAHandler handler;

    public TOARoomHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config, plugin);
        this.plugin = plugin;
        this.handler = handler;
    }
}
