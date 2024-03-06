package com.advancedraidtracker.constants;

import java.util.Arrays;
import java.util.Optional;

public enum TOBRoom
{
    MAIDEN(0), BLOAT(1), NYLOCAS(2), SOTETSEG(3), XARPUS(4), VERZIK(5), UNKNOWN(-1), ANY(6);

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
