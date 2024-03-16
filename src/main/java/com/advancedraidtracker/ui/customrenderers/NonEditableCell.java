package com.advancedraidtracker.ui.customrenderers;

import javax.swing.*;
import java.util.EventObject;

public class NonEditableCell extends DefaultCellEditor
{
    public NonEditableCell(JTextField textField)
    {
        super(textField);
        textField.addActionListener(e -> fireEditingStopped());
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
