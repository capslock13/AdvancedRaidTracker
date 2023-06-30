package com.TheatreTracker.utility.thrallvengtracking;

public class DamageQueueShell
{
    public int sourceIndex;
    public int offset;
    public int targetIndex;
    public String source;

    public int getSourceIndex()
    {
        return sourceIndex;
    }
    public DamageQueueShell(int targetIndex, int sourceIndex, int offset, String source)
    {
        this.source = source;
        this.offset = offset;
        this.targetIndex = targetIndex;
        this.sourceIndex = sourceIndex;
    }
}
