package com.cTimers.panelcomponents;

import com.cTimers.cRoomData;
import com.cTimers.utility.RoomUtil;
import com.cTimers.utility.cDataPoint;
import com.cTimers.utility.cStatisticGatherer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class cStatisticRoomPanel extends JPanel
{
    public static enum stat
    {
        AVERAGE,
        MEDIAN,
        MODE,
        MINIMUM,
        MAXIMUM
    }

    private final ArrayList<JLabel> timeLabels;
    private final ArrayList<String> labelNames;
    private final stat type;
    public cStatisticRoomPanel(ArrayList<cRoomData> data, stat type, cDataPoint.rooms room)
    {
        super();
        JPanel subPanel = new JPanel();
        this.type = type;
        timeLabels = new ArrayList<>();
        ArrayList<JLabel> nameLabels = new ArrayList<>();
        labelNames = cDataPoint.getTimeNamesByRoom(room);
        for(String s : labelNames)
        {
            nameLabels.add(new JLabel(s.substring(s.indexOf(' ')+1), SwingConstants.LEFT));
            timeLabels.add(new JLabel("-", SwingConstants.RIGHT));
        }
        String borderString = "";
        switch(type)
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
        setBorder(BorderFactory.createTitledBorder(borderString));
        subPanel.setLayout(new GridLayout(7, 2));
        for(int i = 0; i < labelNames.size(); i++)
        {
            subPanel.add(nameLabels.get(i));
            subPanel.add(timeLabels.get(i));
        }
        for(int i = labelNames.size(); i < 7; i++)
        {
            subPanel.add(new JLabel(""));
            subPanel.add(new JLabel(""));
        }
        add(subPanel);
        updateLabels(data);
    }

    public void updateLabels(ArrayList<cRoomData> data)
    {
        for(int i = 0; i < labelNames.size(); i++)
        {
            ArrayList<Integer> collectedData = new ArrayList<>();
            for (cRoomData d : data)
            {
                if(d.getTimeAccurate(cDataPoint.getValue(labelNames.get(i))))
                {
                    collectedData.add(d.getValue(labelNames.get(i)));
                }
            }
            double statistic = 0;
            switch(type)
            {
                case AVERAGE:
                    statistic = cStatisticGatherer.getGenericAverage(collectedData);
                    break;
                case MEDIAN:
                    statistic = cStatisticGatherer.getGenericMedian(collectedData);
                    break;
                case MODE:
                    statistic = cStatisticGatherer.getGenericMode(collectedData);
                    break;
                case MINIMUM:
                    statistic = cStatisticGatherer.getGenericMin(collectedData);
                    break;
                case MAXIMUM:
                    statistic = cStatisticGatherer.getGenericMax(collectedData);
                    break;
            }
            timeLabels.get(i).setText(RoomUtil.time(statistic));
        }
        validate();
        repaint();
    }

}
