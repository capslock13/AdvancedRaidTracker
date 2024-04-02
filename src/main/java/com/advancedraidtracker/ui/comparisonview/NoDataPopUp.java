package com.advancedraidtracker.ui.comparisonview;

import com.advancedraidtracker.ui.BaseFrame;

import javax.swing.*;
import java.awt.*;

import static com.advancedraidtracker.utility.UISwingUtility.*;

public class NoDataPopUp extends BaseFrame
{
    public NoDataPopUp()
    {
        setTitle("No Data");
        JPanel container = getThemedPanel();
        container.add(getThemedLabel("No data to compare. Add a data set by selecting raids in the table and using the right click context menu -> \"Add set to comparison\"\""));
        JButton okButton = getThemedButton("Ok");
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
