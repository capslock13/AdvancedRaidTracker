package com.cTimers.panelcomponents;

import net.runelite.client.ui.ClientUI;

import javax.swing.*;

public class cFrame extends JFrame
{
    public cFrame()
    {
        setIconImage(ClientUI.ICON);
       // setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
