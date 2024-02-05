package com.TheatreTracker.viewraid;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.panelcomponents.BaseFrame;
import com.TheatreTracker.utility.DataPoint;
import lombok.extern.slf4j.Slf4j;

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
