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
    public BufferedImage img = null;
    private boolean setUnkitted = false;

    private PlayerAnimation playerAnimation = PlayerAnimation.UNDECIDED;

    private static final Set<Integer> scythe = new HashSet<>(Arrays.asList(ItemID.HOLY_SCYTHE_OF_VITUR, ItemID.SANGUINE_SCYTHE_OF_VITUR));
    private static final Set<Integer> bp = new HashSet<>(Collections.singletonList(ItemID.BLAZING_BLOWPIPE));
    private static final Set<Integer> sang = new HashSet<>(Collections.singletonList(ItemID.HOLY_SANGUINESTI_STAFF));
    private static final Set<Integer> bulwark = new HashSet<>(Collections.singletonList(ItemID.DINHS_BLAZING_BULWARK));
    private static final Set<Integer> claws = new HashSet<>(Collections.singletonList(ItemID.CORRUPTED_DRAGON_CLAWS));
    private static final Set<Integer> dwh = new HashSet<>(Arrays.asList(ItemID.DRAGON_WARHAMMER_CR, ItemID.DRAGON_WARHAMMER_OR));

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

    public void useUnkitted()
    {
        setUnkitted = true;
    }

    public static int getReplacement(int original)
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

    private static final Map<Integer, Integer> spellIconMap = Map.ofEntries(
            Map.entry(-1, SpriteID.UNKNOWN_INFORMATION_I),
            Map.entry(6299, SpriteID.SPELL_SPELLBOOK_SWAP),
            Map.entry(1000000, SpriteID.EMOTE_WAVE),
            Map.entry(4411, SpriteID.SPELL_VENGEANCE_OTHER),
            Map.entry(8316, SpriteID.SPELL_VENGEANCE),
            Map.entry(6294, SpriteID.SPELL_HUMIDIFY),
            Map.entry(722, SpriteID.SPELL_MAGIC_IMBUE),
            Map.entry(836, SpriteID.PLAYER_KILLER_SKULL),
            Map.entry(10629, SpriteID.PLAYER_KILLER_SKULL),
            Map.entry(8070, SpriteID.SPELL_LUMBRIDGE_HOME_TELEPORT),
            Map.entry(1816, SpriteID.SPELL_LUMBRIDGE_HOME_TELEPORT),
            Map.entry(8970, SpriteID.SPELL_DEATH_CHARGE),
            Map.entry(4409, SpriteID.SPELL_HEAL_GROUP),
            Map.entry(8973, 2979), //resurrect greater ghost; not in the API
            Map.entry(827, SpriteID.TAB_INVENTORY),
            Map.entry(832, SpriteID.MAP_ICON_WATER_SOURCE),
            Map.entry(7855, SpriteID.SPELL_FIRE_SURGE));

    public static int getSpellIcon(int animation)
    {
        return spellIconMap.getOrDefault(animation, 0);
    }

    public void setIcons(ItemManager itemManager, SpriteManager spriteManager)
    {
        if (AnimationDecider.getWeapon(animation, spotAnims, projectile, weapon).attackTicks > 0)
        {
            int weaponID = this.weapon;
            if (setUnkitted)
            {
                weaponID = getReplacement(weaponID);
            }
            img = itemManager.getImage(weaponID, 1, false);
        } else
        {
            try
            {
                img = spriteManager.getSprite(getSpellIcon(Integer.parseInt(animation)), 0);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
