package com.TheatreTracker.utility.thrallvengtracking;

import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.Player;

import java.awt.*;
import java.util.ArrayList;

import static com.TheatreTracker.constants.NpcIDs.*;

public class Thrall {
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

    public boolean matchesGraphic(int animationID) {
        return (animationID == 1873 && npc.getId() == 10880)
                || (animationID == 1874 && npc.getId() == 10883)
                || (animationID == 1875 && npc.getId() == 10886);
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
