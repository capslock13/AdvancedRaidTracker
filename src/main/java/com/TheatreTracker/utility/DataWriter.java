package com.TheatreTracker.utility;

import com.TheatreTracker.TheatreTrackerConfig;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import com.TheatreTracker.constants.LogID;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

@Slf4j
public class DataWriter
{
    private Client client;
    private TheatreTrackerConfig config;
    private int versionID = 1;

    public DataWriter(Client client, TheatreTrackerConfig config)
    {
        this.config = config;
        this.client = client;
    }

    /**
     * Writes a message to the log with the time, message ID, and up to 5 additional parameters
     *
     * @param id     LogID of message
     * @param params
     */
    public void write(LogID id, String... params)
    {
        if (params.length > 5)
            throw new IllegalArgumentException("Too many values passed to DataWriter");
        String[] values = {"", "", "", "", ""};
        for (int i = 0; i < params.length; i++)
        {
            values[i] = params[i];
        }
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
        String UID = "";
        for (int i = 0; i < Objects.requireNonNull(Objects.requireNonNull(client.getLocalPlayer()).getName()).length(); i++)
        {
            int value = Objects.requireNonNull(client.getLocalPlayer().getName()).toCharArray()[i];
            UID += value;
        }
        return UID;
    }
}
