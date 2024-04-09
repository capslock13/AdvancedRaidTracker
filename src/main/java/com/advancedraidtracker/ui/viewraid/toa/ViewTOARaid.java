package com.advancedraidtracker.ui.viewraid.toa;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.toa.Toa;

import javax.swing.*;

import java.awt.*;

import static com.advancedraidtracker.utility.UISwingUtility.*;

public class ViewTOARaid extends BaseFrame
{
    Toa toaData;
    private final String HTML = "<html>";
    String red = "<html><font color='#FF0000'>";
    String green = "<html><font color='#33FF33'>";
    String blue = "<html><font color='#6666DD'>";
    String orange = "<html><font color='#ddaa1c'>";

    String full;
    String soft;
    String dark;

    public ViewTOARaid(Toa toaData, AdvancedRaidTrackerConfig config)
    {
        this.toaData = toaData;

        Color c = config.fontColor();
        full = colorStr(c);
        soft = colorStr(c.darker());
        dark = colorStr(c.darker().darker());
        setTitle("View Raid");
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel topContainer = getThemedPanel();
        topContainer.setLayout(new GridLayout(1, 2));
        topContainer.setPreferredSize(new Dimension(800, 200));

        JPanel summaryPanel = getTitledPanel("Summary");
        JPanel wardensPanel = getTitledPanel("Wardens");

        topContainer.add(summaryPanel);
        topContainer.add(wardensPanel);

        JPanel bottomContainer = getThemedPanel();
        bottomContainer.setPreferredSize(new Dimension(800, 400));
        bottomContainer.setLayout(new GridLayout(2, 4));
        JPanel apmekenPanel = getApmekenPanel();
        JPanel scabarasPanel = getScabarasPanel();

        JPanel hetPanel = getHetPanel();
        JPanel crondisPanel = getCrondisPanel();

        bottomContainer.add(apmekenPanel);
        bottomContainer.add(scabarasPanel);
        bottomContainer.add(hetPanel);
        bottomContainer.add(crondisPanel);

        add(topContainer);
        add(bottomContainer);

        setResizable(false);
        pack();
    }

