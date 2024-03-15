package com.advancedraidtracker.rooms.toa;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.Point;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcSpawned;

@Slf4j
public class ApmekenHandler extends TOARoomHandler
{
    public String getName()
    {
        return "Apmeken";
    }

    public ApmekenHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config, plugin, handler);
    }

    @Override
    public void updateGameTick(GameTick event)
    {
        if (!active && RoomUtil.playerPastLine(15186, 21, true, client))
        {
            active = true;
            roomStartTick = client.getTickCount();
            clog.addLine(LogID.TOA_APMEKEN_START, roomStartTick);
        }
    }

    @Override
    public void updateNpcSpawned(NpcSpawned spawned)
    {
        if (spawned.getNpc().getId() == 11715)
        {
            clog.addLine(LogID.TOA_APMEKEN_SHAMAN_SPAWN, client.getTickCount() - roomStartTick);
        } else if (spawned.getNpc().getId() == 11716)
        {
            clog.addLine(LogID.TOA_APMEKEN_VOLATILE_SPAWN, client.getTickCount() - roomStartTick);
        } else if (spawned.getNpc().getId() == 11717)
        {
            clog.addLine(LogID.TOA_APMEKEN_CURSED_SPAWN, client.getTickCount() - roomStartTick);
        }
    }

    public boolean isActive()
    {
        return active;
    }

    @Override
    public void updateGameObjectDespawned(GameObjectDespawned gameObjectDespawned)
    {
        if (active && gameObjectDespawned.getGameObject().getId() == 45135 && gameObjectDespawned.getGameObject().getWorldLocation().getRegionX() == 44 &&
                gameObjectDespawned.getGameObject().getWorldLocation().getRegionY() == 32)
        {
            active = false;
            int duration = client.getTickCount() - roomStartTick;
            sendTimeMessage("Apmeken Duration: ", duration);
            clog.addLine(LogID.TOA_APMEKEN_FINISHED, duration);
            plugin.liveFrame.setRoomFinished(getName(), duration);
        }
    }
}
