package com.advancedraidtracker.utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

public class UISwingUtility
{
    public static Color primaryDark = new Color(20, 20, 20);

    public static String colorStr(Color c)
    {
        return "<html><font color='#" + Integer.toHexString(c.getRGB()).substring(2) + "'>";
    }

    public final static String roomColor = colorStr(new Color(200, 200, 200));

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

    public static int getStringWidth(Graphics2D g, String str)
    {
        FontRenderContext frc = g.getFontRenderContext();
        GlyphVector gv = g.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, 0, 0).width;
    }

    public static int getStringHeight(Graphics2D g)
    {
        FontRenderContext frc = g.getFontRenderContext();
        GlyphVector gv = g.getFont().createGlyphVector(frc, "A");
        return gv.getPixelBounds(null, 0, 0).height;
    }
}
