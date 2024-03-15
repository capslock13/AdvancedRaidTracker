package com.advancedraidtracker.utility.datautility.datapoints.toa;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.ParseInstruction;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.advancedraidtracker.constants.ParseType.*;
import static com.advancedraidtracker.constants.RaidRoom.*;

public class Toa extends Raid
{
    private ApmekenParser apmekenParser = new ApmekenParser();
    private BabaParser babaParser = new BabaParser();
    private ScabarasParser scabarasParser = new ScabarasParser();
    private KephriParser kephriParser = new KephriParser();
    private HetParser hetParser = new HetParser();
    private AkkhaParser akkhaParser = new AkkhaParser();
    private CrondisParser crondisParser = new CrondisParser();
    private ZebakParser zebakParser = new ZebakParser();
    private WardensParser wardensParser = new WardensParser();

    private ToaParser toaParser = new ToaParser();

    public Toa(Path filepath, List<LogEntry> raidData)
    {
        super(filepath, raidData);
        roomParsers.put(APMEKEN, apmekenParser);
        roomParsers.put(BABA, babaParser);
        roomParsers.put(SCABARAS, scabarasParser);
        roomParsers.put(KEPHRI, kephriParser);
        roomParsers.put(HET, hetParser);
        roomParsers.put(AKKHA, akkhaParser);
        roomParsers.put(CRONDIS, crondisParser);
        roomParsers.put(ZEBAK, zebakParser);
        roomParsers.put(WARDENS, wardensParser);
        roomParsers.put(ANY_TOA, toaParser);
    }

    @Override
    public String getRoomStatus()
    {
        return roomStatus;
    }

    @Override
    public RaidType getRaidType()
    {
        return RaidType.TOA;
    }

    @Override
    protected boolean parseLogEntry(LogEntry entry)
    {
        super.parseLogEntry(entry);
        try
        {
            for (ParseInstruction instruction : entry.logEntry.parseInstructions)
            {
                if (Objects.requireNonNull(instruction.type) == LEFT_RAID)
                {
                    if (wasReset)
                    {
                        if (lastRoom.equals(WARDENS.name))
                        {
                            addColorToStatus(green);
                            completed = true;
                        } else
                        {
                            addColorToStatus(orange);
                        }
                    } else
                    {
                        addColorToStatus(red);
                    }
                }
                else if(Objects.requireNonNull(instruction.type) == RAID_SPECIFIC)
                {
                    if(entry.logEntry.equals(LogID.ENTERED_NEW_TOA_REGION))
                    {
                        if(entry.getValue("Region").equals("TOA Nexus") && !roomStatus.isEmpty())
                        {
                            addColorToStatus(green);
                        }
                    }
                }
                else if(Objects.requireNonNull(instruction.type) == ROOM_START_FLAG)
                {
                    if(entry.logEntry.getRoom().isTOAPath())
                    {
                        roomStatus += roomLetters.get(entry.logEntry.getRoom().name);
                    }
                }
                else if(Objects.requireNonNull(instruction.type) == MANUAL_PARSE)
                {
                    if(entry.logEntry.equals(LogID.TOA_WARDENS_SKULLS_STARTED)) //todo revisit when not cooked //todo pt2 also it just doesnt work
                    {
                        for(int i = 1; i < 5; i++)
                        {
                            if(getParser(WARDENS).data.get(DataPoint.getValue("Wardens Skull " + i + " Split")) < 1)
                            {
                                getParser(WARDENS).data.set(DataPoint.getValue("Wardens Skull " + i + " Split"), Integer.parseInt(entry.getValue("Room Tick")));
                                getParser(WARDENS).data.set(DataPoint.getValue("Wardens Skull " + i + " Duration"), Integer.parseInt(entry.getValue("Room Tick")) - getParser(WARDENS).data.get(DataPoint.getValue("Wardens Skull " + i + " Split")));
                                break;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ignored)
        {

        }
        return true;
    }

    @Override
    public String getScaleString()
    {
        return super.getScaleString() + " (" + (get(DataPoint.TOA_INVOCATION_LEVEL)) + ")";
    }
}
