package com.advancedraidtracker.ui.crableaks;

import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.wrappers.StringInt;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CrabLeakInfo extends BaseFrame
{
    public CrabLeakInfo(ArrayList<ArrayList<StringInt>> crabs)
    {
        JPanel primary = new JPanel();
        Map<String, Integer> crabLeakSums = new LinkedHashMap<>();
        primary.setBorder(BorderFactory.createTitledBorder("Crab Leak Info (Based on " + crabs.size() + " Raids)"));
        for (ArrayList<StringInt> crabData : crabs)
        {
            for (StringInt crab : crabData)
            {
                if (crabLeakSums.containsKey(crab.string))
                {
                    crabLeakSums.put(crab.string, crabLeakSums.get(crab.string) + crab.val);
                } else
                {
                    crabLeakSums.put(crab.string, crab.val);
                }
            }
        }

        primary.setLayout(new GridLayout(1, 0, 2, 2));

        JPanel panel70s = new JPanel();
        JPanel panel50s = new JPanel();
        JPanel panel30s = new JPanel();
        panel70s.setLayout(new GridLayout(0, 2, 2, 2));
        panel50s.setLayout(new GridLayout(0, 2, 2, 2));
        panel30s.setLayout(new GridLayout(0, 2, 2, 2));
        panel70s.setBorder(BorderFactory.createTitledBorder("70s"));
        panel50s.setBorder(BorderFactory.createTitledBorder("50s"));
        panel30s.setBorder(BorderFactory.createTitledBorder("30s"));

        Color color = new Color(30, 30, 30);
        for (int i = 0; i < 3; i++)
        {
            JLabel test = new JLabel("Crab");
            test.setOpaque(true);
            test.setBackground(Color.BLACK);

            JLabel test2 = new JLabel("Average HP", SwingConstants.RIGHT);
            test2.setOpaque(true);
            test2.setBackground(Color.BLACK);

            JPanel currentPanel = (i == 0) ? panel70s : (i == 1) ? panel50s : panel30s;

            currentPanel.add(test);
            currentPanel.add(test2);
            for (int j = 0; j < 10; j++)
            {
                int averageTemp = 0;
                String crabDescription = RoomUtil.MAIDEN_CRAB_NAMES[(i * 10) + j];
                try
                {
                    averageTemp = (int) ((crabLeakSums.get(RoomUtil.MAIDEN_CRAB_NAMES[(i * 10) + j]) / (double) crabs.size()) * 100);
                } catch (Exception ignored)
                {

                }
                double average = averageTemp / 100.0;

                JLabel desc = new JLabel(crabDescription, SwingConstants.LEFT);
                JLabel avg = new JLabel(String.valueOf(average), SwingConstants.RIGHT);

                if (j % 2 == 0)
                {
                    desc.setOpaque(true);
                    avg.setOpaque(true);
                    avg.setBackground(color);
                    desc.setBackground(color);
                }
                currentPanel.add(desc);
                currentPanel.add(avg);
            }
        }
        primary.add(panel70s);
        primary.add(panel50s);
        primary.add(panel30s);

        add(primary);
        pack();
        open();
    }
}
