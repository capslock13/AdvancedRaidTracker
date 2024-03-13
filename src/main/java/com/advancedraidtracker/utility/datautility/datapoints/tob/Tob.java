package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import lombok.Getter;

import java.nio.file.Path;
import java.util.*;

import static com.advancedraidtracker.constants.RaidRoom.*;

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

    private MaidenParser maidenParser = new MaidenParser();
    private BloatParser bloatParser = new BloatParser();
    private NylocasParser nylocasParser = new NylocasParser();
    private SotetsegParser sotetsegParser = new SotetsegParser();
    private XarpusParser xarpusParser = new XarpusParser();
    private VerzikParser verzikParser = new VerzikParser();

    public Tob(Path logfile, List<LogEntry> raidData)
    {
        super(logfile, raidData);
        roomParsers.put(MAIDEN, maidenParser);
        roomParsers.put(BLOAT, bloatParser);
        roomParsers.put(NYLOCAS, nylocasParser);
        roomParsers.put(SOTETSEG, sotetsegParser);
        roomParsers.put(XARPUS, xarpusParser);
        roomParsers.put(VERZIK, verzikParser);
    }

    @Override
    protected boolean parseLogEntry(LogEntry entry)
    {
        super.parseLogEntry(entry);
        return true;
    }

    @Override
    public void parseAllEntries()
    {
        super.parseAllEntries();
    }

    @Override
    public String getRoomStatus()
    {
        return roomStatus;
    }

    /**
     * Checks whether a room has started.
     * @param entry Log entry to compare
     * @return true if it has begun, false if not.
     */
    private boolean didRoomStart(LogID entry)
    {
        return false;
    }


    @Override
    public RaidType getRaidType() {
        return RaidType.TOB;
    }
}
