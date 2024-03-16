package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.ParseInstruction;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;
import lombok.Getter;

import java.nio.file.Path;
import java.util.*;

import static com.advancedraidtracker.constants.ParseType.*;
import static com.advancedraidtracker.constants.RaidRoom.*;
import static com.advancedraidtracker.utility.datautility.DataPoint.*;

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
    public boolean isAccurate()
    {
        return (getRoomAccurate(MAIDEN) && getRoomAccurate(BLOAT) && getRoomAccurate(NYLOCAS) && getRoomAccurate(SOTETSEG) && getRoomAccurate(XARPUS) && getRoomAccurate(VERZIK));
    }

    @Override
    public void parseAllEntries()
    {
        super.parseAllEntries();
    }

    @Override
    protected boolean parseLogEntry(LogEntry entry)
    {
        try
        {
            for (ParseInstruction instruction : entry.logEntry.parseInstructions)
            {
                if (Objects.requireNonNull(instruction.type) == LEFT_RAID)
                {
                    if (wasReset)
                    {
                        if (lastRoom.equals(VERZIK.name) || lastRoom.equals(WARDENS.name))
                        {
                            roomStatus = green + "Completion";
                            completed = true;
                        } else
                        {
                            roomStatus = yellow + lastRoom + " Reset";
                        }
                    } else
                    {
                        roomStatus = red + lastRoom + " Wipe";
                    }
                }
                else if(Objects.requireNonNull(instruction.type) == MANUAL_PARSE)
                {
                    if(entry.logEntry.equals(LogID.VERZIK_P2_REDS_PROC))
                    {
                        RoomParser parser = getParser(VERZIK);
                        if(parser.data.get(VERZIK_REDS_SPLIT) < 1)
                        {
                            parser.data.set(VERZIK_REDS_SPLIT, entry.getFirstInt());
                            parser.data.set(VERZIK_P2_TILL_REDS, entry.getFirstInt() - parser.data.get(VERZIK_P2_SPLIT));
                        }
                    }
                }
            }
        }
        catch (Exception ignored)
        {

        }
        return super.parseLogEntry(entry);
    }

    @Override
    public int getTimeSum()
    {
        int time = 0;
        for(RaidRoom room : RaidRoom.values())
        {
            if(room.isTOB())
            {
                if(getRoomAccurate(room))
                {
                    int val = get(room.name + " Time");
                    time += (val == -1) ? 0 : val;
                }
            }
        }
        return time;
    }

    @Override
    public boolean getOverallTimeAccurate()
    {
        for(RaidRoom room : RaidRoom.values())
        {
            if(room.isTOB())
            {
                if(!getRoomAccurate(room))
                {
                    return false;
                }
            }
        }
        return true;
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
