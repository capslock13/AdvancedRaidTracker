package com.advancedraidtracker.utility.wrappers;

import com.advancedraidtracker.utility.PlayerWornItems;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.weapons.AnimationDecider;
import com.advancedraidtracker.utility.weapons.PlayerAnimation;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.SpriteID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.util.AsyncBufferedImage;

import java.awt.image.BufferedImage;
import java.util.*;

@Slf4j
public class PlayerDidAttack
{
    public String player;
    public String animation;
    public int tick;
    public int weapon;
    public String projectile;
    public String spotAnims;
    public int targetedIndex;
    public int targetedID;
    public String targetName;
    public String wornItems;
    public String[] wornItemNames = {};

    public ItemManager itemManager;

    private PlayerAnimation playerAnimation = PlayerAnimation.UNDECIDED;
	public int damage = 0;

    public PlayerDidAttack(ItemManager itemManager, String player, String animation, int tick, int weapon, String projectile, String spotAnims, int targetedIndex, int targetedID, String targetName, String worn)
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

    public PlayerAnimation getPlayerAnimation()
    {
        if(playerAnimation.equals(PlayerAnimation.UNDECIDED))
        {
            this.playerAnimation = AnimationDecider.getWeapon(animation, spotAnims, projectile, weapon);
        }
        return playerAnimation;
    }

    public void setWornNames()
    {
        wornItemNames = new PlayerWornItems(wornItems, itemManager).getAll();
    }

}
