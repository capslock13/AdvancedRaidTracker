package com.advancedraidtracker.ui.filters;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.customrenderers.ButtonEditorLoadFilters;
import com.advancedraidtracker.ui.customrenderers.ButtonEditorViewFilters;
import com.advancedraidtracker.ui.customrenderers.ButtonRenderer;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.ui.Raids;
import com.advancedraidtracker.ui.customrenderers.NonEditableCell;
import lombok.extern.slf4j.Slf4j;
import com.advancedraidtracker.filters.Filter;
import com.advancedraidtracker.filters.FilterManager;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;

import static com.advancedraidtracker.utility.UISwingUtility.*;

@Slf4j
public class LoadFilter extends BaseFrame
{
    public void resizeColumnWidth(JTable table)
    {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++)
        {
            int width = 30; // Min width
            for (int row = 0; row < table.getRowCount(); row++)
            {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            if (width > 400)
            {
                width = 400;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    public LoadFilter(Raids FilteredRaidsFrame, AdvancedRaidTrackerConfig config)
    {
        setTitle("Load Filters");
        JPanel mainPanel = getTitledPanel("Filters");

        String[] columnNames = {"Filter Name", "View", "Replace", "Add"};
        ArrayList<Object[]> tableBuilder = new ArrayList<>();

        ArrayList<Filter> filters = FilterManager.getFilters();
        for (Filter filter : filters)
        {
            Object[] row =
                    {
                            filter.getName(),
                            "View Filter",
                            "Replace Active",
                            "Add to Active"
                    };
            tableBuilder.add(row);
        }
        Object[][] tableObject = new Object[filters.size()][4];
        int count = 0;
        for (Object[] row : tableBuilder)
        {
            tableObject[count] = row;
            count++;
        }
        JPanel container = getThemedPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JTable table = getThemedTable(tableObject, columnNames);
        table.getColumn("Filter Name").setCellEditor(new NonEditableCell(getThemedTextField()));

        table.getColumn("View").setCellRenderer(new ButtonRenderer(config));
        table.getColumn("View").setCellEditor(new ButtonEditorViewFilters(getThemedCheckBox(), filters));

        table.getColumn("Replace").setCellRenderer(new ButtonRenderer(config));
        table.getColumn("Replace").setCellEditor(new ButtonEditorLoadFilters(getThemedCheckBox(), FilteredRaidsFrame, filters, this));

        table.getColumn("Add").setCellRenderer(new ButtonRenderer(config));
        table.getColumn("Add").setCellEditor(new ButtonEditorLoadFilters(getThemedCheckBox(), FilteredRaidsFrame, filters, this, false));

        resizeColumnWidth(table);
        JScrollPane pane = getThemedScrollPane(table);
        table.setFillsViewportHeight(true);
        mainPanel.add(pane);
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
}
