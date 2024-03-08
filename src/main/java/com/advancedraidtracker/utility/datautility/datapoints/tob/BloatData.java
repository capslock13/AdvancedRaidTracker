package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.RoomDataManager;

import java.util.List;

public class BloatData extends RoomDataManager
{

    public BloatData(List<LogEntry> roomData)
    {
        super(50, roomData);
    }
}
