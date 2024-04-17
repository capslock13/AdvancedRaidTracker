package com.advancedraidtracker.ui.charts.chartcreator;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.ui.charts.ChartActionType;
import com.advancedraidtracker.ui.charts.ChartChangedEvent;
import com.advancedraidtracker.ui.charts.ChartIOData;
import com.advancedraidtracker.ui.charts.ChartListener;
import com.advancedraidtracker.ui.charts.ChartPanel;
import com.advancedraidtracker.ui.charts.ChartSpecCalculatorPanel;
import com.advancedraidtracker.utility.weapons.PlayerAnimation;
import java.awt.event.KeyEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.List;

import static com.advancedraidtracker.ui.charts.ChartConstants.ADD_ATTACK_TOOL;
import static com.advancedraidtracker.ui.charts.ChartIO.loadChartFromClipboard;
import static com.advancedraidtracker.utility.UISwingUtility.*;

@Slf4j
public class ChartCreatorFrame extends BaseFrame implements ChartListener
{
    private final ChartPanel chart;
	private final JTree tree;

    public ChartCreatorFrame(AdvancedRaidTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager, SpriteManager spriteManager)
    {
        setTitle("Chart Creator");
        chart = new ChartPanel("Creator", false, config, clientThread, configManager, itemManager, spriteManager);
        chart.setPreferredSize(new Dimension(0, 0));
        setPlayerCount(5);
        setEndTick(50);
        setStartTick(1);
        setPrimaryTool(PlayerAnimation.SCYTHE);
        setSecondaryTool(PlayerAnimation.NOT_SET);

        ChartTopMenuPanel menu = new ChartTopMenuPanel(this, config);
        menu.setBorder(BorderFactory.createTitledBorder("Menu"));
        menu.setPreferredSize(new Dimension(0, 50));
		menu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        ChartToolPanel tools = new ChartToolPanel(config, this, itemManager, clientThread, spriteManager);
        tools.setBorder(BorderFactory.createTitledBorder("Tools"));
        tools.setPreferredSize(new Dimension(350, 0));

        JPanel container = getThemedPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JPanel bottomContainer = getThemedPanel();
        bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.X_AXIS));
		bottomContainer.setFocusable(true);

        JPanel bottomLeftcontainer = getThemedPanel();
        bottomLeftcontainer.setLayout(new BoxLayout(bottomLeftcontainer, BoxLayout.Y_AXIS));

		JPanel chartContainer = getTitledPanel("Chart");
		chartContainer.setLayout(new BorderLayout());
		chartContainer.add(chart, BorderLayout.CENTER);
        bottomLeftcontainer.add(chartContainer);
        ChartStatusBar chartStatusBar = new ChartStatusBar("");
        chart.setStatusBar(chartStatusBar);
        chart.setToolSelection(ADD_ATTACK_TOOL);
		JPanel preachCalc = getTitledPanel("Preach/Ring Calculator");
		preachCalc.setPreferredSize(new Dimension(0, 200));
		ChartSpecCalculatorPanel specCalculator = new ChartSpecCalculatorPanel(config);
		chart.addChartListener(specCalculator);
		chart.addChartListener(this);
		preachCalc.add(specCalculator);
		bottomLeftcontainer.add(preachCalc);

		tree = getThemedTree("Chart Actions");
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
		root.add(new DefaultMutableTreeNode("Attacks"));
		root.add(new DefaultMutableTreeNode("Lines"));
		root.add(new DefaultMutableTreeNode("Text"));
		root.add(new DefaultMutableTreeNode("Autos"));
		JPanel treePanel = getThemedPanel();
		treePanel.setLayout(new BorderLayout());
		treePanel.add(tree, BorderLayout.CENTER);
		treePanel.setPreferredSize(new Dimension(300, 0));
		bottomContainer.add(treePanel);

        bottomContainer.add(bottomLeftcontainer);
        bottomContainer.add(tools);

        container.add(menu);
        container.add(bottomContainer);
		container.add(chartStatusBar);

        add(container);
        //setPreferredSize(new Dimension(1000, 600));

        JMenuBar menuBar = new JMenuBar();


        JMenu fileMenu = getThemedMenu("File");

		JMenuItem newMenu = getThemedMenuItem("New...");
		newMenu.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
		newMenu.addActionListener(o ->
		{
			chart.newFile();
		});

        JMenuItem importFromClipboard = getThemedMenuItem("Import from clipboard");

        importFromClipboard.addActionListener(o ->
        {
            try
            {
                ChartIOData data = loadChartFromClipboard((String) Toolkit.getDefaultToolkit()
                        .getSystemClipboard().getData(DataFlavor.stringFlavor));
                chart.applyFromSave(data);
            } catch (Exception e)
            {
                log.info("Failed to copy");
            }

        });

        JMenuItem openMenu = getThemedMenuItem("Open...");
        openMenu.addActionListener(o->
        {
			chart.openFile();
        });
		openMenu.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

		JMenuItem saveMenu = getThemedMenuItem("Save...");
		saveMenu.addActionListener(o ->
		{
			chart.saveFile();
		});
		saveMenu.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

		JMenuItem saveAsMenu = getThemedMenuItem("Save As...");
		saveAsMenu.addActionListener(o ->
		{
			chart.saveAs();
		});
		saveAsMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | java.awt.Event.SHIFT_MASK));

		JMenuItem exportMenu = getThemedMenuItem("Export to Image");
		exportMenu.addActionListener(o ->
		{
			chart.exportImage();
		});

		JMenuItem exportAttackData = getThemedMenuItem("Copy Attack Data to Clipboard");
		exportAttackData.addActionListener(o ->
		{
			chart.copyAttackData();
		});

		fileMenu.add(newMenu);
		fileMenu.add(openMenu);
		fileMenu.add(saveMenu);
		fileMenu.add(saveAsMenu);
		fileMenu.add(exportMenu);
		fileMenu.add(importFromClipboard);
		fileMenu.add(exportAttackData);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        pack();
        tools.build();
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		this.setMaximizedBounds(env.getMaximumWindowBounds());
		this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
    }

    public void setPlayerCount(int players)
    {
        List<String> playerList = new ArrayList<>();
        for (int i = 1; i < players + 1; i++)
        {
            playerList.add("Player" + i);
        }
        chart.setAttackers(playerList);
        chart.redraw();
    }

    public void setStartTick(int tick)
    {
        chart.setStartTick(tick);
    }

    public void setEndTick(int tick)
    {
        chart.setEndTick(tick);
    }

    public void setPrimaryTool(PlayerAnimation tool)
    {
        chart.setPrimaryTool(tool);
    }

    public void setSecondaryTool(PlayerAnimation tool)
    {
        chart.setSecondaryTool(tool);
    }

    public void setEnforceCD(boolean bool)
    {
        chart.setEnforceCD(bool);
    }

    public void setToolSelection(int tool)
    {
        chart.setToolSelection(tool);
    }

    public void changeLineText(String text)
    {
        chart.setManualLineText(text);
    }

	@Override
	public void onChartChanged(ChartChangedEvent event)
	{
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
		for(Object object : event.chartObjects)
		{
			if(event.actionType == ChartActionType.ADD_ELEMENT)
			{
				DefaultMutableTreeNode node = getNodeByObject(event.objectType.name, root, true);
				if(node != null)
				{
					node.add(new DefaultMutableTreeNode(object));
				}
			}
			else if(event.actionType == ChartActionType.REMOVE_ELEMENT)
			{
				DefaultMutableTreeNode parentNode = getNodeByObject(event.objectType.name, root, true);
				if(parentNode != null)
				{
					DefaultMutableTreeNode node = getNodeByObject(object, root, false);
					if (node != null)
					{
						parentNode.remove(node);
					}
				}
			}
		}
		reloadTreeButPreserveExpandState();
	}


	public static DefaultMutableTreeNode getNodeByObject(Object object, DefaultMutableTreeNode parentNode, boolean compareByValue)
	{
		DefaultMutableTreeNode treeNode;
		int size = parentNode.getChildCount();

		List<DefaultMutableTreeNode> parentNodes = new ArrayList<>();
		for (int i = 0; i < size; i++)
		{
			treeNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
			if(compareByValue)
			{
				if(treeNode.getUserObject().equals(object))
				{
					return treeNode;
				}
			}
			else
			{
				if (treeNode.getUserObject() == object)
				{
					return treeNode;
				}
			}
			if (treeNode.getChildCount() > 0)
			{
				parentNodes.add(treeNode);
			}
		}
		for (DefaultMutableTreeNode node : parentNodes)
		{
			treeNode = getNodeByObject(object, node, compareByValue);
			if (treeNode != null)
			{
				return treeNode;
			}
		}
		return null;
	}

	public void reloadTreeButPreserveExpandState()
	{
		List<TreePath> expanded = new ArrayList<>();
		for (int i = 0; i < tree.getRowCount() - 1; i++)
		{
			TreePath currPath = tree.getPathForRow(i);
			TreePath nextPath = tree.getPathForRow(i + 1);
			if (currPath.isDescendant(nextPath))
			{
				expanded.add(currPath);
			}
		}
		((DefaultTreeModel)tree.getModel()).reload();
		for (TreePath path : expanded)
		{
			tree.expandPath(path);
		}
	}
}
