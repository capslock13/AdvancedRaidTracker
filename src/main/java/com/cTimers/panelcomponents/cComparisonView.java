package com.cTimers.panelcomponents;

import com.cTimers.cRoomData;
import com.cTimers.utility.RoomUtil;
import com.cTimers.utility.cDataPoint;
import com.cTimers.utility.cStatisticGatherer;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
@Slf4j
public class cComparisonView extends cFrame
{
    private JPanel container;
    private cGraphPanel graph;

    private JSlider leftCutOff;
    private JSlider rightCutOff;

    private JLabel leftLabel;
    private JLabel rightLabel;

    private JLabel graph1Average;
    private JLabel graph1Median;
    private JLabel graph1Maximum;
    private JLabel graph1Minimum;
    private JLabel graph1Mode;

    private JLabel graph2Average;
    private JLabel graph2Median;
    private JLabel graph2Maximum;
    private JLabel graph2Minimum;
    private JLabel graph2Mode;

    private JPanel otherTopLeft;
    private JPanel otherTopRight;
    private JPanel otherBottomLeft;
    private JPanel otherBottomRight;
    private JTabbedPane topGraphTabs;
    private JTabbedPane bottomGraphTabs;

    private JComboBox compareByComboBox;
    private boolean time = false;
    ArrayList<ArrayList<cRoomData>> data;

    JPanel scrollTopPanel;
    JPanel scrollBottomPanel;
    JScrollPane scrollTopGraphData;
    JScrollPane scrollBottomGraphData;
    ArrayList<cGraphPanel> topGraphs;
    ArrayList<cGraphPanel> bottomGraphs;
    ArrayList<String> labels;
    public cComparisonView(ArrayList<ArrayList<cRoomData>> raidData, ArrayList<String> names)
    {
        leftLabel = new JLabel("Min cutoff: ");
        rightLabel = new JLabel("Max cutoff: ");
        leftCutOff = new JSlider();
        rightCutOff = new JSlider();
        topGraphs = new ArrayList<>();
        bottomGraphs = new ArrayList<>();
        data = raidData;
        labels = names;

        scrollTopPanel = new JPanel();
        scrollBottomPanel = new JPanel();

        scrollBottomGraphData = new JScrollPane(scrollBottomPanel);
        scrollTopGraphData = new JScrollPane(scrollTopPanel);

        graph1Average = new JLabel("", SwingConstants.RIGHT);
        graph1Median = new JLabel("", SwingConstants.RIGHT);
        graph1Mode = new JLabel("", SwingConstants.RIGHT);
        graph1Maximum = new JLabel("", SwingConstants.RIGHT);
        graph1Minimum = new JLabel("", SwingConstants.RIGHT);

        graph2Average = new JLabel("", SwingConstants.RIGHT);
        graph2Median = new JLabel("", SwingConstants.RIGHT);
        graph2Mode = new JLabel("", SwingConstants.RIGHT);
        graph2Maximum = new JLabel("", SwingConstants.RIGHT);
        graph2Minimum = new JLabel("", SwingConstants.RIGHT);

        topGraphTabs = new JTabbedPane();
        topGraphTabs.addChangeListener(cl ->
        {
            switchGraphData();
            updateOtherPanels();
        });
        bottomGraphTabs = new JTabbedPane();
        bottomGraphTabs.addChangeListener(cl->
        {
            switchGraphData();
            updateOtherPanels();
        });
        compareByComboBox = new JComboBox(cDataPoint.getByNames());
        compareByComboBox.addActionListener(al->
        {
            switchGraphData();
            updateOtherPanels();
        });

        container = new JPanel();
        container.setPreferredSize(new Dimension(980, 600));
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        buildUI();
        add(container);
        pack();
    }

    private int valX = 0;

