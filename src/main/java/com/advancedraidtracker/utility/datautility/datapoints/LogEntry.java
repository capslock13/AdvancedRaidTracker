package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.ParseType;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class LogEntry
{
    private final static HashSet<String> intValues = new HashSet<>(Arrays.asList("Damage", "Room Tick", "Raid Level", "Wave Number", "Amount", "Health"));
    public long ts;
    int raid;
    public LogID logEntry;
    public Map<String, String> values;
    public String[] lines; // debug only todo remove

    public LogEntry(String[] line)
    {
        this.lines = line;
        // line[0] is unused, essentially a UID
        ts = Long.parseLong(line[1]);
        raid = Integer.parseInt(line[2]);
        logEntry = LogID.valueOf(Integer.parseInt(line[3]));
        values = new LinkedHashMap<>();
        for (int i = 4; i < line.length; i++)
        {
            if (!line[i].isEmpty())
            {
                try
                {
                    values.put(logEntry.getStringArgs().get(i - 4), line[i]);
                } catch (Exception ignored)
                {
                }
            }
        }
    }

    public int getFirstInt()
    {
        for (String value : values.keySet())
        {
            if (intValues.contains(value))
            {
                int returnVal = -1;
                try
                {
                    returnVal = Integer.parseInt(values.get(value));
                } catch (Exception ignored)
                {
                }
                return returnVal;
            }
        }
        return -1;
    }

    public String getValue(String name)
    {
        return values.getOrDefault(name, "");
    }

    public int getValueAsInt(String name)
    {
        int value = -1;
        try
        {
            value = Integer.parseInt(values.getOrDefault(name, "-1"));
        } catch (Exception e)
        {
            log.info("Failed to parse " + name + " to int");
        }
        return value;
    }
}
