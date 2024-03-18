package com.advancedraidtracker.utility.weapons;

import java.awt.*;

//TODO rename refactor recycle
public enum PlayerAnimation //todo add weapon animations and IDs as int array and kill WeaponDecider into a cleaner method
{
    UNDECIDED("Undecided", "?", new Color(0, 0, 0), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    BLOWPIPE("Blowpipe", "BP", new Color(100, 150, 200), 2, new int[]{5061, 10656}, new int[]{}, new int[]{}, new int[]{}), //todo itemid
    BLOWPIPE_SPEC("Blowpipe spec", "bp", new Color(100, 150, 200), 2, new int[]{}, new int[]{}, new int[]{}, new int[]{1043, 2599}),
    HAMMER("DWH Spec", "H", new Color(100, 100, 100), 6, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    HAMMER_BOP("DWH Bop", "h", new Color(50, 50, 50), 6, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    SANG("Sang/Trident", "T", new Color(30, 120, 130), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    CHALLY_SPEC("Chally spec", "CH", new Color(150, 50, 50), 7, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    CHALLY_WHACK("Chally whack", "ch", new Color(150, 50, 50), 7, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    SWIFT_BLADE("Swift Blade", "SB", new Color(225, 50, 50), 3, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    BGS_WHACK("BGS whack", "bg", new Color(170, 20, 20), 6, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    BGS_SPEC("BGS spec", "BG", new Color(170, 20, 20, 0), 6, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    TBOW("Tbow", "TB", new Color(30, 120, 30), 5, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    ZCB_AUTO("ZCB Auto", "zc", new Color(10, 170, 50), 5, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    ZCB_SPEC("ZCB Spec", "ZC", new Color(10, 170, 50), 5, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    SCYTHE("Scythe", "S", new Color(230, 100, 100), 5, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    DINHS_SPEC("Dinhs Spec", "BW", new Color(20, 20, 20), 5, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    DINHS_WHACK("Dinhs Whack", "bw", new Color(20, 20, 20), 5, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    CHIN("Chinchompa", "CC", new Color(0, 130, 0), 3, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    WHIP("Tent Whip", "TW", new Color(10, 70, 80), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    FREEZE("Freeze", "F", new Color(50, 50, 170), 5, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    DAWN_SPEC("Dawnbringer Spec", "DB", new Color(10, 100, 150), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    DAWN_AUTO("Dawnbringer Auto", "db", new Color(10, 100, 150), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    CLAW_SCRATCH("Claw Scratch", "c", new Color(76, 89, 1), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    CLAW_SPEC("Claw Spec", "C", new Color(76, 89, 1), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    SHADOW("Shadow", "Sh", new Color(20, 20, 60), 5, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    DART("Dart", "Da", new Color(10, 60, 60), 2, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    SBS("Spellbook Swap", "SS", new Color(10, 100, 60), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    BOUNCE("Bounce", "VB", new Color(200, 10, 10), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    AID_OTHER("Aid other", "AO", new Color(100, 100, 100), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    VENG_SELF("Veng Self", "VS", new Color(160, 89, 13), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    HUMIDIFY("Humidify", "HU", new Color(20, 20, 200), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    MAGIC_IMBUE("Magic Imbue", "MI", new Color(60, 60, 150), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    WEB_WEAVER("Web Weaver", "WW", new Color(240, 18, 119), 3, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    DEATH("Death", "X", new Color(0, 0, 0), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    TELEPORT("Teleport", "TP", new Color(60, 70, 80), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    HEAL_GROUP("Heal Group", "HG", new Color(50, 170, 100), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    CONSUME("Food/Drink Consumed", "CS", new Color(110, 50, 60), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    THRALL_CAST("Thrall Cast", "TC", new Color(20, 65, 187), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    BLADE_OF_SAELDOR("Blade of Saeldor", "BS", new Color(67, 10, 45), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    FANG("Osmumten's Fang", "FNG", new Color(10, 10, 100), 5, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    TSOTD("Toxic Staff of the Dead", "TS", new Color(70, 10, 10), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    VOID_WAKER("Voidwaker", "vw", new Color(60, 30, 10), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    VOID_WAKER_SPEC("Voidwaker Spec", "VW", new Color(160, 70, 70), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    MINING("Mining", "M", new Color(30, 30, 30), 3, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    DDS_SPEC("Dragon Dagger Spec", "DDS", new Color(140, 10, 10), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    DDS_POKE("Dragon Dagger poke", "dds", new Color(10, 10, 10), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    KERIS_BREACHING("Keris of the Breaching", "KB", new Color(140, 140, 30), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    KERIS_CORRUPTION("Keris of the Corruption", "kc", new Color(10, 140, 30), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    KERIS_SUN("Keris of the Sun", "ks", new Color(10, 140, 30), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),

    KERIS_CORRUPTION_SPEC("Keris of the Corruption spec", "KC", new Color(140, 140, 30), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    KERIS_SUN_SPEC("Keris of the Sun spec", "KS", new Color(140, 140, 30), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    PICKUP("Pickup Animation", "PCK", new Color(30, 30, 90), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    PUSH_JUG("Push Animation", "PSH", new Color(10, 10, 10), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    INQ_MACE("Inquisitor's Mace", "IQM", new Color(180, 50, 30), 4, new int[]{}, new int[]{}, new int[]{}, new int[]{}),

    ;

    public final String name;
    public final String shorthand;
    public final Color color;
    public final int attackTicks;
    private final int[] animations;
    private final int[] graphics;
    private final int[] weaponIDs;
    private final int[] projectiles;


    PlayerAnimation(String name, String shorthand, Color color, int attackTicks, int[] animations, int[] graphics, int[] weaponIDs, int[] projectiles)
    {
        this.attackTicks = attackTicks;
        this.name = name;
        this.shorthand = shorthand;
        this.color = color;
        this.animations = animations;
        this.graphics = graphics;
        this.weaponIDs = weaponIDs;
        this.projectiles = projectiles;
    }
}
