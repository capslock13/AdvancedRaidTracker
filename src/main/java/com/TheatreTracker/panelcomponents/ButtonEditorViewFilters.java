package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.filters.Filter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class ButtonEditorViewFilters extends DefaultCellEditor
{

    protected JButton button;
    private String label;
    private boolean isPushed;
    int row;
    private ArrayList<Filter> data;

    public ButtonEditorViewFilters(JCheckBox checkBox, ArrayList<Filter> data)
    {
        super(checkBox);
        this.data = data;
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
    public Object getCellEditorValue() {
        if (isPushed)
        {
            ViewFilterBaseFrame frame = new ViewFilterBaseFrame(data.get(row));
            frame.open();
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
