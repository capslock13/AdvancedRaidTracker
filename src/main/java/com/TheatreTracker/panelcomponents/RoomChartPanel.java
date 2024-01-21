package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.rooms.RoomHandler;
import com.TheatreTracker.utility.PlayerDidAttack;
import com.TheatreTracker.utility.WeaponAttack;
import com.TheatreTracker.utility.WeaponDecider;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.raids.RoomType;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
@Slf4j
public class RoomChartPanel extends JPanel
{
    private boolean shouldWrap;
    private BufferedImage img;
    int scale = 20;
    int boxCount;
    int boxHeight;
    int boxWidth;
    private ArrayList<PlayerDidAttack> attacks;
    int startTick;
    int endTick;
    ArrayList<String> players;
    String room;
    ArrayList<Integer> specific;
    ArrayList<Integer> lines;

    public RoomChartPanel(ArrayList<PlayerDidAttack> attacks, Set<String> players, String room, int size, int start, int end, ArrayList<Integer> specificData, ArrayList<Integer> lines)
    {
        this.specific = specificData;
        this.lines = lines;
        if(size != 0 && players.size() != 0 && end > 0)
        {
            this.room = room;
            startTick = start;
            endTick = end;
            this.attacks = attacks;
            this.players = new ArrayList<>(players);
            int length = end-start;
            shouldWrap = (size == 1);
            boxCount = (length / 50);
            if (boxCount % 50 != 0) {
                boxCount++;
            }
            if (!shouldWrap)
            {
                boxCount = 1;
            }
            boxHeight = (players.size() + 3) * 20;
            boxWidth = (shouldWrap) ? 1120 : 100 + (length + 1) * scale;
            int height = boxCount * boxHeight;
            img = new BufferedImage(boxWidth, height, BufferedImage.TYPE_INT_ARGB);
            drawGraph();
        }
        else
        {
            img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        }
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

        Font f = g.getFont();
        int fontHeight = getStringBounds(g, "a", 0, 0).height;

        for(int i = startTick; i < endTick; i++)
        {
            int xOffset = (shouldWrap) ? ((i-startTick)%50)*scale : i*scale;
            int yOffset = (shouldWrap) ? ((i-startTick)/50)*boxHeight : 0;
            yOffset += scale;

            switch(room)
            {
                case "Verzik P1":
                    g.setColor(new Color(255, 80, 80, 100));
                    if((i-19)%14==0 && i > 10)
                    {
                        g.fillRect(xOffset+100, yOffset-scale+10, scale, boxHeight-20);
                    }
                    break;
            }
            //g.setColor(new Color(255, 80, 80)); AUTO
            g.setColor(Color.DARK_GRAY);
            g.drawLine(100+xOffset+scale, yOffset-(fontHeight/2), 100+xOffset+scale, yOffset+boxHeight-(2*scale)+10);
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(i), 100+xOffset, yOffset+(fontHeight/2));
        }

        for(int i = 0; i < boxCount; i++)
        {
            int startX = 100;
            int startY = boxHeight*i+10;
            int endX = boxWidth-scale;
            int endY = startY+boxHeight;
            g.setColor(new Color(100, 100,100));

            g.drawLine(startX, startY+scale, endX, startY+scale);
            g.drawLine(startX, startY+scale, startX, endY-scale);
            g.drawLine(startX,endY-scale,endX,endY-scale);
            g.drawLine(endX,endY-scale,endX,startY+scale);
        }

        g.setColor(Color.WHITE);
        Map<String, Integer> playerOffsets = new LinkedHashMap<>();
        for(int i = 0; i < players.size(); i++)
        {
            playerOffsets.put(players.get(i), i);
            for(int j = 0; j < boxCount; j++)
            {
                g.setColor(Color.DARK_GRAY);
                g.drawLine(100, 10+(j*boxHeight)+((i+2)*scale), boxWidth-20, 10+(j*boxHeight)+((i+2)*scale));
                g.setColor(Color.WHITE);
                g.drawString(players.get(i), 10, (j*boxHeight)+((i+2)*scale)+(fontHeight)/2);
                if(i == 0)
                {
                    switch(room)
                    {
                        case "Verzik P1":
                            g.drawString("Dawn Appear: ", 10, j*boxHeight+((players.size()+2)*scale)+(fontHeight/2));
                            break;
                    }
                }
            }
        }
        for(Integer i : specific)
        {
            int xOffset = (shouldWrap) ? ((i - startTick) % 50) * scale : i * scale;
            int yOffset = (shouldWrap) ? ((i - startTick) / 50) * boxHeight : 0;
            xOffset += 100;
            yOffset += (playerOffsets.size() + 2) * scale - 10;
            g.setColor(Color.WHITE);
            g.drawString("X", xOffset, yOffset + (fontHeight / 2) + 10);
        }


        for(PlayerDidAttack attack : attacks)
        {
            if(attack.tick >= startTick && attack.tick <= endTick)
            {
                WeaponAttack weaponAttack = WeaponDecider.getWeapon(attack.animation, attack.spotAnims, attack.projectile, attack.weapon);
                if (weaponAttack != WeaponAttack.UNDECIDED)
                {
                    String letter = weaponAttack.shorthand;
                    Color color = weaponAttack.color;

                    int xOffset = (shouldWrap) ? ((attack.tick - startTick) % 50) * scale : attack.tick * scale;
                    int yOffset = (shouldWrap) ? ((attack.tick - startTick) / 50) * boxHeight : 0;
                    xOffset += 100;
                    yOffset += (playerOffsets.get(attack.player) + 2) * scale - 10;
                    g.setColor(color);
                    g.fillRect(xOffset, yOffset, scale - 1, scale - 1);
                    g.setColor(Color.WHITE);
                    g.drawString(letter, xOffset, yOffset + (fontHeight / 2) + 10);
                }
            }
        }

        for(Integer i : lines)
        {
            int xOffset = (shouldWrap) ? ((i - startTick) % 50) * scale : i * scale;
            int yOffset = (shouldWrap) ? ((i- startTick) / 50) * boxHeight : 0;
            xOffset += 100;
            yOffset += 10;
            g.setColor(new Color(255, 0, 0));
            g.drawLine(xOffset, yOffset, xOffset, yOffset+boxHeight-20);
        }

        g.setColor(oldColor);
        g.dispose();
        repaint();
    }
}
