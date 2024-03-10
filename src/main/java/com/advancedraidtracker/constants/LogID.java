package com.advancedraidtracker.constants;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.advancedraidtracker.constants.TOBRoom.*;

/**
 * Convenience class for the possible keys used to log events. The parameters each of these events should include can be found in
 * RoomData.
 */
@Getter
public enum LogID
{
    ENTERED_TOB(0, true, ANY_TOB, "Entered TOB"),
    PARTY_MEMBERS(1, true, ANY_TOB, "Party Members", "Player1", "Player2", "Player3", "Player4", "Player5"),
    DWH(2, true, ANY_TOB, "DWH Hit", "Player", "Room Tick"),
    BGS(3, true, ANY_TOB, "BGS Hit", "Player", "Damage", "Room Tick"),
    LEFT_TOB(4, true, ANY_TOB, "Left TOB", "Last Room Tick", "Last Room Name"),
    PLAYER_DIED(5, true, ANY_TOB, "Played Died", "Player", "Room Tick"),
    ENTERED_NEW_TOB_REGION(6, true, ANY_TOB, "Entered New TOB Region", "Room (Int)"),
    HAMMER_ATTEMPTED(7, true, ANY_TOB, "DWH Attempted", "Player"),
    DAWN_DROPPED(800, false, VERZIK, "Dawnbringer appeared", "Room Tick"),
    WEBS_STARTED(901, false, VERZIK, "Webs Thrown", "Room Tick"),
    PLAYER_ATTACK(801, false, ANY, "Player Animation", "Player:Room Tick", "Animation:Worn Items ~ Separated", "Spot animations", "Weapon:Interated Index:Interacted ID", "Matched Projectile:Interacted Name", "Room Name"),
    BLOOD_THROWN(9, true, MAIDEN, "Maiden blood thrown"),
    BLOOD_SPAWNED(10, true, MAIDEN, "Blood Spawned"),
    CRAB_LEAK(11, true, MAIDEN, "Crab Leaked", "Position", "Health"),
    MAIDEN_SPAWNED(12, true, MAIDEN, "Spawned"),
    MAIDEN_70S(13, true, MAIDEN, "70s", "Room Tick"),
    MAIDEN_50S(14, true, MAIDEN, "50s", "Room Tick"),
    MAIDEN_30S(15, true, MAIDEN, "30s", "Room Tick"),
    MAIDEN_0HP(16, true, MAIDEN, "0 HP", "Room Tick"),
    MAIDEN_DESPAWNED(17, true, MAIDEN, "Despawned", "Room Tick"),
    MATOMENOS_SPAWNED(18, true, MAIDEN, "Crab Spawned", "Crab Description"),
    MAIDEN_SCUFFED(19, true, MAIDEN, "Scuffed", "Current Proc"),
    BLOAT_STARTED(26, true, BLOAT, "Bloat Started", "Client Tick"),
    BLOAT_SPAWNED(20, true, BLOAT, "Spawned"),
    BLOAT_DOWN(21, true, BLOAT, "Down", "Room Tick"),
    BLOAT_0HP(22, true, BLOAT, "0 HP"),
    BLOAT_DESPAWN(23, true, BLOAT, "Despawned", "Room Tick"),
    BLOAT_HP_1ST_DOWN(24, true, BLOAT, "HP at First Down", "Bloat HP"), //Jagex format, 744 -> 74.4%
    BLOAT_SCYTHE_1ST_WALK(25, true, BLOAT, "First Walk Scythes", "Player", "Room Tick"),

