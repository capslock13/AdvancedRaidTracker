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

@Slf4j
public class ZebakHandler extends TOARoomHandler
{
    public String getName()
    {
        return "Zebak";
    }
    RoomState.ZebakRoomState roomState = RoomState.ZebakRoomState.NOT_STARTED;
    public ZebakHandler(Client client, DataWriter clog, TheatreTrackerConfig config, TheatreTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config, plugin, handler);
    }

    @Override
    public void reset()
    {
        roomState = RoomState.ZebakRoomState.NOT_STARTED;
    }

    @Override
    public void updateGameTick(GameTick gameTick)
    {
        if(roomState == RoomState.ZebakRoomState.NOT_STARTED && RoomUtil.crossedLine(15700, new Point(37, 32), new Point(37, 32), true, client))
        {
            roomState = RoomState.ZebakRoomState.PHASE_1;
            roomStartTick = client.getTickCount();
            clog.addLine(LogID.TOA_ZEBAK_START, roomStartTick);
        }
    }

    public boolean isActive()
    {
        return !(roomState == RoomState.ZebakRoomState.NOT_STARTED || roomState == RoomState.ZebakRoomState.FINISHED);
    }

    @Override
    public void handleNPCChanged(int changed)
    {
        if(roomState == RoomState.ZebakRoomState.PHASE_1 && changed == 11733)
        {
            roomState = RoomState.ZebakRoomState.FINISHED;
            int duration = client.getTickCount()-roomStartTick;
            sendTimeMessage("Zebak Duration: ", duration);
            clog.addLine(LogID.TOA_ZEBAK_FINISHED, duration);
            plugin.liveFrame.setRoomFinished(getName(), duration);
        }
    }
}
