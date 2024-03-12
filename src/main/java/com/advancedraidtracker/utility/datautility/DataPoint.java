package com.advancedraidtracker.utility.datautility;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.RaidType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.advancedraidtracker.constants.RaidRoom.*;

public enum DataPoint
{
    CHALLENGE_TIME("Challenge Time", types.TIME, ANY),
    OVERALL_TIME("Overall Time", types.TIME, ANY),
    TIME_OUTSIDE_ROOMS("Time Outside Rooms", types.TIME, ANY_TOB),
    MAIDEN_BLOOD_SPAWNED("Maiden blood spawned", types.OTHER_INT, MAIDEN),
    MAIDEN_BLOOD_THROWN("Maiden blood thrown", types.OTHER_INT, MAIDEN),
    MAIDEN_PLAYER_STOOD_IN_THROWN_BLOOD("Maiden player stood in thrown blood", types.OTHER_INT, MAIDEN, true),
    MAIDEN_PLAYER_STOOD_IN_SPAWNED_BLOOD("Maiden player stood in spawned blood", types.OTHER_INT, MAIDEN, true),
    MAIDEN_HEALS_FROM_THROWN_BLOOD("Maiden heals from thrown blood", types.OTHER_INT, MAIDEN, true),
    MAIDEN_HEALS_FROM_SPAWNED_BLOOD("Maiden heals from spawned blood", types.OTHER_INT, MAIDEN, true),
    MAIDEN_PLAYER_STOOD_IN_BLOOD("Maiden player stood in any blood", types.OTHER_INT, MAIDEN, true),
    MAIDEN_HEALS_FROM_ANY_BLOOD("Maiden heals from any blood", types.OTHER_INT, MAIDEN, true),
    MAIDEN_MELEE_DRAINS("Maiden drained melee", types.OTHER_INT, MAIDEN, true),
    MAIDEN_CRABS_LEAKED("Maiden crabs leaked", types.OTHER_INT, MAIDEN),
    MAIDEN_CRABS_LEAKED_FULL_HP("Maiden crabs leaked full", types.OTHER_INT, MAIDEN),
    MAIDEN_HP_HEALED("Maiden HP healed", types.OTHER_INT, MAIDEN),
    MAIDEN_DINHS_SPECS("Maiden dinhs specs", types.OTHER_INT, MAIDEN, true),
    MAIDEN_DINHS_CRABS_HIT("Maiden dinhs crabs Hit", types.OTHER_INT, MAIDEN, true),
    MAIDEN_DINHS_TARGETS_HIT("Maiden dinhs targets hit", types.OTHER_INT, MAIDEN, true),
    MAIDEN_DINHS_AVERAGE_HP_HIT("Maiden dinhs average HP crab", types.OTHER_INT, MAIDEN, true),
    MAIDEN_DINHS_PERCENT_TARGETS_CRAB("Maiden dinhs % crabs targeted", types.OTHER_INT, MAIDEN, true),
    MAIDEN_DINHS_CRABS_UNDER_27_TARGETED("Maiden dinhs crab < 27hp targeted", types.OTHER_INT, MAIDEN, true),
    MAIDEN_DINHS_CRABS_UNDER_27_TARGETED_PERCENT("Maiden dinhs crabs <27hp targeted %", types.OTHER_INT, MAIDEN, true),
    MAIDEN_CHINS_THROWN("Maiden chins thrown", types.OTHER_INT, MAIDEN, true),
    MAIDEN_CHINS_THROWN_WRONG_DISTANCE("Maiden chins thrown wrong distance", types.OTHER_INT, MAIDEN, true),
    MAIDEN_CHIN_CORRECT_DISTANCE_PERCENT("Maiden chins correct distance %", types.OTHER_INT, MAIDEN, true),
    RAID_INDEX("Raid Index", types.OTHER_INT, ANY),
    PARTY_SIZE("Party Size", types.OTHER_INT, ANY_TOB),

