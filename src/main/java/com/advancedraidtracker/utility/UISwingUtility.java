package com.advancedraidtracker.utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UISwingUtility
{
    public static String colorStr(Color c)
    {
        return "<html><font color='#" + Integer.toHexString(c.getRGB()).substring(2) + "'>";
    }
    public  final static String roomColor = colorStr(new Color(200, 200, 200));
    public static JLabel getDarkJLabel(String labelText)
    {
        return new JLabel(labelText);
    }

    public static JLabel getDarkJLabel(String labelText, int swingConstant)
    {
        return new JLabel(labelText, swingConstant);
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

    public static JComboBox<String> getActionListenCheckBox(String[] options, ActionListener actionListener)
    {
        JComboBox<String> dark = new JComboBox<>(options);
        dark.addActionListener(actionListener);
        return dark;
    }
}
