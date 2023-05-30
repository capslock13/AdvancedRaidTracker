package com.cTimers.rooms;

import com.cTimers.cTimersConfig;
import com.cTimers.constants.LogID;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcSpawned;
import com.cTimers.utility.cLogger;
import com.cTimers.utility.cRoomState;

@Slf4j
public class cSotetseg extends cRoom
{
    public cRoomState.SotetsegRoomState roomState = cRoomState.SotetsegRoomState.NOT_STARTED;
    private boolean chosen = false;
    private int soteEntryTick = -1;
    private int soteFirstMazeStart = -1;
    private int soteSecondMazeStart = -1;
    private int soteFirstMazeEnd = -1;
    private int soteSecondMazeEnd = -1;
    private int soteDeathTick = -1;
    private int soteEnd = -1;
    private int deferTick = -1;
    private int lastRegion = -1;
    public cSotetseg(Client client, cLogger clog, cTimersConfig config)
    {
        super(client, clog, config);
    }

    public void reset()
    {
        accurateTimer = true;
        soteEntryTick = -1;
        roomState = cRoomState.SotetsegRoomState.NOT_STARTED;
        soteFirstMazeStart = -1;
        soteSecondMazeStart = -1;
        soteFirstMazeEnd = -1;
        soteSecondMazeEnd = -1;
        soteDeathTick = -1;
        soteEnd = -1;
        lastRegion = -1;
        chosen = false;
    }

    public void updateNpcSpawned(NpcSpawned event)
    {
        if(event.getNpc().getId() == 8388)
        {
            if(lastRegion == 13379)
            {
                if(roomState == cRoomState.SotetsegRoomState.MAZE_1)
                {
                    endFirstMaze();
                }
                else if(roomState == cRoomState.SotetsegRoomState.MAZE_2)
                {
                    endSecondMaze();
                }
            }
        }
    }

    public void updateAnimationChanged(AnimationChanged event)
    {
        if(event.getActor().getAnimation() == 8140)
        {
            endSotetseg();
        }
    }

    public void startSotetseg()
    {
        soteEntryTick = client.getTickCount();
        deferTick = soteEntryTick+2;
        roomState = cRoomState.SotetsegRoomState.PHASE_1;
        clog.write(51);
    }

    public void endSotetseg()
    {
        clog.write(LogID.ACCURATE_SOTE_END);
        clog.write(57, (client.getTickCount()+3-soteEntryTick)+"");
        soteDeathTick = client.getTickCount()+3;
        roomState = cRoomState.SotetsegRoomState.FINISHED;
        sendTimeMessage("Wave 'Sotetseg phase 3' complete. Duration: ", soteDeathTick-soteEntryTick, soteDeathTick-soteSecondMazeEnd, false);
    }

    public void startFirstMaze()
    {
        soteFirstMazeStart = client.getTickCount();
        clog.write(52, (soteFirstMazeStart-soteEntryTick)+"");
        roomState = cRoomState.SotetsegRoomState.MAZE_1;
        sendTimeMessage("Wave 'Sotetseg phase 1' complete. Duration: ", soteFirstMazeStart-soteEntryTick);
    }

    public void endFirstMaze()
    {
        soteFirstMazeEnd = client.getTickCount();
        clog.write(53, (soteFirstMazeEnd-soteEntryTick)+"");
        roomState = cRoomState.SotetsegRoomState.PHASE_2;
        sendTimeMessage("Wave 'Sotetseg maze 1' complete. Duration: ", soteFirstMazeEnd-soteEntryTick, soteFirstMazeEnd-soteFirstMazeStart);
    }

    public void endSecondMaze()
    {
        soteSecondMazeEnd = client.getTickCount();
        clog.write(55, (soteSecondMazeEnd-soteEntryTick)+"");
        roomState = cRoomState.SotetsegRoomState.PHASE_3;
        sendTimeMessage("Wave 'Sotetseg maze 2' complete. Duration: ", soteSecondMazeEnd-soteEntryTick, soteSecondMazeEnd-soteSecondMazeStart);
    }

    public void startSecondMaze()
    {
        soteSecondMazeStart = client.getTickCount();
        clog.write(54, (soteSecondMazeStart-soteEntryTick)+"");
        roomState = cRoomState.SotetsegRoomState.MAZE_2;
        sendTimeMessage("Wave 'Sotetseg phase 2' complete. Duration: ", soteSecondMazeStart-soteEntryTick, soteSecondMazeStart-soteFirstMazeEnd);
    }

    public void updateGameTick(GameTick event)
    {
        lastRegion = client.isInInstancedRegion() ? WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID() : client.getLocalPlayer().getWorldLocation().getRegionID();

        if(client.getTickCount() == deferTick)
        {
            deferTick = -1;
            if(client.getVarbitValue(HP_VARBIT) == 1000)
            {
                clog.write(204);
            }
        }
    }

    public void handleNPCChanged(int id)
    {
        if(id == 8388)
        {
            if(roomState == cRoomState.SotetsegRoomState.NOT_STARTED)
            {
                startSotetseg();
            }
            else if(roomState == cRoomState.SotetsegRoomState.MAZE_1)
            {
                endFirstMaze();
            }
            else if(roomState == cRoomState.SotetsegRoomState.MAZE_2)
            {
                endSecondMaze();
            }
        }
        else if(id == 8387)
        {
            if(roomState == cRoomState.SotetsegRoomState.PHASE_1)
            {
                startFirstMaze();
            }
            else if(roomState == cRoomState.SotetsegRoomState.PHASE_2)
            {
                startSecondMaze();
            }
        }
    }
}
