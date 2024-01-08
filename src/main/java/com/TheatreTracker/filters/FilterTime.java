package com.TheatreTracker.filters;

import com.TheatreTracker.utility.DataPoint;
import lombok.extern.slf4j.Slf4j;
import com.TheatreTracker.RoomData;

@Slf4j
public class FilterTime extends FilterCondition {
    private DataPoint split;
    private int compare;
    private int time;
    private String stringValue;

    public FilterTime(DataPoint split, int compare1, int time1, String str) {
        this.split = split;
        this.compare = compare1;
        this.time = time1;
        stringValue = str;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    @Override
    public boolean evaluate(RoomData data) {
        int checkValue = data.getValue(split);
        switch (compare) {
            case 0:
                return time == checkValue;
            case 1:
                return checkValue < time;
            case 2:
                return checkValue > time;
            case 3:
                return checkValue <= time;
            case 4:
                return checkValue >= time;
            default:
                return false;
        }
    }

    public String getFilterCSV() {
        return "0-" + split.name + "-" + compare + "-" + time + "-" + stringValue;
    }
}

