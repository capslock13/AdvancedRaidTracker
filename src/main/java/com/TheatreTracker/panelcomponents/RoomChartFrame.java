package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.utility.DataPoint;
import com.TheatreTracker.utility.PlayerDidAttack;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.*;
@Slf4j
public class RoomChartFrame extends BaseFrame
{

    public int getHighestTick(ArrayList<PlayerDidAttack> attacks, int start)
    {
        int max = start;
        for(PlayerDidAttack attack : attacks)
        {
            if(attack.tick > max)
            {
                max = attack.tick;
            }
        }
        return max;
    }
    public RoomChartFrame(ArrayList<RoomData> roomData)
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


        for(RoomData data : roomData)
        {
            Map<Integer, String> blankSpecific = new HashMap<>();
            Map<Integer,String> blankLines = new HashMap<>();

            Map<Integer, String> maidenLines = new LinkedHashMap<>();
            maidenLines.put(data.getValue(DataPoint.MAIDEN_70_SPLIT), "70s");
            maidenLines.put(data.getValue(DataPoint.MAIDEN_50_SPLIT), "50s");
            maidenLines.put(data.getValue(DataPoint.MAIDEN_30_SPLIT), "30s");

            Map<Integer, String> bloatLines = new LinkedHashMap<>();
            for(Integer i : data.bloatDowns)
            {
                bloatLines.put(i, "Down");
                bloatLines.put(i+33, "Moving");
            }

            Map<Integer,String> nyloLines = new LinkedHashMap<>();
            for(Integer i : data.nyloWaveStalled)
            {
                nyloLines.put(i, "Stall");
            }
            nyloLines.put(data.getValue(DataPoint.NYLO_LAST_WAVE), "Last Wave");
            nyloLines.put(data.getValue(DataPoint.NYLO_BOSS_SPAWN), "Boss Spawn");
            for(int i = data.getValue(DataPoint.NYLO_BOSS_SPAWN)+11; i < data.getNyloTime(); i+= 10)
            {
                nyloLines.put(i, "Phase");
            }

            for(Integer i : data.waveSpawns.keySet())
            {
                nyloLines.put(data.waveSpawns.get(i), "W"+i);
            }

            Map<Integer, String> soteLines = new LinkedHashMap<>();
            soteLines.put(data.getValue(DataPoint.SOTE_P1_SPLIT), "Maze1 Start");
            soteLines.put(data.getValue(DataPoint.SOTE_P1_SPLIT)+data.getValue(DataPoint.SOTE_M1_SPLIT), "Maze1 End");

            soteLines.put(data.getValue(DataPoint.SOTE_P2_SPLIT)+data.getValue(DataPoint.SOTE_P1_SPLIT)+data.getValue(DataPoint.SOTE_M1_SPLIT), "Maze2 Start");
            soteLines.put(data.getValue(DataPoint.SOTE_P2_SPLIT)+data.getValue(DataPoint.SOTE_P1_SPLIT)+data.getValue(DataPoint.SOTE_M1_SPLIT)+data.getValue(DataPoint.SOTE_M2_SPLIT), "Maze2 End");


            Map<Integer, String> xarpLines = new LinkedHashMap<>();
            xarpLines.put(data.getValue(DataPoint.XARP_SCREECH), "SCREECH");
            for(int i = data.getValue(DataPoint.XARP_SCREECH)+8; i < data.getXarpTime(); i+=8)
            {
                xarpLines.put(i, "Turn");
            }

            Map<Integer, String> verzikP2Lines = new LinkedHashMap<>();
            for(Integer i : data.redsProc)
            {
                verzikP2Lines.put(i, "Reds");
                verzikP2Lines.put(i+10, "Shield End");
            }

            for(Integer i : data.p2Crabs)
            {
                verzikP2Lines.put(i, "Crabs");
            }

            Map<Integer, String> verzikP3Lines = new LinkedHashMap<>();
            for(Integer i : data.websStart)
            {
                if(i%2==0)
                    verzikP3Lines.put(i, "Webs");
            }

            for(Integer i : data.p3Crabs)
            {
                verzikP3Lines.put(i, "Crabs");
            }

            Map<Integer, String> dawnDropsMap = new LinkedHashMap<>();
            for(Integer i : data.dawnDrops)
            {
                dawnDropsMap.put(i, "X");
            }
            ArrayList<Integer> p2autos = new ArrayList<>();
            int lastreset = data.getValue(DataPoint.VERZIK_P1_SPLIT)+11;
            for(int i = lastreset; i < data.getValue(DataPoint.VERZIK_P2_SPLIT); i++)
            {
                boolean wasNextTick = false;
                for(Integer j : data.redsProc)
                {
                    if(i == j)
                    {
                        lastreset = i+15;
                    }
                    else if(i == (j-1))
                    {
                        wasNextTick = true;
                    }
                }
                if((i-lastreset) % 4 == 0 && i >= lastreset && !wasNextTick)
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
            int verzP2Start = data.getValue(DataPoint.VERZIK_P1_SPLIT)+1;
            int verzP2Time = data.getValue(DataPoint.VERZIK_P2_SPLIT);
            int verzP3Start = verzP2Time+1;
            int verzP3Time  = data.getVerzikTime();

            if(maidenTime  < 1)
            {
                maidenTime = getHighestTick(data.maidenAttacks, 0);
            }
            if(bloatTime  < 1)
            {
                bloatTime = getHighestTick(data.bloatAttacks, 0);
            }
            if(nyloTime  < 1)
            {
                nyloTime = getHighestTick(data.nyloAttacks, 0);
            }
            if(soteTime  < 1)
            {
                soteTime = getHighestTick(data.soteAttacks, 0);
            }
            if(xarpTime  < 1)
            {
                xarpTime = getHighestTick(data.xarpAttacks, 0);
            }
            if(verzP1Time  < 1)
            {
                verzP1Time = getHighestTick(data.verzAttacks, 0);
            }
            if(verzP2Time < 1)
            {
                verzP2Time = getHighestTick(data.verzAttacks, verzP1Time);
            }
            if(verzP3Time < 1)
            {
                verzP3Time = getHighestTick(data.verzAttacks, verzP2Time);
            }

            RoomChartPanel maidenRCP = new RoomChartPanel(data.maidenAttacks, data.players.keySet(), "Maiden",roomData.size(), 1, maidenTime, blankSpecific, maidenLines);
            RoomChartPanel bloatRCP = new RoomChartPanel(data.bloatAttacks, data.players.keySet(),"Bloat", roomData.size(), 1, bloatTime, blankSpecific, bloatLines);
            RoomChartPanel nyloRCP = new RoomChartPanel(data.nyloAttacks,data.players.keySet(), "Nylocas", roomData.size(), 1, nyloTime, blankSpecific, nyloLines);
            RoomChartPanel soteRCP = new RoomChartPanel(data.soteAttacks,data.players.keySet(), "Sotetseg", roomData.size(), 1, soteTime, blankSpecific, soteLines);
            RoomChartPanel xarpRCP = new RoomChartPanel(data.xarpAttacks,data.players.keySet(), "Xarpus", roomData.size(), 1, xarpTime, blankSpecific, xarpLines);
            RoomChartPanel verzP1RCP = new RoomChartPanel(data.verzAttacks,data.players.keySet(), "Verzik P1", roomData.size(), 1, verzP1Time, dawnDropsMap, blankLines);
            RoomChartPanel verzP2RCP = new RoomChartPanel(data.verzAttacks,data.players.keySet(), "Verzik P2", roomData.size(), verzP2Start, verzP2Time, blankSpecific, verzikP2Lines);
            RoomChartPanel verzP3RCP = new RoomChartPanel(data.verzAttacks,data.players.keySet(), "Verzik P3", roomData.size(), verzP3Start, verzP3Time, blankSpecific, verzikP3Lines);

            verzP2RCP.setAutos(p2autos);

            maidenRCP.setThrallSpawns(data.maidenThrallSpawns);
            bloatRCP.setThrallSpawns(data.bloatThrallSpawns);
            nyloRCP.setThrallSpawns(data.nyloThrallSpawns);
            soteRCP.setThrallSpawns(data.soteThrallSpawns);
            xarpRCP.setThrallSpawns(data.xarpusThrallSpawns);
            verzP1RCP.setThrallSpawns(data.verzikThrallSpawns);
            verzP2RCP.setThrallSpawns(data.verzikThrallSpawns);
            verzP3RCP.setThrallSpawns(data.verzikThrallSpawns);

            verzP1RCP.setDawnSpecs(data.dawnSpecs);

            maidenRCP.setOutlineBoxes(data.maidenOutlineBoxes);
            bloatRCP.setOutlineBoxes(data.bloatOutlineBoxes);
            nyloRCP.setOutlineBoxes(data.nyloOutlineBoxes);
            soteRCP.setOutlineBoxes(data.soteOutlineBoxes);
            xarpRCP.setOutlineBoxes(data.xarpOutlineBoxes);

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
