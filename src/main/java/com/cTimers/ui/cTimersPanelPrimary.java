package com.cTimers.ui;

import com.cTimers.cRoomData;
import com.cTimers.cTimersPlugin;
import com.cTimers.panelcomponents.cFilteredRaidsFrame;
import com.google.inject.Inject;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;


public class cTimersPanelPrimary extends PluginPanel
{
    private JLabel raidCountLabel;
    private ArrayList<cRoomData> raidsData;

    private final cFilteredRaidsFrame raids;

    @Inject
    cTimersPanelPrimary(cTimersPlugin plugin)
    {
        raidsData = getAllRaids();
        raids = new cFilteredRaidsFrame();
        buildComponents();
    }

    private ArrayList<cRoomData> getAllRaids()
    {
        ArrayList<cRoomData> raids = new ArrayList<>();
        try
        {
            String path = "/.runelite/logs/tobdata.log";
            File logFile = new File(System.getProperty("user.home").replace("\\", "/") + path);
            Scanner logReader = new Scanner(Files.newInputStream(logFile.toPath()));
            ArrayList<String> raid = new ArrayList<>();
            boolean raidActive = false;
            while(logReader.hasNextLine())
            {
                String line = logReader.nextLine();
                String[] lineSplit = line.split(",");
                if(!raidActive)
                {
                    if(lineSplit.length > 3)
                    {
                        if(Integer.parseInt(lineSplit[3]) == 0)
                        {
                            raid.add(line);
                            raidActive = true;
                        }
                    }
                }
                else
                {
                    if(lineSplit.length > 3)
                    {
                        if(Integer.parseInt(lineSplit[3]) == 99)
                        {
                            raid.add(line);
                        }
                        else if(Integer.parseInt(lineSplit[3]) == 4)
                        {
                            raid.add(line);
                            raidActive = false;
                            raids.add(new cRoomData(raid.toArray(new String[raid.size()])));
                            raid.clear();
                        }
                        else
                        {
                            raid.add(line);
                        }
                    }
                }
            }
            logReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return raids;
    }

    private void buildComponents()
    {
        JPanel primaryContainer = new JPanel();
        primaryContainer.setLayout(new GridLayout(3, 1));

        JButton viewRaidsButton = new JButton("View Raids");
        JButton refreshRaidsButton = new JButton("Refresh Raids");

        viewRaidsButton.addActionListener(
                al->
                {
                    raids.getContentPane().removeAll();
                    raids.createFrame(raidsData);
                    raids.getContentPane().setBackground(Color.BLACK);
                    raids.repaint();
                    raids.open();
                });

        refreshRaidsButton.addActionListener(
                al->
                {
                    raidsData = getAllRaids();
                    updateRaidCountLabel();
                });

        raidCountLabel = new JLabel("", SwingConstants.CENTER);
        updateRaidCountLabel();
        primaryContainer.add(raidCountLabel);
        primaryContainer.add(viewRaidsButton);
        primaryContainer.add(refreshRaidsButton);

        add(primaryContainer);
    }

    private void updateRaidCountLabel()
    {
        raidCountLabel.setText("Raids Found: " + raidsData.size());
    }

}
