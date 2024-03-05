package com.TheatreTracker.rooms.cox;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.rooms.tob.RoomHandler;
import com.TheatreTracker.utility.datautility.DataWriter;
import net.runelite.api.Client;

public class IceDemonHandler extends RoomHandler
{
    public IceDemonHandler(Client client, DataWriter clog, TheatreTrackerConfig config)
    {
        super(client, clog, config);
    }
}
