package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.TobIDs;
import com.advancedraidtracker.utility.*;
import com.advancedraidtracker.utility.Point;
import com.advancedraidtracker.utility.weapons.PlayerAnimation;
import com.advancedraidtracker.utility.weapons.AnimationDecider;
import com.advancedraidtracker.utility.wrappers.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static com.advancedraidtracker.utility.UISwingUtility.*;

@Slf4j
public class ChartPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener
{
    private final int TITLE_BAR_PLUS_TAB_HEIGHT = 63;
    private boolean shouldWrap;
    private BufferedImage img;
    int scale;
    int boxCount;
    int boxHeight;
    int boxWidth;
    private int windowHeight = 600;
    private int windowWidth = 1410;
    int RIGHT_MARGIN = 10;
    int TOP_MARGIN = 30;
    int NAME_MARGIN = 6;
    int LEFT_MARGIN = 100;
    int instanceTime = 0;
    int ticksToShow = 50;

    int selectedTick = -1;
    String selectedPlayer = "";

    int selectedRow = -1;
    boolean checkBoxHovered = false;
    boolean checkBox2Hovered = false;
    boolean checkBox3Hovered = false;

    int startTick;
    public int endTick;

    List<String> attackers = new ArrayList<>();

    List<String> playersOnly = new ArrayList<>();
    String room;
    @Setter
    String roomSpecificText = "";
    private int fontHeight;
    public boolean finished = false;
    private boolean enforceCD = false;
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

    private PlayerAnimation selectedPrimary = PlayerAnimation.NOT_SET;
    private PlayerAnimation selectedSecondary = PlayerAnimation.NOT_SET;

    private ConfigManager configManager;
    @Getter
    private boolean isActive = false;

    public void setActive(boolean state)
    {
        isActive = state;
        if(!isActive)
        {
            img = null;
        }
        else
        {
            createImage();
        }
    }

    public boolean shouldDraw()
    {
        return !live || isActive;
    }

    private final ItemManager itemManager;
    private final SpriteManager spriteManager;

    public void enableWrap()
    {
        shouldWrap = true;
        recalculateSize();
    }

    private int boxesToShow = 1;

    public void setSize(int x, int y)
    {
        windowWidth = x;
        windowHeight = y;
        createImage();
    }

    public void createImage()
    {
        if (isActive || !live)
        {
            boxesToShow = Math.min(1 + ((windowHeight - TITLE_BAR_PLUS_TAB_HEIGHT - scale) / boxHeight), boxCount);
            if (img != null)
            {
                img.flush();
            }
            img = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
            recalculateSize();
            sendToBottom();
        }
    }

    public void setPrimaryTool(PlayerAnimation tool)
    {
        selectedPrimary = tool;
    }

    public void setSecondaryTool(PlayerAnimation tool)
    {
        selectedSecondary = tool;
    }

    private final AdvancedRaidTrackerConfig config;

    public void addRoomSpecificData(int tick, String data)
    {
        specific.put(tick, data);
        if (specific.size() == 1)
        {
            recalculateSize();
        }
    }

