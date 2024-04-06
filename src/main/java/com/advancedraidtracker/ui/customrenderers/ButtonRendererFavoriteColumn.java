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

    public ButtonRendererFavoriteColumn(AdvancedRaidTrackerConfig config, java.util.List<Raid> data)
    {
        this.data = data;
        filled = new ImageIcon(ImageUtil.loadImageResource(AdvancedRaidTrackerPlugin.class, "/com/advancedraidtracker/heartfilled.png").getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        outline = new ImageIcon(ImageUtil.loadImageResource(AdvancedRaidTrackerPlugin.class, "/com/advancedraidtracker/heartoutline.png").getScaledInstance(16, 16, Image.SCALE_SMOOTH));

        setIcon(outline);
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
        setIcon(data.get(row).isFavorite() ? filled : outline);
        return this;
    }
}
