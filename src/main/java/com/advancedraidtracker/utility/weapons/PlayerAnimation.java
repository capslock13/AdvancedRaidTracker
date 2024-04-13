package com.advancedraidtracker.utility.weapons;

import net.runelite.api.ItemID;

import java.awt.*;

public enum PlayerAnimation
{
    NOT_SET("Clear", "", new Color(0, 0, 0), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    UNDECIDED("Undecided", "?", new Color(0, 0, 0), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    BLOWPIPE("Blowpipe", "BP", new Color(100, 150, 200), 2, new int[]{5061, 10656}, new int[]{}, new int[]{ItemID.TOXIC_BLOWPIPE, ItemID.BLAZING_BLOWPIPE}, new int[]{1122}),
    BLOWPIPE_SPEC("Blowpipe spec", "bp", new Color(100, 150, 200), 2, new int[]{5061, 10656}, new int[]{}, new int[]{ItemID.TOXIC_BLOWPIPE, ItemID.BLAZING_BLOWPIPE}, new int[]{1043, 2599}),
    HAMMER("DWH Spec", "H", new Color(100, 100, 100), 6, new int[]{1378}, new int[]{}, new int[]{ItemID.DRAGON_WARHAMMER, ItemID.DRAGON_WARHAMMER_OR, ItemID.DRAGON_WARHAMMER_CR}, new int[]{}),
    HAMMER_BOP("DWH Bop", "h", new Color(50, 50, 50), 6, new int[]{401}, new int[]{}, new int[]{ItemID.DRAGON_WARHAMMER, ItemID.DRAGON_WARHAMMER_OR, ItemID.DRAGON_WARHAMMER_CR}, new int[]{}),
    SANG("Sang/Trident", "T", new Color(30, 120, 130), 4, new int[]{1167}, new int[]{}, new int[]{ItemID.SANGUINESTI_STAFF, ItemID.HOLY_SANGUINESTI_STAFF, ItemID.TRIDENT_OF_THE_SEAS, ItemID.TRIDENT_OF_THE_SEAS_E, ItemID.TRIDENT_OF_THE_SEAS_FULL, ItemID.TRIDENT_OF_THE_SWAMP, ItemID.TRIDENT_OF_THE_SWAMP_E}, new int[]{1539, 1040, 1252}),
    CHALLY_SPEC("Chally spec", "CH", new Color(150, 50, 50), 7, new int[]{1203}, new int[]{}, new int[]{ItemID.CRYSTAL_HALBERD}, new int[]{}),
    CHALLY_WHACK("Chally whack", "ch", new Color(150, 50, 50), 7, new int[]{440, 428}, new int[]{}, new int[]{ItemID.CRYSTAL_HALBERD}, new int[]{}),
    SWIFT_BLADE("Swift Blade", "SB", new Color(225, 50, 50), 3, new int[]{2323, 390, 8288, 401}, new int[]{}, new int[]{ItemID.SWIFT_BLADE, ItemID.GOBLIN_PAINT_CANNON, ItemID.HAM_JOINT}, new int[]{}),
    BGS_WHACK("Godsword whack", "gs", new Color(170, 20, 20), 6, new int[]{7045, 7044, 7054}, new int[]{}, new int[]{ItemID.BANDOS_GODSWORD, ItemID.BANDOS_GODSWORD_OR, ItemID.ZAMORAK_GODSWORD, ItemID.ZAMORAK_GODSWORD_OR, ItemID.ARMADYL_GODSWORD, ItemID.ARMADYL_GODSWORD_OR, ItemID.SARADOMIN_GODSWORD, ItemID.SARADOMIN_GODSWORD_OR, ItemID.ANCIENT_GODSWORD}, new int[]{}),
    SGS_SPEC("SGS spec", "SG", new Color(170, 20, 20), 6, new int[]{7640, 7641}, new int[]{}, new int[]{ItemID.SARADOMIN_GODSWORD, ItemID.SARADOMIN_GODSWORD_OR}, new int[]{}),
    ZGS_SPEC("ZGS spec", "ZG", new Color(170, 20, 20), 6, new int[]{7638, 7639}, new int[]{}, new int[]{ItemID.ZAMORAK_GODSWORD, ItemID.ZAMORAK_GODSWORD_OR}, new int[]{}),
    AGS_SPEC("AGS spec", "AG", new Color(170, 20, 20), 6, new int[]{7644, 7645}, new int[]{}, new int[]{ItemID.ARMADYL_GODSWORD, ItemID.ARMADYL_GODSWORD_OR}, new int[]{}),
    ACGS_SPEC("Ancient Godsword spec", "ACG", new Color(170, 20, 20), 6, new int[]{9171}, new int[]{}, new int[]{ItemID.ANCIENT_GODSWORD}, new int[]{}),
    BGS_SPEC("BGS spec", "BG", new Color(170, 20, 20), 6, new int[]{7642, 7643}, new int[]{}, new int[]{ItemID.BANDOS_GODSWORD, ItemID.BANDOS_GODSWORD_OR}, new int[]{}),
    TBOW("Tbow", "TB", new Color(30, 120, 30), 5, new int[]{426}, new int[]{}, new int[]{ItemID.TWISTED_BOW, ItemID.CORRUPTED_TWISTED_BOW}, new int[]{}),
    ZCB_AUTO("ZCB Auto", "zc", new Color(10, 170, 50), 5, new int[]{9168}, new int[]{}, new int[]{ItemID.ZARYTE_CROSSBOW}, new int[]{1468}),
    ZCB_SPEC("ZCB Spec", "ZC", new Color(10, 170, 50), 5, new int[]{9168}, new int[]{}, new int[]{ItemID.ZARYTE_CROSSBOW}, new int[]{1995}),
    SCYTHE("Scythe", "S", new Color(230, 100, 100), 5, new int[]{8056}, new int[]{}, new int[]{ItemID.SCYTHE_OF_VITUR, ItemID.CORRUPTED_SCYTHE_OF_VITUR, ItemID.SANGUINE_SCYTHE_OF_VITUR, ItemID.HOLY_SCYTHE_OF_VITUR}, new int[]{}),
    UNCHARGED_SCYTHE("Uncharged Scythe", "S?", new Color(230, 100, 100), 5, new int[]{8056}, new int[]{}, new int[]{ItemID.SCYTHE_OF_VITUR_UNCHARGED, ItemID.CORRUPTED_SCYTHE_OF_VITUR_UNCHARGED, ItemID.SANGUINE_SCYTHE_OF_VITUR_UNCHARGED, ItemID.HOLY_SCYTHE_OF_VITUR_UNCHARGED}, new int[]{}),

    DINHS_SPEC("Dinhs Spec", "BW", new Color(20, 20, 20), 5, new int[]{7511}, new int[]{}, new int[]{ItemID.DINHS_BULWARK, ItemID.DINHS_BLAZING_BULWARK}, new int[]{}),
    BLACK_CHIN("Black Chinchompa", "CC", new Color(0, 130, 0), 3, new int[]{7618}, new int[]{}, new int[]{ItemID.BLACK_CHINCHOMPA, -1}, new int[]{1272}),
    RED_CHIN("Red Chinchompa", "RCC", new Color(0, 130, 0), 3, new int[]{7618}, new int[]{}, new int[]{ItemID.RED_CHINCHOMPA, -1}, new int[]{909}),
    GRAY_CHIN("Gray Chinchompa", "GCC", new Color(0, 130, 0), 3, new int[]{7618}, new int[]{}, new int[]{ItemID.CHINCHOMPA, -1}, new int[]{908}),
    TENT_WHIP("Tent Whip", "TW", new Color(10, 70, 80), 4, new int[]{1658}, new int[]{}, new int[]{ItemID.ABYSSAL_TENTACLE}, new int[]{}),
    ABYSSAL_WHIP("Abyssal Whip", "AW", new Color(10, 70, 80), 4, new int[]{1658}, new int[]{}, new int[]{ItemID.ABYSSAL_WHIP, ItemID.FROZEN_ABYSSAL_WHIP, ItemID.VOLCANIC_ABYSSAL_WHIP}, new int[]{}),
    BARRAGE("Barrage", "F", new Color(50, 50, 170), 5, new int[]{1979}, new int[]{}, new int[]{ItemID.KODAI_WAND}, new int[]{}),
    BLITZ("Blitz", "F?", new Color(50, 50, 170), 5, new int[]{1978}, new int[]{}, new int[]{ItemID.KODAI_WAND}, new int[]{}),
    DAWN_SPEC("Dawnbringer Spec", "DB", new Color(10, 100, 150), 4, new int[]{1167}, new int[]{}, new int[]{ItemID.DAWNBRINGER}, new int[]{1547}),
    DAWN_AUTO("Dawnbringer Auto", "db", new Color(10, 100, 150), 4, new int[]{1167}, new int[]{}, new int[]{ItemID.DAWNBRINGER}, new int[]{1544}),
    CLAW_SCRATCH("Claw Scratch", "c", new Color(76, 89, 1), 4, new int[]{393, 1067}, new int[]{}, new int[]{ItemID.DRAGON_CLAWS, ItemID.DRAGON_CLAWS_OR, ItemID.DRAGON_CLAWS_CR}, new int[]{}),
    CLAW_SPEC("Claw Spec", "C", new Color(76, 89, 1), 4, new int[]{7514}, new int[]{}, new int[]{ItemID.DRAGON_CLAWS, ItemID.DRAGON_CLAWS_OR, ItemID.DRAGON_CLAWS_CR}, new int[]{}),
    SHADOW("Shadow", "Sh", new Color(20, 20, 60), 5, new int[]{9493}, new int[]{}, new int[]{ItemID.TUMEKENS_SHADOW}, new int[]{2126}),
    DART("Dart", "Da", new Color(10, 60, 60), 2, new int[]{7554}, new int[]{}, new int[]{ItemID.DRAGON_DART, ItemID.AMETHYST_DART, ItemID.RUNE_DART, ItemID.DRAGON_DARTP, 11233, 11234, ItemID.AMETHYST_DARTP, 25855, 25857, ItemID.RUNE_DARTP, 5634, 5641, -1}, new int[]{}),
    KNIFE("Knife", "Kn", new Color(10, 60, 60), 2, new int[]{7617}, new int[]{}, new int[]{ItemID.RUNE_KNIFE, ItemID.RUNE_KNIFEP, -1}, new int[]{}),
    SBS("Spellbook Swap", "SS", new Color(10, 100, 60), -1, new int[]{6299}, new int[]{1062}, new int[]{}, new int[]{}),
    BOUNCE("Bounce", "VB", new Color(200, 10, 10), -1, new int[]{100000}, new int[]{}, new int[]{}, new int[]{}),
    AID_OTHER("Aid other", "AO", new Color(100, 100, 100), -1, new int[]{4411}, new int[]{}, new int[]{}, new int[]{}),
    VENG_SELF("Veng Self", "VS", new Color(160, 89, 13), -1, new int[]{8316}, new int[]{726}, new int[]{}, new int[]{}),
    HUMIDIFY("Humidify", "HU", new Color(20, 20, 200), -1, new int[]{6294}, new int[]{1061}, new int[]{}, new int[]{}),
    MAGIC_IMBUE("Magic Imbue", "MI", new Color(60, 60, 150), -1, new int[]{722}, new int[]{}, new int[]{}, new int[]{}),
    WEB_WEAVER("Web Weaver Auto", "ww", new Color(240, 18, 119), 3, new int[]{426}, new int[]{}, new int[]{ItemID.WEBWEAVER_BOW}, new int[]{}),
    WEB_WEAVER_SPEC("Web Weaver Spec", "WW", new Color(240, 18, 119), 3, new int[]{9964}, new int[]{}, new int[]{ItemID.WEBWEAVER_BOW}, new int[]{}),
    DEATH("Death", "X", new Color(0, 0, 0), -1, new int[]{836, 10629}, new int[]{}, new int[]{}, new int[]{}),
    TELEPORT("Teleport", "TP", new Color(60, 70, 80), -1, new int[]{8070, 1816}, new int[]{}, new int[]{}, new int[]{}),
    DEATH_CHARGE("Death Charge", "DC", new Color(60, 70, 80), -1, new int[]{8970}, new int[]{}, new int[]{}, new int[]{}),
    HEAL_GROUP("Heal Group", "HG", new Color(50, 170, 100), -1, new int[]{4409}, new int[]{}, new int[]{}, new int[]{}),
    CONSUME("Food/Drink Consumed", "CS", new Color(110, 50, 60), -1, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    THRALL_CAST("Thrall Cast", "TC", new Color(20, 65, 187), -1, new int[]{8973}, new int[]{}, new int[]{}, new int[]{}),
    EXCLUDED_ANIMATION("Excluded", "EX", new Color(0, 0, 0), -1, new int[]{435, 829, 420, 424, 430}, new int[]{}, new int[]{}, new int[]{}),
    BLADE_OF_SAELDOR("Blade of Saeldor", "BS", new Color(67, 10, 45), 4, new int[]{390, 386}, new int[]{}, new int[]{ItemID.BLADE_OF_SAELDOR, ItemID.BLADE_OF_SAELDOR_C}, new int[]{}),
    FANG("Osmumten's Fang", "fng", new Color(10, 10, 100), 5, new int[]{9471, 390}, new int[]{}, new int[]{ItemID.OSMUMTENS_FANG, ItemID.OSMUMTENS_FANG_OR}, new int[]{}),
    FANG_SPEC("Osmumten's Fang Spec", "FNG", new Color(10, 10, 100), 5, new int[]{6118}, new int[]{}, new int[]{ItemID.OSMUMTENS_FANG, ItemID.OSMUMTENS_FANG_OR}, new int[]{}),
    TSOTD("Toxic Staff of the Dead", "TS", new Color(70, 10, 10), 4, new int[]{428, 440, 419}, new int[]{}, new int[]{ItemID.TOXIC_STAFF_OF_THE_DEAD, ItemID.STAFF_OF_THE_DEAD, ItemID.STAFF_OF_LIGHT}, new int[]{}),
    TSOTD_SPEC("Toxic Staff of the Dead Spec", "TS?", new Color(70, 10, 10), 4, new int[]{7083, 7967, 1720}, new int[]{}, new int[]{ItemID.TOXIC_STAFF_OF_THE_DEAD, ItemID.STAFF_OF_THE_DEAD, ItemID.STAFF_OF_LIGHT}, new int[]{}),

    VOID_WAKER("Voidwaker", "vw", new Color(60, 30, 10), 4, new int[]{386, 390}, new int[]{}, new int[]{ItemID.VOIDWAKER}, new int[]{}),
    VOID_WAKER_SPEC("Voidwaker Spec", "VW", new Color(160, 70, 70), 4, new int[]{1378}, new int[]{}, new int[]{ItemID.VOIDWAKER}, new int[]{}),
    MINING("Mining", "M", new Color(30, 30, 30), 3, new int[]{9479}, new int[]{}, new int[]{ItemID.DRAGON_PICKAXE}, new int[]{}),
    DDS_SPEC("Dragon Dagger Spec", "DDS", new Color(140, 10, 10), 4, new int[]{1062}, new int[]{}, new int[]{ItemID.DRAGON_DAGGER, ItemID.DRAGON_DAGGERP, 5680, 5698}, new int[]{}),
    DDS_POKE("Dragon Dagger poke", "dds", new Color(10, 10, 10), 4, new int[]{376, 377}, new int[]{}, new int[]{ItemID.DRAGON_DAGGER, ItemID.DRAGON_DAGGERP, 5680, 5698}, new int[]{}),
    KERIS_BREACHING("Keris of the Breaching", "KB", new Color(140, 140, 30), 4, new int[]{381, 419}, new int[]{}, new int[]{ItemID.KERIS_PARTISAN_OF_BREACHING}, new int[]{}),
    KERIS_CORRUPTION("Keris of the Corruption", "kc", new Color(10, 140, 30), 4, new int[]{381, 419}, new int[]{}, new int[]{ItemID.KERIS_PARTISAN_OF_CORRUPTION}, new int[]{}),
    KERIS_SUN("Keris of the Sun", "ks", new Color(10, 140, 30), 4, new int[]{}, new int[]{381, 419}, new int[]{}, new int[]{ItemID.KERIS_PARTISAN_OF_THE_SUN}),

    KERIS_CORRUPTION_SPEC("Keris of the Corruption spec", "KC", new Color(140, 140, 30), 4, new int[]{9544}, new int[]{}, new int[]{ItemID.KERIS_PARTISAN_OF_CORRUPTION}, new int[]{}),
    KERIS_SUN_SPEC("Keris of the Sun spec", "KS", new Color(140, 140, 30), 4, new int[]{9546}, new int[]{}, new int[]{ItemID.KERIS_PARTISAN_OF_THE_SUN}, new int[]{}),
    PICKUP("Pickup Animation", "PCK", new Color(30, 30, 90), -1, new int[]{827}, new int[]{}, new int[]{}, new int[]{}),
    PUSH_JUG("Push Animation", "PSH", new Color(10, 10, 10), -1, new int[]{832}, new int[]{}, new int[]{}, new int[]{}),
    INQ_MACE("Inquisitor's Mace", "IQM", new Color(180, 50, 30), 4, new int[]{400, 4503}, new int[]{}, new int[]{ItemID.INQUISITORS_MACE}, new int[]{}),
    COLOSSEUM_AUTO("Colosseum Auto Attack", "Auto", new Color(100, 20, 20), 5, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    COLOSSEUM_SPECIAL("Colosseum Special Attack", "Spec", new Color(20, 100, 20), 5, new int[]{}, new int[]{}, new int[]{}, new int[]{}),
    GLAIVE_AUTO("Glaive Auto", "g", new Color(0, 70, 0), 6, new int[]{10923}, new int[]{}, new int[]{ItemID.TONALZTICS_OF_RALOS}, new int[]{}),
    GLAIVE_SPEC("Glaive Spec", "G", new Color(0, 70, 0), 6, new int[]{10914}, new int[]{}, new int[]{ItemID.TONALZTICS_OF_RALOS}, new int[]{}),
    ABYSSAL_BLUDEGON("Abyssal Bludegon", "ab", new Color(10, 70, 80), 4, new int[]{3298}, new int[]{}, new int[]{ItemID.ABYSSAL_BLUDGEON}, new int[]{}),

    ABYSSAL_BLUDEGON_SPEC("Abyssal Bludegon Spec", "AB", new Color(10, 70, 80), 4, new int[]{3299}, new int[]{}, new int[]{ItemID.ABYSSAL_BLUDGEON}, new int[]{}),
    VENATOR_BOW("Venator bow", "VNB", new Color(10, 70, 10), 4, new int[]{9858}, new int[]{}, new int[]{ItemID.VENATOR_BOW}, new int[]{}),
    ARMADYL_CROSSBOW("Armadyl crossbow", "ACB", new Color(10, 70, 10), 5, new int[]{7552}, new int[]{}, new int[]{ItemID.ARMADYL_CROSSBOW}, new int[]{}),
    DRAGON_HUNTER_CROSSBOW("Dragon Hunter crossbow", "DHCB", new Color(10, 70, 10), 5, new int[]{7552}, new int[]{}, new int[]{ItemID.DRAGON_HUNTER_CROSSBOW, ItemID.DRAGON_HUNTER_CROSSBOW_B, ItemID.DRAGON_HUNTER_CROSSBOW_T}, new int[]{}),

    BOWFA("Bowfa", "BOF", new Color(10, 70, 10), 4, new int[]{426}, new int[]{}, new int[]{ItemID.BOW_OF_FAERDHINEN, ItemID.BOW_OF_FAERDHINEN_C}, new int[]{}),
    SOULREAPER_AUTO("Soulreaper Auto", "sr", new Color(80, 10, 10), 5, new int[]{10172, 10171}, new int[]{}, new int[]{ItemID.SOULREAPER_AXE}, new int[]{}),
    SOULREAPER_SPEC("Soulreaper Spec", "SR", new Color(80, 10, 10), 5, new int[]{10173}, new int[]{}, new int[]{ItemID.SOULREAPER_AXE}, new int[]{}),
    NIGHTMARE_STAFF_BASH("Nightmare staff bash", "nsb", new Color(19, 10, 10), 5, new int[]{4505}, new int[]{}, new int[]{ItemID.NIGHTMARE_STAFF, ItemID.ELDRITCH_NIGHTMARE_STAFF, ItemID.HARMONISED_NIGHTMARE_STAFF, ItemID.VOLATILE_NIGHTMARE_STAFF}, new int[]{}),
    VOLATILE_SPEC("Volatile Spec", "VS", new Color(10, 10, 100), 5, new int[]{8532}, new int[]{}, new int[]{ItemID.VOLATILE_NIGHTMARE_STAFF}, new int[]{}),
    ELDRITCH_SPEC("Eldritch Spec", "ES", new Color(10, 10, 100), 5, new int[]{8532}, new int[]{}, new int[]{ItemID.ELDRITCH_NIGHTMARE_STAFF}, new int[]{}),
    HARM_AUTO("Harm Auto", "HA", new Color(10, 10, 100), 4, new int[]{7855}, new int[]{}, new int[]{ItemID.HARMONISED_NIGHTMARE_STAFF}, new int[]{}),
    SURGE_CAST("Surge Cast", "FS", new Color(10, 10, 100), 5, new int[]{7855}, new int[]{}, new int[]{}, new int[]{}),
    SEERCULL("Seercull", "SC", new Color(10, 100, 10), 4, new int[]{426}, new int[]{}, new int[]{ItemID.SEERCULL}, new int[]{}),
    DRAGON_HUNTER_LANCE("Dragon Hunter Lance", "DHL", new Color(100, 10, 10), 4, new int[]{8288, 8289, 8290}, new int[]{}, new int[]{ItemID.DRAGON_HUNTER_LANCE}, new int[]{}),
    DRAGON_CROSSBOW("Dragon crossbow", "dcb", new Color(10, 70, 10), 5, new int[]{7552}, new int[]{}, new int[]{ItemID.DRAGON_CROSSBOW}, new int[]{}),
    DRAGON_CROSSBOW_SPEC("Dragon crossbow Spec", "DCB", new Color(10, 70, 10), 5, new int[]{4230}, new int[]{}, new int[]{ItemID.DRAGON_CROSSBOW}, new int[]{}),
    ACCURSED_SCEPTRE_AUTO("Accursed Sceptre Auto", "acs", new Color(30, 120, 130), 4, new int[]{1167}, new int[]{}, new int[]{ItemID.ACCURSED_SCEPTRE}, new int[]{2337}),
    ACCURSED_SCEPTRE_SPEC("Accursed Sceptre Spec", "ACS", new Color(30, 120, 130), 4, new int[]{9961}, new int[]{}, new int[]{ItemID.ACCURSED_SCEPTRE}, new int[]{2339}),
    ATLATL("Atlatl Auto", "atl", new Color(10, 60, 60), 3, new int[]{10757}, new int[]{}, new int[]{ItemID.ECLIPSE_ATLATL}, new int[]{}),
    ATLATL_SPEC("Atlatl Spec", "ATL", new Color(10, 60, 60), 3, new int[]{11060}, new int[]{}, new int[]{ItemID.ECLIPSE_ATLATL}, new int[]{}),
    SALAMANDER("Salamander", "SLM", new Color(160, 160, 160), 4, new int[]{5247}, new int[]{}, new int[]{ItemID.BLACK_SALAMANDER, ItemID.ORANGE_SALAMANDER, ItemID.TECU_SALAMANDER, ItemID.RED_SALAMANDER, ItemID.SWAMP_LIZARD}, new int[]{}),
    HUNTERS_SPEAR("Hunter's Spear", "HS", new Color(120, 130, 30), 5, new int[]{929}, new int[]{}, new int[]{ItemID.HUNTERS_SPEAR}, new int[]{}),
    HUNTERS_SUNLIGHT_CROSSBOW("Hunter's Sunlight CrossBow", "HSCB", new Color(50, 130, 30), 3, new int[]{7552}, new int[]{}, new int[]{ItemID.HUNTERS_SUNLIGHT_CROSSBOW}, new int[]{}),
    DUAL_MAUCUAHUITL("Dual Maucuahuitl", "DMC", new Color(180, 30, 60), 5, new int[]{10989}, new int[]{}, new int[]{ItemID.DUAL_MACUAHUITL}, new int[]{}),
    ZOMBIE_AXE("Zombie Axe", "ZBX", new Color(150, 0, 10), 5, new int[]{7004, 3852}, new int[]{}, new int[]{ItemID.ZOMBIE_AXE}, new int[]{}),
    BLUE_MOON_SPEAR_AUTO("Blue Moon Spear", "bms", new Color(40, 100, 100), 5, new int[]{1711, 1712}, new int[]{}, new int[]{ItemID.BLUE_MOON_SPEAR}, new int[]{}),
    BLUE_MOON_SPEAR_SPEC("Blue Moon spear Spec", "BMS", new Color(40, 100, 100), 5, new int[]{1710}, new int[]{}, new int[]{ItemID.BLUE_MOON_SPEAR}, new int[]{}),
    ELDER_MAUL("Elder Maul", "EM", new Color(100, 60, 60), 6, new int[]{7516}, new int[]{}, new int[]{ItemID.ELDER_MAUL, ItemID.ELDER_MAUL_OR}, new int[]{}),
    ABYSSAL_DAGGER("Abyssal Dagger", "ad", new Color(20, 20, 20), 4, new int[]{3297, 3294}, new int[]{}, new int[]{ItemID.ABYSSAL_DAGGER, ItemID.ABYSSAL_DAGGER_P, ItemID.ABYSSAL_DAGGER_P_13269, ItemID.ABYSSAL_DAGGER_P_13271}, new int[]{}),
    ABYSSAL_DAGGER_SPEC("Abyssal Dagger Spec", "AD", new Color(20, 20, 20), 4, new int[]{3300}, new int[]{}, new int[]{ItemID.ABYSSAL_DAGGER, ItemID.ABYSSAL_DAGGER_P, ItemID.ABYSSAL_DAGGER_P_13269, ItemID.ABYSSAL_DAGGER_P_13271}, new int[]{}),
    KODAI_BOP("Kodai Bop", "f", new Color(50, 50, 170), 4, new int[]{414}, new int[]{}, new int[]{ItemID.KODAI_WAND}, new int[]{}),
    PICKAXE("Pickaxe", "PX", new Color(150, 50, 120), 5, new int[]{400, 401}, new int[]{}, new int[]{ItemID.CRYSTAL_PICKAXE, ItemID.DRAGON_PICKAXE, ItemID.DRAGON_PICKAXE_OR, ItemID.RUNE_PICKAXE, ItemID.ADAMANT_PICKAXE, ItemID.MITHRIL_PICKAXE, ItemID.BLACK_PICKAXE, ItemID.STEEL_PICKAXE, ItemID.IRON_PICKAXE, ItemID.BRONZE_PICKAXE}, new int[]{}),
    BONE_DAGGER_POKE("Bone Dagger Poke", "bd", new Color(150, 20, 20), 4, new int[]{386, 390}, new int[]{}, new int[]{ItemID.BONE_DAGGER, ItemID.BONE_DAGGER_P, ItemID.BONE_DAGGER_P_8876, ItemID.BONE_DAGGER_P_8878}, new int []{}),
    BONE_DAGGER_SPEC("Bone Dagger Spec", "BD", new Color(150, 20, 20), 4, new int[]{4198}, new int[]{}, new int[]{ItemID.BONE_DAGGER, ItemID.BONE_DAGGER_P, ItemID.BONE_DAGGER_P_8876, ItemID.BONE_DAGGER_P_8878}, new int[]{}),
    UNARMED("Unarmed", "???", new Color(0, 0, 0), 4, new int[]{422, 423}, new int[]{}, new int[]{-1}, new int[]{}),



    ;

    public final String name;
    public final String shorthand;
    public final Color color;
    public final int attackTicks;
    public final int[] animations;
    public final int[] graphics;
    public final int[] weaponIDs;
    public final int[] projectiles;


    PlayerAnimation(String name, String shorthand, Color color, int attackTicks, int[] animations, int[] graphics, int[] weaponIDs, int[] projectiles)
    {
        if (attackTicks == -1)
        {
            this.attackTicks = 0;
        } else
        {
            this.attackTicks = attackTicks;
        }
        this.name = name;
        this.shorthand = shorthand;
        this.color = color;
        this.animations = animations;
        this.graphics = graphics;
        this.weaponIDs = weaponIDs;
        this.projectiles = projectiles;
    }
}