    BLOAT_DOWNS("Bloat downs", types.OTHER_INT, BLOAT),
    BLOAT_HP_FIRST_DOWN("Bloat HP% 1st down", types.OTHER_INT, BLOAT),
    NYLO_STALLS_PRE_20("Nylo stalls pre 20", types.OTHER_INT, NYLOCAS),
    NYLO_STALLS_POST_20("Nylo stalls post 20", types.OTHER_INT, NYLOCAS),
    NYLO_STALLS_TOTAL("Nylo stalls total", types.OTHER_INT, NYLOCAS),
    NYLO_SPLITS_RANGE("Nylo splits range", types.OTHER_INT, NYLOCAS),
    NYLO_SPLITS_MAGE("Nylo splits mage", types.OTHER_INT, NYLOCAS),
    NYLO_SPLITS_MELEE("Nylo splits melee", types.OTHER_INT, NYLOCAS),
    NYLO_ROTATIONS_RANGE("Nylo rotations range", types.OTHER_INT, NYLOCAS),
    NYLO_ROTATIONS_MAGE("Nylo rotations mage", types.OTHER_INT, NYLOCAS),
    NYLO_ROTATIONS_MELEE("Nylo rotations melee", types.OTHER_INT, NYLOCAS),
    NYLO_ROTATIONS_TOTAL("Nylo total rotations", types.OTHER_INT, NYLOCAS),
    NYLO_DEFENSE("Nylo defense", types.OTHER_INT, NYLOCAS, 50),
    NYLO_DEATHS("Nylo deaths", types.OTHER_INT, NYLOCAS, true),
    SOTE_SPECS_P1("Sote specs p1", types.OTHER_INT, SOTETSEG),
    SOTE_SPECS_P2("Sote specs p2", types.OTHER_INT, SOTETSEG),
    SOTE_SPECS_P3("Sote specs p3", types.OTHER_INT, SOTETSEG),
    SOTE_SPECS_TOTAL("Sote specs total", types.OTHER_INT, SOTETSEG),
    XARP_HEALING("Xarp Healing", types.OTHER_INT, XARPUS),
    VERZIK_BOUNCES("Verzik bounces", types.OTHER_INT, VERZIK, true),
    VERZIK_CRABS_SPAWNED("Verzik crabs spawned", types.OTHER_INT, VERZIK),
    VERZIK_P2_CRABS_SPAWNED("Verzik P2 crabs spawned", types.OTHER_INT, VERZIK),
    VERZIK_P3_CRABS_SPAWNED("Verzik P3 crabs spawned", types.OTHER_INT, VERZIK),
    VERZIK_REDS_SETS("Verzik Red Sets", types.OTHER_INT, VERZIK),
    VERZIK_REDS_PROC_PERCENT("Verzik Red Proc Percent", types.OTHER_INT, VERZIK),
    VERZIK_HP_AT_WEBS("Verzik HP% at webs", types.OTHER_INT, VERZIK),
    MAIDEN_70_SPLIT("Maiden 70s split", types.TIME_SPLIT, MAIDEN),
    MAIDEN_7050_DURATION("Maiden 70-50s split", types.TIME_DURATION, MAIDEN),
    MAIDEN_50_SPLIT("Maiden 50s split", types.TIME_SPLIT, MAIDEN),
    MAIDEN_5030_SPLIT("Maiden 50-30s split", types.TIME_DURATION, MAIDEN),
    MAIDEN_30_SPLIT("Maiden 30s split", types.TIME, MAIDEN),
    MAIDEN_SKIP_SPLIT("Maiden Skip split", types.TIME_SPLIT, MAIDEN),
    BLOAT_FIRST_DOWN_TIME("Bloat 1st down time", types.OTHER_INT, BLOAT),
    NYLO_BOSS_SPAWN("Nylo boss spawn", types.TIME, NYLOCAS),
    NYLO_BOSS_DURATION("Nylo boss duration", types.TIME, NYLOCAS),
    NYLO_LAST_WAVE("Nylo last wave", types.TIME, NYLOCAS),
    NYLO_LAST_DEAD("Nylo last dead", types.TIME, NYLOCAS),
    NYLO_CLEANUP("Nylo cleanup", types.TIME, NYLOCAS),
    NYLOCAS_PILLAR_DESPAWN_TICK("Nylo Pillar despawn tick", types.TIME, NYLOCAS),
    SOTE_P1_SPLIT("Sote P1 split", types.TIME, SOTETSEG),
    SOTE_P2_SPLIT("Sote P2 split", types.TIME, SOTETSEG),
    SOTE_P2_DURATION("Sote P2 duration", types.TIME, SOTETSEG),

