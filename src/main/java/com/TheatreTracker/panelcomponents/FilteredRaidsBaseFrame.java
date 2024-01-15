package com.TheatreTracker.panelcomponents;


import com.TheatreTracker.RoomData;
import com.TheatreTracker.filters.*;
import com.TheatreTracker.utility.DataPoint;
import lombok.extern.slf4j.Slf4j;
import com.TheatreTracker.utility.RoomUtil;
import com.TheatreTracker.utility.StatisticGatherer;
import net.runelite.client.plugins.raids.solver.Room;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

@Slf4j
public class FilteredRaidsBaseFrame extends BaseFrame
{
    private final ArrayList<Integer> filteredIndices;
    private JTable comparisonTable;
    private final ArrayList<ArrayList<RoomData>> comparisons;

    private final JTabbedPane tabbedPane = new JTabbedPane();
    public ArrayList<ImplicitFilter> activeFilters;
    private final JLabel raidsFoundLabel = new JLabel("", SwingConstants.LEFT);
    private final JLabel completionsFound = new JLabel("", SwingConstants.LEFT);
    private JComboBox<String> viewByRaidComboBox;

    private JComboBox<String> sortOrderBox;
    private JComboBox<String> sortOptionsBox;
    private final JLabel overallPanelMaidenAverage = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelBloatAverage = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelNyloAverage = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelSoteAverage = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelXarpusAverage = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelVerzikAverage = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelOverallAverage = new JLabel("", SwingConstants.RIGHT);

    private final JLabel overallPanelMaidenMedian = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelBloatMedian = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelNyloMedian = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelSoteMedian = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelXarpusMedian = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelVerzikMedian = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelOverallMedian = new JLabel("", SwingConstants.RIGHT);

    private final JLabel overallPanelMaidenMinimum = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelBloatMinimum = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelNyloMinimum = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelSoteMinimum = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelXarpusMinimum = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelVerzikMinimum = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelOverallMinimum = new JLabel("", SwingConstants.RIGHT);

    private final JLabel overallPanelMaidenMaximum = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelBloatMaximum = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelNyloMaximum = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelSoteMaximum = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelXarpusMaximum = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelVerzikMaximum = new JLabel("", SwingConstants.RIGHT);
    private final JLabel overallPanelOverallMaximum = new JLabel("", SwingConstants.RIGHT);

    private final JLabel resultsAverage = new JLabel("", SwingConstants.RIGHT);
    private JLabel resultsMedian = new JLabel("", SwingConstants.RIGHT);
    private JLabel resultsMode = new JLabel("", SwingConstants.RIGHT);
    private JLabel resultsMinimum = new JLabel("", SwingConstants.RIGHT);
    private JLabel resultsMaximum = new JLabel("", SwingConstants.RIGHT);
    public JComboBox statisticsBox;
    public JLabel customAverageLabel = new JLabel("", SwingConstants.RIGHT);
    public JLabel customMedianLabel = new JLabel("", SwingConstants.RIGHT);
    public JLabel customModeLabel = new JLabel("", SwingConstants.RIGHT);
    public JLabel customMinLabel = new JLabel("", SwingConstants.RIGHT);
    public JLabel customMaxLabel = new JLabel("", SwingConstants.RIGHT);

    JTextField dateTextField;
    JCheckBox filterSpectateOnly;
    JCheckBox filterInRaidOnly;
    JCheckBox filterCompletionOnly;
    JCheckBox filterWipeResetOnly;
    JComboBox filterComboBoxScale;
    JCheckBox filterCheckBoxScale;
    JCheckBox filterTodayOnly;
    JCheckBox filterPartyOnly;
    JCheckBox filterPartialData;
    JCheckBox filterPartialOnly;
    JCheckBox filterNormalOnly;
    JTable table;
    JPanel container;
    private JPanel filterTableContainer;
    public ArrayList<RoomData> currentData;
    private JComboBox<String> timeFilterChoice;
    private JComboBox<String> timeFilterOperator;
    private JTextField timeFilterValue;
    private JTable filterTable;
    private JComboBox<String> playerFilterOperator;
    private JTextField playerFilterValue;
    private JCheckBox timeFollowsTab;
    private StatisticTab maidenTab;
    private StatisticTab bloatTab;
    private StatisticTab nyloTab;
    private StatisticTab soteTab;
    private StatisticTab xarpTab;
    private StatisticTab verzikTab;
    private boolean built = false;
    private JComboBox<String> dateFilterOperator;
    private JTextField dateFilterValue;
    private JComboBox<String> otherIntFilterChoice;
    private JComboBox<String> otherIntFilterOperator;
    private JTextField otherIntFilterValue;
    private JComboBox<String> otherBoolFilterChoice;
    private JComboBox<String> otherBoolFilterOperator;
    String colorStr(Color c)
    {
        return "<html><font color='#" + Integer.toHexString(c.getRGB()).substring(2) + "'>";
    }

    public FilteredRaidsBaseFrame()
    {
        filteredIndices = new ArrayList<>();
        comparisons = new ArrayList<>();
        activeFilters = new ArrayList<>();
        this.setPreferredSize(new Dimension(1200,820));
    }

    public void updateCustomStats(ArrayList<RoomData> raids)
    {
        DataPoint dataPoint = DataPoint.values()[statisticsBox.getSelectedIndex()];
        boolean time = dataPoint.type == DataPoint.types.TIME;

        double avg = StatisticGatherer.getGenericAverage(raids, dataPoint);
        double med = StatisticGatherer.getGenericMedian(raids,dataPoint);
        double mod = StatisticGatherer.getGenericMode(raids,dataPoint);
        double min = StatisticGatherer.getGenericMin(raids,dataPoint);
        double max = StatisticGatherer.getGenericMax(raids,dataPoint);

        String avgStr = (time) ? RoomUtil.time(avg) : avg + "";
        String medStr = (time) ? RoomUtil.time(med) : med + "";
        String modStr = (time) ? RoomUtil.time(mod) : mod + "";
        String minStr = (time) ? RoomUtil.time(min) : min + "";
        String maxStr = (time) ? RoomUtil.time(max) : max + "";

        if(avg == -1) avgStr = "-";
        if(med == -1) medStr = "-";
        if(mod == -1) modStr = "-";
        if(min == -1) minStr = "-";
        if(max == -1) maxStr = "-";

        customAverageLabel.setText(avgStr);
        customMedianLabel.setText(medStr);
        customModeLabel.setText(modStr);
        customMinLabel.setText(minStr);
        customMaxLabel.setText(maxStr);
    }

    private boolean evaluateAllFilters(RoomData data)
    {
        for(ImplicitFilter filter : activeFilters)
        {
            if(!filter.evaluate(data))
            {
                return false;
            }
        }
        return true;
    }

