package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.LogID;
import lombok.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
}
