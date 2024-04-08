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
import com.advancedraidtracker.utility.weapons.PlayerAnimation;
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

    public static final Map<Integer, Integer> websiteIDMap = Map.of(
            7692, 1,
            7693, 2,
            7697, 5,
            7698, 6, 7702, 6,
            7699, 7, 7703, 7);

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
        topPanel.setPreferredSize(new Dimension(900, 100));

        JTabbedPane tabbedPane = getThemedTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(900, 500));


        tabbedPane.addTab("Basic View", getBasicPanel());
        tabbedPane.addTab("Detailed View", getDetailedPanel());
        setPreferredSize(new Dimension(900, 600));

        add(topPanel);
        add(tabbedPane);
        setResizable(false);
        pack();
    }

    public int getChartDataAttackSum(int startWave, int endWave, PlayerAnimation... animations)
    {
        int sum = 0;
        for (int i = startWave; i < endWave; i++)
        {
            sum += chartData.getWeaponHits(RaidRoom.getRoom("Inf Wave " + i), animations);
        }
        return sum;
    }

    public int getDataPointSum(DataPoint point, int startWave, int endWave)
    {
        int sum = 0;
        for (int i = startWave; i < endWave; i++)
        {
            sum += infData.get(point, RaidRoom.getRoom("Inf Wave " + i));
        }
        return sum;
    }

    public int getTimeThroughWave(int wave)
    {
            return 0;
    }


    public int getIdleTickSum(int startWave, int endWave)
    {
        int sum = 0;
        for (int i = startWave; i < endWave; i++)
        {
            int waveTime = 0;
            java.util.List<Integer> waveDurations = infData.getList(DataPoint.INFERNO_WAVE_DURATIONS);
            if (waveDurations.size() > i - 1)
            {
                waveTime = waveDurations.get(i - 1);
            }
            if(waveTime > 0)
            {
                sum += chartData.getIdleTicks(infData.getPlayerString(), RaidRoom.getRoom("Inf Wave " + i), infData.getScale());
            }
        }
        return sum;
    }

    private JPanel getBasicPanel()
    {
        JPanel base = getThemedPanel();
        base.setLayout(new GridLayout(0, 4));

        for (String wave : InfernoHandler.roomMap.values())
        {
            int start = 0;
            int end = 0;
            if (wave.contains("-"))
            {
                start = Integer.parseInt(wave.split(" ")[1].split("-")[0]);
                end = Integer.parseInt(wave.split(" ")[1].split("-")[1]);
            } else
            {
                start = Integer.parseInt(wave.split(" ")[1]);
                end = start;
            }
            int split = infData.waveStarts.getOrDefault(start, -1);
            int nextSplit = infData.waveStarts.getOrDefault(end+1, -1);
            String title = (split > 0) ? green + wave + " - " + RoomUtil.time(split) : red + wave;
            if(split > 0 && nextSplit > 0)
            {
                title += " (+" + RoomUtil.time(nextSplit-split) +")";
            }

            JPanel panel = getTitledPanel(title);
            panel.setPreferredSize(new Dimension(200, 125));
            panel.setLayout(new GridLayout(0, 1));

            panel.add(getThemedLabel("<html>Prayer Used: " + blue + getDataPointSum(DataPoint.PRAYER_USED, start, end)));
            panel.add(getThemedLabel("<html>Damage Dealt: " + green + getDataPointSum(DataPoint.DAMAGE_DEALT, start, end)));
            panel.add(getThemedLabel("<html>Damage Received: " + red + getDataPointSum(DataPoint.DAMAGE_RECEIVED, start, end)));
            panel.add(getThemedLabel("Idle Ticks: " + getIdleTickSum(start, end)));
            panel.add(getThemedLabel("Chins Thrown: " + getChartDataAttackSum(start, end, PlayerAnimation.BLACK_CHIN, PlayerAnimation.RED_CHIN, PlayerAnimation.GRAY_CHIN)));
            panel.add(getThemedLabel("BP Hits: " + getChartDataAttackSum(start, end, PlayerAnimation.BLOWPIPE, PlayerAnimation.BLOWPIPE_SPEC)));
            panel.add(getThemedLabel("Barrage Casts: " + getChartDataAttackSum(start, end, PlayerAnimation.BARRAGE)));
            panel.add(getThemedLabel("Scythe Swings: " + getChartDataAttackSum(start, end, PlayerAnimation.SCYTHE)));
            panel.add(getThemedLabel("Claw Specs: " + getChartDataAttackSum(start, end, PlayerAnimation.CLAW_SPEC)));
            panel.add(getThemedLabel("TBow Shots: " + getChartDataAttackSum(start, end, PlayerAnimation.TBOW)));

            base.add(panel);
        }

        return base;
    }

    private JScrollPane getDetailedPanel()
    {
        JPanel base = getThemedPanel();
        JScrollPane scrollPane = getThemedScrollPane(base);
        base.setLayout(new GridLayout(0, 3));
        for (int i = 1; i < 69; i++)
        {
            base.add(getWaveBox(i));
        }
        return scrollPane;
    }

    private String getColor(int wave)
    {
        String color = red;
        if (infData.getList(DataPoint.INFERNO_WAVE_DURATIONS).size() > wave - 1)
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
        if (waveDurations.size() > wave - 1)
        {
            waveTime = waveDurations.get(wave - 1);
        }
        if (waveTime > 0)
        {
            title += " - " + RoomUtil.time(waveTime);
        }
        JPanel container = getTitledPanel(title);
        container.setPreferredSize(new Dimension(300, 200));
        container.setLayout(new GridLayout(1, 2));
        JPanel base = getThemedPanel();
        base.setLayout(new GridLayout(0, 1));
        StringBuilder spawnString = new StringBuilder("https://infernostats.github.io/inferno.html?");
        final BufferedImage colImage = ImageUtil.loadImageResource(AdvancedRaidTrackerPlugin.class, "/com/advancedraidtracker/inferno.png");
        Graphics2D g = (Graphics2D) colImage.getGraphics();
        g.setColor(RED);
        int respawnCount = 0;
        for (InfernoSpawn spawn : infData.spawns.get(wave))
        {
            try
            {
                int ID = spawn.getId();
                int size = spawnSize.getOrDefault(ID, 0);
                if (spawn.isRespawned())
                {
                    respawnCount++;
                    continue;
                }
                if (size == 0)
                {
                    continue;
                }
                Point p = new Point((spawn.getRegionX() - 17), spawn.getRegionY() - 17); //normalize to 0,0
                int websiteID = websiteIDMap.getOrDefault(ID, 0);
                spawnString.append(String.format("%02d", p.getX())).append(String.format("%02d", 29 - p.getY())).append(websiteID).append("."); //website format is "XX|YY|ID."
                g.setColor(websiteColorMap.getOrDefault(ID, new Color(0, 0, 0)));
                g.fillRect(8 * (p.getX()), 8 * (30 - p.getY() - (size)), 8 * size, 8 * size); //swing coordinate system starts top left
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
            base.add(getThemedLabel("<html>NPC Respawns: " + red + respawnCount));
            base.add(getThemedLabel("Chins Thrown: " + chartData.getWeaponHits(RaidRoom.getRoom("Inf Wave " + wave), PlayerAnimation.BLACK_CHIN, PlayerAnimation.RED_CHIN, PlayerAnimation.GRAY_CHIN)));
            base.add(getThemedLabel("BP Hits: " + chartData.getWeaponHits(RaidRoom.getRoom("Inf Wave " + wave), PlayerAnimation.BLOWPIPE, PlayerAnimation.BLOWPIPE_SPEC)));
            base.add(getThemedLabel("Barrage Casts: " + chartData.getWeaponHits(RaidRoom.getRoom("Inf Wave " + wave), PlayerAnimation.BARRAGE)));
            base.add(getThemedLabel("Scythe Swings: " + chartData.getWeaponHits(RaidRoom.getRoom("Inf Wave " + wave), PlayerAnimation.SCYTHE)));
            base.add(getThemedLabel("Claw Specs: " + chartData.getWeaponHits(RaidRoom.getRoom("Inf Wave " + wave), PlayerAnimation.CLAW_SPEC)));
            base.add(getThemedLabel("TBow Shots: " + chartData.getWeaponHits(RaidRoom.getRoom("Inf Wave " + wave), PlayerAnimation.TBOW)));
            container.add(base);

            JLabel picLabel = new JLabel(new ImageIcon(createFlipped(colImage).getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
            picLabel.setMaximumSize(new Dimension(140, 140));
            picLabel.setToolTipText("View spawn in website");
            picLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            String finalSpawnString = spawnString.toString();
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
