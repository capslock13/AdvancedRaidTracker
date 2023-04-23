package com.cTimers.panelcomponents;

import com.cTimers.cRoomData;
import com.cTimers.utility.RoomUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class cViewRaidFrame extends cFrame
{

    public cViewRaidFrame(cRoomData data)
    {
        String red = "<html><font color='#FF0000'>";
        String soft = "<html><font color='#666666'>";
        String dark = "<html><font color='#404040'>";
        String green = "<html><font color='#33FF33'>";
        String blue = "<html><font color='#6666DD'>";
        String white = "";

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
        maidenPanel.setBorder(BorderFactory.createTitledBorder(maidenColor + "Maiden"));

        JPanel bloatPanel = new JPanel();
        bloatPanel.setLayout(new BorderLayout());
        bloatPanel.setBorder(BorderFactory.createTitledBorder(bloatColor + "Bloat"));

        JPanel nylocasPanel = new JPanel();
        nylocasPanel.setLayout(new BorderLayout());
        nylocasPanel.setBorder(BorderFactory.createTitledBorder(nyloColor + "Nylocas"));

        JPanel sotetsegPanel = new JPanel();
        sotetsegPanel.setLayout(new BorderLayout());
        sotetsegPanel.setBorder(BorderFactory.createTitledBorder(soteColor + "Sotetseg"));

        JPanel xarpusPanel = new JPanel();
        xarpusPanel.setLayout(new BorderLayout());
        xarpusPanel.setBorder(BorderFactory.createTitledBorder(xarpColor + "Xarpus"));

        JPanel verzikPanel = new JPanel();
        verzikPanel.setLayout(new BorderLayout());
        verzikPanel.setBorder(BorderFactory.createTitledBorder(verzikColor + "Verzik"));

        JPanel maidenSubPanel = new JPanel();
        GridLayout gl = new GridLayout(8, 2);
        maidenSubPanel.setLayout(gl);

        maidenSubPanel.add(new JLabel(maidenBodyColor + "Bloods"));
        maidenSubPanel.add(new JLabel(maidenBodyColor+data.maidenBloodsSpawned));


        maidenSubPanel.add(new JLabel(maidenBodyColor+"Defense"));
        maidenSubPanel.add(new JLabel(maidenBodyColor+((data.maidenDefenseAccurate) ? ""+ Math.max(0, data.maidenDefense) : "Party Incomplete")));

        maidenSubPanel.add(new JLabel(maidenBodyColor+"Crabs leaked"));
        maidenSubPanel.add(new JLabel(maidenBodyColor+data.maidenCrabsLeaked + ", HP: " + data.maidenHeal));

        //maidenSubPanel.add(new JLabel(maidenBodyColor+"100% Crabs leaked"));
        //maidenSubPanel.add(new JLabel(maidenBodyColor+data.getMaidenCrabsLeakedFullHP));

        //maidenSubPanel.add(new JLabel(maidenBodyColor+"Scuffed?"));
        //maidenSubPanel.add(new JLabel(maidenBodyColor+ ((data.maidenScuffed) ? data.firstMaidenCrabScuffed : "No")));

        maidenSubPanel.add(new JLabel(maidenBodyColor+"Deaths"));
        maidenSubPanel.add(new JLabel(maidenBodyColor+data.maidenDeaths));

        maidenSubPanel.add(new JLabel(maidenBodyColor+"70s"));
        maidenSubPanel.add(new JLabel(maidenBodyColor+RoomUtil.time(data.maiden70Split) + " (" + RoomUtil.time(data.maiden70Split) + ")"));

        maidenSubPanel.add(new JLabel(maidenBodyColor+"50s"));
        maidenSubPanel.add(new JLabel(maidenBodyColor+RoomUtil.time(data.maiden50Split) + " (" + RoomUtil.time(data.maiden50Split-data.maiden70Split) + ")"));

        maidenSubPanel.add(new JLabel(maidenBodyColor+"30s"));
        maidenSubPanel.add(new JLabel(maidenBodyColor+RoomUtil.time(data.maiden30Split) + " (" + RoomUtil.time(data.maiden30Split-data.maiden50Split)+")"));

        maidenSubPanel.add(new JLabel(maidenBodyColor+"Room time"));
        maidenSubPanel.add(new JLabel(maidenBodyColor+RoomUtil.time(data.maidenTime) + " (" + RoomUtil.time(data.maidenTime-data.maiden30Split) + ")"));

        JPanel bloatSubPanel = new JPanel();
        bloatSubPanel.setLayout(new GridLayout(8, 2));

        bloatSubPanel.add(new JLabel(bloatBodyColor+"Downs"));
        bloatSubPanel.add(new JLabel(bloatBodyColor+data.bloatDowns));

        bloatSubPanel.add(new JLabel(bloatBodyColor+"Deaths (1st walk)"));
        bloatSubPanel.add(new JLabel(bloatBodyColor+data.bloatFirstWalkDeaths));

        bloatSubPanel.add(new JLabel(bloatBodyColor+"Deaths (Total)"));
        bloatSubPanel.add(new JLabel(bloatBodyColor+data.bloatDeaths));

        bloatSubPanel.add(new JLabel(bloatBodyColor+"Defense (1st walk)"));
        bloatSubPanel.add(new JLabel(bloatBodyColor+((data.bloatDefenseAccurate) ? "" +data.bloatfirstWalkDefense : "Party Incomplete")));

        bloatSubPanel.add(new JLabel(bloatBodyColor+"Scythes 1st walk"));
        bloatSubPanel.add(new JLabel(bloatBodyColor+data.bloatScytheBeforeFirstDown));

        bloatSubPanel.add(new JLabel(bloatBodyColor+"HP % 1st down"));

        bloatSubPanel.add(new JLabel(bloatBodyColor+(((double)data.bloatHPAtDown)/10.0)+"%"));

        bloatSubPanel.add(new JLabel(bloatBodyColor+"1st down time"));
        bloatSubPanel.add(new JLabel(bloatBodyColor+data.bloatFirstDownSplit));

        bloatSubPanel.add(new JLabel(bloatBodyColor+"Room time"));
        bloatSubPanel.add(new JLabel(bloatBodyColor+RoomUtil.time(data.bloatTime)));


        JPanel nylocasSubPanel = new JPanel();
        nylocasSubPanel.setLayout(new GridLayout(9, 2));

        nylocasSubPanel.add(new JLabel(nyloBodyColor+"Stalls"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor+data.nyloStallsPre20 + " " + data.nyloStallsPost20 + " (" + (data.nyloStallsPre20+data.nyloStallsPost20) + ")"));

        String nyloSplits = "<html><font color='#999999'>"+data.nyloMeleeSplits+"<font color='#00AA00'> "+data.nyloRangeSplits+"<font color ='#2299FF'> " + data.nyloMageSplits;
        if(nyloBodyColor.equals(dark))
        {
            nyloSplits = nyloBodyColor+data.nyloMeleeSplits+" "+data.nyloRangeSplits+" " + data.nyloMageSplits;
        }
        else if(nyloBodyColor.equals(soft))
        {
            nyloSplits = "<html><font color='#444444'>"+data.nyloMeleeSplits+"<font color='#008800'> "+data.nyloRangeSplits+"<font color ='#0066CC'> " + data.nyloMageSplits;
        }
        nylocasSubPanel.add(new JLabel(nyloBodyColor+"Splits"));
        nylocasSubPanel.add(new JLabel(nyloSplits));

        String nyloRotations = "<html><font color='#999999'>"+data.nyloMeleeRotations+"<font color='#00AA00'> "+data.nyloRangeRotations+"<font color ='#2299FF'> " + data.nyloMageRotations;
        if(nyloBodyColor.equals(dark))
        {
            nyloRotations = nyloBodyColor+data.nyloMeleeRotations+" "+data.nyloRangeRotations+" " + data.nyloMageRotations;
        }
        else if(nyloBodyColor.equals(soft))
        {
            nyloRotations = "<html><font color='#444444'>"+data.nyloMeleeRotations+"<font color='#008800'> "+data.nyloRangeRotations+"<font color ='#0066CC'> " + data.nyloMageRotations;
        }

        nylocasSubPanel.add(new JLabel(nyloBodyColor+"Rotations"));
        nylocasSubPanel.add(new JLabel(nyloRotations));

        nylocasSubPanel.add(new JLabel(nyloBodyColor+"Defense"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor+((data.nyloDefenseAccurate) ? "" + Math.max(0, (50-data.nyloDefenseReduction)) : "Party Incomplete")));

        nylocasSubPanel.add(new JLabel(nyloBodyColor+"Deaths"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor+data.nyloDeaths));

        nylocasSubPanel.add(new JLabel(nyloBodyColor+"Last wave"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor+RoomUtil.time(data.nyloLastWave)));

        nylocasSubPanel.add(new JLabel(nyloBodyColor+"Clean up"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor+RoomUtil.time(data.nyloLastDead) + " (" + RoomUtil.time(data.nyloLastDead-data.nyloLastWave) + ")"));

        nylocasSubPanel.add(new JLabel(nyloBodyColor+"Boss Spawn"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor+RoomUtil.time(data.nyloBossSpawn) + " (" + RoomUtil.time(data.nyloBossSpawn-data.nyloLastDead) + ")"));

        nylocasSubPanel.add(new JLabel(nyloBodyColor+"Time"));
        nylocasSubPanel.add(new JLabel(nyloBodyColor+RoomUtil.time(data.nyloTime) + " (" + RoomUtil.time(data.nyloTime-data.nyloBossSpawn) + ")"));

        JPanel sotetsegSubPanel = new JPanel();
        sotetsegSubPanel.setLayout(new GridLayout(8, 2));

        sotetsegSubPanel.add(new JLabel(soteBodyColor+"Hammers hit"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor+((data.soteDefenseAccurate) ? ""  + data.soteSpecsP1 + " " + data.soteSpecsP2 + " " + data.soteSpecsP3 + " (" +(data.soteSpecsP1+data.soteSpecsP2+data.soteSpecsP3)+")" : "Party Incomplete")));

        sotetsegSubPanel.add(new JLabel(soteBodyColor+"Deaths"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor+data.soteDeaths));

        sotetsegSubPanel.add(new JLabel(soteBodyColor+"First Maze Start"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor+RoomUtil.time(data.soteFirstMazeStartSplit) + " (" + RoomUtil.time(data.soteFirstMazeStartSplit) + ")"));

        sotetsegSubPanel.add(new JLabel(soteBodyColor+"First Maze End"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor+RoomUtil.time(data.soteFirstMazeEndSplit) + " (" + RoomUtil.time(data.soteFirstMazeEndSplit-data.soteFirstMazeStartSplit) + ")"));

        sotetsegSubPanel.add(new JLabel(soteBodyColor+"Second Maze Start"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor+RoomUtil.time(data.soteSecondMazeStartSplit) + " (" + RoomUtil.time(data.soteSecondMazeStartSplit-data.soteFirstMazeEndSplit) + ")"));

        sotetsegSubPanel.add(new JLabel(soteBodyColor+"Second Maze End"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor+RoomUtil.time(data.soteSecondMazeEndSplit) + " (" + RoomUtil.time(data.soteSecondMazeEndSplit-data.soteSecondMazeStartSplit) + ")"));

        sotetsegSubPanel.add(new JLabel(soteBodyColor+"Time"));
        sotetsegSubPanel.add(new JLabel(soteBodyColor+RoomUtil.time(data.soteTime) + " (" + RoomUtil.time(data.soteTime-data.soteSecondMazeEndSplit) + ")"));

        JPanel xarpusSubPanel = new JPanel();
        xarpusSubPanel.setLayout(new GridLayout(8, 2));

        xarpusSubPanel.add(new JLabel(xarpBodyColor+"Defense"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor+((data.xarpDefenseAccurate) ? ""+ Math.max(0, data.xarpDefense) : "Party Incomplete")));

        xarpusSubPanel.add(new JLabel(xarpBodyColor+"Deaths"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor+data.xarpDeaths));

        xarpusSubPanel.add(new JLabel(xarpBodyColor+"Healing"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor+data.xarpHealing));

        xarpusSubPanel.add(new JLabel(xarpBodyColor+"Screech"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor+RoomUtil.time(data.xarpScreechSplit) + " (" + RoomUtil.time(data.xarpScreechSplit) + ")"));

        xarpusSubPanel.add(new JLabel(xarpBodyColor+"Time"));
        xarpusSubPanel.add(new JLabel(xarpBodyColor+RoomUtil.time(data.xarpTime) + " (" + RoomUtil.time(data.xarpTime-data.xarpScreechSplit) + ")"));

        xarpusSubPanel.add(new JLabel(""));
        xarpusSubPanel.add(new JLabel(""));

        xarpusSubPanel.add(new JLabel(""));
        xarpusSubPanel.add(new JLabel(""));

        xarpusSubPanel.add(new JLabel(""));
        xarpusSubPanel.add(new JLabel(""));

        JPanel verzikSubPanel = new JPanel();
        verzikSubPanel.setLayout(new GridLayout(8, 2));

        verzikSubPanel.add(new JLabel(verzikBodyColor+"Bounces"));
        verzikSubPanel.add(new JLabel(verzikBodyColor+data.verzikBounces));

        verzikSubPanel.add(new JLabel(verzikBodyColor+"Deaths"));
        verzikSubPanel.add(new JLabel(verzikBodyColor+data.verzikDeaths));

        verzikSubPanel.add(new JLabel(verzikBodyColor+"Crabs Spawned"));
        verzikSubPanel.add(new JLabel(verzikBodyColor+data.verzikCrabsSpawned+""));

        verzikSubPanel.add(new JLabel(verzikBodyColor+"Phase 1"));
        verzikSubPanel.add(new JLabel(verzikBodyColor+RoomUtil.time(data.verzikP1Split) + " (" + RoomUtil.time(data.verzikP1Split) + ")"));

        verzikSubPanel.add(new JLabel(verzikBodyColor+"Reds Proc"));
        verzikSubPanel.add(new JLabel(verzikBodyColor+RoomUtil.time(data.verzikRedsProc) + " (" + RoomUtil.time(data.verzikRedsProc-data.verzikP1Split) + ")"));

        verzikSubPanel.add(new JLabel(verzikBodyColor+"Phase 2"));
        verzikSubPanel.add(new JLabel(verzikBodyColor+RoomUtil.time(data.verzikP2Split) + " (" + RoomUtil.time(data.verzikP2Split-data.verzikP1Split) + ")"));

        verzikSubPanel.add(new JLabel(verzikBodyColor+"Time"));
        verzikSubPanel.add(new JLabel(verzikBodyColor+RoomUtil.time(data.verzikTime) + " (" + RoomUtil.time(data.verzikTime-data.verzikP2Split) + ")"));

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
        String dateString = (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR);
        JPanel summarySubPanel = new JPanel(new GridLayout(10, 1));
        summarySubPanel.add(new JLabel("Date: " + dateString));
        String scaleString = "";
        switch(data.raidTeamSize)
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
        String raidStatusString = "";
        if(data.maidenWipe)
        {
            raidStatusString = "Maiden Wipe";
        }
        else if(data.maidenReset)
        {
            raidStatusString = "Maiden Reset";
        }
        else if(data.bloatWipe)
        {
            raidStatusString = "Bloat Wipe";
        }
        else if(data.bloatReset)
        {
            raidStatusString = "Bloat Reset";
        }
        else if(data.nyloWipe)
        {
            raidStatusString = "Nylo Wipe";
        }
        else if(data.nyloReset)
        {
            raidStatusString = "Nylo Reset";
        }
        else if(data.soteWipe)
        {
            raidStatusString = "Sotetseg Wipe";
        }
        else if(data.soteReset)
        {
            raidStatusString = "Sotetseg Reset";
        }
        else if(data.xarpWipe)
        {
            raidStatusString = "Xarpus Wipe";
        }
        else if(data.xarpReset)
        {
            raidStatusString = "Xarpus Reset";
        }
        else if(data.verzikWipe)
        {
            raidStatusString = "Verzik Wipe";
        }
        else
        {
            raidStatusString = "Completion";
        }
        summarySubPanel.add(new JLabel("Raid Status: " + raidStatusString));
        summarySubPanel.add(new JLabel("Time: " + RoomUtil.time(data.maidenTime+data.bloatTime+data.nyloTime+data.soteTime+data.xarpTime+data.verzikTime)));
        summarySubPanel.add(new JLabel("Players: " + data.players.toString().substring(1, data.players.toString().length()-1)));
        summaryPanel.add(summarySubPanel);

        add(summaryPanel);
        add(thisSubPanel);

        pack();
    }
}
