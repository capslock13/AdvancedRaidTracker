package com.advancedraidtracker.ui.customrenderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StripedTableRowCellRenderer extends DefaultTableCellRenderer
{
    public StripedTableRowCellRenderer()
    {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component cell = new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row % 2 == 0 && !isSelected)
        {
            cell.setBackground(new Color(20, 20, 20));
        }
        else if(isSelected)
        {
            cell.setBackground(new Color(120, 120, 120, 200));
        }
        return cell;
    }
}