    NYLO_PILLAR_SPAWN(30, true, NYLOCAS, "Pillar Spawn"),
    NYLO_STALL(31, true, NYLOCAS, "Stall", "Wave", "Room Tick", "Nylos Alive"),
    RANGE_SPLIT(32, true, NYLOCAS, "Range Split", "Wave", "Room Tick"),
    MAGE_SPLIT(33, true, NYLOCAS, "Mage Split", "Wave", "Room Tick"),
    MELEE_SPLIT(34, true, NYLOCAS, "Melee Split", "Wave", "Room Tick"),
    LAST_WAVE(35, true, NYLOCAS, "Last Wave", "Room Tick"),
    LAST_DEAD(36, true, NYLOCAS, "Last Dead", "Room Tick"),
    NYLO_WAVE(37, true, NYLOCAS, "Wave", "Wave Number", "Room Tick"),
    BOSS_SPAWN(40, true, NYLOCAS, "Boss Spawn", "Room Tick"),
    MELEE_PHASE(41, true, NYLOCAS, "Melee Phase", "Room Tick"),
    MAGE_PHASE(42, true, NYLOCAS, "Mage Phase", "Room Tick"),
    RANGE_PHASE(43, true, NYLOCAS, "Range Phase", "Room Tick"),
    NYLO_0HP(44, true, NYLOCAS, "0 HP"),
    NYLO_DESPAWNED(45, true, NYLOCAS, "Despawn", "Room Tick"),
    NYLO_PILLAR_DESPAWNED(46, true, NYLOCAS, "Pillar Despawn", "Room Tick"),
    SOTETSEG_STARTED(51, true, SOTETSEG, "Started", "Room Tick"),
    SOTETSEG_FIRST_MAZE_STARTED(52, true, SOTETSEG, "First Maze Start", "Room Tick"),
    SOTETSEG_FIRST_MAZE_ENDED(53, true, SOTETSEG, "First Maze End", "Room Tick"),
    SOTETSEG_SECOND_MAZE_STARTED(54, true, SOTETSEG, "Second Maze Start", "Room Tick"),
    SOTETSEG_SECOND_MAZE_ENDED(55, true, SOTETSEG, "Second Maze End", "Room Tick"),
    SOTETSEG_ENDED(57, true, SOTETSEG, "Room End", "Room Tick"),
    XARPUS_SPAWNED(60, true, XARPUS, "Spawned"),
    XARPUS_STARTED(61, true, XARPUS, "Started"),
    XARPUS_HEAL(62, true, XARPUS, "Heal"),
    XARPUS_SCREECH(63, true, XARPUS, "Screech", "Room Tick"),
    XARPUS_0HP(64, true, XARPUS, "0 HP"),
    XARPUS_DESPAWNED(65, true, XARPUS, "Despawned", "Room Tick"),
    VERZIK_SPAWNED(70, true, VERZIK, "Spawned"),
    VERZIK_P1_START(71, true, VERZIK, "P1 Start"),
    VERZIK_P1_0HP(72, true, VERZIK, "P1 0 HP"),
    VERZIK_P1_DESPAWNED(73, true, VERZIK, "P1 Despawned", "Room Tick"),
    VERZIK_P2_END(74, true, VERZIK, "P2 End", "Room Tick"),
    VERZIK_P3_0HP(75, true, VERZIK, "P2 0 HP"),
    VERZIK_P3_DESPAWNED(76, true, VERZIK, "P3 Despawned", "Room Tick"),
    VERZIK_BOUNCE(77, true, VERZIK, "Bounce", "Player", "Room Tick"),
    VERZIK_CRAB_SPAWNED(78, true, VERZIK, "Crab Spawned", "Room Tick"),
    VERZIK_P2_REDS_PROC(80, true, VERZIK, "Reds Proc", "Room Tick"),

    LATE_START(98, true, ANY_TOB, "Joined Raid After Start", "Room Name"),
    SPECTATE(99, true, ANY_TOB, "Is Spectating"),
    DEPRECATED_1(998, true, ANY_TOB, "DEPRECATED"),
    DEPRECATED_2(999, true, ANY_TOB, "DEPRECATED"),

    BLOAT_HAND(975, false, BLOAT, "Bloat Hand", "Game Object ID", "RegionX", "RegionY", "Room Tick"),
    BLOAT_DIRECTION(976, false, BLOAT, "Bloat Direction on instance creation", "Orientation (Runelite Angle)", "NPC Index"),

    PARTY_COMPLETE(100, true, ANY_TOB, "Party Is Complete"),
    PARTY_INCOMPLETE(101, true, ANY_TOB, "Party Is Not Complete"),
    PARTY_ACCURATE_PREMAIDEN(102, true, ANY_TOB, "Party Is Complete Prior To Maiden"),

    MAIDEN_DINHS_SPEC(111, true, MAIDEN, "Dinhs Spec", "Player", "Tick", "Primary Target:Primary Target HP", "Targets~HP, : Separated", "Targets Below 27hp"), //Player, tick, primary target:primary target hp, targets~hp:,stats:stats
    MAIDEN_DINHS_TARGET(112, true, MAIDEN, "Dinhs Target"), //

    MAIDEN_CHIN_THROWN(113, true, MAIDEN, "Chin Thrown", "Player", "Distance"), //player, distance

