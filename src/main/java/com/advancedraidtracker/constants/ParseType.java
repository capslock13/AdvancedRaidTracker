package com.advancedraidtracker.constants;

public enum ParseType
{
    UNUSED(0),
    ADD_TO_VALUE(1),
    INCREMENT(1),
    INCREMENT_IF_GREATER_THAN(3),
    INCREMENT_IF_LESS_THAN(3),
    INCREMENT_IF_EQUALS(0),
    DECREMENT(0),
    SET(1),
    SET_IF(0),
    SUM(3),
    SPLIT(3),
    DWH(1),
    BGS(2),
    ROOM_END_FLAG(0),
    ROOM_START_FLAG(0),
    AGNOSTIC(0),
    RAID_SPECIFIC(0),
    ACCURATE_START(0),
    ACCURATE_END(0),
    MANUAL_PARSE(0),
    MAP(0),
    ENTERED_RAID(0),
    LEFT_RAID(0),
    ;
    public final int offset;

    ParseType(int offset)
    {
        this.offset = offset;
    }
}
