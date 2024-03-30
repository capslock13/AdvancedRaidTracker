package com.advancedraidtracker.rooms.tob;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import net.runelite.api.Client;
import com.advancedraidtracker.utility.datautility.DataWriter;

public class TOBLobbyHandler extends TOBRoomHandler
{
    public TOBLobbyHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin)
    {
        super(client, clog, config, plugin);
    }
}