    public void updateTable()
    {
        int timeToDisplay = 0;
        int completions = 0;
        ArrayList<RoomData> tableData = new ArrayList<>();
        for(RoomData data : currentData)
        {
            boolean shouldDataBeIncluded = true;
            if(filterSpectateOnly.isSelected())
            {
                if(!data.spectated)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(filterInRaidOnly.isSelected())
            {
                if(data.spectated)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(filterCompletionOnly.isSelected())
            {
                if(!data.raidCompleted || !data.getOverallTimeAccurate())
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(filterWipeResetOnly.isSelected())
            {
                if(data.raidCompleted)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(filterPartialData.isSelected())
            {
                if(!(data.maidenStartAccurate == data.maidenEndAccurate &&
                        data.bloatStartAccurate == data.bloatEndAccurate &&
                        data.nyloStartAccurate == data.nyloEndAccurate &&
                        data.soteStartAccurate == data.soteEndAccurate &&
                        data.xarpStartAccurate == data.xarpEndAccurate &&
                        data.verzikStartAccurate == data.verzikEndAccurate))
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(shouldDataBeIncluded && filterTodayOnly.isSelected())
            {
                shouldDataBeIncluded = false;
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(data.raidStarted);
                cal2.setTime(new Date(System.currentTimeMillis()));
                if(cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH))
                {
                    shouldDataBeIncluded = true;
                }
            }
            if(filterPartyOnly.isSelected())
            {
                if(!data.maidenDefenseAccurate || !data.bloatDefenseAccurate || !data.nyloDefenseAccurate || !data.soteDefenseAccurate || !data.xarpDefenseAccurate)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(filterNormalOnly.isSelected())
            {
                if(data.storyMode || data.hardMode)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(filterPartialOnly.isSelected())
            {
                switch(viewByRaidComboBox.getSelectedItem().toString())
                {
                    case "Overall Time":
                        if(!data.getOverallTimeAccurate())
                        {
                            shouldDataBeIncluded = false;
                        }
                        break;
                    case "Maiden Time":
                        if(!data.maidenStartAccurate || !data.maidenEndAccurate)
                        {
                            shouldDataBeIncluded = false;
                        }
                        break;
                    case "Bloat Time":
                        if(!data.bloatStartAccurate || !data.bloatEndAccurate)
                        {
                            shouldDataBeIncluded = false;
                        }
                        break;
                    case "Nylo Time":
                        if(!data.nyloStartAccurate || !data.nyloEndAccurate)
                        {
                            shouldDataBeIncluded = false;
                        }
                        break;
                    case "Sote Time":
                        if(!data.soteStartAccurate || !data.soteEndAccurate)
                        {
                            shouldDataBeIncluded = false;
                        }
                        break;
                    case "Xarp Time":
                        if(!data.xarpStartAccurate || !data.xarpEndAccurate)
                        {
                            shouldDataBeIncluded = false;
                        }
                        break;
                    case "Verzik Time":
                        if(!data.verzikStartAccurate || !data.verzikEndAccurate)
                        {
                            shouldDataBeIncluded = false;
                        }
                        break;
                }
            }
            if(shouldDataBeIncluded && filterCheckBoxScale.isSelected())
            {
                shouldDataBeIncluded = filterComboBoxScale.getSelectedIndex()+1 == data.raidTeamSize;
            }
            timeToDisplay = data.getSpecificTimeInactive(viewByRaidComboBox.getSelectedItem().toString());
            if(timeToDisplay == 0)
            {
                //shouldDataBeIncluded = false;
            }
            for(Integer i : filteredIndices)
            {
                if(data.index == i)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(!evaluateAllFilters(data))
            {
                shouldDataBeIncluded = false;
            }
            if(shouldDataBeIncluded)
            {
                tableData.add(data);
                if(data.raidCompleted && data.getOverallTimeAccurate())
                {
                    completions++;
                }
            }
        }
        if(sortOptionsBox.getSelectedIndex() == 0)
        {
            if(sortOrderBox.getSelectedIndex() == 0)
            {
                tableData.sort(Comparator.comparing(RoomData::getDate));
            }
            else
            {
                tableData.sort(Comparator.comparing(RoomData::getDate).reversed());
            }
        }
        else if(sortOptionsBox.getSelectedIndex() == 1)
        {
            if(sortOrderBox.getSelectedIndex() == 0)
            {
                for (RoomData data : tableData) {
                    data.activeValue = viewByRaidComboBox.getSelectedItem().toString();
                }
                tableData.sort(Comparator.comparing(RoomData::getSpecificTime));
            }
            else
            {
                for (RoomData data : tableData) {
                    data.activeValue = viewByRaidComboBox.getSelectedItem().toString();
                }
                tableData.sort(Comparator.comparing(RoomData::getSpecificTime).reversed());
            }
        }
        else if(sortOptionsBox.getSelectedIndex() == 2)
        {
            if(sortOrderBox.getSelectedIndex() == 0)
            {
                tableData.sort(Comparator.comparing(RoomData::getScale));
            }
            else
            {
                tableData.sort(Comparator.comparing(RoomData::getScale).reversed());
            }
        }

        updateCustomStats(tableData);
        raidsFoundLabel.setText("Raids Found: " + tableData.size());
        completionsFound.setText("Completions Found: " + completions);
        updateTabNames(tableData);

        String[] columnNames = { "", "Date", "Scale", "Status", viewByRaidComboBox.getSelectedItem().toString(), "Players", "Spectate", "View"};
        ArrayList<Object[]> tableBuilder = new ArrayList<>();
        for(RoomData raid : tableData)
        {
            String players = "";
            for(String s : raid.players.keySet())
            {
                players += s + ", ";
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(raid.raidStarted);
            String dateString = (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR);
            String scaleString = "";
            switch(raid.players.size())
            {
                case 1:
                    scaleString = "Solo";
                    break;
                case 2:
                    scaleString = "Duo";
                    break;
                case 3:
                    scaleString = "Trio";
                    break;
                case 4:
                    scaleString = "4 Man";
                    break;
                case 5:
                    scaleString = "5 Man";
                    break;
            }
            if(raid.storyMode)
            {
                scaleString += " (Story)";
            }
            if(raid.hardMode)
            {
                scaleString += " (Hard)";
            }
            timeToDisplay = raid.getSpecificTimeInactive(viewByRaidComboBox.getSelectedItem().toString());
            Object[] row =
                    {
                            raid.index,
                            dateString,
                            scaleString,
                            getRoomStatus(raid),
                            (isTime())? RoomUtil.time(timeToDisplay) : timeToDisplay,
                            (players.length() > 2) ? players.substring(0, players.length()-2) : "",
                            (raid.spectated) ? "Yes" : "No",
                            "View"
                    };
            tableBuilder.add(row);
        }
        Object[][] tableObject = new Object[tableData.size()][8];
        int count = 0;
        for(Object[] row : tableBuilder)
        {
            tableObject[count] = row;
            count++;
        }
        table.setModel(new DefaultTableModel(tableObject, columnNames));
        table.getColumn("Date").setCellEditor(new NonEditableCell(new JTextField()));
        table.getColumn("Scale").setCellEditor(new NonEditableCell(new JTextField()));
        table.getColumn("Status").setCellEditor(new NonEditableCell(new JTextField()));
        table.getColumn("").setCellEditor(new NonEditableCell(new JTextField()));
        table.getColumn(viewByRaidComboBox.getSelectedItem().toString()).setCellEditor(new NonEditableCell(new JTextField()));
        table.getColumn("Players").setCellEditor(new NonEditableCell(new JTextField()));
        table.getColumn("Spectate").setCellEditor(new NonEditableCell(new JTextField()));
        table.getColumn("View").setCellRenderer(new ButtonRenderer());
        table.getColumn("View").setCellEditor(new ButtonEditorRoomData(new JCheckBox(), tableData));
        resizeColumnWidth(table);
        table.setFillsViewportHeight(true);
        setLabels(tableData);
        container.validate();
        container.repaint();


    }

    boolean isTime()
    {
        return (Objects.requireNonNull(DataPoint.getValue(Objects.requireNonNull(viewByRaidComboBox.getSelectedItem()).toString())).type == DataPoint.types.TIME);
    }

    public String getRoomStatus(RoomData data)
    {
        String raidStatusString = "";
        if(data.maidenWipe)
        {
            raidStatusString = "Maiden Wipe";
        }
        else if(data.maidenReset)
        {
            raidStatusString = "Maiden Reset";
            if(!data.maidenSpawned)
            {
                raidStatusString += "*";
            }
        }
        else if(data.bloatWipe)
        {
            raidStatusString = "Bloat Wipe";
        }
        else if(data.bloatReset)
        {
            raidStatusString = "Bloat Reset";
            if(data.getBloatTime() == 0)
            {
                raidStatusString += "*";
            }
        }
        else if(data.nyloWipe)
        {
            raidStatusString = "Nylo Wipe";
        }
        else if(data.nyloReset)
        {
            raidStatusString = "Nylo Reset";
            if(data.getNyloTime() == 0)
            {
                raidStatusString += "*";
            }
        }
        else if(data.soteWipe)
        {
            raidStatusString = "Sotetseg Wipe";
        }
        else if(data.soteReset)
        {
            raidStatusString = "Sotetseg Reset";
            if(data.getSoteTime() == 0)
            {
                raidStatusString += "*";
            }
        }
        else if(data.xarpWipe)
        {
            raidStatusString = "Xarpus Wipe";
        }
        else if(data.xarpReset)
        {
            raidStatusString = "Xarpus Reset";
            if(data.getXarpTime() == 0)
            {
                raidStatusString += "*";
            }
        }
        else if(data.verzikWipe)
        {
            raidStatusString = "Verzik Wipe";
        }
        else
        {
            raidStatusString = "Completion";
            if(!data.getOverallTimeAccurate())
            {
                raidStatusString += "*";
            }
        }
        return raidStatusString;
    }

    private void updateTabNames(ArrayList<RoomData> data)
    {
        int maidenCount = 0;
        int bloatCount = 0;
        int nyloCount = 0;
        int soteCount = 0;
        int xarpCount = 0;
        int verzikCount = 0;
        for(RoomData d : data)
        {
            if(d.maidenStartAccurate && d.maidenEndAccurate)
            {
                maidenCount++;
            }
            if(d.bloatStartAccurate && d.bloatEndAccurate)
            {
                bloatCount++;
            }
            if(d.nyloStartAccurate && d.nyloEndAccurate)
            {
                nyloCount++;
            }
            if(d.soteStartAccurate && d.soteEndAccurate)
            {
                soteCount++;
            }
            if(d.xarpStartAccurate && d.xarpEndAccurate)
            {
                xarpCount++;
            }
            if(d.verzikStartAccurate && d.verzikEndAccurate)
            {
                verzikCount++;
            }
        }
        tabbedPane.setTitleAt(1, "Maiden (" + maidenCount + ")");
        tabbedPane.setTitleAt(2, "Bloat (" + bloatCount + ")");
        tabbedPane.setTitleAt(3, "Nylo (" + nyloCount + ")");
        tabbedPane.setTitleAt(4, "Sotetseg (" + soteCount + ")");
        tabbedPane.setTitleAt(5, "Xarpus (" + xarpCount + ")");
        tabbedPane.setTitleAt(6, "Verzik (" + verzikCount + ")");
    }

    public void resizeColumnWidthFilters(JTable table)
    {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++)
        {
            int width = 25; // Min width
            for (int row = 0; row < table.getRowCount(); row++)
            {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
            {
                width = 300;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
    public void resizeColumnWidth(JTable table)
    {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++)
        {
            int width = 50; // Min width
            for (int row = 0; row < table.getRowCount(); row++)
            {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 500)
            {
                width = 500;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    public void setLabels(ArrayList<RoomData> data)
    {
        setOverallLabels(data);
        maidenTab.updateTab(data);
        bloatTab.updateTab(data);
        nyloTab.updateTab(data);
        soteTab.updateTab(data);
        xarpTab.updateTab(data);
        verzikTab.updateTab(data);
    }
    public void setOverallLabels(ArrayList<RoomData> data)
    {
        setOverallAverageLabels(data);
        setOverallMedianLabels(data);
        setOverallMinLabels(data);
        setOverallMaxLabels(data);
    }

    public void setOverallAverageLabels(ArrayList<RoomData> data)
    {
        overallPanelMaidenAverage.setText(RoomUtil.time(StatisticGatherer.getGenericAverage(data, DataPoint.MAIDEN_TOTAL_TIME)));
        overallPanelBloatAverage.setText(RoomUtil.time(StatisticGatherer.getGenericAverage(data, DataPoint.BLOAT_TOTAL_TIME)));
        overallPanelNyloAverage.setText(RoomUtil.time(StatisticGatherer.getGenericAverage(data, DataPoint.NYLO_TOTAL_TIME)));
        overallPanelSoteAverage.setText(RoomUtil.time(StatisticGatherer.getGenericAverage(data, DataPoint.SOTE_TOTAL_TIME)));
        overallPanelXarpusAverage.setText(RoomUtil.time(StatisticGatherer.getGenericAverage(data, DataPoint.XARP_TOTAL_TIME)));
        overallPanelVerzikAverage.setText(RoomUtil.time(StatisticGatherer.getGenericAverage(data, DataPoint.VERZIK_TOTAL_TIME)));
        overallPanelOverallAverage.setText(RoomUtil.time(StatisticGatherer.getOverallTimeAverage(data)));
    }

    public void setOverallMedianLabels(ArrayList<RoomData> data)
    {
        overallPanelMaidenMedian.setText(RoomUtil.time(StatisticGatherer.getGenericMedian(data, DataPoint.MAIDEN_TOTAL_TIME)));
        overallPanelBloatMedian.setText(RoomUtil.time(StatisticGatherer.getGenericMedian(data, DataPoint.BLOAT_TOTAL_TIME)));
        overallPanelNyloMedian.setText(RoomUtil.time(StatisticGatherer.getGenericMedian(data, DataPoint.NYLO_TOTAL_TIME)));
        overallPanelSoteMedian.setText(RoomUtil.time(StatisticGatherer.getGenericMedian(data, DataPoint.SOTE_TOTAL_TIME)));
        overallPanelXarpusMedian.setText(RoomUtil.time(StatisticGatherer.getGenericMedian(data, DataPoint.XARP_TOTAL_TIME)));
        overallPanelVerzikMedian.setText(RoomUtil.time(StatisticGatherer.getGenericMedian(data, DataPoint.VERZIK_TOTAL_TIME)));
        overallPanelOverallMedian.setText(RoomUtil.time(StatisticGatherer.getOverallMedian(data)));
    }

    public void setOverallMinLabels(ArrayList<RoomData> data)
    {
        overallPanelMaidenMinimum.setText(RoomUtil.time(StatisticGatherer.getGenericMin(data, DataPoint.MAIDEN_TOTAL_TIME)));
        overallPanelBloatMinimum.setText(RoomUtil.time(StatisticGatherer.getGenericMin(data, DataPoint.BLOAT_TOTAL_TIME)));
        overallPanelNyloMinimum.setText(RoomUtil.time(StatisticGatherer.getGenericMin(data, DataPoint.NYLO_TOTAL_TIME)));
        overallPanelSoteMinimum.setText(RoomUtil.time(StatisticGatherer.getGenericMin(data, DataPoint.SOTE_TOTAL_TIME)));
        overallPanelXarpusMinimum.setText(RoomUtil.time(StatisticGatherer.getGenericMin(data, DataPoint.XARP_TOTAL_TIME)));
        overallPanelVerzikMinimum.setText(RoomUtil.time(StatisticGatherer.getGenericMin(data, DataPoint.VERZIK_TOTAL_TIME)));
        overallPanelOverallMinimum.setText(RoomUtil.time(StatisticGatherer.getOverallTimeMin(data)));
    }
    private void setOverallMaxLabels(ArrayList<RoomData> data)
    {
        overallPanelMaidenMaximum.setText(RoomUtil.time(StatisticGatherer.getGenericMax(data, DataPoint.MAIDEN_TOTAL_TIME)));
        overallPanelBloatMaximum.setText(RoomUtil.time(StatisticGatherer.getGenericMax(data, DataPoint.BLOAT_TOTAL_TIME)));
        overallPanelNyloMaximum.setText(RoomUtil.time(StatisticGatherer.getGenericMax(data, DataPoint.NYLO_TOTAL_TIME)));
        overallPanelSoteMaximum.setText(RoomUtil.time(StatisticGatherer.getGenericMax(data, DataPoint.SOTE_TOTAL_TIME)));
        overallPanelXarpusMaximum.setText(RoomUtil.time(StatisticGatherer.getGenericMax(data, DataPoint.XARP_TOTAL_TIME)));
        overallPanelVerzikMaximum.setText(RoomUtil.time(StatisticGatherer.getGenericMax(data, DataPoint.VERZIK_TOTAL_TIME)));
        overallPanelOverallMaximum.setText(RoomUtil.time(StatisticGatherer.getOverallMax(data)));
    }
    private Map<String, String[]> testMenuData;
    private JPopupMenu testPopupMenu;
    private List<String> testFlattenedData;
    private AbstractButton arrowButton;

    private void setPopupVisible(boolean visible)
    {
        if (visible)
        {
            testPopupMenu.show(viewByRaidComboBox, 0, viewByRaidComboBox.getSize().height);
        }
        else
        {
            testPopupMenu.setVisible(false);
        }
    }
    private void setComboSelection(String name)
    {
        Vector<String> items = new Vector<String>();

        for (String item : testFlattenedData)
        {
            if (item.endsWith(name))
            {
                items.add(item);
                break;
            }
        }

        viewByRaidComboBox.setModel(new DefaultComboBoxModel<String>(items));

        if (items.size() == 1)
        {
            viewByRaidComboBox.setSelectedIndex(0);
        }
    }

    private JMenuItem createMenuItem(final String name)
    {
        JMenuItem item = new JMenuItem(name);
        item.setBackground(Color.BLACK);
        item.setOpaque(true);


        item.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                setComboSelection(name);
            }
        });
        return item;
    }

    public void createFrame(ArrayList<RoomData> data)
    {
        testMenuData = new LinkedHashMap<String, String[]>();
        testMenuData.put("Room Times", DataPoint.getRoomTimes());
        testMenuData.put("Maiden", DataPoint.getMaidenNames());
        testMenuData.put("Bloat", DataPoint.getBloatNames());
        testMenuData.put("Nylocas", DataPoint.getNyloNames());
        testMenuData.put("Sotetseg", DataPoint.getSoteNames());
        testMenuData.put("Xarpus", DataPoint.getXarpNames());
        testMenuData.put("Verzik", DataPoint.getVerzikNames());
        testMenuData.put("Any", DataPoint.getAnyRoomNames());

        testPopupMenu = new JPopupMenu();
        testPopupMenu.setBorder(new MatteBorder(1, 1, 1, 1, Color.DARK_GRAY));

        List<String> testCategories = new ArrayList<String>(testMenuData.keySet());

        testFlattenedData = new ArrayList<String>();

        for(String category : testCategories)
        {
            JMenu menu = new JMenu(category);
            menu.setBackground(Color.BLACK);
            menu.setOpaque(true);
            if(!category.equals("Room Times") && !category.equals("Any"))
            {
                JMenu timeMenu = new JMenu("Time");
                timeMenu.setBackground(Color.BLACK);
                timeMenu.setOpaque(true);
                for (String itemName : DataPoint.filterTimes(testMenuData.get(category))) {
                    timeMenu.add(createMenuItem(itemName));
                    testFlattenedData.add(itemName);
                }
                JMenu countMenu = new JMenu("Misc");
                countMenu.setBackground(Color.BLACK);
                countMenu.setOpaque(true);
                for (String itemName : DataPoint.filterInt(testMenuData.get(category))) {
                    countMenu.add(createMenuItem(itemName));
                    testFlattenedData.add(itemName);
                }
                JMenu thrallMenu = new JMenu("Thrall");
                thrallMenu.setBackground(Color.BLACK);
                thrallMenu.setOpaque(true);
                for (String itemName : DataPoint.filterThrall(testMenuData.get(category))) {
                    thrallMenu.add(createMenuItem(itemName));
                    testFlattenedData.add(itemName);
                }
                JMenu vengMenu = new JMenu("Veng");
                vengMenu.setBackground(Color.BLACK);
                vengMenu.setOpaque(true);
                for (String itemName : DataPoint.filterVeng(testMenuData.get(category)))
                {
                    vengMenu.add(createMenuItem(itemName));
                    testFlattenedData.add(itemName);
                }

                JMenu specMenu = new JMenu("Spec");
                specMenu.setBackground(Color.BLACK);
                specMenu.setOpaque(true);
                for (String itemName : DataPoint.filterSpecs(testMenuData.get(category)))
                {
                    specMenu.add(createMenuItem(itemName));
                    testFlattenedData.add(itemName);
                }

                menu.add(timeMenu);
                menu.add(countMenu);
                menu.add(thrallMenu);
                menu.add(vengMenu);
                menu.add(specMenu);
            }
            else
            {
                for(String itemName : testMenuData.get(category))
                {
                    menu.add(createMenuItem(itemName));
                    testFlattenedData.add(itemName);
                }
            }

            testPopupMenu.add(menu);
        }

        viewByRaidComboBox = new JComboBox<>();
        viewByRaidComboBox.setEditable(true);
        viewByRaidComboBox.setPrototypeDisplayValue("Overall Time");
        viewByRaidComboBox.setSelectedItem("Overall Time");
        viewByRaidComboBox.setEditable(false);
        for(Component comp : viewByRaidComboBox.getComponents())
        {
            if(comp instanceof AbstractButton)
            {
                arrowButton = (AbstractButton) comp;
                arrowButton.setBackground(Color.BLACK);
            }
        }

        arrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setPopupVisible(!testPopupMenu.isVisible());
            }
        });

        viewByRaidComboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setPopupVisible(!testPopupMenu.isVisible());
            }
        });

        //FIX TIMER OPTIONS

        //viewByRaidComboBox = new JComboBox(new String[]{"Overall Time", "Maiden Time", "Bloat Time", "Nylocas Time", "Sotetseg Time", "Xarpus Time", "Verzik Time"});
        //viewByRaidComboBox = new JComboBox<>(DataPoint.getByNames());
        timeFollowsTab = new JCheckBox("Time Follows Tab");
        timeFollowsTab.setSelected(true);

        for(int i = 0; i < data.size(); i++)
        {
            data.get(i).index = i;
        }

        int completions = 0;
        currentData = data;
        setTitle("Raids");
        table = new JTable();
        JScrollPane pane = new JScrollPane(table);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Raids"));
        tablePanel.add(pane);


        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        tabbedPane.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if(timeFollowsTab.isSelected())
                {
                    if(built)
                    {
                        viewByRaidComboBox.setEditable(true);
                        switch(tabbedPane.getSelectedIndex())
                        {
                            case 0:
                                viewByRaidComboBox.setSelectedItem("Overall Time");
                                break;
                            case 1:
                                viewByRaidComboBox.setSelectedItem("Maiden Time");
                                break;
                            case 2:
                                viewByRaidComboBox.setSelectedItem("Bloat Time");
                                break;
                            case 3:
                                viewByRaidComboBox.setSelectedItem("Nylo Time");
                                break;
                            case 4:
                                viewByRaidComboBox.setSelectedItem("Sote Time");
                                break;
                            case 5:
                                viewByRaidComboBox.setSelectedItem("Xarp Time");
                                break;
                            case 6:
                                viewByRaidComboBox.setSelectedItem("Verzik Time");
                                break;

                        }
                        viewByRaidComboBox.setEditable(false);
                        updateTable();
                    }
                }
            }
        });

        JComponent overallPanel = new JPanel();
        tabbedPane.addTab("Overall", overallPanel);
        overallPanel.setLayout(new GridLayout(2, 3));

        JPanel overallCustomPanel = new JPanel();
        overallCustomPanel.setLayout(new BorderLayout());
        overallCustomPanel.setBorder(BorderFactory.createTitledBorder(""));

        JPanel customSubPanel = new JPanel();
        customSubPanel.setLayout(new GridLayout(1, 4));

        JPanel subPanel1 = new JPanel();
        subPanel1.setLayout(new GridLayout(1, 1));

        JPanel subPanel2 = new JPanel();
        subPanel2.setLayout(new GridLayout(5, 2));

        JPanel subPanel3 = new JPanel();

        JPanel subPanel4 = new JPanel();

        sortOptionsBox = new JComboBox(new String[]
                {
                        "Date",
                        "Value",
                        "Scale"
                }
                );

        sortOrderBox = new JComboBox(new String[]
                {
                        "Ascending",
                        "Descending"
                });

        statisticsBox = new JComboBox(DataPoint.getByNames());


        statisticsBox.addActionListener(
                al->
                {
                    updateTable();
                });

        sortOptionsBox.addActionListener(
                al->
                {
                    updateTable();
                }
        );

        sortOrderBox.addActionListener(
                al->
                {
                    updateTable();
                }
        );

        JLabel textCustomAverageLabel = new JLabel("Average:", SwingConstants.LEFT);
        JLabel textCustomMedianLabel = new JLabel("Median:", SwingConstants.LEFT);
        JLabel textCustomModeLabel = new JLabel("Mode:", SwingConstants.LEFT);
        JLabel textCustomMinLabel = new JLabel("Minimum:", SwingConstants.LEFT);
        JLabel textCustomMaxLabel = new JLabel("Maximum:", SwingConstants.LEFT);

        subPanel2.add(textCustomAverageLabel);
        subPanel2.add(customAverageLabel);

        subPanel2.add(textCustomMedianLabel);
        subPanel2.add(customMedianLabel);

        subPanel2.add(textCustomModeLabel);
        subPanel2.add(customModeLabel);

        subPanel2.add(textCustomMinLabel);
        subPanel2.add(customMinLabel);

        subPanel2.add(textCustomMaxLabel);
        subPanel2.add(customMaxLabel);

        subPanel1.add(statisticsBox);

        JButton undoFilter = new JButton("Clear manual filter");
        undoFilter.addActionListener(al->
        {
            filteredIndices.clear();
            updateTable();
        });

        subPanel1.setBorder(BorderFactory.createTitledBorder("Choose Statistic"));
        subPanel2.setBorder(BorderFactory.createTitledBorder("Results"));
        subPanel3.setBorder(BorderFactory.createTitledBorder("Table Options"));
        subPanel3.add(sortOptionsBox);
        subPanel3.add(sortOrderBox);
        subPanel3.add(undoFilter);
        JPanel buttonLine = new JPanel();
        buttonLine.setLayout(new GridLayout(1, 2));
        buttonLine.add(new JLabel("Config"));


        subPanel4.setBorder(BorderFactory.createTitledBorder("View Raid By"));
        viewByRaidComboBox.addActionListener(
                al->
                {
                    updateTable();
                });

        subPanel4.add(viewByRaidComboBox);

        subPanel4.add(timeFollowsTab);

        subPanel4.add(raidsFoundLabel);
        subPanel4.add(completionsFound);
        raidsFoundLabel.setText("Raids found: " + data.size());
        completionsFound.setText("Completions found: " + completions);

        customSubPanel.add(subPanel1);
        customSubPanel.add(subPanel2);
        customSubPanel.add(subPanel3);
        customSubPanel.add(subPanel4);

        overallCustomPanel.add(customSubPanel);

        JPanel overallAveragePanel = new JPanel();
        overallAveragePanel.setLayout(new BorderLayout());
        overallAveragePanel.setBorder(BorderFactory.createTitledBorder("Average"));

        String roomColor = colorStr(new Color(200, 200, 200));

        JPanel overallAverageSubPanel = new JPanel();
        overallAverageSubPanel.setLayout(new GridLayout(7, 2));

        overallAverageSubPanel.add(new JLabel(roomColor + "Maiden"));
        overallAverageSubPanel.add(overallPanelMaidenAverage);

        overallAverageSubPanel.add(new JLabel("Bloat"));
        overallAverageSubPanel.add(overallPanelBloatAverage);

        overallAverageSubPanel.add(new JLabel("Nylocas"));
        overallAverageSubPanel.add(overallPanelNyloAverage);

        overallAverageSubPanel.add(new JLabel("Sotetseg"));
        overallAverageSubPanel.add(overallPanelSoteAverage);

        overallAverageSubPanel.add(new JLabel("Xarpus"));
        overallAverageSubPanel.add(overallPanelXarpusAverage);

        overallAverageSubPanel.add(new JLabel("Verzik"));
        overallAverageSubPanel.add(overallPanelVerzikAverage);

        overallAverageSubPanel.add(new JLabel("Overall"));
        overallAverageSubPanel.add(overallPanelOverallAverage);

        JPanel overallMedianPanel = new JPanel();
        overallMedianPanel.setLayout(new BorderLayout());
        overallMedianPanel.setBorder(BorderFactory.createTitledBorder("Median"));

        JPanel overallMedianSubPanel = new JPanel();
        overallMedianSubPanel.setLayout(new GridLayout(7, 2));

        overallMedianSubPanel.add(new JLabel("Maiden"));
        overallMedianSubPanel.add(overallPanelMaidenMedian);

        overallMedianSubPanel.add(new JLabel("Bloat"));
        overallMedianSubPanel.add(overallPanelBloatMedian);

        overallMedianSubPanel.add(new JLabel("Nylocas"));
        overallMedianSubPanel.add(overallPanelNyloMedian);

        overallMedianSubPanel.add(new JLabel("Sotetseg"));
        overallMedianSubPanel.add(overallPanelSoteMedian);

        overallMedianSubPanel.add(new JLabel("Xarpus"));
        overallMedianSubPanel.add(overallPanelXarpusMedian);

        overallMedianSubPanel.add(new JLabel("Verzik"));
        overallMedianSubPanel.add(overallPanelVerzikMedian);


        overallMedianSubPanel.add(new JLabel("Overall"));
        overallMedianSubPanel.add(overallPanelOverallMedian);


        overallAveragePanel.add(overallAverageSubPanel);
        overallMedianPanel.add(overallMedianSubPanel);

        JPanel overallMinimumPanel = new JPanel();
        overallMinimumPanel.setLayout(new BorderLayout());
        overallMinimumPanel.setBorder(BorderFactory.createTitledBorder("Minimum"));

        JPanel overallMinimumSubPanel = new JPanel();
        overallMinimumSubPanel.setLayout(new GridLayout(7, 2));

        overallMinimumSubPanel.add(new JLabel("Maiden"));
        overallMinimumSubPanel.add(overallPanelMaidenMinimum);

        overallMinimumSubPanel.add(new JLabel("Bloat"));
        overallMinimumSubPanel.add(overallPanelBloatMinimum);

        overallMinimumSubPanel.add(new JLabel("Nylocas"));
        overallMinimumSubPanel.add(overallPanelNyloMinimum);

        overallMinimumSubPanel.add(new JLabel("Sotetseg"));
        overallMinimumSubPanel.add(overallPanelSoteMinimum);

        overallMinimumSubPanel.add(new JLabel("Xarpus"));
        overallMinimumSubPanel.add(overallPanelXarpusMinimum);

        overallMinimumSubPanel.add(new JLabel("Verzik"));
        overallMinimumSubPanel.add(overallPanelVerzikMinimum);

        overallMinimumSubPanel.add(new JLabel("Overall"));
        overallMinimumSubPanel.add(overallPanelOverallMinimum);

        overallMinimumPanel.add(overallMinimumSubPanel);

        JPanel overallMaximumPanel = new JPanel();
        overallMaximumPanel.setLayout(new BorderLayout());
        overallMaximumPanel.setBorder(BorderFactory.createTitledBorder("Maximum"));

        JPanel overallMaximumSubPanel = new JPanel();
        overallMaximumSubPanel.setLayout(new GridLayout(7, 2));

        overallMaximumSubPanel.add(new JLabel("Maiden"));
        overallMaximumSubPanel.add(overallPanelMaidenMaximum);

        overallMaximumSubPanel.add(new JLabel("Bloat"));
        overallMaximumSubPanel.add(overallPanelBloatMaximum);

        overallMaximumSubPanel.add(new JLabel("Nylocas"));
        overallMaximumSubPanel.add(overallPanelNyloMaximum);

        overallMaximumSubPanel.add(new JLabel("Sotetseg"));
        overallMaximumSubPanel.add(overallPanelSoteMaximum);

        overallMaximumSubPanel.add(new JLabel("Xarpus"));
        overallMaximumSubPanel.add(overallPanelXarpusMaximum);

        overallMaximumSubPanel.add(new JLabel("Verzik"));
        overallMaximumSubPanel.add(overallPanelVerzikMaximum);

        overallMaximumSubPanel.add(new JLabel("Overall"));
        overallMaximumSubPanel.add(overallPanelOverallMaximum);

        overallMaximumPanel.add(overallMaximumSubPanel);

        JPanel topStatPanel = new JPanel();
        topStatPanel.setLayout(new GridLayout(1, 4));

        topStatPanel.add(overallAveragePanel);
        topStatPanel.add(overallMedianPanel);
        topStatPanel.add(overallMinimumPanel);
        topStatPanel.add(overallMaximumPanel);

        overallPanel.add(topStatPanel);
        overallPanel.add(overallCustomPanel);

        maidenTab = new StatisticTab(data, DataPoint.rooms.MAIDEN);
        tabbedPane.addTab("Maiden", maidenTab);
        bloatTab = new StatisticTab(data, DataPoint.rooms.BLOAT);
        tabbedPane.addTab("Bloat", bloatTab);
        nyloTab = new StatisticTab(data, DataPoint.rooms.NYLOCAS);
        tabbedPane.addTab("Nylo", nyloTab);
        soteTab = new StatisticTab(data, DataPoint.rooms.SOTETSEG);
        tabbedPane.addTab("Sotetseg", soteTab);
        xarpTab = new StatisticTab(data, DataPoint.rooms.XARPUS);
        tabbedPane.addTab("Xarpus", xarpTab);
        verzikTab = new StatisticTab(data, DataPoint.rooms.VERZIK);
        tabbedPane.addTab("Verzik", verzikTab);


        tabbedPane.setMinimumSize(new Dimension(100,300));

        JPanel additionalFiltersPanel = new JPanel();
        additionalFiltersPanel.setLayout(new BorderLayout());
        additionalFiltersPanel.setBorder(BorderFactory.createTitledBorder("Quick Filters"));
        additionalFiltersPanel.setMinimumSize(new Dimension(200, 300));
        additionalFiltersPanel.setPreferredSize(new Dimension(200, 300));

         filterSpectateOnly = new JCheckBox("Spectate Only");
         filterInRaidOnly = new JCheckBox("In Raid Only");
         filterCompletionOnly = new JCheckBox("Completion Only");
         filterWipeResetOnly = new JCheckBox("Wipe/Reset Only");
         filterComboBoxScale = new JComboBox(new String[]{"Solo", "Duo", "Trio", "4-Man", "5-Man"});
         filterCheckBoxScale = new JCheckBox("Scale");
         filterTodayOnly = new JCheckBox("Today Only");
         filterPartyOnly = new JCheckBox("Party Only");
         filterPartialData = new JCheckBox("Filter Partial Raids");
         filterPartialOnly = new JCheckBox("Filter Partial Rooms");
         filterPartialData.setToolTipText("Removes data sets that have any rooms that were partially completed");
         filterNormalOnly = new JCheckBox("Normal Mode Only", true);

        filterSpectateOnly.addActionListener(
                al->
                {
                    updateTable();
                });
        filterInRaidOnly.addActionListener(
                al->
                {
                    updateTable();
                });
        filterCompletionOnly.addActionListener(
                al->
                {
                    updateTable();
                });
        filterWipeResetOnly.addActionListener(
                al->
                {
                    updateTable();
                });
        filterComboBoxScale.addActionListener(
                al->
                {
                    updateTable();
                });
        filterCheckBoxScale.addActionListener(
                al->
                {
                    updateTable();
                });
        filterTodayOnly.addActionListener(
                al->
                {
                    updateTable();
                });
        filterPartyOnly.addActionListener(
                al->
                {
                    updateTable();
                });

        filterPartialOnly.addActionListener(
                al->
                {
                    updateTable();
                }
        );
        filterPartialData.addActionListener(
                al->
                {
                    updateTable();
                });
        filterNormalOnly.addActionListener(
                al->
                {
                    updateTable();
                }
        );

        JPanel scaleContainer = new JPanel();
        scaleContainer.setLayout(new BoxLayout(scaleContainer, BoxLayout.X_AXIS));


        JPanel filterHolder = new JPanel();
        filterHolder.setLayout(new GridLayout(10, 1));
        filterHolder.add(filterSpectateOnly);
        filterHolder.add(filterInRaidOnly);
        filterHolder.add(filterCompletionOnly);
        filterHolder.add(filterWipeResetOnly);
        filterHolder.add(filterTodayOnly);
        filterHolder.add(filterPartyOnly);
        filterHolder.add(filterPartialData);
        filterHolder.add(filterPartialOnly);
        filterHolder.add(filterNormalOnly);
        scaleContainer.add(filterCheckBoxScale);
        scaleContainer.add(filterComboBoxScale);
        filterHolder.add(scaleContainer);

        additionalFiltersPanel.add(filterHolder);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));

