package com.TheatreTracker.utility;

import com.TheatreTracker.RoomData;

import java.util.ArrayList;

public class RaidsArrayWrapper {
    public ArrayList<RoomData> data;
    public String filename;

    public RaidsArrayWrapper(ArrayList<RoomData> data, String filename) {
        this.data = data;
        this.filename = filename;
    }
}
