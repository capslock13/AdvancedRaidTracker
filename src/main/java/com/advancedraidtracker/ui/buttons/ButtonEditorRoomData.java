package com.advancedraidtracker.ui.buttons;

import com.advancedraidtracker.SimpleRaidData;
import com.advancedraidtracker.ui.viewraid.ViewTOBRaid;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ButtonEditorRoomData extends DefaultCellEditor
{

    protected JButton button;
    private String label;
    private boolean isPushed;
    private final ArrayList<SimpleRaidData> data;
    int row;

    public ButtonEditorRoomData(JCheckBox checkBox, ArrayList<SimpleRaidData> data)
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
            ViewTOBRaid raid = new ViewTOBRaid(data.get(row));
            //ViewRaidFrame raid = new ViewRaidFrame(data.get(row));
            raid.open();
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
