package com.advancedraidtracker.ui.charts.chartcreator;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.ui.charts.ChartPanel;
import com.advancedraidtracker.utility.weapons.PlayerAnimation;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.advancedraidtracker.utility.UISwingUtility.getThemedPanel;

public class ChartCreatorFrame extends BaseFrame
{
    private final ChartPanel chart;

    public ChartCreatorFrame(AdvancedRaidTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager, SpriteManager spriteManager)
    {
        setTitle("Chart Creator");

        chart = new ChartPanel("Creator", false, config, clientThread, configManager, itemManager, spriteManager);
        chart.setPreferredSize(new Dimension(0, 0));
        setPlayerCount(5);
        setEndTick(50);
        setStartTick(1);
        setPrimaryTool(PlayerAnimation.SCYTHE);
        setSecondaryTool(PlayerAnimation.NOT_SET);

        ChartTopMenuPanel menu = new ChartTopMenuPanel(this, config);
        menu.setBorder(BorderFactory.createTitledBorder("Menu"));
        menu.setPreferredSize(new Dimension(0, 50));

        ChartToolPanel tools = new ChartToolPanel(config, this, itemManager, clientThread, spriteManager);
        tools.setBorder(BorderFactory.createTitledBorder("Tools"));
        tools.setPreferredSize(new Dimension(350, 0));

        JPanel container = getThemedPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JPanel bottomContainer = getThemedPanel();
        bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.X_AXIS));

        bottomContainer.add(chart);
        bottomContainer.add(tools);

        container.add(menu);
        container.add(bottomContainer);

        add(container);
        setPreferredSize(new Dimension(1000, 600));
        pack();
        tools.build();
    }

    public void setPlayerCount(int players)
    {
        List<String> playerList = new ArrayList<>();
        for (int i = 1; i < players + 1; i++)
        {
            playerList.add("Player" + i);
        }
        chart.setAttackers(playerList);
        chart.redraw();
    }

    public void setStartTick(int tick)
    {
        chart.setStartTick(tick);
    }

    public void setEndTick(int tick)
    {
        chart.setTick(tick);
    }

    public void setPrimaryTool(PlayerAnimation tool)
    {
        chart.setPrimaryTool(tool);
    }

    public void setSecondaryTool(PlayerAnimation tool)
    {
        chart.setSecondaryTool(tool);
    }

    public void setEnforceCD(boolean bool)
    {
        chart.setEnforceCD(bool);
    }

    public void setToolSelection(int tool)
    {
        chart.setToolSelection(tool);
    }

    public void changeLineText(String text)
    {
        chart.setManualLineText(text);
    }
}
