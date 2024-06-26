package com.advancedraidtracker.rooms.toa;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.Point;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;

import static com.advancedraidtracker.constants.LogID.TOA_HET_DOWN;

@Slf4j
public class HetHandler extends TOARoomHandler
{
    public String getName()
    {
        return "Het";
    }

    public HetHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config, plugin, handler);
    }

    @Override
    public void updateGameTick(GameTick event)
    {
        if (!active && roomStartTick == -1 && RoomUtil.playerPastLine(14674, 43, true, client, true)) //todo fix magic numbers
        {
            active = true;
            roomStartTick = client.getTickCount();
            clog.addLine(LogID.TOA_HET_START, roomStartTick);
        }
        super.updateGameTick(event);
    }

    public boolean isActive()
    {
        return active;
    }

    @Override
    public void handleNPCChanged(int changed)
    {
        if (changed == 11707)
        {
            clog.addLine(TOA_HET_DOWN, client.getTickCount() - roomStartTick);
        }
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
			plugin.lastSplits += "Het: " + RoomUtil.time(plugin.currentDurationSum) + "(+" + RoomUtil.time(duration) + ")\n";
			plugin.currentDurationSum += duration;
        }
    }
}
