package com.cTimers.panelcomponents;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

public class cNonEditableCell extends DefaultCellEditor
{
    public cNonEditableCell(JTextField textField)
    {
        super(textField);
        textField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                fireEditingStopped();
            }
        });
    }

    protected void fireEditingStopped()
    {
        super.fireEditingStopped();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent)
    {
        return false;
    }
}
