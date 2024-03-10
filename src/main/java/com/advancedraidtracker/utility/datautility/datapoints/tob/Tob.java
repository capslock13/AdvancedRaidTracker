package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomDataManager;
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

    public Tob(Path logfile, List<LogEntry> raidData)
    {
        super(logfile, raidData);
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
                case ENTERED_TOB:
                    this.date = new Date(entry.getTs());
                    continue;
                case SPECTATE:
                    spectated = true;
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
                case MAIDEN_DESPAWNED:
                    maidenData = new MaidenData(roomData);
                    break;
                case BLOAT_DESPAWN:
                    bloatData = new BloatData(roomData);
                    break;
                case NYLO_DESPAWNED:
                    nylocasData = new NylocasData(roomData);
                    break;
                case SOTETSEG_ENDED:
                    sotetsegData = new SotetsegData(roomData);
                    break;
                case XARPUS_DESPAWNED:
                    xarpusData = new XarpusData(roomData);
                    break;
                case VERZIK_P3_DESPAWNED:
                    verzikData = new VerzikData(roomData);
                    return;
                default:
                    break;
            }

        }
    }

    @Override
    public String getRoomStatus() {
        return "";
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
                entry == LogID.BLOAT_STARTED ||
                entry == LogID.NYLO_PILLAR_SPAWN ||
                entry == LogID.XARPUS_SPAWNED ||
                entry == LogID.SOTETSEG_STARTED ||
                entry == LogID.VERZIK_P1_START;
    }

    @Override
    public List<RoomDataManager> getAllData()
    {
        return Arrays.asList(maidenData, bloatData, nylocasData, sotetsegData, xarpusData, verzikData);
    }

    @Override
    public RaidType getRaidType() {
        return RaidType.TOB;
    }
}
