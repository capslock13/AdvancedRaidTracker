package com.advancedraidtracker.ui.viewraid;

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
import static com.advancedraidtracker.utility.datautility.DataPoint.*;

@Slf4j
public class ViewTOBRaid extends BaseFrame //todo @fisu not sure if you wanted to tackle this but this is a big one that needs fixing post migration
{
    String INCOMPLETE_MARKER = "-";
    String red = "<html><font color='#FF0000'>";
    String soft = "<html><font color='#666666'>";
    String dark = "<html><font color='#404040'>";
    String green = "<html><font color='#33FF33'>";
    String blue = "<html><font color='#6666DD'>";
    String white = "<html><font color='#BBBBBB'>";



    public ViewTOBRaid(Tob data)
    {

        setTitle("View Raid");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel thisSubPanel = new JPanel();
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

        JPanel maidenPanel = new JPanel();
        maidenPanel.setLayout(new BorderLayout());
        maidenPanel.setBorder(BorderFactory.createTitledBorder(maidenColor + "Maiden"/* + ((data.maidenScuffed) ? " (Scuffed after " + data.firstMaidenCrabScuffed + ")" : "")*/)); //todo scuffed!

        JPanel maidenSubPanel = new JPanel();
        GridLayout gl = new GridLayout(10, 2);
        maidenSubPanel.setLayout(gl);

        maidenSubPanel.add(new JLabel(maidenBodyColor + "Blood Spawned (thrown)"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + data.get(DataPoint.MAIDEN_BLOOD_SPAWNED) + " (" + data.get(DataPoint.MAIDEN_BLOOD_THROWN) + ")"));


        maidenSubPanel.add(new JLabel(maidenBodyColor + "Defense"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + ((data.getDefenseAccurate(MAIDEN)) ? data.get(DataPoint.DEFENSE, MAIDEN) : INCOMPLETE_MARKER)));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "Crabs leaked"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + data.get(DataPoint.MAIDEN_CRABS_LEAKED) + ", HP: " + data.get(DataPoint.MAIDEN_HP_HEALED)));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "100% leaked"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + data.get(DataPoint.MAIDEN_CRABS_LEAKED_FULL_HP)));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "Scuffed?"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + (/*(data.maidenScuffed) ? data.firstMaidenCrabScuffed : */"No"))); //todo scuffed

        maidenSubPanel.add(new JLabel(maidenBodyColor + "Deaths"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + data.get(DataPoint.DEATHS, MAIDEN)));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "70s"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + RoomUtil.time(data.get(DataPoint.MAIDEN_70_SPLIT))));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "50s"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + RoomUtil.time(data.get(DataPoint.MAIDEN_50_SPLIT)) + " (" + RoomUtil.time(data.get(DataPoint.MAIDEN_50_SPLIT) - data.get(DataPoint.MAIDEN_70_SPLIT)) + ")"));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "30s"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + RoomUtil.time(data.get(DataPoint.MAIDEN_30_SPLIT)) + " (" + RoomUtil.time(data.get(DataPoint.MAIDEN_30_SPLIT) - data.get(DataPoint.MAIDEN_50_SPLIT)) + ")"));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "Room time"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + RoomUtil.time(data.get(MAIDEN_TIME)) + " (" + RoomUtil.time(data.get(MAIDEN_TIME) - data.get(DataPoint.MAIDEN_30_SPLIT)) + ")"));


        JPanel bloatPanel = new JPanel();
        bloatPanel.setLayout(new BorderLayout());
        bloatPanel.setBorder(BorderFactory.createTitledBorder(bloatColor + "Bloat - " + bloatBodyColor + nyloEntryStr + " (" + bloatSplitStr + ")"));

        JPanel nylocasPanel = new JPanel();
        nylocasPanel.setLayout(new BorderLayout());
        nylocasPanel.setBorder(BorderFactory.createTitledBorder(nyloColor + "Nylocas - " + nyloBodyColor + soteEntryStr + " (" + nyloSplitStr + ")"));

        JPanel sotetsegPanel = new JPanel();
        sotetsegPanel.setLayout(new BorderLayout());
        sotetsegPanel.setBorder(BorderFactory.createTitledBorder(soteColor + "Sotetseg - " + soteBodyColor + xarpEntryStr + " (" + soteSplitStr + ")"));

        JPanel xarpusPanel = new JPanel();
        xarpusPanel.setLayout(new BorderLayout());
        xarpusPanel.setBorder(BorderFactory.createTitledBorder(xarpColor + "Xarpus - " + xarpBodyColor + verzikEntryStr + " (" + xarpSplitStr + ")"));

        JPanel verzikPanel = new JPanel();
        verzikPanel.setLayout(new BorderLayout());
        verzikPanel.setBorder(BorderFactory.createTitledBorder(verzikColor + "Verzik"));


        JPanel bloatSubPanel = new JPanel();
        bloatSubPanel.setLayout(new GridLayout(8, 2));


        bloatSubPanel.add(new JLabel(bloatBodyColor + "Downs"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + data.get(DataPoint.BLOAT_DOWNS)));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "Deaths (1st walk)"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + data.get(DataPoint.BLOAT_FIRST_WALK_DEATHS)));


        bloatSubPanel.add(new JLabel(bloatBodyColor + "Deaths (Total)"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + data.get(DataPoint.DEATHS, BLOAT)));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "Defense (1st walk)"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + ((data.getDefenseAccurate(BLOAT)) ? String.valueOf(data.get(DataPoint.DEFENSE, BLOAT)) : INCOMPLETE_MARKER)));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "Scythes 1st walk"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + data.get(DataPoint.BLOAT_FIRST_WALK_SCYTHES)));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "HP % 1st down"));

        bloatSubPanel.add(new JLabel(bloatBodyColor + (((double) data.get(DataPoint.BLOAT_HP_FIRST_DOWN))) + "%"));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "1st down time"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + data.get(DataPoint.BLOAT_FIRST_DOWN_TIME)));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "Room time"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + RoomUtil.time(bloatSplit)));


        JPanel nylocasSubPanel = new JPanel();
        nylocasSubPanel.setLayout(new GridLayout(9, 2));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Stalls"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + data.get(DataPoint.NYLO_STALLS_PRE_20) + " " + data.get(DataPoint.NYLO_STALLS_POST_20) + " (" + (data.get(DataPoint.NYLO_STALLS_TOTAL)) + ")"));

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
        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Splits"));
        nylocasSubPanel.add(new JLabel(nyloSplits));

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

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Rotations"));
        nylocasSubPanel.add(new JLabel(nyloRotations));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Defense"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + ((data.getDefenseAccurate(NYLOCAS)) ? String.valueOf(data.get(DataPoint.DEFENSE, NYLOCAS)) : INCOMPLETE_MARKER)));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Deaths"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + data.get(DataPoint.DEATHS, NYLOCAS)));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Last wave"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + RoomUtil.time(data.get(DataPoint.NYLO_LAST_WAVE))));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Clean up"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + RoomUtil.time(data.get(NYLO_LAST_DEAD)) + " (" + RoomUtil.time(data.get(NYLO_LAST_DEAD) - data.get(DataPoint.NYLO_LAST_WAVE)) + ")"));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Boss Spawn"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + RoomUtil.time(data.get(DataPoint.NYLO_BOSS_SPAWN)) + " (" + RoomUtil.time(data.get(DataPoint.NYLO_BOSS_SPAWN) - data.get(NYLO_LAST_DEAD)) + ")"));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Time"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + RoomUtil.time(nyloSplit) + " (" + RoomUtil.time(nyloSplit- data.get(DataPoint.NYLO_BOSS_SPAWN)) + ")"));

        JPanel sotetsegSubPanel = new JPanel();
        sotetsegSubPanel.setLayout(new GridLayout(8, 2));

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "Hammers hit")); //todo sote specs maybe?
        sotetsegSubPanel.add(new JLabel(soteBodyColor + ((data.getDefenseAccurate(SOTETSEG)) ? data.get(DataPoint.SOTE_SPECS_P1) + " " + data.get(DataPoint.SOTE_SPECS_P2) + " " + data.get(DataPoint.SOTE_SPECS_P3) + " (" + (data.get(DataPoint.SOTE_SPECS_TOTAL)) + ")" : INCOMPLETE_MARKER)));

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "Deaths"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor + data.get(DataPoint.DEATHS, SOTETSEG))); //todo specific deaths

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "First Maze Start"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor + RoomUtil.time(data.get(SOTE_M1_SPLIT))));

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "First Maze End"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor + RoomUtil.time(data.get(SOTE_P2_SPLIT)) + " (" + RoomUtil.time(data.get(SOTE_M1_DURATION)) + ")"));

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "Second Maze Start"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor + RoomUtil.time(data.get(DataPoint.SOTE_M2_SPLIT)) + " (" + RoomUtil.time(data.get(SOTE_P2_DURATION)) + ")"));

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "Second Maze End"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor + RoomUtil.time(data.get(DataPoint.SOTE_P3_SPLIT)) + " (" + RoomUtil.time(data.get(SOTE_M2_DURATION)) + ")"));

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "Time"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor + RoomUtil.time(soteSplit) + " (" + RoomUtil.time(data.get(SOTE_P3_DURATION)) + ")"));

        JPanel xarpusSubPanel = new JPanel();
        xarpusSubPanel.setLayout(new GridLayout(8, 2));

        xarpusSubPanel.add(new JLabel(xarpBodyColor + "Defense")); //todo defense
        xarpusSubPanel.add(new JLabel(xarpBodyColor + ((data.getDefenseAccurate(XARPUS)) ? data.get(DataPoint.DEFENSE, XARPUS) : INCOMPLETE_MARKER)));

        xarpusSubPanel.add(new JLabel(xarpBodyColor + "Deaths"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor + data.get(DataPoint.DEATHS, XARPUS)));

        xarpusSubPanel.add(new JLabel(xarpBodyColor + "Healing"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor + data.get(DataPoint.XARP_HEALING)));

        xarpusSubPanel.add(new JLabel(xarpBodyColor + "Screech"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor + RoomUtil.time(data.get(DataPoint.XARP_SCREECH))));

        xarpusSubPanel.add(new JLabel(xarpBodyColor + "Time"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor + RoomUtil.time(xarpSplit) + " (" + RoomUtil.time(xarpSplit- data.get(DataPoint.XARP_SCREECH)) + ")"));

        xarpusSubPanel.add(new JLabel(""));
        xarpusSubPanel.add(new JLabel(""));

        xarpusSubPanel.add(new JLabel(""));
        xarpusSubPanel.add(new JLabel(""));

        xarpusSubPanel.add(new JLabel(""));
        xarpusSubPanel.add(new JLabel(""));

        JPanel verzikSubPanel = new JPanel();
        verzikSubPanel.setLayout(new GridLayout(8, 2));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Bounces"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + data.get(DataPoint.VERZIK_BOUNCES)));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Deaths"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + data.get(DataPoint.DEATHS, VERZIK)));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Crabs Spawned"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + data.get(DataPoint.VERZIK_CRABS_SPAWNED)));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Phase 1"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + RoomUtil.time(data.get(DataPoint.VERZIK_P2_SPLIT))));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Reds Proc"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + RoomUtil.time(data.get(DataPoint.VERZIK_REDS_SPLIT)) + " (" + RoomUtil.time(data.get(DataPoint.VERZIK_REDS_SPLIT) - data.get(DataPoint.VERZIK_P2_SPLIT)) + ")"));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Phase 2"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + RoomUtil.time(data.get(DataPoint.VERZIK_P3_SPLIT)) + " (" + RoomUtil.time(data.get(DataPoint.VERZIK_P2_DURATION)) + ")"));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Time"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + RoomUtil.time(data.get(DataPoint.VERZIK_TIME)) + " (" + RoomUtil.time(data.get(VERZIK_P3_DURATION)) + ")"));

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


        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        summaryPanel.setMinimumSize(new Dimension(100, 30));
        Calendar cal = Calendar.getInstance();
        cal.setTime(data.getDate());
        String dateString = (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR);
        JPanel summarySubPanel = new JPanel(new GridLayout(10, 1));
        summarySubPanel.add(new JLabel("Date: " + dateString));

        summarySubPanel.add(new JLabel("Scale: " + data.getScaleString()));
        setSummaryStatus(data, summarySubPanel);
        summarySubPanel.add(new JLabel("Time: " + RoomUtil.time(data.get(CHALLENGE_TIME))));
        summarySubPanel.add(new JLabel("Players:"));
        for (String player : data.getPlayers())
        {
            summarySubPanel.add(new JLabel("        " + player + " (" + data.get(DEATHS, player) + ")"));
        }

        summaryPanel.add(summarySubPanel);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 3));
        topPanel.add(summaryPanel);

        JPanel thrallsPanel = new JPanel();
        thrallsPanel.setBorder(BorderFactory.createTitledBorder("Thralls"));
        thrallsPanel.setLayout(new GridLayout(7, 2));



        thrallsPanel.add(new JLabel("Maiden Thrall Hits: "));
        thrallsPanel.add(new JLabel(String.valueOf(data.get(DataPoint.THRALL_ATTACKS, MAIDEN)), SwingConstants.RIGHT));

        thrallsPanel.add(new JLabel("Bloat Thrall Hits: "));
        thrallsPanel.add(new JLabel(String.valueOf(data.get(DataPoint.THRALL_ATTACKS, BLOAT)), SwingConstants.RIGHT));

        thrallsPanel.add(new JLabel("Nylo Thrall Hits: "));
        thrallsPanel.add(new JLabel(String.valueOf(data.get(DataPoint.THRALL_ATTACKS, NYLOCAS)), SwingConstants.RIGHT));

        thrallsPanel.add(new JLabel("Sotetseg Thrall Hits: "));
        thrallsPanel.add(new JLabel(String.valueOf(data.get(DataPoint.THRALL_ATTACKS, SOTETSEG)), SwingConstants.RIGHT));

        thrallsPanel.add(new JLabel("Xarpus Thrall Hits: "));
        thrallsPanel.add(new JLabel(String.valueOf(data.get(DataPoint.THRALL_ATTACKS, XARPUS)), SwingConstants.RIGHT));

        thrallsPanel.add(new JLabel("Verzik Thrall Hits: "));
        thrallsPanel.add(new JLabel(String.valueOf(data.get(DataPoint.THRALL_ATTACKS, VERZIK)), SwingConstants.RIGHT));


        JPanel vengPanel = new JPanel();
        vengPanel.setBorder(BorderFactory.createTitledBorder("Venges"));
        vengPanel.setLayout(new GridLayout(8, 2));

        vengPanel.add(new JLabel("Venges"));
        vengPanel.add(new JLabel("(Procced/Cast): Damage"));

        vengPanel.add(new JLabel("All Rooms"));
        vengPanel.add(new JLabel("(" + data.get(DataPoint.VENG_PROCS) + "/" + data.get(DataPoint.VENG_CASTS) + "): " + data.get(DataPoint.VENG_DAMAGE), SwingConstants.RIGHT));

        vengPanel.add(new JLabel("Maiden"));
        vengPanel.add(new JLabel("(" + data.get(DataPoint.VENG_PROCS, MAIDEN) + "/" + data.get(DataPoint.VENG_CASTS, MAIDEN) + "): " + data.get(DataPoint.VENG_DAMAGE, MAIDEN), SwingConstants.RIGHT));

        vengPanel.add(new JLabel("Bloat"));
        vengPanel.add(new JLabel("(" + data.get(DataPoint.VENG_PROCS, BLOAT) + "/" + data.get(DataPoint.VENG_CASTS, BLOAT) + "): " + data.get(DataPoint.VENG_DAMAGE, BLOAT), SwingConstants.RIGHT));

        vengPanel.add(new JLabel("Nylo"));
        vengPanel.add(new JLabel("(" + data.get(DataPoint.VENG_PROCS, NYLOCAS) + "/" + data.get(DataPoint.VENG_CASTS, NYLOCAS) + "): " + data.get(DataPoint.VENG_DAMAGE, NYLOCAS), SwingConstants.RIGHT));

        vengPanel.add(new JLabel("Sote"));
        vengPanel.add(new JLabel("(" + data.get(DataPoint.VENG_PROCS, SOTETSEG) + "/" + data.get(DataPoint.VENG_CASTS, SOTETSEG) + "): " + data.get(DataPoint.VENG_DAMAGE, SOTETSEG), SwingConstants.RIGHT));

        vengPanel.add(new JLabel("Xarp"));
        vengPanel.add(new JLabel("(" + data.get(DataPoint.VENG_PROCS, XARPUS) + "/" + data.get(DataPoint.VENG_CASTS, XARPUS) + "): " + data.get(DataPoint.VENG_DAMAGE, XARPUS), SwingConstants.RIGHT));

        vengPanel.add(new JLabel("Verzik"));
        vengPanel.add(new JLabel("(" + data.get(DataPoint.VENG_PROCS, VERZIK) + "/" + data.get(DataPoint.VENG_CASTS, VERZIK) + "): " + data.get(DataPoint.VENG_DAMAGE, VERZIK), SwingConstants.RIGHT));

        topPanel.add(vengPanel);


        topPanel.add(thrallsPanel);


        add(topPanel);
        add(thisSubPanel);

        pack();
    }

    private String getColor(Raid data, RaidRoom room)
    {
        String color = red;
        if(data.getRoomPartiallyAccurate(room))
        {
            color = blue;
        }
        if(data.getRoomAccurate(room))
        {
            color = green;
        }
        return color;
    }

    private String getBodyColor(Raid data, RaidRoom room)
    {
        String color = white;
        if (!data.isAccurate())
        {
            color = data.getRoomPartiallyAccurate(room) ? soft : dark;
        }
        return color;
    }
