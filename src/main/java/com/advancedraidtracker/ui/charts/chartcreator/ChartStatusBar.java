package com.advancedraidtracker.ui.charts.chartcreator;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;

import static com.advancedraidtracker.utility.UISwingUtility.getThemedLabel;
import static com.advancedraidtracker.utility.UISwingUtility.getThemedPanel;

public class ChartStatusBar extends JPanel
{
    JLabel label;
    public ChartStatusBar(String text)
    {
        setLayout(new BorderLayout());
        JPanel panel = getThemedPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        label = getThemedLabel(text, SwingConstants.LEFT);
        panel.add(label, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);
    }

    public void set(String text)
    {
        label.setText(text);
    }

    public void append(String text)
    {
        label.setText(label.getText() + text);
    }

    public void appendToStart(String text)
    {
        label.setText(text + label.getText());
    }
}
