package com.advancedraidtracker.rooms.toa;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.Point;
import com.advancedraidtracker.utility.RoomState;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;

@Slf4j
public class BabaHandler extends TOARoomHandler
{
    public String getName()
    {
        return "Baba";
    }

    RoomState.BabaRoomState roomState = RoomState.BabaRoomState.NOT_STARTED;
    private int p1End = -1;
    private int b1End = -1;
    private int p2End = -1;
    private int b2End = -1;

    public BabaHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config, plugin, handler);
    }

    @Override
    public void reset()
    {
        roomState = RoomState.BabaRoomState.NOT_STARTED;
        p1End = -1;
        b1End = -1;
        p2End = -1;
        b2End = -1;
        super.reset();
    }

    @Override
    public void updateGameTick(GameTick gameTick)
    {
        if (roomState == RoomState.BabaRoomState.NOT_STARTED && RoomUtil.playerPastLine(15188, 23, true, client, false))
        {
            roomState = RoomState.BabaRoomState.PHASE_1;
            roomStartTick = client.getTickCount();
            clog.addLine(LogID.TOA_BABA_START, roomStartTick);
            active = true;
        }
        super.updateGameTick(gameTick);
    }

    public boolean isActive()
    {
        return !(roomState == RoomState.BabaRoomState.NOT_STARTED || roomState == RoomState.BabaRoomState.FINISHED);
    }

    @Override
    public void updateNpcSpawned(NpcSpawned spawned)
    {
        if (roomState == RoomState.BabaRoomState.PHASE_3 && spawned.getNpc().getId() == 11689)
        {
            int duration = client.getTickCount() - roomStartTick;
            sendTimeMessage("Baba Duration: ", duration, duration - b2End);
            roomState = RoomState.BabaRoomState.FINISHED;
            clog.addLine(LogID.TOA_BABA_FINISHED, duration);
            plugin.liveFrame.setRoomFinished(getName(), duration);
            active = false;
			plugin.lastSplits += "Baba: " + RoomUtil.time(plugin.currentDurationSum) + "(+" + RoomUtil.time(duration) + ")\n";
			plugin.currentDurationSum += duration;
        } else if (spawned.getNpc().getId() == 11783)
        {
            clog.addLine(LogID.TOA_BABA_BOULDER_THROW, client.getTickCount() - roomStartTick);
        }
    }

    @Override
    public void updateNpcDespawned(NpcDespawned spawned)
    {
        if (spawned.getNpc().getId() == 11783)
        {
            if (spawned.getNpc().getWorldLocation().getRegionX() > 23)
            {
                clog.addLine(LogID.TOA_BABA_BOULDER_BROKEN, client.getTickCount() - roomStartTick);
            }
        }
    }

    @Override
    public void handleNPCChanged(int changed)
    {
        if (changed == 11780)
        {
            if (roomState == RoomState.BabaRoomState.PHASE_1)
            {
                roomState = RoomState.BabaRoomState.BOULDER_1;
                p1End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Baba 'Phase 1' Complete: ", p1End);
                clog.addLine(LogID.TOA_BABA_PHASE_1_END, p1End);
            } else if (roomState == RoomState.BabaRoomState.PHASE_2)
            {
                roomState = RoomState.BabaRoomState.BOULDER_2;
                p2End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Baba 'Phase 2' Complete: ", p2End, p2End - b1End);
                clog.addLine(LogID.TOA_BABA_PHASE_2_END, p2End);
            }
        } else if (changed == 11778 && roomState == RoomState.BabaRoomState.BOULDER_1)
        {
            roomState = RoomState.BabaRoomState.PHASE_2;
            b1End = client.getTickCount() - roomStartTick;
            sendTimeMessage("Baba 'Boulder 1' Complete: ", b1End, b1End - p1End);
            clog.addLine(LogID.TOA_BABA_BOULDER_1_END, b1End);
        } else if (changed == 11779 && roomState == RoomState.BabaRoomState.BOULDER_2)
        {
            roomState = RoomState.BabaRoomState.PHASE_3;
            b2End = client.getTickCount() - roomStartTick;
            sendTimeMessage("Baba 'Boulder 2' complete: ", b2End, b2End - p2End);
            clog.addLine(LogID.TOA_BABA_BOULDER_2_END, b2End);
        }
    }
}
