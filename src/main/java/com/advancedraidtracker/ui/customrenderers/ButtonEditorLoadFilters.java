package com.advancedraidtracker.ui.customrenderers;

import com.advancedraidtracker.filters.Filter;
import com.advancedraidtracker.filters.ImplicitFilter;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.ui.Raids;
import com.advancedraidtracker.ui.filters.LoadFilter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@Slf4j
public class ButtonEditorLoadFilters extends DefaultCellEditor
{

    protected JButton button;
    private String label;
    private boolean isPushed;
    private final boolean replace;
    int row;
    private final ArrayList<Filter> data;
    private final BaseFrame closeBaseFrame;
    private final Raids filteredRaidsFrame;

    public ButtonEditorLoadFilters(JCheckBox checkBox, Raids filteredRaidsFrame, ArrayList<Filter> data, LoadFilter loadFrame)
    {
        super(checkBox);
        this.data = data;
        this.replace = true;
        this.filteredRaidsFrame = filteredRaidsFrame;
        this.closeBaseFrame = loadFrame;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public ButtonEditorLoadFilters(JCheckBox checkBox, Raids filteredRaidsFrame, ArrayList<Filter> data, LoadFilter loadFrame, boolean replace)
    {
        super(checkBox);
        this.data = data;
        this.filteredRaidsFrame = filteredRaidsFrame;
        this.closeBaseFrame = loadFrame;
        button = new JButton();
        button.setOpaque(true);
        this.replace = replace;
        button.addActionListener(e -> fireEditingStopped());
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
                if (!s.startsWith("QF-"))
                {
                    filteredRaidsFrame.activeFilters.add(new ImplicitFilter(s));
                } else
                {
                    filteredRaidsFrame.setFilterState(s.substring(3));
                }
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
