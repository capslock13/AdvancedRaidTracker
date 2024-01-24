package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.utility.DataPoint;
import net.runelite.client.plugins.raids.RoomType;
import sun.awt.image.ImageWatched;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class RoomChartFrame extends BaseFrame
{
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
            bloatLines.put(data.getValue(DataPoint.BLOAT_FIRST_DOWN_TIME), "Down");

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


            maidenCharts.add(new RoomChartPanel(data.maidenAttacks, data.players.keySet(), "Maiden",roomData.size(), 1, data.getMaidenTime(), blankSpecific, maidenLines));
            bloatCharts.add(new RoomChartPanel(data.bloatAttacks, data.players.keySet(),"Bloat", roomData.size(), 1, data.getBloatTime(), blankSpecific, bloatLines));
            nyloCharts.add(new RoomChartPanel(data.nyloAttacks,data.players.keySet(), "Nylocas", roomData.size(), 1, data.getNyloTime(), blankSpecific, nyloLines));
            soteCharts.add(new RoomChartPanel(data.soteAttacks,data.players.keySet(), "Sotetseg", roomData.size(), 1, data.getSoteTime(), blankSpecific, soteLines));
            xarpCharts.add(new RoomChartPanel(data.xarpAttacks,data.players.keySet(), "Xarpus", roomData.size(), 1, data.getXarpTime(), blankSpecific, xarpLines));
            verzp1Charts.add(new RoomChartPanel(data.verzAttacks,data.players.keySet(), "Verzik P1", roomData.size(), 1, data.getValue(DataPoint.VERZIK_P1_SPLIT), dawnDropsMap, blankLines));
            verzp2Charts.add(new RoomChartPanel(data.verzAttacks,data.players.keySet(), "Verzik P2", roomData.size(), data.getValue(DataPoint.VERZIK_P1_SPLIT)+1, data.getValue(DataPoint.VERZIK_P2_SPLIT), blankSpecific, verzikP2Lines));
            verzp3Charts.add(new RoomChartPanel(data.verzAttacks,data.players.keySet(), "Verzik P3", roomData.size(), data.getValue(DataPoint.VERZIK_P2_SPLIT)+1, data.getVerzikTime(), blankSpecific, verzikP3Lines));

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
