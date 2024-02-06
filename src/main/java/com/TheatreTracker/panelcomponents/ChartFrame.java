package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.utility.DataPoint;
import com.TheatreTracker.utility.PlayerDidAttack;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.*;

@Slf4j
public class ChartFrame extends BaseFrame
{

    public int getHighestTick(ArrayList<PlayerDidAttack> attacks, int start)
    {
        int max = start;
        for (PlayerDidAttack attack : attacks)
        {
            if (attack.tick > max)
            {
                max = attack.tick;
            }
        }
        return max;
    }

    public ChartFrame(ArrayList<RoomData> roomData)
    {
        JTabbedPane basepane = new JTabbedPane();

        JPanel maidenTab = new JPanel();
        JPanel bloatTab = new JPanel();
        JPanel nyloTab = new JPanel();
        JPanel soteTab = new JPanel();
        JPanel xarpTab = new JPanel();
        JPanel verzP1Tab = new JPanel();
        JPanel verzP2Tab = new JPanel();
        JPanel verzP3Tab = new JPanel();

        maidenTab.setLayout(new GridLayout(1, 2));
        bloatTab.setLayout(new GridLayout(1, 2));
        nyloTab.setLayout(new GridLayout(1, 2));
        soteTab.setLayout(new GridLayout(1, 2));
        xarpTab.setLayout(new GridLayout(1, 2));
        verzP1Tab.setLayout(new GridLayout(1, 2));
        verzP2Tab.setLayout(new GridLayout(1, 2));
        verzP3Tab.setLayout(new GridLayout(1, 2));

        JPanel maidenCharts = new JPanel();
        JPanel bloatCharts = new JPanel();
        JPanel nyloCharts = new JPanel();
        JPanel soteCharts = new JPanel();
        JPanel xarpCharts = new JPanel();
        JPanel verzp1Charts = new JPanel();
        JPanel verzp2Charts = new JPanel();
        JPanel verzp3Charts = new JPanel();

        maidenCharts.setLayout(new BoxLayout(maidenCharts, BoxLayout.Y_AXIS));
        bloatCharts.setLayout(new BoxLayout(bloatCharts, BoxLayout.Y_AXIS));
        nyloCharts.setLayout(new BoxLayout(nyloCharts, BoxLayout.Y_AXIS));
        soteCharts.setLayout(new BoxLayout(soteCharts, BoxLayout.Y_AXIS));
        xarpCharts.setLayout(new BoxLayout(xarpCharts, BoxLayout.Y_AXIS));
        verzp1Charts.setLayout(new BoxLayout(verzp1Charts, BoxLayout.Y_AXIS));
        verzp2Charts.setLayout(new BoxLayout(verzp2Charts, BoxLayout.Y_AXIS));
        verzp3Charts.setLayout(new BoxLayout(verzp3Charts, BoxLayout.Y_AXIS));


        for (RoomData data : roomData)
        {
            Map<Integer, String> maidenLines = new LinkedHashMap<>();
            maidenLines.put(data.getValue(DataPoint.MAIDEN_70_SPLIT), "70s");
            maidenLines.put(data.getValue(DataPoint.MAIDEN_50_SPLIT), "50s");
            maidenLines.put(data.getValue(DataPoint.MAIDEN_30_SPLIT), "30s");

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
            soteLines.put(data.getValue(DataPoint.SOTE_P1_SPLIT) + data.getValue(DataPoint.SOTE_M1_SPLIT), "Maze1 End");

            soteLines.put(data.getValue(DataPoint.SOTE_P2_SPLIT) + data.getValue(DataPoint.SOTE_P1_SPLIT) + data.getValue(DataPoint.SOTE_M1_SPLIT), "Maze2 Start");
            soteLines.put(data.getValue(DataPoint.SOTE_P2_SPLIT) + data.getValue(DataPoint.SOTE_P1_SPLIT) + data.getValue(DataPoint.SOTE_M1_SPLIT) + data.getValue(DataPoint.SOTE_M2_SPLIT), "Maze2 End");


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

            ChartPanel maidenRCP = new ChartPanel("Maiden", false);
            maidenRCP.setNPCMappings(data.maidenNPCMapping);
            maidenRCP.setRoomHP(data.maidenHP);
            maidenRCP.addAttacks(data.maidenAttacks);
            maidenRCP.addMaidenCrabs(data.maidenCrabSpawn);
            maidenRCP.setPlayers(new ArrayList<>(data.players.keySet()));
            if (roomData.size() == 1)
            {
                maidenRCP.enableWrap();
            }
            maidenRCP.setStartTick(1);
            maidenRCP.setTick(maidenTime);
            maidenRCP.addLines(maidenLines);
            maidenRCP.addThrallBoxes(data.maidenThrallSpawns);

            ChartPanel bloatRCP = new ChartPanel("Bloat", false);
            bloatRCP.setRoomHP(data.bloatHP);
            bloatRCP.addAttacks(data.bloatAttacks);
            bloatRCP.setPlayers(new ArrayList<>(data.players.keySet()));
            if (roomData.size() == 1)
            {
                bloatRCP.enableWrap();
            }
            bloatRCP.setStartTick(1);
            bloatRCP.setTick(bloatTime);
            bloatRCP.addLines(bloatLines);
            bloatRCP.addThrallBoxes(data.bloatThrallSpawns);

            ChartPanel nyloRCP = new ChartPanel("Nylocas", false);
            nyloRCP.setNPCMappings(data.nyloNPCMapping);
            nyloRCP.setRoomHP(data.nyloHP);
            nyloRCP.addAttacks(data.nyloAttacks);
            nyloRCP.setPlayers(new ArrayList<>(data.players.keySet()));
            if (roomData.size() == 1)
            {
                nyloRCP.enableWrap();
            }
            nyloRCP.setStartTick(1);
            nyloRCP.setTick(nyloTime);
            nyloRCP.addLines(nyloLines);
            nyloRCP.addThrallBoxes(data.nyloThrallSpawns);

            ChartPanel soteRCP = new ChartPanel("Sotetseg", false);
            soteRCP.setRoomHP(data.soteHP);
            soteRCP.addAttacks(data.soteAttacks);
            soteRCP.setPlayers(new ArrayList<>(data.players.keySet()));
            if (roomData.size() == 1)
            {
                soteRCP.enableWrap();
            }
            soteRCP.setStartTick(1);
            soteRCP.setTick(soteTime);
            soteRCP.addLines(soteLines);
            soteRCP.addThrallBoxes(data.soteThrallSpawns);

            ChartPanel xarpRCP = new ChartPanel("Xarpus", false);
            xarpRCP.setRoomHP(data.xarpHP);
            xarpRCP.addAttacks(data.xarpAttacks);
            xarpRCP.setPlayers(new ArrayList<>(data.players.keySet()));
            if (roomData.size() == 1)
            {
                xarpRCP.enableWrap();
            }
            xarpRCP.setStartTick(1);
            xarpRCP.setTick(xarpTime);
            xarpRCP.addLines(xarpLines);
            xarpRCP.addThrallBoxes(data.xarpusThrallSpawns);

            ChartPanel verzP1RCP = new ChartPanel("Verzik P1", false);
            verzP1RCP.setNPCMappings(data.verzikNPCMapping);
            verzP1RCP.setRoomHP(data.verzikHP);
            verzP1RCP.addAttacks(data.verzAttacks);
            verzP1RCP.setPlayers(new ArrayList<>(data.players.keySet()));
            if (roomData.size() == 1)
            {
                verzP1RCP.enableWrap();
            }
            verzP1RCP.setStartTick(1);
            verzP1RCP.setTick(verzP1Time);
            verzP1RCP.addRoomSpecificDatum(dawnDropsMap);
            verzP1RCP.addThrallBoxes(data.verzikThrallSpawns);
            verzP1RCP.setRoomSpecificText("Dawn Appears: ");
            verzP1RCP.addAutos(p1autos);

            ChartPanel verzP2RCP = new ChartPanel("Verzik P2", false);
            verzP2RCP.setNPCMappings(data.verzikNPCMapping);
            verzP2RCP.setRoomHP(data.verzikHP);
            verzP2RCP.addAttacks(data.verzAttacks);
            verzP2RCP.setPlayers(new ArrayList<>(data.players.keySet()));
            if (roomData.size() == 1)
            {
                verzP2RCP.enableWrap();
            }
            verzP2RCP.setStartTick(verzP2Start);
            verzP2RCP.setTick(verzP2Time);
            verzP2RCP.addLines(verzikP2Lines);
            verzP2RCP.addThrallBoxes(data.verzikThrallSpawns);

            ChartPanel verzP3RCP = new ChartPanel("Verzik P3", false);
            verzP3RCP.setNPCMappings(data.verzikNPCMapping);
            verzP3RCP.setRoomHP(data.verzikHP);

            verzP3RCP.addAttacks(data.verzAttacks);
            verzP3RCP.setPlayers(new ArrayList<>(data.players.keySet()));
            if (roomData.size() == 1)
            {
                verzP3RCP.enableWrap();
            }
            verzP3RCP.setStartTick(verzP3Start);
            verzP3RCP.setTick(verzP3Time);
            verzP3RCP.addLines(verzikP3Lines);
            verzP3RCP.addThrallBoxes(data.verzikThrallSpawns);
            verzP2RCP.addAutos(p2autos);
            verzP1RCP.addDawnSpecs(data.dawnSpecs);

            maidenRCP.redraw();
            bloatRCP.redraw();
            nyloRCP.redraw();
            soteRCP.redraw();
            xarpRCP.redraw();
            verzP1RCP.redraw();
            verzP2RCP.redraw();
            verzP3RCP.redraw();


            maidenCharts.add(maidenRCP);
            bloatCharts.add(bloatRCP);
            nyloCharts.add(nyloRCP);
            soteCharts.add(soteRCP);
            xarpCharts.add(xarpRCP);
            verzp1Charts.add(verzP1RCP);
            verzp2Charts.add(verzP2RCP);
            verzp3Charts.add(verzP3RCP);

        }

        maidenTab.add(new JScrollPane(maidenCharts));
        bloatTab.add(new JScrollPane(bloatCharts));
        nyloTab.add(new JScrollPane(nyloCharts));
        soteTab.add(new JScrollPane(soteCharts));
        xarpTab.add(new JScrollPane(xarpCharts));
        verzP1Tab.add(new JScrollPane(verzp1Charts));
        verzP2Tab.add(new JScrollPane(verzp2Charts));
        verzP3Tab.add(new JScrollPane(verzp3Charts));

        basepane.addTab("Maiden", maidenTab);
        basepane.addTab("Bloat", bloatTab);
        basepane.addTab("Nylocas", nyloTab);
        basepane.addTab("Sotetseg", soteTab);
        basepane.addTab("Xarpus", xarpTab);
        basepane.addTab("Verzik P1", verzP1Tab);
        basepane.addTab("Verzik P2", verzP2Tab);
        basepane.addTab("Verzik P3", verzP3Tab);

        add(basepane);
        pack();
    }
}
