package com.TheatreTracker.rooms;

import com.TheatreTracker.TheatreTrackerConfig;
import com.TheatreTracker.TheatreTrackerPlugin;
import com.TheatreTracker.constants.LogID;
import com.TheatreTracker.constants.TOBRoom;
import com.TheatreTracker.utility.Point;
import com.TheatreTracker.utility.datautility.DataWriter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import com.TheatreTracker.utility.RoomState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import static com.TheatreTracker.constants.TobIDs.*;

@Slf4j
public class SotetsegHandler extends RoomHandler
{
    public RoomState.SotetsegRoomState roomState = RoomState.SotetsegRoomState.NOT_STARTED;
    private int soteEntryTick = -1;
    private int soteFirstMazeStart = -1;
    private int soteSecondMazeStart = -1;
    private int soteFirstMazeEnd = -1;
    private int soteSecondMazeEnd = -1;
    private int soteDeathTick = -1;
    private int deferTick = -1;
    private int lastRegion = -1;
    private final TheatreTrackerPlugin plugin;
    private boolean hasSteppedOnMaze = false;
    private String firstMazeChosen = "";
    private String secondMazeChosen = "";
    private String lastChosen = "";
    private Map<String, ArrayList<Point>> playerTiles = new HashMap<>();

    private ArrayList<Point> excludedTiles = new ArrayList<>();

    public SotetsegHandler(Client client, DataWriter clog, TheatreTrackerConfig config, TheatreTrackerPlugin plugin)
    {
        super(client, clog, config);
        this.plugin = plugin;
    }

    public boolean isActive()
    {
        return !(roomState == RoomState.SotetsegRoomState.NOT_STARTED || roomState == RoomState.SotetsegRoomState.FINISHED);
    }

    public String getName()
    {
        return "Sotetseg";
    }

    public void reset()
    {
        super.reset();
        playerTiles.clear();
        lastChosen = "";
        excludedTiles.clear();
        accurateTimer = true;
        soteEntryTick = -1;
        roomState = RoomState.SotetsegRoomState.NOT_STARTED;
        soteFirstMazeStart = -1;
        soteSecondMazeStart = -1;
        soteFirstMazeEnd = -1;
        soteSecondMazeEnd = -1;
        soteDeathTick = -1;
        lastRegion = -1;
        hasSteppedOnMaze = false;
        firstMazeChosen = "";
        secondMazeChosen = "";
    }

    public void updateNpcSpawned(NpcSpawned event)
    {
        int id = event.getNpc().getId();
        if (id == SOTETSEG_ACTIVE || id == SOTETSEG_ACTIVE_HM || id == SOTETSEG_ACTIVE_SM)
        {
            if (lastRegion == SOTETSEG_UNDERWORLD)
            {
                if (roomState == RoomState.SotetsegRoomState.MAZE_1)
                {
                    endFirstMaze();
                } else if (roomState == RoomState.SotetsegRoomState.MAZE_2)
                {
                    endSecondMaze();
                }
            }
        }
    }

    private ArrayList<Point> currentMaze = new ArrayList<>();
    private int lastTileX = -1;
    private int lastTileY = -1;

    public void updateGroundObjectSpawned(GroundObjectSpawned event)
    {
        if(!hasSteppedOnMaze)
        {
            if (event.getGroundObject().getId() == SOTETSEG_RED_TILE && lastRegion == SOTETSEG_OVERWORLD)
            {
                hasSteppedOnMaze = true;
                int ticksSinceLastProc = -1;
                if(roomState == RoomState.SotetsegRoomState.MAZE_1)
                {
                    ticksSinceLastProc = client.getTickCount()-soteFirstMazeStart;
                }
                else
                {
                    if(roomState == RoomState.SotetsegRoomState.MAZE_2)
                    {
                        ticksSinceLastProc = client.getTickCount()-soteSecondMazeStart;
                    }
                }
                if(ticksSinceLastProc != -1 && lastRegion == SOTETSEG_OVERWORLD)
                {
                    int distance = Math.abs(event.getGroundObject().getWorldLocation().getRegionX()-15);
                    int stallDuration = 5;
                    int tickOffset = 1 + stallDuration + (int)((distance-0.5)/2.0);
                    if(ticksSinceLastProc > tickOffset)
                    {
                        String mazeRunner = (roomState== RoomState.SotetsegRoomState.MAZE_1) ? firstMazeChosen : secondMazeChosen;
                        plugin.sendChatMessage(mazeRunner + " was late to first tile by " + (ticksSinceLastProc-tickOffset) + " ticks");
                    }
                    plugin.sendChatMessage("First tile was " + distance + " away, and showed up after " + ticksSinceLastProc + " ticks. Expected: " + tickOffset + ". X Position: " + event.getGroundObject().getWorldLocation().getRegionX());
                }
                //log.info("Red tile appeared " + ticksSinceLastProc + " ticks after proc, on (" + event.getGroundObject().getWorldLocation().getRegionX() + "," + event.getGroundObject().getWorldLocation().getRegionY() +")");
            }
        }
        if (event.getGroundObject().getId() == SOTETSEG_RED_TILE && lastRegion == SOTETSEG_OVERWORLD)
        {
            int mazeProc = (roomState == RoomState.SotetsegRoomState.MAZE_1) ? soteFirstMazeStart : soteSecondMazeStart;
            int currentTileX = event.getGroundObject().getWorldLocation().getRegionX();
            int currentTileY = event.getGroundObject().getWorldLocation().getRegionY();
            currentMaze.add(new Point(currentTileX, currentTileY));
            //log.info("Tick: " + (client.getTickCount() - (6 - (mazeProc))) + ", Tile: " + event.getGroundObject().getWorldLocation().getRegionX() + ", " + event.getGroundObject().getWorldLocation().getRegionY());
        }
    }


