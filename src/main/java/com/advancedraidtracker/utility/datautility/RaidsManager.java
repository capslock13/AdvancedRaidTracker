package com.advancedraidtracker.utility.datautility;

import com.advancedraidtracker.SimpleRaidDataBase;
import com.advancedraidtracker.ui.RaidTrackerSidePanel;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.wrappers.RaidsArrayWrapper;
import lombok.extern.slf4j.Slf4j;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static com.advancedraidtracker.utility.datautility.DataWriter.PLUGIN_DIRECTORY;

@Slf4j
public class RaidsManager
{
    private static final String raidsFolder = PLUGIN_DIRECTORY + "/misc-dir/raids/";

    public static ArrayList<RaidsArrayWrapper> getRaidsSets()
    {
        ArrayList<RaidsArrayWrapper> raidSets = new ArrayList<>();
        File folder = new File(raidsFolder);
        if (!folder.exists()) if(!folder.mkdirs()){log.info("Couldn't make misc dir");}
        try
        {
            for (File entry : Objects.requireNonNull(folder.listFiles()))
            {
                if (entry.isDirectory())
                {
                    ArrayList<SimpleRaidDataBase> raids = new ArrayList<>();
                    for (File raid : Objects.requireNonNull(entry.listFiles()))
                    {
                        try
                        {
                            RaidTrackerSidePanel.parseLogFile(raids, raid, raid.getAbsolutePath());
                        } catch (Exception e)
                        {
                            log.info("Failed to parse log file " + raid.getAbsolutePath());
                        }
                    }
                    raidSets.add(new RaidsArrayWrapper(raids, entry.getName()));
                }
            }
        } catch (Exception e)
        {
            log.info("Failed parsing raid sets");
        }
        return raidSets;
    }

    public static boolean doesRaidExist(String name)
    {
        File folder = new File(raidsFolder);
        try
        {
            for (File entry : Objects.requireNonNull(folder.listFiles()))
            {
                if (entry.getName().equals(name + ".raids"))
                {
                    return true;
                }
            }
        } catch (Exception e)
        {
            log.info("Could not determine if raid exists");
        }
        return false;
    }

    public static void saveOverwriteRaids(String name, ArrayList<Raid> raids)
    {
        try
        {
            File directory = new File(raidsFolder);
            if (!directory.exists())
            {
                if(!directory.mkdirs()) {log.info("Could not make overwrite dir");}
            }
            File raidsFile = new File(raidsFolder + name + "/");

            if (raidsFile.exists())
            {
                if(!raidsFile.delete())
                {
                    log.info("Could not delete file during overwrite");
                }
            }
            if(!raidsFile.mkdirs())
            {
                log.info("Could not make directories during overwrite");
            }
            writeRaid(name, raids);
        } catch (Exception e)
        {
            log.info("Could not write overwrite raid");
        }
    }

    private static void writeRaid(String name, ArrayList<Raid> raids) throws IOException
    {
        for (Raid raid : raids)
        {
            File newEntry = raid.getFilepath().toFile();
            Files.createFile(newEntry.toPath());
            BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(newEntry.toPath())));
            File file = raid.getFilepath().toFile();
            Scanner logReader = new Scanner(Files.newInputStream(file.toPath()));
            while (logReader.hasNextLine())
            {
                fileWriter.write(logReader.nextLine());
                fileWriter.newLine();
            }
            fileWriter.close();
        }
    }

    public static void saveRaids(String name, ArrayList<Raid> raids)
    {
        try
        {
            File directory = new File(raidsFolder);
            if (!directory.exists())
            {
                if(!directory.mkdirs())
                {
                    log.info("Could not make directory to save raid");
                }
            }
            File raidsFile = new File(raidsFolder + name + "/");
            if (!raidsFile.exists())
            {
                if(!raidsFile.mkdirs())
                {
                    log.info("Could not make directory for specific folder for raid");
                }
            }
            writeRaid(name, raids);
        } catch (Exception e)
        {
            log.info("Could not write save file");
        }
    }
}
