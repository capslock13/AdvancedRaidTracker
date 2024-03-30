package com.advancedraidtracker.rooms.tob;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.utility.datautility.DataWriter;
import net.runelite.api.Client;

public class TOBRoomHandler extends RoomHandler
{
    public TOBRoomHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin)
    {
        super(client, clog, config, plugin);
    }
}
