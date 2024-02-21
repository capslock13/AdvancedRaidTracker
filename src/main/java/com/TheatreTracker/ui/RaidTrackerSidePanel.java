package com.TheatreTracker.ui;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.TheatreTrackerPlugin;
import com.TheatreTracker.ui.buttons.StripedTableRowCellRenderer;
import com.TheatreTracker.utility.datautility.DataWriter;
import com.TheatreTracker.utility.wrappers.RaidsArrayWrapper;
import com.TheatreTracker.utility.datautility.RaidsManager;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

@Slf4j
public class RaidTrackerSidePanel extends PluginPanel
{
    private JLabel raidCountLabel;
    private final JLabel pleaseWait;
    private ArrayList<RoomData> raidsData;
    private JTable loadRaidsTable;
    private ArrayList<RaidsArrayWrapper> raidSets;

    private Raids raids;

    private TheatreTrackerPlugin plugin;
    private static TheatreTrackerConfig config;
    private static ItemManager itemManager;

    @Inject
    RaidTrackerSidePanel(TheatreTrackerPlugin plugin, TheatreTrackerConfig config, ItemManager itemManager, ClientThread clientThread)
    {
        DataWriter.checkLogFileSize();
        pleaseWait = new JLabel("Parsing files please wait..", SwingConstants.CENTER);
        add(pleaseWait);
        new Thread(() ->
        {
            this.config = config;
            this.plugin = plugin;
            RaidTrackerSidePanel.itemManager = itemManager;
            raidsData = new ArrayList<>();
            raidsData = getAllRaids(pleaseWait);
            raids = new Raids(config, itemManager, clientThread);
            removeAll();
            buildComponents();
            updateUI();
        }).start();
    }

    private ArrayList<RoomData> getAllRaids(JLabel statusUpdate)
    {
        //todo reimplement status update
        ArrayList<RoomData> raids = new ArrayList<>();
        try
        {
            String path = "/.runelite/theatretracker/primary/";
            File logDirectory = new File(System.getProperty("user.home").replace("\\", "/") + path);
            File[] logFiles = logDirectory.listFiles();
            if (logFiles != null)
            {
                for (File file : logFiles)
                {
                    {
                        if (!file.isDirectory())
                        {
                            if (file.getName().contains("tobdata"))
                            {
                                File currentFile = new File(logDirectory.getAbsolutePath() + "/" + file.getName());
                                parseLogFile(raids, currentFile);
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored)
        {
        }
        raids.sort(Comparator.comparing(RoomData::getDate));
        return raids;
    }

    public static void parseLogFile(ArrayList<RoomData> raids, File currentFile) throws Exception
    {
        Scanner logReader = new Scanner(Files.newInputStream(currentFile.toPath()));
        ArrayList<String> raid = new ArrayList<>();
        boolean raidActive = false;
        boolean spectate = false;
        boolean lateStart = false;
        while (logReader.hasNextLine())
        {
            String line = logReader.nextLine();
            String[] lineSplit = line.split(",");
            if (!raidActive)
            {
                if (lineSplit.length > 3)
                {
                    if (Integer.parseInt(lineSplit[3]) == 0)
                    {
                        raid.add(line);
                        raidActive = true;
                        spectate = false;
                        lateStart = false;
                    }
                }
            } else
            {
                if (lineSplit.length > 3)
                {
                    int value = Integer.parseInt(lineSplit[3]);
                    if(value != 0)
                    {
                        if(!config.reduceMemoryLoad() || (value != 801 && value != 403 && value != 404 && value != 576 && value != 587 && value != 410 && value != 405))
                        {
                            if (Integer.parseInt(lineSplit[3]) == 99 && !spectate)
                            {
                                spectate = true;
                                raid.add(line);
                            } else if (value == 98 && !lateStart)
                            {
                                lateStart = true;
                                raid.add(line);
                            } else if (Integer.parseInt(lineSplit[3]) == 4)
                            {
                                raid.add(line);
                                raidActive = false;
                                raids.add(new RoomData(raid.toArray(new String[0]), itemManager));
                                raid.clear();
                            } else if (value != 99 && value != 98)
                            {
                                raid.add(line);
                            }
                        }
                    }
                }
            }
        }
        logReader.close();
    }

    public void refreshRaids()
    {
        new Thread(() ->
        {
            raidsData = getAllRaids(raidCountLabel);
            updateRaidCountLabel();
            DefaultTableModel model = getTableModel();
            loadRaidsTable.setModel(model);
        }).start();
    }

    private void buildComponents()
    {
        JPanel container = new JPanel();
        JPanel primaryContainer = new JPanel();

        JPanel container2 = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Raids", container);
        tabbedPane.addTab("Live Summary", container2);

        primaryContainer.setLayout(new GridLayout(5, 1));

        JButton viewRaidsButton = new JButton("View All Raids");
        JButton refreshRaidsButton = new JButton("Refresh");

        JButton tableRaidsButton = new JButton("View Saved Raids From Table");

        viewRaidsButton.addActionListener(
                al ->
                {
                    new Thread(() ->
                    {
                        raids = new Raids(config, itemManager, plugin.clientThread);
                        raids.createFrame(raidsData);
                        raids.getContentPane().setBackground(Color.BLACK);
                        raids.repaint();
                        raids.open();
                    }).start();
                });

        refreshRaidsButton.addActionListener(
                al ->
                        new Thread(() ->
                        {
                            raidsData = getAllRaids(raidCountLabel);
                            updateRaidCountLabel();
                            DefaultTableModel model = getTableModel();
                            loadRaidsTable.setModel(model);
                            raids.clearData();
                            raids = null;
                        }).start());

        tableRaidsButton.addActionListener(
                al ->
                {
                    raids = new Raids(config, itemManager, plugin.clientThread);
                    raids.createFrame(getTableData());
                    raids.getContentPane().setBackground(Color.BLACK);
                    raids.repaint();
                    raids.open();
                }
        );

        JButton livePanelButton = new JButton("View Live Room");
        livePanelButton.addActionListener(al ->
        {
            plugin.openLiveFrame();
        });

        raidCountLabel = new JLabel("", SwingConstants.CENTER);
        updateRaidCountLabel();
        primaryContainer.add(raidCountLabel);
        primaryContainer.add(refreshRaidsButton);
        primaryContainer.add(viewRaidsButton);
        primaryContainer.add(tableRaidsButton);
        primaryContainer.add(livePanelButton);

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
        add(tabbedPane);
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

    private ArrayList<RoomData> getTableData()
    {
        ArrayList<String> includedSets = new ArrayList<>();
        for (int i = 0; i < loadRaidsTable.getRowCount(); i++)
        {
            if ((boolean) loadRaidsTable.getValueAt(i, 1))
            {
                includedSets.add((String) loadRaidsTable.getValueAt(i, 0));
            }
        }
        ArrayList<RoomData> collectedRaids = new ArrayList<>();
        for (RaidsArrayWrapper set : raidSets)
        {
            for (String s : includedSets)
            {
                if (s.equals(set.filename))
                {
                    collectedRaids.addAll(set.data);
                }
            }
        }
        return collectedRaids;
    }

    private void updateRaidCountLabel()
    {
        raidCountLabel.setText("Raids Found: " + raidsData.size());
    }

}
