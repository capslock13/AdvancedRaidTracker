package com.advancedraidtracker.utility.datautility;

import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.advancedraidtracker.constants.TobIDs.EXIT_FLAG;
import static com.advancedraidtracker.utility.datautility.DataWriter.*;

@Slf4j
public class LegacyFileUtility //Older versions of the plugin during testing used a different file structure, this class contains methods used to migrate those
{

    public static void migrateSavedFilesToZip()
    {
        try
        {
            String path = PLUGIN_DIRECTORY + "misc-dir/raids/";
            File saveDirectory = new File(path);
            if(!saveDirectory.exists())
            {
                return;
            }
            for(File entry : Objects.requireNonNull(saveDirectory.listFiles()))
            {
                if(!entry.getName().equals("extracted") && entry.isDirectory() && !entry.getName().endsWith("-archive"))
                {
                    File saveFile = new File(path + entry.getName() + ".zip");
                    if(!saveFile.exists())
                    {
                        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(saveFile)))
                        {
                            for (File raidFile : Objects.requireNonNull(entry.listFiles()))
                            {
                                out.putNextEntry(new ZipEntry(raidFile.getName()));
                                Files.copy(raidFile.toPath(), out);
                            }
                        }
                        entry.renameTo(new File(entry.getAbsolutePath() + "-archive"));
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.info("A failure occurred while attempting to migrate old files");
        }
    }

    public static void splitLegacyFiles()
    {
        try
        {
            String path = System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/primary/"; //old directory
            File logDirectory = new File(path);
            if (!logDirectory.exists())
            {
                return;
            }
            ArrayList<String> currentRaid = new ArrayList<>();
            for (File file : Objects.requireNonNull(logDirectory.listFiles()))
            {
                if (file.getName().contains("tobdata") && !file.getName().contains("archive"))
                {
                    Scanner logReader = new Scanner(Files.newInputStream(file.toPath()));
                    while (logReader.hasNextLine())
                    {
                        String line = logReader.nextLine();
                        String[] lineSplit = line.split(",");
                        currentRaid.add(line);
                        if (Objects.equals(lineSplit[3], EXIT_FLAG))
                        {
                            int highestLog = getHighestLogNumber("legacy-files");
                            writeFile(currentRaid, PLUGIN_DIRECTORY + "legacy-files/primary/" + "tobdata" + (highestLog + 1) + ".log");
                            currentRaid.clear();
                        }
                    }
                    if (!file.renameTo(new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 4) + "-archive.log")))
                    {
                        log.info("failed to rename old file");
                    }
                }
            }
            path = System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/";
            File oldDirectory = new File(path);
            if (oldDirectory.exists())
            {
                for (File folder : Objects.requireNonNull(oldDirectory.listFiles()))
                {
                    if (folder.isDirectory() && !folder.getName().equals("primary"))
                    {
                        try
                        {
                            File newSubDirectory = new File(PLUGIN_DIRECTORY + "misc-dir/");
                            if (!newSubDirectory.exists())
                            {
                                if (!newSubDirectory.mkdirs())
                                {
                                    log.info("Failed to create folder: " + newSubDirectory.getAbsolutePath());
                                }
                            }
                            Files.move(folder.toPath(), (new File(PLUGIN_DIRECTORY + "misc-dir/" + folder.getName()).toPath()));
                        } catch (Exception e)
                        {
                            log.info("Failed to move folder " + folder.getAbsolutePath());
                        }
                    }
                }
            }
        } catch (IOException e)
        {
            log.info("Failed splitting legacy file");
        }
    }
}
