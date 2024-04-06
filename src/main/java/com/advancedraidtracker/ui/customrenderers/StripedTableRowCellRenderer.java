package com.advancedraidtracker.ui.customrenderers;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StripedTableRowCellRenderer extends DefaultTableCellRenderer
{
    private AdvancedRaidTrackerConfig config;

    public StripedTableRowCellRenderer(AdvancedRaidTrackerConfig config)
    {
        super();
        this.config = config;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component cell = new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!isSelected)
        {
            if (row % 2 == 0)
            {
                cell.setBackground(config.primaryDark());
            } else
            {
                cell.setBackground(config.primaryMiddle());
            }
        } else
        {
            Color fontColor = config.fontColor();
            cell.setBackground(new Color(fontColor.getRed(), fontColor.getGreen(), fontColor.getBlue(), 100));
        }
        return cell;
    }
}
