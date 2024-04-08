package com.advancedraidtracker.utility.datautility;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.RaidType;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static com.advancedraidtracker.constants.RaidRoom.*;
import static com.advancedraidtracker.utility.datautility.DataPoint.MenuType.*;

public enum DataPoint
{
    CHALLENGE_TIME("Challenge Time", types.TIME, ANY, EXCLUDED),
    OVERALL_TIME("Overall Time", types.TIME, ANY, EXCLUDED),
    MAIDEN_TIME("Maiden Time", types.TIME, MAIDEN, MenuType.TIME),
    BLOAT_TIME("Bloat Time", types.TIME, BLOAT, MenuType.TIME),
    NYLOCAS_TIME("Nylocas Time", types.TIME, NYLOCAS, MenuType.TIME),
    SOTETSEG_TIME("Sotetseg Time", types.TIME, SOTETSEG, MenuType.TIME),
    XARPUS_TIME("Xarpus Time", types.TIME, XARPUS, MenuType.TIME),
    VERZIK_TIME("Verzik Time", types.TIME, VERZIK, MenuType.TIME),
    DEFENSE("Defense", types.OTHER_INT, ALL, SPEC),
    TIME_OUTSIDE_ROOMS("Time Outside Rooms", types.TIME, ANY, TIME),
    MAIDEN_BLOOD_SPAWNED("Maiden blood spawned", types.OTHER_INT, MAIDEN, MISC),
    MAIDEN_BLOOD_THROWN("Maiden blood thrown", types.OTHER_INT, MAIDEN, MISC),
    MAIDEN_PLAYER_STOOD_IN_THROWN_BLOOD("Maiden player stood in thrown blood", types.OTHER_INT, MAIDEN, MISC, true),
    MAIDEN_PLAYER_STOOD_IN_SPAWNED_BLOOD("Maiden player stood in spawned blood", types.OTHER_INT, MAIDEN, MISC, true),
    MAIDEN_HEALS_FROM_THROWN_BLOOD("Maiden heals from thrown blood", types.OTHER_INT, MAIDEN, MISC, true),
    MAIDEN_HEALS_FROM_SPAWNED_BLOOD("Maiden heals from spawned blood", types.OTHER_INT, MAIDEN, MISC, true),
    MAIDEN_PLAYER_STOOD_IN_BLOOD("Maiden player stood in any blood", types.OTHER_INT, MAIDEN, MISC, true),
    MAIDEN_HEALS_FROM_ANY_BLOOD("Maiden heals from any blood", types.OTHER_INT, MAIDEN, MISC, true),
    MAIDEN_MELEE_DRAINS("Maiden drained melee", types.OTHER_INT, MAIDEN, MISC, true),
    MAIDEN_CRABS_LEAKED("Maiden crabs leaked", types.OTHER_INT, MAIDEN, MISC),
    MAIDEN_CRABS_LEAKED_FULL_HP("Maiden crabs leaked full", types.OTHER_INT, MAIDEN, MISC),
    MAIDEN_HP_HEALED("Maiden HP healed", types.OTHER_INT, MAIDEN, MISC),
    MAIDEN_DINHS_SPECS("Maiden dinhs specs", types.OTHER_INT, MAIDEN, SPEC, true),
    MAIDEN_DINHS_CRABS_HIT("Maiden dinhs crabs Hit", types.OTHER_INT, MAIDEN, SPEC, true),
    MAIDEN_DINHS_TARGETS_HIT("Maiden dinhs targets hit", types.OTHER_INT, MAIDEN, SPEC, true),
    MAIDEN_DINHS_AVERAGE_HP_HIT("Maiden dinhs average HP crab", types.OTHER_INT, MAIDEN, SPEC, true),
    MAIDEN_DINHS_PERCENT_TARGETS_CRAB("Maiden dinhs % crabs targeted", types.OTHER_INT, MAIDEN, SPEC, true),
    MAIDEN_DINHS_CRABS_UNDER_27_TARGETED("Maiden dinhs crab < 27hp targeted", types.OTHER_INT, MAIDEN, SPEC, true),
    MAIDEN_DINHS_CRABS_UNDER_27_TARGETED_PERCENT("Maiden dinhs crabs <27hp targeted %", types.OTHER_INT, MAIDEN, SPEC, true),
    MAIDEN_CHINS_THROWN("Maiden chins thrown", types.OTHER_INT, MAIDEN, MISC, true),
    MAIDEN_CHINS_THROWN_WRONG_DISTANCE("Maiden chins thrown wrong distance", types.OTHER_INT, MAIDEN, MISC, true),
    MAIDEN_CHIN_CORRECT_DISTANCE_PERCENT("Maiden chins correct distance %", types.OTHER_INT, MAIDEN, MISC, true),
    RAID_INDEX("Raid Index", types.OTHER_INT, ANY, EXCLUDED),
    PARTY_SIZE("Party Size", types.OTHER_INT, ANY, EXCLUDED),

