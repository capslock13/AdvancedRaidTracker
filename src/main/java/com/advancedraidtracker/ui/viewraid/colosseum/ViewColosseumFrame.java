package com.advancedraidtracker.ui.viewraid.colosseum;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.rooms.col.ColosseumHandler;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.Point;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.col.Colo;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.advancedraidtracker.utility.UISwingUtility.*;
import static com.advancedraidtracker.utility.UISwingUtility.colorStr;
import static java.awt.Color.RED;

@Slf4j
public class ViewColosseumFrame extends BaseFrame
{
    String red = "<html><font color='#FF0000'>";
    String green = "<html><font color='#33FF33'>";
    String blue = "<html><font color='#6666DD'>";
    String orange = "<html><font color='#ddaa1c'>";
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
        for(int i = 1; i < 13; i++)
        {
            log.info("Wave " + i + " spawn data: " + colData.getSpawnString(i));
        }
        setTitle("View Raid");
        setPreferredSize(new Dimension(800, 750));
        //add(new ViewColosseumPanel(colData, config));

        JPanel topContainer = getThemedPanel();
        topContainer.setPreferredSize(new Dimension(800, 150));
        topContainer.setLayout(new GridLayout(1, 2));
        topContainer.add(getSummaryBox());
        topContainer.add(getSolHereditBox());
        JPanel bottomContainer = getThemedPanel();
        bottomContainer.setPreferredSize(new Dimension(800, 600));
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
        JPanel container = getTitledPanel(title);
        container.setLayout(new GridLayout(1, 2));
        JPanel base = getThemedPanel();
        base.setLayout(new GridLayout(0, 1));
        String[] split = colData.getSpawnString(wave).split("");
        String spawnString = "https://supalosa.github.io/osrs-colosseum/?";
        final BufferedImage colImage = ImageUtil.loadImageResource(AdvancedRaidTrackerPlugin.class, "/com/advancedraidtracker/colosseum.png");
        Graphics2D g = (Graphics2D) colImage.getGraphics();
        g.setColor(RED);
        for(String s : split)
        {
            try
            {
                int ID = ColosseumHandler.mapToWebsiteIDs.getOrDefault(ColosseumHandler.getId(s), -1);
                Point p = ColosseumHandler.mapToWebsitePoint(ColosseumHandler.getCoordinates(s));
                spawnString += String.format("%02d", p.getX()) + String.format("%02d", p.getY()) + ID + ".";
                int size = ColosseumHandler.spawnSize.getOrDefault(ID, 0);
                g.setColor(ColosseumHandler.websiteColorMap.get(ID));
                g.drawRect(8*(p.getX()-1)-2, 8*(p.getY()-size), 8*size, 8*size);
            }
            catch (Exception ignore)
            {

            }
        }
        JLabel websiteLabel = getThemedLabel();
        websiteLabel.setText("<html><a href=\"\">View Spawn</a></html>");
        websiteLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String finalSpawnString = spawnString;
        websiteLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                try
                {
                    Desktop.getDesktop().browse(new URI(finalSpawnString));
                }
                catch (URISyntaxException | IOException ex)
                {
                    //It looks like there's a problem
                }
            }
        });

        base.add(websiteLabel);
        base.add(getThemedLabel("Prayer Used: " + colData.get(DataPoint.PRAYER_USED, RaidRoom.getRoom("Wave " + wave))));
        base.add(getThemedLabel("Damage Dealt: " + colData.get(DataPoint.DAMAGE_DEALT, RaidRoom.getRoom("Wave " + wave))));
        base.add(getThemedLabel("Damage Received: " + colData.get(DataPoint.DAMAGE_RECEIVED, RaidRoom.getRoom("Wave " + wave))));
        base.add(getThemedLabel("Idle Ticks: "));
        base.add(getThemedLabel("Reinforcement? No"));
        container.add(base);

        JLabel picLabel = new JLabel(new ImageIcon(colImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
        container.add(picLabel);
        return container;
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
        else if(colData.highestWaveStarted == wave)
        {
            color = orange;
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
