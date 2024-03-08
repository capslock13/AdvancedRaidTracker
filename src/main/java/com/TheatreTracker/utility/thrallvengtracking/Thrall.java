package com.TheatreTracker.utility.thrallvengtracking;

import net.runelite.api.Actor;
import net.runelite.api.NPC;

import java.util.ArrayList;

import static com.TheatreTracker.constants.TobIDs.*;

public class Thrall
{
    NPC npc;
    PlayerShell player;
    ArrayList<PlayerShell> potentialPlayers;

    public Actor lastParentInteraction;
    boolean isMelee;
    public int spawnTick;

    public Thrall(NPC npc, ArrayList<PlayerShell> potentialPlayers, int spawnTick)
    {
        this.potentialPlayers = potentialPlayers;
        this.npc = npc;
        isMelee = npc.getId() == MELEE_THRALL;
        this.spawnTick = spawnTick;
    }

    public boolean matchesGraphic(int animationID)
    {
        return (animationID == THRALL_CAST_GRAPHIC_MAGE && npc.getId() == MAGE_THRALL)
                || (animationID == THRALL_CAST_GRAPHIC_RANGE && npc.getId() == RANGE_THRALL)
                || (animationID == THRALL_CAST_GRAPHIC_MELEE && npc.getId() == MELEE_THRALL);
    }

    public void setOwner(PlayerShell player)
    {
        this.player = player;
    }

    public String getOwner()
    {
        if (player != null)
        {
            return player.name;
        } else
        {
            return ".unassigned";
        }
    }
}
