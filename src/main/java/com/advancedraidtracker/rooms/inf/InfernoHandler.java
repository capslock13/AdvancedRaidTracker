package com.advancedraidtracker.rooms.inf;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.rooms.tob.RoomHandler;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.util.Text;

import java.util.*;

@Slf4j
public class InfernoHandler extends RoomHandler
{
    Map<Integer, Integer> waveStartTicks = new LinkedHashMap<>();
    Map<Integer, Integer> waveDurations = new LinkedHashMap<>();
    int totalPrayer = 0;
    int totalDamageDealt = 0;
    int totalDamageReceived = 0;
    public static Map<Integer, String> roomMap = new LinkedHashMap<>()
    {{
        put(1, "Wave 1-8");
        put(9, "Wave 9-17");
        put(18, "Wave 18-24");
        put(25, "Wave 25-34");
        put(35, "Wave 35-41");
        put(42, "Wave 42-49");
        put(50, "Wave 50-56");
        put(57, "Wave 57-59");
        put(60, "Wave 60-62");
        put(63, "Wave 63-65");
        put(66, "Wave 66");
        put(67, "Wave 67");
        put(68, "Wave 68");
        put(69, "Wave 69");
    }};
    int currentWave = 0;
    public int lastCompletedWave = 0;
    int lastWaveCheckPoint = 1;
    public int entireDurationTime = 0;

    public InfernoHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin)
    {
        super(client, clog, config, plugin);
    }

    public void start()
    {

    }

    @Override
    public void updateChatMessage(ChatMessage message)
    {
        if (message.getMessage().contains("Wave: "))
        {
            if (roomStartTick < 1 && message.getMessage().contains("Wave: 1"))
            {
                roomStartTick = client.getTickCount() - 10; //timer starts 10t before wave 1
                waveStartTicks.put(0, client.getTickCount() - 10);
                clog.addLine(LogID.INFERNO_TIMER_STARTED, client.getTickCount() - 10);
            }
            active = true;
            currentWave++;
            String[] split = Text.removeTags(message.getMessage()).split(" ");
            if (split.length > 1)
            {
                int wave = Integer.parseInt(split[1]);
                waveStartTicks.put(currentWave, (client.getTickCount()));
                clog.addLine(LogID.INFERNO_WAVE_STARTED, String.valueOf(currentWave), String.valueOf(client.getTickCount()), "Inf Wave " + currentWave);
                if (getLastRelevantSplit() > lastWaveCheckPoint)
                {
                    plugin.lastSplits += "Wave: " + (currentWave) + ", Split: " + RoomUtil.time(client.getTickCount() - waveStartTicks.get(0)) + " (+" + RoomUtil.time(client.getTickCount() - waveStartTicks.get(lastWaveCheckPoint - 1)) + ")\n";
                    lastWaveCheckPoint = getLastRelevantSplit();
                }
            }
        } else if (message.getMessage().contains("Wave completed!"))
        {
            totalPrayer += prayerDrained;
            totalDamageReceived += damageReceived;
            totalDamageDealt += damageDealt;
            active = false;
            waveDurations.put(currentWave, (client.getTickCount() - waveStartTicks.get(currentWave)));
            lastCompletedWave = currentWave;
            clog.addLine(LogID.INFERNO_WAVE_ENDED, String.valueOf(currentWave), String.valueOf(waveDurations.get(currentWave)));
            entireDurationTime += waveDurations.get(currentWave);
        } else if (message.getMessage().contains("Duration: "))
        {
            waveDurations.put(currentWave, (client.getTickCount() - waveStartTicks.get(currentWave)));
            entireDurationTime += waveDurations.get(currentWave);
            lastCompletedWave = currentWave;
            String text = Text.removeTags(message.getMessage());
            String[] splitText = text.split(" ");
            if (splitText.length > 2)
            {
                String timeString = splitText[1];
                if (timeString.endsWith("."))
                {
                    timeString = timeString.substring(0, timeString.length() - 1);
                }
                String[] timeSplit = timeString.split(":");
                int timeInTicks = 0;
                if (timeSplit.length == 2)
                {
                    timeInTicks += (int) ((60 * Integer.parseInt(timeSplit[0])) / .6);
                    timeInTicks += (int) (Double.parseDouble(timeSplit[1]) / .6);
                } else if (timeSplit.length == 3)
                {
                    timeInTicks += (int) ((60 * 60 * Integer.parseInt(timeSplit[0])) / .6);
                    timeInTicks += (int) ((60 * Integer.parseInt(timeSplit[1])) / .6);
                    timeInTicks += (int) (Double.parseDouble(timeSplit[2]) / .6);
                }
                clog.addLine(LogID.INFERNO_WAVE_ENDED, String.valueOf(currentWave), String.valueOf(timeInTicks));
            }
        }
    }

    @Override
    public void updateGameTick(GameTick event)
    {
        super.updateGameTick(event);
    }

    @Override
    public void updateNpcSpawned(NpcSpawned event)
    {
        NPC npc = event.getNpc();
        WorldPoint wp = npc.getWorldLocation();
        String respawn = (waveStartTicks.getOrDefault(currentWave, -1) != client.getTickCount()) ? "Yes" : "No";
        clog.addLine(LogID.INFERNO_NPC_SPAWN, String.valueOf(npc.getId()), String.valueOf(wp.getRegionX()), String.valueOf(wp.getRegionY()), respawn);
    }

    public String getStatString()
    {
        return "Prayer: " + totalPrayer + ", Dealt: " + totalDamageDealt + ", Received: " + totalDamageReceived + "\n";
    }

    public int getLastWaveStartTime()
    {
        return waveStartTicks.get(currentWave);
    }

    public int getLastRelevantSplit()
    {
        return getLastRelevantSplit(currentWave);
    }

    public static int getLastRelevantSplit(int wave)
    {
        int lastSplit = 1;
        for (Map.Entry<Integer, String> entry : roomMap.entrySet())
        {
            if (entry.getKey() <= wave)
            {
                lastSplit = entry.getKey();
            } else
            {
                break;
            }
        }
        return lastSplit;
    }

    public String getName()
    {
        return "Inf Wave " + currentWave;
    }

    public int getDuration()
    {
        return entireDurationTime;
    }

    @Override
    public void reset()
    {
        currentWave = 0;
        lastCompletedWave = 0;
        waveStartTicks.clear();
        totalDamageDealt = 0;
        totalDamageReceived = 0;
        totalPrayer = 0;
        active = false;
        entireDurationTime = 0;
        super.reset();
    }

    public void forceStatUpdate()
    {
        totalDamageReceived += damageReceived;
        totalDamageDealt += damageDealt;
        totalPrayer += prayerDrained;
    }
}
