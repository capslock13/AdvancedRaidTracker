package com.TheatreTracker.rooms.tob;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.utility.datautility.DataWriter;
import net.runelite.api.Client;

public class TOBRoomHandler extends RoomHandler
{
    public TOBRoomHandler(Client client, DataWriter clog, TheatreTrackerConfig config)
    {
        super(client, clog, config);
    }
}
