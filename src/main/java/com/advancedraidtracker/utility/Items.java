package com.advancedraidtracker.utility;

import lombok.Getter;
import net.runelite.api.ItemID;

public enum Items
{
    NONE("Nothing", new int[]{0}),
    SCYTHE("Scythe", new int[]{22325, 25736, 25739, 22664}, new int[]{8056}),
    UNCHARGED_SCYTHE("Uncharged Scythe", new int[]{22486, 25738, 25741}, new int[]{8056}),
    TORVA_HELM("Torva Helm", new int[]{ItemID.SANGUINE_TORVA_FULL_HELM, ItemID.TORVA_FULL_HELM}),
    TORVA_PLATEBODY("Torva Platebody", new int[]{ItemID.SANGUINE_TORVA_PLATEBODY, ItemID.TORVA_PLATEBODY}),
    INFERNAL_CAPE("Infernal Cape", new int[]{ItemID.INFERNAL_CAPE, ItemID.INFERNAL_MAX_CAPE, ItemID.INFERNAL_CAPE_21297, ItemID.INFERNAL_CAPE_L, ItemID.INFERNAL_CAPE_23622, ItemID.INFERNAL_MAX_CAPE_L, ItemID.INFERNAL_MAX_CAPE_21285}),
    TORTURE("Torture", new int[]{ItemID.AMULET_OF_TORTURE, ItemID.AMULET_OF_TORTURE_OR}),
    VOID_KNIGHT_GLOVES("Void Gloves", new int[]{ItemID.VOID_KNIGHT_GLOVES_L, ItemID.VOID_KNIGHT_GLOVES_LOR, ItemID.VOID_KNIGHT_GLOVES_OR}),
    ELITE_VOID_TOP("Elite Void Rob Top", new int[]{ItemID.ELITE_VOID_TOP, ItemID.ELITE_VOID_TOP_OR, ItemID.ELITE_VOID_TOP_L, ItemID.ELITE_VOID_TOP_LOR}),
    ELITE_VOID_ROBES("Elite Void Robe Bottom", new int[]{ItemID.ELITE_VOID_ROBE, ItemID.ELITE_VOID_ROBE_OR, ItemID.ELITE_VOID_ROBE_LOR, ItemID.ELITE_VOID_ROBE_L}),
    VOID_KNIGHT_ROBE("Void Robe Bottom", new int[]{ItemID.VOID_KNIGHT_ROBE, ItemID.VOID_KNIGHT_ROBE_L}),

    ;
    @Getter
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

    public boolean matches(int id)
    {
        for (int i : itemIDs)
        {
            if (i == id)
            {
                return true;
            }
        }
        return false;
    }
}