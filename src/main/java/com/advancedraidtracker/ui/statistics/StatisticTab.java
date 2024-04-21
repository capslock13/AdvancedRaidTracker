package com.advancedraidtracker.ui.statistics;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.advancedraidtracker.utility.UISwingUtility.getThemedPanel;
import static com.advancedraidtracker.utility.UISwingUtility.getTitledPanel;

public class StatisticTab extends JPanel
{
    private final ArrayList<StatisticRoomPanel> panels;
    java.util.List<Raid> data;

    public StatisticTab(java.util.List<Raid> data, String room, AdvancedRaidTrackerConfig config)
    {
        setBackground(config.primaryDark());
        setOpaque(true);
        this.data = data;
        panels = new ArrayList<>();
        panels.add(new StatisticRoomPanel(data, StatisticRoomPanel.stat.AVERAGE, room, config));
        panels.add(new StatisticRoomPanel(data, StatisticRoomPanel.stat.MEDIAN, room, config));
        panels.add(new StatisticRoomPanel(data, StatisticRoomPanel.stat.MODE, room, config));
        panels.add(new StatisticRoomPanel(data, StatisticRoomPanel.stat.MINIMUM, room, config));
        panels.add(new StatisticRoomPanel(data, StatisticRoomPanel.stat.MAXIMUM, room, config));
        setLayout(new GridLayout(2, 3));
        for (StatisticRoomPanel roomPanel : panels)
        {
            add(roomPanel);
        }
        JPanel statistics = getTitledPanel("Statistics");
        add(statistics);

        validate();
        repaint();
    }

    public void updateTab(java.util.List<Raid> data)
    {
        for (StatisticRoomPanel roomPanel : panels)
        {
            roomPanel.updateLabels(data);
        }
    }


}
