package com.advancedraidtracker.constants;

import com.advancedraidtracker.utility.datautility.DataPoint;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.advancedraidtracker.constants.ParseType.*;
import static com.advancedraidtracker.constants.RaidRoom.*;
import static com.advancedraidtracker.utility.datautility.DataPoint.*;

/**
 * Convenience class for the possible keys used to log events. The parameters each of these events should include can be found in
 * RoomData.
 */
@Getter
public enum LogID
{
    ENTERED_RAID(0, true, ANY,
            new ParseInstruction(AGNOSTIC),
            "Entered Raid"),
    PARTY_MEMBERS(1, true, ANY,
            new ParseInstruction(AGNOSTIC),
            "Party Members", "Player1", "Player2", "Player3", "Player4", "Player5", "Player6", "Player7", "Player8"),
    HAMMER_HIT(2, true, ANY,
            new ParseInstruction(INCREMENT, PLAYER_HAMMER_HIT_COUNT),
            new ParseInstruction(DWH, DEFENSE),
            "Hammer Hit", "Player", "Room Tick"),
    BGS_HIT(3, true, ANY,
            new ParseInstruction(INCREMENT, PLAYER_BGS_HIT_COUNT),
            new ParseInstruction(ADD_TO_VALUE, PLAYER_BGS_DAMAGE),
            new ParseInstruction(BGS, DEFENSE),
            "BGS Hit", "Player", "Damage", "Room Tick"),
    LEFT_TOB(4, true, ANY,
            new ParseInstruction(LEFT_RAID),
            "Left TOB", "Last Room Tick", "Last Room Name"),
    PLAYER_DIED(5, true, ANY,
            new ParseInstruction(INCREMENT, DEATHS),
            new ParseInstruction(MANUAL_PARSE),
            "Played Died", "Player", "Room Tick"),
    ENTERED_NEW_TOB_REGION(6, true, ANY_TOB,
            new ParseInstruction(RAID_SPECIFIC),
            "Entered New TOB Region", "Room (Int)"),
    HAMMER_ATTEMPTED(7, true, ANY,
            new ParseInstruction(INCREMENT, PLAYER_HAMMER_ATTEMPTED),
            "DWH Attempted", "Player"),
    DAWN_DROPPED(800, true, VERZIK,
            new ParseInstruction(MAP, DAWN_DROPS),
            "Dawnbringer appeared", "Room Tick"),
    WEBS_STARTED(901, true, VERZIK,
            new ParseInstruction(MAP, WEBS_THROWN),
            "Webs Thrown", "Room Tick"),
    PLAYER_ATTACK(801, false, ANY,
            new ParseInstruction(MANUAL_PARSE),
            "Player Animation", "Player:Room Tick", "Animation:Worn Items ~ Separated", "Spot animations", "Weapon:Interated Index:Interacted ID", "Matched Projectile:Interacted Name", "Room Name"),
    BLOOD_THROWN(9, true, MAIDEN,
            new ParseInstruction(INCREMENT, MAIDEN_BLOOD_THROWN),
            "Maiden blood thrown"),
    BLOOD_SPAWNED(10, true, MAIDEN,
            new ParseInstruction(INCREMENT, MAIDEN_BLOOD_SPAWNED),
            "Blood Spawned"),
    CRAB_LEAK(11, true, MAIDEN,
            new ParseInstruction(MANUAL_PARSE),
            "Crab Leaked", "Position", "Health"),
    MAIDEN_SPAWNED(12, true, MAIDEN,
            new ParseInstruction(ROOM_START_FLAG),
            "Spawned"),
    MAIDEN_70S(13, true, MAIDEN,
            new ParseInstruction(SET, MAIDEN_70_SPLIT),
            "70s", "Room Tick"),
    MAIDEN_50S(14, true, MAIDEN,
            new ParseInstruction(SPLIT, MAIDEN_50_SPLIT, MAIDEN_7050_DURATION, MAIDEN_70_SPLIT),
            "50s", "Room Tick"),
    MAIDEN_30S(15, true, MAIDEN,
            new ParseInstruction(SPLIT, MAIDEN_30_SPLIT, MAIDEN_5030_SPLIT, MAIDEN_50_SPLIT),
            "30s", "Room Tick"),
    MAIDEN_DESPAWNED(17, true, MAIDEN,
            new ParseInstruction(SPLIT, MAIDEN_TIME, MAIDEN_SKIP_SPLIT, MAIDEN_30_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(ROOM_END_FLAG),
            "Despawned", "Room Tick"),
    MATOMENOS_SPAWNED(18, true, MAIDEN,
            new ParseInstruction(MANUAL_PARSE),
            "Crab Spawned", "Crab Description"),
    MAIDEN_SCUFFED(19, true, MAIDEN,
            new ParseInstruction(MANUAL_PARSE),
            "Scuffed", "Current Proc"),
    BLOAT_SPAWNED(20, true, BLOAT,
            new ParseInstruction(ROOM_START_FLAG),
            "Spawned"),
    BLOAT_DOWN(21, true, BLOAT,
            new ParseInstruction(MAP, BLOAT_DOWNS),
            new ParseInstruction(MANUAL_PARSE),
            "Down", "Room Tick"),
    BLOAT_DESPAWN(23, true, BLOAT,
            new ParseInstruction(SET, BLOAT_TIME),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(ROOM_END_FLAG),
            new ParseInstruction(MANUAL_PARSE),
            "Despawned", "Room Tick"),
    BLOAT_HP_1ST_DOWN(24, true, BLOAT,
            new ParseInstruction(MANUAL_PARSE),
            "HP at First Down", "Bloat HP"),
    BLOAT_SCYTHE_1ST_WALK(25, true, BLOAT, //todo: note this is improperly named, it appears to be all scythes? investigate.
            new ParseInstruction(MANUAL_PARSE),
            "First Walk Scythes", "Player", "Room Tick"),
    NYLO_PILLAR_SPAWN(30, true, NYLOCAS,
            new ParseInstruction(ROOM_START_FLAG),
            "Pillar Spawn"),
    NYLO_STALL(31, true, NYLOCAS,
            new ParseInstruction(MAP, NYLO_STALLS_TOTAL),
            new ParseInstruction(INCREMENT_IF_GREATER_THAN, NYLO_STALLS_POST_20, "Wave", 19),
            new ParseInstruction(INCREMENT_IF_LESS_THAN, NYLO_STALLS_PRE_20, "Wave", 20),
            "Stall", "Wave", "Room Tick", "Nylos Alive"),
    RANGE_SPLIT(32, true, NYLOCAS,
            new ParseInstruction(INCREMENT, NYLO_SPLITS_RANGE),
            "Range Split", "Wave", "Room Tick"),
    MAGE_SPLIT(33, true, NYLOCAS,
            new ParseInstruction(INCREMENT, NYLO_SPLITS_MAGE),
            "Mage Split", "Wave", "Room Tick"),
    MELEE_SPLIT(34, true, NYLOCAS,
            new ParseInstruction(INCREMENT, NYLO_SPLITS_MELEE),
            "Melee Split", "Wave", "Room Tick"),
    LAST_WAVE(35, true, NYLOCAS,
            new ParseInstruction(SET, NYLO_LAST_WAVE),
            "Last Wave", "Room Tick"),
    LAST_DEAD(36, true, NYLOCAS,
            new ParseInstruction(SPLIT, NYLO_LAST_DEAD, NYLO_CLEANUP, NYLO_LAST_WAVE),
            "Last Dead", "Room Tick"),
    NYLO_WAVE(37, true, NYLOCAS,
            new ParseInstruction(MAP, NYLO_WAVES),
            "Wave", "Wave Number", "Room Tick"),
    BOSS_SPAWN(40, true, NYLOCAS,
            new ParseInstruction(SPLIT, NYLO_BOSS_SPAWN, NYLO_CLEANUP, NYLO_LAST_WAVE, -2),
            "Boss Spawn", "Room Tick"),
    MELEE_PHASE(41, true, NYLOCAS,
            new ParseInstruction(INCREMENT, NYLO_ROTATIONS_TOTAL),
            new ParseInstruction(INCREMENT, NYLO_ROTATIONS_MELEE),
            "Melee Phase", "Room Tick"),
    MAGE_PHASE(42, true, NYLOCAS,
            new ParseInstruction(INCREMENT, NYLO_ROTATIONS_TOTAL),
            new ParseInstruction(INCREMENT, NYLO_ROTATIONS_MAGE),
            "Mage Phase", "Room Tick"),
    RANGE_PHASE(43, true, NYLOCAS,
            new ParseInstruction(INCREMENT, NYLO_ROTATIONS_TOTAL),
            new ParseInstruction(INCREMENT, NYLO_ROTATIONS_RANGE),
            "Range Phase", "Room Tick"),
    NYLO_DESPAWNED(45, true, NYLOCAS,
            new ParseInstruction(SPLIT, NYLOCAS_TIME, NYLO_BOSS_DURATION, NYLO_BOSS_SPAWN),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(ROOM_END_FLAG),
            "Despawn", "Room Tick"),
    //POSSIBLY_DEPRECATED_NYLO_PILLAR_DESPAWNED(46, true, NYLOCAS, "Pillar Despawn", "Room Tick"),
    SOTETSEG_STARTED(51, true, SOTETSEG,
            new ParseInstruction(ROOM_START_FLAG),
            "Started"),
    SOTETSEG_FIRST_MAZE_STARTED(52, true, SOTETSEG,
            new ParseInstruction(SET, SOTE_M1_SPLIT),
            "First Maze Start", "Room Tick"),
    SOTETSEG_FIRST_MAZE_ENDED(53, true, SOTETSEG,
            new ParseInstruction(SPLIT, SOTE_P2_SPLIT, SOTE_M1_DURATION, SOTE_M1_SPLIT),
            "First Maze End", "Room Tick"),
    SOTETSEG_SECOND_MAZE_STARTED(54, true, SOTETSEG,
            new ParseInstruction(SPLIT, SOTE_M2_SPLIT, SOTE_P2_DURATION, SOTE_P2_SPLIT),
            "Second Maze Start", "Room Tick"),
    SOTETSEG_SECOND_MAZE_ENDED(55, true, SOTETSEG,
            new ParseInstruction(SPLIT, SOTE_P3_SPLIT, SOTE_M2_DURATION, SOTE_M2_SPLIT),
            new ParseInstruction(SUM, SOTE_MAZE_SUM, SOTE_M1_DURATION, SOTE_M2_DURATION),
            "Second Maze End", "Room Tick"),
    SOTETSEG_ENDED(57, true, SOTETSEG,
            new ParseInstruction(SPLIT, SOTETSEG_TIME, SOTE_P3_DURATION, SOTE_P3_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(ROOM_END_FLAG),
            "Room End", "Room Tick"),
    XARPUS_STARTED(61, true, XARPUS,
            new ParseInstruction(ROOM_START_FLAG),
            "Started"),
    XARPUS_HEAL(62, true, XARPUS,
            new ParseInstruction(MANUAL_PARSE),
            "Heal"),
    XARPUS_SCREECH(63, true, XARPUS,
            new ParseInstruction(SET, XARP_SCREECH),
            "Screech", "Room Tick"),
    XARPUS_DESPAWNED(65, true, XARPUS,
            new ParseInstruction(SPLIT, XARPUS_TIME, XARP_POST_SCREECH, XARP_SCREECH),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(ROOM_END_FLAG),
            "Despawned", "Room Tick"),
    //DEPRECATED_VERZIK_SPAWNED(70, true, VERZIK, "Spawned"),
    VERZIK_P1_START(71, true, VERZIK,
            new ParseInstruction(ROOM_START_FLAG),
            "P1 Start"),
    VERZIK_P1_DESPAWNED(73, true, VERZIK,
            new ParseInstruction(SET, VERZIK_P2_SPLIT, -13),
            "P1 Despawned", "Room Tick"),
    VERZIK_P2_END(74, true, VERZIK,
            new ParseInstruction(SPLIT, VERZIK_P3_SPLIT, VERZIK_P2_DURATION, VERZIK_P2_SPLIT),
            "P2 End", "Room Tick"),
    VERZIK_P3_DESPAWNED(76, true, VERZIK,
            new ParseInstruction(SPLIT, VERZIK_TIME, VERZIK_P3_DURATION, VERZIK_P3_SPLIT),
            new ParseInstruction(SPLIT, VERZIK_TIME, VERZIK_REDS_DURATION, VERZIK_REDS_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(ROOM_END_FLAG),
            "P3 Despawned", "Room Tick"),
    VERZIK_BOUNCE(77, true, VERZIK,
            new ParseInstruction(MAP, VERZIK_BOUNCES),
            VERZIK_BOUNCES,
            "Bounce", "Player", "Room Tick"),
    VERZIK_CRAB_SPAWNED(78, true, VERZIK,
            new ParseInstruction(INCREMENT_IF_GREATER_THAN, VERZIK_P3_CRABS_SPAWNED, VERZIK_P2_DURATION, 1),
            new ParseInstruction(INCREMENT_IF_LESS_THAN, VERZIK_P2_CRABS_SPAWNED, VERZIK_P2_DURATION, 2),
            new ParseInstruction(MAP, VERZIK_CRABS_SPAWNED),
            "Crab Spawned", "Room Tick"),
    VERZIK_P2_REDS_PROC(80, true, VERZIK,
            new ParseInstruction(MAP, VERZIK_REDS_SETS),
            new ParseInstruction(MANUAL_PARSE),
            "Reds Proc", "Room Tick"),
    LATE_START(98, true, ANY_TOB,
            new ParseInstruction(RAID_SPECIFIC),
            "Joined Raid After Start", "Room Name"),
    SPECTATE(99, true, ANY_TOB,
            new ParseInstruction(AGNOSTIC), //todo make this raid specific
            "Is Spectating"),
    //DEPRECATED_1(998, true, ANY_TOB, "DEPRECATED"),
    //DEPRECATED_2(999, true, ANY_TOB, "DEPRECATED"),

    //DEPRECATED_BLOAT_HAND(975, false, BLOAT, "Bloat Hand", "Game Object ID", "RegionX", "RegionY", "Room Tick"),
    //DEPRECATED_BLOAT_DIRECTION(976, false, BLOAT, "Bloat Direction on instance creation", "Orientation (Runelite Angle)", "NPC Index"),

    PARTY_COMPLETE(100, true, ANY_TOB, //todo
            new ParseInstruction(AGNOSTIC),
            "Party Is Complete"),
    PARTY_INCOMPLETE(101, true, ANY_TOB, //todo
            new ParseInstruction(AGNOSTIC),
            "Party Is Not Complete"),
    PARTY_ACCURATE_PREMAIDEN(102, true, ANY_TOB, //todo
            new ParseInstruction(AGNOSTIC),
            "Party Is Complete Prior To Maiden"),

    MAIDEN_DINHS_SPEC(111, true, MAIDEN,
            new ParseInstruction(MANUAL_PARSE),
            "Dinhs Spec", "Player", "Primary Target:Primary Target HP", "Targets~HP : Separated", "Targets Below 27hp"), //Player, tick, primary target:primary target hp, targets~hp:,stats:stats
    MAIDEN_DINHS_TARGET(112, true, MAIDEN,
            new ParseInstruction(MANUAL_PARSE),
            "Dinhs Target"), //

    MAIDEN_CHIN_THROWN(113, true, MAIDEN,
            new ParseInstruction(INCREMENT_IF_GREATER_THAN, MAIDEN_CHINS_THROWN_WRONG_DISTANCE, "Distance", 6),
            new ParseInstruction(INCREMENT_IF_LESS_THAN, MAIDEN_CHINS_THROWN_WRONG_DISTANCE, "Distance", 4),
            new ParseInstruction(INCREMENT, MAIDEN_CHINS_THROWN),
            "Chin Thrown", "Player", "Distance"), //player, distance

    ACCURATE_MAIDEN_START(201, true, MAIDEN,
            new ParseInstruction(ACCURATE_START),
            "Accurate Maiden Start"),
    ACCURATE_BLOAT_START(202, true, BLOAT,
            new ParseInstruction(ACCURATE_START),
            "Accurate Bloat Start"),
    ACCURATE_NYLO_START(203, true, NYLOCAS,
            new ParseInstruction(ACCURATE_START),
            "Accurate Nylo Start"),
    ACCURATE_SOTE_START(204, true, SOTETSEG,
            new ParseInstruction(ACCURATE_START),
            "Accurate Sote Start"),
    ACCURATE_XARP_START(205, true, XARPUS,
            new ParseInstruction(ACCURATE_START),
            "Accurate Xarpus Start"),
    ACCURATE_VERZIK_START(206, true, VERZIK,
            new ParseInstruction(ACCURATE_START),
            "Accurate Verzik Start"),

    ACCURATE_MAIDEN_END(301, true, MAIDEN,
            new ParseInstruction(ACCURATE_END),
            "Accurate Maiden End"),
    ACCURATE_BLOAT_END(302, true, BLOAT,
            new ParseInstruction(ACCURATE_END),
            "Accurate Bloat End"),
    ACCURATE_NYLO_END(303, true, NYLOCAS,
            new ParseInstruction(ACCURATE_END),
            "Accurate Nylo End"),
    ACCURATE_SOTE_END(304, true, SOTETSEG,
            new ParseInstruction(ACCURATE_END),
            "Accurate Sote End"),
    ACCURATE_XARP_END(305, true, XARPUS,
            new ParseInstruction(ACCURATE_END),
            "Accurate Xarpus End"),
    ACCURATE_VERZIK_END(306, true, VERZIK,
            new ParseInstruction(ACCURATE_END),
            "Accurate Verzik End"),
    IS_HARD_MODE(401, true, ALL,
            new ParseInstruction(AGNOSTIC),
            "Is Hard Mode"),
    IS_STORY_MODE(402, true, ALL,
            new ParseInstruction(AGNOSTIC),
            "Is Story Mode"),
    THRALL_ATTACKED(403, false, ALL,
            new ParseInstruction(INCREMENT, THRALL_ATTACKS),
            "Thrall Attacked", "Player", "Type"), // player, type
    THRALL_DAMAGED(404, false, ALL,
            new ParseInstruction(INCREMENT, THRALL_DAMAGE),
            "Thrall Damaged", "Player", "Damage"), // player, damage
    VENG_WAS_CAST(405, false, ALL,
            new ParseInstruction(INCREMENT, VENG_CASTS),
            "Veng Cast", "Target", "Player"), //target, source
    VENG_WAS_PROCCED(406, false, ALL,
            new ParseInstruction(INCREMENT, VENG_PROCS),
            "Veng Procced", "Player", "Damage"), //player, source of veng, damage
    PLAYER_STOOD_IN_THROWN_BLOOD(411, true, MAIDEN,
            new ParseInstruction(INCREMENT, MAIDEN_PLAYER_STOOD_IN_THROWN_BLOOD),
                        new ParseInstruction(ADD_TO_VALUE, MAIDEN_HEALS_FROM_THROWN_BLOOD),
                        new ParseInstruction(ADD_TO_VALUE, MAIDEN_HP_HEALED),
            "Player Stood In Thrown Blood", "Player", "Damage", "Ticks blood was alive for"), //player, damage, blood tick
    PLAYER_STOOD_IN_SPAWNED_BLOOD(412, true, MAIDEN,
            new ParseInstruction(INCREMENT, MAIDEN_PLAYER_STOOD_IN_SPAWNED_BLOOD),
                        new ParseInstruction(ADD_TO_VALUE, MAIDEN_HEALS_FROM_ANY_BLOOD),
                        new ParseInstruction(ADD_TO_VALUE, MAIDEN_HP_HEALED),
            "Player Stood In Spawned Blood", "Player", "Damage"),  //player, damage
    CRAB_HEALED_MAIDEN(413, true, MAIDEN,
            new ParseInstruction(INCREMENT, MAIDEN_CRABS_LEAKED),
            new ParseInstruction(ADD_TO_VALUE, MAIDEN_HP_HEALED),
            new ParseInstruction(ADD_TO_VALUE, MAIDEN_HP_HEALED), //repeated because heal is 2*hp
            new ParseInstruction(INCREMENT_IF_GREATER_THAN, MAIDEN_CRABS_LEAKED_FULL_HP, "Damage", 87), //todo fix for scales
            "Crab Healed Maiden", "Damage"), //damage
    /*VERZIK_PURPLE_HEAL(701, true, VERZIK, "Purple Heal"), //unimplemented
    VERZIK_RED_AUTO(702, true, VERZIK, "Red Auto"), //unimplemented
    VERZIK_THRALL_HEAL(703, true, VERZIK, "Thrall Heal"), //unimplemented
    VERZIK_PLAYER_HEAL(704, true, VERZIK, "Player Heal"), //unimplemented*/
    KODAI_BOP(501, true, ANY,
            new ParseInstruction(INCREMENT, KODAI_BOPS),
            "Kodai Bop", "Player"),
    DWH_BOP(502, true, ANY_TOB,
            new ParseInstruction(INCREMENT, DWH_BOPS),
            "DWH Bop", "Player"),
    BGS_WHACK(503, true, ANY_TOB,
            new ParseInstruction(INCREMENT, BGS_WHACKS),
            "BGS Whack", "Player"),
    CHALLY_POKE(504, true, ANY_TOB,
            new ParseInstruction(INCREMENT, DataPoint.CHALLY_POKE),
            "Chally Poke", "Players"),
    THRALL_SPAWN(410, false, ANY_TOB,
            new ParseInstruction(MAP, THRALL_SUMMONS),
            "Thrall Spawn", "Player", "Room Tick", "Npc ID", "Room Name"),
    THRALL_DESPAWN(498, false, ANY_TOB,
            new ParseInstruction(MAP, THRALL_DESPAWNS),
            "Thrall Despawn", "Player", "Room Tick"),
    DAWN_SPEC(487, false, VERZIK,
            new ParseInstruction(MANUAL_PARSE), //todo revisit this and thrall above
            "Dawn Spec", "Player", "Room Tick Damage Applied"),
    DAWN_DAMAGE(488, false, VERZIK,
            new ParseInstruction(MANUAL_PARSE), //todo revisit this and thrall above
            "Dawn Damage", "Damage", "Room Tick"),
    MAIDEN_PLAYER_DRAINED(530, true, MAIDEN,
            new ParseInstruction(INCREMENT, MAIDEN_MELEE_DRAINS),
            "Player Drained", "Player", "Room Tick"),
    MAIDEN_AUTO(531, true, MAIDEN,
            new ParseInstruction(MANUAL_PARSE), //todo
            "Maiden Auto", "Player targeted", "Room Tick"),
    UPDATE_HP(576, false, ANY_TOB,
            new ParseInstruction(MANUAL_PARSE),
            "Update Boss HP", "HP", "Tick", "Room"), //Hp is in jagex format (744 -> 74.4%)
    ADD_NPC_MAPPING(587, false, ANY_TOB,
            new ParseInstruction(MANUAL_PARSE),
            "Update NPC Mappings", "NPC Index", "Description", "Room"),

    UNKNOWN(-1, false, ANY_TOB,
            new ParseInstruction(MANUAL_PARSE),
            "Unknown"),
    @Deprecated
    ENTERED_TOA(1000, true, ANY_TOA,
            new ParseInstruction(AGNOSTIC),
            "Entered TOA"),
    @Deprecated
    TOA_PARTY_MEMBERS(1001, true, ANY_TOA,
            new ParseInstruction(AGNOSTIC),
            "Party Members", "Player1", "Player2", "Player3", "Player4", "Player5", "Player6", "Player7", "Player8"),
    @Deprecated
    LEFT_TOA(1004, true, ANY_TOA,
            new ParseInstruction(LEFT_RAID),
            "Left TOA", "Room Tick", "Last Room"),
    ENTERED_NEW_TOA_REGION(1006, true, ANY_TOA,
            new ParseInstruction(RAID_SPECIFIC),
             "Entered New TOA Region", "Region"),
    INVOCATION_LEVEL(1100, true, ANY_TOA,
            new ParseInstruction(SET, TOA_INVOCATION_LEVEL),
            "Invocation Level", "Raid Level"),
    RAID_TIMER_START(1101, true, ANY,
            new ParseInstruction(RAID_SPECIFIC),
            "Raid Timer Start", "Client Tick"),
    TOA_CRONDIS_START(1010, true, CRONDIS,
            new ParseInstruction(ROOM_START_FLAG),
            "Crondis Start", "Room Tick"),
    TOA_CRONDIS_FINISHED(1011, true, CRONDIS,
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(SET, CRONDIS_TIME),
            new ParseInstruction(ROOM_END_FLAG),
            "Crondis Finished", "Room Tick"),
    TOA_CRONDIS_WATER(1012, true, CRONDIS,
            new ParseInstruction(INCREMENT_IF_EQUALS, CRONDIS_HEALS_100, 100),
            new ParseInstruction(INCREMENT_IF_EQUALS, CRONDIS_HEALS_50, 50),
            new ParseInstruction(INCREMENT_IF_EQUALS, CRONDIS_HEALS_25, 25),
            "Crondis Water", "Damage", "Room Tick"),
    TOA_CRONDIS_CROC_DAMAGE(1013, true, CRONDIS,
            new ParseInstruction(ADD_TO_VALUE, CRONDIS_CROCODILE_DAMAGE),
            "Crondis Croc Damage", "Damage", "Room Tick"),
    TOA_ZEBAK_START(1020, true, ZEBAK,
            new ParseInstruction(ROOM_START_FLAG),
            "Zebak Start", "Room Tick"),
    TOA_ZEBAK_FINISHED(1021, true, ZEBAK,
            new ParseInstruction(SPLIT, ZEBAK_TIME, ZEBAK_ENRAGED_DURATION, ZEBAK_ENRAGED_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(ROOM_END_FLAG),
            "Zebak Finished", "Room Tick"),
    TOA_ZEBAK_JUG_PUSHED(1022, true, ZEBAK,
            new ParseInstruction(INCREMENT, ZEBAK_JUGS_PUSHED),
            "Zebak Jug Pushed", "Player", "Room Tick"),
    TOA_ZEBAK_ENRAGED(1023, true, ZEBAK,
            new ParseInstruction(SET, ZEBAK_ENRAGED_SPLIT),
            "Zebak Enraged", "Room Tick"),
    TOA_ZEBAK_BOULDER_ATTACK(1024, true, ZEBAK,
            new ParseInstruction(MAP, ZEBAK_BOULDER_ATTACKS),
            "Zebak Boulder Attack", "Room Tick"),
    TOA_ZEBAK_WATERFALL_ATTACK(1025, true, ZEBAK,
            new ParseInstruction(MAP, ZEBAK_WATERFALL_ATTACKS),
            "Zebak Waterfall Attack", "Room Tick"),
    TOA_SCABARAS_START(1030, true, SCABARAS,
            new ParseInstruction(ROOM_START_FLAG),
            "Scabaras Start", "Room Tick"),
    TOA_SCABARAS_FINISHED(1031, true, SCABARAS,
            new ParseInstruction(SET, SCABARAS_TIME),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(ROOM_END_FLAG),
            "Scabaras End", "Room Tick"),
    TOA_KEPHRI_START(1040, true, KEPHRI,
            new ParseInstruction(ROOM_START_FLAG),
            "Kephri Start", "Client Tick"),
    TOA_KEPHRI_PHASE_1_END(1041, true, KEPHRI,
            new ParseInstruction(SET, KEPHRI_P1_DURATION),
            "Kephri P1 End", "Room Tick"),
    TOA_KEPHRI_SWARM_1_END(1042, true, KEPHRI,
            new ParseInstruction(SPLIT, KEPHRI_P2_SPLIT, KEPHRI_SWARM1_DURATION, KEPHRI_P1_DURATION),
            "Kephri Swarm1 End", "Room Tick"),
    TOA_KEPHRI_PHASE_2_END(1043, true, KEPHRI,
            new ParseInstruction(SPLIT, KEPHRI_SWARM2_SPLIT, KEPHRI_P2_DURATION, KEPHRI_P2_SPLIT),
            "Kephri P2 End", "Room Tick"),
    TOA_KEPHRI_SWARM_2_END(1044, true, KEPHRI,
            new ParseInstruction(SPLIT, KEPHRI_P3_SPLIT, KEPHRI_SWARM2_DURATION, KEPHRI_SWARM2_SPLIT),
            "Kephri Swarm2 End", "Room Tick"),
    TOA_KEPHRI_FINISHED(1045, true, KEPHRI,
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(SPLIT, KEPHRI_TIME, KEPHRI_P3_DURATION, KEPHRI_P3_SPLIT),
            new ParseInstruction(ROOM_END_FLAG),
            "Kephri Finished", "Room Tick"),
    TOA_KEPHRI_HEAL(1046, true, KEPHRI,
            new ParseInstruction(INCREMENT, KEPHRI_SWARMS_HEALED),
            "Kephri Swarm Heal", "Room Tick", "Heal"),
    TOA_KEPHRI_SWARM_SPAWN(1047, true, KEPHRI,
            new ParseInstruction(INCREMENT, KEPHRI_SWARMS_TOTAL),
            "Kephri Swarm Spawn", "Room Tick"),
    TOA_KEPHRI_BOMB_TANKED(1048, true, KEPHRI,
            new ParseInstruction(MANUAL_PARSE),
            "Kephri Bomb Tanked", "Player", "Room Tick"),
    TOA_KEPHRI_DUNG_THROWN(1049, true, KEPHRI,
            new ParseInstruction(MAP, KEPHRI_DUNG_THROWN),
            "Kephri Dung Thrown", "Room Tick"),
    TOA_KEPHRI_MELEE_ALIVE_TICKS(1350, true, KEPHRI,
            new ParseInstruction(SET, KEPHRI_MELEE_TICKS_ALIVE),
            "Kephri Melee Alive Ticks", "Ticks Alive"),
    TOA_KEPHRI_MELEE_HEAL(1351, true, KEPHRI,
            new ParseInstruction(INCREMENT, KEPHRI_MELEE_SCARAB_HEALS),
            new ParseInstruction(DECREMENT,KEPHRI_SWARMS_HEALED),
            "Kephri Melee Heal", "Room Tick"),
    TOA_APMEKEN_START(1050, true, APMEKEN,
            new ParseInstruction(ROOM_START_FLAG),
            "Apmeken Start", "Client Tick"),
    TOA_APMEKEN_FINISHED(1051, true, APMEKEN,
            new ParseInstruction(SET, APMEKEN_TIME),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(ROOM_END_FLAG),
            "Apmeken Finished", "Room Tick"),
    TOA_APMEKEN_VOLATILE_SPAWN(1052, true, APMEKEN,
            new ParseInstruction(INCREMENT, APMEKEN_VOLATILE_COUNT),
            "Volatile Spawn", "Room Tick"),
    TOA_APMEKEN_SHAMAN_SPAWN(1053, true, APMEKEN,
            new ParseInstruction(INCREMENT, APMEKEN_SHAMAN_COUNT),
            "Shaman Spawn", "Room Tick"),
    TOA_APMEKEN_CURSED_SPAWN(1054, true, APMEKEN,
            new ParseInstruction(INCREMENT, APMEKEN_CURSED_COUNT),
            "Cursed Spawn", "Room Tick"),
    TOA_BABA_START(1060, true, BABA,
            new ParseInstruction(ROOM_START_FLAG),
            "Baba Start", "Client Tick"),
    TOA_BABA_PHASE_1_END(1061, true, BABA,
            new ParseInstruction(SET, BABA_P1_DURATION),
            "Baba P1 End", "Room Tick"),
    TOA_BABA_BOULDER_1_END(1062, true, BABA,
            new ParseInstruction(SPLIT, BABA_P2_SPLIT, BABA_BOULDER_1_DURATION, BABA_P1_DURATION),
            "Baba Boulder1 End", "Room Tick"),
    TOA_BABA_PHASE_2_END(1063, true, BABA,
            new ParseInstruction(SPLIT, BABA_BOULDER_2_SPLIT, BABA_P2_DURATION, BABA_P2_SPLIT),
            "Baba P2 End", "Room Tick"),
    TOA_BABA_BOULDER_2_END(1064, true, BABA,
            new ParseInstruction(SPLIT, BABA_P3_SPLIT, BABA_BOULDER_2_DURATION, BABA_BOULDER_2_SPLIT),
            "Baba Boulder2 End", "Room Tick"),
    TOA_BABA_FINISHED(1065, true, BABA,
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(SPLIT, BABA_TIME, BABA_P3_DURATION, BABA_P3_SPLIT),
            new ParseInstruction(ROOM_END_FLAG),
            "Baba Finished", "Room Tick"),
    TOA_BABA_BOULDER_THROW(1066, true, BABA,
            new ParseInstruction(INCREMENT, BABA_BOULDERS_THROWN),
            "Boulder Throw", "Room Tick"),
    TOA_BABA_BOULDER_BROKEN(1067, true, BABA,
            new ParseInstruction(INCREMENT, BABA_BOULDERS_BROKEN),
            "Boulder Broken", "Room Tick"),
    TOA_HET_START(1070, true, HET,
            new ParseInstruction(ROOM_START_FLAG),
            "Het Start", "Client Tick"),
    TOA_HET_FINISHED(1071, true, HET,
            new ParseInstruction(SET,HET_TIME),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(ROOM_END_FLAG),
            "Het Finished", "Room Tick"),
    TOA_HET_PLAYED_MINED_OBELISK(1072, true, HET,
            new ParseInstruction(MANUAL_PARSE),
            "Het Player Mined Obelisk", "Player", "Room Tick"),
    TOA_HET_DOWN(1073, true, HET,
            new ParseInstruction(MAP,HET_DOWNS),
            "Het Down", "Room Tick"),
    TOA_AKKHA_START(1080, true, AKKHA,
            new ParseInstruction(ROOM_START_FLAG),
            "Akkha Start", "Client Tick"),
    TOA_AKKHA_PHASE_1_END(1081, true, AKKHA,
            new ParseInstruction(SET, AKKHA_P1_DURATION),
            "Akkha P1 End", "Room Tick"),
    TOA_AKKHA_SHADOW_1_END(1082, true, AKKHA,
            new ParseInstruction(SPLIT, AKKHA_P2_SPLIT, AKKHA_SHADOW_1_DURATION, AKKHA_P1_DURATION),
            "Akkha Shadow1 End", "Room Tick"),
    TOA_AKKHA_PHASE_2_END(1083, true, AKKHA,
            new ParseInstruction(SPLIT, AKKHA_SHADOW_2_SPLIT, AKKHA_P2_DURATION, AKKHA_P2_SPLIT),
            "Akkha P2 End", "Room Tick"),
    TOA_AKKHA_SHADOW_2_END(1084, true, AKKHA,
            new ParseInstruction(SPLIT, AKKHA_P3_SPLIT, AKKHA_SHADOW_2_DURATION, AKKHA_SHADOW_2_SPLIT),
            "Akkha Shadow2 End", "Room Tick"),
    TOA_AKKHA_PHASE_3_END(1085, true, AKKHA,
            new ParseInstruction(SPLIT, AKKHA_SHADOW_3_SPLIT, AKKHA_P3_DURATION, AKKHA_P3_SPLIT),
            "Akkha P3 End", "Room Tick"),
    TOA_AKKHA_SHADOW_3_END(1086, true, AKKHA,
            new ParseInstruction(SPLIT, AKKHA_P4_SPLIT, AKKHA_SHADOW_3_DURATION, AKKHA_SHADOW_3_SPLIT),
            "Akkha Shadow3 End", "Room Tick"),
    TOA_AKKHA_PHASE_4_END(1087, true, AKKHA,
            new ParseInstruction(SPLIT, AKKHA_SHADOW_4_SPLIT, AKKHA_P4_DURATION, AKKHA_P4_SPLIT),
            "Akkha P4 End", "Room Tick"),
    TOA_AKKHA_SHADOW_4_END(1088, true, AKKHA,
            new ParseInstruction(SPLIT, AKKHA_P5_SPLIT, AKKHA_SHADOW_4_DURATION, AKKHA_SHADOW_4_SPLIT),
            "Akkha Shadow4 End", "Room Tick"),
    TOA_AKKHA_PHASE_5_END(1089, true, AKKHA,
            new ParseInstruction(SPLIT, AKKHA_FINAL_PHASE_SPLIT, AKKHA_P5_DURATION, AKKHA_P5_SPLIT),
            "Akkha P5 End", "Room Tick"),
    TOA_AKKHA_FINISHED(1090, true, AKKHA,
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(SPLIT, AKKHA_TIME, AKKHA_FINAL_PHASE_DURATION, AKKHA_FINAL_PHASE_SPLIT),
            new ParseInstruction(ROOM_END_FLAG),
            "Akkha Finished", "Room Tick"),
    TOA_AKKHA_NULLED_HIT_ON_SHADOW(1091, true, AKKHA,
            new ParseInstruction(INCREMENT, AKKHA_NULL_HIT),
            "Shadow nulled hit", "Player", "Room Tick", "Weapon"),
    TOA_AKKHA_NULLED_HIT_ON_AKKHA(1092, true, AKKHA,
            new ParseInstruction(INCREMENT, AKKHA_NULL_HIT),
            "Akkha nulled hit", "Player", "Room Tick", "Weapon"),
    TOA_WARDENS_START(1200, true, WARDENS,
            new ParseInstruction(ROOM_START_FLAG),
            "Wardens Start", "Client Tick"),
    TOA_WARDENS_P1_END(1201, true, WARDENS,
            new ParseInstruction(SET, WARDENS_P1_DURATION),
            "Wardens P1 End", "Room Tick"),
    TOA_WARDENS_P2_END(1202, true, WARDENS,
            new ParseInstruction(SPLIT, WARDENS_P3_SPLIT, WARDENS_P2_DURATION, WARDENS_P1_DURATION),
            "Wardens P2 End", "Room Tick"),
    TOA_WARDENS_ENRAGED(1203, true, WARDENS,
            new ParseInstruction(SPLIT, WARDENS_ENRAGED_SPLIT, WARDENS_UNTIL_ENRAGED_DURATION, WARDENS_P3_SPLIT),
            "Wardens Enraged", "Room Tick"),
    TOA_WARDENS_FINISHED(1204, true, WARDENS,
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            new ParseInstruction(SPLIT, WARDENS_TIME, WARDENS_ENRAGED_DURATION, WARDENS_ENRAGED_SPLIT),
            new ParseInstruction(SUM, WARDENS_P3_DURATION, WARDENS_UNTIL_ENRAGED_DURATION, WARDENS_ENRAGED_DURATION),
            new ParseInstruction(ROOM_END_FLAG),
            "Wardens Finished", "Room Tick"),
    TOA_WARDENS_SKULLS_STARTED(1205, true, WARDENS,
            new ParseInstruction(MANUAL_PARSE),
            "Wardens Skull Started", "Room Tick"),
    TOA_WARDENS_SKULLS_ENDED(1206, true, WARDENS,
            new ParseInstruction(MANUAL_PARSE),
            "Wardens Skulls Ended", "Room Tick"),
    TOA_WARDENS_CORE_SPAWNED(1207, true, WARDENS,
            new ParseInstruction(MAP, WARDENS_P2_CORE_SPAWNS),
            "Wardens Core Spawned", "Room Tick"),
    TOA_WARDENS_CORE_DESPAWNED(1208, true, WARDENS,
            new ParseInstruction(MAP, WARDENS_P2_CORE_DESPAWNS),
            "Wardens Core Despawned", "Room Tick"),

    COLOSSEUM_WAVE_1_END(2001, true, COLOSSEUM,
            new ParseInstruction(SET, COLOSSEUM_WAVE_1_DURATION),
            new ParseInstruction(SET, COLOSSEUM_WAVE_2_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            "Wave 1 Ended", "Room Tick"),
    COLOSSEUM_WAVE_2_END(2002, true, COLOSSEUM,
            new ParseInstruction(SET, COLOSSEUM_WAVE_2_DURATION),
            new ParseInstruction(SUM, COLOSSEUM_WAVE_3_SPLIT, COLOSSEUM_WAVE_2_DURATION, COLOSSEUM_WAVE_2_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            "Wave 2 Ended", "Room Tick"),
    COLOSSEUM_WAVE_3_END(2003, true, COLOSSEUM,
            new ParseInstruction(SET, COLOSSEUM_WAVE_3_DURATION),
            new ParseInstruction(SUM, COLOSSEUM_WAVE_4_SPLIT, COLOSSEUM_WAVE_3_DURATION, COLOSSEUM_WAVE_3_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            "Wave 3 Ended", "Room Tick"),
    COLOSSEUM_WAVE_4_END(2004, true, COLOSSEUM,
            new ParseInstruction(SET, COLOSSEUM_WAVE_4_DURATION),
            new ParseInstruction(SUM, COLOSSEUM_WAVE_5_SPLIT, COLOSSEUM_WAVE_4_DURATION, COLOSSEUM_WAVE_4_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            "Wave 4 Ended", "Room Tick"),
    COLOSSEUM_WAVE_5_END(2005, true, COLOSSEUM,
            new ParseInstruction(SET, COLOSSEUM_WAVE_5_DURATION),
            new ParseInstruction(SUM, COLOSSEUM_WAVE_6_SPLIT, COLOSSEUM_WAVE_5_DURATION, COLOSSEUM_WAVE_5_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            "Wave 5 Ended", "Room Tick"),

    COLOSSEUM_WAVE_6_END(2006, true, COLOSSEUM,
            new ParseInstruction(SET, COLOSSEUM_WAVE_6_DURATION),
            new ParseInstruction(SUM, COLOSSEUM_WAVE_7_SPLIT, COLOSSEUM_WAVE_6_DURATION, COLOSSEUM_WAVE_6_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            "Wave 6 Ended", "Room Tick"),

    COLOSSEUM_WAVE_7_END(2007, true, COLOSSEUM,
            new ParseInstruction(SET, COLOSSEUM_WAVE_7_DURATION),
            new ParseInstruction(SUM, COLOSSEUM_WAVE_8_SPLIT, COLOSSEUM_WAVE_7_DURATION, COLOSSEUM_WAVE_7_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            "Wave 7 Ended", "Room Tick"),

    COLOSSEUM_WAVE_8_END(2008, true, COLOSSEUM,
            new ParseInstruction(SET, COLOSSEUM_WAVE_8_DURATION),
            new ParseInstruction(SUM, COLOSSEUM_WAVE_9_SPLIT, COLOSSEUM_WAVE_8_DURATION, COLOSSEUM_WAVE_8_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            "Wave 8 Ended", "Room Tick"),

    COLOSSEUM_WAVE_9_END(2009, true, COLOSSEUM,
            new ParseInstruction(SET, COLOSSEUM_WAVE_9_DURATION),
            new ParseInstruction(SUM, COLOSSEUM_WAVE_10_SPLIT, COLOSSEUM_WAVE_9_DURATION, COLOSSEUM_WAVE_9_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            "Wave 9 Ended", "Room Tick"),

    COLOSSEUM_WAVE_10_END(2010, true, COLOSSEUM,
            new ParseInstruction(SET, COLOSSEUM_WAVE_10_DURATION),
            new ParseInstruction(SUM, COLOSSEUM_WAVE_11_SPLIT, COLOSSEUM_WAVE_10_DURATION, COLOSSEUM_WAVE_10_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            "Wave 10 Ended", "Room Tick"),

    COLOSSEUM_WAVE_11_END(2011, true, COLOSSEUM,
            new ParseInstruction(SET, COLOSSEUM_WAVE_11_DURATION),
            new ParseInstruction(SUM, COLOSSEUM_WAVE_12_SPLIT, COLOSSEUM_WAVE_11_DURATION, COLOSSEUM_WAVE_11_SPLIT),
            new ParseInstruction(ADD_TO_VALUE, CHALLENGE_TIME),
            "Wave 11 Ended", "Room Tick"),

    COLOSSEUM_WAVE_12_END(2012, true, COLOSSEUM,
            new ParseInstruction(SPLIT, CHALLENGE_TIME, COLOSSEUM_WAVE_12_DURATION, COLOSSEUM_WAVE_12_SPLIT),
            "Wave 12 Ended", "Room Tick"),

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
    //public final Object[] arguments;

    private static final Map<Integer, LogID> mapper;

    static
    {
        mapper = new HashMap<>();
        for (LogID id : values())
        {
            mapper.put(id.id, id);
        }
    }
    final int id;
    String commonName;
    final RaidRoom room;
    final boolean simple;
    public final List<ParseInstruction> parseInstructions = new ArrayList<>();
    public final List<String> stringArgs = new ArrayList<>();

    LogID(int id, boolean simple, RaidRoom room, Object... arguments)
    {
        this.id = id;
        this.simple = simple;
        this.room = room;
        commonName = "unknown";
        for(Object obj : arguments)
        {
            if(obj instanceof ParseInstruction)
            {
                parseInstructions.add((ParseInstruction) obj);
            }
            else if(obj instanceof String)
            {
                if(commonName.equals("unknown"))
                {
                    commonName = (String) obj;
                }
                else
                {
                    stringArgs.add((String)obj);
                }
            }
        }
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