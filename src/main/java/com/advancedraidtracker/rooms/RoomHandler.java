package com.advancedraidtracker.rooms;


import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.AdvancedRaidTrackerPlugin;
import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.datautility.DataWriter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.*;
import com.advancedraidtracker.utility.RoomUtil;

import java.util.HashMap;
import java.util.Map;

import static net.runelite.api.Skill.*;

@Slf4j
public class RoomHandler
{
    protected Client client;
    protected DataWriter clog;
    protected AdvancedRaidTrackerPlugin plugin;
    public int roomStartTick = -1;
    protected boolean active = false;
    private boolean wasActive = false;

    protected final AdvancedRaidTrackerConfig config;
    protected int currentPrayer = -1;
    protected boolean accurateTimer = true;
    protected boolean accurateEntry = true;
    protected int prayerDrained = 0;
    protected int damageDealt = 0;
    protected int damageReceived = 0;

    protected Map<Skill, Integer> xpGained = new HashMap<>();
    protected Map<Skill, Integer> lastXP = new HashMap<>();
    private static final Map<Skill, LogID> skillToIDMap = Map.of(
            ATTACK, LogID.ROOM_ATTACK_XP_GAINED,
            STRENGTH, LogID.ROOM_STRENGTH_XP_GAINED,
            DEFENCE, LogID.ROOM_DEFENSE_XP_GAINED,
            HITPOINTS, LogID.ROOM_HP_XP_GAINED,
            MAGIC, LogID.ROOM_MAGIC_XP_GAINED,
            RANGED, LogID.ROOM_RANGE_XP_GAINED,
            AGILITY, LogID.ROOM_AGILITY_XP_GAINED,
            FLETCHING, LogID.ROOM_FLETCHING_XP_GAINED,
            SLAYER, LogID.ROOM_SLAYER_XP_GAINED
    );

    @Getter
    @Setter
    int scale;

    protected String defaultColor()
    {
        return (client.isResized()) ? "<col=FFFFFF>" : "<col=000000>";
    }

    protected String timeColor()
    {
        return "<col=EF1020>";
    }

