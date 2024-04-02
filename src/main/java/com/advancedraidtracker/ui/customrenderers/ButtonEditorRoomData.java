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

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ButtonEditorRoomData extends DefaultCellEditor
{

    protected JButton button;
    private String label;
    private boolean isPushed;
    private final List<Raid> data;
    private final AdvancedRaidTrackerConfig config;
    int row;

    public ButtonEditorRoomData(JCheckBox checkBox, List<Raid> data, AdvancedRaidTrackerConfig config)
    {
        super(checkBox);
        this.config = config;
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
            //data.get(row).dumpValues();
            if (data.get(row) instanceof Tob)
            {
                Tob tobData = (Tob) data.get(row);
                ViewTOBRaid raid = new ViewTOBRaid(tobData);
                //ViewRaidFrame raid = new ViewRaidFrame(data.get(row));
                raid.open();
            }
            else if (data.get(row) instanceof Toa)
            {
                Toa toaData = (Toa) data.get(row);
                ViewTOARaid raid = new ViewTOARaid(toaData, config);
                raid.open();
            }
            else if(data.get(row) instanceof Inf)
            {
                Inf infData = (Inf) data.get(row);
                ViewInfernoFrame raid = new ViewInfernoFrame(infData, config);
                raid.open();
            }
            else if(data.get(row) instanceof Colo)
            {
                Colo colData = (Colo) data.get(row);
                ViewColosseumFrame raid = new ViewColosseumFrame(colData, config);
                raid.open();
            }

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