    ACCURATE_MAIDEN_START(201, true, MAIDEN, "Accurate Maiden Start"),
    ACCURATE_BLOAT_START(202, true, BLOAT, "Accurate Bloat Start"),
    ACCURATE_NYLO_START(203, true, NYLOCAS, "Accurate Nylo Start"),
    ACCURATE_SOTE_START(204, true, SOTETSEG, "Accurate Sote Start"),
    ACCURATE_XARP_START(205, true, XARPUS, "Accurate Xarpus Start"),
    ACCURATE_VERZIK_START(206, true, VERZIK, "Accurate Verzik Start"),

    ACCURATE_MAIDEN_END(301, true, MAIDEN, "Accurate Maiden End"),
    ACCURATE_BLOAT_END(302, true, BLOAT, "Accurate Bloat End"),
    ACCURATE_NYLO_END(303, true, NYLOCAS, "Accurate Nylo End"),
    ACCURATE_SOTE_END(304, true, SOTETSEG, "Accurate Sote End"),
    ACCURATE_XARP_END(305, true, XARPUS, "Accurate Xarpus End"),
    ACCURATE_VERZIK_END(306, true, VERZIK, "Accurate Verzik End"),
    IS_HARD_MODE(401, true, ANY_TOB, "Is Hard Mode"),
    IS_STORY_MODE(402, true, ANY_TOB, "Is Story Mode"),

    THRALL_ATTACKED(403, false, ANY_TOB, "Thrall Attacked", "Player", "Type"), // player, type

    THRALL_DAMAGED(404, false, ANY_TOB, "Thrall Damaged", "Player", "Damage"), // player, damage

    VENG_WAS_CAST(405, false, ANY_TOB, "Veng Cast", "Target", "Source"), //target, source

    VENG_WAS_PROCCED(406, false, ANY_TOB, "Veng Procced", "Player", "Source of veng", "Damage"), //player, source of veng, damage

    PLAYER_STOOD_IN_THROWN_BLOOD(411, true, MAIDEN, "Player Stood In Thrown Blood", "Player", "Damage", "Ticks blood was alive for"), //player, damage, blood tick
    PLAYER_STOOD_IN_SPAWNED_BLOOD(412, true, MAIDEN, "Player Stood In Spawned Blood", "Player", "Damage"),  //player, damage
    CRAB_HEALED_MAIDEN(413, true, MAIDEN, "Crab Healed Maiden", " Heal Amount"), //damage
    VERZIK_PURPLE_HEAL(701, true, VERZIK, "Purple Heal"), //unimplemented
    VERZIK_RED_AUTO(702, true, VERZIK, "Red Auto"), //unimplemented
    VERZIK_THRALL_HEAL(703, true, VERZIK, "Thrall Heal"), //unimplemented
    VERZIK_PLAYER_HEAL(704, true, VERZIK, "Player Heal"), //unimplemented

    KODAI_BOP(501, true, ANY_TOB, "Kodai Bop", "Player"),
    DWH_BOP(502, true, ANY_TOB, "DWH Bop", "Player"),
    BGS_WHACK(503, true, ANY_TOB, "BGS Whack", "Player"),
    CHALLY_POKE(504, true, ANY_TOB, "Chally Poke", "Players"),
    THRALL_SPAWN(410, false, ANY_TOB, "Thrall Spawn", "Owner", "Room Tick", "Npc ID", "Room"),
    THRALL_DESPAWN(498, false, ANY_TOB, "Thrall Despawn", "Owner", "Room Tick"),
    DAWN_SPEC(487, false, VERZIK, "Dawn Spec", "Player", "Room Tick Damage Applied"),
    DAWN_DAMAGE(488, false, VERZIK, "Dawn Damage", "Damage", "Room Tick"),
    MAIDEN_PLAYER_DRAINED(530, true, MAIDEN, "Player Drained", "Player", "Room Tick"),
    MAIDEN_AUTO(531, true, MAIDEN, "Maiden Auto", "Player targeted", "Room Tick"),

    UPDATE_HP(576, false, ANY_TOB, "Update Boss HP", "HP", "Tick", "Room"), //Hp is in jagex format (744 -> 74.4%)
    ADD_NPC_MAPPING(587, false, ANY_TOB, "Update NPC Mappings", "NPC Index", "Description", "Room"),
    UNKNOWN(-1, false, ANY_TOB, "Unknown"),

