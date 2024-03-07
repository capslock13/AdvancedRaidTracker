package com.TheatreTracker.rooms.toa;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.TheatreTrackerPlugin;
import com.TheatreTracker.constants.LogID;
import com.TheatreTracker.utility.Point;
import com.TheatreTracker.constants.RoomState;
import com.TheatreTracker.utility.RoomUtil;
import com.TheatreTracker.utility.datautility.DataWriter;
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

    public BabaHandler(Client client, DataWriter clog, TheatreTrackerConfig config, TheatreTrackerPlugin plugin, TOAHandler handler)
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
    }

    @Override
    public void updateGameTick(GameTick gameTick)
    {
        if(roomState == RoomState.BabaRoomState.NOT_STARTED && RoomUtil.crossedLine(15188, new Point(24, 32), new Point(24, 32), true, client))
        {
            roomState = RoomState.BabaRoomState.PHASE_1;
            roomStartTick = client.getTickCount();
            clog.addLine(LogID.TOA_BABA_START, roomStartTick);
        }
    }

    public boolean isActive()
    {
        return !(roomState == RoomState.BabaRoomState.NOT_STARTED || roomState == RoomState.BabaRoomState.FINISHED);
    }
    @Override
    public void updateNpcSpawned(NpcSpawned spawned)
    {
        if(roomState == RoomState.BabaRoomState.PHASE_3 && spawned.getNpc().getId() == 11689)
        {
            int duration = client.getTickCount()-roomStartTick;
            sendTimeMessage("Baba Duration: ", duration, duration-b2End);
            roomState = RoomState.BabaRoomState.FINISHED;
            clog.addLine(LogID.TOA_BABA_FINISHED, duration);
            plugin.liveFrame.setRoomFinished(getName(), duration);
        }
        else if(spawned.getNpc().getId() == 11783)
        {
            clog.addLine(LogID.TOA_BABA_BOULDER_THROW, client.getTickCount()-roomStartTick);
            log.info("boulder thrown");
        }
    }

    @Override
    public void updateNpcDespawned(NpcDespawned spawned)
    {
        if(spawned.getNpc().getId() == 11783)
        {
            if(spawned.getNpc().getWorldLocation().getRegionX() > 23)
            {
                clog.addLine(LogID.TOA_BABA_BOULDER_BROKEN, client.getTickCount()-roomStartTick);
                log.info("Boulder broken");
            }
        }
    }
    @Override
    public void handleNPCChanged(int changed)
    {
        if(changed == 11780)
        {
            if(roomState == RoomState.BabaRoomState.PHASE_1)
            {
                roomState = RoomState.BabaRoomState.BOULDER_1;
                p1End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Baba 'Phase 1' Complete: ", p1End);
                clog.addLine(LogID.TOA_BABA_PHASE_1_END, p1End);
            }
            else if(roomState == RoomState.BabaRoomState.PHASE_2)
            {
                roomState = RoomState.BabaRoomState.BOULDER_2;
                p2End = client.getTickCount()-roomStartTick;
                sendTimeMessage("Baba 'Phase 2' Complete: ", p2End, p2End-b1End);
                clog.addLine(LogID.TOA_BABA_PHASE_2_END, p2End);
            }
        }
        else if(changed == 11778 && roomState == RoomState.BabaRoomState.BOULDER_1)
        {
            roomState = RoomState.BabaRoomState.PHASE_2;
            b1End = client.getTickCount()-roomStartTick;
            sendTimeMessage("Baba 'Boulder 1' Complete: ", b1End, b1End-p1End);
            clog.addLine(LogID.TOA_BABA_BOULDER_1_END, b1End);
        }
        else if(changed == 11779 && roomState == RoomState.BabaRoomState.BOULDER_2)
        {
            roomState = RoomState.BabaRoomState.PHASE_3;
            b2End = client.getTickCount()-roomStartTick;
            sendTimeMessage("Baba 'Boulder 2' complete: ", b2End, b2End-p2End);
            clog.addLine(LogID.TOA_BABA_BOULDER_2_END, b2End);
        }
    }
}
