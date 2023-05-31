package com.cTimers.panelcomponents;

import com.cTimers.cRoomData;
import com.cTimers.utility.cRaidsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class cSaveRaidsFrame extends cFrame
{
    private JTextField field;
    private JButton saveButton;

    public cSaveRaidsFrame(ArrayList<cRoomData> raids)
    {
        getContentPane().removeAll();
        setTitle("Save Raids");
        JPanel borderPanel = new JPanel(new BorderLayout());
        borderPanel.setBorder(BorderFactory.createTitledBorder("Save Raids"));
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new GridLayout(1, 4));
        field = new JTextField();
        subPanel.add(new JLabel("Raids Name: "));
        subPanel.add(field);
        saveButton = new JButton("Save...");
        saveButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(cRaidsManager.doesRaidExist(field.getText()))
                {
                    cConfirmationDialog dialog = new cConfirmationDialog(field.getText(), raids, (JFrame)(SwingUtilities.getRoot((Component) e.getSource())), 1);
                    dialog.open();
                }
                else
                {
                    cRaidsManager.saveRaids(field.getText(), raids);
                    close();
                }
            }
        });
        subPanel.add(saveButton);
        borderPanel.add(subPanel);
        add(borderPanel);
        pack();
        setLocationRelativeTo(null);
        repaint();
    }
}
