package com.TheatreTracker.rooms;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.TheatreTrackerPlugin;
import com.TheatreTracker.constants.LogID;
import com.TheatreTracker.constants.NpcIDs;
import com.TheatreTracker.utility.DataWriter;
import com.TheatreTracker.utility.PlayerDidAttack;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import com.TheatreTracker.utility.RoomState;
import net.runelite.api.kit.KitType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.TheatreTracker.constants.LogID.*;
import static com.TheatreTracker.constants.NpcIDs.*;

@Slf4j
public class VerzikHandler extends RoomHandler {
    public RoomState.VerzikRoomState roomState;
    private TheatreTrackerPlugin plugin;
    private int healingEndTick = -1;

    public VerzikHandler(Client client, DataWriter clog, TheatreTrackerConfig config, TheatreTrackerPlugin plugin)
    {
        super(client, clog, config);
        this.plugin = plugin;
        currentHits = new ArrayList<>();
        lastHits = new ArrayList<>();
        roomState = RoomState.VerzikRoomState.NOT_STARTED;
    }

    public boolean isActive()
    {
        return !(roomState == RoomState.VerzikRoomState.NOT_STARTED || roomState == RoomState.VerzikRoomState.FINISHED);
    }

    public String getName()
    {
        return "Verzik";
    }

    private int verzikEntryTick = -1;
    private int verzikP1EndTick = -1;
    private int verzikRedsTick = -1;
    private int verzikP2EndTick = -1;
    private int verzikP3EndTick = -1;
    private boolean redsThisTick = false;

    private boolean hasWebbed = false;
    private int webTick = -1;

    private ArrayList<Integer> currentHits;
    private ArrayList<Integer> lastHits;
    private NPC verzNPC;
    Map<Integer, Integer> shieldActives = new HashMap<>();

    public void reset()
    {
        super.reset();
        roomState = RoomState.VerzikRoomState.NOT_STARTED;
        currentHits.clear();
        lastHits.clear();
        verzikEntryTick = -1;
        verzikP1EndTick = -1;
        verzikRedsTick = -1;
        verzikP2EndTick = -1;
        verzikP3EndTick = -1;
        redsThisTick = false;
        hasWebbed = false;
        healingEndTick = -1;
        webTick = -1;
        queuedAutoHits.clear();
        shieldActives.clear();
    }

    public void thrallAttackedShield(int tick)
    {

    }

    public void updateGameTick(GameTick event)
    {
        for(Projectile projectile : client.getProjectiles())
        {
            if (projectile.getId() == 1587)
            {
                log.info("Expecting purple heal on tick: " + client.getTickCount());
            }
        }
        if(client.getTickCount() == healingEndTick)
        {
            plugin.verzShieldActive = false;
        }
        int playersHit = 1;
        for(Player p : queuedAutoHits.keySet())
        {
            if(queuedAutoHits.get(p) == client.getTickCount())
            {
                for(Player p2 : client.getPlayers())
                {
                    if(p2.getWorldLocation().distanceTo(p.getWorldLocation()) <= 1)
                    {
                        log.info(p2.getName() + " is also in target of verz auto AOE");
                        playersHit++;
                    }
                }
            }
        }
        queuedAutoHits.clear();
        for(Integer heal : currentHits)
        {
            log.info("Heal: " + heal);
        }
        currentHits.clear();
        log.info("End tick " + client.getTickCount());
        if(healingEndTick == client.getTickCount())
        {
            healingEndTick = -1;
        }
        redsThisTick = false;
        if(roomState == RoomState.VerzikRoomState.PHASE_1)
        {
            for(Projectile projectile : client.getProjectiles())
            {
                if(projectile.getId() == 1547 || projectile.getId() == 1544)
                {
                }
            }
        }
        if(webTick != -1)
        {
            webTick++;
            if(webTick > 50)
            {
                hasWebbed = false;
                webTick = -1;
            }
        }
        if (lastHits.size() != 0)
        {

        }
        lastHits = currentHits;
    }

    private Map<Player, Integer> queuedAutoHits = new HashMap<>();

