package com.cTimers.panelcomponents;

import lombok.extern.slf4j.Slf4j;
import com.cTimers.filters.cFilter;

import javax.swing.*;
import java.awt.*;
@Slf4j
public class cViewFilterFrame extends cFrame
{
    public cViewFilterFrame(cFilter filter)
    {
        setTitle("View Filter");
        setPreferredSize(new Dimension(300, 300));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Filter Details"));
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(10, 1));
        for(String s : filter.getFilters())
        {
            String[] splitString = s.split("-");
            if(splitString.length != 0)
            {
                JLabel label = new JLabel(splitString[splitString.length-1]);
                pane.add(label);
            }
        }
        mainPanel.add(pane);
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
}
