package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.*;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.AdvancedRaidDataBase;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.RoomDataManager;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;

import javax.swing.*;
import java.awt.*;
import java.util.*;

@Slf4j
public class ChartFrame extends BaseFrame
{
    public ChartFrame(Raid roomData, AdvancedRaidTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager)
    {
        JTabbedPane basepane = new JTabbedPane();

        /*
        JPanel tab = new JPanel();
        tab.setLayout(new GridLayout(1, 2));
        JPanel chart = new JPanel();
        chart.setLayout(new BoxLayout(chart, BoxLayout.Y_AXIS));
        ChartPanel chartPanel = new ChartPanel(bossName, false, config, clientThread, configManager, itemManager);
        chartPanel.setNPCMappings(raidData.npcIndexData.get(bossName));
        chartPanel.addAttacks(raidData.attackData.get(bossName));
        chartPanel.setRoomHP(raidData.hpData.get(bossName));
        chartPanel.setPlayers(roomData.getPlayersArray());
        chartPanel.enableWrap();
        chartPanel.setStartTick((bossName.contains("Verzik") || bossName.contains("Wardens")) ? //Just trust
                (bossName.contains("P1") ? 1 : (bossName.contains("P2") ? roomData.getValue(bossName.replace('2', '1') + " Time") :
                        roomData.getValue(bossName.replace('3', '1') + " Time") + roomData.getValue(bossName.replace('3', '2') + " Time"))) : 1);
        chartPanel.setTick(((bossName.contains("Verzik") || bossName.contains("Wardens")) && !bossName.contains("P1"))
                ? (bossName.contains("P2")) ? roomData.getValue(bossName + " Time") +
                roomData.getValue(bossName.replace('2', '1') + " Time") :
                roomData.getValue(bossName.substring(0, bossName.length() - 2) + "Time") : roomData.getValue(bossName + " Time"));
        chartPanel.addThrallBoxes(raidData.thrallOutlineBoxes.get(bossName));

         */
            Map<Integer, String> lines = new LinkedHashMap<>();
            ArrayList<Integer> autos = new ArrayList<>();
            /*
            if (roomData instanceof SimpleTOBData)
            {
                SimpleTOBData tobData = (SimpleTOBData) roomData;
                switch (bossName)
                {
                    case "Maiden":
                        lines.put(roomData.getValue(DataPoint.MAIDEN_70_SPLIT), "70s");
                        lines.put(roomData.getValue(DataPoint.MAIDEN_50_SPLIT), "50s");
                        lines.put(roomData.getValue(DataPoint.MAIDEN_30_SPLIT), "30s");
                        lines.put(roomData.getValue(DataPoint.MAIDEN_TOTAL_TIME), "Dead");
                        chartPanel.addMaidenCrabs(tobData.maidenCrabSpawn);
                        break;
                    case "Bloat":
                        for (Integer i : tobData.bloatDowns)
                        {
                            lines.put(i, "Down");
                            lines.put(i + 33, "Moving");
                        }
                        break;
                    case "Nylocas":
                        for (Integer i : tobData.nyloWaveStalled)
                        {
                            lines.put(i, "Stall");
                        }
                        lines.put(roomData.getValue(DataPoint.NYLO_LAST_WAVE), "Last Wave");
                        lines.put(roomData.getValue(DataPoint.NYLO_BOSS_SPAWN), "Boss Spawn");
                        for (int i = roomData.getValue(DataPoint.NYLO_BOSS_SPAWN) + 11; i < tobData.getNyloTime(); i += 10)
                        {
                            lines.put(i, "Phase");
                        }

                        for (Integer i : tobData.waveSpawns.keySet())
                        {
                            lines.put(tobData.waveSpawns.get(i), "W" + i);
                        }
                        break;
                    case "Sotetseg":
                        lines.put(roomData.getValue(DataPoint.SOTE_P1_SPLIT), "Maze1 Start");
                        lines.put(roomData.getValue(DataPoint.SOTE_M1_SPLIT), "Maze1 End");
                        lines.put(roomData.getValue(DataPoint.SOTE_P2_SPLIT), "Maze2 Start");
                        lines.put(roomData.getValue(DataPoint.SOTE_M2_SPLIT), "Maze2 End");
                        break;
                    case "Xarpus":
                        lines.put(roomData.getValue(DataPoint.XARP_SCREECH), "SCREECH");
                        for (int i = roomData.getValue(DataPoint.XARP_SCREECH) + 8; i < roomData.getValue("Xarpus Time"); i += 8)
                        {
                            lines.put(i, "Turn");
                        }
                        break;
                    case "Verzik P1":
                        Map<Integer, String> dawnDropsMap = new LinkedHashMap<>();
                        for (Integer i : tobData.dawnDrops)
                        {
                            dawnDropsMap.put(i, "X");
                        }
                        chartPanel.addRoomSpecificDatum(dawnDropsMap);
                        for (int i = 19; i < tobData.getValue("Verzik P1 Time"); i++)
                        {
                            if (i == 19 || (i - 19) % 14 == 0)
                            {
                                autos.add(i);
                            }
                        }
                        chartPanel.setRoomSpecificText("Dawn Appears");
                        break;
                    case "Verzik P2":
                        for (Integer i : tobData.redsProc)
                        {
                            lines.put(i, "Reds");
                            lines.put(i + 10, "Shield End");
                        }
                        for (Integer i : tobData.p2Crabs)
                        {
                            lines.put(i, "Crabs");
                        }
                        int lastreset = tobData.getValue(DataPoint.VERZIK_P1_SPLIT) + 11;
                        for (int i = lastreset; i < tobData.getValue(DataPoint.VERZIK_P2_SPLIT); i++)
                        {
                            boolean wasNextTick = false;
                            for (Integer j : tobData.redsProc)
                            {
                                if (i == j)
                                {
                                    lastreset = i + 11;
                                } else if (i == (j - 5) || i == (j - 1))
                                {
                                    wasNextTick = true;
                                }
                            }
                            if ((i - lastreset) % 4 == 0 && i >= lastreset && !wasNextTick)
                            {
                                autos.add(i);
                            }
                        }
                        break;
                    case "Verzik P3":
                        for (Integer i : tobData.websStart)
                        {
                            if (i % 2 == 0)
                                lines.put(i, "Webs");
                        }
                        for (Integer i : tobData.p3Crabs)
                        {
                            lines.put(i, "Crabs");
                        }
                        break;
                }
            } else if (roomData instanceof SimpleTOAData)
            {
                SimpleTOAData toaData = (SimpleTOAData) roomData;
                switch (bossName)
                {
                    case "Het":
                        for(Integer i : toaData.hetDowns)
                        {
                            lines.put(i, "Obelisk Weakened");
                        }
                        break;
                    case "Baba":
                        lines.put(toaData.getValue(DataPoint.BABA_P1_DURATION), "Boulder 1 Start");
                        lines.put(toaData.getValue(DataPoint.BABA_P2_SPLIT), "Boulder 1 End");
                        lines.put(toaData.getValue(DataPoint.BABA_BOULDER_2_SPLIT), "Boulder 2 Start");
                        lines.put(toaData.getValue(DataPoint.BABA_P3_SPLIT), "Boulder 2 End");
                        lines.put(toaData.getValue(DataPoint.BABA_TIME), "End");
                        break;
                    case "Akkha":
                        lines.put(toaData.getValue(DataPoint.AKKHA_P1_DURATION), "Shadow1 Start");
                        lines.put(toaData.getValue(DataPoint.AKKHA_P2_SPLIT), "Shadow1 End");
                        lines.put(toaData.getValue(DataPoint.AKKHA_SHADOW_2_SPLIT), "Shadow2 Start");
                        lines.put(toaData.getValue(DataPoint.AKKHA_P3_SPLIT), "Shadow2 End");
                        lines.put(toaData.getValue(DataPoint.AKKHA_SHADOW_3_SPLIT), "Shadow3 Start");
                        lines.put(toaData.getValue(DataPoint.AKKHA_P4_SPLIT), "Shadow3 End");
                        lines.put(toaData.getValue(DataPoint.AKKHA_SHADOW_4_SPLIT), "Shadow4 Start");
                        lines.put(toaData.getValue(DataPoint.AKKHA_P5_SPLIT), "Shadow4 End");
                        lines.put(toaData.getValue(DataPoint.AKKHA_FINAL_PHASE_SPLIT), "Final Phase");
                        lines.put(toaData.getValue(DataPoint.AKKHA_TIME), "End");
                        break;
                    case "Kephri":
                        for(Integer i : toaData.dungThrows)
                        {
                            lines.put(i, "Dung");
                        }
                        lines.put(toaData.getValue(DataPoint.KEPHRI_P1_DURATION), "Swarm1 Start");
                        lines.put(toaData.getValue(DataPoint.KEPHRI_P2_SPLIT), "Swarm1 End");
                        lines.put(toaData.getValue(DataPoint.KEPHRI_SWARM2_SPLIT), "Swarm2 Start");
                        lines.put(toaData.getValue(DataPoint.KEPHRI_P3_SPLIT), "Swarm2 End");
                        lines.put(toaData.getValue(DataPoint.KEPHRI_P3_SPLIT), "Final Phase");
                        lines.put(toaData.getValue(DataPoint.KEPHRI_TIME), "End");
                        break;
                    case "Crondis": //Tick perfect actions; solo is 2t faster due to not delaying for pid reasons
                        lines.put(8, "Pickup water");
                        if(toaData.getScale() == 1)
                        {
                            lines.put(19, "First Fill");
                            lines.put(30, "First Watering");
                            lines.put(43, "Second Fill");
                            lines.put(54, "Second Watering");
                        }
                        else
                        {
                            lines.put(20, "First Fill");
                            lines.put(31, "First Watering");
                            lines.put(45, "Second Fill");
                            lines.put(56, "Second Watering");
                        }
                        break;
                    case "Zebak":
                        for(Integer i : toaData.zebakWaterfalls)
                        {
                            lines.put(i, "Waterfall");
                        }
                        for(Integer i : toaData.zebakBoulders)
                        {
                            lines.put(i, "Jugs");
                        }
                        lines.put(toaData.getValue(DataPoint.ZEBAK_ENRAGED_SPLIT), "Enraged");
                        break;
                    case "Wardens P2":
                        for(Integer i : toaData.wardenCoreStarts)
                        {
                            lines.put(i, "Core Start");
                        }
                        for(Integer i : toaData.wardenCoreEnds)
                        {
                            lines.put(i, "Core Ends");
                        }
                        break;
                    case "Wardens P3":
                        lines.put(toaData.getValue(DataPoint.WARDENS_ENRAGED_SPLIT), "Enraged");
                        for(int i = 1; i < 5; i++)
                        {
                            int p3Start = toaData.getValue(DataPoint.WARDENS_P3_SPLIT);
                            int skullStart = toaData.getValue("Wardens Skull " + i + " Split");
                            int skullDuration = toaData.getValue("Wardens Skull " + i + " Duration");
                            lines.put(p3Start+skullStart, "Skull Start");
                            lines.put(p3Start+skullStart+skullDuration, "Skull End");
                        }
                        break;
                }
            }
            chartPanel.addLines(lines);
            chartPanel.addAutos(autos);
            chartPanel.redraw();
            basepane.addChangeListener(cl -> chartPanel.redraw());
            chart.add(chartPanel);
            tab.add(new JScrollPane(chart));
             */
            //basepane.add(bossName, tab);
            add(basepane);
            pack();
    }

    private void addChartPane(RoomDataManager data) {

    }
}