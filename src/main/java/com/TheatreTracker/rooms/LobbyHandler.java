package com.TheatreTracker.rooms;

import com.TheatreTracker.TheatreTrackerConfig;
import net.runelite.api.Client;
import com.TheatreTracker.utility.datautility.DataWriter;

public class LobbyHandler extends RoomHandler
{
    public LobbyHandler(Client client, DataWriter clog, TheatreTrackerConfig config)
    {
        super(client, clog, config);
    }
}
