package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.ParseType;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.MultiRoomDataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.toa.Toa;
import com.advancedraidtracker.utility.datautility.datapoints.tob.Tob;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.util.Text;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.advancedraidtracker.utility.datautility.DataPoint.*;

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
     * Tracks if room start times were accurate due to spectating mid-room, logging in mid-room, etc
     */
    private final Map<RaidRoom, Boolean> roomStartAccurate = new HashMap<>();
    /**
     * Tracks if room ends times were accurate due to leaving spectate mid-room, logging out mid-room, etc
     */
    private final Map<RaidRoom, Boolean> roomEndAccurate = new HashMap<>();

    /**
     * Parsers for specific rooms
     */
    protected Map<RaidRoom, RoomParser> roomParsers;
    private List<LogEntry> raidData;
    DefaultParser defaultParser;

    protected Raid(Path filepath, List<LogEntry> raidData)
    {
        defaultParser = new DefaultParser();
        roomParsers = new HashMap<>();
        this.raidData = raidData;
        date = new Date(0L); //figure out why dates dont parse properly on some raids
        this.filepath = filepath;
        this.players = new HashSet<>();
    }

    public RoomParser getParser(RaidRoom room)
    {
        return roomParsers.getOrDefault(room, defaultParser);
    }

    /**
     * @param room Room to set accurate start
     */
    private void setRoomStartAccurate(RaidRoom room)
    {
        roomStartAccurate.put(room, true);
    }

    /**
     * @param room Room to set accurate end
     */
    private void setRoomEndAccurate(RaidRoom room)
    {
        roomEndAccurate.put(room, true);
    }

    /**
     * @param room room to check accuracy of
     * @return returns true if both the start AND end were recorded as accurate
     */
    public boolean getRoomAccurate(RaidRoom room)
    {
        return roomStartAccurate.getOrDefault(room, false) && roomEndAccurate.getOrDefault(room, false);
    }

    /**
     * @param room room to check accuracy of
     * @return returns true if both the start OR end were recorded as accurate
     */
    public boolean getRoomPartiallyAccurate(RaidRoom room)
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
            case ENTERED_TOA:
                date = new Date(entry.getTs());
                break;
            case PARTY_MEMBERS:
            case TOA_PARTY_MEMBERS:
                for(int i = 1; i < 9; i++)
                {
                    String player = entry.getValue("Player"+i);
                    if(!player.isEmpty())
                    {
                        players.add(Text.toJagexName(player));
                    }
                }
                break;
        }
    }

    public void parseAllEntries()
    {
        raidData.removeIf(this::parseLogEntry);
    }

    /**
     * Uses the operators encoded in the enum to automatically parse the logID to the corresponding datapoint(s)
     * @param entry entry to parse
     */
    protected boolean parseLogEntry(LogEntry entry)
    {
        try
        {
            Object[] args = entry.getLogEntry().arguments;
            RaidRoom room = entry.getLogEntry().getRoom();
            RoomParser parser = getParser(room);
            for (int i = 0; i < args.length; i++)
            {
                if (args[i] instanceof ParseType)
                {
                    ParseType parseType = (ParseType) args[i];
                    switch ((ParseType) args[i])
                    {
                        case ADD_TO_VALUE:
                            parser.data.incrementBy(args[i + 1], entry.getFirstInt(), entry.getValue("Player"));
                            break;
                        case INCREMENT:
                                parser.data.increment(args[i + 1], entry.getValue("Player"));
                            break;
                        case INCREMENT_IF_GREATER_THAN:
                            if (((args[i + 2] instanceof String) ? entry.getValueAsInt((String) args[i + 2]) : parser.data.get((DataPoint) args[i + 2])) > (Integer) args[i + 3])
                            {
                                parser.data.increment(args[i + 1], entry.getValue("Player"));
                            }
                            break;
                        case INCREMENT_IF_LESS_THAN:
                            if (((args[i + 2] instanceof String) ? entry.getValueAsInt((String) args[i + 2]) : parser.data.get((DataPoint) args[i + 2])) < (Integer) args[i + 3])
                            {
                                parser.data.increment(args[i + 1], entry.getValue("Player"));
                            }
                            break;
                        case SET:
                            if (args[i + 1] instanceof DataPoint)
                            {
                                parser.data.set((DataPoint) args[i + 1], entry.getFirstInt());
                            } else if (args[i + 1] instanceof MultiRoomDataPoint)
                            {
                                parser.data.set((MultiRoomDataPoint) args[i + 1], entry.getFirstInt());
                            }
                            break;
                        case SUM:
                            parser.data.set((DataPoint) args[i + 1], parser.data.get((DataPoint) args[i + 2]) + parser.data.get((DataPoint) args[i + 3]));
                            break;
                        case SPLIT:
                            if(args[i+1] instanceof DataPoint)
                            {
                                parser.data.set((DataPoint) args[i + 1], entry.getFirstInt());
                                parser.data.set((DataPoint) args[i + 2], parser.data.get((DataPoint) args[i + 1]) - parser.data.get((DataPoint) args[i + 3]));
                            }
                            else if(args[i+1] instanceof MultiRoomDataPoint)
                            {
                                parser.data.set((MultiRoomDataPoint) args[i + 1], entry.getFirstInt());
                                parser.data.set((DataPoint) args[i + 2], parser.data.get((MultiRoomDataPoint) args[i + 1], entry.getLogEntry().getRoom()) - parser.data.get((DataPoint) args[i + 3]));
                            }
                            break;
                        case DWH:
                            parser.data.dwh((MultiRoomDataPoint) args[i + 1]);
                            break;
                        case BGS:
                            parser.data.bgs((MultiRoomDataPoint) args[i + 1], entry.getFirstInt());
                            break;
                        case ROOM_END_FLAG:
                            //not sure what this needs to be used for yet
                            break;
                        case ROOM_START_FLAG: //todo use this to track raid status
                            break;
                        case AGNOSTIC:
                            handleRaidAgnosticLogEntry(entry);
                            break;
                        case RAID_SPECIFIC: //let it be handled by the raid specific parser TODO
                            return false;
                        case ACCURATE_START:
                            setRoomStartAccurate(entry.getLogEntry().getRoom());
                            break;
                        case ACCURATE_END:
                            setRoomEndAccurate(entry.getLogEntry().getRoom());
                            break;
                        case MANUAL_PARSE:
                            return false;
                    }
                    i += parseType.offset;
                }
            }
            return true;
        }
        catch (Exception e)
        {
            log.info("Could not parse: " + String.join(",", entry.getLines()));
            e.printStackTrace();
            return false;
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
                    ret.parseAllEntries();
                }
                else if(entry.getLogEntry() == LogID.LEFT_TOA)
                {
                    ret = new Toa(path, currentRaid);
                    ret.parseAllEntries();
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
        /*if(data.get(TOA_INVOCATION_LEVEL) >= 0)
        {
            scale += " (" + data.get(TOA_INVOCATION_LEVEL) + ")"; //todo do this as an override in Toa
        }*/
        return scale;
    }

    public int get(String datapoint)
    {
        return getParser(getValue(datapoint).room).data.get(datapoint);
    }

    public void setIndex(int index)
    {
        defaultParser.data.set(RAID_INDEX, index);
    }

    public int getIndex()
    {
        return defaultParser.data.get(RAID_INDEX);
    }
}
