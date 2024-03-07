package com.TheatreTracker.utility.datautility.datapoints;

import com.TheatreTracker.constants.LogID;
import lombok.Value;

@Value
public class LogEntry {
    int uid;
    long ts;
    int raid;
    LogID logEntry;
    String []extra;

    LogEntry(String []line) {
        uid = Integer.parseInt(line[0]);
        ts = Long.parseLong(line[1]);
        raid = Integer.parseInt(line[2]);
        logEntry = LogID.valueOf(Integer.parseInt(line[3]));
        // max 4 extra params
        extra = new String[4];
        System.arraycopy(line, 4, extra, 0, 4);
    }
}
