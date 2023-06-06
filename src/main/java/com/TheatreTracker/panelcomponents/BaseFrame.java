package com.TheatreTracker.panelcomponents;

import net.runelite.client.ui.ClientUI;

import javax.swing.*;

public class BaseFrame extends JFrame
{
    public BaseFrame()
    {
        setIconImage(ClientUI.ICON);
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
