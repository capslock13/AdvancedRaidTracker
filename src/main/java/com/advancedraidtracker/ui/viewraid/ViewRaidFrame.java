package com.advancedraidtracker.ui.viewraid;

import com.advancedraidtracker.SimpleTOBData;
import com.advancedraidtracker.ui.BaseFrame;

public class ViewRaidFrame extends BaseFrame
{
    public ViewRaidFrame(SimpleTOBData roomData)
    {
        setTitle("View Raid");
        ViewRaidPanel primaryPanel = new ViewRaidPanel(roomData);
        add(primaryPanel);
        pack();
    }
}
