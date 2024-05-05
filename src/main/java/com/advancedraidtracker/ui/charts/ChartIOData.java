package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.ui.charts.chartelements.ChartAuto;
import com.advancedraidtracker.ui.charts.chartelements.ChartLine;
import com.advancedraidtracker.ui.charts.chartelements.ChartTextBox;
import com.advancedraidtracker.ui.charts.chartelements.OutlineBox;
import com.advancedraidtracker.ui.charts.chartelements.ThrallOutlineBox;
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
    List<ChartAuto> autos;
    Map<Integer, String> roomSpecificTextMapping;
    List<ChartLine> lines;
    List<OutlineBox> outlineBoxes;
    List<ChartTextBox> textMapping;
    String title;
	List<ThrallOutlineBox> thrallOutlineBoxes;
}