    SOTE_P3_DURATION("Sote P3 duration", types.TIME, SOTETSEG),
    SOTE_M1_SPLIT("Sote maze1 split", types.TIME, SOTETSEG),
    SOTE_M1_DURATION("Sote maze1 Duration", types.TIME, SOTETSEG),
    SOTE_M2_SPLIT("Sote maze2 split", types.TIME, SOTETSEG),
    SOTE_M2_DURATION("Sote maze2 duration", types.TIME, SOTETSEG),
    SOTE_P3_SPLIT("Sote P3 split", types.TIME, SOTETSEG),
    SOTE_MAZE_SUM("Sote mazes combined", types.TIME, SOTETSEG),
    XARP_SCREECH("Xarp screech", types.TIME, XARPUS),
    XARP_POST_SCREECH("Xarp post screech", types.TIME, XARPUS),
    VERZIK_P2_SPLIT("Verzik P1 Time", types.TIME, VERZIK),

    VERZIK_P2_TILL_REDS("Verzik P2 until reds split", types.TIME, VERZIK),
    VERZIK_REDS_SPLIT("Verzik reds split", types.TIME, VERZIK),
    VERZIK_REDS_DURATION("Verzik reds duration", types.TIME, VERZIK),
    VERZIK_P3_SPLIT("Verzik P2 split", types.TIME, VERZIK),
    VERZIK_P2_DURATION("Verzik P2 Time", types.TIME, VERZIK),
    VERZIK_P3_DURATION("Verzik P3 Time", types.TIME, VERZIK),

    NYLO_ENTRY("Nylo Entry", types.TIME, NYLOCAS),
    SOTE_ENTRY("Sote Entry", types.TIME, SOTETSEG),
    XARP_ENTRY("Xarp Entry", types.TIME, XARPUS),
    VERZIK_ENTRY("Verzik Entry", types.TIME, VERZIK),


    ATTEMPTED_BGS_MAIDEN("Maiden attempted BGS", types.OTHER_INT, MAIDEN, true),
    BGS_DAMAGE_MAIDEN("Maiden BGS damage", types.OTHER_INT, MAIDEN, true),

    ATTEMPTED_BGS_BLOAT("Bloat attempted BGS", types.OTHER_INT, BLOAT, true),
    BGS_DAMAGE_BLOAT("Bloat BGS damage", types.OTHER_INT, BLOAT, true),

    ATTEMPTED_BGS_NYLO("Nylo attempted BGS", types.OTHER_INT, NYLOCAS, true),
    BGS_DAMAGE_NYLO("Nylo BGS damage", types.OTHER_INT, NYLOCAS, true),

    ATTEMPTED_BGS_SOTE("Sote attempted BGS", types.OTHER_INT, SOTETSEG, true),
    BGS_DAMAGE_SOTE("Sote BGS damage", types.OTHER_INT, SOTETSEG, true),

    ATTEMPTED_BGS_XARP("Xarp attempted BGS", types.OTHER_INT, XARPUS, true),
    BGS_DAMAGE_XARP("Xarp BGS damage", types.OTHER_INT, XARPUS, true),

    ATTEMPTED_BGS_VERZ("Verzik attempted BGS", types.OTHER_INT, VERZIK, true),
    BGS_DAMAGE_VERZ("Verzik BGS damage", types.OTHER_INT, VERZIK, true),

