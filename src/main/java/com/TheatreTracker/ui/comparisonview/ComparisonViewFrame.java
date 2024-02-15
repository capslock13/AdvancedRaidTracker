package com.TheatreTracker.ui.comparisonview;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.ui.BaseFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class ComparisonViewFrame extends BaseFrame
{
    public ComparisonViewFrame(ArrayList<ArrayList<RoomData>> data, ArrayList<String> labels)
    {
        add(new ComparisonViewPanel(data, labels, config));
        pack();
    }

    private TheatreTrackerConfig config;

    public ComparisonViewFrame(Map<Integer, ArrayList<ArrayList<RoomData>>> dataSets, ArrayList<ArrayList<String>> labelSets, TheatreTrackerConfig config)
    {
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
            pane.addTab(tabName, new ComparisonViewPanel(dataSets.get(i), labelSets.get(index), config));
            index++;
        }
        add(pane);
        pack();
    }
}
