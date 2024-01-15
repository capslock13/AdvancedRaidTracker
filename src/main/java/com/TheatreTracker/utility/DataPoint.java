package com.TheatreTracker.utility;

import java.util.ArrayList;
import java.util.Arrays;

public enum DataPoint {

    RAID_TIME("Overall Time", types.TIME, rooms.ANY),
    MAIDEN_TOTAL_TIME("Maiden Time", types.TIME, rooms.MAIDEN),
    BLOAT_TOTAL_TIME("Bloat Time", types.TIME, rooms.BLOAT),
    NYLO_TOTAL_TIME("Nylo Time", types.TIME, rooms.NYLOCAS),
    XARP_TOTAL_TIME("Xarp Time", types.TIME, rooms.XARPUS),
    SOTE_TOTAL_TIME("Sote Time", types.TIME, rooms.SOTETSEG),
    VERZIK_TOTAL_TIME("Verzik Time", types.TIME, rooms.VERZIK),
    MAIDEN_BLOOD_SPAWNED("Maiden blood spawned", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_BLOOD_THROWN("Maiden blood thrown", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_PLAYER_STOOD_IN_THROWN_BLOOD("Maiden player stood in thrown blood", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_PLAYER_STOOD_IN_SPAWNED_BLOOD("Maiden player stood in spawned blood", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_HEALS_FROM_THROWN_BLOOD("Maiden heals from thrown blood", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_HEALS_FROM_SPAWNED_BLOOD("Maiden heals from spawned blood", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_PLAYER_STOOD_IN_BLOOD("Maiden player stood in any blood", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_HEALS_FROM_ANY_BLOOD("Maiden heals from any blood", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_CRABS_LEAKED("Maiden crabs leaked", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_CRABS_LEAKED_FULL_HP("Maiden crabs leaked full", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_HP_HEALED("Maiden HP healed", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_DEFENSE("Maiden defense", types.OTHER_INT, rooms.MAIDEN, 200),
    MAIDEN_DEATHS("Maiden deaths", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_DINHS_SPECS("Maiden dinhs specs", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_DINHS_CRABS_HIT("Maiden dinhs crabs Hit", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_DINHS_TARGETS_HIT("Maiden dinhs targets hit", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_DINHS_AVERAGE_HP_HIT("Maiden dinhs average HP crab", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_DINHS_PERCENT_TARGETS_CRAB("Maiden dinhs % crabs targeted", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_DINHS_CRABS_UNDER_27_TARGETED("Maiden dinhs crab < 27hp targeted" , types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_DINHS_CRABS_UNDER_27_TARGETED_PERCENT("Maiden dinhs crabs <27hp targeted %", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_CHINS_THROWN("Maiden chins thrown", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_CHINS_THROWN_WRONG_DISTANCE("Maiden chins thrown wrong distance", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_CHIN_CORRECT_DISTANCE_PERCENT("Maiden chins correct distance %", types.OTHER_INT, rooms.MAIDEN),

    BLOAT_DOWNS("Bloat downs", types.OTHER_INT, rooms.BLOAT),
    BLOAT_HP_FIRST_DOWN("Bloat HP 1st down", types.OTHER_INT, rooms.BLOAT),
    BLOAT_FIRST_WALK_SCYTHES("Bloat 1st walk scythes", types.OTHER_INT, rooms.BLOAT),
    BLOAT_FIRST_WALK_DEATHS("Bloat 1st walk deaths", types.OTHER_INT, rooms.BLOAT),
    BLOAT_DEFENSE("Bloat defense", types.OTHER_INT, rooms.BLOAT, 100),
    BLOAT_DEATHS("Bloat deaths", types.OTHER_INT, rooms.BLOAT),
    NYLO_STALLS_PRE_20("Nylo stalls pre 20", types.OTHER_INT, rooms.NYLOCAS),
    NYLO_STALLS_POST_20("Nylo stalls post 20", types.OTHER_INT, rooms.NYLOCAS),
    NYLO_STALLS_TOTAL("Nylo stalls total", types.OTHER_INT, rooms.NYLOCAS),
    NYLO_SPLITS_RANGE("Nylo splits range", types.OTHER_INT, rooms.NYLOCAS),
    NYLO_SPLITS_MAGE("Nylo splits mage", types.OTHER_INT, rooms.NYLOCAS),
    NYLO_SPLITS_MELEE("Nylo splits melee", types.OTHER_INT, rooms.NYLOCAS),
    NYLO_ROTATIONS_RANGE("Nylo rotations range", types.OTHER_INT, rooms.NYLOCAS),
    NYLO_ROTATIONS_MAGE("Nylo rotations mage", types.OTHER_INT, rooms.NYLOCAS),
    NYLO_ROTATIONS_MELEE("Nylo rotations melee", types.OTHER_INT, rooms.NYLOCAS),
    NYLO_DEFENSE("Nylo defense", types.OTHER_INT, rooms.NYLOCAS, 50),
    NYLO_DEATHS("Nylo deaths", types.OTHER_INT, rooms.NYLOCAS),
    SOTE_SPECS_P1("Sote specs p1", types.OTHER_INT, rooms.SOTETSEG),
    SOTE_SPECS_P2("Sote specs p2", types.OTHER_INT, rooms.SOTETSEG),
    SOTE_SPECS_P3("Sote specs p3", types.OTHER_INT, rooms.SOTETSEG),
    SOTE_SPECS_TOTAL("Sote specs total", types.OTHER_INT, rooms.SOTETSEG),
    SOTE_DEATHS("Sote deaths", types.OTHER_INT, rooms.SOTETSEG),
    XARP_HEALING("Xarp Healing", types.OTHER_INT, rooms.SOTETSEG),
    XARP_DEFENSE("Xarp defense", types.OTHER_INT, rooms.XARPUS, 250),
    XARP_DEATHS("Xarp Deaths", types.OTHER_INT, rooms.XARPUS),
    VERZIK_BOUNCES("Verzik bounces", types.OTHER_INT, rooms.VERZIK),
    VERZIK_CRABS_SPAWNED("Verzik crabs spawned", types.OTHER_INT, rooms.VERZIK),
    VERZIK_DEATHS("Verzik deaths", types.OTHER_INT, rooms.VERZIK),
    MAIDEN_70_SPLIT("Maiden 70s split", types.TIME, rooms.MAIDEN),
    MAIDEN_7050_SPLIT("Maiden 70-50s split", types.TIME, rooms.MAIDEN),
    MAIDEN_50_SPLIT("Maiden 50s split", types.TIME, rooms.MAIDEN),
    MAIDEN_5030_SPLIT("Maiden 50-30s split", types.TIME, rooms.MAIDEN),
    MAIDEN_30_SPLIT("Maiden 30s split", types.TIME, rooms.MAIDEN),
    MAIDEN_SKIP_SPLIT("Maiden Skip split", types.TIME, rooms.MAIDEN),
    BLOAT_FIRST_DOWN_TIME("Bloat 1st down time", types.TIME, rooms.BLOAT),
    NYLO_BOSS_SPAWN("Nylo boss spawn", types.TIME, rooms.NYLOCAS),
    NYLO_BOSS_DURATION("Nylo boss duration", types.TIME, rooms.NYLOCAS),
    NYLO_LAST_WAVE("Nylo last wave", types.TIME, rooms.NYLOCAS),
    NYLO_CLEANUP("Nylo cleanup", types.TIME, rooms.NYLOCAS),
    SOTE_P1_SPLIT("Sote P1 split", types.TIME, rooms.SOTETSEG),
    SOTE_P2_SPLIT("Sote P2 split", types.TIME, rooms.SOTETSEG),
    SOTE_P3_SPLIT("Sote P3 split", types.TIME, rooms.SOTETSEG),
    SOTE_M1_SPLIT("Sote Maze1 split", types.TIME, rooms.SOTETSEG),
    SOTE_M2_SPLIT("Sote Maze2 split", types.TIME, rooms.SOTETSEG),
    SOTE_MAZE_SUM("Sote mazes combined", types.TIME, rooms.SOTETSEG),
    XARP_SCREECH("Xarp screech", types.TIME, rooms.XARPUS),
    XARP_POST_SCREECH("Xarp post screech", types.TIME, rooms.XARPUS),
    VERZIK_P1_SPLIT("Verzik P1 split", types.TIME, rooms.VERZIK),
    VERZIK_P2_SPLIT("Verzik P2 split", types.TIME, rooms.VERZIK),
    VERZIK_P2_DURATION("Verzik P2 duration", types.TIME, rooms.VERZIK),
    VERZIK_P3_DURATION("Verzik P3 duration", types.TIME, rooms.VERZIK),

    NYLO_ENTRY("Nylo entry", types.TIME, rooms.NYLOCAS),
    SOTE_ENTRY("Sote entry", types.TIME, rooms.SOTETSEG),
    XARP_ENTRY("Xarp entry", types.TIME, rooms.XARPUS),
    VERZIK_ENTRY("Verzik entry", types.TIME, rooms.VERZIK),

    ATTEMPTED_HAMMERS_MAIDEN("Attempted hammers Maiden", types.OTHER_INT, rooms.MAIDEN),
    ATTEMPTED_HAMMERS_BLOAT("Attempted hammers Bloat", types.OTHER_INT, rooms.BLOAT),
    ATTEMPTED_HAMMERS_NYLO("Attempted hammers Nylo", types.OTHER_INT, rooms.NYLOCAS),
    ATTEMPTED_HAMMERS_SOTE("Attempted hammers Sote", types.OTHER_INT, rooms.SOTETSEG),
    ATTEMPTED_HAMMERS_XARP("Attempted hammers Xarp", types.OTHER_INT, rooms.XARPUS),
    ATTEMPTED_HAMMERS_VERZIK("Attempted hammers Verzik", types.OTHER_INT, rooms.VERZIK),
    HIT_HAMMERS_MAIDEN("Hit hammers Maiden", types.OTHER_INT, rooms.MAIDEN),
    HIT_HAMMERS_BLOAT("Hit hammers Bloat", types.OTHER_INT, rooms.BLOAT),
    HIT_HAMMERS_NYLO("Hit hammers Nylo", types.OTHER_INT, rooms.NYLOCAS),
    HIT_HAMMERS_SOTE("Hit hammers Sote", types.OTHER_INT, rooms.SOTETSEG),
    HIT_HAMMERS_XARP("Hit hammers Xarp", types.OTHER_INT, rooms.XARPUS),
    HIT_HAMMERS_VERZIK("Hit hammers Verzik", types.OTHER_INT, rooms.VERZIK),

    THRALL_ATTACKS_TOTAL("Total thrall attacks", types.OTHER_INT, rooms.ANY),
    THRALL_ATTACKS_MAIDEN("Thrall attacks Maiden", types.OTHER_INT, rooms.MAIDEN),
    THRALL_ATTACKS_BLOAT("Thrall attacks Bloat", types.OTHER_INT, rooms.BLOAT),
    THRALL_ATTACKS_NYLO("Thrall attacks Nylo", types.OTHER_INT, rooms.NYLOCAS),
    THRALL_ATTACKS_SOTE("Thrall attacks Sote", types.OTHER_INT, rooms.SOTETSEG),
    THRALL_ATTACKS_XARP("Thrall attacks Xarp", types.OTHER_INT, rooms.XARPUS),
    THRALL_ATTACKS_VERZIK("Thrall attacks Verz", types.OTHER_INT, rooms.VERZIK),

    THRALL_DAMAGE_TOTAL("Total thrall damage", types.OTHER_INT, rooms.ANY),
    THRALL_DAMAGE_MAIDEN("Thrall damage Maiden", types.OTHER_INT, rooms.MAIDEN),
    THRALL_DAMAGE_BLOAT("Thrall damage Bloat", types.OTHER_INT, rooms.BLOAT),
    THRALL_DAMAGE_NYLO("Thrall damage Nylo", types.OTHER_INT, rooms.NYLOCAS),
    THRALL_DAMAGE_SOTE("Thrall damage Sote", types.OTHER_INT, rooms.SOTETSEG),
    THRALL_DAMAGE_XARP("Thrall damage Xarp", types.OTHER_INT, rooms.XARPUS),
    THRALL_DAMAGE_VERZIK("Thrall damage Verz", types.OTHER_INT, rooms.VERZIK),

    VENG_DAMAGE_TOTAL("Total veng damage", types.OTHER_INT, rooms.ANY),
    VENG_DAMAGE_MAIDEN("Veng damage Maiden", types.OTHER_INT, rooms.MAIDEN),
    VENG_DAMAGE_BLOAT("Veng damage Bloat", types.OTHER_INT, rooms.BLOAT),
    VENG_DAMAGE_NYLO("Veng damage Nylo", types.OTHER_INT, rooms.NYLOCAS),
    VENG_DAMAGE_SOTE("Veng damage Sote", types.OTHER_INT, rooms.SOTETSEG),
    VENG_DAMAGE_XARP("Veng damage Xarp", types.OTHER_INT, rooms.XARPUS),
    VENG_DAMAGE_VERZIK("Veng damage Verz", types.OTHER_INT, rooms.VERZIK),

    VENG_CASTS_TOTAL("Total veng casts", types.OTHER_INT, rooms.ANY),
    VENG_CASTS_MAIDEN("Veng damage Maiden", types.OTHER_INT, rooms.MAIDEN),
    VENG_CASTS_BLOAT("Veng damage Bloat", types.OTHER_INT, rooms.BLOAT),
    VENG_CASTS_NYLO("Veng damage Nylo", types.OTHER_INT, rooms.NYLOCAS),
    VENG_CASTS_SOTE("Veng damage Sote", types.OTHER_INT, rooms.SOTETSEG),
    VENG_CASTS_XARP("Veng damage Xarp", types.OTHER_INT, rooms.XARPUS),
    VENG_CASTS_VERZIK("Veng damage Verz", types.OTHER_INT, rooms.VERZIK),

    VENG_PROCS_TOTAL("Total veng procs", types.OTHER_INT, rooms.ANY),
    VENG_PROCS_MAIDEN("Veng procs Maiden", types.OTHER_INT, rooms.MAIDEN),
    VENG_PROCS_BLOAT("Veng procs Bloat", types.OTHER_INT, rooms.BLOAT),
    VENG_PROCS_NYLO("Veng procs Nylo", types.OTHER_INT, rooms.NYLOCAS),
    VENG_PROCS_SOTE("Veng procs Sote", types.OTHER_INT, rooms.SOTETSEG),
    VENG_PROCS_XARP("Veng procs Xarp", types.OTHER_INT, rooms.XARPUS),
    VENG_PROCS_VERZIK("Veng procs Verz", types.OTHER_INT, rooms.VERZIK),
    KODAI_BOPS("Kodai bops", types.OTHER_INT, rooms.ANY),
    DWH_BOPS("DWH bops", types.OTHER_INT, rooms.ANY),
    CHALLY_POKE("Chally pokes", types.OTHER_INT, rooms.ANY),
    BGS_WHACKS("BGS whacks", types.OTHER_INT, rooms.ANY),


    ;

    public static DataPoint getValue(String s) {
        for (DataPoint point : values()) {
            if (point.name.equals(s)) {
                return point;
            }
        }
        return null;
    }

    public static enum rooms {
        MAIDEN, BLOAT, NYLOCAS, SOTETSEG, XARPUS, VERZIK, ANY
    }

    ;

    public static enum types {
        OTHER_INT, OTHER_BOOL, TIME
    }

    public final String name;
    public final int value;
    public final types type;
    public final rooms room;


    DataPoint(String name, types type, rooms room) {
        this.name = name;
        this.value = 0;
        this.type = type;
        this.room = room;
    }

    DataPoint(String name, types type, rooms room, int value) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.room = room;
    }

    public static String[] getOtherIntNames() {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values()) {
            if (point.type.equals(types.OTHER_INT)) {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getByNames() {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values()) {
            valuesToGather.add(point.name);
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getTimeNames() {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values()) {
            if (point.type.equals(types.TIME)) {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getMaidenNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.room.equals(rooms.MAIDEN))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getBloatNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.room.equals(rooms.BLOAT))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getNyloNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.room.equals(rooms.NYLOCAS))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getSoteNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.room.equals(rooms.SOTETSEG))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getXarpNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.room.equals(rooms.XARPUS))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getVerzikNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.room.equals(rooms.VERZIK))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getAnyRoomNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.room.equals(rooms.ANY))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getRoomTimes()
    {
        return new String[]{"Overall Time", "Maiden Time", "Bloat Time", "Nylo Time", "Sote Time", "Xarp Time", "Verzik Time"};
    }

    public static ArrayList<String> getTimeNamesByRoom(rooms room) {
        ArrayList<String> timesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values()) {
            if (point.room.equals(room) && point.type.equals(types.TIME)) {
                timesToGather.add(point.name);
            }
        }
        return timesToGather;
    }
}
