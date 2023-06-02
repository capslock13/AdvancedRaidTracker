package com.cTimers.rooms;

import com.cTimers.cTimersConfig;
import com.cTimers.constants.LogID;
import com.cTimers.constants.NpcIDs;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.*;
import com.cTimers.Point;
import com.cTimers.utility.RoomUtil;
import com.cTimers.utility.cLogger;
import com.cTimers.utility.cRoomState;

import static com.cTimers.constants.LogID.*;
import static com.cTimers.constants.LogID.ACCURATE_XARP_END;
import static com.cTimers.constants.LogID.XARPUS_STARTED;
import static com.cTimers.constants.NpcIDs.*;

@Slf4j
public class cXarpus extends cRoom
{
    public cRoomState.XarpusRoomState roomState = cRoomState.XarpusRoomState.NOT_STARTED;

    public cXarpus(Client client, cLogger clog, cTimersConfig config)
    {
        super(client, clog, config);
    }
    private int xarpusEntryTick = -1;
    private int xarpusExhumedsEnd = -1;
    private int xarpusScreechTick = -1;
    private int xarpusEndTick = -1;

    public void reset()
    {
        xarpusEntryTick = -1;
        xarpusExhumedsEnd = -1;
        xarpusScreechTick = -1;
        xarpusEndTick = -1;
        roomState = cRoomState.XarpusRoomState.NOT_STARTED;
    }

    public void updateAnimationChanged(AnimationChanged event)
    {
        if(event.getActor().getAnimation() == XARPUS_AWAKENS)
        {
            endExhumeds();
        }
        if(event.getActor().getAnimation() == XARPUS_DEATH_ANIMATION)
        {
            endXarpus();
        }
    }

    public void updateProjectileMoved(ProjectileMoved event)
    {
        if(event.getProjectile().getEndCycle()-event.getProjectile().getStartCycle() == event.getProjectile().getRemainingCycles())
        {
            if(event.getProjectile().getId() == XARPUS_EXHUMED_PROJECTILE)
            {
                clog.write(XARPUS_HEAL);
            }
        }
    }

    public void updateOverheadText(OverheadTextChanged event)
    {
        if(event.getActor() instanceof NPC)
        {
            NPC npc = (NPC) event.getActor();
            int id = npc.getId();
            if(id == XARPUS_P23 || id == XARPUS_P23_HM || id == XARPUS_P23_SM)
            {
                startScreech();
            }
        }
    }

    public void updateNpcDespawned(NpcDespawned event)
    {
        switch(event.getNpc().getId())
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
                clog.write(XARPUS_DESPAWNED, ""+(xarpusEndTick-xarpusEntryTick));
        }
    }

    public void updateNpcSpawned(NpcSpawned event)
    {
        boolean story = false;
        switch(event.getNpc().getId())
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
                if(!story)
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
        if(roomState == cRoomState.XarpusRoomState.NOT_STARTED)
        {
            if(RoomUtil.crossedLine(RoomUtil.XARPUS_REGION, new Point(25, 12), new Point(27, 12), false, client))
            {
                startXarpus();
            }
        }
    }

    private void startScreech()
    {
        clog.write(XARPUS_SCREECH, ""+(client.getTickCount()-xarpusEntryTick));
        roomState = cRoomState.XarpusRoomState.POSTSCREECH;
        xarpusScreechTick = client.getTickCount();
        String splitMessage = "Wave 'Xarpus phase 2' complete. Duration: " + timeColor() + RoomUtil.time(xarpusScreechTick-xarpusEntryTick) + " (" + RoomUtil.time(xarpusScreechTick-xarpusExhumedsEnd) + ")";
        this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null,false);
    }

    private void startXarpus()
    {
        roomState = cRoomState.XarpusRoomState.EXHUMEDS;
        xarpusEntryTick = client.getTickCount();
        clog.write(XARPUS_STARTED);
        clog.write(ACCURATE_XARP_START);
    }

    private void endExhumeds()
    {
        roomState = cRoomState.XarpusRoomState.PRESCREECH;
        xarpusExhumedsEnd = client.getTickCount();
        String splitMessage = "Wave 'Xarpus phase 1' complete. Duration: " + timeColor() + RoomUtil.time(xarpusExhumedsEnd-xarpusEntryTick);
        this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null,false);
    }

    private void endXarpus()
    {

        roomState = cRoomState.XarpusRoomState.FINISHED;
        xarpusEndTick = client.getTickCount()+3;
        clog.write(ACCURATE_XARP_END);
        String splitMessage = "Wave 'Xarpus phase 3' complete. Duration: " + timeColor() + RoomUtil.time(xarpusEndTick-xarpusEntryTick) + " (" + RoomUtil.time(xarpusEndTick-xarpusScreechTick) + ")";
        this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null,false);
    }
}
