package com.advancedraidtracker.ui.filters;

import com.advancedraidtracker.filters.ImplicitFilter;
import com.advancedraidtracker.filters.FilterManager;
import com.advancedraidtracker.ui.BaseFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SaveFilter extends BaseFrame
{
    private final JTextField field;

    public SaveFilter(ArrayList<ImplicitFilter> filters, ArrayList<String> quickFiltersState)
    {
        getContentPane().removeAll();
        setTitle("Save Filter");
        JPanel borderPanel = new JPanel(new BorderLayout());
        borderPanel.setBorder(BorderFactory.createTitledBorder("Save Filter"));
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new GridLayout(1, 0));
        field = new JTextField();
        subPanel.add(new JLabel("Filter Name: "));
        subPanel.add(field);
        JButton saveButton = getSaveButton(filters, quickFiltersState);
        subPanel.add(saveButton);
        JButton saveButtonStrict = getSaveButton(filters);
        subPanel.add(saveButtonStrict);
        borderPanel.add(subPanel);
        add(borderPanel);
        pack();
        setLocationRelativeTo(null);
        repaint();
    }

    private JButton getSaveButton(ArrayList<ImplicitFilter> filters)
    {
        JButton saveButton = new JButton("Save just filter");
        saveButton.addActionListener(e ->
        {
            if (FilterManager.doesFilterExist(field.getText()))
            {
                ConfirmationDialog dialog = new ConfirmationDialog(field.getText(), filters, (JFrame) (SwingUtilities.getRoot((Component) e.getSource())));
                dialog.open();
            } else
            {
                FilterManager.saveFilter(field.getText(), filters);
                close();
            }
        });
        return saveButton;
    }

    private JButton getSaveButton(ArrayList<ImplicitFilter> filters, ArrayList<String> quickFiltersState)
    {
        JButton saveButton = new JButton("Save with quick filters");
        saveButton.addActionListener(e ->
        {
            if (FilterManager.doesFilterExist(field.getText()))
            {
                ConfirmationDialog dialog = new ConfirmationDialog(field.getText(), filters, (JFrame) (SwingUtilities.getRoot((Component) e.getSource())));
                dialog.open();
            } else
            {
                FilterManager.saveFilter(field.getText(), filters, quickFiltersState);
                close();
            }
        });
        return saveButton;
    }
}
