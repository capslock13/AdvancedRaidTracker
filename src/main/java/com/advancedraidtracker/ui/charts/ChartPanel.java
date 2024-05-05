package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.TobIDs;
import static com.advancedraidtracker.constants.TobIDs.MAGE_THRALL;
import static com.advancedraidtracker.ui.charts.ChartActionType.ADD_ELEMENT;
import static com.advancedraidtracker.ui.charts.ChartActionType.REMOVE_ELEMENT;
import static com.advancedraidtracker.ui.charts.ChartObjectType.ATTACK;
import static com.advancedraidtracker.ui.charts.ChartObjectType.AUTO;
import static com.advancedraidtracker.ui.charts.ChartObjectType.LINE;
import static com.advancedraidtracker.ui.charts.ChartObjectType.TEXT;
import static com.advancedraidtracker.ui.charts.ChartObjectType.THRALL;
import com.advancedraidtracker.ui.charts.chartcreator.ChartStatusBar;
import com.advancedraidtracker.ui.charts.chartelements.ChartAuto;
import com.advancedraidtracker.ui.charts.chartelements.ChartLine;
import com.advancedraidtracker.ui.charts.chartelements.ChartTextBox;
import com.advancedraidtracker.ui.charts.chartelements.OutlineBox;
import com.advancedraidtracker.ui.charts.chartelements.ThrallOutlineBox;
import com.advancedraidtracker.utility.*;
import com.advancedraidtracker.utility.Point;
import com.advancedraidtracker.utility.weapons.PlayerAnimation;
import com.advancedraidtracker.utility.weapons.AnimationDecider;
import com.advancedraidtracker.utility.wrappers.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.beans.PropertyChangeListener;
import java.util.stream.Collectors;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;

import static com.advancedraidtracker.ui.charts.ChartConstants.*;
import static com.advancedraidtracker.ui.charts.ChartIO.loadChartFromFile;
import static com.advancedraidtracker.ui.charts.ChartIO.saveChart;
import static com.advancedraidtracker.ui.charts.chartelements.OutlineBox.getSpellSpecificIcon;
import static com.advancedraidtracker.utility.UISwingUtility.*;
import static com.advancedraidtracker.utility.datautility.DataWriter.PLUGIN_DIRECTORY;
import static com.advancedraidtracker.ui.charts.chartelements.OutlineBox.getIcon;
import static com.advancedraidtracker.utility.weapons.PlayerAnimation.*;
@Slf4j
public class ChartPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, FocusListener, KeyEventDispatcher
{
    @Setter
    private ChartStatusBar statusBar;
    private final int TITLE_BAR_PLUS_TAB_HEIGHT = 63;
    private boolean shouldWrap;
    private BufferedImage img;
	boolean changesSaved = false;

	String associatedFileName = "";
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

    int hoveredTick = -1;

    List<ChartTick> selectedTicks = new ArrayList<>();
    boolean selectionDragActive = false;

    String hoveredPlayer = "";

    int hoveredColumn = -1;
    boolean checkBoxHovered = false;
    boolean checkBox2Hovered = false;
    boolean checkBox3Hovered = false;

    public int startTick;
    public int endTick;

    List<String> attackers = new ArrayList<>();

    String room;
    @Setter
    String roomSpecificText = "";
    private int fontHeight;
    public boolean finished = false;
    private boolean enforceCD = false;
    private final boolean live;
    private final List<ChartAuto> autos = new ArrayList<>();
    private Map<Integer, String> NPCMap = new HashMap<>();
    private final List<DawnSpec> dawnSpecs = new ArrayList<>();
    private final List<ThrallOutlineBox> thrallOutlineBoxes = new ArrayList<>();
	@Getter
    private final List<OutlineBox> outlineBoxes = new ArrayList<>();
    private final Map<Integer, String> specific = new HashMap<>();
    @Getter
    private final List<ChartLine> lines = new ArrayList<>();
    private final List<String> crabDescriptions = new ArrayList<>();

    @Setter
    private Map<Integer, Integer> roomHP = new HashMap<>();
    Map<String, Integer> playerOffsets = new LinkedHashMap<>();
    private final Map<PlayerDidAttack, String> actions = new HashMap<>();

    private PlayerAnimation selectedPrimary = PlayerAnimation.NOT_SET;
    private PlayerAnimation selectedSecondary = PlayerAnimation.NOT_SET;

    private ConfigManager configManager;
    @Getter
    private boolean isActive = false;
	List<ChartListener> listeners = new ArrayList<>();

	public void addChartListener(ChartListener listener)
	{
		listeners.add(listener);
	}

	public void removeChartListener(ChartListener listener)
	{
		listeners.remove(listener);
	}

	public void postChartChange(ChartChangedEvent event)
	{
		for(ChartListener listener : listeners)
		{
			listener.onChartChanged(event);
		}
	}

    public void setActive(boolean state)
    {
        isActive = state;
        if(!isActive)
        {
            img = null;
            removeMouseListener(this);
            removeMouseMotionListener(this);
            removeMouseWheelListener(this);
        }
        else
        {
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
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
            recalculateSize();
            boxesToShow = Math.min(1 + ((windowHeight - TITLE_BAR_PLUS_TAB_HEIGHT - scale) / boxHeight), boxCount);
            if (img != null)
            {
                img.flush();
            }
            img = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
            sendToBottom();
            drawGraph();
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
		ChartLine line = new ChartLine(lineInfo, tick);
        lines.add(line);
		postChartChange(new ChartChangedEvent(ADD_ELEMENT, LINE, line));
		changesSaved = false;
    }

    public void addLines(Map<Integer, String> lineData)
    {
		for(Integer tick : lineData.keySet())
		{
			addLine(tick, lineData.get(tick));
		}
    }

	public void addLines(List<ChartLine> lines)
	{
		for(ChartLine line : lines)
		{
			addLine(line.tick, line.text);
		}
	}

    public void addRoomHP(int tick, int hp)
    {
        roomHP.put(tick, hp);
    }

    public void addAuto(int autoTick)
    {
		ChartAuto auto = new ChartAuto(autoTick);
        autos.add(auto);
		postChartChange(new ChartChangedEvent(ADD_ELEMENT, AUTO, auto));
		changesSaved = false;
    }

    public void addAutos(List<Integer> autos)
    {
		for(Integer auto : autos)
		{
			addAuto(auto);
		}
    }

	public void addAutosFromExisting(List<ChartAuto> autos)
	{
		for(ChartAuto auto : autos)
		{
			addAuto(auto.tick);
		}
	}

	private ThrallOutlineBox hoveredThrallBox;

    public void addThrallBox(ThrallOutlineBox thrallOutlineBox)
    {
        synchronized (thrallOutlineBoxes)
        {
			postChartChange(new ChartChangedEvent(ADD_ELEMENT, THRALL, thrallOutlineBox));
            thrallOutlineBoxes.add(thrallOutlineBox);
        }
    }

    public void addThrallBoxes(List<ThrallOutlineBox> outlineBoxes)
    {
		for(ThrallOutlineBox box : outlineBoxes)
		{
			addThrallBox(box);
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
        if (tick - endTick < 10) //todo what is the purpose?
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
        hoveredColumn = -1;
        hoveredTick = -1;
        hoveredPlayer = "";
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
        addAttack(attack, playerAnimation, false);
		changesSaved = false;
    }

    public void addAttack(PlayerDidAttack attack)
    {
        addAttack(attack, PlayerAnimation.NOT_SET);
		changesSaved = false;
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
		if(room.equals("Maiden"))
		{
			attack.tick += 2; //I do not understand why this must be done but the attacks are on the wrong tick otherwise
		}
        attack.tick += baseEndTick;
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
        baseEndTick++;
        if (endTick % ticksToShow < 2 || endTick == 1)
        {
            recalculateSize();
            sendToBottom();
        } else
        {
            drawGraph();
        }
    }

    public void setEndTick(int tick)
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

    int draggedTextOffsetX = 0;
    int draggedTextOffsetY = 0;

    private final ClientThread clientThread;

    private static volatile boolean isShiftPressed = false;

    public static boolean isShiftPressed()
    {
        synchronized (ChartPanel.class)
        {
            return isShiftPressed;
        }
    }

    private static volatile boolean isCtrlPressed = false;
    public static boolean isCtrlPressed()
    {
        synchronized (ChartPanel.class)
        {
            return isCtrlPressed;
        }
    }

    public void release()
    {
		resetGraph();
        img = null;
        removeMouseListener(this);
        removeMouseWheelListener(this);
        removeMouseMotionListener(this);
		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
		removeFocusListener(this);
		for(PropertyChangeListener cl : getPropertyChangeListeners())
		{
			removePropertyChangeListener(cl);
		}
    }

    public void appendToSelected(char c)
    {
        for(ChartTick tick : selectedTicks)
        {
            synchronized (outlineBoxes)
            {
                for(OutlineBox box : outlineBoxes)
                {
                    if(box.tick == tick.getTick() && Objects.equals(box.player, tick.getPlayer()))
                    {
                        box.additionalText += c;
                    }
                }
            }
        }
        redraw();
    }

    public void removeLastCharFromSelected()
    {
        if(currentTool == SELECTION_TOOL)
        {
            for (ChartTick tick : selectedTicks)
            {
                synchronized (outlineBoxes)
                {
                    for (OutlineBox box : outlineBoxes)
                    {
                        if (box.tick == tick.getTick() && Objects.equals(box.player, tick.getPlayer()))
                        {
                            if (!box.additionalText.isEmpty())
                            {
                                box.additionalText = box.additionalText.substring(0, box.additionalText.length() - 1);
                            }
                        }
                    }
                }
            }
        }
        else if(currentTool == ADD_TEXT_TOOL)
        {
            if(currentlyBeingEdited != null)
            {
				String editedText = currentlyBeingEdited.text;
                if(!editedText.isEmpty())
                {
					currentlyBeingEdited.text = editedText.substring(0, editedText.length()-1);
					changesSaved = false;
                }
            }
        }
        drawGraph();
    }

    public static boolean isEditingBoxText = false;

    public ChartPanel(String room, boolean isLive, AdvancedRaidTrackerConfig config, ClientThread clientThread, ConfigManager configManager, ItemManager itemManager, SpriteManager spriteManager)
    {
        this.itemManager = itemManager;
        this.spriteManager = spriteManager;
        this.configManager = configManager;
        this.config = config;
        this.clientThread = clientThread;
        OutlineBox.spriteManager = spriteManager;
        OutlineBox.itemManager = itemManager;
        OutlineBox.clientThread = clientThread;
        OutlineBox.useUnkitted = config.useUnkitted();
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
        if(!isLive)
        {
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
			KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
			addFocusListener(this);
            img = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
        }
        setFocusable(true);
        requestFocus();
        recalculateSize();
    }

	public void openFile()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(PLUGIN_DIRECTORY + "/misc-dir/"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("Chart Files", "json"));
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			ChartIOData data = loadChartFromFile(file.getAbsolutePath());
			associatedFileName = file.getAbsolutePath();
			if(data != null)
			{
				applyFromSave(data);
			}
		}
	}

