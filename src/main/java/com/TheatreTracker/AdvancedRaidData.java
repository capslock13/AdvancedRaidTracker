package com.TheatreTracker;

import com.TheatreTracker.constants.LogID;
import com.TheatreTracker.utility.wrappers.PlayerDidAttack;
import net.runelite.client.game.ItemManager;

import java.util.ArrayList;
import static com.TheatreTracker.constants.TobIDs.EXIT_FLAG;
import static com.TheatreTracker.constants.TobIDs.SPECTATE_FLAG;

public class AdvancedRaidData
{
    public final ArrayList<PlayerDidAttack> maidenAttacks;
    private ArrayList<String> globalData;
    private final ItemManager itemManager;
    public AdvancedRaidData(ArrayList<String> globalData, ItemManager itemManager)
    {
        maidenAttacks = new ArrayList<>();
        this.itemManager = itemManager;
        this.globalData = globalData;
        int room = -1;
        for (String s : globalData)
        {
            String[] subData = s.split(",");
            int key = Integer.parseInt(subData[3]);
            if (key == SPECTATE_FLAG)
            {
                room = Integer.parseInt(subData[4]);
            }
        }
        if (room > 0)
        {
            switch (room)
            {
                case 1:
                    if (!(checkExit() && parseBloat()))
                        break;
                case 2:
                    if (!(checkExit() && parseNylo()))
                        break;
                case 3:
                    if (!(checkExit() && parseSotetseg()))
                        break;
                case 4:
                    if (!(checkExit() && parseXarpus()))
                        break;
                case 5:
                    if (checkExit())
                    {
                        parseVerzik();
                    }
            }
        } else
        {
            try
            {
                if (parseMaiden())
                {
                    if (checkExit() && parseBloat())
                    {
                        if (checkExit() && parseNylo())
                        {
                            if (checkExit() && parseSotetseg())
                            {
                                if (checkExit() && parseXarpus())
                                {
                                    if (checkExit())
                                    {
                                        parseVerzik();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ignored)
            {
            }
        }
    }

    PlayerDidAttack getPlayerDidAttack(String[] subData)
    {
        String player = subData[4].split(":")[0];
        int tick = Integer.parseInt(subData[4].split(":")[1]);
        String wornItems = "";
        String [] animationAndWorn = subData[5].split(":");
        String animation = animationAndWorn[0];
        if(animationAndWorn.length == 2)
        {
            wornItems = animationAndWorn[1];
        }
        String spotAnims = subData[6];
        String[] subsubData = subData[7].split(":");
        String weapon = subsubData[0];
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

    private boolean parseMaiden()
    {
        int activeIndex = 0;
        loop:
        for (String s : globalData)
        {
            String[] subData = s.split(",", -1);
            try
            {
                switch (LogID.valueOf(Integer.parseInt(subData[3])))
                {
                    case LEFT_TOB:
                        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                        return false;
                    case MAIDEN_0HP:
                    case BLOAT_SPAWNED:
                    case MAIDEN_DESPAWNED:
                        break loop;
                    case PLAYER_ATTACK:
                        maidenAttacks.add(getPlayerDidAttack(subData));
                        break;
                }
            }
            catch(Exception e)
            {
            }
            activeIndex++;
        }
        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean parseBloat()
    {
        int activeIndex = 0;
        loop:
        for (String s : globalData)
        {
            String[] subData = s.split(",", -1);
            try
            {
                switch (LogID.valueOf(Integer.parseInt(subData[3])))
                {
                    case LEFT_TOB:
                        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                        return false;
                    case BLOAT_DESPAWN:
                        break loop;
                }
            }
            catch(Exception e)
            {
            }
            activeIndex++;
        }
        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean parseNylo()
    {
        int activeIndex = 0;
        loop:
        for (String s : globalData)
        {
            String[] subData = s.split(",", -1);
            try
            {
                switch (LogID.valueOf(Integer.parseInt(subData[3])))
                {
                    case LEFT_TOB:
                        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                        return false;
                    case NYLO_DESPAWNED:
                        break loop;
                }
            }
            catch(Exception e)
            {
            }
            activeIndex++;
        }
        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean parseSotetseg()
    {
        int activeIndex = 0;
        loop:
        for (String s : globalData)
        {
            String[] subData = s.split(",", -1);
            try
            {
                switch (LogID.valueOf(Integer.parseInt(subData[3])))
                {
                    case LEFT_TOB:
                        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                        return false;
                    case SOTETSEG_ENDED:
                        break loop;

                }
            }
            catch(Exception e)
            {
            }
            activeIndex++;
        }
        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean parseXarpus()
    {

        int activeIndex = 0;
        loop:
        for (String s : globalData)
        {
            String[] subData = s.split(",", -1);
            try
            {
                switch (LogID.valueOf(Integer.parseInt(subData[3])))
                {
                    case LEFT_TOB:
                        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                        return false;
                    case XARPUS_DESPAWNED:
                        break loop;
                }
            }
            catch(Exception e)
            {
            }
            activeIndex++;
        }
        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean parseVerzik()
    {
        int activeIndex = 0;
        for (String s : globalData)
        {
            String[] subData = s.split(",", -1);
            try
            {
                switch (LogID.valueOf(Integer.parseInt(subData[3])))
                {
                    case LEFT_TOB:
                        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                        return false;
                }
            } catch (Exception e)
            {
            }
            activeIndex++;
        }
        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    public boolean checkExit()
    {
        return globalData.isEmpty() || globalData.get(0).split(",", -1)[3].equals(EXIT_FLAG);
    }
}
