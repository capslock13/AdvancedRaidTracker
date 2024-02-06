package com.TheatreTracker.rooms;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.TheatreTrackerPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import com.TheatreTracker.utility.DataWriter;
import com.TheatreTracker.utility.RoomState;
import com.TheatreTracker.utility.nyloutility.NylocasData;
import com.TheatreTracker.utility.nyloutility.NylocasShell;
import com.TheatreTracker.utility.nyloutility.NylocasWave;
import com.TheatreTracker.utility.nyloutility.NylocasWaveMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.TheatreTracker.constants.LogID.*;
import static com.TheatreTracker.constants.NpcIDs.*;
import static com.TheatreTracker.utility.RoomState.NyloRoomState.*;

@Slf4j
public class NyloHandler extends RoomHandler
{
    public RoomState.NyloRoomState roomState;
    private final ArrayList<NylocasShell> buildWave;

    public NyloHandler(Client client, DataWriter clog, TheatreTrackerConfig config, TheatreTrackerPlugin plugin)
    {
        super(client, clog, config);
        this.plugin = plugin;
        buildWave = new ArrayList<>();
        nylosAlive = new ArrayList<>();
        roomState = NOT_STARTED;
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
    boolean story = false;
    private TheatreTrackerPlugin plugin;
    ArrayList<NPC> bigsDeadThisTick = new ArrayList<>();
    Map<Integer, String> bigDescription = new HashMap<>();

    public void reset()
    {
        roomState = NOT_STARTED;
        pillarsSpawnedTick = -1;
        instanceReference = -1;
        instanceStart = -1;
        bossSpawn = -1;
        lastDead = -1;
        accurateEntry = true;
        entryTickOffset = -1;
        hard = false;
        story = false;
        wave31 = -1;
        bigsDeadThisTick.clear();
        bigDescription.clear();
        super.reset();
    }

    public boolean isActive()
    {
        return !(roomState == NOT_STARTED || roomState == FINISHED);
    }

    public String getName()
    {
        return "Nylocas";
    }

    private int expectedWaveTick;

    public void updateGameTick(GameTick event)
    {
        bigsDeadThisTick.clear();
        if (!buildWave.isEmpty())
        {
            if (NylocasWaveMatcher.isWave(buildWave))
            {
                NylocasWave wave = NylocasWaveMatcher.getWave();
                if (wave.getWave() == 1)
                {
                    wave1Spawn();
                }
                if (wave.getWave() == 31)
                {
                    wave31Spawn();
                } else
                {
                    expectedWaveTick = client.getTickCount() + ((hard && wave.getWave() % 10 == 0) ? 16 : NylocasWave.waves[wave.getWave()].getDelay());
                    if (hard && (wave.getWave() % 10 == 0))
                    {
                        expectedWaveTick = client.getTickCount() + 16;
                    }
                }
                currentWave = wave.getWave();
                plugin.addLiveLine(2, client.getTickCount() - pillarsSpawnedTick, "W" + currentWave);
                clog.write(NYLO_WAVE, String.valueOf(currentWave), String.valueOf(client.getTickCount() - pillarsSpawnedTick));
            }
            buildWave.clear();
        }
        if (client.getTickCount() == expectedWaveTick && currentWave != 31)
        {
            clog.write(NYLO_STALL, "" + currentWave, "" + (client.getTickCount() - pillarsSpawnedTick), "" + nylosAlive.size());
            plugin.addLiveLine(2, client.getTickCount() - pillarsSpawnedTick, "Stall");
            expectedWaveTick += 4;
        }
    }

    public void updateNpcSpawned(NpcSpawned event)
    {
        switch (event.getNpc().getId())
        {
            case NYLO_MELEE_SMALL_HM:
            case NYLO_MELEE_SMALL_AGRO_HM:
            case NYLO_MELEE_BIG_HM:
            case NYLO_MELEE_BIG_AGRO_HM:
            case NYLO_RANGE_SMALL_HM:
            case NYLO_RANGE_SMALL_AGRO_HM:
            case NYLO_RANGE_BIG_HM:
            case NYLO_RANGE_BIG_AGRO_HM:
            case NYLO_MAGE_SMALL_HM:
            case NYLO_MAGE_SMALL_AGRO_HM:
            case NYLO_MAGE_BIG_HM:
            case NYLO_MAGE_BIG_AGRO_HM:
                if (!hard)
                    clog.write(IS_HARD_MODE);
                hard = true;
            case NYLO_MELEE_SMALL_SM:
            case NYLO_MELEE_SMALL_AGRO_SM:
            case NYLO_MELEE_BIG_SM:
            case NYLO_MELEE_BIG_AGRO_SM:
            case NYLO_RANGE_SMALL_SM:
            case NYLO_RANGE_SMALL_AGRO_SM:
            case NYLO_RANGE_BIG_SM:
            case NYLO_RANGE_BIG_AGRO_SM:
            case NYLO_MAGE_SMALL_SM:
            case NYLO_MAGE_SMALL_AGRO_SM:
            case NYLO_MAGE_BIG_SM:
            case NYLO_MAGE_BIG_AGRO_SM:
                if (!hard)
                {
                    if (!story)
                    {
                        clog.write(IS_STORY_MODE);
                    }
                }
                story = true;
            case NYLO_MELEE_BIG:
            case NYLO_MELEE_BIG_AGRO:
            case NYLO_RANGE_BIG:
            case NYLO_RANGE_BIG_AGRO:
            case NYLO_MAGE_BIG:
            case NYLO_MAGE_BIG_AGRO:
            case NYLO_MELEE_SMALL:
            case NYLO_MELEE_SMALL_AGRO:
            case NYLO_RANGE_SMALL:
            case NYLO_RANGE_SMALL_AGRO:
            case NYLO_MAGE_SMALL:
            case NYLO_MAGE_SMALL_AGRO:
                NylocasShell cShell = new NylocasShell(event.getNpc().getId(), event.getNpc().getWorldLocation().getRegionX(), event.getNpc().getWorldLocation().getRegionY());
                if (cShell.isBig())
                {
                    bigDescription.put(event.getNpc().getIndex(), "W" + (currentWave + 1) + " " + cShell.getDescription());
                }
                if (cShell.position != NylocasData.NyloPosition.ROOM)
                {
                    buildWave.add(cShell);
                    plugin.liveFrame.getPanel(getName()).addNPCMapping(event.getNpc().getIndex(), "W" + (currentWave + 1) + " " + cShell.getDescription());
                    clog.write(ADD_NPC_MAPPING, String.valueOf(event.getNpc().getIndex()), "W" + (currentWave + 1) + " " + cShell.getDescription());
                } else
                {
                    int matches = 0;
                    String lastMatchedDescription = "";
                    int lastMatchedIndex = -1;
                    for (NPC npc : bigsDeadThisTick)
                    {
                        int bigX = npc.getWorldLocation().getRegionX();
                        int bigY = npc.getWorldLocation().getRegionY();
                        int littleX = event.getNpc().getWorldLocation().getRegionX();
                        int littleY = event.getNpc().getWorldLocation().getRegionY();
                        if ((bigX == littleX && bigY == littleY) || (littleX - 1 == bigX || littleY - 1 == bigY))
                        {
                            matches++;
                            lastMatchedDescription = bigDescription.get(npc.getIndex());
                            lastMatchedIndex = npc.getIndex();
                            if (bigDescription.get(npc.getIndex()) == null)
                            {
                                log.info("Null for " + npc.getIndex());
                            }
                            log.info("matched big " + bigDescription.get(npc.getIndex()));
                        }
                    }
                    if (matches == 1)
                    {
                        clog.write(ADD_NPC_MAPPING, String.valueOf(event.getNpc().getIndex()), NylocasShell.getTypeName(event.getNpc().getId()) + " split from " + lastMatchedDescription + "(on w" + currentWave + ")");
                        plugin.liveFrame.getPanel(getName()).addNPCMapping(event.getNpc().getIndex(), NylocasShell.getTypeName(event.getNpc().getId()) + " split from " + lastMatchedDescription + "(on w" + currentWave + ")");
                    } else
                    {
                        log.info("could not match little on tick " + (client.getTickCount() - roomStartTick));
                    }
                    switch (event.getNpc().getId())
                    {
                        case NYLO_MELEE_SMALL:
                        case NYLO_MELEE_SMALL_AGRO:
                        case NYLO_MELEE_SMALL_HM:
                        case NYLO_MELEE_SMALL_AGRO_HM:
                        case NYLO_MELEE_SMALL_SM:
                        case NYLO_MELEE_SMALL_AGRO_SM:
                            clog.write(MELEE_SPLIT, "" + currentWave, "" + (client.getTickCount() - pillarsSpawnedTick));
                            break;
                        case NYLO_RANGE_SMALL:
                        case NYLO_RANGE_SMALL_AGRO:
                        case NYLO_RANGE_SMALL_HM:
                        case NYLO_RANGE_SMALL_AGRO_HM:
                        case NYLO_RANGE_SMALL_SM:
                        case NYLO_RANGE_SMALL_AGRO_SM:
                            clog.write(RANGE_SPLIT, "" + currentWave, "" + (client.getTickCount() - pillarsSpawnedTick));
                            break;
                        case NYLO_MAGE_SMALL:
                        case NYLO_MAGE_SMALL_AGRO:
                        case NYLO_MAGE_SMALL_HM:
                        case NYLO_MAGE_SMALL_AGRO_HM:
                        case NYLO_MAGE_SMALL_SM:
                        case NYLO_MAGE_SMALL_AGRO_SM:
                            clog.write(MAGE_SPLIT, "" + currentWave, "" + (client.getTickCount() - pillarsSpawnedTick));
                            break;
                    }
                }
                nylosAlive.add(event.getNpc());
                break;
            case NYLO_BOSS_MELEE:
            case NYLO_BOSS_RANGE:
            case NYLO_BOSS_MAGE:
            case NYLO_BOSS_MELEE_HM:
            case NYLO_BOSS_RANGE_HM:
            case NYLO_BOSS_MAGE_HM:
            case NYLO_BOSS_MELEE_SM:
            case NYLO_BOSS_RANGE_SM:
            case NYLO_BOSS_MAGE_SM:
                bossSpawned();
            case NYLO_PILLAR:
            case NYLO_PILLAR_HM:
            case NYLO_PILLAR_SM:
                if (pillarsSpawnedTick == -1)
                {
                    startNylo();
                    if (client.getNpcs().stream().anyMatch(p -> p.getName().toLowerCase().contains("nylo")))
                    {
                        accurateEntry = false;
                    } else
                    {
                        clog.write(ACCURATE_NYLO_START);
                    }
                }
                break;
            case NYLO_PRINKIPAS_DROPPING:
                break;
            case NYLO_PRINKIPAS_MELEE:
            case NYLO_PRINKIPAS_MAGIC:
            case NYLO_PRINKIPAS_RANGE:
                nylosAlive.add(event.getNpc());
                break;

        }
    }

    public void updateNpcDespawned(NpcDespawned event)
    {
        switch (event.getNpc().getId())
        {
            case NYLO_PILLAR:
            case NYLO_PILLAR_HM:
            case NYLO_PILLAR_SM:
                clog.write(NYLO_PILLAR_DESPAWNED, client.getTickCount() - pillarsSpawnedTick);
                break;
            case NYLO_BOSS_MELEE:
            case NYLO_BOSS_RANGE:
            case NYLO_BOSS_MAGE:
            case NYLO_BOSS_MELEE_HM:
            case NYLO_BOSS_RANGE_HM:
            case NYLO_BOSS_MAGE_HM:
            case NYLO_BOSS_MELEE_SM:
            case NYLO_BOSS_RANGE_SM:
            case NYLO_BOSS_MAGE_SM:
            {
                bossDefinitelyKilled();
            }
            break;
            case NYLO_MELEE_BIG:
            case NYLO_MELEE_BIG_AGRO:
            case NYLO_MAGE_BIG:
            case NYLO_MAGE_BIG_AGRO:
            case NYLO_RANGE_BIG:
            case NYLO_RANGE_BIG_AGRO:
            case NYLO_MELEE_BIG_HM:
            case NYLO_MELEE_BIG_AGRO_HM:
            case NYLO_RANGE_BIG_HM:
            case NYLO_RANGE_BIG_AGRO_HM:
            case NYLO_MAGE_BIG_HM:
            case NYLO_MAGE_BIG_AGRO_HM:
            case NYLO_MELEE_BIG_SM:
            case NYLO_MELEE_BIG_AGRO_SM:
            case NYLO_RANGE_BIG_SM:
            case NYLO_RANGE_BIG_AGRO_SM:
            case NYLO_MAGE_BIG_SM:
            case NYLO_MAGE_BIG_AGRO_SM:
                bigsDeadThisTick.add(event.getNpc());
            case NYLO_MELEE_SMALL_HM:
            case NYLO_MELEE_SMALL_AGRO_HM:
            case NYLO_RANGE_SMALL_HM:
            case NYLO_RANGE_SMALL_AGRO_HM:
            case NYLO_MAGE_SMALL_HM:
            case NYLO_MAGE_SMALL_AGRO_HM:
            case NYLO_MELEE_SMALL_SM:
            case NYLO_MELEE_SMALL_AGRO_SM:
            case NYLO_RANGE_SMALL_SM:
            case NYLO_RANGE_SMALL_AGRO_SM:
            case NYLO_MAGE_SMALL_SM:
            case NYLO_MAGE_SMALL_AGRO_SM:
            case NYLO_MELEE_SMALL:
            case NYLO_MELEE_SMALL_AGRO:
            case NYLO_RANGE_SMALL:
            case NYLO_RANGE_SMALL_AGRO:
            case NYLO_MAGE_SMALL:
            case NYLO_MAGE_SMALL_AGRO:
            case NYLO_PRINKIPAS_DROPPING:
            case NYLO_PRINKIPAS_MELEE:
            case NYLO_PRINKIPAS_MAGIC:
            case NYLO_PRINKIPAS_RANGE:
                nylosAlive.remove(event.getNpc());
                if (nylosAlive.isEmpty() && NylocasWaveMatcher.getWave().getWave() == 31)
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
     *
     * @param id ID of new form
     */
    public void handleNPCChanged(int id)
    {
        switch (id)
        {
            case NYLO_BOSS_MELEE:
            case NYLO_BOSS_MELEE_HM:
            case NYLO_BOSS_MELEE_SM:
                clog.write(MELEE_PHASE, "" + (client.getTickCount() - pillarsSpawnedTick));
                plugin.addLiveLine(2, client.getTickCount() - pillarsSpawnedTick, "Phase");
                break;
            case NYLO_BOSS_MAGE:
            case NYLO_BOSS_MAGE_HM:
            case NYLO_BOSS_MAGE_SM:
                clog.write(MAGE_PHASE, "" + (client.getTickCount() - pillarsSpawnedTick));
                plugin.addLiveLine(2, client.getTickCount() - pillarsSpawnedTick, "Phase");
                break;
            case NYLO_BOSS_RANGE:
            case NYLO_BOSS_RANGE_HM:
            case NYLO_BOSS_RANGE_SM:
                clog.write(RANGE_PHASE, "" + (client.getTickCount() - pillarsSpawnedTick));
                plugin.addLiveLine(2, client.getTickCount() - pillarsSpawnedTick, "Phase");
                break;
        }
    }

    private void startNylo()
    {
        clog.write(NYLO_PILLAR_SPAWN);
        roomState = WAVES;
        pillarsSpawnedTick = client.getTickCount();
        roomStartTick = client.getTickCount();
    }

    private void wave1Spawn()
    {
        instanceReference = client.getTickCount();
        entryTickOffset = (instanceReference - pillarsSpawnedTick - 4);
    }

    private void wave31Spawn()
    {
        clog.write(LAST_WAVE, "" + (client.getTickCount() - pillarsSpawnedTick));
        roomState = CLEANUP;
        int stalls = (client.getTickCount() - pillarsSpawnedTick - entryTickOffset - 236) / 4;
        wave31 = client.getTickCount();
        sendTimeMessage("Wave 'Nylocas last wave' complete! Duration: ", wave31 - pillarsSpawnedTick, " Stalls: ", stalls);
    }

    private void cleanupOver()
    {
        roomState = WAITING_FOR_BOSS;
        lastDead = client.getTickCount();
        int offsetTick = 4 - ((client.getTickCount() - instanceReference) % 4);
        if (offsetTick == 4)
        {
            offsetTick = 0;
        }
        clog.write(ACCURATE_NYLO_END);
        clog.write(36, "" + (lastDead - pillarsSpawnedTick)); //TODO huh?
        sendTimeMessage("Wave 'Nylocas waves and cleanup' complete! Duration: ", lastDead - pillarsSpawnedTick, lastDead - wave31);
    }

    private void bossSpawned()
    {
        clog.write(BOSS_SPAWN, "" + (client.getTickCount() - pillarsSpawnedTick));
        roomState = BOSS;
        bossSpawn = client.getTickCount() - 2;
        plugin.addLiveLine(2, client.getTickCount() - pillarsSpawnedTick - 2, "W" + currentWave);
        sendTimeMessage("Wave 'Nylocas boss spawn' complete! Duration: ", bossSpawn - pillarsSpawnedTick, bossSpawn - lastDead);
    }

    private void bossDefinitelyKilled()
    {
        roomState = FINISHED;
        int deathTick = client.getTickCount();
        int offset1 = 4 - ((deathTick - instanceReference) % 4);
        if ((4 - ((deathTick - instanceReference) % 4) == 4))
        {
            offset1 = 0;
        }
        plugin.liveFrame.setNyloFinished();
        sendTimeMessage("Wave 'Nylocas boss' complete! Duration: ", deathTick - pillarsSpawnedTick + offset1, deathTick + offset1 - bossSpawn, false);
        clog.write(NYLO_DESPAWNED, "" + (deathTick - pillarsSpawnedTick + offset1));
    }
}
