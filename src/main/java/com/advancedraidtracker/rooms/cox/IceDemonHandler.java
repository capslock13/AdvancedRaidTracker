package com.advancedraidtracker.rooms.cox;

import com.advancedraidtracker.TheatreTrackerConfig;
import com.advancedraidtracker.rooms.tob.RoomHandler;
import com.advancedraidtracker.utility.datautility.DataWriter;
import net.runelite.api.Client;

public class IceDemonHandler extends RoomHandler
{
    public IceDemonHandler(Client client, DataWriter clog, TheatreTrackerConfig config)
    {
        super(client, clog, config);
    }
}
