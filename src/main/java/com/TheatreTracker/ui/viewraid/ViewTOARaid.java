package com.TheatreTracker.ui.viewraid;

import com.TheatreTracker.SimpleTOAData;
import com.TheatreTracker.ui.BaseFrame;

import javax.swing.*;

public class ViewTOARaid extends BaseFrame
{
    public ViewTOARaid(SimpleTOAData toaData)
    {
        setTitle("View Raid");
        add(new ViewTOAPanel(toaData));
        setResizable(false);
        pack();
    }
}
