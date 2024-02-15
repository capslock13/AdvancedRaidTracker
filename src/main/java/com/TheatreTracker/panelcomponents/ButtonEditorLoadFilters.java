package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.filters.Filter;
import com.TheatreTracker.filters.ImplicitFilter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

@Slf4j
class ButtonEditorLoadFilters extends DefaultCellEditor
{

    protected JButton button;
    private String label;
    private boolean isPushed;

    private final boolean replace;
    int row;
    private final ArrayList<Filter> data;
    private final BaseFrame closeBaseFrame;

    private final FilteredRaidsBaseFrame filteredRaidsFrame;

    public ButtonEditorLoadFilters(JCheckBox checkBox, FilteredRaidsBaseFrame filteredRaidsFrame, ArrayList<Filter> data, LoadFilterBaseFrame loadFrame)
    {
        super(checkBox);
        this.data = data;
        this.replace = true;
        this.filteredRaidsFrame = filteredRaidsFrame;
        this.closeBaseFrame = loadFrame;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                fireEditingStopped();
            }
        });
    }

    public ButtonEditorLoadFilters(JCheckBox checkBox, FilteredRaidsBaseFrame filteredRaidsFrame, ArrayList<Filter> data, LoadFilterBaseFrame loadFrame, boolean replace)
    {
        super(checkBox);
        this.data = data;
        this.filteredRaidsFrame = filteredRaidsFrame;
        this.closeBaseFrame = loadFrame;
        button = new JButton();
        button.setOpaque(true);
        this.replace = replace;
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
        if (isSelected)
        {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else
        {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        this.row = row;
        return button;
    }


    @Override
    public Object getCellEditorValue()
    {
        if (isPushed)
        {

            if (replace)
                filteredRaidsFrame.activeFilters.clear();
            for (String s : data.get(row).getFilters())
            {
                filteredRaidsFrame.activeFilters.add(new ImplicitFilter(s));
            }
            filteredRaidsFrame.updateFilterTable();
            closeBaseFrame.close();
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing()
    {
        isPushed = false;
        return super.stopCellEditing();
    }
}