    DAMAGE_DEALT("Damage Dealt", types.OTHER_INT, ALL, DAMAGE_PRAYER),
    DAMAGE_RECEIVED("Damage Received", types.OTHER_INT, ALL, DAMAGE_PRAYER),
    PRAYER_USED("Prayer Used", types.OTHER_INT, ALL, DAMAGE_PRAYER),

    ATTACK_XP("Attack XP Gained", types.OTHER_INT, ALL, EXPERIENCE),
    STRENGTH_XP("Strength XP Gained", types.OTHER_INT, ALL, EXPERIENCE),
    DEFENSE_XP("Defense XP Gained", types.OTHER_INT, ALL, EXPERIENCE),
    HP_XP("HP XP Gained", types.OTHER_INT, ALL, EXPERIENCE),
    MAGIC_XP("Magic XP Gained", types.OTHER_INT, ALL, EXPERIENCE),
    RANGE_XP("Range XP Gained", types.OTHER_INT, ALL, EXPERIENCE),
    AGILITY_XP("Agility XP Gained", types.OTHER_INT, ALL, EXPERIENCE),
    FLETCHING_XP("Fletching XP Gained", types.OTHER_INT, ALL, EXPERIENCE),
    SLAYER_XP("Slayer XP Gained", types.OTHER_INT, ALL, EXPERIENCE),
    BLOAT_DOWNS("Bloat downs", types.INT_MAP, BLOAT, MISC),
    BLOAT_FIRST_WALK_SCYTHES("Bloat 1st Walk Scythes", types.OTHER_INT, BLOAT, MISC, true),
    BLOAT_FIRST_WALK_DEATHS("Bloat 1st Walk Deaths", types.OTHER_INT, BLOAT, MISC, true),
    BLOAT_HP_FIRST_DOWN("Bloat HP% 1st down", types.OTHER_INT, BLOAT, MISC),
    NYLO_STALLS_PRE_20("Nylo stalls pre 20", types.OTHER_INT, NYLOCAS, MISC),
    NYLO_STALLS_POST_20("Nylo stalls post 20", types.OTHER_INT, NYLOCAS, MISC),
    NYLO_STALLS_TOTAL("Nylo stalls total", types.INT_MAP, NYLOCAS, MISC),
    NYLO_SPLITS_RANGE("Nylo splits range", types.OTHER_INT, NYLOCAS, MISC),
    NYLO_SPLITS_MAGE("Nylo splits mage", types.OTHER_INT, NYLOCAS, MISC),
    NYLO_SPLITS_MELEE("Nylo splits melee", types.OTHER_INT, NYLOCAS, MISC),
    NYLO_ROTATIONS_RANGE("Nylo rotations range", types.OTHER_INT, NYLOCAS, MISC),
    NYLO_ROTATIONS_MAGE("Nylo rotations mage", types.OTHER_INT, NYLOCAS, MISC),
    NYLO_ROTATIONS_MELEE("Nylo rotations melee", types.OTHER_INT, NYLOCAS, MISC),
    NYLO_ROTATIONS_TOTAL("Nylo total rotations", types.OTHER_INT, NYLOCAS, MISC),
    NYLO_DEFENSE("Nylo defense", types.OTHER_INT, NYLOCAS, SPEC, 50), //todo hmm why does this exist
    NYLO_WAVES("Nylo waves", types.INT_MAP, NYLOCAS, MenuType.EXCLUDED, false),
    SOTE_SPECS_P1("Sote specs p1", types.OTHER_INT, SOTETSEG, SPEC),
    SOTE_SPECS_P2("Sote specs p2", types.OTHER_INT, SOTETSEG, SPEC),
    SOTE_SPECS_P3("Sote specs p3", types.OTHER_INT, SOTETSEG, SPEC),
    SOTE_SPECS_TOTAL("Sote specs total", types.OTHER_INT, SOTETSEG, SPEC),
    XARP_HEALING("Xarp Healing", types.OTHER_INT, XARPUS, MISC),
    VERZIK_BOUNCES("Verzik bounces", types.INT_MAP, VERZIK, MISC, true),
    VERZIK_CRABS_SPAWNED("Verzik crabs spawned", types.INT_MAP, VERZIK, EXCLUDED),
    VERZIK_P2_CRABS_SPAWNED("Verzik P2 crabs spawned", types.OTHER_INT, VERZIK, MISC),
    VERZIK_P3_CRABS_SPAWNED("Verzik P3 crabs spawned", types.OTHER_INT, VERZIK, MISC),
    VERZIK_REDS_SETS("Verzik Red Sets", types.INT_MAP, VERZIK, MISC),
    VERZIK_REDS_PROC_PERCENT("Verzik Red Proc Percent", types.OTHER_INT, VERZIK, MISC),
    VERZIK_HP_AT_WEBS("Verzik HP% at webs", types.OTHER_INT, VERZIK, MISC),
    MAIDEN_70_SPLIT("Maiden 70s split", types.TIME_SPLIT, MAIDEN, TIME),
    MAIDEN_7050_DURATION("Maiden 70-50s split", types.TIME_DURATION, MAIDEN, TIME),
    MAIDEN_50_SPLIT("Maiden 50s split", types.TIME_SPLIT, MAIDEN, TIME),
    MAIDEN_5030_SPLIT("Maiden 50-30s split", types.TIME_DURATION, MAIDEN, TIME),
    MAIDEN_30_SPLIT("Maiden 30s split", types.TIME_SPLIT, MAIDEN, TIME),
    MAIDEN_SKIP_SPLIT("Maiden Skip split", types.TIME_DURATION, MAIDEN, TIME),
    BLOAT_FIRST_DOWN_TIME("Bloat 1st down time", types.OTHER_INT, BLOAT, TIME),
    NYLO_BOSS_SPAWN("Nylo boss spawn", types.TIME_SPLIT, NYLOCAS, TIME),
    NYLO_BOSS_DURATION("Nylo boss duration", types.TIME_DURATION, NYLOCAS, TIME),
    NYLO_LAST_WAVE("Nylo last wave", types.TIME_SPLIT, NYLOCAS, TIME),
    NYLO_LAST_DEAD("Nylo last dead", types.TIME_SPLIT, NYLOCAS, TIME),
    NYLO_CLEANUP("Nylo cleanup", types.TIME_SPLIT, NYLOCAS, TIME),
    NYLOCAS_PILLAR_DESPAWN_TICK("Nylo Pillar despawn tick", types.TIME_SPLIT, NYLOCAS, TIME),
    SOTE_P1_SPLIT("Sote P1 split", types.TIME_SPLIT, SOTETSEG, TIME),
    SOTE_P2_SPLIT("Sote P2 split", types.TIME_SPLIT, SOTETSEG, TIME),
    SOTE_P2_DURATION("Sote P2 duration", types.TIME_SPLIT, SOTETSEG, TIME),

