package com.TheatreTracker.rooms.toa;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.TheatreTrackerPlugin;
import com.TheatreTracker.constants.LogID;
import com.TheatreTracker.constants.RoomState;
import com.TheatreTracker.utility.Point;
import com.TheatreTracker.utility.RoomUtil;
import com.TheatreTracker.utility.datautility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.*;

import static com.TheatreTracker.constants.ToaIDs.SCABARAS_GATE_OBJECT;

@Slf4j
public class ScabarasHandler extends TOARoomHandler
{
    public String getName()
    {
        return "Scabaras";
    }
    public ScabarasHandler(Client client, DataWriter clog, TheatreTrackerConfig config, TheatreTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config, plugin, handler);
    }

    @Override
    public void updateGameObjectSpawned(GameObjectSpawned event)
    {
        if(event.getGameObject().getId() == SCABARAS_GATE_OBJECT && roomStartTick == -1)
        {

        }
    }

    @Override
    public void updateGameTick(GameTick event)
    {
        if(!active)
        {
            if(RoomUtil.crossedLine(14162, new Point(13, 26), new Point(13, 38), true, client))
            {
                roomStartTick = client.getTickCount();
                active = true;
                clog.addLine(LogID.TOA_SCABARAS_START, roomStartTick);
            }
        }
    }

    public boolean isActive()
    {
        return active;
    }

    public void endScabaras()
    {
        active = false;
        int scabarasDuration = client.getTickCount()-roomStartTick+1;
        sendTimeMessage("Scabaras Puzzle time: ", scabarasDuration);
        clog.addLine(LogID.TOA_SCABARAS_FINISHED, scabarasDuration);
        plugin.liveFrame.setRoomFinished(getName(), scabarasDuration);
    }

    @Override
    public void updateGameObjectDespawned(GameObjectDespawned event)
    {
        if(event.getGameObject().getId() == SCABARAS_GATE_OBJECT && event.getGameObject().getWorldLocation().getRegionX() == 55 && active && client.getTickCount()-roomStartTick > 20)
        {
            endScabaras();
        }
    }
}