    ENTERED_TOA(1000, true, ANY_TOB, "Entered TOA"),
    @Deprecated
    TOA_PARTY_MEMBERS(1001, true, ANY_TOB, "Party Members", "Player1", "Player2", "Player3", "Player4", "Player5", "Player6", "Player7", "Player8"),
    LEFT_TOA(1004, true, ANY_TOB, "Left TOA", "Room Tick", "Last Room"),
    ENTERED_NEW_TOA_REGION(1006, true, ANY_TOB, "Entered New TOA Region", "Region"),
    INVOCATION_LEVEL(1100, true, ANY_TOB, "Invocation Level", "Raid Level"),
    TOA_TIMER_START(1101, true, ANY_TOB, "TOA Timer Start", "Client Tick"),

    TOA_CRONDIS_START(1010, true, CRONDIS, "Crondis Start", "Room Tick"),
    TOA_CRONDIS_FINISHED(1011, true, CRONDIS, "Crondis Finished", "Room Tick"),
    TOA_CRONDIS_WATER(1012, true, CRONDIS, "Crondis Water", "Heal", "Room Tick"),
    TOA_CRONDIS_CROC_DAMAGE(1013, true, CRONDIS, "Crondis Croc Damage", "Damage", "Room Tick"),

    TOA_ZEBAK_START(1020, true, ZEBAK, "Zebak Start", "Room Tick"),
    TOA_ZEBAK_FINISHED(1021, true, ZEBAK, "Zebak Finished", "Room Tick"),
    TOA_ZEBAK_JUG_PUSHED(1022, true, ZEBAK, "Zebak Jug Pushed", "Player", "Room Tick"),
    TOA_ZEBAK_ENRAGED(1023, true, ZEBAK, "Zebak Enraged", "Room Tick"),
    TOA_ZEBAK_BOULDER_ATTACK(1024, true, ZEBAK, "Zebak Boulder Attack", "Room Tick"),
    TOA_ZEBAK_WATERFALL_ATTACK(1025, true, ZEBAK, "Zebak Waterfall Attack", "Room Tick"),

    TOA_SCABARAS_START(1030, true, SCABARAS, "Scabaras Start", "Room Tick"),
    TOA_SCABARAS_FINISHED(1031, true, SCABARAS, "Scabaras End", "Room Tick"),

    TOA_KEPHRI_START(1040, true, KEPHRI, "Kephri Start", "Client Tick"),
    TOA_KEPHRI_PHASE_1_END(1041, true, KEPHRI, "Kephri P1 End", "Room Tick"),
    TOA_KEPHRI_SWARM_1_END(1042, true, KEPHRI, "Kephri Swarm1 End", "Room Tick"),
    TOA_KEPHRI_PHASE_2_END(1043, true, KEPHRI, "Kephri P2 End", "Room Tick"),
    TOA_KEPHRI_SWARM_2_END(1044, true, KEPHRI, "Kephri Swarm2 End", "Room Tick"),
    TOA_KEPHRI_FINISHED(1045, true, KEPHRI, "Kephri Finished", "Room Tick"),
    TOA_KEPHRI_HEAL(1046, true, KEPHRI, "Kephri Swarm Heal", "Room Tick", "Heal"),
    TOA_KEPHRI_SWARM_SPAWN(1047, true, KEPHRI, "Kephri Swarm Spawn", "Room Tick"),
    TOA_KEPHRI_BOMB_TANKED(1048, true, KEPHRI, "Kephri Bomb Tanked", "Player", "Room Tick"),
    TOA_KEPHRI_DUNG_THROWN(1049, true, KEPHRI, "Kephri Dung Thrown", "Room Tick"),
    TOA_KEPHRI_MELEE_ALIVE_TICKS(1350, true, KEPHRI, "Kephri Melee Alive Ticks", "Ticks Alive"),
    TOA_KEPHRI_MELEE_HEAL(1351, true, KEPHRI, "Kephri Melee Heal", "Room Tick"),


    TOA_APMEKEN_START(1050, true, APMEKEN, "Apmeken Start", "Client Tick"),
    TOA_APMEKEN_FINISHED(1051, true, APMEKEN, "Apmeken Finished", "Room Tick"),
    TOA_APMEKEN_VOLATILE_SPAWN(1052, true, APMEKEN, "Volatile Spawn", "Room Tick"),
    TOA_APMEKEN_SHAMAN_SPAWN(1053, true, APMEKEN, "Shaman Spawn", "Room Tick"),
    TOA_APMEKEN_CURSED_SPAWN(1054, true, APMEKEN, "Cursed Spawn", "Room Tick"),

