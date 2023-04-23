package com.cTimers.panelcomponents;

import com.cTimers.utility.cFilterManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class cSaveFilterFrame extends cFrame
{
    private JTextField field;
    private JButton saveButton;

    public void updateFilterFrame(String[] filters)
    {
        getContentPane().removeAll();
        setTitle("Save Filter");
        JPanel borderPanel = new JPanel(new BorderLayout());
        borderPanel.setBorder(BorderFactory.createTitledBorder("Save Filter"));
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new GridLayout(1, 4));
        field = new JTextField();
        subPanel.add(new JLabel("Filter Name: "));
        subPanel.add(field);
        saveButton = new JButton("Save...");
        saveButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(cFilterManager.doesFilterExist(field.getText()))
                {
                    cConfirmationDialog dialog = new cConfirmationDialog(field.getText(), filters, (JFrame)(SwingUtilities.getRoot((Component) e.getSource())));
                    dialog.open();
                }
                else
                {
                    cFilterManager.saveFilter(field.getText(), filters);
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

    public cSaveFilterFrame()
    {
        updateFilterFrame(new String[] { });
    }
}
