package com.TheatreTracker.rooms;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.constants.NpcIDs;
import com.TheatreTracker.utility.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.HitsplatID;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import com.TheatreTracker.TheatreTrackerPlugin;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.TheatreTracker.constants.LogID.*;
import static com.TheatreTracker.constants.NpcIDs.*;

@Slf4j
public class MaidenHandler extends RoomHandler
{
    public RoomState.MaidenRoomState roomState;

    int maidenStartTick;
    int p70;
    int p50;
    int p30;
    int maidenDeathTick;
    NPC maidenNPC;
    int bloodHeals = 0;

    public int deferVarbitCheck = -1;
    ArrayList<MaidenCrab> maidenCrabs = new ArrayList<>();

    ArrayList<PlayerHitsWrapper> hitsplatsPerPlayer;
    ArrayList<PlayerHitsWrapper> ignoredHitsPerPlayer;
    ArrayList<PlayerDamageQueueItem> queuedDamage;
    ArrayList<BloodPositionWrapper> thrownBloodLocations;
    ArrayList<Player> playersWithCastAnimation;
    ArrayList<Player> playersWithCrossbowAnimation;
    ArrayList<WorldPoint> zcbProjectileOrigins;
    ArrayList<WorldPoint> spawnedBloodLocations;
    ArrayList<Integer> maidenHeals;
    ArrayList<BloodDamageToBeApplied> queuedBloodDamage;


    public MaidenHandler(Client client, DataWriter clog, TheatreTrackerConfig config)
    {
        super(client, clog, config);
        p70 = -1;
        p50 = -1;
        p30 = -1;
        maidenStartTick = -1;
        maidenDeathTick = -1;
        accurateEntry = true;
        hitsplatsPerPlayer = new ArrayList<>();
        ignoredHitsPerPlayer = new ArrayList<>();
        queuedDamage = new ArrayList<>();
        thrownBloodLocations = new ArrayList<>();
        spawnedBloodLocations = new ArrayList<>();
        playersWithCastAnimation = new ArrayList<>();
        playersWithCrossbowAnimation = new ArrayList<>();
        zcbProjectileOrigins = new ArrayList<>();
        maidenHeals = new ArrayList<>();
        queuedBloodDamage = new ArrayList<>();
    }

    public void reset()
    {
        accurateEntry = true;
        p70 = -1;
        p50 = -1;
        p30 = -1;
        maidenStartTick = -1;
        maidenDeathTick = -1;
        hitsplatsPerPlayer.clear();
        queuedDamage.clear();
        ignoredHitsPerPlayer.clear();
        queuedDamage.clear();
        thrownBloodLocations.clear();
        playersWithCastAnimation.clear();
        playersWithCrossbowAnimation.clear();
        maidenHeals.clear();
        queuedBloodDamage.clear();
        bloodHeals = 0;
    }

    public void startMaiden()
    {
        maidenStartTick = client.getTickCount();
        deferVarbitCheck = maidenStartTick+2;
    }

    public void proc70()
    {
        p70 = super.client.getTickCount();
        roomState = RoomState.MaidenRoomState.PHASE_2;
        if(maidenStartTick != -1)
            sendTimeMessage("Wave 'Maiden phase 1' complete! Duration: ", p70-maidenStartTick);
        clog.write(MAIDEN_70S, ""+(p70-maidenStartTick));

    }

    public void proc50()
    {
        p50 = super.client.getTickCount();
        roomState = RoomState.MaidenRoomState.PHASE_3;
        if(maidenStartTick != -1)
            sendTimeMessage("Wave 'Maiden phase 2' complete! Duration: ", p50-maidenStartTick, p50-p70);
        clog.write(MAIDEN_50S, ""+(p50-maidenStartTick));
    }

    public void proc30()
    {
        p30 = super.client.getTickCount();
        roomState = RoomState.MaidenRoomState.PHASE_4;
        if(maidenStartTick != -1)
            sendTimeMessage("Wave 'Maiden phase 3' complete! Duration: ", p30-maidenStartTick, p30-p50);
        clog.write(MAIDEN_30S, ""+(p30-maidenStartTick));
    }

    public void endMaiden()
    {
        roomState = RoomState.MaidenRoomState.FINISHED;
        maidenDeathTick = client.getTickCount()+7;
        if(maidenStartTick != -1)
            sendTimeMessage("Wave 'Maiden Skip' complete! Duration: ", maidenDeathTick-maidenStartTick,maidenDeathTick-p30, false);
        clog.write(301);
        clog.write(MAIDEN_0HP, ""+(client.getTickCount()-maidenStartTick));
    }


