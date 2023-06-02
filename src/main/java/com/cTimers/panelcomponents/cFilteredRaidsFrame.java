package com.cTimers.panelcomponents;


import com.cTimers.filters.*;
import com.cTimers.jdatepicker.JDatePanel;
import com.cTimers.jdatepicker.JDatePicker;
import com.cTimers.jdatepicker.UtilDateModel;
import com.cTimers.utility.cDataPoint;
import lombok.extern.slf4j.Slf4j;
import com.cTimers.cRoomData;
import com.cTimers.utility.RoomUtil;
import com.cTimers.utility.cStatisticGatherer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

@Slf4j
public class cFilteredRaidsFrame extends cFrame
{
    private final ArrayList<Integer> filteredIndices;
    private JTable comparisonTable;
    private final ArrayList<ArrayList<cRoomData>> comparisons;

    private final JTabbedPane tabbedPane = new JTabbedPane();
    public ArrayList<cImplicitFilter> activeFilters;
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
    public ArrayList<cRoomData> currentData;
    private JComboBox<String> timeFilterChoice;
    private JComboBox<String> timeFilterOperator;
    private JTextField timeFilterValue;
    private JTable filterTable;
    private JComboBox<String> playerFilterOperator;
    private JTextField playerFilterValue;
    private cStatisticTab maidenTab;
    private cStatisticTab bloatTab;
    private cStatisticTab nyloTab;
    private cStatisticTab soteTab;
    private cStatisticTab xarpTab;
    private cStatisticTab verzikTab;
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

    public cFilteredRaidsFrame()
    {
        filteredIndices = new ArrayList<>();
        comparisons = new ArrayList<>();
        activeFilters = new ArrayList<>();
        this.setPreferredSize(new Dimension(1200,820));
    }

