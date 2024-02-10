package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.Point;
import com.TheatreTracker.RoomData;

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
    }
}
