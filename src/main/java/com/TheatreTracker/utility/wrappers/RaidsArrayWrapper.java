package com.TheatreTracker.utility.wrappers;

import com.TheatreTracker.SimpleRaidData;

import java.util.ArrayList;

public class RaidsArrayWrapper
{
    public ArrayList<SimpleRaidData> data;
    public String filename;

    public RaidsArrayWrapper(ArrayList<SimpleRaidData> data, String filename)
    {
        this.data = data;
        this.filename = filename;
    }
}
