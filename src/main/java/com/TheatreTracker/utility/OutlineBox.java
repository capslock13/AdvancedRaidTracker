package com.TheatreTracker.utility;

import java.awt.*;

public class OutlineBox
{
    public String player;
    public int tick;
    public String letter;
    public Color color;
    public boolean primaryTarget;

    public String additionalText = "";

    public OutlineBox(String player, int tick, String letter, Color color, boolean primaryTarget, String additionalText)
    {
        this.player = player;
        this.tick = tick;
        this.letter = letter;
        this.color = color;
        this.primaryTarget = primaryTarget;
        this.additionalText = additionalText;
    }
}