    private static ArrayList<Point> removeDuplicatePoints(ArrayList<Point> tiles)
    {
        ArrayList<Point> newTiles = new ArrayList<>();
        Point old = null;
        for(Point p : tiles)
        {
            if(old != null)
            {
                if(!(p.getX() == old.getX() && p.getY() == old.getY()))
                {
                    newTiles.add(p);
                }
            }
            else
            {
                newTiles.add(p);
            }
            old = p;
        }
        return newTiles;
    }

    private ArrayList<Point> addEveryTileBetween(ArrayList<Point> tiles)
    {
        ArrayList<Point> allTiles = new ArrayList<>();
        Point previous = null;
        for(Point p : tiles)
        {
            if(previous != null)
            {
                allTiles.addAll(everyTileBetween(previous, p));
            }
            previous = p;
        }
        return removeDuplicatePoints(allTiles);
    }
    public static ArrayList<Point> everyTileBetween(Point start, Point end)
    {
        if(end.getY() < start.getY())
        {
            return new ArrayList<>();
        }
        ArrayList<Point> crossTiles = new ArrayList<>();
        crossTiles.add(start);
        //log.info("Trying every tile between " + start.getX() + ", " + start.getY() + " and " + end.getX() + ", " + end.getY());
        while (Math.abs(end.getX() - crossTiles.get(crossTiles.size() - 1).getX()) != end.getY() - crossTiles.get(crossTiles.size() - 1).getY())
        {
            if(crossTiles.size() > 1000)
            {
                //log.info("failed on " + start.getX() + ", " + start.getY() + " to " + end.getX() + ", " + end.getY());
                return new ArrayList<>();
            }
            //log.info("Current: " + crossTiles.get((crossTiles.size()-1)).getX() + ", " + crossTiles.get(crossTiles.size()-1).getY());
            if (Math.abs(end.getX() - start.getX()) > end.getY() - start.getY())
            {
                int offset = (end.getX() - start.getX() > 0) ? 1 : -1;
                crossTiles.add(new Point(crossTiles.get(crossTiles.size() - 1).getX() + offset, crossTiles.get(crossTiles.size() - 1).getY()));
            } else if (end.getY() - start.getY() > Math.abs(end.getX() - start.getX()))
            {
                crossTiles.add(new Point(crossTiles.get(crossTiles.size() - 1).getX(), crossTiles.get(crossTiles.size() - 1).getY() + 1));
            }
        }
        while (end.getX() != crossTiles.get(crossTiles.size() - 1).getX() && end.getY() != crossTiles.get(crossTiles.size() - 1).getY())
        {
            int offset = (end.getX() - crossTiles.get(crossTiles.size()-1).getX() > 0) ? 1 : -1;
            crossTiles.add(new Point(crossTiles.get(crossTiles.size() - 1).getX() + offset, crossTiles.get(crossTiles.size() - 1).getY() + 1));
        }
        return crossTiles;
    }

