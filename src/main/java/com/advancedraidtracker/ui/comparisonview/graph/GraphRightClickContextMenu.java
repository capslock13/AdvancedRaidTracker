package com.advancedraidtracker.ui.comparisonview.graph;

import com.advancedraidtracker.SimpleRaidDataBase;
import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.Raids;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;

import javax.swing.*;
import java.util.ArrayList;

@Slf4j
public class GraphRightClickContextMenu extends JPopupMenu
{
    JMenuItem item;

    public GraphRightClickContextMenu(ArrayList<Raid> raids, AdvancedRaidTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager)
    {
        item = new JMenuItem("Show Represented Raids In New Window");
        item.addActionListener(al ->
        {
            Raids raidFrame = new Raids(config, itemManager, clientThread, configManager);
            raidFrame.createFrame(raids);
            raidFrame.repaint();
            raidFrame.open();
        });
        add(item);
    }
}