    SOTE_P3_DURATION("Sote P3 duration", types.TIME_SPLIT, SOTETSEG, TIME),
    SOTE_M1_SPLIT("Sote maze1 split", types.TIME_SPLIT, SOTETSEG, TIME),
    SOTE_M1_DURATION("Sote maze1 Duration", types.TIME_SPLIT, SOTETSEG, TIME),
    SOTE_M2_SPLIT("Sote maze2 split", types.TIME_SPLIT, SOTETSEG, TIME),
    SOTE_M2_DURATION("Sote maze2 duration", types.TIME_SPLIT, SOTETSEG, TIME),
    SOTE_P3_SPLIT("Sote P3 split", types.TIME_SPLIT, SOTETSEG, TIME),
    SOTE_MAZE_SUM("Sote mazes combined", types.TIME_SPLIT, SOTETSEG, TIME),
    XARP_SCREECH("Xarp screech", types.TIME_SPLIT, XARPUS, TIME),
    XARP_POST_SCREECH("Xarp post screech", types.TIME_SPLIT, XARPUS, TIME),
    VERZIK_P2_SPLIT("Verzik P1 Time", types.TIME_SPLIT, VERZIK, TIME),

    VERZIK_P2_TILL_REDS("Verzik P2 until reds split", types.TIME_SPLIT, VERZIK, TIME),
    VERZIK_REDS_SPLIT("Verzik reds split", types.TIME_SPLIT, VERZIK, TIME),
    VERZIK_REDS_DURATION("Verzik reds duration", types.TIME_SPLIT, VERZIK, TIME),
    VERZIK_P3_SPLIT("Verzik P2 split", types.TIME_SPLIT, VERZIK, TIME),
    VERZIK_P2_DURATION("Verzik P2 Time", types.TIME_SPLIT, VERZIK, TIME),
    VERZIK_P3_DURATION("Verzik P3 Time", types.TIME_SPLIT, VERZIK, TIME),

