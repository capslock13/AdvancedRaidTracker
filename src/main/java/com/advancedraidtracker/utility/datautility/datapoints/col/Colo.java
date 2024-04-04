package com.advancedraidtracker.utility.datautility.datapoints.col;

import com.advancedraidtracker.constants.*;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.advancedraidtracker.utility.datautility.DataPoint.CHALLENGE_TIME;
import static com.advancedraidtracker.utility.datautility.DataPoint.COLOSSEUM_WAVE_12_SPLIT;

@Slf4j
public class Colo extends Raid
{
    public int highestWaveStarted = 0;
    public Colo(Path filepath, List<LogEntry> raidData)
    {
        super(filepath, raidData);
    }
    private Map<Integer, String> waveSpawnMap = new HashMap<>();
    public Map<Integer, String> invocationSelected = new HashMap<>();
    public Multimap<Integer, String> invocationsOffered = ArrayListMultimap.create();

    @Override
    public String getSplits()
    {
        StringBuilder split = new StringBuilder();
        for(int i = 1; i < 13; i++)
        {
            if(i == 1)
            {
                if(get("Wave " + i + " Duration") > 0)
                {
                    split.append("Wave: ").append(i).append(", Split: ").append(RoomUtil.time(get("Wave " + i + " Duration"))).append(" (+").append(RoomUtil.time(get("Wave " + i + " Duration"))).append(")").append("\n");
                }
            }
            else if(get("Wave " + i + " Split") > 0 && i > 2)
            {
                split.append("Wave: ").append(i - 1).append(", Split: ").append(RoomUtil.time(get("Wave " + i + " Split"))).append(" (+").append(RoomUtil.time(get("Wave " + i + " Duration"))).append(")").append("\n");
            }
        }
        if(completed)
        {
            split.append("Duration (Success): ").append(RoomUtil.time(getChallengeTime())).append(" (+").append(RoomUtil.time(getChallengeTime() - get("Wave 12 Split"))).append(")");
        }
        return split.toString();
    }

    @Override
    protected boolean parseLogEntry(LogEntry entry)
    {
        if(entry.logEntry.equals(LogID.ROOM_PRAYER_DRAINED))
        {
            log.info("hmm");
        }
        for(ParseInstruction instruction : entry.logEntry.parseInstructions)
        {
            if(instruction.type == ParseType.MANUAL_PARSE)
            {
                if(entry.logEntry.equals(LogID.COLOSSEUM_WAVE_STARTED))
                {
                    highestWaveStarted = entry.getFirstInt();
                    currentRoom = "Wave " + highestWaveStarted;
                }
                else if(entry.logEntry.equals(LogID.COLOSSEUM_WAVE_12_END))
                {
                    data.set(CHALLENGE_TIME, entry.getFirstInt());
                    int val = data.get(CHALLENGE_TIME)-get(COLOSSEUM_WAVE_12_SPLIT);
                    data.set(DataPoint.COLOSSEUM_WAVE_12_DURATION, val);
                    completed = true;
                }
                else if(entry.logEntry.equals(LogID.COLOSSEUM_SPAWN_STRING))
                {
                    waveSpawnMap.put(Integer.parseInt(entry.getValue("Wave Number")), entry.getValue("Spawn String"));
                }
                else if(entry.logEntry.equals(LogID.COLOSSEUM_INVOCATION_SELECTED))
                {
                    invocationSelected.put(highestWaveStarted+1, entry.getValue("Invocation"));
                }
                else if(entry.logEntry.equals(LogID.COLOSSEUM_INVOCATION_CHOICES)) //todo refactor
                {
                    if(!invocationsOffered.get(highestWaveStarted+1).contains(entry.getValue("Invocation 1")))
                    {
                        invocationsOffered.put(highestWaveStarted + 1, entry.getValue("Invocation 1"));
                    }
                    if(!invocationsOffered.get(highestWaveStarted+1).contains(entry.getValue("Invocation 2")))
                    {
                        invocationsOffered.put(highestWaveStarted + 1, entry.getValue("Invocation 2"));
                    }
                    if(!invocationsOffered.get(highestWaveStarted+1).contains(entry.getValue("Invocation 3")))
                    {
                        invocationsOffered.put(highestWaveStarted + 1, entry.getValue("Invocation 3"));
                    }
                }
            }
        }
        return super.parseLogEntry(entry);
    }

    public String getSpawnString(int wave)
    {
        return waveSpawnMap.getOrDefault(wave, "");
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