    private JPanel getCrondisPanel()
    {
        int puzzleTime = toaData.get(DataPoint.CRONDIS_TIME);
        int bossTime = toaData.get(DataPoint.ZEBAK_TIME);
        int pathTime = puzzleTime+bossTime;
        String bodyColor = getBodyColor("Crondis");

        JPanel panel = getTitledPanel(green + "Path of Crondis - " + RoomUtil.time(pathTime));
        panel.setLayout(new GridLayout(0, 2));

        panel.add(getThemedLabel("Crondis Time: "));
        panel.add(getThemedLabel(green+RoomUtil.time(puzzleTime)));

        panel.add(getThemedLabel("100 Heals: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.CRONDIS_HEALS_100))));

        panel.add(getThemedLabel("50 Heals: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.CRONDIS_HEALS_50))));

        panel.add(getThemedLabel("25 Heals: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.CRONDIS_HEALS_25))));

        panel.add(getThemedLabel("Croc Damage: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.CRONDIS_CROCODILE_DAMAGE))));

        panel.add(getThemedLabel("Jugs Pushed: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.ZEBAK_JUGS_PUSHED))));

        panel.add(getThemedLabel("Earthquakes: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.ZEBAK_BOULDER_ATTACKS))));

        panel.add(getThemedLabel("WaterFalls: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.ZEBAK_WATERFALL_ATTACKS))));

        panel.add(getThemedLabel("Enraged: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.ZEBAK_ENRAGED_SPLIT))));

        panel.add(getThemedLabel("Zebak Time: "));
        panel.add(getThemedLabel(green+RoomUtil.time(toaData.get(DataPoint.ZEBAK_TIME)) + " (" + RoomUtil.time(toaData.get(DataPoint.ZEBAK_ENRAGED_DURATION)) + ")"));

        return panel;
    }

    private JPanel getHetPanel()
    {
        int puzzleTime = toaData.get(DataPoint.HET_TIME);
        int bossTime = toaData.get(DataPoint.AKKHA_TIME);
        int pathTime = puzzleTime+bossTime;

        JPanel panel = getTitledPanel(green + "Path of Het - " + RoomUtil.time(pathTime));
        panel.setLayout(new GridLayout(0, 2));

        panel.add(getThemedLabel("Het Time: "));
        panel.add(getThemedLabel(green+RoomUtil.time(puzzleTime)));

        panel.add(getThemedLabel("Downs: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.HET_DOWNS))));

        panel.add(getThemedLabel("Null Hits: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.AKKHA_NULL_HIT))));

        panel.add(getThemedLabel("Shadow1 Start:"));
        panel.add(getThemedLabel(String.valueOf(RoomUtil.time(toaData.get(DataPoint.AKKHA_P1_DURATION)))));

        panel.add(getThemedLabel("Shadow1 End: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.AKKHA_P2_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.AKKHA_SHADOW_1_DURATION)) + ")"));

        panel.add(getThemedLabel("Shadow2 Start: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.AKKHA_SHADOW_2_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.AKKHA_P2_DURATION)) + ")"));

        panel.add(getThemedLabel("Shadow2 End: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.AKKHA_P3_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.AKKHA_SHADOW_2_DURATION)) + ")"));

        panel.add(getThemedLabel("Shadow3 Start: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.AKKHA_SHADOW_3_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.AKKHA_P3_DURATION)) + ")"));

        panel.add(getThemedLabel("Shadow3 End: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.AKKHA_P4_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.AKKHA_SHADOW_3_DURATION)) + ")"));

        panel.add(getThemedLabel("Shadow4 Start: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.AKKHA_SHADOW_4_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.AKKHA_P4_DURATION)) + ")"));

        panel.add(getThemedLabel("Shadow4 End: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.AKKHA_P5_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.AKKHA_SHADOW_4_DURATION)) + ")"));

        panel.add(getThemedLabel("Phase5 End: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.AKKHA_FINAL_PHASE_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.AKKHA_P5_DURATION)) + ")"));

        panel.add(getThemedLabel("Akkha Time: "));
        panel.add(getThemedLabel(green+RoomUtil.time(toaData.get(DataPoint.AKKHA_TIME)) + " (" + RoomUtil.time(toaData.get(DataPoint.AKKHA_FINAL_PHASE_DURATION)) + ")"));


        return panel;
    }

    private JPanel getScabarasPanel()
    {
        int puzzleTime = toaData.get(DataPoint.SCABARAS_TIME);
        int bossTime = toaData.get(DataPoint.KEPHRI_TIME);
        int pathTime = puzzleTime+bossTime;

        JPanel panel = getTitledPanel(green + "Path of Scabaras - " + RoomUtil.time(pathTime));
        panel.setLayout(new GridLayout(0, 2));

        panel.add(getThemedLabel("Scabaras Time: "));
        panel.add(getThemedLabel(green+RoomUtil.time(puzzleTime)));

        panel.add(getThemedLabel("Swarm Heals: "));
        panel.add(getThemedLabel(toaData.get(DataPoint.KEPHRI_SWARMS_HEALED) + "/" + toaData.get(DataPoint.KEPHRI_SWARMS_TOTAL)));

        panel.add(getThemedLabel("Melee Heals: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.KEPHRI_MELEE_SCARAB_HEALS))));

        panel.add(getThemedLabel("Melee Alive: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.KEPHRI_MELEE_TICKS_ALIVE))));

        panel.add(getThemedLabel("Dung Throws: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.KEPHRI_DUNG_THROWN))));

        panel.add(getThemedLabel("Swarm1 Start:"));
        panel.add(getThemedLabel(String.valueOf(RoomUtil.time(toaData.get(DataPoint.KEPHRI_P1_DURATION)))));

        panel.add(getThemedLabel("Swarm1 End: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.KEPHRI_P2_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.KEPHRI_SWARM1_DURATION)) + ")"));

        panel.add(getThemedLabel("Swarm2 Start: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.KEPHRI_SWARM2_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.KEPHRI_P2_DURATION)) + ")"));

        panel.add(getThemedLabel("Swarm2 End: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.KEPHRI_P3_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.KEPHRI_SWARM2_DURATION)) + ")"));

        panel.add(getThemedLabel("Kephri Time: "));
        panel.add(getThemedLabel(green+RoomUtil.time(toaData.get(DataPoint.KEPHRI_TIME)) + " (" + RoomUtil.time(toaData.get(DataPoint.KEPHRI_P3_DURATION)) + ")"));
        return panel;
    }

    private JPanel getApmekenPanel()
    {
        int puzzleTime = toaData.get(DataPoint.APMEKEN_TIME);
        int bossTime = toaData.get(DataPoint.BABA_TIME);
        int pathTime = puzzleTime+bossTime;

        JPanel panel = getTitledPanel(green + "Path of Apmeken - " + RoomUtil.time(pathTime));
        panel.setLayout(new GridLayout(0, 2));

        panel.add(getThemedLabel("Apmeken Time: "));
        panel.add(getThemedLabel(green+RoomUtil.time(puzzleTime)));

        panel.add(getThemedLabel("Cursed: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.APMEKEN_CURSED_COUNT))));

        panel.add(getThemedLabel("Volatile: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.APMEKEN_VOLATILE_COUNT))));

        panel.add(getThemedLabel("Shaman: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.APMEKEN_SHAMAN_COUNT))));

        panel.add(getThemedLabel("Thrown: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.BABA_BOULDERS_THROWN))));

        panel.add(getThemedLabel("Broken: "));
        panel.add(getThemedLabel(String.valueOf(toaData.get(DataPoint.BABA_BOULDERS_BROKEN))));

        panel.add(getThemedLabel("Boulder1 Start:"));
        panel.add(getThemedLabel(String.valueOf(RoomUtil.time(toaData.get(DataPoint.BABA_P1_DURATION)))));

        panel.add(getThemedLabel("Boulder1 End: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.BABA_P2_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.BABA_BOULDER_1_DURATION)) + ")"));

        panel.add(getThemedLabel("Boulder2 Start: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.BABA_BOULDER_2_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.BABA_P2_DURATION)) + ")"));

        panel.add(getThemedLabel("Boulder2 End: "));
        panel.add(getThemedLabel(RoomUtil.time(toaData.get(DataPoint.BABA_P3_SPLIT)) + " (" + RoomUtil.time(toaData.get(DataPoint.BABA_BOULDER_2_DURATION)) + ")"));

        panel.add(getThemedLabel("Room Time: "));
        panel.add(getThemedLabel(green+RoomUtil.time(toaData.get(DataPoint.BABA_TIME)) + " (" + RoomUtil.time(toaData.get(DataPoint.BABA_P3_DURATION)) + ")"));

        return panel;
    }

    private String getBodyColor(String room)
    {
        String color = dark;
        if (toaData.get(room + " Time") > 0)
        {
            color = full;
        }
        return color;
    }
}
