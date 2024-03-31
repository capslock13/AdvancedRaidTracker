package com.advancedraidtracker.rooms.col;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.rooms.tob.RoomHandler;
import com.advancedraidtracker.ui.charts.LiveChart;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataWriter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.client.util.Text;

import java.util.*;

@Slf4j
public class ColosseumHandler extends RoomHandler
{
    public String getName()
    {
        return "Wave " + currentWave;
    }
    public int currentWave = 0;
    public int lastCompletedWave = 0;
    public int timeSum = 0;
    public int lastWaveDuration = 0;
    int lastWaveStartTick = 0;
    private LiveChart liveFrame;
    List<Integer> currentInvos = new ArrayList<>();
    private Map<Integer, Integer> selectedInvos = new HashMap<>();
    private Multimap<Integer, Integer> offeredInvos = ArrayListMultimap.create();
    private Map<Integer, String> invoMap = new HashMap<Integer, String>()
    {{
        put(0, "Scorpion");
        put(1, "Reentry");
        put(2, "Bees");
        put(3, "Volatility");
        put(4, "Blasphemy");
        put(5, "Relentless");
        put(6, "Quartet");
        put(7, "Totemic");
        put(8, "Doom");
        put(9, "Dynamic Duo");
        put(10, "Solarflare");
        put(11, "Myopia");
        put(12, "Frailty");
        put(13, "Red Flag");
    }};
    public ColosseumHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, LiveChart liveFrame, AdvancedRaidTrackerPlugin plugin)
    {
        super(client, clog, config, plugin);
        this.liveFrame = liveFrame;
    }

    private boolean active = false;
    @Override
    public boolean isActive()
    {
        return active;
    }


    @Override
    public void reset()
    {
        super.reset();
        currentWave = 0;
        timeSum = 0;
        lastWaveStartTick = 0;
        lastCompletedWave = 0;
        lastWaveDuration = 0;
        offeredInvos.clear();
        selectedInvos.clear();
        active = false;
    }

    public void updateScriptPreFired(ScriptPreFired event)
    {
        if(event.getScriptId() == 4931)
        {
            try
            {
                Object[] args = event.getScriptEvent().getArguments();
                currentInvos.add((Integer)args[2]);
                currentInvos.add((Integer)args[3]);
                currentInvos.add((Integer)args[4]);
                offeredInvos.putAll(currentWave+1, currentInvos);
                clog.addLine(LogID.COLOSSEUM_INVOCATION_CHOICES, String.valueOf(args[2]), String.valueOf(args[3]), String.valueOf(args[4]));
            }
            catch (Exception e)
            {

            }
        }
    }

    public String getInvos()
    {
        String list = "";
        Map<Integer, Integer> invoLevels = new HashMap<>();
        for(Integer invo : selectedInvos.values())
        {
            invoLevels.merge(invo, 1, Integer::sum);
        }
        int index = 0;
        for(Integer invo : invoLevels.keySet())
        {
            if(index%3==0 && index != 0)
            {
                list += "\n";
            }
            list += invoMap.get(invo);
            if(invoLevels.get(invo) > 1)
            {
                list += invoLevels.get(invo);
            }
            list += " ";
            index++;
        }
        if(!list.endsWith("\n"))
        {
            list += "\n";
        }
        return list;
    }

    @Override
    public void updateNpcDespawned(NpcDespawned event)
    {
        if(event.getNpc().getId() == 12808)
        {
            int selection = client.getVarbitValue(9788);
            if(selection > 0)
            {
                selectedInvos.put(currentWave+1, currentInvos.get(selection-1));
                clog.addLine(LogID.COLOSSEUM_INVOCATION_SELECTED, currentInvos.get(selection-1));
            }
            currentInvos.clear();
        }
    }

    public String getCurrentInvos()
    {
        StringBuilder invoString = new StringBuilder();
        for(Integer i : offeredInvos.get(currentWave))
        {
            if(Objects.equals(selectedInvos.get(currentWave), i))
            {
                invoString.append(" (").append(invoMap.get(i)).append(") ");
            }
            else
            {
                invoString.append(" ").append(invoMap.get(i)).append(" ");
            }
        }
        return invoString.toString();
    }

    @Override
    public void updateChatMessage(ChatMessage message)
    {
        if(Text.removeTags(message.getMessage()).contains("Wave: "))
        {
            currentWave++;
            active = true;
            roomStartTick = client.getTickCount();
            liveFrame.tabbedPane.setSelectedIndex(currentWave-1);
            clog.addLine(LogID.COLOSSEUM_WAVE_STARTED, currentWave);
        }
        if(message.getMessage().contains("Wave duration: "))
        {
            active = false;
            String[] messageSplit = message.getMessage().split(" ");
            if(messageSplit.length == 6)
            {
                String time = Text.removeTags(messageSplit[5]);
                String[] timeSplit = time.split(":");
                if(timeSplit.length == 2)
                {
                    int duration = (Integer.parseInt(timeSplit[0])*100) + (int)(Double.parseDouble(timeSplit[1])/0.6);
                    timeSum += duration;
                    lastCompletedWave = Integer.parseInt(messageSplit[1]);
                    lastWaveDuration = duration;
                    plugin.lastSplits += "Wave: " + messageSplit[1] + ", Split: " + RoomUtil.time(timeSum) + " (+" + RoomUtil.time(duration) + ")\n";
                    switch(messageSplit[1])
                    {
                        case "1":
                            clog.addLine(LogID.COLOSSEUM_WAVE_1_END, duration);
                            break;
                        case "2":
                            clog.addLine(LogID.COLOSSEUM_WAVE_2_END, duration);
                            break;
                        case "3":
                            clog.addLine(LogID.COLOSSEUM_WAVE_3_END, duration);
                            break;
                        case "4":
                            clog.addLine(LogID.COLOSSEUM_WAVE_4_END, duration);
                            break;
                        case "5":
                            clog.addLine(LogID.COLOSSEUM_WAVE_5_END, duration);
                            break;
                        case "6":
                            clog.addLine(LogID.COLOSSEUM_WAVE_6_END, duration);
                            break;
                        case "7":
                            clog.addLine(LogID.COLOSSEUM_WAVE_7_END, duration);
                            break;
                        case "8":
                            clog.addLine(LogID.COLOSSEUM_WAVE_8_END, duration);
                            break;
                        case "9":
                            clog.addLine(LogID.COLOSSEUM_WAVE_9_END, duration);
                            break;
                        case "10":
                            clog.addLine(LogID.COLOSSEUM_WAVE_10_END, duration);
                            break;
                        case "11":
                            clog.addLine(LogID.COLOSSEUM_WAVE_11_END, duration);
                            break;
                        case "12":
                            clog.addLine(LogID.COLOSSEUM_WAVE_12_END, duration);
                            break;
                    }
                }
            }
        }
        else if(message.getMessage().contains("Colosseum duration: "))
        {
            log.info("Found duration msg: " + message.getMessage());
            String[] split = message.getMessage().split(" ");
            if(split.length >= 3)
            {
                String[] subSplit = Text.removeTags(split[2]).split(":");
                String timeMessage = subSplit[1];
                if(subSplit[1].endsWith("."))
                {
                    timeMessage = subSplit[1].substring(0, subSplit[1].length()-1);
                }
                int timeSplit = (Integer.parseInt(subSplit[0])*100) + (int)(Double.parseDouble(timeMessage)/0.6);
                lastWaveDuration = timeSplit-timeSum;
                clog.addLine(LogID.COLOSSEUM_WAVE_12_END, timeSplit);
                timeSum += timeSplit;
                lastCompletedWave = 12;
            }
        }
        else if(message.getMessage().contains("Sol Heredit jumps down"))
        {
            currentWave++;
            liveFrame.tabbedPane.setSelectedIndex(currentWave-1);
            active = true;
            roomStartTick = client.getTickCount();
        }
    }
}
