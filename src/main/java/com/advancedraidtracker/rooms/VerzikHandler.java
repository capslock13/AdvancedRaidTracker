package com.advancedraidtracker.rooms;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.TOBRoom;
import com.advancedraidtracker.utility.datautility.DataWriter;
import com.advancedraidtracker.utility.wrappers.PlayerDidAttack;
import com.advancedraidtracker.utility.wrappers.DawnSpec;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import com.advancedraidtracker.utility.RoomState;
import net.runelite.client.game.ItemManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.advancedraidtracker.constants.LogID.*;
import static com.advancedraidtracker.constants.TobIDs.*;

@Slf4j
public class VerzikHandler extends RoomHandler
{
    public RoomState.VerzikRoomState roomState;
    private final AdvancedRaidTrackerPlugin plugin;
    private int healingEndTick = -1;
    private final ItemManager itemManager;

    public VerzikHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin, ItemManager itemManager)
    {
        super(client, clog, config);
        this.plugin = plugin;
        this.itemManager = itemManager;
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

    private final ArrayList<Integer> currentHits;
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
        //todo finish p2 heal tracking
    }

    public void updateGameTick(GameTick event)
    {
        for (Projectile projectile : client.getProjectiles())
        {
            if (projectile.getId() == VERZIK_CRAB_HEAL_PROJECTILE)
            {
                //todo finish p2 heal tracking
            }
        }
        if (client.getTickCount() == healingEndTick)
        {
            plugin.verzShieldActive = false;
        }
        int playersHit = 1;
        for (Player p : queuedAutoHits.keySet())
        {
            if (queuedAutoHits.get(p) == client.getTickCount())
            {
                for (Player p2 : client.getPlayers())
                {
                    if (p2.getWorldLocation().distanceTo(p.getWorldLocation()) <= 1)
                    {
                        playersHit++;
                        //todo finish p2 heal tracking
                    }
                }
            }
        }
        queuedAutoHits.clear();
        currentHits.clear();
        if (healingEndTick == client.getTickCount())
        {
            healingEndTick = -1;
        }
        redsThisTick = false;
        if (webTick != -1)
        {
            webTick++;
            if (webTick > 50) //non-specific large number > web length but < time before next webs could happen again
            {
                hasWebbed = false;
                webTick = -1;
            }
        }
        lastHits = currentHits;
    }

    private final Map<Player, Integer> queuedAutoHits = new HashMap<>();

    public void updateProjectileMoved(ProjectileMoved event)
    {
        if (event.getProjectile().getId() == VERZIK_CRAB_HEAL_PROJECTILE)
        {
            if (event.getProjectile().getRemainingCycles() == client.getGameCycle())
            {
                //todo finish p2 heal tracking
            }
        }
        if (event.getProjectile().getId() == VERZIK_RED_MAGE_AUTO_PROJECTILE)
        {
            if (event.getProjectile().getRemainingCycles() == 0)
            {
                if (verzNPC.getInteracting() instanceof Player)
                {
                    Player p = (Player) verzNPC;
                    queuedAutoHits.put(p, client.getTickCount() + 2);
                }
            }
        } else if (event.getProjectile().getId() == VERZIK_WEB_PROJECTILE)
        {
            if (!hasWebbed)
            {
                hasWebbed = true;
                clog.addLine(WEBS_STARTED, String.valueOf(client.getTickCount() - verzikEntryTick));
                webTick = client.getTickCount();
                if ((webTick - verzikEntryTick) % 2 == 0)
                {
                    plugin.addDelayedLine(TOBRoom.VERZIK, webTick - verzikEntryTick, "Webs");
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
            if (roomState == RoomState.VerzikRoomState.PHASE_1 && event.getActor().getName().contains("Verzik"))
            {
                if (event.getHitsplat().getAmount() >= DAWNBRINGER_MINIMUM_HIT)
                {
                    clog.addLine(DAWN_DAMAGE, String.valueOf(event.getHitsplat().getAmount()), String.valueOf(client.getTickCount() - roomStartTick));
                    DawnSpec dawnSpec = new DawnSpec("", client.getTickCount() - roomStartTick);
                    dawnSpec.setDamage(event.getHitsplat().getAmount());
                    plugin.liveFrame.getPanel(getName()).addDawnSpec(dawnSpec);
                }
            }
        }
    }

    public void updateGraphicChanged(GraphicChanged event)
    {
        if (event.getActor().hasSpotAnim(VERZIK_BOUNCE_SPOT_ANIMATION))
        {
            clog.addLine(LogID.VERZIK_BOUNCE, event.getActor().getName(), String.valueOf(client.getTickCount() - verzikEntryTick));
            plugin.liveFrame.addAttack(new PlayerDidAttack(itemManager, event.getActor().getName(), VERZIK_BOUNCE_ANIMATION, client.getTickCount() - verzikEntryTick, "-1", "-1", "-1", -1, -1, "", ""), "Verzik");

        }
    }

    public void updateItemSpawned(ItemSpawned event)
    {
        if (event.getItem().getId() == DAWNBRINGER_ITEM)
        {
            clog.addLine(DAWN_DROPPED, String.valueOf(client.getTickCount() - verzikEntryTick));
            plugin.liveFrame.getPanel(getName()).addRoomSpecificData(client.getTickCount() - verzikEntryTick, "X");
        }
    }


    public void updateAnimationChanged(AnimationChanged event)
    {
        int id = event.getActor().getAnimation();
        if (roomState == RoomState.VerzikRoomState.PHASE_2 || roomState == RoomState.VerzikRoomState.PHASE_2_REDS)
        {
            if (plugin.verzShieldActive)
            {
                if (event.getActor() instanceof Player)
                {
                    Player p = (Player) event.getActor();
                    if (p.getInteracting() instanceof NPC)
                    {
                        NPC interacting = (NPC) p.getInteracting();
                        if (interacting.getId() == VERZIK_P2 || interacting.getId() == VERZIK_P2_HM || interacting.getId() == VERZIK_P2_SM)
                        {
                            //TODO: P2 Healing
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

    public void updateNpcSpawned(NpcSpawned event)
    {
        int id = event.getNpc().getId();
        if (id == VERZIK_MATOMENOS || id == VERZIK_MATOMENOS_HM || id == VERZIK_MATOMENOS_SM)
        {
            if (!redsThisTick)
            {
                clog.addLine(VERZIK_P2_REDS_PROC, String.valueOf(client.getTickCount() - verzikEntryTick));
                plugin.addDelayedLine(TOBRoom.VERZIK, client.getTickCount() - verzikEntryTick, "Reds");
                healingEndTick = client.getTickCount() + VERZIK_SHIELD_LENGTH;
                plugin.addDelayedLine(TOBRoom.VERZIK, healingEndTick - verzikEntryTick, "Shield End");
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
                clog.addLine(VERZIK_CRAB_SPAWNED, String.valueOf(client.getTickCount() - roomStartTick));
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

    public void updateNpcDespawned(NpcDespawned event)
    {
        int id = event.getNpc().getId();
        if (id == VERZIK_P1 || id == VERZIK_P1_HM || id == VERZIK_P1_SM)
        {
            endP1();
        } else if (id == VERZIK_P2 || id == VERZIK_P2_HM || id == VERZIK_P2_SM)
        {
            endP2();
        }
    }

    public void handleNPCChanged(int id)
    {
        if (id == VERZIK_P1 || id == VERZIK_P1_HM || id == VERZIK_P1_SM)
        {
            if (id == VERZIK_P1_HM)
            {
                clog.addLine(IS_HARD_MODE);
            } else if (id == VERZIK_P1_SM)
            {
                clog.addLine(IS_STORY_MODE);
            }
            startVerzik();
        } else if (id == VERZIK_P2 || id == VERZIK_P2_HM || id == VERZIK_P2_SM)
        {
            endP1();
        } else if (id == VERZIK_P3 || id == VERZIK_P3_HM || id == VERZIK_P3_SM)
        {
            endP2();
        } else if (id == VERZIK_DEAD || id == VERZIK_DEAD_HM || id == VERZIK_DEAD_SM)
        {
            endP3();
        }
    }

    private void startVerzik()
    {
        roomState = RoomState.VerzikRoomState.PHASE_1;
        verzikEntryTick = client.getTickCount();
        clog.addLine(VERZIK_P1_START);
        clog.addLine(ACCURATE_VERZIK_START);
        roomStartTick = client.getTickCount();
    }

    private void endP1()
    {
        roomState = RoomState.VerzikRoomState.PHASE_2;
        verzikP1EndTick = client.getTickCount();
        sendTimeMessage("Wave 'Verzik phase 1' complete. Duration: ", verzikP1EndTick - verzikEntryTick);
        clog.addLine(VERZIK_P1_DESPAWNED, String.valueOf(verzikP1EndTick - verzikEntryTick));
        plugin.addDelayedLine(TOBRoom.VERZIK, verzikP1EndTick - verzikEntryTick, "P1 End");

    }

    private void procReds()
    {
        roomState = RoomState.VerzikRoomState.PHASE_2_REDS;
        verzikRedsTick = client.getTickCount();
        sendTimeMessage("Red Crabs Spawned. Duration: ", verzikRedsTick - verzikEntryTick);
    }

    private void endP2()
    {
        roomState = RoomState.VerzikRoomState.PHASE_3;
        verzikP2EndTick = client.getTickCount();
        sendTimeMessage("Wave 'Verzik phase 2' complete. Duration: ", verzikP2EndTick - verzikEntryTick, verzikP2EndTick - verzikP1EndTick);
        clog.addLine(VERZIK_P2_END, String.valueOf(verzikP2EndTick - verzikEntryTick));
        plugin.addDelayedLine(TOBRoom.VERZIK, verzikP2EndTick - verzikEntryTick, "P2 End");

    }

    private void endP3()
    {
        //todo incorrect doubles sometimes
        roomState = RoomState.VerzikRoomState.FINISHED;
        verzikP3EndTick = client.getTickCount() + VERZIK_DEATH_ANIMATION_LENGTH;
        clog.addLine(ACCURATE_VERZIK_END);
        sendTimeMessage("Wave 'Verzik phase 3' complete. Duration: ", verzikP3EndTick - verzikEntryTick, verzikP3EndTick - verzikP2EndTick);
        clog.addLine(VERZIK_P3_DESPAWNED, String.valueOf(verzikP3EndTick - verzikEntryTick));
        plugin.addDelayedLine(TOBRoom.VERZIK, client.getTickCount() - verzikEntryTick, "Dead");
        plugin.liveFrame.setVerzFinished(verzikP3EndTick - verzikEntryTick);

    }
}
