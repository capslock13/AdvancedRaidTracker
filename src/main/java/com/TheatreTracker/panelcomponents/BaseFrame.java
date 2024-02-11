package com.TheatreTracker.panelcomponents;


import com.formdev.flatlaf.FlatClientProperties;

import com.formdev.flatlaf.ui.FlatNativeWindowBorder;


import javax.swing.*;

public class BaseFrame extends JFrame
{
    public BaseFrame()
    {
        JRootPane rp = getRootPane();
        if (FlatNativeWindowBorder.isSupported())
        {
            rp.putClientProperty(FlatClientProperties.USE_WINDOW_DECORATIONS, true);
        }
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void open()
    {
        setVisible(true);
        toFront();
        repaint();
    }

    public void close()
    {
        setVisible(false);
    }
}
