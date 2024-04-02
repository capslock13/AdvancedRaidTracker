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
    private RaidMode mode;


    public Tob(Path logfile, List<LogEntry> raidData)
    {
        super(logfile, raidData);
    }

    @Override
    public int getChallengeTime()
    {
        return getIfAccurate(MAIDEN_TIME) + getIfAccurate(BLOAT_TIME) + getIfAccurate(NYLOCAS_TIME) + getIfAccurate(SOTETSEG_TIME) + getIfAccurate(XARPUS_TIME) + getIfAccurate(VERZIK_TIME);
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
                        roomStatus = red + currentRoom + " Wipe";
                    }
                }
                else if(Objects.requireNonNull(instruction.type) == MANUAL_PARSE)
                {
                    if(entry.logEntry.equals(LogID.VERZIK_P2_REDS_PROC))
                    {
                        if(data.get(VERZIK_REDS_SPLIT) < 1)
                        {
                            data.set(VERZIK_REDS_SPLIT, entry.getFirstInt());
                            data.set(VERZIK_P2_TILL_REDS, entry.getFirstInt() - data.get(VERZIK_P2_SPLIT));
                        }
                    }
                    else if(entry.logEntry.equals(LogID.CRAB_HEALED_MAIDEN))
                    {
                        int maxHP = 0;
                        switch(getScale())
                        {
                            case 1:
                            case 2:
                            case 3:
                                maxHP = 75;
                                break;
                            case 4:
                                maxHP = 87;
                                break;
                            case 5:
                                maxHP = 100;
                                break;
                        }
                        if(Integer.parseInt(entry.getValue("Damage")) == maxHP)
                        {
                            data.increment(MAIDEN_CRABS_LEAKED_FULL_HP);
                        }
                    }
                    else if(entry.logEntry.equals(LogID.PLAYER_DIED))
                    {
                        if(data.get(MAIDEN_TIME) > 0 && data.getList(BLOAT_DOWNS).isEmpty())
                        {
                            data.increment(BLOAT_FIRST_WALK_DEATHS, entry.getValue("Player"));
                        }
                    }
                    else if(entry.logEntry.equals(LogID.BLOAT_DESPAWN))
                    {
                        if(data.get(BLOAT_DOWNS) > 0)
                        {
                            data.set(BLOAT_FIRST_DOWN_TIME, data.getList(BLOAT_DOWNS).get(0));
                        }
                    }
                    else if(entry.logEntry.equals(LogID.BLOAT_HP_1ST_DOWN))
                    {
                        data.set(DataPoint.BLOAT_HP_FIRST_DOWN, Integer.parseInt(entry.getValue("Bloat HP")) / 10);
                    }
                    else if(entry.logEntry.equals(LogID.XARPUS_HEAL))
                    {
                        int healAmount = 0;
                        switch(getScale())
                        {
                            case 1:
                                healAmount = 20;
                                break;
                            case 2:
                                healAmount = 16;
                                break;
                            case 3:
                                healAmount = 12;
                                break;
                            case 4:
                                healAmount = 9;
                                break;
                            case 5:
                                healAmount = 8;
                                break;
                        }
                        data.incrementBy(XARP_HEALING, healAmount);
                    }
                    else if(entry.logEntry.equals(LogID.BLOAT_SCYTHE_1ST_WALK))
                    {
                        if(data.get(BLOAT_DOWNS) == 0)
                        {
                            data.increment(BLOAT_FIRST_WALK_SCYTHES, entry.getValue("Player"));
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
