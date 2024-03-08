package com.advancedraidtracker.utility.datautility.datapoints.tob;


import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.RoomDataManager;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;

import java.util.List;

public class MaidenData extends RoomDataManager
{
    /**
     * Holds room specific data
     */
    @Getter
    private final Multimap<LogID, Integer> data;

    public MaidenData(List<LogEntry> roomData)
    {
        super(200, roomData);
        data = ArrayListMultimap.create();
        parse();
    }

    @Override
    public void parse()
    {
        super.parse();
    }
}
