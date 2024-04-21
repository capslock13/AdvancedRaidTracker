package com.advancedraidtracker.utility.datautility.datapoints.inf;

import com.advancedraidtracker.constants.*;
import com.advancedraidtracker.rooms.inf.InfernoHandler;
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

import static com.advancedraidtracker.rooms.inf.InfernoHandler.roomMap;

@Slf4j
public class Inf extends Raid
{
    public int highestWaveStarted = 0;
    int lastCheckPoint = 0;
    int endTime = 0;
    int startTime = 0;
    public Multimap<Integer, InfernoSpawn> spawns = ArrayListMultimap.create();
    public Map<Integer, Integer> waveStarts = new HashMap<>();

    public Inf(Path filepath, List<LogEntry> raidData)
    {
        super(filepath, raidData);
    }

    @Override
    protected boolean parseLogEntry(LogEntry entry)
    {
        for (ParseInstruction instruction : entry.logEntry.getParseInstructions())
        {
            if (instruction.type == ParseType.MANUAL_PARSE)
            {
                if (entry.logEntry.equals(LogID.INFERNO_WAVE_STARTED))
                {
                    highestWaveStarted = entry.getFirstInt();
                    lastCheckPoint = InfernoHandler.getLastRelevantSplit(highestWaveStarted);
                    currentRoom = "Inf Wave " + highestWaveStarted;
                    waveStarts.put(highestWaveStarted, entry.getValueAsInt("Client Tick")-startTime);
					data.set("Inf Wave " + entry.getValueAsInt("Wave Number") + " Split", entry.getValueAsInt("Client Tick")-startTime);
                }
                else if (entry.logEntry.equals(LogID.INFERNO_TIMER_STARTED))
                {
                    startTime = entry.getValueAsInt("Client Tick");
                } else if (entry.logEntry.equals(LogID.INFERNO_WAVE_ENDED))
                {
                    if (entry.getValueAsInt("Wave Number") == 69)
                    {
                        endTime = entry.getValueAsInt("Room Tick");
                        data.set("Inf Wave " + entry.getValueAsInt("Wave Number") + " Duration", 1);
                        completed = true;
                    }
                    else
                    {
                        data.set("Inf Wave " + entry.getValueAsInt("Wave Number") + " Duration", entry.getValueAsInt("Room Tick"));
                    }
                }
                else if(entry.logEntry.equals(LogID.INFERNO_NPC_SPAWN))
                {
                    int id = entry.getValueAsInt("Npc ID");
                    int regionX = entry.getValueAsInt("RegionX");
                    int regionY = entry.getValueAsInt("RegionY");
                    boolean respawn = (entry.getValue("Respawn?")).equals("Yes");
                    spawns.put(highestWaveStarted, new InfernoSpawn(id, regionX, regionY, respawn));
                }
            } else if (instruction.type == ParseType.LEFT_RAID)
            {
                if (entry.logEntry.equals(LogID.LEFT_TOB))
                {
                    if (endTime == 0)
                    {
                        endTime = entry.getValueAsInt("Last Room Tick");
                    }
                    if (!completed)
                    {
                        data.set(DataPoint.CHALLENGE_TIME, endTime - startTime - 1);
                    } else
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
        StringBuilder split = new StringBuilder();
        int last = -1;
        for (Integer val : roomMap.keySet())
        {
            if (val > 1)
            {
                int i = waveStarts.getOrDefault(val, -1);
                if(i > 0)
                {
                    int lastTime = (val == 9) ? 0 : waveStarts.getOrDefault(last, -1);
                    split.append("Wave ").append(val).append(", Split: ").append(RoomUtil.time(i)).append((lastTime > 0) ? " (+ " + (RoomUtil.time(i - lastTime)) + ")" : "").append("\n");
                }
            }
            last = val;
        }
        if(!completed)
        {
            split.append("Duration (Failed): ").append(RoomUtil.time(endTime - startTime)).append(" (+").append(RoomUtil.time(endTime - startTime - waveStarts.getOrDefault(highestWaveStarted, 0))).append(")\n");
        }
        else
        {
            split.append("Duration (Success): ").append(RoomUtil.time(endTime)).append(" (+").append(RoomUtil.time(endTime - waveStarts.getOrDefault(highestWaveStarted, 0))).append(")\n");

        }
        return split.toString();
    }

    @Override
    public boolean isAccurate()
    {
        return true;
    }

    @Override
    public int getTimeSum()
    {
        if (!completed)
        {
            return endTime - startTime;
        } else
        {
            return endTime;
        }
    }

    @Override
    public String getRoomStatus()
    {
        if (!completed)
        {
            return red + "Wave " + highestWaveStarted;
        } else
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
