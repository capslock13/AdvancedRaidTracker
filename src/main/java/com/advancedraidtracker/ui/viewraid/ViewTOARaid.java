package com.advancedraidtracker.ui.viewraid;

import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.datautility.datapoints.toa.Toa;

public class ViewTOARaid extends BaseFrame
{
    public ViewTOARaid(Toa toaData)
    {
        setTitle("View Raid");
        add(new ViewTOAPanel(toaData));
        setResizable(false);
        pack();
    }
}
