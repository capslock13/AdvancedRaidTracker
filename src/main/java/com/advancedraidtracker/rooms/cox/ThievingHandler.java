package com.advancedraidtracker.rooms.cox;

import com.advancedraidtracker.TheatreTrackerConfig;
import com.advancedraidtracker.utility.datautility.DataWriter;
import net.runelite.api.Client;

public class ThievingHandler extends CoxRoomHandler
{
    public ThievingHandler(Client client, DataWriter clog, TheatreTrackerConfig config)
    {
        super(client, clog, config);
    }
}
