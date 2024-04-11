package com.advancedraidtracker.utility.wrappers;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.ItemReference;
import com.advancedraidtracker.utility.weapons.AnimationDecider;
import com.advancedraidtracker.utility.weapons.PlayerAnimation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.SpriteID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import static com.advancedraidtracker.constants.RaidRoom.BLOAT;
@Slf4j
public class OutlineBox
{
    public String player;
    public int tick;
    public String letter;
    public Color color;
    public boolean primaryTarget;
    public PlayerAnimation playerAnimation;
    public String additionalText;
    public int cd;
    private final RaidRoom room;
    public int weapon;

    @Setter
    String[] wornItems = new String[0];

    public OutlineBox(String letter, Color color, boolean primaryTarget, String additionalText, PlayerAnimation playerAnimation, int cd, int tick, String player, RaidRoom room, int weapon)
    {
        this.room = room;
        this.playerAnimation = playerAnimation;
        this.player = player;
        this.tick = tick;
        this.letter = letter;
        this.color = color;
        this.primaryTarget = primaryTarget;
        this.additionalText = additionalText;
        this.cd = cd;
        this.weapon = weapon;
    }

    public final int NONE = 0;
    public final int MELEE = 1;
    public final int RANGE = 2;
    public final int MAGE = 3;
    public int style = NONE;
    public Color outlineColor = new Color(0, 0, 0, 0);

    private boolean anyMatch(String item, String[] items)
    {
        for (String s : items)
        {
            if (item.toLowerCase().contains(s))
            {
                return true;
            }
        }
        return false;
    }

    private void setStyle(String weapon)
    {
        if (anyMatch(weapon, ItemReference.ITEMS[MELEE]))
        {
            style = MELEE;
        } else if (anyMatch(weapon, ItemReference.ITEMS[RANGE]))
        {
            style = RANGE;
        } else if (anyMatch(weapon, ItemReference.ITEMS[MAGE]))
        {
            style = MAGE;
        }
    }

    public void createOutline()
    {
        if(wornItems.length == 0)
        {
            return;
        }
        if (cd < 1 || letter.equals("VS") || letter.equals("AO") || letter.equals("HU") || letter.equals("MI") || letter.equals("DB") || letter.equals("SS"))
        {
            return;
        }
        int correctItems = 0;
        boolean voidHelmWorn = false;
        if (wornItems.length == 9)
        {
            setStyle(wornItems[3]);
            if (wornItems[0].toLowerCase().contains("void"))
            {
                voidHelmWorn = true;
            }
            for (String s : wornItems)
            {
                if (anyMatch(s, ItemReference.ITEMS[style]) || (voidHelmWorn && s.toLowerCase().contains("void")))
                {
                    if (s.contains("salve"))
                    {
                        if (room.equals(BLOAT))
                        {
                            correctItems++;
                        }
                    } else
                    {
                        correctItems++;
                    }
                }
            }
            if (wornItems[2].toLowerCase().contains("blood fury") && style == MELEE)
            {
                correctItems++;
            }
        }
        switch (style)
        {
            case MELEE:
                if (correctItems < 8)
                {
                    int opacity = (int) ((255) * ((8 - correctItems) / 8.0));
                    opacity = Math.max(opacity, 150);
                    outlineColor = new Color(255, 255, 0, opacity);
                }
                break;
            case RANGE:
                if (correctItems < 6)
                {
                    outlineColor = new Color(255, 255, 0, (int) ((255) * ((6 - correctItems) / 6.0)));
                }
                break;
            case MAGE:
                if (correctItems < 5)
                {
                    outlineColor = new Color(255, 255, 0, (int) ((255) * ((5 - correctItems) / 5.0)));
                }
                break;
        }
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

    private static final Set<Integer> scythe = new HashSet<>(Arrays.asList(ItemID.HOLY_SCYTHE_OF_VITUR, ItemID.SANGUINE_SCYTHE_OF_VITUR));
    private static final Set<Integer> bp = new HashSet<>(Collections.singletonList(ItemID.BLAZING_BLOWPIPE));
    private static final Set<Integer> sang = new HashSet<>(Collections.singletonList(ItemID.HOLY_SANGUINESTI_STAFF));
    private static final Set<Integer> bulwark = new HashSet<>(Collections.singletonList(ItemID.DINHS_BLAZING_BULWARK));
    private static final Set<Integer> claws = new HashSet<>(Collections.singletonList(ItemID.CORRUPTED_DRAGON_CLAWS));
    private static final Set<Integer> dwh = new HashSet<>(Arrays.asList(ItemID.DRAGON_WARHAMMER_CR, ItemID.DRAGON_WARHAMMER_OR));

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

    public static boolean useUnkitted;

    public static final Map<String, BufferedImage> iconMap = new HashMap<>();
    public static ClientThread clientThread;
    public static ItemManager itemManager;
    public static SpriteManager spriteManager;

    public static BufferedImage getIcon(PlayerAnimation animation, int weapon)
    {
        synchronized (iconMap)
        {
            if (animation.attackTicks > 0)
            {
                int weaponID = useUnkitted ? getReplacement(weapon) : weapon;
                if (iconMap.containsKey("Weapon:" + weaponID))
                {
                    return iconMap.get("Weapon:" + weaponID);
                } else
                {
                    if (clientThread != null)
                    {
                        clientThread.invoke(() ->
                        {
                            if (itemManager != null)
                            {
                                iconMap.put("Weapon:" + weaponID, itemManager.getImage(weaponID));
                            }
                        });
                    }
                    return null;
                }
            } else
            {
                try
                {
                    if (iconMap.containsKey("Sprite:" + animation.animations[0]))
                    {
                        return iconMap.get("Sprite:" + animation.animations[0]);
                    } else
                    {
                        if (clientThread != null)
                        {
                            clientThread.invoke(() ->
                            {
                                if (spriteManager != null)
                                {
                                    iconMap.put("Sprite:"+animation.animations[0], spriteManager.getSprite(getSpellIcon(animation.animations[0]), 0));
                                }
                            });
                        }
                    }
                } catch (Exception ignored)
                {
                }
            }
            return null;
        }
    }

}
