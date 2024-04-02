package com.advancedraidtracker.ui.crableaks;

import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.wrappers.StringInt;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.advancedraidtracker.utility.UISwingUtility.*;

public class CrabLeakInfo extends BaseFrame
{
    public CrabLeakInfo(ArrayList<ArrayList<StringInt>> crabs)
    {
        JPanel primary = getTitledPanel("Crab Leak Info (Based on " + crabs.size() + " Raids)");
        Map<String, Integer> crabLeakSums = new LinkedHashMap<>();
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

        JPanel panel70s = getTitledPanel("70s");
        JPanel panel50s = getTitledPanel("50s");
        JPanel panel30s = getTitledPanel("30s");
        panel70s.setLayout(new GridLayout(0, 2, 2, 2));
        panel50s.setLayout(new GridLayout(0, 2, 2, 2));
        panel30s.setLayout(new GridLayout(0, 2, 2, 2));

        Color color = new Color(30, 30, 30);
        for (int i = 0; i < 3; i++)
        {
            JLabel test = getThemedLabel("Crab");
            test.setOpaque(true);
            test.setBackground(Color.BLACK);

            JLabel test2 = getThemedLabel("Average HP", SwingConstants.RIGHT);
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

                JLabel desc = getThemedLabel(crabDescription, SwingConstants.LEFT);
                JLabel avg = getThemedLabel(String.valueOf(average), SwingConstants.RIGHT);

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
