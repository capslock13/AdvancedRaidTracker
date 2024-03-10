package com.advancedraidtracker.ui.comparisonview;

import com.advancedraidtracker.SimpleRaidDataBase;
import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class ComparisonViewFrame extends BaseFrame
{
    public ComparisonViewFrame(ArrayList<ArrayList<Raid>> data, ArrayList<String> labels)
    {
        add(new ComparisonViewPanel(data, labels, config, itemManager, clientThread, configManager));
        pack();
    }

    private AdvancedRaidTrackerConfig config;
    private ItemManager itemManager;
    private ClientThread clientThread;
    private ConfigManager configManager;

    public ComparisonViewFrame(Map<Integer, ArrayList<ArrayList<Raid>>> dataSets, ArrayList<ArrayList<String>> labelSets, AdvancedRaidTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager)
    {
        this.configManager = configManager;
        this.clientThread = clientThread;
        this.itemManager = itemManager;
        this.config = config;
        JTabbedPane pane = new JTabbedPane();
        pane.setBackground(Color.BLACK);
        pane.setOpaque(true);
        int index = 0;
        for (Integer i : dataSets.keySet())
        {
            String tabName = "";
            if (i == 1)
                tabName = "Solo";
            if (i == 2)
                tabName = "Duo";
            if (i == 3)
                tabName = "Trio";
            if (i == 4)
                tabName = "4-Man";
            if (i == 5)
                tabName = "5-Man";
            pane.addTab(tabName, new ComparisonViewPanel(dataSets.get(i), labelSets.get(index), config, itemManager, clientThread, configManager));
            index++;
        }
        add(pane);
        pack();
    }
}
