package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.wrappers.PlayerDidAttack;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class LiveChart extends BaseFrame
{
    public JTabbedPane tabbedPane;

    private final AdvancedRaidTrackerConfig config;
    private final ClientThread clientThread;
    private final ConfigManager configManager;
    Map<String, ChartPanel> tobPanels;


    private final String[] names = {"Maiden", "Bloat", "Nylocas", "Sotetseg", "Xarpus", "Verzik"};
    public LiveChart(AdvancedRaidTrackerConfig config, ClientThread clientThread, ConfigManager configManager)
    {
        this.configManager = configManager;
        this.clientThread = clientThread;
        this.config = config;
        tobPanels = new LinkedHashMap<>();
        for(String name : names)
        {
            tobPanels.put(name, new ChartPanel(name, true, config, clientThread, configManager));
        }

        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(cl->
        {
            String activePanel = names[tabbedPane.getSelectedIndex()];
            for(String panelNames : names)
            {
                if(activePanel.equals(panelNames) && this.isShowing())
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
        for(String panelName : tobPanels.keySet())
        {
            tabbedPane.add(panelName, tobPanels.get(panelName));
        }
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
                pack();
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
        return tobPanels.getOrDefault(room, new ChartPanel("", true, config, clientThread, configManager));
    }

    public void incrementTick(String room)
    {
        getPanel(room).incrementTick();
        if (getPanel(room).endTick % 50 == 0)
        {
            for(ChartPanel panel : tobPanels.values())
            {
                panel.sendToBottom();
            }
        }
    }


    public void addAttack(PlayerDidAttack attack, String room)
    {
        getPanel(room).addLiveAttack(attack);
    }

    public void addMaidenLine(int value, String description)
    {
        tobPanels.get("Maiden").addLine(value, description);
    }

    public void addBloatLine(int value, String description)
    {
        tobPanels.get("Bloat").addLine(value, description);
    }

    public void addNyloLine(int value, String description)
    {
        tobPanels.get("Nylocas").addLine(value, description);
    }

    public void addSoteLine(int value, String description)
    {
        tobPanels.get("Sotetseg").addLine(value, description);
    }

    public void addXarpLine(int value, String description)
    {
        tobPanels.get("Xarpus").addLine(value, description);
    }

    public void addVerzikLine(int value, String description)
    {
        tobPanels.get("Verzik").addLine(value, description);
    }

    public void setMaidenFinished(int tick)
    {
        tobPanels.get("Maiden").setRoomFinished(tick);
    }

    public void setBloatFinished(int tick)
    {
        tobPanels.get("Bloat").setRoomFinished(tick);
    }

    public void setNyloFinished(int tick)
    {
        tobPanels.get("Nylocas").setRoomFinished(tick);
    }

    public void setSoteFinished(int tick)
    {
        tobPanels.get("Sotetseg").setRoomFinished(tick);
    }

    public void setXarpFinished(int tick)
    {
        tobPanels.get("Xarpus").setRoomFinished(tick);
    }

    public void setVerzFinished(int tick)
    {
        tobPanels.get("Verzik").setRoomFinished(tick);
    }

    public void resetAll()
    {
        for(ChartPanel panel : tobPanels.values())
        {
            panel.resetGraph();
        }
    }

    public void redrawAll()
    {
        for(ChartPanel panel : tobPanels.values())
        {
            panel.redraw();
        }
    }

    public void setPlayers(ArrayList<String> players)
    {
        ArrayList<String> cleanedPlayers = new ArrayList<>();
        for (String s : players)
        {
            cleanedPlayers.add(s.replaceAll(String.valueOf((char) 160), String.valueOf((char) 32)));
        }
        for(ChartPanel panel : tobPanels.values())
        {
            panel.setPlayers(cleanedPlayers);
        }
    }
}
