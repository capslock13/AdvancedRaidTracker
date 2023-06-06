package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.utility.DataPoint;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StatisticTab extends JPanel
{
    private final ArrayList<StatisticRoomPanel> panels;
    DataPoint.rooms room;
    ArrayList<RoomData> data;
    public StatisticTab(ArrayList<RoomData> data, DataPoint.rooms room)
    {
        this.room = room;
        this.data = data;
        JPanel panel = new JPanel();
        panels = new ArrayList<>();
        panel.setLayout(new GridLayout(2, 3));
        panels.add(new StatisticRoomPanel(data, StatisticRoomPanel.stat.AVERAGE, room));
        panels.add(new StatisticRoomPanel(data, StatisticRoomPanel.stat.MEDIAN, room));
        panels.add(new StatisticRoomPanel(data, StatisticRoomPanel.stat.MODE, room));
        panels.add(new StatisticRoomPanel(data, StatisticRoomPanel.stat.MINIMUM, room));
        panels.add(new StatisticRoomPanel(data, StatisticRoomPanel.stat.MAXIMUM, room));
        setLayout(new GridLayout(2, 3));
        for(StatisticRoomPanel roomPanel : panels)
        {
            add(roomPanel);
        }
        JPanel statistics = new JPanel();
        statistics.setBorder(BorderFactory.createTitledBorder("Statistics"));
        add(statistics);

        validate();
        repaint();
    }
    public void updateTab(ArrayList<RoomData> data)
    {
        for(StatisticRoomPanel roomPanel : panels)
        {
            roomPanel.updateLabels(data);
        }
    }


}