	public void saveFile()
	{
		if(associatedFileName.isEmpty())
		{
			JFileChooser saveFile = new JFileChooser();
			saveFile.setCurrentDirectory(new File(PLUGIN_DIRECTORY + "/misc-dir/"));
			saveFile.setFileFilter(new FileNameExtensionFilter("Chart Files", "json"));
			if(saveFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				File file = saveFile.getSelectedFile();
				associatedFileName = file.getAbsolutePath();
				saveChart(this, associatedFileName);
				changesSaved = true;
			}
		}
		else
		{
			saveChart(this, associatedFileName);
		}
	}

	public void exportImage()
	{

	}

	public void saveAs()
	{
		JFileChooser saveFile = new JFileChooser();
		saveFile.setCurrentDirectory(new File(PLUGIN_DIRECTORY + "/misc-dir/"));
		saveFile.setFileFilter(new FileNameExtensionFilter("Chart Files", "json"));
		if(saveFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			File file = saveFile.getSelectedFile();
			associatedFileName = file.getAbsolutePath();
			saveChart(this, associatedFileName);
			changesSaved = true;
		}
	}

    public void applyFromSave(ChartIOData data)
    {
		playerSBSCoolDown.clear();
		playerWasOnCD.clear();
		playerVengCoolDown.clear();
		playerThrallCoolDown.clear();
		playerDCCoolDown.clear();

		changesSaved = true;
        setStartTick(data.getStartTick());
        setEndTick(data.getEndTick());
        room = data.getRoomName();
        roomSpecificText = data.getRoomSpecificText();

        autos.clear();
        addAutosFromExisting(data.getAutos());

        specific.clear();
        addRoomSpecificDatum(data.getRoomSpecificTextMapping());

        lines.clear();
        addLines(data.getLines());

        outlineBoxes.clear();
        addAttacks(data.getOutlineBoxes());

        textBoxes.clear();
		addTextBoxes(data.getTextMapping());

		thrallOutlineBoxes.clear();
		addThrallBoxes(data.getThrallOutlineBoxes());

        recalculateSize();
    }

	private void addTextBoxes(List<ChartTextBox> textBoxes)
	{
		for(ChartTextBox textBox : textBoxes)
		{
			this.textBoxes.add(textBox);
			postChartChange(new ChartChangedEvent(ADD_ELEMENT, TEXT, textBox));
		}
	}

	public void newFile()
	{
		if(!changesSaved)
		{
			int result = JOptionPane.showConfirmDialog(this, "You have unsaved changes. Would you like to save?");
			if(result == JOptionPane.YES_OPTION)
			{
				saveFile();
			}
			else if(result == JOptionPane.CANCEL_OPTION)
			{
				return;
			}
		}
		applyFromSave(new ChartIOData(1, 50, "Creator", "", new ArrayList<>(), new HashMap<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "", new ArrayList<>()));
		changesSaved = true;
		associatedFileName = "";
	}

    public void addAttacks(List<OutlineBox> outlineBoxes)
    {
        for(OutlineBox box : outlineBoxes)
        {
            addAttack(box);
        }
    }

    private List<OutlineBox> getSelectedOutlineBoxes()
    {
        List<OutlineBox> selectedBoxes = new ArrayList<>();
        for(ChartTick tick : selectedTicks)
        {
            for(OutlineBox box : outlineBoxes)
            {
                if(box.tick == tick.getTick() && Objects.equals(box.player, tick.getPlayer()))
                {
                    selectedBoxes.add(box);
                    removeAttack(box);
                }
            }
        }
        return selectedBoxes;
    }

