package com.TheatreTracker.utility.thrallvengtracking;

import com.TheatreTracker.TheatreTrackerPlugin;
import com.TheatreTracker.constants.LogID;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Player;

import java.util.ArrayList;
@Slf4j
public class VengTracker
{
    public ArrayList<VengCastQueue> vengedPlayers;
    private TheatreTrackerPlugin plugin;
    public VengTracker(TheatreTrackerPlugin plugin)
    {
        vengedPlayers = new ArrayList<>();
        this.plugin = plugin;
    }

    private void handleApplyVeng()
    {
        ArrayList<VengCastQueue> temp = new ArrayList<>();
        for(VengCastQueue vcq : vengedPlayers)
        {
            boolean flag = false;
            for(VengCastQueue v : temp)
            {
                if(v.target != null && v.source != null)
                {
                if (v.target.equals(vcq.target))
                {
                    if (!v.source.equals(vcq.source))
                    {
                        vcq.source = v.source;
                    }
                    flag = true;
                }
            }
            }
            if(!flag)
            {
                temp.add(vcq);
            }
        }
        vengedPlayers.clear();
        for(VengCastQueue vcq : temp)
        {
            plugin.clog.write(LogID.VENG_WAS_CAST, vcq.target, vcq.source);
        }
    }

    public void vengProcced(vengpair veng)
    {
        int amount = Math.max(1, ((int)(veng.hitsplat * .75)));
    }

    public void updateTick()
    {
        handleApplyVeng();
    }

    public void vengSelfCast(Player player)
    {
        vengedPlayers.add(new VengCastQueue(player.getName(), player.getName()));
    }

    public void vengOtherCast(Player caster)
    {
        String nameTarget = "";
        String nameSource = "";
        nameSource = caster.getName();
        if(caster.getInteracting() != null)
        {
            nameTarget = caster.getInteracting().getName();
        }
        vengedPlayers.add(new VengCastQueue(nameTarget, nameSource));
    }

    public void vengOtherGraphicApplied(Player receiver)
    {
        vengedPlayers.add(new VengCastQueue(receiver.getName(), ".unknown"));
    }

    public void vengSelfGraphicApplied(Player self)
    {
        vengedPlayers.add(new VengCastQueue(self.getName(), self.getName()));
    }
}
