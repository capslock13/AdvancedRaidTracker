package com.advancedraidtracker.rooms.toa;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.Point;
import com.advancedraidtracker.utility.RoomState;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.kit.KitType;
import net.runelite.client.game.ItemManager;

import java.util.*;

@Slf4j
public class AkkhaHandler extends TOARoomHandler
{
    public String getName()
    {
        return "Akkha";
    }

    private final Set<Integer> attackIDs = new HashSet<>(Arrays.asList(5061, 10656, 1167, 1979, 8056, 7511, 7618, 1658, 401, 1378, 428, 419, 440, 1203, 390, 9471, 8288, 386, 7642, 7643, 7045, 426, 9168, 393, 7514, 9493, 7554, 6299, 377, 376, 1062, 381, 9546, 9544));
    RoomState.AkkhaRoomState roomState = RoomState.AkkhaRoomState.NOT_STARTED;
    private int p1End = -1;
    private int s1End = -1;
    private int p2End = -1;
    private int s2End = -1;
    private int p3End = -1;
    private int s3End = -1;
    private int p4End = -1;
    private int s4End = -1;
    private int p5End = -1;

    private final List<String> playersWhoAttacked = new ArrayList<>();
    private final ItemManager itemManager;

    public AkkhaHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin, TOAHandler handler, ItemManager itemManager)
    {
        super(client, clog, config, plugin, handler);
        this.itemManager = itemManager;
    }

    @Override
    public void reset()
    {
        p1End = -1;
        s1End = -1;
        p2End = -1;
        s2End = -1;
        p3End = -1;
        s3End = -1;
        p4End = -1;
        s4End = -1;
        p5End = -1;
        playersWhoAttacked.clear();
        roomState = RoomState.AkkhaRoomState.NOT_STARTED;
        super.reset();
    }

    public boolean isShadowPhase()
    {
        return roomState == RoomState.AkkhaRoomState.SHADOW_1 || roomState == RoomState.AkkhaRoomState.SHADOW_2 || roomState == RoomState.AkkhaRoomState.SHADOW_3 || roomState == RoomState.AkkhaRoomState.SHADOW_4;
    }

    private void handleAnimationChanges()
    {
        for (Player p : client.getPlayers())
        {
            for (String name : playersWhoAttacked)
            {
                if (name.equals(p.getName()))
                {
                    Actor interacting = p.getInteracting();
                    if (interacting == null || interacting.getName() == null)
                    {
                        continue;
                    }
                    if (isShadowPhase())
                    {
                        if (interacting.getName().equals("Akkha"))
                        {
                            int weapon = p.getPlayerComposition().getEquipmentId(KitType.WEAPON);
                            clog.addLine(LogID.TOA_AKKHA_NULLED_HIT_ON_AKKHA, name, String.valueOf(client.getTickCount() - roomStartTick), String.valueOf(weapon));
                            if (config.showMistakesInChat())
                            {
                                plugin.sendChatMessage(name + " attacked Akkha during shadow phase with weapon: " + itemManager.getItemComposition(weapon).getName());
                            }
                        }
                    } else
                    {
                        if (interacting.getName().contains("Akkha's Shadow"))
                        {
                            int weapon = p.getPlayerComposition().getEquipmentId(KitType.WEAPON);
                            clog.addLine(LogID.TOA_AKKHA_NULLED_HIT_ON_SHADOW, name, String.valueOf(client.getTickCount() - roomStartTick), String.valueOf(weapon));
                            if (config.showMistakesInChat())
                            {
                                plugin.sendChatMessage(name + " attacked Shadow during regular phase with weapon: " + itemManager.getItemComposition(weapon).getName());
                            }
                        }
                    }
                }
            }
        }
        playersWhoAttacked.clear();
    }

    @Override
    public void updateGameTick(GameTick gameTick)
    {
        handleAnimationChanges();
        if (roomState == RoomState.AkkhaRoomState.NOT_STARTED && RoomUtil.crossedLine(14676, new Point(41, 32), new Point(41, 32), true, client))
        {
            roomState = RoomState.AkkhaRoomState.PHASE_1;
            roomStartTick = client.getTickCount();
            clog.addLine(LogID.TOA_AKKHA_START, roomStartTick);
            active = true;
        }
        super.updateGameTick(gameTick);
    }

    @Override
    public void updateNpcSpawned(NpcSpawned spawned)
    {
        if (spawned.getNpc().getId() == 11797)
        {
            if (roomState == RoomState.AkkhaRoomState.PHASE_1)
            {
                roomState = RoomState.AkkhaRoomState.SHADOW_1;
                p1End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Akkha 'Phase 1' Completed: ", p1End);
                clog.addLine(LogID.TOA_AKKHA_PHASE_1_END, p1End);
            } else if (roomState == RoomState.AkkhaRoomState.PHASE_2)
            {
                roomState = RoomState.AkkhaRoomState.SHADOW_2;
                p2End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Akkha 'Phase 2' Completed: ", p2End, p2End - s1End);
                clog.addLine(LogID.TOA_AKKHA_PHASE_2_END, p2End);
            } else if (roomState == RoomState.AkkhaRoomState.PHASE_3)
            {
                roomState = RoomState.AkkhaRoomState.SHADOW_3;
                p3End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Akkha 'Phase 3' Completed: ", p3End, p3End - s2End);
                clog.addLine(LogID.TOA_AKKHA_PHASE_3_END, p3End);
            } else if (roomState == RoomState.AkkhaRoomState.PHASE_4)
            {
                roomState = RoomState.AkkhaRoomState.SHADOW_4;
                p4End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Akkha 'Phase 4' Completed: ", p4End, p4End - s3End);
                clog.addLine(LogID.TOA_AKKHA_PHASE_4_END, p4End);
            }
        } else if (roomState == RoomState.AkkhaRoomState.FINAL_PHASE && spawned.getNpc().getId() == 11689)
        {
            int duration = client.getTickCount() - roomStartTick;
            sendTimeMessage("Akkha Duration: ", duration, duration - p5End);
            roomState = RoomState.AkkhaRoomState.FINISHED;
            clog.addLine(LogID.TOA_AKKHA_FINISHED, duration);
            plugin.liveFrame.setRoomFinished(getName(), duration);
            active = false;
			plugin.lastSplits += "Akkha: " + RoomUtil.time(plugin.currentDurationSum) + "(+" + RoomUtil.time(duration) + ")\n";
			plugin.currentDurationSum += duration;
        }
    }

    public boolean isActive()
    {
        return !(roomState == RoomState.AkkhaRoomState.NOT_STARTED || roomState == RoomState.AkkhaRoomState.FINISHED);
    }

    @Override
    public void updateNpcDespawned(NpcDespawned spawned)
    {
        if (spawned.getNpc().getId() == 11797)
        {
            if (roomState == RoomState.AkkhaRoomState.SHADOW_1)
            {
                roomState = RoomState.AkkhaRoomState.PHASE_2;
                s1End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Akkha 'Shadow 1' Completed: ", s1End, s1End - p1End);
                clog.addLine(LogID.TOA_AKKHA_SHADOW_1_END, s1End);
            } else if (roomState == RoomState.AkkhaRoomState.SHADOW_2)
            {
                roomState = RoomState.AkkhaRoomState.PHASE_3;
                s2End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Akkha 'Shadow 2' Completed: ", s2End, s2End - p2End);
                clog.addLine(LogID.TOA_AKKHA_SHADOW_2_END, s2End);
            } else if (roomState == RoomState.AkkhaRoomState.SHADOW_3)
            {
                roomState = RoomState.AkkhaRoomState.PHASE_4;
                s3End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Akkha 'Shadow 3' Completed: ", s3End, s3End - p3End);
                clog.addLine(LogID.TOA_AKKHA_SHADOW_3_END, s3End);
            } else if (roomState == RoomState.AkkhaRoomState.SHADOW_4)
            {
                roomState = RoomState.AkkhaRoomState.PHASE_5;
                s4End = client.getTickCount() - roomStartTick;
                sendTimeMessage("Akkha 'Shadow 4' Completed: ", s4End, s4End - p4End);
                clog.addLine(LogID.TOA_AKKHA_SHADOW_4_END, s4End);
            }
        }
    }

    @Override
    public void handleNPCChanged(int changed)
    {
        if (changed == 11793 && roomState == RoomState.AkkhaRoomState.PHASE_5)
        {
            roomState = RoomState.AkkhaRoomState.FINAL_PHASE;
            p5End = client.getTickCount() - roomStartTick;
            sendTimeMessage("Akkha 'Phase 5' Completed: ", p5End, p5End - s4End);
            clog.addLine(LogID.TOA_AKKHA_PHASE_5_END, p5End);
        }
    }

    @Override
    public void updateAnimationChanged(AnimationChanged animationChanged)
    {
        Actor actor = animationChanged.getActor();
        if (actor instanceof Player && attackIDs.contains(actor.getAnimation()))
        {
            playersWhoAttacked.add(actor.getName());
        }
    }
}
