package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.wrappers.PlayerDidAttack;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

@Slf4j
public class LiveChart extends BaseFrame
{
    ChartPanel maidenPanel;
    ChartPanel bloatPanel;
    ChartPanel nyloPanel;
    ChartPanel sotetsegPanel;
    ChartPanel xarpPanel;
    ChartPanel verzPanel;
    JScrollPane maidenScroll;
    JScrollPane bloatScroll;
    JScrollPane nyloScroll;
    JScrollPane sotetsegScroll;
    JScrollPane xarpusScroll;
    JScrollPane verzikScroll;
    public JTabbedPane tabbedPane;

    private final AdvancedRaidTrackerConfig config;
    private final ClientThread clientThread;
    private final ConfigManager configManager;

    private final String[] names = {"Maiden", "Bloat", "Nylocas", "Sotetseg", "Xarpus", "Verzik"};
    public LiveChart(AdvancedRaidTrackerConfig config, ClientThread clientThread, ConfigManager configManager)
    {
        this.configManager = configManager;
        this.clientThread = clientThread;
        this.config = config;
        maidenPanel = new ChartPanel("Maiden", true, config, clientThread, configManager);
        bloatPanel = new ChartPanel("Bloat", true, config, clientThread, configManager);
        nyloPanel = new ChartPanel("Nylocas", true, config, clientThread, configManager);
        sotetsegPanel = new ChartPanel("Sotetseg", true, config, clientThread, configManager);
        xarpPanel = new ChartPanel("Xarpus", true, config, clientThread, configManager);
        verzPanel = new ChartPanel("Verzik", true, config, clientThread,  configManager);

        maidenScroll = new JScrollPane(maidenPanel);
        bloatScroll = new JScrollPane(bloatPanel);
        nyloScroll = new JScrollPane(nyloPanel);
        sotetsegScroll = new JScrollPane(sotetsegPanel);
        xarpusScroll = new JScrollPane(xarpPanel);
        verzikScroll = new JScrollPane(verzPanel);


        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(cl->
        {
            String activePanel = names[tabbedPane.getSelectedIndex()];
            for(String panelNames : names)
            {
                if(activePanel.equals(panelNames))
                {
                    getPanel(panelNames).setActive(true);
                    getPanel(panelNames).redraw();
                }
                else
                {
                    getPanel(panelNames).setActive(false);
                }
            }
        });
        tabbedPane.add("Maiden", maidenScroll);
        tabbedPane.add("Bloat", bloatScroll);
        tabbedPane.add("Nylocas", nyloScroll);
        tabbedPane.add("Sotetseg", sotetsegScroll);
        tabbedPane.add("Xarpus", xarpusScroll);
        tabbedPane.add("Verzik", verzikScroll);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                super.windowClosing(e);
                for(String panelNames : names)
                {
                    getPanel(panelNames).setActive(false);
                }
            }

            @Override
            public void windowOpened(WindowEvent e)
            {
                super.windowOpened(e);
                String activePanel = names[tabbedPane.getSelectedIndex()];
                getPanel(activePanel).setActive(true);
                getPanel(activePanel).redraw();
            }
        });
        add(tabbedPane);
        pack();
    }

    @Override
    public void close()
    {
        super.close();
    }

    public ChartPanel getPanel(String room)
    {
        switch (room)
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
        return new ChartPanel("", true, config, clientThread, configManager);
    }

    public void incrementTick(String room)
    {
        getPanel(room).incrementTick();
        if (getPanel(room).endTick % 50 == 0)
        {
            maidenScroll.getViewport().setViewPosition(new Point(maidenPanel.getViewRect().x, maidenPanel.getViewRect().y));
            bloatScroll.getViewport().setViewPosition(new Point(bloatPanel.getViewRect().x, bloatPanel.getViewRect().y));
            nyloScroll.getViewport().setViewPosition(new Point(nyloPanel.getViewRect().x, nyloPanel.getViewRect().y));
            sotetsegScroll.getViewport().setViewPosition(new Point(sotetsegPanel.getViewRect().x, sotetsegPanel.getViewRect().y));
            xarpusScroll.getViewport().setViewPosition(new Point(xarpPanel.getViewRect().x, xarpPanel.getViewRect().y));
            verzikScroll.getViewport().setViewPosition(new Point(verzPanel.getViewRect().x, verzPanel.getViewRect().y));
        }
    }


    public void addAttack(PlayerDidAttack attack, String room)
    {
        getPanel(room).addLiveAttack(attack);
    }

    public void addMaidenLine(int value, String description)
    {
        maidenPanel.addLine(value, description);
    }

    public void addBloatLine(int value, String description)
    {
        bloatPanel.addLine(value, description);
    }

    public void addNyloLine(int value, String description)
    {
        nyloPanel.addLine(value, description);
    }

    public void addSoteLine(int value, String description)
    {
        sotetsegPanel.addLine(value, description);
    }

    public void addXarpLine(int value, String description)
    {
        xarpPanel.addLine(value, description);
    }

    public void addVerzikLine(int value, String description)
    {
        verzPanel.addLine(value, description);
    }

    public void setMaidenFinished(int tick)
    {
        maidenPanel.setRoomFinished(tick);
    }

    public void setBloatFinished(int tick)
    {
        bloatPanel.setRoomFinished(tick);
    }

    public void setNyloFinished(int tick)
    {
        nyloPanel.setRoomFinished(tick);
    }

    public void setSoteFinished(int tick)
    {
        sotetsegPanel.setRoomFinished(tick);
    }

    public void setXarpFinished(int tick)
    {
        xarpPanel.setRoomFinished(tick);
    }

    public void setVerzFinished(int tick)
    {
        verzPanel.setRoomFinished(tick);
    }

    public void resetAll()
    {
        maidenPanel.resetGraph();
        bloatPanel.resetGraph();
        nyloPanel.resetGraph();
        sotetsegPanel.resetGraph();
        xarpPanel.resetGraph();
        verzPanel.resetGraph();

        maidenScroll.getVerticalScrollBar().setValue(0);
        bloatScroll.getVerticalScrollBar().setValue(0);
        nyloScroll.getVerticalScrollBar().setValue(0);
        sotetsegScroll.getVerticalScrollBar().setValue(0);
        xarpusScroll.getVerticalScrollBar().setValue(0);
        verzikScroll.getVerticalScrollBar().setValue(0);
    }

    public void redrawAll()
    {
        maidenPanel.redraw();
        bloatPanel.redraw();
        nyloPanel.redraw();
        sotetsegPanel.redraw();
        xarpPanel.redraw();
        verzPanel.redraw();
    }

    public void setPlayers(ArrayList<String> players)
    {
        ArrayList<String> cleanedPlayers = new ArrayList<>();
        for (String s : players)
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
