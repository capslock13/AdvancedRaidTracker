package com.advancedraidtracker.ui.charts;

public enum ChartObjectType
{
	ATTACK("Attacks"),
	LINE("Lines"),
	TEXT("Text"),
	AUTO("Autos"),
	THRALL("Thralls"),
	;

	public final String name;
	ChartObjectType(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
