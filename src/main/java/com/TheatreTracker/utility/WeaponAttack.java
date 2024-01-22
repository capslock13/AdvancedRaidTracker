package com.TheatreTracker.utility;

import java.awt.*;

public enum WeaponAttack
{
    UNDECIDED("Undecided", "?", new Color(0, 0, 0)),
    BLOWPIPE("Blowpipe", "BP", new Color(100, 150, 200)),
    BLOWPIPE_SPEC("Blowpipe spec", "bp", new Color(100, 150, 200)),
    HAMMER("DWH Spec", "H", new Color(100, 100, 100)),
    HAMMER_BOP("DWH Bop", "h", new Color(50, 50, 50)),
    SANG("Sang/Trident", "T", new Color(30, 120, 130)),
    CHALLY_SPEC("Chally spec", "CH", new Color(150, 50, 50)),
    CHALLY_WHACK("Chally whack", "ch", new Color(150, 50, 50)),
    SWIFT_BLADE("Swift Blade", "SB", new Color(225, 50, 50)),
    BGS_WHACK("BGS whack", "bg", new Color(170, 20, 20)),
    BGS_SPEC("BGS spec", "BG", new Color(170, 20, 20)),
    TBOW("Tbow", "TB", new Color(30, 120, 30)),
    ZCB_AUTO("ZCB Auto", "zc", new Color(10, 170, 50)),
    ZCB_SPEC("ZCB Spec", "ZC", new Color(10, 170, 50)),
    SCYTHE("Scythe", "S", new Color(230, 100, 100)),
    DINHS_SPEC("Dinhs Spec", "BW", new Color(20, 20, 20)),
    DINHS_WHACK("Dinhs Whack", "bw", new Color(20, 20, 20)),
    CHIN("Chinchompa", "CC", new Color(0, 130, 0)),
    WHIP("Tent Whip", "TW", new Color(10, 70, 80)),
    FREEZE("Freeze", "F", new Color(50, 50, 170)),
    DAWN_SPEC("Dawnbringer Spec", "DB", new Color(10, 100, 150)),
    DAWN_AUTO("Dawnbringer Auto", "db", new Color(10, 100, 150)),
    CLAW_SCRATCH("Claw Scratch", "c", new Color(76, 89, 1)),
    CLAW_SPEC("Claw Spec", "C", new Color(76,89,1)),
    SHADOW("Shadow", "Sh", new Color(20, 20, 60)),
    DART("Dart", "Da", new Color(10, 60, 60));

    ;

    public String name;
    public String shorthand;
    public Color color;


    WeaponAttack(String name, String shorthand, Color color)
    {
        this.name = name;
        this.shorthand = shorthand;
        this.color = color;
    }
}
