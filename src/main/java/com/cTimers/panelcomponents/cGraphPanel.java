package com.cTimers.panelcomponents;

import com.cTimers.cRoomData;
import com.cTimers.utility.RoomUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
@Slf4j
public class cGraphPanel extends JPanel implements MouseMotionListener
{
    public static final int IMG_WIDTH = 600;
    public static final int IMG_HEIGHT = 300;


    public static final int GRAPH_WIDTH = 500;
    public static final int GRAPH_HEIGHT = 250;
    public static final int GRAPH_XS = 50;
    public static final int GRAPH_XE = 550;

    public static final int GRAPH_YS = 25;
    public static final int GRAPH_YE = 225;

    private Color gridColor = new Color(110, 110, 110);

    private int xSections = 10;
    private int ySections = 10;

    private cToolTipData activeToolTip;
    private boolean shouldDrawToolTip = false;


    private cBounds activeBound = new cBounds(-1, -1, -1, -1);

    private Point activePoint;
    private ArrayList<cBounds> bounds;
    private Color gradientStart;
    private Color gradientEnd;

    private Color gradientStartHighlighted;
    private Color gradientEndHighlighted;
    private int activeKey = 0;

    private boolean time = false;
    private BufferedImage img = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);

    private ArrayList<cRoomData> internalData;

    public cGraphPanel(ArrayList<cRoomData> data)
    {
        gradientStart =  new Color(100, 170, 230, 100);
        gradientEnd = new Color(200, 240, 255, 100);
        gradientStartHighlighted =  new Color(100, 170, 230, 200);
        gradientEndHighlighted = new Color(200, 240, 255, 200);
        internalData = data;
        drawBlankGraph();
        addMouseMotionListener(this);
        bounds = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if(img != null)
        {
            g.drawImage(img, 0, 0, null);
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(IMG_WIDTH, IMG_HEIGHT);
    }

    private boolean isNyloTime()
    {
        return activeKey > 38 && activeKey < 44; //nylo time keys
    }
    public void switchKey(int key)
    {
        time = key > 29;
        activeKey = key;
    }

    private ArrayList<Integer> getCounts(ArrayList<Integer> data, int highestValue)
    {
        ArrayList<Integer> countedData = new ArrayList<>();
        for(int i = 0; i < highestValue+1; i++)
        {
            countedData.add(0);
        }
        for(Integer i : data)
        {
            if(countedData.size() > i)
            {
                int incrementedValue = countedData.get(i) + 1;
                countedData.set(i, incrementedValue);
            }
        }
        return countedData;
    }

    private int getCountedTotal(ArrayList<Integer> data)
    {
        int count = 0;
        for(Integer i : data)
        {
            count += i;
        }
        return count;
    }

    private void drawBar(Graphics2D g, int width, int height, int left, int count, int total, String value)
    {
        Color oldColor = g.getColor();
        if(height == 0)
        {
            return;
        }
        if(width == 1)
        {
            g.drawLine(left, GRAPH_HEIGHT-GRAPH_YS, left, GRAPH_HEIGHT-GRAPH_YS-height);
        }

        int right = left+width;
        int top = GRAPH_HEIGHT-GRAPH_YS-height;
        int bottom = GRAPH_HEIGHT-GRAPH_YS;

        g.setColor(new Color(100, 170, 230));
        g.drawLine(left, bottom, right, bottom);
        g.drawLine(left, bottom, left, top);
        g.drawLine(left, top, right, top);
        g.drawLine(right, bottom, right, top);

        boolean highlight = (left == activeBound.getLeft() && right == activeBound.getRight() && top == activeBound.getTop() && activeBound.getBottom() == bottom);

        Paint oldPaint = g.getPaint();
        GradientPaint gradient = new GradientPaint(left, bottom, (highlight) ? gradientStartHighlighted : gradientStart, left, top, (highlight) ? gradientEndHighlighted : gradientEnd);
        g.setPaint(gradient);
        g.fillRect(left, top, width, height);

        if(highlight)
        {
            String percent = Math.round((100.0 * count / (double) total)*100.0)/100.0 + "%";
            String message = value + ": " + count + "/" + total + " (" + percent + ")";
            int msgWidth = g.getFontMetrics().stringWidth(message);
            int msgHeight = g.getFontMetrics().getHeight();

            int ml = left+width/2-msgWidth/2;
            int mb = top-msgHeight/2;

            activeToolTip = new cToolTipData(ml, mb, message);
            shouldDrawToolTip = true;
        }
        g.setPaint(oldPaint);
        g.setColor(oldColor);
    }

    private int getHighestCount(ArrayList<Integer> data)
    {
        int max = 0;
        int index = 0;
        for(Integer i : data)
        {
            if(time && index == 0)
            {
                index++;
                continue;
            }
            if(i > max)
            {
                max = i;
            }
            index++;
        }
        return max;
    }

    public BufferedImage createStringImage(Graphics g, String s) {
        int w = g.getFontMetrics().stringWidth(s) + 5;
        int h = g.getFontMetrics().getHeight();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageGraphics = image.createGraphics();
        imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        imageGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        imageGraphics.setColor(Color.WHITE);
        imageGraphics.setFont(new Font("SansSerif", Font.PLAIN, 12)); //HEIGHT 16
        imageGraphics.drawString(s, 0, h - g.getFontMetrics().getDescent());
        imageGraphics.dispose();
        return image;
    }

    private void drawStringRotated(Graphics g, String s, int tx, int ty) {
        AffineTransform aff = AffineTransform.getRotateInstance(Math.toRadians(90), tx, ty);
        aff.translate(tx, ty);

        Graphics2D g2D = ((Graphics2D) g);
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2D.drawImage(createStringImage(g, s), aff, this);
    }

    private String getString(int s)
    {
        return (time) ? RoomUtil.time(s) : s+"";
    }

    private int xScaleLow = 0;
    private int xScaleHigh = 0;
    private int yScaleHigh = 0;

    public int getScaleXLow()
    {
        return xScaleLow;
    }

    public int getScaleXHigh()
    {
        return xScaleHigh;
    }

    public int getScaleYHigh()
    {
        return yScaleHigh;
    }

    public void generateScales()
    {
        ArrayList<Integer> data = getInternalDataSet(activeKey);
        int lowestValue = Integer.MAX_VALUE;
        int highestValue = 0;

        for(Integer i : data)
        {
            if(time && i == 0)
            {
                continue;
            }
            if(i < lowestValue)
            {
                lowestValue = i;
            }
            if(i > highestValue)
            {
                highestValue = i;
            }
        }
        ArrayList<Integer> countedDataSet = getCounts(data, highestValue);
        int highestCount = getHighestCount(countedDataSet);

        xScaleLow = lowestValue;
        xScaleHigh = highestValue;
        yScaleHigh = highestCount;
    }

    public void setScales(int xl, int xh, int yh)
    {
        xScaleLow = xl;
        xScaleHigh = xh;
        yScaleHigh = yh;
    }

    public void setBounds()
    {
        bounds.clear();
        ArrayList<Integer> data = getInternalDataSet(activeKey);
        ArrayList<Integer> countedDataSet = getCounts(data, xScaleHigh);
        int highestCount = yScaleHigh;
        int bars = xScaleHigh-xScaleLow+1;
        int barWidth = GRAPH_WIDTH/(bars);
        int usedWidth = barWidth*bars;
        int startX = GRAPH_XS+(GRAPH_WIDTH/2) - (usedWidth/2);
        int scale = (highestCount == 0) ? (int)(GRAPH_HEIGHT*.75) : (int)((GRAPH_HEIGHT*.75)/highestCount);

        for(int i = xScaleLow; i < xScaleHigh+1; i++)
        {
            int height = countedDataSet.get(i) * scale;
            int left = startX + ((i-xScaleLow)*(barWidth));
            int right = left+barWidth;
            int top = GRAPH_HEIGHT-GRAPH_YS-height;
            int bottom = GRAPH_HEIGHT-GRAPH_YS;
            bounds.add(new cBounds(left, right, bottom, top));
        }
    }

    private void drawToolTip()
    {
        if(shouldDrawToolTip)
        {
            shouldDrawToolTip = false;
            Graphics2D g = (Graphics2D) img.getGraphics();
            Font oldFont = g.getFont();
            Color oldColor = g.getColor();
            g.setFont(new Font("SansSerif", Font.PLAIN, 14));

            int msgWidth = g.getFontMetrics().stringWidth(activeToolTip.message);
            int msgHeight = g.getFontMetrics().getHeight();

            g.setColor(new Color(30, 30, 30, 255));
            g.fillRect(activeToolTip.messageLeft-5, activeToolTip.messageBottom-msgHeight, msgWidth+10, msgHeight+10);
            g.setColor(new Color(240, 240, 240));
            g.drawString(activeToolTip.message, activeToolTip.messageLeft, activeToolTip.messageBottom);
            g.setFont(oldFont);
            g.setColor(oldColor);
        }
    }
    public void drawGraph()
    {
        ArrayList<Integer> data = getInternalDataSet(activeKey);
        drawBlankGraph();

        Graphics2D g = (Graphics2D) img.getGraphics();
        Stroke oldStroke = g.getStroke();

        int lowestValue = xScaleLow;
        int highestValue = xScaleHigh;

        ArrayList<Integer> countedDataSet = getCounts(data, highestValue);
        int highestCount = yScaleHigh;
        int bars = highestValue-lowestValue+1;
        int barWidth = GRAPH_WIDTH/(bars);
        int usedWidth = barWidth*bars;
        int startX = GRAPH_XS+(GRAPH_WIDTH/2) - (usedWidth/2);
        int scale = (highestCount == 0) ? (int)(GRAPH_HEIGHT*.75) : (int)((GRAPH_HEIGHT*.75)/highestCount);
        int totalCount = getCountedTotal(countedDataSet);
        int horizontalScaleToUse = (highestValue-lowestValue > 100) ? 25 : (highestValue-lowestValue > 50) ? 10 : (highestValue-lowestValue > 10) ? 5 : 1;
        if(barWidth > 16)
        {
            horizontalScaleToUse = 1;
        }

        int verticalScaleToUse = (highestCount > 100) ? 25 : (highestCount > 50) ? 10 : (highestCount > 10) ? 5 : 1;

        for(int i = 0; i < highestCount+1; i++)
        {
            int stringOffset = 500-GRAPH_HEIGHT-GRAPH_YS-scale*i+8;
            if(i == 0 || i%verticalScaleToUse == 0)
            {
                Color oldColor = g.getColor();
                g.setColor(new Color(100, 100, 100, 100));
                g.drawLine(GRAPH_XS, stringOffset-8, GRAPH_XE, stringOffset-8);
                g.setColor(oldColor);
                g.drawString(i + "", GRAPH_XS - 20, stringOffset);
            }
        }

        for(int i = lowestValue; i < highestValue+1; i++)
        {
            int currentBarCenter = startX + ((i-lowestValue)*(barWidth));
            int height = countedDataSet.get(i) * scale;
            drawBar(g, barWidth, height, currentBarCenter, countedDataSet.get(i), totalCount, getString(i));
            int stringOffset = 16 + barWidth/2 - 8;
            if(i == lowestValue || i == highestValue || i%horizontalScaleToUse==0)
            {
                drawStringRotated(g, getString(i), startX + stringOffset + ((i - lowestValue) * barWidth), 500 - GRAPH_HEIGHT - GRAPH_YS + 5);
            }
        }
        drawToolTip();


        g.setStroke(oldStroke);
        g.dispose();
        repaint();
    }

    private ArrayList<Integer> getInternalDataSet(int key)
    {
        ArrayList<Integer> dataSet = new ArrayList<>();
        for(cRoomData data : internalData)
        {
            if(data.getValueFromKey(key) != -1)
            {
                dataSet.add(data.getValueFromKey(key));
            }
        }
        return dataSet;
    }
    private void drawGridLines()
    {
        Graphics g = img.getGraphics();
        g.setColor(gridColor);

        g.drawLine(GRAPH_XS, GRAPH_YS, GRAPH_XE, GRAPH_YS);
        g.drawLine(GRAPH_XS, GRAPH_YS, GRAPH_XS, GRAPH_YE);
        g.drawLine(GRAPH_XS, GRAPH_YE, GRAPH_XE, GRAPH_YE);
        g.drawLine(GRAPH_XE, GRAPH_YE, GRAPH_XE, GRAPH_YS);

        g.dispose();
    }

    public void drawBlankGraph()
    {
        Graphics g = img.getGraphics();
        g.setColor(new Color(40, 40, 40));
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.dispose();

        drawGridLines();

        repaint();
    }

    private boolean boundActive = false;

    private void checkBounds(int x, int y)
    {
        for(cBounds bound : bounds)
        {
            if(x >= bound.getLeft() && x <= bound.getRight() && y <= bound.getBottom() && y >= bound.getTop())
            {
                if(!boundActive)
                {
                    activeBound = new cBounds(bound.getLeft(), bound.getRight(), bound.getBottom(), bound.getTop());
                    boundActive = true;
                    drawGraph();
                }

            }
            else if(boundActive)
            {
                if(bound.matches(activeBound))
                {
                    activeBound.reset();
                    boundActive = false;
                    drawGraph();
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        checkBounds(e.getX(), e.getY());
    }
}
