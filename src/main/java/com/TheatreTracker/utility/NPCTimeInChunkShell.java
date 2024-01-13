package com.TheatreTracker.utility;

import lombok.Getter;
import net.runelite.api.NPC;

public class NPCTimeInChunkShell
{
    @Getter
    public int timeInChunk;
    public int chunk;
    public NPC npc;
    public boolean marked;

    public int getIndex()
    {
        return npc.getIndex();
    }

    public NPCTimeInChunkShell(NPC npc)
    {
        this.npc = npc;
    }

    public NPCTimeInChunkShell(NPC npc, int chunk, int timeInChunk)
    {
        this.npc = npc;
        this.chunk = chunk;
        this.timeInChunk = timeInChunk;
        marked = false;
    }
}
