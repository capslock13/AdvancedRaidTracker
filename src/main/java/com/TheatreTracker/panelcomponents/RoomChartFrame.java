package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.utility.DataPoint;
import net.runelite.client.plugins.raids.RoomType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
            ArrayList<Integer> blankSpecific = new ArrayList<>();
            ArrayList<Integer> blankLines = new ArrayList<>();

            ArrayList<Integer> maidenLines = new ArrayList<>();
            maidenLines.add(data.getValue(DataPoint.MAIDEN_70_SPLIT));
            maidenLines.add(data.getValue(DataPoint.MAIDEN_50_SPLIT));
            maidenLines.add(data.getValue(DataPoint.MAIDEN_30_SPLIT));

            ArrayList<Integer> bloatLines = new ArrayList<>();
            bloatLines.add(data.getValue(DataPoint.BLOAT_FIRST_DOWN_TIME));

            ArrayList<Integer> verzikP2Lines = new ArrayList<>();
            verzikP2Lines.add(data.getValue(DataPoint.VERZIK_REDS_SPLIT));


            maidenCharts.add(new RoomChartPanel(data.maidenAttacks, data.players.keySet(), "Maiden",roomData.size(), 1, data.getMaidenTime(), blankSpecific, maidenLines));
            bloatCharts.add(new RoomChartPanel(data.bloatAttacks, data.players.keySet(),"Bloat", roomData.size(), 1, data.getBloatTime(), blankSpecific, bloatLines));
            nyloCharts.add(new RoomChartPanel(data.nyloAttacks,data.players.keySet(), "Nylocas Boss", roomData.size(), data.getValue(DataPoint.NYLO_BOSS_SPAWN)+3, data.getNyloTime(), blankSpecific, blankLines));
            soteCharts.add(new RoomChartPanel(data.soteAttacks,data.players.keySet(), "Sotetseg", roomData.size(), 1, data.getSoteTime(), blankSpecific, blankLines));
            xarpCharts.add(new RoomChartPanel(data.xarpAttacks,data.players.keySet(), "Xarpus", roomData.size(), 1, data.getXarpTime(), blankSpecific, blankLines));
            verzp1Charts.add(new RoomChartPanel(data.verzAttacks,data.players.keySet(), "Verzik P1", roomData.size(), 1, data.getValue(DataPoint.VERZIK_P1_SPLIT), data.dawnDrops, blankLines));
            verzp2Charts.add(new RoomChartPanel(data.verzAttacks,data.players.keySet(), "Verzik P2", roomData.size(), data.getValue(DataPoint.VERZIK_P1_SPLIT)+1, data.getValue(DataPoint.VERZIK_P2_SPLIT), blankSpecific, verzikP2Lines));
            verzp3Charts.add(new RoomChartPanel(data.verzAttacks,data.players.keySet(), "Verzik P3", roomData.size(), data.getValue(DataPoint.VERZIK_P2_SPLIT)+1, data.getVerzikTime(), blankSpecific, blankLines));

        }

        RoomChartLegend legendPanel = new RoomChartLegend();

        maidenTab.add(new JScrollPane(maidenCharts));
        //maidenTab.add(legendPanel);

        bloatTab.add(new JScrollPane(bloatCharts));
        //bloatTab.add(legendPanel);

        nyloTab.add(new JScrollPane(nyloCharts));
        //nyloTab.add(legendPanel);

        soteTab.add(new JScrollPane(soteCharts));
        //soteTab.add(legendPanel);

        xarpTab.add(new JScrollPane(xarpCharts));
        //xarpTab.add(legendPanel);

        verzP1Tab.add(new JScrollPane(verzp1Charts));
        //verzP1Tab.add(legendPanel);

        verzP2Tab.add(new JScrollPane(verzp2Charts));
        //verzP2Tab.add(legendPanel);

        verzP3Tab.add(new JScrollPane(verzp3Charts));
        //verzP3Tab.add(legendPanel);

        basepane.addTab("Maiden", maidenTab);
        basepane.addTab("Bloat", bloatTab);
        basepane.addTab("Nylocas Boss", nyloTab);
        basepane.addTab("Sotetseg", soteTab);
        basepane.addTab("Xarpus", xarpTab);
        basepane.addTab("Verzik P1", verzP1Tab);
        basepane.addTab("Verzik P2", verzP2Tab);
        basepane.addTab("Verzik P3", verzP3Tab);

        add(basepane);
        pack();
    }
}
