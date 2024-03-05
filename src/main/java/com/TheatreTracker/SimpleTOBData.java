package com.TheatreTracker;

import com.TheatreTracker.constants.LogID;
import com.TheatreTracker.constants.RaidType;
import com.TheatreTracker.constants.TOBRoom;
import com.TheatreTracker.utility.datautility.DataManager;
import com.TheatreTracker.utility.datautility.DataPoint;
import com.TheatreTracker.utility.wrappers.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import static com.TheatreTracker.constants.TOBRoom.*;
import static com.TheatreTracker.constants.TobIDs.EXIT_FLAG;
import static com.TheatreTracker.constants.TobIDs.SPECTATE_FLAG;
import static com.TheatreTracker.utility.datautility.DataPoint.*;

@Slf4j
public class SimpleTOBData extends SimpleRaidData
{
    public boolean maidenStartAccurate = false;
    public boolean bloatStartAccurate = false;
    public boolean nyloStartAccurate = false;
    public boolean soteStartAccurate = false;
    public boolean xarpStartAccurate = false;
    public boolean verzikStartAccurate = false;

    public boolean maidenEndAccurate = false;
    public boolean bloatEndAccurate = false;
    public boolean nyloEndAccurate = false;
    public boolean soteEndAccurate = false;
    public boolean xarpEndAccurate = false;
    public boolean verzikEndAccurate = false;
    public boolean resetBeforeMaiden;

    public boolean partyComplete;
    public boolean hardMode;
    public boolean storyMode;
    private ArrayList<String> globalData;
    public LinkedHashMap<String, Integer> players;
    public String filePath = "";
    public String fileName = "";
    // Maiden tracking
    public boolean maidenTimeAccurate;
    public boolean maidenDefenseAccurate;
    public boolean maidenScuffed = false;
    public String firstMaidenCrabScuffed = "";
    public ArrayList<StringInt> maidenCrabs = new ArrayList<>();
    public ArrayList<String> maidenCrabSpawn = new ArrayList<>();
    public boolean maidenSpawned = false;
    public boolean maidenSkip;
    public boolean maidenReset;
    public boolean maidenWipe;
    // Bloat tracking
    public ArrayList<Integer> bloatDowns = new ArrayList<>();
    public boolean bloatTimeAccurate;
    public boolean bloatDefenseAccurate;
    public boolean bloatStarted;
    public boolean bloatReset;
    public boolean bloatWipe;
    // Nylo tracking
    public boolean nyloTimeAccurate;
    public boolean nyloDefenseAccurate;
    public boolean nyloWipe;
    public boolean nyloReset;
    public boolean nyloStarted;
    public ArrayList<Integer> nyloWaveStalled = new ArrayList<>();
    public Map<Integer, Integer> waveSpawns = new HashMap<>();
    // Sotetseg tracking
    public boolean soteTimeAccurate;
    public boolean soteDefenseAccurate;
    public boolean soteStarted;
    public boolean soteWipe;
    public boolean soteReset;
    // Xarpus tracking
    public boolean xarpTimeAccurate;
    public boolean xarpDefenseAccurate;
    public boolean xarpWipe;
    public boolean xarpReset;
    public boolean xarpStarted;
    // Verzik tracking
    public boolean verzikWipe;
    public boolean verzikStarted;
    public boolean verzikTimeAccurate;
    public ArrayList<Integer> websStart = new ArrayList<>();
    public ArrayList<DawnSpec> dawnSpecs = new ArrayList<>();
    public ArrayList<Integer> p2Crabs = new ArrayList<>();
    public ArrayList<Integer> p3Crabs = new ArrayList<>();
    public ArrayList<Integer> redsProc = new ArrayList<>();
    public ArrayList<Integer> dawnDrops;
    public ArrayList<PlayerDidAttack> attacksP1;




    // Thrall tracking
    public ArrayList<ThrallOutlineBox> maidenThrallSpawns;
    public ArrayList<ThrallOutlineBox> bloatThrallSpawns;
    public ArrayList<ThrallOutlineBox> nyloThrallSpawns;
    public ArrayList<ThrallOutlineBox> soteThrallSpawns;
    public ArrayList<ThrallOutlineBox> xarpusThrallSpawns;
    public ArrayList<ThrallOutlineBox> verzikThrallSpawns;

    public Map<Integer, Integer> verzikHP = new HashMap<>();

    @Override
    public Date getDate()
    {
        return raidStarted;
    }

    public String getPlayers()
    {
        StringBuilder playerString = new StringBuilder();
        for (String s : players.keySet())
        {
            playerString.append(s).append(", ");
        }
        return (playerString.length() > 2) ? playerString.substring(0, playerString.length() - 2) : "";
    }

    public int getScale()
    {
        return dataManager.get(PARTY_SIZE);
    }

    public boolean getTimeAccurate(DataPoint param)
    {
        switch (param.room)
        {
            case MAIDEN:
                return maidenStartAccurate && maidenEndAccurate;
            case BLOAT:
                return bloatStartAccurate && bloatEndAccurate;
            case NYLOCAS:
                return nyloStartAccurate && nyloEndAccurate;
            case SOTETSEG:
                return soteStartAccurate && soteEndAccurate;
            case XARPUS:
                return xarpStartAccurate && xarpEndAccurate;
            case VERZIK:
                return verzikStartAccurate && verzikEndAccurate;
            case ANY:
                return true;
            default:
                return false;
        }
    }

    public void setIndex(int index)
    {
        dataManager.set(RAID_INDEX, index);
    }

    public String getPlayerList(ArrayList<Map<String, ArrayList<String>>> aliases)
    {
        StringBuilder list = new StringBuilder();
        ArrayList<String> names = new ArrayList<>();
        for (String s : players.keySet())
        {
            String name = s;
            for (Map<String, ArrayList<String>> alternateNames : aliases)
            {
                for (String alias : alternateNames.keySet())
                {
                    for (String potentialName : alternateNames.get(alias))
                    {
                        if (name.equalsIgnoreCase(potentialName))
                        {
                            name = alias;
                            break;
                        }
                    }
                }
            }
            names.add(name);
        }
        names.sort(String::compareToIgnoreCase);
        for (String s : names)
        {
            list.append(s);
            list.append(",");
        }
        if (list.length() > 0)
        {
            return list.substring(0, list.length() - 1);
        } else
        {
            return "";
        }
    }

    public PlayerCorrelatedPointData getSpecificTimeInactiveCorrelated(String inactive)
    {
        if (inactive.contains("Player: "))
        {
            return dataManager.getHighest(DataPoint.getValue(inactive.substring(8)));
        } else
        {
            return null;
        }
    }

    public int getSpecificTimeInactive(String inactive)
    {
        if (inactive.contains("Player: "))
        {
            return dataManager.getHighest(DataPoint.getValue(inactive.substring(8))).value;
        }
        if (inactive.equals("Challenge Time"))
        {
            return getMaidenTime() + getBloatTime() + getNyloTime() + getSoteTime() + getXarpTime() + getVerzikTime();
        }
        return getValue(DataPoint.getValue(inactive));
    }

