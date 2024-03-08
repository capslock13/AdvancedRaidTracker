package com.advancedraidtracker.ui.buttons;

import com.advancedraidtracker.filters.Filter;
import com.advancedraidtracker.ui.filters.ViewFilter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ButtonEditorViewFilters extends DefaultCellEditor
{

    protected JButton button;
    private String label;
    private boolean isPushed;
    int row;
    private final ArrayList<Filter> data;

    public ButtonEditorViewFilters(JCheckBox checkBox, ArrayList<Filter> data)
    {
        super(checkBox);
        this.data = data;
        button = new JButton();
        button.setOpaque(true);
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
            ViewFilter frame = new ViewFilter(data.get(row));
            frame.open();
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
