package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.wrappers.PlayerDidAttack;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class LiveChart extends BaseFrame
{
    public JTabbedPane tabbedPane;

    private final AdvancedRaidTrackerConfig config;
    private final ItemManager itemManager;
    private final ClientThread clientThread;
    private final ConfigManager configManager;
    String[] tob = {"Maiden", "Bloat", "Nylocas", "Sotetseg", "Xarpus", "Verzik"};
    String[] toa = {"Apmeken", "Baba", "Scabaras", "Kephri", "Crondis", "Zebak", "Het", "Akkha", "Wardens"};
    Map<String, ChartPanel> toaPanels;
    Map<String, ChartPanel> tobPanels;
    Map<String, JScrollPane> toaScrollPanes;
    Map<String, JScrollPane> tobScrollPanes;

    public LiveChart(AdvancedRaidTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager)
    {
        this.configManager = configManager;
        this.clientThread = clientThread;
        this.config = config;
        this.itemManager = itemManager;

        toaPanels = new LinkedHashMap<>();
        tobPanels = new LinkedHashMap<>();
        toaScrollPanes = new LinkedHashMap<>();
        tobScrollPanes = new LinkedHashMap<>();

        for (String name : tob)
        {
            ChartPanel chartPanel = new ChartPanel(name, true, config, clientThread, configManager, itemManager);
            tobPanels.put(name, chartPanel);
            tobScrollPanes.put(name, new JScrollPane(chartPanel));
        }

        for (String name : toa)
        {
            ChartPanel chartPanel = new ChartPanel(name, true, config, clientThread, configManager, itemManager);
            toaPanels.put(name, chartPanel);
            toaScrollPanes.put(name, new JScrollPane(chartPanel));
        }

        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(cl ->
        {
            for (String name : tobPanels.keySet())
            {
                tobPanels.get(name).redraw();
            }
            for (String name : toaPanels.keySet())
            {
                toaPanels.get(name).redraw();
            }
        });

        for (String name : tobScrollPanes.keySet())
        {
            tabbedPane.add(name, tobScrollPanes.get(name));
        }

        add(tabbedPane);
        pack();
    }

    public void switchToTOB()
    {
        tabbedPane.removeAll();
        for (String name : tobScrollPanes.keySet())
        {
            tabbedPane.add(name, tobScrollPanes.get(name));
        }
    }

    public void switchToTOA()
    {
        tabbedPane.removeAll();
        for (String name : toaScrollPanes.keySet())
        {
            tabbedPane.add(name, toaScrollPanes.get(name));
        }
    }

    public ChartPanel getPanel(String room)
    {
        if (tobPanels.containsKey(room))
        {
            return tobPanels.get(room);
        } else if (toaPanels.containsKey(room))
        {
            return toaPanels.get(room);
        } else
        {
            return new ChartPanel("", true, config, clientThread, configManager, itemManager);
        }
    }

    public void incrementTick(String room)
    {
        getPanel(room).incrementTick();
        if (getPanel(room).endTick % 50 == 0)
        {
            for (String name : tobPanels.keySet())
            {
                tobScrollPanes.get(name).getViewport().setViewPosition(new Point(tobPanels.get(name).getViewRect().x, tobPanels.get(name).getViewRect().y));
            }
            for (String name : toaPanels.keySet())
            {
                toaScrollPanes.get(name).getViewport().setViewPosition(new Point(toaPanels.get(name).getViewRect().x, toaPanels.get(name).getViewRect().y));
            }
        }
    }

    public void addAttack(PlayerDidAttack attack, String room)
    {
        getPanel(room).addLiveAttack(attack);
    }

    public void addLine(String room, int value, String description)
    {
        getPanel(room).addLine(value, description);
    }

    public void setRoomFinished(String room, int tick)
    {
        getPanel(room).setRoomFinished(tick);
    }

    public void resetAll()
    {
        for (String name : tobPanels.keySet())
        {
            tobScrollPanes.get(name).getVerticalScrollBar().setValue(0);
            tobPanels.get(name).resetGraph();
        }
        for (String name : toaPanels.keySet())
        {
            toaScrollPanes.get(name).getVerticalScrollBar().setValue(0);
            toaPanels.get(name).resetGraph();
        }
    }

    public void redrawAll()
    {
        for (String name : tobPanels.keySet())
        {
            tobPanels.get(name).redraw();
        }
        for (String name : toaPanels.keySet())
        {
            toaPanels.get(name).redraw();
        }
    }

    public void setPlayers(ArrayList<String> players)
    {
        ArrayList<String> cleanedPlayers = new ArrayList<>();
        for (String s : players)
        {
            cleanedPlayers.add(s.replaceAll(String.valueOf((char) 160), String.valueOf((char) 32)));
        }
        for (String name : tobPanels.keySet())
        {
            tobPanels.get(name).setPlayers(cleanedPlayers);
        }
        for (String name : toaPanels.keySet())
        {
            toaPanels.get(name).setPlayers(cleanedPlayers);
        }

    }
}
