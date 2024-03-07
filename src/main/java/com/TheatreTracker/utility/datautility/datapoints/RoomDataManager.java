package com.TheatreTracker.utility.datautility.datapoints;

import com.TheatreTracker.utility.wrappers.PlayerDidAttack;
import com.google.common.collect.Multimap;
import lombok.Getter;

public abstract class RoomDataManager {
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

    private Multimap<String, PlayerDidAttack> attacks;

    public RoomDataManager(int defence) {
        this.defence = defence;
    }

    /**
     * Reduces defence by 30%.
     */
    public void hammer() {
        this.defence = (int) Math.floor(this.defence * 0.7);
    }

    /**
     * Reduces defence by a specific amount.
     *
     * @param damage Damage dealt by bgs/bone dagger.
     */
    public void bgs(int damage) {
        this.defence -= damage;
    }

    /**
     * Parses generic data.
     */
    public void parse() {

    }
}
