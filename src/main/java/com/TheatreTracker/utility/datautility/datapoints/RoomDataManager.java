package com.TheatreTracker.utility.datautility.datapoints;

import com.TheatreTracker.utility.wrappers.PlayerDidAttack;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class RoomDataManager
{
    /**
     * Time of room in ticks
     */
    @Getter
    protected int time;

    /**
     * Total thrall damage througout the room
     */
    @Getter
    protected int thrallDamage;

    /**
     * If the time of the room is accurate, the player viewed the entire room.
     */
    @Getter
    protected boolean isAccurate;

    /**
     * Amount of deaths in the room.
     */
    @Getter
    protected boolean deaths;

    /**
     * Defence of the target.
     */
    @Getter
    protected int defence;

    @Getter
    protected final List<PlayerDidAttack> attacks;

    protected List<LogEntry> roomData;

    public RoomDataManager(int defence, List<LogEntry> roomData)
    {
        this.defence = defence;
        this.roomData = roomData;
        this.attacks = new ArrayList<>();
    }

    /**
     * Reduces defence by 30%.
     */
    public void hammer()
    {
        this.defence = (int) Math.floor(this.defence * 0.7);
    }

    /**
     * Reduces defence by a specific amount.
     *
     * @param damage Damage dealt by bgs/bone dagger.
     */
    public void bgs(int damage)
    {
        this.defence -= damage;
    }

    /**
     * Parses generic data.
     */
    public void parse()
    {
        String affectedPlayer = "";

        for (LogEntry entry : roomData)
        {
            switch (entry.getLogEntry())
            {
                case PLAYER_ATTACK:
                    PlayerDidAttack atk = new PlayerDidAttack(entry);
                    this.attacks.add(atk);
                    break;
                case DWH:
                    hammer();
                    break;
                case BGS:
                    break;
            }

        }
    }

}
