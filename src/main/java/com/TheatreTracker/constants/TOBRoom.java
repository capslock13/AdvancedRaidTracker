package com.TheatreTracker.constants;

import java.util.Arrays;
import java.util.Optional;

public enum TOBRoom
{
    MAIDEN(0), BLOAT(1), NYLOCAS(2), SOTETSEG(3), XARPUS(4), VERZIK(5), UNKNOWN(-1), ANY_TOB(6),
    CRONDIS(7), ZEBAK(8), SCABARAS(9), KEPHRI(10), APMEKEN(11), BABA(12), HET(13), AKKHA(14), WARDENS(15), ANY_TOA(16),
    ANY(17),

    ;

    public final int value;
    TOBRoom(int value)
    {
        this.value = value;
    }

    public static TOBRoom valueOf(int number)
    {
        Optional<TOBRoom> o = Arrays.stream(values()).filter(room -> room.value == number).findFirst();
        return o.orElse(UNKNOWN);
    }
}
