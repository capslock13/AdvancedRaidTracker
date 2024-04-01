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
import net.runelite.api.Player;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcSpawned;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ZebakHandler extends TOARoomHandler
{
    public String getName()
    {
        return "Zebak";
    }
    private int lastJugSpecialTick = -1;
    private int lastWaterfallSpecialTick = -1;

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
        super.updateGameTick(gameTick);
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
    public void updateNpcSpawned(NpcSpawned spawned)
    {
        if(lastJugSpecialTick+5 < client.getTickCount() && spawned.getNpc().getId() == 11737)
        {
            lastJugSpecialTick = client.getTickCount();
            clog.addLine(LogID.TOA_ZEBAK_BOULDER_ATTACK, client.getTickCount()-roomStartTick);
        }
        else if(lastWaterfallSpecialTick != client.getTickCount() && spawned.getNpc().getId() == 11738)
        {
            lastWaterfallSpecialTick = client.getTickCount();
            clog.addLine(LogID.TOA_ZEBAK_WATERFALL_ATTACK, client.getTickCount()-roomStartTick);
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
        if(changed == 11732)
        {
            clog.addLine(LogID.TOA_ZEBAK_ENRAGED, client.getTickCount()-roomStartTick);
        }
    }
}