    private static ArrayList<Point> restoreMaze(ArrayList<Point> tiles)
    {
        ArrayList<Point> unfilteredTiles = new ArrayList<Point>();
        if (!tiles.isEmpty())
        {
            unfilteredTiles.add(tiles.get(0));
            for (Point p : tiles)
            {
                if (p.getY() != 22)
                {
                    Point last = unfilteredTiles.get(unfilteredTiles.size() - 1);
                    for (int i = 0; i < Math.abs(p.getX() - last.getX()) + 1; i++)
                    {
                        if (p.getX() - last.getX() < 0)
                        {
                            unfilteredTiles.add(new Point(last.getX() - i, last.getY() + 1));
                        } else
                        {
                            unfilteredTiles.add(new Point(last.getX() + i, last.getY() + 1));
                        }
                    }
                    unfilteredTiles.add(p);
                }
            }
        }
        return unfilteredTiles;
    }

    public static void printMaze(ArrayList<Point> tiles)
    {
        if(tiles != null)
        {
            if (!tiles.isEmpty())
            {
                String output = "";
                for (Point p : tiles)
                {
                    output += "(" + p.getX() + "," + p.getY() + "),";
                }
                log.info(output.substring(0, output.length() - 1));
            } else
            {
                log.info("empty maze");
            }
        }
    }

    public void updateAnimationChanged(AnimationChanged event)
    {
        if (event.getActor().getAnimation() == SOTETSEG_DEATH_ANIMATION)
        {
            endSotetseg();
        }
    }

    public void startSotetseg()
    {
        soteEntryTick = client.getTickCount();
        roomStartTick = client.getTickCount();
        deferTick = soteEntryTick + 2;
        roomState = RoomState.SotetsegRoomState.PHASE_1;
        clog.write(LogID.SOTETSEG_STARTED);
    }

    public void endSotetseg()
    {
        plugin.addDelayedLine(TOBRoom.SOTETSEG, client.getTickCount() - soteEntryTick, "Dead");
        soteDeathTick = client.getTickCount() + SOTETSEG_DEATH_ANIMATION_LENGTH;
        roomState = RoomState.SotetsegRoomState.FINISHED;
        clog.write(LogID.ACCURATE_SOTE_END);
        clog.write(LogID.SOTETSEG_ENDED, String.valueOf(soteDeathTick - soteEntryTick));
        plugin.liveFrame.setSoteFinished(soteDeathTick - soteEntryTick);
        sendTimeMessage("Wave 'Sotetseg phase 3' complete. Duration: ", soteDeathTick - soteEntryTick, soteDeathTick - soteSecondMazeEnd, false);
    }

    public void startFirstMaze()
    {
        startEitherMaze();
        lastChosen = "";
        excludedTiles.clear();
        currentMaze.clear();
        firstMazeChosen = "";
        hasSteppedOnMaze = false;
        soteFirstMazeStart = client.getTickCount();
        clog.write(LogID.SOTETSEG_FIRST_MAZE_STARTED, String.valueOf(soteFirstMazeStart - soteEntryTick));
        roomState = RoomState.SotetsegRoomState.MAZE_1;
        sendTimeMessage("Wave 'Sotetseg phase 1' complete. Duration: ", soteFirstMazeStart - soteEntryTick);
        plugin.addDelayedLine(TOBRoom.SOTETSEG, soteFirstMazeStart - soteEntryTick, "Maze1 Start");
    }

    public void startEitherMaze()
    {
        playerTiles.clear();
        for(String s : plugin.currentPlayers)
        {
            playerTiles.put(s, new ArrayList<>());
        }
    }

    public void endFirstMaze()
    {
        endEitherMaze();
        soteFirstMazeEnd = client.getTickCount();
        clog.write(LogID.SOTETSEG_FIRST_MAZE_ENDED, String.valueOf(soteFirstMazeEnd - soteEntryTick));
        roomState = RoomState.SotetsegRoomState.PHASE_2;
        sendTimeMessage("Wave 'Sotetseg maze 1' complete. Duration: ", soteFirstMazeEnd - soteEntryTick, soteFirstMazeEnd - soteFirstMazeStart);
        plugin.addDelayedLine(TOBRoom.SOTETSEG, soteFirstMazeEnd - soteEntryTick, "Maze1 End");
    }

    public void startSecondMaze()
    {
        startEitherMaze();
        lastChosen = "";
        excludedTiles.clear();
        currentMaze.clear();
        secondMazeChosen = "";
        hasSteppedOnMaze = false;
        soteSecondMazeStart = client.getTickCount();
        clog.write(LogID.SOTETSEG_SECOND_MAZE_STARTED, String.valueOf(soteSecondMazeStart - soteEntryTick));
        roomState = RoomState.SotetsegRoomState.MAZE_2;
        sendTimeMessage("Wave 'Sotetseg phase 2' complete. Duration: ", soteSecondMazeStart - soteEntryTick, soteSecondMazeStart - soteFirstMazeEnd);
        plugin.addDelayedLine(TOBRoom.SOTETSEG, soteSecondMazeStart - soteEntryTick, "Maze2 Start");
    }
    private static ArrayList<Point> filterMaze(ArrayList<Point> tiles)
    {
        ArrayList<Point> filteredTiles = new ArrayList<Point>();
        for (Point p : tiles)
        {
            if (p.getY() % 2 == 0)
            {
                filteredTiles.add(p);
            }
        }
        return filteredTiles;
    }

