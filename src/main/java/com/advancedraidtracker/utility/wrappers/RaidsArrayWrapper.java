package com.advancedraidtracker.utility.wrappers;

import com.advancedraidtracker.SimpleTOBData;

import java.util.ArrayList;

public class RaidsArrayWrapper
{
    public ArrayList<SimpleTOBData> data;
    public String filename;

    public RaidsArrayWrapper(ArrayList<SimpleTOBData> data, String filename)
    {
        this.data = data;
        this.filename = filename;
    }
}
