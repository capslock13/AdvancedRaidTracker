package com.TheatreTracker.panelcomponents;

import lombok.extern.slf4j.Slf4j;
import com.TheatreTracker.filters.Filter;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class ViewFilterBaseFrame extends BaseFrame
{
    public ViewFilterBaseFrame(Filter filter)
    {
        setTitle("View Filter");
        setPreferredSize(new Dimension(300, 300));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Filter Details"));
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(10, 1));
        for (String s : filter.getFilters())
        {
            String[] splitString = s.split("-");
            if (splitString.length != 0)
            {
                JLabel label = new JLabel(splitString[splitString.length - 1]);
                pane.add(label);
            }
        }
        mainPanel.add(pane);
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
}
