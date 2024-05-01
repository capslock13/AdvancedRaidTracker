package com.advancedraidtracker.ui.viewraid.tob;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.ui.BaseFrame;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.advancedraidtracker.utility.datautility.datapoints.tob.*;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

import static com.advancedraidtracker.constants.RaidRoom.*;
import static com.advancedraidtracker.utility.UISwingUtility.*;
import static com.advancedraidtracker.utility.datautility.DataPoint.*;

@Slf4j
public class ViewTOBRaid extends BaseFrame //todo @fisu not sure if you wanted to tackle this but this is a big one that needs fixing post migration
{
    String INCOMPLETE_MARKER = "-";
    String red = "<html><font color='#FF0000'>";
    String green = "<html><font color='#44AF33'>";
    String blue = "<html><font color='#6666DD'>";
    String full;
    String soft;
    String dark;


    public ViewTOBRaid(Tob data, AdvancedRaidTrackerConfig config)
    {
        Color c = config.fontColor();
        full = colorStr(c);
        soft = colorStr(c.darker());
        dark = colorStr(c.darker().darker());
        setTitle("View Raid");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel thisSubPanel = getThemedPanel();
        thisSubPanel.setLayout(new GridLayout(2, 3));


        String maidenColor = getColor(data, MAIDEN);
        String bloatColor = getColor(data, BLOAT);
        String nyloColor = getColor(data, NYLOCAS);
        String soteColor = getColor(data, SOTETSEG);
        String xarpColor = getColor(data, XARPUS);
        String verzikColor = getColor(data, VERZIK);

        String maidenBodyColor = getBodyColor(data, MAIDEN);
        String bloatBodyColor = getBodyColor(data, BLOAT);
        String nyloBodyColor = getBodyColor(data, NYLOCAS);
        String soteBodyColor = getBodyColor(data, SOTETSEG);
        String xarpBodyColor = getBodyColor(data, XARPUS);
        String verzikBodyColor = getBodyColor(data, VERZIK);

        //todo ... rename split / duration @caps

        int bloatEntry = data.get(DataPoint.MAIDEN_TIME);
        int bloatSplit = data.get(DataPoint.BLOAT_TIME);

        int nyloEntry = bloatEntry + bloatSplit;
        int nyloSplit = data.get(DataPoint.NYLOCAS_TIME);

        int soteEntry = nyloEntry + nyloSplit;
        int soteSplit = data.get(DataPoint.SOTETSEG_TIME);

        int xarpEntry = soteEntry + soteSplit;
        int xarpSplit = data.get(DataPoint.XARPUS_TIME);

        int verzikEntry = xarpEntry + xarpSplit;

        String bloatSplitStr = RoomUtil.time(bloatSplit);

        String nyloEntryStr = RoomUtil.time(nyloEntry);
        String nyloSplitStr = RoomUtil.time(nyloSplit);

        String soteEntryStr = RoomUtil.time(soteEntry);
        String soteSplitStr = RoomUtil.time(soteSplit);

        String xarpEntryStr = RoomUtil.time(xarpEntry);
        String xarpSplitStr = RoomUtil.time(xarpSplit);

        String verzikEntryStr = RoomUtil.time(verzikEntry);

        JPanel maidenPanel = getTitledPanel(maidenColor + "Maiden"/* + ((data.maidenScuffed) ? " (Scuffed after " + data.firstMaidenCrabScuffed + ")" : "")*/); //todo scuff
        maidenPanel.setLayout(new BorderLayout());
        JPanel maidenSubPanel = getThemedPanel();
        GridLayout gl = new GridLayout(10, 2);
        maidenSubPanel.setLayout(gl);

        maidenSubPanel.add(getThemedLabel(maidenBodyColor + "Blood Spawned (thrown)"));
        maidenSubPanel.add(getThemedLabel(maidenBodyColor + data.get(DataPoint.MAIDEN_BLOOD_SPAWNED) + " (" + data.get(DataPoint.MAIDEN_BLOOD_THROWN) + ")"));


        maidenSubPanel.add(getThemedLabel(maidenBodyColor + "Defense"));
        maidenSubPanel.add(getThemedLabel(maidenBodyColor + ((data.getDefenseAccurate(MAIDEN)) ? data.get(DataPoint.DEFENSE, MAIDEN) : INCOMPLETE_MARKER)));

        maidenSubPanel.add(getThemedLabel(maidenBodyColor + "Crabs leaked"));
        maidenSubPanel.add(getThemedLabel(maidenBodyColor + data.get(DataPoint.MAIDEN_CRABS_LEAKED) + ", HP: " + data.get(DataPoint.MAIDEN_HP_HEALED)));

        maidenSubPanel.add(getThemedLabel(maidenBodyColor + "100% leaked"));
        maidenSubPanel.add(getThemedLabel(maidenBodyColor + data.get(DataPoint.MAIDEN_CRABS_LEAKED_FULL_HP)));

        maidenSubPanel.add(getThemedLabel(maidenBodyColor + "Scuffed?"));
        maidenSubPanel.add(getThemedLabel(maidenBodyColor + (/*(data.maidenScuffed) ? data.firstMaidenCrabScuffed : */"No"))); //todo scuffed

        maidenSubPanel.add(getThemedLabel(maidenBodyColor + "Deaths"));
        maidenSubPanel.add(getThemedLabel(maidenBodyColor + data.get(DataPoint.DEATHS, MAIDEN)));

        maidenSubPanel.add(getThemedLabel(maidenBodyColor + "70s"));
        maidenSubPanel.add(getThemedLabel(maidenBodyColor + RoomUtil.time(data.get(DataPoint.MAIDEN_70_SPLIT))));

        maidenSubPanel.add(getThemedLabel(maidenBodyColor + "50s"));
        maidenSubPanel.add(getThemedLabel(maidenBodyColor + RoomUtil.time(data.get(DataPoint.MAIDEN_50_SPLIT)) + " (" + RoomUtil.time(data.get(DataPoint.MAIDEN_50_SPLIT) - data.get(DataPoint.MAIDEN_70_SPLIT)) + ")"));

        maidenSubPanel.add(getThemedLabel(maidenBodyColor + "30s"));
        maidenSubPanel.add(getThemedLabel(maidenBodyColor + RoomUtil.time(data.get(DataPoint.MAIDEN_30_SPLIT)) + " (" + RoomUtil.time(data.get(DataPoint.MAIDEN_30_SPLIT) - data.get(DataPoint.MAIDEN_50_SPLIT)) + ")"));

        maidenSubPanel.add(getThemedLabel(maidenBodyColor + "Room time"));
        maidenSubPanel.add(getThemedLabel(maidenBodyColor + RoomUtil.time(data.get(MAIDEN_TIME)) + " (" + RoomUtil.time(data.get(MAIDEN_TIME) - data.get(DataPoint.MAIDEN_30_SPLIT)) + ")"));


        JPanel bloatPanel = getTitledPanel(bloatColor + "Bloat - " + bloatBodyColor + nyloEntryStr + " (" + bloatSplitStr + ")");
        bloatPanel.setLayout(new BorderLayout());
        JPanel nylocasPanel = getTitledPanel(nyloColor + "Nylocas - " + nyloBodyColor + soteEntryStr + " (" + nyloSplitStr + ")");
        nylocasPanel.setLayout(new BorderLayout());
        JPanel sotetsegPanel = getTitledPanel(soteColor + "Sotetseg - " + soteBodyColor + xarpEntryStr + " (" + soteSplitStr + ")");
        sotetsegPanel.setLayout(new BorderLayout());
        JPanel xarpusPanel = getTitledPanel(xarpColor + "Xarpus - " + xarpBodyColor + verzikEntryStr + " (" + xarpSplitStr + ")");
        xarpusPanel.setLayout(new BorderLayout());
        JPanel verzikPanel = getTitledPanel(verzikColor + "Verzik");
        verzikPanel.setLayout(new BorderLayout());

        JPanel bloatSubPanel = getThemedPanel();
        bloatSubPanel.setLayout(new GridLayout(8, 2));


        bloatSubPanel.add(getThemedLabel(bloatBodyColor + "Downs"));
        bloatSubPanel.add(getThemedLabel(bloatBodyColor + data.get(DataPoint.BLOAT_DOWNS)));

        bloatSubPanel.add(getThemedLabel(bloatBodyColor + "Deaths (1st walk)"));
        bloatSubPanel.add(getThemedLabel(bloatBodyColor + data.get(DataPoint.BLOAT_FIRST_WALK_DEATHS)));


        bloatSubPanel.add(getThemedLabel(bloatBodyColor + "Deaths (Total)"));
        bloatSubPanel.add(getThemedLabel(bloatBodyColor + data.get(DataPoint.DEATHS, BLOAT)));

        bloatSubPanel.add(getThemedLabel(bloatBodyColor + "Defense (1st walk)"));
        bloatSubPanel.add(getThemedLabel(bloatBodyColor + ((data.getDefenseAccurate(BLOAT)) ? String.valueOf(data.get(DataPoint.DEFENSE, BLOAT)) : INCOMPLETE_MARKER)));

        bloatSubPanel.add(getThemedLabel(bloatBodyColor + "Scythes 1st walk"));
        bloatSubPanel.add(getThemedLabel(bloatBodyColor + data.get(DataPoint.BLOAT_FIRST_WALK_SCYTHES)));

        bloatSubPanel.add(getThemedLabel(bloatBodyColor + "HP % 1st down"));

        bloatSubPanel.add(getThemedLabel(bloatBodyColor + (((double) data.get(DataPoint.BLOAT_HP_FIRST_DOWN))) + "%"));

        bloatSubPanel.add(getThemedLabel(bloatBodyColor + "1st down time"));
        bloatSubPanel.add(getThemedLabel(bloatBodyColor + data.get(DataPoint.BLOAT_FIRST_DOWN_TIME)));

        bloatSubPanel.add(getThemedLabel(bloatBodyColor + "Room time"));
        bloatSubPanel.add(getThemedLabel(bloatBodyColor + RoomUtil.time(bloatSplit)));


        JPanel nylocasSubPanel = getThemedPanel();
        nylocasSubPanel.setLayout(new GridLayout(9, 2));

        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + "Stalls"));
        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + data.get(DataPoint.NYLO_STALLS_PRE_20) + " " + data.get(DataPoint.NYLO_STALLS_POST_20) + " (" + (data.get(DataPoint.NYLO_STALLS_TOTAL)) + ")"));

        String nyloSplits = "<html><font color='#999999'>"
                + data.get(DataPoint.NYLO_SPLITS_MELEE)
                + "<font color='#00AA00'> "
                + data.get(DataPoint.NYLO_SPLITS_RANGE)
                + "<font color ='#2299FF'> "
                + data.get(DataPoint.NYLO_SPLITS_MAGE);
        if (nyloBodyColor.equals(dark))
        {
            nyloSplits = nyloBodyColor
                    + data.get(DataPoint.NYLO_SPLITS_MELEE) + " "
                    + data.get(DataPoint.NYLO_SPLITS_RANGE) + " "
                    + data.get(DataPoint.NYLO_SPLITS_MAGE);
        } else if (nyloBodyColor.equals(soft))
        {
            nyloSplits = "<html><font color='#444444'>"
                    + data.get(DataPoint.NYLO_SPLITS_MELEE) + "<font color='#008800'> "
                    + data.get(DataPoint.NYLO_SPLITS_RANGE) + "<font color ='#0066CC'> "
                    + data.get(DataPoint.NYLO_SPLITS_MAGE);
        }
        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + "Splits"));
        nylocasSubPanel.add(getThemedLabel(nyloSplits));

        String nyloRotations = "<html><font color='#999999'>"
                + data.get(DataPoint.NYLO_ROTATIONS_MELEE) + "<font color='#00AA00'> "
                + data.get(DataPoint.NYLO_ROTATIONS_RANGE) + "<font color ='#2299FF'> "
                + data.get(DataPoint.NYLO_ROTATIONS_MAGE);

        if (nyloBodyColor.equals(dark))
        {
            nyloRotations = nyloBodyColor
                    + data.get(DataPoint.NYLO_ROTATIONS_MELEE) + " "
                    + data.get(DataPoint.NYLO_ROTATIONS_RANGE) + " "
                    + data.get(DataPoint.NYLO_ROTATIONS_MAGE);
        } else if (nyloBodyColor.equals(soft))
        {
            nyloRotations = "<html><font color='#444444'>"
                    + data.get(DataPoint.NYLO_ROTATIONS_MELEE) + "<font color='#008800'> "
                    + data.get(DataPoint.NYLO_ROTATIONS_RANGE) + "<font color ='#0066CC'> "
                    + data.get(DataPoint.NYLO_ROTATIONS_MAGE);
        }

        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + "Rotations"));
        nylocasSubPanel.add(getThemedLabel(nyloRotations));

        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + "Defense"));
        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + ((data.getDefenseAccurate(NYLOCAS)) ? String.valueOf(data.get(DataPoint.DEFENSE, NYLOCAS)) : INCOMPLETE_MARKER)));

        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + "Deaths"));
        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + data.get(DataPoint.DEATHS, NYLOCAS)));

        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + "Last wave"));
        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + RoomUtil.time(data.get(DataPoint.NYLO_LAST_WAVE))));

        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + "Clean up"));
        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + RoomUtil.time(data.get(NYLO_LAST_DEAD)) + " (" + RoomUtil.time(data.get(NYLO_LAST_DEAD) - data.get(DataPoint.NYLO_LAST_WAVE)) + ")"));

        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + "Boss Spawn"));
        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + RoomUtil.time(data.get(DataPoint.NYLO_BOSS_SPAWN)) + " (" + RoomUtil.time(data.get(DataPoint.NYLO_BOSS_SPAWN) - data.get(NYLO_LAST_DEAD)) + ")"));

        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + "Time"));
        nylocasSubPanel.add(getThemedLabel(nyloBodyColor + RoomUtil.time(nyloSplit) + " (" + RoomUtil.time(nyloSplit - data.get(DataPoint.NYLO_BOSS_SPAWN)) + ")"));

        JPanel sotetsegSubPanel = getThemedPanel();
        sotetsegSubPanel.setLayout(new GridLayout(8, 2));

        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + "Hammers hit"));
        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + ((data.getDefenseAccurate(SOTETSEG)) ? data.get(DataPoint.SOTE_SPECS_P1) + " " + data.get(DataPoint.SOTE_SPECS_P2) + " " + data.get(DataPoint.SOTE_SPECS_P3) + " (" + (data.get(DataPoint.SOTE_SPECS_TOTAL)) + "/" + data.get(SOTE_SPECS_ATTEMPTED_TOTAL) + ")" : INCOMPLETE_MARKER)));

        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + "Deaths"));
        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + data.get(DataPoint.DEATHS, SOTETSEG))); //todo specific deaths

        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + "First Maze Start"));
        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + RoomUtil.time(data.get(SOTE_M1_SPLIT))));

        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + "First Maze End"));
        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + RoomUtil.time(data.get(SOTE_P2_SPLIT)) + " (" + RoomUtil.time(data.get(SOTE_M1_DURATION)) + ")"));

        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + "Second Maze Start"));
        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + RoomUtil.time(data.get(DataPoint.SOTE_M2_SPLIT)) + " (" + RoomUtil.time(data.get(SOTE_P2_DURATION)) + ")"));

        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + "Second Maze End"));
        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + RoomUtil.time(data.get(DataPoint.SOTE_P3_SPLIT)) + " (" + RoomUtil.time(data.get(SOTE_M2_DURATION)) + ")"));

        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + "Time"));
        sotetsegSubPanel.add(getThemedLabel(soteBodyColor + RoomUtil.time(soteSplit) + " (" + RoomUtil.time(data.get(SOTE_P3_DURATION)) + ")"));

        JPanel xarpusSubPanel = getThemedPanel();
        xarpusSubPanel.setLayout(new GridLayout(8, 2));

        xarpusSubPanel.add(getThemedLabel(xarpBodyColor + "Defense")); //todo defense
        xarpusSubPanel.add(getThemedLabel(xarpBodyColor + ((data.getDefenseAccurate(XARPUS)) ? data.get(DataPoint.DEFENSE, XARPUS) : INCOMPLETE_MARKER)));

        xarpusSubPanel.add(getThemedLabel(xarpBodyColor + "Deaths"));
        xarpusSubPanel.add(getThemedLabel(xarpBodyColor + data.get(DataPoint.DEATHS, XARPUS)));

        xarpusSubPanel.add(getThemedLabel(xarpBodyColor + "Healing"));
        xarpusSubPanel.add(getThemedLabel(xarpBodyColor + data.get(DataPoint.XARP_HEALING)));

        xarpusSubPanel.add(getThemedLabel(xarpBodyColor + "Screech"));
        xarpusSubPanel.add(getThemedLabel(xarpBodyColor + RoomUtil.time(data.get(DataPoint.XARP_SCREECH))));

        xarpusSubPanel.add(getThemedLabel(xarpBodyColor + "Time"));
        xarpusSubPanel.add(getThemedLabel(xarpBodyColor + RoomUtil.time(xarpSplit) + " (" + RoomUtil.time(xarpSplit - data.get(DataPoint.XARP_SCREECH)) + ")"));

        xarpusSubPanel.add(getThemedLabel(""));
        xarpusSubPanel.add(getThemedLabel(""));

        xarpusSubPanel.add(getThemedLabel(""));
        xarpusSubPanel.add(getThemedLabel(""));

        xarpusSubPanel.add(getThemedLabel(""));
        xarpusSubPanel.add(getThemedLabel(""));

        JPanel verzikSubPanel = getThemedPanel();
        verzikSubPanel.setLayout(new GridLayout(8, 2));

        verzikSubPanel.add(getThemedLabel(verzikBodyColor + "Bounces"));
        verzikSubPanel.add(getThemedLabel(verzikBodyColor + data.get(DataPoint.VERZIK_BOUNCES)));

        verzikSubPanel.add(getThemedLabel(verzikBodyColor + "Deaths"));
        verzikSubPanel.add(getThemedLabel(verzikBodyColor + data.get(DataPoint.DEATHS, VERZIK)));

        verzikSubPanel.add(getThemedLabel(verzikBodyColor + "Crabs Spawned"));
        verzikSubPanel.add(getThemedLabel(verzikBodyColor + data.get(DataPoint.VERZIK_CRABS_SPAWNED)));

        verzikSubPanel.add(getThemedLabel(verzikBodyColor + "Phase 1"));
        verzikSubPanel.add(getThemedLabel(verzikBodyColor + RoomUtil.time(data.get(DataPoint.VERZIK_P2_SPLIT))));

        verzikSubPanel.add(getThemedLabel(verzikBodyColor + "Reds Proc"));
        verzikSubPanel.add(getThemedLabel(verzikBodyColor + RoomUtil.time(data.get(DataPoint.VERZIK_REDS_SPLIT)) + " (" + RoomUtil.time(data.get(DataPoint.VERZIK_REDS_SPLIT) - data.get(DataPoint.VERZIK_P2_SPLIT)) + ")"));

        verzikSubPanel.add(getThemedLabel(verzikBodyColor + "Phase 2"));
        verzikSubPanel.add(getThemedLabel(verzikBodyColor + RoomUtil.time(data.get(DataPoint.VERZIK_P3_SPLIT)) + " (" + RoomUtil.time(data.get(DataPoint.VERZIK_P2_DURATION)) + ")"));

        verzikSubPanel.add(getThemedLabel(verzikBodyColor + "Time"));
        verzikSubPanel.add(getThemedLabel(verzikBodyColor + RoomUtil.time(data.get(DataPoint.VERZIK_TIME)) + " (" + RoomUtil.time(data.get(VERZIK_P3_DURATION)) + ")"));

        maidenPanel.add(maidenSubPanel);
        bloatPanel.add(bloatSubPanel);
        nylocasPanel.add(nylocasSubPanel);
        sotetsegPanel.add(sotetsegSubPanel);
        xarpusPanel.add(xarpusSubPanel);
        verzikPanel.add(verzikSubPanel);


        thisSubPanel.add(maidenPanel);
        thisSubPanel.add(bloatPanel);
        thisSubPanel.add(nylocasPanel);
        thisSubPanel.add(sotetsegPanel);
        thisSubPanel.add(xarpusPanel);
        thisSubPanel.add(verzikPanel);


        JPanel summaryPanel = getTitledPanel("Summary");
        summaryPanel.setLayout(new BorderLayout());
        summaryPanel.setMinimumSize(new Dimension(100, 30));
        Calendar cal = Calendar.getInstance();
        cal.setTime(data.getDate());
        String dateString = (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR);
        JPanel summarySubPanel = getThemedPanel(new GridLayout(10, 1));
        summarySubPanel.add(getThemedLabel("Date: " + dateString));

        summarySubPanel.add(getThemedLabel("Scale: " + data.getScaleString()));
        setSummaryStatus(data, summarySubPanel);
        summarySubPanel.add(getThemedLabel("Time: " + RoomUtil.time(data.get(CHALLENGE_TIME))));
        summarySubPanel.add(getThemedLabel("Players:"));
        for (String player : data.getPlayers())
        {
            summarySubPanel.add(getThemedLabel("        " + player + " (" + data.get(DEATHS, player) + ")"));
        }

        summaryPanel.add(summarySubPanel);

        JPanel topPanel = getThemedPanel();
        topPanel.setLayout(new GridLayout(1, 3));
        topPanel.add(summaryPanel);

        JPanel thrallsPanel = getTitledPanel("Thralls");
        thrallsPanel.setLayout(new GridLayout(7, 2));


        thrallsPanel.add(getThemedLabel("Maiden Thrall Hits: "));
        thrallsPanel.add(getThemedLabel(String.valueOf(data.get(DataPoint.THRALL_ATTACKS, MAIDEN)), SwingConstants.RIGHT));

        thrallsPanel.add(getThemedLabel("Bloat Thrall Hits: "));
        thrallsPanel.add(getThemedLabel(String.valueOf(data.get(DataPoint.THRALL_ATTACKS, BLOAT)), SwingConstants.RIGHT));

        thrallsPanel.add(getThemedLabel("Nylo Thrall Hits: "));
        thrallsPanel.add(getThemedLabel(String.valueOf(data.get(DataPoint.THRALL_ATTACKS, NYLOCAS)), SwingConstants.RIGHT));

        thrallsPanel.add(getThemedLabel("Sotetseg Thrall Hits: "));
        thrallsPanel.add(getThemedLabel(String.valueOf(data.get(DataPoint.THRALL_ATTACKS, SOTETSEG)), SwingConstants.RIGHT));

        thrallsPanel.add(getThemedLabel("Xarpus Thrall Hits: "));
        thrallsPanel.add(getThemedLabel(String.valueOf(data.get(DataPoint.THRALL_ATTACKS, XARPUS)), SwingConstants.RIGHT));

        thrallsPanel.add(getThemedLabel("Verzik Thrall Hits: "));
        thrallsPanel.add(getThemedLabel(String.valueOf(data.get(DataPoint.THRALL_ATTACKS, VERZIK)), SwingConstants.RIGHT));


        JPanel vengPanel = getThemedPanel();
        vengPanel.setBorder(BorderFactory.createTitledBorder("Venges"));
        vengPanel.setLayout(new GridLayout(8, 2));

        vengPanel.add(getThemedLabel("Venges"));
        vengPanel.add(getThemedLabel("(Procced/Cast): Damage"));

        vengPanel.add(getThemedLabel("All Rooms"));
        vengPanel.add(getThemedLabel("(" + data.get(DataPoint.VENG_PROCS) + "/" + data.get(DataPoint.VENG_CASTS) + "): " + data.get(DataPoint.VENG_DAMAGE), SwingConstants.RIGHT));

        vengPanel.add(getThemedLabel("Maiden"));
        vengPanel.add(getThemedLabel("(" + data.get(DataPoint.VENG_PROCS, MAIDEN) + "/" + data.get(DataPoint.VENG_CASTS, MAIDEN) + "): " + data.get(DataPoint.VENG_DAMAGE, MAIDEN), SwingConstants.RIGHT));

        vengPanel.add(getThemedLabel("Bloat"));
        vengPanel.add(getThemedLabel("(" + data.get(DataPoint.VENG_PROCS, BLOAT) + "/" + data.get(DataPoint.VENG_CASTS, BLOAT) + "): " + data.get(DataPoint.VENG_DAMAGE, BLOAT), SwingConstants.RIGHT));

        vengPanel.add(getThemedLabel("Nylo"));
        vengPanel.add(getThemedLabel("(" + data.get(DataPoint.VENG_PROCS, NYLOCAS) + "/" + data.get(DataPoint.VENG_CASTS, NYLOCAS) + "): " + data.get(DataPoint.VENG_DAMAGE, NYLOCAS), SwingConstants.RIGHT));

        vengPanel.add(getThemedLabel("Sote"));
        vengPanel.add(getThemedLabel("(" + data.get(DataPoint.VENG_PROCS, SOTETSEG) + "/" + data.get(DataPoint.VENG_CASTS, SOTETSEG) + "): " + data.get(DataPoint.VENG_DAMAGE, SOTETSEG), SwingConstants.RIGHT));

        vengPanel.add(getThemedLabel("Xarp"));
        vengPanel.add(getThemedLabel("(" + data.get(DataPoint.VENG_PROCS, XARPUS) + "/" + data.get(DataPoint.VENG_CASTS, XARPUS) + "): " + data.get(DataPoint.VENG_DAMAGE, XARPUS), SwingConstants.RIGHT));

        vengPanel.add(getThemedLabel("Verzik"));
        vengPanel.add(getThemedLabel("(" + data.get(DataPoint.VENG_PROCS, VERZIK) + "/" + data.get(DataPoint.VENG_CASTS, VERZIK) + "): " + data.get(DataPoint.VENG_DAMAGE, VERZIK), SwingConstants.RIGHT));

        topPanel.add(vengPanel);


        topPanel.add(thrallsPanel);


        add(topPanel);
        add(thisSubPanel);

        pack();
    }

    private String getColor(Raid data, RaidRoom room)
    {
        String color = red;
        if (data.getRoomPartiallyAccurate(room))
        {
            color = blue;
        }
        if (data.getRoomAccurate(room))
        {
            color = green;
        }
        return color;
    }

    private String getBodyColor(Raid data, RaidRoom room)
    {
        String color = dark;
        if (data.getRoomPartiallyAccurate(room))
        {
            color = soft;
        }
        if (data.getRoomAccurate(room))
        {
            color = full;
        }
        return color;
    }

    private static void setSummaryStatus(Tob data, JPanel summarySubPanel)
    {
        String raidStatusString = data.getRoomStatus();

        summarySubPanel.add(getThemedLabel("<html>Raid Status: " + raidStatusString));
    }
}
