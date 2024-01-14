package com.TheatreTracker;

import com.TheatreTracker.utility.DataManager;
import com.TheatreTracker.utility.DataPoint;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.raids.RoomType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.TheatreTracker.RoomData.TobRoom.*;

@Slf4j

public class RoomData {

    private static final String EXIT_FLAG = "4";
    /* public static String[] DataPoint =
             {
             "Maiden bloods spawned",
             "Maiden crabs leaked",
             "Maiden def reduction",
             "Maiden deaths",
             "Bloat Downs",
             "Bloat 1st walk deaths",
             "Bloat first walk BGS",
             "Bloat deaths",
             "Nylo stalls pre 20",
             "Nylo stalls post 20",
             "Nylo total stalls",
             "Nylo range splits",
             "Nylo mage splits",
             "Nylo melee splits",
             "Nylo range rotations",
             "Nylo mage rotations",
             "Nylo melee roations",
             "Nylo def reduction",
             "Nylo deaths",
             "Sote specs p1",
             "Sote specs p2",
             "Sote specs p3",
             "Sote deaths",
             "Sote total specs hit",
             "Xarp def reduction",
             "Xarp deaths",
             "Xarpus healing",
             "Verzik bounces",
             "Verzik deaths",
             "Raid team size",
             "Maiden total time",
             "Maiden 70 split",
             "Maiden 50 split",
             "Maiden 30 split",
             "Maiden 70-50 split",
             "Maiden 50-30 split",
             "Maiden skip split",
             "Bloat total time",
             "Bloat first down split",
             "Nylocas total time",
             "Nylo boss spawn",
             "Nylo boss duration",
             "Nylo last wave",
             "Nylo cleanup",
             "Sotetseg total time",
             "Sote p1 split",
             "Sote p2 split",
             "Sote p3 split",
             "Sote maze1 split",
             "Sote maze2 split",
             "Xarpus total time",
             "Xarpus screech",
             "Xarpus post screech",
             "Verzik total time",
             "Verzik p1 split",
             "Verzik p2 split",
             "Verzik p2 duration",
             "Verzik p3 duration"
     };*/
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
    public ArrayList<String> players;

    public String[] raidDataRaw;

    public String activeValue = "";


    enum TobRoom {
        MAIDEN, BLOAT, NYLOCAS, SOTETSEG, XARPUS, VERZIK
    }

    public Date getDate() {
        return raidStarted;
    }

    public int getScale() {
        return raidTeamSize;
    }

    public boolean getTimeAccurate(com.TheatreTracker.utility.DataPoint param) {
        switch (param.room) {
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
            default:
                return false;
        }
    }

