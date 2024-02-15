package com.TheatreTracker.utility;

import com.TheatreTracker.constants.NpcIDs;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import com.TheatreTracker.Point;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;


public class RoomUtil
{
    public final static int MAIDEN_REGION = 12613;
    public final static int BLOAT_REGION = 13125;
    public final static int NYLO_REGION = 13122;
    public final static int SOTETSEG_REGION = 13123;
    public final static int XARPUS_REGION = 12612;
    public final static int VERZIK_REGION = 12611;

    public final static String[] MAIDEN_CRAB_NAMES = {
            "N1 70s", "N2 70s", "N3 70s", "N4 (1) 70s", "N4 (2) 70s",
            "S1 70s", "S2 70s", "S3 70s", "S4 (1) 70s", "S4 (2) 70s",
            "N1 50s", "N2 50s", "N3 50s", "N4 (1) 50s", "N4 (2) 50s",
            "S1 50s", "S2 50s", "S3 50s", "S4 (1) 50s", "S4 (2) 50s",
            "N1 30s", "N2 30s", "N3 30s", "N4 (1) 30s", "N4 (2) 30s",
            "S1 30s", "S2 30s", "S3 30s", "S4 (1) 30s", "S4 (2) 30s",
    };

    public static String time(String ticks)
    {
        try
        {
            return time(Integer.parseInt(ticks));
        } catch (Exception e)
        {
            return ticks;
        }
    }

    public static String varbitHPtoReadable(int varbitHP)
    {
        return (varbitHP / 10) + "." + (varbitHP % 10) + "%";
    }


    public static boolean isPrimaryBoss(int ID)
    {
        int[] bosses = {
                NpcIDs.MAIDEN_P0,
                NpcIDs.MAIDEN_P1,
                NpcIDs.MAIDEN_P2,
                NpcIDs.MAIDEN_P3,
                NpcIDs.MAIDEN_PRE_DEAD,
                NpcIDs.MAIDEN_P0_HM,
                NpcIDs.MAIDEN_P1_HM,
                NpcIDs.MAIDEN_P2_HM,
                NpcIDs.MAIDEN_P3_HM,
                NpcIDs.MAIDEN_PRE_DEAD_HM,
                NpcIDs.MAIDEN_P0_SM,
                NpcIDs.MAIDEN_P1_SM,
                NpcIDs.MAIDEN_P2_SM,
                NpcIDs.MAIDEN_P3_SM,
                NpcIDs.MAIDEN_PRE_DEAD_SM,
                NpcIDs.BLOAT,
                NpcIDs.BLOAT_HM,
                NpcIDs.BLOAT_SM,
                NpcIDs.NYLO_BOSS_MELEE,
                NpcIDs.NYLO_BOSS_RANGE,
                NpcIDs.NYLO_BOSS_MAGE,
                NpcIDs.NYLO_BOSS_MELEE_HM,
                NpcIDs.NYLO_BOSS_RANGE_HM,
                NpcIDs.NYLO_BOSS_MAGE_HM,
                NpcIDs.NYLO_BOSS_MELEE_SM,
                NpcIDs.NYLO_BOSS_RANGE_SM,
                NpcIDs.NYLO_BOSS_MAGE_SM,
                NpcIDs.SOTETSEG_ACTIVE,
                NpcIDs.SOTETSEG_ACTIVE_HM,
                NpcIDs.SOTETSEG_ACTIVE_SM,
                NpcIDs.XARPUS_P23,
                NpcIDs.XARPUS_P23_HM,
                NpcIDs.XARPUS_P23_SM,
                NpcIDs.VERZIK_P1,
                NpcIDs.VERZIK_P2,
                NpcIDs.VERZIK_P3,
                NpcIDs.VERZIK_P1_HM,
                NpcIDs.VERZIK_P2_HM,
                NpcIDs.VERZIK_P3_HM,
                NpcIDs.VERZIK_P1_SM,
                NpcIDs.VERZIK_P2_SM,
                NpcIDs.VERZIK_P3_SM,
        };
        return (Arrays.stream(bosses).anyMatch(p -> p == ID));
    }

    public static String time(int ticks)
    {
        if (ticks == 0 || ticks == Integer.MAX_VALUE || ticks == -1)
        {
            return "-";
        }
        String timeStr = "";
        double seconds = ticks * .6;
        int minutes = ((int) seconds - ((int) seconds) % 60) / 60;
        int onlySeconds = (int) seconds - 60 * minutes;
        String secondsString = String.format("%.1f", ticks * .6);
        if (minutes != 0)
            timeStr += minutes + ":";
        if (onlySeconds < 10 && minutes != 0)
        {
            timeStr += "0";
        }
        timeStr += onlySeconds;
        if (StringUtils.split(secondsString, ".").length == 2)
        {
            String[] subStr = StringUtils.split(secondsString, ".");
            timeStr += "." + subStr[1];
        } else
        {
            timeStr += ".0";
        }
        if (minutes == 0)
        {
            timeStr += "s";
        }
        return timeStr;
    }

    public static String time(double ticks)
    {
        if (ticks == Integer.MAX_VALUE || ticks < 1)
        {
            return "-";
        }
        String timeStr = "";
        double seconds = ticks * .6;
        int minutes = ((int) seconds - ((int) seconds) % 60) / 60;
        int onlySeconds = (int) seconds - 60 * minutes;
        String secondsString = String.format("%.1f", ticks * .6);
        if (minutes != 0)
            timeStr += minutes + ":";
        if (onlySeconds < 10 && minutes != 0)
        {
            timeStr += "0";
        }
        timeStr += onlySeconds;
        if (StringUtils.split(secondsString, ".").length == 2)
        {
            String[] subStr = StringUtils.split(secondsString, ".");
            timeStr += "." + subStr[1];
        } else
        {
            timeStr += ".0";
        }
        if (minutes == 0)
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
                if (vertical)
                {
                    for (int i = start.getY(); i < end.getY() + 1; i++)
                    {
                        if (wp.getRegionY() == i && wp.getRegionX() == start.getX())
                        {
                            return true;
                        }
                    }
                } else
                {
                    for (int i = start.getX(); i < end.getX() + 1; i++)
                    {
                        if (wp.getRegionX() == i && wp.getRegionY() == start.getY())
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
