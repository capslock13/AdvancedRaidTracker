package com.advancedraidtracker.ui.statistics;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.RoomUtil;
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

    public StatisticRoomPanel(List<Raid> data, stat type, RaidRoom room, AdvancedRaidTrackerConfig config)
    {
        super();
        JPanel subPanel = getThemedPanel();
        setBackground(config.primaryDark());
        setOpaque(true);
        this.type = type;
        timeLabels = new ArrayList<>();
        ArrayList<JLabel> nameLabels = new ArrayList<>();
        labelNames = DataPoint.getTimeNamesByRoom(room);
        for (String s : labelNames)
        {
            nameLabels.add(getThemedLabel(s.substring(s.indexOf(' ') + 1), SwingConstants.LEFT));
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
        setLayout(new BorderLayout());
        TitledBorder border = BorderFactory.createTitledBorder(borderString);
        border.setTitleColor(config.fontColor());
        setBorder(border);
        subPanel.setLayout(new GridLayout(0, 2));
        for (int i = 0; i < labelNames.size(); i++)
        {
            subPanel.add(nameLabels.get(i));
            subPanel.add(timeLabels.get(i));
        }
        for (int i = labelNames.size(); i < 14; i++)
        {
            subPanel.add(getThemedLabel(""));
            subPanel.add(getThemedLabel(""));
        }
        subPanel.setPreferredSize(new Dimension(100, 200));
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
                if (d.getTimeAccurate(Objects.requireNonNull(DataPoint.getValue(labelNames.get(i)))))
                {
                    if (Objects.requireNonNull(DataPoint.getValue(labelNames.get(i))).type != DataPoint.types.TIME || d.get(labelNames.get(i)) != 0)
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
