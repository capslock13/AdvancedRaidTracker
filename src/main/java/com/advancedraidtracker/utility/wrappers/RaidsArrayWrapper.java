package com.advancedraidtracker.utility.wrappers;

import com.advancedraidtracker.SimpleRaidDataBase;

import java.util.ArrayList;

public class RaidsArrayWrapper
{
    public ArrayList<SimpleRaidDataBase> data;
    public String filename;

    public RaidsArrayWrapper(ArrayList<SimpleRaidDataBase> data, String filename)
    {
        this.data = data;
        this.filename = filename;
    }
}