    public ArrayList<Point> findOverlappingTiles(ArrayList<Point> tiles1, ArrayList<Point> tiles2)
    {
        ArrayList<Point> overlap = new ArrayList<>();
        for(Point p1 : tiles1)
        {
            for(Point p2 : tiles2)
            {
                if(p1.getX() == p2.getX() && p1.getY() == p2.getY())
                {
                    overlap.add(p1);
                }
            }
        }
        return overlap;
    }

    public ArrayList<Point> findNonOverlappingTiles(ArrayList<Point> tiles1, ArrayList<Point> tiles2)
    {
        ArrayList<Point> nonOverlappedTiles = new ArrayList<>();
        for(Point p1 : tiles1)
        {
            boolean found = false;
            for(Point p2 : tiles2)
            {
                if(p2.getX() == p1.getX() && p2.getY() == p1.getY())
                {
                    found = true;
                }
            }
            if(!found)
            {
                nonOverlappedTiles.add(p1);
            }
        }
        return nonOverlappedTiles;
    }

    public void endEitherMaze()
    {
        //log.info("Recorded Maze: ");
        //printMaze(currentMaze);
        if(!currentMaze.isEmpty())
        {
            if(currentMaze.get(currentMaze.size()-1).getY() == 35)
            {
                currentMaze.add(new Point(currentMaze.get(currentMaze.size()-1).getX(), 37));
            }
        }
        currentMaze = removeDuplicatePoints(currentMaze);
        //log.info("Filtered Maze: ");
        //printMaze(currentMaze);
        currentMaze = addEveryTileBetween(currentMaze);
        //log.info("restored maze: ");
        //printMaze(currentMaze);
        ArrayList<Point> overlap = removeDuplicatePoints(findOverlappingTiles(currentMaze, excludedTiles));
        ArrayList<Point> nonoverlap = removeDuplicatePoints(findNonOverlappingTiles(excludedTiles, currentMaze));
        //log.info("Overlap: ");
        //printMaze(overlap);
        //log.info("Nonoverlap: ");
        //printMaze(nonoverlap);
        if(!overlap.isEmpty())
        {
            plugin.sendChatMessage(lastChosen + " ragged " + overlap.size() + " while running the maze");
        }
        if(!nonoverlap.isEmpty())
        {
            plugin.sendChatMessage("Players following the maze ragged " + nonoverlap.size() + " tiles");
        }
        //printMaze(overlap);
        //log.info("Tiles ragged by non runner: ");
        //printMaze(nonoverlap);
        //log.info("shortened maze: ");
        currentMaze = filterMaze(currentMaze);
        //printMaze(currentMaze);
        excludedTiles = removeDuplicatePoints(excludedTiles);
        //log.info("excluded tiles: ");
        //printMaze(excludedTiles);
        for(String s : playerTiles.keySet())
        {
            //log.info("Player " + s + " array size " + playerTiles.get(s).size());
            for(Point p : playerTiles.get(s))
            {
                //log.info("Point: " + p.getX() + ", " + p.getY());
            }
        }
        playerTiles.replaceAll((s, v) -> addEveryTileBetween(removeDuplicatePoints(v)));
        for(String s : playerTiles.keySet())
        {
            int ragged = 0;
            for(Point tile : playerTiles.get(s))
            {
                for(Point raggedTile : nonoverlap)
                {
                    if(tile.getX() == raggedTile.getX() && tile.getY() == raggedTile.getY())
                    {
                        ragged++;
                        //log.info(s + " ragged tile: " + raggedTile.getX() + ", " + raggedTile.getY());
                    }
                }
            }
            if(ragged > 0)
            {
                //plugin.sendChatMessage(s + " ragged " + ragged + " tiles");
            }
        }
    }

    public void endSecondMaze()
    {
        endEitherMaze();
        soteSecondMazeEnd = client.getTickCount();
        clog.write(LogID.SOTETSEG_SECOND_MAZE_ENDED, String.valueOf(soteSecondMazeEnd - soteEntryTick));
        roomState = RoomState.SotetsegRoomState.PHASE_3;
        sendTimeMessage("Wave 'Sotetseg maze 2' complete. Duration: ", soteSecondMazeEnd - soteEntryTick, soteSecondMazeEnd - soteSecondMazeStart);
        plugin.addDelayedLine(TOBRoom.SOTETSEG, soteSecondMazeEnd - soteEntryTick, "Maze2 End");
    }

