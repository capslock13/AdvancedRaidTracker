package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.utility.Point;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class ChartIOData
{
    int startTick;
    int endTick;
    String roomName;
    String roomSpecificText;
    List<Integer> autos;
    Map<Integer, String> roomSpecificTextMapping;
    Map<Integer, String> lines;
    List<OutlineBox> outlineBoxes;
    Map<Point, String> textMapping;
    String title;
}
