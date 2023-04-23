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
        setPreferredSize(new Dimension(100, 200));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Filter Details"));
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(10, 1));
        for(String s : filter.getFilters())
        {
            JLabel label = new JLabel(s);
            pane.add(label);
        }
        mainPanel.add(pane);
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
}
