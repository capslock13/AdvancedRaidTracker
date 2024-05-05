package com.advancedraidtracker.ui.charts.chartelements;

import com.advancedraidtracker.utility.Point;
import lombok.Getter;
import lombok.Setter;

public class ChartTextBox
{
	@Setter
	@Getter
	public String text;

	@Setter
	@Getter
	public Point point;

	public ChartTextBox(String text, Point point)
	{
		this.text = text;
		this.point = point;
	}

	@Override
	public String toString()
	{
		return point.toString() + ": \"" + text + "\"";
	}
}
