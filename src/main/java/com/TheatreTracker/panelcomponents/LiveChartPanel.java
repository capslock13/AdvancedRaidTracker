package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.utility.*;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.*;

@Slf4j
public class LiveChartPanel extends JPanel
{
    private boolean shouldWrap = true;
    private BufferedImage img;
    int scale = 20;
    int boxCount;
    int boxHeight;
    int boxWidth;
    private ArrayList<PlayerDidAttack> attacks;
    int startTick;
    public int currentTick;
    ArrayList<String> players;
    String room;
    ArrayList<Integer> specific;
    Map<Integer, String> lines;
    WeaponAttack[] weaponAttacks;
    private boolean finished;
    int keyColumns;
    int keyRows;
    int keyCount;
    int keyMargin;

    public LiveChartPanel(String room)
    {
        this.specific = new ArrayList<>();
        this.lines = new HashMap<>();
        this.players = new ArrayList<>();
        this.room = room;
        startTick = 0;
        currentTick = 0;
        attacks = new ArrayList<>();
        finished = false;

        recalculateSize();
        drawGraph();

    }
    private ArrayList<ThrallOutlineBox> thrallOutlineBoxes = new ArrayList<>();

    public void addThrallOutlineBox(ThrallOutlineBox outlineBox)
    {
        thrallOutlineBoxes.add(outlineBox);
    }

    public void addDelayedLine(Integer value, String description)
    {
        lines.put(value, description);
    }

    public void setRoomFinished()
    {
        finished = true;
        drawGraph();
    }

    public void setPlayers(ArrayList<String> players)
    {
        this.players = players;
    }

    private void recalculateSize()
    {
        int length = currentTick-startTick;
        boxCount = (length / 50);
        if (boxCount % 50 != 0)
        {
            boxCount++;
        }
        if(boxCount < 1)
        {
            boxCount = 1;
        }
        boxHeight = (players.size() + 3) * 20;
        int height = boxCount * boxHeight;
        if(height < 600)
        {
            height = 600;
        }
        boxWidth = 1120;
        this.weaponAttacks = WeaponAttack.values();
        keyCount = weaponAttacks.length;
        keyRows = 9;
        keyMargin = 10;
        keyColumns = keyCount/keyRows;
        if(keyCount%keyRows != 0)
        {
            keyColumns++;
        }
        int width = boxWidth + (keyColumns*150)+40;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        drawGraph();
    }

    public Rectangle getViewRect()
    {
        return new Rectangle(0, (boxCount > 0) ? (boxCount-1)*boxHeight : 0, boxWidth, boxHeight);
    }

    public void addAttack(PlayerDidAttack attack)
    {
        attack.tick += currentTick;
        attacks.add(new PlayerDidAttack(attack.player, attack.animation, attack.tick, attack.weapon, attack.projectile, attack.spotAnims, attack.targetedIndex, attack.targetedID));
        drawGraph();
    }

    public void incrementTick()
    {
        currentTick++;
        if(currentTick % 50 == 0 || currentTick == 1)
        {
            recalculateSize();
        }
        else
        {
            drawGraph();
        }
    }

