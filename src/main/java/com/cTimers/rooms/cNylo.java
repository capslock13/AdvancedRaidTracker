package com.cTimers.rooms;

import com.cTimers.cTimersConfig;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import com.cTimers.utility.RoomUtil;
import com.cTimers.utility.cLogger;
import com.cTimers.utility.cRoomState;
import com.cTimers.utility.nyloutility.cNylocas;
import com.cTimers.utility.nyloutility.cNylocasShell;
import com.cTimers.utility.nyloutility.cNylocasWave;
import com.cTimers.utility.nyloutility.cNylocasWaveMatcher;

import java.util.ArrayList;

import static com.cTimers.constants.LogID.*;
import static com.cTimers.constants.NpcIDs.*;
import static com.cTimers.utility.cRoomState.NyloRoomState.*;

@Slf4j
public class cNylo extends cRoom
{
    public cRoomState.NyloRoomState roomState;
    private final ArrayList<cNylocasShell> buildWave;
    public cNylo(Client client, cLogger clog, cTimersConfig config)
    {
        super(client, clog, config);
        buildWave = new ArrayList<>();
        nylosAlive = new ArrayList<>();
    }
    public static int instanceStart = -1;
    private int pillarsSpawnedTick = -1;
    private int instanceReference = -1;
    private int bossSpawn = -1;
    private int lastDead = -1;
    private int entryTickOffset = -1;
    private int wave31 = -1;
    private final ArrayList<NPC> nylosAlive;
    int currentWave = 0;
    boolean hard = false;
    public void reset()
    {
        pillarsSpawnedTick = -1;
        instanceReference = -1;
        instanceStart = -1;
        bossSpawn = -1;
        lastDead = -1;
        accurateEntry = true;
        entryTickOffset = -1;
        hard = false;
        wave31 = -1;
        super.reset();
    }
    private int expectedWaveTick;
    public void updateGameTick(GameTick event)
    {
        if(buildWave.size() != 0)
        {
            if(cNylocasWaveMatcher.isWave(buildWave))
            {
                cNylocasWave wave = cNylocasWaveMatcher.getWave();
                if(wave.getWave() == 1)
                {
                    wave1Spawn();
                }
                if(wave.getWave() == 31)
                {
                    wave31Spawn();
                }
                else
                {
                    expectedWaveTick = client.getTickCount()+ ((hard && wave.getWave() % 10 == 0) ? 16 : cNylocasWave.waves[wave.getWave()].getDelay());
                    if(hard && wave.getWave() % 10 == 0)
                    {
                        expectedWaveTick = client.getTickCount()+16;
                    }
                }
                currentWave = wave.getWave();
            }
            buildWave.clear();
        }
        if(client.getTickCount() == expectedWaveTick && currentWave != 31)
        {
            clog.write(NYLO_STALL, ""+currentWave, ""+(client.getTickCount()-pillarsSpawnedTick), ""+nylosAlive.size()); //todo hm
            expectedWaveTick += 4;
        }
    }
    public void updateNpcSpawned(NpcSpawned event)
    {
        switch(event.getNpc().getId())
        {
            case 10794:
            case 10795:
            case 10796:
            case 10791:
            case 10792:
            case 10793:
                hard = true;
            case 8342: //mel
            case 8343: //ran
            case 8344: //mag
            case 8345: //mel
            case 8346: //ran
            case 8347: //mag
            case 8351: //mel
            case 8352: //ran
            case 8353: //mag
                cNylocasShell cShell = new cNylocasShell(event.getNpc().getId(), event.getNpc().getWorldLocation().getRegionX(), event.getNpc().getWorldLocation().getRegionY());

                if(cShell.position != cNylocas.cNyloPosition.ROOM)
                {
                    buildWave.add(cShell);
                }
                else
                {
                    switch(event.getNpc().getId())
                    {
                        case 8342:
                            clog.write(34, ""+currentWave, ""+(client.getTickCount()-pillarsSpawnedTick));
                            break;
                        case 8343:
                            clog.write(32, ""+currentWave, ""+(client.getTickCount()-pillarsSpawnedTick));
                            break;
                        case 8344:
                            clog.write(33, ""+currentWave, ""+(client.getTickCount()-pillarsSpawnedTick));
                            break;
                    }
                }
                nylosAlive.add(event.getNpc());
                break;
            case 8355:
            case 8356:
            case 8357:
                bossSpawned();
            case 8358:
                if(pillarsSpawnedTick == -1)
                {
                    startNylo();
                    if (client.getNpcs().stream().anyMatch(p -> p.getName().toLowerCase().contains("nylo")))
                    {
                        accurateEntry = false;
                    }
                    else
                    {
                        clog.write(ACCURATE_NYLO_START);
                    }
                }
                break;
            case 10803:
                break;
            case 10804:
            case 10805:
            case 10806:
                nylosAlive.add(event.getNpc());
                break;

        }
    }
    public void updateNpcDespawned(NpcDespawned event)
    {
        switch(event.getNpc().getId())
        {
            case 8355:
            case 8356:
            case 8357:
            {
                bossDefinitelyKilled();
            }
            break;
            case 8342:
            case 8343:
            case 8344:
            case 8345:
            case 8351:
            case 8346:
            case 8352:
            case 8348:
            case 8350:
            case 8349:
            case 8347:
            case 8353:
            case 10791:
            case 10792:
            case 10793:
            case 10794:
            case 10795:
            case 10796:
            case 10797:
            case 10798:
            case 10799:
            case 10803:
            case 10804:
            case 10805:
            case 10806:
            case 10800:
            case 10801:
            case 10802:
                nylosAlive.remove(event.getNpc());
                if(nylosAlive.size() == 0 && cNylocasWaveMatcher.getWave().getWave() == 31)
                {
                    cleanupOver();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Handles saving data about boss phases.
     * @param id ID of new form
     */
    public void handleNPCChanged(int id)
    {
        if(id == MELEE_NYLO_BOSS_ID)
        {
            clog.write(MELEE_PHASE, ""+(client.getTickCount()-pillarsSpawnedTick));
        }
        else if(id == MAGE_NYLO_BOSS_ID)
        {
            clog.write(MAGE_PHASE, ""+(client.getTickCount()-pillarsSpawnedTick));
        }
        else if(id == RANGE_NYLO_BOSS_ID)
        {
            clog.write(RANGE_PHASE, ""+(client.getTickCount()-pillarsSpawnedTick));
        }
    }

    private void startNylo()
    {
        clog.write(NYLO_PILLAR_SPAWN);
        roomState = WAVES;
        pillarsSpawnedTick = client.getTickCount();
    }

    private void wave1Spawn()
    {
        instanceReference = client.getTickCount();
        entryTickOffset = (instanceReference-pillarsSpawnedTick-4);
    }

    private void wave31Spawn()
    {
        clog.write(LAST_WAVE, ""+(client.getTickCount()-pillarsSpawnedTick));
        roomState = CLEANUP;
        int stalls = (client.getTickCount()-pillarsSpawnedTick-entryTickOffset-236)/4;
        wave31 = client.getTickCount();
        sendTimeMessage("Wave 'Nylocas last wave' complete! Duration: ", wave31-pillarsSpawnedTick, " Stalls: ", stalls);
    }

    private void cleanupOver()
    {
        roomState = WAITING_FOR_BOSS;
        lastDead = client.getTickCount();
        int offsetTick = 4-((client.getTickCount()-instanceReference)%4);
        if(offsetTick == 4)
        {
            offsetTick = 0;
        }
       //TODO int estimateBossSpawn = client.getTickCount()-pillarsSpawnedTick+offsetTick+16;
        clog.write(303);
        clog.write(36, ""+(lastDead-pillarsSpawnedTick));
        sendTimeMessage("Wave 'Nylocas waves and cleanup' complete! Duration: ",lastDead-pillarsSpawnedTick, lastDead-wave31);
    }

    private void bossSpawned()
    {
        clog.write(BOSS_SPAWN, ""+(client.getTickCount()-pillarsSpawnedTick));
        roomState = BOSS;
        bossSpawn = client.getTickCount()-2;
        sendTimeMessage("Wave 'Nylocas boss spawn' complete! Duration: ", bossSpawn-pillarsSpawnedTick, bossSpawn-lastDead);
    }

    private void bossDefinitelyKilled()
    {
        roomState = FINISHED;
        int deathTick = client.getTickCount();
        int offset1 = 4-((deathTick-instanceReference) % 4);
        if((4-((deathTick-instanceReference) % 4) == 4))
        {
            offset1 = 0;
        }
        sendTimeMessage("Wave 'Nylocas boss' complete! Duration: ", deathTick-pillarsSpawnedTick+offset1,deathTick+offset1-bossSpawn, false);
        clog.write(NYLO_DESPAWNED, ""+(deathTick-pillarsSpawnedTick+offset1));
    }
}
