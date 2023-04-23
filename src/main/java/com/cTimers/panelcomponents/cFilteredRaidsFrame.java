package com.cTimers.panelcomponents;


import lombok.extern.slf4j.Slf4j;
import com.cTimers.cRoomData;
import com.cTimers.utility.RoomUtil;
import com.cTimers.utility.cDebugHelper;
import com.cTimers.utility.cStatisticGatherer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

@Slf4j
public class cFilteredRaidsFrame extends cFrame
{
    public cFilteredRaidsFrame(ArrayList<cRoomData> data)
    {
        updateFrameData(data);
    }


    private JLabel raidsFoundLabel = new JLabel("", SwingConstants.LEFT);
    private JComboBox viewByRaidComboBox;

    private JComboBox sortOptionsBox;
    private JLabel overallPanelMaidenAverage = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelBloatAverage = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelNyloAverage = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelSoteAverage = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelXarpusAverage = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelVerzikAverage = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelOverallAverage = new JLabel("", SwingConstants.RIGHT);

    private JLabel overallPanelMaidenMedian = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelBloatMedian = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelNyloMedian = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelSoteMedian = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelXarpusMedian = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelVerzikMedian = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelOverallMedian = new JLabel("", SwingConstants.RIGHT);

    private JLabel overallPanelMaidenMinimum = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelBloatMinimum = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelNyloMinimum = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelSoteMinimum = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelXarpusMinimum = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelVerzikMinimum = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelOverallMinimum = new JLabel("", SwingConstants.RIGHT);

    private JLabel overallPanelMaidenMaximum = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelBloatMaximum = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelNyloMaximum = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelSoteMaximum = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelXarpusMaximum = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelVerzikMaximum = new JLabel("", SwingConstants.RIGHT);
    private JLabel overallPanelOverallMaximum = new JLabel("", SwingConstants.RIGHT);

    private JLabel resultsAverage = new JLabel("", SwingConstants.RIGHT);
    private JLabel resultsMedian = new JLabel("", SwingConstants.RIGHT);
    private JLabel resultsMode = new JLabel("", SwingConstants.RIGHT);
    private JLabel resultsMinimum = new JLabel("", SwingConstants.RIGHT);
    private JLabel resultsMaximum = new JLabel("", SwingConstants.RIGHT);

    private JLabel maidenAverage70 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenAverage7050 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenAverage50 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenAverage5030 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenAverage30 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenAverageSkip = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenAverageTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel maidenMedian70 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMedian7050 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMedian50 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMedian5030 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMedian30 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMedianSkip = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMedianTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel maidenMode70 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMode7050 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMode50 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMode5030 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMode30 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenModeSkip = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenModeTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel maidenMax70 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMax7050 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMax50 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMax5030 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMax30 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMaxSkip = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMaxTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel maidenMin70 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMin7050 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMin50 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMin5030 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMin30 = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMinSkip = new JLabel("", SwingConstants.RIGHT);
    private JLabel maidenMinTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel bloatAverageFirstDown = new JLabel("", SwingConstants.RIGHT);
    private JLabel bloatAverageTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel bloatMedianFirstDown = new JLabel("", SwingConstants.RIGHT);
    private JLabel bloatMedianTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel bloatModeFirstDown = new JLabel("", SwingConstants.RIGHT);
    private JLabel bloatModeTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel bloatMinFirstDown = new JLabel("", SwingConstants.RIGHT);
    private JLabel bloatMinTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel bloatMaxFirstDown = new JLabel("", SwingConstants.RIGHT);
    private JLabel bloatMaxTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel nyloAverageLastWave = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloAverageBossSpawn = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloAverageBossDuration = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloAverageTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel nyloMedianLastWave = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloMedianBossSpawn = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloMedianBossDuration = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloMedianTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel nyloModeLastWave = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloModeBossSpawn = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloModeBossDuration = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloModeTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel nyloMinLastWave = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloMinBossSpawn = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloMinBossDuration = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloMinTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel nyloMaxLastWave = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloMaxBossSpawn = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloMaxBossDuration = new JLabel("", SwingConstants.RIGHT);
    private JLabel nyloMaxTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel soteAverageP1 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteAverageM1= new JLabel("", SwingConstants.RIGHT);
    private JLabel soteAverageP2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteAverageM2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteAverageP3 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteAverageTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel soteMedianP1 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMedianM1= new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMedianP2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMedianM2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMedianP3 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMedianTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel soteModeP1 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteModeM1= new JLabel("", SwingConstants.RIGHT);
    private JLabel soteModeP2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteModeM2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteModeP3 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteModeTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel soteMinP1 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMinM1= new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMinP2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMinM2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMinP3 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMinTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel soteMaxP1 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMaxM1= new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMaxP2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMaxM2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMaxP3 = new JLabel("", SwingConstants.RIGHT);
    private JLabel soteMaxTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel xarpModeScreech = new JLabel("", SwingConstants.RIGHT);
    private JLabel xarpModeTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel xarpAverageScreech = new JLabel("", SwingConstants.RIGHT);
    private JLabel xarpAverageTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel xarpMedianScreech = new JLabel("", SwingConstants.RIGHT);
    private JLabel xarpMedianTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel xarpMaxScreech = new JLabel("", SwingConstants.RIGHT);
    private JLabel xarpMaxTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel xarpMinScreech = new JLabel("", SwingConstants.RIGHT);
    private JLabel xarpMinTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel verzikModeP1 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikModeP2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikModeP3Entry = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikModeP3 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikModeTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel verzikAverageP1 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikAverageP2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikAverageP3Entry = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikAverageP3 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikAverageTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel verzikMedianP1 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikMedianP2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikMedianP3Entry = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikMedianP3 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikMedianTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel verzikMinP1 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikMinP2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikMinP3Entry = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikMinP3 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikMinTime = new JLabel("", SwingConstants.RIGHT);

    private JLabel verzikMaxP1 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikMaxP2 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikMaxP3Entry = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikMaxP3 = new JLabel("", SwingConstants.RIGHT);
    private JLabel verzikMaxTime = new JLabel("", SwingConstants.RIGHT);

    public JComboBox statisticsBox;
    public JLabel customAverageLabel = new JLabel("", SwingConstants.RIGHT);
    public JLabel customMedianLabel = new JLabel("", SwingConstants.RIGHT);
    public JLabel customModeLabel = new JLabel("", SwingConstants.RIGHT);
    public JLabel customMinLabel = new JLabel("", SwingConstants.RIGHT);
    public JLabel customMaxLabel = new JLabel("", SwingConstants.RIGHT);
    JCheckBox filterSpectateOnly;
    JCheckBox filterInRaidOnly;
    JCheckBox filterCompletionOnly;
    JCheckBox filterWipeResetOnly;
    JComboBox filterComboBoxScale;
    JCheckBox filterCheckBoxScale;
    JCheckBox filterTodayOnly;
    JCheckBox filterPartyOnly;
    JCheckBox filter8;
    JTable table;

    private ArrayList<cRoomData> filteredData;

    JPanel container;

    public ArrayList<cRoomData> currentData;

