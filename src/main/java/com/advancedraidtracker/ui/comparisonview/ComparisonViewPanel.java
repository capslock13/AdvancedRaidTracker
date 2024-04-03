package com.advancedraidtracker.ui.comparisonview;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.ui.DataPointMenu;
import com.advancedraidtracker.ui.UpdateableWindow;
import com.advancedraidtracker.ui.comparisonview.graph.GraphPanel;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.StatisticGatherer;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

import static com.advancedraidtracker.constants.RaidRoom.*;
import static com.advancedraidtracker.utility.UISwingUtility.*;
import static com.advancedraidtracker.utility.UISwingUtility.getThemedLabel;

@Slf4j
public class ComparisonViewPanel extends JPanel implements UpdateableWindow
{
    private final JPanel container;
    private final JSlider leftCutOff;
    private final JSlider rightCutOff;
    private final JSlider threshold;

    private final JTextField leftLabel;
    private final JTextField rightLabel;
    private final JTextField thresholdLabel;

    private final JLabel graph1Average;
    private final JLabel graph1Median;
    private final JLabel graph1Maximum;
    private final JLabel graph1Minimum;
    private final JLabel graph1Mode;
    private final JPanel otherPanel = getThemedPanel();
    private final JLabel graph2Average;
    private final JLabel graph2Median;
    private final JLabel graph2Maximum;
    private final JLabel graph2Minimum;
    private final JLabel graph2Mode;

    private final JLabel graph1PercentThreshold;
    private final JLabel graph2PercentThreshold;

    private String panelName = "Other";
    private JPanel otherTopLeft;
    private JPanel otherTopRight;
    private JPanel otherBottomLeft;
    private JPanel otherBottomRight;
    private final JTabbedPane topGraphTabs;
    private final JTabbedPane bottomGraphTabs;
    private final JCheckBox matchYScales;

    private final JCheckBox matchXScales;

    private final JComboBox<String> compareByComboBox;
    private final JComboBox<String> graphTypeComboBox;
    private boolean time = false;
    List<List<Raid>> data;

    JPanel scrollTopPanel;
    JPanel scrollBottomPanel;
    JScrollPane scrollTopGraphData;
    JScrollPane scrollBottomGraphData;
    List<GraphPanel> topGraphs;
    List<GraphPanel> bottomGraphs;
    List<String> labels;

    JSpinner groupSizeSpinner;
    SpinnerNumberModel spinnerSizeModel;
    SpinnerNumberModel spinnerOffsetModel;
    JSpinner groupOffsetSpinner;
    JLabel leftThresholdLabel;
    JLabel rightThresholdLabel;

    JCheckBox groupingEnabled;

    private final AdvancedRaidTrackerConfig config;
    private final ItemManager itemManager;
    private final ConfigManager configManager;

    private final ClientThread clientThread;
    private List<String> comboFlatData = new ArrayList<>();

