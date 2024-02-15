package com.TheatreTracker.ui.summary;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.ui.BaseFrame;
import com.TheatreTracker.ui.summary.SummarizeRaidsPanel;

import javax.swing.*;
import java.util.ArrayList;

public class SummarizeRaids extends BaseFrame
{
    public SummarizeRaids(ArrayList<RoomData> data)
    {
        JPanel panel = new SummarizeRaidsPanel(data);
        add(panel);
        pack();
        open();
    }
}
