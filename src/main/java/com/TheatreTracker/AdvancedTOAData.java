package com.TheatreTracker;

import com.TheatreTracker.constants.LogID;
import com.TheatreTracker.utility.AdvancedRaidData;
import com.TheatreTracker.utility.wrappers.PlayerDidAttack;
import net.runelite.client.game.ItemManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AdvancedTOAData extends AdvancedRaidData
{
    private final String[] names = {"Apmeken", "Baba", "Scabaras", "Kephri", "Crondis", "Zebak", "Het", "Akkha", "Wardens"};
    public AdvancedTOAData(ArrayList<String> globalData, ItemManager itemManager)
    {
        attackData = new LinkedHashMap<>();
        for(String name : names)
        {
            attackData.put(name, new ArrayList<>());
        }
        for(String s : globalData)
        {
            String[] subData = s.split(",");
            if(LogID.valueOf(Integer.parseInt(subData[3])).equals(LogID.PLAYER_ATTACK))
            {
                if(subData.length > 9)
                {
                    if(attackData.containsKey(subData[9]))
                    {
                        attackData.get(subData[9]).add(getPlayerDidAttack(subData, itemManager));
                    }
                }
            }
        }
    }

}
