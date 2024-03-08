package com.TheatreTracker.utility.datautility.datapoints.tob;

import com.TheatreTracker.utility.datautility.datapoints.LogEntry;
import com.TheatreTracker.utility.datautility.datapoints.RoomDataManager;

import java.util.List;

public class BloatData extends RoomDataManager {

    public BloatData(List<LogEntry> roomData) {
        super(50, roomData);
    }
}