    public void updateAnimationChanged(AnimationChanged event)
    {
        if(event.getActor().getAnimation() == 8093)
        {
            endMaiden();
        }
        if(event.getActor() instanceof Player)
        {
            if(event.getActor().getAnimation() == 4411)
            {
                playersWithCastAnimation.add((Player) event.getActor());
            }
            else if(event.getActor().getAnimation() == 9168)
            {
                playersWithCrossbowAnimation.add((Player)event.getActor());
            }
        }
    }

    public void updateGraphicChanged(GraphicChanged event)
    {
        if(event.getActor() instanceof Player)
        {
            if(event.getActor().hasSpotAnim(560)) //divine
            {
                queuedDamage.add(new PlayerDamageQueueItem(client.getTickCount(), event.getActor().getName()));
            }
        }
    }

    public void updateNpcDespawned(NpcDespawned event)
    {
        NPC npc = event.getNpc();
        switch(npc.getId())
        {
            case NpcIDs.MAIDEN_P0:
            case NpcIDs.MAIDEN_P1:
            case NpcIDs.MAIDEN_P2:
            case NpcIDs.MAIDEN_P3:
            case NpcIDs.MAIDEN_PRE_DEAD:
            case NpcIDs.MAIDEN_DEAD:
            case NpcIDs.MAIDEN_P0_HM:
            case NpcIDs.MAIDEN_P1_HM:
            case NpcIDs.MAIDEN_P2_HM:
            case NpcIDs.MAIDEN_P3_HM:
            case NpcIDs.MAIDEN_PRE_DEAD_HM:
            case NpcIDs.MAIDEN_DEAD_HM:
            case NpcIDs.MAIDEN_P0_SM:
            case NpcIDs.MAIDEN_P1_SM:
            case NpcIDs.MAIDEN_P2_SM:
            case NpcIDs.MAIDEN_P3_SM:
            case NpcIDs.MAIDEN_PRE_DEAD_SM:
            case NpcIDs.MAIDEN_DEAD_SM:
                clog.write(MAIDEN_DESPAWNED, ""+(client.getTickCount()-maidenStartTick));
                break;
            case MAIDEN_MATOMENOS:
            case MAIDEN_MATOMENOS_HM:
            case MAIDEN_MATOMENOS_SM:
                maidenCrabs.removeIf(x -> x.crab.equals(event.getNpc()));
            default:
                break;
        }
    }

    public void updateNpcSpawned(NpcSpawned event)
    {
        NPC npc = event.getNpc();
        boolean story = false;
        switch(npc.getId())
        {
            case MAIDEN_P0_SM:
            case MAIDEN_P1_SM:
            case MAIDEN_P2_SM:
            case MAIDEN_P3_SM:
            case MAIDEN_PRE_DEAD_SM:
            case MAIDEN_DEAD_SM:
                story = true;
                clog.write(IS_STORY_MODE);
            case MAIDEN_P0_HM:
            case MAIDEN_P1_HM:
            case MAIDEN_P2_HM:
            case MAIDEN_P3_HM:
            case MAIDEN_PRE_DEAD_HM:
            case MAIDEN_DEAD_HM:
                if(!story)
                    clog.write(IS_HARD_MODE);
            case MAIDEN_P0:
            case MAIDEN_P1:
            case MAIDEN_P2:
            case MAIDEN_P3:
            case MAIDEN_DEAD:
                clog.write(MAIDEN_SPAWNED);
                maidenNPC = npc;
                startMaiden();
                break;
            case MAIDEN_MATOMENOS:
            case MAIDEN_MATOMENOS_HM:
            case MAIDEN_MATOMENOS_SM:
                MaidenCrab crab = new MaidenCrab(npc, TheatreTrackerPlugin.scale, identifySpawn(npc));
                logCrabSpawn(crab.description);
                maidenCrabs.add(crab);
                break;
            case NpcIDs.MAIDEN_BLOOD:
            case NpcIDs.MAIDEN_BLOOD_HM:
            case NpcIDs.MAIDEN_BLOOD_SM:
                clog.write(BLOOD_SPAWNED);
                break;
        }
    }

