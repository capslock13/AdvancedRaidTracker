package com.cTimers.panelcomponents;

import com.cTimers.cRoomData;
import com.cTimers.utility.RoomUtil;
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

    private JTabbedPane topGraphTabs;
    private JTabbedPane bottomGraphTabs;

    private JComboBox compareByComboBox;
    private boolean time = false;
    ArrayList<ArrayList<cRoomData>> data;

    ArrayList<cGraphPanel> topGraphs;
    ArrayList<cGraphPanel> bottomGraphs;
    ArrayList<String> labels;
    cComparisonView(ArrayList<ArrayList<cRoomData>> raidData, ArrayList<String> names)
    {
        leftLabel = new JLabel("Min cutoff: ");
        rightLabel = new JLabel("Max cutoff: ");
        leftCutOff = new JSlider();
        rightCutOff = new JSlider();
        topGraphs = new ArrayList<>();
        bottomGraphs = new ArrayList<>();
        data = raidData;
        labels = names;
        topGraphTabs = new JTabbedPane();
        topGraphTabs.addChangeListener(cl ->
        {
            switchGraphData();
        });
        bottomGraphTabs = new JTabbedPane();
        bottomGraphTabs.addChangeListener(cl->
        {
            switchGraphData();
        });
        compareByComboBox = new JComboBox(cRoomData.DataPoint);
        compareByComboBox.addActionListener(al->
        {
            switchGraphData();
        });

        container = new JPanel();
        container.setPreferredSize(new Dimension(800, 600));
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        buildUI();
        add(container);
        pack();
    }

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
        sidebar.setPreferredSize(new Dimension(190, 600));

        JPanel graphOptionsPanel = new JPanel();
        graphOptionsPanel.setBorder(BorderFactory.createTitledBorder("Graph Options"));

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
        compareByPanel.add(compareByComboBox);



        JPanel otherPanel = new JPanel();
        otherPanel.setBorder(BorderFactory.createTitledBorder("Other"));
        otherPanel.setPreferredSize(new Dimension(200, 170));

        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.add(compareByPanel);
        sidebar.add(graphOptionsPanel);
        sidebar.add(otherPanel);

        container.add(sidebar);
    }
}