    NYLO_ENTRY("Nylo Entry", types.TIME, NYLOCAS, TIME),
    SOTE_ENTRY("Sote Entry", types.TIME, SOTETSEG, TIME),
    XARP_ENTRY("Xarp Entry", types.TIME, XARPUS, TIME),
    VERZIK_ENTRY("Verzik Entry", types.TIME, VERZIK, TIME),

    PLAYER_HAMMER_ATTEMPTED("Hammer Attempted", types.OTHER_INT, ALL, SPEC),
    PLAYER_HAMMER_HIT_COUNT("Hammer Hit", types.OTHER_INT, ALL, SPEC),
    PLAYER_BGS_HIT_COUNT("BGS Hits", types.OTHER_INT, ALL, SPEC),
    PLAYER_BGS_DAMAGE("BGS Damage", types.OTHER_INT, ALL, SPEC),

    DEATHS("Deaths", types.OTHER_INT, ALL, MISTAKES, true),
    THRALL_SUMMONS("Thrall Summons", types.INT_MAP, ALL, THRALL, true),
    THRALL_DESPAWNS("Thrall Despawns", types.INT_MAP, ALL, THRALL, true),
    THRALL_ATTACKS("Thrall Attacks", types.OTHER_INT, ALL, THRALL, true),

    THRALL_DAMAGE("Thrall Damage", types.OTHER_INT, ALL, THRALL, true),

    VENG_DAMAGE("Veng Damage", types.OTHER_INT, ALL, VENG, true),

    VENG_CASTS("Veng Casts", types.OTHER_INT, ALL, VENG, true),

    VENG_PROCS("Veng Procs", types.OTHER_INT, ALL, VENG, true),
    KODAI_BOPS("Kodai bops", types.OTHER_INT, ALL, MISTAKES, true),
    DWH_BOPS("DWH bops", types.OTHER_INT, ALL, MISTAKES, true),
    CHALLY_POKE("Chally pokes", types.OTHER_INT, ALL, MISTAKES, true),
    BGS_WHACKS("BGS whacks", types.OTHER_INT, ALL, MISTAKES, true),
    UNKNOWN("Unknown", types.OTHER_BOOL, ALL, EXCLUDED),
    DAWN_DROPS("Dawn Drops", types.INT_MAP, VERZIK, EXCLUDED),

    WEBS_THROWN("Webs Thrown", types.INT_MAP, VERZIK, EXCLUDED),

    TOA_PARTY_SIZE("TOA Party Size", types.OTHER_INT, ANY_TOA, EXCLUDED),
    TOA_RAID_INDEX("TOA Raid Index", types.OTHER_INT, ANY_TOA, EXCLUDED),
    TOA_INVOCATION_LEVEL("TOA Invocation Level", types.OTHER_INT, ANY_TOA, EXCLUDED),

    CRONDIS_TIME("Crondis Time", types.TIME, CRONDIS, TIME),
    ZEBAK_TIME("Zebak Time", types.TIME, ZEBAK, TIME),
    ZEBAK_ENRAGED_SPLIT("Zebak Enraged Split", types.TIME_SPLIT, ZEBAK, TIME),
    ZEBAK_ENRAGED_DURATION("Zebak Enraged Duration", types.TIME_SPLIT, ZEBAK, TIME),

    SCABARAS_TIME("Scabaras Time", types.TIME, SCABARAS, TIME),

