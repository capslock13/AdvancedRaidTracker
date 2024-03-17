package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.*;
import com.advancedraidtracker.utility.datautility.*;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.util.Text;

import java.nio.file.Path;
import java.util.*;

import static com.advancedraidtracker.constants.RaidRoom.*;
import static com.advancedraidtracker.utility.datautility.DataPoint.*;

@Slf4j
public abstract class Raid
{
    protected Map<String, String> roomLetters = ImmutableMap.of("Scabaras", "K", "Apmeken", "B", "Het", "A", "Crondis", "Z", "Wardens", "W");
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
    protected boolean accurate; //todo

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
    private final List<LogEntry> raidData;
    DefaultParser defaultParser;
    UnknownParser unknownParser;

    public boolean isHardMode = false;//todo move these to raid specific
    public boolean isStoryMode = false;
    public boolean isSpectate = false;

    protected String red = "<html><font color='#FF0000'>"; //todo convert along with usages to colors (looking @ you fisu)
    protected String green = "<html><font color='#44AF33'>";
    protected String orange = "<html><font color='#FF7733'>";
    protected String yellow = "<html><font color='#FFFF33'>";

    protected boolean wasReset = false; //todo idk where these belong tbh
    protected String lastRoom = "";
    protected String roomStatus = orange;

    public abstract int getTimeSum();

    /**
     * Used for sorting to have String.toCompare method compared to the set
     * @return player string e.g. Player1,Player2,Player3,Player4
     */
    public String getPlayerString()
    {
        return Text.toJagexName(String.join(",", getPlayers()));
    }

    @Getter
    private boolean spectated;

    protected Raid(Path filepath, List<LogEntry> raidData)
    {
        defaultParser = new DefaultParser();
        unknownParser = new UnknownParser();
        roomParsers = new HashMap<>();
        roomParsers.put(ANY, defaultParser);
        this.raidData = raidData;
        date = new Date(0L); //todo figure out why dates dont parse properly on some raids
        this.filepath = filepath;
        this.players = new LinkedHashSet<>();
    }

    public boolean getOverallTimeAccurate()
    {
        return false;
    }

    public int get(DataPoint point, RaidRoom room)
    {
        int sum = 0;
        if(point.playerSpecific)
        {
            RoomDataManager rdm = getParser(room).data;
            for(String player : rdm.playerSpecificMap.get(point).keySet())
            {
                sum += rdm.playerSpecificMap.get(point).get(player);
            }
        }
    }

    public int get(DataPoint point)
    {
        if(point.room.equals(RaidRoom.ALL))
        {
            int sum = 0;
            for(RaidRoom room : RaidRoom.values())
            {
                if(point.playerSpecific)
                {
                    RoomDataManager rdm = getParser(room).data;
                    for(String player : rdm.playerSpecificMap.get(point).keySet())
                    {
                        sum += rdm.playerSpecificMap.get(point).get(player);
                    }
                }
                else
                {
                    sum += getParser(room).data.get(point);
                }
            }
            return sum;
        }
        else
        {
            if(point.playerSpecific)
            {
                int sum = 0;
                RoomDataManager rdm = getParser(point.room).data;
                if(rdm.playerSpecificMap.containsKey(point))
                {
                    for (String player : rdm.playerSpecificMap.get(point).keySet())
                    {
                        sum += rdm.playerSpecificMap.get(point).get(player);
                    }
                }
                return sum;
            }
            else
            {
                return getParser(point.room).data.get(point);
            }
        }
    }

    public Integer get(String datapoint)
    {
        DataPoint point = DataPoint.getValue(datapoint);
        return get(point);
    }

    public int get(DataPoint point, String player)
    {
        if(point.room.equals(RaidRoom.ALL))
        {
            int sum = 0;
            for(RaidRoom room : RaidRoom.values())
            {
                sum+= getParser(room).data.get(point, player);
            }
            return sum;
        }
        else
        {
            return getParser(point.room).data.get(point, player);
        }
    }

    public int get(String datapoint, String player)
    {
        return get(DataPoint.getValue(datapoint), player);
    }