        topContainer.setPreferredSize(new Dimension(800, 300));
        topContainer.add(tabbedPane);
        topContainer.add(additionalFiltersPanel);

        setLabels(data);
        updateTable();

        container.setPreferredSize(new Dimension(800, 700));

        container.add(topContainer);
        container.add(tablePanel);

        JPanel splitLeftRight = new JPanel();
        splitLeftRight.setLayout(new BoxLayout(splitLeftRight, BoxLayout.X_AXIS));
        splitLeftRight.add(container);

        JPanel rightContainer = new JPanel();
        rightContainer.setPreferredSize(new Dimension(400, 700));
        rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));

        JPanel rightTopContainer = new JPanel();
        rightTopContainer.setBorder(BorderFactory.createTitledBorder("Advanced Filters"));
        rightTopContainer.setLayout(new GridLayout(3, 2));

        JPanel filterTimePanel = new JPanel();
        filterTimePanel.setBorder(BorderFactory.createTitledBorder("Filter by room or split time"));
        filterTimePanel.setLayout(new BoxLayout(filterTimePanel, BoxLayout.Y_AXIS));

        JPanel filterPlayerPanel = new JPanel();
        filterPlayerPanel.setBorder(BorderFactory.createTitledBorder("Filter by players in raid"));
        filterPlayerPanel.setLayout(new GridLayout(2,2));

        JPanel filterDatePanel = new JPanel();
        filterDatePanel.setBorder(BorderFactory.createTitledBorder("Filter by date"));
        filterDatePanel.setLayout(new GridLayout(2,2));

        JPanel filterOtherIntPanel = new JPanel();
        filterOtherIntPanel.setBorder(BorderFactory.createTitledBorder("Filter by other condition (int)"));
        filterOtherIntPanel.setLayout(new GridLayout(2,2));

        JPanel filterOtherBoolPanel = new JPanel();
        filterOtherBoolPanel.setBorder(BorderFactory.createTitledBorder("Filter by other condition (bool)"));
        filterOtherBoolPanel.setLayout(new GridLayout(2,2));


        timeFilterChoice = new JComboBox<String>(DataPoint.getTimeNames());

        String[] timeOperatorChoices =
                {
                "=",
                "<",
                ">",
                "<=",
                ">="
        };

        timeFilterOperator = new JComboBox<String>(timeOperatorChoices);


        timeFilterValue = new JTextField();

        JButton timeFilterAdd = new JButton("Add");
        timeFilterAdd.addActionListener(
                al->
                {
                    String time = validateTime(timeFilterValue.getText());
                    if(time.equals(""))
                    {
                        return;
                    }
                    String timeStr = timeFilterChoice.getSelectedItem().toString() + " " + timeFilterOperator.getSelectedItem().toString() + " " + time;
                    activeFilters.add(new ImplicitFilter(new FilterTime(DataPoint.getValue(String.valueOf(timeFilterChoice.getSelectedItem())), timeFilterOperator.getSelectedIndex(), getTimeFromString(time), timeStr)));
                    updateFilterTable();
                });
        timeFilterAdd.setPreferredSize(new Dimension(55, timeFilterAdd.getPreferredSize().height));
        timeFilterOperator.setPreferredSize(new Dimension(50, timeFilterAdd.getPreferredSize().height));
        timeFilterOperator.setMaximumSize(new Dimension(Integer.MAX_VALUE, timeFilterAdd.getPreferredSize().height));
        timeFilterChoice.setMaximumSize(new Dimension(Integer.MAX_VALUE, timeFilterAdd.getPreferredSize().height));
        timeFilterValue.setMaximumSize(new Dimension(Integer.MAX_VALUE, timeFilterAdd.getPreferredSize().height));
        timeFilterValue.setPreferredSize(new Dimension(75, timeFilterAdd.getPreferredSize().height));


        JPanel filterTimePanelTop = new JPanel();
        filterTimePanelTop.setLayout(new BoxLayout(filterTimePanelTop, BoxLayout.X_AXIS));
        filterTimePanelTop.add(timeFilterChoice);

        JPanel filterTimePanelBottom = new JPanel();
        filterTimePanelBottom.setLayout(new BoxLayout(filterTimePanelBottom, BoxLayout.X_AXIS));
        filterTimePanelBottom.add(timeFilterOperator);
        filterTimePanelBottom.add(Box.createRigidArea(new Dimension(2, 2)));
        filterTimePanelBottom.add(timeFilterValue);
        filterTimePanelBottom.add(Box.createRigidArea(new Dimension(2, 2)));
        filterTimePanelBottom.add(timeFilterAdd);
        filterTimePanel.add(filterTimePanelTop);
        filterTimePanel.add(Box.createRigidArea(new Dimension(5, 5)));
        filterTimePanel.add(filterTimePanelBottom);

        String[] playersQualifier = {
                "contains exactly",
                "includes",
                "includes any of",
                "excludes",
                "excludes all of"
        };

       playerFilterOperator = new JComboBox<String>(playersQualifier);
       playerFilterValue = new JTextField();
       JButton playerFilterAdd = new JButton("Add");
        playerFilterValue.setMaximumSize(new Dimension(Integer.MAX_VALUE, playerFilterAdd.getPreferredSize().height));
        playerFilterValue.setPreferredSize(new Dimension(75, playerFilterAdd.getPreferredSize().height));
        playerFilterOperator.setMaximumSize(new Dimension(Integer.MAX_VALUE, playerFilterAdd.getPreferredSize().height));
        playerFilterAdd.setPreferredSize(new Dimension(55, playerFilterAdd.getPreferredSize().height));

        playerFilterAdd.addActionListener(
                al->
                {
                    String filterStr = "Raid " + playerFilterOperator.getSelectedItem().toString() + " " + playerFilterValue.getText();
                    activeFilters.add(new ImplicitFilter(new FilterPlayers(playerFilterValue.getText(), playerFilterOperator.getSelectedIndex(), filterStr)));
                    updateFilterTable();
                });

        JPanel filterPlayerPanelTop = new JPanel();
        filterPlayerPanelTop.setLayout(new BoxLayout(filterPlayerPanelTop, BoxLayout.X_AXIS));
        JPanel filterPlayerPanelBottom = new JPanel();
        filterPlayerPanelBottom.setLayout(new BoxLayout(filterPlayerPanelBottom, BoxLayout.X_AXIS));

        filterPlayerPanel.setLayout(new BoxLayout(filterPlayerPanel, BoxLayout.Y_AXIS));

        filterPlayerPanelTop.add(playerFilterOperator);
        filterPlayerPanelBottom.add(playerFilterValue);
        filterPlayerPanelBottom.add(Box.createRigidArea(new Dimension(2, 2)));
        filterPlayerPanelBottom.add(playerFilterAdd);

        filterPlayerPanel.add(filterPlayerPanelTop);
        filterPlayerPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        filterPlayerPanel.add(filterPlayerPanelBottom);

        String[] choicesDate =
                {
                "on",
                "before",
                "after"
        };

        dateFilterOperator = new JComboBox<String>(choicesDate);
        dateFilterValue = new JTextField();
        dateFilterOperator.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateFilterValue.getPreferredSize().height));


        JButton dateFilterAdd = new JButton("Add");
        dateFilterAdd.addActionListener(
                al->
                {
                    try
                    {
                        String dateString = dateTextField.getText();
                        String[] datePartial = dateString.split("/");
                        int year = Integer.parseInt(datePartial[0]);
                        int month = Integer.parseInt(datePartial[1]);
                        int day = Integer.parseInt(datePartial[2]);
                        Date date = new GregorianCalendar(year, month-1, day).getTime();
                        String filterStr = "Raid was " + dateFilterOperator.getSelectedItem().toString() + " " + date.toString();
                        activeFilters.add(new ImplicitFilter(new FilterDate(date, dateFilterOperator.getSelectedIndex(), filterStr)));
                    }
                    catch (Exception e)
                    {

                    }
                    updateFilterTable();
                });

        dateFilterValue.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateFilterAdd.getPreferredSize().height));
        dateFilterValue.setPreferredSize(new Dimension(90, dateFilterAdd.getPreferredSize().height));

        dateFilterAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateFilterAdd.getPreferredSize().height));
        dateFilterAdd.setPreferredSize(new Dimension(55, dateFilterAdd.getPreferredSize().height));

        JPanel dateTopRow = new JPanel();
        dateTopRow.setLayout(new BoxLayout(dateTopRow, BoxLayout.X_AXIS));

        JPanel dateBottomRow = new JPanel();
        dateBottomRow.setLayout(new BoxLayout(dateBottomRow, BoxLayout.X_AXIS));

        dateTopRow.add(dateFilterOperator);
        dateTopRow.add(Box.createRigidArea(new Dimension(2, 2)));
        dateTopRow.add(dateFilterAdd);
        dateTextField = new JTextField();
        dateBottomRow.add(dateTextField);
        dateBottomRow.add(new JLabel("YYYY/MM/DD"));
        filterDatePanel.setLayout(new BoxLayout(filterDatePanel, BoxLayout.Y_AXIS));
        filterDatePanel.add(dateTopRow);
        filterDatePanel.add(Box.createRigidArea(new Dimension(5, 5)));
        filterDatePanel.add(dateBottomRow);


        String[] otherIntOperatorChoices = {
                "=",
                "<",
                ">",
                "<=",
                ">="
        };


        otherIntFilterChoice = new JComboBox<String>(DataPoint.getOtherIntNames());
        otherIntFilterOperator = new JComboBox<String>(otherIntOperatorChoices);
        otherIntFilterValue = new JTextField();

        JButton otherIntAdd = new JButton("Add");
        otherIntAdd.addActionListener(
                al->
                {
                    String filterStr = otherIntFilterChoice.getSelectedItem().toString() + " " + otherIntFilterOperator.getSelectedItem().toString() + " " + otherIntFilterValue.getText() + " ";
                    activeFilters.add(new ImplicitFilter(new FilterOtherInt(DataPoint.getValue(String.valueOf(otherIntFilterChoice.getSelectedItem())), otherIntFilterOperator.getSelectedIndex(), Integer.parseInt(otherIntFilterValue.getText()), filterStr)));
                    updateFilterTable();
                }
        );

        otherIntFilterChoice.setMaximumSize(new Dimension(Integer.MAX_VALUE, otherIntAdd.getPreferredSize().height));

        otherIntFilterValue.setMaximumSize(new Dimension(Integer.MAX_VALUE, otherIntAdd.getPreferredSize().height));
        otherIntFilterValue.setPreferredSize(new Dimension(70, otherIntAdd.getPreferredSize().height));

        otherIntAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, otherIntAdd.getPreferredSize().height));
        otherIntAdd.setPreferredSize(new Dimension(55, otherIntAdd.getPreferredSize().height));

        otherIntFilterOperator.setMaximumSize(new Dimension(Integer.MAX_VALUE, otherIntAdd.getPreferredSize().height));
        otherIntFilterOperator.setPreferredSize(new Dimension(50, otherIntAdd.getPreferredSize().height));


        JPanel otherIntTop = new JPanel();
        otherIntTop.setLayout(new BoxLayout(otherIntTop, BoxLayout.X_AXIS));
        JPanel otherIntBottom = new JPanel();
        otherIntBottom.setLayout(new BoxLayout(otherIntBottom, BoxLayout.X_AXIS));

        otherIntTop.add(otherIntFilterChoice);
        otherIntBottom.add(otherIntFilterOperator);
        otherIntBottom.add(Box.createRigidArea(new Dimension(2, 2)));
        otherIntBottom.add(otherIntFilterValue);
        otherIntBottom.add(Box.createRigidArea(new Dimension(2, 2)));
        otherIntBottom.add(otherIntAdd);

        filterOtherIntPanel.setLayout(new BoxLayout(filterOtherIntPanel, BoxLayout.Y_AXIS));
        filterOtherIntPanel.add(otherIntTop);
        filterOtherIntPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        filterOtherIntPanel.add(otherIntBottom);

        String[] choicesOtherBool = {
                "Maiden skip successful",
                "Reset after maiden",
                "Maiden wipe",
                "Reset after bloat",
                "Bloat wipe",
                "Reset after nylo",
                "Nylo wipe",
                "Reset after sote",
                "Sote wipe",
                "Reset after xarp",
                "Xarp wipe",
                "Verzik  wipe"
        };

        String[] qualifierOtherBool = {
                "True",
                "False"
        };

        otherBoolFilterChoice = new JComboBox<String>(choicesOtherBool);
        otherBoolFilterOperator = new JComboBox<String>(qualifierOtherBool);


        JButton otherBoolAdd = new JButton("Add Filter");
        otherBoolAdd.addActionListener(
                al->
                {
                    String filterStr = otherBoolFilterChoice.getSelectedItem().toString() + " " + otherBoolFilterOperator.getSelectedItem().toString();
                    activeFilters.add(new ImplicitFilter(new FilterOtherBool(otherBoolFilterChoice.getSelectedIndex(), otherBoolFilterOperator.getSelectedIndex() == 0, filterStr)));
                    updateFilterTable();
                }
        );

        otherBoolFilterOperator.setMaximumSize(new Dimension(Integer.MAX_VALUE, otherBoolAdd.getPreferredSize().height));
        otherBoolFilterChoice.setMaximumSize(new Dimension(Integer.MAX_VALUE, otherBoolAdd.getPreferredSize().height));

        JPanel filterBoolTop = new JPanel();
        filterBoolTop.setLayout(new BoxLayout(filterBoolTop, BoxLayout.X_AXIS));
        JPanel filterBoolBottom = new JPanel();
        filterBoolBottom.setLayout(new BoxLayout(filterBoolBottom, BoxLayout.X_AXIS));

        filterBoolTop.add(otherBoolFilterChoice);
        filterBoolBottom.add(otherBoolFilterOperator);
        filterBoolBottom.add(Box.createRigidArea(new Dimension(2, 2)));
        filterBoolBottom.add(otherBoolAdd);

        filterOtherBoolPanel.setLayout(new BoxLayout(filterOtherBoolPanel, BoxLayout.Y_AXIS));

        filterOtherBoolPanel.add(filterBoolTop);
        filterOtherBoolPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        filterOtherBoolPanel.add(filterBoolBottom);

        JPanel filterOptions = new JPanel();
        filterOptions.setBorder(BorderFactory.createTitledBorder("Filter Options"));
        rightTopContainer.setPreferredSize(new Dimension(400, 250));
        rightTopContainer.add(filterTimePanel);
        rightTopContainer.add(filterOtherIntPanel);
        rightTopContainer.add(filterPlayerPanel);
        rightTopContainer.add(filterOtherBoolPanel);
        rightTopContainer.add(filterDatePanel);


        JPanel rightBottomContainer = new JPanel();
        rightBottomContainer.setPreferredSize(new Dimension(400, 200));
        rightBottomContainer.setBorder(BorderFactory.createTitledBorder("Active Filters"));

        filterTableContainer = new JPanel();

        JPopupMenu raidPopup = new JPopupMenu();

        JMenuItem analyzeSessions = new JMenuItem("Analyze Sessions");
        analyzeSessions.setBackground(Color.BLACK);
        analyzeSessions.setOpaque(true);
        analyzeSessions.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ArrayList<RoomData> rows = new ArrayList<>();
                int[] toRemove = table.getSelectedRows();
                for(int i = 0; i < toRemove.length; i++)
                {
                    rows.add(currentData.get(Integer.parseInt(table.getModel().getValueAt(toRemove[i], 0).toString())));
                }
                Map<Integer, Map<String,ArrayList<RoomData>>> sessions = new LinkedHashMap<>();
                for(RoomData data : rows)
                {
                    if(!sessions.containsKey(data.players.size()))
                    {
                        Map<String, ArrayList<RoomData>> scale = new LinkedHashMap<>();
                        ArrayList<RoomData> list = new ArrayList<>();
                        list.add(data);
                        scale.put(data.getPlayerList(), list);
                        sessions.put(data.players.size(), scale);
                    }
                    else
                    {
                        if(!sessions.get(data.players.size()).containsKey(data.getPlayerList()))
                        {
                            ArrayList<RoomData> list = new ArrayList<>();
                            list.add(data);
                            sessions.get(data.players.size()).put(data.getPlayerList(), list);
                        }
                        else
                        {
                            sessions.get(data.players.size()).get(data.getPlayerList()).add(data);
                        }
                    }
                }
                ArrayList<String> labels = new ArrayList<>();
                ArrayList<ArrayList<RoomData>> dataSets = new ArrayList<>();
                for(Integer scale : sessions.keySet())
                {
                    for(String playerList : sessions.get(scale).keySet())
                    {
                        dataSets.add(sessions.get(scale).get(playerList));
                        labels.add(playerList);
                    }
                }
                ComparisonView graphView = new ComparisonView(dataSets, labels);
                graphView.open();
            }
        });

        JMenuItem addToComparison = new JMenuItem("Add set to comparison");
        addToComparison.setBackground(Color.BLACK);
        addToComparison.setOpaque(true);

        addToComparison.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ArrayList<RoomData> rows = new ArrayList<>();
                int[] toRemove = table.getSelectedRows();
                for(int i = 0; i < toRemove.length; i++)
                {
                    rows.add(currentData.get(Integer.parseInt(table.getModel().getValueAt(toRemove[i], 0).toString())));
                }
                comparisons.add(rows);
                updateComparisonTable();
            }
        });
        JMenuItem exportRaids = new JMenuItem("Export Selected Raids to CSV");
        exportRaids.setBackground(Color.BLACK);
        exportRaids.setOpaque(true);
        exportRaids.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ArrayList<RoomData> rows = new ArrayList<>();
                int[] toRemove = table.getSelectedRows();
                for(int i = 0; i < toRemove.length; i++)
                {
                    rows.add(currentData.get(Integer.parseInt(table.getModel().getValueAt(toRemove[i], 0).toString())));
                }
                new SaveRaidsBaseFrame(rows).open();
            }
        });

        JMenuItem filterRaids = new JMenuItem("Filter Selected Raids");
        filterRaids.setBackground(Color.BLACK);
        filterRaids.setOpaque(true);
        filterRaids.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int[] toRemove = table.getSelectedRows();
                for(int i = 0; i < toRemove.length; i++)
                {
                    filteredIndices.add(Integer.parseInt(table.getModel().getValueAt(toRemove[i], 0).toString()));
                }

                updateTable();
            }
        });

        JMenuItem filterExclusiveRaids = new JMenuItem("Filter All Except Selected Raids");
        filterExclusiveRaids.setBackground(Color.BLACK);
        filterExclusiveRaids.setOpaque(true);
        filterExclusiveRaids.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int[] toKeep = table.getSelectedRows();
                for(int i = 0; i < table.getRowCount(); i++)
                {
                    boolean found = false;
                    for(int j = 0; j < toKeep.length; j++)
                    {
                        if(i == toKeep[j])
                        {
                            found = true;
                        }
                    }
                    if(!found)
                    {
                        filteredIndices.add(Integer.parseInt(table.getModel().getValueAt(i, 0).toString()));
                    }
                }

                updateTable();
            }
        });

        raidPopup.add(exportRaids);
        raidPopup.add(addToComparison);
        raidPopup.add(filterRaids);
        raidPopup.add(filterExclusiveRaids);
        raidPopup.add(analyzeSessions);
        table.setComponentPopupMenu(raidPopup);

        filterTable = new JTable();
        filterTable.setPreferredSize(new Dimension(380, 135));
        JScrollPane tableScrollView = new JScrollPane(filterTable);
        tableScrollView.setPreferredSize(new Dimension(380, 140));
        updateFilterTable();
        filterTableContainer.add(tableScrollView);

        rightBottomContainer.add(filterTableContainer);

        JButton saveFiltersButton = new JButton("Save");
        saveFiltersButton.addActionListener(
                al->
                {
                    SaveFilterBaseFrame saveFilter = new SaveFilterBaseFrame(activeFilters);
                    saveFilter.open();
                });
        JButton loadFiltersButton = new JButton("Load");
        loadFiltersButton.addActionListener(
                al->
                {
                   new LoadFilterBaseFrame(this).open();
                });
        JButton clearFiltersButton = new JButton("Clear");
        clearFiltersButton.addActionListener(
                al->
                {
                    activeFilters.clear();
                    updateFilterTable();
                });
        GridLayout layout = new GridLayout(2, 2);
        layout.setHgap(2);
        layout.setVgap(2);
        filterOptions.setLayout(layout);

        filterOptions.add(saveFiltersButton);
        filterOptions.add(loadFiltersButton);
        filterOptions.add(clearFiltersButton);
        rightTopContainer.add(filterOptions);

        JPanel rightBottomBottomContainer = new JPanel();
        rightBottomBottomContainer.setPreferredSize(new Dimension(400, 250));
        rightBottomBottomContainer.setBorder(BorderFactory.createTitledBorder("Comparison Options"));

        comparisonTable = new JTable();
        JScrollPane comparisonTableScroll = new JScrollPane(comparisonTable);
        comparisonTable.setPreferredSize(new Dimension(380, 170));
        comparisonTableScroll.setPreferredSize(new Dimension(380, 155));
        updateComparisonTable();


        rightBottomBottomContainer.add(comparisonTableScroll);
        JButton viewComparisonsButton = new JButton("View Comparisons");
        viewComparisonsButton.addActionListener(al ->
        {
            if(comparisonTable.getModel().getRowCount() == 0)
            {
                new NoDataPopUp().open();
            }
            else
            {
                ArrayList<String> labels = new ArrayList<>();
                for (int i = 0; i < comparisonTable.getModel().getRowCount(); i++) {
                    labels.add(comparisonTable.getModel().getValueAt(i, 1).toString());
                }
                ComparisonView graphView = new ComparisonView(comparisons, labels);
                graphView.open();
            }
        });
        rightBottomBottomContainer.add(viewComparisonsButton);

        rightContainer.add(rightTopContainer);
        rightContainer.add(rightBottomContainer);
        rightContainer.add(rightBottomBottomContainer);

        splitLeftRight.add(rightContainer);

        add(splitLeftRight);
        pack();
        built = true;
    }

    private String validateTime(String text)
    {
        if(text.startsWith(":"))
        {
            //ignore for now
        }
        return text;
    }

    private int getTimeFromString(String text)
    {
        int ticks = 0;
        String sub = text;
        if(sub.contains(":"))
        {
            ticks += 100 * Integer.parseInt(sub.substring(0, sub.indexOf(":")));
            sub = text.substring(sub.indexOf(":")+1);
        }
        ticks += (Double.parseDouble(sub)/0.6);
        return ticks;
    }

    public void removeFilterRow(int row)
    {
        activeFilters.remove(row);
        updateFilterTable();
    }

    public void removeComparisonRow(int row)
    {
        comparisons.remove(row);
        updateComparisonTable();
    }

    private void updateComparisonTable()
    {
        String[] columnNames = {"Sets", "Label", ""};
        ArrayList<Object[]> tableData = new ArrayList<>();

        int index = 0;
        for(ArrayList<RoomData> comparison : comparisons)
        {
            Object[] row = {comparison.size() +" raids averaging: " + RoomUtil.time(StatisticGatherer.getOverallTimeAverage(comparison)),"Set " + index, "Remove"};
            tableData.add(row);
            index++;
        }

        Object[][] tableObject = new Object[tableData.size()][2];
        int count = 0;
        for(Object[] row : tableData)
        {
            tableObject[count] = row;
            count++;
        }
        comparisonTable.setModel(new DefaultTableModel(tableObject, columnNames));
        comparisonTable.getColumn("Sets").setCellEditor(new NonEditableCell(new JTextField()));
        comparisonTable.getColumn("").setCellRenderer(new ButtonRenderer());
        comparisonTable.getColumn("").setCellEditor(new ButtonEditorComparisonData(new JCheckBox(), this));
        resizeColumnWidthFilters(comparisonTable);
        comparisonTable.getColumn("").setMaxWidth(100);
        comparisonTable.setFillsViewportHeight(true);
        comparisonTable.validate();
        comparisonTable.repaint();
    }
    public void updateFilterTable()
    {
        String[] columnNames = {"Filter Descriptions", ""};
        ArrayList<Object[]> tableData = new ArrayList<>();

        for(ImplicitFilter filter: activeFilters)
        {
            Object[] row = {filter.getFilterDescription(), "Remove"};
            tableData.add(row);
        }

        Object[][] tableObject = new Object[tableData.size()][2];
        int count = 0;
        for (Object[] row : tableData) {
            tableObject[count] = row;
            count++;
        }
        filterTable.setModel(new DefaultTableModel(tableObject, columnNames));
        filterTable.getColumn("Filter Descriptions").setCellEditor(new NonEditableCell(new JTextField()));
        filterTable.getColumn("").setCellRenderer(new ButtonRenderer());
        filterTable.getColumn("").setCellEditor(new ButtonEditorFilterData(new JCheckBox(), this));
        resizeColumnWidthFilters(filterTable);
        filterTable.getColumn("").setMaxWidth(100);
        filterTable.setFillsViewportHeight(true);
        filterTableContainer.validate();
        filterTableContainer.repaint();
        updateTable();
    }
}