    public void updateProjectileMoved(ProjectileMoved event)
    {
        if (event.getProjectile().getId() == 1587)
        {
            if (event.getProjectile().getRemainingCycles() == client.getGameCycle())
            {
                //log.info("Expecting red heal on tick " + client.getTickCount());
            }
        }
        if (event.getProjectile().getId() == 1591)
        {
            if (event.getProjectile().getRemainingCycles() == 0)
            {
                log.info("Expecting auto heal on tick " + (client.getTickCount()+2));
                if(verzNPC.getInteracting() instanceof Player)
                {
                    Player p = (Player) verzNPC;
                    log.info("Verz targeting: " + p.getName());
                    log.info("");
                    queuedAutoHits.put(p, client.getTickCount()+2);
                }
            }
        }
        else if(event.getProjectile().getId() == 1601)
        {
            if(!hasWebbed)
            {
                hasWebbed = true;
                clog.write(WEBS_STARTED, String.valueOf(client.getTickCount()-verzikEntryTick));
                webTick = client.getTickCount();
                if((webTick-verzikEntryTick) % 2 == 0)
                {
                    plugin.addLiveLine(5, webTick - verzikEntryTick, "Webs");
                }
            }
        }
    }

    public void updateHitsplatApplied(HitsplatApplied event)
    {
        if (event.getActor().getName() != null)
        {
            if (event.getActor().getName().contains("Verzik") && event.getHitsplat().getHitsplatType() == HitsplatID.HEAL)
            {
                currentHits.add(event.getHitsplat().getAmount());
            }
            if(roomState == RoomState.VerzikRoomState.PHASE_1 && event.getActor().getName().contains("Verzik"))
            {
                if(event.getHitsplat().getAmount() > 74)
                {
                    log.info("dawn hit on tick " + client.getTickCount());
                    clog.write(DAWN_DAMAGE, String.valueOf(event.getHitsplat().getAmount()), String.valueOf(client.getTickCount()-roomStartTick));
                }
            }
        }
    }

    public void updateGraphicChanged(GraphicChanged event) {
        if (event.getActor().hasSpotAnim(VERZIK_BOUNCE_SPOT_ANIMATION))
        {
            clog.write(LogID.VERZIK_BOUNCE, event.getActor().getName(), String.valueOf(client.getTickCount()-verzikEntryTick));
            plugin.liveFrame.addAttack(new PlayerDidAttack(event.getActor().getName(), "100000", client.getTickCount()-verzikEntryTick, "-1","-1","-1",-1,-1), "Verzik");

        }
    }

    public void updateItemSpawned(ItemSpawned event)
    {
        if(event.getItem().getId() == 22516)
        {
            clog.write(DAWN_DROPPED, client.getTickCount()-verzikEntryTick);
        }
    }


    public void updateAnimationChanged(AnimationChanged event)
    {
        int id = event.getActor().getAnimation();
        if(roomState == RoomState.VerzikRoomState.PHASE_2 || roomState == RoomState.VerzikRoomState.PHASE_2_REDS)
        {
            if(plugin.verzShieldActive)
            {
                if(event.getActor() instanceof Player)
                {
                    Player p = (Player) event.getActor();
                    if(p.getInteracting() instanceof NPC)
                    {
                        NPC interacting = (NPC) p.getInteracting();
                        if(interacting.getId() == VERZIK_P2 || interacting.getId() == VERZIK_P2_HM || interacting.getId() == VERZIK_P2_SM)
                        {
                            switch(p.getAnimation())
                            {
                                case 8056:
                                    log.info("expecting 3 heals from scy by " + p.getName() + " on tick " + (client.getTickCount()+1));
                                    break;
                            }
                        }
                    }
                }
            }
        }

        if (event.getActor().getAnimation() == VERZIK_BECOMES_SPIDER)
        {
            endP3();
        }
    }

