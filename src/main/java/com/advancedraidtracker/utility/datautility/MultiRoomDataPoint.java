package com.advancedraidtracker.utility.datautility;

import static com.advancedraidtracker.utility.datautility.DataPoint.types;

public enum MultiRoomDataPoint
{
    ROOM_TIME("Time", types.TIME),
    ROOM_DEFENSE("Defense", types.OTHER_INT),


    ;
    public types type;
    public String name;
    MultiRoomDataPoint(String name, DataPoint.types type)
    {
        this.type = type;
        this.name = name;
    }
}
