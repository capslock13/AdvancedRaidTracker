package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.ParseType;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.constants.TOBRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.MultiRoomDataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.tob.Tob;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.advancedraidtracker.utility.datautility.DataPoint.RAID_INDEX;
import static com.advancedraidtracker.utility.datautility.DataPoint.TOA_INVOCATION_LEVEL;

@Slf4j
public abstract class Raid
{
    /**
     * Path to the log file.
     */
    @Getter
    private final Path filepath;

    /**
     * Date the raid was run.
     */
    @Getter
    protected Date date;

    /**
     * Time for the entire raid.
     */
    @Getter
    protected int overallTime;

    /**
     * Time spent inside rooms.
     */
    @Getter
    protected int challengeTime;

    /**
     * Time spent outside rooms.
     */
    @Getter
    protected int afkTime;

    /**
     * If all the times are accurate.
     */
    @Getter
    protected boolean accurate;

    /**
     * Was the raid completed.
     */
    @Getter
    protected boolean completed;

    /**
     * If the player was in a runelite party to get precise defence tracking.
     */
    @Getter
    protected boolean inParty;

    /**
     * Amount of players in the raid.
     */
    @Getter
    protected final Set<String> players;

    /**
     * Manages data for the raid
     */
    RaidDataManager data;

    /**
     * Tracks if room start times were accurate due to spectating mid room, logging in mid room, etc
     */
    private final Map<TOBRoom, Boolean> roomStartAccurate = new HashMap<>();
    /**
     * Tracks if room ends times were accurate due to leaving spectate mid room, logging out mid room, etc
     */
    private final Map<TOBRoom, Boolean> roomEndAccurate = new HashMap<>();

    protected Raid(Path filepath, List<LogEntry> raidData)
    {
        date = new Date(0L); //figure out why dates dont parse properly on some raids
        data = new RaidDataManager();
        this.filepath = filepath;
        this.players = new HashSet<>();
        for (LogEntry entry : raidData)
        {
            if(entry.getLogEntry().isSimple())
            {
                parseLogEntry(entry);
            }
        }
    }

    /**
     * @param room Room to set accurate start
     */
    private void setRoomStartAccurate(TOBRoom room)
    {
        roomStartAccurate.put(room, true);
    }

    /**
     * @param room Room to set accurate end
     */
    private void setRoomEndAccurate(TOBRoom room)
    {
        roomEndAccurate.put(room, true);
    }

    /**
     * @param room room to check accuracy of
     * @return returns true if both the start AND end were recorded as accurate
     */
    public boolean getRoomAccurate(TOBRoom room)
    {
        return roomStartAccurate.getOrDefault(room, false) && roomEndAccurate.getOrDefault(room, false);
    }

    /**
     * @param room room to check accuracy of
     * @return returns true if both the start OR end were recorded as accurate
     */
    public boolean getRoomPartiallyAccurate(TOBRoom room)
    {
        return roomStartAccurate.getOrDefault(room, false) || roomEndAccurate.getOrDefault(room, false);
    }

    /**
     * Handles log entries which are the same regardless of raid
     * @param entry raid agnostic log entry to be parsed
     */
    private void handleRaidAgnosticLogEntry(LogEntry entry)
    {
        switch (entry.getLogEntry())
        {
            case ENTERED_RAID:
                date = new Date(entry.getTs());
                break;
            case PARTY_MEMBERS:
                for(int i = 1; i < 9; i++)
                {
                    String player = entry.getValue("Player"+i);
                    if(!player.isEmpty())
                    {

                        players.add(player.replaceAll(String.valueOf((char) 160), String.valueOf((char) 32)));
                        log.info("Adding player: " + player);
                    }
                }
                break;
        }
    }

