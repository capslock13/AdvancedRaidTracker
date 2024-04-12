package com.advancedraidtracker.ui.customrenderers;

import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import java.awt.*;

public class IconManager
{
    static ImageIcon outline;
    static ImageIcon filled;
    public static ImageIcon getHeartFilled()
    {
        if(filled == null)
        {
            filled = new ImageIcon(ImageUtil.loadImageResource(AdvancedRaidTrackerPlugin.class, "/com/advancedraidtracker/heartfilled.png").getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        }
        return filled;
    }

    public static ImageIcon getHeartOutline()
    {
        if(outline == null)
        {
            outline = new ImageIcon(ImageUtil.loadImageResource(AdvancedRaidTrackerPlugin.class, "/com/advancedraidtracker/heartoutline.png").getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        }
        return outline;
    }
}
