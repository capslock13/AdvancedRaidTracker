package com.cTimers.panelcomponents;

import net.runelite.client.ui.ClientUI;

import javax.swing.*;

public class cFrame extends JFrame
{
    public cFrame()
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
