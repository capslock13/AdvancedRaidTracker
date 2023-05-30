package com.cTimers.panelcomponents;

import com.cTimers.cRoomData;
import com.cTimers.utility.cDataPoint;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class cStatisticTab extends JPanel
{
    private final ArrayList<cStatisticRoomPanel> panels;
    cDataPoint.rooms room;
    ArrayList<cRoomData> data;
    public cStatisticTab(ArrayList<cRoomData> data, cDataPoint.rooms room)
    {
        this.room = room;
        this.data = data;
        JPanel panel = new JPanel();
        panels = new ArrayList<>();
        panel.setLayout(new GridLayout(2, 3));
        panels.add(new cStatisticRoomPanel(data, cStatisticRoomPanel.stat.AVERAGE, room));
        panels.add(new cStatisticRoomPanel(data, cStatisticRoomPanel.stat.MEDIAN, room));
        panels.add(new cStatisticRoomPanel(data, cStatisticRoomPanel.stat.MODE, room));
        panels.add(new cStatisticRoomPanel(data, cStatisticRoomPanel.stat.MINIMUM, room));
        panels.add(new cStatisticRoomPanel(data, cStatisticRoomPanel.stat.MAXIMUM, room));
        setLayout(new GridLayout(2, 3));
        for(cStatisticRoomPanel roomPanel : panels)
        {
            add(roomPanel);
        }
        JPanel statistics = new JPanel();
        statistics.setBorder(BorderFactory.createTitledBorder("Statistics"));
        add(statistics);

        validate();
        repaint();
    }
    public void updateTab(ArrayList<cRoomData> data)
    {
        for(cStatisticRoomPanel roomPanel : panels)
        {
            roomPanel.updateLabels(data);
        }
    }


}
