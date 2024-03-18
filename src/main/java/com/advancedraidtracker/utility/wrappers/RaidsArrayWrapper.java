package com.advancedraidtracker.utility.wrappers;

import com.advancedraidtracker.utility.datautility.datapoints.Raid;

import java.util.List;

public class RaidsArrayWrapper
{
    public List<Raid> data;
    public String filename;

    public RaidsArrayWrapper(List<Raid> data, String filename)
    {
        this.data = data;
        this.filename = filename;
    }
}
