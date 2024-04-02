package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.ParseType;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class LogEntry
{
    private final static HashSet<String> intValues = new HashSet<>(Arrays.asList("Damage", "Room Tick", "Raid Level", "Wave Number", "Amount", "Health"));
    long ts;
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
        for(int i = 4; i < line.length; i++)
        {
            if(!line[i].isEmpty())
            {
                try
                {
                    values.put(logEntry.getStringArgs().get(i - 4), line[i]);
                }
                catch (Exception e)
                {
                    if(logEntry.getStringArgs().size() != line.length-4)
                    {
                        //log.info("Mismatch, args.size(): " + logEntry.getStringArgs().size() + ", line length-4: " + (line.length-4) + ", value: " + String.join(",",line));
                    }
                    else
                    {
                        //log.info("Not mismatch: " + String.join(",",line));
                    }
                }
            }
        }
    }

    public int getFirstInt()
    {
        for(String value : values.keySet())
        {
            if(intValues.contains(value))
            {
                int returnVal = -1;
                try
                {
                    returnVal = Integer.parseInt(values.get(value));
                }
                catch (Exception e)
                {
                    log.info("Value: " + value + " Failed with " + logEntry.getId() + " and string: " + String.join(",", lines));
                    log.info("Values array: " + String.join(",", values.keySet()));
                    e.printStackTrace();
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
        }
        catch(Exception e)
        {
            log.info("Failed to parse " + name + " to int");
        }
        return value;
    }

    public Map<String, String> parseExtra()
    {
        // TODO add shorthands for common ones like room tick
        Map<String, String> map = new HashMap<>();
        //String []descriptors = logEntry.getValueDescriptors();
        //Streams.forEachPair(Arrays.stream(descriptors).map(String::toLowerCase), extra.stream(), map::put);
        return map;
    }

    public Integer getRoomTick() {
        Map<String, String> logData = parseExtra();
        return Integer.valueOf(logData.get("room tick"));
    }

    public static Integer getRoomTick(Map<String, String> logData)
    {
        return Integer.valueOf(logData.get("room tick"));
    }
}
