package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.Point;
import com.TheatreTracker.RoomData;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SummarizeRaidsPanel extends JPanel
{
    private ArrayList<RoomData> data;
    private BufferedImage img;
    int width = 600;
    int height = 400;

    Color primaryDark = new Color(23, 23, 23);
    Color primaryLight = new Color(30, 30, 30);

    public SummarizeRaidsPanel(ArrayList<RoomData> data)
    {
        this.data = data;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        drawPanel();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if(img != null)
        {
            g.drawImage(img, 0, 0, null);
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(img.getWidth(), img.getHeight());
    }

    public void drawPanel()
    {
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setColor(primaryLight);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.WHITE);

        g.setFont(FontManager.getDefaultBoldFont());
        g.drawString("Summary", 20, 20);

        g.setColor(primaryDark);

        g.fillRoundRect(20, 40, 400, 40, 15, 15);

    }
}
