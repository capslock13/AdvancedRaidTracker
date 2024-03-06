package com.advancedraidtracker.ui.comparisonview;

import com.advancedraidtracker.ui.BaseFrame;

import javax.swing.*;
import java.awt.*;

public class NoDataPopUp extends BaseFrame
{
    public NoDataPopUp()
    {
        setTitle("No Data");
        JPanel container = new JPanel();
        container.add(new JLabel("No data to compare. Add a data set by selecting raids in the table and using the right click context menu -> \"Add set to comparison\"\""));
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e ->
        {
            close();
            rootPane.setVisible(false);
        });
        container.add(okButton);
        setPreferredSize(new Dimension(800, 100));
        add(container);
        pack();
        setLocationRelativeTo(null);
    }
}
