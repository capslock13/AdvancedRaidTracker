package com.TheatreTracker.ui.summary;

import com.TheatreTracker.SimpleRaidData;
import com.TheatreTracker.SimpleTOBData;
import com.TheatreTracker.ui.BaseFrame;

import javax.swing.*;
import java.util.ArrayList;

public class SummarizeRaids extends BaseFrame
{
    public SummarizeRaids(ArrayList<SimpleRaidData> data)
    {
        JPanel panel = new SummarizeRaidsPanel(data);
        add(panel);
        pack();
        open();
    }
}
