package com.advancedraidtracker.rooms;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.rooms.tob.RoomHandler;
import com.advancedraidtracker.ui.charts.LiveChart;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.util.Text;
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
            String[] split = message.getMessage().split(" ");
            if(split.length >= 3)
            {
                String[] subSplit = Text.removeTags(split[2]).split(":");
                int timeSplit = (Integer.parseInt(subSplit[0])*100) + (int)(Double.parseDouble(subSplit[1])/0.6);
                lastWaveDuration = timeSplit-timeSum;
                clog.addLine(LogID.COLOSSEUM_WAVE_12_END, timeSplit);
                timeSum += timeSplit;
                lastCompletedWave = 12;
            }
        }
    }
}
