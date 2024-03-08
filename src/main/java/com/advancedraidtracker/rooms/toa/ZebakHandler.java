package com.advancedraidtracker.rooms.toa;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.Point;
import com.advancedraidtracker.constants.RoomState;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;

@Slf4j
public class ZebakHandler extends TOARoomHandler
{
    public String getName()
    {
        return "Zebak";
    }

    RoomState.ZebakRoomState roomState = RoomState.ZebakRoomState.NOT_STARTED;

    public ZebakHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin, TOAHandler handler)
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
        if (roomState == RoomState.ZebakRoomState.NOT_STARTED && RoomUtil.crossedLine(15700, new Point(37, 32), new Point(37, 32), true, client))
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
    public void updateAnimationChanged(AnimationChanged animationChanged)
    {
        if (animationChanged.getActor() instanceof Player)
        {
            if (animationChanged.getActor().getAnimation() == 832)
            {
                clog.addLine(LogID.TOA_ZEBAK_JUG_PUSHED, animationChanged.getActor().getName(), String.valueOf(client.getTickCount() - roomStartTick));
            }
        }
    }

    @Override
    public void handleNPCChanged(int changed)
    {
        if (roomState == RoomState.ZebakRoomState.PHASE_1 && changed == 11733)
        {
            roomState = RoomState.ZebakRoomState.FINISHED;
            int duration = client.getTickCount() - roomStartTick;
            sendTimeMessage("Zebak Duration: ", duration);
            clog.addLine(LogID.TOA_ZEBAK_FINISHED, duration);
            plugin.liveFrame.setRoomFinished(getName(), duration);
        }
    }
}
