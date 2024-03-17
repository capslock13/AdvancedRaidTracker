package com.advancedraidtracker.ui.customrenderers;


import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.ui.DataPointMenu;
import com.advancedraidtracker.ui.Raids;
import com.advancedraidtracker.utility.datautility.DataPoint;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
@Slf4j
public class DynamicTableHeaderRenderer implements TableCellRenderer
{
    private JTable table = null;
    private MouseEventReposter reporter = null;
    private JComponent editor;
    java.util.List<String> allComboValues;
    Map<String, String[]> popupData;
    java.util.List<String> flatData;
    JPopupMenu popupMenu;
    DataPointMenu dataPointMenu;
    Raids window;

    public DynamicTableHeaderRenderer(JComboBox<String> editor, Raids window)
    {
        this.window = window;
        this.editor = editor;
        this.editor.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        popupData = new LinkedHashMap<>();
        flatData = new ArrayList<>();
        for(RaidRoom room : RaidRoom.values())
        {
            popupData.put(room.name, DataPoint.getSpecificNames(room));
        }
        editor = new JComboBox<>();
        editor.setEditable(true);
        editor.setPrototypeDisplayValue("Challenge Time");
        editor.setSelectedItem("Challenge Time");
        editor.setEditable(false);
        allComboValues = new ArrayList<>(popupData.keySet());
        popupMenu = new JPopupMenu();
        popupMenu.setBorder(new MatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
        List<String> allComboValues = new ArrayList<String>(popupData.keySet());
        dataPointMenu = new DataPointMenu(allComboValues, popupData, flatData, popupMenu, editor, window);
    }

    private boolean menuVisible = false;
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
    {
        if (table != null && this.table != table)
        {
            this.table = table;
            final JTableHeader header = table.getTableHeader();
            if (header != null)
            {
                this.editor.setForeground(header.getForeground());
                this.editor.setBackground(header.getBackground());
                this.editor.setFont(header.getFont());
                reporter = new MouseEventReposter(header, col, this.editor, popupMenu, window);
                header.addMouseListener(reporter);
            }
        }

        if (reporter != null) reporter.setColumn(col);

        return this.editor;
    }

    static public class MouseEventReposter extends MouseAdapter
    {

        private Component dispatchComponent;
        private JTableHeader header;
        @Setter
        private int column = -1;
        private Component editor;
        private JPopupMenu menu;
        private Raids window;

        public MouseEventReposter(JTableHeader header, int column, Component editor, JPopupMenu menu, Raids window)
        {
            this.window = window;
            this.header = header;
            this.column = column;
            this.editor = editor;
            this.menu = menu;
            menu.addPropertyChangeListener(pcl ->
            {
                if(menu.isVisible())
                {
                    //window.setTableSorterActive(true);
                }
            });
        }

        private void setDispatchComponent(MouseEvent e)
        {
            int col = header.getTable().columnAtPoint(e.getPoint());
            if (col != column || col == -1) return;

            Point p = e.getPoint();
            Point p2 = SwingUtilities.convertPoint(header, p, editor);
            dispatchComponent = SwingUtilities.getDeepestComponentAt(editor, p2.x, p2.y);
        }

        private boolean repostEvent(MouseEvent e)
        {
            if (dispatchComponent == null)
            {
                return false;
            }
            MouseEvent e2 = SwingUtilities.convertMouseEvent(header, e, dispatchComponent);
            dispatchComponent.dispatchEvent(e2);
            return true;
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            if (header.getResizingColumn() == null)
            {
                Point p = e.getPoint();

                int col = header.getTable().columnAtPoint(p);
                if (col != column || col == -1) return;

                int index = header.getColumnModel().getColumnIndexAtX(p.x);
                if (index == -1) return;

                editor.setBounds(header.getHeaderRect(index));
                header.add(editor);
                editor.validate();
                setDispatchComponent(e);
                repostEvent(e);
            }
            if(SwingUtilities.isLeftMouseButton(e))
            {
                try
                {
                    Rectangle rect = header.getTable().getCellRect(0, header.getColumnModel().getColumnIndexAtX(e.getX()), true);
                    if(e.getX() > rect.getX()+rect.getWidth()-20)
                    {
                        menu.show(editor, (int)rect.getWidth()-menu.getPreferredSize().width, editor.getHeight());
                        //window.setTableSorterActive(false);
                    }
                }
                catch (Exception ignore)
                {
                    //resizing quirk
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            repostEvent(e);
            dispatchComponent = null;
            header.remove(editor);
        }
    }
}
