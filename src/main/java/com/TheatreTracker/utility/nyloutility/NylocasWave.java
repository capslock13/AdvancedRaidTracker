package com.TheatreTracker.utility.nyloutility;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class NylocasWave {
    @Getter
    private final int wave;
    @Getter
    private final int delay;
    private final NylocasData[] nylos;

    public static NylocasWave[] waves =
            {
                    new NylocasWave(1, 4,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.RANGE_SMALL, true),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_EAST, NylocasData.NylocasType.MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_NORTH, NylocasData.NylocasType.MELEE_SMALL, false)
                            }),
                    new NylocasWave(2, 4,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.EAST_SOUTH, NylocasData.NylocasType.RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MELEE_SMALL, true)
                            }),
                    new NylocasWave(3, 4,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_EAST, NylocasData.NylocasType.RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_NORTH, NylocasData.NylocasType.MAGE_SMALL, true),
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MELEE_SMALL, false)
                            }),
                    new NylocasWave(4, 4,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MAGE_BIG, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_SOUTH, NylocasData.NylocasType.MELEE_SMALL, false)
                            }),
                    new NylocasWave(5, 4,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.RANGE_BIG, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_NORTH, NylocasData.NylocasType.MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_EAST, NylocasData.NylocasType.MELEE_SMALL, false)
                            }),
                    new NylocasWave(6, 16,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.MELEE_BIG, false)
                            }),
                    new NylocasWave(7, 4,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.RANGE_BIG, true),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_EAST, NylocasData.NylocasType.MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_NORTH, NylocasData.NylocasType.MELEE_SMALL, false)
                            }),
                    new NylocasWave(8, 12,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.EAST_SOUTH, NylocasData.NylocasType.RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MAGE_BIG, true),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MELEE_SMALL, false)
                            }),
                    new NylocasWave(9, 4,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.RANGE_BIG, true),
                                    new NylocasData(NylocasData.NyloPosition.EAST_NORTH, NylocasData.NylocasType.MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MELEE_SMALL, false)
                            }),
                    new NylocasWave(10, 16,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.RANGE_BIG, true),
                                    new NylocasData(NylocasData.NyloPosition.EAST_NORTH, NylocasData.NylocasType.RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_EAST, NylocasData.NylocasType.RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.RANGE_SMALL, true)
                            }),
                    new NylocasWave(11, 8,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.EAST_SOUTH, NylocasData.NylocasType.MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_NORTH, NylocasData.NylocasType.MAGE_SMALL, true),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_EAST, NylocasData.NylocasType.MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MAGE_BIG, true),
                            }),
                    new NylocasWave(12, 8,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.EAST_SOUTH, NylocasData.NylocasType.MELEE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_NORTH, NylocasData.NylocasType.MELEE_SMALL, true),
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.MELEE_SMALL, true),
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MELEE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MELEE_BIG, false),
                            }),
                    new NylocasWave(13, 8,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_EAST, NylocasData.NylocasType.RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MELEE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MAGE_SMALL, true),
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.MELEE_BIG, true),
                            }),
                    new NylocasWave(14, 8,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MELEE_SMALL, true),
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_EAST, NylocasData.NylocasType.MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.RANGE_BIG, true),
                            }),
                    new NylocasWave(15, 8,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.RANGE_SMALL, true),
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.MELEE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_SOUTH, NylocasData.NylocasType.RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_NORTH, NylocasData.NylocasType.MAGE_SMALL, true),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MAGE_BIG, false),
                            }),
                    new NylocasWave(16, 8,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MELEE_MAGE_RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.RANGE_MAGE_RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_SOUTH, NylocasData.NylocasType.MAGE_MELEE_RANGE_SMALL, false),
                            }),
                    new NylocasWave(17, 4,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MAGE_MELEE_MAGE_BIG, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MAGE_MELEE_MAGE_BIG, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.MAGE_MELEE_MAGE_BIG, false),
                            }),
                    new NylocasWave(18, 12,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.RANGE_MAGE_RANGE_BIG, true),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.RANGE_MAGE_RANGE_BIG, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.RANGE_MAGE_RANGE_BIG, false),
                            }),
                    new NylocasWave(19, 8,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MAGE_MELEE_MAGE_BIG, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MAGE_MELEE_MAGE_BIG, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.MAGE_MELEE_MAGE_BIG, false),
                            }),
                    new NylocasWave(20, 12,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MELEE_RANGE_MELEE_BIG, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MAGE_RANGE_MELEE_BIG, true),
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.MELEE_MAGE_RANGE_BIG, false),
                            }),
                    new NylocasWave(21, 16,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MAGE_MELEE_RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.MAGE_RANGE_MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_SOUTH, NylocasData.NylocasType.RANGE_MELEE_RANGE_SMALL, true),
                                    new NylocasData(NylocasData.NyloPosition.EAST_NORTH, NylocasData.NylocasType.RANGE_MELEE_RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MELEE_RANGE_MELEE_SMALL, true),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_EAST, NylocasData.NylocasType.MELEE_MAGE_MELEE_SMALL, false),
                            }),
                    new NylocasWave(22, 8,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MAGE_RANGE_MELEE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_EAST, NylocasData.NylocasType.RANGE_MAGE_MELEE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MELEE_RANGE_MELEE_BIG, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.MAGE_RANGE_MELEE_BIG, true),
                            }),
                    new NylocasWave(23, 12,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.RANGE_MAGE_RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.MAGE_RANGE_MELEE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.RANGE_MAGE_MELEE_BIG, true),
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.MAGE_RANGE_MELEE_BIG, false),
                            }),
                    new NylocasWave(24, 8,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.RANGE_MAGE_MELEE_BIG, true),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MAGE_BIG, true),
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.MELEE_BIG, true),
                            }),
                    new NylocasWave(25, 8,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MELEE_BIG, true),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.RANGE_BIG, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.MAGE_MELEE_MAGE_BIG, false),
                            }),
                    new NylocasWave(26, 8,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MAGE_BIG, true),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MELEE_MAGE_MELEE_BIG, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.MAGE_BIG, false),
                            }),
                    new NylocasWave(27, 4,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.MAGE_BIG, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MELEE_MAGE_RANGE_BIG, true),
                                    new NylocasData(NylocasData.NyloPosition.EAST_BIG, NylocasData.NylocasType.MAGE_MELEE_MAGE_BIG, false),
                            }),
                    new NylocasWave(28, 8,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.EAST_NORTH, NylocasData.NylocasType.MAGE_MELEE_MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_SOUTH, NylocasData.NylocasType.RANGE_MAGE_MELEE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_EAST, NylocasData.NylocasType.MELEE_RANGE_MELEE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MAGE_MELEE_RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.RANGE_MELEE_MAGE_SMALL, true),
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.MELEE_MAGE_MELEE_SMALL, false),
                            }),
                    new NylocasWave(29, 4,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.EAST_NORTH, NylocasData.NylocasType.MAGE_RANGE_MELEE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_SOUTH, NylocasData.NylocasType.RANGE_MELEE_MAGE_SMALL, true),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MELEE_BIG, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.RANGE_MELEE_RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.MELEE_RANGE_MAGE_SMALL, true),
                            }),
                    new NylocasWave(30, 4,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.EAST_SOUTH, NylocasData.NylocasType.MAGE_BIG, true),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_EAST, NylocasData.NylocasType.MELEE_RANGE_MELEE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MAGE_RANGE_MELEE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.RANGE_MELEE_RANGE_BIG, false),
                            }),

                    new NylocasWave(31, 4,
                            new NylocasData[]{
                                    new NylocasData(NylocasData.NyloPosition.EAST_NORTH, NylocasData.NylocasType.MAGE_RANGE_MAGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.EAST_SOUTH, NylocasData.NylocasType.RANGE_MELEE_RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_EAST, NylocasData.NylocasType.MELEE_MAGE_RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.SOUTH_WEST, NylocasData.NylocasType.MAGE_MELEE_RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_SOUTH, NylocasData.NylocasType.RANGE_MAGE_RANGE_SMALL, false),
                                    new NylocasData(NylocasData.NyloPosition.WEST_NORTH, NylocasData.NylocasType.MELEE_RANGE_MAGE_SMALL, false),
                            }),
            };

    public NylocasWave(int wave, int delay, NylocasData nylos[])
    {
        this.wave = wave;
        this.delay = delay;
        this.nylos = nylos;
    }

    public int count() {
        return this.nylos.length;
    }

    public List<NylocasData> getNylos() {
        return Arrays.asList(nylos);
    }
}
