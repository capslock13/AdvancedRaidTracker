package com.advancedraidtracker.ui.viewraid;

import com.advancedraidtracker.SimpleTOAData;
import com.advancedraidtracker.ui.BaseFrame;

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
