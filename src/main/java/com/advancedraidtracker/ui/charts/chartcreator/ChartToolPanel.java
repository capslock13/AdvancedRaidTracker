package com.advancedraidtracker.ui.charts.chartcreator;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.charts.ChartPanel;
import com.advancedraidtracker.ui.charts.ChartTick;
import com.advancedraidtracker.utility.weapons.AnimationDecider;
import com.advancedraidtracker.utility.weapons.PlayerAnimation;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.advancedraidtracker.ui.charts.ChartConstants.*;
import static com.advancedraidtracker.ui.charts.ChartPanel.createDropShadow;
import static com.advancedraidtracker.ui.charts.ChartPanel.isCtrlPressed;
import static com.advancedraidtracker.utility.UISwingUtility.*;
import static com.advancedraidtracker.utility.UISwingUtility.createFlipped;
import static com.advancedraidtracker.utility.wrappers.OutlineBox.getReplacement;
import static com.advancedraidtracker.utility.wrappers.OutlineBox.getSpellIcon;


@Slf4j
public class ChartToolPanel extends JPanel implements MouseListener, MouseMotionListener
{
    private BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    private final AdvancedRaidTrackerConfig config;
    private final ChartCreatorFrame parentFrame;
    private PlayerAnimation primary = PlayerAnimation.SCYTHE;
    private PlayerAnimation secondary = PlayerAnimation.NOT_SET;
    private PlayerAnimation hoveredAttack = PlayerAnimation.EXCLUDED_ANIMATION;

    int tool = 0;
    int hoveredTool = NO_TOOL;
    int toolMargin = 10;
    int xMargin = 5;
    int yMargin = 15;
    int initialXMargin = 5;
    int initialYMargin;
    int toolsPerColumn;
    private ItemManager itemManager;

    Map<PlayerAnimation, BufferedImage> iconMap = new HashMap<>();
    private final Set<PlayerAnimation> excludedAnimations = new HashSet<>(Arrays.asList(PlayerAnimation.UNARMED, PlayerAnimation.UNDECIDED, PlayerAnimation.EXCLUDED_ANIMATION, PlayerAnimation.ACCURSED_SCEPTRE_AUTO, PlayerAnimation.ACCURSED_SCEPTRE_SPEC, PlayerAnimation.COLOSSEUM_AUTO, PlayerAnimation.COLOSSEUM_SPECIAL, PlayerAnimation.KERIS_SUN_SPEC));

    public ChartToolPanel(AdvancedRaidTrackerConfig config, ChartCreatorFrame parentFrame, ItemManager itemManager, ClientThread clientThread, SpriteManager spriteManager)
    {
        this.itemManager = itemManager;
        this.parentFrame = parentFrame;
        this.config = config;
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(config.primaryDark());
        setOpaque(true);
        clientThread.invoke(()->
        {
            for(PlayerAnimation playerAnimation : PlayerAnimation.values())
            {
                if (playerAnimation.attackTicks > 0)
                {
                    int weaponID = 0;
                    if(playerAnimation.weaponIDs.length > 0)
                    {
                        weaponID = playerAnimation.weaponIDs[0];
                    }
                    if (config.useUnkitted())
                    {
                        weaponID = getReplacement(weaponID);
                    }
                    iconMap.put(playerAnimation, itemManager.getImage(weaponID, 1, false));
                } else
                {
                    try
                    {
                        int animation = 0;
                        if(playerAnimation.animations.length > 0)
                        {
                            animation = playerAnimation.animations[0];
                        }
                        iconMap.put(playerAnimation, spriteManager.getSprite(getSpellIcon(animation), 0));
                    }
                    catch (Exception e)
                    {

                    }
                }
            }
        });
        try
        {
            Thread.sleep(20); //wait for icons to populate
        }
        catch (Exception e)
        {

        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e ->
        {
            synchronized (ChartPanel.class)
            {
                if (e.getID() == KeyEvent.KEY_PRESSED)
                {
                    if(!isCtrlPressed())
                    {
                        switch (e.getKeyCode())
                        {
                            case KeyEvent.VK_V:
                                tool = SELECTION_TOOL;
                                parentFrame.setToolSelection(SELECTION_TOOL);
                                drawPanel();
                                break;
                            case KeyEvent.VK_A:
                                tool = ADD_ATTACK_TOOL;
                                parentFrame.setToolSelection(ADD_ATTACK_TOOL);
                                drawPanel();
                                break;
                            case KeyEvent.VK_L:
                                tool = ADD_LINE_TOOL;
                                parentFrame.setToolSelection(ADD_LINE_TOOL);
                                drawPanel();
                                break;
                        }
                    }
                }
                return false;
            }
        });
    }

