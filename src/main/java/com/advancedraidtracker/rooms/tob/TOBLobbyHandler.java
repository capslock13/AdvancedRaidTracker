package com.advancedraidtracker.rooms.tob;

import com.advancedraidtracker.TheatreTrackerConfig;
import net.runelite.api.Client;
import com.advancedraidtracker.utility.datautility.DataWriter;

public class TOBLobbyHandler extends TOBRoomHandler
{
    public TOBLobbyHandler(Client client, DataWriter clog, TheatreTrackerConfig config)
    {
        super(client, clog, config);
    }
}
