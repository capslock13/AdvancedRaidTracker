package com.cTimers.rooms;

import com.cTimers.cTimersConfig;
import com.cTimers.constants.LogID;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import com.cTimers.utility.cLogger;
import com.cTimers.utility.cRoomState;

import static com.cTimers.constants.LogID.*;

@Slf4j
public class cVerzik extends cRoom
{
    public cRoomState.VerzikRoomState roomState;

    public cVerzik(Client client, cLogger clog, cTimersConfig config)
    {
        super(client, clog, config);
    }

    private int verzikEntryTick = -1;
    private int verzikP1EndTick = -1;
    private int verzikRedsTick = -1;
    private int verzikP2EndTick = -1;
    private int verzikP3EndTick = -1;

    private int redTicks = -1;

    public void reset()
    {
        log.info("Resetting verzik");
        verzikEntryTick = -1;
        verzikP1EndTick = -1;
        verzikRedsTick = -1;
        verzikP2EndTick = -1;
        verzikP3EndTick = -1;
        redTicks = -1;
    }

    public void updateGraphicChanged(GraphicChanged event)
    {
        if(event.getActor().hasSpotAnim(245))
        {
            clog.write(LogID.VERZIK_BOUNCE);
        }
    }

    public void updateAnimationChanged(AnimationChanged event)
    {
        if(event.getActor().getAnimation() == 8128)
        {
            endP3();
        }
    }

    public void updateGameTick(GameTick event)
    {
        if(redTicks != -1)
        {
            redTicks--;
        }
    }
    public void updateNpcSpawned(NpcSpawned event)
    {
        if(event.getNpc().getId() == 8385)
        {
            if(roomState != cRoomState.VerzikRoomState.PHASE_2_REDS)
            {
                procReds();
            }
        }
        if(event.getNpc().getId() == 8381 || event.getNpc().getId() == 8382 || event.getNpc().getId() == 8383)
        {
            clog.write(78);
        }
    }

    public void updateNpcDespawned(NpcDespawned event)
    {
        if(event.getNpc().getId() == 8370)
        {
            endP1();
        }
        else if(event.getNpc().getId() == 8372)
        {
            endP2();
        }
    }

    public void handleNPCChanged(int id)
    {
        if(id == 8370)
        {
            startVerzik();
        }
        else if(id == 8371)
        {
            endP1();
        }
        else if (id == 8373)
        {
            endP2();
        }
        else if(id == 8375)
        {
            endP3();
        }
    }

    private void startVerzik()
    {
        roomState = cRoomState.VerzikRoomState.PHASE_1;
        verzikEntryTick = client.getTickCount();
        clog.write(VERZIK_P1_START);
        clog.write(ACCURATE_VERZIK_START);
    }

    private void endP1()
    {
        roomState = cRoomState.VerzikRoomState.PHASE_2;
        verzikP1EndTick = client.getTickCount();
        sendTimeMessage("Wave 'Verzik phase 1' complete. Duration: ", verzikP1EndTick-verzikEntryTick);
        clog.write(VERZIK_P1_DESPAWNED, (verzikP1EndTick-verzikEntryTick)+"");
    }

    private void procReds()
    {
        roomState = cRoomState.VerzikRoomState.PHASE_2_REDS;
        verzikRedsTick = client.getTickCount();
        sendTimeMessage("Red Crabs Spawned. Duration: ", verzikRedsTick-verzikEntryTick);
        clog.write(VERZIK_P2_REDS_PROC, (verzikRedsTick-verzikEntryTick)+"");
    }

    private void endP2()
    {
        roomState = cRoomState.VerzikRoomState.PHASE_3;
        verzikP2EndTick = client.getTickCount();
        sendTimeMessage("Wave 'Verzik phase 2' complete. Duration: ", verzikP2EndTick-verzikEntryTick, verzikP2EndTick-verzikP1EndTick);
        clog.write(VERZIK_P2_END, (verzikP2EndTick-verzikEntryTick)+"");
    }

    private void endP3()
    {
        roomState = cRoomState.VerzikRoomState.FINISHED;
        verzikP3EndTick = client.getTickCount()+6;
        clog.write(ACCURATE_VERZIK_END);
        sendTimeMessage("Wave 'Verzik phase 3' complete. Duration: ", verzikP3EndTick-verzikEntryTick, verzikP3EndTick-verzikP2EndTick);
        clog.write(VERZIK_P3_DESPAWNED, (verzikP3EndTick-verzikEntryTick)+"");
    }
}