    private void processChartAction(ChartAction action)
    {
        for(OutlineBox box : action.getBoxes())
        {
            if(action.getActionType().equals(ADD_ELEMENT))
            {
                removeAttack(box);
            }
            else if(action.getActionType().equals(ChartActionType.REMOVE_ELEMENT))
            {
                addAttack(box);
            }
        }
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
            for (ChartLine line : lines)
            {
                if (line.text.equals("W1"))
                {
                    instanceTime = line.tick % 4;
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
                    for (ChartLine line : lines)
                    {
                        if (line.tick < (i - 3))
                        {
                            if (line.text.equals("Stall"))
                            {
                                stallsUntilThisPoint++;
                            }
                        }
                    }
                }
                int ticks = i - (stallsUntilThisPoint * 4);
                boolean autosContains = autos.stream().anyMatch(a->a.tick==ticks);
                boolean potentialAutosContains = potentialAutos.contains(ticks);
                if(autosContains && potentialAutosContains)
                {
                    g.setColor(new Color(40, 140, 235));
                }
                else if (autosContains || potentialAutosContains)
                {
                    g.setColor(Color.RED);
                }
                String tick = (config.useTimeOnChart()) ? RoomUtil.time(ticks) : String.valueOf(ticks);
                if (tick.endsWith("s")) //strip scuffed indicator from crab description because 5 letters is too many to draw
                {
                    tick = tick.substring(0, tick.length() - 1);
                }
                int textPosition = (config.wrapAllBoxes()) ? xOffset + scale - strWidth - (scale / 4) : xOffset + (scale / 2) - (strWidth / 2);
                if (yOffset - (fontHeight / 2) > scale + 5 && xOffset > LEFT_MARGIN-5)
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

    List<Integer> potentialAutos = new ArrayList<>();

    private void drawAuto(Graphics2D g, int tick, int opacity)
    {
        g.setColor(new Color(255, 80, 80, opacity));
        int xOffset = getXOffset(tick);
        int yOffset = getYOffset(tick);
        g.fillRect(xOffset, yOffset + scale, scale, boxHeight - (scale * (2 + getAdditionalRow())));
    }

    private void drawAutos(Graphics2D g)
    {
        for (ChartAuto auto : autos)
        {
            if (shouldTickBeDrawn(auto.tick))
            {
                drawAuto(g, auto.tick, 40);
            }
        }
    }

    private void drawPotentialAutos(Graphics2D g)
    {
        for(Integer i : potentialAutos)
        {
            if(shouldTickBeDrawn(i))
            {
                drawAuto(g, i, 20);
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
                    if (textYPosition > scale + 5 && !specific.isEmpty()  && textPosition > LEFT_MARGIN-5)
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
                        if (yOffset - (scale / 2) > scale + 5 && xOffset > LEFT_MARGIN-5)
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
                    if (yOffset > scale + 5 && xOffset > LEFT_MARGIN-5)
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
                if (yOffset > scale + 5 && xOffset+textOffset > LEFT_MARGIN-5)
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
                    if (yOffset > scale + 5 && xOffset > LEFT_MARGIN-5)
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
									if(box.playerAnimation.color.getAlpha() == 0)
									{
										opacity = 0;
									}
                                    if (config.attackBoxColor().equals(Color.WHITE))
                                    {
                                        g.setColor(new Color(box.color.getRed(), box.color.getGreen(), box.color.getBlue(), opacity));
                                    } else
                                    {
                                        g.setColor(config.attackBoxColor());
                                    }
                                    fillBoxStyleAccordingToConfig(g, xOffset + 2, yOffset + 2, scale - 3, scale - 3, 5, 5);
                                    BufferedImage icon = getIcon(box.playerAnimation, box.weapon);
                                    if(icon == null)
                                    {
                                        continue;
                                    }
                                    BufferedImage scaled = getScaledImage(icon, (scale - 2), (scale - 2));
                                    if (box.playerAnimation.shouldFlip)
                                    {
                                        g.drawImage(createFlipped(createDropShadow(scaled)), xOffset + 3, yOffset + 3, null);
                                        g.drawImage(createFlipped(scaled), xOffset + 2, yOffset + 1, null);
                                    } else
                                    {
                                        g.drawImage(createDropShadow(scaled), xOffset + 3, yOffset + 3, null);
                                        g.drawImage(scaled, xOffset + 2, yOffset + 1, null);
                                    }
                                    BufferedImage secondary = getSpellSpecificIcon(box.secondaryID);
                                    if(secondary != null && secondary != icon)
                                    {
                                        BufferedImage scaledSecondary = getScaledImage(secondary, scale/2, scale/2);
                                        g.drawImage(scaledSecondary, xOffset + (scale/2), yOffset+(scale/2), null);
                                    }
									if(box.tertiaryID != -2)
									{
										BufferedImage tertiary = getSpellSpecificIcon(box.tertiaryID);
										if (tertiary != null)
										{
											BufferedImage scaledTertiary = getScaledImage(tertiary, scale/2, scale/2);
											g.drawImage(scaledTertiary, xOffset + (scale/2), yOffset, null);
										}
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
									if(box.damage > -1)
									{
										Font f = g.getFont();
										g.setFont(f.deriveFont(9.0f));
										int textOffset = (scale)-(getStringWidth(g, String.valueOf(box.damage)))-5;
										g.setColor(config.fontColor());
										g.drawString(String.valueOf(box.damage), xOffset+textOffset, yOffset+3+getStringHeight(g));
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
            for (ChartLine line : lines)
            {
                if (shouldTickBeDrawn(line.tick))
                {
                    int xOffset = getXOffset(line.tick);
                    int yOffset = getYOffset(line.tick);
                    g.setColor(config.markerColor());
                    if (currentTool == ADD_LINE_TOOL && hoveredTick == line.tick)
                    {
                        g.setColor(new Color(40, 140, 235));
                    }
                    g.drawLine(xOffset, yOffset + (scale / 2), xOffset, yOffset + boxHeight - scale);
                    int stringLength = getStringBounds(g, line.text).width;
                    g.setColor(config.fontColor());
                    if (yOffset + (scale / 2) > scale + 5 && xOffset-(stringLength/2) > LEFT_MARGIN-5)
                    {
                        g.drawString(line.text, xOffset - (stringLength / 2), yOffset + (scale / 2)); //todo
                    }
                }
            }
        }
    }

	private boolean hoveredThrallIntersectsExisting()
	{
		if(hoveredThrallBox == null)
		{
			return false;
		}
		synchronized (thrallOutlineBoxes)
		{
			for(ThrallOutlineBox box : thrallOutlineBoxes)
			{
				if(box.owner.equals(hoveredThrallBox.owner) && box.spawnTick == hoveredThrallBox.spawnTick)
				{
					return true;
				}
			}
		}
		return false;
	}

    private void drawThrallBoxes(Graphics2D g)
    {
		if(hoveredThrallBox != null)
		{
			int opacity = (hoveredThrallIntersectsExisting()) ? 60 : 10;
			drawThrallBox(g, hoveredThrallBox, opacity);
		}
        synchronized (thrallOutlineBoxes)
        {
            for (ThrallOutlineBox box : thrallOutlineBoxes)
            {
				drawThrallBox(g, box, 30);
			}
        }
    }

	private void drawThrallBox(Graphics2D g, ThrallOutlineBox box, int opacity)
	{
		g.setColor(new Color(box.getColor().getRed(), box.getColor().getGreen(), box.getColor().getBlue(), opacity));

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

	/**
     * Finds the highest tick that doesn't overlap if they summoned a thrall in the future before this one would naturally expire
     *
     * @param owner     player who summoned this thrall
     * @param startTick tick the thrall was summoned
     * @return
     */
    private int getMaxTick(String owner, int startTick)
    {
        int maxTick = startTick + 99;
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
        if (hoveredTick != -1 && !hoveredPlayer.equalsIgnoreCase(""))
        {
            g.setColor(config.fontColor());
            if (enforceCD)
            {
                if (playerWasOnCD.containsEntry(hoveredPlayer, hoveredTick))
                {
                    g.setColor(Color.RED);
                }
            }
            int xOffset = getXOffset(hoveredTick);
            int yOffset = ((playerOffsets.get(hoveredPlayer) + 1) * scale) + getYOffset(hoveredTick);
            if (yOffset > scale + 5 && xOffset > LEFT_MARGIN-5)
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
        if (hoveredColumn != -1 && shouldTickBeDrawn(hoveredColumn))
        {
            g.setColor(config.fontColor());
            int xOffset = getXOffset(hoveredColumn);
            int yOffset = getYOffset(hoveredColumn);
            int additionalRows = 1 + getAdditionalRow();
            g.drawRect(xOffset, yOffset, scale, scale * (attackers.size() + additionalRows));

            int selectedTickHP = -1;
            try
            {
                selectedTickHP = roomHP.getOrDefault(hoveredColumn + 1, -1);
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
                    offset += (4 - ((offset + hoveredColumn) % 4));
                    offset -= 2;
                    break;
            }
            String bossWouldHaveDied = (offset != -1) ? "Melee attack on this tick killing would result in: " + RoomUtil.time(hoveredColumn + 1 + offset + 1) + " (Quick death: " + RoomUtil.time(hoveredColumn + offset + 1) + ")" : "";
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

	public void chartSelectionChanged(List<OutlineBox> boxes)
	{
		List<ChartTick> newSelection = new ArrayList<>();
		for(OutlineBox box : boxes)
		{
			newSelection.add(new ChartTick(box.tick, box.player));
		}
		selectedTicks = newSelection;
		drawGraph();
	}

    private void drawHoverBox(Graphics2D g)
    {
        synchronized (actions)
        {
            for (PlayerDidAttack action : actions.keySet())
            {
                if (action.tick == hoveredTick && action.player.equals(hoveredPlayer) && shouldTickBeDrawn(action.tick))
                {
                    Point location = getPoint(action.tick, action.player);
                    HoverBox hoverBox = new HoverBox(actions.get(action), config);
					if(action.getPlayerAnimation().attackTicks > 0)
					{
						hoverBox.addString("");
						for (String item : action.wornItemNames)
						{
							hoverBox.addString("." + item);
						}
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
            for (ChartLine line : lines)
            {
                if (line.text.contains("Dead"))
                {
                    continue;
                }
                String proc = line.text.split(" ")[0];
                int xOffset = getXOffset(line.tick + 1);
                int yOffset = 10 + getYOffset(line.tick + 1);
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
                        xOffset = getXOffset(line.tick + 1);
                        yOffset = 10 + getYOffset(line.tick + 1);
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
                    if (yOffset > scale + 5 && xOffset > LEFT_MARGIN-5)
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
        if (shouldTickBeDrawn(hoveredTick) && lines.stream().noneMatch(o->o.tick ==hoveredTick))
        {
            int xOffset = getXOffset(hoveredTick);
            int yOffset = getYOffset(hoveredTick);
            g.setColor(config.markerColor());
            g.drawLine(xOffset, yOffset + (scale / 2), xOffset, yOffset + boxHeight - scale);
        }
    }

    private void drawSelectedTicks(Graphics2D g)
    {
        for(ChartTick tick : selectedTicks)
        {
            if(playerOffsets.containsKey(tick.getPlayer()))
            {
                int xOffset = getXOffset(tick.getTick());
                int yOffset = getYOffset(tick.getTick());
                yOffset += (playerOffsets.get(tick.getPlayer())+1) * scale;
                if(isEditingBoxText)
                {
                    g.setColor(config.markerColor());
                }
                else
                {
                    g.setColor(getTransparentColor(config.fontColor(), 128));
                }
                g.drawRect(xOffset, yOffset, scale, scale);
            }
        }
    }

    private void drawSelectionRegion(Graphics2D g)
    {
        if(selectionDragActive && playerOffsets.containsKey(activeDragPlayer) && playerOffsets.containsKey(dragStartPlayer))
        {
            int beginTick = Math.min(dragStartTick, activeDragTick);
            int stopTick = Math.max(dragStartTick, activeDragTick);
            int xStart = getXOffset(beginTick);
            int xEnd = getXOffset(stopTick)+scale;
            int lowOffset = Math.min(playerOffsets.get(activeDragPlayer), playerOffsets.get(dragStartPlayer));
            int highOffset = Math.max(playerOffsets.get(activeDragPlayer), playerOffsets.get(dragStartPlayer));
            int yStart = getYOffset(beginTick)+scale+(lowOffset*scale);
            int yEnd = getYOffset(stopTick)+scale+scale+(highOffset*scale);
            g.setColor(getTransparentColor(config.primaryDark(), 180));
            g.fillRect(xStart, yStart, xEnd-xStart, yEnd-yStart);
            g.setColor(config.fontColor());
            g.drawRect(xStart, yStart, xEnd-xStart, yEnd-yStart);
        }
    }

	private long lastRefresh = 0;

    private synchronized void drawGraph()
    {
        if (!shouldDraw() || img == null)
        {
            return;
        }
		if(System.nanoTime()-lastRefresh < 20*1000000)
		{
			return;
		}
		lastRefresh = System.nanoTime();
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
        drawPotentialAutos(g);
        drawMarkerLines(g);
        drawMaidenCrabs(g);

        if (currentTool != ADD_LINE_TOOL && currentTool != ADD_TEXT_TOOL)
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

        if (currentTool == ADD_LINE_TOOL)
        {
            drawLinePlacement(g);
        }

        drawSelectedTicks(g);
        drawSelectionRegion(g);

        drawMappedText(g);
        if(currentTool == ADD_TEXT_TOOL)
        {
            drawCurrentlyEditedTextBox(g);
            drawAlignmentMarkers(g);
        }

        updateStatus();
        g.setColor(oldColor);
        g.dispose();
        repaint();
    }

    private void drawAlignmentMarkers(Graphics2D g)
    {
        g.setColor(config.markerColor());
        if(currentTool == ADD_TEXT_TOOL && currentlyBeingEdited == null)
        {
            for(Line l : alignmentMarkers)
            {
                g.drawLine(l.getP1().getX(), l.getP1().getY(), l.getP2().getX(), l.getP2().getY());
            }
        }
    }

    private void drawMappedText(Graphics2D g)
    {
        g.setColor(config.fontColor());
        for(ChartTextBox p : textBoxes)
        {
            if(currentlyBeingHovered != null && p.getPoint().equals(currentlyBeingHovered.getPoint()) && isDragging)
            {
                g.drawString(p.text, p.getPoint().getX()+alignmentOffsetX-currentScrollOffsetX, p.getPoint().getY()-alignmentOffsetY-currentScrollOffsetY);
            }
            else
            {
                g.drawString(p.text, p.getPoint().getX()-currentScrollOffsetX, p.getPoint().getY()-currentScrollOffsetY);
            }
        }
    }

    private void drawCurrentlyEditedTextBox(Graphics2D g)
    {
        if(currentlyBeingEdited != null)
        {
            g.setColor(new Color(40, 140, 235));
            g.drawRect(currentlyBeingEdited.point.getX()-5-currentScrollOffsetX, currentlyBeingEdited.point.getY()-20 + (getStringHeight(g)/2)-currentScrollOffsetY, 10 + getStringWidth(g, currentlyBeingEdited.text), 20);
        }
        else if(currentlyBeingHovered != null)
        {
            g.setColor(config.boxColor());
            if(isDragging)
            {
                g.drawRect(currentlyBeingHovered.getPoint().getX() - 5-currentScrollOffsetX, currentlyBeingHovered.getPoint().getY() - 20 + (getStringHeight(g) / 2)-currentScrollOffsetY, 10 + getStringWidth(g, currentlyBeingHovered.text), 20);
            }
            else
            {
                g.drawRect(currentlyBeingHovered.getPoint().getX() - 5+alignmentOffsetX-currentScrollOffsetX, currentlyBeingHovered.getPoint().getY() - 20 + (getStringHeight(g) / 2)+alignmentOffsetY-currentScrollOffsetY, 10 + getStringWidth(g, currentlyBeingHovered.text), 20);
            }
        }
    }

    public void updateStatus()
    {
        switch(currentTool)
        {
            case ADD_ATTACK_TOOL:
                setStatus("Left Click: " + selectedPrimary.name + ", Right Click: " + selectedSecondary.name);
                break;
            case ADD_AUTO_TOOL:
                setStatus("Adding " + potentialAutos.size() + " autos. Spacing: " + autoScrollAmount);
                break;
            case ADD_LINE_TOOL:
                setStatus("Placing Lines");
                break;
            case ADD_TEXT_TOOL:
                setStatus("Setting Text");
                break;
            case SELECTION_TOOL:
                setStatus(selectedTicks.size() + " ticks selected. ");
                if(isEditingBoxText)
                {
                    appendStatus("Editing Box Text");
                }
                break;
        }
    }

    public void setAttackers(List<String> attackers)
    {
        this.attackers.clear();
        this.attackers.addAll(attackers); //don't do this.attackers = because the reference changes on plugin end
        playerOffsets.clear();
        recalculateSize();
    }
	private long lastHoverRefresh = 0;
    public void setTickHovered(int x, int y)
    {
		if(System.nanoTime()-lastHoverRefresh < 20*1000000)
		{
			return;
		}
		lastHoverRefresh = System.nanoTime();
        if (boxHeight > 0 && scale > 0)
        {
            y = y + currentScrollOffsetY;
            x = x + (currentScrollOffsetX%scale);
            if (y > 20) //todo why do I use 20 here when TOP_MARGIN is 30?
            {
                int boxNumber = (y - 20) / boxHeight;
                if (x > LEFT_MARGIN-5)
                {
                    int tick = startTick + (ticksToShow * boxNumber + ((x - LEFT_MARGIN) / scale));
                    int playerOffsetPosition = (((y - TOP_MARGIN - scale) % boxHeight) / scale);
                    if (playerOffsetPosition >= 0 && playerOffsetPosition < attackers.size() && (y - TOP_MARGIN - scale > 0))
                    {
                        hoveredTick = tick;
                        hoveredPlayer = attackers.get(playerOffsetPosition);
                        hoveredColumn = -1;
                    } else if (y % boxHeight < TOP_MARGIN + scale)
                    {
                        hoveredColumn = tick;
                        hoveredPlayer = "";
                        hoveredTick = -1;
                    } else
                    {
                        hoveredPlayer = "";
                        hoveredTick = -1;
                        hoveredColumn = -1;
                    }
                    currentlyBeingHovered = getNearByText(new Point(x, y));
                } else
                {
                    hoveredPlayer = "";
                    hoveredTick = -1;
                    hoveredColumn = -1;
                    currentlyBeingHovered = null;
                }
				if(currentTool == ADD_THRALL_TOOL)
				{
					if(!hoveredPlayer.isEmpty() && hoveredTick != -1)
					{
						hoveredThrallBox = new ThrallOutlineBox(hoveredPlayer, hoveredTick, MAGE_THRALL);
					}
					else
					{
						hoveredThrallBox = null;
					}
				}
				else
				{
					hoveredThrallBox = null;
				}
                drawGraph();
            }
        }
    }

    private int currentBox = 0;
    private int currentScrollOffsetY = 0;
    private int currentScrollOffsetX = 0;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) //implement scrolling
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
        else //ctrl + scroll behavior for adding autos to add them every x ticks
        {
            if(e.getWheelRotation() < 0)
            {
                if(currentTool == ADD_AUTO_TOOL)
                {
                    autoScrollAmount = Math.max(0, --autoScrollAmount);
                    setPotentialAutos();
                }
                else
                {
                    if (startTick > 0)
                    {
                        startTick--;
                        endTick--;
                    }
                }
            }
            else
            {
                if(currentTool == ADD_AUTO_TOOL)
                {
                    autoScrollAmount++;
                    setPotentialAutos();
                }
                else
                {
                    startTick++;
                    endTick++;
                }
            }
        }
        recalculateSize();
    }

    private int autoScrollAmount;

    @Override
    public void mouseClicked(MouseEvent e)
    {
        checkRelease(e, false);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(SwingUtilities.isMiddleMouseButton(e))
        {
            if(!isDragging)
            {
                currentScrollOffsetX = 0;
                currentScrollOffsetY = 0;
                startTick = baseStartTick;
                endTick = baseEndTick;
                redraw();
            }
        }
    }

    private boolean isDragging = false;

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (isDragging) //prevent checkRelease from being double called by both clicked and released
        {
            checkRelease(e, true);
            draggedTextOffsetX = 0;
            draggedTextOffsetY = 0;
            isDragging = false;
        }
    }

    private final Stack<ChartAction> actionHistory = new Stack<>();

    private void reverseAction(ChartAction action)
    {

    }

    private void checkRelease(MouseEvent e, boolean wasDragging)
    {
		requestFocus();
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
            switch(currentTool)
            {
                case ADD_LINE_TOOL:
                    if(hoveredTick != -1)
                    {
                        if (SwingUtilities.isLeftMouseButton(e))
                        {
                            addLine(hoveredTick, manualLineText);
                        }
                        else if (SwingUtilities.isRightMouseButton(e))
                        {
							List<ChartLine> removedLines = lines.stream().filter(o->o.tick ==hoveredTick).collect(Collectors.toList());
							removedLines.forEach(l->postChartChange(new ChartChangedEvent(REMOVE_ELEMENT, LINE, l)));
                            lines.removeAll(removedLines);
                        }
                    }
                    break;
                case SELECTION_TOOL:
                    if(SwingUtilities.isLeftMouseButton(e))
                    {
                        if(isCtrlPressed())
                        {
                            ChartTick tick = new ChartTick(hoveredTick, hoveredPlayer);
                            if(!selectedTicks.contains(tick))
                            {
                                selectedTicks.add(tick);
                            }
                        }
                        else
                        {
                            selectedTicks.clear();
                            selectedTicks.add(new ChartTick(hoveredTick, hoveredPlayer));
                        }
                    }
                    else if(SwingUtilities.isRightMouseButton(e))
                    {

                    }
                    break;
                case ADD_ATTACK_TOOL:
                    if(SwingUtilities.isLeftMouseButton(e))
                    {
                        if (hoveredTick != -1 && !selectedPrimary.equals(PlayerAnimation.NOT_SET))
                        {
                            int weapon = 0;
                            if (selectedPrimary.weaponIDs.length > 0)
                            {
                                weapon = selectedPrimary.weaponIDs[0];
                            }
                            addAttack(new PlayerDidAttack(itemManager, hoveredPlayer, String.valueOf(selectedPrimary.animations[0]), hoveredTick, weapon, "", "", 0, 0, "", ""), selectedPrimary, true);
                        }
                        else if (selectedPrimary.equals(PlayerAnimation.NOT_SET))
                        {
                            removeAttack(hoveredTick, hoveredPlayer, true);
                        }
                        if (hoveredTick != -1)
                        {
                            if (isDragging)
                            {
                                if (dragStartTick > 0 && activeDragTick > 0 && !dragStartPlayer.isEmpty() && !activeDragPlayer.isEmpty())
                                {
                                    selectionDragActive = false;
                                }
                            }
                        }
                    }
                    else if(SwingUtilities.isRightMouseButton(e))
                    {
                        if (hoveredTick != -1 && !selectedSecondary.equals(PlayerAnimation.NOT_SET))
                        {
                            int weapon = 0;
                            if (selectedSecondary.weaponIDs.length > 0)
                            {
                                weapon = selectedSecondary.weaponIDs[0];
                            }
                            addAttack(new PlayerDidAttack(itemManager, hoveredPlayer, String.valueOf(selectedPrimary.animations[0]), hoveredTick, weapon, "", "", 0, 0, "", ""), selectedSecondary, true);
                        }
                        else if (selectedSecondary.equals(PlayerAnimation.NOT_SET))
                        {
                            removeAttack(hoveredTick, hoveredPlayer, true);
                        }
                    }
                    break;
                case ADD_AUTO_TOOL:
                    if(SwingUtilities.isLeftMouseButton(e))
                    {
                        for(Integer potential : potentialAutos)
                        {
                            if(autos.stream().noneMatch(a->a.tick==potential))
                            {
								addAuto(potential);
                            }
                        }
                        potentialAutos.clear();
                    }
                    else if(SwingUtilities.isRightMouseButton(e))
                    {
						List<ChartAuto> toRemove = autos.stream().filter(a->potentialAutos.contains(a.tick)).collect(Collectors.toList());
						toRemove.forEach(ca->postChartChange(new ChartChangedEvent(REMOVE_ELEMENT, AUTO, ca)));
						autos.removeAll(toRemove);
                        potentialAutos.clear();
                    }
                    break;
                case ADD_TEXT_TOOL:
                    if(SwingUtilities.isLeftMouseButton(e) && !wasDragging)
                    {
                        ChartTextBox nearby = getNearByText(new Point(e.getX(), e.getY()));
                        if(currentlyBeingEdited != null) //currently editing a text box
                        {
                            if(nearby != currentlyBeingEdited) //clicked away from current box
                            {
                                stoppedEditingTextBox();
                            }
                        }
                        else //not currently editing a text box
                        {
                            if(nearby != null) //clicked an existing text box
                            {
                                currentlyBeingEdited = nearby;
                            }
                            else //create a new text box
                            {
								currentlyBeingEdited = new ChartTextBox("", new Point(e.getX(), e.getY()));
								postChartChange(new ChartChangedEvent(ADD_ELEMENT, TEXT, currentlyBeingEdited));
								textBoxes.add(currentlyBeingEdited);
                            }
                        }
                    }
                    else if(wasDragging)
                    {
                        if(currentlyBeingHovered != null)
                        {
							currentlyBeingHovered.point = new Point(currentlyBeingHovered.point.getX()+alignmentOffsetX, currentlyBeingHovered.point.getY()+alignmentOffsetY);
                        }
                        alignmentOffsetX = 0;
                        alignmentOffsetY = 0;
                    }
                    drawGraph();
                    break;
				case ADD_THRALL_TOOL:
					if(SwingUtilities.isLeftMouseButton(e))
					{
						addThrallBox(new ThrallOutlineBox(hoveredPlayer, hoveredTick, MAGE_THRALL));
					}
					else if(SwingUtilities.isRightMouseButton(e))
					{
						if(hoveredThrallIntersectsExisting())
						{
							synchronized (thrallOutlineBoxes)
							{
								List<ThrallOutlineBox> toRemove = thrallOutlineBoxes.stream().filter
									(o->o.spawnTick == hoveredThrallBox.spawnTick && o.owner.equals(hoveredThrallBox.owner)).collect(Collectors.toList());
								toRemove.forEach(o->postChartChange(new ChartChangedEvent(REMOVE_ELEMENT, THRALL, o)));
								thrallOutlineBoxes.removeAll(toRemove);
							}
						}
					}
					drawGraph();
					break;
            }
            if (SwingUtilities.isLeftMouseButton(e))
            {
                if(isShiftPressed())
                {
                    selectionDragActive = false;
                    int lowestPlayer = Math.min(playerOffsets.get(activeDragPlayer), playerOffsets.get(dragStartPlayer));
                    int highestPlayer = Math.max(playerOffsets.get(activeDragPlayer), playerOffsets.get(dragStartPlayer));
                    int lowestTick = Math.min(activeDragTick, dragStartTick);
                    int highestTick = Math.max(activeDragTick, dragStartTick);
                    for(OutlineBox box : outlineBoxes)
                    {
                        if(box.tick >= lowestTick && box.tick <= highestTick && playerOffsets.get(box.player) >= lowestPlayer && playerOffsets.get(box.player) <= highestPlayer)
                        {
                            selectedTicks.add(new ChartTick(box.tick, box.player));
                        }
                    }
                }
                else if(selectedTicks.size() > 1 && !isCtrlPressed())
                {
                    selectedTicks.clear();
                }
            }
        }
    }

    private void stoppedEditingTextBox()
    {
        if(currentlyBeingEdited != null && textBoxes.contains(currentlyBeingEdited))
        {
            if (currentlyBeingEdited.text.isEmpty())
            {
				postChartChange(new ChartChangedEvent(REMOVE_ELEMENT, TEXT, currentlyBeingEdited));
                textBoxes.remove(currentlyBeingEdited);
            }
        }
        currentlyBeingEdited = null;
    }

    boolean isOuterEdgeOfNearbyText = false;

    public boolean inRectangle(Rectangle rect, int x, int y)
    {
        return x > rect.x && x < rect.x+rect.width && y > rect.y && y < rect.y+rect.height;
    }

    private void setIsOuterEdgeOfNearbyText(boolean state)
    {
        if(currentTool == ADD_TEXT_TOOL && currentlyBeingEdited == null) //don't change cursor if actively editing
        {
            isOuterEdgeOfNearbyText = state;
            setCursor(Cursor.getPredefinedCursor(state ? Cursor.MOVE_CURSOR : Cursor.TEXT_CURSOR));
        }
    }

    private ChartTextBox getNearByText(Point current)
    {
        for(ChartTextBox p : textBoxes)
        {
            Rectangle bounds = getStringBounds((Graphics2D) img.getGraphics(), p.text);
            bounds.x += p.getPoint().getX();
            bounds.y += p.getPoint().getY();

            Rectangle boundsExtruded = new Rectangle(bounds.x-5, bounds.y-5, bounds.width+10, bounds.height+10);
            if(inRectangle(boundsExtruded, current.getX(), current.getY()))
            {
                setIsOuterEdgeOfNearbyText(!inRectangle(bounds, current.getX(), current.getY()));
                return p;
            }
        }
        setIsOuterEdgeOfNearbyText(false);
        return null;
    }

    List<ChartTextBox> textBoxes = new ArrayList<>();
    public static ChartTextBox currentlyBeingEdited = null;
    private ChartTextBox currentlyBeingHovered = null;
    Map<String, Integer> playerSBSCoolDown = new HashMap<>();
    Map<String, Integer> playerVengCoolDown = new HashMap<>();
	Map<String, Integer> playerThrallCoolDown = new HashMap<>();
	Map<String, Integer> playerDCCoolDown = new HashMap<>();

    private void addAttack(PlayerDidAttack attack, PlayerAnimation playerAnimation, boolean recordAttack)
    {
		changesSaved = false;
        if (clientThread != null)
        {
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
			if(playerAnimation == VENG_APPLIED)
			{
				targetString = playerAnimation.name + ": " + attack.damage;
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
                OutlineBox outlineBox = new OutlineBox(playerAnimation.shorthand, playerAnimation.color, isTarget, additionalText, playerAnimation, playerAnimation.attackTicks, attack.tick, attack.player, RaidRoom.getRoom(this.room), attack.weapon);
				outlineBox.setWornItems(attack.wornItems);
				if(attack.damage > -1)
				{
					outlineBox.setDamage(attack.damage);
				}
				if(clientThread != null)
				{
					clientThread.invoke(outlineBox::setWornNames);
				}
                String[] spotAnims = attack.spotAnims.split(":");
                if (playerAnimation == SBS)
                {
                    playerSBSCoolDown.put(attack.player, attack.tick + 10);
                }
				else if(playerAnimation == VENG_SELF)
				{
					playerVengCoolDown.put(attack.player, attack.tick+15);
				}
				else if(playerAnimation == DEATH_CHARGE)
				{
					playerDCCoolDown.put(attack.player, attack.tick+15);
				}
				else if(playerAnimation == THRALL_CAST)
				{
					playerThrallCoolDown.put(attack.player, attack.tick+15);
				}

                if (spotAnims.length > 0)
                {
                    if (!Objects.equals(spotAnims[0], ""))
                    {
                        int graphic = Integer.parseInt(spotAnims[0]);
                        if (graphic == 1062 && playerSBSCoolDown.getOrDefault(attack.player, 0) <= attack.tick) //sbs
                        {
                            outlineBox.secondaryID = Integer.parseInt(spotAnims[0]);
                            playerSBSCoolDown.put(attack.player, attack.tick + 10);
                        }
						else if(graphic == 726 && playerVengCoolDown.getOrDefault(attack.player, 0) <= attack.tick)
						{
							outlineBox.secondaryID = Integer.parseInt(spotAnims[0]);
							playerVengCoolDown.put(attack.player, attack.tick+15);
						}
						else if(graphic == 1854 && playerDCCoolDown.getOrDefault(attack.player, 0) <= attack.tick)
						{
							outlineBox.secondaryID = Integer.parseInt(spotAnims[0]);
							playerDCCoolDown.put(attack.player, attack.tick+15);
						}
						else if((graphic == 1873 || graphic == 1874 || graphic == 1875) && playerThrallCoolDown.getOrDefault(attack.player, 0) <= attack.tick)
						{
							outlineBox.secondaryID = Integer.parseInt(spotAnims[0]);
							playerThrallCoolDown.put(attack.player, attack.tick+15);
						}
						if (graphic != 1062)//non sbs secondary graphic -> force reset?
                        {
                            playerSBSCoolDown.put(attack.player, 0);
                        }
                        if (graphic != 1062 && graphic != 726 && graphic != 1854 && graphic != 1873 && graphic != 1874 && graphic != 1875 && !(playerAnimation == BARRAGE || playerAnimation == BLITZ))
                        {
                            outlineBox.secondaryID = Integer.parseInt(spotAnims[0]);
                        }
                    }
                }
                if (playerAnimation == BARRAGE || playerAnimation == BLITZ || playerAnimation == THRALL_CAST || playerAnimation == VENG_SELF
                        || playerAnimation == AID_OTHER || playerAnimation == HUMIDIFY || playerAnimation == MAGIC_IMBUE)
                {
                    playerSBSCoolDown.put(attack.player, 0);
                }
				if(playerAnimation != VENG_APPLIED)
				{
					for (OutlineBox box : outlineBoxes)
					{
						if (box.playerAnimation == VENG_APPLIED)
						{
							if (box.tick == attack.tick && Objects.equals(box.player, attack.player))
							{
								outlineBoxes.remove(box);
								for(PlayerDidAttack pda : actions.keySet())
								{
									if(pda.tick == box.tick && pda.player.equals(box.player) && pda != attack)
									{
										actions.remove(pda);
										actions.put(attack, targetString + " (Veng Applied: " + box.damage + ")");
										break;
									}
								}
								outlineBox.tertiaryID = -3;
								outlineBox.setDamage(box.damage);
								break;
							}
						}
					}
				}
                outlineBoxes.add(outlineBox);
				postChartChange(new ChartChangedEvent(ADD_ELEMENT, ATTACK, outlineBox));
                if (recordAttack)
                {
                    actionHistory.add(new ChartAction(List.of(outlineBox), ADD_ELEMENT));
                }
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

    private void removeAttack(OutlineBox box)
    {
        removeAttack(box.tick, box.player, false);
    }

    private void removeAttack(int tick, String player, boolean shouldRecord)
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
				postChartChange(new ChartChangedEvent(REMOVE_ELEMENT, ATTACK, removedBoxes.toArray()));
				if(shouldRecord)
                {
					actionHistory.push(new ChartAction(removedBoxes, ChartActionType.REMOVE_ELEMENT));
                }
            }
        }
        drawGraph();
    }

	public void copyAttackData()
	{
		String attackData = "";
		for(String player : playerOffsets.keySet())
		{
			attackData += player + ",";
			for(OutlineBox box : outlineBoxes)
			{
				if(box.player.equals(player))
				{
					attackData += "{" + box.tick + ":" + box.playerAnimation.ordinal() + "},";
				}
			}
			attackData += "\n";
		}

		StringSelection selection = new StringSelection(attackData);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}

    int currentTool = NO_TOOL;

    public void setToolSelection(int tool)
    {
        if(tool == ADD_TEXT_TOOL)
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        }
        else
        {
            setCursor(Cursor.getDefaultCursor());
        }
        currentTool = tool;
        drawGraph();
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        hoveredPlayer = "";
        hoveredTick = -1;
        hoveredColumn = -1;
        drawGraph();
    }

    int dragStartX = 0;
    int dragStartY = 0;
    int lastScrollOffsetX = 0;
    int lastScrollOffsetY = 0;
    int lastStartTick = 0;
    int lastEndTick = 0;
    int dragStartTick = 0;
    String dragStartPlayer = "";
    int activeDragTick = 0;
    String activeDragPlayer = "";
    int activeDragX = 0;
    int activeDragY = 0;
    @Override
    public void mouseDragged(MouseEvent e)
    {
        setDraggedTickValues(e.getX(), e.getY());
        if(!isDragging)
        {
            dragStartX = e.getX();
            dragStartY = e.getY();
        }
        if(SwingUtilities.isMiddleMouseButton(e))
        {
            if (!isDragging)
            {
                lastScrollOffsetX = 0;
                lastScrollOffsetY = currentScrollOffsetY;
                lastStartTick = startTick;
                lastEndTick = endTick;
            } else
            {
                currentScrollOffsetY = lastScrollOffsetY + (dragStartY - e.getY());
                currentScrollOffsetX = lastScrollOffsetX + (dragStartX - e.getX());
                if (lastStartTick + (currentScrollOffsetX / scale) > 0)
                {
                    startTick = lastStartTick + (currentScrollOffsetX / scale);
                    endTick = lastEndTick + (currentScrollOffsetX / scale);
                }
                drawGraph();
            }
        }
        else if(SwingUtilities.isLeftMouseButton(e) && isShiftPressed())
        {
            if(!isDragging)
            {
                selectionDragActive = true;
                dragStartPlayer = hoveredPlayer;
                dragStartTick = hoveredTick;
            }
            else
            {
                activeDragX = e.getX();
                activeDragY = e.getY();
            }
        }
        else if(currentTool == ADD_TEXT_TOOL && currentlyBeingEdited == null && isOuterEdgeOfNearbyText && currentlyBeingHovered != null)
        {
            setAlignmentMarkers(e.getX()-draggedTextOffsetX, e.getY()-draggedTextOffsetY);
            if(!isDragging)
            {
                draggedTextOffsetX = e.getX()-currentlyBeingHovered.getPoint().getX();
                draggedTextOffsetY = e.getY()-currentlyBeingHovered.getPoint().getY();
            }
			currentlyBeingHovered.point = new Point(e.getX()-draggedTextOffsetX, e.getY()-draggedTextOffsetY);

        }
        isDragging = true;
    }

    public void setDraggedTickValues(int x, int y) //todo migrate generic
    {
        if (boxHeight > 0 && scale > 0)
        {
            y = y + currentScrollOffsetY;
            x = x + (currentScrollOffsetX%scale);
            if (y > 20) //todo why do I use 20 here when TOP_MARGIN is 30?
            {
                int boxNumber = (y - 20) / boxHeight;
                if (x > LEFT_MARGIN-5)
                {
                    int tick = startTick + (ticksToShow * boxNumber + ((x - LEFT_MARGIN) / scale));
                    int playerOffsetPosition = (((y - TOP_MARGIN - scale) % boxHeight) / scale);
                    if (playerOffsetPosition >= 0 && playerOffsetPosition < attackers.size() && (y - TOP_MARGIN - scale > 0))
                    {
                        activeDragTick = tick;
                        activeDragPlayer = attackers.get(playerOffsetPosition);
                    } else if (y % boxHeight < TOP_MARGIN + scale)
                    {
                        activeDragTick = tick;
                        activeDragPlayer = "";
                    } else
                    {
                        activeDragPlayer = "";
                        activeDragTick = -1;
                    }
                } else
                {
                    activeDragPlayer = "";
                    activeDragTick = -1;
                }
                drawGraph();
            }
        }
    }

    private final List<OutlineBox> copiedOutlineBoxes = new ArrayList<>();
    int copiedTick = 0;
    int copiedPlayer = 0;

    int alignmentOffsetX = 0;
    int alignmentOffsetY = 0;

    public void copyAttacks()
    {
        if(!selectedTicks.isEmpty())
        {
            int lowestTick = Integer.MAX_VALUE;
            int lowestPlayer = Integer.MAX_VALUE;
            for(ChartTick chartTick : selectedTicks)
            {
                lowestTick = Math.min(lowestTick, chartTick.getTick());
                lowestPlayer = Math.min(lowestPlayer, playerOffsets.get(chartTick.getPlayer()));
            }
            copiedTick = lowestTick;
            copiedPlayer = lowestPlayer;
            copiedOutlineBoxes.clear();
            synchronized (outlineBoxes)
            {
                for(OutlineBox box : outlineBoxes)
                {
                    if(selectedTicks.stream().anyMatch(b->b.getTick()==box.tick && b.getPlayer().equals(box.player)))
                    {
                        copiedOutlineBoxes.add(box);
                    }
                }
            }
        }
    }

    public void pasteAttacks()
    {
        if(selectedTicks.size() == 1)
        {
            List<OutlineBox> boxesToAddToHistory = new ArrayList<>();
            for (OutlineBox box : copiedOutlineBoxes)
            {
                int tickOffset = selectedTicks.get(0).getTick() - copiedTick;
                int playerOffset = playerOffsets.get(selectedTicks.get(0).getPlayer()) - copiedPlayer;
                if (playerOffset + playerOffsets.get(box.player) <= playerOffsets.size() - 1 && tickOffset < endTick)
                {
                    synchronized (outlineBoxes)
                    {
                        String translatedPlayer = "";
                        for (String player : playerOffsets.keySet())
                        {
                            if (playerOffsets.get(player).equals(playerOffset + playerOffsets.get(box.player)))
                            {
                                translatedPlayer = player;
                            }
                        }
                        OutlineBox outlineBox = new OutlineBox(box.letter, box.color, box.primaryTarget, box.additionalText, box.playerAnimation, box.cd, box.tick+tickOffset, translatedPlayer, RaidRoom.getRoom(this.room), box.weapon);
                        addAttack(outlineBox);
                        boxesToAddToHistory.add(outlineBox);
                    }
                }
            }
            actionHistory.push(new ChartAction(boxesToAddToHistory, ADD_ELEMENT));
        }
        drawGraph();
    }

    public void addAttack(OutlineBox box)
    {
		changesSaved = false;
        synchronized (outlineBoxes)
        {
            outlineBoxes.add(box);
			postChartChange(new ChartChangedEvent(ADD_ELEMENT, ATTACK, box));
        }
        for(int i = box.tick; i < box.tick+box.cd; i++)
        {
            playerWasOnCD.put(box.player, i);
        }
    }

    private void setPotentialAutos()
    {
        potentialAutos.clear();
        if(hoveredTick != -1)
        {
            potentialAutos.add(hoveredTick);
            if(autoScrollAmount > 0)
            {
                for(int i = hoveredTick; i < endTick; i++)
                {
                    if(((i-hoveredTick) % autoScrollAmount) == 0)
                    {
                        potentialAutos.add(i);
                    }
                }
            }
        }
    }

    List<Line> alignmentMarkers = new ArrayList<>();

    private void setAlignmentMarkers(int x, int y)
    {
        alignmentMarkers.clear();
        if(currentTool == ADD_TEXT_TOOL && currentlyBeingEdited == null)
        {
            for(ChartTextBox p : textBoxes)
            {
                if(Math.abs(p.getPoint().getX()-x) < 5)
                {
                    alignmentOffsetX = p.getPoint().getX()-x;
                    Point p1 = (p.getPoint().getY() > y) ? new Point(p.getPoint().getX(), y+10) : new Point(p.getPoint().getX(), p.getPoint().getY()+10);
                    Point p2 = (p.getPoint().getY() > y) ? new Point(p.getPoint().getX(), p.getPoint().getY()-10) : new Point(p.getPoint().getX(), y-10);
                    alignmentMarkers.add(new Line(p1, p2));
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        checkBoxHovered = e.getX() >= 180 && e.getX() <= 200 && e.getY() >= 2 && e.getY() <= 22;
        checkBox2Hovered = e.getX() >= 290 && e.getX() <= 310 && e.getY() >= 2 && e.getY() <= 22;
        checkBox3Hovered = e.getX() >= 410 && e.getX() <= 430 && e.getY() >= 2 && e.getY() <= 22;
        setTickHovered(e.getX(), e.getY());


        setAlignmentMarkers(e.getX(), e.getY());

        if(currentTool == ADD_AUTO_TOOL)
        {
            setPotentialAutos();
        }
        else
        {
            potentialAutos.clear();
        }
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
        drawGraph();
    }

    public ChartIOData getForSerialization()
    {
        return new ChartIOData(startTick, endTick, room, roomSpecificText, autos, specific, lines, outlineBoxes, textBoxes, "", thrallOutlineBoxes);
    }

    public void setStatus(String text)
    {
        if(statusBar != null)
        {
            statusBar.set(text);
        }
    }

    public void appendStatus(String text)
    {
        if(statusBar != null)
        {
            statusBar.append(text);
        }
    }

    public void appendToStartStatus(String text)
    {
        if(statusBar != null)
        {
            statusBar.appendToStart(text);
        }
    }

	@Setter
	private JTree tree;

	public void updateTree()
	{
		if(tree != null)
		{
			tree.setModel(null);
			tree.setCellRenderer(new DefaultTreeCellRenderer()
			{
				@Override
				public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
																  boolean isLeaf, int row, boolean focused)
				{
					Component cell = super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, row, focused);
					cell.setForeground(config.fontColor());
					return cell;
				}
			});
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("Layers")
			{
				{
					setForeground(config.fontColor());
				}
			};
			DefaultMutableTreeNode attacks = new DefaultMutableTreeNode("Attacks")
			{
				{
					setForeground(config.fontColor());
				}
			};
			Map<String, DefaultMutableTreeNode> playerNodes = new LinkedHashMap<>();
			for(String player : playerOffsets.keySet())
			{
				playerNodes.put(player, new DefaultMutableTreeNode(player)
				{
					{
						setForeground(config.fontColor());
					}
				});
			}
			for(OutlineBox box : outlineBoxes)
			{
				playerNodes.get(box.player).add(new DefaultMutableTreeNode(box));
			}
			for(DefaultMutableTreeNode nodes : playerNodes.values())
			{
				attacks.add(nodes);
			}

			root.add(attacks);
			tree.setModel(new DefaultTreeModel(root));
		}
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		setBorder(new LineBorder(config.boxColor()));
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		setBorder(null);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e)
	{
		synchronized (ChartPanel.class)
		{
			if(!shouldDraw() || (!room.equals("Creator") && !(e.getKeyCode() == KeyEvent.VK_CONTROL)))
			{
				return false;
			}
			switch (e.getID())
			{
				case KeyEvent.KEY_PRESSED:
					switch(e.getKeyCode())
					{
						case KeyEvent.VK_CONTROL:
							isCtrlPressed = true;
							break;
						case KeyEvent.VK_SHIFT:
							isShiftPressed = true;
							break;
					}
					break;
				case KeyEvent.KEY_RELEASED:
					switch(e.getKeyCode())
					{
						case KeyEvent.VK_CONTROL:
							isCtrlPressed = false;
							autoScrollAmount = 0;
							break;
						case KeyEvent.VK_SHIFT:
							isShiftPressed = false;
							break;
						case KeyEvent.VK_C:
							if(isCtrlPressed())
							{
								copyAttacks();
							}
							break;
						case KeyEvent.VK_V:
							if(isCtrlPressed())
							{
								pasteAttacks();
							}
							break;
						case KeyEvent.VK_DELETE:
							if(currentTool == SELECTION_TOOL)
							{
								List<OutlineBox> selectedBoxes = new ArrayList<>();
								for(ChartTick tick : selectedTicks)
								{
									for(OutlineBox box : outlineBoxes)
									{
										if(box.tick == tick.getTick() && Objects.equals(box.player, tick.getPlayer()))
										{
											selectedBoxes.add(box);
										}
									}
								}
								for(OutlineBox box : selectedBoxes)
								{
									removeAttack(box);
								}
								actionHistory.add(new ChartAction(selectedBoxes, ChartActionType.REMOVE_ELEMENT));
								selectedTicks.clear();
								redraw();
							}
							break;
						case KeyEvent.VK_Z:
							if(isCtrlPressed())
							{
								if(!actionHistory.isEmpty())
								{
									processChartAction(actionHistory.pop());
								}
							}
							break;
						case KeyEvent.VK_ENTER:
							if(isEditingBoxText)
							{
								isEditingBoxText = false;
								setCursor(Cursor.getDefaultCursor());
							}
							if(currentlyBeingEdited != null)
							{
								stoppedEditingTextBox();
							}
							else if(!selectedTicks.isEmpty())
							{
								List<OutlineBox> createdBoxes = new ArrayList<>();
								boolean shouldApplyBoxes = true;
								for(ChartTick tick : selectedTicks)
								{
									synchronized (outlineBoxes)
									{
										for(OutlineBox box : outlineBoxes)
										{
											if (tick.getTick() == box.tick && tick.getPlayer().equals(box.player))
											{
												shouldApplyBoxes = false;
												break;
											}
										}
										if(shouldApplyBoxes)
										{
											OutlineBox createdBox = new OutlineBox(selectedPrimary.shorthand, selectedPrimary.color, true, "", selectedPrimary, selectedPrimary.attackTicks, tick.getTick(), tick.getPlayer(), RaidRoom.getRoom(room), selectedPrimary.weaponIDs[0]);
											createdBoxes.add(createdBox);
										}
									}
								}
								if(shouldApplyBoxes)
								{
									for(OutlineBox box : createdBoxes)
									{
										addAttack(box);
										actionHistory.add(new ChartAction(createdBoxes, ADD_ELEMENT));
									}
								}
								else
								{
									isEditingBoxText = true;
									setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
								}
							}
							drawGraph();
							break;
						case KeyEvent.VK_A:
							if(isCtrlPressed())
							{
								selectedTicks.clear();
								currentTool = SELECTION_TOOL;
								synchronized (outlineBoxes)
								{
									for (OutlineBox box : outlineBoxes)
									{
										selectedTicks.add(new ChartTick(box.tick, box.player));
									}
								}
								drawGraph();
							}
							break;
					}
					break;
				case KeyEvent.KEY_TYPED:
					if (!isCtrlPressed())
					{
						if((int)e.getKeyChar() == 8) //Unicode Backspace (U+0008)
						{
							removeLastCharFromSelected();
						}
						else
						{
							if (currentTool == SELECTION_TOOL)
							{
								appendToSelected(e.getKeyChar());
							}
							else if (currentTool == ADD_TEXT_TOOL)
							{
								if (currentlyBeingEdited != null)
								{
									currentlyBeingEdited.text += String.valueOf(e.getKeyChar());
									changesSaved = false;
								}
							}
						}
					}
					drawGraph();
					break;

			}
			return false;
		}
	}
}