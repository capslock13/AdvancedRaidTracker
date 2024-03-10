package com.advancedraidtracker.utility.datautility.datapoints.tob;


import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.RoomDataManager;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MaidenData extends RoomDataManager
{
    @Getter
    private boolean isScuffed;

    @Getter
    private String scuffedAfter;

    /**
     * Holds room specific data
     */
    @Getter
    private final Multimap<LogID, Integer> data;

    /**
     * Amount of blood that maiden threw.
     */
    @Getter
    private int bloodThrown;

    /**
     * Amount of blood that has been spawned from e.g. blood spawns
     */
    @Getter
    private int bloodSpawned;

    /**
     * Contains health of crabs leaked.
     */
    @Getter
    private List<Integer> crabsLeaked;

    @Getter
    private Integer []splits;

    @Getter
    private List<String> crabSpawns;


    public MaidenData(List<LogEntry> roomData)
    {
        super(200, roomData);
        data = ArrayListMultimap.create();
        crabsLeaked = new ArrayList<>();
        crabSpawns = new ArrayList<>();
        splits = new Integer[3];
        splits[0] = splits[1] = splits[2] = 0;

        parse();
    }

    @Override
    public void parse()
    {
        super.parse();
        int current_split = 0;

        for (LogEntry entry : roomData)
        {
            LogID id = entry.getLogEntry();
            Map<String, String> extra = entry.parseExtra();
            String data;

            switch (id)
            {
                case MAIDEN_0HP:
                    return;

                case CRAB_LEAK:
                    crabsLeaked.add(Integer.valueOf(extra.get("health")));
                    break;

                case BLOOD_SPAWNED:
                    bloodSpawned++;
                    break;

                case BLOOD_THROWN:
                    bloodThrown++;
                    break;

                case MAIDEN_70S:
                case MAIDEN_50S:
                case MAIDEN_30S:
                    splits[current_split++] = LogEntry.getRoomTick(extra);
                    break;

                case MAIDEN_SCUFFED:
                    scuffedAfter = extra.get("current proc");
            }
        }
    }

    @Override
    public String getName() {
        return "Maiden";
    }
}
