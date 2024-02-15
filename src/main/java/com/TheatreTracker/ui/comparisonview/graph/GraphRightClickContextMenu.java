package com.TheatreTracker.ui.comparisonview.graph;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.ui.Raids;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.ArrayList;

@Slf4j
public class GraphRightClickContextMenu extends JPopupMenu
{
    JMenuItem item;

    public GraphRightClickContextMenu(ArrayList<RoomData> raids, TheatreTrackerConfig config)
    {
        item = new JMenuItem("Show Represented Raids In New Window");
        item.addActionListener(al ->
        {
            Raids raidFrame = new Raids(config);
            raidFrame.createFrame(raids);
            raidFrame.repaint();
            raidFrame.open();
        });
        add(item);
    }
}
