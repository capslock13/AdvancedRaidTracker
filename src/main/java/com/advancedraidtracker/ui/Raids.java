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
import com.advancedraidtracker.utility.datautility.datapoints.col.Colo;
import com.advancedraidtracker.utility.datautility.datapoints.inf.Inf;
import com.advancedraidtracker.utility.datautility.datapoints.toa.Toa;
import com.advancedraidtracker.utility.datautility.datapoints.tob.Tob;
import com.advancedraidtracker.utility.wrappers.StringInt;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.List;

import static com.advancedraidtracker.rooms.inf.InfernoHandler.roomMap;
import static com.advancedraidtracker.utility.UISwingUtility.*;

@Slf4j
public class Raids extends BaseFrame implements UpdateableWindow
{
    private final ArrayList<Integer> filteredIndices;
    private JTable comparisonTable;
    private final List<List<Raid>> comparisons;

    private JTabbedPane tabbedPane = null;
    public ArrayList<ImplicitFilter> activeFilters;


    List<Map<String, JLabel>> averageLabels2 = new ArrayList<>();
    List<Map<String, JLabel>> medianLabels2 = new ArrayList<>();
    List<Map<String, JLabel>> minLabels2 = new ArrayList<>();
    List<Map<String, JLabel>> maxLabels2 = new ArrayList<>();

    private final List<Map<String, List<String>>> aliases;

    private JTextArea aliasText;

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
    JCheckBox filterFavoritesOnly;
    JCheckBox filterIncludeNormal;
    JCheckBox filterIncludeHard;
    JCheckBox filterIncludeEntry;
    JCheckBox filterIncludeTOB;
    JCheckBox filterIncludeCOX;
    JCheckBox filterIncludeTOA;
    JCheckBox filterIncludeInferno;
    JCheckBox filterIncludeColosseum;

    JTable table;
    JPanel container;
    private JPanel filterTableContainer;
    public List<Raid> currentData = new ArrayList<>();
    public List<Raid> inactiveData = new ArrayList<>();

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
    private final SpriteManager spriteManager;

    public String[] rooms = {"Maiden", "Bloat", "Nylocas", "Sotetseg", "Xarpus", "Verzik", "Challenge"};
    private final ConfigManager configManager;
    private final ChartFrame chartFrame;
    private final List<Long> favorites;

    private final Set<String> shouldNotBeSortedByTicks = new HashSet<>(Arrays.asList("", "Scale", "Raid", "Status", "Players", "Spectate", "Date"));

    public Raids(AdvancedRaidTrackerConfig config, ItemManager itemManager, ClientThread clientThread, ConfigManager configManager, SpriteManager spriteManager)
    {
        this.clientThread = clientThread;
        this.itemManager = itemManager;
        this.configManager = configManager;
        this.spriteManager = spriteManager;
        this.chartFrame = new ChartFrame(config, itemManager, clientThread, configManager, spriteManager);

        columnHeaders = new ArrayList<>();
        favorites = DataReader.getFavorites();
        for (String s : columnHeaderNames)
        {
            columnHeaders.add(getCheckBoxMenuItem(s));
        }
        aliases = new ArrayList<>();
        filteredIndices = new ArrayList<>();
        comparisons = new ArrayList<>();
        activeFilters = new ArrayList<>();
        this.config = config;
        this.setPreferredSize(new Dimension(1225, 820));

        aliasText = getThemedTextArea();
        tabbedPane = getThemedTabbedPane();
        int index = 0;
        for (RaidType raidType : RaidType.values())
        {
            averageLabels2.add(new LinkedHashMap<>());
            medianLabels2.add(new LinkedHashMap<>());
            maxLabels2.add(new LinkedHashMap<>());
            minLabels2.add(new LinkedHashMap<>());

            for (RaidRoom room : RaidRoom.getRaidRoomsForRaidType(raidType))
            {
                String labelName = room.name;
                if (labelName.contains("-") && labelName.contains("Wave"))
                {
                    String[] split = labelName.split(" ");
                    if (split.length == 2)
                    {
                        labelName = split[1];
                    }
                }
                averageLabels2.get(index).put(labelName, getThemedLabel("", SwingConstants.RIGHT));
                medianLabels2.get(index).put(labelName, getThemedLabel("", SwingConstants.RIGHT));
                maxLabels2.get(index).put(labelName, getThemedLabel("", SwingConstants.RIGHT));
                minLabels2.get(index).put(labelName, getThemedLabel("", SwingConstants.RIGHT));
            }
            index++;
        }
        customColumnComboBox.setEnabled(true);
        for (Component c : customColumnComboBox.getComponents())
        {
            //we are using the combobox for its appearance only, we do not want the real combobox dropdown to pop up when clicked because we are using
            //a jpopupmenu to display the content of the dropdown. Even if we set it not visible it will flash for a frame before going away.
            if (c instanceof AbstractButton)
            {
                c.setEnabled(false);
            }
        }
        customColumnComboBox.addItem("Challenge Time");

        timeFollowsTab = getThemedCheckBox("Time Follows Tab");
        timeFollowsTab.setSelected(true);

        JPopupMenu tstMenu = getCustomColumnPopUpMenu();

        table = getThemedTable();
        table.getTableHeader().setComponentPopupMenu(tstMenu);

        createFrame();
    }

    public JComboBox<String> customColumnComboBox = getThemedComboBox();

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


