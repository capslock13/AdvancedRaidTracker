package com.cTimers.panelcomponents;

import com.cTimers.filters.cFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class cButtonEditorLoadFilters extends DefaultCellEditor
{

    protected JButton button;
    private String label;
    private boolean isPushed;
    int row;
    private ArrayList<cFilter> data;
    private JPanel activeFiltersContainer;
    private cFrame closeFrame;

    public cButtonEditorLoadFilters(JCheckBox checkBox, ArrayList<cFilter> data, JPanel activeFiltersContainer, cLoadFilterFrame loadFrame)
    {
        super(checkBox);
        this.data = data;
        this.activeFiltersContainer = activeFiltersContainer;
        this.closeFrame = loadFrame;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
        if (isSelected)
        {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else
        {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        this.row = row;
        return button;
    }


    @Override
    public Object getCellEditorValue() {
        if (isPushed)
        {
            ArrayList<Component> components = new ArrayList<>();
            for(Component c : activeFiltersContainer.getComponents())
            {
                if(c instanceof JLabel)
                {
                    components.add(c);
                }
            }
            for(Component c : components)
            {
                activeFiltersContainer.remove(c);
            }
            //TODO add clear filter?
            activeFiltersContainer.setLayout(new GridLayout(1+data.get(row).getFilters().length, 1));
            for(String s : data.get(row).getFilters())
            {
                activeFiltersContainer.add(new JLabel(s));
            }
            activeFiltersContainer.repaint();
            activeFiltersContainer.revalidate();
            closeFrame.close();
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