    public RoomParser getParser(RaidRoom room)
    {
        return roomParsers.getOrDefault(room, unknownParser);
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

    public boolean getTimeAccurate(DataPoint point)
    {
        return get(point) > -1;
        //return getRoomAccurate(point.room);
    }

    /**
     * Handles log entries which are the same regardless of raid
     *
     * @param entry raid agnostic log entry to be parsed
     */
    private void handleRaidAgnosticLogEntry(LogEntry entry) //todo most of this should really be in subclass
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
            case IS_HARD_MODE:
                isHardMode = true;
                break;
            case IS_STORY_MODE:
                isStoryMode = true;
                break;
            case SPECTATE:
                isSpectate = true;
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
            for(ParseInstruction instruction : entry.logEntry.parseInstructions)
            {
                if(instruction.dataPoint1 != null && instruction.dataPoint1.room.equals(ANY))
                {
                    parser = defaultParser;
                }
                switch (instruction.type)
                {
                    case ADD_TO_VALUE:
                        parser.data.incrementBy(instruction.dataPoint1, entry.getFirstInt(), entry.getValue("Player"));
                        break;
                    case INCREMENT:
                        parser.data.increment(instruction.dataPoint1, entry.getValue("Player"));
                        break;
                    case INCREMENT_IF_GREATER_THAN:
                        if (((instruction.marker != null) ? Integer.parseInt(entry.values.get(instruction.marker)) : parser.data.get(instruction.dataPoint1)) > instruction.value)
                        {
                            parser.data.increment(instruction.dataPoint1, entry.getValue("Player"));
                        }
                        break;
                    case INCREMENT_IF_LESS_THAN:
                        if (((instruction.marker != null) ? Integer.parseInt(entry.values.get(instruction.marker)) : parser.data.get(instruction.dataPoint1)) < instruction.value)
                        {
                            parser.data.increment(instruction.dataPoint1, entry.getValue("Player"));
                        }
                        break;
                    case INCREMENT_IF_EQUALS:
                        if(instruction.value == Integer.parseInt(entry.values.get(instruction.marker)))
                        {
                            parser.data.incrementBy(instruction.dataPoint1, entry.getFirstInt());
                        }
                        break;
                    case SET:
                        parser.data.set(instruction.dataPoint1, entry.getFirstInt());
                        break;
                    case SUM:
                        parser.data.set(instruction.dataPoint1, parser.data.get(instruction.dataPoint2) + parser.data.get(instruction.dataPoint3));
                        break;
                    case SPLIT:
                        parser.data.set(instruction.dataPoint1, entry.getFirstInt());
                        parser.data.set(instruction.dataPoint2, parser.data.get(instruction.dataPoint1) - parser.data.get(instruction.dataPoint3));
                        break;
                    case DWH:
                        parser.data.dwh(instruction.dataPoint1);
                        break;
                    case BGS:
                        parser.data.bgs(instruction.dataPoint1, entry.getFirstInt());
                        break;
                    case ROOM_END_FLAG:
                        lastRoom = entry.logEntry.getRoom().name;
                        wasReset = true;
                        break;
                    case ROOM_START_FLAG:
                        wasReset = false;
                        break;
                    case AGNOSTIC:
                        handleRaidAgnosticLogEntry(entry);
                        break;
                    case ACCURATE_START:
                        setRoomStartAccurate(entry.logEntry.getRoom());
                        break;
                    case ACCURATE_END:
                        setRoomEndAccurate(entry.logEntry.getRoom());
                        break;
                    case MANUAL_PARSE:
                        break;
                    case MAP:
                        parser.data.increment(instruction.dataPoint1, entry.getValue("Player"));
                        parser.data.addToList(instruction.dataPoint1, Integer.parseInt(entry.getValue("Room Tick")));
                        break;
                }
                parser = getParser(room);
            }
            return true;
        } catch (Exception e)
        {
            log.info("Could not parse: " + String.join(",", entry.lines));
            return false;
        }
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

        return scale;
    }

    protected void addColorToStatus(String color)
    {
        if(!roomStatus.isEmpty() && !roomStatus.endsWith(">"))
        {
            String lastLetter = roomStatus.substring(roomStatus.length() - 1);
            roomStatus = roomStatus.substring(0, roomStatus.length() - 1) + color + lastLetter;
        }
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
