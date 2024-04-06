package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.utility.ItemReference;
import com.advancedraidtracker.utility.Point;
import net.runelite.client.ui.FontManager;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.ArrayList;

import static com.advancedraidtracker.utility.ItemReference.*;

public class HoverBox
{
    ArrayList<String> info;
    Point location = new Point(0, 0);
    private final AdvancedRaidTrackerConfig config;

    public HoverBox(String s, AdvancedRaidTrackerConfig config)
    {
        this.config = config;
        info = new ArrayList<>();
        addString(s);
    }

    public void addString(String s)
    {
        info.add(s);
        if (s.toLowerCase().startsWith(".weapon"))
        {
            setStyle(s);
        }
    }

    public void setPosition(int x, int y)
    {
        location = new Point(x + config.chartScaleSize(), y);
    }

    int style = NONE;

    private void setStyle(String weapon)
    {
        if (anyMatch(weapon, ItemReference.ITEMS[MELEE]))
        {
            style = MELEE;
        } else if (anyMatch(weapon, ItemReference.ITEMS[RANGE]))
        {
            style = RANGE;
        } else if (anyMatch(weapon, ItemReference.ITEMS[MAGE]))
        {
            style = MAGE;
        }
    }

    private boolean anyMatch(String item, String[] items)
    {
        for (String s : items)
        {
            if (item.toLowerCase().contains(s))
            {
                return true;
            }
        }
        return false;
    }

    public int getWidth(Graphics2D g)
    {
        Font oldFont = g.getFont();
        g.setFont(FontManager.getRunescapeBoldFont());
        int longestString = 0;
        for (String s : info)
        {
            int stringLength = getStringWidth(g, s);
            if (stringLength > longestString)
            {
                longestString = stringLength;
            }
        }
        g.setFont(oldFont);
        return longestString + 10;
    }

    public int getHeight(Graphics2D g)
    {
        Font oldFont = g.getFont();
        g.setFont(FontManager.getRunescapeBoldFont());
        int fontHeight = getStringBounds(g).height;
        g.setFont(oldFont);
        return 10 + ((fontHeight + 7) * info.size());
    }

    public void draw(Graphics2D g)
    {
        Font oldFont = g.getFont();
        g.setFont(FontManager.getRunescapeBoldFont());
        int fontHeight = getStringBounds(g).height;
        g.setColor(config.primaryDark());
        int boxHeight = 10 + (fontHeight + 7) * info.size();
        g.fillRoundRect(location.getX(), location.getY(), getWidth(g), boxHeight, 10, 10);
        g.setColor(config.fontColor());
        g.drawRoundRect(location.getX(), location.getY(), getWidth(g), boxHeight, 10, 10);

        for (int i = 0; i < info.size(); i++)
        {
            String label = info.get(i);
            if (label.startsWith(".") && label.length() > 1)
            {
                label = label.substring(1);
                if (anyMatch(label, ItemReference.ITEMS[style]))
                {
                    g.setColor(new Color(60, 190, 60));
                } else if (anyMatch(label, ItemReference.ITEMS[0]))
                {
                    g.setColor(new Color(120, 120, 120));
                } else
                {
                    g.setColor(new Color(190, 30, 30));
                }
            }
            g.drawString(label, location.getX() + 5, location.getY() + 5 + (fontHeight + 7) * (i + 1));
        }
        g.setFont(oldFont);
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
