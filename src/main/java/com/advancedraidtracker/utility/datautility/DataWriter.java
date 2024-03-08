package com.advancedraidtracker.utility.datautility;

import com.advancedraidtracker.TheatreTrackerConfig;
import com.advancedraidtracker.constants.RaidType;
import lombok.extern.slf4j.Slf4j;
import com.advancedraidtracker.constants.LogID;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

@Slf4j
public class DataWriter
{
    private final TheatreTrackerConfig config;
    private String activeUsername = "";
    private final ArrayList<String> currentBuffer;
    private RaidType currentRaidType = RaidType.UNASSIGNED;

    public final static String PLUGIN_DIRECTORY = System.getProperty("user.home").replace("\\", "/") + "/.runelite/advancedraidtracker/";

    public DataWriter(TheatreTrackerConfig config) throws IOException
    {
        this.config = config;
        currentBuffer = new ArrayList<>();
    }

    public void setRaidType(RaidType raidType)
    {
        this.currentRaidType = raidType;
    }

    public void setName(String name) throws IOException
    {
        activeUsername = name;
        File dirMain = new File(PLUGIN_DIRECTORY + name + "/primary/");
        File dirFilters = new File(PLUGIN_DIRECTORY + "misc-dir/filters/");
        File dirRaids = new File(PLUGIN_DIRECTORY + "misc-dir/raids/");

        if (!dirRaids.exists())
        {
            if (!dirRaids.mkdirs())
            {
                log.info("Failed to create raids directory for username " + name);
            }
        }
        if (!dirMain.exists())
        {
            if (!dirMain.mkdirs())
            {
                log.info("Failed to create main directory for username " + name);
            }
        }
        if (!dirFilters.exists())
        {
            if (!dirFilters.mkdirs())
            {
                log.info("Failed to create filter directory for username " + name);
            }
        }

        for (RaidType raidType : RaidType.values())
        {
            if (!raidType.equals(RaidType.UNASSIGNED))
            {
                File logFile = new File(PLUGIN_DIRECTORY + name + "/primary/" + raidType.name + "data.log");
                if (!logFile.exists())
                {
                    if (!logFile.createNewFile())
                    {
                        log.info("Failed to create log file for " + raidType.name);
                    }
                }
            }
        }
    }

    //If you X out client in the middle of a raid it does not record the flag that the raid ended, so this is called when you enter a tob to see
    //if an active datafile and if so it adds an exit flag to the end
    public void checkForEndFlag()
    {
        File logFile = new File(PLUGIN_DIRECTORY + activeUsername + "/primary/tobdata.log");
        if (logFile.exists())
        {
            if (logFile.length() > 0)
            {
                currentBuffer.add("," + System.currentTimeMillis() + ",1," + 4 + "," + "," + "," + "," + ",");
                writeFile();
            }
        }
    }

    public void migrateToNewRaid()
    {
        int highest = getHighestLogNumber(activeUsername);
        File logFile = new File(PLUGIN_DIRECTORY + activeUsername + "/primary/" + currentRaidType.name + "data.log");
        if (!logFile.exists())
        {
            log.info("Could not migrate because file does not exist");
            return;
        }
        if (logFile.length() == 0)
        {
            return;
        } //Inject number to log file before the '.log' e.g. tobdata.log -> tobdata<number>.log
        if (!logFile.renameTo(new File(logFile.getAbsolutePath().substring(0, logFile.getAbsolutePath().length() - 4) + (highest + 1) + ".log")))
        {
            log.info("Could not rename primary log file");
        } else
        {
            logFile = new File(PLUGIN_DIRECTORY + activeUsername + "/primary/" + currentRaidType.name + "data.log");
            if (!logFile.exists())
            {
                try
                {
                    if (!logFile.createNewFile())
                    {
                        log.info("Replacement file creation unsuccessful");
                    }
                } catch (Exception e)
                {
                    log.info("Exception thrown when creating replacement log file: " + logFile.getAbsolutePath());
                }
            }
        }
    }

