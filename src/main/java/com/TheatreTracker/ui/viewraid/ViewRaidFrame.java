package com.TheatreTracker.ui.viewraid;

import com.TheatreTracker.SimpleTOBData;
import com.TheatreTracker.ui.BaseFrame;
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
