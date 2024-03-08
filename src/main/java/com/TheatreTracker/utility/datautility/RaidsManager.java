package com.TheatreTracker.utility.datautility;

import com.TheatreTracker.SimpleRaidData;
import com.TheatreTracker.SimpleTOBData;
import com.TheatreTracker.ui.RaidTrackerSidePanel;
import com.TheatreTracker.utility.wrappers.RaidsArrayWrapper;
import lombok.extern.slf4j.Slf4j;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static com.TheatreTracker.utility.datautility.DataWriter.PLUGIN_DIRECTORY;

@Slf4j
public class RaidsManager
{
    private static final String raidsFolder = PLUGIN_DIRECTORY + "/misc-dir/raids/";

    public static ArrayList<RaidsArrayWrapper> getRaidsSets()
    {
        ArrayList<RaidsArrayWrapper> raidSets = new ArrayList<>();
        File folder = new File(raidsFolder);
        if (!folder.exists()) folder.mkdirs();
        try
        {
            for (File entry : Objects.requireNonNull(folder.listFiles()))
            {
                if (entry.isDirectory())
                {
                    ArrayList<SimpleRaidData> raids = new ArrayList<>();
                    for (File raid : Objects.requireNonNull(entry.listFiles()))
                    {
                        try
                        {
                            RaidTrackerSidePanel.parseLogFile(raids, raid, raid.getAbsolutePath());
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    raidSets.add(new RaidsArrayWrapper(raids, entry.getName()));
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return false;
    }

    public static void saveOverwriteRaids(String name, ArrayList<SimpleRaidData> raids)
    {
        try
        {
            File directory = new File(raidsFolder);
            if (!directory.exists())
            {
                directory.mkdirs();
            }
            File raidsFile = new File(raidsFolder + name + "/");

            if (raidsFile.exists())
            {
                raidsFile.delete();
            }
            raidsFile.mkdirs();
            writeRaid(name, raids);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void writeRaid(String name, ArrayList<SimpleRaidData> raids) throws IOException
    {
        for (SimpleRaidData raid : raids)
        {
            File newEntry = new File(raidsFolder + name + "/" + raid.fileName);
            Files.createFile(newEntry.toPath());
            BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(newEntry.toPath())));
            File file = new File(raid.filePath);
            Scanner logReader = new Scanner(Files.newInputStream(file.toPath()));
            while (logReader.hasNextLine())
            {
                fileWriter.write(logReader.nextLine());
                fileWriter.newLine();
            }
            fileWriter.close();
        }
    }

    public static void saveRaids(String name, ArrayList<SimpleRaidData> raids)
    {
        try
        {
            File directory = new File(raidsFolder);
            if (!directory.exists())
            {
                directory.mkdirs();
            }
            File raidsFile = new File(raidsFolder + name + "/");
            if (!raidsFile.exists())
            {
                raidsFile.mkdirs();
            }
            writeRaid(name, raids);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
