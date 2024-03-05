package com.TheatreTracker.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

import static com.TheatreTracker.constants.TOBRoom.*;

/**
 * Convenience class for the possible keys used to log events. The parameters each of these events should include can be found in
 * RoomData.
 */
@Getter
public enum LogID
{
    ENTERED_TOB(0, true, ANY, "Entered TOB"),
    PARTY_MEMBERS(1, true, ANY, "Party Members"),
    DWH(2, true, ANY,"DWH Hit"),
    BGS(3, true, ANY,"BGS Hit"),
    LEFT_TOB(4, true, ANY,"Left TOB"),
    PLAYER_DIED(5, true, ANY,"Played Died"),
    ENTERED_NEW_TOB_REGION(6, true, ANY,"Entered New TOB Region"),
    HAMMER_ATTEMPTED(7, true, ANY,"DWH Attempted"),
    DAWN_DROPPED(800, false, VERZIK,"Dawnbringer appeared"),
    WEBS_STARTED(901, false, VERZIK,"Webs Thrown"),
    PLAYER_ATTACK(801, false, ANY,"Player Animation"),
    BLOOD_THROWN(9, true, MAIDEN,"Maiden blood thrown"),
    BLOOD_SPAWNED(10, true, MAIDEN, "Blood Spawned"),
    CRAB_LEAK(11, true, MAIDEN, "Crab Leaked"),
    MAIDEN_SPAWNED(12, true, MAIDEN, "Spawned"),
    MAIDEN_70S(13, true, MAIDEN, "70s"),
    MAIDEN_50S(14, true, MAIDEN, "50s"),
    MAIDEN_30S(15, true, MAIDEN, "30s"),
    MAIDEN_0HP(16, true, MAIDEN, "0 HP"),
    MAIDEN_DESPAWNED(17, true, MAIDEN, "Despawned"),
    MATOMENOS_SPAWNED(18, true, MAIDEN, "Crab Spawned"),
    MAIDEN_SCUFFED(19, true, MAIDEN, "Scuffed"),
    BLOAT_SPAWNED(20, true, BLOAT, "Spawned"),
    BLOAT_DOWN(21, true, BLOAT, "Down"),
    BLOAT_0HP(22, true, BLOAT, "0 HP"),
    BLOAT_DESPAWN(23, true, BLOAT, "Despawned"),
    BLOAT_HP_1ST_DOWN(24, true, BLOAT, "HP at First Down"),
    BLOAT_SCYTHE_1ST_WALK(25, true, BLOAT, "First Walk Scythes"),

    NYLO_PILLAR_SPAWN(30, true, NYLOCAS, "Pillar Spawn"),
    NYLO_STALL(31, true, NYLOCAS, "Stall"),
    RANGE_SPLIT(32, true, NYLOCAS, "Range Split"),
    MAGE_SPLIT(33, true, NYLOCAS, "Mage Split"),
    MELEE_SPLIT(34, true, NYLOCAS, "Melee Split"),
    LAST_WAVE(35, true, NYLOCAS, "Last Wave"),
    LAST_DEAD(36, true, NYLOCAS, "Last Dead"),
    NYLO_WAVE(37, true, NYLOCAS, "Wave"),
    BOSS_SPAWN(40, true, NYLOCAS, "Boss Spawn"),
    MELEE_PHASE(41, true, NYLOCAS, "Melee Phase"),
    MAGE_PHASE(42, true, NYLOCAS, "Mage Phase"),
    RANGE_PHASE(43, true, NYLOCAS, "Range Phase"),
    NYLO_0HP(44, true, NYLOCAS, "0 HP"),
    NYLO_DESPAWNED(45, true, NYLOCAS, "Despawn"),
    NYLO_PILLAR_DESPAWNED(46, true, NYLOCAS, "Pillar Despawn"), //tick
    SOTETSEG_STARTED(51, true, SOTETSEG, "Started"),
    SOTETSEG_FIRST_MAZE_STARTED(52, true, SOTETSEG, "First Maze Start"),
    SOTETSEG_FIRST_MAZE_ENDED(53, true, SOTETSEG, "First Maze End"),
    SOTETSEG_SECOND_MAZE_STARTED(54, true, SOTETSEG, "Second Maze Start"),
    SOTETSEG_SECOND_MAZE_ENDED(55, true, SOTETSEG, "Second Maze End"),
    SOTETSEG_ENDED(57, true, SOTETSEG, "Room End"),
    XARPUS_SPAWNED(60, true, XARPUS, "Spawned"),
    XARPUS_STARTED(61, true, XARPUS, "Started"),
    XARPUS_HEAL(62, true, XARPUS, "Heal"),
    XARPUS_SCREECH(63, true, XARPUS, "Screech"),
    XARPUS_0HP(64, true, XARPUS, "0 HP"),
    XARPUS_DESPAWNED(65, true, XARPUS, "Despawned"),
    VERZIK_SPAWNED(70, true, VERZIK, "Spawned"),
    VERZIK_P1_START(71, true, VERZIK, "P1 Start"),
    VERZIK_P1_0HP(72, true, VERZIK, "P1 0 HP"),
    VERZIK_P1_DESPAWNED(73, true, VERZIK, "P1 Despawned"),
    VERZIK_P2_END(74, true, VERZIK, "P2 End"),
    VERZIK_P3_0HP(75, true, VERZIK, "P2 0 HP"),
    VERZIK_P3_DESPAWNED(76, true, VERZIK, "P3 Despawned"),
    VERZIK_BOUNCE(77, true, VERZIK, "Bounce"),
    VERZIK_CRAB_SPAWNED(78, true, VERZIK, "Crab Spawned"),
    VERZIK_P2_REDS_PROC(80, true, VERZIK, "Reds Proc"),

