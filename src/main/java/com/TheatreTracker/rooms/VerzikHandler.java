package com.TheatreTracker.rooms;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.constants.LogID;
import com.TheatreTracker.utility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import com.TheatreTracker.utility.RoomState;
import net.runelite.api.kit.KitType;

import java.util.ArrayList;

import static com.TheatreTracker.constants.LogID.*;
import static com.TheatreTracker.constants.NpcIDs.*;

@Slf4j
public class VerzikHandler extends RoomHandler {
    public RoomState.VerzikRoomState roomState;

    public VerzikHandler(Client client, DataWriter clog, TheatreTrackerConfig config) {
        super(client, clog, config);
        currentHits = new ArrayList<>();
        lastHits = new ArrayList<>();
    }

    private int verzikEntryTick = -1;
    private int verzikP1EndTick = -1;
    private int verzikRedsTick = -1;
    private int verzikP2EndTick = -1;
    private int verzikP3EndTick = -1;

    private boolean hasWebbed = false;
    private int webTick = -1;

    private ArrayList<Integer> currentHits;
    private ArrayList<Integer> lastHits;

    public void reset()
    {
        super.reset();
        currentHits.clear();
        lastHits.clear();
        verzikEntryTick = -1;
        verzikP1EndTick = -1;
        verzikRedsTick = -1;
        verzikP2EndTick = -1;
        verzikP3EndTick = -1;
        hasWebbed = true;
        webTick = -1;
    }

    public void updateGameTick(GameTick event)
    {
        if(roomState == RoomState.VerzikRoomState.PHASE_1)
        {
            for(Projectile projectile : client.getProjectiles())
            {
                if(projectile.getId() == 1547 || projectile.getId() == 1544)
                {
                    log.info("Projectile on tick " + client.getTickCount() + ", " + " game cylce: " + client.getGameCycle() + ", projectile starts: " + projectile.getStartCycle());
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

    public void updateProjectileMoved(ProjectileMoved event)
    {
        if (event.getProjectile().getId() == 1587)
        {
            if (event.getProjectile().getStartCycle() == client.getGameCycle())
            {
            }
        }
        if (event.getProjectile().getId() == 1591)
        {
            if (event.getProjectile().getRemainingCycles() == 0)
            {
            }
        }
        else if(event.getProjectile().getId() == 1601)
        {
            if(!hasWebbed)
            {
                hasWebbed = true;
                clog.write(WEBS_STARTED, String.valueOf(client.getTickCount()-verzikEntryTick));
                webTick = client.getTickCount();
            }
        }
    }

    public void updateHitsplatApplied(HitsplatApplied event) {
        if (event.getActor().getName() != null) {
            if (event.getActor().getName().contains("Verzik") && event.getHitsplat().getHitsplatType() == HitsplatID.HEAL) {
                currentHits.add(event.getHitsplat().getAmount());
            }
        }
    }

    public void updateGraphicChanged(GraphicChanged event) {
        if (event.getActor().hasSpotAnim(VERZIK_BOUNCE_SPOT_ANIMATION))
        {
            clog.write(LogID.VERZIK_BOUNCE, event.getActor().getName());
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
        if(roomState == RoomState.VerzikRoomState.PHASE_1) {
            if (event.getActor() instanceof Player)
            {
                Player p = (Player) event.getActor();
                IterableHashTable<ActorSpotAnim> graphics = p.getSpotAnims();
                String animations = "";
                for(ActorSpotAnim anim : graphics)
                {
                    animations += String.valueOf(anim.getId());
                    animations += ":";
                }
                int weaponID = p.getPlayerComposition().getEquipmentId(KitType.WEAPON);
                clog.write(P1_ATTACK, p.getName(), ""+p.getAnimation(), ""+(client.getTickCount()-verzikEntryTick),animations,String.valueOf(weaponID));
            }

        }
        if (id == 8117)
        {
        }
        if (event.getActor().getAnimation() == VERZIK_BECOMES_SPIDER)
        {
            endP3();
        }
    }

    public void updateNpcSpawned(NpcSpawned event) {
        int id = event.getNpc().getId();
        if (id == VERZIK_MATOMENOS || id == VERZIK_MATOMENOS_HM || id == VERZIK_MATOMENOS_SM) {
            if (roomState != RoomState.VerzikRoomState.PHASE_2_REDS) {
                procReds();
            }
        }
        switch (id) {
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

    public void updateNpcDespawned(NpcDespawned event) {
        int id = event.getNpc().getId();
        if (id == VERZIK_P1 || id == VERZIK_P1_HM || id == VERZIK_P1_SM) {
            endP1();
        } else if (id == VERZIK_P2 || id == VERZIK_P2_HM || id == VERZIK_P2_SM) {
            endP2();
        }
    }

    public void handleNPCChanged(int id) {
        if (id == VERZIK_P1 || id == VERZIK_P1_HM || id == VERZIK_P1_SM) {
            if (id == VERZIK_P1_HM) {
                clog.write(IS_HARD_MODE);
            } else if (id == VERZIK_P1_SM) {
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
    }

    private void procReds() {
        roomState = RoomState.VerzikRoomState.PHASE_2_REDS;
        verzikRedsTick = client.getTickCount();
        sendTimeMessage("Red Crabs Spawned. Duration: ", verzikRedsTick - verzikEntryTick);
        clog.write(VERZIK_P2_REDS_PROC, (verzikRedsTick - verzikEntryTick) + "");
    }

    private void endP2() {
        roomState = RoomState.VerzikRoomState.PHASE_3;
        verzikP2EndTick = client.getTickCount();
        sendTimeMessage("Wave 'Verzik phase 2' complete. Duration: ", verzikP2EndTick - verzikEntryTick, verzikP2EndTick - verzikP1EndTick);
        clog.write(VERZIK_P2_END, (verzikP2EndTick - verzikEntryTick) + "");
    }

    private void endP3() {
        roomState = RoomState.VerzikRoomState.FINISHED;
        verzikP3EndTick = client.getTickCount() + 6;
        clog.write(ACCURATE_VERZIK_END);
        sendTimeMessage("Wave 'Verzik phase 3' complete. Duration: ", verzikP3EndTick - verzikEntryTick, verzikP3EndTick - verzikP2EndTick);
        clog.write(VERZIK_P3_DESPAWNED, (verzikP3EndTick - verzikEntryTick) + "");
    }
}