    THRALL_ATTACKS_TOTAL("Total thrall attacks", types.OTHER_INT, ANY_TOB, true),
    THRALL_ATTACKS_MAIDEN("Maiden thrall attacks", types.OTHER_INT, MAIDEN, true),
    THRALL_ATTACKS_BLOAT("Bloat thrall attacks", types.OTHER_INT, BLOAT, true),
    THRALL_ATTACKS_NYLO("Nylo thrall attacks", types.OTHER_INT, NYLOCAS, true),
    THRALL_ATTACKS_SOTE("Sote thrall attacks", types.OTHER_INT, SOTETSEG, true),
    THRALL_ATTACKS_XARP("Xarp thrall attacks", types.OTHER_INT, XARPUS, true),
    THRALL_ATTACKS_VERZIK("Verzik thrall attacks", types.OTHER_INT, VERZIK, true),

    THRALL_DAMAGE_TOTAL("Total thrall damage", types.OTHER_INT, ANY_TOB, true),
    THRALL_DAMAGE_MAIDEN("Maiden thrall damage", types.OTHER_INT, MAIDEN, true),
    THRALL_DAMAGE_BLOAT("Bloat thrall damage", types.OTHER_INT, BLOAT, true),
    THRALL_DAMAGE_NYLO("Nylo thrall damage", types.OTHER_INT, NYLOCAS, true),
    THRALL_DAMAGE_SOTE("Sote thrall damage", types.OTHER_INT, SOTETSEG, true),
    THRALL_DAMAGE_XARP("Xarp thrall damage", types.OTHER_INT, XARPUS, true),
    THRALL_DAMAGE_VERZIK("Verzik thrall damage", types.OTHER_INT, VERZIK, true),

    VENG_DAMAGE_TOTAL("Total veng damage", types.OTHER_INT, ANY_TOB),
    VENG_DAMAGE_MAIDEN("Maiden veng damage", types.OTHER_INT, MAIDEN),
    VENG_DAMAGE_BLOAT("Bloat veng damage", types.OTHER_INT, BLOAT),
    VENG_DAMAGE_NYLO("Nylo veng damage", types.OTHER_INT, NYLOCAS),
    VENG_DAMAGE_SOTE("Sote veng damage", types.OTHER_INT, SOTETSEG),
    VENG_DAMAGE_XARP("Xarp veng damage", types.OTHER_INT, XARPUS),
    VENG_DAMAGE_VERZIK("Verzik veng damage", types.OTHER_INT, VERZIK),

    VENG_CASTS_TOTAL("Total veng casts", types.OTHER_INT, ANY_TOB),
    VENG_CASTS_MAIDEN("Maiden veng casts", types.OTHER_INT, MAIDEN),
    VENG_CASTS_BLOAT("Bloat veng casts", types.OTHER_INT, BLOAT),
    VENG_CASTS_NYLO("Nylo veng casts", types.OTHER_INT, NYLOCAS),
    VENG_CASTS_SOTE("Sote veng casts", types.OTHER_INT, SOTETSEG),
    VENG_CASTS_XARP("Xarp veng casts", types.OTHER_INT, XARPUS),
    VENG_CASTS_VERZIK("Verzik veng casts", types.OTHER_INT, VERZIK),

    VENG_PROCS_TOTAL("Total veng procs", types.OTHER_INT, ANY_TOB),
    VENG_PROCS_MAIDEN("Maiden veng procs", types.OTHER_INT, MAIDEN),
    VENG_PROCS_BLOAT("Bloat veng procs", types.OTHER_INT, BLOAT),
    VENG_PROCS_NYLO("Nylo veng procs", types.OTHER_INT, NYLOCAS),
    VENG_PROCS_SOTE("Sote veng procs", types.OTHER_INT, SOTETSEG),
    VENG_PROCS_XARP("Xarp veng procs", types.OTHER_INT, XARPUS),
    VENG_PROCS_VERZIK("Verzik veng procs", types.OTHER_INT, VERZIK),
    KODAI_BOPS("Kodai bops", types.OTHER_INT, ANY_TOB, true),
    DWH_BOPS("DWH bops", types.OTHER_INT, ANY_TOB, true),
    CHALLY_POKE("Chally pokes", types.OTHER_INT, ANY_TOB, true),
    BGS_WHACKS("BGS whacks", types.OTHER_INT, ANY_TOB, true),
    TOTAL_DEATHS("Total deaths", types.OTHER_INT, ANY_TOB, true),
    DEATHS("Alternate Deaths", types.OTHER_INT, ALL, true),
    UNKNOWN("Unknown", types.OTHER_BOOL, ANY_TOB),

