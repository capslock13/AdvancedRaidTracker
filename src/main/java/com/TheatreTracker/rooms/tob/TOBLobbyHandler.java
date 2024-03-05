package com.TheatreTracker.rooms.tob;

import com.TheatreTracker.TheatreTrackerConfig;
import net.runelite.api.Client;
import com.TheatreTracker.utility.datautility.DataWriter;

public class TOBLobbyHandler extends TOBRoomHandler
{
    public TOBLobbyHandler(Client client, DataWriter clog, TheatreTrackerConfig config)
    {
        super(client, clog, config);
    }
}
