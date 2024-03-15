package com.advancedraidtracker.utility.datautility;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.wrappers.PlayerDidAttack;
import com.advancedraidtracker.utility.wrappers.ThrallOutlineBox;
import net.runelite.client.game.ItemManager;

import java.util.*;

public class ChartData
{
    private final Map<RaidRoom, Map<Integer, Integer>> hpMapping;
    private final Map<RaidRoom, Map<Integer, String>> npcMapping;
    private final Map<RaidRoom, List<PlayerDidAttack>> attacks;

    private final Map<RaidRoom, List<ThrallOutlineBox>> thrallBoxes;


    public ChartData()
    {
        this.hpMapping = new HashMap<>();
        this.npcMapping = new HashMap<>();
        this.attacks = new HashMap<>();
        this.thrallBoxes = new HashMap<>();
    }

    public void addHPMapping(RaidRoom room, Integer tick, Integer hp)
    {
        hpMapping.computeIfAbsent(room, k -> new HashMap<>()).put(tick, hp);
    }

    public void addNPCMapping(RaidRoom room, Integer index, String npcDescription)
    {
        npcMapping.computeIfAbsent(room, k -> new HashMap<>()).put(index, npcDescription);
    }

    public void addAttack(RaidRoom room, PlayerDidAttack attack)
    {
        attacks.computeIfAbsent(room, k -> new ArrayList<>()).add(attack);
    }

    public void addThrallOutlineBox(RaidRoom room, String owner, int spawnTick, int id)
    {
        thrallBoxes.computeIfAbsent(room, k -> new ArrayList<>()).add(new ThrallOutlineBox(owner, spawnTick, id));
    }

    public Map<Integer, Integer> getHPMapping(RaidRoom room)
    {
        return hpMapping.getOrDefault(room, new HashMap<>());
    }

    public Map<Integer, String> getNPCMapping(RaidRoom room)
    {
        return npcMapping.getOrDefault(room, new HashMap<>());
    }

    public List<PlayerDidAttack> getAttacks(RaidRoom room)
    {
        return attacks.getOrDefault(room, new ArrayList<>());
    }

    public List<ThrallOutlineBox> getThralls(RaidRoom room)
    {
        return thrallBoxes.getOrDefault(room, new ArrayList<>());
    }

    public static PlayerDidAttack getPlayerDidAttack(String[] subData, ItemManager itemManager)
    {
        String player = subData[4].split(":")[0];
        int tick = Integer.parseInt(subData[4].split(":")[1]);
        String wornItems = "";
        String[] animationAndWorn = subData[5].split(":");
        String animation = animationAndWorn[0];
        if (animationAndWorn.length == 2)
        {
            wornItems = animationAndWorn[1];
        }
        String spotAnims = subData[6];
        String[] subsubData = subData[7].split(":");
        int weapon = Integer.parseInt(subsubData[0]);
        int interactedIndex = -1;
        int interactedID = -1;
        if (subsubData.length > 2)
        {
            interactedIndex = Integer.parseInt(subsubData[1]);
            interactedID = Integer.parseInt(subsubData[2]);
        }
        String[] projectileAndTargetData = subData[8].split(":");
        String projectile = projectileAndTargetData[0];
        String targetName = "";
        if (projectileAndTargetData.length > 1)
        {
            targetName = projectileAndTargetData[1];
        }
        return (new PlayerDidAttack(itemManager, player, animation, tick, weapon, projectile, spotAnims, interactedIndex, interactedID, targetName, wornItems));
    }


}
