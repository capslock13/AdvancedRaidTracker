package com.TheatreTracker.utility.nyloutility;

import static com.TheatreTracker.constants.NpcIDs.*;
import static com.TheatreTracker.constants.NpcIDs.NYLO_MELEE_SMALL_AGRO_SM;

public class NylocasShell {
    public int style;
    public NylocasData.NyloPosition position;

    public boolean isBig()
    {
        return position.equals(NylocasData.NyloPosition.EAST_BIG) || position.equals(NylocasData.NyloPosition.SOUTH_BIG) || position.equals(NylocasData.NyloPosition.WEST_BIG);
    }

    public static String getTypeName(int id)
    {
        switch(id)
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
        switch (position)
        {
            case EAST_BIG:
                return "east big " + type;
            case SOUTH_BIG:
                return "south big " + type;
            case WEST_BIG:
                return "west big " + type;
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
            case 10791:
            case 8342:
                style = 0;
                break;
            case 10792:
            case 8343:
                style = 1;
                break;
            case 10793:
            case 8344:
                style = 2;
                break;
            case 8345:
            case 10794:
            case 8351:
                style = 3;
                break;
            case 10795:
            case 8346:
            case 8352:
                style = 4;
                break;
            case 10796:
            case 8347:
            case 8353:
                style = 5;
                break;
        }
        if (x == 9 && y == 25)
        {
            position = NylocasData.NyloPosition.WEST_NORTH;
        }
        else if (x == 9 && y == 24)
        {
            position = NylocasData.NyloPosition.WEST_SOUTH;
        }
        else if (x == 23 && y == 9)
        {
            position = NylocasData.NyloPosition.SOUTH_WEST;
        }
        else if (x == 24 && y == 9)
        {
            position = NylocasData.NyloPosition.SOUTH_EAST;
        }
        else if (x == 38 && y == 24)
        {
            position = NylocasData.NyloPosition.EAST_SOUTH;
        }
        else if (x == 38 && y == 25)
        {
            position = NylocasData.NyloPosition.EAST_NORTH;
        }
        else if (x == 37 && y == 24)
        {
            position = NylocasData.NyloPosition.EAST_BIG;
        }
        else
        {
            position = NylocasData.NyloPosition.ROOM;
        }
    }
}
