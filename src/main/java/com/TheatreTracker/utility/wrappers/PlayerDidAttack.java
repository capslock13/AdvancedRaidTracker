package com.TheatreTracker.utility.wrappers;

import com.TheatreTracker.utility.PlayerWornItems;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.AsyncBufferedImage;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
    public AsyncBufferedImage img = null;
    private boolean setUnkitted = false;
    public AsyncBufferedImage alternateIcon = null;

    private static Set<Integer> scythe = new HashSet<>(Arrays.asList(ItemID.HOLY_SCYTHE_OF_VITUR, ItemID.SANGUINE_SCYTHE_OF_VITUR));
    private static Set<Integer> bp = new HashSet<>(Collections.singletonList(ItemID.BLAZING_BLOWPIPE));
    private static Set<Integer> sang = new HashSet<>(Collections.singletonList(ItemID.HOLY_SANGUINESTI_STAFF));
    private static Set<Integer> bulwark = new HashSet<>(Collections.singletonList(ItemID.DINHS_BLAZING_BULWARK));
    private static Set<Integer> claws = new HashSet<>(Collections.singletonList(ItemID.CORRUPTED_DRAGON_CLAWS));
    private static Set<Integer> dwh = new HashSet<>(Arrays.asList(ItemID.DRAGON_WARHAMMER_CR, ItemID.DRAGON_WARHAMMER_OR));

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

    public void setWornNames()
    {
        wornItemNames = new PlayerWornItems(wornItems, itemManager).getAll();
    }

    public void useUnkitted()
    {
        setUnkitted = true;
    }

    private int getReplacement(int original)
    {
        if(scythe.contains(original))
        {
            return ItemID.SCYTHE_OF_VITUR;
        }
        if(sang.contains(original))
        {
            return ItemID.SANGUINESTI_STAFF;
        }
        if(bp.contains(original))
        {
            return ItemID.TOXIC_BLOWPIPE;
        }
        if(bulwark.contains(original))
        {
            return ItemID.DINHS_BULWARK;
        }
        if(claws.contains(original))
        {
            return ItemID.DRAGON_CLAWS;
        }
        if(dwh.contains(original))
        {
            return ItemID.DRAGON_WARHAMMER;
        }
        return original;
    }

    public void setIcons()
    {
        int weaponID = this.weapon;
        if(setUnkitted)
        {
            weaponID = getReplacement(weaponID);
        }
        img = itemManager.getImage(weaponID, 1, false);
    }
}
