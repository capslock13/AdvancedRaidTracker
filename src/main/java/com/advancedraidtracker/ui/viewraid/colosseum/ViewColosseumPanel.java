package com.advancedraidtracker.ui.viewraid.colosseum;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.utility.datautility.datapoints.col.Colo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ViewColosseumPanel extends JPanel
{
    private int width = 800;
    private int height = 600;
    private BufferedImage img;
    private AdvancedRaidTrackerConfig config;
    private Colo data;

    public ViewColosseumPanel(Colo colData, AdvancedRaidTrackerConfig config)
    {
        this.config = config;
        this.data = colData;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        draw();
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

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(img.getWidth(), img.getHeight());
    }

    public void draw()
    {
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(qualityHints);

        //draw background
        g.setColor(config.primaryDark());
        g.fillRect(0, 0, width, height);
    }

}