    /**
     * Uses the operators encoded in the enum to automatically parse the logID to the corresponding datapoint(s)
     * @param entry entry to parse
     */
    private void parseLogEntry(LogEntry entry)
    {
        try
        {
            Object[] args = entry.getLogEntry().arguments;
            TOBRoom currentRoom = null;
            for (int i = 0; i < args.length; i++)
            {
                if (args[i] instanceof ParseType)
                {
                    ParseType parseType = (ParseType) args[i];
                    switch ((ParseType) args[i])
                    {
                        case ADD_TO_VALUE:
                            data.incrementBy(args[i + 1], entry.getFirstInt(), entry.getValue("Player"), entry.getLogEntry().getRoom());
                            break;
                        case INCREMENT:
                                data.increment(args[i + 1], entry.getValue("Player"), entry.getLogEntry().getRoom());
                            break;
                        case INCREMENT_IF_GREATER_THAN:
                            if (((args[i + 2] instanceof String) ? entry.getValueAsInt((String) args[i + 2]) : data.get((DataPoint) args[i + 2])) > (Integer) args[i + 3])
                            {
                                data.increment(args[i + 1], entry.getValue("Player"), entry.getLogEntry().getRoom());
                            }
                            break;
                        case INCREMENT_IF_LESS_THAN:
                            if (((args[i + 2] instanceof String) ? entry.getValueAsInt((String) args[i + 2]) : data.get((DataPoint) args[i + 2])) < (Integer) args[i + 3])
                            {
                                data.increment(args[i + 1], entry.getValue("Player"), entry.getLogEntry().getRoom());
                            }
                            break;
                        case SET:
                            if (args[i + 1] instanceof DataPoint)
                            {
                                data.set((DataPoint) args[i + 1], entry.getFirstInt());
                            } else if (args[i + 1] instanceof MultiRoomDataPoint)
                            {
                                data.set((MultiRoomDataPoint) args[i + 1], entry.getFirstInt(), entry.getLogEntry().getRoom());
                            }
                            break;
                        case SUM:
                            data.set((DataPoint) args[i + 1], data.get((DataPoint) args[i + 2]) + data.get((DataPoint) args[i + 3]));
                            break;
                        case SPLIT:
                            if(args[i+1] instanceof DataPoint)
                            {
                                data.set((DataPoint) args[i + 1], entry.getFirstInt());
                                data.set((DataPoint) args[i + 2], data.get((DataPoint) args[i + 1]) - data.get((DataPoint) args[i + 3]));
                            }
                            else if(args[i+1] instanceof MultiRoomDataPoint)
                            {
                                data.set((MultiRoomDataPoint) args[i + 1], entry.getFirstInt(), entry.getLogEntry().getRoom());
                                data.set((DataPoint) args[i + 2], data.get((MultiRoomDataPoint) args[i + 1], entry.getLogEntry().getRoom()) - data.get((DataPoint) args[i + 3]));
                            }
                            break;
                        case DWH:
                            data.dwh((MultiRoomDataPoint) args[i + 1], entry.getLogEntry().getRoom());
                            break;
                        case BGS:
                            data.bgs((MultiRoomDataPoint) args[i + 1], entry.getFirstInt(), entry.getLogEntry().getRoom());
                            break;
                        case ROOM_END_FLAG:
                            //not sure what this needs to be used for yet
                            break;
                        case ROOM_START_FLAG: //todo use this to track raid status
                            currentRoom = entry.getLogEntry().getRoom();
                            break;
                        case AGNOSTIC:
                            handleRaidAgnosticLogEntry(entry);
                            return;
                        case RAID_SPECIFIC: //let it be handled by the raid specific parser TODO
                            return;
                        case ACCURATE_START:
                            setRoomStartAccurate(entry.getLogEntry().getRoom());
                            return;
                        case ACCURATE_END:
                            setRoomEndAccurate(entry.getLogEntry().getRoom());
                            return;
                        case MANUAL_PARSE:
                            return;
                    }
                    i += parseType.offset;
                }
            }
        }
        catch (Exception e)
        {
            log.info("Could not parse: " + String.join(",", entry.getLines()));
            e.printStackTrace();
        }
    }

    /**
     * Gets a raid from a single log file, current structure is that each
     * raid has its own log file.
     *
     * @param path path to log file
     * @return A raid for the log
     */
    public static Raid getRaid(Path path)
    {
        List<String> raidData = getRaidStrings(path);
        List<LogEntry> currentRaid = new ArrayList<>();
        Raid ret = null;
        for (String line : raidData)
        {
            String[] split = line.split(",", -1);
            LogEntry entry = new LogEntry(split);
            if(entry.getLogEntry().isSimple()) //Do not load chart data; that is pulled on demand
            {
                currentRaid.add(entry);
                if (entry.getLogEntry() == LogID.LEFT_TOB)
                {
                    ret = new Tob(path, currentRaid);
                }
            }
        }
        return ret;
    }

    /**
     * Goes through the file and returns an arraylist containing all of the lines in the file.
     *
     * TODO find out if this is something that should simply be done in `getRaid` to not parse it twice.
     * @param path Path to log file.
     * @return A list of all lines in the log file.
     */
    private static List<String> getRaidStrings(Path path)
    {
        List<String> lines = new ArrayList<>();
        File file = path.toFile();
        try
        {
            Scanner scanner = new Scanner(Files.newInputStream(file.toPath()));
            while (scanner.hasNextLine())
            {
                lines.add(scanner.nextLine());
            }
        } catch (Exception e)
        {
            System.err.println("Could not find file: " + path);
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
        return lines;
    }

    /**
     * Determines what the status of the raid was when it was left
     *
     * @return A string with the final status of the raid.
     */
    public abstract String getRoomStatus();

    /**
     * Determines what type of raid it is. Used as a shorthand for the UI.
     *
     * @return TOB/TOA/COX.
     */
    public abstract RaidType getRaidType();

    /**
     * @return Amount of players in a raid.
     */
    public int getScale()
    {
        return players.size();
    }

    /**
     * @return Scale String in non int form
     */
    public String getScaleString()
    {
        String scale;
        if (players.size() == 1)
        {
            scale = "Solo";
        } else if (players.size() == 2)
        {
            scale = "Duo";
        } else if (players.size() == 3)
        {
            scale = "Trio";
        } else
        {
            scale = players.size() + " Man";
        }
        if(data.get(TOA_INVOCATION_LEVEL) >= 0)
        {
            scale += " (" + data.get(TOA_INVOCATION_LEVEL) + ")";
        }
        return scale;
    }

    public void setIndex(int index)
    {
        data.set(RAID_INDEX, index);
    }

    public int getIndex()
    {
        return data.get(RAID_INDEX);
    }

    public int get(String value)
    {
        return data.get(value);
    }

    public int get(DataPoint value)
    {
        return data.get(value);
    }
}
