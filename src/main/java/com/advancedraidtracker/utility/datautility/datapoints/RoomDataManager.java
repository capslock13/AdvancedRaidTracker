package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.utility.wrappers.PlayerDidAttack;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected boolean startAccurate;

    @Getter
    protected boolean endAccurate;

    /**
     * Amount of deaths in the room.
     */
    @Getter
    protected final Map<String, Integer> deaths;

    /**
     * Defence of the target.
     */
    @Getter
    protected int defence;

    /**
     * If the players were in a party to get exact defence reduction.
     */
    @Getter
    protected boolean defenceAccurate;

    @Getter
    protected final List<PlayerDidAttack> attacks;

    @Getter
    protected final Map<String, LogID> specs;

    @Getter
    protected final Multimap<String, Integer> thrallAttacks;

    // Maybe this should be assigned null after it's been parsed so that it can be freed
    // Could also investigate an approach where the "generic" data is removed after the generic
    // parser has gone through them, depending on log size this may be significant?
    // Would this be parallelize-able?
    protected List<LogEntry> roomData;

    public RoomDataManager(int defence, List<LogEntry> roomData)
    {
        this.defence = defence;
        this.roomData = roomData;
        this.attacks = new ArrayList<>();
        this.thrallAttacks = ArrayListMultimap.create();
        this.specs = new HashMap<>();
        this.deaths = new HashMap<>();
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

    public void bgs(LogEntry entry) {
        Map<String, String> data = entry.parseExtra();
        int damage = Integer.parseInt(data.get("damage"));
        bgs(damage);
    }

    /**
     * Parses generic data.
     */
    public void parse()
    {
        for (LogEntry entry : roomData)
        {
            LogID logID = entry.getLogEntry();
            Map<String, String> extras = entry.parseExtra();
            switch (logID)
            {
                case PLAYER_ATTACK:
                    PlayerDidAttack atk = new PlayerDidAttack(entry);
                    this.attacks.add(atk);
                    break;

                case DWH:
                    hammer(); // fallthrough
                case HAMMER_ATTEMPTED:
                    specs.put(extras.get("player"), logID);
                    break;

                case BGS:
                    bgs(entry);
                    specs.put(extras.get("player"), logID);
                    break;
                case PLAYER_DIED:
                    deaths.merge(extras.get("player"),1, Integer::sum);
                    break;

                case THRALL_ATTACKED:
                    thrallAttacks.put(extras.get("player"), 0);
                    break;
                case THRALL_DAMAGED:
                    thrallAttacks.put(extras.get("player"), Integer.valueOf(extras.get("damage")));
                    break;
            }

        }
    }

    /**
     * Determines if the time for a room is accurate
     * @return true if both the start and end are true
     */
    public boolean isAccurate() {
        return startAccurate && endAccurate;
    }

    public abstract String getName();
}
