package com.TheatreTracker.ui.viewraid;

import com.TheatreTracker.SimpleTOBData;
import com.TheatreTracker.ui.BaseFrame;
import com.TheatreTracker.utility.RoomUtil;
import com.TheatreTracker.utility.datautility.DataPoint;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

import static com.TheatreTracker.utility.datautility.DataPoint.NYLO_LAST_DEAD;

@Slf4j
public class ViewTOBRaid extends BaseFrame
{
    String INCOMPLETE_MARKER = "-";

    public ViewTOBRaid(SimpleTOBData data)
    {
        String red = "<html><font color='#FF0000'>";
        String soft = "<html><font color='#666666'>";
        String dark = "<html><font color='#404040'>";
        String green = "<html><font color='#33FF33'>";
        String blue = "<html><font color='#6666DD'>";
        String white = "<html><font color='#BBBBBB'>";

        setTitle("View Raid");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel thisSubPanel = new JPanel();
        thisSubPanel.setLayout(new GridLayout(2, 3));

        String maidenColor = (data.maidenTimeAccurate) ? green : (data.maidenStartAccurate || data.maidenEndAccurate) ? blue : red;
        String bloatColor = (data.bloatTimeAccurate) ? green : (data.bloatStartAccurate || data.bloatEndAccurate) ? blue : red;
        String nyloColor = (data.nyloTimeAccurate) ? green : (data.nyloStartAccurate || data.nyloEndAccurate) ? blue : red;
        String soteColor = (data.soteTimeAccurate) ? green : (data.soteStartAccurate || data.soteEndAccurate) ? blue : red;
        String xarpColor = (data.xarpTimeAccurate) ? green : (data.xarpStartAccurate || data.xarpEndAccurate) ? blue : red;
        String verzikColor = (data.verzikTimeAccurate) ? green : (data.verzikStartAccurate || data.verzikEndAccurate) ? blue : red;

        String maidenBodyColor = (data.maidenTimeAccurate) ? white : (data.maidenStartAccurate || data.maidenEndAccurate) ? soft : dark;
        String bloatBodyColor = (data.bloatTimeAccurate) ? white : (data.bloatStartAccurate || data.bloatEndAccurate) ? soft : dark;
        String nyloBodyColor = (data.nyloTimeAccurate) ? white : (data.nyloStartAccurate || data.nyloEndAccurate) ? soft : dark;
        String soteBodyColor = (data.soteTimeAccurate) ? white : (data.soteStartAccurate || data.soteEndAccurate) ? soft : dark;
        String xarpBodyColor = (data.xarpTimeAccurate) ? white : (data.xarpStartAccurate || data.xarpEndAccurate) ? soft : dark;
        String verzikBodyColor = (data.verzikTimeAccurate) ? white : (data.verzikStartAccurate || data.verzikEndAccurate) ? soft : dark;

        JPanel maidenPanel = new JPanel();
        maidenPanel.setLayout(new BorderLayout());

        int maidenEntry = 0;
        int maidenSplit = data.getMaidenTime();

        int bloatEntry = maidenEntry + maidenSplit;
        int bloatSplit = data.getBloatTime();

        int nyloEntry = bloatEntry + bloatSplit;
        int nyloSplit = data.getNyloTime();

        int soteEntry = nyloEntry + nyloSplit;
        int soteSplit = data.getSoteTime();

        int xarpEntry = soteEntry + soteSplit;
        int xarpSplit = data.getXarpTime();

        int verzikEntry = xarpEntry + xarpSplit;

        String bloatSplitStr = RoomUtil.time(bloatSplit);

        String nyloEntryStr = RoomUtil.time(nyloEntry);
        String nyloSplitStr = RoomUtil.time(nyloSplit);

        String soteEntryStr = RoomUtil.time(soteEntry);
        String soteSplitStr = RoomUtil.time(soteSplit);

        String xarpEntryStr = RoomUtil.time(xarpEntry);
        String xarpSplitStr = RoomUtil.time(xarpSplit);

        String verzikEntryStr = RoomUtil.time(verzikEntry);

        maidenPanel.setBorder(BorderFactory.createTitledBorder(maidenColor + "Maiden" + ((data.maidenScuffed) ? " (Scuffed after " + data.firstMaidenCrabScuffed + ")" : "")));

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

        JPanel maidenSubPanel = new JPanel();
        GridLayout gl = new GridLayout(10, 2);
        maidenSubPanel.setLayout(gl);

        maidenSubPanel.add(new JLabel(maidenBodyColor + "Blood Spawned (thrown)"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + data.getValue(DataPoint.MAIDEN_BLOOD_SPAWNED) + " (" + data.getValue(DataPoint.MAIDEN_BLOOD_THROWN) + ")"));


        maidenSubPanel.add(new JLabel(maidenBodyColor + "Defense"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + ((data.maidenDefenseAccurate) ? data.getValue(DataPoint.MAIDEN_DEFENSE) : INCOMPLETE_MARKER)));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "Crabs leaked"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + data.getValue(DataPoint.MAIDEN_CRABS_LEAKED) + ", HP: " + data.getValue(DataPoint.MAIDEN_HP_HEALED)));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "100% leaked"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + data.getValue(DataPoint.MAIDEN_CRABS_LEAKED_FULL_HP)));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "Scuffed?"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + ((data.maidenScuffed) ? data.firstMaidenCrabScuffed : "No")));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "Deaths"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + data.getValue(DataPoint.MAIDEN_DEATHS)));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "70s"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + RoomUtil.time(data.getValue(DataPoint.MAIDEN_70_SPLIT))));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "50s"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + RoomUtil.time(data.getValue(DataPoint.MAIDEN_50_SPLIT)) + " (" + RoomUtil.time(data.getValue(DataPoint.MAIDEN_50_SPLIT) - data.getValue(DataPoint.MAIDEN_70_SPLIT)) + ")"));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "30s"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + RoomUtil.time(data.getValue(DataPoint.MAIDEN_30_SPLIT)) + " (" + RoomUtil.time(data.getValue(DataPoint.MAIDEN_30_SPLIT) - data.getValue(DataPoint.MAIDEN_50_SPLIT)) + ")"));

        maidenSubPanel.add(new JLabel(maidenBodyColor + "Room time"));
        maidenSubPanel.add(new JLabel(maidenBodyColor + RoomUtil.time(data.getMaidenTime()) + " (" + RoomUtil.time(data.getMaidenTime() - data.getValue(DataPoint.MAIDEN_30_SPLIT)) + ")"));

        JPanel bloatSubPanel = new JPanel();
        bloatSubPanel.setLayout(new GridLayout(8, 2));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "Downs"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + data.getValue(DataPoint.BLOAT_DOWNS)));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "Deaths (1st walk)"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + data.getValue(DataPoint.BLOAT_FIRST_WALK_DEATHS)));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "Deaths (Total)"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + data.getValue(DataPoint.BLOAT_DEATHS)));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "Defense (1st walk)"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + ((data.bloatDefenseAccurate) ?String.valueOf(data.getValue(DataPoint.BLOAT_DEFENSE)) : INCOMPLETE_MARKER)));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "Scythes 1st walk"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + data.getValue(DataPoint.BLOAT_FIRST_WALK_SCYTHES)));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "HP % 1st down"));

        bloatSubPanel.add(new JLabel(bloatBodyColor + (((double) data.getValue(DataPoint.BLOAT_HP_FIRST_DOWN))) + "%"));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "1st down time"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + data.getValue(DataPoint.BLOAT_FIRST_DOWN_TIME)));

        bloatSubPanel.add(new JLabel(bloatBodyColor + "Room time"));
        bloatSubPanel.add(new JLabel(bloatBodyColor + RoomUtil.time(data.getBloatTime())));


        JPanel nylocasSubPanel = new JPanel();
        nylocasSubPanel.setLayout(new GridLayout(9, 2));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Stalls"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + data.getValue(DataPoint.NYLO_STALLS_PRE_20) + " " + data.getValue(DataPoint.NYLO_STALLS_POST_20) + " (" + (data.getValue(DataPoint.NYLO_STALLS_TOTAL)) + ")"));

        String nyloSplits = "<html><font color='#999999'>"
                + data.getValue(DataPoint.NYLO_SPLITS_MELEE)
                + "<font color='#00AA00'> "
                + data.getValue(DataPoint.NYLO_SPLITS_RANGE)
                + "<font color ='#2299FF'> "
                + data.getValue(DataPoint.NYLO_SPLITS_MAGE);
        if (nyloBodyColor.equals(dark))
        {
            nyloSplits = nyloBodyColor
                    + data.getValue(DataPoint.NYLO_SPLITS_MELEE) + " "
                    + data.getValue(DataPoint.NYLO_SPLITS_RANGE) + " "
                    + data.getValue(DataPoint.NYLO_SPLITS_MAGE);
        } else if (nyloBodyColor.equals(soft))
        {
            nyloSplits = "<html><font color='#444444'>"
                    + data.getValue(DataPoint.NYLO_SPLITS_MELEE) + "<font color='#008800'> "
                    + data.getValue(DataPoint.NYLO_SPLITS_RANGE) + "<font color ='#0066CC'> "
                    + data.getValue(DataPoint.NYLO_SPLITS_MAGE);
        }
        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Splits"));
        nylocasSubPanel.add(new JLabel(nyloSplits));

        String nyloRotations = "<html><font color='#999999'>"
                + data.getValue(DataPoint.NYLO_ROTATIONS_MELEE) + "<font color='#00AA00'> "
                + data.getValue(DataPoint.NYLO_ROTATIONS_RANGE) + "<font color ='#2299FF'> "
                + data.getValue(DataPoint.NYLO_ROTATIONS_MAGE);

        if (nyloBodyColor.equals(dark))
        {
            nyloRotations = nyloBodyColor
                    + data.getValue(DataPoint.NYLO_ROTATIONS_MELEE) + " "
                    + data.getValue(DataPoint.NYLO_ROTATIONS_RANGE) + " "
                    + data.getValue(DataPoint.NYLO_ROTATIONS_MAGE);
        } else if (nyloBodyColor.equals(soft))
        {
            nyloRotations = "<html><font color='#444444'>"
                    + data.getValue(DataPoint.NYLO_ROTATIONS_MELEE) + "<font color='#008800'> "
                    + data.getValue(DataPoint.NYLO_ROTATIONS_RANGE) + "<font color ='#0066CC'> "
                    + data.getValue(DataPoint.NYLO_ROTATIONS_MAGE);
        }

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Rotations"));
        nylocasSubPanel.add(new JLabel(nyloRotations));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Defense"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + ((data.nyloDefenseAccurate) ? String.valueOf(data.getValue(DataPoint.NYLO_DEFENSE)) : INCOMPLETE_MARKER)));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Deaths"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + data.getValue(DataPoint.NYLO_DEATHS)));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Last wave"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + RoomUtil.time(data.getValue(DataPoint.NYLO_LAST_WAVE))));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Clean up"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + RoomUtil.time(data.getValue(NYLO_LAST_DEAD)) + " (" + RoomUtil.time(data.getValue(NYLO_LAST_DEAD) - data.getValue(DataPoint.NYLO_LAST_WAVE)) + ")"));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Boss Spawn"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + RoomUtil.time(data.getValue(DataPoint.NYLO_BOSS_SPAWN)) + " (" + RoomUtil.time(data.getValue(DataPoint.NYLO_BOSS_SPAWN) - data.getValue(NYLO_LAST_DEAD)) + ")"));

        nylocasSubPanel.add(new JLabel(nyloBodyColor + "Time"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor + RoomUtil.time(data.getNyloTime()) + " (" + RoomUtil.time(data.getNyloTime() - data.getValue(DataPoint.NYLO_BOSS_SPAWN)) + ")"));

        JPanel sotetsegSubPanel = new JPanel();
        sotetsegSubPanel.setLayout(new GridLayout(8, 2));

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "Hammers hit"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor + ((data.soteDefenseAccurate) ? data.getValue(DataPoint.SOTE_SPECS_P1) + " " + data.getValue(DataPoint.SOTE_SPECS_P2) + " " + data.getValue(DataPoint.SOTE_SPECS_P3) + " (" + (data.getValue(DataPoint.SOTE_SPECS_TOTAL)) + ")" : INCOMPLETE_MARKER)));

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "Deaths"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor + data.getValue(DataPoint.SOTE_DEATHS)));

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "First Maze Start"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor + RoomUtil.time(data.getValue(DataPoint.SOTE_P1_SPLIT))));

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "First Maze End"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor + RoomUtil.time(data.getValue(DataPoint.SOTE_M1_SPLIT)) + " (" + RoomUtil.time(data.getValue(DataPoint.SOTE_M1_SPLIT) - data.getValue(DataPoint.SOTE_P1_SPLIT)) + ")"));

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "Second Maze Start"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor + RoomUtil.time(data.getValue(DataPoint.SOTE_P2_SPLIT)) + " (" + RoomUtil.time(data.getValue(DataPoint.SOTE_P2_SPLIT) - data.getValue(DataPoint.SOTE_M1_SPLIT)) + ")"));

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "Second Maze End"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor + RoomUtil.time(data.getValue(DataPoint.SOTE_M2_SPLIT)) + " (" + RoomUtil.time(data.getValue(DataPoint.SOTE_M2_SPLIT) - data.getValue(DataPoint.SOTE_P2_SPLIT)) + ")"));

        sotetsegSubPanel.add(new JLabel(soteBodyColor + "Time"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor + RoomUtil.time(data.getSoteTime()) + " (" + RoomUtil.time(data.getSoteTime() - data.getValue(DataPoint.SOTE_M2_SPLIT)) + ")"));

        JPanel xarpusSubPanel = new JPanel();
        xarpusSubPanel.setLayout(new GridLayout(8, 2));

        xarpusSubPanel.add(new JLabel(xarpBodyColor + "Defense"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor + ((data.xarpDefenseAccurate) ? data.getValue(DataPoint.XARP_DEFENSE) : INCOMPLETE_MARKER)));

        xarpusSubPanel.add(new JLabel(xarpBodyColor + "Deaths"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor + data.getValue(DataPoint.XARP_DEATHS)));

        xarpusSubPanel.add(new JLabel(xarpBodyColor + "Healing"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor + data.getValue(DataPoint.XARP_HEALING)));

        xarpusSubPanel.add(new JLabel(xarpBodyColor + "Screech"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor + RoomUtil.time(data.getValue(DataPoint.XARP_SCREECH))));

        xarpusSubPanel.add(new JLabel(xarpBodyColor + "Time"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor + RoomUtil.time(data.getXarpTime()) + " (" + RoomUtil.time(data.getXarpTime() - data.getValue(DataPoint.XARP_SCREECH)) + ")"));

        xarpusSubPanel.add(new JLabel(""));
        xarpusSubPanel.add(new JLabel(""));

        xarpusSubPanel.add(new JLabel(""));
        xarpusSubPanel.add(new JLabel(""));

        xarpusSubPanel.add(new JLabel(""));
        xarpusSubPanel.add(new JLabel(""));

        JPanel verzikSubPanel = new JPanel();
        verzikSubPanel.setLayout(new GridLayout(8, 2));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Bounces"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + data.getValue(DataPoint.VERZIK_BOUNCES)));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Deaths"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + data.getValue(DataPoint.VERZIK_DEATHS)));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Crabs Spawned"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + data.getValue(DataPoint.VERZIK_CRABS_SPAWNED)));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Phase 1"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + RoomUtil.time(data.getValue(DataPoint.VERZIK_P1_SPLIT))));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Reds Proc"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + RoomUtil.time(data.getValue(DataPoint.VERZIK_REDS_SPLIT)) + " (" + RoomUtil.time(data.getValue(DataPoint.VERZIK_REDS_SPLIT) - data.getValue(DataPoint.VERZIK_P1_SPLIT)) + ")"));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Phase 2"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + RoomUtil.time(data.getValue(DataPoint.VERZIK_P2_SPLIT)) + " (" + RoomUtil.time(data.getValue(DataPoint.VERZIK_P2_SPLIT) - data.getValue(DataPoint.VERZIK_P1_SPLIT)) + ")"));

        verzikSubPanel.add(new JLabel(verzikBodyColor + "Time"));
        verzikSubPanel.add(new JLabel(verzikBodyColor + RoomUtil.time(data.getVerzikTime()) + " (" + RoomUtil.time(data.getVerzikTime() - data.getValue(DataPoint.VERZIK_P2_SPLIT)) + ")"));

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
        cal.setTime(data.raidStarted);
        String dateString = (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR);
        JPanel summarySubPanel = new JPanel(new GridLayout(10, 1));
        summarySubPanel.add(new JLabel("Date: " + dateString));
        String scaleString = "";
        switch (data.getScale())
        {
            case 1:
                scaleString = "Solo";
                break;
            case 2:
                scaleString = "Duo";
                break;
            case 3:
                scaleString = "Trio";
                break;
            case 4:
                scaleString = "4 Man";
                break;
            case 5:
                scaleString = "5 Man";
                break;
        }
        summarySubPanel.add(new JLabel("Scale: " + scaleString));
        setSummaryStatus(data, summarySubPanel);
        summarySubPanel.add(new JLabel("Time: " + RoomUtil.time(data.getTimeSum())));
        summarySubPanel.add(new JLabel("Players:"));
        for (String player : data.players.keySet())
        {
            summarySubPanel.add(new JLabel("        " + player + " (" + data.players.get(player) + ")"));
        }

        summaryPanel.add(summarySubPanel);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 3));
        topPanel.add(summaryPanel);

        JPanel thrallsPanel = new JPanel();
        thrallsPanel.setBorder(BorderFactory.createTitledBorder("Thralls"));
        thrallsPanel.setLayout(new GridLayout(7, 2));

        thrallsPanel.add(new JLabel("Total Thrall Hits: "));
        thrallsPanel.add(new JLabel(String.valueOf(data.getValue(DataPoint.THRALL_ATTACKS_TOTAL)), SwingConstants.RIGHT));

        thrallsPanel.add(new JLabel("Maiden Thrall Hits: "));
        thrallsPanel.add(new JLabel(String.valueOf(data.getValue(DataPoint.THRALL_ATTACKS_MAIDEN)), SwingConstants.RIGHT));

        thrallsPanel.add(new JLabel("Bloat Thrall Hits: "));
        thrallsPanel.add(new JLabel(String.valueOf(data.getValue(DataPoint.THRALL_ATTACKS_BLOAT)), SwingConstants.RIGHT));

        thrallsPanel.add(new JLabel("Nylo Thrall Hits: "));
        thrallsPanel.add(new JLabel(String.valueOf(data.getValue(DataPoint.THRALL_ATTACKS_NYLO)), SwingConstants.RIGHT));

        thrallsPanel.add(new JLabel("Sotetseg Thrall Hits: "));
        thrallsPanel.add(new JLabel(String.valueOf(data.getValue(DataPoint.THRALL_ATTACKS_SOTE)), SwingConstants.RIGHT));

        thrallsPanel.add(new JLabel("Xarpus Thrall Hits: "));
        thrallsPanel.add(new JLabel(String.valueOf(data.getValue(DataPoint.THRALL_ATTACKS_XARP)), SwingConstants.RIGHT));

        thrallsPanel.add(new JLabel("Verzik Thrall Hits: "));
        thrallsPanel.add(new JLabel(String.valueOf(data.getValue(DataPoint.THRALL_ATTACKS_VERZIK)), SwingConstants.RIGHT));


        JPanel vengPanel = new JPanel();
        vengPanel.setBorder(BorderFactory.createTitledBorder("Venges"));
        vengPanel.setLayout(new GridLayout(8, 2));

        vengPanel.add(new JLabel("Venges"));
        vengPanel.add(new JLabel("(Procced/Cast): Damage"));

        vengPanel.add(new JLabel("All Rooms"));
        vengPanel.add(new JLabel("(" + data.getValue(DataPoint.VENG_PROCS_TOTAL) + "/" + data.getValue(DataPoint.VENG_CASTS_TOTAL) + "): " + data.getValue(DataPoint.VENG_DAMAGE_TOTAL), SwingConstants.RIGHT));

        vengPanel.add(new JLabel("Maiden"));
        vengPanel.add(new JLabel("(" + data.getValue(DataPoint.VENG_PROCS_MAIDEN) + "/" + data.getValue(DataPoint.VENG_CASTS_MAIDEN) + "): " + data.getValue(DataPoint.VENG_DAMAGE_MAIDEN), SwingConstants.RIGHT));

        vengPanel.add(new JLabel("Bloat"));
        vengPanel.add(new JLabel("(" + data.getValue(DataPoint.VENG_PROCS_BLOAT) + "/" + data.getValue(DataPoint.VENG_CASTS_BLOAT) + "): " + data.getValue(DataPoint.VENG_DAMAGE_BLOAT), SwingConstants.RIGHT));

        vengPanel.add(new JLabel("Nylo"));
        vengPanel.add(new JLabel("(" + data.getValue(DataPoint.VENG_PROCS_NYLO) + "/" + data.getValue(DataPoint.VENG_CASTS_NYLO) + "): " + data.getValue(DataPoint.VENG_DAMAGE_NYLO), SwingConstants.RIGHT));

        vengPanel.add(new JLabel("Sote"));
        vengPanel.add(new JLabel("(" + data.getValue(DataPoint.VENG_PROCS_SOTE) + "/" + data.getValue(DataPoint.VENG_CASTS_SOTE) + "): " + data.getValue(DataPoint.VENG_DAMAGE_SOTE), SwingConstants.RIGHT));

        vengPanel.add(new JLabel("Xarp"));
        vengPanel.add(new JLabel("(" + data.getValue(DataPoint.VENG_PROCS_XARP) + "/" + data.getValue(DataPoint.VENG_CASTS_XARP) + "): " + data.getValue(DataPoint.VENG_DAMAGE_XARP), SwingConstants.RIGHT));

        vengPanel.add(new JLabel("Verzik"));
        vengPanel.add(new JLabel("(" + data.getValue(DataPoint.VENG_PROCS_VERZIK) + "/" + data.getValue(DataPoint.VENG_CASTS_VERZIK) + "): " + data.getValue(DataPoint.VENG_DAMAGE_VERZIK), SwingConstants.RIGHT));

        topPanel.add(thrallsPanel);
        topPanel.add(vengPanel);

        add(topPanel);
        add(thisSubPanel);

        pack();
    }

    private static void setSummaryStatus(SimpleTOBData data, JPanel summarySubPanel)
    {
        String raidStatusString;
        if (data.maidenWipe)
        {
            raidStatusString = "Maiden Wipe";
        } else if (data.maidenReset)
        {
            raidStatusString = "Maiden Reset";
        } else if (data.bloatWipe)
        {
            raidStatusString = "Bloat Wipe";
        } else if (data.bloatReset)
        {
            raidStatusString = "Bloat Reset";
        } else if (data.nyloWipe)
        {
            raidStatusString = "Nylo Wipe";
        } else if (data.nyloReset)
        {
            raidStatusString = "Nylo Reset";
        } else if (data.soteWipe)
        {
            raidStatusString = "Sotetseg Wipe";
        } else if (data.soteReset)
        {
            raidStatusString = "Sotetseg Reset";
        } else if (data.xarpWipe)
        {
            raidStatusString = "Xarpus Wipe";
        } else if (data.xarpReset)
        {
            raidStatusString = "Xarpus Reset";
        } else if (data.verzikWipe)
        {
            raidStatusString = "Verzik Wipe";
        } else
        {
            raidStatusString = "Completion";
        }
        summarySubPanel.add(new JLabel("Raid Status: " + raidStatusString));
    }
}
