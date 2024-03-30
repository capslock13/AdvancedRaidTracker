package com.advancedraidtracker.rooms.cox;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.utility.datautility.DataWriter;
import net.runelite.api.Client;

public class TektonHandler extends CoxRoomHandler
{
    public TektonHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin)
    {
        super(client, clog, config, plugin);
    }
}
