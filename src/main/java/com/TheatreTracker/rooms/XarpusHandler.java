package com.TheatreTracker.rooms;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.TheatreTrackerPlugin;
import com.TheatreTracker.constants.NpcIDs;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.*;
import com.TheatreTracker.Point;
import com.TheatreTracker.utility.RoomUtil;
import com.TheatreTracker.utility.DataWriter;
import com.TheatreTracker.utility.RoomState;

import static com.TheatreTracker.constants.LogID.*;
import static com.TheatreTracker.constants.LogID.ACCURATE_XARP_END;
import static com.TheatreTracker.constants.LogID.XARPUS_STARTED;
import static com.TheatreTracker.constants.NpcIDs.*;
import static com.TheatreTracker.utility.RoomState.XarpusRoomState.FINISHED;
@Slf4j
public class XarpusHandler extends RoomHandler
{
    public RoomState.XarpusRoomState roomState = RoomState.XarpusRoomState.NOT_STARTED;
    private final TheatreTrackerPlugin plugin;

    public XarpusHandler(Client client, DataWriter clog, TheatreTrackerConfig config, TheatreTrackerPlugin plugin)
    {
        super(client, clog, config);
        this.plugin = plugin;
    }

    private int xarpusEntryTick = -1;
    private int xarpusExhumedsEnd = -1;
    private int xarpusScreechTick = -1;
    private int xarpusEndTick = -1;

    public String getName()
    {
        return "Xarpus";
    }

    public boolean isActive()
    {
        return !(roomState == RoomState.XarpusRoomState.NOT_STARTED || roomState == FINISHED);
    }

    public void reset()
    {
        super.reset();
        xarpusEntryTick = -1;
        xarpusExhumedsEnd = -1;
        xarpusScreechTick = -1;
        xarpusEndTick = -1;
        roomState = RoomState.XarpusRoomState.NOT_STARTED;
    }

    public void updateAnimationChanged(AnimationChanged event)
    {
        if (event.getActor().getAnimation() == XARPUS_AWAKENS)
        {
            endExhumeds();
        }
        if (event.getActor().getAnimation() == XARPUS_DEATH_ANIMATION)
        {
            endXarpus();
        }
    }

    public void updateProjectileMoved(ProjectileMoved event)
    {
        if (event.getProjectile().getEndCycle() - event.getProjectile().getStartCycle() == event.getProjectile().getRemainingCycles())
        {
            if (event.getProjectile().getId() == XARPUS_EXHUMED_PROJECTILE)
            {
                clog.write(XARPUS_HEAL);
            }
        }
    }

    public void updateOverheadText(OverheadTextChanged event)
    {
        if (event.getActor() instanceof NPC)
        {
            NPC npc = (NPC) event.getActor();
            int id = npc.getId();
            if (id == XARPUS_P23 || id == XARPUS_P23_HM || id == XARPUS_P23_SM)
            {
                startScreech();
            }
        }
    }

    public void updateNpcDespawned(NpcDespawned event)
    {
        switch (event.getNpc().getId())
        {
            case XARPUS_INACTIVE:
            case XARPUS_P1:
            case XARPUS_P23:
            case XARPUS_DEAD:
            case XARPUS_INACTIVE_HM:
            case XARPUS_P1_HM:
            case XARPUS_P23_HM:
            case XARPUS_DEAD_HM:
            case XARPUS_INACTIVE_SM:
            case XARPUS_P1_SM:
            case XARPUS_P23_SM:
            case XARPUS_DEAD_SM:
                clog.write(XARPUS_DESPAWNED, "" + (xarpusEndTick - xarpusEntryTick));
        }
    }

    public void updateNpcSpawned(NpcSpawned event)
    {
        boolean story = false;
        switch (event.getNpc().getId())
        {
            case NpcIDs.XARPUS_INACTIVE_SM:
            case NpcIDs.XARPUS_P1_SM:
            case NpcIDs.XARPUS_P23_SM:
            case NpcIDs.XARPUS_DEAD_SM:
                story = true;
                clog.write(IS_STORY_MODE);
            case NpcIDs.XARPUS_INACTIVE_HM:
            case NpcIDs.XARPUS_P1_HM:
            case NpcIDs.XARPUS_P23_HM:
            case NpcIDs.XARPUS_DEAD_HM:
                if (!story)
                    clog.write(IS_HARD_MODE);
            case NpcIDs.XARPUS_INACTIVE:
            case NpcIDs.XARPUS_P1:
            case NpcIDs.XARPUS_P23:
            case NpcIDs.XARPUS_DEAD:
                clog.write(XARPUS_SPAWNED);
        }
    }

    public void updateGameTick(GameTick event)
    {
        if (roomState == RoomState.XarpusRoomState.NOT_STARTED)
        {
            if (RoomUtil.crossedLine(RoomUtil.XARPUS_REGION, new Point(25, 12), new Point(27, 12), false, client))
            {
                startXarpus();
            }
        }
        if (xarpusScreechTick != -1 && xarpusScreechTick != 0 && client.getTickCount() != xarpusScreechTick && (client.getTickCount() - xarpusScreechTick) % 8 == 0 && isActive())
        {
            plugin.addLiveLine(4, client.getTickCount() - xarpusEntryTick, "Turn");
        }
    }

    private void startScreech()
    {
        clog.write(XARPUS_SCREECH, "" + (client.getTickCount() - xarpusEntryTick));
        roomState = RoomState.XarpusRoomState.POSTSCREECH;
        xarpusScreechTick = client.getTickCount();
        String splitMessage = "Wave 'Xarpus phase 2' complete. Duration: " + timeColor() + RoomUtil.time(xarpusScreechTick - xarpusEntryTick) + " (" + RoomUtil.time(xarpusScreechTick - xarpusExhumedsEnd) + ")";
        this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
        plugin.addLiveLine(4, xarpusScreechTick - xarpusEntryTick, "SCREECH");
    }

    private void startXarpus()
    {
        roomState = RoomState.XarpusRoomState.EXHUMEDS;
        xarpusEntryTick = client.getTickCount();
        roomStartTick = client.getTickCount();
        clog.write(XARPUS_STARTED);
        clog.write(ACCURATE_XARP_START);
    }

    private void endExhumeds()
    {
        roomState = RoomState.XarpusRoomState.PRESCREECH;
        xarpusExhumedsEnd = client.getTickCount();
        String splitMessage = "Wave 'Xarpus phase 1' complete. Duration: " + timeColor() + RoomUtil.time(xarpusExhumedsEnd - xarpusEntryTick);
        this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
        plugin.addLiveLine(4, client.getTickCount() - xarpusEntryTick + 1, "Exhumeds End");
    }

    private void endXarpus()
    {

        roomState = FINISHED;
        xarpusEndTick = client.getTickCount() + 3;
        clog.write(ACCURATE_XARP_END);
        plugin.liveFrame.setXarpFinished(xarpusEndTick - xarpusEntryTick);
        String splitMessage = "Wave 'Xarpus phase 3' complete. Duration: " + timeColor() + RoomUtil.time(xarpusEndTick - xarpusEntryTick) + " (" + RoomUtil.time(xarpusEndTick - xarpusScreechTick) + ")";
        this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
    }
}
