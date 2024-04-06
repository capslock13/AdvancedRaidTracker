package com.advancedraidtracker.constants;

import java.util.*;
import java.util.stream.Collectors;

import static com.advancedraidtracker.rooms.inf.InfernoHandler.roomMap;

public enum RaidRoom
{
    MAIDEN(0, "Maiden"), BLOAT(1, "Bloat"), NYLOCAS(2, "Nylocas"), SOTETSEG(3, "Sotetseg"), XARPUS(4, "Xarpus"), VERZIK(5, "Verzik"), UNKNOWN(-1, "Unknown"), ANY_TOB(6, "Any TOB"),
    CRONDIS(7, "Crondis"), ZEBAK(8, "Zebak"), SCABARAS(9, "Scabaras"), KEPHRI(10, "Kephri"), APMEKEN(11, "Apmeken"), BABA(12, "Baba"), HET(13, "Het"), AKKHA(14, "Akkha"), WARDENS(15, "Wardens"), ANY_TOA(16, "Any TOA"),

    TEKTON(19, "Tekton"), CRABS(20, "Crabs"), ICE_DEMON(21, "Ice"), SHAMANS(22, "Shamans"), VANGUARDS(23, "Vanguards"), THIEVING(24, "Thieving"), VESPULA(25, "Vespula"),
    TIGHT_ROPE(26, "Rope"), GUARDIANS(27, "Guardians"), VASA_NISTIRIO(28, "Vasa"), SKELETAL_MYSTICS(29, "Mystics"), MUTTADILES(30, "Muttadiles"), OLM(31, "Olm"),
    COLOSSEUM(32, "Colosseum"),
    WAVE_1_COL(101, "Wave 1"),
    WAVE_2_COL(102, "Wave 2"),
    WAVE_3_COL(103, "Wave 3"),
    WAVE_4_COL(104, "Wave 4"),
    WAVE_5_COL(105, "Wave 5"),
    WAVE_6_COL(106, "Wave 6"),
    WAVE_7_COL(107, "Wave 7"),
    WAVE_8_COL(108, "Wave 8"),
    WAVE_9_COL(109, "Wave 9"),
    WAVE_10_COL(110, "Wave 10"),
    WAVE_11_COL(111, "Wave 11"),
    WAVE_12_COL(112, "Wave 12"),
    INFERNO(33, "Inferno"),
    WAVE_1_8_INF(201, "Wave 1-8"),
    WAVE_9_17_INF(202, "Wave 9-17"),
    WAVE_18_24_INF(203, "Wave 18-24"),
    WAVE_25_34_INF(204, "Wave 25-34"),
    WAVE_35_41_INF(205, "Wave 35-41"),
    WAVE_42_49_INF(206, "Wave 42-49"),
    WAVE_50_56_INF(207, "Wave 50-56"),
    WAVE_57_59_INF(208, "Wave 57-59"),
    WAVE_60_62_INF(209, "Wave 60-62"),
    WAVE_63_65_INF(210, "Wave 63-65"),
    WAVE_66_INF(211, "Wave 66"),
    WAVE_67_INF(212, "Wave 67"),
    WAVE_68_INF(213, "Wave 68"),
    WAVE_69_INF(214, "Wave 69"),
    ANY(17, "Any"), ALL(18, "All"),

    ;

    public final int value;
    public final String name;

