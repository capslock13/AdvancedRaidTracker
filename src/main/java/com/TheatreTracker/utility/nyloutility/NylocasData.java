package com.TheatreTracker.utility.nyloutility;

public class NylocasData
{
    public enum NylocasType
    {
        MELEE_BIG,
        MELEE_SMALL,
        RANGE_BIG,
        RANGE_SMALL,
        MAGE_BIG,
        MAGE_SMALL,
        MAGE_MELEE_RANGE_SMALL,
        MELEE_MAGE_RANGE_SMALL,
        RAMGE_MAGE_RANGE_SMALL,
        MAGE_MELEE_MAGE_BIG,
        RANGE_MAGE_RANGE_BIG,
        MELEE_RANGE_MELEE_BIG,
        MAGE_RANGE_MELEE_BIG,
        RANGE_MELEE_RANGE_SMALL,
        MELEE_MAGE_MELEE_SMALL,
        MELEE_RANGE_MELEE_SMALL,
        MAGE_RANGE_MAGE_SMALL,
        RANGE_MAGE_MELEE_SMALL,
        MAGE_RANGE_MELEE_SMALL,
        RANGE_MAGE_MELEE_BIG,
        RANGE_MAGE_RANGE_SMALL,
        MELEE_MAGE_MELEE_BIG,
        MELEE_MAGE_RANGE_BIG,
        MAGE_MELEE_MAGE_SMALL,
        RANGE_MELEE_MAGE_SMALL,
        MELEE_RANGE_MAGE_SMALL,
        RANGE_MELEE_RANGE_BIG,
    }

    public enum NyloPosition
    {
        EAST_NORTH,
        EAST_SOUTH,
        SOUTH_EAST,
        SOUTH_WEST,
        WEST_SOUTH,
        WEST_NORTH,
        EAST_BIG,
        SOUTH_BIG,
        WEST_BIG,
        ROOM
    }

    public NylocasData(NyloPosition position, NylocasType type, boolean aggro)
    {
        this.position = position;
        this.type = type;
        this.aggro = aggro;
    }

    public int getSpawnStyle()
    {
        switch (type)
        {
            case MELEE_SMALL:
            case MELEE_MAGE_RANGE_SMALL:
            case MELEE_MAGE_MELEE_SMALL:
            case MELEE_RANGE_MELEE_SMALL:
            case MELEE_RANGE_MAGE_SMALL:
                return 0;
            case MELEE_MAGE_MELEE_BIG:
            case MELEE_MAGE_RANGE_BIG:
            case MELEE_RANGE_MELEE_BIG:
            case MELEE_BIG:
                return 3;
            case MAGE_BIG:
            case MAGE_RANGE_MELEE_BIG:
            case MAGE_MELEE_MAGE_BIG:
                return 5;
            case MAGE_SMALL:
            case MAGE_MELEE_MAGE_SMALL:
            case MAGE_MELEE_RANGE_SMALL:
            case MAGE_RANGE_MAGE_SMALL:
            case MAGE_RANGE_MELEE_SMALL:
                return 2;
            case RANGE_MAGE_MELEE_BIG:
            case RANGE_BIG:
            case RANGE_MAGE_RANGE_BIG:
            case RANGE_MELEE_RANGE_BIG:
                return 4;
            case RANGE_MELEE_RANGE_SMALL:
            case RANGE_MELEE_MAGE_SMALL:
            case RANGE_MAGE_RANGE_SMALL:
            case RANGE_MAGE_MELEE_SMALL:
            case RANGE_SMALL:
                return 1;
        }
        return -1;
    }

    public NyloPosition position;
    public NylocasType type;
    public boolean aggro;
}
