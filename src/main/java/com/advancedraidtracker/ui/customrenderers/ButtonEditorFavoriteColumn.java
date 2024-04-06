package com.advancedraidtracker.ui.customrenderers;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.viewraid.colosseum.ViewColosseumFrame;
import com.advancedraidtracker.ui.viewraid.inferno.ViewInfernoFrame;
import com.advancedraidtracker.ui.viewraid.toa.ViewTOARaid;
import com.advancedraidtracker.ui.viewraid.tob.ViewTOBRaid;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.col.Colo;
import com.advancedraidtracker.utility.datautility.datapoints.inf.Inf;
import com.advancedraidtracker.utility.datautility.datapoints.toa.Toa;
import com.advancedraidtracker.utility.datautility.datapoints.tob.Tob;
import net.runelite.client.game.ItemManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.advancedraidtracker.utility.UISwingUtility.getThemedButton;

public class ButtonEditorFavoriteColumn extends DefaultCellEditor
{

    protected JButton button;
    private String label;
    private boolean isPushed;
    private final List<Raid> data;
    private final AdvancedRaidTrackerConfig config;
    private final ItemManager itemManager;
    int row;

    public ButtonEditorFavoriteColumn(JCheckBox checkBox, List<Raid> data, AdvancedRaidTrackerConfig config, ItemManager itemManager)
    {
        super(checkBox);
        this.config = config;
        this.itemManager = itemManager;
        this.data = data;
        button = getThemedButton();
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
        isPushed = true;
        this.row = row;
        return button;
    }

    @Override
    public Object getCellEditorValue()
    {
        if (isPushed)
        {
            data.get(row).setFavorite(!data.get(row).isFavorite());
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
