package com.TheatreTracker.utility;

import com.TheatreTracker.Point;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.ArrayList;

public class HoverBox
{
    ArrayList<String> info;
    Point location = new Point(0,0);

    public HoverBox(String s)
    {
        info = new ArrayList<>();
        info.add(s);
    }

    public void addString(String s)
    {
        info.add(s);
    }

    public void setPosition(int x, int y)
    {
        location = new Point(x, y);
    }

    public void draw(Graphics2D g)
    {
        int longestString = 0;
        for(String s : info)
        {
            int stringLength = getStringWidth(g, s);
            if (stringLength > longestString)
            {
                longestString = stringLength;
            }
        }
        int fontHeight = getStringBounds(g).height;
        g.setColor(new Color(0, 0, 0, 220));
        int boxHeight = 10 + (fontHeight+5)*info.size();
        g.fillRect(location.getX(), location.getY(), longestString + 10, boxHeight);
        g.setColor(Color.WHITE);
        g.drawRect(location.getX(), location.getY(), longestString + 10, boxHeight);
        for(int i = 0; i < info.size(); i++)
        {
            g.drawString(info.get(i), location.getX()+5, location.getY() + 5 + (fontHeight+5)*(i+1));
        }
    }

    private int getStringWidth(Graphics2D g, String str)
    {
        FontRenderContext frc = g.getFontRenderContext();
        GlyphVector gv = g.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, 0, 0).width;
    }

    private Rectangle getStringBounds(Graphics2D g2)
    {
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, "a");
        return gv.getPixelBounds(null, (float) 0, (float) 0);
    }
}
