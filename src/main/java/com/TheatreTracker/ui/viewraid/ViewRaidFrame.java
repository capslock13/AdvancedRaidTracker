package com.TheatreTracker.ui.viewraid;

import com.TheatreTracker.SimpleRaidData;
import com.TheatreTracker.ui.BaseFrame;
public class ViewRaidFrame extends BaseFrame
{
    public ViewRaidFrame(SimpleRaidData roomData)
    {
        setTitle("View Raid");
        ViewRaidPanel primaryPanel = new ViewRaidPanel(roomData);
        add(primaryPanel);
        pack();
    }
}
