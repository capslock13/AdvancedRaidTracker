package com.advancedraidtracker.ui.viewraid.colosseum;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.col.Colo;

import javax.swing.*;
import java.awt.*;

import static com.advancedraidtracker.utility.UISwingUtility.*;
import static com.advancedraidtracker.utility.UISwingUtility.colorStr;

public class ViewColosseumFrame extends BaseFrame
{
    String red = "<html><font color='#FF0000'>";
    String green = "<html><font color='#33FF33'>";
    String blue = "<html><font color='#6666DD'>";
    String full;
    String soft;
    String dark;
    private final Colo colData;
    public ViewColosseumFrame(Colo colData, AdvancedRaidTrackerConfig config)
    {
        Color c = config.fontColor();
        full = colorStr(c);
        soft = colorStr(c.darker());
        dark = colorStr(c.darker().darker());

        this.colData = colData;
        setTitle("View Raid");
        setPreferredSize(new Dimension(800, 600));
        //add(new ViewColosseumPanel(colData, config));

        JPanel topContainer = getThemedPanel();
        topContainer.setPreferredSize(new Dimension(800, 120));
        topContainer.setLayout(new GridLayout(1, 2));
        topContainer.add(getSummaryBox());
        topContainer.add(getSolHereditBox());
        JPanel bottomContainer = getThemedPanel();
        bottomContainer.setPreferredSize(new Dimension(800, 480));
        bottomContainer.setLayout(new GridLayout(4,3));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        for(int i = 0; i < 12; i++)
        {
            bottomContainer.add(getWaveBox(i+1));
        }
        add(topContainer);
        add(bottomContainer);
        setResizable(false);
        pack();
    }

    public JPanel getWaveBox(int wave)
    {
        String title = getColor(wave) + "Wave " + wave;
        if(colData.get("Wave " + wave + " Duration") > 0)
        {
            title += " - " + RoomUtil.time(colData.get("Wave " + wave + " Duration"));
        }
        JPanel base = getTitledPanel(title);
        base.setLayout(new GridLayout(0, 1));
        base.add(getThemedLabel("Prayer Used: " + colData.get(DataPoint.PRAYER_USED, RaidRoom.getRoom("Wave " + wave))));
        base.add(getThemedLabel("Damage Dealt: " + colData.get(DataPoint.DAMAGE_DEALT, RaidRoom.getRoom("Wave " + wave))));
        base.add(getThemedLabel("Damage Received: " + colData.get(DataPoint.DAMAGE_RECEIVED, RaidRoom.getRoom("Wave " + wave))));
        return base;
    }

    public JPanel getSummaryBox()
    {
        String title = ((colData.isCompleted()) ? green : red) + "Summary - " + RoomUtil.time(colData.getChallengeTime());
        JPanel base = getTitledPanel(title);

        return base;
    }

    public JPanel getSolHereditBox()
    {
        String title = (colData.isCompleted()) ? green : red;
        title += "Sol Heredit";
        title += (colData.get("Wave 12 Split") > 0) ? " - " + RoomUtil.time(colData.get("Wave 12 Duration")) : "";
        JPanel base = getTitledPanel(title);

        return base;
    }

    private String getColor(int wave)
    {
        String color = red;
        if(colData.get("Wave " + wave + " Duration") > 0)
        {
            color = green;
        }
        return color;
    }

    private String getBodyColor(int wave)
    {
        String color = dark;
        if(colData.get("Wave " + wave + " Duration") > 0)
        {
            color = full;
        }
        return color;
    }
}