    TOA_PARTY_SIZE("TOA Party Size", types.OTHER_INT, ANY_TOA),
    TOA_RAID_INDEX("TOA Raid Index", types.OTHER_INT, ANY_TOA),
    TOA_INVOCATION_LEVEL("TOA Invocation Level", types.OTHER_INT, ANY_TOA),

    CRONDIS_TIME("Crondis Time", types.TIME, CRONDIS),
    ZEBAK_TIME("Zebak Time", types.TIME, ZEBAK),
    ZEBAK_ENRAGED_SPLIT("Zebak Enraged Split", types.TIME, ZEBAK),
    ZEBAK_ENRAGED_DURATION("Zebak Enraged Duration", types.TIME, ZEBAK),

    SCABARAS_TIME("Scabaras Time", types.TIME, SCABARAS),

    KEPHRI_TIME("Kephri Time", types.TIME, KEPHRI),
    KEPHRI_P1_DURATION("Kephri P1 Duration", types.TIME, KEPHRI),
    KEPHRI_SWARM1_DURATION("Kephri Swarm1 Duration", types.TIME, KEPHRI),
    KEPHRI_P2_SPLIT("Kephri P2 Split", types.TIME, KEPHRI),
    KEPHRI_P2_DURATION("Kephri P2 Duration", types.TIME, KEPHRI),
    KEPHRI_SWARM2_SPLIT("Kephri Swarm2 Split", types.TIME, KEPHRI),
    KEPHRI_SWARM2_DURATION("Kephri Swarm2 Duration", types.TIME, KEPHRI),
    KEPHRI_P3_SPLIT("Kephri P3 Split", types.TIME, KEPHRI),
    KEPHRI_P3_DURATION("Kephri P3 Duration", types.TIME, KEPHRI),

    APMEKEN_TIME("Apmeken Time", types.TIME, APMEKEN),

    BABA_TIME("Baba Time", types.TIME, BABA),
    BABA_P1_DURATION("Baba P1 Duration", types.TIME, BABA),
    BABA_BOULDER_1_DURATION("Baba Boulder1 Duration", types.TIME, BABA),
    BABA_P2_SPLIT("Baba P2 Split", types.TIME, BABA),
    BABA_P2_DURATION("Baba P2 Duration", types.TIME, BABA),
    BABA_BOULDER_2_SPLIT("Baba Boulder2 Split", types.TIME, BABA),
    BABA_BOULDER_2_DURATION("Baba Boulder2 Duration", types.TIME, BABA),
    BABA_P3_SPLIT("Baba P3 Split", types.TIME, BABA),
    BABA_P3_DURATION("Baba P3 Duration", types.TIME, BABA),

    HET_TIME("Het Time", types.TIME, HET),

    AKKHA_TIME("Akkha Time", types.TIME, AKKHA),

    AKKHA_P1_DURATION("Akkha P1 Duration", types.TIME, AKKHA),
    AKKHA_SHADOW_1_DURATION("Akkha Shadow1 Duration", types.TIME, AKKHA),

    AKKHA_P2_SPLIT("Akkha P2 Split", types.TIME, AKKHA),
    AKKHA_P2_DURATION("Akkha P2 Duration", types.TIME, AKKHA),
    AKKHA_SHADOW_2_SPLIT("Akkha Shadow2 Split", types.TIME, AKKHA),
    AKKHA_SHADOW_2_DURATION("Akkha Shadow2 Duration", types.TIME, AKKHA),

