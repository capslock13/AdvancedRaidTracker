package com.TheatreTracker.utility;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.ui.RaidTrackerPanelPrimary;
import lombok.extern.slf4j.Slf4j;
import com.TheatreTracker.constants.LogID;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

@Slf4j
public class DataWriter
{
    private final TheatreTrackerConfig config;

    public DataWriter(TheatreTrackerConfig config)
    {
        this.config = config;
    }

    public static void splitLegacyFile()
    {
        ArrayList<RoomData> raids = new ArrayList<>();
        try
        {
            String path = "/.runelite/theatretracker/primary/tobdata.log";
            File logFile = new File(System.getProperty("user.home").replace("\\", "/") + path);
            RaidTrackerPanelPrimary.parseLogFile(raids, logFile);
            if(!logFile.delete())
            {
                log.info("Failed to delete previous field");
            }
        } catch (Exception ignored)
        {
        }
        int currentSize = 0;
        int highestFile = getHighestLogNumber();
        ArrayList<RoomData> currentFileRaids = new ArrayList<>();
        int last = raids.size()-1;
        int index = 0;
        for(RoomData r : raids)
        {
            currentSize += r.raidDataRaw.length;
            currentFileRaids.add(r);
            if(currentSize > 50000 || last == index)
            {
                try
                {
                    File logFile = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/primary/tobdata" + highestFile + ".log");
                    if (!logFile.exists())
                    {
                        if(!logFile.createNewFile())
                        {
                            return;
                        }
                    }
                    BufferedWriter logger = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), StandardCharsets.UTF_8));
                    for(RoomData data : currentFileRaids)
                    {
                        for(String s : data.raidDataRaw)
                        {
                            logger.write(s);
                            logger.newLine();
                        }
                    }
                    logger.close();
                } catch (IOException ignored)
                {
                }
                currentFileRaids.clear();
                currentSize = 0;
                highestFile++;
            }
            index++;
        }
    }

    public static void checkLogFileSize()
    {
        try
        {
            File logFile = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/primary/tobdata.log");
            if (!logFile.exists())
            {
                return;
            }
            long fileSize = logFile.length();
            if(fileSize > (6*1024*1024))
            {
                splitLegacyFile();
            }
            else if (fileSize > (5 * 1024 * 1024))
            {
                log.info("Migrating log to new file");
                int highestLogNumber = getHighestLogNumber();
                log.info("Highest log found: " + highestLogNumber);
                File newFile = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/primary/tobdata" + (highestLogNumber + 1) + ".log");
                if (newFile.exists())
                {
                    log.info("Failed to migrate file due to name conflict.");
                    return;
                }
                boolean wasSuccessful = logFile.renameTo(newFile);
                if (!wasSuccessful)
                {
                    log.info("Failed to migrate file due to unknown reason");
                } else
                {
                    File toCreate = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/primary/tobdata.log");

                    if(!toCreate.createNewFile())
                    {
                        log.info("Failed to create");
                    }
                }
            }
        }
        catch(Exception ignored)
        {
        }
    }

    private static int getHighestLogNumber()
    {
        int highestLogNumber = 0;
        for (File file : Objects.requireNonNull(new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/primary/").listFiles()))
        {
            if (file.getName().contains("tobdata"))
            {
                int index = -1;
                index = file.getName().indexOf(".log");
                if (index != -1)
                {
                    try
                    {
                        int logNumber = Integer.parseInt(file.getName().substring(7, index));
                        if (logNumber > highestLogNumber)
                        {
                            highestLogNumber = logNumber;
                        }
                    } catch
                    (Exception ignored)
                    {
                    }
                }
            }
        }
        return highestLogNumber;
    }

    /**
     * Writes a message to the log with the time, message ID, and up to 5 additional parameters
     *
     * @param id     LogID of message
     */
    public void write(LogID id, String... params)
    {
        if (params.length > 5)
            throw new IllegalArgumentException("Too many values passed to DataWriter");
        String[] values = {"", "", "", "", ""};
        System.arraycopy(params, 0, values, 0, params.length);
        write(id.getId(), values[0], values[1], values[2], values[3], values[4]);
    }

    public void write(int key)
    {
        write(key, "", "", "", "", "");
    }

    public void write(LogID id, int param)
    {
        write(id, String.valueOf(param));
    }

    public void write(int key, String v1)
    {
        write(key, v1, "", "", "", "");
    }

    public void write(int key, String v1, String v2)
    {
        write(key, v1, v2, "", "", "");
    }

    public void write(int key, String v1, String v2, String v3, String v4, String v5)
    {
        int versionID = 1;
        writeFile(getUID() + "," + System.currentTimeMillis() + "," + versionID + "," + key + "," + v1 + "," + v2 + "," + v3 + "," + v4 + "," + v5);
    }

    public static void writeAliasFile(String aliasText)
    {
        try
        {
            File aliasFile = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/alias/alias.log");
            if (!aliasFile.exists())
            {
                File directory = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/alias/");
                if (!directory.exists())
                {
                    directory.mkdirs();
                }
                aliasFile.createNewFile();
            }
            BufferedWriter logger = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/alias/alias.log", false), StandardCharsets.UTF_8));
            logger.write(aliasText);
            logger.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readAliasFile()
    {
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new FileReader(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/alias/alias.log"));
            String line = reader.readLine();
            while (line != null)
            {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (Exception e)
        {
            return lines;
        }
        return lines;
    }

    public void writeFile(String msg)
    {
        if (config.writeToLog())
        {
            try
            {
                File logFile = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/primary/tobdata.log");
                if (!logFile.exists())
                {
                    logFile.createNewFile();
                }
                BufferedWriter logger = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/primary/tobdata.log", true), StandardCharsets.UTF_8));
                logger.write(msg);
                logger.newLine();
                logger.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private String getUID()
    {
        return "";
    }
}
