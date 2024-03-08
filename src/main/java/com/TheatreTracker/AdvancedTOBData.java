package com.TheatreTracker;

import com.TheatreTracker.constants.LogID;
import com.TheatreTracker.utility.AdvancedRaidData;
import com.TheatreTracker.utility.wrappers.PlayerDidAttack;
import com.TheatreTracker.utility.wrappers.ThrallOutlineBox;
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
    private ArrayList<String> globalData;
    private final ItemManager itemManager;

    public AdvancedTOBData(ArrayList<String> globalData, ItemManager itemManager)
    {
        this.itemManager = itemManager;
        this.globalData = globalData;
        attackData = new LinkedHashMap<>();
        hpData = new LinkedHashMap<>();
        npcIndexData = new LinkedHashMap<>();
        thrallOutlineBoxes = new LinkedHashMap<>();
        for (String name : names)
        {
            attackData.put(name, new ArrayList<>());
            hpData.put(name, new LinkedHashMap<>());
            npcIndexData.put(name, new LinkedHashMap<>());
            thrallOutlineBoxes.put(name, new ArrayList<>());
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
                        log.info("Attack data size is now: " + attackData.get("Maiden").size());
                        break;
                    case UPDATE_HP:
                        hpData.get("Maiden").put(Integer.parseInt(subData[5]), Integer.parseInt(subData[4]));
                        break;
                    case ADD_NPC_MAPPING:
                        npcIndexData.get("Maiden").put(Integer.parseInt(subData[4]), subData[5]);
                        break;
                    case THRALL_SPAWN:
                        thrallOutlineBoxes.get("Maiden").add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
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
                        hpData.get("Bloat").put(Integer.parseInt(subData[5]), Integer.parseInt(subData[4]));
                        break;
                    case ADD_NPC_MAPPING:
                        npcIndexData.get("Bloat").put(Integer.parseInt(subData[4]), subData[5]);
                        break;
                    case THRALL_SPAWN:
                        thrallOutlineBoxes.get("Bloat").add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
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
                        hpData.get("Nylocas").put(Integer.parseInt(subData[5]), Integer.parseInt(subData[4]));
                        break;
                    case ADD_NPC_MAPPING:
                        npcIndexData.get("Nylocas").put(Integer.parseInt(subData[4]), subData[5]);
                        break;
                    case THRALL_SPAWN:
                        thrallOutlineBoxes.get("Nylocas").add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
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
                        hpData.get("Sotetseg").put(Integer.parseInt(subData[5]), Integer.parseInt(subData[4]));
                        break;
                    case ADD_NPC_MAPPING:
                        npcIndexData.get("Sotetseg").put(Integer.parseInt(subData[4]), subData[5]);
                        break;
                    case THRALL_SPAWN:
                        thrallOutlineBoxes.get("Sotetseg").add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
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
                        hpData.get("Xarpus").put(Integer.parseInt(subData[5]), Integer.parseInt(subData[4]));
                        break;
                    case ADD_NPC_MAPPING:
                        npcIndexData.get("Xarpus").put(Integer.parseInt(subData[4]), subData[5]);
                        break;
                    case THRALL_SPAWN:
                        thrallOutlineBoxes.get("Xarpus").add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
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
                        hpData.get("Verzik P" + phase).put(Integer.parseInt(subData[5]), Integer.parseInt(subData[4]));
                        break;
                    case ADD_NPC_MAPPING:
                        npcIndexData.get("Verzik P" + phase).put(Integer.parseInt(subData[4]), subData[5]);
                        break;
                    case THRALL_SPAWN:
                        thrallOutlineBoxes.get("Verzik P" + phase).add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                        break;
                    case VERZIK_BOUNCE:
                        if (!subData[5].equalsIgnoreCase(""))
                        { //use fake animation ID when bounce occurs
                            attackData.get("Verzik P2").add(new PlayerDidAttack(itemManager, subData[4], "100000", Integer.parseInt(subData[5]), -1, "-1", "-1", -1, -1, "", ""));
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
