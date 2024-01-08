package com.TheatreTracker.filters;

import com.TheatreTracker.RoomData;

public abstract class FilterCondition {
    public abstract boolean evaluate(RoomData data);

    public abstract String getFilterCSV();
}
