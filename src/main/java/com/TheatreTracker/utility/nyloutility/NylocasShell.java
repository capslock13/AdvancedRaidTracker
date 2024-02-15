package com.TheatreTracker.utility.nyloutility;

import static com.TheatreTracker.constants.TobIDs.*;
import static com.TheatreTracker.constants.TobIDs.NYLO_MELEE_SMALL_AGRO_SM;

public class NylocasShell
{
    public int style;
    public NylocasData.NyloPosition position;

    public boolean isBig()
    {
        return (style > 2);
    }

    public static String getTypeName(int id)
    {
        switch (id)
        {
            case NYLO_MELEE_SMALL:
            case NYLO_MELEE_SMALL_AGRO:
            case NYLO_MELEE_SMALL_HM:
            case NYLO_MELEE_SMALL_AGRO_HM:
            case NYLO_MELEE_SMALL_SM:
            case NYLO_MELEE_SMALL_AGRO_SM:
                return "melee";
            case NYLO_RANGE_SMALL:
            case NYLO_RANGE_SMALL_AGRO:
            case NYLO_RANGE_SMALL_HM:
            case NYLO_RANGE_SMALL_AGRO_HM:
            case NYLO_RANGE_SMALL_SM:
            case NYLO_RANGE_SMALL_AGRO_SM:
                return "range";
            case NYLO_MAGE_SMALL:
            case NYLO_MAGE_SMALL_AGRO:
            case NYLO_MAGE_SMALL_HM:
            case NYLO_MAGE_SMALL_AGRO_HM:
            case NYLO_MAGE_SMALL_SM:
            case NYLO_MAGE_SMALL_AGRO_SM:
                return "mage";

        }
        return "";
    }

    public String getDescription()
    {
        String type = "";
        switch (style)
        {
            case 0:
            case 3:
                type = "melee";
                break;
            case 1:
            case 4:
                type = "range";
                break;
            case 2:
            case 5:
                type = "mage";
                break;
        }
        if (style > 2)
        {
            switch (position) //spawn position for bigs is shared with littles on south/west, but southwest tile of east big is in unique spot
            {
                case EAST_BIG:
                    return "east big " + type;
                case SOUTH_WEST:
                    return "south big " + type;
                case WEST_SOUTH:
                    return "west big " + type;
            }
        }
        switch (position)
        {
            case EAST_NORTH:
                return "east small " + type + " (N)";
            case EAST_SOUTH:
                return "east small " + type + " (S)";
            case SOUTH_EAST:
                return "south small " + type + " (E)";
            case SOUTH_WEST:
                return "south small " + type + " (W)";
            case WEST_NORTH:
                return "west small " + type + " (N)";
            case WEST_SOUTH:
                return "west small " + type + " (S)";
        }
        return "";
    }

    public NylocasShell(int id, int x, int y)
    {
        switch (id)
        {
            case NYLO_MELEE_SMALL_HM:
            case NYLO_MELEE_SMALL_SM:
            case NYLO_MELEE_SMALL:
            case NYLO_MELEE_SMALL_AGRO_HM:
            case NYLO_MELEE_SMALL_AGRO_SM:
                style = 0;
                break;
            case NYLO_RANGE_SMALL_HM:
            case NYLO_RANGE_SMALL_SM:
            case NYLO_RANGE_SMALL:
            case NYLO_RANGE_SMALL_AGRO_HM:
            case NYLO_RANGE_SMALL_AGRO_SM:
            case NYLO_RANGE_SMALL_AGRO:
                style = 1;
                break;
            case NYLO_MAGE_SMALL_HM:
            case NYLO_MAGE_SMALL_SM:
            case NYLO_MAGE_SMALL:
            case NYLO_MAGE_SMALL_AGRO_HM:
            case NYLO_MAGE_SMALL_AGRO_SM:
            case NYLO_MAGE_SMALL_AGRO:
                style = 2;
                break;
            case NYLO_MELEE_BIG:
            case NYLO_MELEE_BIG_HM:
            case NYLO_MELEE_BIG_SM:
            case NYLO_MELEE_BIG_AGRO:
            case NYLO_MELEE_BIG_AGRO_HM:
            case NYLO_MELEE_BIG_AGRO_SM:
                style = 3;
                break;
            case NYLO_RANGE_BIG_HM:
            case NYLO_RANGE_BIG_SM:
            case NYLO_RANGE_BIG:
            case NYLO_RANGE_BIG_AGRO_HM:
            case NYLO_RANGE_BIG_AGRO_SM:
            case NYLO_RANGE_BIG_AGRO:
                style = 4;
                break;
            case NYLO_MAGE_BIG:
            case NYLO_MAGE_BIG_HM:
            case NYLO_MAGE_BIG_SM:
            case NYLO_MAGE_BIG_AGRO:
            case NYLO_MAGE_BIG_AGRO_HM:
            case NYLO_MAGE_BIG_AGRO_SM:
                style = 5;
                break;
        }
        if (x == 9 && y == 25)
        {
            position = NylocasData.NyloPosition.WEST_NORTH;
        } else if (x == 9 && y == 24)
        {
            position = NylocasData.NyloPosition.WEST_SOUTH;
        } else if (x == 23 && y == 9)
        {
            position = NylocasData.NyloPosition.SOUTH_WEST;
        } else if (x == 24 && y == 9)
        {
            position = NylocasData.NyloPosition.SOUTH_EAST;
        } else if (x == 38 && y == 24)
        {
            position = NylocasData.NyloPosition.EAST_SOUTH;
        } else if (x == 38 && y == 25)
        {
            position = NylocasData.NyloPosition.EAST_NORTH;
        } else if (x == 37 && y == 24)
        {
            position = NylocasData.NyloPosition.EAST_BIG;
        } else
        {
            position = NylocasData.NyloPosition.ROOM;
        }
    }
}
