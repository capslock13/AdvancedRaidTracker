package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.LogID;
import lombok.Value;

@Value
public class LogEntry
{
    long ts;
    int raid;
    LogID logEntry;
    String[] extra;

    LogEntry(String[] line)
    {
        // line[0] is unused, essentially a UID
        ts = Long.parseLong(line[1]);
        raid = Integer.parseInt(line[2]);
        logEntry = LogID.valueOf(Integer.parseInt(line[3]));
        // max 4 extra params
        extra = new String[5];
        System.arraycopy(line, 4, extra, 0, 5);
    }
}
