package com.cTimers.panelcomponents;

import com.cTimers.cRoomData;
import com.cTimers.filters.cImplicitFilter;
import com.cTimers.utility.cFilterManager;
import com.cTimers.utility.cRaidsManager;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class cConfirmationDialog extends cFrame
{
    public cConfirmationDialog(String filterName, ArrayList<cImplicitFilter> filters, JFrame root)
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
                cFilterManager.saveOverwriteFilter(filterName, filters);
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

    public cConfirmationDialog(String raidsName, ArrayList<cRoomData> raids, JFrame root, int mark)
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
                cRaidsManager.saveOverwriteRaids(raidsName, raids);
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
