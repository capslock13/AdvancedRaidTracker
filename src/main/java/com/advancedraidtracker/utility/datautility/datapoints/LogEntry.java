package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.LogID;
import com.google.common.collect.Streams;
import lombok.Value;

import java.util.*;
import java.util.stream.Collectors;

@Value
public class LogEntry
{
    long ts;
    int raid;
    LogID logEntry;
    List<String> extra;

    LogEntry(String[] line)
    {
        // line[0] is unused, essentially a UID
        ts = Long.parseLong(line[1]);
        raid = Integer.parseInt(line[2]);
        logEntry = LogID.valueOf(Integer.parseInt(line[3]));
        extra = Arrays.stream(line, 4, line.length).collect(Collectors.toList());
    }

    public Map<String, String> parseExtra()
    {
        // TODO add shorthands for common ones like room tick
        Map<String, String> map = new HashMap<>();
        String []descriptors = logEntry.getValueDescriptors();
        Streams.forEachPair(Arrays.stream(descriptors).map(String::toLowerCase), extra.stream(), map::put);
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
