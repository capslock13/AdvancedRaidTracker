package com.TheatreTracker.ui.charts;

import com.TheatreTracker.utility.ItemReference;
import com.TheatreTracker.utility.Point;

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
        addString(s);
    }

    String[] styles = {"None", "Melee", "Range", "Mage"};

    public void addString(String s)
    {
        info.add(s);
        if(s.toLowerCase().startsWith(".weapon"))
        {
            setStyle(s);
            addString("Style: " + styles[style]);
        }
    }

    public void setPosition(int x, int y)
    {
        location = new Point(x+20, y);
    }

    private final int MELEE = 1;
    private final int RANGE = 2;
    private final int MAGE = 3;
    private final int NONE = 0;
    int style = NONE;

    private void setStyle(String weapon)
    {
        if(anyMatch(weapon, ItemReference.ITEMS[MELEE]))
        {
            style = MELEE;
        }
        else if(anyMatch(weapon, ItemReference.ITEMS[RANGE]))
        {
            style = RANGE;
        }
        else if(anyMatch(weapon, ItemReference.ITEMS[MAGE]))
        {
            style = MAGE;
        }
    }
    private boolean anyMatch(String item, String[] items)
    {
        for(String s : items)
        {
            if(item.toLowerCase().contains(s))
            {
                return true;
            }
        }
        return false;
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
            String label = info.get(i);
            if(label.startsWith(".") && label.length() > 1)
            {
                label = label.substring(1);
                if(anyMatch(label, ItemReference.ITEMS[style]))
                {
                    g.setColor(Color.GREEN);
                }
                else if(anyMatch(label, ItemReference.ITEMS[0]))
                {
                    g.setColor(Color.CYAN);
                }
                else
                {
                    g.setColor(Color.RED);
                }
            }
            g.drawString(label, location.getX()+5, location.getY() + 5 + (fontHeight+5)*(i+1));
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
