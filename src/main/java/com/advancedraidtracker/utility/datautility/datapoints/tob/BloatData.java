package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.RoomDataManager;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BloatData extends RoomDataManager
{
    /**
     * A list of ticks that bloat went down.
     */
    @Getter
    private List<Integer> downs;

    /**
     * Scythe hits while bloat is walking.
     */
    @Getter
    private Multimap<String, Integer> firstWalkScythes;

    /**
     * How much health bloat had when it first went down.
     */
    @Getter
    private int hpFirstDown;

    public BloatData(List<LogEntry> roomData)
    {
        super(50, roomData);
        downs = new ArrayList<>();
        firstWalkScythes = ArrayListMultimap.create();
        parse();
    }

    @Override
    public void parse() {
        super.parse();

        for (LogEntry entry : roomData) {
            LogID logID = entry.getLogEntry();
            Map<String, String> extras = entry.parseExtra();
            String data;

            switch (logID) {
                case BLOAT_DESPAWN:
                    data = extras.get("room tick");
                    time = Integer.parseInt(data);
                    break;

                case BLOAT_DOWN:
                    data = extras.get("room tick");
                    downs.add(Integer.parseInt(data));
                    break;

                case BLOAT_HP_1ST_DOWN:
                    data = extras.get("bloat hp");
                    hpFirstDown = Integer.parseInt(data);
                    break;

                case BLOAT_SCYTHE_1ST_WALK:
                    data = extras.get("room tick");
                    firstWalkScythes.put(extras.get("player"), Integer.parseInt(data));
                    break;
            }
        }
    }

    @Override
    public String getName() {
        return "Bloat";
    }
}
