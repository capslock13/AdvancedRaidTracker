package com.cTimers.panelcomponents;

import com.cTimers.cRoomData;
import com.cTimers.filters.cFilter;
import com.cTimers.panelcomponents.cFilteredRaidsFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class cButtonEditorComparisonData extends DefaultCellEditor
{
    protected JButton button;
    private String label;
    private boolean isPushed;
    private cFilteredRaidsFrame frame;
    int row;

    public cButtonEditorComparisonData(JCheckBox checkBox, cFilteredRaidsFrame raidsFrame)
    {
        super(checkBox);
        frame = raidsFrame;
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
    public Object getCellEditorValue()
    {
        if (isPushed)
        {
            frame.removeComparisonRow(row);
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
