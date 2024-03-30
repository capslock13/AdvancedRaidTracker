package com.advancedraidtracker.rooms.cox;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.utility.datautility.DataWriter;
import net.runelite.api.Client;

public class SecondSavengersHandler extends CoxRoomHandler
{
    public SecondSavengersHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin)
    {
        super(client, clog, config, plugin);
    }
}
