package com.cTimers.utility;

import java.util.ArrayList;
import java.util.Arrays;

public enum cDataPoint
{
    MAIDEN_BLOOD_SPAWNED("Maiden blood spawned", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_CRABS_LEAKED("Maiden crabs leaked", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_CRABS_LEAKED_FULL_HP("Maiden crabs leaked full", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_HP_HEALED("Maiden HP Healed", types.OTHER_INT, rooms.MAIDEN),
    MAIDEN_DEFENSE("Maiden defense", types.OTHER_INT, rooms.MAIDEN, 200),
    MAIDEN_DEATHS("Maiden deaths", types.OTHER_INT, rooms.MAIDEN),
    BLOAT_DOWNS("Bloat downs", types.OTHER_INT, rooms.BLOAT),
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
    VERZIK_BOUNCES("Verzik Bounces", types.OTHER_INT, rooms.VERZIK),
    VERZIK_DEATHS("Verzik Deaths", types.OTHER_INT, rooms.VERZIK),
    MAIDEN_70_SPLIT("Maiden 70s Split", types.TIME, rooms.MAIDEN),
    MAIDEN_7050_SPLIT("Maiden 70-50s Split", types.TIME, rooms.MAIDEN),
    MAIDEN_50_SPLIT("Maiden 50s Split", types.TIME, rooms.MAIDEN),
    MAIDEN_5030_SPLIT("Maiden 50-30s Split", types.TIME, rooms.MAIDEN),
    MAIDEN_30_SPLIT("Maiden 30s Split", types.TIME, rooms.MAIDEN),
    MAIDEN_SKIP_SPLIT("Maiden Skip Split", types.TIME, rooms.MAIDEN),
    MAIDEN_TOTAL_TIME("Maiden Room Time", types.TIME, rooms.MAIDEN),
    BLOAT_TOTAL_TIME("Bloat Room Time", types.TIME, rooms.BLOAT),
    BLOAT_FIRST_DOWN_TIME("Bloat 1st Down Time", types.TIME, rooms.BLOAT),
    NYLO_TOTAL_TIME("Nylo Room Time", types.TIME, rooms.NYLOCAS),
    NYLO_BOSS_SPAWN("Nylo Boss Spawn", types.TIME, rooms.NYLOCAS),
    NYLO_BOSS_DURATION("Nylo Boss Duration", types.TIME, rooms.NYLOCAS),
    NYLO_LAST_WAVE("Nylo Last Wave", types.TIME, rooms.NYLOCAS),
    NYLO_CLEANUP("Nylo Cleanup", types.TIME, rooms.NYLOCAS),
    SOTE_TOTAL_TIME("Sote Room Time", types.TIME, rooms.SOTETSEG),
    SOTE_P1_SPLIT("Sote P1 Split", types.TIME, rooms.SOTETSEG),
    SOTE_P2_SPLIT("Sote P2 Split", types.TIME, rooms.SOTETSEG),
    SOTE_P3_SPLIT("Sote P3 Split", types.TIME, rooms.SOTETSEG),
    SOTE_M1_SPLIT("Sote Maze1 Split", types.TIME, rooms.SOTETSEG),
    SOTE_M2_SPLIT("Sote Maze2 Split", types.TIME, rooms.SOTETSEG),
    XARP_TOTAL_TIME("Xarp Room Time", types.TIME, rooms.XARPUS),
    XARP_SCREECH("Xarp Screech", types.TIME, rooms.XARPUS),
    XARP_POST_SCREECH("Xarp Post Screech", types.TIME, rooms.XARPUS),
    VERZIK_TOTAL_TIME("Verzik Room Time", types.TIME, rooms.VERZIK),
    VERZIK_P1_SPLIT("Verzik P1 Split", types.TIME, rooms.VERZIK),
    VERZIK_P2_SPLIT("Verzik P2 Split", types.TIME, rooms.VERZIK),
    VERZIK_P2_DURATION("Verzik P2 Duration", types.TIME, rooms.VERZIK),
    VERZIK_P3_DURATION("Verzik P3 Duration", types.TIME, rooms.VERZIK);

    public static cDataPoint getValue(String s)
    {
        for(cDataPoint point : values())
        {
            if(point.name.equals(s))
            {
                return point;
            }
        }
        return null;
    }

    public static enum rooms
    {
        MAIDEN, BLOAT, NYLOCAS, SOTETSEG, XARPUS, VERZIK
    };

    public static enum types
    {
        OTHER_INT, OTHER_BOOL, TIME
    }

    public final String name;
    public final int value;
    public final types type;
    public final rooms room;


    cDataPoint(String name, types type, rooms room)
    {
        this.name = name;
        this.value = 0;
        this.type = type;
        this.room = room;
    }

    cDataPoint(String name, types type, rooms room, int value)
    {
        this.name = name;
        this.value = value;
        this.type = type;
        this.room = room;
    }

    public static String[] getOtherIntNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for(cDataPoint point : cDataPoint.values())
        {
            if(point.type.equals(types.OTHER_INT))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getByNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for(cDataPoint point : cDataPoint.values())
        {
            valuesToGather.add(point.name);
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getTimeNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for(cDataPoint point : cDataPoint.values())
        {
            if(point.type.equals(types.TIME))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }
    public static ArrayList<String> getTimeNamesByRoom(rooms room)
    {
        ArrayList<String> timesToGather = new ArrayList<>();
        for(cDataPoint point : cDataPoint.values())
        {
            if(point.room.equals(room) && point.type.equals(types.TIME))
            {
                timesToGather.add(point.name);
            }
        }
        return timesToGather;
    }
}