    public cFilteredRaidsFrame()
    {
        this.setPreferredSize(new Dimension(900,600));
        if(cDebugHelper.raidDataSource == cDebugHelper.GENERATED)
        {
            ArrayList<cRoomData> data = new ArrayList<>();
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(1));
            data.add(new cRoomData(2));
            data.add(new cRoomData(3));
            updateFrameData(data);
        }
    }

    public void updateCustomStats(ArrayList<cRoomData> raids)
    {
        ArrayList<Integer> dataForCalc = new ArrayList<>();
        for(cRoomData raid : raids)
        {
            if(statisticsBox.getSelectedIndex() < 6)
            {
                if(!raid.maidenEndAccurate || !raid.maidenStartAccurate)
                {
                    continue;
                }
            }
            else if(statisticsBox.getSelectedIndex() < 13)
            {
                if(!raid.bloatStartAccurate || !raid.bloatEndAccurate)
                {
                    continue;
                }
            }
            else if(statisticsBox.getSelectedIndex() < 22)
            {
                if(!raid.nyloStartAccurate || !raid.nyloEndAccurate)
                {
                    continue;
                }
            }
            else if(statisticsBox.getSelectedIndex() < 27)
            {
                if(!raid.soteEndAccurate || !raid.soteStartAccurate)
                {
                    continue;
                }
            }
            else if(statisticsBox.getSelectedIndex() < 30)
            {
                if(!raid.xarpEndAccurate || !raid.xarpStartAccurate)
                {
                    continue;
                }
            }
            else
            {
                if(!raid.verzikStartAccurate || !raid.verzikEndAccurate)
                {
                    continue;
                }
            }
            switch(statisticsBox.getSelectedIndex())
            {
                case 1:
                    dataForCalc.add(raid.maidenBloodsSpawned);
                    break;
                case 2:
                    dataForCalc.add(raid.maidenDefense);
                    break;
                case 3:
                    dataForCalc.add(raid.maidenCrabsLeaked);
                    break;
                case 4:
                    dataForCalc.add(raid.getMaidenCrabsLeakedFullHP);
                    break;
                case 5:
                    dataForCalc.add(raid.maidenDeaths);
                    break;
                case 6:
                    dataForCalc.add(raid.bloatDowns);
                    break;
                case 7:
                    dataForCalc.add(raid.bloatFirstWalkDeaths);
                    break;
                case 8:
                    dataForCalc.add(raid.bloatDeaths);
                    break;
                case 9:
                    dataForCalc.add(raid.bloatfirstWalkDefense);
                    break;
                case 10:
                    dataForCalc.add(raid.bloatScytheBeforeFirstDown);
                    break;
                case 11:
                    dataForCalc.add(raid.bloatHPAtDown);
                    break;
                case 12:
                    dataForCalc.add(raid.bloatFirstDownSplit);
                    break;
                case 13:
                    dataForCalc.add(raid.nyloStallsPre20);
                    break;
                case 14:
                    dataForCalc.add(raid.nyloStallsPost20);
                    break;
                case 15:
                    dataForCalc.add(raid.nyloStallsTotal);
                    break;
                case 16:
                    dataForCalc.add(raid.nyloMeleeRotations);
                    break;
                case 17:
                    dataForCalc.add(raid.nyloRangeRotations);
                    break;
                case 18:
                    dataForCalc.add(raid.nyloMageRotations);
                    break;
                case 19:
                    dataForCalc.add(raid.nyloRangeRotations+raid.nyloMageRotations+raid.nyloMeleeRotations);
                    break;
                case 20:
                    dataForCalc.add(50-raid.nyloDefenseReduction);
                    break;
                case 21:
                    dataForCalc.add(raid.nyloDeaths);
                    break;
                case 22:
                    dataForCalc.add(raid.soteSpecsP1);
                    break;
                case 23:
                    dataForCalc.add(raid.soteSpecsP2);
                    break;
                case 24:
                    dataForCalc.add(raid.soteSpecsP3);
                    break;
                case 25:
                    dataForCalc.add(raid.soteSpecsTotal);
                    break;
                case 26:
                    dataForCalc.add(raid.soteDeaths);
                    break;
                case 27:
                    dataForCalc.add(raid.xarpDefense);
                    break;
                case 28:
                    dataForCalc.add(raid.xarpDeaths);
                    break;
                case 29:
                    dataForCalc.add(raid.xarpHealing);
                    break;
                case 30:
                    dataForCalc.add(raid.verzikBounces);
                    break;
                case 31:
                    dataForCalc.add(raid.verzikDeaths);
                    break;
                case 32:
                    dataForCalc.add(raid.verzikCrabsSpawned);
                    break;
                default:
                    break;
            }
        }
        if(dataForCalc.size() == 0)
        {
            return;
        }
        String avgStr = "" + +cStatisticGatherer.getGenericTimeAverage(dataForCalc);
        if(avgStr.length() > 5)
        {
            avgStr = avgStr.substring(0, 5);
        }

        String medStr = "" + +cStatisticGatherer.getGenericMedian(dataForCalc);
        if(medStr.length() > 5)
        {
            medStr = medStr.substring(0, 5);
        }

        String modStr = "" + +cStatisticGatherer.getGenericMode(dataForCalc);
        if(modStr.length() > 5)
        {
            modStr = modStr.substring(0, 5);
        }

        String minStr = "" + +cStatisticGatherer.getGenericTimeMin(dataForCalc);
        if(minStr.length() > 5)
        {
            minStr = minStr.substring(0, 5);
        }

        String maxStr = "" + +cStatisticGatherer.getGenericMax(dataForCalc);
        if(maxStr.length() > 5)
        {
            maxStr = maxStr.substring(0, 5);
        }
        customAverageLabel.setText(avgStr);
        customMedianLabel.setText(medStr);
        customModeLabel.setText(modStr);
        customMinLabel.setText(minStr);
        customMaxLabel.setText(maxStr);
    }

    public void updateTable()
    {
        int timeToDisplay = 0;
        ArrayList<cRoomData> tableData = new ArrayList<>();
        for(cRoomData data : currentData)
        {
            boolean shouldDataBeIncluded = true;
            if(filterSpectateOnly.isSelected())
            {
                if(!data.spectated)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(filterInRaidOnly.isSelected())
            {
                if(data.spectated)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(filterCompletionOnly.isSelected())
            {
                if(!data.raidCompleted)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(filterWipeResetOnly.isSelected())
            {
                if(data.raidCompleted)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(shouldDataBeIncluded && filterTodayOnly.isSelected())
            {
                shouldDataBeIncluded = false;
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(data.raidStarted);
                cal2.setTime(new Date(System.currentTimeMillis()));
                if(cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH))
                {
                    shouldDataBeIncluded = true;
                }
            }
            if(filterPartyOnly.isSelected())
            {
                if(!data.maidenDefenseAccurate || !data.bloatDefenseAccurate || !data.nyloDefenseAccurate || !data.soteDefenseAccurate || !data.xarpDefenseAccurate)
                {
                    shouldDataBeIncluded = false;
                }
            }
            if(shouldDataBeIncluded && filterCheckBoxScale.isSelected())
            {
                shouldDataBeIncluded = filterComboBoxScale.getSelectedIndex()+1 == data.raidTeamSize;
            }
            switch(viewByRaidComboBox.getSelectedIndex())
            {
                case 0:
                    timeToDisplay = data.maidenTime+data.bloatTime+data.nyloTime+data.soteTime+data.xarpTime+data.verzikTime;
                    break;
                case 1:
                    timeToDisplay = data.maidenTime;
                    break;
                case 2:
                    timeToDisplay = data.bloatTime;
                    break;
                case 3:
                    timeToDisplay = data.nyloTime;
                    break;
                case 4:
                    timeToDisplay = data.soteTime;
                    break;
                case 5:
                    timeToDisplay = data.xarpTime;
                    break;
                case 6:
                    timeToDisplay = data.verzikTime;
                    break;
            }
            if(timeToDisplay == 0)
            {
                shouldDataBeIncluded = false;
            }
            if(shouldDataBeIncluded)
            {
                tableData.add(data);
            }
        }
        if(sortOptionsBox.getSelectedIndex() == 0)
        {
            tableData.sort(Comparator.comparing(cRoomData::getDate));
        }
        else if(sortOptionsBox.getSelectedIndex() == 1)
        {
            tableData.sort(Comparator.comparing(cRoomData::getOverallTime));
        }
        else if(sortOptionsBox.getSelectedIndex() == 2)
        {
            tableData.sort(Comparator.comparing(cRoomData::getScale));
        }

        updateCustomStats(tableData);
        raidsFoundLabel.setText("Raids Found: " + tableData.size());

        String[] columnNames = { "Date", "Scale", "Status", viewByRaidComboBox.getSelectedItem().toString(), "Players", "Spectated?", "View"};
        ArrayList<Object[]> tableBuilder = new ArrayList<>();
        for(cRoomData raid : tableData)
        {
            String players = "";
            for(String s : raid.players)
            {
                players += s + ", ";
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(raid.raidStarted);
            String dateString = (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR);
            String scaleString = "";
            switch(raid.players.size())
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
            switch(viewByRaidComboBox.getSelectedIndex())
            {
                case 0:
                    timeToDisplay = raid.maidenTime+raid.bloatTime+raid.nyloTime+raid.soteTime+raid.xarpTime+raid.verzikTime;
                    break;
                case 1:
                    timeToDisplay = raid.maidenTime;
                    break;
                case 2:
                    timeToDisplay = raid.bloatTime;
                    break;
                case 3:
                    timeToDisplay = raid.nyloTime;
                    break;
                case 4:
                    timeToDisplay = raid.soteTime;
                    break;
                case 5:
                    timeToDisplay = raid.xarpTime;
                    break;
                case 6:
                    timeToDisplay = raid.verzikTime;
                    break;
            }

            Object[] row =
                    {
                            dateString,
                            scaleString,
                            getRoomStatus(raid),
                            RoomUtil.time(timeToDisplay),
                            (players.length() > 2) ? players.substring(0, players.length()-2) : "",
                            (raid.spectated) ? "Yes" : "No",
                            "View"
                    };
            tableBuilder.add(row);
        }
        Object[][] tableObject = new Object[tableData.size()][7];
        int count = 0;
        for(Object[] row : tableBuilder)
        {
            tableObject[count] = row;
            count++;
        }
        table.setModel(new DefaultTableModel(tableObject, columnNames));
        table.getColumn("Date").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("Scale").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("Status").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn(viewByRaidComboBox.getSelectedItem().toString()).setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("Players").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("Spectated?").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("View").setCellRenderer(new cButtonRenderer());
        table.getColumn("View").setCellEditor(new cButtonEditorRoomData(new JCheckBox(), tableData));
        resizeColumnWidth(table);
        table.setFillsViewportHeight(true);
        setLabels(tableData);
        container.validate();
        container.repaint();


    }

    public String getRoomStatus(cRoomData data)
    {
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
        return raidStatusString;
    }

    public void resizeColumnWidth(JTable table)
    {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++)
        {
            int width = 50; // Min width
            for (int row = 0; row < table.getRowCount(); row++)
            {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 500)
            {
                width = 500;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    public void setLabels(ArrayList<cRoomData> data)
    {
        setOverallLabels(data);
        setMaidenLabels(data);
        setBloatLabels(data);
        setNyloLabels(data);
        setSoteLabels(data);
        setXarpLabels(data);
        setVerzikLabels(data);
    }
    public void setOverallLabels(ArrayList<cRoomData> data)
    {
        setOverallAverageLabels(data);
        setOverallMedianLabels(data);
        setOverallMinLabels(data);
        setOverallMaxLabels(data);
    }

    public void setOverallAverageLabels(ArrayList<cRoomData> data)
    {
        overallPanelMaidenAverage.setText(RoomUtil.time(cStatisticGatherer.getMaidenTimeAverage(data)));
        overallPanelBloatAverage.setText(RoomUtil.time(cStatisticGatherer.getBloatTimeAverage(data)));
        overallPanelNyloAverage.setText(RoomUtil.time(cStatisticGatherer.getNyloTimeAverage(data)));
        overallPanelSoteAverage.setText(RoomUtil.time(cStatisticGatherer.getSoteTimeAverage(data)));
        overallPanelXarpusAverage.setText(RoomUtil.time(cStatisticGatherer.getXarpTimeAverage(data)));
        overallPanelVerzikAverage.setText(RoomUtil.time(cStatisticGatherer.getVerzikTimeAverage(data)));
        overallPanelOverallAverage.setText(RoomUtil.time(cStatisticGatherer.getOverallTimeAverage(data)));
    }

    public void setOverallMedianLabels(ArrayList<cRoomData> data)
    {
        overallPanelMaidenMedian.setText(RoomUtil.time(cStatisticGatherer.getMaidenTimeMedian(data)));
        overallPanelBloatMedian.setText(RoomUtil.time(cStatisticGatherer.getBloatTimeMedian(data)));
        overallPanelNyloMedian.setText(RoomUtil.time(cStatisticGatherer.getNyloTimeMedian(data)));
        overallPanelSoteMedian.setText(RoomUtil.time(cStatisticGatherer.getSoteTimeMedian(data)));
        overallPanelXarpusMedian.setText(RoomUtil.time(cStatisticGatherer.getXarpTimeMedian(data)));
        overallPanelVerzikMedian.setText(RoomUtil.time(cStatisticGatherer.getVerzikTimeMedian(data)));
        overallPanelOverallMedian.setText(RoomUtil.time(cStatisticGatherer.getOverallMedian(data)));
    }

    public void setOverallMinLabels(ArrayList<cRoomData> data)
    {
        overallPanelMaidenMinimum.setText(RoomUtil.time(cStatisticGatherer.getMaidenTimeMin(data)));
        overallPanelBloatMinimum.setText(RoomUtil.time(cStatisticGatherer.getBloatTimeMin(data)));
        overallPanelNyloMinimum.setText(RoomUtil.time(cStatisticGatherer.getNyloTimeMinimum(data)));
        overallPanelSoteMinimum.setText(RoomUtil.time(cStatisticGatherer.getSoteTimeMin(data)));
        overallPanelXarpusMinimum.setText(RoomUtil.time(cStatisticGatherer.getXarpTimeMin(data)));
        overallPanelVerzikMinimum.setText(RoomUtil.time(cStatisticGatherer.getVerzikTimeMin(data)));
        overallPanelOverallMinimum.setText(RoomUtil.time(cStatisticGatherer.getOverallTimeMin(data)));
    }
    private void setOverallMaxLabels(ArrayList<cRoomData> data)
    {
        overallPanelMaidenMaximum.setText(RoomUtil.time(cStatisticGatherer.getMaidenTimeMax(data)));
        overallPanelBloatMaximum.setText(RoomUtil.time(cStatisticGatherer.getBloatTimeMax(data)));
        overallPanelNyloMaximum.setText(RoomUtil.time(cStatisticGatherer.getNyloTimeMaximum(data)));
        overallPanelSoteMaximum.setText(RoomUtil.time(cStatisticGatherer.getSoteTimeMax(data)));
        overallPanelXarpusMaximum.setText(RoomUtil.time(cStatisticGatherer.getXarpTimeMax(data)));
        overallPanelVerzikMaximum.setText(RoomUtil.time(cStatisticGatherer.getVerzikTimeMax(data)));
        overallPanelOverallMaximum.setText(RoomUtil.time(cStatisticGatherer.getOverallMax(data)));
    }

    public void setMaidenLabels(ArrayList<cRoomData> data)
    {
        setMaidenAverageLabels(data);
        setMaidenMedianLabels(data);
        setMaidenModeLabels(data);
        setMaidenMinLabels(data);
        setMaidenMaxLabels(data);
    }

    public void setMaidenAverageLabels(ArrayList<cRoomData> data)
    {
        maidenAverage70.setText(RoomUtil.time(cStatisticGatherer.getMaiden70SplitAverage(data)));
        maidenAverage7050.setText(RoomUtil.time(cStatisticGatherer.getMaiden7050SplitAverage(data)));
        maidenAverage50.setText(RoomUtil.time(cStatisticGatherer.getMaiden50SplitAverage(data)));
        maidenAverage5030.setText(RoomUtil.time(cStatisticGatherer.getMaiden5030SplitAverage(data)));
        maidenAverage30.setText(RoomUtil.time(cStatisticGatherer.getMaiden30SplitAverage(data)));
        maidenAverageSkip.setText(RoomUtil.time(cStatisticGatherer.getMaidenSkipSplitAverage(data)));
        maidenAverageTime.setText(RoomUtil.time(cStatisticGatherer.getMaidenTimeAverage(data)));
    }

    public void setMaidenMedianLabels(ArrayList<cRoomData> data)
    {
        maidenMedian70.setText(RoomUtil.time(cStatisticGatherer.getMaiden70SplitMedian(data)));
        maidenMedian7050.setText(RoomUtil.time(cStatisticGatherer.getMaiden7050SplitMedian(data)));
        maidenMedian50.setText(RoomUtil.time(cStatisticGatherer.getMaiden50SplitMedian(data)));
        maidenMedian5030.setText(RoomUtil.time(cStatisticGatherer.getMaiden5030SplitMedian(data)));
        maidenMedian30.setText(RoomUtil.time(cStatisticGatherer.getMaiden30SplitMedian(data)));
        maidenMedianSkip.setText(RoomUtil.time(cStatisticGatherer.getMaidenSkipSplitMedian(data)));
        maidenMedianTime.setText(RoomUtil.time(cStatisticGatherer.getMaidenTimeMedian(data)));
    }

    public void setMaidenModeLabels(ArrayList<cRoomData> data)
    {
        maidenMode70.setText(RoomUtil.time(cStatisticGatherer.getMaiden70SplitMode(data)));
        maidenMode7050.setText(RoomUtil.time(cStatisticGatherer.getMaiden7050SplitMode(data)));
        maidenMode50.setText(RoomUtil.time(cStatisticGatherer.getMaiden50SplitMode(data)));
        maidenMode5030.setText(RoomUtil.time(cStatisticGatherer.getMaiden5030SplitMode(data)));
        maidenMode30.setText(RoomUtil.time(cStatisticGatherer.getMaiden30SplitMode(data)));
        maidenModeSkip.setText(RoomUtil.time(cStatisticGatherer.getMaidenSkipSplitMode(data)));
        maidenModeTime.setText(RoomUtil.time(cStatisticGatherer.getMaidenTimeMode(data)));
    }

    public void setMaidenMinLabels(ArrayList<cRoomData> data)
    {
        maidenMin70.setText(RoomUtil.time(cStatisticGatherer.getMaiden70SplitMin(data)));
        maidenMin7050.setText(RoomUtil.time(cStatisticGatherer.getMaiden7050SplitMin(data)));
        maidenMin50.setText(RoomUtil.time(cStatisticGatherer.getMaiden50SplitMin(data)));
        maidenMin5030.setText(RoomUtil.time(cStatisticGatherer.getMaiden5030SplitMin(data)));
        maidenMin30.setText(RoomUtil.time(cStatisticGatherer.getMaiden30SplitMin(data)));
        maidenMinSkip.setText(RoomUtil.time(cStatisticGatherer.getMaidenSkipSplitMin(data)));
        maidenMinTime.setText(RoomUtil.time(cStatisticGatherer.getMaidenTimeMin(data)));
    }

    public void setMaidenMaxLabels(ArrayList<cRoomData> data)
    {
        maidenMax70.setText(RoomUtil.time(cStatisticGatherer.getMaiden70SplitMax(data)));
        maidenMax7050.setText(RoomUtil.time(cStatisticGatherer.getMaiden7050SplitMax(data)));
        maidenMax50.setText(RoomUtil.time(cStatisticGatherer.getMaiden50SplitMax(data)));
        maidenMax5030.setText(RoomUtil.time(cStatisticGatherer.getMaiden5030SplitMax(data)));
        maidenMax30.setText(RoomUtil.time(cStatisticGatherer.getMaiden30SplitMax(data)));
        maidenMaxSkip.setText(RoomUtil.time(cStatisticGatherer.getMaidenSkipSplitMax(data)));
        maidenMaxTime.setText(RoomUtil.time(cStatisticGatherer.getMaidenTimeMax(data)));
    }

    public void setBloatLabels(ArrayList<cRoomData> data)
    {
        setBloatAverageLabels(data);
        setBloatMedianLabels(data);
        setBloatModeLabels(data);
        setBloatMinLabels(data);
        setBloatMaxLabels(data);
    }

    public void setBloatAverageLabels(ArrayList<cRoomData> data)
    {
        bloatAverageFirstDown.setText(RoomUtil.time(cStatisticGatherer.getBloatFirstDownAverage(data)));
        bloatAverageTime.setText(RoomUtil.time(cStatisticGatherer.getBloatTimeAverage(data)));
    }

    public void setBloatMedianLabels(ArrayList<cRoomData> data)
    {
        bloatMedianFirstDown.setText(RoomUtil.time(cStatisticGatherer.getBloatFirstDownMedian(data)));
        bloatMedianTime.setText(RoomUtil.time(cStatisticGatherer.getBloatTimeMedian(data)));
    }

    public void setBloatModeLabels(ArrayList<cRoomData> data)
    {
        bloatModeFirstDown.setText(RoomUtil.time(cStatisticGatherer.getBloatFirstDownMode(data)));
        bloatModeTime.setText(RoomUtil.time(cStatisticGatherer.getBloatTimeMode(data)));
    }

    public void setBloatMinLabels(ArrayList<cRoomData> data)
    {
        bloatMinFirstDown.setText(RoomUtil.time(cStatisticGatherer.getBloatFirstDownMin(data)));
        bloatMinTime.setText(RoomUtil.time(cStatisticGatherer.getBloatTimeMin(data)));
    }

    public void setBloatMaxLabels(ArrayList<cRoomData> data)
    {
        bloatMaxFirstDown.setText(RoomUtil.time(cStatisticGatherer.getBloatFirstDownMax(data)));
        bloatMaxTime.setText(RoomUtil.time(cStatisticGatherer.getBloatTimeMax(data)));
    }

    public void setNyloLabels(ArrayList<cRoomData> data)
    {
        setNyloAverageLabels(data);
        setNyloMedianLabels(data);
        setNyloModeLabels(data);
        setNyloMinLabels(data);
        setNyloMaxLabels(data);
    }

    public void setNyloAverageLabels(ArrayList<cRoomData> data)
    {
        nyloAverageLastWave.setText(RoomUtil.time(cStatisticGatherer.getNyloLastWaveAverage(data)));
        nyloAverageBossSpawn.setText(RoomUtil.time(cStatisticGatherer.getNyloBossSpawnAverage(data)));
        nyloAverageBossDuration.setText(RoomUtil.time(cStatisticGatherer.getNyloBossSplitAverage(data)));
        nyloAverageTime.setText(RoomUtil.time(cStatisticGatherer.getNyloTimeAverage(data)));
    }

    public void setNyloMedianLabels(ArrayList<cRoomData> data)
    {
        nyloMedianLastWave.setText(RoomUtil.time(cStatisticGatherer.getNyloLastWaveMedian(data)));
        nyloMedianBossSpawn.setText(RoomUtil.time(cStatisticGatherer.getNyloBossSpawnMedian(data)));
        nyloMedianBossDuration.setText(RoomUtil.time(cStatisticGatherer.getNyloBossSplitMedian(data)));
        nyloMedianTime.setText(RoomUtil.time(cStatisticGatherer.getNyloTimeMedian(data)));
    }

    public void setNyloModeLabels(ArrayList<cRoomData> data)
    {
        nyloModeLastWave.setText(RoomUtil.time(cStatisticGatherer.getNyloLastWaveMode(data)));
        nyloModeBossSpawn.setText(RoomUtil.time(cStatisticGatherer.getNyloBossSpawnMode(data)));
        nyloModeBossDuration.setText(RoomUtil.time(cStatisticGatherer.getNyloBossSplitMode(data)));
        nyloModeTime.setText(RoomUtil.time(cStatisticGatherer.getNyloTimeMode(data)));
    }

    public void setNyloMinLabels(ArrayList<cRoomData> data)
    {
        nyloMinLastWave.setText(RoomUtil.time(cStatisticGatherer.getNyloLastWaveMinimum(data)));
        nyloMinBossSpawn.setText(RoomUtil.time(cStatisticGatherer.getNyloBossSpawnMinimum(data)));
        nyloMinBossDuration.setText(RoomUtil.time(cStatisticGatherer.getNyloBossSplitMinimum(data)));
        nyloMinTime.setText(RoomUtil.time(cStatisticGatherer.getNyloTimeMinimum(data)));
    }

    public void setNyloMaxLabels(ArrayList<cRoomData> data)
    {
        nyloMaxLastWave.setText(RoomUtil.time(cStatisticGatherer.getNyloLastWaveMaximum(data)));
        nyloMaxBossSpawn.setText(RoomUtil.time(cStatisticGatherer.getNyloBossSpawnMaximum(data)));
        nyloMaxBossDuration.setText(RoomUtil.time(cStatisticGatherer.getNyloBossSplitMaximum(data)));
        nyloMaxTime.setText(RoomUtil.time(cStatisticGatherer.getNyloTimeMaximum(data)));
    }

    public void setSoteLabels(ArrayList<cRoomData> data)
    {
        setSoteAverageLabels(data);
        setSoteMedianLabels(data);
        setSoteModeLabels(data);
        setSoteMinLabels(data);
        setSoteMaxLabels(data);
    }

    public void setSoteAverageLabels(ArrayList<cRoomData> data)
    {
        soteAverageP1.setText(RoomUtil.time(cStatisticGatherer.getSoteP1Average(data)));
        soteAverageM1.setText(RoomUtil.time(cStatisticGatherer.getSoteM1Average(data)));
        soteAverageP2.setText(RoomUtil.time(cStatisticGatherer.getSoteP2Average(data)));
        soteAverageM2.setText(RoomUtil.time(cStatisticGatherer.getSoteM2Average(data)));
        soteAverageP3.setText(RoomUtil.time(cStatisticGatherer.getSoteP3Average(data)));
        soteAverageTime.setText(RoomUtil.time(cStatisticGatherer.getSoteTimeAverage(data)));
    }

    public void setSoteMedianLabels(ArrayList<cRoomData> data)
    {
        soteMedianP1.setText(RoomUtil.time(cStatisticGatherer.getSoteP1Median(data)));
        soteMedianM1.setText(RoomUtil.time(cStatisticGatherer.getSoteM1Median(data)));
        soteMedianP2.setText(RoomUtil.time(cStatisticGatherer.getSoteP2Median(data)));
        soteMedianM2.setText(RoomUtil.time(cStatisticGatherer.getSoteM2Median(data)));
        soteMedianP3.setText(RoomUtil.time(cStatisticGatherer.getSoteP3Median(data)));
        soteMedianTime.setText(RoomUtil.time(cStatisticGatherer.getSoteTimeMedian(data)));
    }

    public void setSoteModeLabels(ArrayList<cRoomData> data)
    {
        soteModeP1.setText(RoomUtil.time(cStatisticGatherer.getSoteP1Mode(data)));
        soteModeM1.setText(RoomUtil.time(cStatisticGatherer.getSoteM1Mode(data)));
        soteModeP2.setText(RoomUtil.time(cStatisticGatherer.getSoteP2Mode(data)));
        soteModeM2.setText(RoomUtil.time(cStatisticGatherer.getSoteM2Mode(data)));
        soteModeP3.setText(RoomUtil.time(cStatisticGatherer.getSoteP3Mode(data)));
        soteModeTime.setText(RoomUtil.time(cStatisticGatherer.getSoteTimeMode(data)));
    }

    public void setSoteMinLabels(ArrayList<cRoomData> data)
    {
        soteMinP1.setText(RoomUtil.time(cStatisticGatherer.getSoteP1Min(data)));
        soteMinM1.setText(RoomUtil.time(cStatisticGatherer.getSoteM1Min(data)));
        soteMinP2.setText(RoomUtil.time(cStatisticGatherer.getSoteP2Min(data)));
        soteMinM2.setText(RoomUtil.time(cStatisticGatherer.getSoteM2Min(data)));
        soteMinP3.setText(RoomUtil.time(cStatisticGatherer.getSoteP3Min(data)));
        soteMinTime.setText(RoomUtil.time(cStatisticGatherer.getSoteTimeMin(data)));
    }

    public void setSoteMaxLabels(ArrayList<cRoomData> data)
    {
        soteMaxP1.setText(RoomUtil.time(cStatisticGatherer.getSoteP1Max(data)));
        soteMaxM1.setText(RoomUtil.time(cStatisticGatherer.getSoteM1Max(data)));
        soteMaxP2.setText(RoomUtil.time(cStatisticGatherer.getSoteP2Max(data)));
        soteMaxM2.setText(RoomUtil.time(cStatisticGatherer.getSoteM2Max(data)));
        soteMaxP3.setText(RoomUtil.time(cStatisticGatherer.getSoteP3Max(data)));
        soteMaxTime.setText(RoomUtil.time(cStatisticGatherer.getSoteTimeMax(data)));
    }

    public void setXarpLabels(ArrayList<cRoomData> data)
    {
        setXarpAverageLabels(data);
        setXarpMedianLabels(data);
        setXarpModeLabels(data);
        setXarpMinLabels(data);
        setXarpMaxLabels(data);
    }

    public void setXarpAverageLabels(ArrayList<cRoomData> data)
    {
        xarpAverageScreech.setText(RoomUtil.time(cStatisticGatherer.getXarpScreechAverage(data)));
        xarpAverageTime.setText(RoomUtil.time(cStatisticGatherer.getXarpTimeAverage(data)));
    }

    public void setXarpMedianLabels(ArrayList<cRoomData> data)
    {
        xarpMedianScreech.setText(RoomUtil.time(cStatisticGatherer.getXarpScreechMedian(data)));
        xarpMedianTime.setText(RoomUtil.time(cStatisticGatherer.getXarpTimeMedian(data)));
    }

    public void setXarpModeLabels(ArrayList<cRoomData> data)
    {
        xarpModeScreech.setText(RoomUtil.time(cStatisticGatherer.getXarpScreechMode(data)));
        xarpModeTime.setText(RoomUtil.time(cStatisticGatherer.getXarpTimeMode(data)));
    }

    public void setXarpMinLabels(ArrayList<cRoomData> data)
    {
        xarpMinScreech.setText(RoomUtil.time(cStatisticGatherer.getXarpScreechMin(data)));
        xarpMinTime.setText(RoomUtil.time(cStatisticGatherer.getXarpTimeMin(data)));
    }

    public void setXarpMaxLabels(ArrayList<cRoomData> data)
    {
        xarpMaxScreech.setText(RoomUtil.time(cStatisticGatherer.getXarpScreechMax(data)));
        xarpMaxTime.setText(RoomUtil.time(cStatisticGatherer.getXarpTimeMax(data)));
    }

    public void setVerzikLabels(ArrayList<cRoomData> data)
    {
        setVerzikAverageLabels(data);
        setVerzikMedianLabels(data);
        setVerzikModeLabels(data);
        setVerzikMinLabels(data);
        setVerzikMaxLabels(data);
    }

    public void setVerzikAverageLabels(ArrayList<cRoomData> data)
    {
        verzikAverageP1.setText(RoomUtil.time(cStatisticGatherer.getVerzikP1Average(data)));
        verzikAverageP2.setText(RoomUtil.time(cStatisticGatherer.getVerzikP2Average(data)));
        verzikAverageP3Entry.setText(RoomUtil.time(cStatisticGatherer.getVerzikP3EntryAverage(data)));
        verzikAverageP3.setText(RoomUtil.time(cStatisticGatherer.getVerzikP3Average(data)));
        verzikAverageTime.setText(RoomUtil.time(cStatisticGatherer.getVerzikTimeAverage(data)));
    }

    public void setVerzikMedianLabels(ArrayList<cRoomData> data)
    {
        verzikMedianP1.setText(RoomUtil.time(cStatisticGatherer.getVerzikP1Median(data)));
        verzikMedianP2.setText(RoomUtil.time(cStatisticGatherer.getVerzikP2Median(data)));
        verzikMedianP3Entry.setText(RoomUtil.time(cStatisticGatherer.getVerzikP3EntryMedian(data)));
        verzikMedianP3.setText(RoomUtil.time(cStatisticGatherer.getVerzikP3Median(data)));
        verzikMedianTime.setText(RoomUtil.time(cStatisticGatherer.getVerzikTimeMedian(data)));
    }

    public void setVerzikModeLabels(ArrayList<cRoomData> data)
    {
        verzikModeP1.setText(RoomUtil.time(cStatisticGatherer.getVerzikP1Mode(data)));
        verzikModeP2.setText(RoomUtil.time(cStatisticGatherer.getVerzikP2Mode(data)));
        verzikModeP3Entry.setText(RoomUtil.time(cStatisticGatherer.getVerzikP3EntryMode(data)));
        verzikModeP3.setText(RoomUtil.time(cStatisticGatherer.getVerzikP3Mode(data)));
        verzikModeTime.setText(RoomUtil.time(cStatisticGatherer.getVerzikTimeMode(data)));
    }

    public void setVerzikMinLabels(ArrayList<cRoomData> data)
    {
        verzikMinP1.setText(RoomUtil.time(cStatisticGatherer.getVerzikP1Min(data)));
        verzikMinP2.setText(RoomUtil.time(cStatisticGatherer.getVerzikP2Min(data)));
        verzikMinP3Entry.setText(RoomUtil.time(cStatisticGatherer.getVerzikP3EntryMin(data)));
        verzikMinP3.setText(RoomUtil.time(cStatisticGatherer.getVerzikP3Min(data)));
        verzikMinTime.setText(RoomUtil.time(cStatisticGatherer.getVerzikTimeMin(data)));
    }

    public void setVerzikMaxLabels(ArrayList<cRoomData> data)
    {
        verzikMaxP1.setText(RoomUtil.time(cStatisticGatherer.getVerzikP1Max(data)));
        verzikMaxP2.setText(RoomUtil.time(cStatisticGatherer.getVerzikP2Max(data)));
        verzikMaxP3Entry.setText(RoomUtil.time(cStatisticGatherer.getVerzikP3EntryMax(data)));
        verzikMaxP3.setText(RoomUtil.time(cStatisticGatherer.getVerzikP3Max(data)));
        verzikMaxTime.setText(RoomUtil.time(cStatisticGatherer.getVerzikTimeMax(data)));
    }

    public void updateFrameData(ArrayList<cRoomData> data)
    {
        /**/
        currentData = data;
        setTitle("Raids");
        String[] columnNames = { "Date", "Scale", "Status", "Overall Time", "Players", "Spectated?", "View"};
        ArrayList<Object[]> tableBuilder = new ArrayList<>();
        for(cRoomData raid : data)
        {
            String players = "";
            for(String s : raid.players)
            {
                players += s + ", ";
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(raid.raidStarted);
            String dateString = (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR);
            String scaleString = "";
            switch(raid.players.size())
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
            Object[] row =
                    {
                            dateString,
                            scaleString,
                            getRoomStatus(raid),
                            RoomUtil.time(raid.maidenTime+raid.bloatTime+raid.nyloTime+raid.soteTime+raid.xarpTime+raid.verzikTime),
                            (players.length() > 2) ? players.substring(0, players.length()-2) : "",
                            (raid.spectated) ? "Yes" : "No",
                            "View"
                    };
            tableBuilder.add(row);
        }
        Object[][] tableObject = new Object[data.size()][7];
        int count = 0;
        for(Object[] row : tableBuilder)
        {
            tableObject[count] = row;
            count++;
        }
        table = new JTable(tableObject, columnNames);
        table.getColumn("Date").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("Scale").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("Status").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("Overall Time").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("Players").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("Spectated?").setCellEditor(new cNonEditableCell(new JTextField()));
        table.getColumn("View").setCellRenderer(new cButtonRenderer());
        table.getColumn("View").setCellEditor(new cButtonEditorRoomData(new JCheckBox(), data));
        resizeColumnWidth(table);
        table.setFillsViewportHeight(true);
        JScrollPane pane = new JScrollPane(table);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Raids"));
        tablePanel.add(pane);

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JTabbedPane tabbedPane = new JTabbedPane();

        JComponent overallPanel = new JPanel();
        tabbedPane.addTab("Overall", overallPanel);
        overallPanel.setLayout(new GridLayout(2, 3));

        JPanel overallCustomPanel = new JPanel();
        overallCustomPanel.setLayout(new BorderLayout());
        overallCustomPanel.setBorder(BorderFactory.createTitledBorder(""));

        JPanel customSubPanel = new JPanel();
        customSubPanel.setLayout(new GridLayout(1, 4));

        JPanel subPanel1 = new JPanel();
        subPanel1.setLayout(new GridLayout(1, 1));
        //subPanel1.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel subPanel2 = new JPanel();
        subPanel2.setLayout(new GridLayout(5, 2));
        //subPanel2.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel subPanel3 = new JPanel();
       // subPanel3.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel subPanel4 = new JPanel();
       // subPanel4.setLayout(new BoxLayout(subPanel4, BoxLayout.Y_AXIS));
       // subPanel4.setBorder(BorderFactory.createLineBorder(Color.black));

        sortOptionsBox = new JComboBox(new String[]
                {
                        "Date",
                        "Time",
                        "Scale"
                }
                );

        statisticsBox = new JComboBox(new String[]
                {
                        "None",
                        "Maiden Bloods Spawned",
                        "Maiden Defense",
                        "Maiden Crabs Leaked",
                        "Maiden Crabs Leaked @ 100%",
                        "Maiden HP Healed from crabs",
                        "Maiden Deaths",
                        "Bloat Downs",
                        "Bloat Deaths During 1st Walk",
                        "Bloat Deaths Total",
                        "Bloat Defense 1st Walk",
                        "Bloat Scythes 1st Walk",
                        "Bloat HP % After 1st Down",
                        "Bloat 1st Down Time",
                        "Nylo precap stalls",
                        "Nylo postcap stalls",
                        "Nylo total stalls",
                        "Nylo Melee Rotations",
                        "Nylo Range Rotations",
                        "Nylo Mage Rotations",
                        "Nylo Total Rotations",
                        "Nylo Boss Defense",
                        "Nylo Deaths",
                        "Sotetseg P1 Hammers",
                        "Sotetseg P2 Hammers",
                        "Sotetseg P3 Hammers",
                        "Sotetseg Total Hammers",
                        "Sotetseg Deaths",
                        "Xarpus Defense",
                        "Xarpus Deaths",
                        "Xarpus Healing",
                        "Verzik Bounces",
                        "Verzik Deaths",
                        "Verzik Crabs Spawned"
                });

        statisticsBox.addActionListener(
                al->
                {
                    updateTable();
                });

        sortOptionsBox.addActionListener(
                al->
                {
                    updateTable();
                }
        );

        JLabel textCustomAverageLabel = new JLabel("Average:", SwingConstants.LEFT);
        JLabel textCustomMedianLabel = new JLabel("Median:", SwingConstants.LEFT);
        JLabel textCustomModeLabel = new JLabel("Mode:", SwingConstants.LEFT);
        JLabel textCustomMinLabel = new JLabel("Minimum:", SwingConstants.LEFT);
        JLabel textCustomMaxLabel = new JLabel("Maximum:", SwingConstants.LEFT);

        subPanel2.add(textCustomAverageLabel);
        subPanel2.add(customAverageLabel);

        subPanel2.add(textCustomMedianLabel);
        subPanel2.add(customMedianLabel);

        subPanel2.add(textCustomModeLabel);
        subPanel2.add(customModeLabel);

        subPanel2.add(textCustomMinLabel);
        subPanel2.add(customMinLabel);

        subPanel2.add(textCustomMaxLabel);
        subPanel2.add(customMaxLabel);

        subPanel1.add(statisticsBox);

        subPanel1.setBorder(BorderFactory.createTitledBorder("Choose Statistic"));
        subPanel2.setBorder(BorderFactory.createTitledBorder("Results"));
        subPanel3.setBorder(BorderFactory.createTitledBorder("Sort Options"));
        subPanel3.add(sortOptionsBox);
        JPanel buttonLine = new JPanel();
        buttonLine.setLayout(new GridLayout(1, 2));
        buttonLine.add(new JLabel("Config"));


        subPanel4.setBorder(BorderFactory.createTitledBorder("View Raid Time By"));
         viewByRaidComboBox = new JComboBox(new String[]{"Overall Time", "Maiden Time", "Bloat Time", "Nylocas Time", "Sotetseg Time", "Xarpus Time", "Verzik Time"});
        viewByRaidComboBox.addActionListener(
                al->
                {
                    updateTable();
                });

        subPanel4.add(viewByRaidComboBox);

        subPanel4.add(raidsFoundLabel);
        raidsFoundLabel.setText("Raids found: " + data.size());

        customSubPanel.add(subPanel1);
        customSubPanel.add(subPanel2);
        customSubPanel.add(subPanel3);
        customSubPanel.add(subPanel4);

        overallCustomPanel.add(customSubPanel);

        JPanel overallAveragePanel = new JPanel();
        overallAveragePanel.setLayout(new BorderLayout());
        overallAveragePanel.setBorder(BorderFactory.createTitledBorder("Average"));

        JPanel overallAverageSubPanel = new JPanel();
        overallAverageSubPanel.setLayout(new GridLayout(7, 2));

        overallAverageSubPanel.add(new JLabel("Maiden Time"));
        overallAverageSubPanel.add(overallPanelMaidenAverage);

        overallAverageSubPanel.add(new JLabel("Bloat Time"));
        overallAverageSubPanel.add(overallPanelBloatAverage);

        overallAverageSubPanel.add(new JLabel("Nylocas Time"));
        overallAverageSubPanel.add(overallPanelNyloAverage);

        overallAverageSubPanel.add(new JLabel("Sotetseg Time"));
        overallAverageSubPanel.add(overallPanelSoteAverage);

        overallAverageSubPanel.add(new JLabel("Xarpus Time"));
        overallAverageSubPanel.add(overallPanelXarpusAverage);

        overallAverageSubPanel.add(new JLabel("Verzik Time"));
        overallAverageSubPanel.add(overallPanelVerzikAverage);

        overallAverageSubPanel.add(new JLabel("Overall Time"));
        overallAverageSubPanel.add(overallPanelOverallAverage);

        JPanel overallMedianPanel = new JPanel();
        overallMedianPanel.setLayout(new BorderLayout());
        overallMedianPanel.setBorder(BorderFactory.createTitledBorder("Median"));

        JPanel overallMedianSubPanel = new JPanel();
        overallMedianSubPanel.setLayout(new GridLayout(7, 2));

        overallMedianSubPanel.add(new JLabel("Maiden Time"));
        overallMedianSubPanel.add(overallPanelMaidenMedian);

        overallMedianSubPanel.add(new JLabel("Bloat Time"));
        overallMedianSubPanel.add(overallPanelBloatMedian);

        overallMedianSubPanel.add(new JLabel("Nylocas Time"));
        overallMedianSubPanel.add(overallPanelNyloMedian);

        overallMedianSubPanel.add(new JLabel("Sotetseg Time"));
        overallMedianSubPanel.add(overallPanelSoteMedian);

        overallMedianSubPanel.add(new JLabel("Xarpus Time"));
        overallMedianSubPanel.add(overallPanelXarpusMedian);

        overallMedianSubPanel.add(new JLabel("Verzik Time"));
        overallMedianSubPanel.add(overallPanelVerzikMedian);


        overallMedianSubPanel.add(new JLabel("Overall Time"));
        overallMedianSubPanel.add(overallPanelOverallMedian);


        overallAveragePanel.add(overallAverageSubPanel);
        overallMedianPanel.add(overallMedianSubPanel);

        JPanel overallMinimumPanel = new JPanel();
        overallMinimumPanel.setLayout(new BorderLayout());
        overallMinimumPanel.setBorder(BorderFactory.createTitledBorder("Minimum"));

        JPanel overallMinimumSubPanel = new JPanel();
        overallMinimumSubPanel.setLayout(new GridLayout(7, 2));

        overallMinimumSubPanel.add(new JLabel("Maiden Time"));
        overallMinimumSubPanel.add(overallPanelMaidenMinimum);

        overallMinimumSubPanel.add(new JLabel("Bloat Time"));
        overallMinimumSubPanel.add(overallPanelBloatMinimum);

        overallMinimumSubPanel.add(new JLabel("Nylocas Time"));
        overallMinimumSubPanel.add(overallPanelNyloMinimum);

        overallMinimumSubPanel.add(new JLabel("Sotetseg Time"));
        overallMinimumSubPanel.add(overallPanelSoteMinimum);

        overallMinimumSubPanel.add(new JLabel("Xarpus Time"));
        overallMinimumSubPanel.add(overallPanelXarpusMinimum);

        overallMinimumSubPanel.add(new JLabel("Verzik Time"));
        overallMinimumSubPanel.add(overallPanelVerzikMinimum);

        overallMinimumSubPanel.add(new JLabel("Overall Time"));
        overallMinimumSubPanel.add(overallPanelOverallMinimum);

        overallMinimumPanel.add(overallMinimumSubPanel);

        JPanel overallMaximumPanel = new JPanel();
        overallMaximumPanel.setLayout(new BorderLayout());
        overallMaximumPanel.setBorder(BorderFactory.createTitledBorder("Maximum"));

        JPanel overallMaximumSubPanel = new JPanel();
        overallMaximumSubPanel.setLayout(new GridLayout(7, 2));

        overallMaximumSubPanel.add(new JLabel("Maiden Time"));
        overallMaximumSubPanel.add(overallPanelMaidenMaximum);

        overallMaximumSubPanel.add(new JLabel("Bloat Time"));
        overallMaximumSubPanel.add(overallPanelBloatMaximum);

        overallMaximumSubPanel.add(new JLabel("Nylocas Time"));
        overallMaximumSubPanel.add(overallPanelNyloMaximum);

        overallMaximumSubPanel.add(new JLabel("Sotetseg Time"));
        overallMaximumSubPanel.add(overallPanelSoteMaximum);

        overallMaximumSubPanel.add(new JLabel("Xarpus Time"));
        overallMaximumSubPanel.add(overallPanelXarpusMaximum);

        overallMaximumSubPanel.add(new JLabel("Verzik Time"));
        overallMaximumSubPanel.add(overallPanelVerzikMaximum);

        overallMaximumSubPanel.add(new JLabel("Overall Time"));
        overallMaximumSubPanel.add(overallPanelOverallMaximum);

        overallMaximumPanel.add(overallMaximumSubPanel);

        JPanel topStatPanel = new JPanel();
        topStatPanel.setLayout(new GridLayout(1, 4));

        topStatPanel.add(overallAveragePanel);
        topStatPanel.add(overallMedianPanel);
        topStatPanel.add(overallMinimumPanel);
        topStatPanel.add(overallMaximumPanel);

        overallPanel.add(topStatPanel);
        overallPanel.add(overallCustomPanel);
        JComponent maidenPanel = new JPanel();
        tabbedPane.addTab("Maiden", maidenPanel);
        maidenPanel.setLayout(new GridLayout(2, 3));
        JPanel maidenAveragePanel = new JPanel();
        maidenAveragePanel.setLayout(new BorderLayout());
        maidenAveragePanel.setBorder(BorderFactory.createTitledBorder("Average"));

        JPanel maidenAverageSubPanel = new JPanel();
        maidenAverageSubPanel.setLayout(new GridLayout(7, 2));

        maidenAverageSubPanel.add(new JLabel("70s Split"));
        maidenAverageSubPanel.add(maidenAverage70);

        maidenAverageSubPanel.add(new JLabel("70-50s Split"));
        maidenAverageSubPanel.add(maidenAverage7050);

        maidenAverageSubPanel.add(new JLabel("50s Split"));
        maidenAverageSubPanel.add(maidenAverage50);

        maidenAverageSubPanel.add(new JLabel("50-30s Split"));
        maidenAverageSubPanel.add(maidenAverage5030);

        maidenAverageSubPanel.add(new JLabel("30s Split"));
        maidenAverageSubPanel.add(maidenAverage30);

        maidenAverageSubPanel.add(new JLabel("Skip Split"));
        maidenAverageSubPanel.add(maidenAverageSkip);

        maidenAverageSubPanel.add(new JLabel("Room Time"));
        maidenAverageSubPanel.add(maidenAverageTime);

        maidenAveragePanel.add(maidenAverageSubPanel);

        JPanel maidenMedianPanel = new JPanel();
        maidenMedianPanel.setLayout(new BorderLayout());
        maidenMedianPanel.setBorder(BorderFactory.createTitledBorder("Median"));

        JPanel maidenMedianSubPanel = new JPanel();
        maidenMedianSubPanel.setLayout(new GridLayout(7, 2));

        maidenMedianSubPanel.add(new JLabel("70s Split"));
        maidenMedianSubPanel.add(maidenMedian70);

        maidenMedianSubPanel.add(new JLabel("70-50s Split"));
        maidenMedianSubPanel.add(maidenMedian7050);

        maidenMedianSubPanel.add(new JLabel("50s Split"));
        maidenMedianSubPanel.add(maidenMedian50);

        maidenMedianSubPanel.add(new JLabel("50-30s Split"));
        maidenMedianSubPanel.add(maidenMedian5030);

        maidenMedianSubPanel.add(new JLabel("30s Split"));
        maidenMedianSubPanel.add(maidenMedian30);

        maidenMedianSubPanel.add(new JLabel("Skip Split"));
        maidenMedianSubPanel.add(maidenMedianSkip);

        maidenMedianSubPanel.add(new JLabel("Room Time"));
        maidenMedianSubPanel.add(maidenMedianTime);

        maidenMedianPanel.add(maidenMedianSubPanel);

        JPanel maidenModePanel = new JPanel();

        maidenModePanel.setLayout(new BorderLayout());
        maidenModePanel.setBorder(BorderFactory.createTitledBorder("Mode"));

        JPanel maidenModeSubPanel = new JPanel();
        maidenModeSubPanel.setLayout(new GridLayout(7, 2));

        maidenModeSubPanel.add(new JLabel("70s Split"));
        maidenModeSubPanel.add(maidenMode70);

        maidenModeSubPanel.add(new JLabel("70-50s Split"));
        maidenModeSubPanel.add(maidenMode7050);

        maidenModeSubPanel.add(new JLabel("50s Split"));
        maidenModeSubPanel.add(maidenMode50);

        maidenModeSubPanel.add(new JLabel("50-30s Split"));
        maidenModeSubPanel.add(maidenMode5030);

        maidenModeSubPanel.add(new JLabel("30s Split"));
        maidenModeSubPanel.add(maidenMode30);

        maidenModeSubPanel.add(new JLabel("Skip Split"));
        maidenModeSubPanel.add(maidenModeSkip);

        maidenModeSubPanel.add(new JLabel("Room Time"));
        maidenModeSubPanel.add(maidenModeTime);

        maidenModePanel.add(maidenModeSubPanel);

        JPanel maidenMinimumPanel = new JPanel();
        maidenMinimumPanel.setLayout(new BorderLayout());
        maidenMinimumPanel.setBorder(BorderFactory.createTitledBorder("Minimum"));

        JPanel maidenMinimumSubPanel = new JPanel();
        maidenMinimumSubPanel.setLayout(new GridLayout(7, 2));

        maidenMinimumSubPanel.add(new JLabel("70s Split"));
        maidenMinimumSubPanel.add(maidenMin70);

        maidenMinimumSubPanel.add(new JLabel("70-50s Split"));
        maidenMinimumSubPanel.add(maidenMin7050);

        maidenMinimumSubPanel.add(new JLabel("50s Split"));
        maidenMinimumSubPanel.add(maidenMin50);

        maidenMinimumSubPanel.add(new JLabel("50-30s Split"));
        maidenMinimumSubPanel.add(maidenMin5030);

        maidenMinimumSubPanel.add(new JLabel("30s Split"));
        maidenMinimumSubPanel.add(maidenMin30);

        maidenMinimumSubPanel.add(new JLabel("Skip Split"));
        maidenMinimumSubPanel.add(maidenMinSkip);

        maidenMinimumSubPanel.add(new JLabel("Room Time"));
        maidenMinimumSubPanel.add(maidenMinTime);

        maidenMinimumPanel.add(maidenMinimumSubPanel);

        JPanel maidenMaximumPanel = new JPanel();
        maidenMaximumPanel.setLayout(new BorderLayout());
        maidenMaximumPanel.setBorder(BorderFactory.createTitledBorder("Maximum"));

        JPanel maidenMaximumSubPanel = new JPanel();
        maidenMaximumSubPanel.setLayout(new GridLayout(7, 2));

        maidenMaximumSubPanel.add(new JLabel("70s Split"));
        maidenMaximumSubPanel.add(maidenMax70);

        maidenMaximumSubPanel.add(new JLabel("70-50s Split"));
        maidenMaximumSubPanel.add(maidenMax7050);

        maidenMaximumSubPanel.add(new JLabel("50s Split"));
        maidenMaximumSubPanel.add(maidenMax50);

        maidenMaximumSubPanel.add(new JLabel("50-30s Split"));
        maidenMaximumSubPanel.add(maidenMax5030);

        maidenMaximumSubPanel.add(new JLabel("30s Split"));
        maidenMaximumSubPanel.add(maidenMax30);

        maidenMaximumSubPanel.add(new JLabel("Skip Split"));
        maidenMaximumSubPanel.add(maidenMaxSkip);

        maidenMaximumSubPanel.add(new JLabel("Room Time"));
        maidenMaximumSubPanel.add(maidenMaxTime);

        maidenMaximumPanel.add(maidenMaximumSubPanel);

        JPanel maidenStatisticsPanel = new JPanel();
        maidenStatisticsPanel.setLayout(new BorderLayout());
        maidenStatisticsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));



        maidenPanel.add(maidenAveragePanel);
        maidenPanel.add(maidenMedianPanel);
        maidenPanel.add(maidenModePanel);
        maidenPanel.add(maidenMinimumPanel);
        maidenPanel.add(maidenMaximumPanel);
        maidenPanel.add(maidenStatisticsPanel);


        JComponent bloatPanel = new JPanel();
        tabbedPane.addTab("Bloat", bloatPanel);
        bloatPanel.setLayout(new GridLayout(2, 3));

        JPanel bloatAveragePanel = new JPanel();
        bloatAveragePanel.setLayout(new BorderLayout());
        bloatAveragePanel.setBorder(BorderFactory.createTitledBorder("Average"));

        JPanel bloatAverageSubPanel = new JPanel();
        bloatAverageSubPanel.setLayout(new GridLayout(7,2));

        bloatAverageSubPanel.add(new JLabel("First Down Split"));
        bloatAverageSubPanel.add(bloatAverageFirstDown);

        bloatAverageSubPanel.add(new JLabel("Room Time"));
        bloatAverageSubPanel.add(bloatAverageTime);

        bloatAverageSubPanel.add(new JLabel(""));
        bloatAverageSubPanel.add(new JLabel(""));

        bloatAverageSubPanel.add(new JLabel(""));
        bloatAverageSubPanel.add(new JLabel(""));

        bloatAverageSubPanel.add(new JLabel(""));
        bloatAverageSubPanel.add(new JLabel(""));

        bloatAverageSubPanel.add(new JLabel(""));
        bloatAverageSubPanel.add(new JLabel(""));

        bloatAverageSubPanel.add(new JLabel(""));
        bloatAverageSubPanel.add(new JLabel(""));


        bloatAveragePanel.add(bloatAverageSubPanel);


        JPanel bloatMedianPanel = new JPanel();
        bloatMedianPanel.setLayout(new BorderLayout());
        bloatMedianPanel.setBorder(BorderFactory.createTitledBorder("Median"));

        JPanel bloatMedianSubPanel = new JPanel();
        bloatMedianSubPanel.setLayout(new GridLayout(7, 2));

        bloatMedianSubPanel.add(new JLabel("First Down Split"));
        bloatMedianSubPanel.add(bloatMedianFirstDown);

        bloatMedianSubPanel.add(new JLabel("Room Time"));
        bloatMedianSubPanel.add(bloatMedianTime);

        bloatMedianSubPanel.add(new JLabel(""));
        bloatMedianSubPanel.add(new JLabel(""));

        bloatMedianSubPanel.add(new JLabel(""));
        bloatMedianSubPanel.add(new JLabel(""));

        bloatMedianSubPanel.add(new JLabel(""));
        bloatMedianSubPanel.add(new JLabel(""));

        bloatMedianSubPanel.add(new JLabel(""));
        bloatMedianSubPanel.add(new JLabel(""));

        bloatMedianSubPanel.add(new JLabel(""));
        bloatMedianSubPanel.add(new JLabel(""));

        bloatMedianPanel.add(bloatMedianSubPanel);

        JPanel bloatModePanel = new JPanel();
        bloatModePanel.setLayout(new BorderLayout());
        bloatModePanel.setBorder(BorderFactory.createTitledBorder("Mode"));

        JPanel bloatModeSubPanel = new JPanel();
        bloatModeSubPanel.setLayout(new GridLayout(7, 2));

        bloatModeSubPanel.add(new JLabel("First Down Split"));
        bloatModeSubPanel.add(bloatModeFirstDown);

        bloatModeSubPanel.add(new JLabel("Room Time"));
        bloatModeSubPanel.add(bloatModeTime);

        bloatModeSubPanel.add(new JLabel(""));
        bloatModeSubPanel.add(new JLabel(""));

        bloatModeSubPanel.add(new JLabel(""));
        bloatModeSubPanel.add(new JLabel(""));

        bloatModeSubPanel.add(new JLabel(""));
        bloatModeSubPanel.add(new JLabel(""));

        bloatModeSubPanel.add(new JLabel(""));
        bloatModeSubPanel.add(new JLabel(""));

        bloatModeSubPanel.add(new JLabel(""));
        bloatModeSubPanel.add(new JLabel(""));

        bloatModePanel.add(bloatModeSubPanel);

        JPanel bloatMinimumPanel = new JPanel();
        bloatMinimumPanel.setLayout(new BorderLayout());
        bloatMinimumPanel.setBorder(BorderFactory.createTitledBorder("Minimum"));

        JPanel bloatMinSubPanel = new JPanel();
        bloatMinSubPanel.setLayout(new GridLayout(7, 2));

        bloatMinSubPanel.add(new JLabel("First Down Split"));
        bloatMinSubPanel.add(bloatMinFirstDown);


        bloatMinSubPanel.add(new JLabel("Room Time"));
        bloatMinSubPanel.add(bloatMinTime);


        bloatMinSubPanel.add(new JLabel(""));
        bloatMinSubPanel.add(new JLabel(""));

        bloatMinSubPanel.add(new JLabel(""));
        bloatMinSubPanel.add(new JLabel(""));

        bloatMinSubPanel.add(new JLabel(""));
        bloatMinSubPanel.add(new JLabel(""));

        bloatMinSubPanel.add(new JLabel(""));
        bloatMinSubPanel.add(new JLabel(""));

        bloatMinSubPanel.add(new JLabel(""));
        bloatMinSubPanel.add(new JLabel(""));

        bloatMinimumPanel.add(bloatMinSubPanel);

        JPanel bloatMaximumPanel = new JPanel();
        bloatMaximumPanel.setLayout(new BorderLayout());
        bloatMaximumPanel.setBorder(BorderFactory.createTitledBorder("Maximum"));

        JPanel bloatMaxSubPanel = new JPanel();
        bloatMaxSubPanel.setLayout(new GridLayout(7, 2));

        bloatMaxSubPanel.add(new JLabel("First Down Split"));
        bloatMaxSubPanel.add(bloatMaxFirstDown);

        bloatMaxSubPanel.add(new JLabel("Room Time"));
        bloatMaxSubPanel.add(bloatMaxTime);

        bloatMaxSubPanel.add(new JLabel(""));
        bloatMaxSubPanel.add(new JLabel(""));

        bloatMaxSubPanel.add(new JLabel(""));
        bloatMaxSubPanel.add(new JLabel(""));

        bloatMaxSubPanel.add(new JLabel(""));
        bloatMaxSubPanel.add(new JLabel(""));

        bloatMaxSubPanel.add(new JLabel(""));
        bloatMaxSubPanel.add(new JLabel(""));

        bloatMaxSubPanel.add(new JLabel(""));
        bloatMaxSubPanel.add(new JLabel(""));

        bloatMaximumPanel.add(bloatMaxSubPanel);

        JPanel bloatStatisticsPanel = new JPanel();
        bloatStatisticsPanel.setLayout(new BorderLayout());
        bloatStatisticsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

        bloatPanel.add(bloatAveragePanel);
        bloatPanel.add(bloatMedianPanel);
        bloatPanel.add(bloatModePanel);
        bloatPanel.add(bloatMinimumPanel);
        bloatPanel.add(bloatMaximumPanel);
        bloatPanel.add(bloatStatisticsPanel);


        JComponent nylocasPanel = new JPanel();
        tabbedPane.addTab("Nylocas", nylocasPanel);
        nylocasPanel.setLayout(new GridLayout(2, 3));

        JPanel nylocasAveragePanel = new JPanel();
        nylocasAveragePanel.setLayout(new BorderLayout());
        nylocasAveragePanel.setBorder(BorderFactory.createTitledBorder("Average"));

        JPanel nylocasAverageSubPanel = new JPanel();
        nylocasAverageSubPanel.setLayout(new GridLayout(7, 2));

        nylocasAverageSubPanel.add(new JLabel("Last Wave"));
        nylocasAverageSubPanel.add(nyloAverageLastWave);


        nylocasAverageSubPanel.add(new JLabel("Boss Spawn"));
        nylocasAverageSubPanel.add(nyloAverageBossSpawn);

        nylocasAverageSubPanel.add(new JLabel("Boss Duration"));
        nylocasAverageSubPanel.add(nyloAverageBossDuration);

        nylocasAverageSubPanel.add(new JLabel("Room Time"));
        nylocasAverageSubPanel.add(nyloAverageTime);


        nylocasAverageSubPanel.add(new JLabel(""));
        nylocasAverageSubPanel.add(new JLabel(""));

        nylocasAverageSubPanel.add(new JLabel(""));
        nylocasAverageSubPanel.add(new JLabel(""));

        nylocasAverageSubPanel.add(new JLabel(""));
        nylocasAverageSubPanel.add(new JLabel(""));

        nylocasAveragePanel.add(nylocasAverageSubPanel);

        JPanel nylocasMedianPanel = new JPanel();
        nylocasMedianPanel.setLayout(new BorderLayout());
        nylocasMedianPanel.setBorder(BorderFactory.createTitledBorder("Median"));

        JPanel nylocasMedianSubPanel = new JPanel();
        nylocasMedianSubPanel.setLayout(new GridLayout(7, 2));

        nylocasMedianSubPanel.add(new JLabel("Last Wave"));
        nylocasMedianSubPanel.add(nyloMedianLastWave);

        nylocasMedianSubPanel.add(new JLabel("Boss Spawn"));
        nylocasMedianSubPanel.add(nyloMedianBossSpawn);

        nylocasMedianSubPanel.add(new JLabel("Boss Duration"));
        nylocasMedianSubPanel.add(nyloMedianBossDuration);

        nylocasMedianSubPanel.add(new JLabel("Room Time"));
        nylocasMedianSubPanel.add(nyloMedianTime);

        nylocasMedianSubPanel.add(new JLabel(""));
        nylocasMedianSubPanel.add(new JLabel(""));

        nylocasMedianSubPanel.add(new JLabel(""));
        nylocasMedianSubPanel.add(new JLabel(""));

        nylocasMedianSubPanel.add(new JLabel(""));
        nylocasMedianSubPanel.add(new JLabel(""));

        nylocasMedianPanel.add(nylocasMedianSubPanel);

        JPanel nylocasModePanel = new JPanel();
        nylocasModePanel.setLayout(new BorderLayout());
        nylocasModePanel.setBorder(BorderFactory.createTitledBorder("Mode"));

        JPanel nylocasModeSubPanel = new JPanel();
        nylocasModeSubPanel.setLayout(new GridLayout(7, 2));

        nylocasModeSubPanel.add(new JLabel("Last Wave"));
        nylocasModeSubPanel.add(nyloModeLastWave);

        nylocasModeSubPanel.add(new JLabel("Boss Spawn"));
        nylocasModeSubPanel.add(nyloModeBossSpawn);

        nylocasModeSubPanel.add(new JLabel("Boss Duration"));
        nylocasModeSubPanel.add(nyloModeBossDuration);

        nylocasModeSubPanel.add(new JLabel("Room Time"));
        nylocasModeSubPanel.add(nyloModeTime);

        nylocasModeSubPanel.add(new JLabel(""));
        nylocasModeSubPanel.add(new JLabel(""));

        nylocasModeSubPanel.add(new JLabel(""));
        nylocasModeSubPanel.add(new JLabel(""));

        nylocasModeSubPanel.add(new JLabel(""));
        nylocasModeSubPanel.add(new JLabel(""));

        nylocasModePanel.add(nylocasModeSubPanel);

        JPanel nylocasMinPanel = new JPanel();
        nylocasMinPanel.setLayout(new BorderLayout());
        nylocasMinPanel.setBorder(BorderFactory.createTitledBorder("Minimum"));

        JPanel nylocasMinSubPanel = new JPanel();
        nylocasMinSubPanel.setLayout(new GridLayout(7, 2));

        nylocasMinSubPanel.add(new JLabel("Last Wave"));
        nylocasMinSubPanel.add(nyloMinLastWave);

        nylocasMinSubPanel.add(new JLabel("Boss Spawn"));
        nylocasMinSubPanel.add(nyloMinBossSpawn);

        nylocasMinSubPanel.add(new JLabel("Boss Duration"));
        nylocasMinSubPanel.add(nyloMinBossDuration);

        nylocasMinSubPanel.add(new JLabel("Room Time"));
        nylocasMinSubPanel.add(nyloMinTime);

        nylocasMinSubPanel.add(new JLabel(""));
        nylocasMinSubPanel.add(new JLabel(""));

        nylocasMinSubPanel.add(new JLabel(""));
        nylocasMinSubPanel.add(new JLabel(""));

        nylocasMinSubPanel.add(new JLabel(""));
        nylocasMinSubPanel.add(new JLabel(""));

        nylocasMinPanel.add(nylocasMinSubPanel);

        JPanel nylocasMaxPanel = new JPanel();
        nylocasMaxPanel.setLayout(new BorderLayout());
        nylocasMaxPanel.setBorder(BorderFactory.createTitledBorder("Maximum"));

        JPanel nylocasMaxSubPanel = new JPanel();
        nylocasMaxSubPanel.setLayout(new GridLayout(7, 2));

        nylocasMaxSubPanel.add(new JLabel("Last Wave"));
        nylocasMaxSubPanel.add(nyloMaxLastWave);

        nylocasMaxSubPanel.add(new JLabel("Boss Spawn"));
        nylocasMaxSubPanel.add(nyloMaxBossSpawn);

        nylocasMaxSubPanel.add(new JLabel("Boss Duration"));
        nylocasMaxSubPanel.add(nyloMaxBossDuration);

        nylocasMaxSubPanel.add(new JLabel("Room Time"));
        nylocasMaxSubPanel.add(nyloMaxTime);

        nylocasMaxSubPanel.add(new JLabel(""));
        nylocasMaxSubPanel.add(new JLabel(""));

        nylocasMaxSubPanel.add(new JLabel(""));
        nylocasMaxSubPanel.add(new JLabel(""));

        nylocasMaxPanel.add(nylocasMaxSubPanel);

        JPanel nylocasStatisticsPanel = new JPanel();
        nylocasStatisticsPanel.setLayout(new BorderLayout());
        nylocasStatisticsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

        nylocasPanel.add(nylocasAveragePanel);
        nylocasPanel.add(nylocasMedianPanel);
        nylocasPanel.add(nylocasModePanel);
        nylocasPanel.add(nylocasMinPanel);
        nylocasPanel.add(nylocasMaxPanel);
        nylocasPanel.add(nylocasStatisticsPanel);

        JComponent sotetsegPanel = new JPanel();
        tabbedPane.addTab("Sotetseg", sotetsegPanel);
        sotetsegPanel.setLayout(new GridLayout(2, 3));

        JPanel sotetsegAveragePanel = new JPanel();
        sotetsegAveragePanel.setLayout(new BorderLayout());
        sotetsegAveragePanel.setBorder(BorderFactory.createTitledBorder("Average"));

        JPanel sotetsegAverageSubPanel = new JPanel();
        sotetsegAverageSubPanel.setLayout(new GridLayout(7, 2));

        sotetsegAverageSubPanel.add(new JLabel("Phase 1"));
        sotetsegAverageSubPanel.add(soteAverageP1);

        sotetsegAverageSubPanel.add(new JLabel("Maze 1"));
        sotetsegAverageSubPanel.add(soteAverageM1);

        sotetsegAverageSubPanel.add(new JLabel("Phase 2"));
        sotetsegAverageSubPanel.add(soteAverageP2);

        sotetsegAverageSubPanel.add(new JLabel("Maze 2"));
        sotetsegAverageSubPanel.add(soteAverageM2);

        sotetsegAverageSubPanel.add(new JLabel("Phase 3"));
        sotetsegAverageSubPanel.add(soteAverageP3);

        sotetsegAverageSubPanel.add(new JLabel("Room Time"));
        sotetsegAverageSubPanel.add(soteAverageTime);


        sotetsegAverageSubPanel.add(new JLabel(""));
        sotetsegAverageSubPanel.add(new JLabel(""));

        sotetsegAveragePanel.add(sotetsegAverageSubPanel);

        JPanel sotetsegMedianPanel = new JPanel();
        sotetsegMedianPanel.setLayout(new BorderLayout());
        sotetsegMedianPanel.setBorder(BorderFactory.createTitledBorder("Median"));

        JPanel sotetsegMedianSubPanel = new JPanel();
        sotetsegMedianSubPanel.setLayout(new GridLayout(7, 2));

        sotetsegMedianSubPanel.add(new JLabel("Phase 1"));
        sotetsegMedianSubPanel.add(soteMedianP1);

        sotetsegMedianSubPanel.add(new JLabel("Maze 1"));
        sotetsegMedianSubPanel.add(soteMedianM1);

        sotetsegMedianSubPanel.add(new JLabel("Phase 2"));
        sotetsegMedianSubPanel.add(soteMedianP2);

        sotetsegMedianSubPanel.add(new JLabel("Maze 2"));
        sotetsegMedianSubPanel.add(soteMedianM2);

        sotetsegMedianSubPanel.add(new JLabel("Phase 3"));
        sotetsegMedianSubPanel.add(soteMedianP3);

        sotetsegMedianSubPanel.add(new JLabel("Room Time"));
        sotetsegMedianSubPanel.add(soteMedianTime);

        sotetsegMedianSubPanel.add(new JLabel(""));
        sotetsegMedianSubPanel.add(new JLabel(""));

        sotetsegMedianPanel.add(sotetsegMedianSubPanel);

        JPanel sotetsegModePanel = new JPanel();
        sotetsegModePanel.setLayout(new BorderLayout());
        sotetsegModePanel.setBorder(BorderFactory.createTitledBorder("Mode"));

        JPanel sotetsegModeSubPanel = new JPanel();
        sotetsegModeSubPanel.setLayout(new GridLayout(7, 2));

        sotetsegModeSubPanel.add(new JLabel("Phase 1"));
        sotetsegModeSubPanel.add(soteModeP1);

        sotetsegModeSubPanel.add(new JLabel("Maze 1"));
        sotetsegModeSubPanel.add(soteModeM1);

        sotetsegModeSubPanel.add(new JLabel("Phase 2"));
        sotetsegModeSubPanel.add(soteModeP2);

        sotetsegModeSubPanel.add(new JLabel("Maze 2"));
        sotetsegModeSubPanel.add(soteModeM2);

        sotetsegModeSubPanel.add(new JLabel("Phase 3"));
        sotetsegModeSubPanel.add(soteModeP3);

        sotetsegModeSubPanel.add(new JLabel("Room Time"));
        sotetsegModeSubPanel.add(soteModeTime);

        sotetsegModeSubPanel.add(new JLabel(""));
        sotetsegModeSubPanel.add(new JLabel(""));

        sotetsegModePanel.add(sotetsegModeSubPanel);

        JPanel sotetsegMinPanel = new JPanel();
        sotetsegMinPanel.setLayout(new BorderLayout());
        sotetsegMinPanel.setBorder(BorderFactory.createTitledBorder("Minimum"));

        JPanel sotetsegMinSubPanel = new JPanel();
        sotetsegMinSubPanel.setLayout(new GridLayout(7, 2));

        sotetsegMinSubPanel.add(new JLabel("Phase 1"));
        sotetsegMinSubPanel.add(soteMinP1);

        sotetsegMinSubPanel.add(new JLabel("Maze 1"));
        sotetsegMinSubPanel.add(soteMinM1);

        sotetsegMinSubPanel.add(new JLabel("Phase 2"));
        sotetsegMinSubPanel.add(soteMinP2);

        sotetsegMinSubPanel.add(new JLabel("Maze 2"));
        sotetsegMinSubPanel.add(soteMinM2);

        sotetsegMinSubPanel.add(new JLabel("Phase 3"));
        sotetsegMinSubPanel.add(soteMinP3);

        sotetsegMinSubPanel.add(new JLabel("Room Time"));
        sotetsegMinSubPanel.add(soteMinTime);

        sotetsegMinSubPanel.add(new JLabel(""));
        sotetsegMinSubPanel.add(new JLabel(""));

        sotetsegMinPanel.add(sotetsegMinSubPanel);

        JPanel sotetsegMaxPanel = new JPanel();
        sotetsegMaxPanel.setLayout(new BorderLayout());
        sotetsegMaxPanel.setBorder(BorderFactory.createTitledBorder("Maximum"));

        JPanel sotetsegMaxSubPanel = new JPanel();
        sotetsegMaxSubPanel.setLayout(new GridLayout(7, 2));

        sotetsegMaxSubPanel.add(new JLabel("Phase 1"));
        sotetsegMaxSubPanel.add(soteMaxP1);

        sotetsegMaxSubPanel.add(new JLabel("Maze 1"));
        sotetsegMaxSubPanel.add(soteMaxM1);

        sotetsegMaxSubPanel.add(new JLabel("Phase 2"));
        sotetsegMaxSubPanel.add(soteMaxP2);

        sotetsegMaxSubPanel.add(new JLabel("Maze 2"));
        sotetsegMaxSubPanel.add(soteMaxM2);

        sotetsegMaxSubPanel.add(new JLabel("Phase 3"));
        sotetsegMaxSubPanel.add(soteMaxP3);

        sotetsegMaxSubPanel.add(new JLabel("Room Time"));
        sotetsegMaxSubPanel.add(soteMaxTime);

        sotetsegMaxSubPanel.add(new JLabel(""));
        sotetsegMaxSubPanel.add(new JLabel(""));

        sotetsegMaxPanel.add(sotetsegMaxSubPanel);

        JPanel sotetsegStatisticsPanel = new JPanel();
        sotetsegStatisticsPanel.setLayout(new BorderLayout());
        sotetsegStatisticsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

        sotetsegPanel.add(sotetsegAveragePanel);
        sotetsegPanel.add(sotetsegMedianPanel);
        sotetsegPanel.add(sotetsegModePanel);
        sotetsegPanel.add(sotetsegMinPanel);
        sotetsegPanel.add(sotetsegMaxPanel);
        sotetsegPanel.add(sotetsegStatisticsPanel);

        JComponent xarpusPanel = new JPanel();
        tabbedPane.addTab("Xarpus", xarpusPanel);
        xarpusPanel.setLayout(new GridLayout(2, 3));

        JPanel xarpusAveragePanel = new JPanel();
        xarpusAveragePanel.setLayout(new BorderLayout());
        xarpusAveragePanel.setBorder(BorderFactory.createTitledBorder("Average"));

        JPanel xarpusAverageSubPanel = new JPanel();
        xarpusAverageSubPanel.setLayout(new GridLayout(7, 2));

        xarpusAverageSubPanel.add(new JLabel("Screech Split"));
        xarpusAverageSubPanel.add(xarpAverageScreech);

        xarpusAverageSubPanel.add(new JLabel("Room Time"));
        xarpusAverageSubPanel.add(xarpAverageTime);


        xarpusAverageSubPanel.add(new JLabel(""));
        xarpusAverageSubPanel.add(new JLabel(""));

        xarpusAverageSubPanel.add(new JLabel(""));
        xarpusAverageSubPanel.add(new JLabel(""));

        xarpusAverageSubPanel.add(new JLabel(""));
        xarpusAverageSubPanel.add(new JLabel(""));

        xarpusAverageSubPanel.add(new JLabel(""));
        xarpusAverageSubPanel.add(new JLabel(""));

        xarpusAverageSubPanel.add(new JLabel(""));
        xarpusAverageSubPanel.add(new JLabel(""));

        xarpusAveragePanel.add(xarpusAverageSubPanel);

        JPanel xarpusMedianPanel = new JPanel();
        xarpusMedianPanel.setLayout(new BorderLayout());
        xarpusMedianPanel.setBorder(BorderFactory.createTitledBorder("Median"));

        JPanel xarpusMedianSubPanel = new JPanel();
        xarpusMedianSubPanel.setLayout(new GridLayout(7, 2));

        xarpusMedianSubPanel.add(new JLabel("Screech Split"));
        xarpusMedianSubPanel.add(xarpMedianScreech);

        xarpusMedianSubPanel.add(new JLabel("Room Time"));
        xarpusMedianSubPanel.add(xarpMedianTime);


        xarpusMedianSubPanel.add(new JLabel(""));
        xarpusMedianSubPanel.add(new JLabel(""));

        xarpusMedianSubPanel.add(new JLabel(""));
        xarpusMedianSubPanel.add(new JLabel(""));

        xarpusMedianSubPanel.add(new JLabel(""));
        xarpusMedianSubPanel.add(new JLabel(""));

        xarpusMedianSubPanel.add(new JLabel(""));
        xarpusMedianSubPanel.add(new JLabel(""));

        xarpusMedianSubPanel.add(new JLabel(""));
        xarpusMedianSubPanel.add(new JLabel(""));

        xarpusMedianPanel.add(xarpusMedianSubPanel);

        JPanel xarpusModePanel = new JPanel();
        xarpusModePanel.setLayout(new BorderLayout());
        xarpusModePanel.setBorder(BorderFactory.createTitledBorder("Mode"));

        JPanel xarpusModeSubPanel = new JPanel();
        xarpusModeSubPanel.setLayout(new GridLayout(7, 2));

        xarpusModeSubPanel.add(new JLabel("Screech Split"));
        xarpusModeSubPanel.add(xarpModeScreech);

        xarpusModeSubPanel.add(new JLabel("Room Time"));
        xarpusModeSubPanel.add(xarpModeTime);

        xarpusModeSubPanel.add(new JLabel(""));
        xarpusModeSubPanel.add(new JLabel(""));

        xarpusModeSubPanel.add(new JLabel(""));
        xarpusModeSubPanel.add(new JLabel(""));

        xarpusModeSubPanel.add(new JLabel(""));
        xarpusModeSubPanel.add(new JLabel(""));

        xarpusModeSubPanel.add(new JLabel(""));
        xarpusModeSubPanel.add(new JLabel(""));

        xarpusModeSubPanel.add(new JLabel(""));
        xarpusModeSubPanel.add(new JLabel(""));

        xarpusModePanel.add(xarpusModeSubPanel);

        JPanel xarpusMinPanel = new JPanel();
        xarpusMinPanel.setLayout(new BorderLayout());
        xarpusMinPanel.setBorder(BorderFactory.createTitledBorder("Minimum"));

        JPanel xarpusMinSubPanel = new JPanel();
        xarpusMinSubPanel.setLayout(new GridLayout(7, 2));

        xarpusMinSubPanel.add(new JLabel("Screech Split"));
        xarpusMinSubPanel.add(xarpMinScreech);

        xarpusMinSubPanel.add(new JLabel("Room Time"));
        xarpusMinSubPanel.add(xarpMinTime);

        xarpusMinSubPanel.add(new JLabel(""));
        xarpusMinSubPanel.add(new JLabel(""));

        xarpusMinSubPanel.add(new JLabel(""));
        xarpusMinSubPanel.add(new JLabel(""));

        xarpusMinSubPanel.add(new JLabel(""));
        xarpusMinSubPanel.add(new JLabel(""));

        xarpusMinSubPanel.add(new JLabel(""));
        xarpusMinSubPanel.add(new JLabel(""));

        xarpusMinSubPanel.add(new JLabel(""));
        xarpusMinSubPanel.add(new JLabel(""));

        xarpusMinPanel.add(xarpusMinSubPanel);

        JPanel xarpusMaxPanel = new JPanel();
        xarpusMaxPanel.setLayout(new BorderLayout());
        xarpusMaxPanel.setBorder(BorderFactory.createTitledBorder("Maximum"));

        JPanel xarpusMaxSubPanel = new JPanel();
        xarpusMaxSubPanel.setLayout(new GridLayout(7, 2));

        xarpusMaxSubPanel.add(new JLabel("Screech Split"));
        xarpusMaxSubPanel.add(xarpMaxScreech);

        xarpusMaxSubPanel.add(new JLabel("Room Time"));
        xarpusMaxSubPanel.add(xarpMaxTime);

        xarpusMaxSubPanel.add(new JLabel(""));
        xarpusMaxSubPanel.add(new JLabel(""));

        xarpusMaxSubPanel.add(new JLabel(""));
        xarpusMaxSubPanel.add(new JLabel(""));

        xarpusMaxSubPanel.add(new JLabel(""));
        xarpusMaxSubPanel.add(new JLabel(""));

        xarpusMaxSubPanel.add(new JLabel(""));
        xarpusMaxSubPanel.add(new JLabel(""));

        xarpusMaxSubPanel.add(new JLabel(""));
        xarpusMaxSubPanel.add(new JLabel(""));

        xarpusMaxPanel.add(xarpusMaxSubPanel);

        JPanel xarpusStatisticsPanel = new JPanel();
        xarpusStatisticsPanel.setLayout(new BorderLayout());
        xarpusStatisticsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

        xarpusPanel.add(xarpusAveragePanel);
        xarpusPanel.add(xarpusMedianPanel);
        xarpusPanel.add(xarpusModePanel);
        xarpusPanel.add(xarpusMinPanel);
        xarpusPanel.add(xarpusMaxPanel);
        xarpusPanel.add(xarpusStatisticsPanel);


        JComponent verzikPanel = new JPanel();
        tabbedPane.addTab("Verzik", verzikPanel);
        verzikPanel.setLayout(new GridLayout(2,  3));

        JPanel verzikAveragePanel = new JPanel();
        verzikAveragePanel.setLayout(new BorderLayout());
        verzikAveragePanel.setBorder(BorderFactory.createTitledBorder("Average"));

        JPanel verzikAverageSubPanel = new JPanel();
        verzikAverageSubPanel.setLayout(new GridLayout(7, 2));

        verzikAverageSubPanel.add(new JLabel("Phase 1"));
        verzikAverageSubPanel.add(verzikAverageP1);

        verzikAverageSubPanel.add(new JLabel("Phase 2"));
        verzikAverageSubPanel.add(verzikAverageP2);

        verzikAverageSubPanel.add(new JLabel("Phase 3 Entry"));
        verzikAverageSubPanel.add(verzikAverageP3Entry);

        verzikAverageSubPanel.add(new JLabel("Phase 3"));
        verzikAverageSubPanel.add(verzikAverageP3);

        verzikAverageSubPanel.add(new JLabel("Room Time"));
        verzikAverageSubPanel.add(verzikAverageTime);

        verzikAverageSubPanel.add(new JLabel(""));
        verzikAverageSubPanel.add(new JLabel(""));

        verzikAverageSubPanel.add(new JLabel(""));
        verzikAverageSubPanel.add(new JLabel(""));

        verzikAveragePanel.add(verzikAverageSubPanel);

        JPanel verzikMedianPanel = new JPanel();
        verzikMedianPanel.setLayout(new BorderLayout());
        verzikMedianPanel.setBorder(BorderFactory.createTitledBorder("Median"));

        JPanel verzikMedianSubPanel = new JPanel();
        verzikMedianSubPanel.setLayout(new GridLayout(7, 2));

        verzikMedianSubPanel.add(new JLabel("Phase 1"));
        verzikMedianSubPanel.add(verzikMedianP1);

        verzikMedianSubPanel.add(new JLabel("Phase 2"));
        verzikMedianSubPanel.add(verzikMedianP2);

        verzikMedianSubPanel.add(new JLabel("Phase 3 Entry"));
        verzikMedianSubPanel.add(verzikMedianP3Entry);

        verzikMedianSubPanel.add(new JLabel("Phase 3"));
        verzikMedianSubPanel.add(verzikMedianP3);

        verzikMedianSubPanel.add(new JLabel("Room Time"));
        verzikMedianSubPanel.add(verzikMedianTime);

        verzikMedianSubPanel.add(new JLabel(""));
        verzikMedianSubPanel.add(new JLabel(""));

        verzikMedianSubPanel.add(new JLabel(""));
        verzikMedianSubPanel.add(new JLabel(""));

        verzikMedianPanel.add(verzikMedianSubPanel);

        JPanel verzikModePanel = new JPanel();
        verzikModePanel.setLayout(new BorderLayout());
        verzikModePanel.setBorder(BorderFactory.createTitledBorder("Mode"));

        JPanel verzikModeSubPanel = new JPanel();
        verzikModeSubPanel.setLayout(new GridLayout(7, 2));

        verzikModeSubPanel.add(new JLabel("Phase 1"));
        verzikModeSubPanel.add(verzikModeP1);

        verzikModeSubPanel.add(new JLabel("Phase 2"));
        verzikModeSubPanel.add(verzikModeP2);

        verzikModeSubPanel.add(new JLabel("Phase 3 Entry"));
        verzikModeSubPanel.add(verzikModeP3Entry);

        verzikModeSubPanel.add(new JLabel("Phase 3"));
        verzikModeSubPanel.add(verzikModeP3);

        verzikModeSubPanel.add(new JLabel("Room Time"));
        verzikModeSubPanel.add(verzikModeTime);

        verzikModeSubPanel.add(new JLabel(""));
        verzikModeSubPanel.add(new JLabel(""));

        verzikModeSubPanel.add(new JLabel(""));
        verzikModeSubPanel.add(new JLabel(""));

        verzikModePanel.add(verzikModeSubPanel);

        JPanel verzikMinPanel = new JPanel();
        verzikMinPanel.setLayout(new BorderLayout());
        verzikMinPanel.setBorder(BorderFactory.createTitledBorder("Minimum"));

        JPanel verzikMinSubPanel = new JPanel();
        verzikMinSubPanel.setLayout(new GridLayout(7, 2));

        verzikMinSubPanel.add(new JLabel("Phase 1"));
        verzikMinSubPanel.add(verzikMinP1);

        verzikMinSubPanel.add(new JLabel("Phase 2"));
        verzikMinSubPanel.add(verzikMinP2);

        verzikMinSubPanel.add(new JLabel("Phase 3 Entry"));
        verzikMinSubPanel.add(verzikMinP3Entry);

        verzikMinSubPanel.add(new JLabel("Phase 3"));
        verzikMinSubPanel.add(verzikMinP3);

        verzikMinSubPanel.add(new JLabel("Room Time"));
        verzikMinSubPanel.add(verzikMinTime);

        verzikMinSubPanel.add(new JLabel(""));
        verzikMinSubPanel.add(new JLabel(""));

        verzikMinSubPanel.add(new JLabel(""));
        verzikMinSubPanel.add(new JLabel(""));

        verzikMinPanel.add(verzikMinSubPanel);

        JPanel verzikMaxPanel = new JPanel();
        verzikMaxPanel.setLayout(new BorderLayout());
        verzikMaxPanel.setBorder(BorderFactory.createTitledBorder("Maximum"));

        JPanel verzikMaxSubPanel = new JPanel();
        verzikMaxSubPanel.setLayout(new GridLayout(7, 2));

        verzikMaxSubPanel.add(new JLabel("Phase 1"));
        verzikMaxSubPanel.add(verzikMaxP1);

        verzikMaxSubPanel.add(new JLabel("Phase 2"));
        verzikMaxSubPanel.add(verzikMaxP2);

        verzikMaxSubPanel.add(new JLabel("Phase 3 Entry"));
        verzikMaxSubPanel.add(verzikMaxP3Entry);

        verzikMaxSubPanel.add(new JLabel("Phase 3"));
        verzikMaxSubPanel.add(verzikMaxP3);

        verzikMaxSubPanel.add(new JLabel("Room Time"));
        verzikMaxSubPanel.add(verzikMaxTime);

        verzikMaxSubPanel.add(new JLabel(""));
        verzikMaxSubPanel.add(new JLabel(""));

        verzikMaxSubPanel.add(new JLabel(""));
        verzikMaxSubPanel.add(new JLabel(""));

        verzikMaxPanel.add(verzikMaxSubPanel);

        JPanel verzikStatisticsPanel = new JPanel();
        verzikStatisticsPanel.setLayout(new BorderLayout());
        verzikStatisticsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

        verzikPanel.add(verzikAveragePanel);
        verzikPanel.add(verzikMedianPanel);
        verzikPanel.add(verzikModePanel);
        verzikPanel.add(verzikMinPanel);
        verzikPanel.add(verzikMaxPanel);
        verzikPanel.add(verzikStatisticsPanel);

        tabbedPane.setMinimumSize(new Dimension(100,200));

        JPanel additionalFiltersPanel = new JPanel();
        additionalFiltersPanel.setLayout(new BorderLayout());
        additionalFiltersPanel.setBorder(BorderFactory.createTitledBorder("Additional Filters"));
        additionalFiltersPanel.setMinimumSize(new Dimension(200, 200));

         filterSpectateOnly = new JCheckBox("Spectate Only");
         filterInRaidOnly = new JCheckBox("In Raid Only");
         filterCompletionOnly = new JCheckBox("Completion Only");
         filterWipeResetOnly = new JCheckBox("Wipe/Reset Only");
         filterComboBoxScale = new JComboBox(new String[]{"Solo", "Duo", "Trio", "4-Man", "5-Man"});
         filterCheckBoxScale = new JCheckBox("Scale");
         filterTodayOnly = new JCheckBox("Today Only");
         filterPartyOnly = new JCheckBox("Party Only");

        filterSpectateOnly.addActionListener(
                al->
                {
                    updateTable();
                });
        filterInRaidOnly.addActionListener(
                al->
                {
                    updateTable();
                });
        filterCompletionOnly.addActionListener(
                al->
                {
                    updateTable();
                });
        filterWipeResetOnly.addActionListener(
                al->
                {
                    updateTable();
                });
        filterComboBoxScale.addActionListener(
                al->
                {
                    updateTable();
                });
        filterCheckBoxScale.addActionListener(
                al->
                {
                    updateTable();
                });
        filterTodayOnly.addActionListener(
                al->
                {
                    updateTable();
                });
        filterPartyOnly.addActionListener(
                al->
                {
                    updateTable();
                });


        JPanel scaleContainer = new JPanel();
        scaleContainer.setLayout(new BoxLayout(scaleContainer, BoxLayout.X_AXIS));


        JPanel filterHolder = new JPanel();
        filterHolder.setLayout(new GridLayout(8, 1));
        filterHolder.add(filterSpectateOnly);
        filterHolder.add(filterInRaidOnly);
        filterHolder.add(filterCompletionOnly);
        filterHolder.add(filterWipeResetOnly);
        filterHolder.add(filterTodayOnly);
        filterHolder.add(filterPartyOnly);
        scaleContainer.add(filterCheckBoxScale);
        scaleContainer.add(filterComboBoxScale);
        filterHolder.add(scaleContainer);



        JLabel blank6 = new JLabel("");
        filterHolder.add(blank6);

        additionalFiltersPanel.add(filterHolder);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));


        topContainer.add(tabbedPane);
        topContainer.add(additionalFiltersPanel);

        setLabels(data);

        //container.add(tabbedPane);
        container.add(topContainer);
        container.add(tablePanel);
        add(container);
        pack();
    }

    protected JComponent makeTextPanel(String text)
    {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
}
