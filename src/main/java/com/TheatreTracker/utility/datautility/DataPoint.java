package com.TheatreTracker.utility.datautility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public enum DataPoint
{

    CHALLENGE_TIME("Challenge Time", types.TIME, rooms.ANY),
    OVERALL_TIME("Overall Time", types.TIME, rooms.ANY),
    TIME_OUTSIDE_ROOMS("Time Outside Rooms", types.TIME, rooms.ANY),
    MAIDEN_TOTAL_TIME("Maiden Time", types.TIME, rooms.MAIDEN),
    BLOAT_TOTAL_TIME("Bloat Time", types.TIME, rooms.BLOAT),
    NYLO_TOTAL_TIME("Nylocas Time", types.TIME, rooms.NYLOCAS),
    XARP_TOTAL_TIME("Xarpus Time", types.TIME, rooms.XARPUS),
    SOTE_TOTAL_TIME("Sotetseg Time", types.TIME, rooms.SOTETSEG),
    VERZIK_TOTAL_TIME("Verzik Time", types.TIME, rooms.VERZIK),
    MAIDEN_BLOOD_SPAWNED("Maiden blood spawned", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_BLOOD_THROWN("Maiden blood thrown", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_PLAYER_STOOD_IN_THROWN_BLOOD("Maiden player stood in thrown blood", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_PLAYER_STOOD_IN_SPAWNED_BLOOD("Maiden player stood in spawned blood", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_HEALS_FROM_THROWN_BLOOD("Maiden heals from thrown blood", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_HEALS_FROM_SPAWNED_BLOOD("Maiden heals from spawned blood", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_PLAYER_STOOD_IN_BLOOD("Maiden player stood in any blood", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_HEALS_FROM_ANY_BLOOD("Maiden heals from any blood", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_MELEE_DRAINS("Maiden drained melee", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_CRABS_LEAKED("Maiden crabs leaked", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_CRABS_LEAKED_FULL_HP("Maiden crabs leaked full", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_HP_HEALED("Maiden HP healed", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_DEFENSE("Maiden defense", types.OTHER_INT, rooms.MAIDEN, 200),
    MAIDEN_DEATHS("Maiden deaths", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_DINHS_SPECS("Maiden dinhs specs", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_DINHS_CRABS_HIT("Maiden dinhs crabs Hit", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_DINHS_TARGETS_HIT("Maiden dinhs targets hit", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_DINHS_AVERAGE_HP_HIT("Maiden dinhs average HP crab", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_DINHS_PERCENT_TARGETS_CRAB("Maiden dinhs % crabs targeted", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_DINHS_CRABS_UNDER_27_TARGETED("Maiden dinhs crab < 27hp targeted", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_DINHS_CRABS_UNDER_27_TARGETED_PERCENT("Maiden dinhs crabs <27hp targeted %", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_CHINS_THROWN("Maiden chins thrown", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_CHINS_THROWN_WRONG_DISTANCE("Maiden chins thrown wrong distance", types.OTHER_INT, rooms.MAIDEN, true),
    MAIDEN_CHIN_CORRECT_DISTANCE_PERCENT("Maiden chins correct distance %", types.OTHER_INT, rooms.MAIDEN, true),

    BLOAT_DOWNS("Bloat downs", types.OTHER_INT, rooms.BLOAT),
    BLOAT_HP_FIRST_DOWN("Bloat HP% 1st down", types.OTHER_INT, rooms.BLOAT),
    BLOAT_FIRST_WALK_SCYTHES("Bloat 1st walk scythes", types.OTHER_INT, rooms.BLOAT, true),
    BLOAT_FIRST_WALK_DEATHS("Bloat 1st walk deaths", types.OTHER_INT, rooms.BLOAT, true),
    BLOAT_DEFENSE("Bloat defense", types.OTHER_INT, rooms.BLOAT, 100),
    BLOAT_DEATHS("Bloat deaths", types.OTHER_INT, rooms.BLOAT, true),
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
    NYLO_DEATHS("Nylo deaths", types.OTHER_INT, rooms.NYLOCAS, true),
    SOTE_SPECS_P1("Sote specs p1", types.OTHER_INT, rooms.SOTETSEG),
    SOTE_SPECS_P2("Sote specs p2", types.OTHER_INT, rooms.SOTETSEG),
    SOTE_SPECS_P3("Sote specs p3", types.OTHER_INT, rooms.SOTETSEG),
    SOTE_SPECS_TOTAL("Sote specs total", types.OTHER_INT, rooms.SOTETSEG),
    SOTE_DEATHS("Sote deaths", types.OTHER_INT, rooms.SOTETSEG, true),
    XARP_HEALING("Xarp Healing", types.OTHER_INT, rooms.XARPUS),
    XARP_DEFENSE("Xarp defense", types.OTHER_INT, rooms.XARPUS, 250),
    XARP_DEATHS("Xarp deaths", types.OTHER_INT, rooms.XARPUS, true),
    VERZIK_BOUNCES("Verzik bounces", types.OTHER_INT, rooms.VERZIK, true),
    VERZIK_CRABS_SPAWNED("Verzik crabs spawned", types.OTHER_INT, rooms.VERZIK),
    VERZIK_REDS_SETS("Verzik Red Sets", types.OTHER_INT, rooms.VERZIK),
    VERZIK_REDS_PROC_PERCENT("Verzik Red Proc Percent", types.OTHER_INT, rooms.VERZIK),
    VERZIK_DEATHS("Verzik deaths", types.OTHER_INT, rooms.VERZIK, true),
    VERZIK_HP_AT_WEBS("Verzik HP% at webs", types.OTHER_INT, rooms.VERZIK),
    MAIDEN_70_SPLIT("Maiden 70s split", types.TIME, rooms.MAIDEN),
    MAIDEN_7050_SPLIT("Maiden 70-50s split", types.TIME, rooms.MAIDEN),
    MAIDEN_50_SPLIT("Maiden 50s split", types.TIME, rooms.MAIDEN),
    MAIDEN_5030_SPLIT("Maiden 50-30s split", types.TIME, rooms.MAIDEN),
    MAIDEN_30_SPLIT("Maiden 30s split", types.TIME, rooms.MAIDEN),
    MAIDEN_SKIP_SPLIT("Maiden Skip split", types.TIME, rooms.MAIDEN),
    BLOAT_FIRST_DOWN_TIME("Bloat 1st down time", types.OTHER_INT, rooms.BLOAT),
    NYLO_BOSS_SPAWN("Nylo boss spawn", types.TIME, rooms.NYLOCAS),
    NYLO_BOSS_DURATION("Nylo boss duration", types.TIME, rooms.NYLOCAS),
    NYLO_LAST_WAVE("Nylo last wave", types.TIME, rooms.NYLOCAS),
    NYLO_CLEANUP("Nylo cleanup", types.TIME, rooms.NYLOCAS),
    SOTE_P1_SPLIT("Sote P1 split", types.TIME, rooms.SOTETSEG),
    SOTE_P2_SPLIT("Sote P2 split", types.TIME, rooms.SOTETSEG),
    SOTE_P2_DURATION("Sote P2 duration", types.TIME, rooms.SOTETSEG),

    SOTE_P3_DURATION("Sote P3 duration", types.TIME, rooms.SOTETSEG),
    SOTE_M1_SPLIT("Sote maze1 split", types.TIME, rooms.SOTETSEG),
    SOTE_M1_DURATION("Sote maze1 Duration", types.TIME, rooms.SOTETSEG),
    SOTE_M2_SPLIT("Sote maze2 split", types.TIME, rooms.SOTETSEG),
    SOTE_M2_DURATION("Sote maze2 duration", types.TIME, rooms.SOTETSEG),
    SOTE_MAZE_SUM("Sote mazes combined", types.TIME, rooms.SOTETSEG),
    XARP_SCREECH("Xarp screech", types.TIME, rooms.XARPUS),
    XARP_POST_SCREECH("Xarp post screech", types.TIME, rooms.XARPUS),
    VERZIK_P1_SPLIT("Verzik P1 split", types.TIME, rooms.VERZIK),

    VERZIK_P2_TILL_REDS("Verzik P2 until reds split", types.TIME, rooms.VERZIK),
    VERZIK_REDS_SPLIT("Verzik reds split", types.TIME, rooms.VERZIK),
    VERZIK_REDS_DURATION("Verzik reds duration", types.TIME, rooms.VERZIK),
    VERZIK_P2_SPLIT("Verzik P2 split", types.TIME, rooms.VERZIK),
    VERZIK_P2_DURATION("Verzik P2 duration", types.TIME, rooms.VERZIK),
    VERZIK_P3_DURATION("Verzik P3 duration", types.TIME, rooms.VERZIK),

    NYLO_ENTRY("Nylo Entry", types.TIME, rooms.NYLOCAS),
    SOTE_ENTRY("Sote Entry", types.TIME, rooms.SOTETSEG),
    XARP_ENTRY("Xarp Entry", types.TIME, rooms.XARPUS),
    VERZIK_ENTRY("Verzik Entry", types.TIME, rooms.VERZIK),

    ATTEMPTED_HAMMERS_MAIDEN("Maiden attempted hammers", types.OTHER_INT, rooms.MAIDEN, true),
    ATTEMPTED_HAMMERS_BLOAT("Bloat attempted hammers", types.OTHER_INT, rooms.BLOAT, true),
    ATTEMPTED_HAMMERS_NYLO("Nylo attempted hammers", types.OTHER_INT, rooms.NYLOCAS, true),
    ATTEMPTED_HAMMERS_SOTE("Sote attempted hammers", types.OTHER_INT, rooms.SOTETSEG, true),
    ATTEMPTED_HAMMERS_XARP("Xarp attempted hammers", types.OTHER_INT, rooms.XARPUS, true),
    ATTEMPTED_HAMMERS_VERZIK("Verzik attempted hammers", types.OTHER_INT, rooms.VERZIK, true),
    HIT_HAMMERS_MAIDEN("Maiden hit hammers", types.OTHER_INT, rooms.MAIDEN, true),
    HIT_HAMMERS_BLOAT("Bloat hit hammers", types.OTHER_INT, rooms.BLOAT, true),
    HIT_HAMMERS_NYLO("Nylo hit hammers", types.OTHER_INT, rooms.NYLOCAS, true),
    HIT_HAMMERS_SOTE("Sote hit hammers", types.OTHER_INT, rooms.SOTETSEG, true),
    HIT_HAMMERS_XARP("Xarp hit hammers", types.OTHER_INT, rooms.XARPUS, true),
    HIT_HAMMERS_VERZIK("Verzik hit hammers", types.OTHER_INT, rooms.VERZIK, true),

    ATTEMPTED_BGS_MAIDEN("Maiden attempted BGS", types.OTHER_INT, rooms.MAIDEN, true),
    BGS_DAMAGE_MAIDEN("Maiden BGS damage", types.OTHER_INT, rooms.MAIDEN, true),

    ATTEMPTED_BGS_BLOAT("Bloat attempted BGS", types.OTHER_INT, rooms.BLOAT, true),
    BGS_DAMAGE_BLOAT("Bloat BGS damage", types.OTHER_INT, rooms.BLOAT, true),

    ATTEMPTED_BGS_NYLO("Nylo attempted BGS", types.OTHER_INT, rooms.NYLOCAS, true),
    BGS_DAMAGE_NYLO("Nylo BGS damage", types.OTHER_INT, rooms.NYLOCAS, true),

    ATTEMPTED_BGS_SOTE("Sote attempted BGS", types.OTHER_INT, rooms.SOTETSEG, true),
    BGS_DAMAGE_SOTE("Sote BGS damage", types.OTHER_INT, rooms.SOTETSEG, true),

    ATTEMPTED_BGS_XARP("Xarp attempted BGS", types.OTHER_INT, rooms.XARPUS, true),
    BGS_DAMAGE_XARP("Xarp BGS damage", types.OTHER_INT, rooms.XARPUS, true),

    ATTEMPTED_BGS_VERZ("Verzik attempted BGS", types.OTHER_INT, rooms.VERZIK, true),
    BGS_DAMAGE_VERZ("Verzik BGS damage", types.OTHER_INT, rooms.VERZIK, true),

    THRALL_ATTACKS_TOTAL("Total thrall attacks", types.OTHER_INT, rooms.ANY, true),
    THRALL_ATTACKS_MAIDEN("Maiden thrall attacks", types.OTHER_INT, rooms.MAIDEN, true),
    THRALL_ATTACKS_BLOAT("Bloat thrall attacks", types.OTHER_INT, rooms.BLOAT, true),
    THRALL_ATTACKS_NYLO("Nylo thrall attacks", types.OTHER_INT, rooms.NYLOCAS, true),
    THRALL_ATTACKS_SOTE("Sote thrall attacks", types.OTHER_INT, rooms.SOTETSEG, true),
    THRALL_ATTACKS_XARP("Xarp thrall attacks", types.OTHER_INT, rooms.XARPUS, true),
    THRALL_ATTACKS_VERZIK("Verzik thrall attacks", types.OTHER_INT, rooms.VERZIK, true),

    THRALL_DAMAGE_TOTAL("Total thrall damage", types.OTHER_INT, rooms.ANY, true),
    THRALL_DAMAGE_MAIDEN("Maiden thrall damage", types.OTHER_INT, rooms.MAIDEN, true),
    THRALL_DAMAGE_BLOAT("Bloat thrall damage", types.OTHER_INT, rooms.BLOAT, true),
    THRALL_DAMAGE_NYLO("Nylo thrall damage", types.OTHER_INT, rooms.NYLOCAS, true),
    THRALL_DAMAGE_SOTE("Sote thrall damage", types.OTHER_INT, rooms.SOTETSEG, true),
    THRALL_DAMAGE_XARP("Xarp thrall damage", types.OTHER_INT, rooms.XARPUS, true),
    THRALL_DAMAGE_VERZIK("Verzik thrall damage", types.OTHER_INT, rooms.VERZIK, true),

    VENG_DAMAGE_TOTAL("Total veng damage", types.OTHER_INT, rooms.ANY),
    VENG_DAMAGE_MAIDEN("Maiden veng damage", types.OTHER_INT, rooms.MAIDEN),
    VENG_DAMAGE_BLOAT("Bloat veng damage", types.OTHER_INT, rooms.BLOAT),
    VENG_DAMAGE_NYLO("Nylo veng damage", types.OTHER_INT, rooms.NYLOCAS),
    VENG_DAMAGE_SOTE("Sote veng damage", types.OTHER_INT, rooms.SOTETSEG),
    VENG_DAMAGE_XARP("Xarp veng damage", types.OTHER_INT, rooms.XARPUS),
    VENG_DAMAGE_VERZIK("Verzik veng damage", types.OTHER_INT, rooms.VERZIK),

    VENG_CASTS_TOTAL("Total veng casts", types.OTHER_INT, rooms.ANY),
    VENG_CASTS_MAIDEN("Maiden veng casts", types.OTHER_INT, rooms.MAIDEN),
    VENG_CASTS_BLOAT("Bloat veng casts", types.OTHER_INT, rooms.BLOAT),
    VENG_CASTS_NYLO("Nylo veng casts", types.OTHER_INT, rooms.NYLOCAS),
    VENG_CASTS_SOTE("Sote veng casts", types.OTHER_INT, rooms.SOTETSEG),
    VENG_CASTS_XARP("Xarp veng casts", types.OTHER_INT, rooms.XARPUS),
    VENG_CASTS_VERZIK("Verzik veng casts", types.OTHER_INT, rooms.VERZIK),

    VENG_PROCS_TOTAL("Total veng procs", types.OTHER_INT, rooms.ANY),
    VENG_PROCS_MAIDEN("Maiden veng procs", types.OTHER_INT, rooms.MAIDEN),
    VENG_PROCS_BLOAT("Bloat veng procs", types.OTHER_INT, rooms.BLOAT),
    VENG_PROCS_NYLO("Nylo veng procs", types.OTHER_INT, rooms.NYLOCAS),
    VENG_PROCS_SOTE("Sote veng procs", types.OTHER_INT, rooms.SOTETSEG),
    VENG_PROCS_XARP("Xarp veng procs", types.OTHER_INT, rooms.XARPUS),
    VENG_PROCS_VERZIK("Verzik veng procs", types.OTHER_INT, rooms.VERZIK),
    KODAI_BOPS("Kodai bops", types.OTHER_INT, rooms.ANY, true),
    DWH_BOPS("DWH bops", types.OTHER_INT, rooms.ANY, true),
    CHALLY_POKE("Chally pokes", types.OTHER_INT, rooms.ANY, true),
    BGS_WHACKS("BGS whacks", types.OTHER_INT, rooms.ANY, true),
    TOTAL_DEATHS("Total deaths", types.OTHER_INT, rooms.ANY, true),
    DEATHS("Alternate Deaths", types.OTHER_INT, rooms.ALL, true),
    UNKNOWN("Unknown", types.OTHER_BOOL, rooms.ANY);



    public static DataPoint getValue(String s)
    {
        for (DataPoint point : values())
        {
            if (point.name.equals(s))
            {
                return point;
            }
        }
        return null;
    }

    public static enum rooms
    {
        ANY,
        MAIDEN,
        BLOAT,
        NYLOCAS,
        SOTETSEG,
        XARPUS,
        VERZIK,
        ALL
    }

    ;

    public enum types
    {
        OTHER_INT, OTHER_BOOL, TIME
    }

    public final String name;
    public final int value;
    public final types type;
    public final rooms room;
    public final boolean playerSpecific;


    DataPoint(String name, types type, rooms room)
    {
        this.name = name;
        this.value = 0;
        this.type = type;
        this.room = room;
        this.playerSpecific = false;
    }

    DataPoint(String name, types type, rooms room, int value)
    {
        this.name = name;
        this.value = value;
        this.type = type;
        this.room = room;
        this.playerSpecific = false;
    }

    DataPoint(String name, types type, rooms room, boolean playerSpecific)
    {
        this.playerSpecific = playerSpecific;
        this.name = name;
        this.value = 0;
        this.type = type;
        this.room = room;
    }

    public static String[] getPlayerSpecific()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.playerSpecific)
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
            if (point.type.equals(types.TIME))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] filterTimes(String[] data)
    {
        ArrayList<String> filtered = new ArrayList<>();
        for (String s : data)
        {
            if (Objects.requireNonNull(DataPoint.getValue(s)).type == types.TIME)
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
        return new String[]{"Challenge Time", "Overall Time", "Time Outside Rooms", "Maiden Time", "Bloat Time", "Nylocas Time", "Sotetseg Time", "Xarpus Time", "Verzik Time"};
    }

    public static ArrayList<String> getTimeNamesByRoom(rooms room)
    {
        ArrayList<String> timesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.room.equals(room) && point.type.equals(types.TIME))
            {
                timesToGather.add(point.name);
            }
        }
        return timesToGather;
    }
}