    public String getAboveWorldChosen()
    {
        for(String playerName : plugin.currentPlayers)
        {
            boolean found = false;
            for(Player player : client.getPlayers())
            {
                if(playerName.equals(player.getName()))
                {
                    found = true;
                }
            }
            if(!found)
            {
                return playerName;
            }
        }
        return "";
    }

    public void updateGraphicsObjectCreated(GraphicsObjectCreated event)
    {
        if(event.getGraphicsObject().getId() == 505)
        {
            WorldPoint wp = WorldPoint.fromLocal(client, event.getGraphicsObject().getLocation());
            excludedTiles.add(new Point(wp.getRegionX(), wp.getRegionY()));
        }
    }

    public void updateGameTick(GameTick event)
    {
        lastRegion = client.isInInstancedRegion() ? WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID() : client.getLocalPlayer().getWorldLocation().getRegionID();
        if(roomState == RoomState.SotetsegRoomState.MAZE_1 || roomState == RoomState.SotetsegRoomState.MAZE_2)
        {
            int ticksSinceLastProc = -1;
            if(roomState == RoomState.SotetsegRoomState.MAZE_1)
            {
                ticksSinceLastProc = client.getTickCount()-soteFirstMazeStart;
            }
            else
            {
                ticksSinceLastProc = client.getTickCount() - soteSecondMazeStart;
            }
            if(ticksSinceLastProc > 5)
            {
                for (Player player : client.getPlayers())
                {
                    WorldPoint location = player.getWorldLocation();
                    if (location.getRegionX() <= 22 && location.getRegionX() >= 9)
                    {
                        if (location.getRegionY() >= 21 && location.getRegionY() <= 37)
                        {
                            if (playerTiles.containsKey(player.getName()))
                            {
                                playerTiles.get(player.getName()).add(new Point(location.getRegionX(), location.getRegionY()));
                            } else
                            {
                                //log.info("How?" + player.getName());
                            }
                        }
                    }
                }
            }
        }
        if (client.getTickCount() == deferTick)
        {
            deferTick = -1;
            if (client.getVarbitValue(HP_VARBIT) == FULL_HP)
            {
                clog.write(LogID.ACCURATE_SOTE_START);
            }
        }
        if(client.getTickCount() == soteFirstMazeStart+5)
        {
            if(lastRegion == SOTETSEG_UNDERWORLD)
            {
                firstMazeChosen = client.getLocalPlayer().getName();
            }
            else
            {
                firstMazeChosen = getAboveWorldChosen();
            }
            lastChosen = firstMazeChosen;
            log.info("Chosen 1st maze: " + firstMazeChosen);
        }
        else if(client.getTickCount() == soteSecondMazeStart+5)
        {
            if(lastRegion == SOTETSEG_UNDERWORLD)
            {
                secondMazeChosen = client.getLocalPlayer().getName();
            }
            else
            {
                secondMazeChosen = getAboveWorldChosen();
            }
            log.info("Chosen 2nd maze: " + secondMazeChosen);
            lastChosen = secondMazeChosen;
        }
    }

    public void handleNPCChanged(int id)
    {
        if (id == SOTETSEG_ACTIVE || id == SOTETSEG_ACTIVE_HM || id == SOTETSEG_ACTIVE_SM)
        {
            if (roomState == RoomState.SotetsegRoomState.NOT_STARTED)
            {
                if (id == SOTETSEG_ACTIVE_HM)
                {
                    clog.write(LogID.IS_HARD_MODE);
                } else if (id == SOTETSEG_ACTIVE_SM)
                {
                    clog.write(LogID.IS_STORY_MODE);
                }
                startSotetseg();
            } else if (roomState == RoomState.SotetsegRoomState.MAZE_1)
            {
                endFirstMaze();
            } else if (roomState == RoomState.SotetsegRoomState.MAZE_2)
            {
                endSecondMaze();
            }
        } else if (id == SOTETSEG_INACTIVE || id == SOTETSEG_INACTIVE_HM || id == SOTETSEG_INACTIVE_SM)
        {
            if (roomState == RoomState.SotetsegRoomState.PHASE_1)
            {
                startFirstMaze();
            } else if (roomState == RoomState.SotetsegRoomState.PHASE_2)
            {
                startSecondMaze();
            }
        }
    }
}
