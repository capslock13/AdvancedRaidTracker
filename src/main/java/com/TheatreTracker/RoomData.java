package com.TheatreTracker;

import com.TheatreTracker.panelcomponents.DefenseReductionOutlineBox;
import com.TheatreTracker.utility.*;
import com.TheatreTracker.utility.nyloutility.DawnSpec;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.*;

import static com.TheatreTracker.RoomData.TobRoom.*;

@Slf4j

public class RoomData {

    private static final String EXIT_FLAG = "4";
    public boolean spectated = false;
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

    public ArrayList<Integer> websStart = new ArrayList<>();

    public boolean maidenTimeAccurate;
    public boolean partyComplete;

    public boolean maidenDefenseAccurate;
    public int index = -1;

    public boolean maidenScuffed = false;
    public String firstMaidenCrabScuffed = "";

    public boolean maidenSpawned = false;
    public boolean maidenSkip;
    public boolean maidenReset;
    public boolean maidenWipe;

    public boolean hardMode;
    public boolean storyMode;
    public ArrayList<Integer> p2Crabs = new ArrayList<>();
    public ArrayList<Integer> p3Crabs = new ArrayList<>();
    public ArrayList<Integer> redsProc = new ArrayList<>();
    public ArrayList<PlayerBounce> bounces = new ArrayList<>();
    public Map<Integer, Integer> waveSpawns = new HashMap<>();

    public ArrayList<Integer> bloatDowns = new ArrayList<>();


    public boolean bloatTimeAccurate;
    public boolean bloatDefenseAccurate;
    public int bloatHPAtDown;
    public int bloatScytheBeforeFirstDown;

    public boolean bloatStarted;
    public boolean bloatReset;
    public boolean bloatWipe;


    public boolean nyloTimeAccurate;

    public boolean nyloDefenseAccurate;
    public int nyloDeaths;

    public int nyloLastDead;

    public int pillarDespawnTick;

    public boolean nyloWipe;
    public boolean nyloReset;
    public boolean nyloStarted;
    public ArrayList<Integer> nyloWaveStalled = new ArrayList<>();
    public ArrayList<DawnSpec> dawnSpecs = new ArrayList<>();


    public boolean soteTimeAccurate;
    public boolean soteDefenseAccurate;
    public int soteCyclesLost = -1;

    public boolean soteStarted;
    public boolean soteWipe;
    public boolean soteReset;


    public boolean xarpTimeAccurate;
    public boolean xarpDefenseAccurate;

    public boolean xarpWipe;
    public boolean xarpReset;
    public boolean xarpStarted;


    public boolean verzikWipe;
    public boolean verzikStarted;

    public boolean verzikTimeAccurate;

    public int verzikCrabsSpawned;
    public int verzikDawnDamage;
    public int verzikDawnCount;
    public int verzikRedsProc;

    public int raidTeamSize;

    public boolean raidCompleted;

    public Date raidStarted;

    private ArrayList<String> globalData;
    public LinkedHashMap<String, Integer> players;

    private ArrayList<DataPointPlayerData> playerSpecificData;
    public ArrayList<Integer> dawnDrops;

    public ArrayList<PlayerDidAttack> attacksP1;

    public String[] raidDataRaw;

    public String activeValue = "";

    public ArrayList<DefenseReductionOutlineBox> maidenOutlineBoxes = new ArrayList<>();
    public ArrayList<DefenseReductionOutlineBox> bloatOutlineBoxes = new ArrayList<>();
    public ArrayList<DefenseReductionOutlineBox> nyloOutlineBoxes = new ArrayList<>();
    public ArrayList<DefenseReductionOutlineBox> soteOutlineBoxes = new ArrayList<>();
    public ArrayList<DefenseReductionOutlineBox> xarpOutlineBoxes = new ArrayList<>();
    public ArrayList<DefenseReductionOutlineBox> verzikOutlineBoxes = new ArrayList<>();


    enum TobRoom {
        MAIDEN, BLOAT, NYLOCAS, SOTETSEG, XARPUS, VERZIK
    }

    public ArrayList<PlayerDidAttack> maidenAttacks;
    public ArrayList<PlayerDidAttack> bloatAttacks;
    public ArrayList<PlayerDidAttack> nyloAttacks;
    public ArrayList<PlayerDidAttack> soteAttacks;
    public ArrayList<PlayerDidAttack> xarpAttacks;
    public ArrayList<PlayerDidAttack> verzAttacks;

    public ArrayList<ThrallOutlineBox> maidenThrallSpawns;
    public ArrayList<ThrallOutlineBox> bloatThrallSpawns;
    public ArrayList<ThrallOutlineBox> nyloThrallSpawns;
    public ArrayList<ThrallOutlineBox> soteThrallSpawns;
    public ArrayList<ThrallOutlineBox> xarpusThrallSpawns;
    public ArrayList<ThrallOutlineBox> verzikThrallSpawns;


    public Date getDate() {
        return raidStarted;
    }

    public int getScale() {
        return raidTeamSize;
    }

    public boolean getTimeAccurate(com.TheatreTracker.utility.DataPoint param)
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

    public String getPlayerList(ArrayList<Map<String, ArrayList<String>>> aliases)
    {
        String list = "";
        ArrayList<String> names = new ArrayList<>();
        for(String s : players.keySet())
        {
            String name = s;
            for(Map<String, ArrayList<String>> alternateNames : aliases)
            {
                for(String alias : alternateNames.keySet())
                {
                    for(String potentialName : alternateNames.get(alias))
                    {
                        if(name.equalsIgnoreCase(potentialName))
                        {
                            name = alias;
                        }
                    }
                }
            }
            names.add(name);
        }
        names.sort(String::compareToIgnoreCase);
        for(String s : names)
        {
            list += s;
            list += ",";
        }
        if(list.length() != 0)
        {
            return list.substring(0,list.length()-1);
        }
        else
        {
            return "";
        }
    }

    public PlayerCorrelatedPointData getSpecificTimeInactiveCorrelated(String inactive)
    {
        if(inactive.contains("Player: "))
        {
            return dataManager.getHighest(DataPoint.getValue(inactive.substring(8)));
        }
        else
        {
            return null;
        }
    }

    public int getSpecificTimeInactive(String inactive)
    {
        if(inactive.contains("Player: "))
        {
            return dataManager.getHighest(DataPoint.getValue(inactive.substring(8))).value;
        }
        if(inactive.equals("Overall Time"))
        {
            return getMaidenTime() + getBloatTime() + getNyloTime() + getSoteTime() + getXarpTime() + getVerzikTime();
        }
        return getValue(DataPoint.getValue(inactive));
    }
    public int getSpecificTime()
    {
        if(activeValue.contains("Player: "))
        {
            return dataManager.getHighest(DataPoint.getValue(activeValue.substring(8))).value;
        }
        if(activeValue.equals("Overall Time"))
        {
            return getMaidenTime() + getBloatTime() + getNyloTime() + getSoteTime() + getXarpTime() + getVerzikTime();
        }
        return getValue(DataPoint.getValue(activeValue));
    }

    public void setOverallTime()
    {
        int overallTime = getMaidenTime()+getBloatTime()+getNyloTime()+getSoteTime()+getXarpTime()+getVerzikTime();
        dataManager.set(DataPoint.RAID_TIME, overallTime);
    }
    public int getMaidenTime() {
        return (maidenStartAccurate && maidenEndAccurate) ? getValue(com.TheatreTracker.utility.DataPoint.MAIDEN_TOTAL_TIME) : 0;
    }

    public int getBloatTime() {
        return (bloatStartAccurate && bloatEndAccurate) ? getValue(com.TheatreTracker.utility.DataPoint.BLOAT_TOTAL_TIME) : 0;
    }

    public int getNyloTime() {
        return (nyloStartAccurate && nyloEndAccurate) ? getValue(com.TheatreTracker.utility.DataPoint.NYLO_TOTAL_TIME) : 0;
    }

    public int getSoteTime() {
        return (soteStartAccurate && soteEndAccurate) ? getValue(com.TheatreTracker.utility.DataPoint.SOTE_TOTAL_TIME) : 0;
    }

    public int getXarpTime() {
        return (xarpStartAccurate && xarpEndAccurate) ? getValue(com.TheatreTracker.utility.DataPoint.XARP_TOTAL_TIME) : 0;
    }

    public int getVerzikTime() {
        return (verzikStartAccurate && verzikEndAccurate) ? getValue(com.TheatreTracker.utility.DataPoint.VERZIK_TOTAL_TIME) : 0;
    }

    public boolean getOverallTimeAccurate() {
        return maidenStartAccurate && maidenEndAccurate
                && bloatStartAccurate && bloatEndAccurate
                && nyloStartAccurate && nyloEndAccurate
                && soteStartAccurate && soteEndAccurate
                && xarpStartAccurate && xarpEndAccurate
                && verzikStartAccurate && verzikEndAccurate;
    }

