package com.TheatreTracker.utility.thrallvengtracking;

import com.TheatreTracker.TheatreTrackerPlugin;
import com.TheatreTracker.constants.LogID;
import com.TheatreTracker.utility.ThrallOutlineBox;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.Objects;

import static com.TheatreTracker.constants.NpcIDs.*;

@Slf4j
public class ThrallTracker {
    private ArrayList<Player> queuedCastAnimation;
    private ArrayList<Player> queuedMageCastGraphic;
    private ArrayList<Player> queuedRangeCastGraphic;
    private ArrayList<Player> queuedMeleeCastGraphic;
    private ArrayList<Thrall> queuedThrallSpawn;

    private ArrayList<Thrall> activeThralls;


    private TheatreTrackerPlugin plugin;

    public ThrallTracker(TheatreTrackerPlugin plugin) {
        this.plugin = plugin;
        queuedCastAnimation = new ArrayList<>();
        queuedMageCastGraphic = new ArrayList<>();
        queuedMeleeCastGraphic = new ArrayList<>();
        queuedRangeCastGraphic = new ArrayList<>();
        queuedThrallSpawn = new ArrayList<>();

        activeThralls = new ArrayList<>();
    }

    public void removeThrall(NPC npc) {
        activeThralls.removeIf(thrall -> thrall.npc.getIndex() == npc.getIndex());
    }

    public void handleCasts()
    {
        if (queuedThrallSpawn.size() == 0)
        {
            return;
        }
        ArrayList<PlayerShell> assignedPlayers = new ArrayList<>();
        for (Thrall thrall : queuedThrallSpawn)
        {
            if (thrall.potentialPlayers.size() == 1)
            {
                thrall.setOwner(thrall.potentialPlayers.get(0));
                plugin.addThrallOutlineBox(new ThrallOutlineBox(thrall.potentialPlayers.get(0).name, thrall.spawnTick, thrall.npc.getId()));
                activeThralls.add(thrall);

                assignedPlayers.add(new PlayerShell(thrall.player.worldLocation, thrall.player.name));
            }
        }
        queuedThrallSpawn.removeIf(thrall -> thrall.potentialPlayers.size() == 1);
        queuedCastAnimation.removeIf(p -> assignedPlayers.stream().anyMatch(p2 -> Objects.equals(p2.name, p.getName())));
        queuedMeleeCastGraphic.removeIf(p -> assignedPlayers.stream().anyMatch(p2 -> Objects.equals(p2.name, p.getName())));
        queuedRangeCastGraphic.removeIf(p -> assignedPlayers.stream().anyMatch(p2 -> Objects.equals(p2.name, p.getName())));
        queuedMageCastGraphic.removeIf(p -> assignedPlayers.stream().anyMatch(p2 -> Objects.equals(p2.name, p.getName())));

        for (Thrall thrall : queuedThrallSpawn)
        {
            thrall.potentialPlayers.removeIf(p1 -> queuedCastAnimation.stream().noneMatch(p2 -> Objects.equals(p2.getName(), p1.name)));
            if (thrall.potentialPlayers.size() == 1)
            {
                thrall.setOwner(thrall.potentialPlayers.get(0));
                plugin.addThrallOutlineBox(new ThrallOutlineBox(thrall.potentialPlayers.get(0).name, thrall.spawnTick, thrall.npc.getId()));
                activeThralls.add(thrall);
            }
        }
        queuedThrallSpawn.removeIf(thrall -> thrall.potentialPlayers.size() == 1);
        queuedCastAnimation.removeIf(p -> assignedPlayers.stream().anyMatch(p2 -> Objects.equals(p2.name, p.getName())));
        queuedMeleeCastGraphic.removeIf(p -> assignedPlayers.stream().anyMatch(p2 -> Objects.equals(p2.name, p.getName())));
        queuedRangeCastGraphic.removeIf(p -> assignedPlayers.stream().anyMatch(p2 -> Objects.equals(p2.name, p.getName())));
        queuedMageCastGraphic.removeIf(p -> assignedPlayers.stream().anyMatch(p2 -> Objects.equals(p2.name, p.getName())));

        for (Thrall thrall : queuedThrallSpawn)
        {
            thrall.potentialPlayers.removeIf(p1 ->
                    queuedMageCastGraphic.stream().noneMatch(p2 -> thrall.matchesGraphic(THRALL_CAST_GRAPHIC_MAGE))
                            && queuedRangeCastGraphic.stream().noneMatch(p2 -> thrall.matchesGraphic(THRALL_CAST_GRAPHIC_RANGE))
                            && queuedMeleeCastGraphic.stream().noneMatch(p2 -> thrall.matchesGraphic(THRALL_CAST_GRAPHIC_MELEE)));
            if (thrall.potentialPlayers.size() == 1)
            {
                thrall.setOwner(thrall.potentialPlayers.get(0));
                plugin.addThrallOutlineBox(new ThrallOutlineBox(thrall.potentialPlayers.get(0).name, thrall.spawnTick, thrall.npc.getId()));
                activeThralls.add(thrall);
            }
        }
        queuedThrallSpawn.removeIf(thrall -> thrall.potentialPlayers.size() == 1);
        queuedCastAnimation.removeIf(p -> assignedPlayers.stream().anyMatch(p2 -> Objects.equals(p2.name, p.getName())));
        queuedMeleeCastGraphic.removeIf(p -> assignedPlayers.stream().anyMatch(p2 -> Objects.equals(p2.name, p.getName())));
        queuedRangeCastGraphic.removeIf(p -> assignedPlayers.stream().anyMatch(p2 -> Objects.equals(p2.name, p.getName())));
        queuedMageCastGraphic.removeIf(p -> assignedPlayers.stream().anyMatch(p2 -> Objects.equals(p2.name, p.getName())));

        for (Thrall thrall : queuedThrallSpawn)
        {
            activeThralls.add(thrall);
        }
        queuedThrallSpawn.clear();

    }

