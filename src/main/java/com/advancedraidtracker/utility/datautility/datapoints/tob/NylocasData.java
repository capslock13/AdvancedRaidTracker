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

@Getter
public class NylocasData extends RoomDataManager
{
    /**
     * What type of split that spawned from large nylocas.
     */
    public enum Split {
        MAGE_SPLIT,
        RANGE_SPLIT,
        MELEE_SPLIT
    }

    /**
     * A structure to group what tick waves began
     */
    public static class Wave {
        int wave;
        int tick;
        public Wave(Map<String, String> logData)
        {
            wave = Integer.parseInt(logData.get("wave"));
            tick = Integer.parseInt(logData.get("room tick"));
        }
    }

    /**
     * Class to track what phases the boss had.
     */
    @Value
    public static class BossPhase {
         enum BossPhaseEnum {
            MAGE_PHASE,
            RANGE_PHASE,
            MELEE_PHASE
        }

        BossPhaseEnum phase;
        int tick;
    }

    /**
     * A class to handle stalling at nylo.
     */
    @Value
    public static class Stall {
        Wave wave;
        int nyloCount;

        public Stall(Map<String, String> logData)
        {
            wave = new Wave(logData);
            nyloCount = Integer.parseInt(logData.get("nylos alive"));
        }
    }

    /**
     * Waves which were stalled
     */
    private final List<Stall> stalls;

    /**
     * A list of when each wave spawned.
     */
    private final List<Wave> waves;

    private final List<BossPhase> bossPhases;

    /**
     * Splits that came from large nylocas on each wave.
     */
    private final Multimap<Integer, Split> splits;

    private int bossSpawn;

    public NylocasData(List<LogEntry> roomData)
    {
        super(50, roomData);
        splits = ArrayListMultimap.create();
        stalls = new ArrayList<>();
        bossPhases = new ArrayList<>();
        waves = new ArrayList<>();
    }

    private void addSplit(Map<String, String> extras, Split split) {
        String wave = extras.get("wave");
        splits.put(Integer.valueOf(wave), split);
    }

    @Override
    public void parse() {
        super.parse();

        for (LogEntry entry : roomData) {
            LogID logID = entry.getLogEntry();
            Map<String, String> extras = entry.parseExtra();
            String data;

            switch (logID) {
                case MELEE_SPLIT:
                    addSplit(extras, Split.MELEE_SPLIT);
                    break;
                case MAGE_SPLIT:
                    addSplit(extras, Split.MAGE_SPLIT);
                    break;
                case RANGE_SPLIT:
                    addSplit(extras, Split.RANGE_SPLIT);

                case NYLO_STALL:
                    Stall stall = new Stall(extras);
                    stalls.add(stall);
                    break;

                case NYLO_WAVE:
                    Wave wave = new Wave(extras);
                    waves.add(wave);
                    break;

                case BOSS_SPAWN:
                    bossSpawn = LogEntry.getRoomTick(extras);
                    break;

                case NYLO_PILLAR_DESPAWNED:
                    time = LogEntry.getRoomTick(extras);
                    break;
            }
        }
    }

    @Override
    public String getName() {
        return "Nylocas";
    }
}