    public int getSpecificTime()
    {
        if (activeValue.contains("Player: "))
        {
            return dataManager.getHighest(DataPoint.getValue(activeValue.substring(8))).value;
        }
        if (activeValue.equals("Challenge Time"))
        {
            return getMaidenTime() + getBloatTime() + getNyloTime() + getSoteTime() + getXarpTime() + getVerzikTime();
        }
        if(getTimeAccurate(Objects.requireNonNull(DataPoint.getValue(activeValue))))
        {
            return getValue(DataPoint.getValue(activeValue));
        }
        else
        {
            return Integer.MAX_VALUE;
        }
    }

    public void setOverallTime()
    {
        int overallTime = getMaidenTime() + getBloatTime() + getNyloTime() + getSoteTime() + getXarpTime() + getVerzikTime();
        dataManager.set(DataPoint.CHALLENGE_TIME, overallTime);
    }

    public int getMaidenTime()
    {
        return (maidenStartAccurate && maidenEndAccurate) ? getValue(DataPoint.MAIDEN_TOTAL_TIME) : 0;
    }

    public int getBloatTime()
    {
        return (bloatStartAccurate && bloatEndAccurate) ? getValue(DataPoint.BLOAT_TOTAL_TIME) : 0;
    }

    public int getNyloTime()
    {
        return (nyloStartAccurate && nyloEndAccurate) ? getValue(DataPoint.NYLO_TOTAL_TIME) : 0;
    }

    public int getSoteTime()
    {
        return (soteStartAccurate && soteEndAccurate) ? getValue(DataPoint.SOTE_TOTAL_TIME) : 0;
    }

    public int getXarpTime()
    {
        return (xarpStartAccurate && xarpEndAccurate) ? getValue(DataPoint.XARP_TOTAL_TIME) : 0;
    }

    public int getVerzikTime()
    {
        return (verzikStartAccurate && verzikEndAccurate) ? getValue(DataPoint.VERZIK_TOTAL_TIME) : 0;
    }

    public boolean getOverallTimeAccurate()
    {
        return maidenStartAccurate && maidenEndAccurate
                && bloatStartAccurate && bloatEndAccurate
                && nyloStartAccurate && nyloEndAccurate
                && soteStartAccurate && soteEndAccurate
                && xarpStartAccurate && xarpEndAccurate
                && verzikStartAccurate && verzikEndAccurate;
    }

    public boolean checkExit(TOBRoom room)
    {
        if (globalData.isEmpty() || globalData.get(0).split(",", -1)[3].equals(EXIT_FLAG))
        {
            switch (room)
            {
                case MAIDEN:
                    maidenReset = true;
                    break;
                case BLOAT:
                    if (!bloatEndAccurate)
                    {
                        bloatWipe = true;
                    } else
                    {
                        bloatReset = true;
                    }
                    break;
                case NYLOCAS:
                    nyloReset = true;
                    break;
                case SOTETSEG:
                    soteReset = true;
                    break;
                case XARPUS:
                    xarpReset = true;
                    break;
            }
            return false;
        }
        return true;
    }

    private final DataManager dataManager;

    public int getValue(String name)
    {
        return dataManager.get(name);
    }

    public int getValue(DataPoint point)
    {
        return dataManager.get(point);
    }

