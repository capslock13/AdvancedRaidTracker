package com.advancedraidtracker.constants;

import lombok.RequiredArgsConstructor;

public enum ParseType
{
    UNUSED(0),
    ADD_TO_VALUE(1),
    INCREMENT(1),
    INCREMENT_IF_GREATER_THAN(3),
    INCREMENT_IF_LESS_THAN(3),
    SET(1),
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
    ;
    public final int offset;
    ParseType(int offset)
    {
        this.offset = offset;
    }
}
