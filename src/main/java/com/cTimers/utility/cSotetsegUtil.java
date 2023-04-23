package com.cTimers.utility;

import com.cTimers.Point;

import java.util.ArrayList;

public class cSotetsegUtil
{
    private static ArrayList<Point> filterMaze(ArrayList<Point> tiles)
    {
        ArrayList<Point> filteredTiles = new ArrayList<>();
        for (Point p : tiles)
        {
            if (p.getY() % 2 == 0)
            {
                filteredTiles.add(p);
            }
        }
        return filteredTiles;
    }

    private static ArrayList<Point> restoreMaze(ArrayList<Point> tiles)
    {
        ArrayList<Point> unfilteredTiles = new ArrayList<>();
        if (tiles.size() != 0)
        {
            unfilteredTiles.add(tiles.get(0));
            for (Point p : tiles)
            {
                if (p.getY() != 22)
                {
                    Point last = unfilteredTiles.get(unfilteredTiles.size() - 1);
                    for (int i = 0; i < Math.abs(p.getX() - last.getX()) + 1; i++)
                    {
                        if (p.getX() - last.getX() < 0)
                        {
                            unfilteredTiles.add(new Point(last.getX() - i, last.getY() + 1));
                        } else
                        {
                            unfilteredTiles.add(new Point(last.getX() + i, last.getY() + 1));
                        }
                    }
                    unfilteredTiles.add(p);
                }
            }
        }
        return unfilteredTiles;
    }

    private static String encodeMaze(ArrayList<Point> tiles)
    {
        String encoded = "";
        ArrayList<Point> filteredMaze = filterMaze(tiles);
        for (Point p : filteredMaze)
        {
            encoded += String.valueOf((char) (56 + p.getX()));
        }
        return encoded;
    }

    private static ArrayList<Point> decodeMaze(String maze)
    {
        ArrayList<Point> tiles = new ArrayList<>();
        int y = 0;
        for (char c : maze.toCharArray())
        {
            tiles.add(new Point(c - 56, 22 + y));
            y += 2;
        }
        return restoreMaze(tiles);
    }
}