    public int getSpecificTimeInactive(String inactive)
    {
        if(inactive.equals("Overall Time"))
        {
            return getMaidenTime() + getBloatTime() + getNyloTime() + getSoteTime() + getXarpTime() + getVerzikTime();
        }
        return getValue(DataPoint.getValue(inactive));
    }
    public int getSpecificTime()
    {
        if(activeValue.equals("Overall Time"))
        {
            return getMaidenTime() + getBloatTime() + getNyloTime() + getSoteTime() + getXarpTime() + getVerzikTime();
        }
        return getValue(DataPoint.getValue(activeValue));
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

    public int getValue(com.TheatreTracker.utility.DataPoint point) {
        return dataManager.get(point);
    }

    public int getTimeSum() {
        return getMaidenTime() + getBloatTime() + getNyloTime() + getSoteTime() + getXarpTime() + getVerzikTime(); /*
                + dataManager.get(com.TheatreTracker.utility.DataPoint.BLOAT_TOTAL_TIME)
                + dataManager.get(com.TheatreTracker.utility.DataPoint.NYLO_TOTAL_TIME)
                + dataManager.get(com.TheatreTracker.utility.DataPoint.SOTE_TOTAL_TIME)
                + dataManager.get(com.TheatreTracker.utility.DataPoint.XARP_TOTAL_TIME)
                + dataManager.get(com.TheatreTracker.utility.DataPoint.VERZIK_TOTAL_TIME);*/
    }

    public RoomData(String[] parameters) throws Exception {
        dataManager = new DataManager();
        raidDataRaw = parameters;
        partyComplete = false;
        maidenDefenseAccurate = false;
        bloatDefenseAccurate = false;
        nyloDefenseAccurate = false;
        soteDefenseAccurate = false;
        xarpDefenseAccurate = false;
        hardMode = false;
        storyMode = false;

        players = new ArrayList<>();
        globalData = new ArrayList<String>(Arrays.asList(parameters));

        int room = -1;
        for (String s : globalData) {
            String[] subData = s.split(",");
            int key = Integer.parseInt(subData[3]);
            if (key == 98) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean parseVerzik() {
        int activeIndex = 0;
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
                            players.add(subData[i].replaceAll("[^\\p{ASCII}]", " ").replaceAll(" +", " "));
                        }
                    }
                    break;
                case 2:
                    dataManager.increment(DataPoint.HIT_HAMMERS_VERZIK);
                    break;
                case 3:
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
                case 6:
                    break;
                case 7:
                    dataManager.increment(DataPoint.ATTEMPTED_HAMMERS_VERZIK);
                    break;
                case 70:
                    break;
                case 71:
                    verzikStarted = true;
                case 72:
                    break;
                case 73:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.VERZIK_P1_SPLIT, Integer.parseInt(subData[4]));
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
                    break;
                case 78:
                    dataManager.increment(DataPoint.VERZIK_CRABS_SPAWNED);
                    break;
                case 79:
                    verzikDawnCount++;
                    verzikDawnDamage += Integer.parseInt(subData[5]);
                    break;
                case 80:
                    verzikRedsProc = Integer.parseInt(subData[4]);
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
                            players.add(subData[i].replaceAll("[^\\p{ASCII}]", " ").replaceAll(" +", " "));
                        }
                    }
                    break;
                case 2:
                    dataManager.hammer(com.TheatreTracker.utility.DataPoint.XARP_DEFENSE);
                    dataManager.increment(DataPoint.HIT_HAMMERS_XARP);
                    break;
                case 3:
                    dataManager.bgs(com.TheatreTracker.utility.DataPoint.XARP_DEFENSE, Integer.parseInt(subData[5]));
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
                    break;
                case 6:
                    break;
                case 7:
                    dataManager.increment(DataPoint.ATTEMPTED_HAMMERS_XARP);
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
                            players.add(subData[i].replaceAll("[^\\p{ASCII}]", " ").replaceAll(" +", " "));
                        }
                    }
                    break;
                case 2:
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.SOTE_P1_SPLIT) == 0) {
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
                    break;
                case 3:
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
                    break;
                case 5:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.SOTE_DEATHS);
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
                    dataManager.set(com.TheatreTracker.utility.DataPoint.SOTE_M1_SPLIT, Integer.parseInt(subData[4])-dataManager.get(DataPoint.SOTE_P1_SPLIT));
                    break;
                case 54:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.SOTE_P2_SPLIT, Integer.parseInt(subData[4])-dataManager.get(DataPoint.SOTE_P1_SPLIT)-dataManager.get(DataPoint.SOTE_M1_SPLIT));
                    break;
                case 55:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.SOTE_M2_SPLIT, Integer.parseInt(subData[4])-dataManager.get(DataPoint.SOTE_P2_SPLIT)-dataManager.get(DataPoint.SOTE_M1_SPLIT)-dataManager.get(DataPoint.SOTE_P1_SPLIT));
                    dataManager.set(DataPoint.SOTE_MAZE_SUM, dataManager.get(DataPoint.SOTE_M1_SPLIT)+ dataManager.get(DataPoint.SOTE_M2_SPLIT));
                    break;
                case 56:
                    break;
                case 57:
                    dataManager.set(com.TheatreTracker.utility.DataPoint.SOTE_TOTAL_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(com.TheatreTracker.utility.DataPoint.SOTE_P3_SPLIT, dataManager.get(com.TheatreTracker.utility.DataPoint.SOTE_TOTAL_TIME) - dataManager.get(com.TheatreTracker.utility.DataPoint.SOTE_M2_SPLIT)-dataManager.get(DataPoint.SOTE_P2_SPLIT)-dataManager.get(DataPoint.SOTE_M1_SPLIT)-dataManager.get(DataPoint.SOTE_P1_SPLIT));
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

            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean parseNylo() {
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
                            players.add(subData[i].replaceAll("[^\\p{ASCII}]", " ").replaceAll(" +", " "));
                        }
                    }
                    break;
                case 2:
                    dataManager.increment(DataPoint.HIT_HAMMERS_NYLO);
                    break;
                case 44:
                case 6:
                    break;
                case 3:
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.NYLO_BOSS_SPAWN) != 0) {
                        dataManager.bgs(com.TheatreTracker.utility.DataPoint.NYLO_DEFENSE, Integer.parseInt(subData[5]));
                    }
                    break;
                case 4:
                    if(pillarDespawnTick-5 < dataManager.get(DataPoint.NYLO_BOSS_SPAWN))
                    {
                        dataManager.set(DataPoint.NYLO_BOSS_SPAWN, 0);
                        dataManager.set(DataPoint.NYLO_TOTAL_TIME, 0);
                    }
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.NYLO_TOTAL_TIME) != 0) {
                        nyloReset = true;
                    } else {
                        if (!nyloStarted) {
                            if (!bloatEndAccurate) {
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
                    break;
                case 7:
                    dataManager.increment(DataPoint.ATTEMPTED_HAMMERS_NYLO);
                    break;
                case 30:
                    nyloStarted = true;
                    break;
                case 31:
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

            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
        return true;
    }

    private boolean parseBloat() {
        int activeIndex = 0;
        bloatDefenseAccurate = maidenDefenseAccurate;
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
                            players.add(subData[i].replaceAll("[^\\p{ASCII}]", " ").replaceAll(" +", " "));
                        }
                    }
                    break;
                case 2:
                    dataManager.increment(DataPoint.HIT_HAMMERS_BLOAT);
                    break;
                case 6:
                    break;
                case 24:
                    dataManager.set(DataPoint.BLOAT_HP_FIRST_DOWN, Integer.parseInt(subData[4]));
                    break;
                case 25:
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.BLOAT_DOWNS) == 0)
                    {
                        dataManager.increment(DataPoint.BLOAT_FIRST_WALK_SCYTHES);
                        if(subData[4].equals("RostiKz"))
                        {
                            dataManager.increment(DataPoint.BLOAT_SCY_ROSTIKZ);
                        }
                        else if(subData[4].equals("Caps lock13"))
                        {
                            dataManager.increment(DataPoint.BLOAT_SCY_CAPS);
                        }
                        bloatScytheBeforeFirstDown++;
                    }
                    break;
                case 3:
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.BLOAT_DOWNS) == 0) {
                        dataManager.bgs(com.TheatreTracker.utility.DataPoint.BLOAT_DEFENSE, 2 * Integer.parseInt(subData[5]));
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
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.BLOAT_DOWNS) == 0) {
                        dataManager.increment(com.TheatreTracker.utility.DataPoint.BLOAT_FIRST_WALK_DEATHS);
                    }
                    break;
                case 7:
                    dataManager.increment(DataPoint.ATTEMPTED_HAMMERS_BLOAT);
                    break;
                case 20:
                    bloatStarted = true;
                    if (partyComplete) {
                        bloatDefenseAccurate = true;
                    }
                    break;
                case 21:
                    if (dataManager.get(com.TheatreTracker.utility.DataPoint.BLOAT_DOWNS) == 0) {
                        dataManager.set(com.TheatreTracker.utility.DataPoint.BLOAT_FIRST_DOWN_TIME, Integer.parseInt(subData[4]));
                    }
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.BLOAT_DOWNS);
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
                            players.add(subData[i].replaceAll("[^\\p{ASCII}]", " ").replaceAll(" +", " "));
                        }
                    }
                    break;
                case 2:
                    dataManager.hammer(com.TheatreTracker.utility.DataPoint.MAIDEN_DEFENSE);
                    dataManager.increment(DataPoint.HIT_HAMMERS_MAIDEN);
                    break;
                case 3:
                    dataManager.bgs(com.TheatreTracker.utility.DataPoint.MAIDEN_DEFENSE, Integer.parseInt(subData[5]));
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
                        if (!maidenSpawned) {
                            maidenReset = true;
                            resetBeforeMaiden = true;
                        } else {
                            maidenWipe = true;
                        }
                    }
                    globalData = new ArrayList(globalData.subList(activeIndex + 1, globalData.size()));
                    return false;
                case 5:
                    dataManager.increment(com.TheatreTracker.utility.DataPoint.MAIDEN_DEATHS);
                    break;
                case 6:
                    break;
                case 7:
                    dataManager.increment(DataPoint.ATTEMPTED_HAMMERS_MAIDEN);
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
                    try
                    {
                        if(Integer.parseInt(subData[5]) < 4 || Integer.parseInt(subData[5]) > 6)
                        {
                            dataManager.increment(DataPoint.MAIDEN_CHINS_THROWN_WRONG_DISTANCE);
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

