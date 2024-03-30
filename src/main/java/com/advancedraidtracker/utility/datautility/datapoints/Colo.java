package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.RaidType;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;

import static com.advancedraidtracker.constants.RaidRoom.*;
import static com.advancedraidtracker.constants.RaidRoom.VERZIK;
@Slf4j
public class Colo extends Raid
{
    public Colo(Path filepath, List<LogEntry> raidData)
    {
        super(filepath, raidData);
        roomParsers.put(RaidRoom.COLOSSEUM, new ColosseumParser());
    }

    @Override
    protected boolean parseLogEntry(LogEntry entry)
    {
        log.info("parsing: " + String.join(",", entry.lines));
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
        return red + "Wave " + (wave+1);
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
