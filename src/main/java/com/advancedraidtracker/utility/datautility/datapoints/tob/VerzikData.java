package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.RoomDataManager;

import java.util.List;

public class VerzikData extends RoomDataManager
{
    public VerzikData(List<LogEntry> roomData)
    {
        // TODO might need three verzik datas?
        super(-1, roomData);
    }

    @Override
    public String getName() {
        return "Verzik";
    }
}
