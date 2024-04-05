package com.advancedraidtracker.utility;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import static com.advancedraidtracker.utility.datautility.DataWriter.PLUGIN_DIRECTORY;
import static net.runelite.client.RuneLite.RUNELITE_DIR;
@Slf4j
public class UISwingUtility
{
    @Setter
    private static AdvancedRaidTrackerConfig config;

    public static String colorStr(Color c) //todo @fisu it looks like I already had a method for this..........
    {
        return "<html><font color='" + toHex(c) + "'>";
    }

    public static String fontColorString()
    {
        return colorStr(config.fontColor());
    }

    public static String toHex(Color color)
    {
        String red = pad(Integer.toHexString(color.getRed()));
        String green = pad(Integer.toHexString(color.getGreen()));
        String blue = pad(Integer.toHexString(color.getBlue()));
        return "#" + red + green + blue;
    }

    private static String pad(String s)
    {
        return (s.length() == 1) ? "0" + s : s;
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
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(config.fontColor());
        panel.setBorder(border);
        panel.setBackground(config.primaryDark());
        panel.setOpaque(true);
        return panel;
    }

    public static JCheckBox getActionListenCheckBox(String name, ActionListener actionListener)
    {
        return getActionListenCheckBox(name, false, actionListener);
    }

    public static JCheckBox getActionListenCheckBox(String name, boolean state, ActionListener actionListener)
    {
        JCheckBox darkCheckBox = new JCheckBox(name, state)
        {
            @Override
            public JToolTip createToolTip()
            {
                JToolTip toolTip = super.createToolTip();
                toolTip.setBackground(config.primaryDark());
                return toolTip;
            }
        };
        darkCheckBox.addActionListener(actionListener);
        darkCheckBox.setBackground(config.primaryDark());
        try
        {
            darkCheckBox.setSelectedIcon(new ImageIcon(ImageIO.read(new File(RUNELITE_DIR.getAbsolutePath() + "test.png"))));
        } catch (Exception e)
        {

        }
        darkCheckBox.setOpaque(false);
        darkCheckBox.setForeground(config.fontColor());
        return darkCheckBox;
    }

