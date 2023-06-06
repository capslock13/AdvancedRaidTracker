package com.TheatreTracker.utility;

import com.TheatreTracker.TheatreTrackerConfig;
import net.runelite.api.Client;
import com.TheatreTracker.constants.LogID;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class DataWriter
{
    private Client client;
    private TheatreTrackerConfig config;
    private int versionID = 1;

    public DataWriter(Client client, TheatreTrackerConfig config)
    {
        File dirMain = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/primary/");
        File dirFilters = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/filters/");
        File dirRaids = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/raids/");

        if(!dirMain.exists()) dirMain.mkdirs();
        this.config = config;
        this.client = client;
    }

    /**
     * Writes a message to the log with the time, message ID, and up to 5 additional parameters
     * @param id LogID of message
     * @param params
     */
    public void write(LogID id, String... params) {
        if (params.length > 5)
            throw new IllegalArgumentException("Too many values passed to DataWriter");
        String[] values = {"", "", "", "", ""};
        for (int i = 0; i < params.length; i++) {
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

    public void writeFile(String msg)
    {
        if(config.writeToLog())
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
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private String getUID()
    {
        String UID = "";
        for(int i = 0; i < Objects.requireNonNull(Objects.requireNonNull(client.getLocalPlayer()).getName()).length(); i++)
        {
            int value = Objects.requireNonNull(client.getLocalPlayer().getName()).toCharArray()[i];
            UID += value;
        }
        return UID;
    }
}