    public ComparisonViewPanel(List<List<Raid>> raidData, List<String> names, AdvancedRaidTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager)
    {
        this.configManager = configManager;
        this.clientThread = clientThread;
        this.itemManager = itemManager;
        this.config = config;

        setBackground(config.primaryDark());
        setOpaque(true);

        leftLabel = getThemedTextField("Min cutoff: ");
        rightLabel = getThemedTextField("Max cutoff: ");
        leftThresholdLabel = getThemedLabel("% <= ");
        rightThresholdLabel = getThemedLabel("% <= ");
        thresholdLabel = getThemedTextField("Threshold: ");
        thresholdLabel.setEditable(false);
        leftLabel.setEditable(false);
        rightLabel.setEditable(false);
        leftCutOff = getThemedSlider();
        rightCutOff = getThemedSlider();
        threshold = getThemedSlider();
        topGraphs = new ArrayList<>();
        bottomGraphs = new ArrayList<>();
        data = raidData;
        matchYScales = getThemedCheckBox("Match Y-Axis", true);
        matchXScales = getThemedCheckBox("Match X-Axis", true);

        spinnerSizeModel = new SpinnerNumberModel(1, 1, 100, 1);
        groupSizeSpinner = getThemedSpinner(spinnerSizeModel);

        spinnerOffsetModel = new SpinnerNumberModel(0, 0, 0, 1);
        groupOffsetSpinner = getThemedSpinner(spinnerOffsetModel);

        groupingEnabled = getThemedCheckBox("Enable Grouping?");
        groupingEnabled.setSelected(false);
        groupOffsetSpinner.setEnabled(false);
        groupSizeSpinner.setEnabled(false);

        graphTypeComboBox = getThemedComboBox(new String[]{"Bar Graph", "Pie Chart", "Line Plot"});

        graphTypeComboBox.addActionListener(e ->
        {
            for (GraphPanel panel : topGraphs)
            {
                panel.setGraphType(graphTypeComboBox.getSelectedIndex());
            }
            for (GraphPanel panel : bottomGraphs)
            {
                panel.setGraphType(graphTypeComboBox.getSelectedIndex());
            }
        });
        groupingEnabled.addActionListener(e ->
        {
            for (GraphPanel panel : topGraphs)
            {
                panel.setGroupingEnabled(groupingEnabled.isSelected());
                panel.updateGroupOffset((Integer) groupOffsetSpinner.getValue());
                panel.updateGroupSize((Integer) groupSizeSpinner.getValue());

            }
            for (GraphPanel panel : bottomGraphs)
            {
                panel.setGroupingEnabled(groupingEnabled.isSelected());
                panel.updateGroupOffset((Integer) groupOffsetSpinner.getValue());
                panel.updateGroupSize((Integer) groupSizeSpinner.getValue());


            }
            groupSizeSpinner.setEnabled(groupingEnabled.isSelected());
            groupOffsetSpinner.setEnabled(groupingEnabled.isSelected());

        });

        groupSizeSpinner.addChangeListener(e ->
        {
            for (GraphPanel panel : topGraphs)
            {
                panel.updateGroupSize((int) groupSizeSpinner.getValue());
            }
            for (GraphPanel panel : bottomGraphs)
            {
                panel.updateGroupSize((int) groupSizeSpinner.getValue());
            }
            spinnerOffsetModel.setMaximum((int) groupSizeSpinner.getValue() - 1);
        });

        groupOffsetSpinner.addChangeListener(e ->
        {
            for (GraphPanel panel : topGraphs)
            {
                panel.updateGroupOffset((int) groupOffsetSpinner.getValue());
            }
            for (GraphPanel panel : bottomGraphs)
            {
                panel.updateGroupOffset((int) groupOffsetSpinner.getValue());
            }
            spinnerSizeModel.setMinimum((int) groupOffsetSpinner.getValue() + 1);
        });


        labels = names;

        scrollTopPanel = getThemedPanel();
        scrollBottomPanel = getThemedPanel();

        scrollBottomGraphData = getThemedScrollPane(scrollBottomPanel);
        scrollTopGraphData = getThemedScrollPane(scrollTopPanel);

        graph1Average = getThemedLabel("", SwingConstants.RIGHT);
        graph1Median = getThemedLabel("", SwingConstants.RIGHT);
        graph1Mode = getThemedLabel("", SwingConstants.RIGHT);
        graph1Maximum = getThemedLabel("", SwingConstants.RIGHT);
        graph1Minimum = getThemedLabel("", SwingConstants.RIGHT);
        graph1PercentThreshold = getThemedLabel("", SwingConstants.RIGHT);

        graph2Average = getThemedLabel("", SwingConstants.RIGHT);
        graph2Median = getThemedLabel("", SwingConstants.RIGHT);
        graph2Mode = getThemedLabel("", SwingConstants.RIGHT);
        graph2Maximum = getThemedLabel("", SwingConstants.RIGHT);
        graph2Minimum = getThemedLabel("", SwingConstants.RIGHT);
        graph2PercentThreshold = getThemedLabel("", SwingConstants.RIGHT);

        topGraphTabs = getThemedTabbedPane();
        topGraphTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        topGraphTabs.addChangeListener(cl ->
        {
            switchGraphData();
            updateOtherPanels();
        });
        bottomGraphTabs = getThemedTabbedPane();
        bottomGraphTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        bottomGraphTabs.addChangeListener(cl ->
        {
            switchGraphData();
            updateOtherPanels();
        });

        Map<String, String[]> comboPopupData = new LinkedHashMap<>();

        for(RaidRoom room : RaidRoom.values())
        {
            comboPopupData.put(room.name, DataPoint.getSpecificNames(room));
        }
        compareByComboBox = getThemedComboBox();
        compareByComboBox.setPreferredSize(new Dimension(300, compareByComboBox.getPreferredSize().height));
        compareByComboBox.setEditable(true);
        compareByComboBox.setPrototypeDisplayValue("Maiden Time");
        compareByComboBox.setSelectedItem("Maiden Time");
        compareByComboBox.setEditable(false);

        compareByComboBox.setEnabled(true);
        for(Component c : compareByComboBox.getComponents())
        {
            //we are using the combobox for its appearance only, we do not want the real combobox dropdown to pop up when clicked because we are using
            //a jpopupmenu to display the content of the dropdown. Even if we set it not visible it will flash for a frame before going away.
            if(c instanceof AbstractButton)
            {
                c.setEnabled(false);
            }
        }


        ArrayList<String> allComboValues = new ArrayList<>(comboPopupData.keySet());
        comboPopupMenu = getThemedPopupMenu();
        comboStrictData = new ArrayList<>(comboPopupData.keySet());

        DataPointMenu menu = new DataPointMenu(allComboValues, comboPopupData, comboFlatData, comboPopupMenu, compareByComboBox, this);
        compareByComboBox.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                comboPopupMenu.show(compareByComboBox, e.getX(), e.getY());
            }
        });

        matchYScales.addActionListener(al ->
                switchGraphData());

        matchXScales.addActionListener(al ->
        {
            switchGraphData();
            leftCutOff.setEnabled(matchXScales.isSelected());
            rightCutOff.setEnabled(matchXScales.isSelected());
        });

        container = getThemedPanel();
        container.setPreferredSize(new Dimension(1000, 730));
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        buildUI();
        add(container);
    }

    private int valX = 0;

    private JPopupMenu comboPopupMenu;
    private ArrayList<String> comboStrictData;
    private AbstractButton arrowButton;

    private void setComboSelection(String name)
    {
        Vector<String> items = new Vector<>();

        addComboItems(name, items, comboStrictData, compareByComboBox);
    }

    public static void addComboItems(String name, Vector<String> items, List<String> comboStrictData, JComboBox<String> compareByComboBox)
    {
        for (String item : comboStrictData)
        {
            if (item.endsWith(name))
            {
                items.add(item);
                break;
            }
        }

        compareByComboBox.setModel(new DefaultComboBoxModel<>(items));

        if (items.size() == 1)
        {
            compareByComboBox.setSelectedIndex(0);
        }
    }

    private void switchGraphData()
    {
        if (topGraphs.size() != bottomGraphs.size())
        {
            return;
        }
        int xHigh = 0;
        int xLow = Integer.MAX_VALUE;
        int yHigh = 0;
        if (!Objects.requireNonNull(compareByComboBox.getSelectedItem()).toString().contains("Player:"))
        {
            time = Objects.requireNonNull(DataPoint.getValue(Objects.requireNonNull(compareByComboBox.getSelectedItem()).toString())).type == DataPoint.types.TIME;
        } else
        {
            time = false;
        }

        for (int i = 0; i < topGraphs.size(); i++)
        {
            topGraphs.get(i).switchKey(Objects.requireNonNull(DataPoint.getValue(Objects.requireNonNull(compareByComboBox.getSelectedItem()).toString())));
            bottomGraphs.get(i).switchKey(Objects.requireNonNull(DataPoint.getValue(Objects.requireNonNull(compareByComboBox.getSelectedItem()).toString())));

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

        threshold.setMaximum(xHigh);
        threshold.setMinimum(xLow);
        threshold.setValue(((xHigh - xLow) / 2) + xLow);
        setThresholdTimes();

        leftCutOff.setValue(xLow);
        rightCutOff.setValue(xLow);

        updateSliders();
        container.repaint();
        valX = xHigh;
        if (matchYScales.isSelected())
        {
            setYScales(yHigh);
        }
        if (matchXScales.isSelected())
        {
            setXScales(xLow, xHigh);
        }

        redrawGraphs();

    }

    private void setThresholdTimes()
    {
        thresholdLabel.setText("Threshold: " + ((time) ? RoomUtil.time(threshold.getValue()) : threshold.getValue()));
        leftThresholdLabel.setText("% <= " + ((time) ? RoomUtil.time(threshold.getValue()) : threshold.getValue()));
        rightThresholdLabel.setText("% <= " + ((time) ? RoomUtil.time(threshold.getValue()) : threshold.getValue()));
    }

    private void setXScales(int xLow, int xHigh)
    {
        for (int i = 0; i < topGraphs.size(); i++)
        {
            topGraphs.get(i).setScales(xLow, xHigh, topGraphs.get(i).getScaleYHigh());
            bottomGraphs.get(i).setScales(xLow, xHigh, bottomGraphs.get(i).getScaleYHigh());
        }
    }

    private void setYScales(int yHigh)
    {
        for (int i = 0; i < topGraphs.size(); i++)
        {
            topGraphs.get(i).setScales(topGraphs.get(i).getScaleXLow(), topGraphs.get(i).getScaleXHigh(), yHigh);
            bottomGraphs.get(i).setScales(bottomGraphs.get(i).getScaleXLow(), bottomGraphs.get(i).getScaleXHigh(), yHigh);
        }
    }

    private void redrawGraphs(int xLow, int xHigh)
    {
        for (int i = 0; i < topGraphs.size(); i++)
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
        for (int i = 0; i < topGraphs.size(); i++)
        {
            topGraphs.get(i).setBounds();
            topGraphs.get(i).drawGraph();
            bottomGraphs.get(i).setBounds();
            bottomGraphs.get(i).drawGraph();
        }
    }

    private void updateCutoffs()
    {
        if (matchXScales.isSelected())
        {
            redrawGraphs(leftCutOff.getValue(), rightCutOff.getMaximum() - rightCutOff.getValue() + rightCutOff.getMinimum());

        }
    }

    private void updateSliders()
    {
        int leftExtent = rightCutOff.getValue() - rightCutOff.getMinimum();
        int rightExtent = leftCutOff.getValue() - leftCutOff.getMinimum();
        rightCutOff.setExtent(rightExtent);
        leftCutOff.setExtent(leftExtent);

        //For some unbelievably bizarre reasons extents can only be set for the upper bound so we have to inverse it

        leftLabel.setText("Min cutoff: " + ((time) ? RoomUtil.time(leftCutOff.getValue()) : leftCutOff.getValue()));
        rightLabel.setText("Max cutoff: " + ((time) ? RoomUtil.time(rightCutOff.getMaximum() - rightCutOff.getValue() + rightCutOff.getMinimum()) : rightCutOff.getMaximum() - rightCutOff.getValue() + rightCutOff.getMinimum()));
        updateCutoffs();
    }

    GraphPanel getGraphPanel(List<Raid> points)
    {
        return new GraphPanel(points, config, itemManager, clientThread, configManager);
    }

    private List<Integer> getArrayForStatistics(List<Raid> data)
    {
        List<Integer> arrayToPass = new ArrayList<>();

        for (Raid raidData : data)
        {
            int value = raidData.get(DataPoint.getValue(String.valueOf(compareByComboBox.getSelectedItem())));
            if (value > -1)
            {
                if (!time || value != 0)
                {
                        switch ((Objects.requireNonNull(DataPoint.getValue(String.valueOf(compareByComboBox.getSelectedItem())))).room) //todo ???
                        {
                            case MAIDEN:
                                if (!raidData.getRoomAccurate(MAIDEN))
                                {
                                    continue;
                                }
                                break;
                            case BLOAT:
                                if (!raidData.getRoomAccurate(BLOAT))
                                {
                                    continue;
                                }
                                break;
                            case NYLOCAS:
                                if (!raidData.getRoomAccurate(NYLOCAS))
                                {
                                    continue;
                                }
                                break;
                            case SOTETSEG:
                                if (!raidData.getRoomAccurate(SOTETSEG))
                                {
                                    continue;
                                }
                                break;
                            case XARPUS:
                                if (!raidData.getRoomAccurate(XARPUS))
                                {
                                    continue;
                                }
                                break;
                            case VERZIK:
                                if (!raidData.getRoomAccurate(VERZIK))
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
        return (time) ? RoomUtil.time(val) : String.valueOf(Math.round(val * 100.0) / 100.0);
    }

    private void updateOtherPanels()
    {
        panelName = "Other - " + compareByComboBox.getSelectedItem();
        TitledBorder border = BorderFactory.createTitledBorder(panelName);
        border.setTitleColor(config.fontColor());
        otherPanel.setBorder(border);
        if (topGraphTabs.getSelectedIndex() != -1 && bottomGraphTabs.getSelectedIndex() != -1 && built)
        {
            otherTopLeft.setBorder(getColoredTitledBorder(topGraphTabs.getTitleAt(topGraphTabs.getSelectedIndex()), config.fontColor()));
            otherTopRight.setBorder(getColoredTitledBorder(bottomGraphTabs.getTitleAt(bottomGraphTabs.getSelectedIndex()), config.fontColor()));

            otherBottomLeft.setBorder(getColoredTitledBorder(topGraphTabs.getTitleAt(topGraphTabs.getSelectedIndex()) + " values", config.fontColor()));
            otherBottomRight.setBorder(getColoredTitledBorder(bottomGraphTabs.getTitleAt(bottomGraphTabs.getSelectedIndex()) + " values", config.fontColor()));

            List<Raid> topGraphData = (data.get(topGraphTabs.getSelectedIndex()));
            List<Raid> bottomGraphData = data.get(bottomGraphTabs.getSelectedIndex());

            String worse = "<html><font color='#F63131'>";
            String better = "<html><font color='#99E622'>";
            String even = "<html><font color='#CCCCF6'>";

            double g1a = StatisticGatherer.getGenericAverage(getArrayForStatistics(topGraphData));
            double g1med = StatisticGatherer.getGenericMedian(getArrayForStatistics(topGraphData));
            double g1mod = StatisticGatherer.getGenericMode(getArrayForStatistics(topGraphData));
            double g1max = StatisticGatherer.getGenericMax(getArrayForStatistics(topGraphData));
            double g1min = StatisticGatherer.getGenericMin(getArrayForStatistics(topGraphData));
            double g1percent = StatisticGatherer.getGenericPercent(getArrayForStatistics(topGraphData), threshold.getValue());

            double g2a = StatisticGatherer.getGenericAverage(getArrayForStatistics(bottomGraphData));
            double g2med = StatisticGatherer.getGenericMedian(getArrayForStatistics(bottomGraphData));
            double g2mod = StatisticGatherer.getGenericMode(getArrayForStatistics(bottomGraphData));
            double g2max = StatisticGatherer.getGenericMax(getArrayForStatistics(bottomGraphData));
            double g2min = StatisticGatherer.getGenericMin(getArrayForStatistics(bottomGraphData));
            double g2percent = StatisticGatherer.getGenericPercent(getArrayForStatistics(bottomGraphData), threshold.getValue());

            String g1as = (g1a < g2a) ? better : g2a == g1a ? even : worse;
            String g1meds = (g1med < g2med) ? better : g2med == g1med ? even : worse;
            String g1mods = (g1mod < g2mod) ? better : g2mod == g1mod ? even : worse;
            String g1maxs = (g1max < g2max) ? better : g2max == g1max ? even : worse;
            String g1mins = (g1min < g2min) ? better : g2min == g1min ? even : worse;
            String g1perc = (g1percent > g2percent) ? better : g1percent == g2percent ? even : worse;

            String g2as = (g1a > g2a) ? better : g2a == g1a ? even : worse;
            String g2meds = (g1med > g2med) ? better : g2med == g1med ? even : worse;
            String g2mods = (g1mod > g2mod) ? better : g2mod == g1mod ? even : worse;
            String g2maxs = (g1max > g2max) ? better : g2max == g1max ? even : worse;
            String g2mins = (g1min > g2min) ? better : g2min == g1min ? even : worse;
            String g2perc = (g2percent > g1percent) ? better : g1percent == g2percent ? even : worse;


            graph1Average.setText(g1as + getString(g1a));
            graph1Median.setText(g1meds + getString(g1med));
            graph1Mode.setText(g1mods + getString(g1mod));
            graph1Maximum.setText(g1maxs + getString(g1max));
            graph1Minimum.setText(g1mins + getString(g1min));
            graph1PercentThreshold.setText(g1perc + (g1percent) + "%");

            graph2Average.setText(g2as + getString(g2a));
            graph2Median.setText(g2meds + getString(g2med));
            graph2Mode.setText(g2mods + getString(g2mod));
            graph2Maximum.setText(g2maxs + getString(g2max));
            graph2Minimum.setText(g2mins + getString(g2min));
            graph2PercentThreshold.setText(g2perc + (g2percent) + "%");


            List<Integer> topSet = GraphPanel.getCounts(getArrayForStatistics(topGraphData), valX);
            List<Integer> bottomSet = GraphPanel.getCounts(getArrayForStatistics(bottomGraphData), valX);

            scrollTopPanel.removeAll();
            scrollBottomPanel.removeAll();

            scrollTopPanel.add(getThemedLabel("Value", SwingConstants.LEFT));
            scrollTopPanel.add(getThemedLabel("Count", SwingConstants.RIGHT));

            int total = GraphPanel.getCountedTotal(topSet);
            int count = 0;
            count = getCount(topSet, total, count, scrollTopPanel);

            int altCount = 0;
            for (int i = count; i < 16; i++)
            {
                scrollTopPanel.add(getThemedLabel());
                scrollTopPanel.add(getThemedLabel());
                altCount++;
            }
            scrollTopPanel.setLayout(new GridLayout(count + 1 + altCount, 2));
            count = 0;

            scrollBottomPanel.add(getThemedLabel("Value", SwingConstants.LEFT));
            scrollBottomPanel.add(getThemedLabel("Count", SwingConstants.RIGHT));

            total = GraphPanel.getCountedTotal(bottomSet);

            count = getCount(bottomSet, total, count, scrollBottomPanel);
            altCount = 0;
            for (int i = count; i < 16; i++)
            {
                scrollBottomPanel.add(getThemedLabel());
                scrollBottomPanel.add(getThemedLabel());
                altCount++;
            }
            scrollBottomPanel.setLayout(new GridLayout(count + 1 + altCount, 2));

            scrollTopPanel.validate();
            scrollBottomPanel.validate();
        }
        container.repaint();
    }

    private int getCount(List<Integer> topSet, double total, int count, JPanel scrollTopPanel)
    {
        for (int i = 0; i < topSet.size(); i++)
        {
            if (topSet.get(i) > 0)
            {
                String percent = Math.round((100.0 * topSet.get(i) / total) * 100.0) / 100.0 + "%";
                scrollTopPanel.add(getThemedLabel(getString(i), SwingConstants.LEFT));
                scrollTopPanel.add(getThemedLabel(topSet.get(i) + " (" + percent + ")", SwingConstants.RIGHT));
                count++;
            }
        }
        return count;
    }

    private boolean built = false;

    private void buildUI()
    {
        for (int i = 0; i < data.size(); i++)
        {
            GraphPanel topGraph = getGraphPanel(data.get(i));
            GraphPanel bottomGraph = getGraphPanel(data.get(i));
            topGraphTabs.addTab(labels.get(i), topGraph);
            bottomGraphTabs.addTab(labels.get(i), bottomGraph);
            topGraphs.add(topGraph);
            bottomGraphs.add(bottomGraph);
        }
        switchGraphData();

        JPanel leftContainer = getThemedPanel();
        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.Y_AXIS));

        leftContainer.add(topGraphTabs);
        leftContainer.add(bottomGraphTabs);

        container.add(leftContainer);

        JPanel sidebar = getThemedPanel();
        sidebar.setPreferredSize(new Dimension(390, 730));

        JPanel graphOptionsPanel = getThemedPanel();
        graphOptionsPanel.setBorder(BorderFactory.createTitledBorder("Graph Options"));
        graphOptionsPanel.setPreferredSize(new Dimension(210, 215));

        threshold.setPaintLabels(true);
        threshold.setPaintTicks(true);
        threshold.setPaintTrack(true);
        threshold.addChangeListener(cl ->
        {
            Object source = cl.getSource();
            if (source instanceof JSlider)
            {
                if (((JSlider) source).getValueIsAdjusting())
                {
                    setThresholdTimes();
                    updateOtherPanels();
                }
            }
        });
        threshold.setPreferredSize(new Dimension(180, threshold.getPreferredSize().height));
        leftCutOff.setPaintLabels(true);
        leftCutOff.setPaintTicks(true);
        leftCutOff.setPaintTrack(true);

        leftCutOff.addChangeListener(cl ->
        {
            Object source = cl.getSource();
            if (source instanceof JSlider)
            {
                if (((JSlider) source).getValueIsAdjusting())
                {
                    updateSliders();
                }
            }
        });

        rightCutOff.addChangeListener(cl ->
        {
            Object source = cl.getSource();
            if (source instanceof JSlider)
            {
                if (((JSlider) source).getValueIsAdjusting())
                {
                    updateSliders();
                }
            }
        });

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

        graphOptionsPanel.add(getThemedLabel(""));

        graphOptionsPanel.add(groupingEnabled);
        graphOptionsPanel.add(getThemedLabel("Group Size: "));
        graphOptionsPanel.add(groupSizeSpinner);
        graphOptionsPanel.add(getThemedLabel("Group Offset: "));
        graphOptionsPanel.add(groupOffsetSpinner);

        graphOptionsPanel.add(getThemedCheckBox("Show Chronological"));
        graphOptionsPanel.add(getThemedCheckBox("Rotate 90"));

        graphOptionsPanel.add(graphTypeComboBox);

        graphOptionsPanel.add(threshold);
        thresholdLabel.setPreferredSize(new Dimension(140, thresholdLabel.getPreferredSize().height));
        graphOptionsPanel.add(thresholdLabel);

        JPanel compareByPanel = getThemedPanel();
        compareByPanel.setBorder(BorderFactory.createTitledBorder("Compare by"));
        compareByPanel.setPreferredSize(new Dimension(190, 60));
        compareByPanel.add(compareByComboBox);


        otherPanel.setBorder(BorderFactory.createTitledBorder(panelName));
        otherPanel.setPreferredSize(new Dimension(190, 455));
        otherPanel.setLayout(new BoxLayout(otherPanel, BoxLayout.Y_AXIS));


        JPanel otherTop = getThemedPanel();
        otherTop.setLayout(new BoxLayout(otherTop, BoxLayout.X_AXIS));

        JPanel otherBottom = getThemedPanel();
        otherBottom.setLayout(new BoxLayout(otherBottom, BoxLayout.X_AXIS));

        otherTopLeft = getThemedPanel();
        otherTopLeft.setBorder(BorderFactory.createTitledBorder("Set 0"));
        otherTopLeft.setPreferredSize(new Dimension(100, 125));

        otherTopRight = getThemedPanel();
        otherTopRight.setBorder(BorderFactory.createTitledBorder("Set 1"));
        otherTopRight.setPreferredSize(new Dimension(100, 125));

        otherTop.add(otherTopLeft);
        otherTop.add(otherTopRight);

        otherBottomLeft = getThemedPanel();
        otherBottomLeft.setBorder(getColoredTitledBorder("Values", config.fontColor()));

        otherTopLeft.setLayout(new GridLayout(6, 2));
        otherTopRight.setLayout(new GridLayout(6, 2));


        otherTopLeft.add(getThemedLabel("Average ", SwingConstants.LEFT));
        otherTopLeft.add(graph1Average);
        otherTopLeft.add(getThemedLabel("Median ", SwingConstants.LEFT));
        otherTopLeft.add(graph1Median);
        otherTopLeft.add(getThemedLabel("Mode ", SwingConstants.LEFT));
        otherTopLeft.add(graph1Mode);
        otherTopLeft.add(getThemedLabel("Maximum ", SwingConstants.LEFT));
        otherTopLeft.add(graph1Maximum);
        otherTopLeft.add(getThemedLabel("Minimum ", SwingConstants.LEFT));
        otherTopLeft.add(graph1Minimum);
        otherTopLeft.add(leftThresholdLabel);
        otherTopLeft.add(graph1PercentThreshold);

        otherTopRight.add(getThemedLabel("Average ", SwingConstants.LEFT));
        otherTopRight.add(graph2Average);
        otherTopRight.add(getThemedLabel("Median ", SwingConstants.LEFT));
        otherTopRight.add(graph2Median);
        otherTopRight.add(getThemedLabel("Mode ", SwingConstants.LEFT));
        otherTopRight.add(graph2Mode);
        otherTopRight.add(getThemedLabel("Maximum ", SwingConstants.LEFT));
        otherTopRight.add(graph2Maximum);
        otherTopRight.add(getThemedLabel("Minimum ", SwingConstants.LEFT));
        otherTopRight.add(graph2Minimum);
        otherTopRight.add(rightThresholdLabel);
        otherTopRight.add(graph2PercentThreshold);

        otherBottomRight = getThemedPanel();
        otherBottomRight.setBorder(getColoredTitledBorder("Values", config.fontColor()));

        scrollTopGraphData.setPreferredSize(new Dimension(150, 250));
        scrollBottomGraphData.setPreferredSize(new Dimension(150, 250));

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

    @Override
    public void update()
    {
        switchGraphData();
        updateOtherPanels();
    }

    @Override
    public void setComboBox(String item)
    {
        compareByComboBox.addItem(item);
        compareByComboBox.setSelectedItem(item);
    }
}