    public void updateNpcSpawned(NpcSpawned event) {
        int id = event.getNpc().getId();
        if (id == VERZIK_MATOMENOS || id == VERZIK_MATOMENOS_HM || id == VERZIK_MATOMENOS_SM)
        {
            if(!redsThisTick)
            {
                clog.write(VERZIK_P2_REDS_PROC, (client.getTickCount() - verzikEntryTick) + "");
                plugin.addLiveLine(5, client.getTickCount()-verzikEntryTick, "Reds");
                healingEndTick = client.getTickCount()+11;
                plugin.addLiveLine(5, healingEndTick-verzikEntryTick, "Shield End");
                redsThisTick = true;
                plugin.verzShieldActive = true;
            }
            if (roomState != RoomState.VerzikRoomState.PHASE_2_REDS)
            {
                procReds();
            }
        }
        switch (id)
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
                clog.write(VERZIK_CRAB_SPAWNED, client.getTickCount()-roomStartTick);
                break;
            case VERZIK_P1_INACTIVE:
            case VERZIK_P1_INACTIVE_SM:
            case VERZIK_P1_INACTIVE_HM:
            case VERZIK_P1:
            case VERZIK_P2:
            case VERZIK_P3:
            case VERZIK_P1_HM:
            case VERZIK_P2_HM:
            case VERZIK_P3_HM:
            case VERZIK_P1_SM:
            case VERZIK_P2_SM:
            case VERZIK_P3_SM:
                verzNPC = event.getNpc();
                break;
        }
    }

    public void updateNpcDespawned(NpcDespawned event) {
        int id = event.getNpc().getId();
        if (id == VERZIK_P1 || id == VERZIK_P1_HM || id == VERZIK_P1_SM) {
            endP1();
        } else if (id == VERZIK_P2 || id == VERZIK_P2_HM || id == VERZIK_P2_SM) {
            endP2();
        }
    }

    public void handleNPCChanged(int id)
    {
        if (id == VERZIK_P1 || id == VERZIK_P1_HM || id == VERZIK_P1_SM)
        {
            if (id == VERZIK_P1_HM)
            {
                clog.write(IS_HARD_MODE);
            } else if (id == VERZIK_P1_SM)
            {
                clog.write(IS_STORY_MODE);
            }
            startVerzik();
        } else if (id == VERZIK_P2 || id == VERZIK_P2_HM || id == VERZIK_P2_SM) {
            endP1();
        } else if (id == VERZIK_P3 || id == VERZIK_P3_HM || id == VERZIK_P3_SM) {
            endP2();
        } else if (id == VERZIK_DEAD || id == VERZIK_DEAD_HM || id == VERZIK_DEAD_SM) {
            endP3();
        }
    }

    private void startVerzik()
    {
        roomState = RoomState.VerzikRoomState.PHASE_1;
        verzikEntryTick = client.getTickCount();
        clog.write(VERZIK_P1_START);
        clog.write(ACCURATE_VERZIK_START);
        roomStartTick = client.getTickCount();
    }

    private void endP1() {
        roomState = RoomState.VerzikRoomState.PHASE_2;
        verzikP1EndTick = client.getTickCount();
        sendTimeMessage("Wave 'Verzik phase 1' complete. Duration: ", verzikP1EndTick - verzikEntryTick);
        clog.write(VERZIK_P1_DESPAWNED, (verzikP1EndTick - verzikEntryTick) + "");
        plugin.addLiveLine(5, verzikP1EndTick-verzikEntryTick, "P1 End");

    }

    private void procReds() {
        roomState = RoomState.VerzikRoomState.PHASE_2_REDS;
        verzikRedsTick = client.getTickCount();
        sendTimeMessage("Red Crabs Spawned. Duration: ", verzikRedsTick - verzikEntryTick);
    }

    private void endP2() {
        roomState = RoomState.VerzikRoomState.PHASE_3;
        verzikP2EndTick = client.getTickCount();
        sendTimeMessage("Wave 'Verzik phase 2' complete. Duration: ", verzikP2EndTick - verzikEntryTick, verzikP2EndTick - verzikP1EndTick);
        clog.write(VERZIK_P2_END, (verzikP2EndTick - verzikEntryTick) + "");
        plugin.addLiveLine(5, verzikP2EndTick-verzikEntryTick, "P2 End");

    }

    private void endP3()
    {
        roomState = RoomState.VerzikRoomState.FINISHED;
        verzikP3EndTick = client.getTickCount() + 6;
        clog.write(ACCURATE_VERZIK_END);
        sendTimeMessage("Wave 'Verzik phase 3' complete. Duration: ", verzikP3EndTick - verzikEntryTick, verzikP3EndTick - verzikP2EndTick);
        clog.write(VERZIK_P3_DESPAWNED, (verzikP3EndTick - verzikEntryTick) + "");
        plugin.addLiveLine(5, client.getTickCount()-verzikEntryTick, "Dead");
        plugin.liveFrame.setVerzFinished();

    }
}
