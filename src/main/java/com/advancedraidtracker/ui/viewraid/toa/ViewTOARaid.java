package com.advancedraidtracker.ui.viewraid.toa;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.datautility.datapoints.toa.Toa;

public class ViewTOARaid extends BaseFrame
{
    public ViewTOARaid(Toa toaData, AdvancedRaidTrackerConfig config)
    {
        setTitle("View Raid");
        add(new ViewTOAPanel(toaData, config));
        setResizable(false);
        pack();
    }
}
