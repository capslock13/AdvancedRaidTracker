package com.advancedraidtracker.utility;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.constants.Room;
import com.advancedraidtracker.constants.TobIDs;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.google.common.base.CharMatcher;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class RoomUtil
{
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

	public static String toCleanName(String str)
	{
		return CharMatcher.ascii().retainFrom(str.replaceAll("[ -]", " ")).trim();
	}

    public static String varbitHPtoReadable(int varbitHP)
    {
        return (varbitHP / 10) + "." + (varbitHP % 10) + "%";
    }


    public static boolean isPrimaryBoss(int ID)
    {
        if (ID == 0)
        {
            return true;
        }
        int[] bosses = {
                TobIDs.MAIDEN_P0,
                TobIDs.MAIDEN_P1,
                TobIDs.MAIDEN_P2,
                TobIDs.MAIDEN_P3,
                TobIDs.MAIDEN_PRE_DEAD,
                TobIDs.MAIDEN_P0_HM,
                TobIDs.MAIDEN_P1_HM,
                TobIDs.MAIDEN_P2_HM,
                TobIDs.MAIDEN_P3_HM,
                TobIDs.MAIDEN_PRE_DEAD_HM,
                TobIDs.MAIDEN_P0_SM,
                TobIDs.MAIDEN_P1_SM,
                TobIDs.MAIDEN_P2_SM,
                TobIDs.MAIDEN_P3_SM,
                TobIDs.MAIDEN_PRE_DEAD_SM,
                TobIDs.BLOAT,
                TobIDs.BLOAT_HM,
                TobIDs.BLOAT_SM,
                TobIDs.NYLO_BOSS_MELEE,
                TobIDs.NYLO_BOSS_RANGE,
                TobIDs.NYLO_BOSS_MAGE,
                TobIDs.NYLO_BOSS_MELEE_HM,
                TobIDs.NYLO_BOSS_RANGE_HM,
                TobIDs.NYLO_BOSS_MAGE_HM,
                TobIDs.NYLO_BOSS_MELEE_SM,
                TobIDs.NYLO_BOSS_RANGE_SM,
                TobIDs.NYLO_BOSS_MAGE_SM,
                TobIDs.SOTETSEG_ACTIVE,
                TobIDs.SOTETSEG_ACTIVE_HM,
                TobIDs.SOTETSEG_ACTIVE_SM,
                TobIDs.XARPUS_P23,
                TobIDs.XARPUS_P23_HM,
                TobIDs.XARPUS_P23_SM,
                TobIDs.VERZIK_P1,
                TobIDs.VERZIK_P2,
                TobIDs.VERZIK_P3,
                TobIDs.VERZIK_P1_HM,
                TobIDs.VERZIK_P2_HM,
                TobIDs.VERZIK_P3_HM,
                TobIDs.VERZIK_P1_SM,
                TobIDs.VERZIK_P2_SM,
                TobIDs.VERZIK_P3_SM,
        };
        return (Arrays.stream(bosses).anyMatch(p -> p == ID));
    }

    public static String time(int ticks)
    {
        return time((double) ticks);
    }

    public static String value(int value)
    {
        return (value < 0) ? "-" : String.valueOf(value);
    }

    public static boolean isTime(String value)
    {
        try
        {
            DataPoint pointFound = DataPoint.getValue(value);
            if(pointFound != DataPoint.UNKNOWN)
            {
                return pointFound.isTime();
            }
            else return value.contains("Time") || value.contains("Entry") || value.contains("Split") || value.contains("Duration");
        } catch (Exception e)
        {
            return false;
        }
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

    public static Integer ticks(String time)
    {
        if (time.equals("-"))
        {
            return -1;
        }
        String[] split = time.split(":");
        int tickCount = 0;
        try
        {
            if (split.length == 2)
            {
                tickCount += Integer.parseInt(split[0]) * 100;
                tickCount += (int) ((Double.parseDouble(split[1].split("s")[0])) / .6);
            }
            if (split.length == 1)
            {
                tickCount += (int) ((Double.parseDouble(split[0].split("s")[0])) / .6);
            }
        } catch (Exception e)
        {
            log.info("Failed to convert time to ticks: " + time);
        }
        return tickCount;
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

    public static boolean crossedLineNew(int region, Point start, Point end, boolean vertical, Client client)
    {
        if (inRegion(client, region))
        {
            for (Player p : client.getPlayers())
            {
                WorldPoint wp = p.getWorldLocation();
                int x = wp.getRegionX();
                int y = wp.getRegionY();
                if (vertical)
                {
                    if (x == start.getX() && y >= start.getY() && y <= end.getY())
                    {
                        return true;
                    }
                } else
                {
                    if (y == start.getY() && x >= start.getX() && x <= end.getX())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

	public static boolean playerPastLine(int region, int regionXY, boolean vertical, Client client, boolean inverted)
	{
		return playerPastLine(region, regionXY, vertical, client, inverted, false);
	}

    public static boolean playerPastLine(int region, int regionXY, boolean vertical, Client client, boolean inverted, boolean scabaras)
    {
        if (inRegion(client, region))
        {
            for (Player p : client.getPlayers())
            {
                WorldPoint wp = p.getWorldLocation();
                int x = wp.getRegionX();
                int y = wp.getRegionY();
				if(scabaras && x == 32 && y == 32)
				{
					continue;
				}
                if (vertical)
                {
					if(inverted)
					{
						if(x < regionXY)
						{
							return true;
						}
					}
					else
					{
						if (x > regionXY)
						{
							return true;
						}
					}
                } else
                {
					if(inverted)
					{
						if(y < regionXY)
						{
							return true;
						}
					}
					else
					{
						if (y > regionXY)
						{
							return true;
						}
					}
                }
            }
        }
        return false;
    }

    public static boolean inRegion(Client client, Room... rooms)
    {
        for (Room room : rooms)
        {
            if (inRegion(client, room))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean inRegion(Client client, Room room)
    {
        if (room.raidType.equals(RaidType.COX)) //todo flag
        {
            return false;
        }
        return inRegion(client, room.regions);
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

    public static List<RaidRoom> getVariations(String name)
    {
        List<RaidRoom> rooms = new ArrayList<>();
        String[] words = name.split(" ");
        if(words.length > 2)
        {
            String[] vals = words[2].split("-");
            if(vals.length == 2)
            {
                try
                {
                    int start = Integer.parseInt(vals[0]);
                    int end = Integer.parseInt(vals[1]);
                    for(int i = start; i < end+1; i++)
                    {
                        rooms.add(RaidRoom.getRoom(words[0] + " Wave " + i));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return rooms;
    }
}
