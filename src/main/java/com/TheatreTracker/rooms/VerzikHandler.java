package com.TheatreTracker.rooms;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.constants.LogID;
import com.TheatreTracker.utility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import com.TheatreTracker.utility.RoomState;

import static com.TheatreTracker.constants.LogID.*;
import static com.TheatreTracker.constants.NpcIDs.*;

@Slf4j
public class VerzikHandler extends RoomHandler
{
    public RoomState.VerzikRoomState roomState;
    public VerzikHandler(Client client, DataWriter clog, TheatreTrackerConfig config)
    {
        super(client, clog, config);
    }
    private int verzikEntryTick = -1;
    private int verzikP1EndTick = -1;
    private int verzikRedsTick = -1;
    private int verzikP2EndTick = -1;
    private int verzikP3EndTick = -1;
    public void reset()
    {
        verzikEntryTick = -1;
        verzikP1EndTick = -1;
        verzikRedsTick = -1;
        verzikP2EndTick = -1;
        verzikP3EndTick = -1;
    }

    public void updateGraphicChanged(GraphicChanged event)
    {
        if(event.getActor().hasSpotAnim(VERZIK_BOUNCE_SPOT_ANIMATION))
        {
            clog.write(LogID.VERZIK_BOUNCE);
        }
    }

    public void updateAnimationChanged(AnimationChanged event)
    {
        if(event.getActor().getAnimation() == VERZIK_BECOMES_SPIDER)
        {
            endP3();
        }
    }

    public void updateNpcSpawned(NpcSpawned event)
    {
        int id = event.getNpc().getId();
        if(id == VERZIK_MATOMENOS || id == VERZIK_MATOMENOS_HM || id == VERZIK_MATOMENOS_SM)
        {
            if(roomState != RoomState.VerzikRoomState.PHASE_2_REDS)
            {
                procReds();
            }
        }
        switch(id)
        {
            case VERZIK_MELEE_NYLO:
            case VERZIK_RANGE_NYLO:
            case VERZIK_MAGE_NYLO:
            case VERZIK_MELEE_NYLO_HM:
            case VERZIK_RANGE_NYLO_HM:
            case VERZIK_MAGE_NYLO_HM:
            case VERZIK_MELEE_NYLO_SM:
            case VERZIK_RANGE_NYLO_SM:
            case VERZIK_MAGE_NYLO_SM:
                clog.write(VERZIK_CRAB_SPAWNED);
                break;
        }
    }

    public void updateNpcDespawned(NpcDespawned event)
    {
        int id = event.getNpc().getId();
        if(id == VERZIK_P1 || id == VERZIK_P1_HM || id == VERZIK_P1_SM)
        {
            endP1();
        }
        else if(id == VERZIK_P2 || id == VERZIK_P2_HM || id == VERZIK_P2_SM)
        {
            endP2();
        }
    }

    public void handleNPCChanged(int id)
    {
        if(id == VERZIK_P1 || id == VERZIK_P1_HM || id == VERZIK_P1_SM)
        {
            if(id == VERZIK_P1_HM)
            {
                clog.write(IS_HARD_MODE);
            }
            else if(id == VERZIK_P1_SM)
            {
                clog.write(IS_STORY_MODE);
            }
            startVerzik();
        }
        else if(id == VERZIK_P2 || id == VERZIK_P2_HM || id == VERZIK_P2_SM)
        {
            endP1();
        }
        else if (id == VERZIK_P3 || id == VERZIK_P3_HM || id == VERZIK_P3_SM)
        {
            endP2();
        }
        else if(id == VERZIK_DEAD || id == VERZIK_DEAD_HM || id == VERZIK_DEAD_SM)
        {
            endP3();
        }
    }

    private void startVerzik()
    {
        roomState = RoomState.VerzikRoomState.PHASE_1;
        verzikEntryTick = client.getTickCount();
        clog.write(VERZIK_P1_START);
        clog.write(ACCURATE_VERZIK_START);
    }

    private void endP1()
    {
        roomState = RoomState.VerzikRoomState.PHASE_2;
        verzikP1EndTick = client.getTickCount();
        sendTimeMessage("Wave 'Verzik phase 1' complete. Duration: ", verzikP1EndTick-verzikEntryTick);
        clog.write(VERZIK_P1_DESPAWNED, (verzikP1EndTick-verzikEntryTick)+"");
    }

    private void procReds()
    {
        roomState = RoomState.VerzikRoomState.PHASE_2_REDS;
        verzikRedsTick = client.getTickCount();
        sendTimeMessage("Red Crabs Spawned. Duration: ", verzikRedsTick-verzikEntryTick);
        clog.write(VERZIK_P2_REDS_PROC, (verzikRedsTick-verzikEntryTick)+"");
    }

    private void endP2()
    {
        roomState = RoomState.VerzikRoomState.PHASE_3;
        verzikP2EndTick = client.getTickCount();
        sendTimeMessage("Wave 'Verzik phase 2' complete. Duration: ", verzikP2EndTick-verzikEntryTick, verzikP2EndTick-verzikP1EndTick);
        clog.write(VERZIK_P2_END, (verzikP2EndTick-verzikEntryTick)+"");
    }

    private void endP3()
    {
        roomState = RoomState.VerzikRoomState.FINISHED;
        verzikP3EndTick = client.getTickCount()+6;
        clog.write(ACCURATE_VERZIK_END);
        sendTimeMessage("Wave 'Verzik phase 3' complete. Duration: ", verzikP3EndTick-verzikEntryTick, verzikP3EndTick-verzikP2EndTick);
        clog.write(VERZIK_P3_DESPAWNED, (verzikP3EndTick-verzikEntryTick)+"");
    }
}
