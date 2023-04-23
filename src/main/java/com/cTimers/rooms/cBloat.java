package com.cTimers.rooms;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.EventBus;
import com.cTimers.Point;
import com.cTimers.utility.RoomUtil;
import com.cTimers.utility.cLogger;
import com.cTimers.utility.cRoomState;

import java.util.ArrayList;

@Slf4j
public class cBloat extends cRoom
{
    public cRoomState.BloatRoomState roomState;
    final private int BLOAT = 8359;
    private ArrayList<Integer> walks;
    private ArrayList<Integer> downs;
    private int bloatStartTick = -1;

    @Inject
    private EventBus eventBus;

    private int bloatDeathTick;

    public cBloat(Client client, cLogger clog)
    {
        super(client, clog);
        walks = new ArrayList<Integer>();
        downs = new ArrayList<Integer>();
        bloatDeathTick = -1;
        bloatStartTick = -1;
    }

    private String timeColor = "<col=EF1020>";
    private String defaultColor = "<col=000000>";

    public void reset()
    {
        log.info("Resetting bloat");
        accurateEntry = true;
        bloatStartTick = -1;
        bloatDeathTick = -1;
        walks.clear();
        downs.clear();
    }

    public void endBloat()
    {
        roomState = cRoomState.BloatRoomState.FINISHED;
        bloatDeathTick = client.getTickCount()+3;
        clog.write(302);
        if(bloatStartTick != -1)
            sendTimeMessage("Wave 'Bloat last down' complete! Duration: ", splitLastDown(), " Room time: ", bloatDeathTick-bloatStartTick, true);
    }


    public int splitLastDown()
    {
        if(bloatDeathTick != -1)
        {
            if(walks.size() == downs.size()+1)
            {
                return bloatDeathTick-walks.get(walks.size()-1);
            }
            else if(walks.size() == downs.size())
            {
                return bloatDeathTick-downs.get(walks.size()-1);
            }
            else
            {
                return -1;
            }
        }
        else
        {
            return -1;
        }
    }

    public void start()
    {
        bloatStartTick = client.getTickCount();
        roomState = cRoomState.BloatRoomState.WALKING;
    }

    private int getLastWalk()
    {
        if(downs.size() != 0 && walks.size() != 0 && downs.size() == walks.size())
        {
            return downs.get(downs.size()-1) - walks.get(walks.size()-1);
        }
        else
        {
            return -1;
        }
    }

    private int getLastDownTime()
    {
        if(downs.size() != 0)
        {
            return downs.get(downs.size()-1)-bloatStartTick;
        }
        else
        {
            return -1;
        }
    }

    public void down()
    {
        clog.write(21, ""+(client.getTickCount()-bloatStartTick));
        if(downs.size() == 0)
        {
            int currentBloatHP = client.getVarbitValue(6448);
            clog.write(24, ""+currentBloatHP);
        }
        downs.add(client.getTickCount());
        roomState = cRoomState.BloatRoomState.DOWN;
        if(bloatStartTick != -1)
            sendTimeMessage("Wave 'Bloat walk' complete! Duration: ", getLastWalk(), getLastDownTime(), true);
    }

    public void walk()
    {
        walks.add(client.getTickCount());
        roomState = cRoomState.BloatRoomState.WALKING;
    }
    public void updateGameTick(GameTick event)
    {
        if(bloatStartTick == -1 && RoomUtil.crossedLine(RoomUtil.BLOAT_REGION, new Point(39, 30), new Point(39, 33), true, client))
        {
            start();
            walk();
        }

        if(RoomUtil.crossedLine(RoomUtil.BLOAT_REGION, new Point(4, 31), new Point(4, 32), true, client))
        {
            if(cNylo.instanceStart == -1)
            {
                cNylo.instanceStart = client.getTickCount();
            }
        }
    }

    public void updateAnimationChanged(AnimationChanged event)
    {
        if(event.getActor().getAnimation() == 8082)
        {
            down();
        }
        else if(event.getActor().getAnimation() == -1 && event.getActor().getCombatLevel() > 400)
        {
            walk();
        }
        if(event.getActor().getAnimation() == 8085)
        {
            endBloat();
        }
        if(event.getActor().getAnimation() == 8056)
        {
            if(event.getActor() instanceof Player)
            {
                Player p = (Player) event.getActor();
                clog.write(25, p.getName(), (client.getTickCount()-bloatStartTick)+"");
            }
        }
    }

    public void updateNpcSpawned(NpcSpawned event)
    {
        if (event.getNpc().getId() == BLOAT)
        {
            clog.write(20);
            if(client.getVarbitValue(6447) != 0)
            {
                accurateEntry = false;
            //    bloatStartTick = client.getTickCount();
            }
            else
            {
                clog.write(202);
            }
        }
    }

    public void updateNpcDespawned(NpcDespawned event)
    {
        if(event.getNpc().getId() == BLOAT)
        {
            clog.write(23, ""+(client.getTickCount()-bloatStartTick));
        }
    }
}
