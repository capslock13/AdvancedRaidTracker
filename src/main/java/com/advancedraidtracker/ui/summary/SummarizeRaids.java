package com.advancedraidtracker.ui.summary;

import com.advancedraidtracker.SimpleRaidDataBase;
import com.advancedraidtracker.ui.BaseFrame;

import javax.swing.*;
import java.util.ArrayList;

public class SummarizeRaids extends BaseFrame
{
    public SummarizeRaids(ArrayList<SimpleRaidDataBase> data)
    {
        JPanel panel = new SummarizeRaidsPanel(data);
        add(panel);
        pack();
        open();
    }
}