    public void build()
    {
        img = new BufferedImage(400, getHeight(), BufferedImage.TYPE_INT_ARGB);
        drawPanel();
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

    private void drawPanel()
    {
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

        g.setColor(config.primaryDark());
        g.fillRect(0, 0, img.getWidth(), img.getHeight());

        int toolCount = PlayerAnimation.values().length;
        int height = 550;
        int toolHeight = config.chartScaleSize();
        int xMargin = 5;
        int yMargin = 15;
        int totalToolHeight = toolCount * (toolHeight + toolMargin);
        initialYMargin = 15 + (toolHeight * 2) + (toolMargin / 2);
        int columns = (totalToolHeight / height) + 1;
        toolsPerColumn = (height) / (toolHeight + toolMargin);

        //draw active outline

        if (tool == ADD_ATTACK_TOOL || hoveredTool == ADD_ATTACK_TOOL)
        {
            if (hoveredTool == ADD_ATTACK_TOOL)
            {
                g.setColor(config.fontColor());
            } else
            {
                g.setColor(new Color(45, 140, 235)); //todo selection color
            }
            g.drawRect(xMargin + 1, yMargin + 1, (toolMargin) + (toolHeight * 2) + (toolHeight * 2), toolHeight * 2);
        }
        if (tool == ADD_LINE_TOOL || hoveredTool == ADD_LINE_TOOL)
        {
            if (hoveredTool == ADD_LINE_TOOL)
            {
                g.setColor(config.fontColor());
            } else
            {
                g.setColor(new Color(45, 140, 235));
            }
            g.drawRect(xMargin + 1 + (2 * toolMargin) + (toolHeight * 2) + (toolHeight * 2), yMargin + 1, 2 * toolHeight, toolHeight * 2);
        }
        if (tool == SELECTION_TOOL || hoveredTool == SELECTION_TOOL)
        {
            if (hoveredTool == SELECTION_TOOL)
            {
                g.setColor(config.fontColor());
            } else
            {
                g.setColor(new Color(45, 140, 235));
            }
            g.drawRect(xMargin + 1 + (3 * toolMargin) + (toolHeight * 2) + (toolHeight * 2) + (toolHeight * 2), yMargin + 1, 2 * toolHeight, toolHeight * 2);
        }

        //draw primary

        g.setColor(config.boxColor());
        g.drawRoundRect(xMargin + 3, yMargin + 3, toolHeight * 2 - 5, toolHeight * 2 - 5, 10, 10);
        int textOffset = (toolHeight) - (getStringWidth(g, primary.shorthand) / 2);
        g.setColor(config.fontColor());
        if(config.useIconsOnChart())
        {
            if(!primary.equals(PlayerAnimation.NOT_SET))
            {
                BufferedImage scaled = getScaledImage(iconMap.get(primary), (toolHeight * 2 - 4), (toolHeight * 2 - 4));
                if (primary.equals(PlayerAnimation.HAMMER_BOP) || primary.equals(PlayerAnimation.BGS_WHACK) || primary.equals(PlayerAnimation.UNCHARGED_SCYTHE) || primary.equals(PlayerAnimation.KODAI_BOP) || primary.equals(PlayerAnimation.CHALLY_WHACK))
                {
                    g.drawImage(createFlipped(createDropShadow(scaled)), xMargin + 3, yMargin + 3, null);
                    g.drawImage(createFlipped(scaled), xMargin + 2, yMargin + 1, null);
                } else
                {
                    g.drawImage(createDropShadow(scaled), xMargin + 3, yMargin + 3, null);
                    g.drawImage(scaled, xMargin + 2, yMargin + 1, null);
                }
            }
        }
        else
        {
            g.setColor(primary.color);
            g.fillRoundRect(xMargin + 4, yMargin + 4, toolHeight * 2 - 6, toolHeight * 2 - 6, 10, 10);
            g.drawString(primary.shorthand, xMargin + textOffset - 1, yMargin + (getStringHeight(g) / 2) + (toolHeight) + 1);
        }
        //draw secondary

        xMargin += toolMargin + toolHeight * 2;
        g.setColor(config.boxColor());
        g.drawRoundRect(xMargin + 3, yMargin + 3, toolHeight * 2 - 5, toolHeight * 2 - 5, 10, 10);
        textOffset = (toolHeight) - (getStringWidth(g, secondary.shorthand) / 2);
        g.setColor(config.fontColor());

        if(config.useIconsOnChart())
        {
            if(!secondary.equals(PlayerAnimation.NOT_SET))
            {
                BufferedImage scaled = getScaledImage(iconMap.get(secondary), (toolHeight * 2 - 2), (toolHeight * 2 - 2));
                if (secondary.equals(PlayerAnimation.HAMMER_BOP) || secondary.equals(PlayerAnimation.BGS_WHACK) || secondary.equals(PlayerAnimation.UNCHARGED_SCYTHE) || secondary.equals(PlayerAnimation.KODAI_BOP) || secondary.equals(PlayerAnimation.CHALLY_WHACK))
                {
                    g.drawImage(createFlipped(createDropShadow(scaled)), xMargin + 3, yMargin + 3, null);
                    g.drawImage(createFlipped(scaled), xMargin + 2, yMargin + 1, null);
                } else
                {
                    g.drawImage(createDropShadow(scaled), xMargin + 3, yMargin + 3, null);
                    g.drawImage(scaled, xMargin + 2, yMargin + 1, null);
                }
            }
        }
        else
        {
            g.setColor(secondary.color);
            g.fillRoundRect(xMargin + 4, yMargin + 4, toolHeight * 2 - 6, toolHeight * 2 - 6, 10, 10);
            g.drawString(secondary.shorthand, xMargin + textOffset - 1, yMargin + (getStringHeight(g) / 2) + (toolHeight) + 1);
        }
        //draw line tool

        xMargin += toolMargin + toolHeight * 2;
        g.setColor(config.markerColor());
        g.fillRoundRect(xMargin + 4, yMargin + 4, toolHeight * 2 - 6, toolHeight * 2 - 6, 10, 10);
        g.setColor(config.boxColor());
        g.drawRoundRect(xMargin + 3, yMargin + 3, toolHeight * 2 - 5, toolHeight * 2 - 5, 10, 10);
        textOffset = (toolHeight) - (getStringWidth(g, "Line") / 2);
        g.setColor(config.fontColor());
        g.drawString("Line", xMargin + textOffset - 1, yMargin + (getStringHeight(g) / 2) + (toolHeight) + 1);

        //draw selection tool

        xMargin += toolMargin + toolHeight*2;
        g.setColor(config.markerColor());
        g.fillRoundRect(xMargin +4, yMargin+4, toolHeight*2 - 6, toolHeight * 2- 6, 10, 10);
        g.setColor(config.boxColor());
        g.drawRoundRect(xMargin + 3, yMargin + 3, toolHeight * 2 - 5, toolHeight * 2 - 5, 10, 10);
        textOffset = (toolHeight) - (getStringWidth(g, "Select") / 2);
        g.setColor(config.fontColor());
        g.drawString("Select", xMargin + textOffset - 1, yMargin + (getStringHeight(g) / 2) + (toolHeight) + 1);

        //draw tools

        List<PlayerAnimation> filteredValues = Arrays.stream(PlayerAnimation.values()).filter(o->!excludedAnimations.contains(o)).collect(Collectors.toList());
        toolCount = filteredValues.size();
        for (int i = 0; i < toolCount; i++)
        {
            PlayerAnimation playerAnimation = filteredValues.get(i);
            if(excludedAnimations.contains(playerAnimation))
            {
                continue;
            }
            int positionInRow = i / toolsPerColumn;
            int positionInColumn = i % toolsPerColumn;
            int xOffset = initialXMargin + (positionInRow * (toolHeight + (toolMargin / 2)));
            int yOffset = initialYMargin + (positionInColumn * (toolHeight + (toolMargin / 2)));

            g.setColor(PlayerAnimation.values()[i].color);

            if(config.useIconsOnChart())
            {
                try
                {
                    if(!playerAnimation.equals(PlayerAnimation.NOT_SET))
                    {
                        BufferedImage scaled = getScaledImage(iconMap.get(playerAnimation), (toolHeight - 2), (toolHeight - 2));
                        if (playerAnimation.equals(PlayerAnimation.HAMMER_BOP) || playerAnimation.equals(PlayerAnimation.BGS_WHACK) || playerAnimation.equals(PlayerAnimation.UNCHARGED_SCYTHE) || playerAnimation.equals(PlayerAnimation.KODAI_BOP) || playerAnimation.equals(PlayerAnimation.CHALLY_WHACK))
                        {
                            g.drawImage(createFlipped(createDropShadow(scaled)), xOffset + 3, yOffset + 3, null);
                            g.drawImage(createFlipped(scaled), xOffset + 2, yOffset + 1, null);
                        } else
                        {
                            g.drawImage(createDropShadow(scaled), xOffset + 3, yOffset + 3, null);
                            g.drawImage(scaled, xOffset + 2, yOffset + 1, null);
                        }
                    }
                }
                catch (Exception e)
                {

                }
            }
            else
            {
                g.fillRoundRect(xOffset + 2, yOffset + 2, toolHeight - 3, toolHeight - 3, 5, 5);

                g.setColor(config.fontColor());
                String letter = playerAnimation.shorthand;
                textOffset = (toolHeight / 2) - (getStringWidth(g, letter) / 2);
                g.drawString(letter, xOffset + textOffset - 1, yOffset + (getStringHeight(g) / 2) + (toolHeight / 2) + 1);
            }

            //draw hovered tool outline box
            if (!(playerAnimation.equals(PlayerAnimation.EXCLUDED_ANIMATION)) && playerAnimation.equals(hoveredAttack))
            {
                g.setColor(config.fontColor());
                g.drawRoundRect(xOffset + 1, yOffset + 1, toolHeight - 2, toolHeight - 2, 5, 5);
            }

        }
        repaint();
    }

    private void setHoveredTool(int x, int y)
    {
        if (y > 16 && y < (16 + (config.chartScaleSize() * 2)))
        {
            if (x > 6 && x < (6 + toolMargin + (4 * config.chartScaleSize())))
            {
                hoveredTool = ADD_ATTACK_TOOL;
            } else if (x > (6 + (4 * config.chartScaleSize()) + (2 * toolMargin)) && x < (6 + (6 * config.chartScaleSize()) + (2 * toolMargin)))
            {
                hoveredTool = ADD_LINE_TOOL;
            }
            else if(x > (6 + (6 * config.chartScaleSize()) + (3*toolMargin)) && x < (6 + (8*config.chartScaleSize()) + (3*toolMargin)))
            {
                hoveredTool = SELECTION_TOOL;
            }
            else
            {
                hoveredTool = NO_TOOL;
            }
        } else
        {
            hoveredTool = NO_TOOL;
        }
    }

    private void setHoveredAttack(int x, int y)
    {
        int gap = config.chartScaleSize() + (toolMargin / 2);
        if (x > 5 && y > 15 + (config.chartScaleSize() * 2) + (toolMargin / 2))
        {
            int positionInRow = (x - initialXMargin) / gap;
            int positionInColumn = (y - initialYMargin) / gap;
            int index = positionInRow * toolsPerColumn + positionInColumn;
            List<PlayerAnimation> filteredValues = Arrays.stream(PlayerAnimation.values()).filter(o->!excludedAnimations.contains(o)).collect(Collectors.toList());

            if (index >= 0 && index < filteredValues.size())
            {
                hoveredAttack = filteredValues.get(index);
            }
        } else
        {
            hoveredAttack = PlayerAnimation.EXCLUDED_ANIMATION;
        }
    }

    private void handleRelease(MouseEvent e)
    {
        if (hoveredAttack.equals(PlayerAnimation.EXCLUDED_ANIMATION))
        {
            if (SwingUtilities.isLeftMouseButton(e))
            {
                if (tool == ADD_ATTACK_TOOL || tool == ADD_LINE_TOOL || tool == SELECTION_TOOL)
                {
                    tool = hoveredTool;
                    parentFrame.setToolSelection(hoveredTool);
                    drawPanel();
                }
            }
            return;
        }
        if (SwingUtilities.isLeftMouseButton(e))
        {
            primary = hoveredAttack;
            parentFrame.setPrimaryTool(primary);
        } else if (SwingUtilities.isRightMouseButton(e))
        {
            secondary = hoveredAttack;
            parentFrame.setSecondaryTool(secondary);
        }
        tool = ADD_ATTACK_TOOL;
        parentFrame.setToolSelection(ADD_ATTACK_TOOL);
        drawPanel();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        handleRelease(e);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        handleRelease(e);
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
        setHoveredAttack(e.getX(), e.getY());
        setHoveredTool(e.getX(), e.getY());
        drawPanel();
    }
}
