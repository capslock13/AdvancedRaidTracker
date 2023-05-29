package com.cTimers;

import com.cTimers.utility.cDataManager;
import com.cTimers.utility.cDataPoint;
import lombok.extern.slf4j.Slf4j;
import com.cTimers.exceptions.cDataOutOfOrderException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.cTimers.cRoomData.TobRoom.*;
@Slf4j
/**
Key:Description //Value1, Value2, Value3, Value4, Value5
 0:Entered tob //0, 0, 0, 0, 0
 1:Party members //Player1, Player2, Player3, Player4, Player5
 2:DWH //Player, 0, 0, 0, 0
 3:BGS //Player, Damage, 0, 0, 0
 4:Left tob region //Last region, 0, 0, 0, 0
 5:Player died //Player, 0, 0, 0, 0
 6:Entered new tob region //Region, 0, 0, 0, 0 // Regions: (Bloat 1, Nylo 2, Sote 3, Xarpus 4, Verzik 5)
 8: DB Specs // Player, DMG

 10: Blood Spawned //room time, 0, 0, 0, 0
 11: Crab leaked //room time, Description (E.G. N1 30s), Last known health, Current maiden dealth, 0
 12: Maiden spawned //0, 0, 0, 0, 0
 13: Maiden 70s //room time, 0, 0, 0, 0
 14: Maiden 50s //room time, 0, 0, 0, 0
 15: Maiden 30s //room time, 0, 0, 0, 0
 16: Maiden 0 hp //room time, 0, 0, 0, 0
 17: Maiden despawned //room time, 0, 0, 0, 0
 18: Matomenos spawned //position, 0, 0, 0, 0
 19: Maiden Scuffed

 20: Bloat spawned //0, 0, 0, 0, 0
 21: Bloat down //Room time, 0, 0, 0, 0
 22: Bloat 0 HP //room time, 0, 0, 0, 0
 23: Bloat despawn //room time, 0, 0, 0, 0
 24: Bloat HP on 1st down //HP, 0, 0, 0,0

 30: Nylo pillars spawned //0, 0, 0, 0 ,0
 31: Nylo stall //Wave, room time, 0, 0, 0
 32: Range split //Wave, room time, 0, 0, 0
 33: Mage split //Wave, room time, 0, 0, 0
 34: Melee split //Wave, room time, 0, 0, 0
 35: Last wave //Room time, 0, 0, 0, 0
 40: Boss spawn //Room time, 0, 0, 0, 0
 41: Melee rotation //room time, 0, 0, 0, 0
 42: Mage rotation //room time, 0, 0, 0, 0
 43: Range rotation //room time, 0, 0, 0, 0
 44: Nylo 0 HP // room time, 0, 0, 0, 0
 45: Nylo despawned // room time, 0, 0, 0, 0

 5x: sote

 60: xarpus spawned //0, 0, 0, 0, 0
 61: xarpus room started //0, 0, 0, 0, 0
 62: xarpus heal //amount, room time, 0, 0, 0
 63: xarpus screech //room time, 0, 0, 0, 0
 64: xarpus 0 hp //room time, 0, 0, 0, 0
 65: xarpus despawned //room time, 0, 0, 0, 0

 70: verzik spawned //room time, 0, 0, 0, 0
 71: verzik p1 started //0, 0, 0, 0, 0
 72: verzik p1 0 hp //room time, 0, 0, 0, 0
 73: verzik p1 despawned //room time, 0, 0, 0, 0
 80: verzik p2 reds proc // room time, 0, 0, 0, 0
 74: verzik p2 end
 75: verzik p3 0 hp
 76: verzik p3 despawned
 77: verzik bounce //player, room time, 0, 0, 0

 100: party complete
 101: party incomplete
 102: party accurate pre maiden

 201 accurate maiden start
 202 accurate bloat start
 203 accurate nylo start
 204 accurate sote start
 205 accurate xarp start
 206 accurate verzik start

 301 accurate maiden end
 302 accurate bloat end
 303 accurate nylo end
 304 accurate sote end
 305 accurate xarp end
 306 accurate verzik end

 **/

