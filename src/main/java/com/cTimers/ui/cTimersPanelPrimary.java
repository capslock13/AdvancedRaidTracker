package com.cTimers.ui;

import com.cTimers.cRoomData;
import com.cTimers.cTimersPlugin;
import com.cTimers.panelcomponents.cFilteredRaidsFrame;
import com.cTimers.utility.cDebugHelper;
import com.google.inject.Inject;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;


public class cTimersPanelPrimary extends PluginPanel
{
    private JButton viewRaidsButton;

    private JButton refreshRaidsButton;

    private JLabel raidCountLabel;

    private JPanel primaryContainer;

    private ArrayList<cRoomData> raidsData;

    private cFilteredRaidsFrame raids;

    private final cTimersPlugin plugin;
    @Inject
    cTimersPanelPrimary(cTimersPlugin plugin)
    {
        this.plugin = plugin;
        raidsData = getAllRaids();
        raids = new cFilteredRaidsFrame();
        buildComponents();
    }

    private ArrayList<cRoomData> getAllRaids()
    {
        ArrayList<cRoomData> raids = new ArrayList<>();
        try
        {
            String path = (cDebugHelper.raidDataSource == cDebugHelper.PRIMARY_FILE) ? "/.runelite/logs/tobdata.log" : "/.runelite/logs/altdata.log";
            File logFile = new File(System.getProperty("user.home").replace("\\", "/") + path);
            Scanner logReader = new Scanner(new FileInputStream(logFile));
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
        primaryContainer = new JPanel();
        primaryContainer.setLayout(new GridLayout(3, 1));

        viewRaidsButton = new JButton("View Raids");
        refreshRaidsButton = new JButton("Refresh Raids");

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
