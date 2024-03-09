package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomDataManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
    private boolean spectate;

    @Getter
    private RaidMode mode;

    @Getter
    private MaidenData maidenData;

    @Getter
    private BloatData bloatData;

    @Getter
    private NylocasData nylocasData;

    @Getter
    private SotetsegData sotetsegData;

    @Getter
    private XarpusData xarpusData;

    @Getter
    private VerzikData verzikData;

    public Tob(List<LogEntry> raidData)
    {
        super(raidData);
        parse();
    }

    /**
     * Parses events that are raid specific, anything boss related is offloaded to
     * the room specific handlers.
     */
    @Override
    public void parse()
    {
        // We want the generic class to handle all "generic" events such as party size etc.
        super.parse();
        List<LogEntry> roomData = null;

        for (LogEntry entry : raidData)
        {
            switch (entry.getLogEntry())
            {
                case SPECTATE:
                    spectate = true;
                    continue;

            }

            if (didRoomStart(entry.getLogEntry()))
            {
                roomData = new ArrayList<>();
            }

            if (roomData != null)
            {
                roomData.add(entry);
            }

            switch (entry.getLogEntry())
            {
                case MAIDEN_0HP:
                    maidenData = new MaidenData(roomData);
                    break;
                case BLOAT_0HP:
                    bloatData = new BloatData(roomData);
                    break;
                case NYLO_0HP:
                    nylocasData = new NylocasData(roomData);
                    break;
                case SOTETSEG_ENDED:
                    // TODO
                    break;
                case XARPUS_0HP:
                    // TODO
                    break;
                case VERZIK_P3_0HP:
                    // TODO
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * Checks whether a room has started.
     * @param entry Log entry to compare
     * @return true if it has begun, false if not.
     */
    private boolean didRoomStart(LogID entry)
    {
        // TODO sote
        return  entry == LogID.MAIDEN_SPAWNED ||
                entry == LogID.NYLO_PILLAR_SPAWN ||
                entry == LogID.XARPUS_SPAWNED ||
                entry == LogID.SOTETSEG_STARTED ||
                entry == LogID.VERZIK_P1_START;
    }

    @Override
    public List<RoomDataManager> getAllData()
    {
        return null;
    }
}
