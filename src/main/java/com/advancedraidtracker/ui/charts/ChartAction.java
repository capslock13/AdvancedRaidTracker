package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.utility.wrappers.OutlineBox;
import lombok.Value;

import java.util.List;

@Value
public class ChartAction
{
    List<OutlineBox> boxes;
    ChartActionType actionType;
}
