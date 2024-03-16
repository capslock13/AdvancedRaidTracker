package com.advancedraidtracker.utility.datautility.datapoints.toa;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.ParseInstruction;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.advancedraidtracker.constants.ParseType.*;
import static com.advancedraidtracker.constants.RaidRoom.*;
@Slf4j
public class Toa extends Raid
{
    private ApmekenParser apmekenParser = new ApmekenParser();
    private BabaParser babaParser = new BabaParser();
    private ScabarasParser scabarasParser = new ScabarasParser();
    private KephriParser kephriParser = new KephriParser();
    private HetParser hetParser = new HetParser();

    public Toa(Path filepath, List<LogEntry> raidData)
    {
        super(filepath, raidData);
        roomParsers.put(APMEKEN, apmekenParser);
        roomParsers.put(BABA, babaParser);
        roomParsers.put(SCABARAS, scabarasParser);
        roomParsers.put(KEPHRI, kephriParser);
        roomParsers.put(HET, hetParser);
        AkkhaParser akkhaParser = new AkkhaParser();
        roomParsers.put(AKKHA, akkhaParser);
        CrondisParser crondisParser = new CrondisParser();
        roomParsers.put(CRONDIS, crondisParser);
        ZebakParser zebakParser = new ZebakParser();
        roomParsers.put(ZEBAK, zebakParser);
        WardensParser wardensParser = new WardensParser();
        roomParsers.put(WARDENS, wardensParser);
        ToaParser toaParser = new ToaParser();
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
    public int getTimeSum()
    {
        int time = 0;
        for(RaidRoom room : RaidRoom.values())
        {
            if(room.isTOA())
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
            if(room.isTOA())
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
                                getParser(WARDENS).data.set(DataPoint.getValue("Wardens Skull " + i + " Split"), entry.getFirstInt());
                                break;
                            }
                        }
                    }
                    if(entry.logEntry.equals(LogID.TOA_WARDENS_SKULLS_ENDED)) //todo revisit when not cooked //todo pt2 also it just doesnt work
                    {
                        for(int i = 1; i < 5; i++)
                        {
                            if(getParser(WARDENS).data.get(DataPoint.getValue("Wardens Skull " + i + " Duration")) < 1)
                            {
                                getParser(WARDENS).data.set(DataPoint.getValue("Wardens Skull " + i + " Duration"), entry.getFirstInt() - getParser(WARDENS).data.get(DataPoint.getValue("Wardens Skull " + i + " Split")));
                                break;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ignored)
        {
            ignored.printStackTrace();
        }
        return true;
    }

    @Override
    public String getScaleString()
    {
        return super.getScaleString() + " (" + (get(DataPoint.TOA_INVOCATION_LEVEL)) + ")";
    }
}
