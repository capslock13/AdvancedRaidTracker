package com.advancedraidtracker.rooms.toa;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.Point;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Tile;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;

import static com.advancedraidtracker.constants.ToaIDs.SCABARAS_GATE_OBJECT;

@Slf4j
public class ScabarasHandler extends TOARoomHandler
{
    public String getName()
    {
        return "Scabaras";
    }

    public ScabarasHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config, plugin, handler);
    }

    @Override
    public void updateChatMessage(ChatMessage message)
    {
        if(active && message.getSender() == null && message.getMessage().startsWith("Challenge complete: Path of Scabaras."))
        {
            endScabaras();
        }
    }
    @Override
    public void updateGameTick(GameTick event)
    {
        if (!active)
        {
            if (RoomUtil.crossedLine(14162, new Point(13, 26), new Point(13, 38), true, client))
            {
                roomStartTick = client.getTickCount();
                active = true;
                clog.addLine(LogID.TOA_SCABARAS_START, roomStartTick);
            }
        }
    }

    public boolean isActive()
    {
        return active;
    }

    public void endScabaras()
    {
        active = false;
        int scabarasDuration = client.getTickCount() - roomStartTick;
        sendTimeMessage("Scabaras Puzzle time: ", scabarasDuration);
        clog.addLine(LogID.TOA_SCABARAS_FINISHED, scabarasDuration);
        plugin.liveFrame.setRoomFinished(getName(), scabarasDuration);
    }
}
