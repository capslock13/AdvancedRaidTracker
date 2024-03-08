package com.advancedraidtracker;

import com.advancedraidtracker.utility.wrappers.PlayerDidAttack;
import com.advancedraidtracker.utility.wrappers.ThrallOutlineBox;
import net.runelite.client.game.ItemManager;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public abstract class AdvancedRaidDataBase
{
    public Map<String, ArrayList<PlayerDidAttack>> attackData;
    public Map<String, Map<Integer, Integer>> hpData;
    public Map<String, Map<Integer, String>> npcIndexData;

    public Map<String, ArrayList<ThrallOutlineBox>> thrallOutlineBoxes;

    public static ArrayList<String> getRaidStrings(String path)
    {
        ArrayList<String> lines = new ArrayList<>();
        File file = new File(path);
        try
        {
            Scanner scanner = new Scanner(Files.newInputStream(file.toPath()));
            while (scanner.hasNextLine())
            {
                lines.add(scanner.nextLine());
            }
        } catch (Exception e)
        {

        }
        return lines;
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
