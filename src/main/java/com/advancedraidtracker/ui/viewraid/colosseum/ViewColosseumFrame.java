package com.advancedraidtracker.ui.viewraid.colosseum;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.rooms.col.ColosseumHandler;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.Point;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.UISwingUtility;
import com.advancedraidtracker.utility.datautility.ChartData;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.DataReader;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.col.Colo;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.advancedraidtracker.rooms.col.ColosseumHandler.invoMap;
import static com.advancedraidtracker.utility.UISwingUtility.*;
import static com.advancedraidtracker.utility.UISwingUtility.colorStr;
import static java.awt.Color.RED;

@Slf4j
public class ViewColosseumFrame extends BaseFrame
{
    String red = "<html><font color='#FF0000'>";
    String green = "<html><font color='#33FF33'>";
    String blue = "<html><font color='#6666DD'>";
    String orange = "<html><font color='#ddaa1c'>";
    String full;
    String soft;
    String dark;
    private final Colo colData;
    private final ItemManager itemManager;
    private final ChartData chartData;
    public ViewColosseumFrame(Colo colData, AdvancedRaidTrackerConfig config, ItemManager itemManager)
    {
        this.itemManager = itemManager;
        chartData = DataReader.getChartData(colData.getFilepath(), itemManager);
        Color c = config.fontColor();
        full = colorStr(c);
        soft = colorStr(c.darker());
        dark = colorStr(c.darker().darker());

        this.colData = colData;
        for(int i = 1; i < 13; i++)
        {
            log.info("Wave " + i + " spawn data: " + colData.getSpawnString(i));
        }
        setTitle("View Raid");
        setPreferredSize(new Dimension(900, 800));
        //add(new ViewColosseumPanel(colData, config));

        JPanel topContainer = getThemedPanel();
        topContainer.setPreferredSize(new Dimension(900, 100));
        topContainer.setLayout(new GridLayout(1, 2));
        topContainer.add(getSummaryBox());
        //topContainer.add(getSolHereditBox());
        JPanel bottomContainer = getThemedPanel();
        bottomContainer.setPreferredSize(new Dimension(900, 650));
        bottomContainer.setLayout(new GridLayout(4,3));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        for(int i = 0; i < 12; i++)
        {
            bottomContainer.add(getWaveBox(i+1));
        }
        add(topContainer);
        add(bottomContainer);
        setResizable(false);
        pack();
    }

