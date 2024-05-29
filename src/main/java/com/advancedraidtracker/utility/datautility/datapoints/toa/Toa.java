package com.advancedraidtracker.utility.datautility.datapoints.toa;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.ParseInstruction;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.util.Text;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.advancedraidtracker.constants.ParseType.*;
import static com.advancedraidtracker.constants.RaidRoom.*;
import static com.advancedraidtracker.utility.datautility.DataPoint.*;
import static com.advancedraidtracker.utility.datautility.DataPoint.VERZIK_TIME;

@Slf4j
public class Toa extends Raid
{
    public Toa(Path filepath, List<LogEntry> raidData)
    {
        super(filepath, raidData);
    }

    @Override
    public String getRoomStatus()
    {
        return roomStatus;
    }

    @Override
    public RaidType getRaidType()
    {
        return RaidType.TOA;
    }

    @Override
    public boolean isAccurate()
    {
        return true;
    }

    @Override
    public int getTimeSum()
    {
        int time = 0;
        for (RaidRoom room : RaidRoom.values())
        {
            if (room.isTOA())
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
    public int getChallengeTime()
    {
        return getIfAccurate(APMEKEN_TIME) + getIfAccurate(BABA_TIME) + getIfAccurate(SCABARAS_TIME) + getIfAccurate(KEPHRI_TIME)
                + getIfAccurate(HET_TIME) + getIfAccurate(AKKHA_TIME) + getIfAccurate(CRONDIS_TIME) + getIfAccurate(ZEBAK_TIME)
                + getIfAccurate(WARDENS_TIME);
    }

    @Override
    public boolean getOverallTimeAccurate()
    {
        for (RaidRoom room : RaidRoom.values())
        {
            if (room.isTOA())
            {
                if (!getRoomAccurate(room))
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected boolean parseLogEntry(LogEntry entry)
    {
        super.parseLogEntry(entry);
        try
        {
            for (ParseInstruction instruction : entry.logEntry.parseInstructions)
            {
                if (Objects.requireNonNull(instruction.type) == LEFT_RAID)
                {
                    if (wasReset)
                    {
                        if (lastRoom.equals(WARDENS.name))
                        {
                            addColorToStatus(green);
                            completed = true;
                        } else
                        {
                            addColorToStatus(orange);
                        }
                    } else
                    {
                        addColorToStatus(red);
                    }
                } else if (Objects.requireNonNull(instruction.type) == RAID_SPECIFIC)
                {
                    if (entry.logEntry.equals(LogID.ENTERED_NEW_TOA_REGION))
                    {
                        if (entry.getValue("Region").equals("TOA Nexus") && !roomStatus.isEmpty())
                        {
                            addColorToStatus(green);
                        }
                    }
                } else if (Objects.requireNonNull(instruction.type) == ROOM_START_FLAG)
                {
                    if (entry.logEntry.getRoom().isTOAPath())
                    {
                        roomStatus += roomLetters.get(entry.logEntry.getRoom().name);
                    }
                } else if (Objects.requireNonNull(instruction.type) == MANUAL_PARSE)
                {
                    if (entry.logEntry.equals(LogID.TOA_WARDENS_SKULLS_STARTED))
                    {
                        for (int i = 1; i < 5; i++)
                        {
                            if (data.get(DataPoint.getValue("Wardens Skull " + i + " Split")) < 1)
                            {
                                data.set(DataPoint.getValue("Wardens Skull " + i + " Split"), entry.getFirstInt());
                                break;
                            }
                        }
                    }
                    if (entry.logEntry.equals(LogID.TOA_WARDENS_SKULLS_ENDED))
                    {
                        for (int i = 1; i < 5; i++)
                        {
                            if (data.get(DataPoint.getValue("Wardens Skull " + i + " Duration")) < 1)
                            {
                                data.set(DataPoint.getValue("Wardens Skull " + i + " Duration"), entry.getFirstInt() - data.get(DataPoint.getValue("Wardens Skull " + i + " Split")));
                                break;
                            }
                        }
                    }
					else if(entry.logEntry.equals(LogID.TOA_WARDENS_FINISHED))
					{
						Date end = new Date(entry.ts);
						Long timeDifference = end.getTime()-getDate().getTime();
						int ticks = (int)(timeDifference/600.0);
						data.set(OVERALL_TIME, ticks);
						data.set(TIME_OUTSIDE_ROOMS, get(OVERALL_TIME) - get(CHALLENGE_TIME)); //todo not accurate potentially?
					}
                }
            }
        } catch (Exception ignored)
        {
            ignored.printStackTrace();
        }
        return true;
    }

    @Override
    public String getScaleString()
    {
        return super.getScaleString() + " (" + (get(DataPoint.TOA_INVOCATION_LEVEL)) + ")";
    }

    @Override
    public boolean getRoomAccurate(RaidRoom room) //todo logout/login?
    {
        return room.isTOA();
    }


    @Override
    public String getSplits()
    {
        LocalDate date = getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault());
        String split = getScaleString() + ", " + formatter.format(date) + "\n";

        int sum = 0;
        DataPoint currentPoint;

        currentPoint = APMEKEN_TIME;
        if(get(currentPoint) > 0)
        {
            split += currentPoint.room.name + ": " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(currentPoint)) + ")\n";
            sum += get(currentPoint);
        }

        currentPoint = BABA_TIME;
        if(get(currentPoint) > 0)
        {
            split += currentPoint.room.name + ": " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(currentPoint)) + ")\n";
            sum += get(currentPoint);
        }

        currentPoint = SCABARAS_TIME;
        if(get(currentPoint) > 0)
        {
            split += currentPoint.room.name + ": " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(currentPoint)) + ")\n";
            sum += get(currentPoint);
        }

        currentPoint = KEPHRI_TIME;
        if(get(currentPoint) > 0)
        {
            split += currentPoint.room.name + ": " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(currentPoint)) + ")\n";
            sum += get(currentPoint);
        }

        currentPoint = HET_TIME;
        if(get(currentPoint) > 0)
        {
            split += currentPoint.room.name + ": " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(currentPoint)) + ")\n";
            sum += get(currentPoint);
        }

        currentPoint = AKKHA_TIME;
        if(get(currentPoint) > 0)
        {
            split += currentPoint.room.name + ": " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(currentPoint)) + ")\n";
            sum += get(currentPoint);
        }

        currentPoint = CRONDIS_TIME;
        if(get(currentPoint) > 0)
        {
            split += currentPoint.room.name + ": " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(currentPoint)) + ")\n";
            sum += get(currentPoint);
        }

        currentPoint = ZEBAK_TIME;
        if(get(currentPoint) > 0)
        {
            split += currentPoint.room.name + ": " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(currentPoint)) + ")\n";
            sum += get(currentPoint);
        }

        currentPoint = WARDENS_TIME;
        if(get(currentPoint) > 0)
        {
            split += currentPoint.room.name + ": " + RoomUtil.time(sum) + " (+" + RoomUtil.time(get(currentPoint)) + ")\n";
            sum += get(currentPoint);
        }

        if (completed)
        {
            split += "Duration (Completion): " + RoomUtil.time(getChallengeTime()) + " (+" + RoomUtil.time(get(WARDENS_TIME)) + ")";
        } else
        {
            split += "Duration (" + Text.removeTags(roomStatus) + "): " + RoomUtil.time(getChallengeTime());
        }
        return split;
    }

}
