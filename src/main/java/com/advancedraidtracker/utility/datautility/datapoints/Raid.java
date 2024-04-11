package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.*;
import com.advancedraidtracker.utility.datautility.*;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.util.Text;

import java.nio.file.Path;
import java.util.*;

import static com.advancedraidtracker.constants.RaidRoom.*;
import static com.advancedraidtracker.constants.RaidType.TOB;
import static com.advancedraidtracker.utility.datautility.DataPoint.*;
import static com.advancedraidtracker.utility.datautility.datapoints.RoomDataManager.getDataPointStringAsInteger;

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
    //@Getter
    //protected int challengeTime;

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
    private List<LogEntry> raidData;

    protected RoomDataManager data = new RoomDataManager();

    public boolean isHardMode = false;//todo move these to raid specific
    public boolean isStoryMode = false;

    protected String red = "<html><font color='#FF0000'>"; //todo convert along with usages to colors (looking @ you fisu)
    protected String green = "<html><font color='#44AF33'>";
    protected String orange = "<html><font color='#FF7733'>";
    protected String yellow = "<html><font color='#FFFF33'>";
    protected List<String> roomStarts = new ArrayList<>();

    protected boolean wasReset = false; //todo idk where these belong tbh
    protected String lastRoom = "";
    protected String currentRoom = "";
    protected String roomStatus = orange;

    protected LineManager lineManager;

    protected Map<RaidRoom, Boolean> defenseAccurate = new HashMap<>();

    public boolean getDefenseAccurate(RaidRoom room) //todo implement
    {
        return defenseAccurate.getOrDefault(room, false);
    }

    public abstract int getTimeSum();

    /**
     * Used for sorting to have String.toCompare method compared to the set
     *
     * @return player string e.g. Player1,Player2,Player3,Player4
     */
    public String getPlayerString()
    {
        return Text.toJagexName(String.join(",", getPlayers()));
    }

    @Getter
    private boolean spectated = false;

    protected Raid(Path filepath, List<LogEntry> raidData)
    {
        this.raidData = raidData;
        date = new Date(0L); //todo figure out why dates dont parse properly on some raids
        this.filepath = filepath;
        this.players = new LinkedHashSet<>();
        this.lineManager = new LineManager(this);
        for (RaidRoom room : RaidRoom.getRaidRoomsForRaidType(RaidType.TOA)) //todo assume toa always accurate
        {
            setRoomStartAccurate(room);
        }
        for (RaidRoom room : RaidRoom.getRaidRoomsForRaidType(RaidType.COLOSSEUM)) //todo assume toa always accurate
        {
            setRoomStartAccurate(room);
        }
    }

    public boolean getOverallTimeAccurate()
    {
        return false;
    }

    public int get(DataPoint point, RaidRoom room)
    {
        return data.get(point, room);
    }

    public int get(DataPoint point)
    {
        if (point.equals(CHALLENGE_TIME))
        {
            return getChallengeTime();
        }
        if (point.room.equals(RaidRoom.ALL))
        {
            int sum = 0;
            for (RaidRoom room : RaidRoom.values())
            {
                if (point.playerSpecific)
                {
                    RoomDataManager rdm = data;
                    if (rdm.playerSpecificMap.containsKey(getDataPointStringAsInteger(point.name)))
                    {
                        for (Integer player : rdm.playerSpecificMap.get(getDataPointStringAsInteger(point.name)).keySet())
                        {
                            sum += rdm.playerSpecificMap.get(getDataPointStringAsInteger(point.name)).get(player);
                        }
                    }
                } else
                {
                    sum += data.get(point);
                }
            }
            return sum;
        } else
        {
            if (point.playerSpecific)
            {
                int sum = 0;
                RoomDataManager rdm = data;
                if (rdm.playerSpecificMap.containsKey(getDataPointStringAsInteger(point.name)))
                {
                    for (Integer player : rdm.playerSpecificMap.get(getDataPointStringAsInteger(point.name)).keySet())
                    {
                        sum += rdm.playerSpecificMap.get(getDataPointStringAsInteger(point.name)).get(player);
                    }
                }
                return sum;
            } else
            {
                return data.get(point);
            }
        }
    }

    public Integer get(String datapoint)
    {
        if (datapoint.equals("Challenge Time"))
        {
            return getChallengeTime();
        }
        return data.get(datapoint);
    }

    public int get(DataPoint point, String player)
    {
        return data.get(point, player);
    }

    public int get(String datapoint, String player)
    {
        return get(DataPoint.getValue(datapoint), player);
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

    public boolean getRoomStartAccurate(RaidRoom room)
    {
        return roomStartAccurate.getOrDefault(room, false);
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
        if(point.getRaidType().equals(TOB))
        {
            return this.getRaidType() == TOB && getRoomAccurate(point.room);
        }
        return get(point) > -1 && point.getRaidType() == getRaidType();
        //return getRoomAccurate(point.room);
    }

    public int getIfAccurate(DataPoint point)
    {
        return (getRoomAccurate(point.room)) ? get(point) : 0;
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
                spectated = true;
                break;
        }
    }

    private boolean verzikSet = false; //idk

    public void clearRawData()
    {
        raidData = null;
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
            for (ParseInstruction instruction : entry.logEntry.parseInstructions)
            {
                if (instruction.dataPoint1 != null && instruction.dataPoint1.type.equals(types.TIME))
                {
                    if (!getRoomAccurate(entry.logEntry.getRoom()))
                    {
                        if (!filepath.toString().contains("Col"))
                        {
                            continue;
                        }
                    }
                } else if (instruction.dataPoint1 != null && instruction.dataPoint1.isTime())
                {
                    if (!getRoomStartAccurate(entry.logEntry.getRoom()))
                    {
                        if (!filepath.toString().contains("Col"))
                        {
                            continue;
                        }
                    }
                }
                switch (instruction.type)
                {
                    case ADD_TO_VALUE:
                        if (instruction.dataPoint1 != null && instruction.dataPoint1.equals(CHALLENGE_TIME) && entry.logEntry.equals(LogID.VERZIK_P3_DESPAWNED))
                        {
                            if (!verzikSet)
                            {
                                verzikSet = true;
                            } else
                            {
                                data.set(CHALLENGE_TIME, data.get(CHALLENGE_TIME) + 4);
                                break;
                            }
                        }
                        data.incrementBy(instruction.dataPoint1, entry.getFirstInt(), entry.getValue("Player"), RaidRoom.getRoom(currentRoom));
                        break;
                    case INCREMENT:
                        data.increment(instruction.dataPoint1, entry.getValue("Player"), RaidRoom.getRoom(currentRoom));
                        break;
                    case INCREMENT_IF_GREATER_THAN:
                        if (((instruction.marker != null) ? Integer.parseInt(entry.values.get(instruction.marker)) : data.get(instruction.dataPoint1)) > instruction.value)
                        {
                            data.increment(instruction.dataPoint1, entry.getValue("Player"), RaidRoom.getRoom(currentRoom));
                        }
                        break;
                    case INCREMENT_IF_LESS_THAN:
                        if (((instruction.marker != null) ? Integer.parseInt(entry.values.get(instruction.marker)) : data.get(instruction.dataPoint1)) < instruction.value)
                        {
                            data.increment(instruction.dataPoint1, entry.getValue("Player"), RaidRoom.getRoom(currentRoom));
                        }
                        break;
                    case INCREMENT_IF_EQUALS:
                        if (instruction.value == Integer.parseInt(entry.values.get(instruction.marker)))
                        {
                            data.incrementBy(instruction.dataPoint1, entry.getFirstInt(), RaidRoom.getRoom(currentRoom));
                        }
                        break;
                    case SET:
                        if (instruction.dataPoint1.room.equals(ALL))
                        {
                            data.set(instruction.dataPoint1, entry.getFirstInt() + instruction.value, RaidRoom.getRoom(currentRoom));
                        } else
                        {
                            data.set(instruction.dataPoint1, entry.getFirstInt() + instruction.value);
                        }
                        break;
                    case SUM:
                        data.set(instruction.dataPoint1, data.get(instruction.dataPoint2) + data.get(instruction.dataPoint3));
                        break;
                    case SPLIT:
                        data.set(instruction.dataPoint1, entry.getFirstInt() + instruction.value);
                        data.set(instruction.dataPoint2, data.get(instruction.dataPoint1) - data.get(instruction.dataPoint3));
                        break;
                    case DWH:
                        data.dwh(instruction.dataPoint1);
                        break;
                    case BGS:
                        data.bgs(instruction.dataPoint1, entry.getFirstInt());
                        break;
                    case ROOM_END_FLAG:
                        lastRoom = entry.logEntry.getRoom().name;
                        wasReset = true;
                        break;
                    case ROOM_START_FLAG:
                        currentRoom = entry.logEntry.getRoom().name;
                        roomStarts.add(currentRoom);
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
                        data.increment(instruction.dataPoint1, entry.getValue("Player"), RaidRoom.getRoom(currentRoom));
                        data.addToList(instruction.dataPoint1, Integer.parseInt(entry.getValue("Room Tick")));
                        break;
                }
            }
            return true;
        } catch (Exception e)
        {
            //log.info("Could not parse: " + String.join(",", entry.lines));
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

    @Getter
    public boolean favorite = false;

    public void setFavorite(boolean state)
    {
        if(state)
        {
            DataReader.addFavorite(getDate().getTime());
        }
        else
        {
            DataReader.removeFavorite(getDate().getTime());
        }
        favorite = state;
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
        if (!roomStatus.isEmpty() && !roomStatus.endsWith(">"))
        {
            String lastLetter = roomStatus.substring(roomStatus.length() - 1);
            roomStatus = roomStatus.substring(0, roomStatus.length() - 1) + color + lastLetter;
        }
    }

    public String getSplits()
    {
        return "";
    }

    public void dumpValues()
    {
        data.dumpValues();
    }

    public void setIndex(int index)
    {
        data.set(RAID_INDEX, index);
    }

    public int getIndex()
    {
        return data.get(RAID_INDEX);
    }

    public abstract int getChallengeTime();

    public List<Integer> getList(DataPoint point)
    {
        return data.getList(point);
    }

    public int getFirstPossibleNonIdleTick(RaidRoom room)
    {
        return lineManager.getFirstPossibleNonIdleTick(room);
    }

    public Map<Integer, String> getLines(RaidRoom room)
    {
        return lineManager.getLines(room);
    }

    public Map<Integer, String> getRoomSpecificData(RaidRoom room)
    {
        return lineManager.getRoomSpecificData(room);
    }

    public String getRoomSpecificText(RaidRoom room)
    {
        return lineManager.getRoomSpecificText(room);
    }

    public List<Integer> getRoomAutos(RaidRoom room)
    {
        return lineManager.getRoomSpecificAutos(room);
    }

    public String getPlayerList(List<Map<String, List<String>>> aliases)
    {
        StringBuilder list = new StringBuilder();
        List<String> names = new ArrayList<>();
        for (String s : players)
        {
            String name = s;
            for (Map<String, List<String>> alternateNames : aliases)
            {
                for (String alias : alternateNames.keySet())
                {
                    for (String potentialName : alternateNames.get(alias))
                    {
                        if (name.equalsIgnoreCase(potentialName))
                        {
                            name = alias;
                            break;
                        }
                    }
                }
            }
            names.add(name);
        }
        names.sort(String::compareToIgnoreCase);
        for (String s : names)
        {
            list.append(s);
            list.append(",");
        }
        if (list.length() > 0)
        {
            return list.substring(0, list.length() - 1);
        } else
        {
            return "";
        }
    }
}
