package com.advancedraidtracker.ui;


import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.filters.*;
import com.advancedraidtracker.rooms.cox.Cox;
import com.advancedraidtracker.ui.customrenderers.*;
import com.advancedraidtracker.ui.charts.ChartFrame;
import com.advancedraidtracker.ui.comparisonview.ComparisonViewFrame;
import com.advancedraidtracker.ui.comparisonview.NoDataPopUp;
import com.advancedraidtracker.ui.crableaks.CrabLeakInfo;
import com.advancedraidtracker.ui.exportraids.SaveRaids;
import com.advancedraidtracker.ui.filters.LoadFilter;
import com.advancedraidtracker.ui.filters.SaveFilter;
import com.advancedraidtracker.ui.statistics.StatisticTab;
import com.advancedraidtracker.utility.*;
import com.advancedraidtracker.utility.datautility.*;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.toa.Toa;
import com.advancedraidtracker.utility.datautility.datapoints.tob.Tob;
import com.advancedraidtracker.utility.wrappers.StringInt;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.List;

import static com.advancedraidtracker.constants.RaidRoom.*;
import static com.advancedraidtracker.utility.UISwingUtility.*;

@Slf4j
public class Raids extends BaseFrame
{
    private final ArrayList<Integer> filteredIndices;
    private JTable comparisonTable;
    private final List<List<Raid>> comparisons;

    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final JTabbedPane raidTypeTabbedPane = new JTabbedPane();
    public ArrayList<ImplicitFilter> activeFilters;

    Map<String, JLabel> averageLabels = new LinkedHashMap<>();
    Map<String, JLabel> medianLabels = new LinkedHashMap<>();
    Map<String, JLabel> minLabels = new LinkedHashMap<>();
    Map<String, JLabel> maxLabels = new LinkedHashMap<>();

    List<Map<String, JLabel>> averageLabels2 = new ArrayList<>();
    List<Map<String, JLabel>> medianLabels2 = new ArrayList<>();
    List<Map<String, JLabel>> minLabels2 = new ArrayList<>();
    List<Map<String, JLabel>> maxLabels2 = new ArrayList<>();

    private final Multimap<String, String> aliases;

    private final JTextArea aliasText;

    JTextField dateTextField;
    JCheckBox filterSpectateOnly;
    JCheckBox filterInRaidOnly;
    JCheckBox filterCompletionOnly;
    JCheckBox filterWipeResetOnly;
    JComboBox<String> filterComboBoxScale;
    JCheckBox filterCheckBoxScale;
    JCheckBox filterTodayOnly;
    JCheckBox filterPartyOnly;
    JCheckBox filterPartialData;
    JCheckBox filterPartialOnly;
    JCheckBox filterNormalOnly;
    JTable table;
    JPanel container;
    private JPanel filterTableContainer;
    public List<Raid> currentData;
    private JComboBox<String> timeFilterChoice;
    private JComboBox<String> timeFilterOperator;
    private JTextField timeFilterValue;
    private JTable filterTable;
    private JComboBox<String> playerFilterOperator;
    private JTextField playerFilterValue;
    private JCheckBox timeFollowsTab;
    private boolean built = false;
    private JComboBox<String> dateFilterOperator;
    private JComboBox<String> otherIntFilterChoice;
    private JComboBox<String> otherIntFilterOperator;
    private JTextField otherIntFilterValue;
    private JComboBox<String> otherBoolFilterChoice;
    private JComboBox<String> otherBoolFilterOperator;


    private final AdvancedRaidTrackerConfig config;
    private final ItemManager itemManager;
    private final ClientThread clientThread;

    public String[] rooms = {"Maiden", "Bloat", "Nylocas", "Sotetseg", "Xarpus", "Verzik", "Challenge"};
    private final ConfigManager configManager;

    private final Set<String> shouldNotBeSortedByTicks = new HashSet<>(Arrays.asList("", "Scale", "Raid", "Status", "Players", "Spectate", "Date"));

    public Raids(AdvancedRaidTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager)
    {
        int index = 0;
        for (RaidType raidType : RaidType.values())
        {
            averageLabels2.add(new LinkedHashMap<>());
            medianLabels2.add(new LinkedHashMap<>());
            maxLabels2.add(new LinkedHashMap<>());
            minLabels2.add(new LinkedHashMap<>());

            for(RaidRoom room : RaidRoom.getRaidRoomsForRaidType(raidType))
            {
                averageLabels2.get(index).put(room.name, getDarkJLabel("", SwingConstants.RIGHT));
                medianLabels2.get(index).put(room.name, getDarkJLabel("", SwingConstants.RIGHT));
                maxLabels2.get(index).put(room.name, getDarkJLabel("", SwingConstants.RIGHT));
                minLabels2.get(index).put(room.name, getDarkJLabel("", SwingConstants.RIGHT));
            }
            index++;
        }

        this.clientThread = clientThread;
        this.itemManager = itemManager;
        this.configManager = configManager;
        columnHeaders = new ArrayList<>();
        for (String s : columnHeaderNames)
        {
            columnHeaders.add(getCheckBoxMenuItem(s));
        }
        aliases = ArrayListMultimap.create();
        filteredIndices = new ArrayList<>();
        comparisons = new ArrayList<>();
        activeFilters = new ArrayList<>();
        aliasText = new JTextArea();
        this.config = config;
        this.setPreferredSize(new Dimension(1200, 820));
    }

    public JComboBox<String> customColumnComboBox = new JComboBox<>();

    private boolean evaluateAllFilters(Raid data)
    {
        for (ImplicitFilter filter : activeFilters)
        {
            if (!filter.evaluate(data))
            {
                return false;
            }
        }
        return true;
    }

    private boolean tableSortEnabled = true;

