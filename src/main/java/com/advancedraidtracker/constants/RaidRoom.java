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
    COLOSSEUM(32, "Colosseum"), WAVE_1_COL(101, "Col Wave 1"), WAVE_2_COL(102, "Col Wave 2"), WAVE_3_COL(103, "Col Wave 3"), WAVE_4_COL(104, "Col Wave 4"),
    WAVE_5_COL(105, "Col Wave 5"), WAVE_6_COL(106, "Col Wave 6"), WAVE_7_COL(107, "Col Wave 7"), WAVE_8_COL(108, "Col Wave 8"), WAVE_9_COL(109, "Col Wave 9"),
    WAVE_10_COL(110, "Col Wave 10"), WAVE_11_COL(111, "Col Wave 11"), WAVE_12_COL(112, "Col Wave 12"),
    INFERNO(33, "Inferno"), //done this way instead of map/list/other because of how the UI generates itself based on rooms
    WAVE_1_INF(201, "Inf Wave 1"), WAVE_2_INF(202, "Inf Wave 2"), WAVE_3_INF(203, "Inf Wave 3"), WAVE_4_INF(204, "Inf Wave 4"),
    WAVE_5_INF(205, "Inf Wave 5"), WAVE_6_INF(206, "Inf Wave 6"), WAVE_7_INF(207, "Inf Wave 7"), WAVE_8_INF(208, "Inf Wave 8"),
    WAVE_9_INF(209, "Inf Wave 9"), WAVE_10_INF(210, "Inf Wave 10"), WAVE_11_INF(211, "Inf Wave 11"), WAVE_12_INF(212, "Inf Wave 12"),
    WAVE_13_INF(213, "Inf Wave 13"), WAVE_14_INF(214, "Inf Wave 14"), WAVE_15_INF(215, "Inf Wave 15"), WAVE_16_INF(216, "Inf Wave 16"),
    WAVE_17_INF(217, "Inf Wave 17"), WAVE_18_INF(218, "Inf Wave 18"), WAVE_19_INF(219, "Inf Wave 19"), WAVE_20_INF(220, "Inf Wave 20"),
    WAVE_21_INF(221, "Inf Wave 21"), WAVE_22_INF(222, "Inf Wave 22"), WAVE_23_INF(223, "Inf Wave 23"), WAVE_24_INF(224, "Inf Wave 24"),
    WAVE_25_INF(225, "Inf Wave 25"), WAVE_26_INF(226, "Inf Wave 26"), WAVE_27_INF(227, "Inf Wave 27"), WAVE_28_INF(228, "Inf Wave 28"),
    WAVE_29_INF(229, "Inf Wave 29"), WAVE_30_INF(230, "Inf Wave 30"), WAVE_31_INF(231, "Inf Wave 31"), WAVE_32_INF(232, "Inf Wave 32"),
    WAVE_33_INF(233, "Inf Wave 33"), WAVE_34_INF(234, "Inf Wave 34"), WAVE_35_INF(235, "Inf Wave 35"), WAVE_36_INF(236, "Inf Wave 36"),
    WAVE_37_INF(237, "Inf Wave 37"), WAVE_38_INF(238, "Inf Wave 38"), WAVE_39_INF(239, "Inf Wave 39"), WAVE_40_INF(240, "Inf Wave 40"),
    WAVE_41_INF(241, "Inf Wave 41"), WAVE_42_INF(242, "Inf Wave 42"), WAVE_43_INF(243, "Inf Wave 43"), WAVE_44_INF(244, "Inf Wave 44"),
    WAVE_45_INF(245, "Inf Wave 45"), WAVE_46_INF(246, "Inf Wave 46"), WAVE_47_INF(247, "Inf Wave 47"), WAVE_48_INF(248, "Inf Wave 48"),
    WAVE_49_INF(249, "Inf Wave 49"), WAVE_50_INF(250, "Inf Wave 50"), WAVE_51_INF(251, "Inf Wave 51"), WAVE_52_INF(252, "Inf Wave 52"),
    WAVE_53_INF(253, "Inf Wave 53"), WAVE_54_INF(254, "Inf Wave 54"), WAVE_55_INF(255, "Inf Wave 55"), WAVE_56_INF(256, "Inf Wave 56"),
    WAVE_57_INF(257, "Inf Wave 57"), WAVE_58_INF(258, "Inf Wave 58"), WAVE_59_INF(259, "Inf Wave 59"), WAVE_60_INF(260, "Inf Wave 60"),
    WAVE_61_INF(261, "Inf Wave 61"), WAVE_62_INF(262, "Inf Wave 62"), WAVE_63_INF(263, "Inf Wave 63"), WAVE_64_INF(264, "Inf Wave 64"),
    WAVE_65_INF(265, "Inf Wave 65"), WAVE_66_INF(266, "Inf Wave 66"), WAVE_67_INF(267, "Inf Wave 67"), WAVE_68_INF(268, "Inf Wave 68"),
    WAVE_69_INF(269, "Inf Wave 69"),
    /*WAVE_1_8_INF(201, "Wave 1-8"),
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
    WAVE_69_INF(214, "Wave 69"),*/
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
        return this.equals(INFERNO) || this.equals(WAVE_1_INF) || this.equals(WAVE_2_INF) || this.equals(WAVE_3_INF) ||
                this.equals(WAVE_4_INF) || this.equals(WAVE_5_INF) || this.equals(WAVE_6_INF) || this.equals(WAVE_7_INF) ||
                this.equals(WAVE_8_INF) || this.equals(WAVE_9_INF) || this.equals(WAVE_10_INF) || this.equals(WAVE_11_INF) ||
                this.equals(WAVE_12_INF) || this.equals(WAVE_13_INF) || this.equals(WAVE_14_INF) || this.equals(WAVE_15_INF) ||
                this.equals(WAVE_16_INF) || this.equals(WAVE_17_INF) || this.equals(WAVE_18_INF) || this.equals(WAVE_19_INF) ||
                this.equals(WAVE_20_INF) || this.equals(WAVE_21_INF) || this.equals(WAVE_22_INF) || this.equals(WAVE_23_INF) ||
                this.equals(WAVE_24_INF) || this.equals(WAVE_25_INF) || this.equals(WAVE_26_INF) || this.equals(WAVE_27_INF) ||
                this.equals(WAVE_28_INF) || this.equals(WAVE_29_INF) || this.equals(WAVE_30_INF) || this.equals(WAVE_31_INF) ||
                this.equals(WAVE_32_INF) || this.equals(WAVE_33_INF) || this.equals(WAVE_34_INF) || this.equals(WAVE_35_INF) ||
                this.equals(WAVE_36_INF) || this.equals(WAVE_37_INF) || this.equals(WAVE_38_INF) || this.equals(WAVE_39_INF) ||
                this.equals(WAVE_40_INF) || this.equals(WAVE_41_INF) || this.equals(WAVE_42_INF) || this.equals(WAVE_43_INF) ||
                this.equals(WAVE_44_INF) || this.equals(WAVE_45_INF) || this.equals(WAVE_46_INF) || this.equals(WAVE_47_INF) ||
                this.equals(WAVE_48_INF) || this.equals(WAVE_49_INF) || this.equals(WAVE_50_INF) || this.equals(WAVE_51_INF) ||
                this.equals(WAVE_52_INF) || this.equals(WAVE_53_INF) || this.equals(WAVE_54_INF) || this.equals(WAVE_55_INF) ||
                this.equals(WAVE_56_INF) || this.equals(WAVE_57_INF) || this.equals(WAVE_58_INF) || this.equals(WAVE_59_INF) ||
                this.equals(WAVE_60_INF) || this.equals(WAVE_61_INF) || this.equals(WAVE_62_INF) || this.equals(WAVE_63_INF) ||
                this.equals(WAVE_64_INF) || this.equals(WAVE_65_INF) || this.equals(WAVE_66_INF) || this.equals(WAVE_67_INF) ||
                this.equals(WAVE_68_INF) || this.equals(WAVE_69_INF);
    }

    //todo: Why did I not do the following methods based on a parameter of the enum..?

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
            if (room.name.equals(name) && !room.isColo())
            {
                roomCache.put(name, room);
                return room;
            }
        }
        roomCache.put(name, UNKNOWN);
        return UNKNOWN;
    }
}
