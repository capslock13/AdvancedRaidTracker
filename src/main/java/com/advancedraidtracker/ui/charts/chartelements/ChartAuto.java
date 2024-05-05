package com.advancedraidtracker.ui.charts.chartelements;

public class ChartAuto
{
	public final int tick;

	public ChartAuto(int tick)
	{
		this.tick = tick;
	}

	@Override
	public String toString()
	{
		return "Tick: " + tick;
	}
}