    public boolean checkExit(TobRoom room) {
        if (globalData.size() == 0 || globalData.get(0).split(",", -1)[3].equals(EXIT_FLAG)) {
            switch (room) {
                case MAIDEN:
                    maidenReset = true;
                    break;
                case BLOAT:
                    if (!bloatEndAccurate) {
                        bloatWipe = true;
                    } else {
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

    public int getValue(String name) {
        return dataManager.get(name);
    }

    public int getValue(com.TheatreTracker.utility.DataPoint point)
    {
        return dataManager.get(point);
    }

    public int getTimeSum()
    {
        return getMaidenTime() + getBloatTime() + getNyloTime() + getSoteTime() + getXarpTime() + getVerzikTime();
    }

    public RoomData(String[] parameters) throws Exception
    {
        dataManager = new DataManager();
        raidDataRaw = parameters;
        partyComplete = false;
        maidenDefenseAccurate = false;
        bloatDefenseAccurate = false;
        nyloDefenseAccurate = false;
        soteDefenseAccurate = false;
        xarpDefenseAccurate = false;

        maidenAttacks = new ArrayList<>();
        bloatAttacks = new ArrayList<>();
        nyloAttacks = new ArrayList<>();
        soteAttacks = new ArrayList<>();
        xarpAttacks = new ArrayList<>();
        verzAttacks = new ArrayList<>();

        maidenThrallSpawns = new ArrayList<>();
        bloatThrallSpawns = new ArrayList<>();
        nyloThrallSpawns = new ArrayList<>();
        soteThrallSpawns = new ArrayList<>();
        xarpusThrallSpawns = new ArrayList<>();
        verzikThrallSpawns = new ArrayList<>();

        hardMode = false;
        storyMode = false;
        playerSpecificData = new ArrayList<>();
        attacksP1 = new ArrayList<>();
        dawnDrops = new ArrayList<>();

        players = new LinkedHashMap<>();
        globalData = new ArrayList<String>(Arrays.asList(parameters));

        int room = -1;
        for (String s : globalData)
        {
            String[] subData = s.split(",");
            int key = Integer.parseInt(subData[3]);
            if (key == 98)
            {
                room = Integer.parseInt(subData[4]);
                spectated = true;
            }
        }
        if (room > 0) {
            switch (room) {
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
                    if (checkExit(XARPUS) && parseVerzik()) {
                        finishRaid();
                    }
            }
        } else {
            try {
                if (parseMaiden()) {
                    if (checkExit(MAIDEN) && parseBloat()) {
                        if (checkExit(BLOAT) && parseNylo()) {
                            if (checkExit(NYLOCAS) && parseSotetseg()) {
                                if (checkExit(SOTETSEG) && parseXarpus()) {
                                    if (checkExit(XARPUS) && parseVerzik()) {
                                        finishRaid();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        setOverallTime();
    }

    private boolean parseVerzik()
    {
        int activeIndex = 0;
        for (String s : globalData)
        {
            String[] subData = s.split(",", -1);
            switch (Integer.parseInt(subData[3]))
            {
                case 0:
                    raidStarted = new Date(Long.parseLong(subData[1]));
                    break;
                case 1:
                    for (int i = 4; i < 9; i++)
                    {
                        if (!subData[i].equals(""))
                        {
                            raidTeamSize++;
                            players.put(subData[i].replaceAll("[^\\p{ASCII}]", " ").replaceAll(" +", " "), 0);
                        }
                    }
                    break;
                case 2:
                    dataManager.increment(DataPoint.HIT_HAMMERS_VERZIK);
                    dataManager.incrementPlayerSpecific(DataPoint.HIT_HAMMERS_VERZIK, subData[4]);
                    try
                    {
                        verzikOutlineBoxes.add(new DefenseReductionOutlineBox(subData[4], Integer.parseInt(subData[5]), new Color(0, 255, 0)));
                    }
                    catch(Exception e)
                    {

                    }
                    break;
                case 3:
                    dataManager.increment(DataPoint.ATTEMPTED_BGS_VERZ);
                    dataManager.increment(DataPoint.BGS_DAMAGE_VERZ, Integer.parseInt(subData[5]));
                    try
                    {
                        verzikOutlineBoxes.add(new DefenseReductionOutlineBox(subData[4], Integer.parseInt(subData[6]), new Color(0, 255, 0, Integer.parseInt(subData[5])*3)));
                    }
                    catch(Exception e)
                    {

                    }
                    break;
                case 4:
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.VERZIK_TOTAL_TIME) == 0) {
                        if (!verzikStarted) {
                            xarpReset = true;
                        } else {
                            verzikWipe = true;
                        }
                    } else {
                        return true;
                    }
                    globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                    return false;
                case 5:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.VERZIK_DEATHS);
                    dataManager.increment(DataPoint.TOTAL_DEATHS);
                    dataManager.incrementPlayerSpecific(DataPoint.VERZIK_DEATHS,subData[4]);
                    dataManager.incrementPlayerSpecific(DataPoint.TOTAL_DEATHS, subData[4]);
                    if(players.get(subData[4]) != null)
                    {
                        players.put(subData[4], players.get(subData[4]) + 1);
                    }
                case 6:
                    break;
                case 7:
                    dataManager.increment(DataPoint.ATTEMPTED_HAMMERS_VERZIK);
                    dataManager.incrementPlayerSpecific(DataPoint.ATTEMPTED_HAMMERS_VERZIK, subData[4]);
                    break;
                case 800:
                    if(verzikStarted)
                    {
                        dawnDrops.add(Integer.parseInt(subData[4]));
                    }
                    break;
                case 70:
                    break;
                case 71:
                    verzikStarted = true;
                case 72:
                    break;
                case 73:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.VERZIK_P1_SPLIT, Integer.parseInt(subData[4])-13);
                    break;
                case 74:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.VERZIK_P2_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(com.TheatreTracker.utility.DataPoint.VERZIK_P2_DURATION, dataManager.get(com.TheatreTracker.utility.DataPoint.VERZIK_P2_SPLIT) - dataManager.get(com.TheatreTracker.utility.DataPoint.VERZIK_P1_SPLIT));

                    break;
                case 75:
                    break;
                case 76:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.VERZIK_TOTAL_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(com.TheatreTracker.utility.DataPoint.VERZIK_P3_DURATION, dataManager.get(com.TheatreTracker.utility.DataPoint.VERZIK_TOTAL_TIME) - dataManager.get(com.TheatreTracker.utility.DataPoint.VERZIK_P2_SPLIT));
                    dataManager.set(DataPoint.RAID_TIME, (Integer.parseInt(subData[4])+dataManager.get(DataPoint.VERZIK_ENTRY)));
                    break;
                case 77:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.VERZIK_BOUNCES);
                    dataManager.incrementPlayerSpecific(DataPoint.VERZIK_BOUNCES,subData[4]);
                    if(!subData[5].equalsIgnoreCase(""))
                    {
                        verzAttacks.add(new PlayerDidAttack(subData[4], "100000", Integer.parseInt(subData[5]), "-1", "-1", "-1", -1, -1));
                    }
                    break;
                case 78:
                    if(!subData[4].equalsIgnoreCase(""))
                    {
                        if (dataManager.get(DataPoint.VERZIK_P2_SPLIT) > 1)
                        {
                            if(!p3Crabs.contains(Integer.parseInt(subData[4])))
                            {
                                p3Crabs.add(Integer.parseInt(subData[4]));
                            }
                        } else
                        {
                            if(!p2Crabs.contains(Integer.parseInt(subData[4])))
                            {
                                p2Crabs.add(Integer.parseInt(subData[4]));
                            }
                        }
                    }
                    dataManager.increment(DataPoint.VERZIK_CRABS_SPAWNED);
                    break;
                case 79:
                    verzikDawnCount++;
                    verzikDawnDamage += Integer.parseInt(subData[5]);
                    break;
                case 80:
                    if(dataManager.get(DataPoint.VERZIK_REDS_SPLIT) == 0)
                    {
                        dataManager.set(DataPoint.VERZIK_REDS_SPLIT, Integer.parseInt(subData[4]));
                    }
                    redsProc.add(Integer.parseInt(subData[4]));
                    dataManager.increment(DataPoint.VERZIK_REDS_SETS);
                    break;
                case 206:
                    verzikStartAccurate = true;
                    break;
                case 306:
                    verzikEndAccurate = true;
                    verzikTimeAccurate = verzikStartAccurate;
                    break;
                case 401:
                    hardMode = true;
                    break;
                case 402:
                    storyMode = true;
                    break;
                case 403:
                    dataManager.increment(DataPoint.THRALL_ATTACKS_VERZIK);
                    dataManager.increment(DataPoint.THRALL_ATTACKS_TOTAL);
                    break;
                case 404:
                    int amount = Integer.parseInt(subData[5]);
                    dataManager.increment(DataPoint.THRALL_DAMAGE_VERZIK, amount);
                    dataManager.increment(DataPoint.THRALL_DAMAGE_TOTAL, amount);
                    break;
                case 405:
                    dataManager.increment(DataPoint.VENG_CASTS_VERZIK);
                    dataManager.increment(DataPoint.VENG_CASTS_TOTAL);
                    break;
                case 406:
                    dataManager.increment(DataPoint.VENG_PROCS_VERZIK);
                    dataManager.increment(DataPoint.VENG_PROCS_TOTAL);
                    dataManager.increment(DataPoint.VENG_DAMAGE_VERZIK, Integer.parseInt(subData[5]));
                    dataManager.increment(DataPoint.VENG_DAMAGE_TOTAL, Integer.parseInt(subData[5]));
                    break;
                case 410:
                    try
                    {
                        verzikThrallSpawns.add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                    }
                    catch (Exception e)
                    {

                    }
                    break;
                case 487:
                    dawnSpecs.add(new DawnSpec(subData[4], Integer.parseInt(subData[5])));
                    break;
                case 488:
                    for(DawnSpec dawnSpec : dawnSpecs)
                    {
                        if(dawnSpec.tick == Integer.parseInt(subData[5]))
                        {
                            dawnSpec.setDamage(Integer.parseInt(subData[4]));
                        }
                    }
                    break;
                case 501:
                    dataManager.increment(DataPoint.KODAI_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.KODAI_BOPS, subData[4]);
                    break;
                case 502:
                    dataManager.increment(DataPoint.DWH_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.DWH_BOPS, subData[4]);
                    break;
                case 503:
                    dataManager.increment(DataPoint.BGS_WHACKS);
                    dataManager.incrementPlayerSpecific(DataPoint.BGS_WHACKS, subData[4]);
                    break;
                case 504:
                    dataManager.increment(DataPoint.CHALLY_POKE);
                    dataManager.incrementPlayerSpecific(DataPoint.CHALLY_POKE, subData[4]);
                    break;
                case 801:
                    int tick;
                    String player;
                    String animation;
                    String weapon;
                    String projectile;
                    String spotAnims;
                    try
                    {
                        player = subData[4].split(":")[0];
                        tick = Integer.parseInt(subData[4].split(":")[1]);
                        animation = subData[5];
                        spotAnims = subData[6];
                        String[] subsubData = subData[7].split(":");
                        weapon = subsubData[0];
                        int interactedIndex = -1;
                        int interactedID = -1;
                        if(subsubData.length > 2)
                        {
                            interactedIndex = Integer.parseInt(subsubData[1]);
                            interactedID = Integer.parseInt(subsubData[2]);
                        }
                        projectile = subData[8];
                        verzAttacks.add(new PlayerDidAttack(player, animation, tick, weapon, projectile, spotAnims, interactedIndex, interactedID));
                    }
                    catch(Exception e)
                    {
                    }
                    break;
                case 901:
                    websStart.add(Integer.parseInt(subData[4]));
                    break;

            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean isTimeAccurateThroughRoom(TobRoom room) {
        switch (room) {
            case VERZIK:
                if (!verzikTimeAccurate) {
                    return false;
                }
            case XARPUS:
                if (!xarpTimeAccurate) {
                    return false;
                }
            case SOTETSEG:
                if (!soteTimeAccurate) {
                    return false;
                }
            case NYLOCAS:
                if (!nyloTimeAccurate) {
                    return false;
                }
            case BLOAT:
                if (!bloatTimeAccurate) {
                    return false;
                }
            case MAIDEN:
                if (!maidenTimeAccurate) {
                    return false;
                } else {
                    return true;
                }
        }
        return false;
    }

    private boolean parseXarpus() {

        int activeIndex = 0;
        loop:
        for (String s : globalData) {
            String[] subData = s.split(",", -1);
            switch (Integer.parseInt(subData[3])) {
                case 0:
                    raidStarted = new Date(Long.parseLong(subData[1]));
                    break;
                case 1:
                    for (int i = 4; i < 9; i++) {
                        if (!subData[i].equals("")) {
                            raidTeamSize++;
                            players.put(subData[i].replaceAll("[^\\p{ASCII}]", " ").replaceAll(" +", " "), 0);
                        }
                    }
                    break;
                case 2:
                    dataManager.hammer(com.TheatreTracker.utility.DataPoint.XARP_DEFENSE);
                    dataManager.increment(DataPoint.HIT_HAMMERS_XARP);
                    dataManager.incrementPlayerSpecific(DataPoint.HIT_HAMMERS_XARP, subData[4]);
                    try
                    {
                        xarpOutlineBoxes.add(new DefenseReductionOutlineBox(subData[4], Integer.parseInt(subData[5]), new Color(0, 255, 0)));
                    }
                    catch(Exception e)
                    {

                    }
                    break;
                case 3:
                    dataManager.increment(DataPoint.ATTEMPTED_BGS_XARP);
                    dataManager.increment(DataPoint.BGS_DAMAGE_XARP, Integer.parseInt(subData[5]));
                    dataManager.bgs(com.TheatreTracker.utility.DataPoint.XARP_DEFENSE, Integer.parseInt(subData[5]));
                    try
                    {
                        xarpOutlineBoxes.add(new DefenseReductionOutlineBox(subData[4], Integer.parseInt(subData[6]), new Color(0, 255, 0, Integer.parseInt(subData[5])*3)));
                    }
                    catch(Exception e)
                    {

                    }
                    break;
                case 4:
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.XARP_TOTAL_TIME) != 0) {
                        xarpReset = true;
                    } else {
                        if (!xarpStarted) {
                            soteReset = true;
                        } else {
                            xarpWipe = true;
                        }
                    }
                    globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
                    return false;
                case 5:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.XARP_DEATHS);
                    dataManager.increment(DataPoint.TOTAL_DEATHS);
                    dataManager.incrementPlayerSpecific(DataPoint.XARP_DEATHS,subData[4]);
                    dataManager.incrementPlayerSpecific(DataPoint.TOTAL_DEATHS, subData[4]);
                    if(players.get(subData[4]) != null)
                    {
                        players.put(subData[4], players.get(subData[4]) + 1);
                    }
                    break;
                case 6:
                    break;
                case 7:
                    dataManager.increment(DataPoint.ATTEMPTED_HAMMERS_XARP);
                    dataManager.incrementPlayerSpecific(DataPoint.ATTEMPTED_HAMMERS_XARP, subData[4]);

                    break;
                case 60:
                    break;
                case 61:
                    xarpStarted = true;
                    if (partyComplete) {
                        xarpDefenseAccurate = true;
                    }
                    break;
                case 62:
                    int amount = 0;
                    switch (raidTeamSize) {
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
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.XARP_HEALING, amount);
                    break;
                case 63:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.XARP_SCREECH, Integer.parseInt(subData[4]));
                    break;
                case 64:
                    break;
                case 65:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.XARP_TOTAL_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(com.TheatreTracker.utility.DataPoint.XARP_POST_SCREECH, dataManager.get(com.TheatreTracker.utility.DataPoint.XARP_TOTAL_TIME) - dataManager.get(com.TheatreTracker.utility.DataPoint.XARP_SCREECH));
                    if (isTimeAccurateThroughRoom(SOTETSEG))
                        dataManager.set(DataPoint.VERZIK_ENTRY, Integer.parseInt(subData[4]) + dataManager.get(DataPoint.XARP_ENTRY));
                    break loop;
                case 100:
                    partyComplete = true;
                    break;
                case 101:
                    partyComplete = false;
                    break;
                case 205:
                    xarpStartAccurate = true;
                    break;
                case 305:
                    xarpTimeAccurate = xarpStartAccurate;
                    xarpEndAccurate = true;
                    break;
                case 401:
                    hardMode = true;
                    break;
                case 402:
                    storyMode = true;
                    break;
                case 403:
                    dataManager.increment(DataPoint.THRALL_ATTACKS_XARP);
                    dataManager.increment(DataPoint.THRALL_ATTACKS_TOTAL);
                    break;
                case 404:
                    dataManager.increment(DataPoint.THRALL_DAMAGE_XARP, Integer.parseInt(subData[5]));
                    dataManager.increment(DataPoint.THRALL_DAMAGE_TOTAL, Integer.parseInt(subData[5]));
                    break;
                case 405:
                    dataManager.increment(DataPoint.VENG_CASTS_XARP);
                    dataManager.increment(DataPoint.VENG_CASTS_TOTAL);
                    break;
                case 406:
                    dataManager.increment(DataPoint.VENG_PROCS_XARP);
                    dataManager.increment(DataPoint.VENG_PROCS_TOTAL);
                    dataManager.increment(DataPoint.VENG_DAMAGE_XARP, Integer.parseInt(subData[5]));
                    dataManager.increment(DataPoint.VENG_DAMAGE_TOTAL, Integer.parseInt(subData[5]));
                    break;
                case 410:
                    try
                    {
                        xarpusThrallSpawns.add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                    }
                    catch (Exception e)
                    {

                    }
                    break;
                case 501:
                    dataManager.increment(DataPoint.KODAI_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.KODAI_BOPS, subData[4]);
                    break;
                case 502:
                    dataManager.increment(DataPoint.DWH_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.DWH_BOPS, subData[4]);
                    break;
                case 503:
                    dataManager.increment(DataPoint.BGS_WHACKS);
                    dataManager.incrementPlayerSpecific(DataPoint.BGS_WHACKS, subData[4]);
                    break;
                case 504:
                    dataManager.increment(DataPoint.CHALLY_POKE);
                    dataManager.incrementPlayerSpecific(DataPoint.CHALLY_POKE, subData[4]);
                    break;
                case 801:
                    int tick;
                    String player;
                    String animation;
                    String weapon;
                    String projectile;
                    String spotAnims;
                    try
                    {
                        player = subData[4].split(":")[0];
                        tick = Integer.parseInt(subData[4].split(":")[1]);
                        animation = subData[5];
                        spotAnims = subData[6];
                        String[] subsubData = subData[7].split(":");
                        weapon = subsubData[0];
                        int interactedIndex = -1;
                        int interactedID = -1;
                        if(subsubData.length > 2)
                        {
                            interactedIndex = Integer.parseInt(subsubData[1]);
                            interactedID = Integer.parseInt(subsubData[2]);
                        }
                        projectile = subData[8];
                        xarpAttacks.add(new PlayerDidAttack(player, animation, tick, weapon, projectile, spotAnims, interactedIndex, interactedID));
                    }
                    catch(Exception e)
                    {
                    }
                    break;
            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean parseSotetseg() {
        int activeIndex = 0;
        loop:
        for (String s : globalData) {
            String[] subData = s.split(",", -1);
            switch (Integer.parseInt(subData[3])) {
                case 0:
                    raidStarted = new Date(Long.parseLong(subData[1]));
                    break;
                case 1:
                    for (int i = 4; i < 9; i++) {
                        if (!subData[i].equals("")) {
                            raidTeamSize++;
                            players.put(subData[i].replaceAll("[^\\p{ASCII}]", " ").replaceAll(" +", " "),0);
                        }
                    }
                    break;
                case 2:
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.SOTE_P1_SPLIT) == 0)
                    {
                        dataManager.increment(com.TheatreTracker.utility.DataPoint.SOTE_SPECS_P1);
                        dataManager.increment(com.TheatreTracker.utility.DataPoint.SOTE_SPECS_TOTAL);
                    } else if (dataManager.get(com.TheatreTracker.utility.DataPoint.SOTE_P2_SPLIT) == 0) {
                        dataManager.increment(com.TheatreTracker.utility.DataPoint.SOTE_SPECS_P2);
                        dataManager.increment(com.TheatreTracker.utility.DataPoint.SOTE_SPECS_TOTAL);
                    } else {
                        dataManager.increment(com.TheatreTracker.utility.DataPoint.SOTE_SPECS_P3);
                        dataManager.increment(com.TheatreTracker.utility.DataPoint.SOTE_SPECS_TOTAL);
                    }
                    dataManager.increment(DataPoint.HIT_HAMMERS_SOTE);
                    dataManager.incrementPlayerSpecific(DataPoint.HIT_HAMMERS_SOTE, subData[4]);
                    try
                    {
                        soteOutlineBoxes.add(new DefenseReductionOutlineBox(subData[4], Integer.parseInt(subData[5]), new Color(0, 255, 0)));
                    }
                    catch(Exception e)
                    {

                    }
                    break;
                case 3:
                    dataManager.increment(DataPoint.ATTEMPTED_BGS_SOTE);
                    dataManager.increment(DataPoint.BGS_DAMAGE_SOTE, Integer.parseInt(subData[5]));
                    try
                    {
                        soteOutlineBoxes.add(new DefenseReductionOutlineBox(subData[4], Integer.parseInt(subData[6]), new Color(0, 255, 0, Integer.parseInt(subData[5])*3)));
                    }
                    catch(Exception e)
                    {

                    }
                    break;
                case 6:
                    break;
                case 4:
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.SOTE_TOTAL_TIME) != 0) {
                        soteReset = true;
                    } else {
                        if (!soteStarted) {
                            nyloReset = true;
                        } else {
                            soteWipe = true;
                        }
                    }
                    globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
                    return false;
                case 7:
                    dataManager.increment(DataPoint.ATTEMPTED_HAMMERS_SOTE);
                    dataManager.incrementPlayerSpecific(DataPoint.ATTEMPTED_HAMMERS_SOTE, subData[4]);

                    break;
                case 5:
                    dataManager.increment(DataPoint.TOTAL_DEATHS);
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.SOTE_DEATHS);
                    dataManager.incrementPlayerSpecific(DataPoint.SOTE_DEATHS,subData[4]);
                    dataManager.incrementPlayerSpecific(DataPoint.TOTAL_DEATHS, subData[4]);
                    if(players.get(subData[4]) != null)
                    {
                        players.put(subData[4], players.get(subData[4]) + 1);
                    }
                    break;
                case 50:
                    break;
                case 51:
                    soteStarted = true;
                    if (partyComplete) {
                        soteDefenseAccurate = true;
                    }
                    break;
                case 52:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.SOTE_P1_SPLIT, Integer.parseInt(subData[4]));
                    break;
                case 53:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.SOTE_M1_DURATION, Integer.parseInt(subData[4])-dataManager.get(DataPoint.SOTE_P1_SPLIT));
                    dataManager.set(com.TheatreTracker.utility.DataPoint.SOTE_M1_SPLIT, Integer.parseInt(subData[4]));

                    break;
                case 54:
                    dataManager.set(DataPoint.SOTE_P2_DURATION, Integer.parseInt(subData[4])-dataManager.get(DataPoint.SOTE_M1_SPLIT));
                    dataManager.set(com.TheatreTracker.utility.DataPoint.SOTE_P2_SPLIT, Integer.parseInt(subData[4]));

                    break;
                case 55:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.SOTE_M2_DURATION, Integer.parseInt(subData[4])-dataManager.get(DataPoint.SOTE_P2_SPLIT));
                    dataManager.set(com.TheatreTracker.utility.DataPoint.SOTE_M2_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(DataPoint.SOTE_MAZE_SUM, dataManager.get(DataPoint.SOTE_M1_DURATION)+ dataManager.get(DataPoint.SOTE_M2_DURATION));
                    break;
                case 56:
                    break;
                case 57:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.SOTE_TOTAL_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(DataPoint.SOTE_P3_DURATION, dataManager.get(com.TheatreTracker.utility.DataPoint.SOTE_TOTAL_TIME) - dataManager.get(com.TheatreTracker.utility.DataPoint.SOTE_M2_SPLIT));
                    if (isTimeAccurateThroughRoom(NYLOCAS))
                        dataManager.set(DataPoint.XARP_ENTRY, Integer.parseInt(subData[4]) + dataManager.get(DataPoint.SOTE_ENTRY));
                    break loop;
                case 58:
                    if (soteCyclesLost != -1) {
                        soteCyclesLost += Integer.parseInt(subData[4]);
                    } else {
                        soteCyclesLost = Integer.parseInt(subData[4]);
                    }
                    break;
                case 100:
                    partyComplete = true;
                    break;
                case 101:
                    partyComplete = false;
                    break;
                case 204:
                    soteStartAccurate = true;
                    break;
                case 304:
                    soteEndAccurate = true;
                    soteTimeAccurate = soteStartAccurate;
                    break;
                case 401:
                    hardMode = true;
                    break;
                case 402:
                    storyMode = true;
                    break;
                case 403:
                    dataManager.increment(DataPoint.THRALL_ATTACKS_SOTE);
                    dataManager.increment(DataPoint.THRALL_ATTACKS_TOTAL);
                    break;
                case 404:
                    int amount = Integer.parseInt(subData[5]);
                    dataManager.increment(DataPoint.THRALL_DAMAGE_SOTE, amount);
                    dataManager.increment(DataPoint.THRALL_DAMAGE_TOTAL, amount);
                    break;
                case 405:
                    dataManager.increment(DataPoint.VENG_CASTS_SOTE);
                    dataManager.increment(DataPoint.VENG_CASTS_TOTAL);
                    break;
                case 406:
                    dataManager.increment(DataPoint.VENG_PROCS_SOTE);
                    dataManager.increment(DataPoint.VENG_PROCS_TOTAL);
                    dataManager.increment(DataPoint.VENG_DAMAGE_SOTE, Integer.parseInt(subData[5]));
                    dataManager.increment(DataPoint.VENG_DAMAGE_TOTAL, Integer.parseInt(subData[5]));
                    break;
                case 410:
                    try
                    {
                        soteThrallSpawns.add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                    }
                    catch (Exception e)
                    {

                    }
                    break;
                case 501:
                    dataManager.increment(DataPoint.KODAI_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.KODAI_BOPS, subData[4]);
                    break;
                case 502:
                    dataManager.increment(DataPoint.DWH_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.DWH_BOPS, subData[4]);
                    break;
                case 503:
                    dataManager.increment(DataPoint.BGS_WHACKS);
                    dataManager.incrementPlayerSpecific(DataPoint.BGS_WHACKS, subData[4]);
                    break;
                case 504:
                    dataManager.increment(DataPoint.CHALLY_POKE);
                    dataManager.incrementPlayerSpecific(DataPoint.CHALLY_POKE, subData[4]);
                    break;
                case 801:
                    int tick;
                    String player;
                    String animation;
                    String weapon;
                    String projectile;
                    String spotAnims;
                    try
                    {
                        player = subData[4].split(":")[0];
                        tick = Integer.parseInt(subData[4].split(":")[1]);
                        animation = subData[5];
                        spotAnims = subData[6];
                        String[] subsubData = subData[7].split(":");
                        weapon = subsubData[0];
                        int interactedIndex = -1;
                        int interactedID = -1;
                        if(subsubData.length > 2)
                        {
                            interactedIndex = Integer.parseInt(subsubData[1]);
                            interactedID = Integer.parseInt(subsubData[2]);
                        }
                        projectile = subData[8];
                        soteAttacks.add(new PlayerDidAttack(player, animation, tick, weapon, projectile, spotAnims, interactedIndex, interactedID));
                    }
                    catch(Exception e)
                    {
                    }
                    break;

            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean parseNylo()
    {
        int activeIndex = 0;
        loop:
        for (String s : globalData) {
            String[] subData = s.split(",", -1);
            switch (Integer.parseInt(subData[3])) {
                case 0:
                    raidStarted = new Date(Long.parseLong(subData[1]));
                    break;
                case 1:
                    for (int i = 4; i < 9; i++) {
                        if (!subData[i].equals("")) {
                            raidTeamSize++;
                            players.put(subData[i].replaceAll("[^\\p{ASCII}]", " ").replaceAll(" +", " "), 0);
                        }
                    }
                    break;
                case 2:
                    dataManager.increment(DataPoint.HIT_HAMMERS_NYLO);
                    dataManager.incrementPlayerSpecific(DataPoint.HIT_HAMMERS_NYLO, subData[4]);
                    try
                    {
                        nyloOutlineBoxes.add(new DefenseReductionOutlineBox(subData[4], Integer.parseInt(subData[5]), new Color(0, 255, 0)));
                    }
                    catch(Exception e)
                    {

                    }
                    break;
                case 44:
                case 6:
                    break;
                case 3:
                    dataManager.increment(DataPoint.ATTEMPTED_BGS_NYLO);
                    dataManager.increment(DataPoint.BGS_DAMAGE_NYLO, Integer.parseInt(subData[5]));
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.NYLO_BOSS_SPAWN) != 0) {
                        dataManager.bgs(com.TheatreTracker.utility.DataPoint.NYLO_DEFENSE, Integer.parseInt(subData[5]));
                    }
                    try
                    {
                        nyloOutlineBoxes.add(new DefenseReductionOutlineBox(subData[4], Integer.parseInt(subData[6]), new Color(0, 255, 0, Integer.parseInt(subData[5])*3)));
                    }
                    catch(Exception e)
                    {

                    }
                    break;
                case 4:
                    if(pillarDespawnTick-5 < dataManager.get(DataPoint.NYLO_BOSS_SPAWN))
                    {
                        dataManager.set(DataPoint.NYLO_BOSS_SPAWN, 0);
                        dataManager.set(DataPoint.NYLO_TOTAL_TIME, 0);
                    }
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.NYLO_TOTAL_TIME) != 0)
                    {
                        nyloReset = true;
                    } else
                    {
                        if (!nyloStarted)
                        {
                            if (!bloatEndAccurate)
                            {
                                bloatWipe = true;
                            } else {
                                bloatReset = true;
                            }
                        } else {
                            nyloWipe = true;
                        }
                    }
                    globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
                    return false;
                case 5:
                    nyloDeaths++;
                    dataManager.increment(DataPoint.TOTAL_DEATHS);
                    dataManager.incrementPlayerSpecific(DataPoint.NYLO_DEATHS,subData[4]);
                    dataManager.incrementPlayerSpecific(DataPoint.TOTAL_DEATHS, subData[4]);
                    if(players.get(subData[4]) != null)
                    {
                        players.put(subData[4], players.get(subData[4]) + 1);
                    }
                    break;
                case 7:
                    dataManager.increment(DataPoint.ATTEMPTED_HAMMERS_NYLO);
                    dataManager.incrementPlayerSpecific(DataPoint.ATTEMPTED_HAMMERS_NYLO, subData[4]);

                    break;
                case 30:
                    nyloStarted = true;
                    break;
                case 31:
                    nyloWaveStalled.add(Integer.parseInt(subData[5]));
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.NYLO_STALLS_TOTAL);
                    if (Integer.parseInt(subData[4]) > 19) {
                        dataManager.increment(com.TheatreTracker.utility.DataPoint.NYLO_STALLS_POST_20);
                    } else {
                        dataManager.increment(com.TheatreTracker.utility.DataPoint.NYLO_STALLS_PRE_20);
                    }
                    break;
                case 32:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.NYLO_SPLITS_RANGE);
                    break;
                case 33:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.NYLO_SPLITS_MAGE);
                    break;
                case 34:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.NYLO_SPLITS_MELEE);
                    break;
                case 35:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.NYLO_LAST_WAVE, Integer.parseInt(subData[4]));
                    break;
                case 36:
                    nyloLastDead = Integer.parseInt(subData[4]);
                    int offset = 20-(nyloLastDead%4);
                    dataManager.set(DataPoint.NYLO_BOSS_SPAWN, nyloLastDead+offset);
                    dataManager.set(com.TheatreTracker.utility.DataPoint.NYLO_CLEANUP, nyloLastDead - dataManager.get(com.TheatreTracker.utility.DataPoint.NYLO_LAST_WAVE));
                    break;
                case 37:
                    waveSpawns.put(Integer.parseInt(subData[4]), Integer.parseInt(subData[5]));
                    break;
                case 40:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.NYLO_BOSS_SPAWN, Integer.parseInt(subData[4]) - 2);
                    if (partyComplete) {
                        nyloDefenseAccurate = true;
                    }
                    break;
                case 41:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.NYLO_ROTATIONS_MELEE);
                    break;
                case 42:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.NYLO_ROTATIONS_MAGE);
                    break;
                case 43:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.NYLO_ROTATIONS_RANGE);
                    break;
                case 45:
                    if(Integer.parseInt(subData[4])-dataManager.get(DataPoint.NYLO_BOSS_SPAWN) > 30)
                    {
                        dataManager.set(com.TheatreTracker.utility.DataPoint.NYLO_TOTAL_TIME, Integer.parseInt(subData[4]));
                        dataManager.set(com.TheatreTracker.utility.DataPoint.NYLO_BOSS_DURATION, dataManager.get(com.TheatreTracker.utility.DataPoint.NYLO_TOTAL_TIME) - dataManager.get(com.TheatreTracker.utility.DataPoint.NYLO_BOSS_SPAWN));
                        if (isTimeAccurateThroughRoom(BLOAT))
                            dataManager.set(DataPoint.SOTE_ENTRY, Integer.parseInt(subData[4]) + dataManager.get(DataPoint.NYLO_ENTRY));
                    }
                    break loop;
                case 46:
                    pillarDespawnTick = Integer.parseInt(subData[4]);
                    break;
                case 100:
                    partyComplete = true;
                    break;
                case 101:
                    partyComplete = false;
                    break;
                case 203:
                    nyloStartAccurate = true;
                    break;
                case 303:
                    nyloEndAccurate = true;
                    nyloTimeAccurate = nyloStartAccurate;
                    break;
                case 401:
                    hardMode = true;
                    break;
                case 402:
                    storyMode = true;
                    break;
                case 403:
                    dataManager.increment(DataPoint.THRALL_ATTACKS_NYLO);
                    dataManager.increment(DataPoint.THRALL_ATTACKS_TOTAL);
                    break;
                case 404:
                    int amount = Integer.parseInt(subData[5]);
                    dataManager.increment(DataPoint.THRALL_DAMAGE_NYLO, amount);
                    dataManager.increment(DataPoint.THRALL_DAMAGE_TOTAL, amount);
                    break;
                case 405:
                    dataManager.increment(DataPoint.VENG_CASTS_NYLO);
                    dataManager.increment(DataPoint.VENG_CASTS_TOTAL);
                    break;
                case 406:
                    dataManager.increment(DataPoint.VENG_PROCS_NYLO);
                    dataManager.increment(DataPoint.VENG_PROCS_TOTAL);
                    dataManager.increment(DataPoint.VENG_DAMAGE_NYLO, Integer.parseInt(subData[5]));
                    dataManager.increment(DataPoint.VENG_DAMAGE_TOTAL, Integer.parseInt(subData[5]));
                    break;
                case 410:
                    try
                    {
                        nyloThrallSpawns.add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                    }
                    catch (Exception e)
                    {

                    }
                    break;
                case 501:
                    dataManager.increment(DataPoint.KODAI_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.KODAI_BOPS, subData[4]);
                    break;
                case 502:
                    dataManager.increment(DataPoint.DWH_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.DWH_BOPS, subData[4]);
                    break;
                case 503:
                    dataManager.increment(DataPoint.BGS_WHACKS);
                    dataManager.incrementPlayerSpecific(DataPoint.BGS_WHACKS, subData[4]);
                    break;
                case 504:
                    dataManager.increment(DataPoint.CHALLY_POKE);
                    dataManager.incrementPlayerSpecific(DataPoint.CHALLY_POKE, subData[4]);
                    break;
                case 801:
                    int tick;
                    String player;
                    String animation;
                    String weapon;
                    String projectile;
                    String spotAnims;
                    try
                    {
                        player = subData[4].split(":")[0];
                        tick = Integer.parseInt(subData[4].split(":")[1]);
                        animation = subData[5];
                        spotAnims = subData[6];
                        String[] subsubData = subData[7].split(":");
                        weapon = subsubData[0];
                        int interactedIndex = -1;
                        int interactedID = -1;
                        if(subsubData.length > 2)
                        {
                            interactedIndex = Integer.parseInt(subsubData[1]);
                            interactedID = Integer.parseInt(subsubData[2]);
                        }
                        projectile = subData[8];
                        nyloAttacks.add(new PlayerDidAttack(player, animation, tick, weapon, projectile, spotAnims, interactedIndex, interactedID));
                    }
                    catch(Exception e)
                    {
                    }
                    break;

            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
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
            switch (Integer.parseInt(subData[3]))
            {
                case 0:
                    raidStarted = new Date(Long.parseLong(subData[1]));
                    break;
                case 1:
                    for (int i = 4; i < 9; i++)
                    {
                        if (!subData[i].equals(""))
                        {
                            raidTeamSize++;
                            players.put(subData[i].replaceAll("[^\\p{ASCII}]", " ").replaceAll(" +", " "), 0);
                        }
                    }
                    break;
                case 2:
                    dataManager.increment(DataPoint.HIT_HAMMERS_BLOAT);
                    dataManager.incrementPlayerSpecific(DataPoint.HIT_HAMMERS_BLOAT, subData[4]);
                    try
                    {
                        bloatOutlineBoxes.add(new DefenseReductionOutlineBox(subData[4], Integer.parseInt(subData[5]), new Color(0, 255, 0)));
                    }
                    catch(Exception e)
                    {

                    }
                    break;
                case 6:
                    break;
                case 24:
                    dataManager.set(DataPoint.BLOAT_HP_FIRST_DOWN, Integer.parseInt(subData[4])/10);
                    break;
                case 25:
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.BLOAT_DOWNS) == 0)
                    {
                        dataManager.increment(DataPoint.BLOAT_FIRST_WALK_SCYTHES);
                        dataManager.incrementPlayerSpecific(DataPoint.BLOAT_FIRST_WALK_SCYTHES, subData[4]);
                        bloatScytheBeforeFirstDown++;
                    }
                    break;
                case 3:
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.BLOAT_DOWNS) == 0)
                    {
                        dataManager.bgs(com.TheatreTracker.utility.DataPoint.BLOAT_DEFENSE, 2 * Integer.parseInt(subData[5]));
                    }
                    dataManager.increment(DataPoint.ATTEMPTED_BGS_BLOAT);
                    dataManager.increment(DataPoint.BGS_DAMAGE_BLOAT, Integer.parseInt(subData[5]));
                    try
                    {
                        bloatOutlineBoxes.add(new DefenseReductionOutlineBox(subData[4], Integer.parseInt(subData[6]), new Color(0, 255, 0, Integer.parseInt(subData[5])*3)));
                    }
                    catch(Exception e)
                    {

                    }
                    break;
                case 4:
                    if (bloatEndAccurate) {
                        bloatReset = true;
                    } else {
                        if (!bloatStarted) {
                            maidenReset = true;
                        } else {
                            bloatWipe = true;
                        }
                    }
                    globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
                    return false;
                case 5:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.BLOAT_DEATHS);
                    dataManager.incrementPlayerSpecific(DataPoint.BLOAT_DEATHS,subData[4]);
                    dataManager.incrementPlayerSpecific(DataPoint.TOTAL_DEATHS, subData[4]);
                    dataManager.increment(DataPoint.TOTAL_DEATHS);
                    if(players.get(subData[4]) != null)
                    {
                        players.put(subData[4], players.get(subData[4]) + 1);
                    }
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.BLOAT_DOWNS) == 0)
                    {
                        dataManager.increment(com.TheatreTracker.utility.DataPoint.BLOAT_FIRST_WALK_DEATHS);
                        dataManager.incrementPlayerSpecific(DataPoint.BLOAT_FIRST_WALK_DEATHS, subData[4]);
                    }
                    break;
                case 7:
                    dataManager.increment(DataPoint.ATTEMPTED_HAMMERS_BLOAT);
                    dataManager.incrementPlayerSpecific(DataPoint.ATTEMPTED_HAMMERS_BLOAT, subData[4]);

                    break;
                case 20:
                    bloatStarted = true;
                    if (partyComplete) {
                        bloatDefenseAccurate = true;
                    }
                    break;
                case 21:
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.BLOAT_DOWNS) == 0)
                    {
                        dataManager.set(com.TheatreTracker.utility.DataPoint.BLOAT_FIRST_DOWN_TIME, Integer.parseInt(subData[4]));
                    }
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.BLOAT_DOWNS);
                    bloatDowns.add(Integer.parseInt(subData[4]));
                    break;
                case 23:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.BLOAT_TOTAL_TIME, Integer.parseInt(subData[4]));
                    if (isTimeAccurateThroughRoom(MAIDEN))
                        dataManager.set(DataPoint.NYLO_ENTRY, Integer.parseInt(subData[4]) + dataManager.get(DataPoint.MAIDEN_TOTAL_TIME));
                    break loop;
                case 100:
                    partyComplete = true;
                    break;
                case 101:
                    partyComplete = false;
                    break;
                case 202:
                    bloatStartAccurate = true;
                    break;
                case 302:
                    bloatEndAccurate = true;
                    bloatTimeAccurate = bloatStartAccurate;
                    break;
                case 401:
                    hardMode = true;
                    break;
                case 402:
                    storyMode = true;
                    break;
                case 403:
                    dataManager.increment(DataPoint.THRALL_ATTACKS_BLOAT);
                    dataManager.increment(DataPoint.THRALL_ATTACKS_TOTAL);
                    break;
                case 404:
                    int amount = Integer.parseInt(subData[5]);
                    dataManager.increment(DataPoint.THRALL_DAMAGE_BLOAT, amount);
                    dataManager.increment(DataPoint.THRALL_DAMAGE_TOTAL, amount);
                    break;
                case 405:
                    dataManager.increment(DataPoint.VENG_CASTS_BLOAT);
                    dataManager.increment(DataPoint.VENG_CASTS_TOTAL);
                    break;
                case 406:
                    dataManager.increment(DataPoint.VENG_PROCS_BLOAT);
                    dataManager.increment(DataPoint.VENG_PROCS_TOTAL);
                    dataManager.increment(DataPoint.VENG_DAMAGE_BLOAT, Integer.parseInt(subData[5]));
                    dataManager.increment(DataPoint.VENG_DAMAGE_TOTAL, Integer.parseInt(subData[5]));
                    break;
                case 410:
                    try
                    {
                        bloatThrallSpawns.add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                    }
                    catch (Exception e)
                    {

                    }
                    break;
                case 501:
                    dataManager.increment(DataPoint.KODAI_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.KODAI_BOPS, subData[4]);
                    break;
                case 502:
                    dataManager.increment(DataPoint.DWH_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.DWH_BOPS, subData[4]);
                    break;
                case 503:
                    dataManager.increment(DataPoint.BGS_WHACKS);
                    dataManager.incrementPlayerSpecific(DataPoint.BGS_WHACKS, subData[4]);
                    break;
                case 504:
                    dataManager.increment(DataPoint.CHALLY_POKE);
                    dataManager.incrementPlayerSpecific(DataPoint.CHALLY_POKE, subData[4]);
                    break;
                case 801:
                    int tick;
                    String player;
                    String animation;
                    String weapon;
                    String projectile;
                    String spotAnims;
                    try
                    {
                        player = subData[4].split(":")[0];
                        tick = Integer.parseInt(subData[4].split(":")[1]);
                        animation = subData[5];
                        spotAnims = subData[6];
                        String[] subsubData = subData[7].split(":");
                        weapon = subsubData[0];
                        int interactedIndex = -1;
                        int interactedID = -1;
                        if(subsubData.length > 2)
                        {
                            interactedIndex = Integer.parseInt(subsubData[1]);
                            interactedID = Integer.parseInt(subsubData[2]);
                        }
                        projectile = subData[8];
                        bloatAttacks.add(new PlayerDidAttack(player, animation, tick, weapon, projectile, spotAnims, interactedIndex, interactedID));
                    }
                    catch(Exception e)
                    {
                    }
                    break;
            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean parseMaiden() throws Exception {
        int activeIndex = 0;
        String lastProc = " 70s";
        loop:
        for (String s : globalData) {
            String[] subData = s.split(",", -1);
            switch (Integer.parseInt(subData[3])) {
                case 0:
                    raidStarted = new Date(Long.parseLong(subData[1]));
                    break;
                case 1:
                    for (int i = 4; i < 9; i++) {
                        if (!subData[i].equals("")) {
                            raidTeamSize++;
                            players.put(subData[i].replaceAll("[^\\p{ASCII}]", " ").replaceAll(" +", " "), 0);
                        }
                    }
                    break;
                case 2:
                    dataManager.hammer(com.TheatreTracker.utility.DataPoint.MAIDEN_DEFENSE);
                    dataManager.increment(DataPoint.HIT_HAMMERS_MAIDEN);
                    dataManager.incrementPlayerSpecific(DataPoint.HIT_HAMMERS_MAIDEN, subData[4]);
                    try
                    {
                        maidenOutlineBoxes.add(new DefenseReductionOutlineBox(subData[4], Integer.parseInt(subData[5]), new Color(0, 255, 0)));
                    }
                    catch(Exception e)
                    {

                    }
                    break;
                case 3:
                    dataManager.bgs(com.TheatreTracker.utility.DataPoint.MAIDEN_DEFENSE, Integer.parseInt(subData[5]));
                    dataManager.increment(DataPoint.ATTEMPTED_BGS_MAIDEN);
                    dataManager.increment(DataPoint.BGS_DAMAGE_MAIDEN, Integer.parseInt(subData[5]));
                    try
                    {
                        maidenOutlineBoxes.add(new DefenseReductionOutlineBox(subData[4], Integer.parseInt(subData[6]), new Color(0, 255, 0, Integer.parseInt(subData[5])*3)));
                    }
                    catch(Exception e)
                    {

                    }
                    break;
                case 4:
                    int percent = 100;
                    if(dataManager.get(DataPoint.MAIDEN_CHINS_THROWN) != 0)
                    {
                        double percentDouble = ((double)(dataManager.get(DataPoint.MAIDEN_CHINS_THROWN_WRONG_DISTANCE))/dataManager.get(DataPoint.MAIDEN_CHINS_THROWN))*100;
                        percent = (int) percentDouble;
                    }
                    dataManager.set(DataPoint.MAIDEN_CHIN_CORRECT_DISTANCE_PERCENT, percent);
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.MAIDEN_TOTAL_TIME) != 0) {
                        maidenReset = true;
                    } else {
                        if (!maidenSpawned)
                        {
                            maidenReset = true;
                            resetBeforeMaiden = true;
                        } else
                        {
                            maidenWipe = true;
                        }
                    }
                    globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
                    return false;
                case 5:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.MAIDEN_DEATHS);
                    dataManager.increment(DataPoint.TOTAL_DEATHS);
                    dataManager.incrementPlayerSpecific(DataPoint.TOTAL_DEATHS, subData[4]);
                    if(players.get(subData[4]) != null)
                    {
                        players.put(subData[4], players.get(subData[4]) + 1);
                    }
                    dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_DEATHS, subData[4]);
                    break;
                case 6:
                    break;
                case 7:
                    dataManager.increment(DataPoint.ATTEMPTED_HAMMERS_MAIDEN);
                    dataManager.incrementPlayerSpecific(DataPoint.ATTEMPTED_HAMMERS_MAIDEN, subData[4]);
                    break;
                case 9:
                    dataManager.increment(DataPoint.MAIDEN_BLOOD_THROWN);
                    break;
                case 10:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.MAIDEN_BLOOD_SPAWNED);
                    break;
                case 11:
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.MAIDEN_TOTAL_TIME) == 0) //TODO: see case 16 fix
                    {
                        dataManager.increment(com.TheatreTracker.utility.DataPoint.MAIDEN_CRABS_LEAKED);
                        int crabHP = -1;
                        try {
                            crabHP = Integer.parseInt(subData[5]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dataManager.increment(com.TheatreTracker.utility.DataPoint.MAIDEN_HP_HEALED, crabHP * 2);
                        int maxCrabHP = 100;
                        switch (players.size()) {
                            case 1:
                            case 2:
                            case 3:
                                maxCrabHP = 75;
                                break;
                            case 4:
                                maxCrabHP = 87;
                                break;
                        }
                        if (crabHP == maxCrabHP) {
                            dataManager.increment(com.TheatreTracker.utility.DataPoint.MAIDEN_CRABS_LEAKED_FULL_HP);
                        }

                        if (subData[4].contains("30")) {
                            maidenSkip = false;
                        }
                    }
                case 12:
                    maidenSpawned = true;
                    if (partyComplete) {
                        maidenDefenseAccurate = true;
                    }
                    break;
                case 13:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.MAIDEN_70_SPLIT, Integer.parseInt(subData[4]));
                    lastProc = " 70s";
                    break;
                case 14:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.MAIDEN_50_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(com.TheatreTracker.utility.DataPoint.MAIDEN_7050_SPLIT, Integer.parseInt(subData[4]) - dataManager.get(com.TheatreTracker.utility.DataPoint.MAIDEN_70_SPLIT));
                    lastProc = " 50s";
                    break;
                case 15:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.MAIDEN_30_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(com.TheatreTracker.utility.DataPoint.MAIDEN_5030_SPLIT, Integer.parseInt(subData[4]) - dataManager.get(com.TheatreTracker.utility.DataPoint.MAIDEN_50_SPLIT));
                    lastProc = " 30s";
                    break;
                case 16:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.MAIDEN_TOTAL_TIME, Integer.parseInt(subData[4]) + 7);
                    dataManager.set(com.TheatreTracker.utility.DataPoint.MAIDEN_SKIP_SPLIT, dataManager.get(com.TheatreTracker.utility.DataPoint.MAIDEN_TOTAL_TIME) - dataManager.get(com.TheatreTracker.utility.DataPoint.MAIDEN_30_SPLIT));
                    if (globalData.get(activeIndex + 1).split(",", -1)[3].equals("4"))
                        maidenReset = true;
                    break loop;
                case 17:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.MAIDEN_TOTAL_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(com.TheatreTracker.utility.DataPoint.MAIDEN_SKIP_SPLIT, dataManager.get(com.TheatreTracker.utility.DataPoint.MAIDEN_TOTAL_TIME) - dataManager.get(com.TheatreTracker.utility.DataPoint.MAIDEN_30_SPLIT));
                    if (globalData.get(activeIndex + 1).split(",", -1)[3].equals("4"))
                        maidenReset = true;
                    break loop;
                case 19:
                    if (!maidenScuffed) {
                        firstMaidenCrabScuffed = lastProc;
                    }
                    maidenScuffed = true;
                    break;
                case 20:
                    //todo: joined after maiden was kill. mark this somehow?
                    maidenReset = true; //TODO remove
                    break loop;
                case 99:
                    spectated = true;
                    break;
                case 100:
                    partyComplete = true;
                    break;
                case 101:
                    partyComplete = false;
                    break;
                case 111:
                    dataManager.increment(DataPoint.MAIDEN_DINHS_SPECS);
                    dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_DINHS_SPECS, subData[4]);
                    String[] targets = subData[6].split(":");
                    int targetCountThisSpec = 0;
                    int crabCountThisSpec = 0;
                    String[] specData = subData[7].split(":");
                    if(specData.length != 5)
                    {
                        break;
                    }
                    for(String target : targets)
                    {
                        String[] targetData = target.split("~");
                        if(targetData.length == 3)
                        {
                            targetCountThisSpec++;
                            String npcName = targetData[0];
                            String spawnID = targetData[1];
                            int hp = Integer.parseInt(targetData[2]);
                            if(!spawnID.equals("^")) //Target is crab
                            {
                                crabCountThisSpec++;
                            }

                        }
                    }
                    int averageHP = Integer.parseInt(specData[0]);
                    int belowThreshold = Integer.parseInt(specData[1]);
                    boolean didDoubleHit = Boolean.parseBoolean(specData[4]);


                    if(dataManager.get(DataPoint.MAIDEN_DINHS_SPECS) == 0)
                    {
                        int percentCrabsTargeted = (int) ((((double)crabCountThisSpec)/targetCountThisSpec)*100);
                        int percentCrabsUnder27Targeted = (int) ((((double)belowThreshold)/crabCountThisSpec)*100);

                        dataManager.set(DataPoint.MAIDEN_DINHS_TARGETS_HIT, targetCountThisSpec);
                        dataManager.set(DataPoint.MAIDEN_DINHS_CRABS_HIT, crabCountThisSpec);
                        dataManager.set(DataPoint.MAIDEN_DINHS_AVERAGE_HP_HIT, averageHP);
                        dataManager.set(DataPoint.MAIDEN_DINHS_CRABS_UNDER_27_TARGETED, belowThreshold);
                        dataManager.set(DataPoint.MAIDEN_DINHS_PERCENT_TARGETS_CRAB, percentCrabsTargeted);
                        dataManager.set(DataPoint.MAIDEN_DINHS_CRABS_UNDER_27_TARGETED_PERCENT, percentCrabsUnder27Targeted);
                    }
                    else
                    {
                        int previousAverage = dataManager.get(DataPoint.MAIDEN_DINHS_AVERAGE_HP_HIT);
                        int previousCrabsHit = dataManager.get(DataPoint.MAIDEN_DINHS_CRABS_HIT);
                        int previousTotalHit = dataManager.get(DataPoint.MAIDEN_DINHS_TARGETS_HIT);
                        int previousBelow27Hit = dataManager.get(DataPoint.MAIDEN_DINHS_CRABS_UNDER_27_TARGETED);

                        dataManager.set(DataPoint.MAIDEN_DINHS_TARGETS_HIT, previousTotalHit+targetCountThisSpec);
                        dataManager.set(DataPoint.MAIDEN_DINHS_CRABS_HIT, previousCrabsHit+crabCountThisSpec);
                        dataManager.set(DataPoint.MAIDEN_DINHS_CRABS_UNDER_27_TARGETED, previousBelow27Hit+belowThreshold);

                        int roundedAverageSum = ((previousAverage*previousCrabsHit)+(averageHP*crabCountThisSpec));
                        int roundedAverageCumulative = (int)(((double)roundedAverageSum)/(previousCrabsHit+crabCountThisSpec));

                        dataManager.set(DataPoint.MAIDEN_DINHS_AVERAGE_HP_HIT, roundedAverageCumulative);

                        int percentTargetedCumulative = (int)(((double)(previousCrabsHit+crabCountThisSpec)/(previousTotalHit+targetCountThisSpec))*100);

                        dataManager.set(DataPoint.MAIDEN_DINHS_PERCENT_TARGETS_CRAB, percentTargetedCumulative);

                        int percentBelow27Cumulative = (int)((double)(previousBelow27Hit+belowThreshold)/(previousCrabsHit+crabCountThisSpec));

                        dataManager.set(DataPoint.MAIDEN_DINHS_CRABS_UNDER_27_TARGETED_PERCENT, percentBelow27Cumulative);
                    }
                    break;
                case 113:
                    dataManager.increment(DataPoint.MAIDEN_CHINS_THROWN);
                    dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_CHINS_THROWN, subData[4]);
                    try
                    {
                        if(Integer.parseInt(subData[5]) < 4 || Integer.parseInt(subData[5]) > 6)
                        {
                            dataManager.increment(DataPoint.MAIDEN_CHINS_THROWN_WRONG_DISTANCE);
                            dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_CHINS_THROWN_WRONG_DISTANCE, subData[4]);
                        }
                    }
                    catch
                    (Exception e)
                    {

                    }
                    break;
                case 201:
                    maidenStartAccurate = true;
                    break;
                case 301:
                    maidenEndAccurate = true;
                    maidenTimeAccurate = maidenStartAccurate;
                    break;
                case 401:
                    hardMode = true;
                    break;
                case 402:
                    storyMode = true;
                    break;
                case 403:
                    dataManager.increment(DataPoint.THRALL_ATTACKS_MAIDEN);
                    dataManager.increment(DataPoint.THRALL_ATTACKS_TOTAL);
                    break;
                case 404:
                    int amount = Integer.parseInt(subData[5]);
                    dataManager.increment(DataPoint.THRALL_DAMAGE_MAIDEN, amount);
                    dataManager.increment(DataPoint.THRALL_DAMAGE_TOTAL, amount);
                    break;
                case 405:
                    dataManager.increment(DataPoint.VENG_CASTS_MAIDEN);
                    dataManager.increment(DataPoint.VENG_CASTS_TOTAL);
                    break;
                case 406:
                    dataManager.increment(DataPoint.VENG_PROCS_MAIDEN);
                    dataManager.increment(DataPoint.VENG_PROCS_TOTAL);
                    dataManager.increment(DataPoint.VENG_DAMAGE_MAIDEN, Integer.parseInt(subData[5]));
                    dataManager.increment(DataPoint.VENG_DAMAGE_TOTAL, Integer.parseInt(subData[5]));
                    break;
                case 410:
                    try
                    {
                        maidenThrallSpawns.add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                    }
                    catch (Exception e)
                    {

                    }
                    break;
                case 411:
                    dataManager.increment(DataPoint.MAIDEN_PLAYER_STOOD_IN_THROWN_BLOOD);
                    dataManager.increment(DataPoint.MAIDEN_HEALS_FROM_THROWN_BLOOD, Integer.parseInt(subData[5]));

                    dataManager.increment(DataPoint.MAIDEN_PLAYER_STOOD_IN_BLOOD);
                    dataManager.increment(DataPoint.MAIDEN_HEALS_FROM_ANY_BLOOD, Integer.parseInt(subData[5]));

                    dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_PLAYER_STOOD_IN_THROWN_BLOOD, subData[4]);
                    dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_HEALS_FROM_THROWN_BLOOD, subData[4], Integer.parseInt(subData[5]));

                    dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_PLAYER_STOOD_IN_BLOOD, subData[4]);
                    dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_HEALS_FROM_ANY_BLOOD, subData[4], Integer.parseInt(subData[5]));
                    break;
                case 412:
                    dataManager.increment(DataPoint.MAIDEN_PLAYER_STOOD_IN_SPAWNED_BLOOD);
                    dataManager.increment(DataPoint.MAIDEN_HEALS_FROM_SPAWNED_BLOOD, Integer.parseInt(subData[5]));

                    dataManager.increment(DataPoint.MAIDEN_PLAYER_STOOD_IN_BLOOD);
                    dataManager.increment(DataPoint.MAIDEN_HEALS_FROM_ANY_BLOOD, Integer.parseInt(subData[5]));

                    dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_PLAYER_STOOD_IN_SPAWNED_BLOOD, subData[4]);
                    dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_HEALS_FROM_SPAWNED_BLOOD, subData[4], Integer.parseInt(subData[5]));

                    dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_PLAYER_STOOD_IN_BLOOD, subData[4]);
                    dataManager.incrementPlayerSpecific(DataPoint.MAIDEN_HEALS_FROM_ANY_BLOOD, subData[4], Integer.parseInt(subData[5]));
                    break;
                case 501:
                    dataManager.increment(DataPoint.KODAI_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.KODAI_BOPS, subData[4]);
                    break;
                case 502:
                    dataManager.increment(DataPoint.DWH_BOPS);
                    dataManager.incrementPlayerSpecific(DataPoint.DWH_BOPS, subData[4]);
                    break;
                case 503:
                    dataManager.increment(DataPoint.BGS_WHACKS);
                    dataManager.incrementPlayerSpecific(DataPoint.BGS_WHACKS, subData[4]);
                    break;
                case 504:
                    dataManager.increment(DataPoint.CHALLY_POKE);
                    dataManager.incrementPlayerSpecific(DataPoint.CHALLY_POKE, subData[4]);
                    break;
                case 801:
                    int tick;
                    String player;
                    String animation;
                    String weapon;
                    String projectile;
                    String spotAnims;
                    try
                    {
                        player = subData[4].split(":")[0];
                        tick = Integer.parseInt(subData[4].split(":")[1]);
                        animation = subData[5];
                        spotAnims = subData[6];
                        String[] subsubData = subData[7].split(":");
                        weapon = subsubData[0];
                        int interactedIndex = -1;
                        int interactedID = -1;
                        if(subsubData.length > 2)
                        {
                            interactedIndex = Integer.parseInt(subsubData[1]);
                            interactedID = Integer.parseInt(subsubData[2]);
                        }
                        projectile = subData[8];
                        maidenAttacks.add(new PlayerDidAttack(player, animation, tick, weapon, projectile, spotAnims, interactedIndex, interactedID));
                    }
                    catch(Exception e)
                    {
                    }
                    break;
            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private void finishRaid() {
        raidCompleted = true;
    }
}

