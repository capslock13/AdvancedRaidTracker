package com.TheatreTracker.rooms;


import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.utility.DataWriter;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import com.TheatreTracker.utility.RoomUtil;

public class RoomHandler
{
    protected Client client;

    DataWriter clog;

    public static final int ROOM_ACTIVE_VARBIT = 6447;

    public static final int HP_VARBIT = 6448;

    public static final int FULL_HP = 1000;


    private final TheatreTrackerConfig config;
    protected boolean accurateTimer = true;
    protected boolean accurateEntry = true;
    @Getter @Setter
    int scale;

    protected String defaultColor()
    {
        return (client.isResized()) ? "<col=FFFFFF>" : "<col=000000>";
    }

    protected String timeColor()
    {
        return "<col=EF1020>";
    }

    public RoomHandler(Client client, DataWriter clog, TheatreTrackerConfig config)
    {
        this.client = client;
        this.clog = clog;
        this.config = config;
    }

    private String accuracy()
    {
        return (accurateTimer) ? "" : "**";
    }

    private String entry()
    {
        return (accurateEntry) ? "" : "*";
    }

    protected void sendTimeMessage(String message, int duration)
    {
        if(config.chatSplits())
        {
            String splitMessage = message + timeColor() + RoomUtil.time(duration) + entry() + accuracy();
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
        }
    }

    protected void sendTimeMessage(String message, int duration, int split)
    {
        if(config.chatSplits())
        {
            String splitMessage = message + timeColor() + RoomUtil.time(duration) + entry() + accuracy() + " (" + RoomUtil.time(split) + ")";
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
        }
    }

    protected void sendTimeMessage(String message, int duration, int split, boolean bloat)
    {
        if(config.chatSplits())
        {
            String splitMessage;
            if (bloat) {
                splitMessage = message + timeColor() + duration + entry() + accuracy() + " (" + RoomUtil.time(split) + ")";
            } else {
                splitMessage = message + timeColor() + RoomUtil.time(split) + entry() + accuracy() + defaultColor() + " Room time: " + timeColor() + RoomUtil.time(duration);
            }
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
        }
    }

    protected void sendTimeMessage(String message, int duration, String alternateText, int alternateNumber)
    {
        if(config.chatSplits())
        {
            String splitMessage = message + timeColor() + RoomUtil.time(duration) + entry() + accuracy() + defaultColor() + alternateText + timeColor() + alternateNumber;
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
        }
    }

    protected void sendTimeMessage(String message, int duration, String alternateText, int alternateNumber, boolean bloat)
    {
        if(config.chatSplits())
        {
            String splitMessage = message + timeColor() + duration + entry() + accuracy() + alternateText + RoomUtil.time(alternateNumber);
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
        }
    }

    public void handleNPCChanged(int id) { }

    public void updateNpcSpawned(NpcSpawned event) { }

    public void updateNpcDespawned(NpcDespawned event) { }

    public void updateOverheadText(OverheadTextChanged event) { }

    public void updateGameTick(GameTick event) { }

    public void updateAnimationChanged(AnimationChanged event) { }

    public void updateInteractingChanged(InteractingChanged event) { }

    public void updateHitsplatApplied(HitsplatApplied event) { }

    public void updateGraphicChanged(GraphicChanged event) { }

    public void updateProjectileMoved(ProjectileMoved event) {}

    public void reset() { }
}