/*
    private JLabel createLabel(String text, String color)
    {
        return new JLabel(color+text);
    }

    private JPanel createMaidenPanel(Tob data)
    {
        JPanel maidenPanel = new JPanel();
        maidenPanel.setLayout(new BorderLayout());
        String color = getColor(data, MAIDEN);
        String bodyColor = getBodyColor(data, MAIDEN);
        JPanel maidenSubPanel = new JPanel();
        GridLayout gl = new GridLayout(10, 2);
        maidenSubPanel.setLayout(gl);

        maidenSubPanel.add(createLabel("Blood Spawned (thrown)", bodyColor));
        maidenSubPanel.add(createLabel(data.get(DataPoint.MAIDEN_BLOOD_THROWN) + " (" + data.getBloodThrown() + ")", bodyColor));


        maidenSubPanel.add(createLabel("Defense", bodyColor));
        maidenSubPanel.add(createLabel(((data.isDefenceAccurate()) ? String.valueOf(data.getDefence()) : INCOMPLETE_MARKER), bodyColor));

        List<Integer> leaks = data.getCrabsLeaked();
        final int crabHealth;
        switch (raid.getScale()) {
            case 4:
                crabHealth = 87;
                break;

            case 5:
                crabHealth = 100;
                break;

            default: // 1, 2, 3
                crabHealth = 75;
                break;
        }
        maidenSubPanel.add(createLabel( "Crabs leaked", bodyColor));
        maidenSubPanel.add(createLabel(leaks.size() + ", HP: " + leaks.stream().reduce(Integer::sum), bodyColor));

        maidenSubPanel.add(createLabel("100% leaked", bodyColor));
        maidenSubPanel.add(createLabel(String.valueOf(data.getCrabsLeaked().stream().filter(health -> health == crabHealth).count()), bodyColor));

        maidenSubPanel.add(createLabel("Scuffed?", bodyColor));
        maidenSubPanel.add(createLabel(((data.isScuffed()) ? data.getScuffedAfter() : "No"), bodyColor));

        maidenSubPanel.add(createLabel("Deaths", bodyColor));
        maidenSubPanel.add(createLabel(String.valueOf(data.getDeaths().values().stream().reduce(Integer::sum)), bodyColor));

        Integer []splits = data.getSplits();
        maidenSubPanel.add(createLabel("70s", bodyColor));
        maidenSubPanel.add(createLabel(RoomUtil.time(splits[0]), bodyColor));

        maidenSubPanel.add(createLabel("50s", bodyColor));
        maidenSubPanel.add(createLabel(RoomUtil.time(splits[1]) + " (" + RoomUtil.time(splits[1] - splits[0]) + ")", bodyColor));

        maidenSubPanel.add(createLabel("30s", bodyColor));
        maidenSubPanel.add(createLabel(RoomUtil.time(splits[2]) + " (" + RoomUtil.time(splits[2] - splits[1]) + ")", bodyColor));

        maidenSubPanel.add(createLabel("Room time", bodyColor));
        maidenSubPanel.add(createLabel(RoomUtil.time(data.getTime()) + " (" + RoomUtil.time(data.getTime() - splits[2]) + ")", bodyColor));
        maidenPanel.setBorder(BorderFactory.createTitledBorder("Maiden" + ((data.isScuffed()) ? " (Scuffed after " + data.getScuffedAfter() + ")" : "")));
        maidenPanel.add(maidenSubPanel);
        return maidenPanel;
    }*/

    private static void setSummaryStatus(Tob data, JPanel summarySubPanel)
    {
        String raidStatusString = data.getRoomStatus();

        summarySubPanel.add(new JLabel("<html>Raid Status: " + raidStatusString));
    }
}
