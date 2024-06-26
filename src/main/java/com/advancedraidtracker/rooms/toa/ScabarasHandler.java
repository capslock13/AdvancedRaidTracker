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
import net.runelite.api.Player;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;

import java.util.*;

import static com.advancedraidtracker.constants.ToaIDs.SCABARAS_GATE_OBJECT;

@Slf4j
public class ScabarasHandler extends TOARoomHandler
{
    private List<Point> points = new ArrayList<>();

    public String getName()
    {
        return "Scabaras";
    }

    int startTick = -1;
    int expectedEndTick = -1;
    private Point origin = null;
    private Point end = null;

	//todo revisit/finish optimal puzzle time evaluation

    //bottom left end: +10x +1y <--- solo,  +10x -1y <-- team
    //top right end: +5x -7y, +8x -1y team
    //bottom right end +5x +5y <-- solo, +8x -1y team
    //top left end +10x -3y <--- solo +10x -1y team

    //tl origin: 18,40
    //tr origin 35, 40
    //br origin 35, 27
    //bl origin 18, 28

    private static final Map<String, Point> soloEndPointMap = new HashMap<>()
	{{
		put("18 40", new Point(28, 36));
		put("35 40", new Point(40, 35));
		put("18 28", new Point(28, 28));
		put("35 27", new Point(40, 32));
	}};

    public ScabarasHandler(Client client, DataWriter clog, AdvancedRaidTrackerConfig config, AdvancedRaidTrackerPlugin plugin, TOAHandler handler)
    {
        super(client, clog, config, plugin, handler);
    }

    @Override
    public void reset()
    {
        active = false;
        points.clear();
        origin = null;
        startTick = -1;
        expectedEndTick = -1;
        solved = false;
        super.reset();
    }

    private void resetPuzzle()
    {
        points.clear();
        origin = null;
    }

    @Override
    public void updateChatMessage(ChatMessage message)
    {
        if (active && message.getSender() == null && message.getMessage().startsWith("Challenge complete: Path of Scabaras."))
        {
            endScabaras();
        }
    }

    List<Point> translatePoints()
    {
        List<Point> translated = new ArrayList<>();
        if (origin != null)
        {
            for (Point p : points)
            {
                translated.add(new Point(p.getX() - origin.getX(), p.getY() - origin.getY()));
            }
        }
        return translated;
    }

    private int getTicksToMove(Point start, Point end)
    {
        return (Math.max(Math.abs(start.getX() - end.getX()), Math.abs(start.getY() - end.getY())) / 2) + 1;
    }

    private void solvePuzzle()
    {
        if (origin != null)
        {
            int totalTicks = getTicksToMove(origin, points.get(0));
            for (int i = 0; i < 4; i++)
            {
                int move = getTicksToMove(points.get(i), points.get(i + 1));
                totalTicks += move;
            }
            int move = getTicksToMove(points.get(4), end);
            totalTicks += move;
            expectedEndTick = startTick + totalTicks;
        }
    }

    private boolean solved = false;

    @Override
    public void updateGameTick(GameTick event)
    {
        if (points.size() == 5)
        {
            solvePuzzle();
            points.clear();
        }
        if (!solved && expectedEndTick >= client.getTickCount())
        {
            for (Player player : client.getPlayers())
            {
                if (player.getWorldLocation().getRegionX() == end.getX() && player.getWorldLocation().getRegionY() == end.getY())
                {
                    //log.info(client.getTickCount()-expectedEndTick + " ticks lost"); //todo future use
                }
            }
        }
        if (!active && roomStartTick == -1)
        {
            if (RoomUtil.playerPastLine(14162, 12, true, client, false, true))
            {
                roomStartTick = client.getTickCount();
                active = true;
                clog.addLine(LogID.TOA_SCABARAS_START, roomStartTick);
			}
        }
        super.updateGameTick(event);
    }

    public boolean isActive()
    {
        return active;
    }

    @Override
    public void updateGraphicsObjectCreated(GraphicsObjectCreated event)
    {
        if (event.getGraphicsObject().getId() == 302)
        {
            LocalPoint lp = event.getGraphicsObject().getLocation();
            Tile gTile = client.getScene().getTiles()[client.getPlane()][lp.getSceneX()][lp.getSceneY()];
            if (gTile.getGroundObject() != null && gTile.getGroundObject().getId() == 45340)
            {
                //reset puzzle todo future use
            }
        }
    }

    @Override
    public void updateAnimationChanged(AnimationChanged animationChanged)
    {
        if (animationChanged.getActor().getAnimation() == 832 && startTick == -1)
        {
            for (Player player : client.getPlayers())
            {
                if (player.getWorldLocation().getRegionX() == origin.getX() && player.getWorldLocation().getRegionY() == origin.getY())
                {
                    startTick = client.getTickCount();
                    return;
                }
            }
        }
    }

    @Override
    public void updateGameObjectSpawned(GameObjectSpawned objectSpawned)
    {
        WorldPoint wp = objectSpawned.getGameObject().getWorldLocation();
        if (objectSpawned.getGameObject().getId() == 45338)
        {
            origin = new Point(wp.getRegionX(), wp.getRegionY() - 1);
            end = soloEndPointMap.get(wp.getRegionX() + " " + wp.getRegionY());
			//todo future use
        }
        if (objectSpawned.getGameObject().getId() == 45341)
        {
            points.add(new Point(wp.getRegionX(), wp.getRegionY()));
			//todo future use
        }
    }

    public void endScabaras()
    {
        active = false;
        int scabarasDuration = client.getTickCount() - roomStartTick;
        sendTimeMessage("Scabaras Puzzle time: ", scabarasDuration);
        clog.addLine(LogID.TOA_SCABARAS_FINISHED, scabarasDuration);
        plugin.liveFrame.setRoomFinished(getName(), scabarasDuration);
		plugin.lastSplits += "Scabaras: " + RoomUtil.time(plugin.currentDurationSum) + "(+" + RoomUtil.time(scabarasDuration) + ")\n";
		plugin.currentDurationSum += scabarasDuration;
    }
}
