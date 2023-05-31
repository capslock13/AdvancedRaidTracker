package com.cTimers.utility;

import com.cTimers.cRoomData;

import java.util.ArrayList;

public class cRaidsArrayWrapper
{
    public ArrayList<cRoomData> data;
    public String filename;
    public cRaidsArrayWrapper(ArrayList<cRoomData> data, String filename)
    {
        this.data = data;
        this.filename = filename;
    }
}
