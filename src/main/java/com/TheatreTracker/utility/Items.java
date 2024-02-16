package com.TheatreTracker.utility;

import net.runelite.api.ItemID;

public enum Items
{
    SCYTHE("Scythe", new int[]{22325, 25736, 25739, 22664}, new int[]{8056}),
    UNCHARGED_SCYTHE("Uncharged Scythe", new int[]{22486,25738,25741}, new int[]{8056}),
    TORVA_HELM("Torva Helm", new int[]{ItemID.SANGUINE_TORVA_FULL_HELM, ItemID.TORVA_FULL_HELM}),
    TORVA_PLATEBODY("Torva Platebody", new int[]{ItemID.SANGUINE_TORVA_PLATEBODY, ItemID.TORVA_PLATEBODY}),
    INFERNAL_CAPE("Infernal Cape", new int[]{ItemID.INFERNAL_CAPE, ItemID.INFERNAL_MAX_CAPE, ItemID.INFERNAL_CAPE_21297, ItemID.INFERNAL_CAPE_L, ItemID.INFERNAL_CAPE_23622, ItemID.INFERNAL_MAX_CAPE_L, ItemID.INFERNAL_MAX_CAPE_21285}),
    TORTURE("Torture", new int[]{ItemID.AMULET_OF_TORTURE, ItemID.AMULET_OF_TORTURE_OR}),





;
    private String name;
    private int[] itemIDs;
    private int[] animationIDs;
    Items(String name, int[] itemIDs, int[] animationIDs)
    {
        this.name = name;
        this.itemIDs = itemIDs;
        this.animationIDs = animationIDs;
    }
    Items(String name, int[] itemIDs)
    {
        this.name = name;
        this.itemIDs = itemIDs;
    }
}