    public void updateTable()
    {
        int completions = 0;
        List<Raid> tableData = new ArrayList<>();
        for (Raid data : currentData)
        {
            boolean shouldDataBeIncluded = true;
            if (filterSpectateOnly.isSelected())
            {
                if (!data.isSpectate)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if (filterInRaidOnly.isSelected())
            {
                if (data instanceof Tob && ((Tob) data).isSpectated())
                {
                    shouldDataBeIncluded = false;
                }
            }
            if (filterCompletionOnly.isSelected())
            {
                if (!data.isCompleted() || !data.isAccurate())
                {
                    shouldDataBeIncluded = false;
                }
            }
            if (filterWipeResetOnly.isSelected())
            {
                if (data.isCompleted())
                {
                    shouldDataBeIncluded = false;
                }
            }
            if (filterPartialData.isSelected())
            {
                if (!(data.isAccurate()))
                {
                    shouldDataBeIncluded = false;
                }
            }
            if (shouldDataBeIncluded && filterTodayOnly.isSelected())
            {
                shouldDataBeIncluded = false;
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(data.getDate());
                cal2.setTime(new Date(System.currentTimeMillis()));
                if (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH))
                {
                    shouldDataBeIncluded = true;
                }
            }
            if (filterPartyOnly.isSelected())
            {
                if (data.isInParty())
                {
                    shouldDataBeIncluded = false;
                }
            }
            if (filterNormalOnly.isSelected())
            {
                if (data instanceof Tob && (data.isStoryMode || data.isHardMode))
                {
                    shouldDataBeIncluded = false;
                }
                else if(data instanceof Toa && data.get(DataPoint.TOA_INVOCATION_LEVEL) >= 150 && data.get(DataPoint.TOA_INVOCATION_LEVEL) < 300)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if (filterPartialOnly.isSelected()) //todo
            {
                if (data instanceof Tob)
                {
                    Tob tobData = (Tob) data;
                    String comboText = customColumnComboBox.getSelectedItem().toString();
                    if(comboText.endsWith("Time"))
                    {
                        if(!data.getRoomAccurate(RaidRoom.valueOf(comboText.split(",")[0])))
                        {
                            shouldDataBeIncluded = false;
                        }
                    }
                }
            }
            if (shouldDataBeIncluded && filterCheckBoxScale.isSelected())
            {
                shouldDataBeIncluded = filterComboBoxScale.getSelectedIndex() + 1 == data.getScale();
            }

            for (Integer i : filteredIndices)
            {
                if (data.getIndex() == i)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(!evaluateAllFilters(data))
            {
                shouldDataBeIncluded = false;
            }
            if (shouldDataBeIncluded)
            {
                tableData.add(data);
                if (data.isCompleted() && data.isAccurate())
                {
                    completions++;
                }
            }
        }
        setTitle("Raids: " + table.getRowCount() + ", Completions: " + completions);
        updateTabNames(tableData);
        tableData = handleTableSort(tableData);
        ArrayList<String> columnNamesDynamic = new ArrayList<>();
        columnNamesDynamic.add("");
        for (JCheckBoxMenuItem item : columnHeaders)
        {
            if (item.getState())
            {
                columnNamesDynamic.add(item.getText());
            }
            if (item.getText().equals("Status")) //adds custom after
            {
                columnNamesDynamic.add("Custom");
            }
        }
        ArrayList<Object[]> tableBuilder = new ArrayList<>();
        for (Raid raid : tableData)
        {
            ArrayList<Object> rowBuilder = new ArrayList<>();
            for (String column : columnNamesDynamic)
            {
                rowBuilder.add(getRowData(column, raid));
            }
            tableBuilder.add(rowBuilder.toArray());
        }
        int columns = 0;
        if (!tableBuilder.isEmpty())
        {
            columns = tableBuilder.get(0).length;
        }
        Object[][] tableObject = new Object[tableData.size()][columns];
        int count = 0;
        for (Object[] row : tableBuilder)
        {
            tableObject[count] = row;
            count++;
        }

        table.setModel(new DefaultTableModel(tableObject, columnNamesDynamic.toArray()));
        for (int i = 0; i < table.getColumnCount(); i++)
        {
            if (table.getColumnName(i).equals("View"))
            {
                table.getColumn(table.getColumnName(i)).setCellEditor(new ButtonEditorRoomData(new JCheckBox(), tableData));
                table.getColumn(table.getColumnName(i)).setCellRenderer(new ButtonRenderer());
            }
            else if(table.getColumnName(i).equals("Custom"))
            {
                table.getColumn(table.getColumnName(i)).setCellEditor(new NonEditableCell(new JTextField()));
                table.getColumn(table.getColumnName(i)).setCellRenderer(new StripedTableRowCellRenderer());
                table.getColumn(table.getColumnName(i)).setHeaderRenderer(new DynamicTableHeaderRenderer(customColumnComboBox, this));

            }
            else
            {
                table.getColumn(table.getColumnName(i)).setCellEditor(new NonEditableCell(new JTextField()));
                table.getColumn(table.getColumnName(i)).setCellRenderer(new StripedTableRowCellRenderer());
            }
        }

        setTableListeners();

        resizeColumnWidth(table);
        table.setFillsViewportHeight(true);
        setLabels(tableData);
        container.validate();
        container.repaint();
    }

    /**
     * Mouselistener events are only dispatched from UI thread which is blocked while dragging so are not sent
     * MouseMotionListener events are only dispatched if the mouse is moving
     * In other to have the expected behavior for releasing right click both stationary and moving both listeners must be added
     */
    private void setTableListeners()
    {
        table.addMouseMotionListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setTableRightClickSelection(e);
            }
            @Override
            public void mouseReleased(MouseEvent e)
            {
                setTableRightClickSelection(e);
            }
        });

