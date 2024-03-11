package com.advancedraidtracker.utility.datautility;
import com.advancedraidtracker.utility.datautility.DataPoint.types;
public enum MultiRoomPlayerDataPoint
{
    PLAYER_HAMMER_ATTEMPTED("Hammer Attempted", types.OTHER_INT),
    PLAYER_HAMMER_HIT_COUNT("Hammer Hit", types.OTHER_INT),
    PLAYER_BGS_HIT_COUNT("BGS Hits", types.OTHER_INT),
    PLAYER_BGS_DAMAGE("BGS Damaged", types.OTHER_INT),
    PLAYER_DEATHS("Deaths", types.OTHER_INT),

    ;
    public DataPoint.types type;
    public String time;
    MultiRoomPlayerDataPoint(String time, DataPoint.types type)
    {
        this.type = type;
        this.time = time;
    }
}