    public void updatePlayerInteracting(String playerName, Actor interacting)
    {
        for (Thrall thrall : activeThralls)
        {
            if (thrall.getOwner().equals(playerName))
            {
                if (interacting != null)
                {
                    thrall.lastParentInteraction = interacting;
                }
            }
        }
    }

    public void updateTick() {
        handleCasts();
    }

    public void castThrallAnimation(Player player) {
        queuedCastAnimation.add(player);
    }

    public void meleeThrallAttacked(NPC npc)
    {
        for (Thrall thrall : activeThralls) {
            if (npc.getIndex() == thrall.npc.getIndex() && thrall.isMelee && thrall.npc.getAnimation() == MELEE_THRALL_ATTACK_ANIMATION)
            {
                if (thrall.lastParentInteraction instanceof NPC)
                {
                    if (thrall.lastParentInteraction.getWorldArea().distanceTo(thrall.npc.getWorldLocation()) < 2)
                    {
                        plugin.clog.write(LogID.THRALL_ATTACKED, thrall.getOwner(), "melee");
                        plugin.addQueuedThrallDamage(((((NPC) thrall.lastParentInteraction).getIndex())), thrall.npc.getIndex(), 1, thrall.getOwner());
                    }
                }
            }
        }
    }

    public void projectileCreated(Projectile projectile, WorldPoint origin, WorldPoint target, int tick)
    {
        if (projectile.getInteracting() instanceof NPC)
        {
            int hitOffset = -1;

            if (projectile.getId() == THRALL_PROJECTILE_RANGE)
            {
                hitOffset = (projectile.getRemainingCycles() > 30) ? (projectile.getRemainingCycles() > 60) ? 2 : 1 : 0;
            } else if (projectile.getId() == THRALL_PROJECTILE_MAGE)
            {
                hitOffset = (projectile.getRemainingCycles() > 40) ? 1 : 0;
            }
            for (Thrall t : activeThralls)
            {
                if (t.npc.getWorldLocation().distanceTo(origin) == 0 && hitOffset != -1)
                {
                    plugin.clog.write(LogID.THRALL_ATTACKED, t.getOwner(), String.valueOf(projectile.getId()));
                    plugin.addQueuedThrallDamage(((NPC) (projectile.getInteracting())).getIndex(), t.npc.getIndex(), hitOffset, t.getOwner());
                    break;
                }
            }
        }
    }

    public void playerHasThrallCastSpotAnim(Player player, int id) {
        switch (id) {
            case THRALL_CAST_GRAPHIC_MELEE:
                queuedMeleeCastGraphic.add(player);
                break;
            case THRALL_CAST_GRAPHIC_RANGE:
                queuedRangeCastGraphic.add(player);
                break;
            case THRALL_CAST_GRAPHIC_MAGE:
                queuedMageCastGraphic.add(player);
                break;
        }
    }

    public void thrallSpawned(NPC thrall, ArrayList<PlayerShell> adjacentPlayers)
    {
        queuedThrallSpawn.add(new Thrall(thrall, adjacentPlayers, plugin.getRoomTick()));
    }

    public void thrallDespawned(NPC thrall) {

    }


}
