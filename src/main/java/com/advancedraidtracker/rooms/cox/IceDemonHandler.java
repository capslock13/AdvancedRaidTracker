package com.advancedraidtracker.rooms.cox;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.rooms.RoomHandler;
import com.advancedraidtracker.utility.datautility.DataWriter;
import net.runelite.api.Client;

public class IceDemonHandler extends RoomHandler
{
    public IceDemonHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin)
    {
        super(client, clog, config, plugin);
    }
}
