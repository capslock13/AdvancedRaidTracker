package com.advancedraidtracker.ui.comparisonview.graph;

import com.advancedraidtracker.SimpleRaidDataBase;
import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

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

    private final Color gridColor = new Color(110, 110, 110);
    private ToolTipData activeToolTip;
    private boolean shouldDrawToolTip = false;

    private boolean groupingEnabled = true;

    private int graphType;
    private final ArrayList<Bounds> selectedBounds;
    private Bounds activeBound = new Bounds(-1, -1, -1, -1, null);
    private final ArrayList<Bounds> bounds;
    private final Color gradientStart;
    private final Color gradientEnd;
    private final Color gradientStartHighlighted;
    private final Color gradientEndHighlighted;
    private final Color gradientStartSelected;
    private final Color gradientEndSelected;
    private final Color gradientStartSelectedAndHighlighted;
    private final Color gradientEndSelectedAndHighlighted;
    private DataPoint activeKey;
    private boolean time = false;
    private final BufferedImage img = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    private final ArrayList<Raid> internalData;
    private final AdvancedRaidTrackerConfig config;

    private final ItemManager itemManager;

    private final ClientThread clientThread;
    private final ConfigManager configManager;

    public GraphPanel(ArrayList<Raid> data, AdvancedRaidTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager)
    {
        this.configManager = configManager;
        this.clientThread = clientThread;
        this.itemManager = itemManager;
        selectedBounds = new ArrayList<>();
        gradientStart = new Color(100, 170, 230, 90);
        gradientEnd = new Color(200, 240, 255, 90);

        gradientStartHighlighted = new Color(100, 170, 230, 215);
        gradientEndHighlighted = new Color(200, 240, 255, 215);

        gradientStartSelected = new Color(100, 170, 230, 190);
        gradientEndSelected = new Color(200, 240, 255, 190);

        gradientStartSelectedAndHighlighted = new Color(100, 170, 230, 240);
        gradientEndSelectedAndHighlighted = new Color(200, 240, 255, 240);
        graphType = 0;
        internalData = data;
        drawBlankBarGraph();
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        this.config = config;
        bounds = new ArrayList<>();
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
        return new Dimension(IMG_WIDTH, IMG_HEIGHT);
    }


    public void switchKey(DataPoint key)
    {
        time = key.type == DataPoint.types.TIME;
        activeKey = key;
    }


    private ArrayList<Integer> filterForTime(ArrayList<Integer> data)
    {
        ArrayList<Integer> arrayToPass = new ArrayList<>();
        for (Integer i : data)
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
        for (int i = 0; i < highestValue + 1; i++)
        {
            countedData.add(0);
        }
        for (Integer i : data)
        {
            if (countedData.size() > i && i > -1)
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
        for (Integer i : data)
        {
            count += i;
        }
        return count;
    }

    boolean isBarSelected(int left, int right, int top, int bottom)
    {
        for (Bounds b : selectedBounds)
        {
            if (b.getLeft() == left && b.getRight() == right && b.getTop() == top && b.getBottom() == bottom)
            {
                return true;
            }
        }
        return false;
    }

    private void drawBar(Graphics2D g, int width, int height, int left, int count, int total, String value)
    {
        Color oldColor = g.getColor();
        if (height == 0)
        {
            return;
        }
        if (width == 1)
        {
            g.drawLine(left, GRAPH_HEIGHT - GRAPH_YS, left, GRAPH_HEIGHT - GRAPH_YS - height);
        }

        int right = left + width;
        int top = GRAPH_HEIGHT - GRAPH_YS - height;
        int bottom = GRAPH_HEIGHT - GRAPH_YS;

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

        if (highlight && selectedBounds.isEmpty())
        {
            String percent = Math.round((100.0 * count / (double) total) * 100.0) / 100.0 + "%";
            String message = value + ": " + count + "/" + total + " (" + percent + ")";
            int msgWidth = g.getFontMetrics().stringWidth(message);
            int msgHeight = g.getFontMetrics().getHeight();

            int ml = left + width / 2 - msgWidth / 2;
            int mb = top - msgHeight / 2;

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
        for (Integer i : data)
        {
            if (time && index == 0)
            {
                index++;
                continue;
            }
            if (i > max)
            {
                max = i;
            }
            index++;
        }
        return max;
    }

    public BufferedImage createStringImage(Graphics g, String s)
    {
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

    private void drawStringRotated(Graphics g, String s, int tx, int ty)
    {
        int angle = (s.length() == 1) ? 0 : -45;
        AffineTransform aff = AffineTransform.getRotateInstance(Math.toRadians(angle), tx, ty);
        aff.translate(tx, ty);

        Graphics2D g2D = ((Graphics2D) g);
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2D.drawImage(createStringImage(g, s), aff, this);
    }

    private String getString(int s)
    {
        return (time) ? RoomUtil.time(s) : String.valueOf(s);
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

        for (Integer i : data)
        {
            if (time && i == 0)
            {
                continue;
            }
            if (i < lowestValue)
            {
                lowestValue = i;
            }
            if (i > highestValue)
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
        ArrayList<ArrayList<SimpleRaidDataBase>> countedFullData = new ArrayList<>();
        for (int i = 0; i < highestValue + 1; i++)
        {
            countedIntData.add(0);
            countedFullData.add(new ArrayList<>());
        }
        for (int i = 0; i < data.intData.size(); i++)
        {
            if (countedIntData.size() > data.intData.get(i) && data.intData.get(i) > -1)
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
        for (int i = 0; i < sets.intData.size(); i++)
        {
            int partialSum = 0;
            for (int j = i; j < i + groupSize; j++)
            {
                if (j < sets.intData.size())
                {
                    partialSum += sets.intData.get(j);
                }
            }
            if (partialSum > max)
            {
                max = partialSum;
            }
        }
        return max;
    }

    public void setBounds()
    {
        bounds.clear();
        if (graphType == 0)
        {
            GraphInternalDataContainer graphData = getInternalDataSet(activeKey);
            GraphInternalBoundMatchedContainer countedDataSet = getCounts(graphData, xScaleHigh);
            int highestCount = (groupingEnabled) ? getAlternateYScale(countedDataSet) : yScaleHigh;
            int firstGroupCount = (groupOffset == 0) ? groupSize : groupOffset;
            int bars = (groupingEnabled) ? ((int) (1 + Math.ceil(((double) (1 + xScaleHigh - firstGroupCount - xScaleLow)) / ((double) groupSize)))) : xScaleHigh - xScaleLow + 1;
            int barWidth = GRAPH_WIDTH / (bars);
            int usedWidth = barWidth * bars;
            int startX = GRAPH_XS + (GRAPH_WIDTH / 2) - (usedWidth / 2);
            double scale = (highestCount == 0) ? (GRAPH_HEIGHT * .75) : ((GRAPH_HEIGHT * .75) / highestCount);

            for (int i = Math.max(0, xScaleLow); i < xScaleHigh + 1; i++)
            {
                if (groupingEnabled)
                {
                    if ((i - xScaleLow) % groupSize == groupOffset || i == xScaleLow + groupOffset || i == xScaleLow)
                    {
                        int barOffset = 0;
                        if (i != xScaleLow && groupOffset != 0)
                        {
                            barOffset = 1;
                        }
                        int summedRegion = (groupOffset == 0 || i != xScaleLow) ? sumRegion(countedDataSet, i, groupSize) : sumRegion(countedDataSet, i - groupSize + groupOffset, groupSize);
                        int height = (int) (summedRegion * scale);
                        int barsToDraw = (i - Math.max(0, xScaleLow)) / groupSize;
                        int left = startX + ((barsToDraw + barOffset) * (barWidth));
                        int right = left + barWidth;
                        int top = GRAPH_HEIGHT - GRAPH_YS - height;
                        int bottom = GRAPH_HEIGHT - GRAPH_YS;
                        ArrayList<Raid> summedRegionData;
                        if (groupOffset == 0 || i != xScaleLow)
                        {
                            summedRegionData = sumRegionRaidData(countedDataSet, i, groupSize);
                        } else
                        {
                            summedRegionData = sumRegionRaidData(countedDataSet, i - groupSize + groupOffset, groupSize);
                        }
                        bounds.add(new Bounds(left, right, bottom, top, summedRegionData));
                    }
                } else
                {
                    int height = (int) (countedDataSet.intData.get(i) * scale);
                    int left = startX + ((i - xScaleLow) * (barWidth));
                    int right = left + barWidth;
                    int top = GRAPH_HEIGHT - GRAPH_YS - height;
                    int bottom = GRAPH_HEIGHT - GRAPH_YS;
                    //bounds.add(new Bounds(left, right, bottom, top, countedDataSet.fullData.get(i)));
                }
            }
        }
    }

    public void setGroupingEnabled(boolean enabled)
    {
        groupingEnabled = enabled;
        setBounds();
        drawGraph();
    }

    public void setGraphType(int type)
    {
        graphType = type;
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
        for (int i = index; i < index + length; i++)
        {
            if (i < data.intData.size() && i > -1)
            {
                sum += data.intData.get(i);
            }
        }
        return sum;
    }

    private ArrayList<Raid> sumRegionRaidData(GraphInternalBoundMatchedContainer data, int index, int length)
    {
        ArrayList<Raid> summedData = new ArrayList<>();
        for (int i = index; i < index + length; i++)
        {
            if (i < data.fullData.size() && i > -1)
            {
                //summedData.addAll(data.fullData.get(i));
            }
        }
        return summedData;
    }

    private int groupSize = 1;
    private int groupOffset = 0;

    private void drawDragArea()
    {
        if (dragCurrentX != -1 && dragCurrentY != -1)
        {
            int startX = Math.min(dragStartX, dragCurrentX);
            int startY = Math.min(dragStartY, dragCurrentY);
            int endX = Math.max(dragStartX, dragCurrentX);
            int endY = Math.max(dragStartY, dragCurrentY);
            Graphics2D g = (Graphics2D) img.getGraphics();
            Color oldColor = g.getColor();
            g.setColor(new Color(200, 200, 100, 180));
            g.drawRect(startX, startY, Math.abs(endX - startX), Math.abs(endY - startY));
            g.setColor(new Color(200, 200, 100, 70));
            g.fillRect(startX, startY, Math.abs(endX - startX), Math.abs(endY - startY));
            g.setColor(oldColor);
        }

    }

    private void drawToolTip()
    {
        if (shouldDrawToolTip && selectedBounds.isEmpty())
        {
            shouldDrawToolTip = false;
            Graphics2D g = (Graphics2D) img.getGraphics();
            Font oldFont = g.getFont();
            Color oldColor = g.getColor();
            g.setFont(new Font("SansSerif", Font.PLAIN, 14));

            int msgWidth = g.getFontMetrics().stringWidth(activeToolTip.message);
            int msgHeight = g.getFontMetrics().getHeight();


            int left = activeToolTip.messageLeft - 5;
            int bottom = activeToolTip.messageBottom - msgHeight;

            g.setColor(new Color(30, 30, 30, 255));
            g.fillRect(activeToolTip.messageLeft - 5, activeToolTip.messageBottom - msgHeight, msgWidth + 10, msgHeight + 10);
            g.setColor(new Color(100, 100, 100));
            g.drawRect(left, bottom, msgWidth + 10, msgHeight + 10);
            g.setColor(new Color(240, 240, 240));
            g.drawString(activeToolTip.message, activeToolTip.messageLeft, activeToolTip.messageBottom);
            g.setFont(oldFont);
            g.setColor(oldColor);
        }
    }

    public static ArrayList<Integer> filterInvalid(ArrayList<Integer> data)
    {
        ArrayList<Integer> filteredData = new ArrayList<>();
        for (Integer i : data)
        {
            if (i > -1)
            {
                filteredData.add(i);
            }
        }
        return filteredData;
    }

    public void drawGraphTitle(Graphics2D g, int totalCount)
    {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Font oldFont = g.getFont();
        Font font = new Font("SansSerif", Font.PLAIN, 14);
        g.setFont(font);
        String title = activeKey.name + " (Based on " + totalCount + " raids)";
        g.drawString(title, 300 - g.getFontMetrics().stringWidth(title) / 2, 16);
        g.setFont(oldFont);
    }

    public void drawGraph()
    {
        Graphics2D g = (Graphics2D) img.getGraphics();
        Stroke oldStroke = g.getStroke();

        drawBlankPanel();
        int lowestValue = xScaleLow;
        int highestValue = xScaleHigh;
        GraphInternalBoundMatchedContainer countedDataSet = getCounts(getInternalDataSet(activeKey), highestValue);
        int totalCount = getCountedTotal(countedDataSet.intData);
        drawGraphTitle(g, totalCount);
        if (graphType == 0)
        {
            drawBlankBarGraph();

            int highestCount = getAlternateYScale(countedDataSet);
            int firstGroupCount = (groupOffset == 0) ? groupSize : groupOffset;
            int bars = ((int) (1 + Math.ceil(((double) (1 + xScaleHigh - firstGroupCount - xScaleLow)) / ((double) groupSize))));
            int barWidth = GRAPH_WIDTH / (bars);
            if (barWidth == 0)
            {
                barWidth = 1;
            }
            int usedWidth = barWidth * bars;
            int startX = GRAPH_XS + (GRAPH_WIDTH / 2) - (usedWidth / 2);
            double scale = ((highestCount == 0) ? (GRAPH_HEIGHT * .75) : ((GRAPH_HEIGHT * .75) / (double) highestCount));
            int horizontalScaleToUse = (highestValue - lowestValue > 100) ? 25 : (highestValue - lowestValue > 50) ? 10 : (highestValue - lowestValue > 10) ? 5 : 1;
            if (barWidth > 16)
            {
                horizontalScaleToUse = 1;
            }

            int verticalScaleToUse = (highestCount > 250) ? 50 : (highestCount > 100) ? 25 : (highestCount > 50) ? 10 : (highestCount > 10) ? 5 : 1;
            if (groupingEnabled)
            {

                drawGraphVerticals(g, highestCount, scale, verticalScaleToUse);

                for (int i = Math.max(lowestValue, 0); i < highestValue + 1; i++)
                {
                    if ((i - lowestValue) % groupSize == groupOffset || i == lowestValue + groupOffset || i == lowestValue)
                    {
                        int barOffset = 0;
                        if (i != lowestValue && groupOffset != 0)
                        {
                            barOffset = 1;
                        }
                        int currentBarCenter = startX + (((i - Math.max(0, xScaleLow)) / groupSize) + barOffset) * (barWidth);
                        int summedRegion = (groupOffset == 0 || i != lowestValue) ? sumRegion(countedDataSet, i, groupSize) : sumRegion(countedDataSet, i - groupSize + groupOffset, groupSize);
                        int height = (int) (summedRegion * scale);
                        int stringOffset = 16 + barWidth / 2 - 8;
                        String axisValue = "";
                        if (i == lowestValue || i == highestValue || i % horizontalScaleToUse == 0)
                        {
                            axisValue = (i == lowestValue && groupOffset != 0) ? getString(i - groupSize + groupOffset) : getString(i);
                            if (groupSize != 1)
                            {
                                if (i != lowestValue || groupOffset == 0)
                                {
                                    axisValue += "-" + getString(i + groupSize - 1);
                                } else
                                {
                                    axisValue += "-" + getString(i);
                                }
                            }
                            FontMetrics m = g.getFontMetrics();
                            int yOffset = (int) (500 - GRAPH_HEIGHT - GRAPH_YS + 10 + (.5 * m.stringWidth(axisValue)));
                            stringOffset -= m.stringWidth(axisValue) - 5;
                            drawStringRotated(g, axisValue, startX + stringOffset + ((((i - Math.max(0, xScaleLow)) / groupSize) + barOffset) * barWidth), yOffset);
                        }
                        drawBar(g, barWidth, height, currentBarCenter, summedRegion, totalCount, axisValue);
                    }
                }

            } else
            {
                ArrayList<Integer> data = filterForTime(filterInvalid(getInternalDataSet(activeKey).intData));

                ArrayList<Integer> intCountedDataSet = getCounts(data, highestValue);
                totalCount = getCountedTotal(intCountedDataSet);

                drawGraphVerticals(g, highestCount, scale, verticalScaleToUse);

                for (int i = Math.max(lowestValue, 0); i < highestValue + 1; i++)
                {
                    int currentBarCenter = startX + ((i - lowestValue) * (barWidth));
                    int height = (int) (intCountedDataSet.get(i) * scale);
                    drawBar(g, barWidth, height, currentBarCenter, intCountedDataSet.get(i), totalCount, getString(i));
                    int stringOffset = 16 + barWidth / 2 - 8;
                    if (i == lowestValue || i == highestValue || i % horizontalScaleToUse == 0)
                    {
                        FontMetrics m = g.getFontMetrics();
                        int yOffset = (int) (500 - GRAPH_HEIGHT - GRAPH_YS + 10 + (.5 * m.stringWidth(getString(i))));
                        stringOffset -= m.stringWidth(getString(i)) - 5;
                        drawStringRotated(g, getString(i), startX + stringOffset + ((i - lowestValue) * barWidth), yOffset);
                    }
                }

            }
            drawToolTip();
            drawDragArea();
            g.setStroke(oldStroke);
            g.dispose();
            repaint();
        } else if (graphType == 1)
        {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            ArrayList<Integer> data = filterForTime(filterInvalid(getInternalDataSet(activeKey).intData));

            ArrayList<Integer> intCountedDataSet = getCounts(data, highestValue);
            totalCount = getCountedTotal(intCountedDataSet);
            int nonZeroCount = getNonZeroCount(intCountedDataSet);
            if (nonZeroCount > 9)
            {
                g.setColor(Color.WHITE);
                g.drawString("Cannot draw pie chart for this data due to too many values", 50, 50);
                return;
            }
            ArrayList<PieChartData> sortedData = createSortedPieChartData(intCountedDataSet, totalCount);

            int position = 90;
            for (int i = 0; i < sortedData.size(); i++)
            {
                Color c = pieChartColors[i];
                Color opacityAdjusted = new Color(c.getRed(), c.getGreen(), c.getBlue(), 150);
                g.setColor(opacityAdjusted);
                g.fillArc(100, 30, 250, 250, position - sortedData.get(i).sections, sortedData.get(i).sections);
                position -= sortedData.get(i).sections;
            }

            //Draw Plot

            int offset = 50;
            for (int i = 0; i < sortedData.size(); i++)
            {
                Color c = pieChartColors[i];
                Color opacityAdjusted = new Color(c.getRed(), c.getGreen(), c.getBlue(), 150);
                g.setColor(opacityAdjusted);
                g.fillRect(375, offset, 50, 18);
                g.setColor(Color.BLACK);
                g.drawRect(375, offset, 50, 18);
                g.setColor(new Color(200, 200, 200, 200));
                Font oldFont = g.getFont();
                g.setFont(oldFont.deriveFont(18f));
                g.drawString(getString(sortedData.get(i).value), 435, offset + 16);
                g.setFont(oldFont);
                offset += 28;
            }

        } else if (graphType == 2)
        {
            drawBlankBarGraph();
            int yMax = 0;
            int yMin = Integer.MAX_VALUE;
            ArrayList<Integer> data = filterForTime(filterInvalid(getInternalDataSet(activeKey).intData));

            ArrayList<Integer> intCountedDataSet = getCounts(data, highestValue);
            for (int i = 0; i < intCountedDataSet.size(); i++)
            {
                if (intCountedDataSet.get(i) != 0)
                {
                    if (i < yMin)
                    {
                        yMin = i;
                    }
                    if (i > yMax)
                    {
                        yMax = i;
                    }
                }
            }
            int horizontalScale = GRAPH_WIDTH / (totalCount - 1);
            int verticalScale = 200 / (yMax - yMin);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //draw horizontal lines
            g.setColor(new Color(100, 100, 100, 150));
            for (int i = 0; i < yMax - yMin; i++)
            {
                if (yMax - yMin > 6)
                {
                    if (i % ((yMax - yMin) / 6) == 0)
                    {
                        g.drawLine(GRAPH_XS, GRAPH_YE - (i * verticalScale), GRAPH_XE, GRAPH_YE - (i * verticalScale));

                        Color oldColor = g.getColor();
                        g.setColor(new Color(200, 200, 200, 220));
                        g.drawString(getString(i + yMin), GRAPH_XS - g.getFontMetrics().stringWidth(getString(i + yMin)) - 10, GRAPH_YE - (i * verticalScale));
                        g.setColor(oldColor);
                    }
                } else
                {
                    g.drawLine(GRAPH_XS, GRAPH_YE - (i * verticalScale), GRAPH_XE, GRAPH_YE - (i * verticalScale));
                }
            }

            g.setColor(new Color(120, 120, 240, 200));
            for (int i = 0; i < data.size(); i++)
            {
                g.fillOval(GRAPH_XS + (i * horizontalScale) - 1, GRAPH_YE - ((data.get(i) - yMin) * verticalScale) - 2, 4, 4);
                if (i != 0)
                {
                    int previousX = GRAPH_XS + ((i - 1) * horizontalScale) + 1;
                    int previousY = GRAPH_YE - ((data.get(i - 1) - yMin) * verticalScale);
                    g.drawLine(GRAPH_XS + (i * horizontalScale) + 1, GRAPH_YE - ((data.get(i) - yMin) * verticalScale), previousX, previousY);
                }
            }

            //line plot
        }
    }

    private void drawGraphVerticals(Graphics2D g, int highestCount, double scale, int verticalScaleToUse)
    {
        for (int i = 0; i < highestCount + 1; i++)
        {
            {
                int stringOffset = (int) (500 - GRAPH_HEIGHT - GRAPH_YS - scale * i + 8);
                if (i == 0 || i % verticalScaleToUse == 0)
                {
                    Color oldColor = g.getColor();
                    g.setColor(new Color(100, 100, 100, 100));
                    g.drawLine(GRAPH_XS, stringOffset - 8, GRAPH_XE, stringOffset - 8);
                    g.setColor(oldColor);
                    g.drawString(String.valueOf(i), GRAPH_XS - 20, stringOffset);
                }
            }
        }
    }

    private ArrayList<PieChartData> createSortedPieChartData(ArrayList<Integer> data, int total)
    {
        ArrayList<PieChartData> fixedData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++)
        {
            if (data.get(i) != 0)
            {
                fixedData.add(new PieChartData(i, data.get(i), total));
            }
        }
        fixedData.sort((o1, o2) ->
        {
            if (Objects.equals(o1.occurrences, o2.occurrences))
            {
                return 0;
            }
            return o1.occurrences > o2.occurrences ? -1 : 1;
        });
        return fixedData;
    }

    private int getNonZeroCount(ArrayList<Integer> data)
    {
        int count = 0;
        for (Integer i : data)
        {
            if (i != 0)
            {
                count++;
            }
        }
        return count;
    }

    private GraphInternalDataContainer getInternalDataSet(DataPoint key)
    {
        ArrayList<Integer> intDataSet = new ArrayList<>();
        ArrayList<SimpleRaidDataBase> fullDataSet = new ArrayList<>();
        /*
        for (Raid data : internalData)
        {
            if (data.getValue(key) != -1)
            {
                if (data.getTimeAccurate(key))
                {
                    intDataSet.add(data.getValue(key));
                    fullDataSet.add(data);
                }
            }
        }
         */
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

    public void drawBlankPanel()
    {
        Graphics g = img.getGraphics();
        g.setColor(new Color(40, 40, 40));
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.dispose();

        repaint();
    }

    public void drawBlankBarGraph()
    {
        drawGridLines();
        repaint();
    }

    private boolean checkContains(Bounds bound)
    {
        for (Bounds b : selectedBounds)
        {
            if (b.getBottom() == bound.getBottom() && b.getTop() == bound.getTop() && b.getLeft() == bound.getLeft() && b.getRight() == bound.getRight())
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
        for (Bounds bound : bounds)
        {
            if (x >= bound.getLeft() && x <= bound.getRight() && y <= bound.getBottom() && y >= bound.getTop())
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
        for (Bounds b : bounds)
        {
            if (checkOverlap(dragStartX, dragStartY, dragCurrentX, dragCurrentY, b.getLeft(), b.getTop(), b.getRight(), b.getBottom()))
            {
                currentlyIntersecting.add(b);
            }
        }
        selectedBounds.clear();
        selectedBounds.addAll(currentlyIntersecting);
    }

    private boolean checkBounds(int x, int y)
    {
        for (Bounds bound : bounds)
        {
            if (x >= bound.getLeft() && x <= bound.getRight() && y <= bound.getBottom() && y >= bound.getTop())
            {
                return true;
            }
        }
        return false;
    }

    private void checkBoundsAndHighlight(int x, int y)
    {
        for (Bounds bound : bounds)
        {
            if (x >= bound.getLeft() && x <= bound.getRight() && y <= bound.getBottom() && y >= bound.getTop())
            {
                if (!boundActive)
                {
                    requestFocusInWindow();
                    activeBound = new Bounds(bound.getLeft(), bound.getRight(), bound.getBottom(), bound.getTop(), bound.raids);
                    boundActive = true;
                    drawGraph();
                }

            } else if (boundActive)
            {
                if (bound.matches(activeBound))
                {
                    activeBound.reset();
                    boundActive = false;
                    drawGraph();
                }
            }
        }
    }

    private ArrayList<Raid> mergeSelectedData()
    {
        ArrayList<Raid> mergedData = new ArrayList<>();
        for (Bounds b : selectedBounds)
        {
            mergedData.addAll(b.raids);
        }
        return mergedData;
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (e.isShiftDown() && currentlyDragging)
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
        if (e.isShiftDown() && SwingUtilities.isLeftMouseButton(e) && selectedBounds.isEmpty())
        {
            if (checkBounds(e.getX(), e.getY()))
            {
                selectedBounds.add(getBound(e.getX(), e.getY()));
                drawGraph();
            }
        } else if (e.isShiftDown() && SwingUtilities.isLeftMouseButton(e))
        {
            if (checkBounds(e.getX(), e.getY()))
            {
                Bounds lastBound = selectedBounds.get(selectedBounds.size() - 1);
                addAllBoundsBetween(lastBound, getBound(e.getX(), e.getY()));
                drawGraph();
            }
        } else if (SwingUtilities.isLeftMouseButton(e) && !e.isControlDown())
        {
            selectedBounds.clear();
            if (checkBounds(e.getX(), e.getY()))
            {
                selectedBounds.add(getBound(e.getX(), e.getY()));

                drawGraph();
            }
        }
    }

    private void addAllBoundsBetween(Bounds lastBound, Bounds bound)
    {
        for (Bounds b : bounds)
        {
            if ((b.getLeft() > lastBound.getLeft() && b.getLeft() < bound.getLeft()) || (b.getLeft() < lastBound.getLeft() && b.getLeft() > bound.getLeft()))
            {
                if (!checkContains(b))
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
        if (SwingUtilities.isRightMouseButton(e))
        {
            if (selectedBounds.isEmpty())
            {
                for (Bounds bound : bounds)
                {
                    if (e.getX() >= bound.getLeft() && e.getX() <= bound.getRight() && e.getY() <= bound.getBottom() && e.getY() >= bound.getTop())
                    {
                        GraphRightClickContextMenu menu = new GraphRightClickContextMenu(bound.raids, config, itemManager, clientThread, configManager);
                        menu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            } else
            {
                GraphRightClickContextMenu menu = new GraphRightClickContextMenu(mergeSelectedData(), config, itemManager, clientThread, configManager);
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
        if (SwingUtilities.isLeftMouseButton(e) && e.isShiftDown())
        {
            if (!currentlyDragging)
            {
                dragStartX = e.getX();
                dragStartY = e.getY();
                currentlyDragging = true;
            }
        }
        if (SwingUtilities.isLeftMouseButton(e) && e.isControlDown())
        {
            if (checkBounds(e.getX(), e.getY()))
            {
                Bounds clicked = getBound(e.getX(), e.getY());
                if (checkContains(clicked) && clicked != null)
                {
                    selectedBounds.removeIf(b -> (b.getLeft() == clicked.getLeft() && b.getRight() == clicked.getRight() && b.getTop() == clicked.getTop() && b.getBottom() == clicked.getBottom()));
                } else
                {
                    selectedBounds.add(getBound(e.getX(), e.getY()));
                }
                drawGraph();
            }
        }
        if (SwingUtilities.isLeftMouseButton(e))
        {
            if (!checkBounds(e.getX(), e.getY()))
            {
                selectedBounds.clear();
                drawGraph();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e))
        {
            if (currentlyDragging)
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
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.isShiftDown())
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (!e.isShiftDown())
        {
            setCursor(Cursor.getDefaultCursor());
            if (currentlyDragging)
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
