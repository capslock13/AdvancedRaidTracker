package com.TheatreTracker.rooms;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.TheatreTrackerPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import com.TheatreTracker.Point;
import com.TheatreTracker.utility.RoomUtil;
import com.TheatreTracker.utility.DataWriter;

import java.util.ArrayList;

import static com.TheatreTracker.constants.LogID.*;
import static com.TheatreTracker.constants.NpcIDs.*;
import static com.TheatreTracker.utility.RoomState.*;
import static com.TheatreTracker.utility.RoomState.BloatRoomState.*;

@Slf4j
public class BloatHandler extends RoomHandler {
    public BloatRoomState roomState;

    private final ArrayList<Integer> walks = new ArrayList<>();
    private final ArrayList<Integer> downs = new ArrayList<>();
    private int bloatStartTick = -1;
    private int bloatDeferTick = -1;
    private int bloatDeathTick = -1;
    private TheatreTrackerPlugin plugin;

    public BloatHandler(Client client, DataWriter clog, TheatreTrackerConfig config, TheatreTrackerPlugin plugin)
    {
        super(client, clog, config);
        this.plugin = plugin;
        roomState = NOT_STARTED;
    }

    public boolean isActive()
    {
        return !(roomState == NOT_STARTED || roomState == FINISHED);
    }

    public String getName()
    {
        return "Bloat";
    }
    public void reset()
    {
        roomState = NOT_STARTED;
        bloatDeferTick = -1;
        accurateEntry = true;
        bloatStartTick = -1;
        bloatDeathTick = -1;
        walks.clear();
        downs.clear();
        super.reset();
    }

    public void endBloat() {
        roomState = FINISHED;
        bloatDeathTick = client.getTickCount() + 3;
        clog.write(ACCURATE_BLOAT_END);
        if (bloatStartTick != -1)
            sendTimeMessage("Wave 'Bloat last down' complete! Duration: ", splitLastDown(), " Room time: ", bloatDeathTick - bloatStartTick, true);
    }

    public int splitLastDown() {
        if (bloatDeathTick != -1) {
            if (walks.size() == downs.size() + 1) {
                return bloatDeathTick - walks.get(walks.size() - 1);
            } else if (walks.size() == downs.size()) {
                return bloatDeathTick - downs.get(walks.size() - 1);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public void start() {
        bloatStartTick = client.getTickCount();
        roomStartTick = client.getTickCount();
        roomState = WALKING;
    }

    private int getLastWalk() {
        if (downs.size() != 0 && walks.size() != 0 && downs.size() == walks.size()) {
            return downs.get(downs.size() - 1) - walks.get(walks.size() - 1);
        } else {
            return -1;
        }
    }

    private int getLastDownTime() {
        if (downs.size() != 0) {
            return downs.get(downs.size() - 1) - bloatStartTick;
        } else {
            return -1;
        }
    }

    public void down()
    {
        clog.write(BLOAT_DOWN, "" + (client.getTickCount() - bloatStartTick));
        if (downs.size() == 0) {
            int currentBloatHP = client.getVarbitValue(HP_VARBIT);
            clog.write(BLOAT_HP_1ST_DOWN, "" + currentBloatHP);
        }
        downs.add(client.getTickCount());
        roomState = DOWN;
        if (bloatStartTick != -1) {
            bloatDeferTick = client.getTickCount() + 5;
        }
    }

    public void walk() {
        walks.add(client.getTickCount());
        roomState = WALKING;
    }

    public void updateGameTick(GameTick event) {
        if (bloatDeferTick != -1 && bloatDeferTick == client.getTickCount()) {
            sendTimeMessage("Wave 'Bloat walk' complete! Duration: ", getLastWalk(), getLastDownTime(), true);
            bloatDeferTick = -1;
        }
        if (bloatStartTick == -1 && RoomUtil.crossedLine(RoomUtil.BLOAT_REGION, new Point(39, 30), new Point(39, 33), true, client)) {
            start();
            walk();
        }

        if (RoomUtil.crossedLine(RoomUtil.BLOAT_REGION, new Point(4, 31), new Point(4, 32), true, client)) {
            if (NyloHandler.instanceStart == -1) {
                NyloHandler.instanceStart = client.getTickCount();
            }
        }
    }

    public void updateAnimationChanged(AnimationChanged event) {
        if (event.getActor().getAnimation() == BLOAT_DOWN_ANIMATION) {
            down();
        } else if (event.getActor().getAnimation() == -1 && event.getActor().getCombatLevel() > 400) {
            walk();
        }
        if (event.getActor().getAnimation() == BLOAT_DEATH_ANIMATION) {
            endBloat();
        }
        if (event.getActor().getAnimation() == 8056) //Player scythed
        {
            if (event.getActor() instanceof Player) {
                Player p = (Player) event.getActor();
                clog.write(25, p.getName(), (client.getTickCount() - bloatStartTick) + "");
            }
        }
    }

    public void updateNpcSpawned(NpcSpawned event) {
        boolean story = false;
        switch (event.getNpc().getId()) {
            case BLOAT_SM:
                story = true;
                clog.write(IS_STORY_MODE);
            case BLOAT_HM:
                if (!story)
                    clog.write(IS_HARD_MODE);
            case BLOAT:
                clog.write(BLOAT_SPAWNED);
                if (client.getVarbitValue(ROOM_ACTIVE_VARBIT) != 0) {
                    accurateEntry = false;
                } else {
                    clog.write(ACCURATE_BLOAT_START);
                }
                break;
        }
    }

    public void updateNpcDespawned(NpcDespawned event) {
        int id = event.getNpc().getId();
        if (id == BLOAT || id == BLOAT_HM || id == BLOAT_SM) {
            clog.write(BLOAT_DESPAWN, "" + (client.getTickCount() - bloatStartTick));
        }
    }
}
