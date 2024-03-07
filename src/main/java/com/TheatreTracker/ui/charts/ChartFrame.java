package com.TheatreTracker.ui.charts;

import com.TheatreTracker.*;
import com.TheatreTracker.ui.BaseFrame;
import com.TheatreTracker.utility.AdvancedRaidData;
import com.TheatreTracker.utility.datautility.DataPoint;
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
    public ChartFrame(SimpleRaidData roomData, TheatreTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager)
    {

        JTabbedPane basepane = new JTabbedPane();
        AdvancedRaidData raidData;
        if (roomData instanceof SimpleTOBData)
        {
            raidData = new AdvancedTOBData(AdvancedRaidData.getRaidStrings(roomData.filePath), itemManager);
        } else
        {
            raidData = new AdvancedTOAData(AdvancedRaidData.getRaidStrings(roomData.filePath), itemManager);
        }
        for (String s : raidData.attackData.keySet())
        {
            JPanel tab = new JPanel();
            tab.setLayout(new GridLayout(1, 2));
            JPanel chart = new JPanel();
            chart.setLayout(new BoxLayout(chart, BoxLayout.Y_AXIS));
            ChartPanel chartPanel = new ChartPanel(s, false, config, clientThread, configManager);
            /*

            TODO add things
             */
            chartPanel.redraw();
            basepane.addChangeListener(cl -> chartPanel.redraw());
            chart.add(chartPanel);
            tab.add(new JScrollPane(chart));
            basepane.add(s, tab);

        }
    }
        /*
        for (SimpleTOBData data : roomData)
        {
            AdvancedTOBData advancedData = new AdvancedTOBData(AdvancedRaidData.getRaidStrings(data.filePath), itemManager);
            Map<Integer, String> maidenLines = new LinkedHashMap<>();
            maidenLines.put(data.getValue(DataPoint.MAIDEN_70_SPLIT), "70s");
            maidenLines.put(data.getValue(DataPoint.MAIDEN_50_SPLIT), "50s");
            maidenLines.put(data.getValue(DataPoint.MAIDEN_30_SPLIT), "30s");
            maidenLines.put(data.getValue(DataPoint.MAIDEN_TOTAL_TIME), "Dead");

            Map<Integer, String> bloatLines = new LinkedHashMap<>();
            for (Integer i : data.bloatDowns)
            {
                bloatLines.put(i, "Down");
                bloatLines.put(i + 33, "Moving");
            }

            Map<Integer, String> nyloLines = new LinkedHashMap<>();
            for (Integer i : data.nyloWaveStalled)
            {
                nyloLines.put(i, "Stall");
            }
            nyloLines.put(data.getValue(DataPoint.NYLO_LAST_WAVE), "Last Wave");
            nyloLines.put(data.getValue(DataPoint.NYLO_BOSS_SPAWN), "Boss Spawn");
            for (int i = data.getValue(DataPoint.NYLO_BOSS_SPAWN) + 11; i < data.getNyloTime(); i += 10)
            {
                nyloLines.put(i, "Phase");
            }

            for (Integer i : data.waveSpawns.keySet())
            {
                nyloLines.put(data.waveSpawns.get(i), "W" + i);
            }

            Map<Integer, String> soteLines = new LinkedHashMap<>();
            soteLines.put(data.getValue(DataPoint.SOTE_P1_SPLIT), "Maze1 Start");
            soteLines.put(data.getValue(DataPoint.SOTE_M1_SPLIT), "Maze1 End");

            soteLines.put(data.getValue(DataPoint.SOTE_P2_SPLIT), "Maze2 Start");
            soteLines.put(data.getValue(DataPoint.SOTE_M2_SPLIT), "Maze2 End");


            Map<Integer, String> xarpLines = new LinkedHashMap<>();
            xarpLines.put(data.getValue(DataPoint.XARP_SCREECH), "SCREECH");
            for (int i = data.getValue(DataPoint.XARP_SCREECH) + 8; i < data.getXarpTime(); i += 8)
            {
                xarpLines.put(i, "Turn");
            }

            Map<Integer, String> verzikP2Lines = new LinkedHashMap<>();
            for (Integer i : data.redsProc)
            {
                verzikP2Lines.put(i, "Reds");
                verzikP2Lines.put(i + 10, "Shield End");
            }

            for (Integer i : data.p2Crabs)
            {
                verzikP2Lines.put(i, "Crabs");
            }

            Map<Integer, String> verzikP3Lines = new LinkedHashMap<>();
            for (Integer i : data.websStart)
            {
                if (i % 2 == 0)
                    verzikP3Lines.put(i, "Webs");
            }

            for (Integer i : data.p3Crabs)
            {
                verzikP3Lines.put(i, "Crabs");
            }

            Map<Integer, String> dawnDropsMap = new LinkedHashMap<>();
            for (Integer i : data.dawnDrops)
            {
                dawnDropsMap.put(i, "X");
            }
            ArrayList<Integer> p2autos = new ArrayList<>();
            int lastreset = data.getValue(DataPoint.VERZIK_P1_SPLIT) + 11;
            for (int i = lastreset; i < data.getValue(DataPoint.VERZIK_P2_SPLIT); i++)
            {
                boolean wasNextTick = false;
                for (Integer j : data.redsProc)
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
                    p2autos.add(i);
                }
            }
            int maidenTime = data.getMaidenTime();
            int bloatTime = data.getBloatTime();
            int nyloTime = data.getNyloTime();
            int soteTime = data.getSoteTime();
            int xarpTime = data.getXarpTime();
            int verzP1Time = data.getValue(DataPoint.VERZIK_P1_SPLIT);
            int verzP2Start = data.getValue(DataPoint.VERZIK_P1_SPLIT) + 1;
            int verzP2Time = data.getValue(DataPoint.VERZIK_P2_SPLIT);
            int verzP3Start = verzP2Time + 1;
            int verzP3Time = data.getVerzikTime();


            ArrayList<Integer> p1autos = new ArrayList<>();
            for (int i = 19; i < verzP1Time; i++)
            {
                if (i == 19 || (i - 19) % 14 == 0)
                {
                    p1autos.add(i);
                }
            }

            ChartPanel maidenRCP = new ChartPanel("Maiden", false, config, clientThread, configManager);
            maidenRCP.setNPCMappings(advancedData.maidenNPCMapping);
            maidenRCP.setRoomHP(advancedData.maidenHP);
            maidenRCP.addAttacks(advancedData.maidenAttacks);
            maidenRCP.addMaidenCrabs(data.maidenCrabSpawn);
            maidenRCP.setPlayers(new ArrayList<>(data.players.keySet()));
            maidenRCP.enableWrap();
            maidenRCP.setStartTick(1);
            maidenRCP.setTick(maidenTime);
            maidenRCP.addLines(maidenLines);
            maidenRCP.addThrallBoxes(data.maidenThrallSpawns);

            ChartPanel bloatRCP = new ChartPanel("Bloat", false, config, clientThread, configManager);
            bloatRCP.setRoomHP(advancedData.bloatHP);
            bloatRCP.addAttacks(advancedData.bloatAttacks);
            bloatRCP.setPlayers(new ArrayList<>(data.players.keySet()));
            bloatRCP.enableWrap();
            bloatRCP.setStartTick(1);
            bloatRCP.setTick(bloatTime);
            bloatRCP.addLines(bloatLines);
            bloatRCP.addThrallBoxes(data.bloatThrallSpawns);

            ChartPanel nyloRCP = new ChartPanel("Nylocas", false, config, clientThread, configManager);
            nyloRCP.setNPCMappings(advancedData.nyloNPCMapping);
            nyloRCP.setRoomHP(advancedData.nyloHP);
            nyloRCP.addAttacks(advancedData.nyloAttacks);
            nyloRCP.setPlayers(new ArrayList<>(data.players.keySet()));
            nyloRCP.enableWrap();
            nyloRCP.setStartTick(1);
            nyloRCP.setTick(nyloTime);
            nyloRCP.addLines(nyloLines);
            nyloRCP.addThrallBoxes(data.nyloThrallSpawns);

            ChartPanel soteRCP = new ChartPanel("Sotetseg", false, config, clientThread, configManager);
            soteRCP.setRoomHP(advancedData.soteHP);
            soteRCP.addAttacks(advancedData.soteAttacks);
            soteRCP.setPlayers(new ArrayList<>(data.players.keySet()));
            soteRCP.enableWrap();
            soteRCP.setStartTick(1);
            soteRCP.setTick(soteTime);
            soteRCP.addLines(soteLines);
            soteRCP.addThrallBoxes(data.soteThrallSpawns);

            ChartPanel xarpRCP = new ChartPanel("Xarpus", false, config, clientThread, configManager);
            xarpRCP.setRoomHP(advancedData.xarpHP);
            xarpRCP.addAttacks(advancedData.xarpAttacks);
            xarpRCP.setPlayers(new ArrayList<>(data.players.keySet()));
            xarpRCP.enableWrap();
            xarpRCP.setStartTick(1);
            xarpRCP.setTick(xarpTime);
            xarpRCP.addLines(xarpLines);
            xarpRCP.addThrallBoxes(data.xarpusThrallSpawns);

            ChartPanel verzP1RCP = new ChartPanel("Verzik P1", false, config, clientThread, configManager);
            verzP1RCP.setNPCMappings(advancedData.verzikNPCMapping);
            verzP1RCP.setRoomHP(advancedData.verzikHP);
            verzP1RCP.addAttacks(advancedData.verzikAttacks);
            verzP1RCP.setPlayers(new ArrayList<>(data.players.keySet()));
            verzP1RCP.enableWrap();
            verzP1RCP.setStartTick(1);
            verzP1RCP.setTick(verzP1Time);
            verzP1RCP.addRoomSpecificDatum(dawnDropsMap);
            verzP1RCP.addThrallBoxes(data.verzikThrallSpawns);
            verzP1RCP.setRoomSpecificText("Dawn Appears");
            verzP1RCP.addAutos(p1autos);

            ChartPanel verzP2RCP = new ChartPanel("Verzik P2", false, config, clientThread, configManager);
            verzP2RCP.setNPCMappings(advancedData.verzikNPCMapping);
            verzP2RCP.setRoomHP(advancedData.verzikHP);
            verzP2RCP.addAttacks(advancedData.verzikAttacks);
            verzP2RCP.setPlayers(new ArrayList<>(data.players.keySet()));
            verzP2RCP.enableWrap();
            verzP2RCP.setStartTick(verzP2Start);
            verzP2RCP.setTick(verzP2Time);
            verzP2RCP.addLines(verzikP2Lines);
            verzP2RCP.addThrallBoxes(data.verzikThrallSpawns);

            ChartPanel verzP3RCP = new ChartPanel("Verzik P3", false, config, clientThread, configManager);
            verzP3RCP.setNPCMappings(advancedData.verzikNPCMapping);
            verzP3RCP.setRoomHP(advancedData.verzikHP);

            verzP3RCP.addAttacks(advancedData.verzikAttacks);
            verzP3RCP.setPlayers(new ArrayList<>(data.players.keySet()));
            verzP3RCP.enableWrap();
            verzP3RCP.setStartTick(verzP3Start);
            verzP3RCP.setTick(verzP3Time);
            verzP3RCP.addLines(verzikP3Lines);
            verzP3RCP.addThrallBoxes(data.verzikThrallSpawns);
            verzP2RCP.addAutos(p2autos);
            verzP1RCP.addDawnSpecs(data.dawnSpecs);


        add(basepane);
        pack();
    }

         */
}
