package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.wrappers.PlayerDidAttack;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.advancedraidtracker.constants.RaidType.*;

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
    String[] col = {"Wave 1", "Wave 2", "Wave 3", "Wave 4", "Wave 5", "Wave 6", "Wave 7", "Wave 8", "Wave 9", "Wave 10", "Wave 11", "Wave 12"};
    Map<String, ChartPanel> toaPanels;
    Map<String, ChartPanel> tobPanels;
    Map<String, ChartPanel> colPanels;
    RaidType activeRaid = RaidType.UNASSIGNED;
    Map<String, ChartPanel> currentPanels;

    public LiveChart(AdvancedRaidTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager)
    {
        this.configManager = configManager;
        this.clientThread = clientThread;
        this.config = config;
        this.itemManager = itemManager;

        toaPanels = new LinkedHashMap<>();
        tobPanels = new LinkedHashMap<>();
        colPanels = new LinkedHashMap<>();
        currentPanels = tobPanels;

        for (String name : tob)
        {
            ChartPanel chartPanel = new ChartPanel(name, true, config, clientThread, configManager, itemManager);
            tobPanels.put(name, chartPanel);
        }

        for (String name : toa)
        {
            ChartPanel chartPanel = new ChartPanel(name, true, config, clientThread, configManager, itemManager);
            toaPanels.put(name, chartPanel);
        }

        for (String name : col)
        {
            ChartPanel chartPanel = new ChartPanel(name, true, config, clientThread, configManager, itemManager);
            colPanels.put(name, chartPanel);
        }

        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(cl -> //todo test this revised logic
        {
            String activePanel = null;
            if(tabbedPane.getSelectedIndex() == -1)
            {
                return;
            }
            switch (activeRaid)
            {
                case TOB:
                    activePanel = tob[tabbedPane.getSelectedIndex()];
                    currentPanels = tobPanels;
                    break;
                case TOA:
                    activePanel = toa[tabbedPane.getSelectedIndex()];
                    currentPanels = toaPanels;
                    break;
                case COLOSSEUM:
                    activePanel = col[tabbedPane.getSelectedIndex()];
                    currentPanels = colPanels;
                    break;
            }
            currentPanels.values().forEach(panel->panel.setActive(false));
            if(activePanel == null)
            {
                return;
            }
            //Because we programatically switch tabs when region changes, which fires a tab changed event, we don't want to set the tab active
            //if the user has the window closed
            if(isShowing())
            {
                currentPanels.get(activePanel).setActive(true);
                currentPanels.get(activePanel).redraw();
            }
        });

        for (String panel : tobPanels.keySet())
        {
            tabbedPane.add(panel, tobPanels.get(panel));
        }
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowOpened(WindowEvent e)
            {
                super.windowOpened(e);
                String activePanel = null;
                if(tabbedPane.getSelectedIndex() == -1)
                {
                    return;
                }
                if(activeRaid.equals(TOB))
                {
                    activePanel = tob[tabbedPane.getSelectedIndex()];
                }
                else if(activeRaid.equals(TOA))
                {
                    activePanel = toa[tabbedPane.getSelectedIndex()];
                }
                else if(activeRaid.equals(COLOSSEUM))
                {
                    activePanel = col[tabbedPane.getSelectedIndex()];
                }
                if(activePanel == null)
                {
                    return;
                }
                getPanel(activePanel).setActive(true);
                getPanel(activePanel).redraw();
                pack();
            }

            @Override
            public void windowClosing(WindowEvent e)
            {
                super.windowClosing(e);
                for (String name : tobPanels.keySet())
                {
                    tobPanels.get(name).setActive(false);
                }
                for (String name : toaPanels.keySet())
                {
                    toaPanels.get(name).setActive(false);
                }
                for (String name : colPanels.keySet())
                {
                    colPanels.get(name).setActive(false);
                }
            }
        });
        addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                super.componentResized(e);
                Component c = (Component) e.getSource();
                for(ChartPanel p : tobPanels.values())
                {
                    p.setSize(c.getWidth(), c.getHeight());
                }
                for(ChartPanel p : toaPanels.values())
                {
                    p.setSize(c.getWidth(), c.getHeight());
                }
                for(ChartPanel p : colPanels.values())
                {
                    p.setSize(c.getWidth(), c.getHeight());
                }
            }
        });
        add(tabbedPane);
        pack();
    }


    public void switchToTOB()
    {
        activeRaid = TOB;
        tabbedPane.removeAll();
        for (String name : tobPanels.keySet())
        {
            tabbedPane.add(name, tobPanels.get(name));
        }
    }

    public void switchToTOA()
    {
        activeRaid = TOA;
        tabbedPane.removeAll();
        for (String name : toaPanels.keySet())
        {
            tabbedPane.add(name, toaPanels.get(name));
        }
    }

    public void switchToCol()
    {
        activeRaid = COLOSSEUM;
        tabbedPane.removeAll();
        for(String name : colPanels.keySet())
        {
            tabbedPane.add(name, colPanels.get(name));
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
        }
        else if(colPanels.containsKey(room))
        {
            return colPanels.get(room);
        }
        else
        {
            return new ChartPanel("", true, config, clientThread, configManager, itemManager);
        }
    }

    public void incrementTick(String room)
    {
        getPanel(room).incrementTick();
        if (getPanel(room).endTick % 50 == 0)
        {
            for (ChartPanel panel : tobPanels.values())
            {
                panel.sendToBottom();
            }
            for (ChartPanel panel : toaPanels.values())
            {
                panel.sendToBottom();
            }
            for (ChartPanel panel : colPanels.values())
            {
                panel.sendToBottom();
            }
        }
    }

    public void addNPC(String room, int index, String name)
    {
        getPanel(room).addNPC(index, name);
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
            tobPanels.get(name).resetGraph();
        }
        for (String name : toaPanels.keySet())
        {
            toaPanels.get(name).resetGraph();
        }
        for (String name : colPanels.keySet())
        {
            colPanels.get(name).resetGraph();
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
        for (String name : colPanels.keySet())
        {
            colPanels.get(name).redraw();
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
            tobPanels.get(name).setAttackers(cleanedPlayers);
        }
        for (String name : toaPanels.keySet())
        {
            toaPanels.get(name).setAttackers(cleanedPlayers);
        }
        for (String name : colPanels.keySet())
        {
            colPanels.get(name).setAttackers(cleanedPlayers);
        }

    }
}