    RaidRoom(int value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public static RaidRoom valueOf(int number)
    {
        Optional<RaidRoom> o = Arrays.stream(values()).filter(room -> room.value == number).findFirst();
        return o.orElse(UNKNOWN);
    }

    public static List<RaidRoom> getRaidRoomsForRaidType(RaidType type)
    {
        switch (type)
        {
            case COX:
                return Arrays.stream(RaidRoom.values()).filter(RaidRoom::isCOX).collect(Collectors.toList());
            case TOB:
                return Arrays.stream(RaidRoom.values()).filter(RaidRoom::isTOB).collect(Collectors.toList());
            case TOA:
                return Arrays.stream(RaidRoom.values()).filter(RaidRoom::isTOA).collect(Collectors.toList());
            case COLOSSEUM:
                return Arrays.stream(RaidRoom.values()).filter(RaidRoom::isColo).collect(Collectors.toList());
            case INFERNO:
                return Arrays.stream(RaidRoom.values()).filter(RaidRoom::isInferno).collect(Collectors.toList());
            case ALL:
                return Arrays.stream(RaidRoom.values()).filter(RaidRoom::isOther).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public boolean isOther()
    {
        return this.equals(ANY_TOA) || this.equals(ANY_TOB) || this.equals(ANY) || this.equals(ALL);
    }

    public RaidType getRaidType()
    {
        if (isTOB())
        {
            return RaidType.TOB;
        } else if (isTOA())
        {
            return RaidType.TOA;
        } else if (isCOX())
        {
            return RaidType.COX;
        } else if (isInferno())
        {
            return RaidType.INFERNO;
        } else if (isColo())
        {
            return RaidType.COLOSSEUM;
        }
        return RaidType.ALL;
    }

    public boolean isColo()
    {
        return this.equals(COLOSSEUM) || this.equals(WAVE_1_COL) || this.equals(WAVE_2_COL) || this.equals(WAVE_3_COL) || this.equals(WAVE_4_COL)
                || this.equals(WAVE_5_COL) || this.equals(WAVE_6_COL) || this.equals(WAVE_7_COL) || this.equals(WAVE_8_COL) || this.equals(WAVE_9_COL)
                || this.equals(WAVE_10_COL) || this.equals(WAVE_11_COL) || this.equals(WAVE_12_COL);
    }

    public boolean isInferno()
    {
        return this.equals(INFERNO) || this.equals(WAVE_1_8_INF) || this.equals(WAVE_9_17_INF) || this.equals(WAVE_18_24_INF) ||
                this.equals(WAVE_25_34_INF) || this.equals(WAVE_35_41_INF) || this.equals(WAVE_42_49_INF) || this.equals(WAVE_50_56_INF) ||
                this.equals(WAVE_57_59_INF) || this.equals(WAVE_60_62_INF) || this.equals(WAVE_63_65_INF) || this.equals(WAVE_66_INF) ||
                this.equals(WAVE_67_INF) || this.equals(WAVE_68_INF) || this.equals(WAVE_69_INF);
    }

    public boolean isTOA()
    {
        return this.equals(CRONDIS) || this.equals(ZEBAK) || this.equals(SCABARAS) || this.equals(KEPHRI) || this.equals(APMEKEN) || this.equals(BABA) || this.equals(HET) || this.equals(AKKHA) || this.equals(WARDENS)/* || this.equals(ANY_TOA)*/; //todo investigate why this was here
    }

    public boolean isTOAPath()
    {
        return this.equals(CRONDIS) || this.equals(SCABARAS) || this.equals(APMEKEN) || this.equals(HET) || this.equals(WARDENS);
    }

    public boolean isTOB()
    {
        return this.equals(MAIDEN) || this.equals(BLOAT) || this.equals(NYLOCAS) || this.equals(SOTETSEG) || this.equals(XARPUS) || this.equals(VERZIK);
    }

    public boolean isCOX()
    {
        return this.value >= TEKTON.value && this.value <= OLM.value;
        //return this.equals(TEKTON) || this.equals(CRABS) || this.equals(ICE_DEMON) || this.equals(SHAMANS) || this.equals(VANGUARDS) || this.equals(THIEVING) ||
        //      this.equals(VESPULA) || this.equals(TIGHT_ROPE) || this.equals(GUARDIANS) || this.equals(VASA_NISTIRIO) || this.equals(SKELETAL_MYSTICS) || this.equals(MUTTADILES) || this.equals(OLM);
    }

    public static Map<RaidRoom, Integer> firstTickMap = Map.of(
            MAIDEN, 5,
            BLOAT, 2,
            NYLOCAS, 5,
            SOTETSEG, 5, //todo mazes...
            XARPUS, 0, //todo ye ye
            VERZIK, 0 //p1

    );

    public int getFirstAttackTick(int scale)
    {
        return 1;
    }

    public static Map<String, RaidRoom> roomCache = new HashMap<>();

    public static RaidRoom getRoom(String name)
    {
        if (name.contains("Verzik"))
        {
            return VERZIK;
        }
        if (name.contains("Wardens"))
        {
            return WARDENS;
        }
        if (roomCache.containsKey(name))
        {
            return roomCache.get(name);
        }
        for (RaidRoom room : RaidRoom.values())
        {
            if (room.name.equals(name))
            {
                roomCache.put(name, room);
                return room;
            }
        }
        roomCache.put(name, UNKNOWN);
        return UNKNOWN;
    }
}