public class cRoomData
{

    public static String[] DataPoint = {
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
    };
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

    public int getMaidenCrabsLeakedFullHP = 0;
    public int maidenHeal = 0;
    public boolean maidenDefenseAccurate;
    public int index = -1;

    public boolean maidenScuffed = false;
    public String firstMaidenCrabScuffed = "";

    public boolean maidenSpawned;
    public boolean maidenSkip;
    public boolean maidenReset;
    public boolean maidenWipe;


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


    enum TobRoom{
        MAIDEN, BLOAT, NYLOCAS, SOTETSEG, XARPUS, VERZIK
    }

    public Date getDate()
    {
        return raidStarted;
    }

    public int getScale()
    {
        return raidTeamSize;
    }

    public boolean getTimeAccurate(cDataPoint param)
    {
        switch(param.room)
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
            default:
                return false;
        }
    }

    public int getMaidenTime() { return getValue(cDataPoint.MAIDEN_TOTAL_TIME); }
    public int getBloatTime() { return getValue(cDataPoint.BLOAT_TOTAL_TIME); }
    public int getNyloTime() { return getValue(cDataPoint.NYLO_TOTAL_TIME); }
    public int getSoteTime() { return getValue(cDataPoint.SOTE_TOTAL_TIME); }
    public int getXarpTime() { return getValue(cDataPoint.XARP_TOTAL_TIME); }
    public int getVerzikTime() { return getValue(cDataPoint.VERZIK_TOTAL_TIME); }

    /*public int getValueFromKey(int key)
    {
        if(key < 4)
        {
            if(!maidenStartAccurate || !maidenEndAccurate)
            {
                return -1;
            }
        }
        if(key > 3 && key < 8)
        {
            if(!bloatStartAccurate || !bloatEndAccurate)
            {
                return -1;
            }
        }
        if(key > 7 && key < 19)
        {
            if(!nyloStartAccurate || !nyloEndAccurate)
            {
                return -1;
            }
        }
        if(key > 18 && key < 24)
        {
            if(!soteStartAccurate || !soteEndAccurate)
            {
                return -1;
            }
        }
        if(key > 23 && key < 27)
        {
            if(!xarpEndAccurate || !xarpStartAccurate)
            {
                return -1;
            }
        }
        if(key > 26 && key < 29)
        {
            if(!verzikStartAccurate || !verzikEndAccurate)
            {
                return -1;
            }
        }
        switch (key)
        {
            case 0: // Maiden bloods spawned
                return maidenBloodsSpawned;
            case 1: // Maiden crabs leaked
                return maidenCrabsLeaked;
            case 2: // Maiden def reduction
                return maidenDefense;
            case 3: // Maiden deaths
                return maidenDeaths;
            case 4: // Bloat Downs
                return bloatDowns;
            case 5: // Bloat 1st walk deaths
                return bloatFirstWalkDeaths;
            case 6: // Bloat first walk BGS
                return bloatfirstWalkDefense;
            case 7: // Bloat deaths
                return bloatDeaths;
            case 8: // Nylo stalls pre 20
                return nyloStallsPre20;
            case 9: // Nylo stalls post 20
                return nyloStallsPost20;
            case 10: // Nylo total stalls
                return nyloStallsTotal;
            case 11: // Nylo range splits
                return nyloRangeSplits;
            case 12: // Nylo mage splits
                return nyloMageSplits;
            case 13: // Nylo melee splits
                return nyloMeleeSplits;
            case 14: // Nylo range rotations
                return nyloRangeRotations;
            case 15: // Nylo mage rotations
                return nyloMageRotations;
            case 16: // Nylo melee roations
                return nyloMeleeRotations;
            case 17: // Nylo def reduction
                return nyloDefenseReduction;
            case 18: // Nylo deaths
                return nyloDeaths;
            case 19: // Sote specs p1
                return soteSpecsP1;
            case 20: // Sote specs p2
                return soteSpecsP2;
            case 21: // Sote specs p3
                return soteSpecsP3;
            case 22: // Sote deaths
                return soteDeaths;
            case 23: // Sote total specs hit
                return soteSpecsTotal;
            case 24: // Xarp def reduction
                return xarpDefense;
            case 25: // Xarp deaths
                return xarpDeaths;
            case 26: // Xarpus healing
                return xarpHealing;
            case 27: // Verzik bounces
                return verzikBounces;
            case 28: // Verzik deaths
                return verzikDeaths;
            case 29: // Raid team size
                return raidTeamSize;
            case 30: // Maiden total time
                return maidenTime;
            case 31: // Maiden 70 split
                return maiden70Split;
            case 32: // Maiden 50 split
                return maiden50Split;
            case 33: // Maiden 30 split
                return maiden30Split;
            case 34: // Maiden 70-50 split
                return maiden50Split - maiden70Split;
            case 35: // Maiden 50-30 split
                return maiden30Split - maiden50Split;
            case 36: // Maiden skip split
                return maidenTime - maiden30Split;
            case 37: // Bloat total time
                return bloatTime;
            case 38: // Bloat first down split
                return bloatFirstDownSplit;
            case 39: // Nylocas total time
                return nyloTime;
            case 40: // Nylo boss spawn
                return nyloBossSpawn;
            case 41: // Nylo boss duration
                return nyloTime - nyloBossSpawn;
            case 42: // Nylo last wave
                return nyloLastWave;
            case 43: // Nylo cleanup
                return nyloBossSpawn - nyloLastWave;
            case 44: // Sotetseg total time
                return soteTime;
            case 45: // Sote p1 split
                return soteFirstMazeStartSplit;
            case 46: // Sote p2 split
                return soteSecondMazeStartSplit - soteFirstMazeEndSplit;
            case 47: // Sote p3 split
                return soteTime - soteSecondMazeEndSplit;
            case 48: // Sote maze1 split
                return soteFirstMazeEndSplit - soteFirstMazeStartSplit;
            case 49: // Sote maze2 split
                return soteSecondMazeEndSplit - soteSecondMazeStartSplit;
            case 50: // Xarpus total time
                return xarpTime;
            case 51: //Xarpus Screech
                return xarpScreechSplit;
            case 52: //Xarpus Post Screech
                return xarpTime - xarpScreechSplit;
            case 53: // Verzik Total Time
                return verzikTime;
            case 54: // Verzik p1
                return verzikP1Split;
            case 55: // verzik p2 split from start
                return verzikP2Split;
            case 56: // verzik p2 duration
                return verzikP2Split - verzikP1Split;
            case 57: // verzik p3 duration
                return verzikTime - verzikP2Split;
            default:
                return -1;
        }
    }*/

    public boolean getOverallTimeAccurate()
    {
        return maidenStartAccurate && maidenEndAccurate
                && bloatStartAccurate && bloatEndAccurate
                && nyloStartAccurate && nyloEndAccurate
                && soteStartAccurate && soteEndAccurate
                && xarpStartAccurate && xarpEndAccurate
                && verzikStartAccurate && verzikEndAccurate;
    }

    public boolean checkExit(TobRoom room)
    {
        if(globalData.size() == 0 || globalData.get(0).split(",", -1)[3].equals("4"))
        {
            switch (room) {
                case MAIDEN:
                    maidenReset = true;
                    break;
                case BLOAT:
                    bloatReset = true;
                    break;
                case NYLOCAS:
                    nyloReset = true;
                    break;
                case SOTETSEG:
                    soteReset = true;
                    break;
            } // TODO missing a case
            return false;
        }
        return true;
    }

    private cDataManager dataManager;

    public int getValue(String name)
    {
        return dataManager.get(name);
    }

    public int getValue(cDataPoint point)
    {
        return dataManager.get(point);
    }

    public int getTimeSum()
    {
        return dataManager.get(cDataPoint.MAIDEN_TOTAL_TIME)
                + dataManager.get(cDataPoint.BLOAT_TOTAL_TIME)
                + dataManager.get(cDataPoint.NYLO_TOTAL_TIME)
                + dataManager.get(cDataPoint.SOTE_TOTAL_TIME)
                + dataManager.get(cDataPoint.XARP_TOTAL_TIME)
                + dataManager.get(cDataPoint.VERZIK_TOTAL_TIME);
    }

    public cRoomData(String[] parameters) throws Exception
    {
        dataManager = new cDataManager();

        partyComplete = false;
        maidenDefenseAccurate = false;
        bloatDefenseAccurate = false;
        nyloDefenseAccurate = false;
        soteDefenseAccurate = false;
        xarpDefenseAccurate = false;

        players = new ArrayList<>();
        globalData = new ArrayList<String>(Arrays.asList(parameters));

        int room = -1;
        for(String s : globalData)
        {
            String[] subData = s.split(",");
            int key = Integer.parseInt(subData[3]);
            if(key == 98)
            {
                room = Integer.parseInt(subData[4]);
                spectated = true;
            }
        }
        if(room > 0)
        {
            switch(room)
            {
                case 1:
                    if(!(checkExit(MAIDEN) && parseBloat()))
                        break;
                case 2:
                    if(!(checkExit(BLOAT) && parseNylo()))
                        break;
                case 3:
                    if(!(checkExit(NYLOCAS) && parseSotetseg()))
                        break;
                case 4:
                    if(!(checkExit(SOTETSEG) && parseXarpus()))
                        break;
                case 5:
                    if(checkExit(XARPUS) && parseVerzik())
                    {
                        finishRaid();
                    }
            }
        }
        else {
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
            } catch (cDataOutOfOrderException e) {
                log.error("FAILED TO PARSE");
                e.printStackTrace();
            }
        }
    }

    private boolean parseVerzik()
    {
        int activeIndex = 0;
        for(String s : globalData)
        {
            String[] subData = s.split(",", -1);
            switch(Integer.parseInt(subData[3]))
            {
                case 0:
                    raidStarted = new Date(Long.parseLong(subData[1]));
                    break;
                case 1:
                    for(int i = 4; i < 9; i++)
                    {
                        if(!subData[i].equals(""))
                        {
                            raidTeamSize++;
                            players.add(subData[i].replaceAll("[^\\p{ASCII}]", ""));
                        }
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    if(dataManager.get(cDataPoint.VERZIK_TOTAL_TIME) == 0)
                    {
                        if(!verzikStarted)
                        {
                            xarpReset = true;
                        }
                        else
                        {
                            verzikWipe = true;
                        }
                    }
                    else
                    {
                        return true;
                    }
                    globalData = new ArrayList<>(globalData.subList(activeIndex + 1, globalData.size()));
                    return false;
                case 5:
                    dataManager.increment(cDataPoint.VERZIK_DEATHS);
                case 6:
                    break;
                case 70:
                    break;
                case 71:
                    verzikStarted = true;
                case 72:
                    break;
                case 73:
                    dataManager.set(cDataPoint.VERZIK_P1_SPLIT, Integer.parseInt(subData[4]));
                    break;
                case 74:
                    dataManager.set(cDataPoint.VERZIK_P2_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(cDataPoint.VERZIK_P2_DURATION, dataManager.get(cDataPoint.VERZIK_P2_SPLIT) - dataManager.get(cDataPoint.VERZIK_P1_SPLIT));

                    break;
                case 75:
                    break;
                case 76:
                    dataManager.set(cDataPoint.VERZIK_TOTAL_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(cDataPoint.VERZIK_P3_DURATION, dataManager.get(cDataPoint.VERZIK_TOTAL_TIME) - dataManager.get(cDataPoint.VERZIK_P2_SPLIT));
                    break;
                case 77:
                    dataManager.increment(cDataPoint.VERZIK_BOUNCES);
                    break;
                case 78:
                    verzikCrabsSpawned++;
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

            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex+1, globalData.size()));
        return true;
    }

    private boolean parseXarpus()
    {

        int activeIndex = 0;
        loop: for(String s : globalData)
        {
            String[] subData = s.split(",", -1);
            switch(Integer.parseInt(subData[3]))
            {
                case 0:
                    raidStarted = new Date(Long.parseLong(subData[1]));
                    break;
                case 1:
                    for(int i = 4; i < 9; i++)
                    {
                        if(!subData[i].equals(""))
                        {
                            raidTeamSize++;
                            players.add(subData[i].replaceAll("[^\\p{ASCII}]", ""));
                        }
                    }
                    break;
                case 2:
                    dataManager.hammer(cDataPoint.XARP_DEFENSE);
                    break;
                case 3:
                    dataManager.bgs(cDataPoint.XARP_DEFENSE, Integer.parseInt(subData[5]));
                    break;
                case 4:
                    if(dataManager.get(cDataPoint.XARP_TOTAL_TIME) != 0)
                    {
                        xarpReset = true;
                    }
                    else
                    {
                        if(!xarpStarted)
                        {
                            soteReset = true;
                        }
                        else
                        {
                            xarpWipe = true;
                        }
                    }
                    globalData = new ArrayList(globalData.subList(activeIndex+1, globalData.size()));
                    return false;
                case 5:
                    dataManager.increment(cDataPoint.XARP_DEATHS);
                    break;
                case 6:
                    break;
                case 60:
                    break;
                case 61:
                    xarpStarted = true;
                    if(partyComplete)
                    {
                        xarpDefenseAccurate = true;
                    }
                    break;
                case 62:
                    int amount = 0;
                    switch(raidTeamSize)
                    {
                        case 5:
                            amount = 8; //correct
                            break;
                        case 4:
                            amount = 9;
                            break;
                        case 3:
                            amount = 12; //correct
                            break;
                        case 2:
                            amount = 16;
                            break;
                        case 1:
                            amount = 20;
                            break;
                    }
                    dataManager.increment(cDataPoint.XARP_HEALING, amount);
                    break;
                case 63:
                    dataManager.set(cDataPoint.XARP_SCREECH, Integer.parseInt(subData[4]));
                    break;
                case 64:
                    break;
                case 65:
                    dataManager.set(cDataPoint.XARP_TOTAL_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(cDataPoint.XARP_POST_SCREECH, dataManager.get(cDataPoint.XARP_TOTAL_TIME) - dataManager.get(cDataPoint.XARP_SCREECH));
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
            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex+1, globalData.size()));
        return true;
    }

    private boolean parseSotetseg()
    {
        int activeIndex = 0;
        loop: for(String s : globalData)
        {
            String[] subData = s.split(",", -1);
            switch(Integer.parseInt(subData[3]))
            {
                case 0:
                    raidStarted = new Date(Long.parseLong(subData[1]));
                    break;
                case 1:
                    for(int i = 4; i < 9; i++)
                    {
                        if(!subData[i].equals(""))
                        {
                            raidTeamSize++;
                            players.add(subData[i].replaceAll("[^\\p{ASCII}]", ""));
                        }
                    }
                    break;
                case 2:
                    if(dataManager.get(cDataPoint.SOTE_P1_SPLIT) == 0)
                    {
                        dataManager.increment(cDataPoint.SOTE_SPECS_P1);
                        dataManager.increment(cDataPoint.SOTE_SPECS_TOTAL);
                    }
                    else if(dataManager.get(cDataPoint.SOTE_P2_SPLIT) == 0)
                    {
                        dataManager.increment(cDataPoint.SOTE_SPECS_P2);
                        dataManager.increment(cDataPoint.SOTE_SPECS_TOTAL);
                    }
                    else
                    {
                        dataManager.increment(cDataPoint.SOTE_SPECS_P3);
                        dataManager.increment(cDataPoint.SOTE_SPECS_TOTAL);
                    }
                    break;
                case 3:
                case 6:
                    break;
                case 4:
                    if(dataManager.get(cDataPoint.SOTE_TOTAL_TIME) == 0)
                    {
                        soteReset = true;
                    }
                    else
                    {
                        if(!soteStarted)
                        {
                            nyloReset = true;
                        }
                        else
                        {
                            soteWipe = true;
                        }
                    }
                    globalData = new ArrayList(globalData.subList(activeIndex+1, globalData.size()));
                    return false;
                case 5:
                    dataManager.increment(cDataPoint.SOTE_DEATHS);
                    break;
                case 50:
                    break;
                case 51:
                    soteStarted = true;
                    if(partyComplete)
                    {
                        soteDefenseAccurate = true;
                    }
                    break;
                case 52:
                    dataManager.set(cDataPoint.SOTE_P1_SPLIT, Integer.parseInt(subData[4]));
                    break;
                case 53:
                    dataManager.set(cDataPoint.SOTE_M1_SPLIT, Integer.parseInt(subData[4]));
                    break;
                case 54:
                    dataManager.set(cDataPoint.SOTE_P2_SPLIT, Integer.parseInt(subData[4]));
                    break;
                case 55:
                    dataManager.set(cDataPoint.SOTE_M2_SPLIT, Integer.parseInt(subData[4]));
                    break;
                case 56:
                    break;
                case 57:
                    dataManager.set(cDataPoint.SOTE_TOTAL_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(cDataPoint.SOTE_P3_SPLIT, dataManager.get(cDataPoint.SOTE_TOTAL_TIME) - dataManager.get(cDataPoint.SOTE_M2_SPLIT));
                    break loop;
                case 58:
                    if(soteCyclesLost != -1)
                    {
                        soteCyclesLost += Integer.parseInt(subData[4]);
                    }
                    else
                    {
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

            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex+1, globalData.size()));
        return true;
    }

    private boolean parseNylo()
    {
        int activeIndex = 0;
        loop: for(String s : globalData)
        {
            String[] subData = s.split(",", -1);
            switch(Integer.parseInt(subData[3]))
            {
                case 0:
                    raidStarted = new Date(Long.parseLong(subData[1]));
                    break;
                case 1:
                    for(int i = 4; i < 9; i++)
                    {
                        if(!subData[i].equals(""))
                        {
                            raidTeamSize++;
                            players.add(subData[i].replaceAll("[^\\p{ASCII}]", ""));
                        }
                    }
                    break;
                case 2:
                case 44:
                case 6:
                    break;
                case 3:
                    if(dataManager.get(cDataPoint.NYLO_BOSS_SPAWN) != 0)
                    {
                        dataManager.bgs(cDataPoint.NYLO_DEFENSE, Integer.parseInt(subData[5]));
                    }
                    break;
                case 4:
                    if(dataManager.get(cDataPoint.NYLO_TOTAL_TIME) != 0)
                    {
                        nyloReset = true;
                    }
                    else
                    {
                        if(!nyloStarted)
                        {
                            bloatReset = true;
                        }
                        else
                        {
                            nyloWipe = true;
                        }
                    }
                    globalData = new ArrayList(globalData.subList(activeIndex+1, globalData.size()));
                    return false;
                case 5:
                    nyloDeaths++;
                    break;
                case 30:
                    nyloStarted = true;
                    break;
                case 31:
                    dataManager.increment(cDataPoint.NYLO_STALLS_TOTAL);
                    if(Integer.parseInt(subData[4]) > 19)
                    {
                        dataManager.increment(cDataPoint.NYLO_STALLS_POST_20);
                    }
                    else
                    {
                        dataManager.increment(cDataPoint.NYLO_STALLS_PRE_20);
                    }
                    break;
                case 32:
                    dataManager.increment(cDataPoint.NYLO_SPLITS_RANGE);
                    break;
                case 33:
                    dataManager.increment(cDataPoint.NYLO_SPLITS_MAGE);
                    break;
                case 34:
                    dataManager.increment(cDataPoint.NYLO_SPLITS_MELEE);
                    break;
                case 35:
                    dataManager.set(cDataPoint.NYLO_LAST_WAVE, Integer.parseInt(subData[4]));
                    break;
                case 36:
                    nyloLastDead = Integer.parseInt(subData[4]);
                    dataManager.set(cDataPoint.NYLO_CLEANUP, nyloLastDead - dataManager.get(cDataPoint.NYLO_LAST_WAVE));
                    break;
                case 40:
                    dataManager.set(cDataPoint.NYLO_BOSS_SPAWN, Integer.parseInt(subData[4])-2);
                    if(partyComplete)
                    {
                        nyloDefenseAccurate = true;
                    }
                    break;
                case 41:
                    dataManager.increment(cDataPoint.NYLO_ROTATIONS_MELEE);
                    break;
                case 42:
                    dataManager.increment(cDataPoint.NYLO_ROTATIONS_MAGE);
                    break;
                case 43:
                    dataManager.increment(cDataPoint.NYLO_ROTATIONS_RANGE);
                    break;
                case 45:
                    dataManager.set(cDataPoint.NYLO_TOTAL_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(cDataPoint.NYLO_BOSS_DURATION, dataManager.get(cDataPoint.NYLO_TOTAL_TIME)- dataManager.get(cDataPoint.NYLO_BOSS_SPAWN));
                    break loop;
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

            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex+1, globalData.size()));
        return true;
    }

    private boolean parseBloat()
    {
        int activeIndex = 0;
        bloatDefenseAccurate = maidenDefenseAccurate;
        loop: for(String s : globalData)
        {
            String[] subData = s.split(",", -1);
            switch(Integer.parseInt(subData[3]))
            {
                case 0:
                    raidStarted = new Date(Long.parseLong(subData[1]));
                    break;
                case 1:
                    for(int i = 4; i < 9; i++)
                    {
                        if(!subData[i].equals(""))
                        {
                            raidTeamSize++;
                            players.add(subData[i].replaceAll("[^\\p{ASCII}]", ""));
                        }
                    }
                    break;
                case 2:
                case 6:
                    break;
                case 24:
                    bloatHPAtDown = Integer.parseInt(subData[4]);
                    break;
                case 25:
                    if(dataManager.get(cDataPoint.BLOAT_DOWNS) == 0)
                    {
                        bloatScytheBeforeFirstDown++;
                    }
                    break;
                case 3:
                    if(dataManager.get(cDataPoint.BLOAT_DOWNS) == 0)
                    {
                        dataManager.bgs(cDataPoint.BLOAT_DEFENSE, 2*Integer.parseInt(subData[5]));
                    }
                    break;
                case 4:
                    if(dataManager.get(cDataPoint.BLOAT_TOTAL_TIME) != 0)
                    {
                        bloatReset = true;
                    }
                    else
                    {
                        if(!bloatStarted)
                        {
                            maidenReset = true;
                        }
                        else
                        {
                            bloatWipe = true;
                        }
                    }
                    globalData = new ArrayList(globalData.subList(activeIndex+1, globalData.size()));
                    return false;
                case 5:
                    dataManager.increment(cDataPoint.BLOAT_DEATHS);
                    if(dataManager.get(cDataPoint.BLOAT_DOWNS) == 0)
                    {
                        dataManager.increment(cDataPoint.BLOAT_FIRST_WALK_DEATHS);
                    }
                    break;
                case 20:
                    bloatStarted = true;
                    if(partyComplete)
                    {
                        bloatDefenseAccurate = true;
                    }
                    break;
                case 21:
                    if(dataManager.get(cDataPoint.BLOAT_DOWNS) == 0)
                    {
                        dataManager.set(cDataPoint.BLOAT_FIRST_DOWN_TIME, Integer.parseInt(subData[4]));
                    }
                    dataManager.increment(cDataPoint.BLOAT_DOWNS);
                    break;
                case 23:
                    dataManager.set(cDataPoint.BLOAT_TOTAL_TIME, Integer.parseInt(subData[4]));
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
            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex+1, globalData.size()));
        return true;
    }

    private boolean parseMaiden() throws Exception
    {
        int activeIndex = 0;
        String lastProc = " 70s";
        loop: for(String s : globalData)
        {
            String[] subData = s.split(",", -1);
            switch(Integer.parseInt(subData[3]))
            {
                case 0:
                    raidStarted = new Date(Long.parseLong(subData[1]));
                    break;
                case 1:
                    for(int i = 4; i < 9; i++)
                    {
                        if(!subData[i].equals(""))
                        {
                            raidTeamSize++;
                            players.add(subData[i].replaceAll("[^\\p{ASCII}]", ""));
                        }
                    }
                    break;
                case 2:
                    dataManager.hammer(cDataPoint.MAIDEN_DEFENSE);
                    break;
                case 3:
                    dataManager.bgs(cDataPoint.MAIDEN_DEFENSE, Integer.parseInt(subData[5]));
                    break;
                case 4:
                    if(dataManager.get(cDataPoint.MAIDEN_TOTAL_TIME) != 0)
                    {
                        maidenReset = true;
                    }
                    else
                    {
                        if(maidenSpawned)
                        {
                            resetBeforeMaiden = true;
                        }
                        else
                        {
                            maidenWipe = true;
                        }
                    }
                    globalData = new ArrayList(globalData.subList(activeIndex+1, globalData.size()));
                    return false;
                case 5:
                    dataManager.increment(cDataPoint.MAIDEN_DEATHS);
                    break;
                case 6: break;
                case 10:
                    dataManager.increment(cDataPoint.MAIDEN_BLOOD_SPAWNED);
                    break;
                case 11:
                    if(dataManager.get(cDataPoint.MAIDEN_TOTAL_TIME) == 0) //TODO: see case 16 fix
                    {
                        dataManager.increment(cDataPoint.MAIDEN_CRABS_LEAKED);
                        int crabHP = -1;
                        try
                        {
                            crabHP = Integer.parseInt(subData[5]);
                        }
                        catch(Exception e )
                        {
                            e.printStackTrace();
                        }
                        maidenHeal += (crabHP*2);
                        int maxCrabHP = 100;
                        switch(players.size())
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
                        if(crabHP == maxCrabHP)
                        {
                            getMaidenCrabsLeakedFullHP++;
                        }

                        if (subData[4].contains("30"))
                        {
                            maidenSkip = false;
                        }
                    }
                case 12:
                    maidenSpawned = true;
                    if(partyComplete)
                    {
                        maidenDefenseAccurate = true;
                    }
                    break;
                case 13:
                    dataManager.set(cDataPoint.MAIDEN_70_SPLIT, Integer.parseInt(subData[4]));
                    lastProc = " 70s";
                    break;
                case 14:
                    dataManager.set(cDataPoint.MAIDEN_50_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(cDataPoint.MAIDEN_7050_SPLIT, Integer.parseInt(subData[4]) - dataManager.get(cDataPoint.MAIDEN_70_SPLIT));
                    lastProc = " 50s";
                    break;
                case 15:
                    dataManager.set(cDataPoint.MAIDEN_30_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(cDataPoint.MAIDEN_5030_SPLIT, Integer.parseInt(subData[4]) - dataManager.get(cDataPoint.MAIDEN_50_SPLIT));
                    lastProc = " 30s";
                    break;
                case 16:
                    dataManager.set(cDataPoint.MAIDEN_TOTAL_TIME, Integer.parseInt(subData[4])+7);
                    dataManager.set(cDataPoint.MAIDEN_SKIP_SPLIT, dataManager.get(cDataPoint.MAIDEN_TOTAL_TIME) - dataManager.get(cDataPoint.MAIDEN_30_SPLIT));
                    if (globalData.get(activeIndex+1).split(",", -1)[3].equals("4"))
                        maidenReset = true;
                    break loop;
                case 17:
                    dataManager.set(cDataPoint.MAIDEN_TOTAL_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(cDataPoint.MAIDEN_SKIP_SPLIT, dataManager.get(cDataPoint.MAIDEN_TOTAL_TIME) - dataManager.get(cDataPoint.MAIDEN_30_SPLIT));
                    if (globalData.get(activeIndex+1).split(",", -1)[3].equals("4"))
                        maidenReset = true;
                    break loop;
                case 19:
                    if(!maidenScuffed)
                    {
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
                case 201:
                    maidenStartAccurate = true;
                    break;
                case 301:
                    maidenEndAccurate = true;
                    maidenTimeAccurate = maidenStartAccurate;
                    break;
                default:
                   // throw new cDataOutOfOrderException("Key: " + subData[3] + " not expected during Maiden");
            }
            activeIndex++;
        }
        globalData = new ArrayList(globalData.subList(activeIndex+1, globalData.size()));
        return true;
    }

    private void finishRaid()
    {
        raidCompleted = true;
    }
}

