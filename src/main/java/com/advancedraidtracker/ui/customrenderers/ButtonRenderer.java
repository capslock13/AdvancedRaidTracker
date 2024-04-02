package com.advancedraidtracker.ui.customrenderers;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer
{

    public ButtonRenderer(AdvancedRaidTrackerConfig config)
    {
        setOpaque(true);
        setBackground(config.primaryMiddle());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (isSelected)
        {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else
        {
            setForeground(table.getForeground());
        }
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

