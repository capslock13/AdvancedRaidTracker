package com.advancedraidtracker.ui.summary;

import com.advancedraidtracker.SimpleRaidDataBase;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class SummarizeRaidsPanel extends JPanel
{
    private final ArrayList<SimpleRaidDataBase> data;
    private final BufferedImage img;
    int width = 600;
    int height = 400;
    int margin = 20;

    Color primaryDark = new Color(23, 23, 23);
    Color primaryLight = new Color(30, 30, 30);

    public SummarizeRaidsPanel(ArrayList<SimpleRaidDataBase> data)
    {
        this.data = data;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        drawPanel();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (img != null)
        {
            g.drawImage(img, 0, 0, null);
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(img.getWidth(), img.getHeight());
    }

    java.util.List<String> statuses = Arrays.asList("Maiden Wipe", "Maiden Reset", "Bloat Wipe", "Bloat Reset", "Nylo Wipe", "Nylo Reset", "Sotetseg Wipe", "Sotetseg Reset", "Xarpus Wipe", "Xarpus Reset", "Verzik Wipe", "Completion");

    private final Color[] statusColors =
            {
                    Color.decode("#ebdc78"),
                    Color.decode("#8be04e"),
                    Color.decode("#5ad45a"),
                    Color.decode("#00b7c7"),
                    Color.decode("#0d88e6"),
                    Color.decode("#1a53ff"),
                    Color.decode("#4421af"),
                    Color.decode("#7c1158"),
                    Color.decode("#b30000"),
                    Color.decode("#5671FF"),
                    Color.decode("#FF3333"),
                    Color.decode("#22FF22")
            };

    private void drawSummaryBar(Graphics2D g)
    {
        g.setColor(Color.WHITE);
        g.setFont(FontManager.getDefaultBoldFont());
        g.drawString("Summary", 20, 20);
        g.setColor(primaryDark);
        g.fillRoundRect(margin, margin + margin, width - (margin * 2), 40, 15, 15);

        Map<String, Integer> statusCounts = new LinkedHashMap<>();
        for (SimpleRaidDataBase roomData : data)
        {
            for (String status : statuses)
            {
                statusCounts.putIfAbsent(status, 0);
                if (roomData.getRoomStatus().contains(status))
                {
                    statusCounts.put(status, statusCounts.get(status) + 1);
                }
            }
        }

        int latestIndex = 0;
        int firstNonZero = -1;
        for (int i = 0; i < statuses.size(); i++)
        {
            if (statusCounts.get(statuses.get(i)) != 0)
            {
                if (firstNonZero == -1)
                {
                    firstNonZero = i;
                }
                latestIndex = i;
            }
        }
        int cumulativeOffset = margin;
        int summaryBarWidth = width - (margin * 2);
        for (int i = 0; i < latestIndex; i++)
        {
            double percent = (statusCounts.get((statuses.get(i)))) / (double) data.size();
            int sectionWidth = (int) (percent * summaryBarWidth);
            g.setColor(statusColors[i]);
            g.fillRoundRect(cumulativeOffset, margin + margin, sectionWidth, 40, 14, 14);
            if (i != firstNonZero && percent != 0)
            {
                g.fillRect(cumulativeOffset - 7, margin + margin, 14, 40);
            }
            cumulativeOffset += sectionWidth;
        }
    }

    public void drawPanel()
    {
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setColor(primaryLight);
        g.fillRect(0, 0, width, height);

        drawSummaryBar(g);

    }
}
