package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.datautility.datapoints.tob.Tob;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * Log entries for the raid.
     */
    protected final List<LogEntry> raidData;

    protected Raid(Path filepath, List<LogEntry> raidData)
    {
        this.raidData = raidData;
        this.filepath = filepath;
        this.players = new HashSet<>();
    }

    /**
     * @return A list that contains the data for all rooms.
     */
    public abstract List<RoomDataManager> getAllData();

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
            currentRaid.add(entry);
            if (entry.getLogEntry() == LogID.LEFT_TOB)
            {
                ret = new Tob(path, currentRaid);
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
     * Parses a log file and fetches generic data points.
     */
    public void parse()
    {
        for (LogEntry entry : raidData)
        {
            switch (entry.getLogEntry())
            {
                case PARTY_MEMBERS:
                    // TODO: may need changing with toa/cox support
                    players.addAll(entry.getExtra().stream()
                            .map(name -> name.replaceAll("\\P{Print}", ""))
                            .filter(name -> !name.isEmpty())
                            .collect(Collectors.toSet()));
                    break;
            }
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
     * Fetches the thrall attacks done throughout the raid.
     *
     * @return A map of each players thrall attacks.
     */
    public Multimap<String, Integer> getThrallAttacks() {
        List<RoomDataManager> data = getAllData();
        Multimap<String, Integer> attacks = ArrayListMultimap.create();

        for (RoomDataManager room : data)
        {
            attacks.putAll(room.getThrallAttacks());
        }

        return attacks;
    }


    /**
     * @return Amount of players in a raid.
     */
    public int getScale()
    {
        return players.size();
    }
}