    public void handleNPCChanged(int id)
    {
        switch(id)
        {
            case MAIDEN_P1:
            case MAIDEN_P1_HM:
            case MAIDEN_P1_SM:
                proc70();
                break;
            case MAIDEN_P2:
            case MAIDEN_P2_HM:
            case MAIDEN_P2_SM:
                proc50();
                break;
            case MAIDEN_P3:
            case MAIDEN_P3_HM:
            case MAIDEN_P3_SM:
                proc30();
                break;
            case MAIDEN_DEAD:
            case MAIDEN_DEAD_HM:
            case MAIDEN_DEAD_SM:
                break;
        }
    }

    private void logCrabSpawn(String description) {
        clog.write(MATOMENOS_SPAWNED, description);
    }

    private void applyBlood()
    {
        for(BloodDamageToBeApplied p : queuedBloodDamage)
        {
            int bloodDamage = -1;
            Optional<PlayerHitsWrapper> hits = hitsplatsPerPlayer.stream().filter(playerHitsWrapper -> playerHitsWrapper.name.equals(p.playerName)).findAny();
            if(hits.isPresent())
            {
                Optional<PlayerHitsWrapper> ignoredHits = ignoredHitsPerPlayer.stream().filter(playerHitsWrapper -> playerHitsWrapper.name.equals(p.playerName)).findAny();
                if(ignoredHits.isPresent())
                {
                    if(hits.get().hitsplats.size() > ignoredHits.get().hits)
                    {
                        bloodDamage = hits.get().hitsplats.get(ignoredHits.get().hits);
                    }
                    else
                    {
                        //log.info("Wrong amount of hits on " + p.playerName);
                    }
                }
                else
                {
                    bloodDamage = hits.get().hitsplats.get(0);
                }
            }
            else
            {
               // log.info(p.playerName + " had no hitsplats but was in blood");
            }
            if(bloodDamage != -1)
            {
                if(p.bloodTicksAlive == -1)
                {
                    //log.info("Spawned: " + p.playerName + " stood in blood for " + bloodDamage + " damage. Blood existed for: " + p.bloodTicksAlive + " ticks. Client ticks: " + client.getTickCount());
                    clog.write(PLAYER_STOOD_IN_SPAWNED_BLOOD, p.playerName, String.valueOf(bloodDamage)); //player, dmg
                    bloodHeals++;
                }
                else
                {
                    //log.info("Thrown: " + p.playerName + " stood in blood for " + bloodDamage + " damage. Blood existed for: " + p.bloodTicksAlive + " ticks. Client ticks: " + client.getTickCount());
                    clog.write(PLAYER_STOOD_IN_THROWN_BLOOD, p.playerName, String.valueOf(bloodDamage), String.valueOf(p.bloodTicksAlive)); //player, dmg, blood tick
                    bloodHeals++;
                }
            }
        }
    }
    private void assessBloodForNextTick()
    {
        queuedBloodDamage.clear();
        for(Player p : client.getPlayers())
        {
            for(BloodPositionWrapper blood : thrownBloodLocations)
            {
                if(blood.initialTick <= client.getTickCount() && p.getWorldLocation().distanceTo(blood.location) == 0)
                {
                    queuedBloodDamage.add(new BloodDamageToBeApplied(p.getName(), 10-(blood.finalTick-client.getTickCount())));
                }
            }
            for(WorldPoint blood : spawnedBloodLocations)
            {
                if(p.getWorldLocation().distanceTo(blood) == 0)
                {
                   queuedBloodDamage.add(new BloodDamageToBeApplied(p.getName(), -1));
                }
            }
        }

        thrownBloodLocations.removeIf(bloodPositionWrapper -> bloodPositionWrapper.finalTick <= client.getTickCount());
    }

    private void generateHitsplats()
    {
        for(PlayerDamageQueueItem item : queuedDamage)
        {
            if(item.arrivalTick == client.getTickCount())
            {
                Optional<PlayerHitsWrapper> playerHits = ignoredHitsPerPlayer.stream().filter(playerHitsWrapper -> playerHitsWrapper.name.equals(item.playerName)).findAny();
                if(playerHits.isPresent())
                {
                    playerHits.get().hits++;
                }
                else
                {
                    ignoredHitsPerPlayer.add(new PlayerHitsWrapper(item.playerName));
                }
            }
        }
    }

    private void handleCasts()
    {
        for(Player p : playersWithCastAnimation)
        {
            if (p.getInteracting() != null)
            {
                if(p.getInteracting().hasSpotAnim(736))
                {
                    queuedDamage.add(new PlayerDamageQueueItem(client.getTickCount() + 1, p.getName()));
                }
            }
        }
        playersWithCastAnimation.clear();
    }

