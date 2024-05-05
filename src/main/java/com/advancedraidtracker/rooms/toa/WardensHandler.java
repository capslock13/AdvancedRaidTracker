package com.advancedraidtracker.rooms.toa;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.RoomState;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NpcID;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;

import static com.advancedraidtracker.utility.RoomState.WardenRoomState.*;

@Slf4j
public class WardensHandler extends TOARoomHandler
{
    public String getName()
    {
        return "Wardens";
    }

    private int p1End = -1;
    private int p2End = -1;
    private int enraged = -1;
    private boolean skullsActive = false;
    private boolean coreActive = false;
    RoomState.WardenRoomState roomState = RoomState.WardenRoomState.NOT_STARTED;

    public WardensHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config, plugin, handler);
    }

    @Override
    public void reset()
    {
        roomState = RoomState.WardenRoomState.NOT_STARTED;
        p1End = -1;
        p2End = -1;
        enraged = -1;
        skullsActive = false;
        coreActive = false;
        super.reset();
    }

    @Override
    public void handleNPCChanged(int changed)
    {
        if (roomState == RoomState.WardenRoomState.NOT_STARTED && changed == 11751)
        {
            roomState = RoomState.WardenRoomState.PHASE_1;
            roomStartTick = client.getTickCount();
            clog.addLine(LogID.TOA_WARDENS_START, roomStartTick);
        } else if (roomState == RoomState.WardenRoomState.PHASE_1 && changed == 11752)
        {
            roomState = RoomState.WardenRoomState.PHASE_2;
            p1End = client.getTickCount() - roomStartTick;
            sendTimeMessage("Wardens 'Phase 1' Completed: ", p1End);
            clog.addLine(LogID.TOA_WARDENS_P1_END, p1End);
        } else if (roomState == PHASE_3 || roomState == ENRAGED)
        {
            if (!skullsActive)
            {
                if (changed == 11763 || changed == 11764)
                {
                    clog.addLine(LogID.TOA_WARDENS_SKULLS_STARTED, client.getTickCount() - roomStartTick);
                    skullsActive = true;
                }
            } else
            {
                if (changed == 11761 || changed == 11762)
                {
                    clog.addLine(LogID.TOA_WARDENS_SKULLS_ENDED, client.getTickCount() - roomStartTick);
                    skullsActive = false;
                }
            }
        }
    }

    @Override
    public void updateAnimationChanged(AnimationChanged event)
    {
        if (roomState == RoomState.WardenRoomState.PHASE_2 && event.getActor().getAnimation() == 9662)
        {
            roomState = RoomState.WardenRoomState.PHASE_3;
            p2End = client.getTickCount() - roomStartTick;
            sendTimeMessage("Wardens 'Phase 2' Completed: ", p2End, p2End - p1End);
            clog.addLine(LogID.TOA_WARDENS_P2_END, p2End);
        } else if (roomState == RoomState.WardenRoomState.PHASE_3 && event.getActor().getAnimation() == 9685)
        {
            roomState = ENRAGED;
            enraged = client.getTickCount() - roomStartTick;
            sendTimeMessage("Wardens 'Enraged': ", enraged, enraged - p2End);
            clog.addLine(LogID.TOA_WARDENS_ENRAGED, enraged);
        }
    }

    public boolean isActive()
    {
        return !(roomState == RoomState.WardenRoomState.NOT_STARTED || roomState == RoomState.WardenRoomState.FINISHED);
    }

    @Override
    public void updateNpcSpawned(NpcSpawned event)
    {
        if (!coreActive && (event.getNpc().getId() == 11770 || event.getNpc().getId() == 11771))
        {
            coreActive = true;
            clog.addLine(LogID.TOA_WARDENS_CORE_SPAWNED, client.getTickCount() - roomStartTick);
        }
    }

    @Override
    public void updateNpcDespawned(NpcDespawned event)
    {
        if (roomState == ENRAGED && (event.getNpc().getId() == 11761 || event.getNpc().getId() == 11762))
        {
			int duration = client.getTickCount()-roomStartTick;
            sendTimeMessage("Wardens Duration: ", client.getTickCount() - roomStartTick, client.getTickCount() - enraged);
            clog.addLine(LogID.TOA_WARDENS_FINISHED, client.getTickCount() - roomStartTick);
            roomState = FINISHED;
            plugin.liveFrame.setRoomFinished(getName(), client.getTickCount() - roomStartTick);
			plugin.lastSplits += "Wardens: " + RoomUtil.time(plugin.currentDurationSum) + "(+" + RoomUtil.time(duration) + ")\n";
			plugin.currentDurationSum += duration;
			plugin.lastSplits += "Duration (Completion): " + RoomUtil.time(plugin.currentDurationSum);
        }
		else if (coreActive && (event.getNpc().getId() == 11770 || event.getNpc().getId() == 11771))
        {
            coreActive = false;
            clog.addLine(LogID.TOA_WARDENS_CORE_DESPAWNED, client.getTickCount() - roomStartTick);
        }
    }
}
