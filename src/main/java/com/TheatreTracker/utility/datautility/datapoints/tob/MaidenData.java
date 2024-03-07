package com.TheatreTracker.utility.datautility.datapoints.tob;


import com.TheatreTracker.utility.datautility.datapoints.RoomDataManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class MaidenData extends RoomDataManager {
    /**
     * Holds room specific data
     */
    @Getter
    private final Map<MaidenDataPoints, Integer> data;


    public enum MaidenDataPoints {
        BLOOD_SPAWNED,
        BLOOD_DESPAWNED,
        STOOD_IN_THROWN_BLOOD,
        STOOD_IN_SPAWNED_BLOOD,
    }

    public MaidenData() {
        super(200);
        data = new HashMap<>();
    }

    public void insertData(MaidenDataPoints event, int ts) {
        data.put(event, ts);
    }
}