    KEPHRI_TIME("Kephri Time", types.TIME, KEPHRI, TIME),
    KEPHRI_P1_DURATION("Kephri P1 Duration", types.TIME_SPLIT, KEPHRI, TIME),
    KEPHRI_SWARM1_DURATION("Kephri Swarm1 Duration", types.TIME_SPLIT, KEPHRI, TIME),
    KEPHRI_P2_SPLIT("Kephri P2 Split", types.TIME_SPLIT, KEPHRI, TIME),
    KEPHRI_P2_DURATION("Kephri P2 Duration", types.TIME_SPLIT, KEPHRI, TIME),
    KEPHRI_SWARM2_SPLIT("Kephri Swarm2 Split", types.TIME_SPLIT, KEPHRI, TIME),
    KEPHRI_SWARM2_DURATION("Kephri Swarm2 Duration", types.TIME_SPLIT, KEPHRI, TIME),
    KEPHRI_P3_SPLIT("Kephri P3 Split", types.TIME_SPLIT, KEPHRI, TIME),
    KEPHRI_P3_DURATION("Kephri P3 Duration", types.TIME_SPLIT, KEPHRI, TIME),

    APMEKEN_TIME("Apmeken Time", types.TIME, APMEKEN, TIME),

    BABA_TIME("Baba Time", types.TIME, BABA, TIME),
    BABA_P1_DURATION("Baba P1 Duration", types.TIME_SPLIT, BABA, TIME),
    BABA_BOULDER_1_DURATION("Baba Boulder1 Duration", types.TIME_SPLIT, BABA, TIME),
    BABA_P2_SPLIT("Baba P2 Split", types.TIME_SPLIT, BABA, TIME),
    BABA_P2_DURATION("Baba P2 Duration", types.TIME_SPLIT, BABA, TIME),
    BABA_BOULDER_2_SPLIT("Baba Boulder2 Split", types.TIME_SPLIT, BABA, TIME),
    BABA_BOULDER_2_DURATION("Baba Boulder2 Duration", types.TIME_SPLIT, BABA, TIME),
    BABA_P3_SPLIT("Baba P3 Split", types.TIME_SPLIT, BABA, TIME),
    BABA_P3_DURATION("Baba P3 Duration", types.TIME_SPLIT, BABA, TIME),

    HET_TIME("Het Time", types.TIME, HET, TIME),

    AKKHA_TIME("Akkha Time", types.TIME, AKKHA, TIME),

    AKKHA_P1_DURATION("Akkha P1 Duration", types.TIME_SPLIT, AKKHA, TIME),
    AKKHA_SHADOW_1_DURATION("Akkha Shadow1 Duration", types.TIME_SPLIT, AKKHA, TIME),

    AKKHA_P2_SPLIT("Akkha P2 Split", types.TIME_SPLIT, AKKHA, TIME),
    AKKHA_P2_DURATION("Akkha P2 Duration", types.TIME_SPLIT, AKKHA, TIME),
    AKKHA_SHADOW_2_SPLIT("Akkha Shadow2 Split", types.TIME_SPLIT, AKKHA, TIME),
    AKKHA_SHADOW_2_DURATION("Akkha Shadow2 Duration", types.TIME_SPLIT, AKKHA, TIME),

    AKKHA_P3_SPLIT("Akkha P3 Split", types.TIME_SPLIT, AKKHA, TIME),
    AKKHA_P3_DURATION("Akkha P3 Duration", types.TIME_SPLIT, AKKHA, TIME),
    AKKHA_SHADOW_3_SPLIT("Akkha Shadow3 Split", types.TIME_SPLIT, AKKHA, TIME),
    AKKHA_SHADOW_3_DURATION("Akkha Shadow3 Duration", types.TIME_SPLIT, AKKHA, TIME),

    AKKHA_P4_SPLIT("Akkha P4 Split", types.TIME_SPLIT, AKKHA, TIME),
    AKKHA_P4_DURATION("Akkha P4 Duration", types.TIME_SPLIT, AKKHA, TIME),
    AKKHA_SHADOW_4_SPLIT("Akkha Shadow4 Split", types.TIME_SPLIT, AKKHA, TIME),
    AKKHA_SHADOW_4_DURATION("Akkha Shadow4 Duration", types.TIME_SPLIT, AKKHA, TIME),

    AKKHA_P5_SPLIT("Akkha P5 Split", types.TIME_SPLIT, AKKHA, TIME),
    AKKHA_P5_DURATION("Akkha P5 Duration", types.TIME_SPLIT, AKKHA, TIME),

