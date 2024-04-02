package com.advancedraidtracker.ui.viewraid.colosseum;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.datautility.datapoints.col.Colo;

public class ViewColosseumFrame extends BaseFrame
{
    public ViewColosseumFrame(Colo colData, AdvancedRaidTrackerConfig config)
    {
        setTitle("View Raid");
        add(new ViewColosseumPanel(colData, config));
        setResizable(false);
        pack();
    }
}
