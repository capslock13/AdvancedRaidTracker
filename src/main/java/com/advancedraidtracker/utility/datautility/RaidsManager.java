package com.advancedraidtracker.utility.datautility;

import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.wrappers.RaidsArrayWrapper;
import lombok.extern.slf4j.Slf4j;


import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.advancedraidtracker.utility.datautility.DataWriter.PLUGIN_DIRECTORY;

@Slf4j
public class RaidsManager //todo merge this with datareader?
{
    private static final String raidsFolder = PLUGIN_DIRECTORY + "/misc-dir/raids/";

    public static ArrayList<RaidsArrayWrapper> getRaidsSets() //check for exported raids
    {
        ArrayList<RaidsArrayWrapper> raidSets = new ArrayList<>();
        File extractFolder = new File(raidsFolder + "/extracted/");
        if(!extractFolder.exists())
        {
            extractFolder.mkdirs();
        }
        File folder = new File(raidsFolder);
        if (!folder.exists()) if (!folder.mkdirs())
        {
            log.info("Couldn't make misc dir");
        }
        try
        {
            for (File entry : Objects.requireNonNull(folder.listFiles())) //on refresh check for any saved .zips that don't have a matching extracted folder
            {
                if(entry.getName().endsWith(".zip")) //zip to allow easier file sharing, automatically extracts and file references use extracted location
                {
                    File extractedFile = new File(extractFolder.getAbsolutePath() + "/" + entry.getName() + "/");
                    if(extractedFile.mkdirs()) //only extract if it doesn't already exist
                    {
                        ZipInputStream in = new ZipInputStream(new FileInputStream(entry));
                        ZipEntry zipEntry = in.getNextEntry();
                        while(zipEntry != null)
                        {
                            File newFile = new File(extractedFile.getAbsolutePath() + "/" + zipEntry.getName());
                            FileOutputStream out = new FileOutputStream(newFile);
                            int len;
                            byte[] buffer = new byte[1024];
                            while((len = in.read(buffer)) > 0)
                            {
                                out.write(buffer, 0, len);
                            }
                            out.close();
                            zipEntry = in.getNextEntry();
                        }
                    }
                }
            }
            for(File entry : Objects.requireNonNull(extractFolder.listFiles())) //add each subdir in extracted to the table
            {
                if(entry.isDirectory())
                {
                    List<Raid> raids = new ArrayList<>();
                    for(File raid : Objects.requireNonNull(entry.listFiles()))
                    {
                        try
                        {
                            raids.add(DataReader.getRaid(raid.toPath()));
                        }
                        catch (Exception e)
                        {
                            log.info("Failed to parse log filed from saved extracted folder: " + raid.getAbsolutePath());
                        }
                    }
                    raidSets.add(new RaidsArrayWrapper(raids, entry.getName()));
                }
            }
        } catch (Exception e)
        {
            log.info("Failed parsing raid sets");
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
            log.info("Could not determine if raid exists");
        }
        return false;
    }

    public static void saveRaids(String name, ArrayList<Raid> raids)
    {
        File saveFile = new File(raidsFolder + name + ".zip");
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(saveFile)))
        {
            for (Raid raid : raids)
            {
                out.putNextEntry(new ZipEntry(raid.getFilepath().getFileName().toString()));
                Files.copy(raid.getFilepath(), out);
            }
        }
        catch (Exception e)
        {
            log.info("failed to save raids");
        }
    }
}
