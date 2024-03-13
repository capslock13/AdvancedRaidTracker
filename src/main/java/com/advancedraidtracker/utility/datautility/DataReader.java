package com.advancedraidtracker.utility.datautility;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;
import com.advancedraidtracker.utility.datautility.datapoints.toa.Toa;
import com.advancedraidtracker.utility.datautility.datapoints.tob.Tob;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DataReader //todo move any methods that read files to here. I believe Raid side panel and the filter manager class has some?
{
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
            //todo find a better way to handle legacy/mismatched values. 8 16 26 46 arent used anymore 998 999 were for testing only. 975 976 were bloat testing
            //todo and 801 is playerattack, the constructor fails because mismatched args (theres a 6th arg, room name, that didnt exist prior to a week or two ago)
            String[] split = line.split(",", -1);
            if(split[3].equals("801") || split[3].equals("975") || split[3].equals("8") || split[3].equals("998") || split[3].equals("999")
                    || split[3].equals("976") || split[3].equals("16") || split[3].equals("26") || split[3].equals("46"))
            //legacy or otherwise ignored values to be excluded while testing parser
            {
                continue; //todo remove (see above)
            }
            LogEntry entry = new LogEntry(split); //todo going off of the above comments, this constructor needs to gracefully handle bad args so that
            //todo the check can be passed to the isSimple() line below for chart data
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
                        //log.info("Dumping room: " + room.name());
                        //parser.data.dumpValues(); //todo remove later, this was for testing the parser

                    }
                } else if (entry.logEntry == LogID.LEFT_TOA) //todo look into what needs to be done to handle this post deprecation
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

    private static void getChartData(Path path)
    {
        //todo read file from path looking only for !logEntry.isSimple()
        //todo...maybe create a class that wraps the values needed?
        //todo should be List<PlayerDidAttack>, Map<RaidRoom, Map<Integer, Integer>> (HP) Map<RaidRoom, Map<String, String>> (NPC Mappings)
        //todo maybe you want to find your own way of handling it per room @fisu
        //todo TOA can be done via the 6th parameter of Player attack / last param npc mapping / last param update hp which was added right before toa
        //todo tob needs to have state tracking to tell what room the data goes to....
    }

}
