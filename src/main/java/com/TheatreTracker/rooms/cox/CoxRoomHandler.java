package com.TheatreTracker.rooms.cox;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.rooms.tob.RoomHandler;
import com.TheatreTracker.utility.datautility.DataWriter;
import net.runelite.api.Client;

public class CoxRoomHandler extends RoomHandler
{
    public CoxRoomHandler(Client client, DataWriter clog, TheatreTrackerConfig config)
    {
        super(client, clog, config);
    }
}
