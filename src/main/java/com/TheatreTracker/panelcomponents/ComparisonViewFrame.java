package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.RoomData;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
@Slf4j
public class ComparisonViewFrame extends BaseFrame
{
    ComparisonViewFrame(ArrayList<ArrayList<RoomData>> data, ArrayList<String> labels)
    {
        add(new ComparisonViewPanel(data, labels));
        pack();
    }

    ComparisonViewFrame(Map<Integer, ArrayList<ArrayList<RoomData>>> dataSets, ArrayList<ArrayList<String>> labelSets)
    {
        JTabbedPane pane = new JTabbedPane();
        pane.setBackground(Color.BLACK);
        pane.setOpaque(true);
        int index = 0;
        for(Integer i : dataSets.keySet())
        {
            String tabName = "";
            if(i == 1)
                tabName = "Solo";
            if(i == 2)
                tabName = "Duo";
            if(i == 3)
                tabName = "Trio";
            if(i == 4)
                tabName = "4-Man";
            if(i == 5)
                tabName = "5-Man";
            pane.addTab(tabName, new ComparisonViewPanel(dataSets.get(i), labelSets.get(index)));
            index++;
        }
        add(pane);
        pack();
    }
}
