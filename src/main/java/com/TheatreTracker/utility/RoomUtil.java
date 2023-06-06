package com.TheatreTracker.utility;

import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import com.TheatreTracker.Point;
import org.apache.commons.lang3.StringUtils;


public class RoomUtil
{
    public final static int MAIDEN_REGION = 12613;
    public final static int BLOAT_REGION = 13125;
    public final static int NYLO_REGION = 13122;
    public final static int SOTETSEG_REGION = 13123;
    public final static int XARPUS_REGION = 12612;
    public final static int VERZIK_REGION = 12611;

    public static boolean debug = true;

    public static String time(int ticks)
    {
        if(ticks == 0 || ticks == Integer.MAX_VALUE || ticks == -1)
        {
            return "-";
        }
        String timeStr = "";
        double seconds = ticks*.6;
        int minutes = ((int)seconds-((int)seconds)%60)/60;
        int onlySeconds = (int)seconds - 60*minutes;
        String secondsString = String.format("%.1f", ticks * .6);
        if(minutes != 0)
            timeStr += minutes + ":";
        if(onlySeconds < 10 && minutes != 0)
        {
            timeStr += "0";
        }
        timeStr += onlySeconds;
        if(StringUtils.split(secondsString, ".").length == 2)
        {
            String[] subStr = StringUtils.split(secondsString, ".");
            timeStr += "." + subStr[1];
        }
        else
        {
            timeStr += ".0";
        }
        if(minutes == 0)
        {
            timeStr += "s";
        }
        return timeStr;
    }

    public static String time(double ticks)
    {
        if(ticks == 0 || ticks == Integer.MAX_VALUE || ticks == -1)
        {
            return "-";
        }
        String timeStr = "";
        double seconds = ticks*.6;
        int minutes = ((int)seconds-((int)seconds)%60)/60;
        int onlySeconds = (int)seconds - 60*minutes;
        String secondsString = String.format("%.1f", ticks * .6);
        if(minutes != 0)
            timeStr += minutes + ":";
        if(onlySeconds < 10 && minutes != 0)
        {
            timeStr += "0";
        }
        timeStr += onlySeconds;
        if(StringUtils.split(secondsString, ".").length == 2)
        {
            String[] subStr = StringUtils.split(secondsString, ".");
            timeStr += "." + subStr[1];
        }
        else
        {
            timeStr += ".0";
        }
        if(minutes == 0)
        {
            timeStr += "s";
        }
        return timeStr;
    }

    public static boolean crossedLine(int region, Point start, Point end, boolean vertical, Client client)
    {
        if (inRegion(client, region))
        {
            for (Player p : client.getPlayers())
            {
                WorldPoint wp = p.getWorldLocation();
                if(vertical)
                {
                    for(int i = start.getY(); i < end.getY()+1; i++)
                    {
                        if(wp.getRegionY() == i && wp.getRegionX() == start.getX())
                        {
                            return true;
                        }
                    }
                }
                else
                {
                    for(int i = start.getX(); i < end.getX()+1; i++)
                    {
                        if(wp.getRegionX() == i && wp.getRegionY() == start.getY())
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean inRegion(Client client, int... regions)
    {
        if (client.getMapRegions() != null)
        {
            for (int i : client.getMapRegions())
            {
                for (int j : regions)
                {
                    if (i == j)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
