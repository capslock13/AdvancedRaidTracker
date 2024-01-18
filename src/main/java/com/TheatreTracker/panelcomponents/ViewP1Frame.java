package com.TheatreTracker.panelcomponents;

import com.TheatreTracker.RoomData;

import javax.swing.*;
import java.awt.*;

public class ViewP1Frame extends BaseFrame
{
    private RoomData data;
    public ViewP1Frame(RoomData roomData)
    {
        data = roomData;
        P1Panel panel = new P1Panel(roomData);
        add(panel);
        pack();
    }
}