    private void handleZCB()
    {
        for(WorldPoint zcbLocation : zcbProjectileOrigins)
        {
            for(Player p : playersWithCrossbowAnimation)
            {
                if(p.getWorldLocation().distanceTo(zcbLocation) == 0)
                {
                    queuedDamage.add(new PlayerDamageQueueItem(client.getTickCount()+1, p.getName()));
                }
            }
        }
        playersWithCrossbowAnimation.clear();
        zcbProjectileOrigins.clear();
    }

    private void handleCrabHeals()
    {
        for(int i = bloodHeals; i < maidenHeals.size(); i++)
        {
            clog.write(CRAB_HEALED_MAIDEN, String.valueOf(maidenHeals.get(i)));
           // log.info("Crab healed maiden for: " + maidenHeals.get(i));
        }
    }

    public void updateGameTick(GameTick event) //TODO: Blood dmg is 1t after being in blood
    {
        handleCasts();
        handleZCB();
        generateHitsplats();
        applyBlood();
        handleCrabHeals();
        assessBloodForNextTick();
        hitsplatsPerPlayer.clear();
        ignoredHitsPerPlayer.clear();
        maidenHeals.clear();

        if(client.getTickCount() == deferVarbitCheck)
        {
            deferVarbitCheck = -1;
            if(client.getVarbitValue(HP_VARBIT) != FULL_HP)
            {
                accurateEntry = false;
            }
            else
            {
                accurateEntry = true;
                roomState = RoomState.MaidenRoomState.PHASE_1;
                clog.write(ACCURATE_MAIDEN_START);
            }
        }
        // Check for crabs that are leaking on this game tick
        List<MaidenCrab> leaked_crabs= maidenCrabs.stream().filter(crab -> (crab.getCrab().getWorldArea().distanceTo2D(maidenNPC.getWorldArea()) - 1 == 0) && (crab.health > 0)).collect(Collectors.toList());
        for (MaidenCrab crab: leaked_crabs) {
            { // TODO replace with distance method in MaidenCrab
                clog.write(CRAB_LEAK, crab.description, String.valueOf(crab.health));
                // TODO add mising parameters (room time, current maiden health)
                // TODO check what happens if someone hits a crab on the last tick possible - is hp correct? does sthis
            }
        }
        maidenCrabs.removeAll(leaked_crabs);
    }

    /**
     * Tracks crab hps
     */
    public void updateHitsplatApplied(HitsplatApplied event)
    {
        if (maidenCrabs.stream().map(x -> x.crab).collect(Collectors.toList()).contains(event.getActor()))
        {
            MaidenCrab crab = maidenCrabs.stream().filter(x -> x.crab.equals(event.getActor())).collect(Collectors.toList()).get(0);
            crab.health -= event.getHitsplat().getAmount();
        }
        if(event.getActor() instanceof Player) //Heal tracking
        {
            //log.info("Damage done to player: " + event.getHitsplat().getAmount() + ", " + client.getTickCount());
            if(hitsplatsPerPlayer.stream().noneMatch(playerHPWrapper -> playerHPWrapper.name.equals(event.getActor().getName())))
            {
                hitsplatsPerPlayer.add(new PlayerHitsWrapper(event.getActor().getName(), event.getHitsplat().getAmount()));
            }
            else
            {
                for(int i = 0; i < hitsplatsPerPlayer.size(); i++)
                {
                    if(hitsplatsPerPlayer.get(i).name.equals(event.getActor().getName()))
                    {
                        hitsplatsPerPlayer.get(i).hitsplats.add(event.getHitsplat().getAmount());
                    }
                }
            }
        }
        else if(event.getActor().getName() != null && event.getActor().getName().contains("Maiden"))
        {
            if(event.getHitsplat().getHitsplatType() == HitsplatID.HEAL)
            {
                maidenHeals.add(event.getHitsplat().getAmount());
            }
        }
    }

    public void updateGameObjectSpawned(GameObjectSpawned event)
    {
        if(event.getGameObject().getId() == 32984)
        {
            spawnedBloodLocations.add(event.getGameObject().getWorldLocation());
        }
    }

