package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.utility.PlayerDidAttack;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class LiveChartFrame extends BaseFrame
{
    LiveChartPanel maidenPanel;
    LiveChartPanel bloatPanel;
    LiveChartPanel nyloPanel;
    LiveChartPanel sotetsegPanel;
    LiveChartPanel xarpPanel;
    LiveChartPanel verzPanel;

    public LiveChartFrame()
    {
        maidenPanel = new LiveChartPanel("Maiden");
        bloatPanel = new LiveChartPanel("Bloat");
        nyloPanel = new LiveChartPanel("Nylocas");
        sotetsegPanel = new LiveChartPanel("Sotetseg");
        xarpPanel = new LiveChartPanel("Xarpus");
        verzPanel = new LiveChartPanel("Verzik");

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Maiden", maidenPanel);
        tabbedPane.add("Bloat", bloatPanel);
        tabbedPane.add("Nylocas", nyloPanel);
        tabbedPane.add("Sotetseg", sotetsegPanel);
        tabbedPane.add("Xarpus", xarpPanel);
        tabbedPane.add("Verzik", verzPanel);

        add(tabbedPane);
        pack();
    }

    public LiveChartPanel getPanel(String room)
    {
        switch(room)
        {
            case "Maiden":
                return maidenPanel;
            case "Bloat":
                return bloatPanel;
            case "Nylocas":
                return nyloPanel;
            case "Sotetseg":
                return sotetsegPanel;
            case "Xarpus":
                return xarpPanel;
            case "Verzik":
                return verzPanel;
        }
        return new LiveChartPanel("");
    }

    public void incrementTick(String room)
    {
        getPanel(room).incrementTick();
    }

    public void addAttack(PlayerDidAttack attack, String room)
    {
        getPanel(room).addAttack(attack);
    }

    public void resetAll()
    {
        maidenPanel.resetGraph();
        bloatPanel.resetGraph();
        nyloPanel.resetGraph();
        sotetsegPanel.resetGraph();
        xarpPanel.resetGraph();
        verzPanel.resetGraph();
    }
    public void setPlayers(ArrayList<String> players)
    {
        ArrayList<String> cleanedPlayers = new ArrayList<>();
        for(String s : players)
        {
            cleanedPlayers.add(s.replaceAll(String.valueOf((char) 160), String.valueOf((char) 32)));
        }
        maidenPanel.setPlayers(cleanedPlayers);
        bloatPanel.setPlayers(cleanedPlayers);
        nyloPanel.setPlayers(cleanedPlayers);
        sotetsegPanel.setPlayers(cleanedPlayers);
        xarpPanel.setPlayers(cleanedPlayers);
        verzPanel.setPlayers(cleanedPlayers);
    }
}
