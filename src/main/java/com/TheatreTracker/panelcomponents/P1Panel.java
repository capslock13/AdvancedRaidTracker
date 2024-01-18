package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.utility.DataPoint;
import com.TheatreTracker.utility.PlayerDidAttack;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GraphicChanged;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
@Slf4j
public class P1Panel extends JPanel
{
    private RoomData data;
    private BufferedImage img;

    int startX;
    int startY;
    int endX;
    int endY;
    public P1Panel(RoomData data)
    {
        this.data = data;
        int length = data.getValue(DataPoint.VERZIK_P1_SPLIT);
        if(data.attacksP1.size() != 0 && length < 150)
        {
            img = new BufferedImage(120+(length*20), 140, BufferedImage.TYPE_INT_ARGB);
            startX = 100;
            startY = 20;
            endX = 120+(length*20)-20;
            endY = 130;
            drawGraph();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, null);
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(800, 200);
    }

    private Rectangle getStringBounds(Graphics2D g2, String str,
                                      float x, float y)
    {
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, x, y);
    }
    private void drawGraph()
    {
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        Color oldColor = g.getColor();
        g.setColor(new Color(40, 40, 40));
        g.fillRect(0, 0, img.getWidth(), img.getHeight());

        g.setColor(new Color(100, 100, 100));

        g.drawLine(startX-1, startY-1, endX+1, startY-2);
        g.drawLine(startX-1, startY-1, startX-1, endY+1);
        g.drawLine(startX-1, endY+1, endX+1, endY+1);
        g.drawLine(endX+1, endY+1, endX+1, startY-1);

        g.setColor(Color.WHITE);
        int offsetY = startY;
        Map<String, Integer> playerOffsets = new LinkedHashMap<>();
        Font f = g.getFont();
        int fontHeight = getStringBounds((Graphics2D)g, "a", 0, 0).height;
        g.setColor(Color.DARK_GRAY);
        g.drawLine(startX, offsetY, endX, offsetY);
        for(String name : data.players.keySet())
        {
            g.setColor(Color.DARK_GRAY);
            g.drawLine(startX, offsetY+20, endX, offsetY+20);
            g.setColor(Color.white);
            g.drawString(name, 10, offsetY+fontHeight+3);
            playerOffsets.put(name, offsetY);
            offsetY += 20;
        }
        g.drawString("Dawn Appears", 10, startY+fontHeight+103);
        int tick = 1;
        for(int i = startX+20; i < endX; i+= 20)
        {
            g.setColor(new Color(255, 80, 80));
            if((tick-18)%14==0 && tick > 10)
            {
                g.fillRect(i, startY-20, 20, 80);
            }
            g.setColor(Color.DARK_GRAY);
            g.drawLine(i, startY-20, i, endY);
            g.setColor(Color.white);
            g.drawString(String.valueOf(tick), i-14, startY-5);
            tick++;
        }

        for(PlayerDidAttack attack : data.attacksP1)
        {
            log.info(attack.player + " attacked with " + attack.weapon + " with animation " + attack.animation + " on tick " + attack.tick);
            String letter = "";
            if(attack.animation == 8056)
            {
                g.setColor(new Color(106, 168, 79));
                letter = "S";
            }
            else if(attack.animation == 1167 && attack.weapon.equalsIgnoreCase("22516"))
            {
                g.setColor(new Color(180, 198, 231, 180));
                letter = "D";
            }
            else if(attack.animation == 1167)//sang: 188, 138, 186
            {
                g.setColor(new Color(188, 138, 186));
                letter = "T";
            }
            else
            {
                continue;
            }
            int x = startX + ((attack.tick-1)*20);
            int y = playerOffsets.get(attack.player);

            g.fillRect(x, y, 20, 20);
            g.setColor(Color.white);
            g.drawString(letter, x+6, y+15);
        }

        for(Integer i : data.dawnDrops)
        {
            g.drawString("X", 6+startX+((i)*20), startY+100+fontHeight+3);
        }

        g.setColor(oldColor);
        g.dispose();
        repaint();
    }
}
