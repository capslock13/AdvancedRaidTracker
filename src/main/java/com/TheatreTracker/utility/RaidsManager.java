package com.TheatreTracker.utility;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.ui.RaidTrackerPanelPrimary;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class RaidsManager
{
    private static final String raidsFolder = System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/raids/";

    public static ArrayList<RaidsArrayWrapper> getRaidsSets()
    {
        ArrayList<RaidsArrayWrapper> raidSets = new ArrayList<>();
        File folder = new File(raidsFolder);
        if (!folder.exists()) folder.mkdirs();
        try
        {
            for (File entry : Objects.requireNonNull(folder.listFiles()))
            {
                if (entry.isFile())
                {
                    if (entry.getAbsolutePath().endsWith(".raids"))
                    {
                        ArrayList<RoomData> raids = new ArrayList<>();
                        try
                        {
                            RaidTrackerPanelPrimary.parseLogFile(raids, entry);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        raidSets.add(new RaidsArrayWrapper(raids, entry.getName()));
                    }
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

    public static void saveOverwriteRaids(String name, ArrayList<RoomData> raids)
    {
        try
        {
            File directory = new File(raidsFolder);
            if (!directory.exists())
            {
                directory.mkdirs();
            }
            File raidsFile = new File(raidsFolder + name + ".raids");

            if (raidsFile.exists())
            {
                raidsFile.delete();
            }
            raidsFile.createNewFile();
            writeRaid(name, raids);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void writeRaid(String name, ArrayList<RoomData> raids) throws IOException
    {
        BufferedWriter raidsWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(raidsFolder + name + ".raids"))));
        for (RoomData raid : raids)
        {
            for (String s : raid.raidDataRaw)
            {
                raidsWriter.write(s);
                raidsWriter.newLine();
            }
        }
        raidsWriter.close();
    }

    public static void saveRaids(String name, ArrayList<RoomData> raids)
    {
        try
        {
            File directory = new File(raidsFolder);
            if (!directory.exists())
            {
                directory.mkdirs();
            }
            File raidsFile = new File(raidsFolder + name + ".raids");
            if (!raidsFile.exists())
            {
                raidsFile.createNewFile();
            }
            writeRaid(name, raids);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
