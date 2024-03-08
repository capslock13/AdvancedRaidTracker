package com.TheatreTracker.ui.charts;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.constants.TobIDs;
import com.TheatreTracker.utility.*;
import com.TheatreTracker.utility.Point;
import com.TheatreTracker.utility.weapons.WeaponAttack;
import com.TheatreTracker.utility.weapons.WeaponDecider;
import com.TheatreTracker.utility.wrappers.*;
import com.google.inject.Inject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.IconID;
import net.runelite.api.SpriteID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class ChartPanel extends JPanel implements MouseListener, MouseMotionListener
{
    private boolean shouldWrap;
    private BufferedImage img;
    int scale;
    int boxCount;
    int boxHeight;
    int boxWidth;

    int selectedTick = -1;
    String selectedPlayer = "";

    int selectedRow = -1;
    boolean checkBoxHovered = false;

    int startTick;
    public int endTick;
    @Setter
    ArrayList<String> players = new ArrayList<>();
    String room;
    WeaponAttack[] weaponAttacks;
    int keyColumns;
    int keyRows;
    int keyCount;
    int keyMargin;
    @Setter
    String roomSpecificText = "";
    private int fontHeight;
    public boolean finished = false;
    private final boolean live;
    private final ArrayList<Integer> autos = new ArrayList<>();
    private Map<Integer, String> NPCMap = new HashMap<>();
    private final ArrayList<DawnSpec> dawnSpecs = new ArrayList<>();
    private final ArrayList<ThrallOutlineBox> thrallOutlineBoxes = new ArrayList<>();
    private final ArrayList<OutlineBox> outlineBoxes = new ArrayList<>();
    private final Map<Integer, String> specific = new HashMap<>();
    private final Map<Integer, String> lines = new HashMap<>();
    private final ArrayList<String> crabDescriptions = new ArrayList<>();

    @Setter
    private Map<Integer, Integer> roomHP = new HashMap<>();
    Map<String, Integer> playerOffsets = new LinkedHashMap<>();
    private final Map<PlayerDidAttack, String> actions = new HashMap<>();

    private ConfigManager configManager;
    private ItemManager itemManager;

    public void enableWrap()
    {
        shouldWrap = true;
        recalculateSize();
    }

    private final TheatreTrackerConfig config;

    public void addRoomSpecificData(int tick, String data)
    {
        specific.put(tick, data);
    }

    public void addRoomSpecificDatum(Map<Integer, String> specificData)
    {
        specific.putAll(specificData);
    }

    public void addLine(int tick, String lineInfo)
    {
        lines.put(tick, lineInfo);
    }

    public void addLines(Map<Integer, String> lineData)
    {
        lines.putAll(lineData);
    }

    public void addRoomHP(int tick, int hp)
    {
        roomHP.put(tick, hp);
    }

    public void addAuto(int autoTick)
    {
        autos.add(autoTick);
    }

    public void addAutos(ArrayList<Integer> autos)
    {
        this.autos.addAll(autos);
    }

    public void addThrallBox(ThrallOutlineBox thrallOutlineBox)
    {
        thrallOutlineBoxes.add(thrallOutlineBox);
    }

    public void addThrallBoxes(ArrayList<ThrallOutlineBox> outlineBoxes)
    {
        thrallOutlineBoxes.addAll(outlineBoxes);
    }

    public void addDawnSpec(DawnSpec dawnSpec)
    {
        this.dawnSpecs.add(dawnSpec);
    }

    public void addDawnSpecs(ArrayList<DawnSpec> dawnSpecs)
    {
        this.dawnSpecs.addAll(dawnSpecs);
        drawGraph();
    }

    public void setRoomFinished(int tick)
    {
        finished = true;
        if (tick - endTick < 10)
        {
            endTick = tick;
        }
        drawGraph();
    }

    public void resetGraph()
    {
        endTick = 0;
        startTick = 0;
        selectedRow = -1;
        selectedTick = -1;
        selectedPlayer = "";
        outlineBoxes.clear();
        autos.clear();
        lines.clear();
        specific.clear();
        dawnSpecs.clear();
        thrallOutlineBoxes.clear();
        players.clear();
        playerOffsets.clear();
        crabDescriptions.clear();
        actions.clear();
        roomHP.clear();
        NPCMap.clear();
        finished = false;
        recalculateSize();
    }

    public void addMaidenCrabs(ArrayList<String> crabDescriptions)
    {
        this.crabDescriptions.addAll(crabDescriptions);
    }

    public void addMaidenCrab(String description)
    {
        crabDescriptions.add(description);
    }

    public void addNPCMapping(int index, String name)
    {
        NPCMap.put(index, name);
    }

    public void setNPCMappings(Map<Integer, String> mapping)
    {
        this.NPCMap = mapping;
    }

    public void redraw()
    {
        recalculateSize();
    }

    public void addAttack(PlayerDidAttack attack)
    {
        if(clientThread != null)
        {
            if(config.useUnkitted())
            {
                attack.useUnkitted();
            }
            clientThread.invoke(() -> {
                attack.setIcons(itemManager);
            });
            clientThread.invoke(attack::setWornNames);
        }
        WeaponAttack weaponAttack = WeaponDecider.getWeapon(attack.animation, attack.spotAnims, attack.projectile, attack.weapon);
        if (weaponAttack != WeaponAttack.UNDECIDED)
        {
            boolean isTarget = RoomUtil.isPrimaryBoss(attack.targetedID) && attack.targetedID != -1;
            String targetString = weaponAttack.name + ": ";
            String targetName = getBossName(attack.targetedID, attack.targetedIndex, attack.tick);
            if (targetName.equals("?"))
            {
                targetString += attack.targetName;
            } else
            {
                targetString += targetName;
            }
            actions.put(attack, targetString);
            String additionalText = "";
            if (targetString.contains("(on w"))
            {
                additionalText = targetString.substring(targetString.indexOf("(on w") + 5);
                additionalText = "s" + additionalText.substring(0, additionalText.indexOf(")"));
            } else if (targetString.contains("small") || targetString.contains("big"))
            {
                additionalText = getShortenedString(targetString, weaponAttack.name.length());
            } else if (targetString.contains("70s") || targetString.contains("50s") || targetString.contains("30s"))
            {
                String shortenedString = targetString.substring(weaponAttack.name.length()+2);
                shortenedString = shortenedString.substring(0, 2);
                String proc = targetString.substring(targetString.indexOf("0s") - 1, targetString.indexOf("0s") + 1);

                additionalText = proc + shortenedString;
            }
            outlineBoxes.add(new OutlineBox(attack, weaponAttack.shorthand, weaponAttack.color, isTarget, additionalText, weaponAttack));
        }
    }

    private static String getShortenedString(String targetString, int index)
    {
        String shortenedString = targetString.substring(index+3);
        shortenedString = shortenedString.substring(0, shortenedString.indexOf(" "));
        if (targetString.contains("east small"))
        {
            shortenedString += "e";
        } else if (targetString.contains("south small"))
        {
            shortenedString += "s";
        } else if (targetString.contains("west small"))
        {
            shortenedString += "w";
        } else if (targetString.contains("east big"))
        {
            shortenedString += "E";
        } else if (targetString.contains("south big"))
        {
            shortenedString += "S";
        } else if (targetString.contains("west big"))
        {
            shortenedString += "W";
        }
        return shortenedString;
    }

    public void addLiveAttack(PlayerDidAttack attack)
    {
        attack.tick += endTick;
        addAttack(new PlayerDidAttack(attack.itemManager, attack.player, attack.animation, attack.tick, attack.weapon, attack.projectile, attack.spotAnims, attack.targetedIndex, attack.targetedID, attack.targetName, attack.wornItems));
    }

    public void addAttacks(ArrayList<PlayerDidAttack> attacks)
    {
        for (PlayerDidAttack attack : attacks)
        {
            addAttack(attack);
        }
    }

    public Rectangle getViewRect()
    {
        return new Rectangle(0, (boxCount > 0) ? (boxCount - 1) * boxHeight : 0, boxWidth, boxHeight);
    }

    public void incrementTick()
    {
        endTick++;
        if (endTick % 50 == 0 || endTick == 1)
        {
            recalculateSize();
        } else
        {
            drawGraph();
        }
    }

    public void setTick(int tick)
    {
        endTick = tick;
        recalculateSize();
    }

    public void setStartTick(int tick)
    {
        startTick = tick;
        recalculateSize();
    }

    public void recalculateSize()
    {
        try
        {
            scale = config.chartScaleSize();
            setBackground(config.primaryDark());
        } catch (Exception ignored)
        {

        }
        int length = endTick - startTick;
        boxCount = (length / 50);
        if (boxCount % 50 != 0)
        {
            boxCount++;
        }
        if (boxCount < 1)
        {
            boxCount = 1;
        }
        boxHeight = ((players.size() + 3) * scale);
        int height = boxCount * boxHeight + scale;
        if (height < 600)
        {
            height = 600;
        }
        boxWidth = (shouldWrap) ? (100 + (scale * 51)) : 100 + (length + 1) * scale;
        this.weaponAttacks = WeaponAttack.values();
        keyCount = weaponAttacks.length;
        keyRows = 20;
        keyMargin = 10;
        keyColumns = keyCount / keyRows;
        if (keyCount % keyRows != 0)
        {
            keyColumns++;
        }
        int width = boxWidth /*+ (keyColumns * 150)*/ + 10;
        if (img != null)
        {
            img.flush();
        }
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        drawGraph();

    }

    private final ClientThread clientThread;
    public ChartPanel(String room, boolean isLive, TheatreTrackerConfig config, ClientThread clientThread, ConfigManager configManager, ItemManager itemManager)
    {
        this.itemManager = itemManager;
        this.configManager = configManager;
        this.config = config;
        this.clientThread = clientThread;
        scale = 26;
        live = isLive;
        this.room = room;
        startTick = 0;
        endTick = 0;
        shouldWrap = isLive;
        recalculateSize();
        addMouseListener(this);
        addMouseMotionListener(this);
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

    private Rectangle getStringBounds(Graphics2D g2, String str)
    {
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, (float) 0, (float) 0);
    }

    private int getStringWidth(Graphics2D g, String str)
    {
        FontRenderContext frc = g.getFontRenderContext();
        GlyphVector gv = g.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, 0, 0).width;
    }

    private void drawKey(Graphics2D g)
    {
        g.drawRect(boxWidth + (keyMargin / 2), keyMargin, (keyColumns * 150) - 10, (keyRows * (scale + 10)));
        int currentColumn = 0;
        int currentRow = 0;
        for (int i = 0; i < keyCount; i++)
        {
            WeaponAttack attack = weaponAttacks[i];
            g.setColor(attack.color);
            g.fillRect(boxWidth + keyMargin + (currentColumn * 150) + 2, keyMargin + (currentRow * (scale + 10)) + 7, scale, scale);
            g.setColor(Color.WHITE);
            g.drawString(attack.shorthand, boxWidth + keyMargin + (currentColumn * 150) + 3, keyMargin + (currentRow * (scale + 10)) + scale + 2);
            g.drawString(attack.name, boxWidth + keyMargin + (currentColumn * 150) + 33, keyMargin + (currentRow * (scale + 10)) + scale + 2);
            currentRow++;
            if (currentRow + 1 > keyRows)
            {
                currentColumn++;
                currentRow = 0;
            }
        }
    }

    int getYOffset(int tick)
    {
        return (shouldWrap) ? (((tick - startTick) / 50) * boxHeight) + 20 : 10;
    }

    int getXOffset(int tick)
    {
        return (shouldWrap) ? ((tick - startTick) % 50) * scale : tick * scale;
    }

    private void drawTicks(Graphics2D g)
    {
        for (int i = startTick; i < endTick; i++)
        {
            int xOffset = getXOffset(i);
            int yOffset = getYOffset(i);
            yOffset += scale;
            g.setColor(Color.DARK_GRAY);
            //g.drawLine(100 + xOffset + scale, yOffset - (fontHeight / 2), 100 + xOffset + scale, yOffset + boxHeight - (2 * scale) + 10);
            g.setColor(new Color(220, 220, 220));
            Font oldFont = g.getFont();
            g.setFont(oldFont.deriveFont(10.0f));
            int strWidth = getStringBounds(g, String.valueOf(i)).width;
            g.drawString(String.valueOf(i), 100 + xOffset + (scale / 2) - (strWidth / 2), yOffset + (fontHeight / 2));
            g.setFont(oldFont);
        }
    }

    private void drawAutos(Graphics2D g)
    {
        for (Integer i : autos)
        {
            g.setColor(new Color(255, 80, 80, 60));
            int xOffset = getXOffset(i);
            int yOffset = getYOffset(i);
            g.fillRoundRect(xOffset + 100, yOffset + 10, scale, boxHeight - scale, 7, 7);
        }
    }

    private void drawGraphBoxes(Graphics2D g)
    {
        for (int i = 0; i < boxCount; i++)
        {
            int startX = 100;
            int startY = boxHeight * i + 30;
            int endX = boxWidth - scale;
            int endY = startY + boxHeight;
            g.setColor(new Color(100, 100, 100));

            g.drawLine(startX, startY + scale, endX, startY + scale);
            g.drawLine(startX, startY + scale, startX, endY - scale);
            g.drawLine(startX, endY - scale, endX, endY - scale);
            g.drawLine(endX, endY - scale, endX, startY + scale);
        }
    }

    public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException
    {
        int imageWidth  = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double)width/imageWidth;
        double scaleY = (double)height/imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(
                image,
                new BufferedImage(width, height, image.getType()));
    }

    private void drawYChartColumn(Graphics2D g)
    {
        g.setColor(Color.WHITE);
        for (int i = 0; i < players.size(); i++)
        {
            playerOffsets.put(players.get(i), i);
            for (int j = 0; j < boxCount; j++)
            {
                g.setColor(Color.DARK_GRAY);
                //g.drawLine(100, 30 + (j * boxHeight) + ((i + 2) * scale), boxWidth - scale, 30 + (j * boxHeight) + ((i + 2) * scale));

                g.setColor(config.primaryLight());
                g.fillRoundRect(5, ((j*boxHeight)+((i+2)*scale))+10-3, 90, scale-6, 10, 10);
                g.setColor(Color.WHITE);
                Font oldFont = g.getFont();
                g.setFont(FontManager.getRunescapeBoldFont());
                int width = getStringWidth(g, players.get(i));
                int margin = 5;
                int subBoxWidth = 90;
                int textPosition = margin + (subBoxWidth-width)/2;
                g.drawString(players.get(i), textPosition, ((j * boxHeight) + ((i + 2) * scale) + (fontHeight) / 2) + (scale/2) + 8);

                if (i == 0)
                {
                    g.drawString(roomSpecificText, 5, j * boxHeight + ((players.size() + 2) * scale) + (fontHeight / 2) + 20);
                }
                g.setFont(oldFont);
            }
        }

    }

    private void drawRoomSpecificData(Graphics2D g)
    {
        for (Integer i : specific.keySet())
        {
            int xOffset = getXOffset(i);
            int yOffset = getYOffset(i);
            xOffset += 100;
            yOffset += (playerOffsets.size() + 2) * scale - 10;
            g.setColor(Color.WHITE);
            int strWidth = getStringBounds(g, "X").width;
            g.drawString("X", xOffset + (scale / 2) - (strWidth / 2), yOffset + (fontHeight / 2) + 10);
        }
    }

    private void drawDawnSpecs(Graphics2D g)
    {
        for (DawnSpec dawnSpec : dawnSpecs)
        {
            String damage = String.valueOf(dawnSpec.getDamage());
            if (dawnSpec.getDamage() != -1)
            {
                int xOffset = (shouldWrap) ? ((dawnSpec.tick - startTick - 2) % 50) * scale : (dawnSpec.tick + 2) * scale;
                int yOffset = (shouldWrap) ? ((dawnSpec.tick - startTick - 2) / 50) * boxHeight + 20 : 20;
                xOffset += 100;
                yOffset += (playerOffsets.size() + 3) * scale - 10;
                g.setColor(Color.WHITE);
                int textOffset = (scale / 2) - (getStringBounds(g, damage).width) / 2;
                g.drawString(damage, xOffset + textOffset, yOffset + (fontHeight / 2) + 10);
            }
        }
    }

    public static BufferedImage createDropShadow(BufferedImage image)
    {
        BufferedImage shadow = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = shadow.createGraphics();
        g2.drawImage(image, 0, 0, null);

        g2.setComposite(AlphaComposite.SrcIn);
        g2.setColor(new Color(0, 0, 0, 128));
        g2.fillRect(0, 0, shadow.getWidth(), shadow.getHeight());

        g2.dispose();
        return shadow;
    }

    private void drawPrimaryBoxes(Graphics2D g)
    {
        for (OutlineBox box : outlineBoxes)
        {
            if (box.tick >= startTick && box.tick <= endTick)
            {
                int xOffset = 100 + ((shouldWrap) ? ((box.tick - startTick) % 50) * scale : box.tick * scale);
                if(playerOffsets.get(box.player) == null)
                {
                    continue;
                }
                int yOffset = ((playerOffsets.get(box.player) + 1) * scale + 30) + ((shouldWrap) ? ((box.tick - startTick) / 50) * boxHeight : 30);
                if(config != null && config.useIconsOnChart())
                {
                    try
                    {
                        if(box.weaponAttack.attackTicks != -1)
                        {
                            int opacity = config.iconBackgroundOpacity();
                            opacity = Math.min(255, opacity);
                            opacity = Math.max(0, opacity);
                            g.setColor(new Color(box.color.getRed(), box.color.getGreen(), box.color.getBlue(), opacity));
                            g.fillRoundRect(xOffset + 2, yOffset + 2, scale - 3, scale - 3, 5, 5);
                            BufferedImage scaled = getScaledImage(box.attack.img, scale-2, scale-2);
                            g.drawImage(createDropShadow(scaled), xOffset + 3, yOffset + 3, null);
                            g.drawImage(scaled, xOffset+2, yOffset+1, null);
                        }
                    }
                    catch(Exception e)
                    {

                    }
                }
                else
                {
                    int opacity = 100;
                    if(config != null)
                    {
                        opacity = config.letterBackgroundOpacity();
                        opacity = Math.min(255, opacity);
                        opacity = Math.max(0, opacity);
                    }
                    g.setColor(new Color(box.color.getRed(), box.color.getGreen(), box.color.getBlue(), opacity));
                    g.fillRoundRect(xOffset + 2, yOffset + 2, scale - 3, scale - 3, 5, 5);
                    g.setColor((box.primaryTarget) ? Color.WHITE : new Color(0, 190, 255));
                    int textOffset = (scale / 2) - (getStringWidth(g, box.letter) / 2);
                    int primaryOffset = yOffset + (box.additionalText.isEmpty() ? (fontHeight / 2) : 0);
                    g.drawString(box.letter, xOffset + textOffset-1, primaryOffset + (scale / 2)+1);
                    if (!box.additionalText.isEmpty())
                    {
                        Font f = g.getFont();
                        g.setFont(f.deriveFont(10.0f));
                        textOffset = (scale / 2) - (getStringWidth(g, box.additionalText) / 2);
                        g.setColor(Color.WHITE);
                        g.drawString(box.additionalText, xOffset + textOffset, yOffset + scale - 3);
                        g.setFont(f);
                    }
                }
                box.createOutline();
                g.setColor(box.outlineColor);
                g.drawRoundRect(xOffset+1, yOffset+1, scale-2, scale-2, 5, 5);
            }
        }
    }

    private void drawMarkerLines(Graphics2D g)
    {
        if (!live || finished)
        {
            for (Integer i : lines.keySet())
            {
                int xOffset = getXOffset(i);
                int yOffset = getYOffset(i);
                xOffset += 100;
                yOffset += (scale / 2);
                g.setColor(new Color(255, 0, 0));
                g.drawLine(xOffset, yOffset, xOffset, yOffset + boxHeight - 20);
                int stringLength = getStringBounds(g, lines.get(i)).width;
                g.setColor(Color.WHITE);
                g.drawString(lines.get(i), xOffset - (stringLength / 2), yOffset - 1);
            }
        }
    }

    private void drawThrallBoxes(Graphics2D g)
    {
        for (ThrallOutlineBox box : thrallOutlineBoxes)
        {
            g.setColor(new Color(box.getColor().getRed(), box.getColor().getGreen(), box.getColor().getBlue(), 30));

            int maxTick = getMaxTick(box);
            int lastEndTick = box.spawnTick;
            while (lastEndTick < maxTick)
            {
                int yOffset = getYOffset(lastEndTick);
                try
                {
                    yOffset += (playerOffsets.get(box.owner) + 1) * scale + 10;
                } catch (Exception e)
                {
                    break;
                }
                int currentEndTick = (shouldWrap) ? lastEndTick + (50 - (lastEndTick % 50) + (startTick % 50)) : maxTick;
                if (currentEndTick > maxTick)
                {
                    currentEndTick = maxTick;
                }
                int xOffsetStart = (shouldWrap) ? ((lastEndTick - startTick) % 50) * scale : (lastEndTick - 1) * scale;
                xOffsetStart += 100;
                int xOffsetEnd = (shouldWrap) ? ((currentEndTick - startTick - 1) % 50) * scale : (currentEndTick - 1) * scale;
                xOffsetEnd += 100;
                lastEndTick = currentEndTick;
                g.fillRect(xOffsetStart, yOffset + 1, xOffsetEnd - xOffsetStart + scale, scale - 2);
            }
        }
    }

    private int getMaxTick(ThrallOutlineBox box)
    {
        int maxTick = box.spawnTick + 99;
        for (ThrallOutlineBox boxCompare : thrallOutlineBoxes)
        {
            if (box.owner.equalsIgnoreCase(boxCompare.owner))
            {
                if (boxCompare.spawnTick > box.spawnTick && boxCompare.spawnTick < (box.spawnTick + 99))
                {
                    maxTick = boxCompare.spawnTick;
                }
            }
        }
        if (endTick < maxTick)
        {
            maxTick = endTick;
        }
        return maxTick;
    }

    private void drawSelectedOutlineBox(Graphics2D g)
    {
        if (selectedTick != -1 && !selectedPlayer.equalsIgnoreCase(""))
        {
            g.setColor(new Color(255, 255, 255));
            int xOffset = 100 + ((shouldWrap) ? ((selectedTick - startTick) % 50) * scale : selectedTick * scale);
            int yOffset = ((playerOffsets.get(selectedPlayer) + 1) * scale + 30) + ((shouldWrap) ? ((selectedTick - startTick) / 50) * boxHeight : 30);
            g.drawRect(xOffset, yOffset, scale, scale);
        }
    }

    private Point getPoint(int tick, String player)
    {
        return new Point(
                100 + ((shouldWrap) ? ((tick - startTick) % 50) * scale : tick * scale),
                ((playerOffsets.get(player) + 1) * scale + 10) + ((shouldWrap) ? ((tick - startTick) / 50) * boxHeight : 0));
    }

    private void drawSelectedRow(Graphics2D g)
    {
        if (selectedRow != -1)
        {
            g.setColor(new Color(255, 255, 255));
            int xOffset = 100 + getXOffset(selectedRow);
            int yOffset = 10 + getYOffset(selectedRow);
            g.drawRect(xOffset, yOffset, scale, scale * (players.size() + 2));

            int selectedTickHP = -1;
            try
            {
                selectedTickHP = roomHP.get(selectedRow + 1);
            } catch (Exception ignored)
            {

            }
            int offset = -1;
            switch (room)
            {
                case "Maiden":
                case "Verzik P3":
                    offset = 7;
                    break;
                case "Bloat":
                case "Sotetseg":
                case "Xarpus":
                    offset = 3;
                    break;
                case "Nylocas":
                    offset = 5;
                    offset += (4 - ((offset + selectedRow) % 4));
                    offset -= 2;
                    break;
            }
            String bossWouldHaveDied = (offset != -1) ? "Melee attack on this tick killing would result in: " + RoomUtil.time(selectedRow + 1 + offset + 1) + " (Quick death: " + RoomUtil.time(selectedRow + offset + 1) + ")" : "";
            String HPString = "Boss HP: " + ((selectedTickHP == -1) ? "-" : RoomUtil.varbitHPtoReadable(selectedTickHP));
            HoverBox hoverBox = new HoverBox(HPString, config);
            if (offset != -1)
            {
                hoverBox.addString(bossWouldHaveDied);
            }
            int xPosition = xOffset+scale;
            if(xPosition+hoverBox.getWidth(g) > img.getWidth())
            {
                xPosition = xPosition-hoverBox.getWidth(g)-3*scale;
            }
            hoverBox.setPosition(xPosition, yOffset);
            hoverBox.draw(g);
        }
    }


    private void drawHoverBox(Graphics2D g)
    {
        for (PlayerDidAttack action : actions.keySet())
        {
            if (action.tick == selectedTick && action.player.equals(selectedPlayer))
            {
                Point location = getPoint(action.tick, action.player);
                HoverBox hoverBox = new HoverBox(actions.get(action), config);
                hoverBox.addString("");
                for(String item : action.wornItemNames)
                {
                    hoverBox.addString("."+item);
                }
                int xPosition = location.getX()+10;
                if(xPosition+hoverBox.getWidth(g) > img.getWidth())
                {
                    xPosition = xPosition-hoverBox.getWidth(g)-scale*2;
                }
                hoverBox.setPosition(xPosition, location.getY() - 10);
                hoverBox.draw(g);
            }
        }
    }

    private void drawMaidenCrabs(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setColor(new Color(230, 20, 20, 200));
        if (room.equals("Maiden"))
        {
            for (Integer tick : lines.keySet())
            {
                if(lines.get(tick).equals("Dead"))
                {
                    continue;
                }
                String proc = lines.get(tick);
                int xOffset = 100 + getXOffset(tick + 1);
                int yOffset = 10 + getYOffset(tick + 1);
                yOffset -= scale;
                int crabOffsetX = 0;
                int crabOffsetY;

                crabOffsetY = 11;
                g.drawOval(xOffset + crabOffsetX, yOffset + crabOffsetY, 7, 7);

                crabOffsetY = 20;
                g.drawOval(xOffset + crabOffsetX, yOffset + crabOffsetY, 7, 7);

                crabOffsetX = 9;
                crabOffsetY = 11;
                g.drawOval(xOffset + crabOffsetX, yOffset + crabOffsetY, 7, 7);

                crabOffsetX = 9;
                crabOffsetY = 20;
                g.drawOval(xOffset + crabOffsetX, yOffset + crabOffsetY, 7, 7);

                crabOffsetX = 18;
                crabOffsetY = 11;
                g.drawOval(xOffset + crabOffsetX, yOffset + crabOffsetY, 7, 7);

                crabOffsetX = 18;
                crabOffsetY = 20;
                g.drawOval(xOffset + crabOffsetX, yOffset + crabOffsetY, 7, 7);

                crabOffsetX = 27;
                crabOffsetY = 2;
                g.drawOval(xOffset + crabOffsetX, yOffset + crabOffsetY, 7, 7);

                crabOffsetX = 27;
                crabOffsetY = 20;
                g.drawOval(xOffset + crabOffsetX, yOffset + crabOffsetY, 7, 7);

                crabOffsetX = 27;
                crabOffsetY = 11;
                g.drawOval(xOffset + crabOffsetX, yOffset + crabOffsetY, 7, 7);

                crabOffsetX = 27;
                crabOffsetY = 29;
                g.drawOval(xOffset + crabOffsetX, yOffset + crabOffsetY, 7, 7);


                for (String crab : crabDescriptions)
                {
                    if (crab.contains(proc))
                    {
                        xOffset = 100 + getXOffset(tick + 1);
                        yOffset = 10 + getYOffset(tick + 1);
                        crabOffsetX = 0;
                        crabOffsetY = 0;
                        if (crab.contains("N1"))
                        {
                            crabOffsetY = 11;
                        } else if (crab.contains("S1"))
                        {
                            crabOffsetY = 20;
                        } else if (crab.contains("N2"))
                        {
                            crabOffsetX = 9;
                            crabOffsetY = 11;
                        } else if (crab.contains("S2"))
                        {
                            crabOffsetX = 9;
                            crabOffsetY = 20;
                        } else if (crab.contains("N3"))
                        {
                            crabOffsetX = 18;
                            crabOffsetY = 11;
                        } else if (crab.contains("S3"))
                        {
                            crabOffsetX = 18;
                            crabOffsetY = 20;
                        } else if (crab.contains("N4 (1)"))
                        {
                            crabOffsetX = 27;
                            crabOffsetY = 2;
                        } else if (crab.contains("S4 (1)"))
                        {
                            crabOffsetX = 27;
                            crabOffsetY = 20;
                        } else if (crab.contains("N4 (2)"))
                        {
                            crabOffsetX = 27;
                            crabOffsetY = 11;
                        } else if (crab.contains("S4 (2)"))
                        {
                            crabOffsetX = 27;
                            crabOffsetY = 29;
                        }
                        crabOffsetY -= scale;
                        g.setColor(new Color(230, 20, 20, 200));
                        g.fillOval(xOffset + crabOffsetX, yOffset + crabOffsetY, 7, 7);
                    }
                }
            }
        }
    }

    private void drawRoomTime(Graphics2D g)
    {
        g.setColor(Color.WHITE);
        Font oldFont = g.getFont();
        g.setFont(FontManager.getRunescapeBoldFont());
        g.drawString("Time " + RoomUtil.time(endTick), 5, 20);
        g.setFont(oldFont);
    }

    private void drawCheckBox(Graphics2D g)
    {
        g.setColor(Color.WHITE);
        Font oldFont = g.getFont();
        g.setFont(FontManager.getRunescapeBoldFont());
        g.drawString("Use Icons? ", 100, 20);
        if(!checkBoxHovered)
        {
            g.setColor(new Color(140, 140, 140));
        }

        g.drawRect(180, 2, 20, 20);
        if(checkBoxHovered)
        {
            g.setColor(new Color(140, 140, 140));
            g.fillRect(181, 3, 19, 19);
        }
        g.setColor(Color.WHITE);
        if(config.useIconsOnChart())
        {
            g.drawString("x", 186, 17);
        }
        g.setFont(oldFont);
    }

    private void drawBaseBoxes(Graphics2D g)
    {
        for(int i = startTick; i < endTick; i++)
        {
            for(int j = 0; j < playerOffsets.size(); j++)
            {
                int xOffset = 100 + ((shouldWrap) ? ((i - startTick) % 50) * scale : i * scale);
                if(playerOffsets.get(players.get(j)) == null)
                {
                    continue;
                }
                int yOffset = ((playerOffsets.get(players.get(j)) + 1) * scale + 30) + ((shouldWrap) ? ((i - startTick) / 50) * boxHeight : 30);
                g.setColor(config.primaryMiddle());
                g.fillRoundRect(xOffset + 2, yOffset + 2, scale - 3, scale - 3, 5, 5);
            }
        }
    }

    private void drawGraph()
    {
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        RenderingHints qualityHints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );
        qualityHints.put(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY );
        g.setRenderingHints( qualityHints);
        Color oldColor = g.getColor();

        g.setColor(config.primaryDark());
        g.fillRect(0, 0, img.getWidth(), img.getHeight());


        fontHeight = getStringBounds(g, "a").height;
        g.setColor(Color.WHITE);

        //drawKey(g);
        drawTicks(g);
        drawGraphBoxes(g);
        drawBaseBoxes(g);
        drawYChartColumn(g);
        drawRoomSpecificData(g);
        drawDawnSpecs(g);
        drawPrimaryBoxes(g);
        drawAutos(g);
        drawMarkerLines(g);
        drawThrallBoxes(g);
        drawMaidenCrabs(g);
        drawSelectedOutlineBox(g);
        drawSelectedRow(g);
        drawHoverBox(g);
        drawRoomTime(g);
        drawCheckBox(g);

        g.setColor(oldColor);
        g.dispose();
        repaint();
    }

    public void getTickHovered(int x, int y)
    {
        if (y > 20)
        {
            int boxNumber = (y - 20) / boxHeight;
            if (x > 100)
            {
                int tick = startTick + (50 * boxNumber + ((x - 100) / scale));
                int playerOffsetPosition = (((y - 30 - scale) % boxHeight) / scale);
                if (playerOffsetPosition >= 0 && playerOffsetPosition < players.size() && (y - 30 - scale > 0))
                {
                    selectedTick = tick;
                    selectedPlayer = players.get(playerOffsetPosition);
                    selectedRow = -1;
                } else if (y % boxHeight < 30 + scale)
                {
                    selectedRow = tick;
                    selectedPlayer = "";
                    selectedTick = -1;
                } else
                {
                    selectedPlayer = "";
                    selectedTick = -1;
                    selectedRow = -1;
                }
            } else
            {
                selectedPlayer = "";
                selectedTick = -1;
                selectedRow = -1;
            }
            drawGraph();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(checkBoxHovered)
        {
            configManager.setConfiguration("Advanced Raid Tracker", "useIconsOnChart", !config.useIconsOnChart());
            drawGraph();
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }


    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        selectedPlayer = "";
        selectedTick = -1;
        selectedRow = -1;
        drawGraph();
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        checkBoxHovered = e.getX() >= 180 && e.getX() <= 200 && e.getY() >= 2 && e.getY() <= 22;
        getTickHovered(e.getX(), e.getY());
        drawGraph();
    }

    public String getBossName(int id, int index, int tick)
    {
        try
        {
            switch (id)
            {
                case TobIDs.MAIDEN_P0:
                case TobIDs.MAIDEN_P1:
                case TobIDs.MAIDEN_P2:
                case TobIDs.MAIDEN_P3:
                case TobIDs.MAIDEN_PRE_DEAD:
                case TobIDs.MAIDEN_P0_HM:
                case TobIDs.MAIDEN_P1_HM:
                case TobIDs.MAIDEN_P2_HM:
                case TobIDs.MAIDEN_P3_HM:
                case TobIDs.MAIDEN_PRE_DEAD_HM:
                case TobIDs.MAIDEN_P0_SM:
                case TobIDs.MAIDEN_P1_SM:
                case TobIDs.MAIDEN_P2_SM:
                case TobIDs.MAIDEN_P3_SM:
                case TobIDs.MAIDEN_PRE_DEAD_SM:
                    return "Maiden (" + RoomUtil.varbitHPtoReadable(roomHP.get(tick + 1)) + ")";
                case TobIDs.BLOAT:
                case TobIDs.BLOAT_HM:
                case TobIDs.BLOAT_SM:
                    return "Bloat (" + RoomUtil.varbitHPtoReadable(roomHP.get(tick)) + ")";
                case TobIDs.NYLO_BOSS_MELEE:
                case TobIDs.NYLO_BOSS_RANGE:
                case TobIDs.NYLO_BOSS_MAGE:
                case TobIDs.NYLO_BOSS_MELEE_HM:
                case TobIDs.NYLO_BOSS_RANGE_HM:
                case TobIDs.NYLO_BOSS_MAGE_HM:
                case TobIDs.NYLO_BOSS_MELEE_SM:
                case TobIDs.NYLO_BOSS_RANGE_SM:
                case TobIDs.NYLO_BOSS_MAGE_SM:
                    return "Nylo Boss (" + RoomUtil.varbitHPtoReadable(roomHP.get(tick)) + ")";
                case TobIDs.XARPUS_P23:
                case TobIDs.XARPUS_P23_HM:
                case TobIDs.XARPUS_P23_SM:
                    return "Xarpus (" + RoomUtil.varbitHPtoReadable(roomHP.get(tick)) + ")";
                case TobIDs.VERZIK_P1:
                case TobIDs.VERZIK_P2:
                case TobIDs.VERZIK_P3:
                case TobIDs.VERZIK_P1_HM:
                case TobIDs.VERZIK_P2_HM:
                case TobIDs.VERZIK_P3_HM:
                case TobIDs.VERZIK_P1_SM:
                case TobIDs.VERZIK_P2_SM:
                case TobIDs.VERZIK_P3_SM:
                    return "Verzik (" + RoomUtil.varbitHPtoReadable(roomHP.get(tick)) + ")";
            }
            for (Integer i : NPCMap.keySet())
            {
                if (i == index)
                {
                    String hp = "-1";
                    try
                    {
                        hp = RoomUtil.varbitHPtoReadable(roomHP.get(tick));
                    } catch (Exception ignored
                    )
                    {
                    }
                    return NPCMap.get(i) + " (Boss: " + hp + ")";
                }
            }
            return "?";
        } catch (Exception e)
        {
            return "?";
        }
    }

    public void removeThrall(String owner)
    {
    }
}