    AKKHA_FINAL_PHASE_SPLIT("Akkha Final Phase Split", types.TIME_SPLIT, AKKHA, TIME),
    AKKHA_FINAL_PHASE_DURATION("Akkha Final Phase Duration", types.TIME_SPLIT, AKKHA, TIME),
    AKKHA_NULL_HIT("Akkha Null Hit", types.OTHER_INT, AKKHA, MISC, true),

    WARDENS_TIME("Wardens Time", types.TIME, WARDENS, TIME),
    WARDENS_P1_DURATION("Wardens P1 Time", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_P2_DURATION("Wardens P2 Time", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_P3_SPLIT("Wardens P3 Split", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_ENRAGED_SPLIT("Wardens Enraged Split", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_UNTIL_ENRAGED_DURATION("Wardens Until Enraged Duration", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_P3_DURATION("Wardens P3 Time", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_ENRAGED_DURATION("Wardens Enraged Duration", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_SKULL_1_SPLIT("Wardens Skull 1 Split", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_SKULL_1_DURATION("Wardens Skull 1 Duration", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_SKULL_2_SPLIT("Wardens Skull 2 Split", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_SKULL_2_DURATION("Wardens Skull 2 Duration", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_SKULL_3_SPLIT("Wardens Skull 3 Split", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_P2_DOWNS("Wardens P2 Downs", types.OTHER_INT, WARDENS, MISC),
    WARDENS_P2_CORE_SPAWNS("Wardens P2 Core Spawns", types.INT_MAP, WARDENS, EXCLUDED),
    WARDENS_P2_CORE_DESPAWNS("Wardens P2 Core Despawns", types.INT_MAP, WARDENS, EXCLUDED),
    WARDENS_SKULL_3_DURATION("Wardens Skull 3 Duration", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_SKULL_4_SPLIT("Wardens Skull 4 Split", types.TIME_SPLIT, WARDENS, TIME),
    WARDENS_SKULL_4_DURATION("Wardens Skull 4 Duration", types.TIME_SPLIT, WARDENS, TIME),
    APMEKEN_VOLATILE_COUNT("Apmeken Volatile Count", types.OTHER_INT, APMEKEN, MISC),
    APMEKEN_SHAMAN_COUNT("Apmeken Shaman Count", types.OTHER_INT, APMEKEN, MISC),
    APMEKEN_CURSED_COUNT("Apmeken Cursed Count", types.OTHER_INT, APMEKEN, MISC),
    BABA_BOULDERS_THROWN("Baba Boulders Thrown", types.OTHER_INT, BABA, MISC),
    BABA_BOULDERS_BROKEN("Baba Boulders Broken", types.OTHER_INT, BABA, MISC),
    KEPHRI_SWARMS_HEALED("Kephri Swarms Healed", types.OTHER_INT, KEPHRI, MISC),
    KEPHRI_SWARMS_TOTAL("Kephri Swarms Total", types.OTHER_INT, KEPHRI, MISC),
    KEPHRI_MELEE_SCARAB_HEALS("Kephri Melee Scarab Heals", types.OTHER_INT, KEPHRI, MISC),
    KEPHRI_DUNG_THROWN("Kephri Dung Thrown", types.INT_MAP, KEPHRI, MISC),
    KEPHRI_MELEE_TICKS_ALIVE("Kephri Melee Ticks Alive", types.OTHER_INT, KEPHRI, MISC),
    HET_DOWNS("Het Downs", types.INT_MAP, HET, MISC),
    CRONDIS_HEALS_100("Crondis Heals 100", types.OTHER_INT, CRONDIS, MISC),
    CRONDIS_HEALS_50("Crondis Heals 50", types.OTHER_INT, CRONDIS, MISC),
    CRONDIS_HEALS_25("Crondis Heals 25", types.OTHER_INT, CRONDIS, MISC),
    CRONDIS_CROCODILE_DAMAGE("Crondis Damage from Crocodile", types.OTHER_INT, CRONDIS, MISC),
    ZEBAK_JUGS_PUSHED("Zebak Jugs Pushed", types.OTHER_INT, ZEBAK, MISC),
    ZEBAK_BOULDER_ATTACKS("Zebak Boulder Attacks", types.INT_MAP, ZEBAK, MISC),
    ZEBAK_WATERFALL_ATTACKS("Zebak Waterfall Attacks", types.INT_MAP, ZEBAK, MISC),

    COLOSSEUM_WAVE_1_DURATION("Col Wave 1 Duration", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_2_SPLIT("Col Wave 2 Split", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_2_DURATION("Col Wave 2 Duration", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_3_SPLIT("Col Wave 3 Split", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_3_DURATION("Col Wave 3 Duration", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_4_SPLIT("Col Wave 4 Split", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_4_DURATION("Col Wave 4 Duration", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_5_SPLIT("Col Wave 5 Split", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_5_DURATION("Col Wave 5 Duration", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_6_SPLIT("Col Wave 6 Split", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_6_DURATION("Col Wave 6 Duration", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_7_SPLIT("Col Wave 7 Split", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_7_DURATION("Col Wave 7 Duration", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_8_SPLIT("Col Wave 8 Split", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_8_DURATION("Col Wave 8 Duration", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_9_SPLIT("Col Wave 9 Split", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_9_DURATION("Col Wave 9 Duration", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_10_SPLIT("Col Wave 10 Split", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_10_DURATION("Col Wave 10 Duration", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_11_SPLIT("Col Wave 11 Split", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_11_DURATION("Col Wave 11 Duration", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_12_SPLIT("Col Wave 12 Split", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_WAVE_12_DURATION("Col Wave 12 Duration", types.TIME_SPLIT, COLOSSEUM, TIME),
    COLOSSEUM_PERFECT_PARRY("Perfect Parries", types.OTHER_INT, COLOSSEUM, MISC),
    COLOSSEUM_GRAPPLES("Grapples", types.INT_MAP, COLOSSEUM, MISC),
    COLOSSEUM_NPC_HEALED("NPC Healed", types.OTHER_INT, ALL, MISC),
    INFERNO_WAVE_STARTS("Inferno Wave Starts", types.INT_MAP, INFERNO, MenuType.EXCLUDED, false),
    INFERNO_WAVE_DURATIONS("Inferno Wave Durations", types.INT_MAP, INFERNO, MenuType.EXCLUDED, false),


    ;;

    public static ArrayList<DataPoint> getTOBValues()
    {
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        for (DataPoint dataPoint : values())
        {
            if (dataPoint.isTOB())
            {
                dataPoints.add(dataPoint);
            }
        }
        return dataPoints;
    }

    public static ArrayList<DataPoint> getTOAValues()
    {
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        for (DataPoint dataPoint : values())
        {
            if (dataPoint.isTOA())
            {
                dataPoints.add(dataPoint);
            }
        }
        return dataPoints;
    }

    public boolean isTOB()
    {
        return room.equals(ANY_TOB)
                || room.equals(MAIDEN)
                || room.equals(BLOAT)
                || room.equals(NYLOCAS)
                || room.equals(SOTETSEG)
                || room.equals(XARPUS)
                || room.equals(VERZIK)
                || room.equals(ALL)
                || room.equals(ANY);
    }

    public boolean isTOA()
    {
        return room.equals(ANY_TOA)
                || room.equals(CRONDIS)
                || room.equals(ZEBAK)
                || room.equals(SCABARAS)
                || room.equals(KEPHRI)
                || room.equals(APMEKEN)
                || room.equals(BABA)
                || room.equals(HET)
                || room.equals(AKKHA)
                || room.equals(WARDENS)
                || room.equals(ALL)
                || room.equals(ANY);
    }

    public boolean isExclusivelyTOA()
    {
        return isTOA() && !(room.equals(ANY) || room.equals(ALL));
    }

    public boolean isExclusivelyTOB()
    {
        return isTOB() && !(room.equals(ANY) || room.equals(ALL));
    }

    public boolean isExclusivelyCOX()
    {
        return isCOX() && !(room.equals(ANY) || room.equals(ALL));
    }

    public boolean isCOX()
    {
        return false;
    }

    public boolean isType(RaidType raidType)
    {
        switch (raidType)
        {
            case COX:
                return isCOX();
            case TOB:
                return isTOB();
            case TOA:
                return isTOA();
            default:
                return true;
        }
    }

    public static Map<String, DataPoint> dataPointCache = new HashMap<>();

    public static DataPoint getValue(String s)
    {
        if (dataPointCache.containsKey(s))
        {
            return dataPointCache.get(s);
        }
        for (DataPoint point : values())
        {
            if (point.name.equals(s))
            {
                dataPointCache.put(s, point);
                return point;
            }
        }
        dataPointCache.put(s, UNKNOWN);
        return DataPoint.UNKNOWN;
    }


    public enum types
    {
        OTHER_INT, OTHER_BOOL, TIME_DURATION, TIME_SPLIT, INT_MAP, TIME
    }

    public enum MenuType
    {
        TIME("Time"),
        MISC("Misc"),
        SPEC("Specs"),
        THRALL("Thrall"),
        VENG("Veng"),
        DAMAGE_PRAYER("Damage/Prayer"),
        EXPERIENCE("Experience"),
        MISTAKES("Mistakes"),
        EXCLUDED("Excluded", true);

        public final String name;
        public final boolean excluded;

        MenuType(String name)
        {
            this.name = name;
            excluded = false;
        }

        MenuType(String name, boolean excluded)
        {
            this.name = name;
            this.excluded = excluded;
        }
    }

    public final String name;
    public final int value;
    public final types type;
    public final RaidRoom room;
    public final boolean playerSpecific;
    public final MenuType menuType;

    DataPoint(String name, types type, RaidRoom room, MenuType menuType)
    {
        this.name = name;
        this.value = 0;
        this.type = type;
        this.room = room;
        this.playerSpecific = false;
        this.menuType = menuType;
    }

    DataPoint(String name, types type, RaidRoom room, MenuType menuType, int value)
    {
        this.name = name;
        this.value = value;
        this.type = type;
        this.room = room;
        this.playerSpecific = false;
        this.menuType = menuType;
    }

    DataPoint(String name, types type, RaidRoom room, MenuType menuType, boolean playerSpecific)
    {
        this.playerSpecific = playerSpecific;
        this.name = name;
        this.value = 0;
        this.type = type;
        this.room = room;
        this.menuType = menuType;
    }

    public static String[] getPlayerSpecific()
    {
        return getPlayerSpecific(RaidType.ALL);
    }

    public static String[] getPlayerSpecific(RaidType raidType)
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.playerSpecific && point.isType(raidType))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getOtherIntNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.type.equals(types.OTHER_INT))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static String[] getTimeNames()
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.type.equals(types.TIME) || point.type.equals(types.TIME_SPLIT) || point.type.equals(types.TIME_DURATION))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public static List<DataPoint> getRoomPoints(RaidRoom room)
    {
        List<DataPoint> points = new ArrayList<>();
        for (DataPoint point : values())
        {
            if (point.room == room || point.room.equals(ALL))
            {
                points.add(point);
            }
        }
        return points;
    }

    public boolean isTime()
    {
        return type.equals(types.TIME) || type.equals(types.TIME_DURATION) || type.equals(types.TIME_SPLIT);
    }

    public static String[] getSpecificNames(RaidRoom room)
    {
        ArrayList<String> valuesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.room.equals(room))
            {
                valuesToGather.add(point.name);
            }
        }
        return Arrays.copyOf(valuesToGather.toArray(), valuesToGather.size(), String[].class);
    }

    public enum MenuCategories
    {
        TIME("Time"),
        OTHER("Other");
        public String name;

        MenuCategories(String name)
        {
            this.name = name;
        }
    }

    public static List<String> getMenuNamesByType(RaidRoom room, MenuCategories category)
    {
        List<String> namesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.room.equals(room))
            {
                switch (category)
                {
                    case TIME:
                        if (point.isTime())
                        {
                            namesToGather.add(point.name);
                        }
                        break;
                    case OTHER:
                        if (point.type.equals(types.OTHER_INT))
                        {
                            namesToGather.add(point.name);
                        }
                        break;
                }
            }
        }
        return namesToGather;
    }

    public static ArrayList<String> getTimeNamesByRoom(RaidRoom room)
    {
        ArrayList<String> timesToGather = new ArrayList<>();
        for (DataPoint point : DataPoint.values())
        {
            if (point.room.equals(room) && (point.type.equals(types.TIME) || point.type.equals(types.TIME_DURATION) || point.type.equals(types.TIME_SPLIT)))
            {
                timesToGather.add(point.name);
            }
        }
        return timesToGather;
    }
}
