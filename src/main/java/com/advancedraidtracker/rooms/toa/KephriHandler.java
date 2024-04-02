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
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;

@Slf4j
public class KephriHandler extends TOARoomHandler
{
    public String getName()
    {
        return "Kephri";
    }

    RoomState.KephriRoomState roomState = RoomState.KephriRoomState.NOT_STARTED;
    private int p1End = -1;
    private int s1End = -1;
    private int p2End = -1;
    private int s2End = -1;
    private int meleeTicksAlive = 0;
    private boolean meleeAlive = false;
    int swarms = 0;
    int swarmsHealed = 0;

    public KephriHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config, plugin, handler);
    }

    @Override
    public void reset()
    {
        roomState = RoomState.KephriRoomState.NOT_STARTED;
        p1End = -1;
        s1End = -1;
        p2End = -1;
        s2End = -1;
        meleeTicksAlive = 0;
        meleeAlive = false;
        swarms = 0;
        swarmsHealed = 0;
        super.reset();
    }

    @Override
    public void updateGameTick(GameTick gameTick)
    {
        if (roomState == RoomState.KephriRoomState.NOT_STARTED && RoomUtil.crossedLine(14164, new Point(25, 32), new Point(25, 32), true, client))
        {
            roomState = RoomState.KephriRoomState.PHASE_1;
            roomStartTick = client.getTickCount();
            clog.addLine(LogID.TOA_KEPHRI_START, roomStartTick);
            active = true;
        }
        if (meleeAlive)
        {
            meleeTicksAlive++;
        }
        super.updateGameTick(gameTick);
    }

    @Override
    public void updateHitsplatApplied(HitsplatApplied applied)
    {
        if (applied.getHitsplat().getHitsplatType() == 11)
        {
            clog.addLine(LogID.TOA_KEPHRI_HEAL, String.valueOf(client.getTickCount() - roomStartTick), String.valueOf(applied.getHitsplat().getAmount()));
            swarmsHealed++;
        }
        super.updateHitsplatApplied(applied);
    }

    @Override
    public void updateProjectileMoved(ProjectileMoved projectileMoved)
    {
        if (projectileMoved.getProjectile().getEndCycle() - projectileMoved.getProjectile().getStartCycle() == projectileMoved.getProjectile().getRemainingCycles())
        {
            if (projectileMoved.getProjectile().getId() == 2150)
            {
                clog.addLine(LogID.TOA_KEPHRI_MELEE_HEAL, client.getTickCount() - roomStartTick);
                swarmsHealed--;
            }
        }
    }

    @Override
    public void updateNpcSpawned(NpcSpawned spawned)
    {
        if (spawned.getNpc().getId() == 11723)
        {
            clog.addLine(LogID.TOA_KEPHRI_SWARM_SPAWN, client.getTickCount() - roomStartTick);
            swarms++;
        } else if (spawned.getNpc().getId() == 11724)
        {
            meleeAlive = true;
        }
    }

    @Override
    public void updateNpcDespawned(NpcDespawned spawned)
    {
        if (spawned.getNpc().getId() == 11724)
        {
            meleeAlive = false;
            clog.addLine(LogID.TOA_KEPHRI_MELEE_ALIVE_TICKS, meleeTicksAlive);
            meleeTicksAlive = 0;
        }
    }

    @Override
    public void updateAnimationChanged(AnimationChanged event)
    {
        if (event.getActor().getName() != null && event.getActor().getName().contains("Kephri") && event.getActor().getAnimation() == 9578)
        {
            clog.addLine(LogID.TOA_KEPHRI_DUNG_THROWN, client.getTickCount() - roomStartTick);
        }
    }

    @Override
    public void updateGraphicsObjectCreated(GraphicsObjectCreated go)
    {
        if (go.getGraphicsObject().getId() == 2158 || go.getGraphicsObject().getId() == 2157)
        {
            WorldPoint wp = WorldPoint.fromLocal(client, go.getGraphicsObject().getLocation());
            for (Player player : client.getPlayers())
            {
                if (player.getWorldLocation().distanceTo(wp) == 0)
                {
                    //clog.addLine(LogID.TOA_KEPHRI_BOMB_TANKED, String.valueOf(client.getTickCount()-roomStartTick), player.getName());
                    //log.info(player.getName() + " tanked a bomb");
                }
            }
        }
    }

    public boolean isActive()
    {
        return !(roomState == RoomState.KephriRoomState.NOT_STARTED || roomState == RoomState.KephriRoomState.FINISHED);
    }

    @Override
    public void handleNPCChanged(int changed)
    {
        if (changed == 11720)
        {
            if (roomState == RoomState.KephriRoomState.PHASE_1)
            {
                roomState = RoomState.KephriRoomState.SWARM_1;
                p1End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Kephri 'Phase 1' Complete: ", p1End);
                clog.addLine(LogID.TOA_KEPHRI_PHASE_1_END, p1End);
            } else if (roomState == RoomState.KephriRoomState.PHASE_2)
            {
                roomState = RoomState.KephriRoomState.SWARM_2;
                p2End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Kephri 'Phase 2' Complete: ", p2End, p2End - s1End);
                clog.addLine(LogID.TOA_KEPHRI_PHASE_2_END, p2End);
            }
        } else if (changed == 11719)
        {
            if (roomState == RoomState.KephriRoomState.SWARM_1)
            {
                roomState = RoomState.KephriRoomState.PHASE_2;
                s1End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Kephri 'Swarm 1' Complete: ", s1End, s1End - p1End);
                clog.addLine(LogID.TOA_KEPHRI_SWARM_1_END, s1End);
                plugin.sendChatMessage("Swarms Healed: (" + swarmsHealed + "/" + swarms + ")");
                swarmsHealed = 0;
                swarms = 0;
            }
        } else if (changed == 11721)
        {
            if (roomState == RoomState.KephriRoomState.SWARM_2)
            {
                roomState = RoomState.KephriRoomState.PHASE_3;
                s2End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Kephri 'Swarm 2' Complete: ", s2End, s2End - p2End);
                clog.addLine(LogID.TOA_KEPHRI_SWARM_2_END, s2End);
                plugin.sendChatMessage("Swarms Healed: (" + swarmsHealed + "/" + swarms + ")");
                swarmsHealed = 0;
                swarms = 0;
            }
        }
        if (roomState == RoomState.KephriRoomState.PHASE_3 && changed == 11722)
        {
            int duration = client.getTickCount() - roomStartTick + 3;
            sendTimeMessage("Kephri Duration: ", duration, duration - s2End);
            clog.addLine(LogID.TOA_KEPHRI_FINISHED, duration);
            plugin.liveFrame.setRoomFinished(getName(), duration);
            active = false;
        }
    }
}