    public void updateTable()
    {
        for(MouseListener listener : table.getTableHeader().getMouseListeners())
        {
            if(listener instanceof DynamicTableHeaderRenderer.MouseEventReposter)
            {
                table.getTableHeader().removeMouseListener(listener);
            }
        }
        int completions = 0;
        int attempts = 0;
        int tobAttempts = 0;
        int toaAttempts = 0;
        int coxAttempts = 0;
        int infAttempts = 0;
        int colAttempts = 0;
        int tobCompletions = 0;
        int toaCompletions = 0;
        int coxCompletions = 0;
        int infCompletions = 0;
        int colCompletions = 0;

        List<Raid> tableData = new ArrayList<>();
        for (Raid data : currentData)
        {
            boolean shouldDataBeIncluded = true;
            if (filterSpectateOnly.isSelected())
            {
                if (!data.isSpectated())
                {
                    shouldDataBeIncluded = false;
                }
            }
            if (filterInRaidOnly.isSelected())
            {
                if (data.isSpectated())
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
            if (!filterIncludeTOB.isSelected())
            {
                if (data instanceof Tob)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if (!filterIncludeTOA.isSelected())
            {
                if (data instanceof Toa)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if (!filterIncludeCOX.isSelected())
            {
                if (data instanceof Cox)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if (!filterIncludeColosseum.isSelected())
            {
                if (data instanceof Colo)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if (!filterIncludeInferno.isSelected())
            {
                if (data instanceof Inf)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if (!filterIncludeNormal.isSelected())
            {
                if (data instanceof Tob)
                {
                    if (!data.isStoryMode && !data.isHardMode)
                    {
                        shouldDataBeIncluded = false;
                    }
                } else if (data instanceof Toa)
                {
                    if (data.get(DataPoint.TOA_INVOCATION_LEVEL) >= 150 && data.get(DataPoint.TOA_INVOCATION_LEVEL) < 300)
                    {
                        shouldDataBeIncluded = false;
                    }
                }
            }
            if (!filterIncludeHard.isSelected())
            {
                if (data instanceof Tob)
                {
                    if (data.isHardMode)
                    {
                        shouldDataBeIncluded = false;
                    }
                } else if (data instanceof Toa)
                {
                    if (data.get(DataPoint.TOA_INVOCATION_LEVEL) >= 300)
                    {
                        shouldDataBeIncluded = false;
                    }
                }
            }
            if (!filterIncludeEntry.isSelected())
            {
                if (data instanceof Tob)
                {
                    if (data.isStoryMode)
                    {
                        shouldDataBeIncluded = false;
                    }
                } else if (data instanceof Toa)
                {
                    if (data.get(DataPoint.TOA_INVOCATION_LEVEL) < 150)
                    {
                        shouldDataBeIncluded = false;
                    }
                }
            }
            if (filterPartialOnly.isSelected()) //todo
            {
                if (data instanceof Tob)
                {
                    Tob tobData = (Tob) data;
                    String comboText = customColumnComboBox.getSelectedItem().toString();
                    if (comboText.endsWith("Time"))
                    {
                        if (!data.getRoomAccurate(RaidRoom.valueOf(comboText.split(",")[0])))
                        {
                            shouldDataBeIncluded = false;
                        }
                    }
                }
            }
            if (filterFavoritesOnly.isSelected())
            {
                if (!data.isFavorite())
                {
                    shouldDataBeIncluded = false;
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
            if (!evaluateAllFilters(data))
            {
                shouldDataBeIncluded = false;
            }
            if (shouldDataBeIncluded)
            {
                if (data instanceof Tob)
                {
                    tobAttempts++;
                    if (data.isCompleted() && data.isAccurate())
                    {
                        tobCompletions++;
                    }
                } else if (data instanceof Toa)
                {
                    toaAttempts++;
                    if (data.isCompleted() && data.isAccurate())
                    {
                        toaCompletions++;
                    }
                } else if (data instanceof Cox)
                {
                    coxAttempts++;
                    if (data.isCompleted() && data.isAccurate())
                    {
                        coxCompletions++;
                    }
                } else if (data instanceof Inf)
                {
                    infAttempts++;
                    if (data.isCompleted() && data.isAccurate())
                    {
                        infCompletions++;
                    }
                } else if (data instanceof Colo)
                {
                    colAttempts++;
                    if (data.isCompleted() && data.isAccurate())
                    {
                        colCompletions++;
                    }
                }
                tableData.add(data);
            }
        }
        attempts = tobAttempts + toaAttempts + coxAttempts + infAttempts + colAttempts;
        completions = tobCompletions + toaCompletions + coxCompletions + infCompletions + colCompletions;

        setTitle("Raids: " + attempts + ", Completions: " + completions + ". Tob: " + tobCompletions + "/" + tobAttempts + ", Toa: " + toaCompletions + "/" + toaAttempts + ", Cox: " + coxCompletions + "/" + coxAttempts + ", Inf: " + infCompletions + "/" + infAttempts + ", Col: " + colCompletions + "/" + colAttempts);

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
                table.getColumn(table.getColumnName(i)).setCellEditor(new ButtonEditorViewColumn(getThemedCheckBox(), tableData, config, itemManager));
                table.getColumn(table.getColumnName(i)).setCellRenderer(new ButtonRendererViewColumn(config));
            } else if (table.getColumnName(i).equals("Custom"))
            {
                table.getColumn(table.getColumnName(i)).setCellEditor(new NonEditableCell(getThemedTextField()));
                table.getColumn(table.getColumnName(i)).setCellRenderer(new StripedTableRowCellRenderer(config));
                table.getColumn(table.getColumnName(i)).setHeaderRenderer(new DynamicTableHeaderRenderer(customColumnComboBox, this));

            } else if (table.getColumnName(i).equals("Favorite"))
            {
                table.getColumn(table.getColumnName(i)).setCellEditor(new ButtonEditorFavoriteColumn(getThemedCheckBox(), tableData, config, itemManager));
                table.getColumn(table.getColumnName(i)).setCellRenderer(new ButtonRendererFavoriteColumn(config, tableData));
            } else
            {
                table.getColumn(table.getColumnName(i)).setCellEditor(new NonEditableCell(getThemedTextField()));
                table.getColumn(table.getColumnName(i)).setCellRenderer(new StripedTableRowCellRenderer(config));
            }
        }
        setTableListeners();
        resizeColumnWidth(table);
        table.setFillsViewportHeight(true);
        List<Raid> finalTableData = tableData;
        new Thread(() -> setLabels(finalTableData)).start();
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
     *
     * @param e mouse event passthrough
     */
    private void setTableRightClickSelection(MouseEvent e)
    {
        if (table.getSelectedRows().length < 2 && SwingUtilities.isRightMouseButton(e))
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
                if (SwingUtilities.isLeftMouseButton(e))
                {
                    String clickedColumn = null;
                    Component c = e.getComponent();
                    if (c instanceof JTableHeader)
                    {
                        JTableHeader header = (JTableHeader) c;
                        clickedColumn = header.getTable().getColumnName(header.getColumnModel().getColumnIndexAtX(e.getX()));
                        if (clickedColumn.equals("Custom"))
                        {
                            Rectangle rect = table.getCellRect(0, header.getColumnModel().getColumnIndexAtX(e.getX()), true);
                            if (e.getX() > rect.getX() + rect.getWidth() - 20)
                            {
                                return;
                            }
                        }
                    }
                    if (clickedColumn != null)
                    {
                        if (clickedColumn.equals(lastColumnClicked)) //if the same column is clicked twice the sort order should be inversed
                        {
                            isSortReversed = !isSortReversed;
                        } else
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
                return (String.join(",", raid.getPlayers()).replaceAll(String.valueOf((char) 160), String.valueOf(' '))); //todo replace with Text method
            case "Spectate":
                return (raid.isSpectated()) ? "Yes" : "No";
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
                    valueToDisplay = RoomUtil.value(raid.get(dataPointName));
                } catch (Exception ignored)
                {

                }
                return (isTime(dataPointName) ? RoomUtil.time(valueToDisplay) : valueToDisplay);
        }
        String valueToDisplay = "(?)";
        try
        {
            valueToDisplay = RoomUtil.value(raid.get(column));
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
            } else
            {
                return false;
            }
        } catch (Exception e)
        {
            return false;
        }
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
        for (int i = 0; i < table.getColumnCount(); i++)
        {
            switch (table.getColumnModel().getColumn(i).getHeaderValue().toString())
            {
                case "":
                    table.getColumnModel().getColumn(i).setPreferredWidth(25);
                    break;
                case "Date":
                    table.getColumnModel().getColumn(i).setPreferredWidth(40);
                    break;
                case "Raid":
                    table.getColumnModel().getColumn(i).setPreferredWidth(30);
                    break;
                case "Scale":
                    table.getColumnModel().getColumn(i).setPreferredWidth(50);
                    break;
                case "Status":
                    table.getColumnModel().getColumn(i).setPreferredWidth(75);
                    break;
                case "Custom":
                    table.getColumnModel().getColumn(i).setPreferredWidth(100);
                    break;
                case "Players":
                    table.getColumnModel().getColumn(i).setPreferredWidth(250);
                    break;
                case "Spectate":
                    table.getColumnModel().getColumn(i).setPreferredWidth(25);
                    break;
                case "Favorite":
                    table.getColumnModel().getColumn(i).setPreferredWidth(50);
                    break;
                case "View":
                    table.getColumnModel().getColumn(i).setPreferredWidth(30);
                    break;
            }
        }
    }

    public void setLabels(List<Raid> data)
    {
        List<Raid> tobData = new ArrayList<>();
        List<Raid> toaData = new ArrayList<>();
        List<Raid> coxData = new ArrayList<>();
        List<Raid> infData = new ArrayList<>();
        List<Raid> colData = new ArrayList<>();
        for (Raid raidData : data)
        {
            if (raidData instanceof Tob) //check if this can just be passed as the raid and not sort into lists per raid //todo
            {
                tobData.add(raidData);
            } else if (raidData instanceof Toa)
            {
                toaData.add(raidData);
            } else if (raidData instanceof Cox)
            {
                coxData.add(raidData);
            } else if (raidData instanceof Inf)
            {
                infData.add(raidData);
            } else if (raidData instanceof Colo)
            {
                colData.add(raidData);
            }
        }
        setOverallLabels(data);
        for (StatisticTab tab : tobTabs)
        {
            tab.updateTab(tobData);
        }
        for (StatisticTab tab : toaTabs)
        {
            tab.updateTab(toaData);
        }
        for (StatisticTab tab : coxTabs)
        {
            tab.updateTab(coxData);
        }
        for (StatisticTab tab : infTabs)
        {
            tab.updateTab(infData);
        }
        for (StatisticTab tab : colTabs)
        {
            tab.updateTab(colData);
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
        for (Map<String, JLabel> labelMap : averageLabels2)
        {
            for (String s : labelMap.keySet())
            {
                labelMap.get(s).setText(RoomUtil.time(StatisticGatherer.getGenericAverage(data, DataPoint.getValue(s + " Time"))));
            }
        }
    }

    public void setOverallMedianLabels(List<Raid> data)
    {
        for (Map<String, JLabel> labelMap : medianLabels2)
        {
            for (String s : labelMap.keySet())
            {
                labelMap.get(s).setText(RoomUtil.time(StatisticGatherer.getGenericMedian(data, DataPoint.getValue(s + " Time"))));
            }
        }
    }

    public void setOverallMinLabels(List<Raid> data)
    {
        for (Map<String, JLabel> labelMap : minLabels2)
        {
            for (String s : labelMap.keySet())
            {
                labelMap.get(s).setText(RoomUtil.time(StatisticGatherer.getGenericMin(data, DataPoint.getValue(s + " Time"))));
            }
        }
    }

    private void setOverallMaxLabels(List<Raid> data)
    {
        for (Map<String, JLabel> labelMap : maxLabels2)
        {
            for (String s : labelMap.keySet())
            {
                labelMap.get(s).setText(RoomUtil.time(StatisticGatherer.getGenericMax(data, DataPoint.getValue(s + " Time"))));
            }
        }
    }

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
            List<String> names = new ArrayList<>(Arrays.asList(split[1].split(",")));
            if (!names.isEmpty())
            {
                Map<String, List<String>> map = new LinkedHashMap<>();
                map.put(name, names);
                aliases.add(map);
            }
        }
        writing = false;
    }


    private JMenuItem createMenuItemTableHeader(final String name)
    {
        JMenuItem item = getThemedMenuItem(name);
        item.addActionListener(event -> getUpdatedPopupMenu(name));
        return item;
    }


    public JPanel getOverallPanel(String title, Map<String, JLabel> labelMap)
    {
        JPanel panel = getTitledPanel(title);
        panel.setLayout(new BorderLayout());

        JPanel subPanel = getThemedPanel();
        subPanel.setLayout(new GridLayout(0, 2));

        int index = 0;
        for (String s : labelMap.keySet())
        {
            JLabel leftLabel = getThemedLabel(s);
            subPanel.add(leftLabel);
            subPanel.add(labelMap.get(s));
            index++;
        }
        /*for (int i = index; i < 19; i++) //enforce formatting, 19 is the most labels used by any panel (Akkha splits)
        {
            JLabel leftLabel = getThemedLabel("");
            JLabel rightLabel = getThemedLabel("");
            subPanel.add(leftLabel);
            subPanel.add(rightLabel);
        }*/
        //subPanel.setPreferredSize(new Dimension(100, 250));
        JScrollPane scrollPane = getThemedScrollPane(subPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);
        panel.setPreferredSize(new Dimension(145, 200));
        return panel;
    }

    public void clearData()
    {
        if (currentData != null)
        {
            currentData.clear();
        }
        if (comparisons != null)
        {
            comparisons.clear();
        }
        close();
    }

    public List<StatisticTab> tobTabs = new ArrayList<>();
    public List<StatisticTab> toaTabs = new ArrayList<>();
    public List<StatisticTab> coxTabs = new ArrayList<>();
    public List<StatisticTab> infTabs = new ArrayList<>();
    public List<StatisticTab> colTabs = new ArrayList<>();

    private String lastColumnClicked = "Date";
    private boolean isSortReversed = true;

    private List<Raid> handleTableSort(List<Raid> tableData)
    {
        Comparator<Raid> comparator;
        switch (lastColumnClicked)
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
        if (comparator == null)
        {
            return tableData;
        }
        if (isSortReversed)
        {
            comparator = comparator.reversed();
        }
        tableData.sort(comparator);
        //If user sorts by a time, e.g., "Xarpus Time", we don't want to include data in the table if xarpus wasn't seen that raid
        if (shouldNotBeSortedByTicks.contains(lastColumnClicked))
        {
            return tableData;
        }
        List<Raid> filteredSortData = new ArrayList<>();
        for (Raid raid : tableData)
        {
            if (lastColumnClicked.equals("Custom"))
            {
                if (raid.getTimeAccurate(DataPoint.getValue(customColumnComboBox.getSelectedItem().toString())))
                {
                    filteredSortData.add(raid);
                }
            } else
            {
                if (raid.getTimeAccurate(DataPoint.getValue(lastColumnClicked)))
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
            for (String nameKey : aliases.keySet())
            {
                for (String alias : aliases.get(nameKey))
                {
                    if (name.equalsIgnoreCase(alias))
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

    public void quickFilterStateChanged()
    {
        saveQuickFilters();
        updateTable();
    }


    public void updateFrameData(List<Raid> data)
    {
        for(Raid raid : data)
        {
            currentData.add(raid);
            if(favorites.contains(raid.getDate().getTime()))
            {
                raid.setFavorite(true);
            }
        }
        for (int i = 0; i < currentData.size(); i++)
        {
            currentData.get(i).setIndex(i);
        }
        updateTable();
    }

    public void createFrame()
    {
        for (int i = 0; i < currentData.size(); i++)
        {
            currentData.get(i).setIndex(i);
        }

        JScrollPane pane = getThemedScrollPane(table);

        JPanel tablePanel = getTitledPanel("Raids");
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(pane);

        container = getThemedPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JTabbedPane tobTabSubpanel = getThemedTabbedPane();
        JTabbedPane toaTabSubpanel = getThemedTabbedPane();
        JTabbedPane coxTabSubpanel = getThemedTabbedPane();
        JTabbedPane infTabSubpanel = getThemedTabbedPane();
        JTabbedPane colTabSubpanel = getThemedTabbedPane();

        tobTabSubpanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        toaTabSubpanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        coxTabSubpanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        infTabSubpanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        colTabSubpanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        List<JPanel> overallPanels = new ArrayList<>();
        for (int k = 0; k < 6; k++)
        {
            JPanel overallPanel = getThemedPanel();
            JPanel overallAveragePanel = getOverallPanel("Average", averageLabels2.get(k));
            JPanel overallMedianPanel = getOverallPanel("Median", medianLabels2.get(k));
            JPanel overallMinPanel = getOverallPanel("Minimum", minLabels2.get(k));
            JPanel overallMaxPanel = getOverallPanel("Maximum", maxLabels2.get(k));

            JPanel topStatPanel = getThemedPanel();
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
        List<Raid> infData = new ArrayList<>();
        List<Raid> colData = new ArrayList<>();
        for (Raid raidData : currentData)
        {
            if (raidData instanceof Tob)
            {
                tobData.add(raidData);
            } else if (raidData instanceof Toa)
            {
                toaData.add(raidData);
            } else if (raidData instanceof Cox)
            {
                coxData.add(raidData);
            } else if (raidData instanceof Inf)
            {
                infData.add(raidData);
            } else if (raidData instanceof Colo)
            {
                colData.add(raidData);
            }
        }
        List<Integer> tobIconIDs = new ArrayList<>(Arrays.asList(ItemID.LIL_MAIDEN, ItemID.LIL_BLOAT, ItemID.LIL_NYLO, ItemID.LIL_SOT, ItemID.LIL_XARP, ItemID.LIL_ZIK));
        List<Integer> toaIconIDs = new ArrayList<>(Arrays.asList(ItemID.NEUTRALISING_POTION, ItemID.BABI, ItemID.SWARM, ItemID.KEPHRITI, ItemID.DRAGON_PICKAXE, ItemID.AKKHITO, ItemID.WATER_CONTAINER, ItemID.ZEBO, ItemID.ELIDINIS_GUARDIAN));
        List<Integer> coxIconIDs = new ArrayList<>(Arrays.asList(ItemID.TEKTINY, ItemID.DRAGON_WARHAMMER, ItemID.KINDLING, ItemID.DYNAMITE, ItemID.VANGUARD, ItemID.LOCKPICK, ItemID.VESPINA, ItemID.PHOENIX_NECKLACE, ItemID.DRAGON_PICKAXE, ItemID.VASA_MINIRIO, ItemID.SALVE_AMULET_E, ItemID.PUPPADILE, ItemID.OLMLET));

        int index = 0;
        tobTabSubpanel.addTab("Overall", overallPanels.get(1));
        toaTabSubpanel.addTab("Overall", overallPanels.get(2));
        coxTabSubpanel.addTab("Overall", overallPanels.get(3));
        infTabSubpanel.addTab("Overall", overallPanels.get(4));
        colTabSubpanel.addTab("Overall", overallPanels.get(5));

        for (RaidRoom room : RaidRoom.getRaidRoomsForRaidType(RaidType.TOB))
        {
            StatisticTab statisticTab = new StatisticTab(tobData, room, config);
            tobTabSubpanel.addTab("", statisticTab);
            tobTabSubpanel.setIconAt(index + 1, new ImageIcon(itemManager.getImage(tobIconIDs.get(index))));
            tobTabs.add(statisticTab);
            index++;
        }
        index = 0;
        for (RaidRoom room : RaidRoom.getRaidRoomsForRaidType(RaidType.TOA))
        {
            StatisticTab statisticTab = new StatisticTab(toaData, room, config);
            toaTabSubpanel.addTab("", statisticTab);
            toaTabSubpanel.setIconAt(index + 1, new ImageIcon(itemManager.getImage(toaIconIDs.get(index))));
            toaTabs.add(statisticTab);
            index++;
        }

        index = 0;
        for (RaidRoom room : RaidRoom.getRaidRoomsForRaidType(RaidType.COX))
        {
            StatisticTab statisticTab = new StatisticTab(coxData, room, config);
            coxTabSubpanel.addTab("", statisticTab);
            coxTabSubpanel.setIconAt(index + 1, new ImageIcon(itemManager.getImage(coxIconIDs.get(index))));
            coxTabs.add(statisticTab);
            index++;
        }
        for (Integer i : roomMap.keySet())
        {
            StatisticTab statisticTab = new StatisticTab(infData, RaidRoom.getRoom(roomMap.get(i)), config);
            infTabSubpanel.addTab(roomMap.get(i), statisticTab);
            infTabs.add(statisticTab);
        }

        for (int i = 1; i < 13; i++)
        {
            StatisticTab statisticTab = new StatisticTab(colData, RaidRoom.getRoom("Col Wave " + i), config);
            colTabSubpanel.addTab("Col Wave " + i, statisticTab);
            colTabs.add(statisticTab);
        }

        tabbedPane.setMinimumSize(new Dimension(100, 300));

        JPanel additionalFiltersPanel = getTitledPanel("Quick Filters");
        additionalFiltersPanel.setLayout(new BorderLayout());
        additionalFiltersPanel.setMinimumSize(new Dimension(220, 300));
        additionalFiltersPanel.setPreferredSize(new Dimension(220, 300));

        filterSpectateOnly = getActionListenCheckBox("Spectated", al -> quickFilterStateChanged());
        filterInRaidOnly = getActionListenCheckBox("In Raid", al -> quickFilterStateChanged());
        filterCompletionOnly = getActionListenCheckBox("Completed", al -> quickFilterStateChanged());
        filterWipeResetOnly = getActionListenCheckBox("Wipe/Reset", al -> quickFilterStateChanged());
        filterComboBoxScale = UISwingUtility.getActionListenCheckBox(new String[]{"Solo", "Duo", "Trio", "4-Man", "5-Man", "6-Man", "7-Man", "8-Man"}, al -> quickFilterStateChanged());
        filterCheckBoxScale = getActionListenCheckBox("Scale", al -> quickFilterStateChanged());
        filterTodayOnly = getActionListenCheckBox("Today", al -> quickFilterStateChanged());
        filterPartyOnly = getActionListenCheckBox("In Party", al -> quickFilterStateChanged());
        filterPartialData = getActionListenCheckBox("Filter Partial", al -> quickFilterStateChanged());
        filterPartialOnly = getActionListenCheckBox("Filter Partial Rooms", al -> quickFilterStateChanged());
        filterFavoritesOnly = getActionListenCheckBox("Favorites", al -> quickFilterStateChanged());
        filterPartialData.setToolTipText("Removes data sets that have any rooms that were partially completed");
        filterIncludeNormal = getActionListenCheckBox("Normal", true, al -> quickFilterStateChanged());
        filterIncludeEntry = getActionListenCheckBox("Entry", true, al -> quickFilterStateChanged());
        filterIncludeHard = getActionListenCheckBox("Hard", true, al -> quickFilterStateChanged());

        filterIncludeTOB = getActionListenCheckBox("ToB", true, al -> quickFilterStateChanged());
        filterIncludeTOA = getActionListenCheckBox("ToA", true, al -> quickFilterStateChanged());
        filterIncludeCOX = getActionListenCheckBox("CoX", true, al -> quickFilterStateChanged());
        filterIncludeInferno = getActionListenCheckBox("Inferno", true, al -> quickFilterStateChanged());
        filterIncludeColosseum = getActionListenCheckBox("Colosseum", true, al -> quickFilterStateChanged());

        loadQuickFilters();

        JPanel scaleContainer = getThemedPanel();
        scaleContainer.setLayout(new BoxLayout(scaleContainer, BoxLayout.X_AXIS));
        scaleContainer.setPreferredSize(new Dimension(150, 25));

        JPanel filterHolder = getThemedPanel();
        filterHolder.setLayout(new GridLayout(9, 2));
        filterHolder.add(filterFavoritesOnly);
        filterHolder.add(filterSpectateOnly);
        filterHolder.add(filterInRaidOnly);
        filterHolder.add(filterCompletionOnly);
        filterHolder.add(filterWipeResetOnly);
        filterHolder.add(filterTodayOnly);
        filterHolder.add(filterPartyOnly);
        filterHolder.add(filterPartialData);

        filterHolder.add(filterIncludeTOB);
        filterHolder.add(filterIncludeTOA);
        filterHolder.add(filterIncludeCOX);
        filterHolder.add(filterIncludeInferno);
        filterHolder.add(filterIncludeColosseum);

        filterHolder.add(filterIncludeNormal);
        filterHolder.add(filterIncludeHard);
        filterHolder.add(filterIncludeEntry);
        scaleContainer.add(filterCheckBoxScale);
        scaleContainer.add(filterComboBoxScale);

        filterHolder.add(scaleContainer);

        additionalFiltersPanel.add(filterHolder);

        JPanel topContainer = getThemedPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));

        topContainer.setPreferredSize(new Dimension(800, 300));

        tabbedPane.addTab("", tobTabSubpanel);
        tabbedPane.addTab("", toaTabSubpanel);
        //tabbedPane.addTab("", coxTabSubpanel);
        tabbedPane.addTab("", infTabSubpanel);
        tabbedPane.addTab("", colTabSubpanel);

        tabbedPane.setIconAt(0, new ImageIcon(itemManager.getImage(ItemID.SCYTHE_OF_VITUR)));
        tabbedPane.setIconAt(1, new ImageIcon(itemManager.getImage(ItemID.TUMEKENS_SHADOW)));
        //tabbedPane.setIconAt(2, new ImageIcon(itemManager.getImage(ItemID.TWISTED_BOW)));
        tabbedPane.setIconAt(2, new ImageIcon(itemManager.getImage(ItemID.INFERNAL_CAPE)));
        tabbedPane.setIconAt(3, new ImageIcon(itemManager.getImage(ItemID.DIZANAS_QUIVER)));

        topContainer.add(tabbedPane);
        topContainer.add(additionalFiltersPanel);
        new Thread(() -> setLabels(currentData)).start();
        filterTableContainer = getThemedPanel();
        filterTable = getThemedTable();
        filterTable.setPreferredSize(new Dimension(380, 135));
        JScrollPane tableScrollView = getThemedScrollPane(filterTable);
        tableScrollView.setPreferredSize(new Dimension(380, 140));
        updateFilterTable();

        container.setPreferredSize(new Dimension(800, 700));

        container.add(topContainer);
        container.add(tablePanel);

        JPanel splitLeftRight = getThemedPanel();
        splitLeftRight.setLayout(new BoxLayout(splitLeftRight, BoxLayout.X_AXIS));
        splitLeftRight.add(container);

        JPanel rightContainer = getThemedPanel();
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


        timeFilterChoice = getThemedComboBox(DataPoint.getTimeNames());

        String[] timeOperatorChoices =
                {
                        "=",
                        "<",
                        ">",
                        "<=",
                        ">="
                };

        timeFilterOperator = getThemedComboBox(timeOperatorChoices);


        timeFilterValue = getThemedTextField();

        JButton timeFilterAdd = getThemedButton("Add");
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


        JPanel filterTimePanelTop = getThemedPanel();
        filterTimePanelTop.setLayout(new BoxLayout(filterTimePanelTop, BoxLayout.X_AXIS));
        filterTimePanelTop.add(timeFilterChoice);

        JPanel filterTimePanelBottom = getThemedPanel();
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

        playerFilterOperator = getThemedComboBox(playersQualifier);
        playerFilterValue = getThemedTextField();
        ;
        JButton playerFilterAdd = getThemedButton("Add");
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

        JPanel filterPlayerPanelTop = getThemedPanel();
        filterPlayerPanelTop.setLayout(new BoxLayout(filterPlayerPanelTop, BoxLayout.X_AXIS));
        JPanel filterPlayerPanelBottom = getThemedPanel();
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

        dateFilterOperator = getThemedComboBox(choicesDate);
        JTextField dateFilterValue = getThemedTextField();
        dateFilterOperator.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateFilterValue.getPreferredSize().height));


        JButton dateFilterAdd = getThemedButton("Add");
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
                        Date date = new GregorianCalendar(year, month - 1, day).getTime();
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

        JPanel dateTopRow = getThemedPanel();
        dateTopRow.setLayout(new BoxLayout(dateTopRow, BoxLayout.X_AXIS));

        JPanel dateBottomRow = getThemedPanel();
        dateBottomRow.setLayout(new BoxLayout(dateBottomRow, BoxLayout.X_AXIS));

        dateTopRow.add(dateFilterOperator);
        dateTopRow.add(Box.createRigidArea(new Dimension(2, 2)));
        dateTopRow.add(dateFilterAdd);
        dateTextField = getThemedTextField();
        dateBottomRow.add(dateTextField);
        dateBottomRow.add(Box.createRigidArea(new Dimension(5, 5)));
        dateBottomRow.add(getThemedLabel("YYYY/MM/DD"));
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


        otherIntFilterChoice = getThemedComboBox(DataPoint.getOtherIntNames());
        otherIntFilterOperator = getThemedComboBox(otherIntOperatorChoices);
        otherIntFilterValue = getThemedTextField();

        JButton otherIntAdd = getThemedButton("Add");
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


        JPanel otherIntTop = getThemedPanel();
        otherIntTop.setLayout(new BoxLayout(otherIntTop, BoxLayout.X_AXIS));
        JPanel otherIntBottom = getThemedPanel();
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

        otherBoolFilterChoice = getThemedComboBox(choicesOtherBool);
        otherBoolFilterOperator = getThemedComboBox(qualifierOtherBool);

        JButton otherBoolAdd = getThemedButton("Add Filter");
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

        JPanel filterBoolTop = getThemedPanel();
        filterBoolTop.setLayout(new BoxLayout(filterBoolTop, BoxLayout.X_AXIS));
        JPanel filterBoolBottom = getThemedPanel();
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


        JPopupMenu raidPopup = getThemedPopupMenu();

        JCheckBoxMenuItem favoriteRaid = getThemedCheckBoxMenuItem("Favorite");
        favoriteRaid.addActionListener(e ->
        {
            int[] toRemove = table.getSelectedRows();
            if (toRemove.length != 1)
            {
                JOptionPane.showMessageDialog(this, "You must select only one raid to Favorite", "Splits Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Raid raid = currentData.get(Integer.parseInt(table.getModel().getValueAt(toRemove[0], 0).toString()));
            if (raid != null)
            {
                raid.setFavorite(!raid.isFavorite());
                favoriteRaid.setText(raid.isFavorite() ? "Unfavorite" : "Favorite");
            }
        });

        JMenuItem analyzeSessions = getThemedMenuItem("Analyze Sessions");
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
            for (Raid data12 : rows) //todo why does this look like it came out of a decompiler?
            {
                if (!sessions.containsKey(data12.getPlayers().size()))
                {
                    Map<String, List<Raid>> scale = new LinkedHashMap<>();
                    List<Raid> list = new ArrayList<>();
                    list.add(data12);
                    scale.put(data12.getPlayerList(aliases), list);
                    sessions.put(data12.getPlayers().size(), scale);
                } else
                {
                    if (!sessions.get(data12.getPlayers().size()).containsKey(data12.getPlayerList(aliases)))
                    {
                        ArrayList<Raid> list = new ArrayList<>();
                        list.add(data12);
                        sessions.get(data12.getPlayers().size()).put(data12.getPlayerList(aliases), list);
                    } else
                    {
                        sessions.get(data12.getPlayers().size()).get(data12.getPlayerList(aliases)).add(data12);
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


        JMenuItem addToComparison = getThemedMenuItem("Add set to comparison");

        JMenuItem viewGraphs = getThemedMenuItem("View Graphs");

        JMenuItem viewCharts = getThemedMenuItem("View Charts");

        JMenuItem copySplits = getThemedMenuItem("Copy Splits");

        copySplits.addActionListener(e ->
        {
            int[] toRemove = table.getSelectedRows();
            if (toRemove.length != 1)
            {
                JOptionPane.showMessageDialog(this, "You must select only one raid to copy splits", "Splits Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Raid raid = currentData.get(Integer.parseInt(table.getModel().getValueAt(toRemove[0], 0).toString()));
            if (raid != null)
            {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(raid.getSplits()), null);
            }
        });

        viewCharts.addActionListener(e ->
        {
            int[] toRemove = table.getSelectedRows();
            if (toRemove.length == 0)
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
                chartFrame.switchTo(raidData);
                chartFrame.open();
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
                ComparisonViewFrame graphView = new ComparisonViewFrame(data1, labels, config, itemManager, clientThread, configManager);
                graphView.open();
            }
        });

        addToComparison.addActionListener(e ->
        {
            List<Raid> rows = new ArrayList<>();
            int[] toRemove = table.getSelectedRows();
            for (int j : toRemove)
            {
                rows.add(currentData.get(Integer.parseInt(table.getModel().getValueAt(j, 0).toString())));
            }
            comparisons.add(rows);
            updateComparisonTable();
        });
        JMenuItem exportRaids = getThemedMenuItem("Export Selected Raids to CSV");
        exportRaids.addActionListener(e ->
        {
            ArrayList<Raid> rows = new ArrayList<>();
            int[] toRemove = table.getSelectedRows();
            for (int j : toRemove)
            {
                rows.add(currentData.get(Integer.parseInt(table.getModel().getValueAt(j, 0).toString())));
            }
            new SaveRaids(rows).open();
        });

        JMenuItem filterRaids = getThemedMenuItem("Filter Selected Raids");
        filterRaids.addActionListener(e ->
        {
            int[] toRemove = table.getSelectedRows();
            for (int j : toRemove)
            {
                filteredIndices.add(Integer.parseInt(table.getModel().getValueAt(j, 0).toString()));
            }

            updateTable();
        });

        JMenu filterOptionsSubMenu = getThemedMenu("Filter Raids");


        JMenuItem undoFilterRaids = getThemedMenuItem("Clear Filtered Raids");
        undoFilterRaids.addActionListener(e ->
        {
            filteredIndices.clear();
            updateTable();
        });

        JMenuItem filterExclusiveRaids = getThemedMenuItem("Filter All Except Selected Raids");
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

        JMenuItem analyzeCrabs = getThemedMenuItem("Analyze selection crab leaks");
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
        raidPopup.add(copySplits);
        raidPopup.add(favoriteRaid);
        table.setComponentPopupMenu(raidPopup);

        filterTableContainer.add(tableScrollView);


        rightBottomContainer.add(filterTableContainer);

        JButton saveFiltersButton = getThemedButton("Save");
        saveFiltersButton.addActionListener(
                al ->
                {
                    SaveFilter saveFilter = new SaveFilter(activeFilters, getQuickFilterStates());
                    saveFilter.open();
                });
        JButton loadFiltersButton = getThemedButton("Load");
        loadFiltersButton.addActionListener(
                al ->
                        new LoadFilter(this, config).open());
        JButton clearFiltersButton = getThemedButton("Clear");
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

        comparisonTable = getThemedTable();
        JScrollPane comparisonTableScroll = getThemedScrollPane(comparisonTable);
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

        JScrollPane aliasScrollPane = getThemedScrollPane(aliasText);
        aliasScrollPane.setPreferredSize(new Dimension(380, 70));
        rightBottomMostContainer.add(aliasScrollPane);

        rightBottomBottomContainer.add(comparisonTableScroll);
        JButton viewComparisonsButton = getThemedButton("View Comparisons");
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
                ComparisonViewFrame graphView = new ComparisonViewFrame(comparisons, labels, config, itemManager, clientThread, configManager);
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

    public List<String> getQuickFilterStates()
    {
        List<String> quickFiltersState = new ArrayList<>();
        quickFiltersState.add("QF-Spectate Only:" + filterSpectateOnly.isSelected());
        quickFiltersState.add("QF-Favorites Only:" + filterFavoritesOnly.isSelected());
        quickFiltersState.add("QF-In Raid Only:" + filterInRaidOnly.isSelected());
        quickFiltersState.add("QF-Completion Only:" + filterCompletionOnly.isSelected());
        quickFiltersState.add("QF-Wipe/Reset Only:" + filterWipeResetOnly.isSelected());
        quickFiltersState.add("QF-Today Only:" + filterTodayOnly.isSelected());
        quickFiltersState.add("QF-Party Only:" + filterPartyOnly.isSelected());
        quickFiltersState.add("QF-Partial Raids:" + filterPartialData.isSelected());
        quickFiltersState.add("QF-ToB:" + filterIncludeTOB.isSelected());
        quickFiltersState.add("QF-ToA:" + filterIncludeTOA.isSelected());
        quickFiltersState.add("QF-CoX:" + filterIncludeCOX.isSelected());
        quickFiltersState.add("QF-Inferno:" + filterIncludeInferno.isSelected());
        quickFiltersState.add("QF-Colosseum:" + filterIncludeColosseum.isSelected());
        quickFiltersState.add("QF-Normal Mode Only:" + filterIncludeNormal.isSelected());
        quickFiltersState.add("QF-Include Hard Mode:" + filterIncludeHard.isSelected());
        quickFiltersState.add("QF-Include Entry Mode:" + filterIncludeEntry.isSelected());
        quickFiltersState.add("QF-Scale:" + filterCheckBoxScale.isSelected() + ":" + filterComboBoxScale.getSelectedIndex());
        quickFiltersState.add("QF-View Raid By:" + customColumnComboBox.getItemAt(customColumnComboBox.getSelectedIndex()));
        return quickFiltersState;
    }

    public String[] columnHeaderNames = new String[]{"Date", "Raid", "Time", "Scale", "Status", "Players", "Favorite", "Spectate", "View"};
    public ArrayList<JCheckBoxMenuItem> columnHeaders;

    private void getUpdatedPopupMenu(String newItem)
    {
        JCheckBoxMenuItem item = getThemedCheckBoxMenuItem(newItem);
        item.setState(true);
        item.addActionListener(al ->
                updateTable());
        columnHeaders.add(item);
        table.getTableHeader().setComponentPopupMenu(getCustomColumnPopUpMenu());
        updateTable();
    }

    private JMenuItem getMenuItem(String text)
    {
        return getThemedMenuItem(text);
    }

    private JMenu getMenu(String text)
    {
        return getThemedMenu(text);
    }

    private void saveQuickFilters()
    {
        DataReader.saveFilterState(getQuickFilterStates());
    }

    private void loadQuickFilters()
    {
        for(String filter : DataReader.getFilterStates())
        {
            setFilterState(filter);
        }
    }

    private JCheckBoxMenuItem getCheckBoxMenuItem(String name, String state)
    {
        JCheckBoxMenuItem item = getThemedCheckBoxMenuItem(name);
        if (state.isEmpty())
        {
            if (!name.equals("Time") && !name.equals("Spectate"))
            {
                item.setState(true);
            }
        } else
        {
            item.setState(state.equals("true"));
        }
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
        if (readColumns.length == 0)
        {
            readColumns = columnHeaderNames;
        }
        columnHeaders.clear();
        for (String column : readColumns)
        {
            String[] split = column.split("~");
            if (split.length == 2)
            {
                JCheckBoxMenuItem option = getCheckBoxMenuItem(split[0], split[1]);
                columnHeaders.add(option);
            } else if (split.length == 1)
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
        switchTo.addActionListener(al ->
        {
            if (name.equals("Default"))
            {
                switchTo(0);
            } else
            {
                int presetNumber = -1;
                try
                {
                    presetNumber = Integer.parseInt(name.substring(name.length() - 1));
                    switchTo(presetNumber);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        return switchTo;
    }

    private void addPreset(JMenu deletePresets)
    {
        for (Integer i : activeMenuPresets)
        {
            if (i != 0)
            {
                JMenuItem deleteItem = getMenuItem("Preset " + i);
                deleteItem.addActionListener(al ->
                {
                    deletePreset(i);
                });
                deletePresets.add(deleteItem);
            }
        }
    }

    private void addSwitchTos(JMenu switchToMenu)
    {
        for (Integer i : activeMenuPresets)
        {
            if (i != 0)
            {
                JMenuItem switchToItem = getSwitchToMenuItem("Preset " + i);
                switchToMenu.add(switchToItem);
            }
        }
    }

    private void deletePreset(Integer preset)
    {
        DataWriter.removePreset(preset);
        activeMenuPresets.removeIf(o -> o.equals(preset));
        table.getTableHeader().setComponentPopupMenu(getCustomColumnPopUpMenu());
    }

    private Integer getNextFreePreset() //20 presets max
    {
        for (int i = 0; i < 20; i++)
        {
            if (!activeMenuPresets.contains(i))
            {
                return i;
            }
        }
        return -1;
    }


    private JPopupMenu getCustomColumnPopUpMenu()
    {
        JPopupMenu baseMenu = getThemedPopupMenu();

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
        if (!activeMenuPresets.contains(0))
        {
            activeMenuPresets.add(0);
        }
        for (Integer i : DataWriter.getPresetCount())
        {
            if (!activeMenuPresets.contains(i))
            {
                activeMenuPresets.add(i);
            }
        }
        switchTo.add(defaultMenuItem);
        addSwitchTos(switchTo);
        Integer nextFree = getNextFreePreset();
        JMenuItem saveToPreset;
        if (nextFree == -1)
        {
            saveToPreset = getMenuItem("<Cannot Save Any More Presets>");
            saveToPreset.setEnabled(false);
        } else
        {
            saveToPreset = getMenuItem("Save Current To Preset " + nextFree);
            saveToPreset.addActionListener(al ->
            {
                activeMenuPresets.add(nextFree);
                List<String> columnNames = new ArrayList<>();
                for (JCheckBoxMenuItem columnCheckBox : columnHeaders)
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
            if (raidType.equals(RaidType.ALL))
            {
                for (DataPoint.MenuCategories menuCategories : DataPoint.MenuCategories.values())
                {
                    JMenu typeLevelMenu = getMenu(menuCategories.name);
                    for (RaidRoom room : RaidRoom.getRaidRoomsForRaidType(raidType))
                    {
                        for (String itemName : DataPoint.getMenuNamesByType(room, menuCategories))
                        {
                            typeLevelMenu.add(createMenuItemTableHeader(itemName));
                        }
                    }
                    raidLevelMenu.add(typeLevelMenu);
                }
            } else
            {
                for (RaidRoom room : RaidRoom.getRaidRoomsForRaidType(raidType))
                {
                    JMenu roomLevelMenu = getMenu(room.name);
                    for (DataPoint.MenuCategories menuCategories : DataPoint.MenuCategories.values())
                    {
                        JMenu typeLevelMenu = getMenu(menuCategories.name);
                        for (String itemName : DataPoint.getMenuNamesByType(room, menuCategories))
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
        for (int i = 0; i < comparisonTable.getRowCount(); i++)
        {
            oldNames.add(comparisonTable.getValueAt(i, 1).toString());
        }
        int index = 0;
        for (List<Raid> comparison : comparisons)
        {
            String comparisonName = "Set " + index;
            if (index < oldNames.size())
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
        comparisonTable.getColumn("Sets").setCellEditor(new NonEditableCell(getThemedTextField()));
        comparisonTable.getColumn("").setCellRenderer(new ButtonRendererViewColumn(config));
        comparisonTable.getColumn("").setCellEditor(new ButtonEditorComparisonData(getThemedCheckBox(), this));
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
                        case "Favorites Only":
                            filterFavoritesOnly.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Partial Rooms":
                            filterPartialData.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "ToB":
                            filterIncludeTOB.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "ToA":
                            filterIncludeTOA.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "CoX":
                            filterIncludeCOX.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Inferno":
                            filterIncludeInferno.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Colosseum":
                            filterIncludeColosseum.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Normal Mode Only":
                            filterIncludeNormal.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Include Hard Mode":
                            filterIncludeHard.setSelected(Boolean.parseBoolean(data[1]));
                            break;
                        case "Include Entry Mode":
                            filterIncludeEntry.setSelected(Boolean.parseBoolean(data[1]));
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
        filterTable.setDefaultRenderer(Object.class, new StripedTableRowCellRenderer(config));
        filterTable.getColumn("Filter Descriptions").setCellEditor(new NonEditableCell(getThemedTextField()));
        filterTable.getColumn("").setCellRenderer(new ButtonRendererViewColumn(config));
        filterTable.getColumn("").setCellEditor(new ButtonEditorFilterData(getThemedCheckBox(), this));
        resizeColumnWidthFilters(filterTable);
        filterTable.getColumn("").setMaxWidth(100);
        filterTable.setFillsViewportHeight(true);
        filterTableContainer.validate();
        filterTableContainer.repaint();
        updateTable();
    }

    @Override
    public void update()
    {
        updateTable();
    }

    @Override
    public void setComboBox(String item)
    {
        customColumnComboBox.addItem(item);
        customColumnComboBox.setSelectedItem(item);
    }
}
