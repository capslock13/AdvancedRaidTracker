package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.RoomDataManager;

import java.util.List;

public class SotetsegData extends RoomDataManager
{
    public SotetsegData(List<LogEntry> roomData)
    {
        super(200, roomData);
    }

    @Override
    public String getName() {
        return "Sotetseg";
    }
}