    AKKHA_P3_SPLIT("Akkha P3 Split", types.TIME, AKKHA),
    AKKHA_P3_DURATION("Akkha P3 Duration", types.TIME, AKKHA),
    AKKHA_SHADOW_3_SPLIT("Akkha Shadow3 Split", types.TIME, AKKHA),
    AKKHA_SHADOW_3_DURATION("Akkha Shadow3 Duration", types.TIME, AKKHA),

    AKKHA_P4_SPLIT("Akkha P4 Split", types.TIME, AKKHA),
    AKKHA_P4_DURATION("Akkha P4 Duration", types.TIME, AKKHA),
    AKKHA_SHADOW_4_SPLIT("Akkha Shadow4 Split", types.TIME, AKKHA),
    AKKHA_SHADOW_4_DURATION("Akkha Shadow4 Duration", types.TIME, AKKHA),

    AKKHA_P5_SPLIT("Akkha P5 Split", types.TIME, AKKHA),
    AKKHA_P5_DURATION("Akkha P5 Duration", types.TIME, AKKHA),

    AKKHA_FINAL_PHASE_SPLIT("Akkha Final Phase Split", types.TIME, AKKHA),
    AKKHA_FINAL_PHASE_DURATION("Akkha Final Phase Duration", types.TIME, AKKHA),
    AKKHA_NULL_HIT("Akkha Null Hit", types.TIME, AKKHA, true),

    WARDENS_TIME("Wardens Time", types.TIME, WARDENS),
    WARDENS_P1_DURATION("Wardens P1 Time", types.TIME, WARDENS),
    WARDENS_P2_DURATION("Wardens P2 Time", types.TIME, WARDENS),
    WARDENS_P3_SPLIT("Wardens P3 Split", types.TIME, WARDENS),
    WARDENS_ENRAGED_SPLIT("Wardens Enraged Split", types.TIME, WARDENS),
    WARDENS_UNTIL_ENRAGED_DURATION("Wardens Until Enraged Duration", types.TIME, WARDENS),
    WARDENS_P3_DURATION("Wardens P3 Time", types.TIME, WARDENS),
    WARDENS_ENRAGED_DURATION("Wardens Enraged Duration", types.TIME, WARDENS),
    WARDENS_SKULL_1_SPLIT("Wardens Skull 1 Split", types.TIME, WARDENS),
    WARDENS_SKULL_1_DURATION("Wardens Skull 1 Duration", types.TIME, WARDENS),
    WARDENS_SKULL_2_SPLIT("Wardens Skull 2 Split", types.TIME, WARDENS),
    WARDENS_SKULL_2_DURATION("Wardens Skull 2 Duration", types.TIME, WARDENS),
    WARDENS_SKULL_3_SPLIT("Wardens Skull 3 Split", types.TIME, WARDENS),
    WARDENS_P2_DOWNS("Wardens P2 Downs", types.OTHER_INT, WARDENS),
    WARDENS_SKULL_3_DURATION("Wardens Skull 3 Duration", types.TIME, WARDENS),
    WARDENS_SKULL_4_SPLIT("Wardens Skull 4 Split", types.TIME, WARDENS),
    WARDENS_SKULL_4_DURATION("Wardens Skull 4 Duration", types.TIME, WARDENS),
    APMEKEN_VOLATILE_COUNT("Apmeken Volatile Count", types.OTHER_INT, APMEKEN),
    APMEKEN_SHAMAN_COUNT("Apmeken Shaman Count", types.OTHER_INT, APMEKEN),
    APMEKEN_CURSED_COUNT("Apmeken Cursed Count", types.OTHER_INT, APMEKEN),
    BABA_BOULDERS_THROWN("Baba Boulders Thrown", types.OTHER_INT, BABA),
    BABA_BOULDERS_BROKEN("Baba Boulders Broken", types.OTHER_INT, BABA),
    KEPHRI_SWARMS_HEALED("Kephri Swarms Healed", types.OTHER_INT, KEPHRI),
    KEPHRI_SWARMS_TOTAL("Kephri Swarms Total", types.OTHER_INT, KEPHRI),
    KEPHRI_MELEE_SCARAB_HEALS("Kephri Melee Scarab Heals", types.OTHER_INT, KEPHRI),
    KEPHRI_DUNG_THROWN("Kephri Dung Thrown", types.OTHER_INT, KEPHRI),
    KEPHRI_MELEE_TICKS_ALIVE("Kephri Melee Ticks Alive", types.OTHER_INT, KEPHRI),
    HET_DOWNS("Het Downs", types.OTHER_INT, HET),
    CRONDIS_HEALS_100("Crondis Heals 100", types.OTHER_INT, CRONDIS),
    CRONDIS_HEALS_50("Crondis Heals 50", types.OTHER_INT, CRONDIS),
    CRONDIS_HEALS_25("Crondis Heals 25", types.OTHER_INT, CRONDIS),
    CRONDIS_CROCODILE_DAMAGE("Crondis Damage from Crocodile", types.OTHER_INT, CRONDIS),
    ZEBAK_JUGS_PUSHED("Zebak Jugs Pushed", types.OTHER_INT, ZEBAK),
    ZEBAK_BOULDER_ATTACKS("Zebak Boulder Attacks", types.OTHER_INT, ZEBAK),
    ZEBAK_WATERFALL_ATTACKS("Zebak Waterfall Attacks", types.OTHER_INT, ZEBAK),

