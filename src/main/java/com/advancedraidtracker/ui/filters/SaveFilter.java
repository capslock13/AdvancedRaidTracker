package com.advancedraidtracker.ui.filters;

import com.advancedraidtracker.filters.ImplicitFilter;
import com.advancedraidtracker.filters.FilterManager;
import com.advancedraidtracker.ui.BaseFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.advancedraidtracker.utility.UISwingUtility.*;

public class SaveFilter extends BaseFrame
{
    private final JTextField field;

    public SaveFilter(ArrayList<ImplicitFilter> filters, ArrayList<String> quickFiltersState)
    {
        getContentPane().removeAll();
        setTitle("Save Filter");
        JPanel borderPanel = getTitledPanel("Save Filter");
        JPanel subPanel = getThemedPanel();
        subPanel.setLayout(new GridLayout(1, 0));
        field = getThemedTextField();
        subPanel.add(getThemedLabel("Filter Name: "));
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
        JButton saveButton = getThemedButton("Save just filter");
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
        JButton saveButton = getThemedButton("Save with quick filters");
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
