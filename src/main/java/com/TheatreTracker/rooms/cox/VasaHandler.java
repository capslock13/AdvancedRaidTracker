package com.TheatreTracker.rooms.cox;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.utility.datautility.DataWriter;
import net.runelite.api.Client;

public class VasaHandler extends CoxRoomHandler
{
    public VasaHandler(Client client, DataWriter clog, TheatreTrackerConfig config)
    {
        super(client, clog, config);
    }
}
