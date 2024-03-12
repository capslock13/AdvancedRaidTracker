package com.advancedraidtracker.utility.datautility;


import com.advancedraidtracker.constants.RaidRoom;

import static com.advancedraidtracker.constants.RaidRoom.BLOAT;
import static com.advancedraidtracker.constants.RaidRoom.VERZIK;

public enum SingleRoomPlayerDataPoint
{
    PLAYER_FIRST_WALK_SCYTHES("Player First Walk Scythes", BLOAT),
    PLAYER_BOUNCES_VERZIK("Player Verzik Bounces", VERZIK),

    ;
    public String name;
    final RaidRoom room;
    SingleRoomPlayerDataPoint(String time, RaidRoom room)
    {
        this.room = room;
        this.name = time;
    }
}
