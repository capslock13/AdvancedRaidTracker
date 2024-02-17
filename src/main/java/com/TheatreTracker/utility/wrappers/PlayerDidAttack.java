package com.TheatreTracker.utility.wrappers;

import com.TheatreTracker.utility.PlayerWornItems;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
@Slf4j
public class PlayerDidAttack
{
    public String player;
    public String animation;
    public int tick;
    public String weapon;
    public String projectile;
    public String spotAnims;
    public int targetedIndex;
    public int targetedID;
    public String targetName;
    public String wornItems;
    public String[] wornItemNames = {};

    public ItemManager itemManager;

    public PlayerDidAttack(ItemManager itemManager, String player, String animation, int tick, String weapon, String projectile, String spotAnims, int targetedIndex, int targetedID, String targetName, String worn)
    {
        this.itemManager = itemManager;
        this.player = player;
        this.animation = animation;
        this.tick = tick;
        this.weapon = weapon;
        this.projectile = projectile;
        this.spotAnims = spotAnims;
        this.targetedIndex = targetedIndex;
        this.targetedID = targetedID;
        this.targetName = targetName;
        this.wornItems = worn;
    }

    public void setWornNames()
    {
        wornItemNames = new PlayerWornItems(wornItems, itemManager).getAll();
    }
}
