package com.TheatreTracker.ui;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.TheatreTrackerPlugin;
import com.TheatreTracker.panelcomponents.FilteredRaidsBaseFrame;
import com.TheatreTracker.utility.RaidsArrayWrapper;
import com.TheatreTracker.utility.RaidsManager;
import com.google.inject.Inject;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;


public class RaidTrackerPanelPrimary extends PluginPanel {
    private JLabel raidCountLabel;
    private ArrayList<RoomData> raidsData;
    private JTable loadRaidsTable;
    private ArrayList<RaidsArrayWrapper> raidSets;

    private FilteredRaidsBaseFrame raids;

    @Inject
    RaidTrackerPanelPrimary(TheatreTrackerPlugin plugin) {
        raidsData = getAllRaids();
        raids = new FilteredRaidsBaseFrame();
        buildComponents();
    }

    private ArrayList<RoomData> getAllRaids() {
        ArrayList<RoomData> raids = new ArrayList<>();
        try {
            String path = "/.runelite/theatretracker/primary/tobdata.log";
            File logFile = new File(System.getProperty("user.home").replace("\\", "/") + path);
            Scanner logReader = new Scanner(Files.newInputStream(logFile.toPath()));
            ArrayList<String> raid = new ArrayList<>();
            boolean raidActive = false;
            while (logReader.hasNextLine()) {
                String line = logReader.nextLine();
                String[] lineSplit = line.split(",");
                if (!raidActive) {
                    if (lineSplit.length > 3) {
                        if (Integer.parseInt(lineSplit[3]) == 0) {
                            raid.add(line);
                            raidActive = true;
                        }
                    }
                } else {
                    if (lineSplit.length > 3) {
                        if (Integer.parseInt(lineSplit[3]) == 99) {
                            raid.add(line);
                        } else if (Integer.parseInt(lineSplit[3]) == 4) {
                            raid.add(line);
                            raidActive = false;
                            raids.add(new RoomData(raid.toArray(new String[raid.size()])));
                            raid.clear();
                        } else {
                            raid.add(line);
                        }
                    }
                }
            }
            logReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return raids;
    }

    private void buildComponents() {
        JPanel container = new JPanel();
        JPanel primaryContainer = new JPanel();
        primaryContainer.setLayout(new GridLayout(4, 1));

        JButton viewRaidsButton = new JButton("View All Raids");
        JButton refreshRaidsButton = new JButton("Refresh");

        JButton tableRaidsButton = new JButton("View Saved Raids From Table");

        viewRaidsButton.addActionListener(
                al ->
                {
                    raids = new FilteredRaidsBaseFrame();
                    raids.createFrame(raidsData);
                    raids.getContentPane().setBackground(Color.BLACK);
                    raids.repaint();
                    raids.open();
                });

        refreshRaidsButton.addActionListener(
                al ->
                {
                    raidsData = getAllRaids();
                    updateRaidCountLabel();

                    Object[] columnNames = {"File Name", "Include?"};
                    raidSets = RaidsManager.getRaidsSets();
                    Object[][] tableData = new Object[raidSets.size()][2];
                    for (int i = 0; i < raidSets.size(); i++) {
                        tableData[i] = new Object[]{raidSets.get(i).filename, false};
                    }
                    DefaultTableModel model = new DefaultTableModel(tableData, columnNames);
                    loadRaidsTable.setModel(model);

                });

        tableRaidsButton.addActionListener(
                al ->
                {
                    raids = new FilteredRaidsBaseFrame();
                    raids.createFrame(getTableData());
                    raids.getContentPane().setBackground(Color.BLACK);
                    raids.repaint();
                    raids.open();
                }
        );

        raidCountLabel = new JLabel("", SwingConstants.CENTER);
        updateRaidCountLabel();
        primaryContainer.add(raidCountLabel);
        primaryContainer.add(refreshRaidsButton);
        primaryContainer.add(viewRaidsButton);
        primaryContainer.add(tableRaidsButton);

        Object[] columnNames = {"File Name", "Include?"};
        raidSets = RaidsManager.getRaidsSets();
        Object[][] tableData = new Object[raidSets.size()][2];
        for (int i = 0; i < raidSets.size(); i++) {
            tableData[i] = new Object[]{raidSets.get(i).filename, false};
        }
        DefaultTableModel model = new DefaultTableModel(tableData, columnNames);
        loadRaidsTable = new JTable(model) {
            @Override
            public Class getColumnClass(int column) {
                if (column == 0) {
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

    private ArrayList<RoomData> getTableData() {
        ArrayList<String> includedSets = new ArrayList<>();
        for (int i = 0; i < loadRaidsTable.getRowCount(); i++) {
            if ((boolean) loadRaidsTable.getValueAt(i, 1)) {
                includedSets.add((String) loadRaidsTable.getValueAt(i, 0));
            }
        }
        ArrayList<RoomData> collectedRaids = new ArrayList<>();
        for (RaidsArrayWrapper set : raidSets) {
            for (String s : includedSets) {
                if (s.equals(set.filename)) {
                    for (RoomData raid : set.data) {
                        collectedRaids.add(raid);
                    }
                }
            }
        }
        return collectedRaids;
    }

    private void updateRaidCountLabel() {
        raidCountLabel.setText("Raids Found: " + raidsData.size());
    }

}