    public void resetGraph()
    {
        currentTick = 0;
        attacks.clear();
        players.clear();
        lines.clear();
        finished = false;
        thrallOutlineBoxes.clear();
        recalculateSize();
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

        int fontHeight = getStringBounds(g, "a", 0, 0).height;

        g.setColor(Color.WHITE);
        g.drawRect(boxWidth+(keyMargin/2), keyMargin, (keyColumns*150)-10, (keyRows*30));

        int currentColumn = 0;
        int currentRow = 0;
        for(int i = 0; i < keyCount; i++)
        {
            WeaponAttack attack = weaponAttacks[i];
            g.setColor(attack.color);
            g.fillRect(boxWidth+keyMargin+(currentColumn*150)+2, keyMargin+(currentRow*30)+7, scale, scale);
            g.setColor(Color.WHITE);
            g.drawString(attack.shorthand, boxWidth+keyMargin+(currentColumn*150)+3, keyMargin+(currentRow*30)+22);
            g.drawString(attack.name, boxWidth+keyMargin+(currentColumn*150)+33, keyMargin+(currentRow*30)+22);
            currentRow++;
            if(currentRow +1 > keyRows)
            {
                currentColumn++;
                currentRow = 0;
            }
        }

        for(int i = startTick; i < currentTick; i++)
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
            g.setColor(Color.DARK_GRAY);
            g.drawLine(100+xOffset+scale, yOffset-(fontHeight/2), 100+xOffset+scale, yOffset+boxHeight-(2*scale)+10);
            g.setColor(new Color(220, 220, 220));
            Font oldFont = g.getFont();
            g.setFont(oldFont.deriveFont(10.0f));
            g.drawString(String.valueOf(i), 100+xOffset+3, yOffset+(fontHeight/2));
            g.setFont(oldFont);
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
                        case "Verzik P2":
                            g.drawString("Healing end: ", 10, j*boxHeight+((players.size()+2)*scale)+(fontHeight/2));
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
        if(playerOffsets.size() != 0)
        {
            for (PlayerDidAttack attack : attacks)
            {
                if (attack.tick >= startTick && attack.tick <= currentTick)
                {
                    WeaponAttack weaponAttack = WeaponDecider.getWeapon(attack.animation, attack.spotAnims, attack.projectile, attack.weapon);
                    if (weaponAttack != WeaponAttack.UNDECIDED)
                    {
                        String letter = weaponAttack.shorthand;
                        Color color = weaponAttack.color;

                        int xOffset = (shouldWrap) ? ((attack.tick - startTick) % 50) * scale : attack.tick * scale;
                        int yOffset = (shouldWrap) ? ((attack.tick - startTick) / 50) * boxHeight : 0;
                        xOffset += 100;
                        try
                        {
                            yOffset += (playerOffsets.get(attack.player) + 2) * scale - 10;
                        }
                        catch(Exception e)
                        {
                            for(String s : playerOffsets.keySet())
                            {
                            }
                            return;
                        }
                        g.setColor(color);
                        g.fillRect(xOffset+1, yOffset+1, scale - 1, scale - 1);
                        g.setColor(Color.WHITE);
                        if(!RoomUtil.isPrimaryBoss(attack.targetedID))
                        {
                            if(attack.targetedID != -1)
                            {
                                g.setColor(new Color(0, 190, 255));
                            }
                        }
                        int textOffset = (scale/2)-(getStringBounds(g, letter, 0, 0).width)/2;
                        g.drawString(letter, xOffset+textOffset, yOffset + (fontHeight / 2) + 10);
                    }
                }
            }
        }
        if(finished)
        {
            for (Integer i : lines.keySet())
            {
                int xOffset = (shouldWrap) ? ((i - startTick) % 50) * scale : i * scale;
                int yOffset = (shouldWrap) ? ((i - startTick) / 50) * boxHeight : 0;
                xOffset += 100;
                yOffset += 10;
                g.setColor(new Color(255, 0, 0));
                g.drawLine(xOffset, yOffset, xOffset, yOffset + boxHeight - 20);
                int stringLength = getStringBounds(g, lines.get(i), 0, 0).width;
                g.setColor(Color.WHITE);
                g.drawString(lines.get(i), xOffset - (stringLength / 2), yOffset - 1);
            }
        }

        for(ThrallOutlineBox box : thrallOutlineBoxes)
        {
            int xOffset = (shouldWrap) ? ((box.spawnTick - startTick) % 50) * scale : box.spawnTick * scale;
            int yOffset = (shouldWrap) ? ((box.spawnTick - startTick) / 50) * boxHeight : 0;
            xOffset += 100;
            try
            {
                yOffset += (playerOffsets.get(box.owner) + 2) * scale - 10;
            }
            catch(Exception e)
            {
                for(String s : playerOffsets.keySet())
                {
                }
                return;
            }
            g.setColor(box.getColor());

            int maxTick = box.spawnTick+99;
            for(ThrallOutlineBox boxCompare : thrallOutlineBoxes)
            {
                if(box.owner.equalsIgnoreCase(boxCompare.owner))
                {
                    if(boxCompare.spawnTick > box.spawnTick && boxCompare.spawnTick < (box.spawnTick+99))
                    {
                        maxTick = boxCompare.spawnTick;
                    }
                }
            }
            if(currentTick < maxTick)
            {
                maxTick = currentTick;
            }
            int lastEndTick = box.spawnTick;
            while(lastEndTick < maxTick)
            {
                int currentEndTick = lastEndTick + (50-(lastEndTick%50));
                int xOffsetStart = (shouldWrap) ? ((lastEndTick - startTick) % 50) * scale : (lastEndTick-1) * scale;
                xOffsetStart += 100;
                int xOffsetEnd = (shouldWrap) ? ((currentEndTick - startTick-1) % 50) * scale : (currentEndTick-1) * scale;
                xOffsetEnd += 100;
                lastEndTick = currentEndTick;
                g.drawRect(xOffsetStart, yOffset+1, xOffsetEnd-xOffsetStart, scale-2);
            }
           // g.drawRect(xOffset, yOffset+1, scale, scale-2);
        }

        g.setColor(oldColor);
        g.dispose();
        repaint();
    }
}
