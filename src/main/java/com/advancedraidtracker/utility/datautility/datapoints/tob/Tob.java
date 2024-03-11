package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import lombok.Getter;

import java.nio.file.Path;
import java.util.*;

public class Tob extends Raid
{
    /**
     * Enum for what difficulty the raid is.
     */
    public enum RaidMode
    {
        ENTRY,
        NORMAL,
        HARD
    }

    @Getter
    private boolean spectated;

    @Getter
    private RaidMode mode;

    public Tob(Path logfile, List<LogEntry> raidData)
    {
        super(logfile, raidData);
    }

    @Override
    public String getRoomStatus()
    {
        return "";
    }

    /**
     * Checks whether a room has started.
     * @param entry Log entry to compare
     * @return true if it has begun, false if not.
     */
    private boolean didRoomStart(LogID entry)
    {
        return true;
        // TODO sote
        /*return  entry == LogID.MAIDEN_SPAWNED ||
                entry == LogID.BLOAT_STARTED ||
                entry == LogID.NYLO_PILLAR_SPAWN ||
                entry == LogID.XARPUS_SPAWNED ||
                entry == LogID.SOTETSEG_STARTED ||
                entry == LogID.VERZIK_P1_START;*/
    }


    @Override
    public RaidType getRaidType() {
        return RaidType.TOB;
    }
}
