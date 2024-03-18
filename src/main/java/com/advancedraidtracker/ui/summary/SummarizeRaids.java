package com.advancedraidtracker.ui.summary;

import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;

import javax.swing.*;
import java.util.ArrayList;

public class SummarizeRaids extends BaseFrame
{
    public SummarizeRaids(ArrayList<Raid> data)
    {
        JPanel panel = new SummarizeRaidsPanel(data);
        add(panel);
        pack();
        open();
    }
}
