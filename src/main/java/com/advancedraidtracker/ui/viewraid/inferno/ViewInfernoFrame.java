package com.advancedraidtracker.ui.viewraid.inferno;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.rooms.col.ColosseumHandler;
import com.advancedraidtracker.rooms.inf.InfernoHandler;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.ui.charts.ChartPanel;
import com.advancedraidtracker.utility.Point;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.UISwingUtility;
import com.advancedraidtracker.utility.datautility.ChartData;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.DataReader;
import com.advancedraidtracker.utility.datautility.datapoints.inf.Inf;
import com.advancedraidtracker.utility.datautility.datapoints.inf.InfernoSpawn;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.Map;

import static com.advancedraidtracker.rooms.col.ColosseumHandler.invoMap;
import static com.advancedraidtracker.utility.UISwingUtility.*;
import static java.awt.Color.BLACK;
import static java.awt.Color.RED;
@Slf4j
public class ViewInfernoFrame extends BaseFrame
{
    public static final Map<Integer, Integer> spawnSize = Map.of(
            7692, 2,
            7693, 3,
            7697, 4,
            7698, 3, 7702, 3,
            7699, 4, 7703, 4);

    public static final Map<Integer, Color> websiteColorMap = Map.of(
            7692, new Color(192, 192, 192),
            7693, new Color(255, 255, 128),
            7697, new Color(255, 165, 0),
            7698, new Color(0, 255, 0), 7702, new Color(0, 255, 0),
            7699, new Color(0, 255, 255), 7703, new Color(0, 255, 255)
            );
    String red = "<html><font color='#FF0000'>";
    String green = "<html><font color='#33FF33'>";
    String blue = "<html><font color='#6666DD'>";
    String orange = "<html><font color='#ddaa1c'>";
    String full;
    String soft;
    String dark;
    private final ChartData chartData;
    private Inf infData;
    public ViewInfernoFrame(Inf infData, AdvancedRaidTrackerConfig config, ItemManager itemManager)
    {
        this.infData = infData;
        this.chartData = DataReader.getChartData(infData.getFilepath(), itemManager);
        Color c = config.fontColor();
        full = colorStr(c);
        soft = colorStr(c.darker());
        dark = colorStr(c.darker().darker());
        setTitle("View Raid");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel topPanel = getThemedPanel();
        topPanel.setPreferredSize(new Dimension(800, 100));

        JTabbedPane tabbedPane = getThemedTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(800, 500));


        tabbedPane.addTab("Basic View", getBasicPanel());
        tabbedPane.addTab("Detailed View", getDetailedPanel());
        setPreferredSize(new Dimension(800, 600));

        add(topPanel);
        add(tabbedPane);
        setResizable(false);
        pack();
    }

    private JPanel getBasicPanel()
    {
        JPanel base = getThemedPanel();
        base.setLayout(new GridLayout(0, 4));

        for(String wave : InfernoHandler.roomMap.values())
        {
                JPanel panel = getTitledPanel(wave);
                panel.setPreferredSize(new Dimension(200, 125));
                base.add(panel);
        }

        return base;
    }

    private JScrollPane getDetailedPanel()
    {
        JPanel base = getThemedPanel();
        JScrollPane scrollPane = getThemedScrollPane(base);
        base.setLayout(new GridLayout(0, 3));
        for(int i = 1; i < 69; i++)
        {
            base.add(getWaveBox(i));
        }
        return scrollPane;
    }

    private String getColor(int wave)
    {
        String color = red;
        if (infData.getList(DataPoint.INFERNO_WAVE_DURATIONS).size() > wave-1)
        {
            color = green;
        } else if (infData.highestWaveStarted == wave)
        {
            color = orange;
        }
        return color;
    }

    public JPanel getWaveBox(int wave)
    {
        String title = getColor(wave) + "Wave " + wave;
        java.util.List<Integer> waveDurations = infData.getList(DataPoint.INFERNO_WAVE_DURATIONS);
        int waveTime = 0;
        if(waveDurations.size() > wave-1)
        {
            waveTime = waveDurations.get(wave-1);
        }
        if (waveTime > 0)
        {
            title += " - " + RoomUtil.time(waveTime);
        }
        JPanel container = getTitledPanel(title);
        container.setLayout(new GridLayout(1, 2));
        JPanel base = getThemedPanel();
        base.setLayout(new GridLayout(0, 1));
        String spawnString = "";
        final BufferedImage colImage = ImageUtil.loadImageResource(AdvancedRaidTrackerPlugin.class, "/com/advancedraidtracker/inferno.png");
        Graphics2D g = (Graphics2D) colImage.getGraphics();
        g.setColor(RED);
        for (InfernoSpawn spawn : infData.spawns.get(wave))
        {
            try
            {
                int ID = spawn.getId();
                int size = spawnSize.getOrDefault(ID, 0);
                if(size == 0)
                {
                    continue;
                }
                Point p = new Point(spawn.getRegionX()-17, spawn.getRegionY()-17);
                spawnString += String.format("%02d", p.getX()) + String.format("%02d", p.getY()) + ID + ".";
                g.setColor(websiteColorMap.getOrDefault(ID, new Color(0,0,0)));
                g.fillRect(8 * (p.getX() - 1) - 2, 8 * (p.getY() - size), 8 * size, 8 * size);
            } catch (Exception ignore)
            {

            }
        }
        if (infData.highestWaveStarted >= wave || infData.isCompleted())
        {
            base.add(getThemedLabel("<html>Prayer Used: " + blue + infData.get(DataPoint.PRAYER_USED, RaidRoom.getRoom("Inf Wave " + wave))));
            base.add(getThemedLabel("<html>Damage Dealt: " + green + infData.get(DataPoint.DAMAGE_DEALT, RaidRoom.getRoom("Inf Wave " + wave))));
            base.add(getThemedLabel("<html>Damage Received: " + red + infData.get(DataPoint.DAMAGE_RECEIVED, RaidRoom.getRoom("Inf Wave " + wave))));
            int idleTicks = chartData.getIdleTicks(infData.getPlayerString(), RaidRoom.getRoom("Inf Wave " + wave), infData.getScale());
            base.add(getThemedLabel("Idle Ticks: " + idleTicks));
            //base.add(getThemedLabel("<html>NPC Heals: " + red + inf.get(DataPoint.COLOSSEUM_NPC_HEALED, RaidRoom.getRoom("Wave " + wave))));
            container.add(base);

            JLabel picLabel = new JLabel(new ImageIcon(colImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
            picLabel.setMaximumSize(new Dimension(140, 140));
            picLabel.setToolTipText("View spawn in website");
            picLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            String finalSpawnString = spawnString;
            picLabel.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    try
                    {
                        Desktop.getDesktop().browse(new URI(finalSpawnString));
                    } catch (Exception ignored)
                    {

                    }
                }
            });
            container.add(picLabel);
        }
        return container;
    }
}
