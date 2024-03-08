package com.advancedraidtracker.rooms.toa;

import com.advancedraidtracker.TheatreTrackerConfig;
import com.advancedraidtracker.TheatreTrackerPlugin;
import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.Point;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;

@Slf4j
public class HetHandler extends TOARoomHandler
{
    public String getName()
    {
        return "Het";
    }

    public HetHandler(Client client, DataWriter clog, TheatreTrackerConfig config, TheatreTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config, plugin, handler);
    }

    @Override
    public void updateGameTick(GameTick event)
    {
        if (!active && RoomUtil.crossedLine(14674, new Point(42, 31), new Point(42, 33), true, client))
        {
            active = true;
            roomStartTick = client.getTickCount();
            clog.addLine(LogID.TOA_HET_START, roomStartTick);
        }
    }

    public boolean isActive()
    {
        return active;
    }

    @Override
    public void updateNpcDespawned(NpcDespawned despawned)
    {
        if (active && despawned.getNpc().getName() != null && despawned.getNpc().getName().contains("Het's Seal"))
        {
            int duration = client.getTickCount() - roomStartTick;
            sendTimeMessage("Het Duration: ", duration);
            clog.addLine(LogID.TOA_HET_FINISHED, duration);
            active = false;
            plugin.liveFrame.setRoomFinished(getName(), duration);
        }
    }
}