    private void switchGraphData()
    {
        if(topGraphs.size() != bottomGraphs.size())
        {
            return;
        }
        int xHigh = 0;
        int xLow = Integer.MAX_VALUE;
        int yHigh = 0;
        time = compareByComboBox.getSelectedIndex() > 29;
        for(int i = 0; i < topGraphs.size(); i++)
        {
            topGraphs.get(i).switchKey(compareByComboBox.getSelectedIndex());
            bottomGraphs.get(i).switchKey(compareByComboBox.getSelectedIndex());
            topGraphs.get(i).generateScales();
            bottomGraphs.get(i).generateScales();

            xHigh = Math.max(xHigh, topGraphs.get(i).getScaleXHigh());
            xHigh = Math.max(xHigh, bottomGraphs.get(i).getScaleXHigh());

            yHigh = Math.max(yHigh, topGraphs.get(i).getScaleYHigh());
            yHigh = Math.max(yHigh, bottomGraphs.get(i).getScaleYHigh());

            xLow = Math.min(xLow, topGraphs.get(i).getScaleXLow());
            xLow = Math.min(xLow, bottomGraphs.get(i).getScaleXLow());
        }


        leftCutOff.setMaximum(xHigh);
        leftCutOff.setMinimum(xLow);
        rightCutOff.setMaximum(xHigh);
        rightCutOff.setMinimum(xLow);

        leftCutOff.setValue(xLow);
        rightCutOff.setValue(xLow);

        updateSliders();
        container.repaint();
        valX = xHigh;
        redrawGraphs(xLow, xHigh, yHigh);

    }

    private void redrawGraphs(int xLow, int xHigh)
    {
        if(topGraphs.size()!= 0)
        {
            redrawGraphs(xLow, xHigh, topGraphs.get(0).getScaleYHigh());
        }
    }


    private void redrawGraphs(int xLow, int xHigh, int yHigh)
    {
        for(int i = 0; i < topGraphs.size(); i++)
        {
            topGraphs.get(i).setScales(xLow, xHigh, yHigh);
            bottomGraphs.get(i).setScales(xLow, xHigh, yHigh);
            topGraphs.get(i).setBounds();
            topGraphs.get(i).drawGraph();
            bottomGraphs.get(i).setBounds();
            bottomGraphs.get(i).drawGraph();
        }
    }

    private void updateCutoffs()
    {
        redrawGraphs(leftCutOff.getValue(), rightCutOff.getMaximum()-rightCutOff.getValue()+rightCutOff.getMinimum());
    }

    private void updateSliders()
    {
        int leftExtent = rightCutOff.getValue()-rightCutOff.getMinimum();
        int rightExtent = leftCutOff.getValue()-leftCutOff.getMinimum();
        rightCutOff.setExtent(rightExtent);
        leftCutOff.setExtent(leftExtent);

        //For some unbelievably bizarre reasons extents can only by set for the upper bound so we have to inverse it

        leftLabel.setText("Min cutoff: " + ((time) ? RoomUtil.time(leftCutOff.getValue()) : leftCutOff.getValue()));
        rightLabel.setText("Max cutoff: " + ((time) ? RoomUtil.time(rightCutOff.getMaximum()-rightCutOff.getValue()+rightCutOff.getMinimum()) : rightCutOff.getMaximum()-rightCutOff.getValue()+rightCutOff.getMinimum()));
        updateCutoffs();
    }

    cGraphPanel getGraphPanel(ArrayList<cRoomData> points)
    {
        cGraphPanel graphPanel = new cGraphPanel(points);
        return graphPanel;
    }

    private ArrayList<Integer> getArrayForStatistics(ArrayList<cRoomData> data)
    {
        ArrayList<Integer> arrayToPass = new ArrayList<>();
        for(cRoomData raidData : data)
        {
            int value = raidData.getValue(cDataPoint.getValue(String.valueOf(compareByComboBox.getSelectedItem())));

            if(value > -1)
            {
                if(!time || value != 0)
                {
                    arrayToPass.add(value);
                }
            }
        }
        return arrayToPass;
    }

