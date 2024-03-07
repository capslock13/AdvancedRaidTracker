package com.TheatreTracker.constants;

import net.runelite.api.InstanceTemplates;

import static com.TheatreTracker.constants.RaidType.*;

public enum Room
{
    UNKNOWN("Unknown", UNASSIGNED, new int[]{}),
    TOB_LOBBY("TOB Lobby", TOB, new int[]{14642}),
    MAIDEN("Maiden", TOB, new int[]{12613}),
    BLOAT("Bloat", TOB, new int[]{13125}),
    NYLOCAS("Nylocas", TOB, new int[]{13122}),
    SOTETSEG("Sotetseg", TOB, new int[]{13123, 13379}),
    XARPUS("Xarpus", TOB, new int[] {12612}),
    VERZIK("Verzik", TOB, new int[]{12611}),
    TOA_LOBBY("TOA Lobby", TOA, new int[]{13455}),
    TOA_NEXUS("TOA Nexus", TOA, new int[] {14160}),
    CRONDIS("Crondis", TOA, new int[] {15698}),
    ZEBAK("Zebak", TOA, new int[] {15700}),
    SCABARAS("Scabaras", TOA, new int[]{14162}),
    KEPHRI("Kephri", TOA, new int[]{14164}),
    APMEKEN("Apmeken", TOA, new int[]{15186}),
    BABA("Baba", TOA, new int[]{15188}),
    HET("Het", TOA, new int[]{14674}),
    AKKHA("Akkha", TOA, new int[]{14676}),
    WARDENS("Wardens", TOA, new int[]{14672}),
    TOMB("Tomb", TOA, new int[]{14672}),
    COX_LOBBY("COX Lobby", COX, InstanceTemplates.RAIDS_LOBBY),
    TEKTON("Tekton", COX, InstanceTemplates.RAIDS_TEKTON),
    CRABS("Crabs", COX, InstanceTemplates.RAIDS_CRABS),
    FIRST_SCAVENGERS("First Scavengers", COX, InstanceTemplates.RAIDS_SCAVENGERS),
    ICE_DEMON("Ice Demon", COX, InstanceTemplates.RAIDS_ICE_DEMON),
    SHAMANS("Shamans", COX, InstanceTemplates.RAIDS_SHAMANS),
    FIRST_RESOURCE_ROOM("First Resource Room", COX, InstanceTemplates.RAIDS_FARMING),
    VANGUARDS("Vanguards", COX, InstanceTemplates.RAIDS_VANGUARDS),
    THIEVING("Thieving", COX, InstanceTemplates.RAIDS_THIEVING),
    SECOND_SCAVENGERS("Second Scavengers", COX, InstanceTemplates.RAIDS_SCAVENGERS2),
    VESPULA("Vespula", COX, InstanceTemplates.RAIDS_VESPULA),
    SECOND_RESOURCE_ROOM("Second Resource Room", COX, InstanceTemplates.RAIDS_FARMING2),
    TIGHT_ROPE("Tight Rope", COX, InstanceTemplates.RAIDS_TIGHTROPE),
    GUARDIANS("Guardians", COX, InstanceTemplates.RAIDS_GUARDIANS),
    VASA_NISTIRIO("Vasa Nistirio", COX, InstanceTemplates.RAIDS_VASA),
    SKELETAL_MYSTICS("Skeletal Mystics", COX, InstanceTemplates.RAIDS_MYSTICS),
    MUTTADILE("Muttadile", COX, InstanceTemplates.RAIDS_MUTTADILES),
    OLM("Olm", COX, InstanceTemplates.RAIDS_END),








    ;

    public final String name;
    public final RaidType raidType;
    public int[] regions = new int[]{};
    public InstanceTemplates instanceTemplates;
    Room(String name, RaidType raidType, int[] regions)
    {
        this.name = name;
        this.raidType = raidType;
        this.regions = regions;
    }

    Room(String name, RaidType raidType, InstanceTemplates instanceTemplates)
    {
        this.name = name;
        this.raidType = raidType;
        this.instanceTemplates = instanceTemplates;
    }
}
