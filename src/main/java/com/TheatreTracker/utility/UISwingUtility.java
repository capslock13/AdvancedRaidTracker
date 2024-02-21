package com.TheatreTracker.utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UISwingUtility
{
    public static Color primaryDark = new Color(20, 20, 20);

    public static String colorStr(Color c)
    {
        return "<html><font color='#" + Integer.toHexString(c.getRGB()).substring(2) + "'>";
    }
    public  final static String roomColor = colorStr(new Color(200, 200, 200));
    public static JLabel getDarkJLabel(String labelText)
    {
        JLabel darkLabel = new JLabel(labelText);
        return darkLabel;
    }

    public static JLabel getDarkJLabel(String labelText, int swingConstant)
    {
        JLabel darkLabel = new JLabel(labelText, swingConstant);
        return darkLabel;
    }

    public static JPanel getTitledPanel(String title)
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    public static JCheckBox getActionListenCheckBox(String name, ActionListener actionListener)
    {
        return getActionListenCheckBox(name, false, actionListener);
    }

    public static JCheckBox getActionListenCheckBox(String name, boolean state, ActionListener actionListener)
    {
        JCheckBox darkCheckBox = new JCheckBox(name, state);
        darkCheckBox.addActionListener(actionListener);
        return darkCheckBox;
    }

    public static JComboBox getActionListenCheckBox(String[] options, ActionListener actionListener)
    {
        JComboBox dark = new JComboBox(options);
        dark.addActionListener(actionListener);
        return dark;
    }
}
