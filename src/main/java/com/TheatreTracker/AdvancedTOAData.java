package com.TheatreTracker;

import com.TheatreTracker.constants.LogID;
import com.TheatreTracker.utility.AdvancedRaidData;
import com.TheatreTracker.utility.wrappers.PlayerDidAttack;
import com.TheatreTracker.utility.wrappers.ThrallOutlineBox;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.game.ItemManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
@Slf4j
public class AdvancedTOAData extends AdvancedRaidData
{
    private final String[] names = {"Apmeken", "Baba", "Scabaras", "Kephri", "Crondis", "Zebak", "Het", "Akkha", "Wardens P1", "Wardens P2", "Wardens P3"};
    public AdvancedTOAData(ArrayList<String> globalData, ItemManager itemManager)
    {
        attackData = new LinkedHashMap<>();
        hpData = new LinkedHashMap<>();
        npcIndexData = new LinkedHashMap<>();
        thrallOutlineBoxes = new LinkedHashMap<>();
        for(String name : names)
        {
            attackData.put(name, new ArrayList<>());
            hpData.put(name, new LinkedHashMap<>());
            npcIndexData.put(name, new LinkedHashMap<>());
            thrallOutlineBoxes.put(name, new ArrayList<>());
        }
        int phase = 1;
        for(String s : globalData)
        {
            String[] subData = s.split(",");
            LogID id = LogID.valueOf(Integer.parseInt(subData[3]));
            if(id == LogID.PLAYER_ATTACK)
            {
                if(subData.length > 9)
                {
                    if(attackData.containsKey(subData[9]))
                    {
                        attackData.get(subData[9]).add(getPlayerDidAttack(subData, itemManager));
                    }
                    else if(subData[9].contains("Wardens"))
                    {
                        attackData.get(subData[9] + " P" + phase).add(getPlayerDidAttack(subData, itemManager));
                    }
                }
            }
            else if(id == LogID.ADD_NPC_MAPPING)
            {
                if(subData.length > 6)
                {
                    if(npcIndexData.containsKey(subData[6]))
                    {
                        npcIndexData.get(subData[6]).put(Integer.parseInt(subData[4]), subData[5]);
                    }
                }
            }
            else if(id == LogID.UPDATE_HP)
            {
                if(subData.length > 6)
                {
                    if(hpData.containsKey(subData[6]))
                    {
                        hpData.get(subData[6]).put(Integer.parseInt(subData[4]), Integer.valueOf(subData[5]));
                    }
                }
            }
            else if(id == LogID.THRALL_SPAWN)
            {
                if(subData.length > 7)
                {
                    if(thrallOutlineBoxes.containsKey(subData[7]))
                    {
                        thrallOutlineBoxes.get(subData[7]).add(new ThrallOutlineBox(subData[4], Integer.parseInt(subData[5]), Integer.parseInt(subData[6])));
                    }
                }
            }
            else if(id == LogID.TOA_WARDENS_P1_END)
            {
                phase = 2;
            }
            else if(id == LogID.TOA_WARDENS_P2_END)
            {
                phase = 3;
            }
        }
    }

}
