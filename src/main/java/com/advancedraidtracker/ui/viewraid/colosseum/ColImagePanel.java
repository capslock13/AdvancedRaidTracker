package com.advancedraidtracker.ui.viewraid.colosseum;

import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import net.runelite.client.util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ColImagePanel extends JPanel
{
    BufferedImage img = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
    public ColImagePanel()
    {
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(img.getWidth(), img.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (img != null)
        {
            g.drawImage(img, 0, 0, null);
        }
    }
}