    ;
    public static ArrayList<DataPoint> getTOBValues()
    {
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        for (DataPoint dataPoint : values())
        {
            if (dataPoint.isTOB())
            {
                dataPoints.add(dataPoint);
            }
        }
        return dataPoints;
    }

    public static ArrayList<DataPoint> getTOAValues()
    {
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        for (DataPoint dataPoint : values())
        {
            if (dataPoint.isTOA())
            {
                dataPoints.add(dataPoint);
            }
        }
        return dataPoints;
    }

    public boolean isTOB()
    {
        return room.equals(ANY_TOB)
                || room.equals(MAIDEN)
                || room.equals(BLOAT)
                || room.equals(NYLOCAS)
                || room.equals(SOTETSEG)
                || room.equals(XARPUS)
                || room.equals(VERZIK)
                || room.equals(ALL)
                || room.equals(ANY);
    }

    public boolean isTOA()
    {
        return room.equals(ANY_TOA)
                || room.equals(CRONDIS)
                || room.equals(ZEBAK)
                || room.equals(SCABARAS)
                || room.equals(KEPHRI)
                || room.equals(APMEKEN)
                || room.equals(BABA)
                || room.equals(HET)
                || room.equals(AKKHA)
                || room.equals(WARDENS)
                || room.equals(ALL)
                || room.equals(ANY);
    }

    public boolean isCOX()
    {
        return false;
    }

    public boolean isType(RaidType raidType)
    {
        switch (raidType)
        {
            case COX:
                return isCOX();
            case TOB:
                return isTOB();
            case TOA:
                return isTOA();
            default:
                return true;
        }
    }

    public static DataPoint getValue(String s)
    {
        for (DataPoint point : values())
        {
            if (point.name.equals(s))
            {
                return point;
            }
        }
        return DataPoint.UNKNOWN;
    }


    public enum types
    {
        OTHER_INT, OTHER_BOOL, TIME_DURATION, TIME_SPLIT, TIME
    }

    public final String name;
    public final int value;
    public final types type;
    public final RaidRoom room;
    public final boolean playerSpecific;


    DataPoint(String name, types type, RaidRoom room)
    {
        this.name = name;
        this.value = 0;
        this.type = type;
        this.room = room;
        this.playerSpecific = false;
    }

    DataPoint(String name, types type, RaidRoom room, int value)
    {
        this.name = name;
        this.value = value;
        this.type = type;
        this.room = room;
        this.playerSpecific = false;
    }

    DataPoint(String name, types type, RaidRoom room, boolean playerSpecific)
    {
        this.playerSpecific = playerSpecific;
        this.name = name;
        this.value = 0;
        this.type = type;
        this.room = room;
    }

