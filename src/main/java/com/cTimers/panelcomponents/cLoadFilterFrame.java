package com.cTimers.panelcomponents;

import lombok.extern.slf4j.Slf4j;
import com.cTimers.filters.cFilter;
import com.cTimers.utility.cFilterManager;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
@Slf4j
public class cLoadFilterFrame extends cFrame
{
    private cFilteredRaidsFrame filteredRaidsFrame;
    public void resizeColumnWidth(JTable table)
    {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++)
        {
            int width = 15; // Min width
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

    public cLoadFilterFrame(cFilteredRaidsFrame cFilteredRaidsFrame)
    {
        this.filteredRaidsFrame = cFilteredRaidsFrame;
        setTitle("Load Filters");
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        String[] columnNames = {"Filter Name", "View", "Load"};
        ArrayList<Object[]> tableBuilder = new ArrayList<>();

        ArrayList<cFilter> filters = cFilterManager.getFilters();
        System.out.println("filters size: " + filters.size());
        for(cFilter filter : filters)
        {
            Object[] row =
                    {
                            filter.getName(),
                            "View",
                            "Load"
                    };
            tableBuilder.add(row);
        }
        Object[][] tableObject = new Object[filters.size()][3];
        int count = 0;
        for(Object[] row : tableBuilder)
        {
            tableObject[count] = row;
            count++;
        }
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JTable table = new JTable(tableObject, columnNames);
        table.getColumn("Filter Name").setCellEditor(new cNonEditableCell(new JTextField()));

        table.getColumn("View").setCellRenderer(new cButtonRenderer());
        table.getColumn("View").setCellEditor(new cButtonEditorViewFilters(new JCheckBox(), filters));

        table.getColumn("Load").setCellRenderer(new cButtonRenderer());
        table.getColumn("Load").setCellEditor(new cButtonEditorLoadFilters(new JCheckBox(), filteredRaidsFrame, filters, this));
        resizeColumnWidth(table);
        JScrollPane pane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        mainPanel.add(pane);
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
}