        table.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setTableRightClickSelection(e);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                setTableRightClickSelection(e);
            }
        });
    }

    /**
     * Sets selection to current row if only one or zero rows are currently selected
     * @param e mouse event passthrough
     */
    private void setTableRightClickSelection(MouseEvent e)
    {
        if(table.getSelectedRows().length < 2 && SwingUtilities.isRightMouseButton(e))
        {
            int r = table.rowAtPoint(e.getPoint());
            if (r >= 0 && r < table.getRowCount())
            {
                table.setRowSelectionInterval(r, r);
            }
        }
    }

    /**
     * We want the custom column to have a dropdown arrow so the user knows if they click it they can switch which data point is shown in that column
     * That combobox is for the renderer only, the actual drop down happens as a JPopUpMenu so that we can get sub layers (e.g. toa->kephri->time)
     * The JTable setautosorter doesn't have enough flexibility to do what we need so we have to manually implement table sorting by checking header clicked.
     * However, we don't want to sort the custom column to sort if you click the dropdown arrow, so we check if the click happens in the last 20 pixels of the
     * custom column. If not, we set the last column clicked and update the table
     */
    private void setupTableHeaderListener()
    {
        table.getTableHeader().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(SwingUtilities.isLeftMouseButton(e))
                {
                        String clickedColumn = null;
                        Component c = e.getComponent();
                        if(c instanceof JTableHeader)
                        {
                            JTableHeader header = (JTableHeader) c;
                            clickedColumn = header.getTable().getColumnName(header.getColumnModel().getColumnIndexAtX(e.getX()));
                            if(clickedColumn.equals("Custom"))
                            {
                                Rectangle rect = table.getCellRect(0, header.getColumnModel().getColumnIndexAtX(e.getX()), true);
                                if(e.getX() > rect.getX()+rect.getWidth()-20)
                                {
                                    return;
                                }
                            }
                        }
                        if(clickedColumn != null)
                        {
                            if(clickedColumn.equals(lastColumnClicked)) //if the same column is clicked twice the sort order should be inversed
                            {
                                isSortReversed = !isSortReversed;
                            }
                            else
                            {
                                //some columns, e.g. Date or Raid index, feel more natural to sort descending first, but time based columns feel better ascending first
                                isSortReversed = shouldNotBeSortedByTicks.contains(clickedColumn);
                            }
                            lastColumnClicked = clickedColumn;
                            updateTable();
                        }

                }
            }
        });
    }

    public Object getRowData(String column, Raid raid)
    {
        switch (column)
        {
            case "":
                return raid.getIndex();
            case "Date":
                LocalDate date = raid.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                DateTimeFormatter formatter =
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault());
                return formatter.format(date);
            case "Scale":
                return raid.getScaleString();
            case "Status":
                return raid.getRoomStatus();
            case "Raid":
                return raid.getRaidType().colorName();
            case "Players":
                return (String.join(",",raid.getPlayers()).replaceAll(String.valueOf((char) 160), String.valueOf(' '))); //todo replace with Text method
            case "Spectate":
                return (raid.isSpectate) ? "Yes" : "No";
            case "View":
                return "View";
            case "Time":
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(raid.getDate());
                int hour = cal2.get(Calendar.HOUR_OF_DAY);
                int minute = cal2.get(Calendar.MINUTE);
                String minuteString = (minute < 10) ? "0" + minute : String.valueOf(minute);
                String period = (hour > 11) ? " PM" : " AM";
                if (hour == 0)
                {
                    hour = 12;
                } else if (hour > 12)
                {
                    hour -= 12;
                }
                return hour + ":" + minuteString + period;
            case "Custom":
                String valueToDisplay = "(?)";
                String dataPointName = "";
                try
                {
                    dataPointName = customColumnComboBox.getSelectedItem().toString();
                    DataPoint point = DataPoint.getValue(dataPointName);
                    valueToDisplay = String.valueOf(raid.getParser(point.room).data.get(point));
                } catch (Exception ignored)
                {

                }
                return (isTime(dataPointName) ? RoomUtil.time(valueToDisplay) : valueToDisplay);
        }
        String valueToDisplay = "(?)";
        String dataPointName = "";
        try
        {
            DataPoint point = DataPoint.getValue(column);
            valueToDisplay = String.valueOf(raid.getParser(point.room).data.get(point));
        } catch (Exception ignored)
        {

        }
        return (isTime(column) ? RoomUtil.time(valueToDisplay) : valueToDisplay);
    }

    boolean isTime(String value) //todo what is the purpose?
    {
        try
        {
            if (!value.contains("Player:"))
            {
                return DataPoint.getValue(value).isTime();
            }
            else
            {
                return false;
            }
        } catch (Exception e)
        {
            return false;
        }
    }

    boolean isTime()
    {
        if (!customColumnComboBox.getSelectedItem().toString().contains("Player:"))
        {
            return (Objects.requireNonNull(DataPoint.getValue(Objects.requireNonNull(customColumnComboBox.getSelectedItem()).toString())).type == DataPoint.types.TIME);
        } else
        {
            return false;
        }
    }


    private void updateTabNames(List<Raid> data)
    {
        ArrayList<Raid> tobData = new ArrayList<>();

        /*
        for (SimpleRaidDataBase raidData : data)
        {
            if (raidData instanceof SimpleTOBData)
            {
                tobData.add((SimpleTOBData) raidData);
            }
        }
         */
        int maidenCount = 0;
        int bloatCount = 0;
        int nyloCount = 0;
        int soteCount = 0;
        int xarpCount = 0;
        int verzikCount = 0;
        for (Raid d : tobData)
        {
            if (d.getRoomAccurate(MAIDEN))
            {
                maidenCount++;
            }
            if (d.getRoomAccurate(BLOAT))
            {
                bloatCount++;
            }
            if (d.getRoomAccurate(NYLOCAS))
            {
                nyloCount++;
            }
            if (d.getRoomAccurate(SOTETSEG))
            {
                soteCount++;
            }
            if (d.getRoomAccurate(XARPUS))
            {
                xarpCount++;
            }
            if (d.getRoomAccurate(VERZIK))
            {
                verzikCount++;
            }
        }
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        /*tabbedPane.setTitleAt(1, "Maiden (" + maidenCount + ")");
        tabbedPane.setTitleAt(2, "Bloat (" + bloatCount + ")");
        tabbedPane.setTitleAt(3, "Nylo (" + nyloCount + ")");
        tabbedPane.setTitleAt(4, "Sotetseg (" + soteCount + ")");
        tabbedPane.setTitleAt(5, "Xarpus (" + xarpCount + ")");
        tabbedPane.setTitleAt(6, "Verzik (" + verzikCount + ")");*/
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
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            if (width > 300)
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
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            if(table.getColumnName(column).equals("Custom"))
            {
                width = Math.max(width, ((String) customColumnComboBox.getSelectedItem()).length()*14);
            }
            width = Math.max(table.getColumnModel().getColumn(column).getPreferredWidth(), width);
            if (width > 400)
            {
                width = 400;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    public void setLabels(List<Raid> data)
    {
        List<Raid> tobData = new ArrayList<>();
        List<Raid> toaData = new ArrayList<>();
        for (Raid raidData : data)
        {
            if (raidData instanceof Tob) //check if this can just be passed as the raid and not sort into lists per raid //todo
            {
                tobData.add(raidData);
            }
            else if(raidData instanceof Toa)
            {
                toaData.add(raidData);
            }
        }
        setOverallLabels(data);
        for(StatisticTab tab : tobTabs)
        {
            tab.updateTab(tobData);
        }
        for(StatisticTab tab : toaTabs)
        {
            tab.updateTab(toaData);
        }
    }

    public void setOverallLabels(List<Raid> data)
    {

        setOverallAverageLabels(data);
        setOverallMedianLabels(data);
        setOverallMinLabels(data);
        setOverallMaxLabels(data);

    }

    public void setOverallAverageLabels(List<Raid> data)
    {
        for(Map<String, JLabel> labelMap : averageLabels2)
        {
            for(String s : labelMap.keySet())
            {
                labelMap.get(s).setText(RoomUtil.time(StatisticGatherer.getGenericAverage(data, DataPoint.getValue(s + " Time"))));
            }
        }
    }

    public void setOverallMedianLabels(List<Raid> data)
    {
        for(Map<String, JLabel> labelMap : medianLabels2)
        {
            for(String s : labelMap.keySet())
            {
                labelMap.get(s).setText(RoomUtil.time(StatisticGatherer.getGenericMedian(data, DataPoint.getValue(s + " Time"))));
            }
        }
    }

    public void setOverallMinLabels(List<Raid> data)
    {
        for(Map<String, JLabel> labelMap : minLabels2)
        {
            for(String s : labelMap.keySet())
            {
                labelMap.get(s).setText(RoomUtil.time(StatisticGatherer.getGenericMin(data, DataPoint.getValue(s + " Time"))));
            }
        }
    }

    private void setOverallMaxLabels(List<Raid> data)
    {
        for(Map<String, JLabel> labelMap : maxLabels2)
        {
            for(String s : labelMap.keySet())
            {
                labelMap.get(s).setText(RoomUtil.time(StatisticGatherer.getGenericMax(data, DataPoint.getValue(s + " Time"))));
            }
        }
    }

    private JPopupMenu comboPopupMenu;
    private ArrayList<String> comboStrictData;
    private AbstractButton arrowButton;
    private boolean writing = false;

    private void updateAliases()
    {
        writing = true;
        aliases.clear();
        aliasText.setText("");
        for (String s : DataWriter.readAliasFile())
        {
            aliasText.append(s + "\n");
            String[] split = s.split(":");
            if (split.length != 2)
            {
                continue;
            }
            String name = split[0];
            aliases.putAll(name, Arrays.asList(split[1].split(",")));
        }
        writing = false;
    }

    DataPointMenu dataPointMenu;

    private JMenuItem createMenuItemTableHeader(final String name)
    {
        JMenuItem item = new JMenuItem(name);
        item.setBackground(Color.BLACK);
        item.setOpaque(true);
        item.addActionListener(event -> getUpdatedPopupMenu(name));
        return item;
    }


    private final Map<String, String[]> comboPopupData = new LinkedHashMap<>();


    public JPanel getOverallPanel(String title, Map<String, JLabel> labelMap)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        JPanel subPanel = new JPanel();
        subPanel.setLayout(new GridLayout(0, 2));

        int index = 0;
        for (String s : labelMap.keySet())
        {
            JLabel leftLabel = new JLabel(roomColor + s);
            subPanel.add(leftLabel);
            subPanel.add(labelMap.get(s));
            index++;
        }
        for(int i = index; i < 13; i++) //enforce formatting because swing is bad. 13 is the most labels used by any panel (# of chambers rooms)
        {
            JLabel leftLabel = new JLabel("");
            JLabel rightLabel = new JLabel("");
            subPanel.add(leftLabel);
            subPanel.add(rightLabel);
        }
        subPanel.setPreferredSize(new Dimension(100, 200));
        JScrollPane scrollPane = new JScrollPane(subPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);
        panel.setPreferredSize(new Dimension(150, 200));
        return panel;
    }

    public void clearData()
    {
        if(currentData != null)
        {
            currentData.clear();
        }
        if(comparisons != null)
        {
            comparisons.clear();
        }
        close();
    }

    public void setTableSorterActive(boolean state)
    {
        tableSortEnabled = state;
        if(state)
        {
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
            int dateIndex = -1;
            List<Integer> indiciesToSortByTimeValue = new ArrayList<>();
            for(int i = 0; i < table.getColumnCount(); i++)
            {
                try
                {
                    if (!shouldNotBeSortedByTicks.contains(table.getColumnName(i)))
                    {
                        indiciesToSortByTimeValue.add(i);
                    }
                }
                catch (Exception ignore)
                {

                }
            }
            try
            {
                dateIndex = table.getColumnModel().getColumnIndex("Date");
            }
            catch (Exception ignore)
            {
                //User has date column hidden
            }
            if(dateIndex != -1)
            {
                sorter.setComparator(dateIndex, (Comparator<String>) (date1, date2) ->
                {
                    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault());
                    LocalDate time1 = LocalDate.parse(date1, formatter);
                    LocalDate time2 = LocalDate.parse(date2, formatter);
                    return time1.compareTo(time2);
                });
            }
            for(Integer i : indiciesToSortByTimeValue)
            {
                sorter.setComparator(i, (Comparator<String>) (time1, time2) ->
                {
                    Integer ticks1 = RoomUtil.ticks(time1);
                    Integer ticks2 = RoomUtil.ticks(time2);
                    return ticks1.compareTo(ticks2);
                });
            }
            table.setRowSorter(sorter);
        }
        else
        {
            table.setRowSorter(null);
        }
    }

    public List<StatisticTab> tobTabs =  new ArrayList<>();
    public List<StatisticTab> toaTabs = new ArrayList<>();
    public List<StatisticTab> coxTabs = new ArrayList<>();

    private String lastColumnClicked = "Date";
    private boolean isSortReversed = true;
    private List<Raid> handleTableSort(List<Raid> tableData)
    {
        Comparator<Raid> comparator = null;
        switch(lastColumnClicked)
        {
            case "Date":
                comparator = Comparator.comparing(Raid::getDate);
                break;
            case "":
                comparator = Comparator.comparing(Raid::getIndex);
                break;
            case "Scale":
                comparator = Comparator.comparing(Raid::getScale);
                break;
            case "Raid":
                comparator = Comparator.comparing(Raid::getRaidType);
                break;
            case "Status":
                comparator = Comparator.comparing(Raid::getRoomStatus); //todo more intelligent sorting not based on alphabetical
                break;
            case "Players":
                comparator = Comparator.comparing(Raid::getPlayerString);
                break;
            case "Spectate":
                comparator = Comparator.comparing(Raid::isSpectated);
                break;
            case "Custom":
                comparator = ((o1, o2) -> o1.get(customColumnComboBox.getSelectedItem().toString()).compareTo(o2.get(customColumnComboBox.getSelectedItem().toString())));
                break;
            default:
                comparator = ((o1, o2) -> o1.get(lastColumnClicked).compareTo(o2.get(lastColumnClicked)));
                break;
        }
        if(comparator == null)
        {
            return tableData;
        }
        if(isSortReversed)
        {
            comparator = comparator.reversed();
        }
        tableData.sort(comparator);
        //If user sorts by a time, e.g., "Xarpus Time", we don't want to include data in the table if xarpus wasn't seen that raid
        if(shouldNotBeSortedByTicks.contains(lastColumnClicked))
        {
            return tableData;
        }
        List<Raid> filteredSortData = new ArrayList<>();
        for(Raid raid : tableData)
        {
            if(lastColumnClicked.equals("Custom"))
            {
                if(raid.getTimeAccurate(DataPoint.getValue(customColumnComboBox.getSelectedItem().toString())))
                {
                    filteredSortData.add(raid);
                }
            }
            else
            {
                if(raid.getTimeAccurate(DataPoint.getValue(lastColumnClicked)))
                {
                    filteredSortData.add(raid);
                }
            }
        }
        return filteredSortData;
    }


    public String getPlayerList(Multimap<String, String> aliases, Set<String> players)
    {
        StringBuilder list = new StringBuilder();
        ArrayList<String> names = new ArrayList<>();
        for (String s : players)
        {
            String name = s;
            for(String nameKey : aliases.keySet())
            {
                for(String alias : aliases.get(nameKey))
                {
                    if(name.equalsIgnoreCase(alias))
                    {
                        name = alias;
                        break;
                    }
                }
            }
            names.add(name);
        }
        names.sort(String::compareToIgnoreCase);
        for (String s : names)
        {
            list.append(s);
            list.append(",");
        }
        if (list.length() > 0)
        {
            return list.substring(0, list.length() - 1);
        } else
        {
            return "";
        }
    }

    public void createFrame(List<Raid> data)
    {
        customColumnComboBox.setEnabled(true);
        for(Component c : customColumnComboBox.getComponents())
        {
            //we are using the combobox for its appearance only, we do not want the real combobox dropdown to pop up when clicked because we are using
            //a jpopupmenu to display the content of the dropdown. Even if we set it not visible it will flash for a frame before going away.
            if(c instanceof AbstractButton)
            {
                c.setEnabled(false);
            }
        }
        customColumnComboBox.addItem("Challenge Time");


        timeFollowsTab = new JCheckBox("Time Follows Tab");
        timeFollowsTab.setSelected(true);

        for (int i = 0; i < data.size(); i++)
        {
            data.get(i).setIndex(i);
        }

        currentData = data;

        JPopupMenu tstMenu = getCustomColumnPopUpMenu();


        table = new JTable();
        //table.setAutoCreateRowSorter(true);
        table.getTableHeader().setComponentPopupMenu(tstMenu);
        JScrollPane pane = new JScrollPane(table);

        JPanel tablePanel = getTitledPanel("Raids");
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(pane);


        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JTabbedPane tobTabSubpanel = new JTabbedPane();
        JTabbedPane toaTabSubpanel = new JTabbedPane();
        JTabbedPane coxTabSubpanel = new JTabbedPane();

        tobTabSubpanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        toaTabSubpanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        coxTabSubpanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        List<JPanel> overallPanels = new ArrayList<>();
        for(int k = 0; k < 3; k++)
        {
            JPanel overallPanel = new JPanel();
            JPanel overallAveragePanel = getOverallPanel("Average", averageLabels2.get(k));
            JPanel overallMedianPanel = getOverallPanel("Median", medianLabels2.get(k));
            JPanel overallMinPanel = getOverallPanel("Minimum", minLabels2.get(k));
            JPanel overallMaxPanel = getOverallPanel("Maximum", maxLabels2.get(k));

            JPanel topStatPanel = new JPanel();
            topStatPanel.setLayout(new GridLayout(1, 4));

            topStatPanel.add(overallAveragePanel);
            topStatPanel.add(overallMedianPanel);
            topStatPanel.add(overallMinPanel);
            topStatPanel.add(overallMaxPanel);


            overallPanel.add(topStatPanel);
            overallPanels.add(overallPanel);
        }


        List<Raid> tobData = new ArrayList<>();
        List<Raid> toaData = new ArrayList<>();
        List<Raid> coxData = new ArrayList<>();

        for (Raid raidData : data)
        {
            if (raidData instanceof Tob)
            {
                tobData.add(raidData);
            }
            else if(raidData instanceof Toa)
            {
                toaData.add(raidData);
            }
            else if(raidData instanceof Cox)
            {
                coxData.add(raidData);
            }

        }

        List<Integer> tobIconIDs = new ArrayList<>(Arrays.asList(ItemID.LIL_MAIDEN, ItemID.LIL_BLOAT, ItemID.LIL_NYLO, ItemID.LIL_SOT, ItemID.LIL_XARP, ItemID.LIL_ZIK));
        List<Integer> toaIconIDs = new ArrayList<>(Arrays.asList(ItemID.NEUTRALISING_POTION, ItemID.BABI, ItemID.SWARM, ItemID.KEPHRITI, ItemID.DRAGON_PICKAXE, ItemID.AKKHITO, ItemID.WATER_CONTAINER, ItemID.ZEBO, ItemID.ELIDINIS_GUARDIAN));
        List<Integer> coxIconIDs = new ArrayList<>(Arrays.asList(ItemID.TEKTINY, ItemID.DRAGON_WARHAMMER, ItemID.KINDLING, ItemID.DYNAMITE, ItemID.VANGUARD, ItemID.LOCKPICK, ItemID.VESPINA, ItemID.PHOENIX_NECKLACE, ItemID.DRAGON_PICKAXE, ItemID.VASA_MINIRIO, ItemID.SALVE_AMULET_E, ItemID.PUPPADILE, ItemID.OLMLET));

        int index = 0;
        tobTabSubpanel.addTab("Overall", overallPanels.get(0));
        toaTabSubpanel.addTab("Overall", overallPanels.get(1));
        coxTabSubpanel.addTab("Overall", overallPanels.get(2));
        for(RaidRoom room : Arrays.stream(RaidRoom.values()).filter(RaidRoom::isTOB).toArray(RaidRoom[]::new))
        {
            StatisticTab statisticTab = new StatisticTab(tobData, room);
            tobTabSubpanel.addTab("", statisticTab);
            tobTabSubpanel.setIconAt(index+1, new ImageIcon(itemManager.getImage(tobIconIDs.get(index))));
            tobTabs.add(statisticTab);
            index++;
        }
        index = 0;
        for(RaidRoom room : Arrays.stream(RaidRoom.values()).filter(RaidRoom::isTOA).toArray(RaidRoom[]::new))
        {
            StatisticTab statisticTab = new StatisticTab(toaData, room);
            toaTabSubpanel.addTab("", statisticTab);
            toaTabSubpanel.setIconAt(index+1, new ImageIcon(itemManager.getImage(toaIconIDs.get(index))));
            toaTabs.add(statisticTab);
            index++;
        }

        index = 0;
        for(RaidRoom room : Arrays.stream(RaidRoom.values()).filter(RaidRoom::isCOX).toArray(RaidRoom[]::new))
        {
            StatisticTab statisticTab = new StatisticTab(coxData, room);
            coxTabSubpanel.addTab("", statisticTab);
            coxTabSubpanel.setIconAt(index+1, new ImageIcon(itemManager.getImage(coxIconIDs.get(index))));
            coxTabs.add(statisticTab);
            index++;
        }

        tabbedPane.setMinimumSize(new Dimension(100, 300));

        JPanel additionalFiltersPanel = getTitledPanel("Quick Filters");
        additionalFiltersPanel.setLayout(new BorderLayout());
        additionalFiltersPanel.setMinimumSize(new Dimension(200, 300));
        additionalFiltersPanel.setPreferredSize(new Dimension(200, 300));

        filterSpectateOnly = getActionListenCheckBox("Spectate Only", al-> updateTable());
        filterInRaidOnly = getActionListenCheckBox("In Raid Only", al-> updateTable());
        filterCompletionOnly = getActionListenCheckBox("Completion Only", al-> updateTable());
        filterWipeResetOnly = getActionListenCheckBox("Wipe/Reset Only", al-> updateTable());
        filterComboBoxScale = UISwingUtility.getActionListenCheckBox(new String[]{"Solo", "Duo", "Trio", "4-Man", "5-Man"}, al -> updateTable());
        filterCheckBoxScale = getActionListenCheckBox("Scale", al -> updateTable());
        filterTodayOnly = getActionListenCheckBox("Today Only", al -> updateTable());
        filterPartyOnly = getActionListenCheckBox("Party Only", al -> updateTable());
        filterPartialData = getActionListenCheckBox("Filter Partial Raids", al -> updateTable());
        filterPartialOnly = getActionListenCheckBox("Filter Partial Rooms", al -> updateTable());
        filterPartialData.setToolTipText("Removes data sets that have any rooms that were partially completed");
        filterNormalOnly = getActionListenCheckBox("Normal Mode Only", true, al-> updateTable());

        JPanel scaleContainer = new JPanel();
        scaleContainer.setLayout(new BoxLayout(scaleContainer, BoxLayout.X_AXIS));
        scaleContainer.setPreferredSize(new Dimension(150, 25));

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

        tabbedPane.addTab("", tobTabSubpanel);
        tabbedPane.addTab("", toaTabSubpanel);
        tabbedPane.addTab("", coxTabSubpanel);

        tabbedPane.setIconAt(0, new ImageIcon(itemManager.getImage(ItemID.SCYTHE_OF_VITUR)));
        tabbedPane.setIconAt(1, new ImageIcon(itemManager.getImage(ItemID.TUMEKENS_SHADOW)));
        tabbedPane.setIconAt(2, new ImageIcon(itemManager.getImage(ItemID.TWISTED_BOW)));

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

        JPanel rightTopContainer = getTitledPanel("Advanced Filters");
        rightTopContainer.setLayout(new GridLayout(3, 2));

        JPanel filterTimePanel = getTitledPanel("Filter by room or split time");
        filterTimePanel.setLayout(new BoxLayout(filterTimePanel, BoxLayout.Y_AXIS));

        JPanel filterPlayerPanel = getTitledPanel("Filter by players in a raid");
        filterPlayerPanel.setLayout(new GridLayout(2, 2));

        JPanel filterDatePanel = getTitledPanel("Filter by date");
        filterDatePanel.setLayout(new GridLayout(2, 2));

        JPanel filterOtherIntPanel = getTitledPanel("Filter by other condition (int)");
        filterOtherIntPanel.setLayout(new GridLayout(2, 2));

        JPanel filterOtherBoolPanel = getTitledPanel("Filter by other condition (bool)");
        filterOtherBoolPanel.setLayout(new GridLayout(2, 2));


        timeFilterChoice = new JComboBox<>(DataPoint.getTimeNames());

        String[] timeOperatorChoices =
                {
                        "=",
                        "<",
                        ">",
                        "<=",
                        ">="
                };

        timeFilterOperator = new JComboBox<>(timeOperatorChoices);


        timeFilterValue = new JTextField();

        JButton timeFilterAdd = new JButton("Add");
        timeFilterAdd.addActionListener(
                al ->
                {
                    String time = timeFilterValue.getText();
                    if (time.isEmpty())
                    {
                        return;
                    }
                    String timeStr = Objects.requireNonNull(timeFilterChoice.getSelectedItem()) + " " + Objects.requireNonNull(timeFilterOperator.getSelectedItem()) + " " + time;
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

        playerFilterOperator = new JComboBox<>(playersQualifier);
        playerFilterValue = new JTextField();
        JButton playerFilterAdd = new JButton("Add");
        playerFilterValue.setMaximumSize(new Dimension(Integer.MAX_VALUE, playerFilterAdd.getPreferredSize().height));
        playerFilterValue.setPreferredSize(new Dimension(75, playerFilterAdd.getPreferredSize().height));
        playerFilterOperator.setMaximumSize(new Dimension(Integer.MAX_VALUE, playerFilterAdd.getPreferredSize().height));
        playerFilterAdd.setPreferredSize(new Dimension(55, playerFilterAdd.getPreferredSize().height));

        playerFilterAdd.addActionListener(
                al ->
                {
                    String filterStr = "Raid " + playerFilterOperator.getSelectedItem() + " " + playerFilterValue.getText();
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

        dateFilterOperator = new JComboBox<>(choicesDate);
        JTextField dateFilterValue = new JTextField();
        dateFilterOperator.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateFilterValue.getPreferredSize().height));


        JButton dateFilterAdd = new JButton("Add");
        dateFilterAdd.addActionListener(
                al ->
                {
                    try
                    {
                        String dateString = dateTextField.getText();
                        String[] datePartial = dateString.split("/");
                        int year = Integer.parseInt(datePartial[0]);
                        int month = Integer.parseInt(datePartial[1]);
                        int day = Integer.parseInt(datePartial[2]);
                        Date date = new GregorianCalendar(year, month-1, day).getTime();
                        String filterStr = "Raid was " + dateFilterOperator.getSelectedItem() + " " + date;
                        activeFilters.add(new ImplicitFilter(new FilterDate(date, dateFilterOperator.getSelectedIndex(), filterStr)));
                    } catch (Exception ignored)
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
        dateBottomRow.add(Box.createRigidArea(new Dimension(5, 5)));
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


        otherIntFilterChoice = new JComboBox<>(DataPoint.getOtherIntNames());
        otherIntFilterOperator = new JComboBox<>(otherIntOperatorChoices);
        otherIntFilterValue = new JTextField();

        JButton otherIntAdd = new JButton("Add");
        otherIntAdd.addActionListener(
                al ->
                {
                    String filterStr = Objects.requireNonNull(otherIntFilterChoice.getSelectedItem()) + " " + Objects.requireNonNull(otherIntFilterOperator.getSelectedItem()) + " " + otherIntFilterValue.getText() + " ";
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
                "Verzik  wipe",
                "Maiden Scuffed",
        };

        String[] qualifierOtherBool = {
                "True",
                "False"
        };

        otherBoolFilterChoice = new JComboBox<>(choicesOtherBool);
        otherBoolFilterOperator = new JComboBox<>(qualifierOtherBool);

        JButton otherBoolAdd = new JButton("Add Filter");
        otherBoolAdd.addActionListener(
                al ->
                {
                    String filterStr = Objects.requireNonNull(otherBoolFilterChoice.getSelectedItem()) + " " + otherBoolFilterOperator.getSelectedItem();
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

        JPanel filterOptions = getTitledPanel("Filter Options");
        rightTopContainer.setPreferredSize(new Dimension(400, 250));
        rightTopContainer.add(filterTimePanel);
        rightTopContainer.add(filterOtherIntPanel);
        rightTopContainer.add(filterPlayerPanel);
        rightTopContainer.add(filterOtherBoolPanel);
        rightTopContainer.add(filterDatePanel);


        JPanel rightBottomContainer = getTitledPanel("Active Filters");
        rightBottomContainer.setPreferredSize(new Dimension(400, 200));

        filterTableContainer = new JPanel();

        JPopupMenu raidPopup = new JPopupMenu();

        JMenuItem analyzeSessions = new JMenuItem("Analyze Sessions");
        analyzeSessions.setBackground(Color.BLACK);
        analyzeSessions.setOpaque(true);
        analyzeSessions.addActionListener(e ->
        {
            updateAliases();
            List<Raid> rows = new ArrayList<>();
            int[] toRemove = table.getSelectedRows();
            for (int j : toRemove)
            {
                rows.add(currentData.get(Integer.parseInt(table.getModel().getValueAt(j, 0).toString())));
            }
            Map<Integer, Map<String, List<Raid>>> sessions = new LinkedHashMap<>();
            for (Raid data12 : rows)
            {

                if (!sessions.containsKey(data12.getPlayers().size()))
                {
                    Map<String, List<Raid>> scale = new LinkedHashMap<>();
                    ArrayList<Raid> list = new ArrayList<>();
                    list.add(data12);
                    scale.put(getPlayerList(aliases, data12.getPlayers()), list);
                    sessions.put(data12.getPlayers().size(), scale);
                } else
                {
                    if (!sessions.get(data12.getPlayers().size()).containsKey(getPlayerList(aliases, data12.getPlayers())))
                    {
                        List<Raid> list = new ArrayList<>();
                        list.add(data12);
                        sessions.get(data12.getPlayers().size()).put(getPlayerList(aliases, data12.getPlayers()), list);
                    } else
                    {
                        sessions.get(data12.getPlayers().size()).get(getPlayerList(aliases, data12.getPlayers())).add(data12);
                    }
                }

            }
            List<List<String>> labelSets = new ArrayList<>();
            Map<Integer, List<List<Raid>>> dataSets = new LinkedHashMap<>();
            for (Integer scale : sessions.keySet())
            {
                List<List<Raid>> scaleData = new ArrayList<>();
                List<String> labels = new ArrayList<>();
                for (String playerList : sessions.get(scale).keySet())
                {
                    scaleData.add(sessions.get(scale).get(playerList));
                    labels.add(playerList);
                }
                dataSets.put(scale, scaleData);
                labelSets.add(labels);
            }
            ComparisonViewFrame graphView = new ComparisonViewFrame(dataSets, labelSets, config, itemManager, clientThread, configManager);
            graphView.open();
        });


        JMenuItem addToComparison = new JMenuItem("Add set to comparison");
        addToComparison.setBackground(Color.BLACK);
        addToComparison.setOpaque(true);

        JMenuItem viewGraphs = new JMenuItem("View Graphs");
        viewGraphs.setBackground(Color.BLACK);
        viewGraphs.setOpaque(true);

        JMenuItem viewCharts = new JMenuItem("View Charts");
        viewCharts.setBackground(Color.BLACK);
        viewCharts.setOpaque(true);

        viewCharts.addActionListener(e ->
        {
            int[] toRemove = table.getSelectedRows();
            if(toRemove.length == 0)
            {
                JOptionPane.showMessageDialog(this, "You must select at least one raid to view charts", "Chart Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Raid raidData = null;
            for (int i = 0; i < toRemove.length; i++)
            {
                raidData = currentData.get(Integer.parseInt(table.getModel().getValueAt(toRemove[i], 0).toString()));
            }
            if (raidData != null)
            {
                ChartFrame roomCharts = new ChartFrame(raidData, config, itemManager, clientThread, configManager);
                roomCharts.open();
            }
        });

        viewGraphs.addActionListener(e ->
        {
            List<String> labels = new ArrayList<>();
            List<Raid> rows = new ArrayList<>();
            int[] toRemove = table.getSelectedRows();
            for (int i = 0; i < toRemove.length; i++)
            {
                rows.add(currentData.get(Integer.parseInt(table.getModel().getValueAt(toRemove[i], 0).toString())));
            }
            if (rows.isEmpty())
            {
                new NoDataPopUp().open();
            } else
            {
                labels.add("");
                List<List<Raid>> data1 = new ArrayList<>();
                data1.add(rows);
                ComparisonViewFrame graphView = new ComparisonViewFrame(data1, labels);
                graphView.open();
            }
        });

        addToComparison.addActionListener(e ->
        {
            List<Raid> rows = new ArrayList<>();
            int[] toRemove = table.getSelectedRows();
            for (int i = 0; i < toRemove.length; i++)
            {
                rows.add(currentData.get(Integer.parseInt(table.getModel().getValueAt(toRemove[i], 0).toString())));
            }
            comparisons.add(rows);
            updateComparisonTable();
        });
        JMenuItem exportRaids = new JMenuItem("Export Selected Raids to CSV");
        exportRaids.setBackground(Color.BLACK);
        exportRaids.setOpaque(true);
        exportRaids.addActionListener(e ->
        {
            ArrayList<Raid> rows = new ArrayList<>();
            int[] toRemove = table.getSelectedRows();
            for (int i = 0; i < toRemove.length; i++)
            {
                rows.add(currentData.get(Integer.parseInt(table.getModel().getValueAt(toRemove[i], 0).toString())));
            }
            new SaveRaids(rows).open();
        });

        JMenuItem filterRaids = new JMenuItem("Filter Selected Raids");
        filterRaids.setBackground(Color.BLACK);
        filterRaids.setOpaque(true);
        filterRaids.addActionListener(e ->
        {
            int[] toRemove = table.getSelectedRows();
            for (int j : toRemove)
            {
                filteredIndices.add(Integer.parseInt(table.getModel().getValueAt(j, 0).toString()));
            }

            updateTable();
        });

        JMenu filterOptionsSubMenu = new JMenu("Filter Raids");
        filterOptionsSubMenu.setOpaque(true);
        filterOptionsSubMenu.setBackground(Color.BLACK);

        JMenuItem undoFilterRaids = new JMenuItem("Clear Filtered Raids");
        undoFilterRaids.setBackground(Color.BLACK);
        undoFilterRaids.setOpaque(true);
        undoFilterRaids.addActionListener(e ->
        {
                filteredIndices.clear();
                updateTable();
        });

        JMenuItem filterExclusiveRaids = new JMenuItem("Filter All Except Selected Raids");
        filterExclusiveRaids.setBackground(Color.BLACK);
        filterExclusiveRaids.setOpaque(true);
        filterExclusiveRaids.addActionListener(e ->
        {
            int[] toKeep = table.getSelectedRows();
            for (int i = 0; i < table.getRowCount(); i++)
            {
                boolean found = false;
                for (int k : toKeep)
                {
                    if (i == k)
                    {
                        found = true;
                        break;
                    }
                }
                if (!found)
                {
                    filteredIndices.add(Integer.parseInt(table.getModel().getValueAt(i, 0).toString()));
                }
            }

            updateTable();
        });
        JMenuItem analyzeCrabs = new JMenuItem("Analyze selection crab leaks");
        analyzeCrabs.setOpaque(true);
        analyzeCrabs.setBackground(Color.BLACK);
        analyzeCrabs.addActionListener(e ->
        {
            ArrayList<ArrayList<StringInt>> crabData = new ArrayList<>();
            int[] toRemove = table.getSelectedRows();
            for (int i = 0; i < toRemove.length; i++)
            {
                Raid row = currentData.get(Integer.parseInt(table.getModel().getValueAt(toRemove[i], 0).toString()));
                /*
                if (row instanceof SimpleTOBData)
                {
                    crabData.add(((SimpleTOBData) row).maidenCrabs);
                }

                 */
            }
            new CrabLeakInfo(crabData); //todo "s" prefix
        });

        raidPopup.add(analyzeCrabs);
        raidPopup.add(exportRaids);
        filterOptionsSubMenu.add(filterRaids);
        filterOptionsSubMenu.add(filterExclusiveRaids);
        filterOptionsSubMenu.add(undoFilterRaids);
        raidPopup.add(addToComparison);
        raidPopup.add(filterOptionsSubMenu);
        raidPopup.add(analyzeSessions);
        raidPopup.add(viewCharts);
        raidPopup.add(viewGraphs);
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
                al ->
                {
                    ArrayList<String> quickFiltersState = new ArrayList<>();
                    quickFiltersState.add("QF-Spectate Only:" + filterSpectateOnly.isSelected());
                    quickFiltersState.add("QF-In Raid Only:" + filterInRaidOnly.isSelected());
                    quickFiltersState.add("QF-Completion Only:" + filterCompletionOnly.isSelected());
                    quickFiltersState.add("QF-Wipe/Reset Only:" + filterWipeResetOnly.isSelected());
                    quickFiltersState.add("QF-Today Only:" + filterTodayOnly.isSelected());
                    quickFiltersState.add("QF-Party Only:" + filterPartyOnly.isSelected());
                    quickFiltersState.add("QF-Partial Raids:" + filterPartialData.isSelected());
                    quickFiltersState.add("QF-Partial Rooms:" + filterPartialOnly.isSelected());
                    quickFiltersState.add("QF-Normal Mode Only:" + filterNormalOnly.isSelected());
                    quickFiltersState.add("QF-Scale:" + filterCheckBoxScale.isSelected() + ":" + filterComboBoxScale.getSelectedIndex());
                    quickFiltersState.add("QF-View Raid By:" + customColumnComboBox.getItemAt(customColumnComboBox.getSelectedIndex()));
                    //quickFiltersState.add("QF-Table Sort By:" + sortOptionsBox.getItemAt(sortOptionsBox.getSelectedIndex())); .//todo
                    //quickFiltersState.add("QF-Table Sort:" + sortOrderBox.getItemAt(sortOrderBox.getSelectedIndex())); //todo
                    SaveFilter saveFilter = new SaveFilter(activeFilters, quickFiltersState);
                    saveFilter.open();
                });
        JButton loadFiltersButton = new JButton("Load");
        loadFiltersButton.addActionListener(
                al ->
                        new LoadFilter(this).open());
        JButton clearFiltersButton = new JButton("Clear");
        clearFiltersButton.addActionListener(
                al ->
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

        JPanel rightBottomBottomContainer = getTitledPanel("Comparison Options");
        rightBottomBottomContainer.setPreferredSize(new Dimension(400, 250));

        comparisonTable = new JTable();
        JScrollPane comparisonTableScroll = new JScrollPane(comparisonTable);
        comparisonTable.setPreferredSize(new Dimension(380, 170));
        comparisonTableScroll.setPreferredSize(new Dimension(380, 155));
        updateComparisonTable();

        JPanel rightBottomMostContainer = getTitledPanel("Alias Options");

        aliasText.setToolTipText("This applies to the tab names when you use the analyze sessions features. Syntax- Name to be displayed:oldname1,oldname2,oldname3");
        aliasText.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                try
                {
                    if (!writing)
                        DataWriter.writeAliasFile(e.getDocument().getText(0, e.getDocument().getLength()).replaceAll("\n", System.lineSeparator()));
                } catch (BadLocationException ignored)
                {
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                try
                {
                    if (!writing)
                        DataWriter.writeAliasFile(e.getDocument().getText(0, e.getDocument().getLength()).replaceAll("\n", System.lineSeparator()));
                } catch (BadLocationException ignored)
                {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                try
                {
                    if (!writing)
                        DataWriter.writeAliasFile(e.getDocument().getText(0, e.getDocument().getLength()).replaceAll("\n", System.lineSeparator()));
                } catch (BadLocationException ignored)
                {
                }
            }
        });

        updateAliases();

        JScrollPane aliasScrollPane = new JScrollPane(aliasText);
        aliasScrollPane.setPreferredSize(new Dimension(380, 70));
        rightBottomMostContainer.add(aliasScrollPane);

        rightBottomBottomContainer.add(comparisonTableScroll);
        JButton viewComparisonsButton = new JButton("View Comparisons");
        viewComparisonsButton.addActionListener(al ->
        {
            if (comparisonTable.getModel().getRowCount() == 0)
            {
                new NoDataPopUp().open();
            } else
            {
                ArrayList<String> labels = new ArrayList<>();
                for (int i = 0; i < comparisonTable.getModel().getRowCount(); i++)
                {
                    labels.add(comparisonTable.getModel().getValueAt(i, 1).toString());
                }
                ComparisonViewFrame graphView = new ComparisonViewFrame(comparisons, labels);
                graphView.open();
            }
        });
        rightBottomBottomContainer.add(viewComparisonsButton);

        rightContainer.add(rightTopContainer);
        rightContainer.add(rightBottomContainer);
        rightContainer.add(rightBottomBottomContainer);
        rightContainer.add(rightBottomMostContainer);
        splitLeftRight.add(rightContainer);

        setupTableHeaderListener();

        add(splitLeftRight);
        pack();
        built = true;
    }


    public String[] columnHeaderNames = new String[]{"Date", "Raid", "Time", "Scale", "Status", "Players", "Spectate", "View"};
    public ArrayList<JCheckBoxMenuItem> columnHeaders;

    private void getUpdatedPopupMenu(String newItem)
    {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(newItem);
        item.setOpaque(true);
        item.setBackground(Color.BLACK);
        item.setState(true);
        item.addActionListener(al ->
                updateTable());
        columnHeaders.add(item);
        table.getTableHeader().setComponentPopupMenu(getCustomColumnPopUpMenu());
        updateTable();
    }

    private JMenuItem getMenuItem(String text)
    {
        JMenuItem item = new JMenuItem(text);
        item.setOpaque(true);
        item.setBackground(Color.BLACK);
        return item;
    }

    private JMenu getMenu(String text)
    {
        JMenu menu = new JMenu(text);
        menu.setOpaque(true);
        menu.setBackground(Color.BLACK);
        return menu;
    }

    private JCheckBoxMenuItem getCheckBoxMenuItem(String name, String state)
    {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(name);
        if(state.isEmpty())
        {
            if (!name.equals("Time"))
            {
                item.setState(true);
            }
        }
        else
        {
            item.setState(state.equals("true"));
        }
        item.setOpaque(true);
        item.setBackground(Color.BLACK);
        item.addActionListener(al ->
        {
            if (built)
            {
                updateTable();
            }
        });
        return item;
    }

    private JCheckBoxMenuItem getCheckBoxMenuItem(String name)
    {
        return getCheckBoxMenuItem(name, "");
    }

    private void switchTo(Integer preset)
    {
        String[] readColumns = DataWriter.getPresetColumns(preset);
        if(readColumns.length == 0)
        {
            readColumns = columnHeaderNames;
        }
        columnHeaders.clear();
        for(String column : readColumns)
        {
            String[] split = column.split("~");
            if(split.length == 2)
            {
                JCheckBoxMenuItem option = getCheckBoxMenuItem(split[0], split[1]);
                columnHeaders.add(option);
            }
            else if(split.length == 1)
            {
                JCheckBoxMenuItem option = getCheckBoxMenuItem(split[0]);
                columnHeaders.add(option);
            }
        }
        table.getTableHeader().setComponentPopupMenu(getCustomColumnPopUpMenu());
        updateTable();
    }

    private List<Integer> activeMenuPresets = new ArrayList<>();

    private JMenuItem getSwitchToMenuItem(String name)
    {
        JMenuItem switchTo = getMenuItem(name);
        switchTo.addActionListener(al->
        {
            if(name.equals("Default"))
            {
                switchTo(0);
            }
            else
            {
                int presetNumber = -1;
                try
                {
                    presetNumber = Integer.parseInt(name.substring(name.length()-1));
                    switchTo(presetNumber);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        return switchTo;
    }

    private void addPreset(JMenu deletePresets)
    {
        for(Integer i : activeMenuPresets)
        {
            if(i != 0)
            {
                JMenuItem deleteItem = getMenuItem("Preset " + i);
                deleteItem.addActionListener(al->
                {
                    deletePreset(i);
                });
                deletePresets.add(deleteItem);
            }
        }
    }

    private void addSwitchTos(JMenu switchToMenu)
    {
        for(Integer i : activeMenuPresets)
        {
            if(i != 0)
            {
                JMenuItem switchToItem = getSwitchToMenuItem("Preset " + i);
                switchToMenu.add(switchToItem);
            }
        }
    }

    private void deletePreset(Integer preset)
    {
        DataWriter.removePreset(preset);
        activeMenuPresets.removeIf(o->o.equals(preset));
        table.getTableHeader().setComponentPopupMenu(getCustomColumnPopUpMenu());
    }

    private Integer getNextFreePreset() //20 presets max
    {
        for(int i = 0; i < 20; i++)
        {
            if(!activeMenuPresets.contains(i))
            {
                return i;
            }
        }
        return -1;
    }


    private JPopupMenu getCustomColumnPopUpMenu()
    {
        JPopupMenu baseMenu = new JPopupMenu();

        for (JCheckBoxMenuItem item : columnHeaders)
        {
            baseMenu.add(item);
        }

        JMenu addCustom = getMenu("Add Column");

        JMenuItem resetCustom = getMenuItem("Reset Custom Columns");
        resetCustom.addActionListener(al ->
        {
            columnHeaders.clear();
            for (String column : columnHeaderNames)
            {
                columnHeaders.add(getCheckBoxMenuItem(column));
            }
            table.getTableHeader().setComponentPopupMenu(getCustomColumnPopUpMenu());
            updateTable();
        });

        JMenu switchTo = getMenu("Switch To...");
        JMenuItem defaultMenuItem = getSwitchToMenuItem("Default");
        if(!activeMenuPresets.contains(0))
        {
            activeMenuPresets.add(0);
        }
        for(Integer i : DataWriter.getPresetCount())
        {
            if(!activeMenuPresets.contains(i))
            {
                activeMenuPresets.add(i);
            }
        }
        switchTo.add(defaultMenuItem);
        addSwitchTos(switchTo);
        Integer nextFree = getNextFreePreset();
        JMenuItem saveToPreset;
        if(nextFree == -1)
        {
            saveToPreset = getMenuItem("<Cannot Save Any More Presets>");
            saveToPreset.setEnabled(false);
        }
        else
        {
            saveToPreset = getMenuItem("Save Current To Preset " + nextFree);
            saveToPreset.addActionListener(al ->
            {
                activeMenuPresets.add(nextFree);
                List<String> columnNames = new ArrayList<>();
                for(JCheckBoxMenuItem columnCheckBox : columnHeaders)
                {
                    columnNames.add(columnCheckBox.getText() + "~" + columnCheckBox.getState());
                }
                DataWriter.writePresetColumn(nextFree, columnNames.toArray(new String[0]));
                table.getTableHeader().setComponentPopupMenu(getCustomColumnPopUpMenu());
            });
        }

        JMenu deletePreset = getMenu("Delete Preset...");
        addPreset(deletePreset);

        for (RaidType raidType : RaidType.values())
        {
                JMenu raidLevelMenu = getMenu(raidType.name);
                if(raidType.equals(RaidType.UNASSIGNED))
                {
                    for(DataPoint.MenuCategories menuCategories : DataPoint.MenuCategories.values())
                    {
                        JMenu typeLevelMenu = getMenu(menuCategories.name);
                        for(RaidRoom room : RaidRoom.getRaidRoomsForRaidType(raidType))
                        {
                            for(String itemName : DataPoint.getMenuNamesByType(room, menuCategories))
                            {
                                typeLevelMenu.add(createMenuItemTableHeader(itemName));
                            }
                        }
                        raidLevelMenu.add(typeLevelMenu);
                    }
                }
                else
                {
                    for (RaidRoom room : RaidRoom.getRaidRoomsForRaidType(raidType))
                    {
                        JMenu roomLevelMenu = getMenu(room.name);
                        for(DataPoint.MenuCategories menuCategories : DataPoint.MenuCategories.values())
                        {
                            JMenu typeLevelMenu = getMenu(menuCategories.name);
                            for(String itemName : DataPoint.getMenuNamesByType(room, menuCategories))
                            {
                                typeLevelMenu.add(createMenuItemTableHeader(itemName));
                            }
                            roomLevelMenu.add(typeLevelMenu);
                        }
                        raidLevelMenu.add(roomLevelMenu);
                    }
                }
                addCustom.add(raidLevelMenu);
        }
        baseMenu.add(addCustom);
        baseMenu.add(resetCustom);
        baseMenu.add(switchTo);
        baseMenu.add(saveToPreset);
        baseMenu.add(deletePreset);
        return baseMenu;
    }

    private int presetCount;

    private int getTimeFromString(String text)
    {
        int ticks = 0;
        String sub = text;
        if (sub.contains(":"))
        {
            ticks += 100 * Integer.parseInt(sub.substring(0, sub.indexOf(":")));
            sub = text.substring(sub.indexOf(":") + 1);
        }
        ticks += (int) ((Double.parseDouble(sub) / 0.6));
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

        List<String> oldNames = new ArrayList<>();
        for(int i = 0; i < comparisonTable.getRowCount(); i++)
        {
            oldNames.add(comparisonTable.getValueAt(i, 1).toString());
        }
        int index = 0;
        for (List<Raid> comparison : comparisons)
        {
            String comparisonName = "Set " + index;
            if(index < oldNames.size())
            {
                comparisonName = oldNames.get(index);
            }
            Object[] row = {comparison.size() + " raids averaging: " + RoomUtil.time(StatisticGatherer.getOverallTimeAverage(comparison)), comparisonName, "Remove"};
            tableData.add(row);
            index++;
        }

        Object[][] tableObject = new Object[tableData.size()][2];
        int count = 0;
        for (Object[] row : tableData)
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

    public void setFilterState(String state)
    {
        try
        {
            if (state.contains(":"))
            {
                String[] data = state.split(":");
                if (data.length > 1)
                {
                    switch (data[0])
                    {
                        case "Spectate Only":
                            filterSpectateOnly.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "In Raid Only":
                            filterInRaidOnly.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Completion Only":
                            filterCompletionOnly.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Wipe/Reset Only":
                            filterWipeResetOnly.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Today Only":
                            filterTodayOnly.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Party Only":
                            filterPartyOnly.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Partial Raids":
                            filterPartialOnly.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Partial Rooms":
                            filterPartialData.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Normal Mode Only":
                            filterNormalOnly.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Scale":
                            if (data.length > 2)
                            {
                                filterCheckBoxScale.setSelected(Boolean.parseBoolean(data[1]));
                                filterComboBoxScale.setSelectedIndex(Integer.parseInt(data[2]));
                            }
                            break;
                        case "View Raid By":
                            if (!Objects.equals(data[1], "null"))
                            {
                                customColumnComboBox.setSelectedItem(data[1]);
                            } else
                            {
                                customColumnComboBox.setSelectedItem("Challenge Time");
                            }
                            break;
                        case "Table Sort By":
                            //sortOptionsBox.setSelectedItem(data[1]); //todo
                            break;
                        case "Table Sort":
                           // sortOrderBox.setSelectedItem(data[1]); //todo
                            break;
                    }
                }
            }
        } catch (Exception e)
        {
            log.info("Failed to set filter state: " + state);
        }
    }

    public void updateFilterTable()
    {
        String[] columnNames = {"Filter Descriptions", ""};
        ArrayList<Object[]> tableData = new ArrayList<>();

        for (ImplicitFilter filter : activeFilters)
        {
            Object[] row = {filter.getFilterDescription(), "Remove"};
            tableData.add(row);
        }

        Object[][] tableObject = new Object[tableData.size()][2];
        int count = 0;
        for (Object[] row : tableData)
        {
            tableObject[count] = row;
            count++;
        }
        filterTable.setModel(new DefaultTableModel(tableObject, columnNames));
        filterTable.setDefaultRenderer(Object.class, new StripedTableRowCellRenderer());
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
