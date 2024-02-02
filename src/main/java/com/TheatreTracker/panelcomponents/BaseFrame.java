package com.TheatreTracker.panelcomponents;

import net.runelite.client.ui.ClientUI;

import javax.swing.*;
import java.util.Arrays;

public class BaseFrame extends JFrame
{
    public BaseFrame()
    {
        //setIconImages(Arrays.asList(ClientUI.ICON_128, ClientUI.ICON_16));
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
