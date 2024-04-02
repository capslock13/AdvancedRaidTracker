package com.advancedraidtracker.utility.datautility.datapoints.col;

import com.advancedraidtracker.constants.*;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;

import static com.advancedraidtracker.utility.datautility.DataPoint.CHALLENGE_TIME;
import static com.advancedraidtracker.utility.datautility.DataPoint.COLOSSEUM_WAVE_12_SPLIT;

@Slf4j
public class Colo extends Raid
{
    int highestWaveStarted = 0;
    public Colo(Path filepath, List<LogEntry> raidData)
    {
        super(filepath, raidData);
        roomParsers.put(RaidRoom.COLOSSEUM, new ColosseumParser());
    }

    @Override
    protected boolean parseLogEntry(LogEntry entry)
    {
        for(ParseInstruction instruction : entry.logEntry.parseInstructions)
        {
            if(instruction.type == ParseType.MANUAL_PARSE)
            {
                if(entry.logEntry.equals(LogID.COLOSSEUM_WAVE_STARTED))
                {
                    highestWaveStarted = entry.getFirstInt();
                }
                else if(entry.logEntry.equals(LogID.COLOSSEUM_WAVE_12_END))
                {
                    getParser(CHALLENGE_TIME.room).data.set(CHALLENGE_TIME, entry.getFirstInt());
                    int val = getParser(CHALLENGE_TIME.room).data.get(CHALLENGE_TIME)-get(COLOSSEUM_WAVE_12_SPLIT);
                    getParser(RaidRoom.COLOSSEUM).data.set(DataPoint.COLOSSEUM_WAVE_12_DURATION, val);
                    completed = true;
                }
            }
        }
        return super.parseLogEntry(entry);
    }

    @Override
    public int getTimeSum()
    {
        int sum = 0;
        for(int i = 1; i < 13; i++)
        {
            sum += get("Wave " + i + " Duration");
        }
        return sum;
    }

    @Override
    public String getRoomStatus()
    {
        int wave = 1;
        for(int i = 2; i < 13; i++)
        {
            if(get("Wave " + i + " Duration") > 0)
            {
                wave++;
            }
        }
        if(get("Wave 12 Duration") > 0)
        {
            return green + "Completion";
        }
        if(wave+1 == highestWaveStarted || highestWaveStarted == 0)
        {
            return red + "Wave " + (wave+1);
        }
        return orange + "Wave " + (wave);
    }

    @Override
    public boolean isAccurate()
    {
        return true;
    }

    @Override
    public RaidType getRaidType()
    {
        return RaidType.COLOSSEUM;
    }

    @Override
    public int getChallengeTime()
    {
        return getTimeSum();
    }

    @Override
    public void parseAllEntries()
    {
        super.parseAllEntries();
    }

}