    TOA_BABA_START(1060, true, BABA, "Baba Start", "Client Tick"),
    TOA_BABA_PHASE_1_END(1061, true, BABA, "Baba P1 End", "Room Tick"),
    TOA_BABA_BOULDER_1_END(1062, true, BABA, "Baba Boulder1 End", "Room Tick"),
    TOA_BABA_PHASE_2_END(1063, true, BABA, "Baba P2 End", "Room Tick"),
    TOA_BABA_BOULDER_2_END(1064, true, BABA, "Baba Boulder2 End", "Room Tick"),
    TOA_BABA_FINISHED(1065, true, BABA, "Baba Finished", "Room Tick"),
    TOA_BABA_BOULDER_THROW(1066, true, BABA, "Boulder Throw", "Room Tick"),
    TOA_BABA_BOULDER_BROKEN(1067, true, BABA, "Boulder Broken", "Room Tick"),

    TOA_HET_START(1070, true, HET, "Het Start", "Client Tick"),
    TOA_HET_FINISHED(1071, true, HET, "Het Finished", "Room Tick"),
    TOA_HET_PLAYED_MINED_OBELISK(1072, true, HET, "Het Player Mined Obelisk", "Player", "Room Tick"),
    TOA_HET_DOWN(1073, true, HET, "Het Down", "Room Tick"),

    TOA_AKKHA_START(1080, true, AKKHA, "Akkha Start", "Client Tick"),
    TOA_AKKHA_PHASE_1_END(1081, true, AKKHA, "Akkha P1 End", "Room Tick"),
    TOA_AKKHA_SHADOW_1_END(1082, true, AKKHA, "Akkha Shadow1 End", "Room Tick"),
    TOA_AKKHA_PHASE_2_END(1083, true, AKKHA, "Akkha P2 End", "Room Tick"),
    TOA_AKKHA_SHADOW_2_END(1084, true, AKKHA, "Akkha Shadow2 End", "Room Tick"),
    TOA_AKKHA_PHASE_3_END(1085, true, AKKHA, "Akkha P3 End", "Room Tick"),
    TOA_AKKHA_SHADOW_3_END(1086, true, AKKHA, "Akkha Shadow3 End", "Room Tick"),
    TOA_AKKHA_PHASE_4_END(1087, true, AKKHA, "Akkha P4 End", "Room Tick"),
    TOA_AKKHA_SHADOW_4_END(1088, true, AKKHA, "Akkha Shadow4 End", "Room Tick"),
    TOA_AKKHA_PHASE_5_END(1089, true, AKKHA, "Akkha P5 End", "Room Tick"),
    TOA_AKKHA_FINISHED(1090, true, AKKHA, "Akkha Finished", "Room Tick"),
    TOA_AKKHA_NULLED_HIT_ON_SHADOW(1091, true, AKKHA, "Shadow nulled hit", "Player", "Room Tick", "Weapon"),
    TOA_AKKHA_NULLED_HIT_ON_AKKHA(1092, true, AKKHA, "Akkha nulled hit", "Player", "Room Tick", "Weapon"),

    TOA_WARDENS_START(1200, true, WARDENS, "Wardens Start", "Client Tick"),
    TOA_WARDENS_P1_END(1201, true, WARDENS, "Wardens P1 End", "Room Tick"),
    TOA_WARDENS_P2_END(1202, true, WARDENS, "Wardens P2 End", "Room Tick"),
    TOA_WARDENS_ENRAGED(1203, true, WARDENS, "Wardens Enraged", "Room Tick"),
    TOA_WARDENS_FINISHED(1204, true, WARDENS, "Wardens Finished", "Room Tick"),
    TOA_WARDENS_SKULLS_STARTED(1205, true, WARDENS, "Wardens Skull Started", "Room Tick"),
    TOA_WARDENS_SKULLS_ENDED(1206, true, WARDENS, "Wardens Skulls Ended", "Room Tick"),
    TOA_WARDENS_CORE_SPAWNED(1207, true, WARDENS, "Wardens Core Spawned", "Room Tick"),
    TOA_WARDENS_CORE_DESPAWNED(1208, true, WARDENS, "Wardens Core Despawned", "Room Tick"),


