package com.advancedraidtracker.utility.datautility.datapoints.tob;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.ParseInstruction;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;
import com.advancedraidtracker.utility.wrappers.StringInt;
import lombok.Getter;
import net.runelite.client.util.Text;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import static com.advancedraidtracker.constants.ParseType.*;
import static com.advancedraidtracker.constants.RaidRoom.*;
import static com.advancedraidtracker.utility.datautility.DataPoint.*;

public class Tob extends Raid
{
	@Getter
	private final ArrayList<StringInt> maidenCrabs = new ArrayList<>();


	/**
     * Enum for what difficulty the raid is.
     */
    public enum RaidMode
    {
        ENTRY,
        NORMAL,
        HARD
    }


    @Getter
    private RaidMode mode;


    public Tob(Path logfile, List<LogEntry> raidData)
    {
        super(logfile, raidData);
    }

    @Override
    public int getChallengeTime()
    {
        return getIfAccurate(MAIDEN_TIME) + getIfAccurate(BLOAT_TIME) + getIfAccurate(NYLOCAS_TIME) + getIfAccurate(SOTETSEG_TIME) + getIfAccurate(XARPUS_TIME) + getIfAccurate(VERZIK_TIME);
    }

    @Override
    public String getSplits()
    {
        LocalDate date = getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault());
        String split = getScaleString() + ", " + formatter.format(date) + "\n";
        int sum = 0;
        if (get(MAIDEN_TIME) > 0 && getRoomAccurate(MAIDEN))
        {
            split += "Maiden: " + RoomUtil.time(get(MAIDEN_TIME)) + "\n";
            sum += get(MAIDEN_TIME);
        }
        if (get(BLOAT_TIME) > 0 && getRoomAccurate(BLOAT))
        {
            split += "Bloat, Split: " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(BLOAT_TIME)) + ")\n";
            sum += get(BLOAT_TIME);
        }
        if (get(NYLOCAS_TIME) > 0 && getRoomAccurate(NYLOCAS))
        {
            split += "Nylo, Split: " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(NYLOCAS_TIME)) + ")\n";
            sum += get(NYLOCAS_TIME);
        }
        if (get(SOTETSEG_TIME) > 0 && getRoomAccurate(SOTETSEG))
        {
            split += "Sotetseg, Split: " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(SOTETSEG_TIME)) + ")\n";
            sum += get(SOTETSEG_TIME);
        }
        if (get(XARPUS_TIME) > 0 && getRoomAccurate(XARPUS))
        {
            split += "Xarpus, Split: " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(XARPUS_TIME)) + ")\n";
            sum += get(XARPUS_TIME);
        }
        if (get(VERZIK_TIME) > 0 && getRoomAccurate(VERZIK))
        {
            split += "Verzik, Split: " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(VERZIK_TIME)) + ")\n";
            sum += get(VERZIK_TIME);
        }
        if (completed)
        {
            split += "Duration (Completion): " + RoomUtil.time(getChallengeTime()) + " (+" + RoomUtil.time(get(VERZIK_TIME)) + ")";
        } else
        {
            split += "Duration (" + Text.removeTags(roomStatus) + "): " + RoomUtil.time(getChallengeTime());
        }
        return split;
    }

    @Override
    public boolean getRoomAccurate(RaidRoom room)
    {
        if(room == MAIDEN)
        {
            return roomStarts.contains("Maiden") && super.getRoomAccurate(room);
        }
        else
        {
            return super.getRoomAccurate(room);
        }
    }

    @Override
    public boolean isAccurate()
    {
        return (getRoomAccurate(MAIDEN) && getRoomAccurate(BLOAT) && getRoomAccurate(NYLOCAS) && getRoomAccurate(SOTETSEG) && getRoomAccurate(XARPUS) && getRoomAccurate(VERZIK));
    }

    @Override
    public void parseAllEntries()
    {
        super.parseAllEntries();
    }

    @Override
    protected boolean parseLogEntry(LogEntry entry)
    {
        try
        {
            for (ParseInstruction instruction : entry.logEntry.parseInstructions)
            {
                if (Objects.requireNonNull(instruction.type) == LEFT_RAID)
                {
                    if (wasReset)
                    {
                        if (lastRoom.equals(VERZIK.name) || lastRoom.equals(WARDENS.name))
                        {
                            roomStatus = green + "Completion";
                            completed = true;
                        } else
                        {
                            roomStatus = yellow + lastRoom + " Reset";
                        }
                    } else
                    {
                        roomStatus = red + currentRoom + " Wipe";
                    }
                } else if (Objects.requireNonNull(instruction.type) == MANUAL_PARSE)
                {
                    if (entry.logEntry.equals(LogID.VERZIK_P2_REDS_PROC))
                    {
                        if (data.get(VERZIK_REDS_SPLIT) < 1)
                        {
                            data.set(VERZIK_REDS_SPLIT, entry.getFirstInt());
                            data.set(VERZIK_P2_TILL_REDS, entry.getFirstInt() - data.get(VERZIK_P2_SPLIT));
                        }
                    } else if (entry.logEntry.equals(LogID.CRAB_HEALED_MAIDEN))
                    {
                        int maxHP = 0;
                        switch (getScale())
                        {
                            case 1:
                            case 2:
                            case 3:
                                maxHP = 150;
                                break;
                            case 4:
                                maxHP = 174;
                                break;
                            case 5:
                                maxHP = 200;
                                break;
                        }
                        if (Integer.parseInt(entry.getValue("Damage")) == maxHP)
                        {
                            data.increment(MAIDEN_CRABS_LEAKED_FULL_HP);
                        }
                    } else if (entry.logEntry.equals(LogID.PLAYER_DIED))
                    {
                        if (data.get(MAIDEN_TIME) > 0 && data.getList(BLOAT_DOWNS).isEmpty())
                        {
                            data.increment(BLOAT_FIRST_WALK_DEATHS, entry.getValue("Player"));
                        }
                    } else if (entry.logEntry.equals(LogID.BLOAT_DESPAWN))
                    {
                        if (data.get(BLOAT_DOWNS) > 0)
                        {
                            data.set(BLOAT_FIRST_DOWN_TIME, data.getList(BLOAT_DOWNS).get(0));
                        }
                    } else if (entry.logEntry.equals(LogID.BLOAT_HP_1ST_DOWN))
                    {
                        data.set(DataPoint.BLOAT_HP_FIRST_DOWN, Integer.parseInt(entry.getValue("Bloat HP")) / 10);
                    } else if (entry.logEntry.equals(LogID.XARPUS_HEAL))
                    {
                        int healAmount = 0;
                        switch (getScale())
                        {
                            case 1:
                                healAmount = 20;
                                break;
                            case 2:
                                healAmount = 16;
                                break;
                            case 3:
                                healAmount = 12;
                                break;
                            case 4:
                                healAmount = 9;
                                break;
                            case 5:
                                healAmount = 8;
                                break;
                        }
                        data.incrementBy(XARP_HEALING, healAmount);
                    }
					else if (entry.logEntry.equals(LogID.BLOAT_SCYTHE_1ST_WALK))
                    {
                        if (data.get(BLOAT_DOWNS) == 0)
                        {
                            data.increment(BLOAT_FIRST_WALK_SCYTHES, entry.getValue("Player"));
                        }
                    }
					else if(entry.logEntry.equals(LogID.CRAB_LEAK))
					{
						maidenCrabs.add(new StringInt(entry.getValue("Description"), entry.getValueAsInt("Health")));
					}
					else if(entry.logEntry.equals(LogID.HAMMER_HIT) && currentRoom.equals("Sotetseg"))
					{
						if(get(SOTE_M2_SPLIT) > 0)
						{
							data.increment(SOTE_SPECS_P3);
						}
						else if(get(SOTE_M1_SPLIT) > 0)
						{
							data.increment(SOTE_SPECS_P2);
						}
						else
						{
							data.increment(SOTE_SPECS_P1);
						}
						data.increment(SOTE_SPECS_TOTAL);
					}
					else if(entry.logEntry.equals(LogID.HAMMER_ATTEMPTED) && currentRoom.equals("Sotetseg"))
					{
						data.increment(SOTE_SPECS_ATTEMPTED_TOTAL);
					}
                }
            }
        } catch (Exception ignored)
        {

        }
        return super.parseLogEntry(entry);
    }

    @Override
    public int getTimeSum()
    {
        int time = 0;
        for (RaidRoom room : RaidRoom.values())
        {
            if (room.isTOB())
            {
                if (getRoomAccurate(room))
                {
                    int val = get(room.name + " Time");
                    time += (val == -1) ? 0 : val;
                }
            }
        }
        return time;
    }

    @Override
    public boolean getOverallTimeAccurate()
    {
        for (RaidRoom room : RaidRoom.values())
        {
            if (room.isTOB())
            {
                if (!getRoomAccurate(room))
                {
                    return false;
                }
            }
        }
        return true;
    }

	public boolean getCompletedRoomsAccurate()
	{
		for(RaidRoom room : RaidRoom.values())
		{
			if(room.isTOB() && getRoomPartiallyAccurate(room) && !getRoomAccurate(room))
			{
				return false;
			}
		}
		return true;
	}

    @Override
    public String getRoomStatus()
    {
        return roomStatus + (getCompletedRoomsAccurate() ? "" : "*");
    }

    /**
     * Checks whether a room has started.
     *
     * @param entry Log entry to compare
     * @return true if it has begun, false if not.
     */
    private boolean didRoomStart(LogID entry)
    {
        return false;
    }


    @Override
    public RaidType getRaidType()
    {
        return RaidType.TOB;
    }
}
