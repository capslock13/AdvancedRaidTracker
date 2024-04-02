package com.advancedraidtracker.ui.filters;

import com.advancedraidtracker.ui.BaseFrame;
import lombok.extern.slf4j.Slf4j;
import com.advancedraidtracker.filters.Filter;

import javax.swing.*;
import java.awt.*;

import static com.advancedraidtracker.utility.UISwingUtility.*;

@Slf4j
public class ViewFilter extends BaseFrame
{
    public ViewFilter(Filter filter)
    {
        setTitle("View Filter");
        setPreferredSize(new Dimension(300, 300));
        JPanel mainPanel = getTitledPanel("Filter Details");
        JPanel pane = getThemedPanel();
        pane.setLayout(new GridLayout(10, 1));
        for (String s : filter.getFilters())
        {
            String[] splitString = s.split("-");
            if (splitString.length != 0)
            {
                JLabel label = getThemedLabel(splitString[splitString.length - 1]);
                pane.add(label);
            }
        }
        mainPanel.add(pane);
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
}