    public static void checkLogFileSize()
    {
        try
        {
            File logFile = new File(PLUGIN_DIRECTORY + "primary/tobdata.log");
            if (!logFile.exists())
            {
                return;
            }
            long fileSize = logFile.length();
            if (fileSize > (6 * 1024 * 1024))
            {
                //splitLegacyFile();
            } else if (fileSize > (5 * 1024 * 1024))
            {
                int highestLogNumber = getHighestLogNumber("");
                File newFile = new File(PLUGIN_DIRECTORY + "primary/tobdata" + (highestLogNumber + 1) + ".log");
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
                    File toCreate = new File(PLUGIN_DIRECTORY + "primary/tobdata.log");

                    if (!toCreate.createNewFile())
                    {
                        log.info("Failed to create");
                    }
                }
            }
        } catch (Exception ignored)
        {
        }
    }

    public static int getHighestLogNumber(String name)
    {
        String directory = PLUGIN_DIRECTORY;
        if (!name.isEmpty())
        {
            directory += name + "/primary/";
        } else
        {
            directory += "primary/";
        }
        File logDirectory = new File(directory);
        if (!logDirectory.exists())
        {
            if (!logDirectory.mkdirs())
            {
                log.info("Could not make directory to find log number");
            }
        }
        int highestLogNumber = 0;
        for (File file : Objects.requireNonNull(new File(directory).listFiles()))
        {
            if (file.getName().contains("data"))
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
     * @param id LogID of message
     */

    public void addLine(LogID id, int value)
    {
        addLine(id, String.valueOf(value));
    }

    public void addLine(LogID id, String... params)
    {
        StringBuilder line = new StringBuilder(getUID() + "," + System.currentTimeMillis() + "," + currentRaidType.value + "," + id.getId());
        for (String s : params)
        {
            line.append(",").append(s);
        }
        currentBuffer.add(line.toString());
    }

    public void addLine(int key, String v1, String v2, String v3, String v4, String v5)
    {
        currentBuffer.add(getUID() + "," + System.currentTimeMillis() + "," + currentRaidType.value + "," + key + "," + v1 + "," + v2 + "," + v3 + "," + v4 + "," + v5);
    }

    public static void writeAliasFile(String aliasText)
    {
        try
        {
            File aliasFile = new File(PLUGIN_DIRECTORY + "alias/alias.log");
            if (!aliasFile.exists())
            {
                File directory = new File(PLUGIN_DIRECTORY + "alias/");
                if (!directory.exists())
                {
                    if (!directory.mkdirs())
                    {
                        log.info("Failed to create alias directory");
                    }
                }
                if (!aliasFile.createNewFile())
                {
                    log.info("Failed to create alias file");
                }
            }
            BufferedWriter logger = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(PLUGIN_DIRECTORY + "alias/alias.log", false), StandardCharsets.UTF_8));
            logger.write(aliasText);
            logger.close();
        } catch (IOException e)
        {
            log.info("Failed writing to alias file");
        }
    }

    public static ArrayList<String> readAliasFile()
    {
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new FileReader(PLUGIN_DIRECTORY + "alias/alias.log"));
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

    public void writeFile()
    {
        if (config.writeToLog())
        {
            try
            {
                File logFile = new File(PLUGIN_DIRECTORY + activeUsername + "/primary/" + currentRaidType.name + "data.log");
                if (!logFile.exists())
                {
                    if (!logFile.createNewFile())
                    {
                        log.info("Failed to create log file");
                    }
                }
                BufferedWriter logger = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(PLUGIN_DIRECTORY + activeUsername + "/primary/" + currentRaidType.name + "data.log", true), StandardCharsets.UTF_8));
                for (String msg : currentBuffer)
                {
                    logger.write(msg);
                    logger.newLine();
                }
                logger.close();
            } catch (IOException e)
            {
                log.info("Failed clearing buffered tob data to log");
            }
        }
        currentBuffer.clear();
    }

    public static void writeFile(ArrayList<String> raid, String filePath)
    {
        try
        {
            File logFile = new File(filePath);
            if (!logFile.exists())
            {
                if (!logFile.createNewFile())
                {
                    log.info("Failed to create log file");
                }
            }
            BufferedWriter logger = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), StandardCharsets.UTF_8));
            for (String msg : raid)
            {
                logger.write(msg);
                logger.newLine();
            }
            logger.close();
        } catch (IOException e)
        {
            log.info("Failed clearing buffered tob data to log");
        }
    }


    private String getUID()
    {
        return "";
    } //deprecated
}
