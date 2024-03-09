package com.advancedraidtracker.utility.wrappers;

import com.advancedraidtracker.utility.PlayerWornItems;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.AsyncBufferedImage;

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
    public AsyncBufferedImage img = null;
    private boolean setUnkitted = false;

    private static final Set<Integer> scythe = new HashSet<>(Arrays.asList(ItemID.HOLY_SCYTHE_OF_VITUR, ItemID.SANGUINE_SCYTHE_OF_VITUR));
    private static final Set<Integer> bp = new HashSet<>(Collections.singletonList(ItemID.BLAZING_BLOWPIPE));
    private static final Set<Integer> sang = new HashSet<>(Collections.singletonList(ItemID.HOLY_SANGUINESTI_STAFF));
    private static final Set<Integer> bulwark = new HashSet<>(Collections.singletonList(ItemID.DINHS_BLAZING_BULWARK));
    private static final Set<Integer> claws = new HashSet<>(Collections.singletonList(ItemID.CORRUPTED_DRAGON_CLAWS));
    private static final Set<Integer> dwh = new HashSet<>(Arrays.asList(ItemID.DRAGON_WARHAMMER_CR, ItemID.DRAGON_WARHAMMER_OR));

    public PlayerDidAttack(LogEntry entry)
    {
        // TODO: make all of the "strings" into integers
        List<String> extra = entry.getExtra();

        String[] playerDetails = extra.get(0).split(":");

        this.player = playerDetails[0];
        this.tick = Integer.parseInt(playerDetails[1]);

        String[] animationDetails = extra.get(1).split(":");
        this.animation = animationDetails[0];
        if (animationDetails.length > 1)
        {
            this.wornItems = animationDetails[1];
        }

        this.spotAnims = extra.get(2);
        String[] weaponDetails = extra.get(3).split(":");

        switch (weaponDetails.length)
        {
            case 3:
                this.targetedID = Integer.parseInt(weaponDetails[2]); // fallthrough
            case 2:
                this.targetedIndex = Integer.parseInt(weaponDetails[1]); // fallthrough
            case 1:
                this.weapon = Integer.parseInt(weaponDetails[0]);
                break;
        }

        String[] projectileDetails = extra.get(4).split(":");
        if (projectileDetails.length == 2)
        {
            this.targetName = projectileDetails[1];
        }
        this.projectile = projectileDetails[0];
    }

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
        if (scythe.contains(original))
        {
            return ItemID.SCYTHE_OF_VITUR;
        }
        if (sang.contains(original))
        {
            return ItemID.SANGUINESTI_STAFF;
        }
        if (bp.contains(original))
        {
            return ItemID.TOXIC_BLOWPIPE;
        }
        if (bulwark.contains(original))
        {
            return ItemID.DINHS_BULWARK;
        }
        if (claws.contains(original))
        {
            return ItemID.DRAGON_CLAWS;
        }
        if (dwh.contains(original))
        {
            return ItemID.DRAGON_WARHAMMER;
        }
        return original;
    }

    public void setIcons()
    {
        setIcons(itemManager);
    }

    public void setIcons(ItemManager itemManager)
    {
        int weaponID = this.weapon;
        if (setUnkitted)
        {
            weaponID = getReplacement(weaponID);
        }
        img = itemManager.getImage(weaponID, 1, false);
    }
}
