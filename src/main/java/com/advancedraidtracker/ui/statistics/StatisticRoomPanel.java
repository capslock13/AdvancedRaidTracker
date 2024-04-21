package com.advancedraidtracker.ui.statistics;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.RoomUtil;
import static com.advancedraidtracker.utility.RoomUtil.isTime;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.StatisticGatherer;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.advancedraidtracker.utility.UISwingUtility.*;


@Slf4j
public class StatisticRoomPanel extends JPanel
{
    public enum stat
    {
        AVERAGE,
        MEDIAN,
        MODE,
        MINIMUM,
        MAXIMUM
    }

    private final List<JLabel> timeLabels;
    private final List<String> labelNames;
    private final stat type;

    public StatisticRoomPanel(List<Raid> data, stat type, String roomName, AdvancedRaidTrackerConfig config)
    {
        super();
		RaidRoom room = RaidRoom.getRoom(roomName);
        JPanel subPanel = getThemedPanel();
        setBackground(config.primaryDark());
        setOpaque(true);
        this.type = type;
        timeLabels = new ArrayList<>();
        List<JLabel> nameLabels = new ArrayList<>();
		if(roomName.contains("Wave") && !roomName.contains("Col"))
		{
			labelNames = new ArrayList<>();
			labelNames.add("Inf " + roomName + " Split");
			labelNames.add("Inf " + roomName + " Time");
			for(RaidRoom raidRoom : RoomUtil.getVariations("Inf " + roomName))
			{
				labelNames.add(raidRoom.name + " Split");
				labelNames.add(raidRoom.name + " Time");
			}
		}
		else
		{
			labelNames = DataPoint.getTimeNamesByRoom(room);
		}
        for (String s : labelNames)
        {
			String name = (s.startsWith("Inf") || s.startsWith("Col")) ? s : s.substring(s.indexOf(' ') + 1);
            nameLabels.add(getThemedLabel(name, SwingConstants.LEFT));
            timeLabels.add(getThemedLabel("-", SwingConstants.RIGHT));
        }
        String borderString = "";
        switch (type)
        {
            case AVERAGE:
                borderString = "Average";
                break;
            case MEDIAN:
                borderString = "Median";
                break;
            case MODE:
                borderString = "Mode";
                break;
            case MAXIMUM:
                borderString = "Maximum";
                break;
            case MINIMUM:
                borderString = "Minimum";
                break;
        }
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        TitledBorder border = BorderFactory.createTitledBorder(borderString);
        border.setTitleColor(config.fontColor());
        setBorder(border);
        subPanel.setLayout(new GridLayout(0, 1));
        for (int i = 0; i < labelNames.size(); i++)
        {
			JPanel labelSubPanel = getThemedPanel();
			labelSubPanel.setLayout(new BorderLayout());
            labelSubPanel.add(nameLabels.get(i), BorderLayout.WEST);
			labelSubPanel.add(timeLabels.get(i), BorderLayout.EAST);
			subPanel.add(labelSubPanel);
        }
        for (int i = labelNames.size(); i < 14; i++)
        {
            subPanel.add(getThemedLabel(""));
        }
        subPanel.setPreferredSize(new Dimension(100, 250));
        JScrollPane scrollPane = getThemedScrollPane(subPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane);
        updateLabels(data);
    }

    public void updateLabels(java.util.List<Raid> data)
    {
        for (int i = 0; i < labelNames.size(); i++)
        {
            ArrayList<Integer> collectedData = new ArrayList<>();
            for (Raid d : data)
            {
                if (d.getTimeAccurate(labelNames.get(i)))
                {
                    if (!isTime(labelNames.get(i)) || d.get(labelNames.get(i)) != 0)
                    {
                        collectedData.add(d.get(labelNames.get(i)));
                    }
                }
            }
            double statistic = 0;
            switch (type)
            {
                case AVERAGE:
                    statistic = StatisticGatherer.getGenericAverage(collectedData);
                    break;
                case MEDIAN:
                    statistic = StatisticGatherer.getGenericMedian(collectedData);
                    break;
                case MODE:
                    statistic = StatisticGatherer.getGenericMode(collectedData);
                    break;
                case MINIMUM:
                    statistic = StatisticGatherer.getGenericMin(collectedData, true);
                    break;
                case MAXIMUM:
                    statistic = StatisticGatherer.getGenericMax(collectedData);
                    break;
            }
            timeLabels.get(i).setText(RoomUtil.time(statistic));
        }
        validate();
        repaint();
    }

}
