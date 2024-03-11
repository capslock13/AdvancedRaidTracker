package com.advancedraidtracker.utility.datautility;
import com.advancedraidtracker.utility.datautility.DataPoint.rooms;

import static com.advancedraidtracker.utility.datautility.DataPoint.rooms.BLOAT;
import static com.advancedraidtracker.utility.datautility.DataPoint.rooms.VERZIK;

public enum SingleRoomPlayerDataPoint
{
    PLAYER_FIRST_WALK_SCYTHES("Player First Walk Scythes", BLOAT),
    PLAYER_BOUNCES_VERZIK("Player Verzik Bounces", VERZIK),

    ;
    public String name;
    rooms room;
    SingleRoomPlayerDataPoint(String time, rooms room)
    {
        this.room = room;
        this.name = time;
    }
}
