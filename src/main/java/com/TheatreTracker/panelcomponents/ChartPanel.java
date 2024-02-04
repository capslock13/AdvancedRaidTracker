package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.constants.NpcIDs;
import com.TheatreTracker.utility.*;
import com.TheatreTracker.utility.DawnSpec;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class ChartPanel extends JPanel implements MouseListener, MouseMotionListener
{
    private boolean shouldWrap = false;
    private BufferedImage img;
    int scale = 26;
    int boxCount;
    int boxHeight;
    int boxWidth;

    int selectedTick = -1;
    String selectedPlayer = "";

    int selectedRow = -1;

    int startTick;
    int endTick;
    ArrayList<String> players = new ArrayList<>();
    String room;
    WeaponAttack[] weaponAttacks;
    int keyColumns;
    int keyRows;
    int keyCount;
    int keyMargin;
    String roomSpecificText = "";
    private int fontHeight;
    public boolean finished = false;
    private boolean live = false;
    private ArrayList<Integer> autos = new ArrayList<>();
    private Map<Integer, String> NPCMap = new HashMap<>();
    private ArrayList<DawnSpec> dawnSpecs = new ArrayList<>();
    private ArrayList<ThrallOutlineBox> thrallOutlineBoxes = new ArrayList<>();
    private ArrayList<OutlineBox> outlineBoxes = new ArrayList<>();
    private Map<Integer, String> specific = new HashMap<>();
    private Map<Integer, String> lines = new HashMap<>();

    private Map<Integer, Integer> roomHP = new HashMap<>();
    Map<String, Integer> playerOffsets = new LinkedHashMap<>();
    private ArrayList<PlayerTick> actions = new ArrayList<>();

    public void enableWrap()
    {
        shouldWrap = true;
        recalculateSize();
    }

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

    public void setRoomHP(Map<Integer, Integer> hps)
    {
        this.roomHP = hps;
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

    public void setRoomFinished()
    {
        finished = true;
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
        actions.clear();
        roomHP.clear();
        NPCMap.clear();
        finished = false;
        recalculateSize();
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
        WeaponAttack weaponAttack = WeaponDecider.getWeapon(attack.animation, attack.spotAnims, attack.projectile, attack.weapon);
        if(weaponAttack != WeaponAttack.UNDECIDED)
        {
            boolean isTarget = RoomUtil.isPrimaryBoss(attack.targetedID) && attack.targetedID != -1;
            outlineBoxes.add(new OutlineBox(attack.player, attack.tick, weaponAttack.shorthand, weaponAttack.color, isTarget));
            actions.add(new PlayerTick(attack.player, attack.tick, "Target: " + getBossName(attack.targetedID, attack.targetedIndex, attack.tick)));
        }
    }

    public String getTarget(int id, int index)
    {
        return "";
    }

    public void addLiveAttack(PlayerDidAttack attack)
    {
        attack.tick += endTick;
        addAttack(new PlayerDidAttack(attack.player, attack.animation, attack.tick, attack.weapon, attack.projectile, attack.spotAnims, attack.targetedIndex, attack.targetedID));
    }

    public void addAttacks(ArrayList<PlayerDidAttack> attacks)
    {
        for(PlayerDidAttack attack : attacks)
        {
            addAttack(attack);
        }
    }

    public void setPlayers(ArrayList<String> players)
    {
        this.players = players;
    }

    public void removeThrall(String player)
    {

    }

    public Rectangle getViewRect()
    {
        return new Rectangle(0, (boxCount > 0) ? (boxCount-1)*boxHeight : 0, boxWidth, boxHeight);
    }

    public void incrementTick()
    {
        endTick++;
        if(endTick % 50 == 0 || endTick == 1)
        {
            recalculateSize();
        }
        else
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

    public void setRoomSpecificText(String text)
    {
        roomSpecificText = text;
    }

    public void recalculateSize()
    {
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
            boxHeight = (players.size() + 3) * scale;
            int height = boxCount * boxHeight+scale;
            if (height < 600)
            {
                height = 600;
            }
            boxWidth = (shouldWrap) ? (100 + (scale * 51)) : 100 + (length + 1) * scale;
            this.weaponAttacks = WeaponAttack.values();
            keyCount = weaponAttacks.length;
            keyRows = 9;
            keyMargin = 10;
            keyColumns = keyCount / keyRows;
            if (keyCount % keyRows != 0)
            {
                keyColumns++;
            }
            int width = boxWidth + (keyColumns * 150) + 40;
            if(img != null)
            {
                img.flush();
            }
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            drawGraph();

    }

    public ChartPanel(String room, boolean isLive)
    {
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

    private int getStringWidth(Graphics2D g, String str)
    {
        FontRenderContext frc = g.getFontRenderContext();
        GlyphVector gv = g.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, 0, 0).width;
    }

    private void drawKey(Graphics2D g)
    {
        int currentColumn = 0;
        int currentRow = 0;
        for (int i = 0; i < keyCount; i++)
        {
            WeaponAttack attack = weaponAttacks[i];
            g.setColor(attack.color);
            g.fillRect(boxWidth + keyMargin + (currentColumn * 150) + 2, keyMargin + (currentRow * (scale+10)) + 7, scale, scale);
            g.setColor(Color.WHITE);
            g.drawString(attack.shorthand, boxWidth + keyMargin + (currentColumn * 150) + 3, keyMargin + (currentRow * 30) + 22);
            g.drawString(attack.name, boxWidth + keyMargin + (currentColumn * 150) + 33, keyMargin + (currentRow * 30) + 22);
            currentRow++;
            if (currentRow + 1 > keyRows)
            {
                currentColumn++;
                currentRow = 0;
            }
        }
    }

    private void drawTicks(Graphics2D g)
    {
        for (int i = startTick; i < endTick; i++)
        {
            int xOffset = (shouldWrap) ? ((i - startTick) % 50) * scale : i * scale;
            int yOffset = (shouldWrap) ? ((i - startTick) / 50) * boxHeight : 0;
            yOffset += scale;
            g.setColor(Color.DARK_GRAY);
            g.drawLine(100 + xOffset + scale, yOffset - (fontHeight / 2), 100 + xOffset + scale, yOffset + boxHeight - (2 * scale) + 10);
            g.setColor(new Color(220, 220, 220));
            Font oldFont = g.getFont();
            g.setFont(oldFont.deriveFont(10.0f));
            int strWidth = getStringBounds(g, String.valueOf(i), 0, 0).width;
            g.drawString(String.valueOf(i), 100 + xOffset + (scale/2)-(strWidth/2), yOffset + (fontHeight / 2));
            g.setFont(oldFont);
        }
    }

    private void drawAutos(Graphics2D g)
    {
        for (Integer i : autos)
        {
            g.setColor(new Color(255, 80, 80, 100));
            int xOffset = (shouldWrap) ? ((i - startTick) % 50) * scale : i * scale;
            int yOffset = (shouldWrap) ? ((i - startTick) / 50) * boxHeight : 0;
            g.fillRect(xOffset + 100, yOffset + 10, scale, boxHeight - scale);
        }
    }

    private void drawGraphBoxes(Graphics2D g)
    {
        for (int i = 0; i < boxCount; i++)
        {
            int startX = 100;
            int startY = boxHeight * i + 10;
            int endX = boxWidth - scale;
            int endY = startY + boxHeight;
            g.setColor(new Color(100, 100, 100));

            g.drawLine(startX, startY + scale, endX, startY + scale);
            g.drawLine(startX, startY + scale, startX, endY - scale);
            g.drawLine(startX, endY - scale, endX, endY - scale);
            g.drawLine(endX, endY - scale, endX, startY + scale);
        }
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
                g.drawLine(100, 10 + (j * boxHeight) + ((i + 2) * scale), boxWidth - scale, 10 + (j * boxHeight) + ((i + 2) * scale));
                g.setColor(Color.WHITE);
                g.drawString(players.get(i), 10, (j * boxHeight) + ((i + 2) * scale) + (fontHeight) / 2);
                if (i == 0)
                {
                    g.drawString(roomSpecificText, 10, j * boxHeight + ((players.size() + 2) * scale) + (fontHeight / 2));
                }
            }
        }

    }

    private void drawRoomSpecificData(Graphics2D g)
    {
        for (Integer i : specific.keySet())
        {
            int xOffset = (shouldWrap) ? ((i - startTick) % 50) * scale : i * scale;
            int yOffset = (shouldWrap) ? ((i - startTick) / 50) * boxHeight : 0;
            xOffset += 100;
            yOffset += (playerOffsets.size() + 2) * scale - 10;
            g.setColor(Color.WHITE);
            int strWidth = getStringBounds(g, "X", 0, 0).width;
            g.drawString("X", xOffset+(scale/2)-(strWidth/2), yOffset + (fontHeight / 2) + 10);
        }
    }

    private void drawDawnSpecs(Graphics2D g)
    {
        for(DawnSpec dawnSpec : dawnSpecs)
        {
            String damage = String.valueOf(dawnSpec.getDamage());
            if(dawnSpec.getDamage() != -1)
            {
                int xOffset = (shouldWrap) ? ((dawnSpec.tick - startTick-2) % 50) * scale : (dawnSpec.tick+2) * scale;
                int yOffset = (shouldWrap) ? ((dawnSpec.tick - startTick-2) / 50) * boxHeight : 0;
                xOffset += 100;
                yOffset += (playerOffsets.size() + 3) * scale - 10;
                g.setColor(Color.WHITE);
                int textOffset = (scale / 2) - (getStringBounds(g, damage, 0, 0).width) / 2;
                g.drawString(damage, xOffset+textOffset, yOffset + (fontHeight/2) + 10);
            }
        }
    }

    private void drawPrimaryBoxes(Graphics2D g)
    {
        for(OutlineBox box : outlineBoxes)
        {
            if(box.tick >= startTick && box.tick <= endTick)
            {
                int xOffset = 100 + ((shouldWrap) ? ((box.tick - startTick) % 50) * scale : box.tick * scale);
                int yOffset = ((playerOffsets.get(box.player) + 1) * scale + 10) + ((shouldWrap) ? ((box.tick - startTick) / 50) * boxHeight : 0);
                g.setColor(box.color);
                g.fillRect(xOffset + 1, yOffset + 1, scale - 1, scale - 1);
                g.setColor((box.primaryTarget) ? Color.WHITE : new Color(0, 190, 255));
                int textOffset = (scale/2) - (getStringWidth(g, box.letter) / 2);
                g.drawString(box.letter, xOffset + textOffset, yOffset + (fontHeight/2) + (scale/2));
            }
        }
    }

    private void drawMarkerLines(Graphics2D g)
    {
        if(!live || finished)
        {
            for (Integer i : lines.keySet())
            {
                int xOffset = (shouldWrap) ? ((i - startTick) % 50) * scale : i * scale;
                int yOffset = (shouldWrap) ? ((i - startTick) / 50) * boxHeight : 0;
                xOffset += 100;
                yOffset += (scale / 2);
                g.setColor(new Color(255, 0, 0));
                g.drawLine(xOffset, yOffset, xOffset, yOffset + boxHeight - 20);
                int stringLength = getStringBounds(g, lines.get(i), 0, 0).width;
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
            // log.info("spawn tick: " + box.spawnTick);
            // log.info("max tick: " + maxTick);
            int lastEndTick = box.spawnTick;
            while (lastEndTick < maxTick)
            {
                int yOffset = (shouldWrap) ? ((lastEndTick - startTick) / 50) * boxHeight : 0;
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
                //   log.info("Current start, end tick: " + lastEndTick +", " + currentEndTick);
                //  log.info("Current Y offset: " + yOffset);
                int xOffsetStart = (shouldWrap) ? ((lastEndTick - startTick) % 50) * scale : (lastEndTick - 1) * scale;
                xOffsetStart += 100;
                int xOffsetEnd = (shouldWrap) ? ((currentEndTick - startTick - 1) % 50) * scale : (currentEndTick - 1) * scale;
                xOffsetEnd += 100;
                //  log.info("xstart: " + xOffsetStart + ", xend: " + xOffsetEnd);
                lastEndTick = currentEndTick;
                g.fillRect(xOffsetStart, yOffset + 1, xOffsetEnd - xOffsetStart + scale, scale - 2);
                //  log.info("done drawing boxes");
            }
        }
    }

    private void drawSelectedOutlineBox(Graphics2D g)
    {
        if(selectedTick != -1 && !selectedPlayer.equalsIgnoreCase(""))
        {
            g.setColor(new Color(255, 255, 255));
            int xOffset = 100 + ((shouldWrap) ? ((selectedTick - startTick) % 50) * scale : selectedTick * scale);
            int yOffset = ((playerOffsets.get(selectedPlayer) + 1) * scale + 10) + ((shouldWrap) ? ((selectedTick - startTick) / 50) * boxHeight : 0);
            g.drawRect(xOffset, yOffset, scale, scale);
        }
    }

    private com.TheatreTracker.Point getPoint(int tick, String player)
    {
        return new com.TheatreTracker.Point(
                100 + ((shouldWrap) ? ((tick - startTick) % 50) * scale : tick * scale),
                ((playerOffsets.get(player) + 1) * scale + 10) + ((shouldWrap) ? ((tick - startTick) / 50) * boxHeight : 0));
    }

    private void drawSelectedRow(Graphics2D g)
    {
        if(selectedRow != -1)
        {
            g.setColor(new Color(255, 255, 255));
            int xOffset = 100 + ((shouldWrap) ? ((selectedRow - startTick) % 50) * scale : selectedRow * scale);
            int yOffset = (10) + ((shouldWrap) ? ((selectedRow - startTick) / 50) * boxHeight : 0);
            g.drawRect(xOffset, yOffset, scale, scale*(players.size()+2));

            int selectedTickHP = -1;
            try
            {
                selectedTickHP = roomHP.get(selectedRow+1);
            }
            catch (Exception e)
            {

            }
            if(selectedTickHP != -1)
            {
                String HPString = "Boss HP: " + RoomUtil.varbitHPtoReadable(selectedTickHP);
                int stringLength = getStringWidth(g, HPString);
                g.setColor(new Color(0, 0, 0, 220));
                g.fillRect(xOffset+5+scale, yOffset, stringLength+10, 20);
                g.setColor(Color.WHITE);
                g.drawRect(xOffset+5+scale, yOffset, stringLength+10, 20);
                g.drawString(HPString, xOffset+10+scale, yOffset+15);
            }
        }
    }

    private void drawHoverBox(Graphics2D g)
    {
        for(PlayerTick action : actions)
        {
            if(action.tick == selectedTick && action.player.equals(selectedPlayer))
            {
                com.TheatreTracker.Point location = getPoint(action.tick, action.player);
                int stringLength = getStringWidth(g, action.action);
                g.setColor(new Color(0, 0, 0, 220));
                g.fillRect(location.getX()+10, location.getY()-30, stringLength+10, 20);
                g.setColor(Color.WHITE);
                g.drawRect(location.getX()+10, location.getY()-30, stringLength+10, 20);
                g.drawString(action.action, location.getX()+15, location.getY()-15);
            }
        }
    }

    private void drawGraph()
    {
        //if(players.size() != 0 && endTick != 0)
        {
            Graphics2D g = (Graphics2D) img.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            Color oldColor = g.getColor();

            g.setColor(new Color(40, 40, 40));
            g.fillRect(0, 0, img.getWidth(), img.getHeight());


            fontHeight = getStringBounds(g, "a", 0, 0).height;
            g.setColor(Color.WHITE);
            g.drawRect(boxWidth + (keyMargin / 2), keyMargin, (keyColumns * 150) - 10, (keyRows * 30));

            drawKey(g);
            drawTicks(g);
            drawAutos(g);
            drawGraphBoxes(g);
            drawYChartColumn(g);
            drawRoomSpecificData(g);
            drawDawnSpecs(g);
            drawPrimaryBoxes(g);
            drawMarkerLines(g);
            drawThrallBoxes(g);
            drawSelectedOutlineBox(g);
            drawSelectedRow(g);
            drawHoverBox(g);

            g.setColor(oldColor);
            g.dispose();
            repaint();
        }
    }

    public void getTickHovered(int x, int y)
    {
        int boxNumber = y/boxHeight;
        if(x > 100)
        {
            int tick = startTick+(50*boxNumber + ((x-100)/scale));
            int playerOffsetPosition = (((y-10-scale)%boxHeight)/scale);
            if(playerOffsetPosition >= 0 && playerOffsetPosition < players.size() && (y-10-scale > 0))
            {
                selectedTick = tick;
                selectedPlayer = players.get(playerOffsetPosition);
                selectedRow = -1;
            }
            else if(y%boxHeight < 10+scale)
            {
                selectedRow = tick;
                selectedPlayer = "";
                selectedTick = -1;
            }
            else
            {
                selectedPlayer = "";
                selectedTick = -1;
                selectedRow = -1;
            }
        }
        else
        {
            selectedPlayer = "";
            selectedTick = -1;
            selectedRow = -1;
        }
        drawGraph();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
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

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        getTickHovered(e.getX(), e.getY());
    }

    public String getBossName(int id, int index, int tick)
    {
        try
        {
            switch (id)
            {
                case NpcIDs.MAIDEN_P0:
                case NpcIDs.MAIDEN_P1:
                case NpcIDs.MAIDEN_P2:
                case NpcIDs.MAIDEN_P3:
                case NpcIDs.MAIDEN_PRE_DEAD:
                case NpcIDs.MAIDEN_P0_HM:
                case NpcIDs.MAIDEN_P1_HM:
                case NpcIDs.MAIDEN_P2_HM:
                case NpcIDs.MAIDEN_P3_HM:
                case NpcIDs.MAIDEN_PRE_DEAD_HM:
                case NpcIDs.MAIDEN_P0_SM:
                case NpcIDs.MAIDEN_P1_SM:
                case NpcIDs.MAIDEN_P2_SM:
                case NpcIDs.MAIDEN_P3_SM:
                case NpcIDs.MAIDEN_PRE_DEAD_SM:
                    return "Maiden (" + RoomUtil.varbitHPtoReadable(roomHP.get(tick+1)) + ")";
                case NpcIDs.BLOAT:
                case NpcIDs.BLOAT_HM:
                case NpcIDs.BLOAT_SM:
                    return "Bloat (" + RoomUtil.varbitHPtoReadable(roomHP.get(tick)) + ")";
                case NpcIDs.NYLO_BOSS_MELEE:
                case NpcIDs.NYLO_BOSS_RANGE:
                case NpcIDs.NYLO_BOSS_MAGE:
                case NpcIDs.NYLO_BOSS_MELEE_HM:
                case NpcIDs.NYLO_BOSS_RANGE_HM:
                case NpcIDs.NYLO_BOSS_MAGE_HM:
                case NpcIDs.NYLO_BOSS_MELEE_SM:
                case NpcIDs.NYLO_BOSS_RANGE_SM:
                case NpcIDs.NYLO_BOSS_MAGE_SM:
                    return "Nylo Boss (" + RoomUtil.varbitHPtoReadable(roomHP.get(tick)) + ")";
                case NpcIDs.XARPUS_P23:
                case NpcIDs.XARPUS_P23_HM:
                case NpcIDs.XARPUS_P23_SM:
                    return "Xarpus (" + RoomUtil.varbitHPtoReadable(roomHP.get(tick)) + ")";
                case NpcIDs.VERZIK_P1:
                case NpcIDs.VERZIK_P2:
                case NpcIDs.VERZIK_P3:
                case NpcIDs.VERZIK_P1_HM:
                case NpcIDs.VERZIK_P2_HM:
                case NpcIDs.VERZIK_P3_HM:
                case NpcIDs.VERZIK_P1_SM:
                case NpcIDs.VERZIK_P2_SM:
                case NpcIDs.VERZIK_P3_SM:
                    return "Verzik (" + RoomUtil.varbitHPtoReadable(roomHP.get(tick)) + ")";
            }
            for(Integer i : NPCMap.keySet())
            {
                if(i == index)
                {
                    String hp = "-1";
                    try
                    {
                        hp = RoomUtil.varbitHPtoReadable(roomHP.get(tick));
                    }
                    catch(Exception e
                    ) {}
                    return NPCMap.get(i) + " (Boss: " + hp + ")";
                }
            }
            return "(?) -> " + id + "," + index;
        }
        catch(Exception e)
        {
            return "?";
        }
    }
}
