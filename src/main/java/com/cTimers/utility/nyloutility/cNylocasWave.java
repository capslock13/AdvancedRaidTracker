package com.cTimers.utility.nyloutility;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class cNylocasWave
{
    @Getter
    private int wave;
    @Getter
    private int delay;
    private cNylocas nylos[];

    public static cNylocasWave[] waves =
            {
                    new cNylocasWave(1, 4,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.RANGE_SMALL, true),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_EAST, cNylocas.cNylocasType.MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_NORTH, cNylocas.cNylocasType.MELEE_SMALL, false)
                            }),
                    new cNylocasWave(2, 4,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.EAST_SOUTH, cNylocas.cNylocasType.RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MELEE_SMALL, true)
                            }),
                    new cNylocasWave(3, 4,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_EAST, cNylocas.cNylocasType.RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_NORTH, cNylocas.cNylocasType.MAGE_SMALL, true),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MELEE_SMALL, false)
                            }),
                    new cNylocasWave(4, 4,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MAGE_BIG, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_SOUTH, cNylocas.cNylocasType.MELEE_SMALL, false)
                            }),
                    new cNylocasWave(5, 4,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.RANGE_BIG, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_NORTH, cNylocas.cNylocasType.MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_EAST, cNylocas.cNylocasType.MELEE_SMALL, false)
                            }),
                    new cNylocasWave(6, 16,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.MELEE_BIG, false)
                            }),
                    new cNylocasWave(7, 4,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.RANGE_BIG, true),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_EAST, cNylocas.cNylocasType.MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_NORTH, cNylocas.cNylocasType.MELEE_SMALL, false)
                            }),
                    new cNylocasWave(8, 12,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.EAST_SOUTH, cNylocas.cNylocasType.RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MAGE_BIG, true),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MELEE_SMALL, false)
                            }),
                    new cNylocasWave(9, 4,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.RANGE_BIG, true),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_NORTH, cNylocas.cNylocasType.MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MELEE_SMALL, false)
                            }),
                    new cNylocasWave(10, 16,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.RANGE_BIG, true),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_NORTH, cNylocas.cNylocasType.RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_EAST, cNylocas.cNylocasType.RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.RANGE_SMALL, true)
                            }),
                    new cNylocasWave(11, 8,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.EAST_SOUTH, cNylocas.cNylocasType.MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_NORTH, cNylocas.cNylocasType.MAGE_SMALL, true),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_EAST, cNylocas.cNylocasType.MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MAGE_BIG, true),
                            }),
                    new cNylocasWave(12, 8,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.EAST_SOUTH, cNylocas.cNylocasType.MELEE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_NORTH, cNylocas.cNylocasType.MELEE_SMALL, true),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.MELEE_SMALL, true),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MELEE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MELEE_BIG, false),
                            }),
                    new cNylocasWave(13, 8,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_EAST, cNylocas.cNylocasType.RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MELEE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MAGE_SMALL, true),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.MELEE_BIG, true),
                            }),
                    new cNylocasWave(14, 8,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MELEE_SMALL, true),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_EAST, cNylocas.cNylocasType.MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.RANGE_BIG, true),
                            }),
                    new cNylocasWave(15, 8,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.RANGE_SMALL, true),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.MELEE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_SOUTH, cNylocas.cNylocasType.RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_NORTH, cNylocas.cNylocasType.MAGE_SMALL, true),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MAGE_BIG, false),
                            }),
                    new cNylocasWave(16, 8,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MELEE_MAGE_RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.RANGE_MAGE_RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_SOUTH, cNylocas.cNylocasType.MAGE_MELEE_RANGE_SMALL, false),
                            }),
                    new cNylocasWave(17, 4,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MAGE_MELEE_MAGE_BIG, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MAGE_MELEE_MAGE_BIG, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.MAGE_MELEE_MAGE_BIG, false),
                            }),
                    new cNylocasWave(18, 12,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.RANGE_MAGE_RANGE_BIG, true),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.RANGE_MAGE_RANGE_BIG, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.RANGE_MAGE_RANGE_BIG, false),
                            }),
                    new cNylocasWave(19, 8,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MAGE_MELEE_MAGE_BIG, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MAGE_MELEE_MAGE_BIG, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.MAGE_MELEE_MAGE_BIG, false),
                            }),
                    new cNylocasWave(20, 12,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MELEE_RANGE_MELEE_BIG, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MAGE_RANGE_MELEE_BIG, true),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.MELEE_MAGE_RANGE_BIG, false),
                            }),
                    new cNylocasWave(21, 16,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MAGE_MELEE_RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.MAGE_RANGE_MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_SOUTH, cNylocas.cNylocasType.RANGE_MELEE_RANGE_SMALL, true),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_NORTH, cNylocas.cNylocasType.RANGE_MELEE_RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MELEE_RANGE_MELEE_SMALL, true),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_EAST, cNylocas.cNylocasType.MELEE_MAGE_MELEE_SMALL, false),
                            }),
                    new cNylocasWave(22, 8,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MAGE_RANGE_MELEE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_EAST, cNylocas.cNylocasType.RANGE_MAGE_MELEE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MELEE_RANGE_MELEE_BIG, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.MAGE_RANGE_MELEE_BIG, true),
                            }),
                    new cNylocasWave(23, 12,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.RANGE_MAGE_RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.MAGE_RANGE_MELEE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.RANGE_MAGE_MELEE_BIG, true),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.MAGE_RANGE_MELEE_BIG, false),
                            }),
                    new cNylocasWave(24, 8,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.RANGE_MAGE_MELEE_BIG, true),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MAGE_BIG, true),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.MELEE_BIG, true),
                            }),
                    new cNylocasWave(25, 8,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MELEE_BIG, true),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.RANGE_BIG, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.MAGE_MELEE_MAGE_BIG, false),
                            }),
                    new cNylocasWave(26, 8,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MAGE_BIG, true),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MELEE_MAGE_MELEE_BIG, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.MAGE_BIG, false),
                            }),
                    new cNylocasWave(27, 4,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.MAGE_BIG, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MELEE_MAGE_RANGE_BIG, true),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_BIG, cNylocas.cNylocasType.MAGE_MELEE_MAGE_BIG, false),
                            }),
                    new cNylocasWave(28, 8,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.EAST_NORTH, cNylocas.cNylocasType.MAGE_MELEE_MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_SOUTH, cNylocas.cNylocasType.RANGE_MAGE_MELEE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_EAST, cNylocas.cNylocasType.MELEE_RANGE_MELEE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MAGE_MELEE_RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.RANGE_MELEE_MAGE_SMALL, true),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.MELEE_MAGE_MELEE_SMALL, false),
                            }),
                    new cNylocasWave(29, 4,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.EAST_NORTH, cNylocas.cNylocasType.MAGE_RANGE_MELEE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_SOUTH, cNylocas.cNylocasType.RANGE_MELEE_MAGE_SMALL, true),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MELEE_BIG, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.RANGE_MELEE_RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.MELEE_RANGE_MAGE_SMALL, true),
                            }),
                    new cNylocasWave(30, 4,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.EAST_SOUTH, cNylocas.cNylocasType.MAGE_BIG, true),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_EAST, cNylocas.cNylocasType.MELEE_RANGE_MELEE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MAGE_RANGE_MELEE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.RANGE_MELEE_RANGE_BIG, false),
                            }),

                    new cNylocasWave(31, 4,
                            new cNylocas[]{
                                    new cNylocas(cNylocas.cNyloPosition.EAST_NORTH, cNylocas.cNylocasType.MAGE_RANGE_MAGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.EAST_SOUTH, cNylocas.cNylocasType.RANGE_MELEE_RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_EAST, cNylocas.cNylocasType.MELEE_MAGE_RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.SOUTH_WEST, cNylocas.cNylocasType.MAGE_MELEE_RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_SOUTH, cNylocas.cNylocasType.RANGE_MAGE_RANGE_SMALL, false),
                                    new cNylocas(cNylocas.cNyloPosition.WEST_NORTH, cNylocas.cNylocasType.MELEE_RANGE_MAGE_SMALL, false),
                            }),
            };
            public cNylocasWave(int wave, int delay, cNylocas nylos[])
            {
                this.wave = wave;
                this.delay = delay;
                this.nylos = nylos;
            }

            public int count()
            {
                return this.nylos.length;
            }

            public List<cNylocas> getNylos()
            {
                return Arrays.asList(nylos);
            }
}
