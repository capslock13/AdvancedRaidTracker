package com.advancedraidtracker.ui.charts.chartelements;

public class ChartLine
{
	public final String text;
	public final int tick;
	public ChartLine(String text, int tick)
	{
		this.text = text;
		this.tick = tick;
	}

	@Override
	public String toString()
	{
		return tick + ": " + ((text.isEmpty()) ? "<no text>" : text);
	}
}
