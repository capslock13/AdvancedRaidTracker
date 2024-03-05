package com.TheatreTracker.ui;

import com.TheatreTracker.*;
import com.TheatreTracker.utility.BloatHand;
import com.TheatreTracker.utility.wrappers.RaidsArrayWrapper;
import com.TheatreTracker.utility.datautility.RaidsManager;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static com.TheatreTracker.utility.datautility.DataWriter.PLUGIN_DIRECTORY;

@Slf4j
public class RaidTrackerSidePanel extends PluginPanel
{
    private JLabel raidCountLabel;
    private final JLabel pleaseWait;
    private ArrayList<SimpleRaidData> raidsData;
    private JTable loadRaidsTable;
    private ArrayList<RaidsArrayWrapper> raidSets;

    private Raids raids;

    private TheatreTrackerPlugin plugin;
    private static TheatreTrackerConfig config;
    private static ItemManager itemManager;
    private ConfigManager configManager;

    @Inject
    RaidTrackerSidePanel(TheatreTrackerPlugin plugin, TheatreTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager)
    {
        this.configManager = configManager;
        //DataWriter.checkLogFileSize();
        pleaseWait = new JLabel("Parsing files please wait..", SwingConstants.CENTER);
        add(pleaseWait);
        new Thread(() ->
        {
            this.config = config;
            this.plugin = plugin;
            RaidTrackerSidePanel.itemManager = itemManager;
            raidsData = new ArrayList<>();
            raidsData = getAllRaids(pleaseWait);
            raids = new Raids(config, itemManager, clientThread, configManager);
            removeAll();
            buildComponents();
            updateUI();
        }).start();
    }