    public void addRoomSpecificDatum(Map<Integer, String> specificData)
    {
        specific.putAll(specificData);
        recalculateSize();
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

    public void addAutos(List<Integer> autos)
    {
        this.autos.addAll(autos);
    }

    public void addThrallBox(ThrallOutlineBox thrallOutlineBox)
    {
        synchronized (thrallOutlineBoxes)
        {
            thrallOutlineBoxes.add(thrallOutlineBox);
        }
    }

    public void addThrallBoxes(List<ThrallOutlineBox> outlineBoxes)
    {
        synchronized (thrallOutlineBoxes)
        {
            thrallOutlineBoxes.addAll(outlineBoxes);
        }
    }

    public void addDawnSpec(DawnSpec dawnSpec)
    {
        synchronized (outlineBoxes)
        {
            for (OutlineBox outlineBox : outlineBoxes)
            {
                //based on projectile time dawn spec will always be applied between 2 and 4 ticks after the animation, and since there is only one
                //dawnbringer its impossible for the next spec to overlap this
                if ((dawnSpec.tick - outlineBox.tick <= 4 && dawnSpec.tick - outlineBox.tick >= 2) && outlineBox.playerAnimation.equals(PlayerAnimation.DAWN_SPEC))
                {
                    outlineBox.additionalText = String.valueOf(dawnSpec.getDamage());
                }
            }
        }
    }

    public void addDawnSpecs(List<DawnSpec> dawnSpecs)
    {
        for (DawnSpec dawnSpec : dawnSpecs)
        {
            addDawnSpec(dawnSpec);
        }
        drawGraph();
    }

    public void addNPC(int index, String name)
    {
        attackers.add(0, String.valueOf(index));
        redraw();
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

    int baseStartTick = 0;
    int baseEndTick = 0;

    public void resetGraph()
    {
        playerWasOnCD.clear();
        currentBox = 0;
        currentScrollOffsetY = 0;
        currentScrollOffsetX = 0;
        endTick = 0;
        startTick = 0;
        baseEndTick = 0;
        baseStartTick = 0;
        selectedRow = -1;
        selectedTick = -1;
        selectedPlayer = "";
        synchronized (outlineBoxes)
        {
            outlineBoxes.clear();
        }
        autos.clear();
        lines.clear();
        specific.clear();
        dawnSpecs.clear();
        thrallOutlineBoxes.clear();
        attackers.clear();
        playerOffsets.clear();
        crabDescriptions.clear();
        actions.clear();
        roomHP.clear();
        NPCMap.clear();
        finished = false;
        recalculateSize();
    }

    public void addMaidenCrabs(List<String> crabDescriptions)
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

    public void addAttack(PlayerDidAttack attack, PlayerAnimation playerAnimation)
    {
        if (clientThread != null)
        {
            if (config.useUnkitted())
            {
                attack.useUnkitted();
            }
            clientThread.invoke(() ->
            {
                attack.setIcons(itemManager, spriteManager);
            });
            clientThread.invoke(attack::setWornNames);
        }
        if (playerAnimation.equals(PlayerAnimation.NOT_SET))
        {
            playerAnimation = AnimationDecider.getWeapon(attack.animation, attack.spotAnims, attack.projectile, attack.weapon);
        }
        if (playerAnimation != PlayerAnimation.EXCLUDED_ANIMATION && playerAnimation != PlayerAnimation.UNDECIDED)
        {
            boolean isTarget = RoomUtil.isPrimaryBoss(attack.targetedID) && attack.targetedID != -1;
            String targetString = playerAnimation.name + ": ";
            String targetName = getBossName(attack.targetedID, attack.targetedIndex, attack.tick);
            if (targetName.equals("?"))
            {
                targetString += attack.targetName;
            } else
            {
                targetString += targetName;
            }
            synchronized (actions)
            {
                actions.put(attack, targetString);
            }
            String additionalText = "";
            if (targetString.contains("(on w"))
            {
                additionalText = targetString.substring(targetString.indexOf("(on w") + 5);
                additionalText = "s" + additionalText.substring(0, additionalText.indexOf(")"));
            } else if (targetString.contains("small") || targetString.contains("big"))
            {
                additionalText = getShortenedString(targetString, playerAnimation.name.length());
            } else if (targetString.contains("70s") || targetString.contains("50s") || targetString.contains("30s"))
            {
                String shortenedString = targetString.substring(playerAnimation.name.length() + 2);
                shortenedString = shortenedString.substring(0, 2);
                String proc = targetString.substring(targetString.indexOf("0s") - 1, targetString.indexOf("0s") + 1);

                additionalText = proc + shortenedString;
            }
            synchronized (outlineBoxes)
            {
                outlineBoxes.add(new OutlineBox(attack, playerAnimation.shorthand, playerAnimation.color, isTarget, additionalText, playerAnimation, playerAnimation.attackTicks, RaidRoom.getRoom(this.room)));
            }
            for (int i = attack.tick; i < attack.tick + playerAnimation.attackTicks; i++)
            {
                playerWasOnCD.put(attack.player, i);
            }
        }
        if (!live)
        {
            drawGraph();
        }
    }

    public void addAttack(PlayerDidAttack attack)
    {
        addAttack(attack, PlayerAnimation.NOT_SET);
    }

    private static String getShortenedString(String targetString, int index)
    {
        String shortenedString = targetString.substring(index + 3);
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

    public void addAttacks(Collection<PlayerDidAttack> attacks)
    {
        for (PlayerDidAttack attack : attacks)
        {
            addAttack(attack);
        }
    }

    public void incrementTick()
    {
        endTick++;
        if (endTick % ticksToShow == 0 || endTick == 1)
        {
            recalculateSize();
            sendToBottom();
        } else
        {
            drawGraph();
        }
    }

    public void setTick(int tick)
    {
        endTick = tick;
        baseEndTick = tick;
        recalculateSize();
    }

    public void setStartTick(int tick)
    {
        startTick = tick;
        baseStartTick = tick;
        recalculateSize();
    }

    public void recalculateSize()
    {
        if (!shouldDraw())
        {
            return;
        }
        try
        {
            scale = config.chartScaleSize();
            setBackground(config.primaryDark());
        } catch (Exception ignored)
        {

        }
        int length = endTick - startTick;
        ticksToShow = ((windowWidth - LEFT_MARGIN - RIGHT_MARGIN) / (scale)) - 1;
        boxCount = (length / ticksToShow);
        if (boxCount % ticksToShow != 0)
        {
            boxCount++;
        }
        if (boxCount < 1)
        {
            boxCount = 1;
        }
        int additionalRows = 2 + getAdditionalRow();
        boxHeight = ((attackers.size() + additionalRows) * scale);
        boxWidth = (LEFT_MARGIN + (scale * (ticksToShow + 1)));
        boxesToShow = Math.min(1 + ((windowHeight - TITLE_BAR_PLUS_TAB_HEIGHT - scale) / boxHeight), boxCount);
        drawGraph();
    }

    public void sendToBottom()
    {
        if (TITLE_BAR_PLUS_TAB_HEIGHT + scale + boxCount * boxHeight > windowHeight)
        {
            currentBox = boxCount - 1 - boxesToShow + 1;
            int lastBoxEnd = (boxesToShow * boxHeight) + scale + TITLE_BAR_PLUS_TAB_HEIGHT;
            currentScrollOffsetY = (currentBox * boxHeight) + (lastBoxEnd - windowHeight);
        }
        drawGraph();
    }


    private final ClientThread clientThread;

    private static volatile boolean isCtrlPressed = false;
    public static boolean isCtrlPressed()
    {
        synchronized (ChartPanel.class)
        {
            return isCtrlPressed;
        }
    }

    public ChartPanel(String room, boolean isLive, AdvancedRaidTrackerConfig config, ClientThread clientThread, ConfigManager configManager, ItemManager itemManager, SpriteManager spriteManager)
    {
        this.itemManager = itemManager;
        this.spriteManager = spriteManager;
        this.configManager = configManager;
        this.config = config;
        this.clientThread = clientThread;
        setBackground(config.primaryDark());
        setOpaque(true);
        scale = 26;
        live = isLive;
        this.room = room;
        startTick = 0;
        endTick = 0;
        shouldWrap = true;
        boxWidth = LEFT_MARGIN + scale * (ticksToShow + 1);
        windowWidth = boxWidth+10;
        windowHeight = 600;
        img = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
        setFocusable(true);
        requestFocus();
        recalculateSize();
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e ->
        {
            synchronized (ChartPanel.class)
            {
                switch (e.getID())
                {
                    case KeyEvent.KEY_PRESSED:
                        if (e.getKeyCode() == KeyEvent.VK_CONTROL)
                        {
                            isCtrlPressed = true;
                        }
                        break;

                    case KeyEvent.KEY_RELEASED:
                        if (e.getKeyCode() == KeyEvent.VK_CONTROL)
                        {
                            isCtrlPressed = false;
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected synchronized void paintComponent(Graphics g)
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
        return new Dimension(windowWidth, windowHeight);
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

    int getYOffset(int tick)
    {
        return ((((tick - startTick) / ticksToShow) * boxHeight) + TOP_MARGIN) - (currentScrollOffsetY);
    }

    int getXOffset(int tick)
    {
        return LEFT_MARGIN + (((tick - startTick) % ticksToShow) * scale) - (currentScrollOffsetX%scale);
    }

    private void drawBoxStyleAccordingToConfig(Graphics2D g, int x, int y, int width, int height, int roundX, int roundY)
    {
        if (config.useRounded())
        {
            g.drawRoundRect(x, y, width, height, roundX, roundY);
        } else
        {
            g.drawRect(x - 1, y - 1, width + 3, height + 3);
        }
    }

    private void fillBoxStyleAccordingToConfig(Graphics2D g, int x, int y, int width, int height, int roundX, int roundY)
    {
        if (config.useRounded())
        {
            g.fillRoundRect(x, y, width, height, roundX, roundY);
        } else
        {
            g.fillRect(x - 1, y - 1, width + 3, height + 3);
        }
    }


    private void drawTicks(Graphics2D g)
    {
        if (room.equals("Nylocas")) //todo make generic, use existing methods
        {
            for (Integer i : lines.keySet())
            {
                if (lines.get(i).equals("W1"))
                {
                    instanceTime = i % 4;
                }
            }
        }
        for (int i = startTick; i < endTick; i++)
        {
            if (shouldTickBeDrawn(i))
            {
                int xOffset = getXOffset(i);
                int yOffset = getYOffset(i) + scale;
                g.setColor(config.fontColor());
                Font oldFont = g.getFont();
                if (!config.useAlternateFont())
                {
                    g.setFont(oldFont.deriveFont(10.0f));
                }
                int strWidth = getStringBounds(g, String.valueOf(i)).width;
                int stallsUntilThisPoint = 0;
                if (room.equals("Nylocas"))
                {
                    for (Integer s : lines.keySet())
                    {
                        if (s < (i - 3))
                        {
                            if (lines.get(s).equals("Stall"))
                            {
                                stallsUntilThisPoint++;
                            }
                        }
                    }
                }
                int ticks = i - (stallsUntilThisPoint * 4);
                if (autos.contains(ticks))
                {
                    g.setColor(Color.RED);
                }
                String tick = (config.useTimeOnChart()) ? RoomUtil.time(ticks) : String.valueOf(ticks);
                if (tick.endsWith("s")) //strip scuffed indicator from crab description because 5 letters is too many to draw
                {
                    tick = tick.substring(0, tick.length() - 1);
                }
                int textPosition = (config.wrapAllBoxes()) ? xOffset + scale - strWidth - (scale / 4) : xOffset + (scale / 2) - (strWidth / 2);
                if (yOffset - (fontHeight / 2) > scale + 5 && xOffset > LEFT_MARGIN)
                {
                    if (config.useTimeOnChart())
                    {
                        drawStringRotated(g, tick, textPosition, yOffset - (fontHeight) - 5, config.fontColor());
                    } else
                    {
                        g.drawString(tick, textPosition, yOffset - (fontHeight / 2));
                    }
                    if (config.wrapAllBoxes())
                    {
                        g.setColor(config.boxColor());
                        g.drawRect(xOffset, yOffset - scale, scale, scale);
                    }
                }
                g.setFont(oldFont);
            }
        }
    }

    private void drawAutos(Graphics2D g)
    {
        for (Integer i : autos)
        {
            if (shouldTickBeDrawn(i))
            {
                g.setColor(new Color(255, 80, 80, 40));
                int xOffset = getXOffset(i);
                int yOffset = getYOffset(i);
                g.fillRect(xOffset, yOffset + scale, scale, boxHeight - (scale * (2 + getAdditionalRow())));
            }
        }
    }

    private void drawGraphBoxes(Graphics2D g)
    {
        for (int i = 0; i < boxesToShow; i++)
        {
            int startX = LEFT_MARGIN;
            int startY = boxHeight * i + TOP_MARGIN - (currentScrollOffsetY - (currentBox * boxHeight));
            int endX = boxWidth - scale;
            int endY = startY + boxHeight;
            g.setColor(config.boxColor());

            if (startY > 5)
            {
                g.drawLine(startX, startY + scale, endX, startY + scale);
            }
            g.drawLine(startX, (startY > 5) ? startY + scale : scale + 5, startX, endY - scale);
            if (endY - scale > 5 + scale)
            {
                g.drawLine(startX, endY - scale, endX, endY - scale);
            }
            g.drawLine(endX, endY - scale, endX, (startY > 5) ? startY + scale : scale + 5);
        }
    }


    private void setConfigFont(Graphics2D g)
    {
        if (config.useAlternateFont())
        {
            g.setFont(new Font("Arial", Font.PLAIN, 12));
        } else
        {
            g.setFont(FontManager.getRunescapeBoldFont());
        }
    }

    private void drawYChartColumn(Graphics2D g)
    {
        g.setColor(config.fontColor());
        for (int i = 0; i < attackers.size(); i++)
        {
            playerOffsets.put(attackers.get(i), i);
            for (int j = currentBox; j < currentBox + boxesToShow; j++)
            {
                g.setColor(config.primaryLight());
                int nameRectsY = (j * boxHeight) + ((i + 1) * scale) + TOP_MARGIN - currentScrollOffsetY;
                if (nameRectsY > scale + 5)
                {
                    g.fillRoundRect(5, nameRectsY + (NAME_MARGIN / 2), 90, scale - NAME_MARGIN, 10, 10);

                }
                g.setColor(config.fontColor());
                Font oldFont = g.getFont();
                setConfigFont(g);
                int width = getStringWidth(g, attackers.get(i));
                int margin = 5;
                int subBoxWidth = LEFT_MARGIN - (margin * 2);
                int textPosition = margin + (subBoxWidth - width) / 2;
                int yPosition = ((j * boxHeight) + ((i + 1) * scale) + (fontHeight) / 2) + (scale / 2) + TOP_MARGIN - (currentScrollOffsetY);
                if (yPosition > scale + 5)
                {
                    String attackerName = attackers.get(i);
                    for (int r : NPCMap.keySet())
                    {
                        if (String.valueOf(r).equals(attackerName))
                        {
                            attackerName = NPCMap.get(r).split(" ")[0];
                        }
                    }
                    if (config.chartTheme().equals(ChartTheme.EXCEL))
                    {
                        if (attackerName.startsWith("Player"))
                        {
                            attackerName = "P" + attackerName.substring(attackerName.length() - 1);
                        }
                        int strWidth = getStringWidth(g, attackerName);
                        textPosition = LEFT_MARGIN - strWidth - (scale / 2) + 2;
                        g.setColor(config.boxColor());
                        g.drawRect(LEFT_MARGIN - (int) (scale * 1.5), yPosition - (fontHeight / 2) - (scale / 2), (int) (scale * 1.5), scale);
                    }
                    g.setColor(config.fontColor());
                    g.drawString(attackerName, textPosition, yPosition + margin);
                }

                if (i == 0)
                {
                    if (room.equals("Nylocas"))
                    {
                        roomSpecificText = "Instance Time";
                    }
                    int textYPosition = getYOffset((j * ticksToShow) + 1) + ((playerOffsets.size() + 2) * scale) - (scale / 2) + (fontHeight / 2);
                    if (textYPosition > scale + 5 && !specific.isEmpty()  && textPosition > LEFT_MARGIN)
                    {
                        g.drawString(roomSpecificText, 5, textYPosition);
                    }
                    if (config.showBoldTick())
                    {
                        int yPos = getYOffset(0);
                        Font oldF = g.getFont();
                        g.setFont(new Font("Arial", Font.BOLD, 12));
                        g.setColor(config.fontColor());
                        String tickString = "Tick";
                        int stringWidth = getStringWidth(g, tickString);
                        g.drawString(tickString, LEFT_MARGIN - stringWidth - 3, yPos + scale - (getStringHeight(g) / 2));
                        g.setFont(oldF);
                        g.setColor(config.boxColor());
                        g.drawRect(LEFT_MARGIN - (int) (scale * 1.5), yPos, (int) (scale * 1.5), scale);
                    }
                }
                g.setFont(oldFont);
            }
        }

    }

    private void drawRoomSpecificData(Graphics2D g)
    {
        if (!specific.isEmpty() || room.equals("Nylocas")) //todo make generic
        {
            if (room.equals("Nylocas"))
            {
                for (int i = startTick; i < endTick; i++)
                {
                    if (shouldTickBeDrawn(i))
                    {
                        int xOffset = getXOffset(i);
                        int yOffset = getYOffset(i);
                        yOffset += (playerOffsets.size() + 2) * scale;
                        g.setColor(config.fontColor());
                        String time = String.valueOf(((i + instanceTime) % 4) + 1);
                        int strWidth = getStringBounds(g, time).width;
                        if (yOffset - (scale / 2) > scale + 5 && xOffset > LEFT_MARGIN)
                        {
                            g.drawString(time, xOffset + (scale / 2) - (strWidth / 2), yOffset - (scale / 2));
                        }
                    }
                }
            } else
            {
                for (Integer i : specific.keySet())
                {
                    int xOffset = getXOffset(i);
                    int yOffset = getYOffset(i);
                    yOffset += (playerOffsets.size() + 2) * scale;
                    g.setColor(config.fontColor());
                    int strWidth = getStringBounds(g, specific.get(i)).width;
                    if (yOffset > scale + 5 && xOffset > LEFT_MARGIN)
                    {
                        g.drawString(specific.get(i), xOffset + (scale / 2) - (strWidth / 2), yOffset - (scale / 2) + (fontHeight / 2));
                    }
                }
            }
        }
    }

    private void drawDawnSpecs(Graphics2D g)
    {
        for (DawnSpec dawnSpec : dawnSpecs)
        {
            String damage = String.valueOf(dawnSpec.getDamage());
            if (dawnSpec.getDamage() != -1)
            {
                int xOffset = getXOffset(dawnSpec.tick - 2);
                int yOffset = getYOffset(dawnSpec.tick);
                yOffset += (playerOffsets.size() + 1) * scale;
                g.setColor(config.fontColor());
                int textOffset = (scale / 2) - (getStringBounds(g, damage).width) / 2;
                if (yOffset > scale + 5 && xOffset+textOffset > LEFT_MARGIN)
                {
                    g.drawString(damage, xOffset + textOffset, yOffset + (scale / 2) - (fontHeight / 2));
                }
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
        synchronized (outlineBoxes)
        {
            for (OutlineBox box : outlineBoxes)
            {
                if (shouldTickBeDrawn(box.tick))
                {
                    int xOffset = getXOffset(box.tick);
                    if (playerOffsets.get(box.player) == null)
                    {
                        continue;
                    }
                    int yOffset = ((playerOffsets.get(box.player) + 1) * scale) + getYOffset(box.tick);
                    if (yOffset > scale + 5 && xOffset > LEFT_MARGIN)
                    {
                        if (config != null && config.useIconsOnChart())
                        {
                            try
                            {
                                if (box.playerAnimation.attackTicks != -1)
                                {
                                    int opacity = config.iconBackgroundOpacity();
                                    opacity = Math.min(255, opacity);
                                    opacity = Math.max(0, opacity);
                                    if (config.attackBoxColor().equals(Color.WHITE))
                                    {
                                        g.setColor(new Color(box.color.getRed(), box.color.getGreen(), box.color.getBlue(), opacity));
                                    } else
                                    {
                                        g.setColor(config.attackBoxColor());
                                    }
                                    fillBoxStyleAccordingToConfig(g, xOffset + 2, yOffset + 2, scale - 3, scale - 3, 5, 5);
                                    BufferedImage scaled = getScaledImage(box.attack.img, (scale - 2), (scale - 2));
                                    if (box.playerAnimation.equals(PlayerAnimation.HAMMER_BOP) || box.playerAnimation.equals(PlayerAnimation.BGS_WHACK) || box.playerAnimation.equals(PlayerAnimation.UNCHARGED_SCYTHE) || box.playerAnimation.equals(PlayerAnimation.KODAI_BOP))
                                    {
                                        g.drawImage(createFlipped(createDropShadow(scaled)), xOffset + 3, yOffset + 3, null);
                                        g.drawImage(createFlipped(scaled), xOffset + 2, yOffset + 1, null);
                                    } else
                                    {
                                        g.drawImage(createDropShadow(scaled), xOffset + 3, yOffset + 3, null);
                                        g.drawImage(scaled, xOffset + 2, yOffset + 1, null);
                                    }
                                    if (!box.additionalText.isEmpty())
                                    {
                                        int textOffset;
                                        Font f = g.getFont();
                                        g.setFont(f.deriveFont(9.0f));
                                        textOffset = (scale / 2) - (getStringWidth(g, box.additionalText) / 2);
                                        g.setColor(config.fontColor());
                                        g.drawString(box.additionalText, xOffset + textOffset, yOffset + scale - 3);
                                        g.setFont(f);
                                    }
                                }
                            } catch (Exception e)
                            {

                            }
                        } else
                        {
                            int opacity = 100;
                            if (config != null)
                            {
                                opacity = config.letterBackgroundOpacity();
                                opacity = Math.min(255, opacity);
                                opacity = Math.max(0, opacity);
                            }
                            g.setColor(new Color(box.color.getRed(), box.color.getGreen(), box.color.getBlue(), opacity));
                            fillBoxStyleAccordingToConfig(g, xOffset + 2, yOffset + 2, scale - 3, scale - 3, 5, 5);
                            g.setColor((box.primaryTarget) ? config.fontColor() : new Color(0, 190, 255));
                            int textOffset = (scale / 2) - (getStringWidth(g, box.letter) / ((config.rightAlignTicks()) ? 4 : 2));
                            int primaryOffset = yOffset + (box.additionalText.isEmpty() ? (fontHeight / 2) : 0);
                            g.drawString(box.letter, xOffset + textOffset - 1, primaryOffset + (scale / 2) + 1);
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
                        drawBoxStyleAccordingToConfig(g, xOffset + 1, yOffset + 1, scale - 2, scale - 2, 5, 5);
                    }
                }
            }
        }
    }

    private void drawMarkerLines(Graphics2D g)
    {
        if (!live || finished)
        {
            for (Integer i : lines.keySet())
            {
                if (shouldTickBeDrawn(i))
                {
                    int xOffset = getXOffset(i);
                    int yOffset = getYOffset(i);
                    g.setColor(config.markerColor());
                    if (linePlacementModeActive && selectedTick == i)
                    {
                        g.setColor(new Color(40, 140, 235));
                    }
                    g.drawLine(xOffset, yOffset + (scale / 2), xOffset, yOffset + boxHeight - scale);
                    int stringLength = getStringBounds(g, lines.get(i)).width;
                    g.setColor(config.fontColor());
                    if (yOffset + (scale / 2) > scale + 5 && xOffset-(stringLength/2) > LEFT_MARGIN)
                    {
                        g.drawString(lines.get(i), xOffset - (stringLength / 2), yOffset + (scale / 2)); //todo
                    }
                }
            }
        }
    }

    private void drawThrallBoxes(Graphics2D g)
    {
        synchronized (thrallOutlineBoxes)
        {
            for (ThrallOutlineBox box : thrallOutlineBoxes)
            {
                g.setColor(new Color(box.getColor().getRed(), box.getColor().getGreen(), box.getColor().getBlue(), 30));

                int maxTick = getMaxTick(box.owner, box.spawnTick);
                int lastEndTick = box.spawnTick;
                while (lastEndTick < maxTick && shouldTickBeDrawn(lastEndTick))
                {
                    int yOffset = getYOffset(lastEndTick);
                    try
                    {
                        yOffset += (playerOffsets.get(box.owner) + 1) * scale;
                    } catch (Exception e)
                    {
                        break;
                    }
                    int currentEndTick = (shouldWrap) ? lastEndTick + (ticksToShow - (lastEndTick % ticksToShow) + (startTick % ticksToShow)) : maxTick;
                    if (currentEndTick > maxTick)
                    {
                        currentEndTick = maxTick;
                    }
                    int xOffsetStart = getXOffset(lastEndTick);
                    int xOffsetEnd = getXOffset(currentEndTick - 1);
                    lastEndTick = currentEndTick;
                    if (yOffset > scale + 5 && xOffsetStart > 100)
                    {
                        g.fillRect(xOffsetStart, yOffset + 1, xOffsetEnd - xOffsetStart + scale, scale - 2);
                    }
                }
            }
        }
    }

    /**
     * Finds the highest tick that doesn't overlap if they summoned a thrall in the future before this one would naturally expire
     *
     * @param owner     player who summoned this thrall
     * @param startTick tick the thrall was summoned
     * @return
     */
    private int getMaxTick(String owner, int startTick)
    {
        int maxTick = startTick + 99; //todo fix assumption that thralls always last 99 tick
        synchronized (thrallOutlineBoxes)
        {
            for (ThrallOutlineBox boxCompare : thrallOutlineBoxes)
            {
                if (owner.equalsIgnoreCase(boxCompare.owner))
                {
                    if (boxCompare.spawnTick > startTick && boxCompare.spawnTick < (startTick + 99))
                    {
                        maxTick = boxCompare.spawnTick;
                    }
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
            g.setColor(config.fontColor());
            if (enforceCD)
            {
                if (playerWasOnCD.containsEntry(selectedPlayer, selectedTick))
                {
                    g.setColor(Color.RED);
                }
            }
            int xOffset = getXOffset(selectedTick);
            int yOffset = ((playerOffsets.get(selectedPlayer) + 1) * scale) + getYOffset(selectedTick);
            if (yOffset > scale + 5 && xOffset > LEFT_MARGIN)
            {
                g.drawRect(xOffset, yOffset, scale, scale);
            }
        }
    }

    private Point getPoint(int tick, String player)
    {
        return new Point(getXOffset(tick), ((playerOffsets.get(player) + 1) * scale) + getYOffset(tick));
    }

    private int getAdditionalRow()
    {
        return (!specific.isEmpty() || room.equals("Nylocas")) ? 1 : 0;
    }

    private void drawSelectedRow(Graphics2D g)
    {
        if (selectedRow != -1 && shouldTickBeDrawn(selectedRow))
        {
            g.setColor(config.fontColor());
            int xOffset = getXOffset(selectedRow);
            int yOffset = getYOffset(selectedRow);
            int additionalRows = 1 + getAdditionalRow();
            g.drawRect(xOffset, yOffset, scale, scale * (attackers.size() + additionalRows));

            int selectedTickHP = -1;
            try
            {
                selectedTickHP = roomHP.get(selectedRow + 1);
            } catch (Exception ignored)
            {

            }
            int offset = -1;
            switch (room) //todo map?
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
            int xPosition = xOffset + scale;
            if (xPosition + hoverBox.getWidth(g) > windowWidth)
            {
                xPosition = xPosition - hoverBox.getWidth(g) - 3 * scale;
            }
            int yPosition = yOffset;
            if (yPosition + hoverBox.getHeight(g) > windowHeight)
            {
                yPosition = yPosition - hoverBox.getHeight(g) - 3 * scale;
            }
            hoverBox.setPosition(xPosition, yPosition);
            hoverBox.draw(g);
        }
    }


    private void drawHoverBox(Graphics2D g)
    {
        synchronized (actions.keySet())
        {
            for (PlayerDidAttack action : actions.keySet())
            {
                if (action.tick == selectedTick && action.player.equals(selectedPlayer) && shouldTickBeDrawn(action.tick))
                {
                    Point location = getPoint(action.tick, action.player);
                    HoverBox hoverBox = new HoverBox(actions.get(action) + ": " + action.animation, config);
                    hoverBox.addString("");
                    for (String item : action.wornItemNames)
                    {
                        hoverBox.addString("." + item);
                    }
                    int xPosition = location.getX() + 10;
                    if (xPosition + hoverBox.getWidth(g) > windowWidth) //box would render off screen
                    {
                        xPosition = xPosition - hoverBox.getWidth(g) - scale * 2; //render to the left side of the selected action
                    }
                    int yPosition = location.getY() - 10;
                    if (yPosition + hoverBox.getHeight(g) > (windowHeight - 2 * TITLE_BAR_PLUS_TAB_HEIGHT)) //box would render off screen
                    {
                        yPosition -= (yPosition + hoverBox.getHeight(g) - (windowHeight - TITLE_BAR_PLUS_TAB_HEIGHT - 10)); //render bottom aligned to window+10
                    }
                    hoverBox.setPosition(xPosition, yPosition);
                    hoverBox.draw(g);
                }
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
                if (lines.get(tick).equals("Dead"))
                {
                    continue;
                }
                String proc = lines.get(tick);
                int xOffset = getXOffset(tick + 1);
                int yOffset = 10 + getYOffset(tick + 1);
                if (yOffset <= scale + 5)
                {
                    continue;
                }
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
                        xOffset = getXOffset(tick + 1);
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
                        if (crab.startsWith("s"))
                        {
                            g.setColor(new Color(220, 200, 0, 200));
                        } else
                        {
                            g.setColor(new Color(230, 20, 20, 200));
                        }
                        g.fillOval(xOffset + crabOffsetX, yOffset + crabOffsetY, 7, 7);
                        g.setColor(new Color(230, 20, 20, 200));
                    }
                }
            }
        }
    }

    private void drawRoomTime(Graphics2D g)
    {
        Font oldFont = g.getFont();
        g.setColor(config.fontColor());
        setConfigFont(g);
        g.drawString("Time " + RoomUtil.time(endTick-startTick), 5, 20);
        g.setFont(oldFont);
    }

    private void drawCheckBox(Graphics2D g)
    {
        Font oldFont = g.getFont();
        g.setColor(config.fontColor());
        setConfigFont(g);
        g.drawString("Use Icons? ", 100, 20);
        if (!checkBoxHovered)
        {
            g.setColor(config.boxColor());
        }

        g.drawRect(180, 2, 20, 20);
        if (checkBoxHovered)
        {
            g.setColor(config.boxColor().brighter().brighter().brighter()); //todo hmmm
            g.fillRect(181, 3, 19, 19);
        }
        g.setColor(config.fontColor());
        if (config.useIconsOnChart())
        {
            g.drawString("x", 186, 17);
        }
        g.setFont(oldFont);
    }

    private void drawCheckBox2(Graphics2D g)
    {
        Font oldFont = g.getFont();
        g.setColor(config.fontColor());
        setConfigFont(g);
        g.drawString("Use Time? ", 210, 20);
        if (!checkBox2Hovered)
        {
            g.setColor(config.boxColor());
        }

        g.drawRect(290, 2, 20, 20);
        if (checkBox2Hovered)
        {
            g.setColor(config.boxColor().brighter().brighter().brighter()); //todo hmmm
            g.fillRect(291, 3, 19, 19);
        }
        g.setColor(config.fontColor());
        if (config.useTimeOnChart())
        {
            g.drawString("x", 296, 17);
        }
        g.setFont(oldFont);
    }

    private void drawCheckBox3(Graphics2D g)
    {
        Font oldFont = g.getFont();
        g.setColor(config.fontColor());
        setConfigFont(g);
        g.drawString("Show Config? ", 320, 20);
        if (!checkBox3Hovered)
        {
            g.setColor(config.boxColor());
        }

        g.drawRect(410, 2, 20, 20);
        if (checkBox3Hovered)
        {
            g.setColor(config.boxColor().brighter().brighter().brighter()); //todo hmmm
            g.fillRect(411, 3, 19, 19);
        }
        g.setColor(config.fontColor());
        if (config.showConfigOnChart())
        {
            g.drawString("x", 416, 17);
        }
        g.setFont(oldFont);
    }

    private void drawBaseBoxes(Graphics2D g)
    {
        for (int i = startTick; i < endTick; i++)
        {
            if (shouldTickBeDrawn(i))
            {
                for (int j = 0; j < playerOffsets.size(); j++)
                {
                    int xOffset = getXOffset(i);
                    if (playerOffsets.get(attackers.get(j)) == null)
                    {
                        continue;
                    }
                    shouldWrap = true;
                    int yOffset = ((playerOffsets.get(attackers.get(j)) + 1) * scale) + getYOffset(i);
                    g.setColor(config.primaryMiddle());
                    if (!playerWasOnCD.get(attackers.get(j)).contains(i))
                    {
                        g.setColor(config.idleColor());
                    }
                    if (yOffset > scale + 5 && xOffset > LEFT_MARGIN)
                    {
                        fillBoxStyleAccordingToConfig(g, xOffset + 2, yOffset + 2, scale - 3, scale - 3, 5, 5);
                    }
                }
            }
        }
    }

    @Setter
    private String manualLineText = "";

    private final Multimap<String, Integer> playerWasOnCD = ArrayListMultimap.create();

    public boolean shouldTickBeDrawn(int tick) //is tick visible, > start tick, < end tick
    {
        return tick >= (startTick + currentBox * ticksToShow) && tick < (startTick + ((currentBox + boxesToShow) * ticksToShow)) && tick >= startTick && tick <= endTick;
    }

    private void drawLinePlacement(Graphics2D g) //chart creator
    {
        if (shouldTickBeDrawn(selectedTick) && !lines.containsKey(selectedTick))
        {
            int xOffset = getXOffset(selectedTick);
            int yOffset = getYOffset(selectedTick);
            g.setColor(config.markerColor());
            g.drawLine(xOffset, yOffset + (scale / 2), xOffset, yOffset + boxHeight - scale);
        }
    }

    private synchronized void drawGraph()
    {
        if (!shouldDraw())
        {
            return;
        }
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        RenderingHints qualityHints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(qualityHints);
        Color oldColor = g.getColor();

        g.setColor(config.primaryDark());
        g.fillRect(0, 0, img.getWidth(), img.getHeight());

        fontHeight = getStringBounds(g, "a").height; //todo is "a" really the best option here?

        drawTicks(g);
        drawGraphBoxes(g);
        drawBaseBoxes(g);
        drawYChartColumn(g);
        drawRoomSpecificData(g);
        drawDawnSpecs(g);
        drawThrallBoxes(g);
        drawPrimaryBoxes(g);
        drawAutos(g);
        drawMarkerLines(g);
        drawMaidenCrabs(g);

        if (!linePlacementModeActive)
        {
            drawSelectedOutlineBox(g);
            drawSelectedRow(g);
        }

        drawHoverBox(g);
        drawRoomTime(g);

        if (config.showConfigOnChart())
        {
            drawCheckBox(g);
            drawCheckBox2(g);
            drawCheckBox3(g);
        }

        if (linePlacementModeActive)
        {
            drawLinePlacement(g);
        }

        g.setColor(oldColor);
        g.dispose();
        repaint();
    }

    public void setAttackers(List<String> attackers)
    {
        this.attackers.clear();
        this.attackers.addAll(attackers); //don't do this.attackers = because the reference changes on plugin end
        playerOffsets.clear();
        recalculateSize();
    }

    public void getTickHovered(int x, int y)
    {
        if (boxHeight > 0 && scale > 0)
        {
            y = y + currentScrollOffsetY;
            x = x + (currentScrollOffsetX%scale);
            if (y > 20) //todo why do I use 20 here when TOP_MARGIN is 30?
            {
                int boxNumber = (y - 20) / boxHeight;
                if (x > LEFT_MARGIN)
                {
                    int tick = startTick + (ticksToShow * boxNumber + ((x - LEFT_MARGIN) / scale));
                    int playerOffsetPosition = (((y - TOP_MARGIN - scale) % boxHeight) / scale);
                    if (playerOffsetPosition >= 0 && playerOffsetPosition < attackers.size() && (y - TOP_MARGIN - scale > 0))
                    {
                        selectedTick = tick;
                        selectedPlayer = attackers.get(playerOffsetPosition);
                        selectedRow = -1;
                    } else if (y % boxHeight < TOP_MARGIN + scale)
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
    }

    private int currentBox = 0;
    private int currentScrollOffsetY = 0;
    private int currentScrollOffsetX = 0;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) //manually implement scrolling
    {
        if(!isCtrlPressed())
        {
            if (e.getWheelRotation() < 0) //top of the first box aligns to top if you scroll up
            {
                currentBox = Math.max(0, currentBox - 1);
                currentScrollOffsetY = currentBox * boxHeight;
            } else //bottom of the bottom box aligns to the bottom if you scroll down
            {
                if (TITLE_BAR_PLUS_TAB_HEIGHT + scale + boxCount * boxHeight > windowHeight) //no need to scroll at all if all boxes fit on screen, boxes would jump to bottom and leave dead space
                {
                    int lastBox = currentBox + boxesToShow - 1;
                    lastBox = Math.min(lastBox + 1, boxCount - 1);
                    currentBox = lastBox - boxesToShow + 1;
                    int lastBoxEnd = (boxesToShow * boxHeight) + scale + TITLE_BAR_PLUS_TAB_HEIGHT;
                    currentScrollOffsetY = (currentBox * boxHeight) + (lastBoxEnd - windowHeight);
                }
            }
        }
        else
        {
            if(e.getWheelRotation() < 0)
            {
                if(startTick > 0)
                {
                    startTick--;
                    endTick--;
                }
            }
            else
            {
                startTick++;
                endTick++;
            }
        }
        recalculateSize();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        checkRelease(e);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    private boolean isDragging = false;

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (isDragging) //prevent checkRelease from being double called by both clicked and released
        {
            checkRelease(e);
            isDragging = false;
        }
        if(SwingUtilities.isRightMouseButton(e))
        {
            currentScrollOffsetX = 0;
            currentScrollOffsetY = 0;
            startTick = baseStartTick;
            endTick = baseEndTick;
        }
    }

    private void checkRelease(MouseEvent e)
    {
        if (checkBoxHovered)
        {
            configManager.setConfiguration("Advanced Raid Tracker", "useIconsOnChart", !config.useIconsOnChart());
            drawGraph();
        } else if (checkBox2Hovered)
        {
            configManager.setConfiguration("Advanced Raid Tracker", "useTimeOnChart", !config.useTimeOnChart());
            drawGraph();
        } else if (checkBox3Hovered)
        {
            configManager.setConfiguration("Advanced Raid Tracker", "showConfigOnChart", !config.showConfigOnChart());
            drawGraph();
        } else
        {
            if (linePlacementModeActive)
            {
                if (selectedTick != -1)
                {
                    if (SwingUtilities.isLeftMouseButton(e))
                    {
                        addLine(selectedTick, manualLineText);
                    } else if (SwingUtilities.isRightMouseButton(e))
                    {
                        lines.remove(selectedTick);
                    }
                }
                return;
            }
            if (SwingUtilities.isLeftMouseButton(e))
            {
                if (selectedTick != -1 && !selectedPrimary.equals(PlayerAnimation.NOT_SET))
                {
                    int weapon = 0;
                    if (selectedPrimary.weaponIDs.length > 0)
                    {
                        weapon = selectedPrimary.weaponIDs[0];
                    }
                    addAttack(new PlayerDidAttack(itemManager, selectedPlayer, "", selectedTick, weapon, "", "", 0, 0, "", ""), selectedPrimary);
                } else if (selectedPrimary.equals(PlayerAnimation.NOT_SET))
                {
                    removeAttack(selectedTick, selectedPlayer);
                }
            } else if (SwingUtilities.isRightMouseButton(e))
            {
                if (selectedTick != -1 && !selectedSecondary.equals(PlayerAnimation.NOT_SET))
                {
                    int weapon = 0;
                    if (selectedSecondary.weaponIDs.length > 0)
                    {
                        weapon = selectedSecondary.weaponIDs[0];
                    }
                    addAttack(new PlayerDidAttack(itemManager, selectedPlayer, "", selectedTick, weapon, "", "", 0, 0, "", ""), selectedSecondary);
                } else if (selectedSecondary.equals(PlayerAnimation.NOT_SET))
                {
                    removeAttack(selectedTick, selectedPlayer);
                }
            }
        }
    }

    private void removeAttack(int tick, String player)
    {
        if (tick != -1 && room.equals("Creator")) //don't allow attacks to be removed if this chart panel isn't part of the chart creator
        {
            synchronized (outlineBoxes)
            {
                List<OutlineBox> removedBoxes = new ArrayList<>();
                for (OutlineBox box : outlineBoxes)
                {
                    if (box.tick == tick && Objects.equals(box.player, player))
                    {
                        removedBoxes.add(box);
                    }
                }
                outlineBoxes.removeAll(removedBoxes);
                for (OutlineBox removedBox : removedBoxes)
                {
                    for (int i = tick; i < tick + removedBox.cd; i++)
                    {
                        playerWasOnCD.remove(player, i);
                    }
                }
            }
        }
        drawGraph();
    }

    boolean linePlacementModeActive = false;

    public void setToolSelection(int tool)
    {
        linePlacementModeActive = tool == 1;
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

    int dragStartX = 0;
    int dragStartY = 0;
    int lastScrollOffsetX = 0;
    int lastScrollOffsetY = 0;
    int lastStartTick = 0;
    int lastEndTick = 0;
    @Override
    public void mouseDragged(MouseEvent e)
    {
        if(!isDragging)
        {
            dragStartX = e.getX();
            dragStartY = e.getY();
            lastScrollOffsetX = 0;
            lastScrollOffsetY = currentScrollOffsetY;
            lastStartTick = startTick;
            lastEndTick = endTick;
        }
        else
        {
            currentScrollOffsetY = lastScrollOffsetY + (dragStartY - e.getY());
            currentScrollOffsetX = lastScrollOffsetX + (dragStartX - e.getX());
            if(lastStartTick+(currentScrollOffsetX/scale) > 0)
            {
                startTick = lastStartTick + (currentScrollOffsetX / scale);
                endTick = lastEndTick + (currentScrollOffsetX/scale);
            }
            drawGraph();
        }
        isDragging = true;
        if(SwingUtilities.isMiddleMouseButton(e))
        {
            log.info(e.getX() +", " + e.getY());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        checkBoxHovered = e.getX() >= 180 && e.getX() <= 200 && e.getY() >= 2 && e.getY() <= 22;
        checkBox2Hovered = e.getX() >= 290 && e.getX() <= 310 && e.getY() >= 2 && e.getY() <= 22;
        checkBox3Hovered = e.getX() >= 410 && e.getX() <= 430 && e.getY() >= 2 && e.getY() <= 22;
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

    public void setEnforceCD(boolean bool)
    {
        enforceCD = bool;
        redraw();
    }
}