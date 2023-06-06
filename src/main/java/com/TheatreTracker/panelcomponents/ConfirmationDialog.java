package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.filters.ImplicitFilter;
import com.TheatreTracker.utility.FilterManager;
import com.TheatreTracker.utility.RaidsManager;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ConfirmationDialog extends BaseFrame
{
    public ConfirmationDialog(String filterName, ArrayList<ImplicitFilter> filters, JFrame root)
    {
        setTitle("Confirm");
        JPanel panelButtons = new JPanel(new GridLayout(1, 2));
        setLayout(new GridLayout(2, 1));
        add(new JLabel("Are you sure you want to overwrite filter " + filterName + "?"));
        JButton yesButton = new JButton("Yes");
        yesButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                FilterManager.saveOverwriteFilter(filterName, filters);
                close();
                root.setVisible(false);
            }
        });
        JButton noButton = new JButton("No");
        noButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                close();
            }
        });
        panelButtons.add(yesButton);
        panelButtons.add(noButton);
        add(panelButtons);
        pack();
        setLocationRelativeTo(null);
    }

    public ConfirmationDialog(String raidsName, ArrayList<RoomData> raids, JFrame root, int mark)
    {
        setTitle("Confirm");
        JPanel panelButtons = new JPanel(new GridLayout(1, 2));
        setLayout(new GridLayout(2, 1));
        add(new JLabel("Are you sure you want to overwrite raids " + raidsName + "?"));
        JButton yesButton = new JButton("Yes");
        yesButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                RaidsManager.saveOverwriteRaids(raidsName, raids);
                close();
                root.setVisible(false);
            }
        });
        JButton noButton = new JButton("No");
        noButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                close();
            }
        });
        panelButtons.add(yesButton);
        panelButtons.add(noButton);
        add(panelButtons);
        pack();
        setLocationRelativeTo(null);
    }
}
