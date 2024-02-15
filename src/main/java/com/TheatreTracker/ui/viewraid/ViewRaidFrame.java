package com.TheatreTracker.ui.viewraid;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.ui.BaseFrame;
public class ViewRaidFrame extends BaseFrame
{
    public ViewRaidFrame(RoomData roomData)
    {
        setTitle("View Raid");
        ViewRaidPanel primaryPanel = new ViewRaidPanel(roomData);
        add(primaryPanel);
        pack();
    }
}
