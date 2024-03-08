package com.TheatreTracker.ui.viewraid;

import com.TheatreTracker.SimpleTOAData;
import com.TheatreTracker.utility.RoomUtil;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Random;

import static com.TheatreTracker.utility.UISwingUtility.getStringHeight;

public class ViewTOAPanel extends JPanel
{
    private BufferedImage img;
    int width = 600;
    int height = 400;
    int margin = 10;
    private final SimpleTOAData data;
    private final Color[] pieChartColors = {
            Color.decode("#ebdc78"),
            Color.decode("#8be04e"),
            Color.decode("#5ad45a"),
            Color.decode("#00b7c7"),
            Color.decode("#0d88e6"),
            Color.decode("#1a53ff"),
            Color.decode("#4421af"),
            Color.decode("#7c1158"),
            Color.decode("#b30000"),
    };

    public ViewTOAPanel(SimpleTOAData toaData)
    {
        this.data = toaData;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        draw();
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

    private void draw()
    {

        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        RenderingHints qualityHints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(qualityHints);
        g.setColor(new Color(20, 20, 20));
        g.fillRect(0, 0, width, height);

        //draw summary box
        g.setColor(new Color(80, 70, 60));
        g.drawRoundRect(margin, margin, width - (2 * margin), 150, 10, 10);
        g.setColor(new Color(35, 35, 35));
        g.fillRoundRect(margin + 1, margin + 1, width - (2 * margin) - 2, 150 - 2, 10, 10);

        g.setFont(FontManager.getRunescapeBoldFont());
        int rowHeight = getStringHeight(g) + 6;
        int currentXOffset = margin * 2;
        int currentYOffset = margin * 2 + rowHeight / 2;
        g.setColor(Color.WHITE);
        g.drawString("Date: " + getDateString(), currentXOffset, currentYOffset);
        currentYOffset += rowHeight;
        g.drawString("Scale: " + data.getScaleString(), currentXOffset, currentYOffset);
        currentYOffset += rowHeight;
        g.drawString("Status: " + getRaidStatusString(), currentXOffset, currentYOffset);
        currentYOffset += rowHeight;
        g.drawString("Overall Time: " + RoomUtil.time(data.getOverallTime()), currentXOffset, currentYOffset);
        currentYOffset += rowHeight;
        g.drawString("Challenge Time: " + RoomUtil.time(data.getTimeSum()), currentXOffset, currentYOffset);
        currentXOffset += 200;
        currentYOffset = margin * 2 + rowHeight / 2;
        g.drawString("Players: ", currentXOffset, currentYOffset);
        currentYOffset += rowHeight;
        Random random = new Random();
        for (int i = 0; i < 8; i++)
        {
            final float hue = random.nextFloat();
// Saturation between 0.1 and 0.3
            final float saturation = (random.nextInt(3000) + 7000) / 10000f;
            final float luminance = 0.9f;
            final Color color = Color.getHSBColor(hue, saturation, luminance);
            g.setColor(color);
            g.drawString(data.getCompletePlayerArray()[i], currentXOffset, currentYOffset);
            currentYOffset += rowHeight;
        }
    }

    public String getDateString()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data.getDate());
        return (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR);
    }

    public String getRaidStatusString()
    {
        return data.getRoomStatus().replaceAll("[^BKZAW]", "");
    }
}
