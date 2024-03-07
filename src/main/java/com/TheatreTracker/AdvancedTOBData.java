package com.TheatreTracker;

import com.TheatreTracker.constants.LogID;
import com.TheatreTracker.utility.AdvancedRaidData;
import com.TheatreTracker.utility.wrappers.PlayerDidAttack;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.game.ItemManager;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static com.TheatreTracker.constants.TobIDs.EXIT_FLAG;
import static com.TheatreTracker.constants.TobIDs.SPECTATE_FLAG;

@Slf4j
public class AdvancedTOBData extends AdvancedRaidData
{
    private final String[] names = {"Maiden", "Bloat", "Nylocas", "Sotetseg", "Xarpus", "Verzik P1", "Verzik P2", "Verzik P3"};
    public Map<Integer, Integer> maidenHP = new HashMap<>();
    public Map<Integer, Integer> bloatHP = new HashMap<>();
    public Map<Integer, Integer> nyloHP = new HashMap<>();
    public Map<Integer, Integer> soteHP = new HashMap<>();
    public Map<Integer, Integer> xarpHP = new HashMap<>();
    public Map<Integer, Integer> verzikHP = new HashMap<>();
    public Map<Integer, String> maidenNPCMapping = new HashMap<>();
    public Map<Integer, String> nyloNPCMapping = new HashMap<>();
    public Map<Integer, String> verzikNPCMapping = new HashMap<>();
    private ArrayList<String> globalData;
    private final ItemManager itemManager;
    public AdvancedTOBData(ArrayList<String> globalData, ItemManager itemManager)
    {
        this.itemManager = itemManager;
        this.globalData = globalData;
        attackData = new LinkedHashMap<>();
        for(String name : names)
        {
            attackData.put(name, new ArrayList<>());
        }
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
                        attackData.get("Maiden").add(getPlayerDidAttack(subData, itemManager));
                        break;
                    case UPDATE_HP:
                        maidenHP.put(Integer.parseInt(subData[5]), Integer.parseInt(subData[4]));
                        break;
                    case ADD_NPC_MAPPING:
                        maidenNPCMapping.put(Integer.parseInt(subData[4]), subData[5]);
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
                    case PLAYER_ATTACK:
                        attackData.get("Bloat").add(getPlayerDidAttack(subData, itemManager));
                        break;
                    case UPDATE_HP:
                        bloatHP.put(Integer.parseInt(subData[5]), Integer.parseInt(subData[4]));
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
                    case PLAYER_ATTACK:
                        attackData.get("Nylocas").add(getPlayerDidAttack(subData, itemManager));
                        break;
                    case UPDATE_HP:
                        nyloHP.put(Integer.parseInt(subData[5]), Integer.parseInt(subData[4]));
                        break;
                    case ADD_NPC_MAPPING:
                        nyloNPCMapping.put(Integer.parseInt(subData[4]), subData[5]);
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
                    case PLAYER_ATTACK:
                        attackData.get("Sotetseg").add(getPlayerDidAttack(subData, itemManager));
                        break;
                    case UPDATE_HP:
                        soteHP.put(Integer.parseInt(subData[5]), Integer.parseInt(subData[4]));
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
                    case PLAYER_ATTACK:
                        attackData.get("Xarpus").add(getPlayerDidAttack(subData, itemManager));
                        break;
                    case UPDATE_HP:
                        xarpHP.put(Integer.parseInt(subData[5]), Integer.parseInt(subData[4]));
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

    private boolean parseVerzik()
    {
        int activeIndex = 0;
        int phase = 1;
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
                    case PLAYER_ATTACK:
                        attackData.get("Verzik P" + phase).add(getPlayerDidAttack(subData, itemManager));
                        break;
                    case UPDATE_HP:
                        verzikHP.put(Integer.parseInt(subData[5]), Integer.parseInt(subData[4]));
                        break;
                    case ADD_NPC_MAPPING:
                        verzikNPCMapping.put(Integer.parseInt(subData[4]), subData[5]);
                        break;
                    case VERZIK_BOUNCE:
                        if (!subData[5].equalsIgnoreCase(""))
                        { //use fake animation ID when bounce occurs
                            attackData.get("Verzik P2").add(new PlayerDidAttack(itemManager, subData[4], "100000", Integer.parseInt(subData[5]), "-1", "-1", "-1", -1, -1, "", ""));
                        }
                        break;
                    case VERZIK_P1_DESPAWNED:
                        phase = 2;
                        break;
                    case VERZIK_P2_END:
                        phase = 3;
                        break;

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
        return !(globalData.isEmpty() || globalData.get(0).split(",", -1)[3].equals(EXIT_FLAG));
    }
}