    LATE_START(98, true, ANY, "Joined Raid After Start"),
    SPECTATE(99, true, ANY, "Is Spectating"),
    NOT_118(998, true, ANY, "Not 118"),
    NO_PIETY(999, true, ANY, "No Piety"),
    RANDOM_TRACKER(1000, true, ANY, "Random Tracker"),
    RANDOM_TRACKER_2(1001, true, ANY, "Random Tracker2"),
    BLOAT_HAND(975, false, BLOAT, "Bloat Hand"),
    BLOAT_DIRECTION(976, false, BLOAT, "Bloat Direction"),

    PARTY_COMPLETE(100, true, ANY, "Party Is Complete"),
    PARTY_INCOMPLETE(101, true, ANY, "Party Is Not Complete"),
    PARTY_ACCURATE_PREMAIDEN(102, true, ANY, "Party Is Complete Prior To Maiden"),

    MAIDEN_DINHS_SPEC(111, true, MAIDEN, "Dinhs Spec"), //Player, tick, primary target:primary target hp, targets~hp:,stats:stats
    MAIDEN_DINHS_TARGET(112, true, MAIDEN, "Dinhs Target"), //

    MAIDEN_CHIN_THROWN(113, true, MAIDEN, "Chin Thrown"), //player, distance

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
    IS_HARD_MODE(401, true, ANY, "Is Hard Mode"),
    IS_STORY_MODE(402, true, ANY, "Is Story Mode"),

    THRALL_ATTACKED(403, false, ANY, "Thrall Attacked"), // player, type

    THRALL_DAMAGED(404, false, ANY, "Thrall Damaged"), // player, damage

    VENG_WAS_CAST(405, false, ANY, "Veng Cast"), //target, source

    VENG_WAS_PROCCED(406, false, ANY, "Veng Procced"), //player, source of veng, damage

    PLAYER_STOOD_IN_THROWN_BLOOD(411, true, MAIDEN, "Player Stood In Thrown Blood"), //player, damage, blood tick
    PLAYER_STOOD_IN_SPAWNED_BLOOD(412, true, MAIDEN, "Player Stood In Spawned Blood"),  //player, damage
    CRAB_HEALED_MAIDEN(413, true, MAIDEN, "Crab Healed Maiden"), //damage
    VERZIK_PURPLE_HEAL(701, true, VERZIK, "Purple Heal"),
    VERZIK_RED_AUTO(702, true, VERZIK, "Red Auto"),
    VERZIK_THRALL_HEAL(703, true, VERZIK, "Thrall Heal"),
    VERZIK_PLAYER_HEAL(704, true, VERZIK, "Player Heal"),

    KODAI_BOP(501, true, ANY, "Kodai Bop"),
    DWH_BOP(502, true, ANY, "DWH Bop"),
    BGS_WHACK(503, true, ANY, "BGS Whack"),
    CHALLY_POKE(504, true, ANY, "Chally Poke"),
    THRALL_SPAWN(410, false, ANY, "Thrall Spawn"),
    THRALL_DESPAWN(498, false, ANY, "Thrall Despawn"),
    DAWN_SPEC(487, false, VERZIK, "Dawn Spec"),
    DAWN_DAMAGE(488, false, VERZIK, "Dawn Damage"),
    MAIDEN_PLAYER_DRAINED(530, true, MAIDEN, "Player Drained"),
    MAIDEN_AUTO(531, true, MAIDEN, "Maiden Auto"),

    UPDATE_HP(576, false, ANY, "Update Boss HP"),
    ADD_NPC_MAPPING(587, false, ANY, "Update NPC Mappings"),
    UNKNOWN(-1, false, ANY, "Unknown");

    final int id;
    final String commonName;
    final TOBRoom room;
    final boolean simple;

    LogID(int id, boolean simple, TOBRoom room, String commonName)
    {
        this.id = id;
        this.commonName = commonName;
        this.room = room;
        this.simple = simple;
    }
    public static LogID valueOf(int value)
    {
        Optional<LogID> o = Arrays.stream(values()).filter(logid -> logid.getId() == value).findFirst();
        return o.orElse(UNKNOWN);
    }
}
