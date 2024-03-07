package com.TheatreTracker.utility.datautility.datapoints;

import com.TheatreTracker.utility.wrappers.PlayerDidAttack;
import com.google.common.collect.Multimap;
import lombok.Getter;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public abstract class Raid {
    /**
     * Time for the entire raid.
     */
    @Getter
    protected int overallTime;

    /**
     * Amount of players in the raid.
     */
    @Getter
    protected int partySize;

    protected final List<String> logData;

    protected Raid(String filepath) {
        logData = getRaidStrings(filepath);
    }

    public abstract List<RoomDataManager> getAllData();

    public static List<Raid> getRaids(String path) {
        List<String> raidData = getRaidStrings(path);
        List<Raid> raids = new ArrayList<>();
        for (String line : raidData) {

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
            while(scanner.hasNextLine())
            {
                lines.add(scanner.nextLine());
            }
        }
        catch(Exception e)
        {
            System.err.println("Could not find file: " + path);
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
        return lines;
    }
}
