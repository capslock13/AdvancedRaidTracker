package com.advancedraidtracker.utility.datautility.datapoints.inf;

import com.advancedraidtracker.constants.*;
import com.advancedraidtracker.rooms.inf.InfernoHandler;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;

import static com.advancedraidtracker.rooms.inf.InfernoHandler.roomMap;

@Slf4j
public class Inf extends Raid
{
    int highestWaveStarted = 0;
    int lastCheckPoint = 0;
    int endTime = 0;
    int startTime = 0;
    public Inf(Path filepath, List<LogEntry> raidData)
    {
        super(filepath, raidData);
    }

    @Override
    protected boolean parseLogEntry(LogEntry entry)
    {
        for(ParseInstruction instruction : entry.logEntry.getParseInstructions())
        {
            if(instruction.type == ParseType.MANUAL_PARSE)
            {
                if(entry.logEntry.equals(LogID.INFERNO_WAVE_STARTED))
                {
                    highestWaveStarted = entry.getFirstInt();
                    lastCheckPoint = InfernoHandler.getLastRelevantSplit(highestWaveStarted);
                    currentRoom = roomMap.get(lastCheckPoint);
                }
                else if(entry.logEntry.equals(LogID.INFERNO_TIMER_STARTED))
                {
                    startTime = entry.getValueAsInt("Client Tick");
                }
                else if(entry.logEntry.equals(LogID.INFERNO_WAVE_ENDED))
                {
                    if(entry.getValueAsInt("Wave Number") == 69)
                    {
                        endTime = entry.getValueAsInt("Room Tick");
                        completed = true;
                    }
                }
            }
            else if(instruction.type == ParseType.LEFT_RAID)
            {
                if(entry.logEntry.equals(LogID.LEFT_TOB))
                {
                    if(endTime == 0)
                    {
                        endTime = entry.getValueAsInt("Last Room Tick");
                    }
                    if(!completed)
                    {
                        data.set(DataPoint.CHALLENGE_TIME, endTime - startTime-1);
                    }
                    else
                    {
                        data.set(DataPoint.CHALLENGE_TIME, endTime);
                    }
                }
            }
        }
        return super.parseLogEntry(entry);
    }

    @Override
    public String getSplits()
    {
        String split = "";
        for(Integer val : roomMap.keySet())
        {
            if(val > 1)
            {
                int i = data.getList(DataPoint.INFERNO_WAVE_STARTS).get(val)-startTime;
                split += "Wave " + val + ", Split: " + i + "\n";
            }
        }
        return split;
    }

    @Override
    public boolean isAccurate()
    {
        return true;
    }

    @Override
    public int getTimeSum()
    {
        if(!completed)
        {
            return endTime - startTime;
        }
        else
        {
            return endTime;
        }
    }

    @Override
    public String getRoomStatus()
    {
        if(!completed)
        {
            return red + "Wave " + highestWaveStarted;
        }
        else
        {
            return green + "Completion";
        }
    }

    @Override
    public RaidType getRaidType()
    {
        return RaidType.INFERNO;
    }

    @Override
    public int getChallengeTime()
    {
        return getTimeSum();
    }
}
