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
import net.runelite.api.events.HitsplatApplied;

@Slf4j
public class CrondisHandler extends TOARoomHandler
{

    public String getName()
    {
        return "Crondis";
    }

    public CrondisHandler(Client client, DataWriter clog, TheatreTrackerConfig config, TheatreTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config, plugin, handler);
    }

    @Override
    public void updateGameTick(GameTick event)
    {
        if (!active && RoomUtil.crossedLine(15698, new Point(45, 31), new Point(45, 33), true, client))
        {
            active = true;
            roomStartTick = client.getTickCount();
            clog.addLine(LogID.TOA_CRONDIS_START, roomStartTick);
        }
    }

    public boolean isActive()
    {
        return active;
    }

    @Override
    public void handleNPCChanged(int npcChanged)
    {
        if (npcChanged == 11704)
        {
            active = false;
            int duration = client.getTickCount() - roomStartTick;
            sendTimeMessage("Crondis Duration: ", duration);
            clog.addLine(LogID.TOA_CRONDIS_FINISHED, duration);
            plugin.liveFrame.setRoomFinished(getName(), duration);
        }
    }

    @Override
    public void updateHitsplatApplied(HitsplatApplied hitsplatApplied)
    {
        log.info(hitsplatApplied.getHitsplat().getAmount() + ", " + hitsplatApplied.getHitsplat().getHitsplatType());
        if (hitsplatApplied.getActor().getName().contains("Palm"))
        {
            if (hitsplatApplied.getHitsplat().getHitsplatType() == 11)
            {
                clog.addLine(LogID.TOA_CRONDIS_WATER, String.valueOf(hitsplatApplied.getHitsplat().getAmount()), String.valueOf(client.getTickCount() - roomStartTick));
            } else if (hitsplatApplied.getHitsplat().getHitsplatType() == 15)
            {
                clog.addLine(LogID.TOA_CRONDIS_CROC_DAMAGE, String.valueOf(hitsplatApplied.getHitsplat().getAmount()), String.valueOf(client.getTickCount() - roomStartTick));
            }
        }
    }
}
