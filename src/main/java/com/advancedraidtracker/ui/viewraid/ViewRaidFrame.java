package com.advancedraidtracker.ui.viewraid;

import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;

public class ViewRaidFrame extends BaseFrame
{
    public ViewRaidFrame(Raid roomData)
    {
        setTitle("View Raid");
        ViewRaidPanel primaryPanel = new ViewRaidPanel(roomData);
        add(primaryPanel);
        pack();
    }
}
