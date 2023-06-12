package com.TheatreTracker.panelcomponents;

import lombok.extern.slf4j.Slf4j;
import com.TheatreTracker.filters.Filter;
import com.TheatreTracker.utility.FilterManager;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
@Slf4j
public class LoadFilterBaseFrame extends BaseFrame
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
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 400)
            {
                width = 400;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    public LoadFilterBaseFrame(FilteredRaidsBaseFrame FilteredRaidsFrame)
    {
        setTitle("Load Filters");
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        String[] columnNames = {"Filter Name", "View", "Replace", "Add"};
        ArrayList<Object[]> tableBuilder = new ArrayList<>();

        ArrayList<Filter> filters = FilterManager.getFilters();
        for(Filter filter : filters)
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
        for(Object[] row : tableBuilder)
        {
            tableObject[count] = row;
            count++;
        }
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JTable table = new JTable(tableObject, columnNames);
        table.getColumn("Filter Name").setCellEditor(new NonEditableCell(new JTextField()));

        table.getColumn("View").setCellRenderer(new ButtonRenderer());
        table.getColumn("View").setCellEditor(new ButtonEditorViewFilters(new JCheckBox(), filters));

        table.getColumn("Replace").setCellRenderer(new ButtonRenderer());
        table.getColumn("Replace").setCellEditor(new ButtonEditorLoadFilters(new JCheckBox(), FilteredRaidsFrame, filters, this));

        table.getColumn("Add").setCellRenderer(new ButtonRenderer());
        table.getColumn("Add").setCellEditor(new ButtonEditorLoadFilters(new JCheckBox(), FilteredRaidsFrame, filters, this, false));

        resizeColumnWidth(table);
        JScrollPane pane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        mainPanel.add(pane);
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
}