    public JPanel getWaveBox(int wave)
    {
        String title = getColor(wave) + "Wave " + wave;
        int waveTime = colData.get("Wave " + wave + " Duration");
        if(waveTime > 0)
        {
            title += " - " + RoomUtil.time(waveTime);
        }
        JPanel container = getTitledPanel(title);
        container.setLayout(new GridLayout(1, 2));
        JPanel base = getThemedPanel();
        base.setLayout(new GridLayout(0, 1));
        String[] split = colData.getSpawnString(wave).split("");
        String spawnString = "https://supalosa.github.io/osrs-colosseum/?";
        final BufferedImage colImage = ImageUtil.loadImageResource(AdvancedRaidTrackerPlugin.class, "/com/advancedraidtracker/colosseum.png");
        Graphics2D g = (Graphics2D) colImage.getGraphics();
        g.setColor(RED);
        for(String s : split)
        {
            try
            {
                int ID = ColosseumHandler.mapToWebsiteIDs.getOrDefault(ColosseumHandler.getId(s), -1);
                Point p = ColosseumHandler.mapToWebsitePoint(ColosseumHandler.getCoordinates(s));
                spawnString += String.format("%02d", p.getX()) + String.format("%02d", p.getY()) + ID + ".";
                int size = ColosseumHandler.spawnSize.getOrDefault(ID, 0);
                g.setColor(ColosseumHandler.websiteColorMap.get(ID));
                g.drawRect(8*(p.getX()-1)-2, 8*(p.getY()-size), 8*size, 8*size);
            }
            catch (Exception ignore)
            {

            }
        }
        if(colData.highestWaveStarted >= wave || colData.isCompleted())
        {
            base.add(getThemedLabel("<html>" + blue + "Invocations: "));
            for (String invo : colData.invocationsOffered.get(wave))
            {
                if (invo.equals(colData.invocationSelected.get(wave)))
                {
                    base.add(getThemedLabel("<html>&emsp" + green + getNextHighestLevelOfInvo(invoMap.get(Integer.parseInt(invo)), wave)));
                } else
                {
                    base.add(getThemedLabel("<html>&emsp" + UISwingUtility.fontColorString() + getNextHighestLevelOfInvo(invoMap.get(Integer.parseInt(invo)), wave)));
                }
            }
            base.add(getThemedLabel("<html>Prayer Used: " + blue + colData.get(DataPoint.PRAYER_USED, RaidRoom.getRoom("Wave " + wave))));
            base.add(getThemedLabel("<html>Damage Dealt: " + green + colData.get(DataPoint.DAMAGE_DEALT, RaidRoom.getRoom("Wave " + wave))));
            base.add(getThemedLabel("<html>Damage Received: " + red + colData.get(DataPoint.DAMAGE_RECEIVED, RaidRoom.getRoom("Wave " + wave))));
            int idleTicks = chartData.getIdleTicks(colData.getPlayerString(), RaidRoom.getRoom("Wave " + wave), colData.getScale());
            base.add(getThemedLabel("Idle Ticks: " + idleTicks));
            base.add(getThemedLabel("<html>NPC Heals: " + red + colData.get(DataPoint.COLOSSEUM_NPC_HEALED, RaidRoom.getRoom("Wave " + wave))));
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

    public JPanel getSummaryBox()
    {
        //String title = ((colData.isCompleted()) ? green : red) + "Summary - " + RoomUtil.time(colData.getChallengeTime());
        JPanel base = getThemedPanel();
        //base.add(getThemedLabel("Total Idle Ticks: " + chartData.getIdleTicks(colData.getPlayerString(), colData.getScale())));
        base.setLayout(new GridLayout(1, 3));

        JPanel wave1to6 = getTitledPanel("Wave 1-6");
        wave1to6.setLayout(new GridLayout(0, 1));
        wave1to6.add(getThemedLabel("<html>Duration: " + green + RoomUtil.time(getWaveSequenceTime(1, 6))));
        wave1to6.add(getThemedLabel("<html>Prayer Used: " + blue + getWaveSequenceData(DataPoint.PRAYER_USED, 1, 6)));
        wave1to6.add(getThemedLabel("<html>Damage Dealt: " + green + getWaveSequenceData(DataPoint.DAMAGE_DEALT, 1, 6)));
        wave1to6.add(getThemedLabel("<html>Damage Received: " + red + getWaveSequenceData(DataPoint.DAMAGE_RECEIVED, 1, 6)));
        wave1to6.add(getThemedLabel("Idle Ticks: " + getWaveSequenceIdleTicks(1, 6)));

        JPanel wave7to11 = getTitledPanel("Wave 7-11");
        wave7to11.setLayout(new GridLayout(0, 1));
        wave7to11.add(getThemedLabel("<html>Duration: " + green + RoomUtil.time(getWaveSequenceTime(7, 11))));
        wave7to11.add(getThemedLabel("<html>Prayer Used: " + blue + getWaveSequenceData(DataPoint.PRAYER_USED, 7, 11)));
        wave7to11.add(getThemedLabel("<html>Damage Dealt: " + green + getWaveSequenceData(DataPoint.DAMAGE_DEALT, 7, 11)));
        wave7to11.add(getThemedLabel("<html>Damage Received: " + red + getWaveSequenceData(DataPoint.DAMAGE_RECEIVED, 7, 11)));
        wave7to11.add(getThemedLabel("Idle Ticks: " + getWaveSequenceIdleTicks(7, 11)));

        JPanel wave1to11 = getTitledPanel("Total");
        wave1to11.setLayout(new GridLayout(0, 1));
        wave1to11.add(getThemedLabel("<html>Duration: " + green + RoomUtil.time(getWaveSequenceTime(1, 12))));
        wave1to11.add(getThemedLabel("<html>Prayer Used: " + blue + getWaveSequenceData(DataPoint.PRAYER_USED, 1, 12)));
        wave1to11.add(getThemedLabel("<html>Damage Dealt: " + green + getWaveSequenceData(DataPoint.DAMAGE_DEALT, 1, 12)));
        wave1to11.add(getThemedLabel("<html>Damage Received: " + red + getWaveSequenceData(DataPoint.DAMAGE_RECEIVED, 1, 12)));
        wave1to11.add(getThemedLabel("Idle Ticks: " + getWaveSequenceIdleTicks(1, 12)));

        JPanel invocationPanel = getTitledPanel("Invocations");
        invocationPanel.setLayout(new GridLayout(0, 1));
        List<String> invoList = getInvocationList();
        for(String invo : invoList)
        {
            invocationPanel.add(getThemedLabel(invo));
        }
        /*if(invoList.size() < 10)
        {
            for (int i = 0; i < 10 - invoList.size(); i++)
            {
                invocationPanel.add(getThemedLabel());
            }
        }*/

        JPanel wavesPanel = getTitledPanel("Wave 1-11");
        wavesPanel.setLayout(new GridLayout(0, 1));
        wavesPanel.add(getThemedLabel("<html>Duration: " + green + RoomUtil.time(getWaveSequenceTime(1, 11))));
        wavesPanel.add(getThemedLabel("<html>Prayer: " + blue + getWaveSequenceData(DataPoint.PRAYER_USED, 1, 11)));
        wavesPanel.add(getThemedLabel("<html>Dealt : " + green + getWaveSequenceData(DataPoint.DAMAGE_DEALT, 1, 11)));
        wavesPanel.add(getThemedLabel("<html>Received: " + red + getWaveSequenceData(DataPoint.DAMAGE_RECEIVED, 1, 11)));
        wavesPanel.add(getThemedLabel("Idle Ticks: " + getWaveSequenceIdleTicks(1, 11)));

        JPanel solHereditPanel = getTitledPanel("Sol Heredit");
        solHereditPanel.setLayout(new GridLayout(0, 1));
        solHereditPanel.add(getThemedLabel("<html>Duration: " + green + RoomUtil.time(getWaveSequenceTime(12, 12))));
        solHereditPanel.add(getThemedLabel("<html>Grapples: " + blue + colData.get(DataPoint.COLOSSEUM_GRAPPLES)));
        solHereditPanel.add(getThemedLabel("<html>Perfect: " + green + colData.get(DataPoint.COLOSSEUM_PERFECT_PARRY)));
        solHereditPanel.add(getThemedLabel());
        solHereditPanel.add(getThemedLabel());


        base.add(wave1to11);
        base.add(wave1to6);
        base.add(wave7to11);
        base.add(wavesPanel);
        base.add(solHereditPanel);
        base.add(invocationPanel);
        return base;
    }

    public int getWaveSequenceTime(int startWave, int endWave)
    {
        int sum = 0;
        for(int i = startWave; i < endWave+1; i++)
        {
            sum += colData.get("Wave " + i + " Duration");
        }
        return sum;
    }

    public int getWaveSequenceIdleTicks(int startWave, int endWave)
    {
        int sum = 0;
        for(int i = startWave; i < endWave+1; i++)
        {
            int ticks = chartData.getIdleTicks(colData.getPlayerString(), RaidRoom.getRoom("Wave " + i), colData.getScale());
            if(ticks > 0)
            {
                sum += ticks;
            }
        }
        return sum;
    }

    public int getWaveSequenceData(DataPoint point, int startWave, int endWave)
    {
        int sum = 0;
        for(int i = startWave; i < endWave+1; i++)
        {
            sum += colData.get(point, RaidRoom.getRoom("Wave " + i));
        }
        return sum;
    }

    public String getNextHighestLevelOfInvo(String invo, int wave)
    {
        Map<String, Integer> invoListMap = new LinkedHashMap<>();
        for(int i = 1; i < wave; i++)
        {
            invoListMap.merge(invoMap.get(Integer.parseInt(colData.invocationSelected.get(i))), 1, Integer::sum);
        }
        for(String invocation : invoListMap.keySet())
        {
            if(invocation.equals(invo))
            {
                return invo + (invoListMap.get(invocation)+1);
            }
        }
        return invo;
    }

    private java.util.List<String> getInvocationList(int wave)
    {
        Map<String, Integer> invoListMap = new LinkedHashMap<>();
        for(int i = 1; i < wave+1; i++)
        {
            if(colData.invocationSelected.containsKey(i))
            {
                invoListMap.merge(invoMap.get(Integer.parseInt(colData.invocationSelected.get(i))), 1, Integer::sum);
            }
        }
        java.util.List<String> invos = new ArrayList<>();
        for(String invo : invoListMap.keySet())
        {
            if(invoListMap.get(invo) > 1)
            {
                invos.add(invo + invoListMap.get(invo));
            }
            else
            {
                invos.add(invo);
            }
        }
        return invos;
    }

    private java.util.List<String> getInvocationList()
    {
        return getInvocationList(12);
    }

    private String getColor(int wave)
    {
        String color = red;
        if(colData.get("Wave " + wave + " Duration") > 0)
        {
            color = green;
        }
        else if(colData.highestWaveStarted == wave)
        {
            color = orange;
        }
        return color;
    }

    private String getBodyColor(int wave)
    {
        String color = dark;
        if(colData.get("Wave " + wave + " Duration") > 0)
        {
            color = full;
        }
        return color;
    }
}