    private ArrayList<SimpleRaidData> getAllRaids(JLabel statusUpdate)
    {
        //todo reimplement status update
        ArrayList<SimpleRaidData> raids = new ArrayList<>();
        ArrayList<BloatHand> hands = new ArrayList<>();
        try
        {
            File logDirectory = new File(PLUGIN_DIRECTORY);
            File[] logFiles = logDirectory.listFiles();
            if (logFiles != null)
            {
                for (File file : logFiles)
                {
                    if (file.isDirectory())
                    {
                        File subDirectory = new File(file.getAbsolutePath()+"/primary/");
                        File[] subLogFiles = subDirectory.listFiles();
                        if(subLogFiles != null)
                        {
                            for (File dataFile : subLogFiles)
                            {
                                if (!dataFile.isDirectory())
                                {
                                    if (dataFile.getName().contains("data"))
                                    {
                                        File currentFile = new File(subDirectory.getAbsolutePath() + "/" + dataFile.getName());
                                        hands.addAll(parseLogFile(raids, currentFile, subDirectory.getAbsolutePath() + "/" + dataFile.getName()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            raids.sort(Comparator.comparing(SimpleRaidData::getDate));
        }
        catch (Exception e)
        {

        }
        Map<String, Integer> handMap = new HashMap<>();
        for(BloatHand bloatHand : hands)
        {
            String position = bloatHand.x + "," + bloatHand.y;
            if(!handMap.containsKey(position))
            {
                handMap.put(position, 1);
            }
            else
            {
                handMap.put(position, handMap.get(position)+1);
            }
        }
        for(String position : handMap.keySet())
        {
           // log.info(position + ": " + handMap.get(position));
        }
        //BaseFrame bf = new BaseFrame();
        //bf.add(new BloatHandDataVisualizer(hands));
        //bf.open();
        return raids;
    }

    public static ArrayList<BloatHand> parseLogFile(ArrayList<SimpleRaidData> raids, File currentFile, String filePath) throws Exception
    {
        ArrayList<BloatHand> hands = new ArrayList<>();
        int lastProc = -1;
        int handsTotal = 0;
        int bottomLeftChunkCount = 0;
        int bottomRightChunkCount = 0;
        int topLeftChunkCount = 0;
        int topRightChunkCount = 0;
        int bltotal = 0;
        int brtotal = 0;
        int tltotal = 0;
        int trtotal = 0;
        Map<Integer, Integer> procCountMap = new HashMap<>();
        Map<Integer, Integer> blProcMap = new HashMap<>();
        Map<Integer, Integer> brProcMap = new HashMap<>();
        Map<Integer, Integer> tlProcMap = new HashMap<>();
        Map<Integer, Integer> trProcMap = new HashMap<>();
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
                    if (Integer.parseInt(lineSplit[3]) == 0 || Integer.parseInt(lineSplit[3]) == 1000)
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
                        if(value != 801 && value != 576 && value != 587)
                        {
                            if (Integer.parseInt(lineSplit[3]) == 99 && !spectate)
                            {
                                spectate = true;
                                raid.add(line);
                            } else if (value == 98 && !lateStart)
                            {
                                lateStart = true;
                                raid.add(line);
                            } else if (Integer.parseInt(lineSplit[3]) == 4 || Integer.parseInt(lineSplit[3]) == 1004)
                            {
                                raid.add(line);
                                raidActive = false;
                                if(Integer.parseInt(lineSplit[3]) == 4)
                                {
                                    raids.add(new SimpleTOBData(raid.toArray(new String[0]), filePath, currentFile.getName()));
                                }
                                else
                                {
                                    raids.add(new SimpleTOAData(raid.toArray(new String[0]), filePath, currentFile.getName()));
                                }
                                raid.clear();
                            } else if (value != 99 && value != 98)
                            {
                                raid.add(line);
                            }
                        }
                        if(value == 975)
                        {
                            if(lineSplit.length > 6)
                            {
                                int x = Integer.parseInt(lineSplit[5]);
                                int y = Integer.parseInt(lineSplit[6]);
                                int id = Integer.parseInt(lineSplit[4]);
                                hands.add(new BloatHand(x, y, id));
                                if(lineSplit.length > 7)
                                {
                                    int proc = Integer.parseInt(lineSplit[7]);
                                    if(lastProc != proc)
                                    {

                                        if(!procCountMap.containsKey(handsTotal))
                                        {
                                            procCountMap.put(handsTotal, 1);
                                        }
                                        else
                                        {
                                            procCountMap.put(handsTotal, procCountMap.get(handsTotal)+1);
                                        }

                                        if(!blProcMap.containsKey(bottomLeftChunkCount))
                                        {
                                            blProcMap.put(bottomLeftChunkCount, 1);
                                        }
                                        else
                                        {
                                            blProcMap.put(bottomLeftChunkCount, blProcMap.get(bottomLeftChunkCount)+1);
                                        }

                                        if(!brProcMap.containsKey(bottomRightChunkCount))
                                        {
                                            brProcMap.put(bottomRightChunkCount, 1);
                                        }
                                        else
                                        {
                                            brProcMap.put(bottomRightChunkCount, brProcMap.get(bottomRightChunkCount)+1);
                                        }

                                        if(!tlProcMap.containsKey(topLeftChunkCount))
                                        {
                                            tlProcMap.put(topLeftChunkCount, 1);
                                        }
                                        else
                                        {
                                            tlProcMap.put(topLeftChunkCount, tlProcMap.get(topLeftChunkCount)+1);
                                        }

                                        if(!trProcMap.containsKey(topRightChunkCount))
                                        {
                                            trProcMap.put(topRightChunkCount, 1);
                                        }
                                        else
                                        {
                                            trProcMap.put(topRightChunkCount, trProcMap.get(topRightChunkCount)+1);
                                        }
                                        if(handsTotal == 13)
                                        {
                                            if(hands.size() > 13)
                                            {
                                                //log.info("Last 13 hands: ");
                                                boolean found1 = false;
                                                boolean found2 = false;
                                                for(int i = hands.size()-13; i < hands.size(); i++)
                                                {
                                                    //log.info(hands.get(i).x + ", " + hands.get(i).y);
                                                    if(hands.get(i).x == 28 && hands.get(i).y == 34)
                                                    {
                                                        found1 = true;
                                                    }
                                                    if(hands.get(i).x == 28 && hands.get(i).y == 25)
                                                    {
                                                        found2 = true;
                                                    }
                                                }
                                                if(found1 && found2)
                                                {
                                                    //log.info("HAND FOUND ON BOTH SIDES OF CORNER");
                                                }
                                            }
                                        }
                                        //log.info(handsTotal + " in last proc on " + lastProc);
                                        //log.info("Bottom Left: " + bottomLeftChunkCount);
                                        //log.info("Bottom Right: " + bottomRightChunkCount);
                                        //log.info("Top Left: " + topLeftChunkCount);
                                        //log.info("Top Right: " + topRightChunkCount);
                                        bltotal += bottomLeftChunkCount;
                                        brtotal += bottomRightChunkCount;
                                        tltotal += topLeftChunkCount;
                                        trtotal += topRightChunkCount;

                                        lastProc = proc;
                                        handsTotal = 0;
                                        bottomLeftChunkCount = 0;
                                        bottomRightChunkCount = 0;
                                        topLeftChunkCount = 0;
                                        topRightChunkCount = 0;
                                    }
                                    else
                                    {
                                        handsTotal++;
                                        if(x < 32 && y < 32)
                                        {
                                            bottomLeftChunkCount++;
                                        }
                                        else if(x < 32 && y > 31)
                                        {
                                            topLeftChunkCount++;
                                        }
                                        else if(x > 31 && y < 32)
                                        {
                                            bottomRightChunkCount++;
                                        }
                                        else if(x > 31 && y > 31)
                                        {
                                            topRightChunkCount++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        int count = 0;
        for(Integer i : procCountMap.keySet())
        {
            count += procCountMap.get(i);
            //log.info(i + " hands procced " + procCountMap.get(i) + " times");
        }

        /*for(Integer i : blProcMap.keySet())
        {
            log.info(i + " hands spawned " + blProcMap.get(i) + " times (BL)");
        }
        for(Integer i : brProcMap.keySet())
        {
            log.info(i + " hands spawned " + brProcMap.get(i) + " times (BR)");
        }
        for(Integer i : tlProcMap.keySet())
        {
            log.info(i + " hands spawned " + tlProcMap.get(i) + " times (TL)");
        }
        for(Integer i : trProcMap.keySet())
        {
            log.info(i + " hands spawned " + trProcMap.get(i) + " times (TR)");
        }*/
        if(count != 0)
        {
            //log.info("Total procs: " + count);
            //log.info("Bottom left total: " + bltotal);
            //log.info("Bottom right total: " + brtotal);
            //log.info("top left total: " + tltotal);
            //log.info("top right total: " + trtotal);
        }
        logReader.close();
        return hands;
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
                        raids = new Raids(config, itemManager, plugin.clientThread, configManager);
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
                    raids = new Raids(config, itemManager, plugin.clientThread, configManager);
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

    private ArrayList<SimpleRaidData> getTableData()
    {
        ArrayList<String> includedSets = new ArrayList<>();
        for (int i = 0; i < loadRaidsTable.getRowCount(); i++)
        {
            if ((boolean) loadRaidsTable.getValueAt(i, 1))
            {
                includedSets.add((String) loadRaidsTable.getValueAt(i, 0));
            }
        }
        ArrayList<SimpleRaidData> collectedRaids = new ArrayList<>();
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