    public RoomHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin)
    {
        this.client = client;
        this.clog = clog;
        this.config = config;
        this.plugin = plugin;
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
        if (config.chatSplits())
        {
            String splitMessage = message + timeColor() + RoomUtil.time(duration) + entry() + accuracy();
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
        }
    }

    protected void sendTimeMessage(String message, int duration, int split)
    {
        if (config.chatSplits())
        {
            String splitMessage = message + timeColor() + RoomUtil.time(duration) + entry() + accuracy() + " (" + RoomUtil.time(split) + ")";
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
        }
    }

    protected void sendTimeMessage(String message, int duration, int split, boolean bloat)
    {
        if (config.chatSplits())
        {
            String splitMessage;
            if (bloat)
            {
                splitMessage = message + timeColor() + duration + entry() + accuracy() + " (" + RoomUtil.time(split) + ")";
            } else
            {
                splitMessage = message + timeColor() + RoomUtil.time(split) + entry() + accuracy() + defaultColor() + " Room time: " + timeColor() + RoomUtil.time(duration);
            }
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
        }
    }

    protected void sendTimeMessage(String message, int duration, int split, boolean bloat, String alternate)
    {
        if (config.chatSplits())
        {
            String splitMessage;
            if (bloat)
            {
                splitMessage = message + timeColor() + duration + entry() + accuracy() + " (" + RoomUtil.time(split) + ")" + defaultColor() + alternate;
            } else
            {
                splitMessage = message + timeColor() + RoomUtil.time(split) + entry() + accuracy() + defaultColor() + " Room time: " + timeColor() + RoomUtil.time(duration) + defaultColor() + alternate;
            }
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
        }
    }

    protected void sendTimeMessage(String message, int duration, String alternateText, int alternateNumber)
    {
        if (config.chatSplits())
        {
            String splitMessage = message + timeColor() + RoomUtil.time(duration) + entry() + accuracy() + defaultColor() + alternateText + timeColor() + alternateNumber;
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
        }
    }


    protected void sendTimeMessage(String message, int duration, String alternateText, int alternateNumber, boolean bloat)
    {
        if (config.chatSplits())
        {
            String splitMessage = message + timeColor() + duration + entry() + accuracy() + alternateText + RoomUtil.time(alternateNumber);
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", splitMessage, null, false);
        }
    }

    public void updateChatMessage(ChatMessage message)
    {

    }

    public void handleNPCChanged(int id)
    {
    }

    public void updateNpcSpawned(NpcSpawned event)
    {
    }

    public void updateNpcDespawned(NpcDespawned event)
    {
    }

    public void updateOverheadText(OverheadTextChanged event)
    {
    }

    public void updateGameTick(GameTick event)
    {
        for(Skill skill : skillToIDMap.keySet())
        {
            xpGained.merge(skill, Math.max(client.getSkillExperience(skill)-lastXP.getOrDefault(skill, Integer.MAX_VALUE), 0), Integer::sum);
            lastXP.put(skill, client.getSkillExperience(skill));
        }
        currentPrayer = client.getBoostedSkillLevel(Skill.PRAYER);
        if (wasActive)
        {
            if (!active)
            {
                wasActive = false;
                writePrayerDamageXPData();
            }
        } else
        {
            if (active)
            {
                wasActive = true;
            }
        }
    }

    private void writePrayerDamageXPData()
    {
        clog.addLine(LogID.ROOM_PRAYER_DRAINED, String.valueOf(prayerDrained), getName());
        clog.addLine(LogID.ROOM_DAMAGE_RECEIVED, String.valueOf(damageReceived), getName());
        clog.addLine(LogID.ROOM_DAMAGE_DEALT, String.valueOf(damageDealt), getName());
        for(Skill skill : xpGained.keySet())
        {
            int gained = xpGained.get(skill);
            if(gained > 0)
            {
                clog.addLine(skillToIDMap.get(skill), String.valueOf(gained), getName());
            }
        }
        xpGained.clear();
        lastXP.clear();
        prayerDrained = 0;
        damageDealt = 0;
        damageReceived = 0;
    }

    public void updateItemSpawned(ItemSpawned event)
    {

    }

    public void updateAnimationChanged(AnimationChanged event)
    {
    }

    public void updateInteractingChanged(InteractingChanged event)
    {
    }

    public void updateHitsplatApplied(HitsplatApplied event)
    {
        if (event.getActor().equals(client.getLocalPlayer()))
        {
            damageReceived += event.getHitsplat().getAmount();
        } else
        {
            if (event.getHitsplat().isMine())
            {
                damageDealt += event.getHitsplat().getAmount();
            }
        }
    }

    public void updateGraphicChanged(GraphicChanged event)
    {
    }

    public void updateStatChanged(StatChanged statChanged)
    {
        if (statChanged.getSkill().equals(Skill.PRAYER))
        {
            if (statChanged.getBoostedLevel() == currentPrayer - 1)
            {
                prayerDrained++; //todo reduction
            }
        }
    }

    public void updateProjectileMoved(ProjectileMoved event)
    {
    }

    public void updateGraphicsObjectCreated(GraphicsObjectCreated event)
    {
    }

    public void updateGameObjectSpawned(GameObjectSpawned event)
    {
    }

    public void updateGameObjectDespawned(GameObjectDespawned event)
    {
    }

    public void updateGroundObjectSpawned(GroundObjectSpawned event)
    {

    }

    public String getName()
    {
        return "";
    }

    public boolean isActive()
    {
        return false;
    }

    public void reset()
    {
        writePrayerDamageXPData();
        roomStartTick = -1;
        active = false;
        wasActive = false;
        xpGained.clear();
        lastXP.clear();
    }

    public void updateGroundObjectDespawned(GroundObjectDespawned event)
    {
    }

    public void updateScriptPreFired(ScriptPreFired event)
    {
    }
}