    public void updateGameObjectDespawned(GameObjectDespawned event)
    {
        if(event.getGameObject().getId() == 32984)
        {
            spawnedBloodLocations.removeIf(worldPoint -> worldPoint.getRegionX() == event.getGameObject().getWorldLocation().getRegionX() &&
                    worldPoint.getRegionY() == event.getGameObject().getWorldLocation().getRegionY());

        }
    }

    public void updateGraphicsObjectCreated(GraphicsObjectCreated event)
    {
        if(event.getGraphicsObject().getId() == MAIDEN_THROWN_BLOOD_GRAPHIC_OBJECT)
        {
            //log.info(event.getGraphicsObject().getStartCycle() + ", " + client.getGameCycle() + ", " + (event.getGraphicsObject().getStartCycle()-client.getGameCycle()+1) + ", " + (((event.getGraphicsObject().getStartCycle()-client.getGameCycle()+1)/30)) + " , tick " + client.getTickCount());

            thrownBloodLocations.add(new BloodPositionWrapper(WorldPoint.fromLocal(client, event.getGraphicsObject().getLocation()), ((((event.getGraphicsObject().getStartCycle()-client.getGameCycle()+1)/30))+client.getTickCount())));
        }
    }
    public void updateProjectileMoved(ProjectileMoved event)
    {
        if(event.getProjectile().getStartCycle() == client.getGameCycle())
        {
            if(event.getProjectile().getId() == 1995)
            {
                zcbProjectileOrigins.add(WorldPoint.fromLocal(client, new LocalPoint(event.getProjectile().getX1(), event.getProjectile().getY1())));
            }
        }
    }

    /**
     * Returns a string describing the spawn position of a maiden crab
     * @param npc
     * @return
     */
    private String identifySpawn(NPC npc)
    {
        int x = npc.getWorldLocation().getRegionX();
        int y = npc.getWorldLocation().getRegionY();
        String proc = "";
        if(x == 21 && y == 40)
        {
            return "N1";
        }
        if(x == 22 && y == 41)
        {
            clog.write(MAIDEN_SCUFFED, "N1");
            return "N1";
        }
        if(x == 25 && y == 40)
        {
            return "N2";
        }
        if(x == 26 && y == 41)
        {
            clog.write(MAIDEN_SCUFFED, "N2");
            return "N2";
        }
        if(x == 29 && y == 40)
        {
            return "N3";
        }
        if(x == 30 && y == 41)
        {
            clog.write(MAIDEN_SCUFFED, "N3");
            return "N3";
        }
        if(x == 33 && y == 40)
        {
            return "N4 (1)";
        }
        if(x == 34 && y == 41)
        {
            clog.write(MAIDEN_SCUFFED, "N4 (1)");
            return "N4 (1)";
        }
        if(x == 33 && y == 38)
        {
            return "N4 (2)";
        }
        if(x == 34 && y == 39)
        {
            clog.write(MAIDEN_SCUFFED, "N4 (2)");
            return "N4 (2)";
        }
        //
        if(x == 21 && y == 20)
        {
            return "S1";
        }
        if(x == 22 && y == 19)
        {
            clog.write(MAIDEN_SCUFFED, "S1");
            return "S1";
        }
        if(x == 25 && y == 20)
        {
            return "S2";
        }
        if(x == 26 && y == 19)
        {
            clog.write(MAIDEN_SCUFFED, "S2");
            return "S2";
        }
        if(x == 29 && y == 20)
        {
            return "S3";
        }
        if(x == 30 && y == 19)
        {
            clog.write(MAIDEN_SCUFFED, "S3");
            return "S3";
        }
        if(x == 33 && y == 20)
        {
            return "S4 (1)";
        }
        if(x == 34 && y == 19)
        {
            clog.write(MAIDEN_SCUFFED, "S4 (1)");
            return "S4 (1)";
        }
        if(x == 33 && y == 22)
        {
            return "S4 (2)";
        }
        if(x == 34 && y == 20)
        {
            clog.write(MAIDEN_SCUFFED, "S4 (2)");
            return "S4 (2)";
        }
        else throw new InvalidParameterException("Impossible crab spawn data at maiden");
    }

    private class MaidenCrab
    {
        @Getter
        NPC crab;
        int maxHealth;
        int health;
        String description;

        public MaidenCrab(NPC crab, int scale, String description)
        {
            switch (scale)
            {
                case 5: maxHealth = 100; break;
                case 4: maxHealth = 87; break;
                default: maxHealth = 75; break;
            }
            this.crab = crab;
            health = maxHealth;
            this.description = description;
        }
    }
}
