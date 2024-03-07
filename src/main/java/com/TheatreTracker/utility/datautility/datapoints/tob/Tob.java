package com.TheatreTracker.utility.datautility.datapoints.tob;

import com.TheatreTracker.utility.datautility.datapoints.Raid;
import com.TheatreTracker.utility.datautility.datapoints.RoomDataManager;
import lombok.Getter;

import java.util.List;

public class Tob extends Raid {
    /**
     * Enum for what difficulty the raid is.
     */
    public enum RaidMode {
        ENTRY,
        NORMAL,
        HARD
    }

    @Getter
    private RaidMode mode;

    @Getter
    private MaidenData maidenData;

    @Getter
    private BloatData bloatData;

    @Getter
    private NylocasData nylocasData;

    @Getter
    private SotetsegData sotetsegData;

    @Getter
    private XarpusData xarpusData;

    @Getter
    private VerzikData verzikData;

    public Tob(String filepath) {
        super(filepath);
    }

    @Override
    public List<RoomDataManager> getAllData() {
        return null;
    }
}