    public void updateCustomStats(ArrayList<cRoomData> raids)
    {
        cDataPoint dataPoint = cDataPoint.values()[statisticsBox.getSelectedIndex()];
        boolean time = dataPoint.type == cDataPoint.types.TIME;

        double avg = cStatisticGatherer.getGenericAverage(raids, dataPoint);
        double med = cStatisticGatherer.getGenericMedian(raids,dataPoint);
        double mod = cStatisticGatherer.getGenericMode(raids,dataPoint);
        double min = cStatisticGatherer.getGenericMin(raids,dataPoint);
        double max = cStatisticGatherer.getGenericMax(raids,dataPoint);

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

    private boolean evaluateAllFilters(cRoomData data)
    {
        for(cImplicitFilter filter : activeFilters)
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
        ArrayList<cRoomData> tableData = new ArrayList<>();
        for(cRoomData data : currentData)
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
                switch(viewByRaidComboBox.getSelectedIndex())
                {
                    case 0:
                        if(!data.getOverallTimeAccurate())
                        {
                            shouldDataBeIncluded = false;
                        }
                        break;
                    case 1:
                        if(!data.maidenStartAccurate || !data.maidenEndAccurate)
                        {
                            shouldDataBeIncluded = false;
                        }
                        break;
                    case 2:
                        if(!data.bloatStartAccurate || !data.bloatEndAccurate)
                        {
                            shouldDataBeIncluded = false;
                        }
                        break;
                    case 3:
                        if(!data.nyloStartAccurate || !data.nyloEndAccurate)
                        {
                            shouldDataBeIncluded = false;
                        }
                        break;
                    case 4:
                        if(!data.soteStartAccurate || !data.soteEndAccurate)
                        {
                            shouldDataBeIncluded = false;
                        }
                        break;
                    case 5:
                        if(!data.xarpStartAccurate || !data.xarpEndAccurate)
                        {
                            shouldDataBeIncluded = false;
                        }
                        break;
                    case 6:
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
            switch(viewByRaidComboBox.getSelectedIndex())
            {
                case 0:
                    timeToDisplay = data.getTimeSum();
                    break;
                case 1:
                    timeToDisplay = data.getValue(cDataPoint.MAIDEN_TOTAL_TIME);
                    break;
                case 2:
                    timeToDisplay = data.getValue(cDataPoint.BLOAT_TOTAL_TIME);
                    break;
                case 3:
                    timeToDisplay = data.getValue(cDataPoint.NYLO_TOTAL_TIME);
                    break;
                case 4:
                    timeToDisplay = data.getValue(cDataPoint.SOTE_TOTAL_TIME);
                    break;
                case 5:
                    timeToDisplay = data.getValue(cDataPoint.XARP_TOTAL_TIME);
                    break;
                case 6:
                    timeToDisplay = data.getValue(cDataPoint.VERZIK_TOTAL_TIME);
                    break;
            }
            if(timeToDisplay == 0)
            {
                shouldDataBeIncluded = false;
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
                tableData.sort(Comparator.comparing(cRoomData::getDate));
            }
            else
            {
                tableData.sort(Comparator.comparing(cRoomData::getDate).reversed());
            }
        }
        else if(sortOptionsBox.getSelectedIndex() == 1)
        {
            switch(viewByRaidComboBox.getSelectedIndex())
            {
                case 1:
                    if(sortOrderBox.getSelectedIndex() == 0)
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getMaidenTime));
                    }
                    else
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getMaidenTime).reversed());
                    }
                    break;
                case 2:
                    if(sortOrderBox.getSelectedIndex() == 0)
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getBloatTime));
                    }
                    else
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getBloatTime).reversed());
                    }
                    break;
                case 3:
                    if(sortOrderBox.getSelectedIndex() == 0)
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getNyloTime));
                    }
                    else
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getNyloTime).reversed());
                    }
                    break;
                case 4:
                    if(sortOrderBox.getSelectedIndex() == 0)
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getSoteTime));
                    }
                    else
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getSoteTime).reversed());
                    }
                    break;
                case 5:
                    if(sortOrderBox.getSelectedIndex() == 0)
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getXarpTime));
                    }
                    else
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getXarpTime).reversed());
                    }
                    break;
                case 6:
                    if(sortOrderBox.getSelectedIndex() == 0)
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getVerzikTime));
                    }
                    else
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getVerzikTime).reversed());
                    }
                    break;
                default:
                    if(sortOrderBox.getSelectedIndex() == 0)
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getTimeSum));
                    }
                    else
                    {
                        tableData.sort(Comparator.comparing(cRoomData::getTimeSum).reversed());
                    }
                    break;
            }
        }
        else if(sortOptionsBox.getSelectedIndex() == 2)
        {
            if(sortOrderBox.getSelectedIndex() == 0)
            {
                tableData.sort(Comparator.comparing(cRoomData::getScale));
            }
            else
            {
                tableData.sort(Comparator.comparing(cRoomData::getScale).reversed());
            }
        }

        updateCustomStats(tableData);
        raidsFoundLabel.setText("Raids Found: " + tableData.size());
        completionsFound.setText("Completions Found: " + completions);
        updateTabNames(tableData);

        String[] columnNames = { "", "Date", "Scale", "Status", viewByRaidComboBox.getSelectedItem().toString(), "Players", "Spectate", "View"};
        ArrayList<Object[]> tableBuilder = new ArrayList<>();
        for(cRoomData raid : tableData)
        {
            String players = "";
            for(String s : raid.players)
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
            switch(viewByRaidComboBox.getSelectedIndex())
            {
                case 0:
                    timeToDisplay = raid.getTimeSum();
                    break;
                case 1:
                    timeToDisplay = raid.getValue(cDataPoint.MAIDEN_TOTAL_TIME);
                    break;
                case 2:
                    timeToDisplay = raid.getValue(cDataPoint.BLOAT_TOTAL_TIME);
                    break;
                case 3:
                    timeToDisplay = raid.getValue(cDataPoint.NYLO_TOTAL_TIME);
                    break;
                case 4:
                    timeToDisplay = raid.getValue(cDataPoint.SOTE_TOTAL_TIME);
                    break;
                case 5:
                    timeToDisplay = raid.getValue(cDataPoint.XARP_TOTAL_TIME);
                    break;
                case 6:
                    timeToDisplay = raid.getValue(cDataPoint.VERZIK_TOTAL_TIME);
                    break;
            }

            Object[] row =
                    {
                            raid.index,
                            dateString,
                            scaleString,
                            getRoomStatus(raid),
                            RoomUtil.time(timeToDisplay),
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
        table.getColumn("Date").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("Scale").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("Status").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn(viewByRaidComboBox.getSelectedItem().toString()).setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("Players").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("Spectate").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("View").setCellRenderer(new cButtonRenderer());
        table.getColumn("View").setCellEditor(new cButtonEditorRoomData(new JCheckBox(), tableData));
        resizeColumnWidth(table);
        table.setFillsViewportHeight(true);
        setLabels(tableData);
        container.validate();
        container.repaint();


    }

    public String getRoomStatus(cRoomData data)
    {
        String raidStatusString = "";
        if(data.maidenWipe)
        {
            raidStatusString = "Maiden Wipe";
        }
        else if(data.maidenReset)
        {
            raidStatusString = "Maiden Reset";
        }
        else if(data.bloatWipe)
        {
            raidStatusString = "Bloat Wipe";
        }
        else if(data.bloatReset)
        {
            raidStatusString = "Bloat Reset";
        }
        else if(data.nyloWipe)
        {
            raidStatusString = "Nylo Wipe";
        }
        else if(data.nyloReset)
        {
            raidStatusString = "Nylo Reset";
        }
        else if(data.soteWipe)
        {
            raidStatusString = "Sotetseg Wipe";
        }
        else if(data.soteReset)
        {
            raidStatusString = "Sotetseg Reset";
        }
        else if(data.xarpWipe)
        {
            raidStatusString = "Xarpus Wipe";
        }
        else if(data.xarpReset)
        {
            raidStatusString = "Xarpus Reset";
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

    private void updateTabNames(ArrayList<cRoomData> data)
    {
        int maidenCount = 0;
        int bloatCount = 0;
        int nyloCount = 0;
        int soteCount = 0;
        int xarpCount = 0;
        int verzikCount = 0;
        for(cRoomData d : data)
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

    public void setLabels(ArrayList<cRoomData> data)
    {
        setOverallLabels(data);
        maidenTab.updateTab(data);
        bloatTab.updateTab(data);
        nyloTab.updateTab(data);
        soteTab.updateTab(data);
        xarpTab.updateTab(data);
        verzikTab.updateTab(data);
    }
    public void setOverallLabels(ArrayList<cRoomData> data)
    {
        setOverallAverageLabels(data);
        setOverallMedianLabels(data);
        setOverallMinLabels(data);
        setOverallMaxLabels(data);
    }

    public void setOverallAverageLabels(ArrayList<cRoomData> data)
    {
        overallPanelMaidenAverage.setText(RoomUtil.time(cStatisticGatherer.getGenericAverage(data, cDataPoint.MAIDEN_TOTAL_TIME)));
        overallPanelBloatAverage.setText(RoomUtil.time(cStatisticGatherer.getGenericAverage(data, cDataPoint.BLOAT_TOTAL_TIME)));
        overallPanelNyloAverage.setText(RoomUtil.time(cStatisticGatherer.getGenericAverage(data, cDataPoint.NYLO_TOTAL_TIME)));
        overallPanelSoteAverage.setText(RoomUtil.time(cStatisticGatherer.getGenericAverage(data, cDataPoint.SOTE_TOTAL_TIME)));
        overallPanelXarpusAverage.setText(RoomUtil.time(cStatisticGatherer.getGenericAverage(data,cDataPoint.XARP_TOTAL_TIME)));
        overallPanelVerzikAverage.setText(RoomUtil.time(cStatisticGatherer.getGenericAverage(data,cDataPoint.VERZIK_TOTAL_TIME)));
        overallPanelOverallAverage.setText(RoomUtil.time(cStatisticGatherer.getOverallTimeAverage(data)));
    }

    public void setOverallMedianLabels(ArrayList<cRoomData> data)
    {
        overallPanelMaidenMedian.setText(RoomUtil.time(cStatisticGatherer.getGenericMedian(data, cDataPoint.MAIDEN_TOTAL_TIME)));
        overallPanelBloatMedian.setText(RoomUtil.time(cStatisticGatherer.getGenericMedian(data, cDataPoint.BLOAT_TOTAL_TIME)));
        overallPanelNyloMedian.setText(RoomUtil.time(cStatisticGatherer.getGenericMedian(data, cDataPoint.NYLO_TOTAL_TIME)));
        overallPanelSoteMedian.setText(RoomUtil.time(cStatisticGatherer.getGenericMedian(data, cDataPoint.SOTE_TOTAL_TIME)));
        overallPanelXarpusMedian.setText(RoomUtil.time(cStatisticGatherer.getGenericMedian(data, cDataPoint.XARP_TOTAL_TIME)));
        overallPanelVerzikMedian.setText(RoomUtil.time(cStatisticGatherer.getGenericMedian(data, cDataPoint.VERZIK_TOTAL_TIME)));
        overallPanelOverallMedian.setText(RoomUtil.time(cStatisticGatherer.getOverallMedian(data)));
    }

    public void setOverallMinLabels(ArrayList<cRoomData> data)
    {
        overallPanelMaidenMinimum.setText(RoomUtil.time(cStatisticGatherer.getGenericMin(data, cDataPoint.MAIDEN_TOTAL_TIME)));
        overallPanelBloatMinimum.setText(RoomUtil.time(cStatisticGatherer.getGenericMin(data, cDataPoint.BLOAT_TOTAL_TIME)));
        overallPanelNyloMinimum.setText(RoomUtil.time(cStatisticGatherer.getGenericMin(data, cDataPoint.NYLO_TOTAL_TIME)));
        overallPanelSoteMinimum.setText(RoomUtil.time(cStatisticGatherer.getGenericMin(data, cDataPoint.SOTE_TOTAL_TIME)));
        overallPanelXarpusMinimum.setText(RoomUtil.time(cStatisticGatherer.getGenericMin(data, cDataPoint.XARP_TOTAL_TIME)));
        overallPanelVerzikMinimum.setText(RoomUtil.time(cStatisticGatherer.getGenericMin(data, cDataPoint.VERZIK_TOTAL_TIME)));
        overallPanelOverallMinimum.setText(RoomUtil.time(cStatisticGatherer.getOverallTimeMin(data)));
    }
    private void setOverallMaxLabels(ArrayList<cRoomData> data)
    {
        overallPanelMaidenMaximum.setText(RoomUtil.time(cStatisticGatherer.getGenericMax(data, cDataPoint.MAIDEN_TOTAL_TIME)));
        overallPanelBloatMaximum.setText(RoomUtil.time(cStatisticGatherer.getGenericMax(data, cDataPoint.BLOAT_TOTAL_TIME)));
        overallPanelNyloMaximum.setText(RoomUtil.time(cStatisticGatherer.getGenericMax(data, cDataPoint.NYLO_TOTAL_TIME)));
        overallPanelSoteMaximum.setText(RoomUtil.time(cStatisticGatherer.getGenericMax(data, cDataPoint.SOTE_TOTAL_TIME)));
        overallPanelXarpusMaximum.setText(RoomUtil.time(cStatisticGatherer.getGenericMax(data, cDataPoint.XARP_TOTAL_TIME)));
        overallPanelVerzikMaximum.setText(RoomUtil.time(cStatisticGatherer.getGenericMax(data, cDataPoint.VERZIK_TOTAL_TIME)));
        overallPanelOverallMaximum.setText(RoomUtil.time(cStatisticGatherer.getOverallMax(data)));
    }

    public void createFrame(ArrayList<cRoomData> data)
    {
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
                        "Time",
                        "Scale"
                }
                );

        sortOrderBox = new JComboBox(new String[]
                {
                        "Ascending",
                        "Descending"
                });

        statisticsBox = new JComboBox(cDataPoint.getByNames());


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


        subPanel4.setBorder(BorderFactory.createTitledBorder("View Raid Time By"));
        viewByRaidComboBox = new JComboBox(new String[]{"Overall Time", "Maiden Time", "Bloat Time", "Nylocas Time", "Sotetseg Time", "Xarpus Time", "Verzik Time"});
        viewByRaidComboBox.addActionListener(
                al->
                {
                    updateTable();
                });

        subPanel4.add(viewByRaidComboBox);

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

        maidenTab = new cStatisticTab(data, cDataPoint.rooms.MAIDEN);
        tabbedPane.addTab("Maiden", maidenTab);
        bloatTab = new cStatisticTab(data, cDataPoint.rooms.BLOAT);
        tabbedPane.addTab("Bloat", bloatTab);
        nyloTab = new cStatisticTab(data, cDataPoint.rooms.NYLOCAS);
        tabbedPane.addTab("Nylo", nyloTab);
        soteTab = new cStatisticTab(data, cDataPoint.rooms.SOTETSEG);
        tabbedPane.addTab("Sotetseg", soteTab);
        xarpTab = new cStatisticTab(data, cDataPoint.rooms.XARPUS);
        tabbedPane.addTab("Xarpus", xarpTab);
        verzikTab = new cStatisticTab(data, cDataPoint.rooms.VERZIK);
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


        timeFilterChoice = new JComboBox<String>(cDataPoint.getTimeNames());

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
                    activeFilters.add(new cImplicitFilter(new cFilterTime(cDataPoint.getValue(String.valueOf(timeFilterChoice.getSelectedItem())), timeFilterOperator.getSelectedIndex(), getTimeFromString(time), timeStr)));
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
                    activeFilters.add(new cImplicitFilter(new cFilterPlayers(playerFilterValue.getText(), playerFilterOperator.getSelectedIndex(), filterStr)));
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

        UtilDateModel model = new UtilDateModel();
        JDatePicker datePicker = new JDatePicker(model);

        JButton dateFilterAdd = new JButton("Add");
        dateFilterAdd.addActionListener(
                al->
                {
                    String filterStr = "Raid was " + dateFilterOperator.getSelectedItem().toString() + " " + datePicker.getModel().getValue().toString();
                    activeFilters.add(new cImplicitFilter(new cFilterDate((Date)datePicker.getModel().getValue(), dateFilterOperator.getSelectedIndex(), filterStr)));
                    dateFilterValue.setText((datePicker.getModel().getValue()).toString());
                    updateFilterTable();
                });

        dateFilterValue.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateFilterAdd.getPreferredSize().height));
        dateFilterValue.setPreferredSize(new Dimension(90, dateFilterAdd.getPreferredSize().height));

        //datePicker.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateFilterAdd.getPreferredSize().height));
        //datePicker.setPreferredSize(new Dimension(30, dateFilterAdd.getPreferredSize().height));

        dateFilterAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateFilterAdd.getPreferredSize().height));
        dateFilterAdd.setPreferredSize(new Dimension(55, dateFilterAdd.getPreferredSize().height));

        JPanel dateTopRow = new JPanel();
        dateTopRow.setLayout(new BoxLayout(dateTopRow, BoxLayout.X_AXIS));

        JPanel dateBottomRow = new JPanel();
        dateBottomRow.setLayout(new BoxLayout(dateBottomRow, BoxLayout.X_AXIS));

        dateTopRow.add(dateFilterOperator);
        dateTopRow.add(Box.createRigidArea(new Dimension(2, 2)));
        dateTopRow.add(dateFilterAdd);
        //dateBottomRow.add(dateFilterValue);
        dateBottomRow.add(datePicker);
        //dateBottomRow.add(Box.createRigidArea(new Dimension(2, 2)));
        //dateBottomRow.add(dateFilterAdd);
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


        otherIntFilterChoice = new JComboBox<String>(cDataPoint.getOtherIntNames());
        otherIntFilterOperator = new JComboBox<String>(otherIntOperatorChoices);
        otherIntFilterValue = new JTextField();

        JButton otherIntAdd = new JButton("Add");
        otherIntAdd.addActionListener(
                al->
                {
                    String filterStr = otherIntFilterChoice.getSelectedItem().toString() + " " + otherIntFilterOperator.getSelectedItem().toString() + " " + otherIntFilterValue.getText() + " ";
                    activeFilters.add(new cImplicitFilter(new cFilterOtherInt(cDataPoint.getValue(String.valueOf(otherIntFilterChoice.getSelectedItem())), otherIntFilterOperator.getSelectedIndex(), Integer.parseInt(otherIntFilterValue.getText()), filterStr)));
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
                    activeFilters.add(new cImplicitFilter(new cFilterOtherBool(otherBoolFilterChoice.getSelectedIndex(), otherBoolFilterOperator.getSelectedIndex() == 0, filterStr)));
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
        JMenuItem addToComparison = new JMenuItem("Add set to comparison");
        addToComparison.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ArrayList<cRoomData> rows = new ArrayList<>();
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
        exportRaids.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ArrayList<cRoomData> rows = new ArrayList<>();
                int[] toRemove = table.getSelectedRows();
                for(int i = 0; i < toRemove.length; i++)
                {
                    rows.add(currentData.get(Integer.parseInt(table.getModel().getValueAt(toRemove[i], 0).toString())));
                }
                new cSaveRaidsFrame(rows).open();
            }
        });

        JMenuItem filterRaids = new JMenuItem("Filter Selected Raids");
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
                    cSaveFilterFrame saveFilter = new cSaveFilterFrame(activeFilters);
                    saveFilter.open();
                });
        JButton loadFiltersButton = new JButton("Load");
        loadFiltersButton.addActionListener(
                al->
                {
                   new cLoadFilterFrame(this).open();
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
            ArrayList<String> labels = new ArrayList<>();
            for(int i = 0; i < comparisonTable.getModel().getRowCount(); i++)
            {
                labels.add(comparisonTable.getModel().getValueAt(i, 1).toString());
            }
            cComparisonView graphView = new cComparisonView(comparisons, labels);
            graphView.open();
        });
        rightBottomBottomContainer.add(viewComparisonsButton);

        rightContainer.add(rightTopContainer);
        rightContainer.add(rightBottomContainer);
        rightContainer.add(rightBottomBottomContainer);

        splitLeftRight.add(rightContainer);

        add(splitLeftRight);
        pack();
    }

    private String validateTime(String text) //TODO ERROR HANDLE + Below
    {
        if(text.startsWith(":"))
        {

        }
        return text;
    }

    private int getTimeFromString(String text) //TODO ERROR HANDLE
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
        for(ArrayList<cRoomData> comparison : comparisons)
        {
            Object[] row = {comparison.size() +" raids averaging: " + RoomUtil.time(cStatisticGatherer.getOverallTimeAverage(comparison)),"Set " + index, "Remove"};
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
        comparisonTable.getColumn("Sets").setCellEditor(new cNonEditableCell(new JTextField()));
        comparisonTable.getColumn("").setCellRenderer(new cButtonRenderer());
        comparisonTable.getColumn("").setCellEditor(new cButtonEditorComparisonData(new JCheckBox(), this));
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

        for(cImplicitFilter filter: activeFilters)
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
        filterTable.getColumn("Filter Descriptions").setCellEditor(new cNonEditableCell(new JTextField()));
        filterTable.getColumn("").setCellRenderer(new cButtonRenderer());
        filterTable.getColumn("").setCellEditor(new cButtonEditorFilterData(new JCheckBox(), this));
        resizeColumnWidthFilters(filterTable);
        filterTable.getColumn("").setMaxWidth(100);
        filterTable.setFillsViewportHeight(true);
        filterTableContainer.validate();
        filterTableContainer.repaint();
        updateTable();
    }
}
