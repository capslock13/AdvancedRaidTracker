package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.utility.RoomUtil;
import com.TheatreTracker.utility.DataPoint;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
@Slf4j
public class GraphPanel extends JPanel implements MouseMotionListener, MouseListener, KeyListener
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

    private ToolTipData activeToolTip;
    private boolean shouldDrawToolTip = false;

    private boolean groupingEnabled = true;


    private ArrayList<Bounds> selectedBounds;
    private Bounds activeBound = new Bounds(-1, -1, -1, -1, null);

    private ArrayList<Bounds> bounds;
    private Color gradientStart;
    private Color gradientEnd;

    private Color gradientStartHighlighted;
    private Color gradientEndHighlighted;

    private Color gradientStartSelected;
    private Color gradientEndSelected;
    private Color gradientStartSelectedAndHighlighted;
    private Color gradientEndSelectedAndHighlighted;
    private int activeKey = 0;

    private boolean time = false;
    private BufferedImage img = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);

    private ArrayList<RoomData> internalData;

    public GraphPanel(ArrayList<RoomData> data)
    {
        selectedBounds = new ArrayList<>();
        gradientStart =  new Color(100, 170, 230, 90);
        gradientEnd = new Color(200, 240, 255, 90);

        gradientStartHighlighted =  new Color(100, 170, 230, 215);
        gradientEndHighlighted = new Color(200, 240, 255, 215);

        gradientStartSelected = new Color(100, 170, 230, 190);
        gradientEndSelected = new Color(200, 240, 255, 190);

        gradientStartSelectedAndHighlighted = new Color(100, 170, 230, 240);
        gradientEndSelectedAndHighlighted = new Color(200, 240, 255, 240);

        internalData = data;
        drawBlankGraph();
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);

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


    public void switchKey(int key)
    {
        time = DataPoint.values()[key].type == DataPoint.types.TIME;
        activeKey = key;
    }

    private ArrayList<Integer> filterForTime(ArrayList<Integer> data)
    {
        ArrayList<Integer> arrayToPass = new ArrayList<>();
        for(Integer i : data)
        {
            if (!time || i != 0)
            {
                arrayToPass.add(i);
            }
        }
        return arrayToPass;
    }

    public static ArrayList<Integer> getCounts(ArrayList<Integer> data, int highestValue)
    {
        ArrayList<Integer> countedData = new ArrayList<>();
        for(int i = 0; i < highestValue+1; i++)
        {
            countedData.add(0);
        }
        for(Integer i : data)
        {
            if(countedData.size() > i && i > -1)
            {
                int incrementedValue = countedData.get(i) + 1;
                countedData.set(i, incrementedValue);
            }
        }
        return countedData;
    }

    public static int getCountedTotal(ArrayList<Integer> data)
    {
        int count = 0;
        int index = 0;
        for(Integer i : data)
        {
            count += i;
            index++;
        }
        return count;
    }

    boolean isBarSelected(int left, int right, int top, int bottom)
    {
        for(Bounds b : selectedBounds)
        {
            if(b.getLeft() == left && b.getRight() == right && b.getTop() == top && b.getBottom() == bottom)
            {
                return true;
            }
        }
        return false;
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
        boolean selected = isBarSelected(left, right, top, bottom);


        Paint oldPaint = g.getPaint();
        GradientPaint gradient = new GradientPaint(left, bottom, (highlight && selected) ? gradientStartSelectedAndHighlighted : (highlight) ? gradientStartHighlighted : (selected) ? gradientStartSelected : gradientStart, left, top, (highlight && selected) ? gradientEndSelectedAndHighlighted : (highlight) ? gradientEndHighlighted : (selected) ? gradientEndSelected : gradientEnd);

        g.setPaint(gradient);
        g.fillRect(left, top, width, height);

        if(highlight && selectedBounds.size() == 0)
        {
            String percent = Math.round((100.0 * count / (double) total)*100.0)/100.0 + "%";
            String message = value + ": " + count + "/" + total + " (" + percent + ")";
            int msgWidth = g.getFontMetrics().stringWidth(message);
            int msgHeight = g.getFontMetrics().getHeight();

            int ml = left+width/2-msgWidth/2;
            int mb = top-msgHeight/2;

            activeToolTip = new ToolTipData(ml, mb, message);
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
        ArrayList<Integer> data = filterInvalid(getInternalDataSet(activeKey).intData);
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

    public static GraphInternalBoundMatchedContainer getCounts(GraphInternalDataContainer data, int highestValue)
    {
        ArrayList<Integer> countedIntData = new ArrayList<>();
        ArrayList<ArrayList<RoomData>> countedFullData = new ArrayList<>();
        for(int i = 0; i < highestValue+1; i++)
        {
            countedIntData.add(0);
            countedFullData.add(new ArrayList<>());
        }
        for(int i = 0; i < data.intData.size(); i++)
        {
            if(countedIntData.size() > data.intData.get(i) && data.intData.get(i) > -1)
            {
                int incrementedValue = countedIntData.get(data.intData.get(i)) + 1;
                countedIntData.set(data.intData.get(i), incrementedValue);
                countedFullData.get(data.intData.get(i)).add(data.fullData.get(i));
            }
        }
        return new GraphInternalBoundMatchedContainer(countedFullData, countedIntData);
    }

    private int getAlternateYScale(GraphInternalBoundMatchedContainer sets)
    {
        int max = 0;
        for(int i = 0; i < sets.intData.size(); i++)
        {
            int partialSum = 0;
            for(int j = i; j < i+groupSize; j++)
            {
                if(j < sets.intData.size())
                {
                    partialSum += sets.intData.get(j);
                }
            }
            if(partialSum > max)
            {
                max = partialSum;
            }
        }
        return max;
    }

    public void setBounds()
    {
        bounds.clear();
        GraphInternalDataContainer graphData = getInternalDataSet(activeKey);
        GraphInternalBoundMatchedContainer countedDataSet = getCounts(graphData, xScaleHigh);
        int highestCount = (groupingEnabled) ? getAlternateYScale(countedDataSet) : yScaleHigh;
        int firstGroupCount = (groupOffset == 0) ? groupSize : groupOffset;
        int bars = (groupingEnabled) ? ((int) (1+ Math.ceil(((double)(1+xScaleHigh-firstGroupCount-xScaleLow))/((double)groupSize)))) : xScaleHigh-xScaleLow+1;
        int barWidth = GRAPH_WIDTH/(bars);
        int usedWidth = barWidth*bars;
        int startX = GRAPH_XS+(GRAPH_WIDTH/2) - (usedWidth/2);
        double scale = (highestCount == 0) ? (GRAPH_HEIGHT*.75) : ((GRAPH_HEIGHT*.75)/highestCount);

        for(int i = Math.max(0, xScaleLow); i < xScaleHigh+1; i++)
        {
            if(groupingEnabled)
            {
                if((i-xScaleLow)%groupSize==groupOffset || i == xScaleLow+groupOffset || i == xScaleLow)
                {
                    int barOffset = 0;
                    if(i != xScaleLow && groupOffset != 0)
                    {
                        barOffset = 1;
                    }
                    int summedRegion = (groupOffset == 0 || i != xScaleLow) ? sumRegion(countedDataSet, i, groupSize) : sumRegion(countedDataSet, i-groupSize+groupOffset, groupSize);
                    int height = (int) (summedRegion * scale);
                    int barsToDraw = (i-Math.max(0, xScaleLow))/groupSize;
                    int left = startX + ((barsToDraw+barOffset) * (barWidth));
                    int right = left + barWidth;
                    int top = GRAPH_HEIGHT - GRAPH_YS - height;
                    int bottom = GRAPH_HEIGHT - GRAPH_YS;
                    ArrayList<RoomData> summedRegionData;
                    if(groupOffset == 0 || i != xScaleLow)
                    {
                        summedRegionData = sumRegionRaidData(countedDataSet, i, groupSize);
                    }
                    else
                    {
                        summedRegionData = sumRegionRaidData(countedDataSet, i - groupSize + groupOffset, groupSize);
                    }
                    bounds.add(new Bounds(left, right, bottom, top, summedRegionData));
                }
            }
            else
            {
                int height = (int) (countedDataSet.intData.get(i) * scale);
                int left = startX + ((i - xScaleLow) * (barWidth));
                int right = left + barWidth;
                int top = GRAPH_HEIGHT - GRAPH_YS - height;
                int bottom = GRAPH_HEIGHT - GRAPH_YS;
                bounds.add(new Bounds(left, right, bottom, top, countedDataSet.fullData.get(i)));
            }
        }
    }

    public void setGroupingEnabled(boolean enabled)
    {
        groupingEnabled = enabled;
        setBounds();
        drawGraph();
    }

    public void updateGroupSize(int groupSize)
    {
        this.groupSize = groupSize;
        setBounds();
        drawGraph();
    }

    public void updateGroupOffset(int groupOffset)
    {
        this.groupOffset = groupOffset;
        setBounds();
        drawGraph();
    }

    private int sumRegion(GraphInternalBoundMatchedContainer data, int index, int length)
    {
        int sum = 0;
        for(int i = index; i < index+length; i++)
        {
            if(i < data.intData.size() && i > -1)
            {
                sum += data.intData.get(i);
            }
        }
        return sum;
    }

    private ArrayList<RoomData> sumRegionRaidData(GraphInternalBoundMatchedContainer data, int index, int length)
    {
        ArrayList<RoomData> summedData = new ArrayList<>();
        for(int i = index; i < index+length; i++)
        {
            if(i < data.fullData.size() && i > -1)
            {
                for(RoomData tempData : data.fullData.get(i))
                {
                    summedData.add(tempData);
                }
            }
        }
        return summedData;
    }

    private int groupSize = 1;
    private int groupOffset = 0;

    private void drawDragArea()
    {
        if(dragCurrentX != -1 && dragCurrentY != -1)
        {
            int startX = Math.min(dragStartX, dragCurrentX);
            int startY = Math.min(dragStartY, dragCurrentY);
            int endX = Math.max(dragStartX, dragCurrentX);
            int endY = Math.max(dragStartY, dragCurrentY);
            Graphics2D g = (Graphics2D) img.getGraphics();
            Color oldColor = g.getColor();
            g.setColor(new Color(200, 200, 100, 180));
            g.drawRect(startX, startY, Math.abs(endX-startX), Math.abs(endY-startY));
            g.setColor(new Color(200, 200, 100, 70));
            g.fillRect(startX, startY, Math.abs(endX-startX), Math.abs(endY-startY));
            g.setColor(oldColor);
        }

    }

    private void drawToolTip()
    {
        if(shouldDrawToolTip && selectedBounds.size() == 0)
        {
            shouldDrawToolTip = false;
            Graphics2D g = (Graphics2D) img.getGraphics();
            Font oldFont = g.getFont();
            Color oldColor = g.getColor();
            g.setFont(new Font("SansSerif", Font.PLAIN, 14));

            int msgWidth = g.getFontMetrics().stringWidth(activeToolTip.message);
            int msgHeight = g.getFontMetrics().getHeight();


            int left = activeToolTip.messageLeft-5;
            int bottom = activeToolTip.messageBottom-msgHeight;

            g.setColor(new Color(30, 30, 30, 255));
            g.fillRect(activeToolTip.messageLeft-5, activeToolTip.messageBottom-msgHeight, msgWidth+10, msgHeight+10);
            g.setColor(new Color(100, 100, 100));
            g.drawRect(left, bottom, msgWidth+10, msgHeight+10);
            g.setColor(new Color(240, 240, 240));
            g.drawString(activeToolTip.message, activeToolTip.messageLeft, activeToolTip.messageBottom);
            g.setFont(oldFont);
            g.setColor(oldColor);
        }
    }

    public static ArrayList<Integer> filterInvalid(ArrayList<Integer> data)
    {
        ArrayList<Integer> filteredData = new ArrayList<>();
        for(Integer i : data)
        {
            if(i > -1)
            {
                filteredData.add(i);
            }
        }
        return filteredData;
    }
    public void drawGraph()
    {
        if(groupingEnabled)
        {
            drawBlankGraph();

            Graphics2D g = (Graphics2D) img.getGraphics();
            Stroke oldStroke = g.getStroke();

            int lowestValue = xScaleLow;
            int highestValue = xScaleHigh;

            GraphInternalBoundMatchedContainer countedDataSet = getCounts(getInternalDataSet(activeKey), highestValue);
            int highestCount = getAlternateYScale(countedDataSet);
            int firstGroupCount = (groupOffset == 0) ? groupSize : groupOffset;
            int bars = ((int) (1+ Math.ceil(((double)(1+xScaleHigh-firstGroupCount-xScaleLow))/((double)groupSize))));
            int barWidth = GRAPH_WIDTH/(bars);
            if(barWidth == 0)
            {
                barWidth = 1;
            }
            int usedWidth = barWidth*bars;
            int startX = GRAPH_XS+(GRAPH_WIDTH/2) - (usedWidth/2);
            double scale = ((highestCount == 0) ? (GRAPH_HEIGHT*.75) : ((GRAPH_HEIGHT*.75)/(double)highestCount));
            int totalCount = getCountedTotal(countedDataSet.intData);
            int horizontalScaleToUse = (highestValue-lowestValue > 100) ? 25 : (highestValue-lowestValue > 50) ? 10 : (highestValue-lowestValue > 10) ? 5 : 1;
            if(barWidth > 16)
            {
                horizontalScaleToUse = 1;
            }

            int verticalScaleToUse = (highestCount > 250) ? 50 : (highestCount > 100) ? 25 : (highestCount > 50) ? 10 : (highestCount > 10) ? 5 : 1;
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Font oldFont = g.getFont();
            Font font = new Font("SansSerif", Font.PLAIN, 14);
            g.setFont(font);
            String title = DataPoint.values()[activeKey].name + " (Based on " + totalCount + " raids)";
            g.drawString(title, 300-g.getFontMetrics().stringWidth(title)/2, 16);
            g.setFont(oldFont);
            for(int i = 0; i < highestCount+1; i++)
            {
                {
                    int stringOffset = (int) (500 - GRAPH_HEIGHT - GRAPH_YS - scale * i + 8);
                    if (i == 0 || i % verticalScaleToUse == 0)
                    {
                        Color oldColor = g.getColor();
                        g.setColor(new Color(100, 100, 100, 100));
                        g.drawLine(GRAPH_XS, stringOffset - 8, GRAPH_XE, stringOffset - 8);
                        g.setColor(oldColor);
                        g.drawString(i + "", GRAPH_XS - 20, stringOffset);
                    }
                }
            }

            for(int i = Math.max(lowestValue, 0); i < highestValue+1; i++)
            {
                if((i-lowestValue)%groupSize==groupOffset || i == lowestValue+groupOffset || i == lowestValue)
                {
                    int barOffset = 0;
                    if(i != lowestValue && groupOffset != 0)
                    {
                        barOffset = 1;
                    }
                    int currentBarCenter = startX + (((i-Math.max(0, xScaleLow))/groupSize)+barOffset) * (barWidth);
                    int summedRegion = (groupOffset == 0 || i != lowestValue) ? sumRegion(countedDataSet, i, groupSize) : sumRegion(countedDataSet, i-groupSize+groupOffset, groupSize);
                    int height = (int) (summedRegion * scale);
                    int stringOffset = 16 + barWidth / 2 - 8;
                    String axisValue = "";
                    if (i == lowestValue || i == highestValue || i % horizontalScaleToUse == 0)
                    {
                        axisValue = (i == lowestValue && groupOffset != 0) ? getString(i-groupSize+groupOffset) : getString(i);
                        if(groupSize != 1)
                        {
                            if (i != lowestValue || groupOffset == 0)
                            {
                                axisValue += "-" + getString(i + groupSize - 1);
                            } else {
                                axisValue += "-" + getString(i);
                            }
                        }
                        drawStringRotated(g, axisValue, startX + stringOffset + ((((i-Math.max(0, xScaleLow))/groupSize)+barOffset) * barWidth), 500 - GRAPH_HEIGHT - GRAPH_YS + 5);
                    }
                    drawBar(g, barWidth, height, currentBarCenter, summedRegion, totalCount, axisValue);
                }
            }
            drawToolTip();

            drawDragArea();

            g.setStroke(oldStroke);
            g.dispose();
            repaint();
        }
        else
        {
            ArrayList<Integer> data = filterForTime(filterInvalid(getInternalDataSet(activeKey).intData));
            drawBlankGraph();

            Graphics2D g = (Graphics2D) img.getGraphics();
            Stroke oldStroke = g.getStroke();

            int lowestValue = xScaleLow;
            int highestValue = xScaleHigh;

            ArrayList<Integer> countedDataSet = getCounts(data, highestValue);
            int highestCount = yScaleHigh;
            int bars = highestValue - lowestValue + 1;
            int barWidth = GRAPH_WIDTH / (bars);
            if (barWidth == 0) {
                barWidth = 1;
            }
            int usedWidth = barWidth * bars;
            int startX = GRAPH_XS + (GRAPH_WIDTH / 2) - (usedWidth / 2);
            double scale = ((highestCount == 0) ? (GRAPH_HEIGHT * .75) : ((GRAPH_HEIGHT * .75) / (double) highestCount));
            int totalCount = getCountedTotal(countedDataSet);
            int horizontalScaleToUse = (highestValue - lowestValue > 100) ? 25 : (highestValue - lowestValue > 50) ? 10 : (highestValue - lowestValue > 10) ? 5 : 1;
            if (barWidth > 16) {
                horizontalScaleToUse = 1;
            }

            int verticalScaleToUse = (highestCount > 250) ? 50 : (highestCount > 100) ? 25 : (highestCount > 50) ? 10 : (highestCount > 10) ? 5 : 1;
            g.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Font oldFont = g.getFont();
            Font font = new Font("SansSerif", Font.PLAIN, 14);
            g.setFont(font);
            String title = DataPoint.values()[activeKey].name + " (Based on " + totalCount + " raids)";
            g.drawString(title, 300 - g.getFontMetrics().stringWidth(title) / 2, 16);
            g.setFont(oldFont);
            for (int i = 0; i < highestCount + 1; i++) {
                int stringOffset = (int) (500 - GRAPH_HEIGHT - GRAPH_YS - scale * i + 8);
                if (i == 0 || i % verticalScaleToUse == 0) {
                    Color oldColor = g.getColor();
                    g.setColor(new Color(100, 100, 100, 100));
                    g.drawLine(GRAPH_XS, stringOffset - 8, GRAPH_XE, stringOffset - 8);
                    g.setColor(oldColor);
                    g.drawString(i + "", GRAPH_XS - 20, stringOffset);
                }
            }

            for (int i = Math.max(lowestValue, 0); i < highestValue + 1; i++) {
                int currentBarCenter = startX + ((i - lowestValue) * (barWidth));
                int height = (int) (countedDataSet.get(i) * scale);
                drawBar(g, barWidth, height, currentBarCenter, countedDataSet.get(i), totalCount, getString(i));
                int stringOffset = 16 + barWidth / 2 - 8;
                if (i == lowestValue || i == highestValue || i % horizontalScaleToUse == 0) {
                    drawStringRotated(g, getString(i), startX + stringOffset + ((i - lowestValue) * barWidth), 500 - GRAPH_HEIGHT - GRAPH_YS + 5);
                }
            }
            drawToolTip();

            drawDragArea();

            g.setStroke(oldStroke);
            g.dispose();
            repaint();
        }
    }

    private GraphInternalDataContainer getInternalDataSet(int key)
    {
        ArrayList<Integer> intDataSet = new ArrayList<>();
        ArrayList<RoomData> fullDataSet = new ArrayList<>();
        for(RoomData data : internalData)
        {
            if(data.getValue(DataPoint.values()[key]) != -1)
            {
                if(data.getTimeAccurate(DataPoint.values()[key]))
                {
                    intDataSet.add(data.getValue(DataPoint.values()[key]));
                    fullDataSet.add(data);
                }
            }
        }
        return new GraphInternalDataContainer(fullDataSet, intDataSet);
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

    private boolean checkContains(Bounds bound)
    {
        for(Bounds b : selectedBounds)
        {
            if(b.getBottom() == bound.getBottom() && b.getTop() == bound.getTop() && b.getLeft() == bound.getLeft() && b.getRight() == bound.getRight())
            {
                return true;
            }
        }
        return false;
    }

    private boolean currentlyDragging = false;
    private boolean boundActive = false;
    private int dragStartX = -1;
    private int dragStartY = -1;
    private int dragCurrentX = -1;
    private int dragCurrentY = -1;

    private Bounds getBound(int x, int y)
    {
        for(Bounds bound : bounds)
        {
            if(x >= bound.getLeft() && x <= bound.getRight() && y <= bound.getBottom() && y >= bound.getTop())
            {
                return bound;
            }
        }
        return null;
    }
    private boolean checkOverlap(int left1, int top1, int right1, int bottom1, int left2, int top2, int right2, int bottom2)
    {
        int leftA = Math.min(left1, right1);
        int rightA = Math.max(left1, right1);
        int bottomA = Math.min(top1, bottom1);
        int topA = Math.max(top1, bottom1);

        int leftB = Math.min(left2, right2);
        int rightB = Math.max(left2, right2);
        int bottomB = Math.min(top2, bottom2);
        int topB = Math.max(top2, bottom2);

        return (leftA < rightB && rightA > leftB && topA > bottomB && bottomA < topB);
    }

    private void checkIntersectingBounds()
    {
        ArrayList<Bounds> currentlyIntersecting = new ArrayList<>();
        for(Bounds b : bounds)
        {
            if(checkOverlap(dragStartX, dragStartY, dragCurrentX, dragCurrentY, b.getLeft(), b.getTop(), b.getRight(), b.getBottom()))
            {
                currentlyIntersecting.add(b);
            }
        }
        selectedBounds.clear();
        selectedBounds.addAll(currentlyIntersecting);
    }
    private boolean checkBounds(int x, int y)
    {
        for(Bounds bound : bounds)
        {
            if(x >= bound.getLeft() && x <= bound.getRight() && y <= bound.getBottom() && y >= bound.getTop())
            {
                return true;
            }
        }
        return false;
    }

    private void checkBoundsAndHighlight(int x, int y)
    {
        for(Bounds bound : bounds)
        {
            if(x >= bound.getLeft() && x <= bound.getRight() && y <= bound.getBottom() && y >= bound.getTop())
            {
                if(!boundActive)
                {
                    requestFocusInWindow();
                    activeBound = new Bounds(bound.getLeft(), bound.getRight(), bound.getBottom(), bound.getTop(), bound.raids);
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

    private ArrayList<RoomData> mergeSelectedData()
    {
        ArrayList<RoomData> mergedData = new ArrayList<>();
        for(Bounds b : selectedBounds)
        {
            for(RoomData raid : b.raids)
            {
                mergedData.add(raid);
            }
        }
        return mergedData;
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if(e.isShiftDown() && currentlyDragging)
        {
            dragCurrentX = e.getX();
            dragCurrentY = e.getY();
            checkIntersectingBounds();
            drawGraph();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        checkBoundsAndHighlight(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(e.isShiftDown() && SwingUtilities.isLeftMouseButton(e) && selectedBounds.size() == 0)
        {
            if(checkBounds(e.getX(), e.getY()))
            {
                selectedBounds.add(getBound(e.getX(), e.getY()));
                drawGraph();
            }
        }
        else if(e.isShiftDown() && SwingUtilities.isLeftMouseButton(e))
        {
            if(checkBounds(e.getX(), e.getY()))
            {
                Bounds lastBound = selectedBounds.get(selectedBounds.size()-1);
                addAllBoundsBetween(lastBound, getBound(e.getX(), e.getY()));
                drawGraph();
            }
        }
        else if(SwingUtilities.isLeftMouseButton(e) && !e.isControlDown())
        {
            selectedBounds.clear();
            if(checkBounds(e.getX(), e.getY()))
            {
                selectedBounds.add(getBound(e.getX(), e.getY()));

                drawGraph();
            }
        }
    }

    private void addAllBoundsBetween(Bounds lastBound, Bounds bound)
    {
        for(Bounds b : bounds)
        {
            if((b.getLeft() > lastBound.getLeft() && b.getLeft() < bound.getLeft()) || (b.getLeft() < lastBound.getLeft() && b.getLeft() > bound.getLeft()))
            {
                if(!checkContains(b))
                {
                    selectedBounds.add(b);
                }
            }
        }
        selectedBounds.add(bound);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(SwingUtilities.isRightMouseButton(e))
        {
            if(selectedBounds.size() == 0)
            {
                for (Bounds bound : bounds)
                {
                    if (e.getX() >= bound.getLeft() && e.getX() <= bound.getRight() && e.getY() <= bound.getBottom() && e.getY() >= bound.getTop())
                    {
                        GraphRightClickContextMenu menu = new GraphRightClickContextMenu(bound.raids);
                        menu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
            else
            {
                GraphRightClickContextMenu menu = new GraphRightClickContextMenu(mergeSelectedData());
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
        if(SwingUtilities.isLeftMouseButton(e) && e.isShiftDown())
        {
            if(!currentlyDragging)
            {
                dragStartX = e.getX();
                dragStartY = e.getY();
                currentlyDragging = true;
            }
        }
        if(SwingUtilities.isLeftMouseButton(e) && e.isControlDown())
        {
            if(checkBounds(e.getX(), e.getY()))
            {
                Bounds clicked = getBound(e.getX(), e.getY());
                if(checkContains(clicked))
                {
                    selectedBounds.removeIf(b -> (b.getLeft() == clicked.getLeft() && b.getRight() == clicked.getRight() && b.getTop() == clicked.getTop() && b.getBottom() == clicked.getBottom()));
                }
                else
                {
                    selectedBounds.add(getBound(e.getX(), e.getY()));
                }
                drawGraph();
            }
        }
        if(SwingUtilities.isLeftMouseButton(e))
        {
            if(!checkBounds(e.getX(), e.getY()))
            {
                selectedBounds.clear();
                drawGraph();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(SwingUtilities.isLeftMouseButton(e))
        {
            if(currentlyDragging)
            {
                currentlyDragging = false;
                dragStartX = -1;
                dragStartY = -1;
                dragCurrentX = -1;
                dragCurrentY = -1;
                drawGraph();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.isShiftDown())
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if(!e.isShiftDown())
        {
            setCursor(Cursor.getDefaultCursor());
            if(currentlyDragging)
            {
                currentlyDragging = false;
                dragStartX = -1;
                dragStartY = -1;
                dragCurrentX = -1;
                dragCurrentY = -1;
                drawGraph();
            }
        }
    }
}
