package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.RoomDataManager;

import java.util.List;

public class XarpusData extends RoomDataManager
{
    public XarpusData(List<LogEntry> roomData) {
        super(200, roomData);
    }

    @Override
    public String getName() {
        return null;
    }
}
