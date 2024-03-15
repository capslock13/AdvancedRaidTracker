package com.advancedraidtracker.constants;

import java.util.Arrays;
import java.util.Optional;

public enum RaidRoom
{
    MAIDEN(0, "Maiden"), BLOAT(1, "Bloat"), NYLOCAS(2, "Nylocas"), SOTETSEG(3, "Sotetseg"), XARPUS(4, "Xarpus"), VERZIK(5, "Verzik"), UNKNOWN(-1, "Unknown"), ANY_TOB(6, "Any TOB"),
    CRONDIS(7, "Crondis"), ZEBAK(8, "Zebak"), SCABARAS(9, "Scabaras"), KEPHRI(10, "Kephri"), APMEKEN(11, "Apmeken"), BABA(12, "Baba"), HET(13, "Het"), AKKHA(14, "Akkha"), WARDENS(15, "Wardens"), ANY_TOA(16, "Any TOA"),
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

    public boolean isTOA()
    {
        return this.equals(CRONDIS) || this.equals(ZEBAK) || this.equals(SCABARAS) || this.equals(KEPHRI) || this.equals(APMEKEN) || this.equals(BABA) || this.equals(HET) || this.equals(AKKHA) || this.equals(WARDENS) || this.equals(ANY_TOA);
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
        return false;
    }

    public static RaidRoom getRoom(String name)
    {
        String compareName = name;
        if(compareName.contains(" "))
        {
            compareName = compareName.substring(0, compareName.length()-3);
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