    public int getTimeSum()
    {
        return getMaidenTime() + getBloatTime() + getNyloTime() + getSoteTime() + getXarpTime() + getVerzikTime();
    }
    public SimpleTOBData(String[] parameters, String filePath, String fileName) throws Exception
    {
        raidType = RaidType.TOB;
        this.filePath = filePath;
        this.fileName = fileName;
        dataManager = new DataManager();
        partyComplete = false;
        maidenDefenseAccurate = false;
        bloatDefenseAccurate = false;
        nyloDefenseAccurate = false;
        soteDefenseAccurate = false;
        xarpDefenseAccurate = false;

        maidenThrallSpawns = new ArrayList<>();
        bloatThrallSpawns = new ArrayList<>();
        nyloThrallSpawns = new ArrayList<>();
        soteThrallSpawns = new ArrayList<>();
        xarpusThrallSpawns = new ArrayList<>();
        verzikThrallSpawns = new ArrayList<>();

        hardMode = false;
        storyMode = false;
        attacksP1 = new ArrayList<>();
        dawnDrops = new ArrayList<>();

        players = new LinkedHashMap<>();
        globalData = new ArrayList<>(Arrays.asList(parameters));
        Date endTime = null;
        int room = -1;
        for (String s : globalData)
        {
            String[] subData = s.split(",");
            int key = Integer.parseInt(subData[3]);
            if (key == SPECTATE_FLAG)
            {
                room = Integer.parseInt(subData[4]);
                spectated = true;
            }
            if (String.valueOf(key).equals(EXIT_FLAG))
            {
                endTime = new Date(Long.parseLong(subData[1]));
            }
        }
        if (room > 0)
        {
            switch (room)
            {
                case 1:
                    if (!(checkExit(MAIDEN) && parseBloat()))
                        break;
                case 2:
                    if (!(checkExit(BLOAT) && parseNylo()))
                        break;
                case 3:
                    if (!(checkExit(NYLOCAS) && parseSotetseg()))
                        break;
                case 4:
                    if (!(checkExit(SOTETSEG) && parseXarpus()))
                        break;
                case 5:
                    if (checkExit(XARPUS) && parseVerzik())
                    {
                        finishRaid();
                    }
            }
        } else
        {
            try
            {
                if (parseMaiden())
                {
                    if (checkExit(MAIDEN) && parseBloat())
                    {
                        if (checkExit(BLOAT) && parseNylo())
                        {
                            if (checkExit(NYLOCAS) && parseSotetseg())
                            {
                                if (checkExit(SOTETSEG) && parseXarpus())
                                {
                                    if (checkExit(XARPUS) && parseVerzik())
                                    {
                                        finishRaid();
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
        setOverallTime();
        if (raidStarted != null && endTime != null)
        {
            long difference = endTime.getTime() - raidStarted.getTime();
            int ticks = (int) (difference / 600);
            dataManager.set(DataPoint.OVERALL_TIME, ticks);
            dataManager.set(DataPoint.TIME_OUTSIDE_ROOMS, dataManager.get(DataPoint.OVERALL_TIME) - dataManager.get(DataPoint.CHALLENGE_TIME));
        }
        globalData.clear();
    }

    private void parseGeneric(String room, String[] subData)
    {
        try
        {
            switch (LogID.valueOf(Integer.parseInt(subData[3])))
            {
                case ENTERED_TOB:
                    raidStarted = new Date(Long.parseLong(subData[1]));
                    break;
                case PARTY_MEMBERS:
                    for (int i = 4; i < 9; i++)
                    {
                        if (!subData[i].isEmpty())
                        {
                            dataManager.increment(PARTY_SIZE);
                            players.put(subData[i].replaceAll("[^\\p{ASCII}]", " ").replaceAll(" +", " "), 0);
                        }
                    }
                    break;
                case DWH:
                    dataManager.increment(Objects.requireNonNull(DataPoint.getValue(room + " hit hammers")));
                    dataManager.incrementPlayerSpecific(DataPoint.getValue(room + " hit hammers"), subData[4]);
                    if(!room.equals("Verzik"))
                    {
                        dataManager.hammer(Objects.requireNonNull(DataPoint.getValue(room + " defense")));
                    }
                    break;
                case BGS:
                    if (!room.equals("Bloat") || dataManager.get(DataPoint.BLOAT_DOWNS) == 0)
                    {
                        dataManager.increment(Objects.requireNonNull(DataPoint.getValue(room + " attempted BGS")));
                        dataManager.incrementPlayerSpecific(DataPoint.getValue(room + " attempted BGS"), subData[4]);
                    }
                    if(!room.equals("Verzik"))
                    {
                        dataManager.bgs(Objects.requireNonNull(DataPoint.getValue(room + " defense")), Integer.parseInt(subData[5]));
                    }
                    break;
                case PLAYER_DIED:
                    dataManager.increment(Objects.requireNonNull(DataPoint.getValue(room + " deaths")));
                    dataManager.increment(DataPoint.TOTAL_DEATHS);
                    dataManager.incrementPlayerSpecific(DataPoint.getValue(room + " deaths"), subData[4]);
                    dataManager.incrementPlayerSpecific(DataPoint.TOTAL_DEATHS, subData[4]);
                    if (players.get(subData[4]) != null)
                    {
                        players.put(subData[4], players.get(subData[4]) + 1);
                    }
                    break;
                case HAMMER_ATTEMPTED:
                    dataManager.increment(Objects.requireNonNull(DataPoint.getValue(room + " attempted hammers")));
                    dataManager.incrementPlayerSpecific(DataPoint.getValue(room + " attempted hammers"), subData[4]);
                    break;
                case IS_HARD_MODE:
                    hardMode = true;
                    break;
                case IS_STORY_MODE:
                    storyMode = true;
                    break;
                case THRALL_ATTACKED:
                    dataManager.increment(Objects.requireNonNull(DataPoint.getValue(room + " thrall attacks")));
                    dataManager.increment(DataPoint.THRALL_ATTACKS_TOTAL);
                    break;
                case THRALL_DAMAGED:
                    int amount = Integer.parseInt(subData[5]);
                    dataManager.increment(Objects.requireNonNull(DataPoint.getValue(room + " thrall damage")), amount);
                    dataManager.increment(DataPoint.THRALL_DAMAGE_TOTAL, amount);
                    break;
                case VENG_WAS_CAST:
                    dataManager.increment(Objects.requireNonNull(DataPoint.getValue(room + " veng casts")));
                    dataManager.increment(DataPoint.VENG_CASTS_TOTAL);
                    break;
                case VENG_WAS_PROCCED:
                    dataManager.increment(Objects.requireNonNull(DataPoint.getValue(room + " veng procs")));
                    dataManager.increment(DataPoint.VENG_PROCS_TOTAL);
                    dataManager.increment(Objects.requireNonNull(DataPoint.getValue(room + " veng damage")), Integer.parseInt(subData[5]));
                    dataManager.increment(DataPoint.VENG_DAMAGE_TOTAL, Integer.parseInt(subData[5]));
                    break;
                case KODAI_BOP:
                    dataManager.increment(DataPoint.KODAI_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.KODAI_BOPS, subData[4]);
                    break;
                case DWH_BOP:
                    dataManager.increment(DataPoint.DWH_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.DWH_BOPS, subData[4]);
                    break;
                case BGS_WHACK:
                    dataManager.increment(DataPoint.BGS_WHACKS);
                    dataManager.incrementPlayerSpecific(DataPoint.BGS_WHACKS, subData[4]);
                    break;
                case CHALLY_POKE:
                    dataManager.increment(DataPoint.CHALLY_POKE);
                    dataManager.incrementPlayerSpecific(DataPoint.CHALLY_POKE, subData[4]);
                    break;
            }
        }
        catch(Exception ignored)
        {

        }
    }

    private boolean parseVerzik()
    {
        int activeIndex = 0;
        for (String s : globalData)
        {
            String[] subData = s.split(",", -1);
            parseGeneric("Verzik", subData);
            try
            {
                switch (LogID.valueOf(Integer.parseInt(subData[3])))
                {
                    case LEFT_TOB:
                        if (dataManager.get(DataPoint.VERZIK_TOTAL_TIME) == 0)
                        {
                            if (!verzikStarted)
                            {
                                xarpReset = true;
                            } else
                            {
                                verzikWipe = true;
                            }
                        } else
                        {
                            return true;
                        }
                        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                        Date endTime = new Date(Long.parseLong(subData[1]));
                        long difference = endTime.getTime() - raidStarted.getTime();
                        int ticks = (int) (difference / 600);
                        dataManager.set(DataPoint.OVERALL_TIME, ticks);
                        return false;
                    case DAWN_DROPPED:
                        if (verzikStarted)
                        {
                            dawnDrops.add(Integer.parseInt(subData[4]));
                        }
                        break;
                    case VERZIK_P1_START:
                        verzikStarted = true;
                        break;
                    case VERZIK_P1_DESPAWNED:
                        dataManager.set(DataPoint.VERZIK_P1_SPLIT, Integer.parseInt(subData[4]) - 13);
                        break;
                    case VERZIK_P2_END:
                        dataManager.set(DataPoint.VERZIK_P2_SPLIT, Integer.parseInt(subData[4]));
                        dataManager.set(DataPoint.VERZIK_P2_DURATION, dataManager.get(DataPoint.VERZIK_P2_SPLIT) - dataManager.get(DataPoint.VERZIK_P1_SPLIT));
                        dataManager.set(DataPoint.VERZIK_REDS_DURATION, dataManager.get(DataPoint.VERZIK_P2_SPLIT)-dataManager.get(VERZIK_REDS_SPLIT));
                        try
                        {
                            int hp = verzikHP.get(dataManager.get(VERZIK_REDS_SPLIT));
                            dataManager.set(DataPoint.VERZIK_REDS_PROC_PERCENT, (hp));
                        } catch
                        (Exception ignored)
                        {

                        }
                        break;
                    case VERZIK_P3_DESPAWNED:
                        dataManager.set(DataPoint.VERZIK_TOTAL_TIME, Integer.parseInt(subData[4]));
                        dataManager.set(DataPoint.VERZIK_P3_DURATION, dataManager.get(DataPoint.VERZIK_TOTAL_TIME) - dataManager.get(DataPoint.VERZIK_P2_SPLIT));
                        dataManager.set(DataPoint.CHALLENGE_TIME, (Integer.parseInt(subData[4]) + dataManager.get(DataPoint.VERZIK_ENTRY)));
                        break;
                    case VERZIK_CRAB_SPAWNED:
                        if (!subData[4].equalsIgnoreCase(""))
                        {
                            if (dataManager.get(DataPoint.VERZIK_P2_SPLIT) > 1)
                            {
                                if (!p3Crabs.contains(Integer.parseInt(subData[4])))
                                {
                                    p3Crabs.add(Integer.parseInt(subData[4]));
                                }
                            } else
                            {
                                if (!p2Crabs.contains(Integer.parseInt(subData[4])))
                                {
                                    p2Crabs.add(Integer.parseInt(subData[4]));
                                }
                            }
                        }
                        dataManager.increment(VERZIK_CRABS_SPAWNED);
                        break;
                    case VERZIK_P2_REDS_PROC:
                        if (dataManager.get(VERZIK_REDS_SPLIT) == 0)
                        {
                            dataManager.set(VERZIK_REDS_SPLIT, Integer.parseInt(subData[4]));
                            dataManager.set(VERZIK_P2_TILL_REDS, Integer.parseInt(subData[4]) - dataManager.get(DataPoint.VERZIK_P1_SPLIT));
                        }
                        redsProc.add(Integer.parseInt(subData[4]));
                        dataManager.increment(DataPoint.VERZIK_REDS_SETS);
                        break;
                    case ACCURATE_VERZIK_START:
                        verzikStartAccurate = true;
                        break;
                    case ACCURATE_VERZIK_END:
                        verzikEndAccurate = true;
                        verzikTimeAccurate = verzikStartAccurate;
                        break;
                    case DAWN_SPEC:
                        dawnSpecs.add(new DawnSpec(subData[4], Integer.parseInt(subData[5])));
                        break;
                    case DAWN_DAMAGE:
                        for (DawnSpec dawnSpec : dawnSpecs)
                        {
                            if (dawnSpec.tick == Integer.parseInt(subData[5]))
                            {
                                dawnSpec.setDamage(Integer.parseInt(subData[4]));
                            }
                        }
                        break;
                    case UPDATE_HP:
                        verzikHP.put(Integer.parseInt(subData[5]), Integer.parseInt(subData[4]));
                        break;
                    case WEBS_STARTED:
                        try
                        {
                            websStart.add(Integer.parseInt(subData[4]));
                            if (dataManager.get(DataPoint.VERZIK_HP_AT_WEBS) == -1)
                            {
                                int hp = verzikHP.get(Integer.parseInt(subData[4]) - 1);
                                hp /= 10;
                                dataManager.set(DataPoint.VERZIK_HP_AT_WEBS, hp);
                            }
                        }
                        catch(Exception ignored)
                        {

                        }
                        break;
                    case THRALL_SPAWN:
                        verzikThrallSpawns.add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                        break;
                }
            } catch (Exception e)
            {
                log.info("Failed on " + s);
            }
            activeIndex++;
        }
        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean isTimeAccurateThroughRoom(TOBRoom room)
    {
        switch (room)
        {
            case VERZIK:
                if (!verzikTimeAccurate)
                {
                    return false;
                }
            case XARPUS:
                if (!xarpTimeAccurate)
                {
                    return false;
                }
            case SOTETSEG:
                if (!soteTimeAccurate)
                {
                    return false;
                }
            case NYLOCAS:
                if (!nyloTimeAccurate)
                {
                    return false;
                }
            case BLOAT:
                if (!bloatTimeAccurate)
                {
                    return false;
                }
            case MAIDEN:
                return maidenTimeAccurate;
        }
        return false;
    }

    private boolean parseXarpus()
    {

        int activeIndex = 0;
        loop:
        for (String s : globalData)
        {
            String[] subData = s.split(",", -1);
            parseGeneric("Xarp", subData);
            try
            {
                switch (LogID.valueOf(Integer.parseInt(subData[3])))
                {
                    case LEFT_TOB:
                        if (dataManager.get(DataPoint.XARP_TOTAL_TIME) != 0)
                        {
                            xarpReset = true;
                        } else
                        {
                            if (!xarpStarted)
                            {
                                soteReset = true;
                            } else
                            {
                                xarpWipe = true;
                            }
                        }
                        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                        Date endTime = new Date(Long.parseLong(subData[1]));
                        long difference = endTime.getTime() - raidStarted.getTime();
                        int ticks = (int) (difference / 600);
                        dataManager.set(DataPoint.OVERALL_TIME, ticks);
                        return false;
                    case XARPUS_STARTED:
                        xarpStarted = true;
                        if (partyComplete)
                        {
                            xarpDefenseAccurate = true;
                        }
                        break;
                    case XARPUS_HEAL:
                        dataManager.increment(DataPoint.XARP_HEALING, getXarpusHealAmount());
                        break;
                    case XARPUS_SCREECH:
                        dataManager.set(DataPoint.XARP_SCREECH, Integer.parseInt(subData[4]));
                        break;
                    case XARPUS_DESPAWNED:
                        dataManager.set(DataPoint.XARP_TOTAL_TIME, Integer.parseInt(subData[4]));
                        dataManager.set(DataPoint.XARP_POST_SCREECH, dataManager.get(DataPoint.XARP_TOTAL_TIME) - dataManager.get(DataPoint.XARP_SCREECH));
                        if (isTimeAccurateThroughRoom(SOTETSEG))
                            dataManager.set(DataPoint.VERZIK_ENTRY, Integer.parseInt(subData[4]) + dataManager.get(DataPoint.XARP_ENTRY));
                        break loop;
                    case ACCURATE_XARP_START:
                        xarpStartAccurate = true;
                        break;
                    case ACCURATE_XARP_END:
                        xarpTimeAccurate = xarpStartAccurate;
                        xarpEndAccurate = true;
                        break;
                    case THRALL_SPAWN:
                        xarpusThrallSpawns.add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                        break;
                }
            }
            catch(Exception e)
            {
                log.info("Failed on " + s);
            }
            activeIndex++;
        }
        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private int getXarpusHealAmount()
    {
        int amount = 0;
        switch (getScale())
        {
            case 5:
                amount = 8;
                break;
            case 4:
                amount = 9;
                break;
            case 3:
                amount = 12;
                break;
            case 2:
                amount = 16;
                break;
            case 1:
                amount = 20;
                break;
        }
        return amount;
    }

    private boolean parseSotetseg()
    {
        int activeIndex = 0;
        loop:
        for (String s : globalData)
        {
            String[] subData = s.split(",", -1);
            parseGeneric("Sote", subData);
            try
            {
                switch (LogID.valueOf(Integer.parseInt(subData[3])))
                {
                    case DWH:
                        if (dataManager.get(DataPoint.SOTE_P1_SPLIT) == 0)
                        {
                            dataManager.increment(DataPoint.SOTE_SPECS_P1);
                            dataManager.increment(DataPoint.SOTE_SPECS_TOTAL);
                        } else if (dataManager.get(DataPoint.SOTE_P2_SPLIT) == 0)
                        {
                            dataManager.increment(DataPoint.SOTE_SPECS_P2);
                            dataManager.increment(DataPoint.SOTE_SPECS_TOTAL);
                        } else
                        {
                            dataManager.increment(DataPoint.SOTE_SPECS_P3);
                            dataManager.increment(DataPoint.SOTE_SPECS_TOTAL);
                        }
                        break;
                    case LEFT_TOB:
                        if (dataManager.get(DataPoint.SOTE_TOTAL_TIME) != 0)
                        {
                            soteReset = true;
                        } else
                        {
                            if (!soteStarted)
                            {
                                nyloReset = true;
                            } else
                            {
                                soteWipe = true;
                            }
                        }
                        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                        Date endTime = new Date(Long.parseLong(subData[1]));
                        long difference = endTime.getTime() - raidStarted.getTime();
                        int ticks = (int) (difference / 600);
                        dataManager.set(DataPoint.OVERALL_TIME, ticks);
                        return false;
                    case SOTETSEG_STARTED:
                        soteStarted = true;
                        if (partyComplete)
                        {
                            soteDefenseAccurate = true;
                        }
                        break;
                    case SOTETSEG_FIRST_MAZE_STARTED:
                        dataManager.set(DataPoint.SOTE_P1_SPLIT, Integer.parseInt(subData[4]));
                        break;
                    case SOTETSEG_FIRST_MAZE_ENDED:
                        dataManager.set(DataPoint.SOTE_M1_DURATION, Integer.parseInt(subData[4]) - dataManager.get(DataPoint.SOTE_P1_SPLIT));
                        dataManager.set(DataPoint.SOTE_M1_SPLIT, Integer.parseInt(subData[4]));
                        break;
                    case SOTETSEG_SECOND_MAZE_STARTED:
                        dataManager.set(DataPoint.SOTE_P2_DURATION, Integer.parseInt(subData[4]) - dataManager.get(DataPoint.SOTE_M1_SPLIT));
                        dataManager.set(DataPoint.SOTE_P2_SPLIT, Integer.parseInt(subData[4]));
                        break;
                    case SOTETSEG_SECOND_MAZE_ENDED:
                        dataManager.set(DataPoint.SOTE_M2_DURATION, Integer.parseInt(subData[4]) - dataManager.get(DataPoint.SOTE_P2_SPLIT));
                        dataManager.set(DataPoint.SOTE_M2_SPLIT, Integer.parseInt(subData[4]));
                        dataManager.set(DataPoint.SOTE_MAZE_SUM, dataManager.get(DataPoint.SOTE_M1_DURATION) + dataManager.get(DataPoint.SOTE_M2_DURATION));
                        break;
                    case SOTETSEG_ENDED:
                        dataManager.set(DataPoint.SOTE_TOTAL_TIME, Integer.parseInt(subData[4]));
                        dataManager.set(DataPoint.SOTE_P3_DURATION, dataManager.get(DataPoint.SOTE_TOTAL_TIME) - dataManager.get(DataPoint.SOTE_M2_SPLIT));
                        if (isTimeAccurateThroughRoom(NYLOCAS))
                            dataManager.set(DataPoint.XARP_ENTRY, Integer.parseInt(subData[4]) + dataManager.get(DataPoint.SOTE_ENTRY));
                        break loop;
                    case ACCURATE_SOTE_START:
                        soteStartAccurate = true;
                        break;
                    case ACCURATE_SOTE_END:
                        soteEndAccurate = true;
                        soteTimeAccurate = soteStartAccurate;
                        if (soteTimeAccurate && bloatTimeAccurate && !spectated)
                        {
                            nyloStartAccurate = true;
                            nyloEndAccurate = true;
                            nyloTimeAccurate = true;
                        }

                        break;
                    case THRALL_SPAWN:
                        soteThrallSpawns.add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                        break;

                }
            }
            catch(Exception e)
            {
                log.info("Failed on " + s);
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
            parseGeneric("Nylo", subData);
            try
            {
                switch (LogID.valueOf(Integer.parseInt(subData[3])))
                {
                    case LEFT_TOB:
                        if (dataManager.get(NYLOCAS_PILLAR_DESPAWN_TICK) - 5 < dataManager.get(DataPoint.NYLO_BOSS_SPAWN))
                        {
                            dataManager.set(DataPoint.NYLO_BOSS_SPAWN, 0);
                            dataManager.set(DataPoint.NYLO_TOTAL_TIME, 0);
                        }
                        if (dataManager.get(DataPoint.NYLO_TOTAL_TIME) != 0)
                        {
                            nyloReset = true;
                        } else
                        {
                            if (!nyloStarted)
                            {
                                if (!bloatEndAccurate)
                                {
                                    bloatWipe = true;
                                } else
                                {
                                    bloatReset = true;
                                }
                            } else
                            {
                                nyloWipe = true;
                            }
                        }
                        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                        Date endTime = new Date(Long.parseLong(subData[1]));
                        long difference = endTime.getTime() - raidStarted.getTime();
                        int ticks = (int) (difference / 600);
                        dataManager.set(DataPoint.OVERALL_TIME, ticks);
                        return false;
                    case NYLO_PILLAR_SPAWN:
                        nyloStarted = true;
                        break;
                    case NYLO_STALL:
                        nyloWaveStalled.add(Integer.parseInt(subData[5]));
                        dataManager.increment(DataPoint.NYLO_STALLS_TOTAL);
                        if (Integer.parseInt(subData[4]) > 19)
                        {
                            dataManager.increment(DataPoint.NYLO_STALLS_POST_20);
                        } else
                        {
                            dataManager.increment(DataPoint.NYLO_STALLS_PRE_20);
                        }
                        break;
                    case RANGE_SPLIT:
                        dataManager.increment(DataPoint.NYLO_SPLITS_RANGE);
                        break;
                    case MAGE_SPLIT:
                        dataManager.increment(DataPoint.NYLO_SPLITS_MAGE);
                        break;
                    case MELEE_SPLIT:
                        dataManager.increment(DataPoint.NYLO_SPLITS_MELEE);
                        break;
                    case LAST_WAVE:
                        dataManager.set(DataPoint.NYLO_LAST_WAVE, Integer.parseInt(subData[4]));
                        break;
                    case LAST_DEAD:
                        int nyloLastDead = Integer.parseInt(subData[4]);
                        dataManager.set(NYLO_LAST_DEAD, nyloLastDead);
                        int offset = 20 - (nyloLastDead % 4); //4 cycle (16 tick) delay for boss + difference to cycle (4-time%instance reference)
                        dataManager.set(DataPoint.NYLO_BOSS_SPAWN, nyloLastDead + offset);
                        dataManager.set(DataPoint.NYLO_CLEANUP, nyloLastDead - dataManager.get(DataPoint.NYLO_LAST_WAVE));
                        break;
                    case NYLO_WAVE:
                        waveSpawns.put(Integer.parseInt(subData[4]), Integer.parseInt(subData[5]));
                        break;
                    case BOSS_SPAWN: //The number people are used to seeing on timers for boss spawn is actually 2 ticks prior to the spawn call for the boss
                        dataManager.set(DataPoint.NYLO_BOSS_SPAWN, Integer.parseInt(subData[4]) - 2);
                        if (partyComplete)
                        {
                            nyloDefenseAccurate = true;
                        }
                        break;
                    case MELEE_PHASE:
                        dataManager.increment(DataPoint.NYLO_ROTATIONS_MELEE);
                        break;
                    case MAGE_PHASE:
                        dataManager.increment(DataPoint.NYLO_ROTATIONS_MAGE);
                        break;
                    case RANGE_PHASE:
                        dataManager.increment(DataPoint.NYLO_ROTATIONS_RANGE);
                        break;
                    case NYLO_DESPAWNED:
                        if (Integer.parseInt(subData[4]) - dataManager.get(DataPoint.NYLO_BOSS_SPAWN) > 30)
                        {
                            dataManager.set(DataPoint.NYLO_TOTAL_TIME, Integer.parseInt(subData[4]));
                            dataManager.set(DataPoint.NYLO_BOSS_DURATION, dataManager.get(DataPoint.NYLO_TOTAL_TIME) - dataManager.get(DataPoint.NYLO_BOSS_SPAWN));
                            if (isTimeAccurateThroughRoom(BLOAT))
                                dataManager.set(DataPoint.SOTE_ENTRY, Integer.parseInt(subData[4]) + dataManager.get(DataPoint.NYLO_ENTRY));
                        }
                        break loop;
                    case NYLO_PILLAR_DESPAWNED:
                        dataManager.set(NYLOCAS_PILLAR_DESPAWN_TICK, Integer.parseInt(subData[4]));
                        break;
                    case ACCURATE_NYLO_START:
                        nyloStartAccurate = true;
                        break;
                    case ACCURATE_NYLO_END:
                        nyloEndAccurate = true;
                        nyloTimeAccurate = nyloStartAccurate;
                        break;
                    case THRALL_SPAWN:
                        nyloThrallSpawns.add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                        break;
                }
            }
            catch(Exception e)
            {
                log.info("Failed on " + s);
            }
            activeIndex++;
        }
        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean parseBloat()
    {
        int activeIndex = 0;
        bloatDefenseAccurate = maidenDefenseAccurate;
        loop:
        for (String s : globalData)
        {
            String[] subData = s.split(",", -1);
            parseGeneric("Bloat", subData);
            try
            {
                switch (LogID.valueOf(Integer.parseInt(subData[3])))
                {
                    case BLOAT_HP_1ST_DOWN:
                        dataManager.set(DataPoint.BLOAT_HP_FIRST_DOWN, Integer.parseInt(subData[4]) / 10);
                        break;
                    case BLOAT_SCYTHE_1ST_WALK:
                        if (dataManager.get(DataPoint.BLOAT_DOWNS) == 0)
                        {
                            dataManager.increment(DataPoint.BLOAT_FIRST_WALK_SCYTHES);
                            dataManager.incrementPlayerSpecific(DataPoint.BLOAT_FIRST_WALK_SCYTHES, subData[4]);
                        }
                        break;
                    case LEFT_TOB:
                        if (bloatEndAccurate)
                        {
                            bloatReset = true;
                        } else
                        {
                            if (!bloatStarted)
                            {
                                maidenReset = true;
                            } else
                            {
                                bloatWipe = true;
                            }
                        }
                        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                        Date endTime = new Date(Long.parseLong(subData[1]));
                        long difference = endTime.getTime() - raidStarted.getTime();
                        int ticks = (int) (difference / 600);
                        dataManager.set(DataPoint.OVERALL_TIME, ticks);
                        return false;
                    case PLAYER_DIED:
                        if (dataManager.get(DataPoint.BLOAT_DOWNS) == 0)
                        {
                            dataManager.increment(DataPoint.BLOAT_FIRST_WALK_DEATHS);
                            dataManager.incrementPlayerSpecific(DataPoint.BLOAT_FIRST_WALK_DEATHS, subData[4]);
                        }
                        break;
                    case BLOAT_SPAWNED:
                        bloatStarted = true;
                        if (partyComplete)
                        {
                            bloatDefenseAccurate = true;
                        }
                        break;
                    case BLOAT_DOWN:
                        if (dataManager.get(DataPoint.BLOAT_DOWNS) == 0)
                        {
                            dataManager.set(DataPoint.BLOAT_FIRST_DOWN_TIME, Integer.parseInt(subData[4]));
                        }
                        dataManager.increment(DataPoint.BLOAT_DOWNS);
                        bloatDowns.add(Integer.parseInt(subData[4]));
                        break;
                    case BLOAT_DESPAWN:
                        dataManager.set(DataPoint.BLOAT_TOTAL_TIME, Integer.parseInt(subData[4]));
                        if (isTimeAccurateThroughRoom(MAIDEN))
                            dataManager.set(DataPoint.NYLO_ENTRY, Integer.parseInt(subData[4]) + dataManager.get(DataPoint.MAIDEN_TOTAL_TIME));
                        break loop;
                    case ACCURATE_BLOAT_START:
                        bloatStartAccurate = true;
                        break;
                    case ACCURATE_BLOAT_END:
                        bloatEndAccurate = true;
                        bloatTimeAccurate = bloatStartAccurate;
                        break;
                    case THRALL_SPAWN:
                        bloatThrallSpawns.add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                        break;
                }
            }
            catch(Exception e)
            {
                log.info("Failed on " + s);
            }
            activeIndex++;
        }
        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean parseMaiden()
    {
        int activeIndex = 0;
        String lastProc = " 70s";
        loop:
        for (String s : globalData)
        {
            String[] subData = s.split(",", -1);
            parseGeneric("Maiden", subData);
            try
            {
                switch (LogID.valueOf(Integer.parseInt(subData[3])))
                {
                    case LEFT_TOB:
                        int percent = 100;
                        if (dataManager.get(DataPoint.MAIDEN_CHINS_THROWN) != 0)
                        {
                            double percentDouble = ((double) (dataManager.get(DataPoint.MAIDEN_CHINS_THROWN_WRONG_DISTANCE)) / dataManager.get(DataPoint.MAIDEN_CHINS_THROWN)) * 100;
                            percent = (int) percentDouble;
                        }
                        dataManager.set(DataPoint.MAIDEN_CHIN_CORRECT_DISTANCE_PERCENT, percent);
                        if (dataManager.get(DataPoint.MAIDEN_TOTAL_TIME) != 0)
                        {
                            maidenReset = true;
                        } else
                        {
                            if (!maidenSpawned)
                            {
                                maidenReset = true;
                                resetBeforeMaiden = true;
                            } else
                            {
                                maidenWipe = true;
                            }
                        }
                        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                        Date endTime = new Date(Long.parseLong(subData[1]));
                        long difference = endTime.getTime() - raidStarted.getTime();
                        int ticks = (int) (difference / 600);
                        dataManager.set(DataPoint.OVERALL_TIME, ticks);
                        return false;
                    case BLOOD_THROWN:
                        dataManager.increment(DataPoint.MAIDEN_BLOOD_THROWN);
                        break;
                    case BLOOD_SPAWNED:
                        dataManager.increment(DataPoint.MAIDEN_BLOOD_SPAWNED);
                        break;
                    case CRAB_LEAK:
                        if (dataManager.get(DataPoint.MAIDEN_TOTAL_TIME) == 0) //TODO: see case 16 fix
                        {
                            dataManager.increment(DataPoint.MAIDEN_CRABS_LEAKED);
                            int crabHP = -1;
                            try
                            {
                                crabHP = Integer.parseInt(subData[5]);

                                maidenCrabs.add(new StringInt(subData[4], crabHP));
                            } catch (Exception ignored)
                            {
                            }
                            dataManager.increment(DataPoint.MAIDEN_HP_HEALED, crabHP * 2);
                            int maxCrabHP = 100;
                            switch (players.size())
                            {
                                case 1:
                                case 2:
                                case 3:
                                    maxCrabHP = 75;
                                    break;
                                case 4:
                                    maxCrabHP = 87;
                                    break;
                            }
                            if (crabHP == maxCrabHP)
                            {
                                dataManager.increment(DataPoint.MAIDEN_CRABS_LEAKED_FULL_HP);
                            }

                            if (subData[4].contains("30"))
                            {
                                maidenSkip = false;
                            }
                        }
                        break;
                    case MAIDEN_PLAYER_DRAINED:
                        dataManager.increment(DataPoint.MAIDEN_MELEE_DRAINS);
                        dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_MELEE_DRAINS, subData[4]);
                        break;
                    case MAIDEN_SPAWNED:
                        maidenSpawned = true;
                        if (partyComplete)
                        {
                            maidenDefenseAccurate = true;
                        }
                        break;
                    case MAIDEN_70S:
                        dataManager.set(DataPoint.MAIDEN_70_SPLIT, Integer.parseInt(subData[4]));
                        lastProc = " 70s";
                        break;
                    case MAIDEN_50S:
                        dataManager.set(DataPoint.MAIDEN_50_SPLIT, Integer.parseInt(subData[4]));
                        dataManager.set(DataPoint.MAIDEN_7050_SPLIT, Integer.parseInt(subData[4]) - dataManager.get(DataPoint.MAIDEN_70_SPLIT));
                        lastProc = " 50s";
                        break;
                    case MAIDEN_30S:
                        dataManager.set(DataPoint.MAIDEN_30_SPLIT, Integer.parseInt(subData[4]));
                        dataManager.set(DataPoint.MAIDEN_5030_SPLIT, Integer.parseInt(subData[4]) - dataManager.get(DataPoint.MAIDEN_50_SPLIT));
                        lastProc = " 30s";
                        break;
                    case MAIDEN_0HP:
                        dataManager.set(DataPoint.MAIDEN_TOTAL_TIME, Integer.parseInt(subData[4]) + 7);
                        dataManager.set(DataPoint.MAIDEN_SKIP_SPLIT, dataManager.get(DataPoint.MAIDEN_TOTAL_TIME) - dataManager.get(DataPoint.MAIDEN_30_SPLIT));
                        if (globalData.get(activeIndex + 1).split(",", -1)[3].equals("4"))
                            maidenReset = true;
                        break loop;
                    case MAIDEN_DESPAWNED:
                        dataManager.set(DataPoint.MAIDEN_TOTAL_TIME, Integer.parseInt(subData[4]));
                        dataManager.set(DataPoint.MAIDEN_SKIP_SPLIT, dataManager.get(DataPoint.MAIDEN_TOTAL_TIME) - dataManager.get(DataPoint.MAIDEN_30_SPLIT));
                        if (globalData.get(activeIndex + 1).split(",", -1)[3].equals("4"))
                            maidenReset = true;
                        break loop;
                    case MATOMENOS_SPAWNED:
                        maidenCrabSpawn.add(subData[4]);
                        break;
                    case MAIDEN_SCUFFED:
                        if (!maidenScuffed)
                        {
                            firstMaidenCrabScuffed = lastProc;
                            if(!maidenCrabSpawn.isEmpty())
                            {
                                firstMaidenCrabScuffed = maidenCrabSpawn.get(maidenCrabSpawn.size()-1);
                            }
                        }
                        maidenScuffed = true;
                        break;
                    case BLOAT_SPAWNED:
                        //todo: joined after maiden was kill. mark this somehow?
                        maidenReset = true; //TODO remove
                        break loop;
                    case SPECTATE:
                        spectated = true;
                        break;
                    case MAIDEN_DINHS_SPEC:
                        dataManager.increment(DataPoint.MAIDEN_DINHS_SPECS);
                        dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_DINHS_SPECS, subData[4]);
                        String[] targets = subData[6].split(":");
                        int targetCountThisSpec = 0;
                        int crabCountThisSpec = 0;
                        String[] specData = subData[7].split(":");
                        if (specData.length != 5)
                        {
                            break;
                        }
                        for (String target : targets)
                        {
                            String[] targetData = target.split("~");
                            if (targetData.length == 3)
                            {
                                targetCountThisSpec++;
                                String spawnID = targetData[1];
                                if (!spawnID.equals("^")) //Target is crab
                                {
                                    crabCountThisSpec++;
                                }
                            }
                        }
                        int averageHP = Integer.parseInt(specData[0]);
                        int belowThreshold = Integer.parseInt(specData[1]);
                        if (dataManager.get(DataPoint.MAIDEN_DINHS_SPECS) == 0)
                        {
                            int percentCrabsTargeted = (int) ((((double) crabCountThisSpec) / targetCountThisSpec) * 100);
                            int percentCrabsUnder27Targeted = (int) ((((double) belowThreshold) / crabCountThisSpec) * 100);

                            dataManager.set(DataPoint.MAIDEN_DINHS_TARGETS_HIT, targetCountThisSpec);
                            dataManager.set(DataPoint.MAIDEN_DINHS_CRABS_HIT, crabCountThisSpec);
                            dataManager.set(DataPoint.MAIDEN_DINHS_AVERAGE_HP_HIT, averageHP);
                            dataManager.set(DataPoint.MAIDEN_DINHS_CRABS_UNDER_27_TARGETED, belowThreshold);
                            dataManager.set(DataPoint.MAIDEN_DINHS_PERCENT_TARGETS_CRAB, percentCrabsTargeted);
                            dataManager.set(DataPoint.MAIDEN_DINHS_CRABS_UNDER_27_TARGETED_PERCENT, percentCrabsUnder27Targeted);
                        } else
                        {
                            int previousAverage = dataManager.get(DataPoint.MAIDEN_DINHS_AVERAGE_HP_HIT);
                            int previousCrabsHit = dataManager.get(DataPoint.MAIDEN_DINHS_CRABS_HIT);
                            int previousTotalHit = dataManager.get(DataPoint.MAIDEN_DINHS_TARGETS_HIT);
                            int previousBelow27Hit = dataManager.get(DataPoint.MAIDEN_DINHS_CRABS_UNDER_27_TARGETED);

                            dataManager.set(DataPoint.MAIDEN_DINHS_TARGETS_HIT, previousTotalHit + targetCountThisSpec);
                            dataManager.set(DataPoint.MAIDEN_DINHS_CRABS_HIT, previousCrabsHit + crabCountThisSpec);
                            dataManager.set(DataPoint.MAIDEN_DINHS_CRABS_UNDER_27_TARGETED, previousBelow27Hit + belowThreshold);

                            int roundedAverageSum = ((previousAverage * previousCrabsHit) + (averageHP * crabCountThisSpec));
                            int roundedAverageCumulative = (int) (((double) roundedAverageSum) / (previousCrabsHit + crabCountThisSpec));

                            dataManager.set(DataPoint.MAIDEN_DINHS_AVERAGE_HP_HIT, roundedAverageCumulative);

                            int percentTargetedCumulative = (int) (((double) (previousCrabsHit + crabCountThisSpec) / (previousTotalHit + targetCountThisSpec)) * 100);

                            dataManager.set(DataPoint.MAIDEN_DINHS_PERCENT_TARGETS_CRAB, percentTargetedCumulative);

                            int percentBelow27Cumulative = (int) ((double) (previousBelow27Hit + belowThreshold) / (previousCrabsHit + crabCountThisSpec));

                            dataManager.set(DataPoint.MAIDEN_DINHS_CRABS_UNDER_27_TARGETED_PERCENT, percentBelow27Cumulative);
                        }
                        break;
                    case MAIDEN_CHIN_THROWN:
                        dataManager.increment(DataPoint.MAIDEN_CHINS_THROWN);
                        dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_CHINS_THROWN, subData[4]);
                        if (Integer.parseInt(subData[5]) < 4 || Integer.parseInt(subData[5]) > 6)
                        {
                            dataManager.increment(DataPoint.MAIDEN_CHINS_THROWN_WRONG_DISTANCE);
                            dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_CHINS_THROWN_WRONG_DISTANCE, subData[4]);
                        }
                        break;
                    case ACCURATE_MAIDEN_START:
                        maidenStartAccurate = true;
                        break;
                    case ACCURATE_MAIDEN_END:
                        maidenEndAccurate = true;
                        maidenTimeAccurate = maidenStartAccurate;
                        break;
                    case THRALL_SPAWN:
                        maidenThrallSpawns.add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                        break;
                    case PLAYER_STOOD_IN_THROWN_BLOOD:
                        dataManager.increment(DataPoint.MAIDEN_PLAYER_STOOD_IN_THROWN_BLOOD);
                        dataManager.increment(DataPoint.MAIDEN_HEALS_FROM_THROWN_BLOOD, Integer.parseInt(subData[5]));

                        dataManager.increment(DataPoint.MAIDEN_PLAYER_STOOD_IN_BLOOD);
                        dataManager.increment(DataPoint.MAIDEN_HEALS_FROM_ANY_BLOOD, Integer.parseInt(subData[5]));

                        dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_PLAYER_STOOD_IN_THROWN_BLOOD, subData[4]);
                        dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_HEALS_FROM_THROWN_BLOOD, subData[4], Integer.parseInt(subData[5]));

                        dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_PLAYER_STOOD_IN_BLOOD, subData[4]);
                        dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_HEALS_FROM_ANY_BLOOD, subData[4], Integer.parseInt(subData[5]));
                        break;
                    case PLAYER_STOOD_IN_SPAWNED_BLOOD:
                        dataManager.increment(DataPoint.MAIDEN_PLAYER_STOOD_IN_SPAWNED_BLOOD);
                        dataManager.increment(DataPoint.MAIDEN_HEALS_FROM_SPAWNED_BLOOD, Integer.parseInt(subData[5]));

                        dataManager.increment(DataPoint.MAIDEN_PLAYER_STOOD_IN_BLOOD);
                        dataManager.increment(DataPoint.MAIDEN_HEALS_FROM_ANY_BLOOD, Integer.parseInt(subData[5]));

                        dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_PLAYER_STOOD_IN_SPAWNED_BLOOD, subData[4]);
                        dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_HEALS_FROM_SPAWNED_BLOOD, subData[4], Integer.parseInt(subData[5]));

                        dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_PLAYER_STOOD_IN_BLOOD, subData[4]);
                        dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_HEALS_FROM_ANY_BLOOD, subData[4], Integer.parseInt(subData[5]));
                        break;
                }
            }
            catch(Exception e)
            {
                log.info("Failed on " + s);
            }
            activeIndex++;
        }
        globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private void finishRaid()
    {
        raidCompleted = true;
    }


    public String getScaleString()
    {
        String scaleString = "";
        switch (players.size())
        {
            case 1:
                scaleString = "Solo";
                break;
            case 2:
                scaleString = "Duo";
                break;
            case 3:
                scaleString = "Trio";
                break;
            case 4:
                scaleString = "4 Man";
                break;
            case 5:
                scaleString = "5 Man";
                break;
        }
        if (storyMode)
        {
            scaleString += " (Story)";
        }
        if (hardMode)
        {
            scaleString += " (Hard)";
        }
        return scaleString;
    }

    public String getRoomStatus()
    {
        String raidStatusString;
        if (maidenWipe)
        {
            raidStatusString = "Maiden Wipe";
        } else if (maidenReset)
        {
            raidStatusString = "Maiden Reset";
            if (!maidenSpawned)
            {
                raidStatusString += "*";
            }
        } else if (bloatWipe)
        {
            raidStatusString = "Bloat Wipe";
        } else if (bloatReset)
        {
            raidStatusString = "Bloat Reset";
            if (getBloatTime() == 0)
            {
                raidStatusString += "*";
            }
        } else if (nyloWipe)
        {
            raidStatusString = "Nylo Wipe";
        } else if (nyloReset)
        {
            raidStatusString = "Nylo Reset";
            if (getNyloTime() == 0)
            {
                raidStatusString += "*";
            }
        } else if (soteWipe)
        {
            raidStatusString = "Sotetseg Wipe";
        } else if (soteReset)
        {
            raidStatusString = "Sotetseg Reset";
            if (getSoteTime() == 0)
            {
                raidStatusString += "*";
            }
        } else if (xarpWipe)
        {
            raidStatusString = "Xarpus Wipe";
        } else if (xarpReset)
        {
            raidStatusString = "Xarpus Reset";
            if (getXarpTime() == 0)
            {
                raidStatusString += "*";
            }
        } else if (verzikWipe)
        {
            raidStatusString = "Verzik Wipe";
        } else
        {
            raidStatusString = "Completion";
            if (!getOverallTimeAccurate())
            {
                raidStatusString += "*";
            }
        }
        String red = "<html><font color='#FF0000'>";
        String green = "<html><font color='#44AF33'>";
        String yellow = "<html><font color='#EEEE44'>";
        if (raidStatusString.contains("Completion"))
        {
            raidStatusString = green + raidStatusString;
        } else if (raidStatusString.contains("Reset"))
        {
            raidStatusString = yellow + raidStatusString;
        } else
        {
            raidStatusString = red + raidStatusString;
        }
        return raidStatusString;
    }
}

