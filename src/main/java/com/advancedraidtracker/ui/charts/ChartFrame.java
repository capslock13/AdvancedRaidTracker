package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.*;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.datautility.ChartData;
import com.advancedraidtracker.utility.datautility.DataReader;
import com.advancedraidtracker.utility.datautility.datapoints.col.Colo;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomParser;
import com.advancedraidtracker.utility.datautility.datapoints.toa.Toa;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;

import static com.advancedraidtracker.utility.UISwingUtility.*;

@Slf4j
public class ChartFrame extends BaseFrame
{
    private int frameX = this.getWidth();
    private int frameY = this.getHeight();
    private Set<String> TOBRooms = new LinkedHashSet<>(Arrays.asList("Maiden", "Bloat", "Nylocas", "Sotetseg", "Xarpus", "Verzik P1", "Verzik P2", "Verzik P3"));
    private Set<String> TOARooms = new LinkedHashSet<>(Arrays.asList("Apmeken", "Baba", "Scabaras", "Kephri", "Het", "Akkha", "Crondis", "Zebak", "Wardens P1", "Wardens P2", "Wardens P3"));
    private Set<String> COLRooms = new LinkedHashSet<>(Arrays.asList("Wave 1", "Wave 2", "Wave 3", "Wave 4", "Wave 5", "Wave 6", "Wave 7", "Wave 8", "Wave 9", "Wave 10", "Wave 11", "Wave 12"));

    public ChartFrame(Raid roomData, AdvancedRaidTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager, SpriteManager spriteManager)
    {
        ChartData chartData = DataReader.getChartData(roomData.getFilepath(), itemManager);

        JTabbedPane basepane = getThemedTabbedPane();

        Set<String> activeSet;

        if (roomData instanceof Toa)
        {
            activeSet = TOARooms;
        } else if (roomData instanceof Colo)
        {
            activeSet = COLRooms;
        } else
        {
            activeSet = TOBRooms;
        }

        for (String bossName : activeSet)
        {
            RaidRoom room = RaidRoom.getRoom(bossName);
            JPanel tab = getThemedPanel();
            tab.setLayout(new GridLayout(1, 2));
            JPanel chart = getThemedPanel();
            chart.setLayout(new BoxLayout(chart, BoxLayout.Y_AXIS));
            ChartPanel chartPanel = new ChartPanel(bossName, false, config, clientThread, configManager, itemManager, spriteManager);
            chartPanel.setNPCMappings(chartData.getNPCMapping(room));
            chartPanel.addAttacks(chartData.getAttacks(room));
            chartPanel.setRoomHP(chartData.getHPMapping(room));
            chartPanel.setAttackers(new ArrayList<>(roomData.getPlayers()));
            chartPanel.enableWrap();
            if (bossName.contains("Wave"))
            {
                int starttick = 1;
                chartPanel.setStartTick(starttick);
                chartPanel.setTick(starttick + roomData.get(bossName + " Duration"));
            } else
            {
                chartPanel.setStartTick((bossName.contains("Verzik") || bossName.contains("Wardens")) ? //Just trust
                        (bossName.contains("P1") ? 1 : (bossName.contains("P2") ? roomData.get(bossName.replace('2', '1') + " Time") :
                                roomData.get(bossName.replace('3', '1') + " Time") + roomData.get(bossName.replace('3', '2') + " Time"))) : 1);
                chartPanel.setTick(((bossName.contains("Verzik") || bossName.contains("Wardens")) && !bossName.contains("P1"))
                        ? (bossName.contains("P2")) ? roomData.get(bossName + " Time") +
                        roomData.get(bossName.replace('2', '1') + " Time") :
                        roomData.get(bossName.substring(0, bossName.length() - 2) + "Time") : roomData.get(bossName + " Time"));
            }
            chartPanel.addThrallBoxes(chartData.getThralls(room));
            chartPanel.addLines(roomData.getLines(room));
            chartPanel.addRoomSpecificDatum(roomData.getRoomSpecificData(room));
            chartPanel.setRoomSpecificText(roomData.getRoomSpecificText(room));
            chartPanel.addAutos(roomData.getRoomAutos(room));
            chartPanel.addMaidenCrabs(chartData.maidenCrabs);
            if (room.equals(RaidRoom.VERZIK))
            {
                chartPanel.addDawnSpecs(chartData.dawnSpecs);
            }

            chartPanel.redraw();
            basepane.addChangeListener(cl -> chartPanel.redraw());
            chart.add(chartPanel);
            tab.add(getThemedScrollPane(chart));
            basepane.add(bossName, tab);

            Timer resizeTimer = new Timer(20, e ->
            {
                chartPanel.setSize(frameX, frameY);
            });

            resizeTimer.setRepeats(false);

            addComponentListener(new ComponentAdapter()
            {
                @Override
                public void componentResized(ComponentEvent e)
                {
                    super.componentResized(e);
                    if (resizeTimer.isRunning()) //redrawing on every resize event will cause severe stuttering, wait 20ms after stopped resizing
                    {
                        resizeTimer.restart();
                    } else
                    {
                        resizeTimer.start();
                    }
                    Component c = (Component) e.getSource();
                    frameX = c.getWidth();
                    frameY = c.getHeight();
                }
            });
        }
        add(basepane);
        pack();
    }
}