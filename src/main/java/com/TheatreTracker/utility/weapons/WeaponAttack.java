package com.TheatreTracker.utility.weapons;

import java.awt.*;

public enum WeaponAttack
{
    UNDECIDED("Undecided", "?", new Color(0, 0, 0), -1),
    BLOWPIPE("Blowpipe", "BP", new Color(100, 150, 200), 2),
    BLOWPIPE_SPEC("Blowpipe spec", "bp", new Color(100, 150, 200), 2),
    HAMMER("DWH Spec", "H", new Color(100, 100, 100), 6),
    HAMMER_BOP("DWH Bop", "h", new Color(50, 50, 50), 6),
    SANG("Sang/Trident", "T", new Color(30, 120, 130), 4),
    CHALLY_SPEC("Chally spec", "CH", new Color(150, 50, 50), 7),
    CHALLY_WHACK("Chally whack", "ch", new Color(150, 50, 50), 7),
    SWIFT_BLADE("Swift Blade", "SB", new Color(225, 50, 50), 3),
    BGS_WHACK("BGS whack", "bg", new Color(170, 20, 20), 6),
    BGS_SPEC("BGS spec", "BG", new Color(170, 20, 20, 0), 6),
    TBOW("Tbow", "TB", new Color(30, 120, 30), 5),
    ZCB_AUTO("ZCB Auto", "zc", new Color(10, 170, 50), 5),
    ZCB_SPEC("ZCB Spec", "ZC", new Color(10, 170, 50), 5),
    SCYTHE("Scythe", "S", new Color(230, 100, 100), 5),
    DINHS_SPEC("Dinhs Spec", "BW", new Color(20, 20, 20), 5),
    DINHS_WHACK("Dinhs Whack", "bw", new Color(20, 20, 20), 5),
    CHIN("Chinchompa", "CC", new Color(0, 130, 0), 3),
    WHIP("Tent Whip", "TW", new Color(10, 70, 80), 4),
    FREEZE("Freeze", "F", new Color(50, 50, 170), 5),
    DAWN_SPEC("Dawnbringer Spec", "DB", new Color(10, 100, 150), 4),
    DAWN_AUTO("Dawnbringer Auto", "db", new Color(10, 100, 150), 4),
    CLAW_SCRATCH("Claw Scratch", "c", new Color(76, 89, 1), 4),
    CLAW_SPEC("Claw Spec", "C", new Color(76, 89, 1), 4),
    SHADOW("Shadow", "Sh", new Color(20, 20, 60), 5),
    DART("Dart", "Da", new Color(10, 60, 60), 2),
    SBS("Spellbook Swap", "SS", new Color(10, 100, 60), -1),
    BOUNCE("Bounce", "VB", new Color(200, 10, 10), -1),
    AID_OTHER("Aid other", "AO", new Color(100, 100, 100), -1),
    VENG_SELF("Veng Self", "VS", new Color(160, 89, 13), -1),
    HUMIDIFY("Humidify", "HU", new Color(20, 20, 200), -1),
    MAGIC_IMBUE("Magic Imbue", "MI", new Color(60, 60, 150), -1),
    WEB_WEAVER("Web Weaver", "WW", new Color(240, 18, 119), 3),
    DEATH("Death", "X", new Color(0, 0, 0), -1),
    TELEPORT("Teleport", "TP", new Color(60, 70, 80), -1),
    HEAL_GROUP("Heal Group", "HG", new Color(50, 170, 100), -1),
    CONSUME("Food/Drink Consumed", "CS", new Color(110, 50, 60), -1),
    THRALL_CAST("Thrall Cast", "TC", new Color(20, 65, 187), -1),
    BLADE_OF_SAELDOR("Blade of Saeldor", "BS", new Color(67, 10, 45), 4),
    FANG("Osmumten's Fang", "OF", new Color(10, 10, 100), 5),
    TSOTD("Toxic Staff of the Dead", "TS", new Color(70, 10, 10), 4),


    ;

    public final String name;
    public final String shorthand;
    public final Color color;
    public final int attackTicks;


    WeaponAttack(String name, String shorthand, Color color, int attackTicks)
    {
        this.attackTicks = attackTicks;
        this.name = name;
        this.shorthand = shorthand;
        this.color = color;
    }
}
