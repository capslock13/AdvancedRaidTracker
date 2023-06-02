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

import static com.cTimers.constants.NpcIDs.*;

@Slf4j
public class cSotetseg extends cRoom
{
    public cRoomState.SotetsegRoomState roomState = cRoomState.SotetsegRoomState.NOT_STARTED;
    private int soteEntryTick = -1;
    private int soteFirstMazeStart = -1;
    private int soteSecondMazeStart = -1;
    private int soteFirstMazeEnd = -1;
    private int soteSecondMazeEnd = -1;
    private int soteDeathTick = -1;
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
        lastRegion = -1;
    }

    public void updateNpcSpawned(NpcSpawned event)
    {
        int id = event.getNpc().getId();
        if(id == SOTETSEG_ACTIVE || id == SOTETSEG_ACTIVE_HM || id == SOTETSEG_ACTIVE_SM)
        {
            if(lastRegion == SOTETSEG_UNDERWORLD)
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
        if(event.getActor().getAnimation() == SOTETSEG_DEATH_ANIMATION)
        {
            endSotetseg();
        }
    }

    public void startSotetseg()
    {
        soteEntryTick = client.getTickCount();
        deferTick = soteEntryTick+2;
        roomState = cRoomState.SotetsegRoomState.PHASE_1;
        clog.write(LogID.SOTETSEG_STARTED);
    }

    public void endSotetseg()
    {
        clog.write(LogID.ACCURATE_SOTE_END);
        clog.write(LogID.SOTETSEG_ENDED, (client.getTickCount()+3-soteEntryTick)+"");
        soteDeathTick = client.getTickCount()+3;
        roomState = cRoomState.SotetsegRoomState.FINISHED;
        sendTimeMessage("Wave 'Sotetseg phase 3' complete. Duration: ", soteDeathTick-soteEntryTick, soteDeathTick-soteSecondMazeEnd, false);
    }

    public void startFirstMaze()
    {
        soteFirstMazeStart = client.getTickCount();
        clog.write(LogID.SOTETSEG_FIRST_MAZE_STARTED, (soteFirstMazeStart-soteEntryTick)+"");
        roomState = cRoomState.SotetsegRoomState.MAZE_1;
        sendTimeMessage("Wave 'Sotetseg phase 1' complete. Duration: ", soteFirstMazeStart-soteEntryTick);
    }

    public void endFirstMaze()
    {
        soteFirstMazeEnd = client.getTickCount();
        clog.write(LogID.SOTETSEG_FIRST_MAZE_ENDED, (soteFirstMazeEnd-soteEntryTick)+"");
        roomState = cRoomState.SotetsegRoomState.PHASE_2;
        sendTimeMessage("Wave 'Sotetseg maze 1' complete. Duration: ", soteFirstMazeEnd-soteEntryTick, soteFirstMazeEnd-soteFirstMazeStart);
    }

    public void startSecondMaze()
    {
        soteSecondMazeStart = client.getTickCount();
        clog.write(LogID.SOTETSEG_SECOND_MAZE_STARTED, (soteSecondMazeStart-soteEntryTick)+"");
        roomState = cRoomState.SotetsegRoomState.MAZE_2;
        sendTimeMessage("Wave 'Sotetseg phase 2' complete. Duration: ", soteSecondMazeStart-soteEntryTick, soteSecondMazeStart-soteFirstMazeEnd);
    }

    public void endSecondMaze()
    {
        soteSecondMazeEnd = client.getTickCount();
        clog.write(LogID.SOTETSEG_SECOND_MAZE_ENDED, (soteSecondMazeEnd-soteEntryTick)+"");
        roomState = cRoomState.SotetsegRoomState.PHASE_3;
        sendTimeMessage("Wave 'Sotetseg maze 2' complete. Duration: ", soteSecondMazeEnd-soteEntryTick, soteSecondMazeEnd-soteSecondMazeStart);
    }

    public void updateGameTick(GameTick event)
    {
        lastRegion = client.isInInstancedRegion() ? WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID() : client.getLocalPlayer().getWorldLocation().getRegionID();

        if(client.getTickCount() == deferTick)
        {
            deferTick = -1;
            if(client.getVarbitValue(HP_VARBIT) == FULL_HP)
            {
                clog.write(LogID.ACCURATE_SOTE_START);
            }
        }
    }

    public void handleNPCChanged(int id)
    {
        if(id == SOTETSEG_ACTIVE || id == SOTETSEG_ACTIVE_HM || id == SOTETSEG_ACTIVE_SM)
        {
            if(roomState == cRoomState.SotetsegRoomState.NOT_STARTED)
            {
                if(id == SOTETSEG_ACTIVE_HM)
                {
                    clog.write(LogID.IS_HARD_MODE);
                }
                else if(id == SOTETSEG_ACTIVE_SM)
                {
                    clog.write(LogID.IS_STORY_MODE);
                }
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
        else if(id == SOTETSEG_INACTIVE || id == SOTETSEG_INACTIVE_HM || id == SOTETSEG_INACTIVE_SM)
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
