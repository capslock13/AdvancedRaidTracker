package com.TheatreTracker.ui.buttons;

import com.TheatreTracker.ui.Raids;

import javax.swing.*;
import java.awt.*;

public class ButtonEditorFilterData extends DefaultCellEditor
{
    protected JButton button;
    private String label;
    private boolean isPushed;
    private final Raids frame;
    int row;

    public ButtonEditorFilterData(JCheckBox checkBox, Raids raidsFrame)
    {
        super(checkBox);
        frame = raidsFrame;
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
            frame.removeFilterRow(row);
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
