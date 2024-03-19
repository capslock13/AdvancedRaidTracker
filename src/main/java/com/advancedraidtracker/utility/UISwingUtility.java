package com.advancedraidtracker.utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class UISwingUtility
{
    public static String colorStr(Color c) //todo @fisu it looks like I already had a method for this..........
    {
        return "<html><font color='#" + Integer.toHexString(c.getRGB()).substring(2) + "'>";
    }

    public final static String roomColor = colorStr(new Color(200, 200, 200));

    public static JLabel getDarkJLabel(String labelText)
    {
        return new JLabel(labelText);
    }

    public static JLabel getDarkJLabel(String labelText, int swingConstant)
    {
        return new JLabel(labelText, swingConstant);
    }

    public static BufferedImage createStringImage(Graphics g, String s, Color color)
    {
        int w = g.getFontMetrics().stringWidth(s) + 5;
        int h = g.getFontMetrics().getHeight();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageGraphics = image.createGraphics();
        imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        imageGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        imageGraphics.setColor(color);
        imageGraphics.setFont(new Font("SansSerif", Font.PLAIN, 12)); //HEIGHT 16
        imageGraphics.drawString(s, 0, h - g.getFontMetrics().getDescent());
        imageGraphics.dispose();
        return image;
    }

    public static BufferedImage createStringImage(Graphics g, String s)
    {
        return createStringImage(g, s, Color.WHITE);
    }

    public static void drawStringRotated(Graphics g, String s, int tx, int ty, Color color)
    {
        int angle = (s.length() == 1) ? 0 : -45;
        AffineTransform aff = AffineTransform.getRotateInstance(Math.toRadians(angle), tx, ty);
        aff.translate(tx, ty);

        Graphics2D g2D = ((Graphics2D) g);
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2D.drawImage(createStringImage(g, s, color), aff, null);
    }

    public static void drawStringRotated(Graphics g, String s, int tx, int ty)
    {
        drawStringRotated(g, s, tx, ty, Color.WHITE);
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
