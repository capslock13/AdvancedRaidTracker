package com.cTimers.panelcomponents;

import com.cTimers.cRoomData;
import com.cTimers.utility.RoomUtil;

import javax.swing.*;

public class cRaidRow extends JPanel
{
    private cRoomData data;

    public cRaidRow(cRoomData data)
    {
        this.data = data;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JLabel raidCompleted = new JLabel((data.raidCompleted) ? "Completed" : "Not completed");
        String time = RoomUtil.time(data.getTimeSum());
        JLabel raidTime = new JLabel(time);
        String players = "";
        for(String s : data.players)
        {
            players += s + ", ";
        }
        JLabel raidPlayers = new JLabel(players.substring(0, players.length()-2));
        JLabel raidScale = new JLabel(""+data.players.size());
        JLabel raidDate = new JLabel(data.raidStarted.toString());

        add(raidDate);
        add(raidTime);
        add(raidScale);
        add(raidCompleted);
        add(raidPlayers);
    }

}
