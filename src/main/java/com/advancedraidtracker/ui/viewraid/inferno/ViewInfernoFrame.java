package com.advancedraidtracker.ui.viewraid.inferno;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.datautility.datapoints.inf.Inf;

public class ViewInfernoFrame extends BaseFrame
{
    public ViewInfernoFrame(Inf infData, AdvancedRaidTrackerConfig config)
    {
        setTitle("View Raid");
        setResizable(false);
        pack();
    }
}