    private String getString(double val)
    {
        return (time) ? RoomUtil.time(val) : ""+Math.round(val*100.0)/100.0;
    }
    private void updateOtherPanels()
    {
        if(topGraphTabs.getSelectedIndex() != -1 && bottomGraphTabs.getSelectedIndex() != -1 && built)
        {
            otherTopLeft.setBorder(BorderFactory.createTitledBorder(topGraphTabs.getTitleAt(topGraphTabs.getSelectedIndex())));
            otherTopRight.setBorder(BorderFactory.createTitledBorder(bottomGraphTabs.getTitleAt(bottomGraphTabs.getSelectedIndex())));

            otherBottomLeft.setBorder(BorderFactory.createTitledBorder(topGraphTabs.getTitleAt(topGraphTabs.getSelectedIndex()) + " values"));
            otherBottomRight.setBorder(BorderFactory.createTitledBorder(bottomGraphTabs.getTitleAt(bottomGraphTabs.getSelectedIndex()) + " values"));

            ArrayList<cRoomData> topGraphData = (data.get(topGraphTabs.getSelectedIndex()));
            ArrayList<cRoomData> bottomGraphData = data.get(bottomGraphTabs.getSelectedIndex());

            String worse = "<html><font color='#F63131'>";
            String better = "<html><font color='#99E622'>";
            String even = "<html><font color='#CCCCF6'>";

            double g1a = cStatisticGatherer.getGenericAverage(getArrayForStatistics(topGraphData));
            double g1med = cStatisticGatherer.getGenericMedian(getArrayForStatistics(topGraphData));
            double g1mod = cStatisticGatherer.getGenericMode(getArrayForStatistics(topGraphData));
            double g1max = cStatisticGatherer.getGenericMax(getArrayForStatistics(topGraphData));
            double g1min = cStatisticGatherer.getGenericMin(getArrayForStatistics(topGraphData));

            double g2a = cStatisticGatherer.getGenericAverage(getArrayForStatistics(bottomGraphData));
            double g2med = cStatisticGatherer.getGenericMedian(getArrayForStatistics(bottomGraphData));
            double g2mod = cStatisticGatherer.getGenericMode(getArrayForStatistics(bottomGraphData));
            double g2max = cStatisticGatherer.getGenericMax(getArrayForStatistics(bottomGraphData));
            double g2min = cStatisticGatherer.getGenericMin(getArrayForStatistics(bottomGraphData));

            String g1as = (g1a<g2a) ? better : g2a==g1a ? even : worse;
            String g1meds = (g1med<g2med) ? better : g2med==g1med ? even : worse;
            String g1mods = (g1mod<g2mod) ? better : g2mod==g1mod ? even : worse;
            String g1maxs = (g1max<g2max) ? better : g2max==g1max ? even : worse;
            String g1mins = (g1min<g2min) ? better : g2min==g1min ? even : worse;

            String g2as = (g1a>g2a) ? better : g2a==g1a ? even : worse;
            String g2meds = (g1med>g2med) ? better : g2med==g1med ? even : worse;
            String g2mods = (g1mod>g2mod) ? better : g2mod==g1mod ? even : worse;
            String g2maxs = (g1max>g2max) ? better : g2max==g1max ? even : worse;
            String g2mins = (g1min>g2min) ? better : g2min==g1min ? even : worse;


            graph1Average.setText(g1as+getString(g1a));
            graph1Median.setText(g1meds+getString(g1med));
            graph1Mode.setText(g1mods+getString(g1mod));
            graph1Maximum.setText(g1maxs+getString(g1max));
            graph1Minimum.setText(g1mins+getString(g1min));

            graph2Average.setText(g2as+getString(g2a));
            graph2Median.setText(g2meds+getString(g2med));
            graph2Mode.setText(g2mods+getString(g2mod));
            graph2Maximum.setText(g2maxs+getString(g2max));
            graph2Minimum.setText(g2mins+getString(g2min));

            ArrayList<Integer> topSet = cGraphPanel.getCounts(getArrayForStatistics(topGraphData), valX);
            ArrayList<Integer> bottomSet = cGraphPanel.getCounts(getArrayForStatistics(bottomGraphData), valX);

            scrollTopPanel.removeAll();
            scrollBottomPanel.removeAll();

            scrollTopPanel.add(new JLabel("Value", SwingConstants.LEFT));
            scrollTopPanel.add(new JLabel("Count", SwingConstants.RIGHT));

            int total = cGraphPanel.getCountedTotal(topSet);
            int count = 0;
            for(int i = 0; i < topSet.size(); i++)
            {
                if(topSet.get(i) > 0)
                {
                    String percent = Math.round((100.0 * topSet.get(i) / (double) total)*100.0)/100.0 + "%";
                    scrollTopPanel.add(new JLabel(getString(i), SwingConstants.LEFT));
                    scrollTopPanel.add(new JLabel(topSet.get(i) + " (" + percent + ")", SwingConstants.RIGHT));
                    count++;
                }
            }

            int altCount = 0;
            for(int i = count; i < 16; i++)
            {
                scrollTopPanel.add(new JLabel());
                scrollTopPanel.add(new JLabel());
                altCount++;
            }
            scrollTopPanel.setLayout(new GridLayout(count+1+altCount, 2));
            count = 0;

            scrollBottomPanel.add(new JLabel("Value", SwingConstants.LEFT));
            scrollBottomPanel.add(new JLabel("Count", SwingConstants.RIGHT));

            total = cGraphPanel.getCountedTotal(bottomSet);

            for(int i = 0; i < bottomSet.size(); i++)
            {
                if(bottomSet.get(i) > 0)
                {
                    String percent = Math.round((100.0 * bottomSet.get(i) / (double) total)*100.0)/100.0 + "%";
                    scrollBottomPanel.add(new JLabel(getString(i), SwingConstants.LEFT));
                    scrollBottomPanel.add(new JLabel(bottomSet.get(i) + " (" + percent + ")", SwingConstants.RIGHT));
                    count++;
                }
            }
            altCount = 0;
            for(int i = count; i < 16; i++)
            {
                scrollBottomPanel.add(new JLabel());
                scrollBottomPanel.add(new JLabel());
                altCount++;
            }
            scrollBottomPanel.setLayout(new GridLayout(count+1+altCount, 2));

            scrollTopPanel.validate();
            scrollBottomPanel.validate();
        }
        container.repaint();
    }
    private boolean built = false;

