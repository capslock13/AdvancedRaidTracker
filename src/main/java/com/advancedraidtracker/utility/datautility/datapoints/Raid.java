package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.datautility.datapoints.tob.Tob;
import lombok.Getter;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Raid
{
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
     * Amount of players in the raid.
     */
    @Getter
    protected final List<String> players;

    protected final List<LogEntry> raidData;

    protected Raid(List<LogEntry> raidData)
    {
        this.raidData = raidData;
        this.players = new ArrayList<>();
    }

    public abstract List<RoomDataManager> getAllData();

    /**
     * Gets a raid from a single log file, current structure is that each
     * raid has its own log file.
     *
     * @param path path to log file
     * @return A raid for the log
     */
    public static Raid getRaid(String path)
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
                ret = new Tob(currentRaid);
            }
        }
        return ret;
    }

    public static List<Raid> getRaids(String path)
    {
        List<String> raidData = getRaidStrings(path);
        List<Raid> raids = new ArrayList<>();
        for (String line : raidData)
        {

        }

        return raids;
    }

    private static List<String> getRaidStrings(String path)
    {
        List<String> lines = new ArrayList<>();
        File file = new File(path);
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
                    players.addAll(Stream.of(entry.getExtra()).filter(name -> !name.isEmpty()).collect(Collectors.toList()));
                    break;
            }
        }
    }
}
