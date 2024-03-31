package com.advancedraidtracker.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum RaidRoom
{
    MAIDEN(0, "Maiden"), BLOAT(1, "Bloat"), NYLOCAS(2, "Nylocas"), SOTETSEG(3, "Sotetseg"), XARPUS(4, "Xarpus"), VERZIK(5, "Verzik"), UNKNOWN(-1, "Unknown"), ANY_TOB(6, "Any TOB"),
    CRONDIS(7, "Crondis"), ZEBAK(8, "Zebak"), SCABARAS(9, "Scabaras"), KEPHRI(10, "Kephri"), APMEKEN(11, "Apmeken"), BABA(12, "Baba"), HET(13, "Het"), AKKHA(14, "Akkha"), WARDENS(15, "Wardens"), ANY_TOA(16, "Any TOA"),

    TEKTON(19, "Tekton"), CRABS(20, "Crabs"), ICE_DEMON(21, "Ice"), SHAMANS(22, "Shamans"), VANGUARDS(23, "Vanguards"), THIEVING(24, "Thieving"), VESPULA(25, "Vespula"),
    TIGHT_ROPE(26, "Rope"), GUARDIANS(27, "Guardians"), VASA_NISTIRIO(28, "Vasa"), SKELETAL_MYSTICS(29, "Mystics"), MUTTADILES(30, "Muttadiles"), OLM(31, "Olm"),
    COLOSSEUM(32, "Colosseum"),
    INFERNO(33, "Inferno"),
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
        switch(type)
        {
            case COX:
                return Arrays.stream(RaidRoom.values()).filter(RaidRoom::isCOX).collect(Collectors.toList());
            case TOB:
                return Arrays.stream(RaidRoom.values()).filter(RaidRoom::isTOB).collect(Collectors.toList());
            case TOA:
                return Arrays.stream(RaidRoom.values()).filter(RaidRoom::isTOA).collect(Collectors.toList());
            case COLOSSEUM:
                return Arrays.stream(RaidRoom.values()).filter(RaidRoom::isColo).collect(Collectors.toList());
            case UNASSIGNED:
                return Arrays.stream(RaidRoom.values()).filter(RaidRoom::isOther).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public boolean isOther()
    {
        return this.equals(ANY_TOA) || this.equals(ANY_TOB) || this.equals(ANY) || this.equals(ALL);
    }

    public boolean isColo()
    {
        return this.equals(COLOSSEUM);
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

    public static RaidRoom getRoom(String name)
    {
        String compareName = name;
        if(compareName.contains(" "))
        {
            compareName = compareName.substring(0, compareName.length()-3);
        }
        if(compareName.contains("Wave"))
        {
            return COLOSSEUM;
        }
        for(RaidRoom room : RaidRoom.values())
        {
            if(room.name.equals(compareName))
            {
                return room;
            }
        }
        return UNKNOWN;
    }
}
