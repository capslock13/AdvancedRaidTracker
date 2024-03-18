package com.advancedraidtracker.ui.viewraid;

import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.toa.Toa;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;

import static com.advancedraidtracker.utility.UISwingUtility.getStringHeight;

public class ViewTOAPanel extends JPanel
{
    private BufferedImage img;
    int width = 800;
    int height = 500;
    int margin = 10;
    int summaryHeight = 150;
    int roomHeight = height - margin - summaryHeight - margin - margin;
    int roomWidth = (width-(6*margin))/5;
    private final Toa data;
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

    public ViewTOAPanel(Toa toaData)
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

    private void drawSummaryBox(Graphics2D g)
    {
        g.setColor(new Color(80, 70, 60));
        g.drawRoundRect(margin, margin, width - (2 * margin), summaryHeight, 10, 10);
        g.setColor(new Color(35, 35, 35));
        g.fillRoundRect(margin + 1, margin + 1, width - (2 * margin) - 2, summaryHeight - 2, 10, 10);

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
        for (int i = 0; i < 8; i++)
        {
            //g.drawString(data.getCompletePlayerArray()[i], currentXOffset, currentYOffset); //todo
            currentYOffset += rowHeight;
        }
    }

    private void drawRoomBoxes(Graphics2D g)
    {
        int yStart = margin+summaryHeight+margin;
        for(int i = 0; i < 5; i++)
        {
            g.setColor(new Color(80, 70, 60));
            g.drawRoundRect((i * (roomWidth+margin)) + margin, yStart, roomWidth, roomHeight, 10, 10);
            g.setColor(new Color(35, 35, 35));
            g.fillRoundRect((i*(roomWidth+margin))+margin + 1, yStart + 1, roomWidth - 2, roomHeight - 2, 10, 10);
        }
    }

    private void drawBabaPath(Graphics2D g, int startX)
    {
        int startY = margin+summaryHeight+7;
        int time = data.get(DataPoint.APMEKEN_TIME);
        String title = "Apmeken" + ((time > 0) ? " - " + RoomUtil.time(time) : "");
        g.drawString(title, startX+10, startY+10);
    }

    private void drawKephriPath(Graphics2D g, int startX)
    {
        int startY = margin+summaryHeight+margin/2;
        int time = data.get(DataPoint.SCABARAS_TIME);
        String title = "Scabaras" + ((time > 0) ? " - " + RoomUtil.time(time) : "");
        g.drawString(title, startX+10, startY+10);
    }

    private void drawAkkhaPath(Graphics2D g, int startX)
    {
        int startY = margin+summaryHeight+7;
        int time = data.get(DataPoint.HET_TIME);
        String title = "Het" + ((time > 0) ? " - " + RoomUtil.time(time) : "");
        g.drawString(title, startX+10, startY+10);
    }

    private void drawZebakPath(Graphics2D g, int startX)
    {
        int startY = margin+summaryHeight+7;
        int time = data.get(DataPoint.CRONDIS_TIME);
        String title = "Crondis" + ((time > 0) ? " - " + RoomUtil.time(time) : "");
        g.drawString(title, startX+10, startY+10);
    }

    private void drawWardenPath(Graphics2D g, int startX)
    {
        int startY = margin+summaryHeight+7;
        int time = data.get(DataPoint.WARDENS_TIME);
        String title = "Wardens" + ((time > 0) ? " - " + RoomUtil.time(time) : "");
        g.drawString(title, startX+10, startY+10);
    }

    private void draw()
    {
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(qualityHints);

        //draw background
        g.setColor(new Color(20, 20, 20));
        g.fillRect(0, 0, width, height);

        drawSummaryBox(g);
        drawRoomBoxes(g);
        g.setColor(Color.WHITE);
        drawBabaPath(g, margin);
        drawKephriPath(g, margin+(roomWidth+margin));
        drawAkkhaPath(g, margin+(2*(roomWidth+margin)));
        drawZebakPath(g, margin+(3*(roomWidth+margin)));
        drawWardenPath(g, margin+(4*(roomWidth+margin)));
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