    private void buildUI()
    {
        for(int i = 0; i < data.size(); i++)
        {
            cGraphPanel topGraph = getGraphPanel(data.get(i));
            cGraphPanel bottomGraph = getGraphPanel(data.get(i));
            topGraphTabs.addTab(labels.get(i), topGraph);
            bottomGraphTabs.addTab(labels.get(i), bottomGraph);
            topGraphs.add(topGraph);
            bottomGraphs.add(bottomGraph);
        }
        switchGraphData();

        JPanel leftContainer = new JPanel();
        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.Y_AXIS));

        leftContainer.add(topGraphTabs);
        leftContainer.add(bottomGraphTabs);

        container.add(leftContainer);

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(370, 600));

        JPanel graphOptionsPanel = new JPanel();
        graphOptionsPanel.setBorder(BorderFactory.createTitledBorder("Graph Options"));
        graphOptionsPanel.setPreferredSize(new Dimension(190, 110));

        leftCutOff.setPaintLabels(true);
        leftCutOff.setPaintTicks(true);
        leftCutOff.setPaintTrack(true);

        leftCutOff.addChangeListener(cl->
        {
            Object source = cl.getSource();
            if(source instanceof JSlider)
            {
                if(((JSlider) source).getValueIsAdjusting())
                {
                    updateSliders();
                }
            }});

        rightCutOff.addChangeListener(cl->
        {
            Object source = cl.getSource();
            if(source instanceof JSlider)
            {
                if (((JSlider) source).getValueIsAdjusting())
                {
                    updateSliders();
                }
            }});

        leftCutOff.setPreferredSize(new Dimension(180, leftCutOff.getPreferredSize().height));

        rightCutOff.setPaintTrack(true);
        rightCutOff.setPaintTicks(true);
        rightCutOff.setPaintLabels(true);

        rightCutOff.setInverted(true);

        rightCutOff.setPreferredSize(new Dimension(180, rightCutOff.getPreferredSize().height));


        graphOptionsPanel.add(leftCutOff);
        graphOptionsPanel.add(leftLabel);
        graphOptionsPanel.add(rightCutOff);
        graphOptionsPanel.add(rightLabel);
        JPanel compareByPanel = new JPanel();
        compareByPanel.setBorder(BorderFactory.createTitledBorder("Compare by"));
        compareByPanel.setPreferredSize(new Dimension(190, 60));
        compareByPanel.add(compareByComboBox);




        JPanel otherPanel = new JPanel();
        otherPanel.setBorder(BorderFactory.createTitledBorder("Other"));
        otherPanel.setPreferredSize(new Dimension(190, 430));
        otherPanel.setLayout(new BoxLayout(otherPanel, BoxLayout.Y_AXIS));

        JPanel otherTop = new JPanel();
        otherTop.setLayout(new BoxLayout(otherTop, BoxLayout.X_AXIS));

        JPanel otherBottom = new JPanel();
        otherBottom.setLayout(new BoxLayout(otherBottom, BoxLayout.X_AXIS));

        otherTopLeft = new JPanel();
        otherTopLeft.setBorder(BorderFactory.createTitledBorder("Set 0"));
        otherTopLeft.setPreferredSize(new Dimension(100, 100));

        otherTopRight = new JPanel();
        otherTopRight.setBorder(BorderFactory.createTitledBorder("Set 1"));
        otherTopRight.setPreferredSize(new Dimension(100, 100));

        otherTop.add(otherTopLeft);
        otherTop.add(otherTopRight);

        otherBottomLeft = new JPanel();
        otherBottomLeft.setBorder(BorderFactory.createTitledBorder("Values"));

        otherTopLeft.setLayout(new GridLayout(5, 2));
        otherTopRight.setLayout(new GridLayout(5, 2));



        otherTopLeft.add(new JLabel("Average ", SwingConstants.LEFT));
        otherTopLeft.add(graph1Average);
        otherTopLeft.add(new JLabel("Median ", SwingConstants.LEFT));
        otherTopLeft.add(graph1Median);
        otherTopLeft.add(new JLabel("Mode ", SwingConstants.LEFT));
        otherTopLeft.add(graph1Mode);
        otherTopLeft.add(new JLabel("Maximum ", SwingConstants.LEFT));
        otherTopLeft.add(graph1Maximum);
        otherTopLeft.add(new JLabel("Minimum ", SwingConstants.LEFT));
        otherTopLeft.add(graph1Minimum);

        otherTopRight.add(new JLabel("Average ", SwingConstants.LEFT));
        otherTopRight.add(graph2Average);
        otherTopRight.add(new JLabel("Median ", SwingConstants.LEFT));
        otherTopRight.add(graph2Median);
        otherTopRight.add(new JLabel("Mode ", SwingConstants.LEFT));
        otherTopRight.add(graph2Mode);
        otherTopRight.add(new JLabel("Maximum ", SwingConstants.LEFT));
        otherTopRight.add(graph2Maximum);
        otherTopRight.add(new JLabel("Minimum ", SwingConstants.LEFT));
        otherTopRight.add(graph2Minimum);

        otherBottomRight = new JPanel();
        otherBottomRight.setBorder(BorderFactory.createTitledBorder("Values"));

        scrollTopGraphData.setPreferredSize(new Dimension(150, 275));
        scrollBottomGraphData.setPreferredSize(new Dimension(150, 275));

        scrollTopGraphData.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollBottomGraphData.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        otherBottomLeft.add(scrollTopGraphData);
        otherBottomRight.add(scrollBottomGraphData);


        otherBottom.add(otherBottomLeft);
        otherBottom.add(otherBottomRight);

        scrollTopPanel.validate();
        scrollBottomPanel.validate();


        otherPanel.add(otherTop);
        otherPanel.add(otherBottom);

        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.add(compareByPanel);
        sidebar.add(graphOptionsPanel);
        sidebar.add(otherPanel);

        built = true;
        updateOtherPanels();

        container.add(sidebar);
    }
}