    public static String[] getPlayerSpecific()
    {
        return getPlayerSpecific(RaidType.UNASSIGNED);
    }

    public static String[] getPlayerSpecific(RaidType raidType)
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.playerSpecific && point.isType(raidType))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getOtherIntNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.type.equals(types.OTHER_INT))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getByNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            valuesToGather.add(point.name);
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getTimeNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.type.equals(types.TIME) || point.type.equals(types.TIME_SPLIT) || point.type.equals(types.TIME_DURATION))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static List<DataPoint> getRoomPoints(RaidRoom room)
    {
        List<DataPoint> points = new ArrayList<>();
        for(DataPoint point : values())
        {
            if(point.room == room)
            {
                points.add(point);
            }
        }
        return points;
    }

    public boolean isTime()
    {
        return type.equals(types.TIME) || type.equals(types.TIME_DURATION) || type.equals(types.TIME_SPLIT);
    }

    public static String[] filterTimes(String[] data)
    {
        ArrayList<String> filtered = new ArrayList<>();
        for (String s : data)
        {
            if (DataPoint.getValue(s).isTime())
            {
                filtered.add(s);
            }
        }
        return Arrays.copyOf(filtered.toArray(), filtered.size(), String[].class);
    }

    public static String[] filterInt(String[] data)
    {
        ArrayList<String> filtered = new ArrayList<>();
        for (String s : data)
        {
            DataPoint dp = DataPoint.getValue(s);
            if (dp != null && dp.type == types.OTHER_INT && !dp.name.contains("thrall") && !dp.name.contains("veng")
                    && !dp.name.contains("BGS") && !dp.name.contains("hammers") && !dp.name.contains("dinhs"))
            {
                filtered.add(s);
            }
        }
        return Arrays.copyOf(filtered.toArray(), filtered.size(), String[].class);
    }

    public static String[] filterThrall(String[] data)
    {
        ArrayList<String> filtered = new ArrayList<>();
        for (String s : data)
        {
            if (Objects.requireNonNull(DataPoint.getValue(s)).name.contains("thrall"))
            {
                filtered.add(s);
            }
        }
        return Arrays.copyOf(filtered.toArray(), filtered.size(), String[].class);
    }

    public static String[] filterVeng(String[] data)
    {
        ArrayList<String> filtered = new ArrayList<>();
        for (String s : data)
        {
            if (Objects.requireNonNull(DataPoint.getValue(s)).name.contains("veng"))
            {
                filtered.add(s);
            }
        }
        return Arrays.copyOf(filtered.toArray(), filtered.size(), String[].class);
    }

    public static String[] filterSpecs(String[] data)
    {
        ArrayList<String> filtered = new ArrayList<>();
        for (String s : data)
        {
            String name = Objects.requireNonNull(DataPoint.getValue(s)).name;
            if (name.contains("BGS") || name.contains("hammers") || name.contains("dinhs"))
            {
                filtered.add(s);
            }
        }
        return Arrays.copyOf(filtered.toArray(), filtered.size(), String[].class);
    }

    public static String[] getSpecificNames(RaidRoom room)
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.room.equals(room))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getRoomTimes()
    {
        return new String[]{"Challenge Time", "Overall Time", "Time Outside Rooms", "Maiden Time", "Bloat Time", "Nylocas Time", "Sotetseg Time", "Xarpus Time", "Verzik Time",
                "Scabaras Time", "Kephri Time", "Apmeken Time", "Baba Time", "Crondis Time", "Zebak Time", "Het Time", "Akkha Time", "Wardens Time"};
    }

    public static ArrayList<String> getTimeNamesByRoom(RaidRoom room)
    {
        ArrayList<String> timesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.room.equals(room) && (point.type.equals(types.TIME) || point.type.equals(types.TIME_DURATION) || point.type.equals(types.TIME_SPLIT)))
            {
                timesToGather.add(point.name);
            }
        }
        return timesToGather;
    }
}
