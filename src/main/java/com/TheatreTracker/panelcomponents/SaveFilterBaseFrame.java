package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.filters.ImplicitFilter;
import com.TheatreTracker.utility.FilterManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SaveFilterBaseFrame extends BaseFrame
{
    private JTextField field;
    private JButton saveButton;

    public SaveFilterBaseFrame(ArrayList<ImplicitFilter> filters)
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
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(FilterManager.doesFilterExist(field.getText()))
                {
                    ConfirmationDialog dialog = new ConfirmationDialog(field.getText(), filters, (JFrame)(SwingUtilities.getRoot((Component) e.getSource())));
                    dialog.open();
                }
                else
                {
                    FilterManager.saveFilter(field.getText(), filters);
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
