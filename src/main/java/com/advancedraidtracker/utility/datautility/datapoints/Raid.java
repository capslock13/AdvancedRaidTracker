package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.*;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.MultiRoomDataPoint;
import com.advancedraidtracker.utility.datautility.MultiRoomPlayerDataPoint;
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
     *
     * @param entry raid agnostic log entry to be parsed
     */
    private void handleRaidAgnosticLogEntry(LogEntry entry)
    {
        switch (entry.logEntry)
        {
            case ENTERED_RAID:
            case ENTERED_TOA:
                date = new Date(entry.ts);
                break;
            case PARTY_MEMBERS:
            case TOA_PARTY_MEMBERS:
                for (int i = 1; i < 9; i++)
                {
                    String player = entry.getValue("Player" + i);
                    if (!player.isEmpty())
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
     *
     * @param entry entry to parse
     */
    protected boolean parseLogEntry(LogEntry entry)
    {
        try
        {
            RaidRoom room = entry.logEntry.getRoom();
            RoomParser parser = getParser(room);
            for(ParseObject po : entry.logEntry.parseObjects)
            {
                log.info("Type: " + String.valueOf(po.type) + ", PointType: " + String.valueOf(po.pointType) + ", DataPoint1: " + String.valueOf(po.dataPoint1.name) + ", DataPoint2: " + String.valueOf(po.dataPoint2.name)
                + ", DataPoint3: " + String.valueOf(po.dataPoint3.name) + ", Marker: " + String.valueOf(po.marker) + ", Value: " + String.valueOf(po.value) + ", SRMPoint" + String.valueOf(po.srmdPoint.name) +
                        ", SRPDPoint: " + String.valueOf(po.srpdPoint.name) + ", MRDPoint: " + String.valueOf(po.mrdPoint.name) + ", " + "MDPDPoint: " + String.valueOf(po.mrpdPoint));
                switch (po.type)
                {
                    case ADD_TO_VALUE:
                        if(po.pointType.equals(MultiRoomPlayerDataPoint.class.toString()))
                        {
                            parser.data.incrementBy(po.mrpdPoint, entry.getFirstInt(), entry.getValue("Player"));
                        }
                        else if(po.pointType.equals(DataPoint.class.toString()))
                        {
                            parser.data.incrementBy(po.dataPoint1, entry.getFirstInt(), entry.getValue("Player"));
                        }
                        break;
                    case INCREMENT:
                        if(po.pointType.equals(MultiRoomPlayerDataPoint.class.toString()))
                        {
                            parser.data.increment(po.mrpdPoint, entry.getValue("Player"));
                        }
                        else if(po.pointType.equals(DataPoint.class.toString()))
                        {
                            parser.data.increment(po.dataPoint1, entry.getValue("Player"));
                        }
                        break;
                    case INCREMENT_IF_GREATER_THAN:
                        if (((po.marker != null) ? Integer.parseInt(entry.values.get(po.marker)) : parser.data.get(po.dataPoint1)) > po.value)
                        {
                            parser.data.increment(po.dataPoint1, entry.getValue("Player"));
                        }
                        break;
                    case INCREMENT_IF_LESS_THAN:
                        if (((po.marker != null) ? Integer.parseInt(entry.values.get(po.marker)) : parser.data.get(po.dataPoint1)) < po.value)
                        {
                            parser.data.increment(po.dataPoint1, entry.getValue("Player"));
                        }
                        break;
                    case SET:
                        if (po.dataPoint1 != null)
                        {
                            parser.data.set(po.dataPoint1, entry.getFirstInt());
                        }
                        else if (po.mrdPoint != null)
                        {
                            parser.data.set(po.mrdPoint, entry.getFirstInt());
                        }
                        break;
                    case SUM:
                        parser.data.set(po.dataPoint1, parser.data.get(po.dataPoint2) + parser.data.get(po.dataPoint3));
                        break;
                    case SPLIT:
                        if (po.pointType.equals(DataPoint.class.toString()))
                        {
                            parser.data.set(po.dataPoint1, entry.getFirstInt());
                            parser.data.set(po.dataPoint2, parser.data.get(po.dataPoint1) - parser.data.get(po.dataPoint3));
                        } else if (po.pointType.equals(MultiRoomDataPoint.class.toString()))
                        {
                            parser.data.set(po.mrdPoint, entry.getFirstInt());
                            parser.data.set(po.dataPoint1, parser.data.get(po.mrdPoint, entry.logEntry.getRoom()) - parser.data.get(po.dataPoint2));
                        }
                        break;
                    case DWH:
                        parser.data.dwh(po.mrdPoint);
                        break;
                    case BGS:
                        parser.data.bgs(po.mrdPoint, entry.getFirstInt());
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
                        setRoomStartAccurate(entry.logEntry.getRoom());
                        break;
                    case ACCURATE_END:
                        setRoomEndAccurate(entry.logEntry.getRoom());
                        break;
                    case MANUAL_PARSE:
                        return false;
                }
            }
            return true;
        } catch (Exception e)
        {
            log.info("Could not parse: " + String.join(",", entry.lines));
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
            if(split[3].equals("801") || split[3].equals("975") || split[3].equals("8") || split[3].equals("998") || split[3].equals("999")
            || split[3].equals("976") || split[3].equals("16") || split[3].equals("26") || split[3].equals("46"))
                //legacy or otherwise ignored values to be excluded while testing parser
            {
                continue; //todo remove
            }
            LogEntry entry = new LogEntry(split);
            if (entry.logEntry.isSimple()) //Do not load chart data; that is pulled on demand
            {
                currentRaid.add(entry);
                if (entry.logEntry == LogID.LEFT_TOB)
                {
                    ret = new Tob(path, currentRaid);
                    ret.parseAllEntries();
                    for(RaidRoom room : RaidRoom.values())
                    {
                        RoomParser parser = ret.getParser(room);
                        if(!(parser instanceof DefaultParser))
                        {
                            log.info("Dumping room: " + room.name());
                            parser.data.dumpValues();
                        }
                        else
                        {
                            log.info(room.name() + " has default parser");
                        }
                    }
                } else if (entry.logEntry == LogID.LEFT_TOA)
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
     * <p>
     * TODO find out if this is something that should simply be done in `getRaid` to not parse it twice.
     *
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
