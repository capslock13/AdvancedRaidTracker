package com.advancedraidtracker.ui.charts;

import java.util.List;

public class ChartChangedEvent
{
	public ChartActionType actionType;
	public ChartObjectType objectType;
	public List<Object> chartObjects;

	public ChartChangedEvent(ChartActionType actionType, ChartObjectType objectType, Object... chartObjects)
	{
		this.actionType = actionType;
		this.chartObjects = List.of(chartObjects);
		this.objectType = objectType;
	}
}
