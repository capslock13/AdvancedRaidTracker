package com.cTimers;

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
    public int maidenBloodsSpawned;
    public int maidenCrabsLeaked;

    public int getMaidenCrabsLeakedFullHP = 0;
    public int maidenHeal = 0;
    public int maidenDefense = 200;
    public boolean maidenDefenseAccurate;
    public int maidenDeaths;

    public boolean maidenScuffed = false;
    public String firstMaidenCrabScuffed = "";

    public boolean maidenSpawned;
    public boolean maidenSkip;
    public boolean maidenReset;
    public boolean maidenWipe;

    public int maiden70Split = 0;
    public int maiden50Split;
    public int maiden30Split;
    public int maidenTime;

    public boolean bloatTimeAccurate;
    public int bloatDowns;
    public int bloatFirstWalkDeaths;
    public int bloatfirstWalkDefense = 100;
    public boolean bloatDefenseAccurate;
    public int bloatDeaths;
    public int bloatHPAtDown;
    public int bloatScytheBeforeFirstDown;

    public boolean bloatStarted;
    public boolean bloatReset;
    public boolean bloatWipe;

    public int bloatFirstDownSplit;
    public int bloatTime;

    public boolean nyloTimeAccurate;
    public int nyloStallsPre20;
    public int nyloStallsPost20;
    public int nyloStallsTotal;
    public int nyloRangeSplits;
    public int nyloMageSplits;
    public int nyloMeleeSplits;
    public int nyloRangeRotations;
    public int nyloMeleeRotations;
    public int nyloMageRotations;
    public int nyloDefenseReduction = 0;
    public boolean nyloDefenseAccurate;
    public int nyloDeaths;

    public int nyloLastDead;

    public boolean nyloWipe;
    public boolean nyloReset;
    public boolean nyloStarted;

    public int nyloLastWave;
    public int nyloBossSpawn;
    public int nyloTime;

    public boolean soteTimeAccurate;
    public int soteSpecsP1;
    public int soteSpecsP2;
    public int soteSpecsP3;
    public int soteSpecsTotal;
    public boolean soteDefenseAccurate;
    public int soteDeaths;
    public int soteCyclesLost = -1;

    public boolean soteStarted;
    public boolean soteWipe;
    public boolean soteReset;

    public int soteFirstMazeStartSplit;
    public int soteFirstMazeEndSplit;
    public int soteSecondMazeStartSplit;
    public int soteSecondMazeEndSplit;
    public int soteTime;

    public boolean xarpTimeAccurate;
    public int xarpDefense = 250;
    public boolean xarpDefenseAccurate;
    public int xarpDeaths;
    public int xarpHealing;

    public boolean xarpWipe;
    public boolean xarpReset;
    public boolean xarpStarted;

    public int xarpScreechSplit;
    public int xarpTime;

    public int verzikBounces;
    public int verzikDeaths;

    public boolean verzikWipe;
    public boolean verzikStarted;

    public boolean verzikTimeAccurate;
    public int verzikP1Split;
    public int verzikP2Split;
    public int verzikTime;
    public int verzikCrabsSpawned;
    public int verzikDawnDamage;
    public int verzikDawnCount;
    public int verzikRedsProc;

    public int raidTeamSize;

    public boolean raidCompleted;

    public Date raidStarted;

    private ArrayList<String> globalData;
    public ArrayList<String> players;

    /**
     * Generates simulated room data for testing purposes
     * @param v Controls maiden time
     */
    public cRoomData(int v)
    {
        resetBeforeMaiden = false;
        maidenBloodsSpawned = 3;
        maidenCrabsLeaked = 1;
        maidenDefense = 0;

        maidenSpawned = true;
        maidenSkip = true;
        maidenReset = false;
        maidenWipe = false;

        maiden70Split = 27;
        maiden50Split = 46;
        maiden30Split = 83;
        maidenTime = 125;
        if(v == 2)
        {
            maidenTime = 145;
        }
        if(v == 3)
        {
            maidenTime = 153;
        }

        bloatDowns = 1;
        bloatFirstWalkDeaths = 0;
        bloatfirstWalkDefense = 0;
        bloatScytheBeforeFirstDown = 0;

        bloatStarted = true;
        bloatReset = false;
        bloatWipe = false;

        bloatFirstDownSplit = 39;
        bloatTime = 70;
        bloatHPAtDown = 745;

        nyloStallsPre20 = 0;
        nyloStallsPost20 = 0;
        nyloStallsTotal = 0;
        nyloRangeSplits = 20;
        nyloMeleeSplits = 30;
        nyloMageSplits = 25;
        nyloRangeRotations = 2;
        nyloMeleeRotations = 3;
        nyloMageRotations = 0;
        nyloDefenseReduction = 50;
        nyloDeaths = 0;

        nyloWipe = false;
        nyloReset = false;
        nyloStarted = true;

        nyloLastWave = 236;
        nyloLastDead = 262;
        nyloBossSpawn = 280;
        nyloTime = 344;

        soteSpecsP1 = 2;
        soteSpecsP2 = 2;
        soteSpecsP3 = 2;
        soteSpecsTotal = 6;
        soteDeaths = 0;

        soteStarted = true;
        soteWipe = false;
        soteReset = false;

        soteFirstMazeStartSplit = 37;
        soteFirstMazeEndSplit = 62;
        soteSecondMazeStartSplit = 99;
        soteSecondMazeEndSplit = 124;
        soteTime = 204;

        xarpDefense = 0;
        xarpDeaths = 0;
        xarpHealing = 16;

        xarpWipe = false;
        xarpReset = false;
        xarpStarted = true;
        xarpScreechSplit = 164;
        xarpTime = 215;

        verzikBounces = 0;
        verzikDeaths = 0;
        verzikWipe = false;
        verzikStarted = true;

        verzikP1Split = 53;
        verzikP2Split = 210;
        verzikTime = 326;

        raidTeamSize = 5;
        raidCompleted = true;
        raidStarted = new Date();

        players = new ArrayList<>();
        players.add("Caps lock13");
        players.add("2008Player44");
        players.add("Rostikz");
        players.add("TensorsFlow");
        players.add("LaupieLaupie");
    }

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
    public int getOverallTime()
    {
        return maidenTime+bloatTime+nyloTime+soteTime+xarpTime+verzikTime;
    }

    public int getMaidenTime() { return maidenTime; }
    public int getBloatTime() { return bloatTime; }
    public int getNyloTime() { return nyloTime; }
    public int getSoteTime() { return soteTime; }
    public int getXarpTime() { return xarpTime; }
    public int getVerzikTime() { return verzikTime; }

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

    public cRoomData(String[] parameters) throws Exception
    {
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
                    if(verzikTime == 0)
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
                    verzikDeaths++;
                case 6:
                    break;
                case 70:
                    break;
                case 71:
                    verzikStarted = true;
                case 72:
                    break;
                case 73:
                    verzikP1Split = Integer.parseInt(subData[4]);
                    break;
                case 74:
                    verzikP2Split = Integer.parseInt(subData[4]);
                    break;
                case 75:
                    break;
                case 76:
                    verzikTime = Integer.parseInt(subData[4]);
                    break;
                case 77:
                    verzikBounces++;
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
                    xarpDefense = (int) (xarpDefense*.7);
                    break;
                case 3:
                    xarpDefense -= Integer.parseInt(subData[5]);
                    if(xarpDefense < 0)
                    {
                        xarpDefense = 0;
                    }
                    break;
                case 4:
                    if(xarpTime != 0)
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
                    xarpDeaths++;
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
                    xarpHealing += amount;
                    break;
                case 63:
                    xarpScreechSplit = Integer.parseInt(subData[4]);
                    break;
                case 64:
                    break;
                case 65:
                    xarpTime = Integer.parseInt(subData[4]);
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
                    if(soteFirstMazeStartSplit == 0)
                    {
                        soteSpecsP1++;
                        soteSpecsTotal++;
                    }
                    else if(soteSecondMazeStartSplit == 0)
                    {
                        soteSpecsP2++;
                        soteSpecsTotal++;
                    }
                    else
                    {
                        soteSpecsP3++;
                        soteSpecsTotal++;
                    }
                    break;
                case 3:
                case 6:
                    break;
                case 4:
                    if(soteTime != 0)
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
                    soteDeaths++;
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
                    soteFirstMazeStartSplit = Integer.parseInt(subData[4]);
                    break;
                case 53:
                    soteFirstMazeEndSplit = Integer.parseInt(subData[4]);
                    break;
                case 54:
                    soteSecondMazeStartSplit = Integer.parseInt(subData[4]);
                    break;
                case 55:
                    soteSecondMazeEndSplit = Integer.parseInt(subData[4]);
                    break;
                case 56:
                    break;
                case 57:
                    soteTime = Integer.parseInt(subData[4]);
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
                    if(nyloBossSpawn != 0)
                    {
                        nyloDefenseReduction += Integer.parseInt(subData[5]);
                        if(nyloDefenseReduction > 50)
                        {
                            nyloDefenseReduction = 50;
                        }
                        break;
                    }
                case 4:
                    if(nyloTime != 0)
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
                    nyloStallsTotal++;
                    if(Integer.parseInt(subData[4]) > 19)
                    {
                        nyloStallsPost20++;
                    }
                    else
                    {
                        nyloStallsPre20++;
                    }
                    break;
                case 32:
                    nyloRangeSplits++;
                    break;
                case 33:
                    nyloMageSplits++;
                    break;
                case 34:
                    nyloMeleeSplits++;
                    break;
                case 35:
                    nyloLastWave = Integer.parseInt(subData[4]);
                    break;
                case 36:
                    nyloLastDead = Integer.parseInt(subData[4]);
                    break;
                case 40:
                    nyloBossSpawn = Integer.parseInt(subData[4])-2;
                    if(partyComplete)
                    {
                        nyloDefenseAccurate = true;
                    }
                    break;
                case 41:
                    nyloMeleeRotations++;
                    break;
                case 42:
                    nyloMageRotations++;
                    break;
                case 43:
                    nyloRangeRotations++;
                    break;
                case 45:
                    nyloTime = Integer.parseInt(subData[4]);
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
                    if(bloatDowns == 0)
                    {
                        bloatScytheBeforeFirstDown++;
                    }
                    break;
                case 3:
                    if(bloatDowns == 0)
                    {
                        bloatfirstWalkDefense -= (2*Integer.parseInt(subData[5]));
                        if(bloatfirstWalkDefense < 0)
                        {
                            bloatfirstWalkDefense = 0;
                        }
                    }
                    break;
                case 4:
                    if(bloatTime != 0)
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
                    bloatDeaths++;
                    if(bloatDowns == 0)
                    {
                        bloatFirstWalkDeaths++;
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
                    if(bloatDowns == 0)
                    {
                        bloatFirstDownSplit = Integer.parseInt(subData[4]);
                    }
                    bloatDowns++;
                    break;
                case 23:
                    bloatTime = Integer.parseInt(subData[4]);
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
                    maidenDefense = (int)(maidenDefense*0.7);
                    break;
                case 3:
                    maidenDefense -= Integer.parseInt(subData[5]);
                    if(maidenDefense < 0)
                    {
                        maidenDefense = 0;
                    }
                    break;
                case 4:
                    if(maidenTime != 0)
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
                    maidenDeaths++;
                    break;
                case 6: break;
                case 10:
                    maidenBloodsSpawned++;
                    break;
                case 11:
                    if(maidenTime == 0) //TODO: see case 16 fix
                    {
                        maidenCrabsLeaked++;
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
                    maiden70Split = Integer.parseInt(subData[4]);
                    lastProc = " 70s";
                    break;
                case 14:
                    maiden50Split = Integer.parseInt(subData[4]);
                    lastProc = " 50s";
                    break;
                case 15:
                    maiden30Split = Integer.parseInt(subData[4]);
                    lastProc = " 30s";
                    break;
                case 16:
                    maidenTime = Integer.parseInt(subData[4])+7;
                    if (globalData.get(activeIndex+1).split(",", -1)[3].equals("4"))
                        maidenReset = true;
                    break loop;
                case 17:
                    maidenTime = Integer.parseInt(subData[4]);
                    if (globalData.get(activeIndex+1).split(",", -1)[3].equals("4"))
                        maidenReset = true;
                    break loop;
                case 19:
                    if(!maidenScuffed)
                    {
                        firstMaidenCrabScuffed = Integer.parseInt(subData[4]) + lastProc;
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

