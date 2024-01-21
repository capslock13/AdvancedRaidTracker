package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.utility.WeaponAttack;

import javax.swing.*;
import java.awt.*;

public class RoomChartLegend extends JPanel
{
    public RoomChartLegend()
    {
        int rows = (WeaponAttack.values().length/2)+1;
        setLayout(new GridLayout(rows, 2));
        for(WeaponAttack attack : WeaponAttack.values())
        {
            add(new JLabel(attack.name));
        }
    }
}