    ;
    /*
    2:DWH //Player, 0, 0, 0, 0
    3:BGS //Player, Damage, 0, 0, 0
    4:Left tob region //Last region, 0, 0, 0, 0
    5:Player died //Player, 0, 0, 0, 0
    6:Entered new tob region //Region, 0, 0, 0, 0 // Regions: (Bloat 1, Nylo 2, Sote 3, Xarpus 4, Verzik 5)
    8: DB Specs // Player, DMG

    10: Blood Spawned //room time, 0, 0, 0, 0
    11: Crab leaked //room time, Description (E.G. N1 30s), Last known health, Current maiden dealth, 0
    12: Maiden spawned //0, 0, 0, 0, 0
    13: Maiden 70s //room time, 0, 0, 0, 0
    14: Maiden 50s //room time, 0, 0, 0, 0
    15: Maiden 30s //room time, 0, 0, 0, 0
    16: Maiden 0 hp //room time, 0, 0, 0, 0
    17: Maiden despawned //room time, 0, 0, 0, 0
    18: Matomenos spawned //position, 0, 0, 0, 0
    19: Maiden Scuffed

    20: Bloat spawned //0, 0, 0, 0, 0
    21: Bloat down //Room time, 0, 0, 0, 0
    22: Bloat 0 HP //room time, 0, 0, 0, 0
    23: Bloat despawn //room time, 0, 0, 0, 0
    24: Bloat HP on 1st down //HP, 0, 0, 0,0

    30: Nylo pillars spawned //0, 0, 0, 0 ,0
    31: Nylo stall //Wave, room time, 0, 0, 0
    32: Range split //Wave, room time, 0, 0, 0
    33: Mage split //Wave, room time, 0, 0, 0
    34: Melee split //Wave, room time, 0, 0, 0
    35: Last wave //Room time, 0, 0, 0, 0
    40: Boss spawn //Room time, 0, 0, 0, 0
    41: Melee rotation //room time, 0, 0, 0, 0
    42: Mage rotation //room time, 0, 0, 0, 0
    43: Range rotation //room time, 0, 0, 0, 0
    44: Nylo 0 HP // room time, 0, 0, 0, 0
    45: Nylo despawned // room time, 0, 0, 0, 0

    5x: sote

    60: xarpus spawned //0, 0, 0, 0, 0
    61: xarpus room started //0, 0, 0, 0, 0
    62: xarpus heal //amount, room time, 0, 0, 0
    63: xarpus screech //room time, 0, 0, 0, 0
    64: xarpus 0 hp //room time, 0, 0, 0, 0
    65: xarpus despawned //room time, 0, 0, 0, 0

    70: verzik spawned //room time, 0, 0, 0, 0
    71: verzik p1 started //0, 0, 0, 0, 0
    72: verzik p1 0 hp //room time, 0, 0, 0, 0
    73: verzik p1 despawned //room time, 0, 0, 0, 0
    80: verzik p2 reds proc // room time, 0, 0, 0, 0
    74: verzik p2 end
    75: verzik p3 0 hp
    76: verzik p3 despawned
    77: verzik bounce //player, room time, 0, 0, 0

    100: party complete
    101: party incomplete
    102: party accurate pre maiden

    201 accurate maiden start
    202 accurate bloat start
    203 accurate nylo start
    204 accurate sote start
    205 accurate xarp start
    206 accurate verzik start

    301 accurate maiden end
    302 accurate bloat end
    303 accurate nylo end
    304 accurate sote end
    305 accurate xarp end
    306 accurate verzik end
    */
    final int id;
    final String commonName;
    final TOBRoom room;
    final boolean simple;

    final String[] valueDescriptors;
    private static final Map<Integer, LogID> mapper;

    static
    {
        mapper = new HashMap<>();
        for (LogID id : values())
        {
            mapper.put(id.id, id);
        }
    }


    LogID(int id, boolean simple, TOBRoom room, String commonName, String... arguments)
    {
        this.id = id;
        this.commonName = commonName;
        this.room = room;
        this.simple = simple;
        this.valueDescriptors = arguments;
    }

    public static boolean isSimple(int value)
    {
        return valueOf(value).simple;
    }

    public static LogID valueOf(int value)
    {
        return mapper.getOrDefault(value, UNKNOWN);
    }
}