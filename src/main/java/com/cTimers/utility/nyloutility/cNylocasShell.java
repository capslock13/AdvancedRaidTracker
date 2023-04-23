package com.cTimers.utility.nyloutility;

public class cNylocasShell
{
    public int style;
    public cNylocas.cNyloPosition position;

    public cNylocasShell(int id, int x, int y)
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
        if(x == 9 && y == 25)
        {
            position = cNylocas.cNyloPosition.WEST_NORTH;
        }
        else if(x == 9 && y == 24)
        {
            position = cNylocas.cNyloPosition.WEST_SOUTH;
        }
        else if(x == 23 && y == 9)
        {
            position = cNylocas.cNyloPosition.SOUTH_WEST;
        }
        else if(x == 24 && y == 9)
        {
            position = cNylocas.cNyloPosition.SOUTH_EAST;
        }
        else if(x == 38 && y == 24)
        {
            position = cNylocas.cNyloPosition.EAST_SOUTH;
        }
        else if(x == 38 && y == 25)
        {
            position = cNylocas.cNyloPosition.EAST_NORTH;
        }
        else if(x == 37 && y == 24)
        {
            position = cNylocas.cNyloPosition.EAST_BIG;
        }
        else
        {
            position = cNylocas.cNyloPosition.ROOM;
        }
    }
}
