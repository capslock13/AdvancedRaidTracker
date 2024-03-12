package com.advancedraidtracker.constants;

import java.util.Arrays;
import java.util.Optional;

public enum RaidRoom
{
    MAIDEN(0), BLOAT(1), NYLOCAS(2), SOTETSEG(3), XARPUS(4), VERZIK(5), UNKNOWN(-1), ANY_TOB(6),
    CRONDIS(7), ZEBAK(8), SCABARAS(9), KEPHRI(10), APMEKEN(11), BABA(12), HET(13), AKKHA(14), WARDENS(15), ANY_TOA(16),
    ANY(17), ALL(18),

    ;

    public final int value;

    RaidRoom(int value)
    {
        this.value = value;
    }

    public static RaidRoom valueOf(int number)
    {
        Optional<RaidRoom> o = Arrays.stream(values()).filter(room -> room.value == number).findFirst();
        return o.orElse(UNKNOWN);
    }


    public static RaidRoom getRoom(String room)
    {
        return RaidRoom.valueOf(room.toUpperCase());
    }
}
