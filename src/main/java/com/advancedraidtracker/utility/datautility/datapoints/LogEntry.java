package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.LogID;
import com.formdev.flatlaf.util.StringUtils;
import com.google.common.collect.Streams;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Slf4j
@Value
public class LogEntry
{
    private final static HashSet<String> intValues = new HashSet<>(Arrays.asList("Damage", "Room Tick"));
    long ts;
    int raid;
    LogID logEntry;
    Map<String, String> values;
    String[] lines; // debug only

    LogEntry(String[] line)
    {
        this.lines = line;
        // line[0] is unused, essentially a UID
        ts = Long.parseLong(line[1]);
        raid = Integer.parseInt(line[2]);
        logEntry = LogID.valueOf(Integer.parseInt(line[3]));
        values = new LinkedHashMap<>();
        if (logEntry.isSimple())
        {
            int index = Integer.MAX_VALUE;
            boolean stringFound = false;
            for (int i = 0; i < logEntry.arguments.length; i++) //find first index of consecutive strings to end the arguments
            {
                if (!stringFound && logEntry.arguments[i] instanceof String)
                {
                    stringFound = true;
                    index = i;
                } else if (stringFound && !(logEntry.arguments[i] instanceof String))
                {
                    stringFound = false;
                }
            }
            index++;
            for (int i = index; i < logEntry.arguments.length; i++)
            {
                try
                {
                    if (logEntry.equals(LogID.PARTY_MEMBERS))
                    {
                        if (4 + i - index < line.length)
                        {
                            values.put((String) logEntry.arguments[i], line[4 + i - index]);
                        }
                    } else
                    {
                        values.put((String) logEntry.arguments[i], line[4 + i - index]);
                    }
                } catch (Exception e)
                {
                    log.info("Mismatch between LogID format and provided values: " + String.join(",", line));
                    log.info("Expected start index was: " + index + " and end index: " + logEntry.arguments.length + ". Failed on index : " + i);
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
