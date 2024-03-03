package com.TheatreTracker.ui;


import com.TheatreTracker.utility.BloatHand;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class BloatHandDataVisualizer extends JPanel
{

    private BufferedImage img;
    private ArrayList<BloatHand> data;
    public BloatHandDataVisualizer(ArrayList<BloatHand> data)
    {
        this.data = data;
        img = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        drawGraph();
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

    private void drawGraph()
    {
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        Color oldColor = g.getColor();

        g.setColor(new Color(40, 40, 40));
        g.fillRect(0, 0, img.getWidth(), img.getHeight());

        g.setColor(Color.WHITE);

        Map<String, Integer> handMap = new HashMap<>();
        for(BloatHand bloatHand : data)
        {
            String position = bloatHand.x + "," + bloatHand.y;
            if(!handMap.containsKey(position))
            {
                handMap.put(position, 1);
            }
            else
            {
                handMap.put(position, handMap.get(position)+1);
            }
        }
        double highestValue = 0;
        for(String s : handMap.keySet())
        {
            if(handMap.get(s) > highestValue)
            {
                highestValue = handMap.get(s);
            }
        }

        for(BloatHand hand : data)
        {
            int x = hand.x-20;
            int y = hand.y-20;
            int value = handMap.get(hand.x+","+hand.y);
            g.drawRect(x*26, y*26, 25, 25);
            int opacity = (int)((value/highestValue)*255);
            g.setColor(new Color(255, 255, 255, opacity));
            g.setFont(FontManager.getRunescapeFont());
            g.drawString(String.valueOf(value), (x*26)+3, (y*26)+13);
        }

        g.setColor(oldColor);
        g.dispose();
        repaint();
    }

}
