package com.advancedraidtracker.ui.charts.chartelements;

import java.awt.*;

import static com.advancedraidtracker.constants.TobIDs.*;

public class ThrallOutlineBox
{
    public int spawnTick;
    public String owner;
    public int id;
	public int duration;

    public ThrallOutlineBox(String owner, int spawnTick, int id)
    {
		this(owner, spawnTick, id, 99);
    }

	public ThrallOutlineBox(String owner, int spawnTick, int id, int duration)
	{
		this.spawnTick = spawnTick;
		this.owner = owner;
		this.id = id;
		this.duration = duration;
	}

    public Color getColor()
    {
        switch (id)
        {
            case MELEE_THRALL:
                return new Color(240, 30, 30);
            case RANGE_THRALL:
                return new Color(30, 240, 30);
            case MAGE_THRALL:
                return new Color(30, 30, 240);
            default:
                return new Color(30, 30, 30);
        }
    }

	@Override
	public String toString()
	{
		String thrallString;
		switch(id)
		{
			case MAGE_THRALL:
				thrallString = "Mage Thrall (";
				break;
			case MELEE_THRALL:
				thrallString = "Melee Thrall (";
				break;
			case RANGE_THRALL:
				thrallString = "Range Thrall (";
				break;
			default:
				thrallString = "Unknown Thrall (";
				break;

		}
		thrallString += owner;
		thrallString += ", " + spawnTick + "->" + (spawnTick+duration) + ")";
		return thrallString;
	}
}
