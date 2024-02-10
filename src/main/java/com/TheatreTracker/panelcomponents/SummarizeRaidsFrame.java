package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.RoomData;

import javax.swing.*;
import java.util.ArrayList;

public class SummarizeRaidsFrame extends BaseFrame
{
    public SummarizeRaidsFrame(ArrayList<RoomData> data)
    {
        JPanel panel = new SummarizeRaidsPanel(data);
        add(panel);
        pack();
        open();
    }
}
