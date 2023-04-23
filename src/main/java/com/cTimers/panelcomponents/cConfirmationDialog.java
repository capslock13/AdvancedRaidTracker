package com.cTimers.panelcomponents;

import com.cTimers.utility.cFilterManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class cConfirmationDialog extends cFrame
{
    public cConfirmationDialog(String filterName, String[] filters, JFrame root)
    { //TODO remove filters delete
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
}