    public static JComboBox<String> getThemedComboBox(String[] options)
    {
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setRenderer(new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus)
            {
                super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                if(isSelected)
                {
                    setBackground(computeBlendColor(config.fontColor(), config.primaryDark(), 128));
                }
                else
                {
                    setBackground(config.primaryMiddle());
                }
                setForeground(config.fontColor());
                return this;
            }
        });
        comboBox.setBorder(BorderFactory.createLineBorder(config.primaryMiddle()));
        comboBox.setBackground(config.primaryMiddle());
        comboBox.setForeground(config.fontColor());
        comboBox.setOpaque(true);
        return comboBox;
    }

    public static JComboBox<String> getThemedComboBox()
    {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setRenderer(new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus)
            {
                super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                if(cellHasFocus)
                {
                    setBackground(computeBlendColor(config.fontColor(), config.primaryDark(), 128));
                }
                else
                {
                    setBackground(config.primaryMiddle());
                }
                setForeground(config.fontColor());
                return this;
            }
        });
        comboBox.setBorder(BorderFactory.createLineBorder(config.primaryMiddle()));
        comboBox.setBackground(config.primaryMiddle());
        comboBox.setForeground(config.fontColor());
        comboBox.setOpaque(true);
        return comboBox;
    }

    public static JComboBox<Integer> getThemedComboBox(Integer[] options)
    {
        JComboBox<Integer> comboBox = new JComboBox<>(options);
        comboBox.setRenderer(new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus)
            {
                super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                if(cellHasFocus)
                {
                    setBackground(computeBlendColor(config.fontColor(), config.primaryDark(), 128));
                }
                else
                {
                    setBackground(config.primaryMiddle());
                }
                setForeground(config.fontColor());
                return this;
            }
        });
        comboBox.setBorder(BorderFactory.createLineBorder(config.primaryMiddle()));
        comboBox.setBackground(config.primaryMiddle());
        comboBox.setForeground(config.fontColor());
        comboBox.setOpaque(true);
        return comboBox;
    }

    public static JComboBox<String> getActionListenCheckBox(String[] options, ActionListener actionListener)
    {
        JComboBox<String> dark = new JComboBox<>(options);
        dark.addActionListener(actionListener);
        dark.setBackground(config.primaryDark());
        dark.setOpaque(false);
        dark.setForeground(config.fontColor());
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

    public static JPanel getThemedPanel()
    {
        JPanel panel = new JPanel();
        panel.setBackground(config.primaryDark());
        panel.setOpaque(true);
        return panel;
    }

    public static JPanel getThemedPanel(LayoutManager layoutManager)
    {
        JPanel panel = new JPanel(layoutManager);
        panel.setBackground(config.primaryDark());
        panel.setOpaque(true);
        return panel;
    }

    public static JLabel getThemedLabel(String text, int swingConstant)
    {
        JLabel label = new JLabel(text, swingConstant);
        label.setForeground(config.fontColor());
        return label;
    }

    public static JSlider getThemedSlider()
    {
        JSlider slider = new JSlider();
        slider.setUI(new BasicSliderUI(slider)
        {
            private static final int TRACK_HEIGHT = 8;
            private static final int TRACK_WIDTH = 8;
            private static final int TRACK_ARC = 5;
            private final Dimension THUMB_SIZE = new Dimension(8, 8);
            private final RoundRectangle2D.Float trackShape = new RoundRectangle2D.Float();

            @Override
            protected void calculateTrackRect()
            {
                super.calculateTrackRect();
                if (isHorizontal())
                {
                    trackRect.y = trackRect.y + (trackRect.height - TRACK_HEIGHT) / 2;
                    trackRect.height = TRACK_HEIGHT;
                } else
                {
                    trackRect.x = trackRect.x + (trackRect.width - TRACK_WIDTH) / 2;
                    trackRect.width = TRACK_WIDTH;
                }
                trackShape.setRoundRect(trackRect.x, trackRect.y, trackRect.width, trackRect.height, TRACK_ARC, TRACK_ARC);
            }

            @Override
            protected void calculateThumbLocation()
            {
                super.calculateThumbLocation();
                if (isHorizontal())
                {
                    thumbRect.y = trackRect.y + (trackRect.height - thumbRect.height) / 2;
                } else
                {
                    thumbRect.x = trackRect.x + (trackRect.width - thumbRect.width) / 2;
                }
            }

            @Override
            protected Dimension getThumbSize()
            {
                return THUMB_SIZE;
            }

            private boolean isHorizontal()
            {
                return slider.getOrientation() == JSlider.HORIZONTAL;
            }

            @Override
            public void paint(final Graphics g, final JComponent c)
            {
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paint(g, c);
            }

            @Override
            public void paintTrack(final Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g;
                Shape clip = g2.getClip();

                boolean horizontal = isHorizontal();
                boolean inverted = slider.getInverted();

                g2.setColor(config.primaryDark());
                g2.fill(trackShape);
                g2.setColor(config.primaryLight());
                g2.setClip(trackShape);

                trackShape.y += 1;
                g2.fill(trackShape);
                trackShape.y = trackRect.y;

                g2.setClip(clip);

                if (horizontal)
                {
                    boolean ltr = slider.getComponentOrientation().isLeftToRight();
                    if (ltr) inverted = !inverted;
                    int thumbPos = thumbRect.x + thumbRect.width / 2;
                    if (inverted)
                    {
                        g2.clipRect(0, 0, thumbPos, slider.getHeight());
                    } else
                    {
                        g2.clipRect(thumbPos, 0, slider.getWidth() - thumbPos, slider.getHeight());
                    }

                } else
                {
                    int thumbPos = thumbRect.y + thumbRect.height / 2;
                    if (inverted)
                    {
                        g2.clipRect(0, 0, slider.getHeight(), thumbPos);
                    } else
                    {
                        g2.clipRect(0, thumbPos, slider.getWidth(), slider.getHeight() - thumbPos);
                    }
                }
                g2.setColor(config.boxColor());
                g2.fill(trackShape);
                g2.setClip(clip);
            }

            @Override
            public void paintThumb(final Graphics g)
            {
                g.setColor(config.fontColor());
                g.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
            }

            @Override
            public void paintFocus(final Graphics g)
            {
            }
        });
        slider.setBackground(config.primaryMiddle());
        slider.setForeground(config.fontColor());
        slider.setOpaque(true);
        return slider;
    }

    public static JSpinner getThemedSpinner(SpinnerNumberModel snm)
    {
        JSpinner spinner = new JSpinner(snm);
        spinner.addPropertyChangeListener(cl ->
        {
            spinner.setBackground(config.primaryDark());
            spinner.setForeground(config.fontColor());
            spinner.setOpaque(true);
        });
        spinner.setBackground(config.primaryDark());
        spinner.setForeground(config.fontColor());
        spinner.setOpaque(true);
        return spinner;
    }

    public static JLabel getThemedLabel(String text)
    {
        JLabel label = new JLabel(text);
        label.setForeground(config.fontColor());
        return label;
    }

    public static JLabel getThemedLabel()
    {
        JLabel label = new JLabel();
        label.setForeground(config.fontColor());
        return label;
    }

    public static JButton getThemedButton(String text)
    {
        JButton button = new JButton(text);
        button.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                button.setBorder(BorderFactory.createLineBorder(config.boxColor()));
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                button.setBorder(BorderFactory.createLineBorder(config.idleColor()));
            }
        });
        button.setForeground(config.fontColor());
        button.setBackground(config.primaryMiddle());
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(config.idleColor()));
        return button;
    }

    public static JButton getThemedButton()
    {
        return getThemedButton("");
    }

    public static JTable getThemedTable()
    {
        JTable table = new JTable();
        table.getTableHeader().setBackground(config.primaryMiddle());
        table.getTableHeader().setOpaque(true);
        table.setForeground(config.fontColor());
        table.getTableHeader().setForeground(config.fontColor());
        table.setBackground(config.primaryDark());
        table.setOpaque(true);
        return table;
    }

    public static JTable getThemedTable(Object[][] obj, String[] strs)
    {
        JTable table = new JTable(obj, strs);
        table.getTableHeader().setBackground(config.primaryMiddle());
        table.getTableHeader().setOpaque(true);
        table.setForeground(config.fontColor());
        table.getTableHeader().setForeground(config.fontColor());
        table.setBackground(config.primaryDark());
        table.setOpaque(true);
        return table;
    }


    public static JScrollPane getThemedScrollPane(JComponent component)
    {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createLineBorder(config.primaryLight()));
        scrollPane.setBackground(config.primaryDark());
        scrollPane.setOpaque(true);
        scrollPane.getVerticalScrollBar().setBackground(config.primaryMiddle());
        scrollPane.getHorizontalScrollBar().setBackground(config.primaryMiddle());
        scrollPane.getHorizontalScrollBar().setBackground(config.primaryDark());
        scrollPane.getVerticalScrollBar().setBackground(config.primaryDark());
        scrollPane.getHorizontalScrollBar().setOpaque(true);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI()
        {
            @Override
            protected void configureScrollBarColors()
            {
                this.thumbColor = config.primaryLight();
            }

            @Override
            protected JButton createDecreaseButton(int orientation)
            {
                return new JButton()
                {
                    @Override
                    public Dimension getPreferredSize()
                    {
                        return new Dimension();
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation)
            {
                return new JButton()
                {
                    @Override
                    public Dimension getPreferredSize()
                    {
                        return new Dimension();
                    }
                };
            }
        });
        scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI()
        {
            @Override
            protected void configureScrollBarColors()
            {
                this.thumbColor = config.primaryLight();
            }
        });
        scrollPane.getVerticalScrollBar().setOpaque(true);
        return scrollPane;
    }

    public static JCheckBox getThemedCheckBox(String name, boolean state)
    {
        JCheckBox checkBox = new JCheckBox(name, state);
        checkBox.setBackground(config.primaryMiddle());
        try
        {
            checkBox.setSelectedIcon(new ImageIcon(ImageIO.read(new File(RUNELITE_DIR.getAbsolutePath() + "test.png"))));
        } catch (Exception e)
        {

        }
        checkBox.setForeground(config.fontColor());
        checkBox.setOpaque(false);
        return checkBox;
    }

    public static JCheckBox getThemedCheckBox(String name)
    {
        JCheckBox checkBox = new JCheckBox(name);
        checkBox.setBackground(config.primaryMiddle());
        try
        {
            checkBox.setSelectedIcon(new ImageIcon(ImageIO.read(new File(RUNELITE_DIR.getAbsolutePath() + "test.png"))));
        } catch (Exception e)
        {

        }
        checkBox.setForeground(config.fontColor());
        checkBox.setOpaque(false);
        return checkBox;
    }

    public static JCheckBox getThemedCheckBox()
    {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setOpaque(false);
        checkBox.setBackground(config.primaryMiddle());
        try
        {
            checkBox.setSelectedIcon(new ImageIcon(ImageIO.read(new File(RUNELITE_DIR.getAbsolutePath() + "test.png"))));
        } catch (Exception e)
        {

        }
        checkBox.setForeground(config.fontColor());
        return checkBox;
    }

    public static JTabbedPane getThemedTabbedPane()
    {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(config.primaryDark());
        tabbedPane.setForeground(config.fontColor());
        tabbedPane.setOpaque(true);
        return tabbedPane;
    }

    public static JTextArea getThemedTextArea()
    {
        return getThemedTextArea("");
    }

    public static JTextArea getThemedTextArea(String text)
    {
        JTextArea textArea = new JTextArea(text);
        textArea.setBackground(config.primaryMiddle());
        textArea.setOpaque(true);
        textArea.setForeground(config.fontColor());
        return textArea;
    }

    public static JTextField getThemedTextField()
    {
        return getThemedTextField("");
    }

    public static JTextField getThemedTextField(String text)
    {
        JTextField textArea = new JTextField(text);
        textArea.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                textArea.setBorder(BorderFactory.createLineBorder(config.boxColor()));
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                textArea.setBorder(BorderFactory.createLineBorder(config.primaryLight()));
            }
        });
        textArea.setBorder(BorderFactory.createLineBorder(config.primaryLight()));
        textArea.setBackground(config.primaryMiddle());
        textArea.setForeground(config.fontColor());
        textArea.setOpaque(true);
        return textArea;
    }

    public static JMenuItem getThemedMenuItem(String item)
    {
        JMenuItem menuItem = new JMenuItem(item);
        menuItem.setUI(new BasicMenuItemUI()
        {
            {
                super.selectionBackground = computeBlendColor(config.fontColor(), config.primaryDark(), 128);
            }
        });
        menuItem.setBackground(config.primaryDark());
        menuItem.setForeground(config.fontColor());
        menuItem.setOpaque(true);
        return menuItem;
    }

    public static JMenu getThemedMenu(String item)
    {
        JMenu menu = new JMenu(item);
        menu.setUI(new BasicMenuUI()
        {
            {
                super.selectionBackground = computeBlendColor(config.fontColor(), config.primaryDark(), 128);
            }
        });
        menu.setBackground(config.primaryDark());
        menu.setForeground(config.fontColor());
        menu.setOpaque(true);
        return menu;
    }

    public static Color computeBlendColor(Color base, Color top, int opacityTop)
    {
        return new Color(
                (top.getRed()*opacityTop + base.getRed() * (255- opacityTop))/255,
                (top.getBlue()*opacityTop + base.getBlue() * (255- opacityTop))/255,
                (top.getGreen()*opacityTop + base.getGreen() * (255- opacityTop))/255

        );
    }

    public static TitledBorder getColoredTitledBorder(String title, Color color)
    {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(color);
        return border;
    }

    public static JCheckBoxMenuItem getThemedCheckBoxMenuItem(String name)
    {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(name);
        item.setUI(new BasicMenuItemUI()
        {
            {
                super.selectionBackground = computeBlendColor(config.fontColor(), config.primaryDark(), 128);
            }
        });
        item.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                item.setBackground(computeBlendColor(config.fontColor(), config.primaryDark(), 128));
            }
            @Override
            public void mouseExited(MouseEvent e)
            {
                item.setBackground(config.primaryDark());
            }
        });
        item.setBackground(config.primaryDark());
        item.setForeground(config.fontColor());
        item.setOpaque(true);
        return item;
    }

    public static JPopupMenu getThemedPopupMenu()
    {
        JPopupMenu jPopupMenu = new JPopupMenu();

        return jPopupMenu;
    }
}
