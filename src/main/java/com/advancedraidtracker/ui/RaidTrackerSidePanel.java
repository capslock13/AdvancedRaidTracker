package com.advancedraidtracker.ui;

import com.advancedraidtracker.*;
import com.advancedraidtracker.ui.charts.chartcreator.ChartCreatorFrame;
import com.advancedraidtracker.utility.UISwingUtility;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.wrappers.RaidsArrayWrapper;
import com.advancedraidtracker.utility.datautility.RaidsManager;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.*;
import java.util.List;

import static com.advancedraidtracker.utility.UISwingUtility.*;
import static com.advancedraidtracker.utility.datautility.DataReader.getAllRaids;
import static com.advancedraidtracker.utility.datautility.DataReader.parsedFiles;

@Slf4j
public class RaidTrackerSidePanel extends PluginPanel
{
    private JLabel raidCountLabel;
    private List<Raid> raidsData;
    private JTable loadRaidsTable;
    private ArrayList<RaidsArrayWrapper> raidSets;

    private Raids raids;

    private AdvancedRaidTrackerPlugin plugin;
    private static AdvancedRaidTrackerConfig config;
    private static ItemManager itemManager;
    private final ConfigManager configManager;
    private final ClientThread clientThread;
    private final SpriteManager spriteManager;

    private final JLabel pleaseWait;

    @Inject
    RaidTrackerSidePanel(AdvancedRaidTrackerPlugin plugin, AdvancedRaidTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager, SpriteManager spriteManager)
    {
        UISwingUtility.setConfig(config);
        this.clientThread = clientThread;
        this.configManager = configManager;
        this.spriteManager = spriteManager;
        pleaseWait = new JLabel("Parsing Files...", SwingConstants.CENTER);
        add(pleaseWait);
        new Thread(() ->
        {
            RaidTrackerSidePanel.config = config;
            this.plugin = plugin;
            RaidTrackerSidePanel.itemManager = itemManager;
            raidsData = getAllRaids();
            raids = new Raids(config, itemManager, clientThread, configManager, spriteManager);
            if(raidsData != null)
            {
                raids.updateFrameData(raidsData);
            }
            removeAll();
            buildComponents();
            updateUI();
        }).start();
    }

    private void buildComponents()
    {
        JPanel container = new JPanel();
        JPanel primaryContainer = new JPanel();

        primaryContainer.setLayout(new GridLayout(0, 1));

        JButton viewRaidsButton = new JButton("View All Raids");
        JButton refreshRaidsButton = new JButton("Refresh");

        JButton tableRaidsButton = new JButton("View Saved Raids From Table");

        viewRaidsButton.addActionListener(
                al ->
                        new Thread(() ->
                        {
							if(raids.hasShelfedData)
							{
								raids.clearFrameData();
								raids.restoreShelfedData();
							}
                            raids.repaint();
                            raids.open();
                        }).start());

        refreshRaidsButton.addActionListener(
                al ->
                        new Thread(() ->
                        {
                            viewRaidsButton.setEnabled(false);
                            tableRaidsButton.setEnabled(false);
                            raidCountLabel.setText("Refreshing, Please Wait...");
                            raidsData = getAllRaids();
                            if(raidsData != null)
                            {
                                raids.updateFrameData(raidsData);
                                raids.repaint();
                                raids.pack();
                            }
                            DefaultTableModel model = getTableModel();
                            loadRaidsTable.setModel(model);
                            viewRaidsButton.setEnabled(true);
                            tableRaidsButton.setEnabled(true);
                            updateRaidCountLabel();
                        }).start());

        tableRaidsButton.addActionListener(
                al ->
                {
					raids.shelfFrameData();
					raids.clearFrameData();
                    raids.updateFrameData(getTableData());
                    raids.repaint();
                    raids.open();
                }
        );

        JButton livePanelButton = new JButton("View Live Room");
        livePanelButton.addActionListener(al ->
                plugin.openLiveFrame());

        JButton chartCreatorButton = new JButton("Create A Chart");
        chartCreatorButton.addActionListener(al ->
        {
            ChartCreatorFrame chartCreator = new ChartCreatorFrame(config, itemManager, clientThread, configManager, spriteManager);
            chartCreator.open();
        });

        JButton copyLastSplitsButton = new JButton("Copy Last Splits");
        copyLastSplitsButton.addActionListener(al ->
        {
            String lastSplits = plugin.getLastSplits();
            if (lastSplits.isEmpty())
            {
                JFrame messageDialog = new JFrame();
                messageDialog.setAlwaysOnTop(true);
                JOptionPane.showMessageDialog(messageDialog, "No splits found to copy.\nAfter leaving a tracked PVM encounter, pressing this button will copy the room/wave splits to the clipboard to paste.", "Dialog", JOptionPane.ERROR_MESSAGE);
            } else
            {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(lastSplits), null);
            }
        });

        raidCountLabel = new JLabel("", SwingConstants.CENTER);
        updateRaidCountLabel();
        primaryContainer.add(raidCountLabel);
        primaryContainer.add(refreshRaidsButton);
        primaryContainer.add(viewRaidsButton);
        primaryContainer.add(tableRaidsButton);
        primaryContainer.add(livePanelButton);
        primaryContainer.add(chartCreatorButton);
        primaryContainer.add(copyLastSplitsButton);

        DefaultTableModel model = getTableModel();
        loadRaidsTable = new JTable(model)
        {
            @Override
            public Class<?> getColumnClass(int column)
            {
                if (column == 0)
                {
                    return String.class;
                }
                return Boolean.class;
            }
        };

        loadRaidsTable.setPreferredScrollableViewportSize(loadRaidsTable.getPreferredScrollableViewportSize());
        JScrollPane scrollPane = new JScrollPane(loadRaidsTable);
        scrollPane.setPreferredSize(new Dimension(225, scrollPane.getPreferredSize().height));
        container.add(primaryContainer);
        container.add(scrollPane);
        add(container);
    }

    private DefaultTableModel getTableModel()
    {
        Object[] columnNames = {"File Name", "Include?"};
        raidSets = RaidsManager.getRaidsSets();
        Object[][] tableData = new Object[raidSets.size()][2];
        for (int i = 0; i < raidSets.size(); i++)
        {
            tableData[i] = new Object[]{raidSets.get(i).filename, false};
        }
        return new DefaultTableModel(tableData, columnNames);
    }

    /**
     * @return data in the table
     */
    private List<Raid> getTableData()
    {
        ArrayList<String> includedSets = new ArrayList<>();
        for (int i = 0; i < loadRaidsTable.getRowCount(); i++)
        {
            if ((boolean) loadRaidsTable.getValueAt(i, 1))
            {
                includedSets.add((String) loadRaidsTable.getValueAt(i, 0));
            }
        }
        List<Raid> collectedRaids = new ArrayList<>();
        for(String set : includedSets)
        {
            for(RaidsArrayWrapper raidWrapper : raidSets)
            {
                if(set.equals(raidWrapper.filename))
                {
                    collectedRaids.addAll(raidWrapper.data);
                }
            }
        }
        return collectedRaids;
    }

    private void updateRaidCountLabel()
    {
        raidCountLabel.setText("New Raids Found: " + raidsData.size() + " (" + parsedFiles.size() + " Total)");
    }

}
