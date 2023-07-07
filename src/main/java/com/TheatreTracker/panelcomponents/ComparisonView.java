package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.utility.RoomUtil;
import com.TheatreTracker.utility.DataPoint;
import com.TheatreTracker.utility.StatisticGatherer;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

@Slf4j
public class ComparisonView extends BaseFrame
{
    private JPanel container;
    private GraphPanel graph;

    private JSlider leftCutOff;
    private JSlider rightCutOff;

    private JTextField leftLabel;
    private JTextField rightLabel;

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
    private JCheckBox matchYScales;

    private JCheckBox matchXScales;

    private JComboBox compareByComboBox;
    private boolean time = false;
    ArrayList<ArrayList<RoomData>> data;

    JPanel scrollTopPanel;
    JPanel scrollBottomPanel;
    JScrollPane scrollTopGraphData;
    JScrollPane scrollBottomGraphData;
    ArrayList<GraphPanel> topGraphs;
    ArrayList<GraphPanel> bottomGraphs;
    ArrayList<String> labels;

    JSpinner groupSizeSpinner;
    SpinnerNumberModel spinnerSizeModel;
    SpinnerNumberModel spinnerOffsetModel;
    JSpinner groupOffsetSpinner;

    JCheckBox groupingEnabled;
    public ComparisonView(ArrayList<ArrayList<RoomData>> raidData, ArrayList<String> names)
    {
        leftLabel = new JTextField("Min cutoff: ");
        rightLabel = new JTextField("Max cutoff: ");
        leftLabel.setEditable(false);
        rightLabel.setEditable(false);
        leftCutOff = new JSlider();
        rightCutOff = new JSlider();
        topGraphs = new ArrayList<>();
        bottomGraphs = new ArrayList<>();
        data = raidData;
        matchYScales = new JCheckBox("Match Y-Axis", true);
        matchXScales = new JCheckBox("Match X-Axis", true);

        spinnerSizeModel = new SpinnerNumberModel(1, 1, 100, 1);
        groupSizeSpinner = new JSpinner(spinnerSizeModel);

        spinnerOffsetModel = new SpinnerNumberModel(0, 0, 0, 1);
        groupOffsetSpinner = new JSpinner(spinnerOffsetModel);

        groupingEnabled = new JCheckBox("Enable Grouping?");
        groupingEnabled.setSelected(false);
        groupOffsetSpinner.setEnabled(false);
        groupSizeSpinner.setEnabled(false);

        groupingEnabled.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(GraphPanel panel : topGraphs)
                {
                    panel.setGroupingEnabled(groupingEnabled.isSelected());
                    panel.updateGroupOffset((Integer) groupOffsetSpinner.getValue());
                    panel.updateGroupSize((Integer) groupSizeSpinner.getValue());

                }
                for(GraphPanel panel : bottomGraphs)
                {
                    panel.setGroupingEnabled(groupingEnabled.isSelected());
                    panel.updateGroupOffset((Integer) groupOffsetSpinner.getValue());
                    panel.updateGroupSize((Integer) groupSizeSpinner.getValue());


                }
                groupSizeSpinner.setEnabled(groupingEnabled.isSelected());
                groupOffsetSpinner.setEnabled(groupingEnabled.isSelected());

            }
        });

        groupSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for(GraphPanel panel : topGraphs)
                {
                    panel.updateGroupSize((int)groupSizeSpinner.getValue());
                }
                for(GraphPanel panel : bottomGraphs)
                {
                    panel.updateGroupSize((int)groupSizeSpinner.getValue());
                }
                spinnerOffsetModel.setMaximum((int)groupSizeSpinner.getValue()-1);
            }
        });

        groupOffsetSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for(GraphPanel panel : topGraphs)
                {
                    panel.updateGroupOffset((int)groupOffsetSpinner.getValue());
                }
                for(GraphPanel panel : bottomGraphs)
                {
                    panel.updateGroupOffset((int)groupOffsetSpinner.getValue());
                }
                spinnerSizeModel.setMinimum((int)groupOffsetSpinner.getValue()+1);
            }
        });


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
        compareByComboBox = new JComboBox(DataPoint.getByNames());
        compareByComboBox.addActionListener(al->
        {
            switchGraphData();
            updateOtherPanels();
        });

        matchYScales.addActionListener(al->
        {
            switchGraphData();
        });

        matchXScales.addActionListener(al->
        {
            switchGraphData();
            leftCutOff.setEnabled(matchXScales.isSelected());
            rightCutOff.setEnabled(matchXScales.isSelected());
        });

        container = new JPanel();
        container.setPreferredSize(new Dimension(1000, 680));
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
        time = DataPoint.values()[compareByComboBox.getSelectedIndex()].type == DataPoint.types.TIME;
        //time = compareByComboBox.getSelectedIndex() > 29; //TODO shouldn't be needed
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
        if(matchYScales.isSelected())
        {
            setYScales(yHigh);
        }
        if(matchXScales.isSelected())
        {
            setXScales(xLow, xHigh);
        }

        redrawGraphs();

    }

    private void setXScales(int xLow, int xHigh)
    {
        for(int i = 0; i < topGraphs.size(); i++)
        {
            topGraphs.get(i).setScales(xLow, xHigh, topGraphs.get(i).getScaleYHigh());
            bottomGraphs.get(i).setScales(xLow, xHigh, bottomGraphs.get(i).getScaleYHigh());
        }
    }

    private void setYScales(int yHigh)
    {
        for(int i = 0; i < topGraphs.size(); i++)
        {
            topGraphs.get(i).setScales(topGraphs.get(i).getScaleXLow(), topGraphs.get(i).getScaleXHigh(), yHigh);
            bottomGraphs.get(i).setScales(bottomGraphs.get(i).getScaleXLow(), bottomGraphs.get(i).getScaleXHigh(), yHigh);
        }
    }

    private void redrawGraphs(int xLow, int xHigh)
    {
        for(int i = 0; i < topGraphs.size(); i++)
        {
            topGraphs.get(i).setScales(xLow, xHigh, topGraphs.get(i).getScaleYHigh());
            bottomGraphs.get(i).setScales(xLow, xHigh, bottomGraphs.get(i).getScaleYHigh());
            topGraphs.get(i).setBounds();
            topGraphs.get(i).drawGraph();
            bottomGraphs.get(i).setBounds();
            bottomGraphs.get(i).drawGraph();
        }
    }


    private void redrawGraphs()
    {
        for(int i = 0; i < topGraphs.size(); i++)
        {
            topGraphs.get(i).setBounds();
            topGraphs.get(i).drawGraph();
            bottomGraphs.get(i).setBounds();
            bottomGraphs.get(i).drawGraph();
        }
    }

    private void updateCutoffs()
    {
        if(matchXScales.isSelected())
        {
            redrawGraphs(leftCutOff.getValue(), rightCutOff.getMaximum() - rightCutOff.getValue() + rightCutOff.getMinimum());

        }
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

    GraphPanel getGraphPanel(ArrayList<RoomData> points)
    {
        GraphPanel graphPanel = new GraphPanel(points);
        return graphPanel;
    }

    private ArrayList<Integer> getArrayForStatistics(ArrayList<RoomData> data)
    {
        ArrayList<Integer> arrayToPass = new ArrayList<>();
        for(RoomData raidData : data)
        {
            int value = raidData.getValue(DataPoint.getValue(String.valueOf(compareByComboBox.getSelectedItem())));
            if(value > -1)
            {
                if(!time || value != 0)
                {
                    switch((Objects.requireNonNull(DataPoint.getValue(String.valueOf(compareByComboBox.getSelectedItem())))).room)
                    {
                        case MAIDEN:
                            if(!raidData.maidenStartAccurate || !raidData.maidenEndAccurate)
                            {
                                continue;
                            }
                            break;
                        case BLOAT:
                            if(!raidData.bloatStartAccurate || !raidData.bloatEndAccurate)
                            {
                                continue;
                            }
                            break;
                        case NYLOCAS:
                            if(!raidData.nyloStartAccurate || !raidData.nyloEndAccurate)
                            {
                                continue;
                            }
                            break;
                        case SOTETSEG:
                            if(!raidData.soteStartAccurate || !raidData.soteEndAccurate)
                            {
                                continue;
                            }
                            break;
                        case XARPUS:
                            if(!raidData.xarpStartAccurate || !raidData.xarpEndAccurate)
                            {
                                continue;
                            }
                            break;
                        case VERZIK:
                            if(!raidData.verzikStartAccurate || !raidData.verzikEndAccurate)
                            {
                                continue;
                            }
                            break;
                    }
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

            ArrayList<RoomData> topGraphData = (data.get(topGraphTabs.getSelectedIndex()));
            ArrayList<RoomData> bottomGraphData = data.get(bottomGraphTabs.getSelectedIndex());

            String worse = "<html><font color='#F63131'>";
            String better = "<html><font color='#99E622'>";
            String even = "<html><font color='#CCCCF6'>";

            double g1a = StatisticGatherer.getGenericAverage(getArrayForStatistics(topGraphData));
            double g1med = StatisticGatherer.getGenericMedian(getArrayForStatistics(topGraphData));
            double g1mod = StatisticGatherer.getGenericMode(getArrayForStatistics(topGraphData));
            double g1max = StatisticGatherer.getGenericMax(getArrayForStatistics(topGraphData));
            double g1min = StatisticGatherer.getGenericMin(getArrayForStatistics(topGraphData));

            double g2a = StatisticGatherer.getGenericAverage(getArrayForStatistics(bottomGraphData));
            double g2med = StatisticGatherer.getGenericMedian(getArrayForStatistics(bottomGraphData));
            double g2mod = StatisticGatherer.getGenericMode(getArrayForStatistics(bottomGraphData));
            double g2max = StatisticGatherer.getGenericMax(getArrayForStatistics(bottomGraphData));
            double g2min = StatisticGatherer.getGenericMin(getArrayForStatistics(bottomGraphData));

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

            ArrayList<Integer> topSet = GraphPanel.getCounts(getArrayForStatistics(topGraphData), valX);
            ArrayList<Integer> bottomSet = GraphPanel.getCounts(getArrayForStatistics(bottomGraphData), valX);

            scrollTopPanel.removeAll();
            scrollBottomPanel.removeAll();

            scrollTopPanel.add(new JLabel("Value", SwingConstants.LEFT));
            scrollTopPanel.add(new JLabel("Count", SwingConstants.RIGHT));

            int total = GraphPanel.getCountedTotal(topSet);
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

            total = GraphPanel.getCountedTotal(bottomSet);

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
            GraphPanel topGraph = getGraphPanel(data.get(i));
            GraphPanel bottomGraph = getGraphPanel(data.get(i));
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
        sidebar.setPreferredSize(new Dimension(390, 680));

        JPanel graphOptionsPanel = new JPanel();
        graphOptionsPanel.setBorder(BorderFactory.createTitledBorder("Graph Options"));
        graphOptionsPanel.setPreferredSize(new Dimension(210, 190));

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
        graphOptionsPanel.add(matchXScales);
        graphOptionsPanel.add(matchYScales);

        graphOptionsPanel.add(new JLabel(""));

        graphOptionsPanel.add(groupingEnabled);
        graphOptionsPanel.add(new JLabel("Group Size: "));
        graphOptionsPanel.add(groupSizeSpinner);
        graphOptionsPanel.add(new JLabel("Group Offset: "));
        graphOptionsPanel.add(groupOffsetSpinner);

        graphOptionsPanel.add(new JCheckBox("Show Chronological"));

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
