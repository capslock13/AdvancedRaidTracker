package com.advancedraidtracker.ui.customrenderers;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRendererFavoriteColumn extends JButton implements TableCellRenderer
{
    private java.util.List<Raid> data;
    ImageIcon filled;
    ImageIcon outline;

    public ButtonRendererFavoriteColumn(java.util.List<Raid> data)
    {
        this.data = data;
        setIcon(IconManager.getHeartOutline());
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
            setBackground(table.getBackground());
        }
        setIcon(data.get(row).isFavorite() ? IconManager.getHeartFilled() : IconManager.getHeartOutline());
        return this;
    }
}
