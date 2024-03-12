package com.advancedraidtracker.constants;

import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.datautility.MultiRoomDataPoint;
import com.advancedraidtracker.utility.datautility.MultiRoomPlayerDataPoint;

public class ParseObject
{
    ParseObject(ParseType type) //MANUAL PARSE, RAID AGNOSTIC PARSE
    {

    }

    ParseObject(ParseType type, MultiRoomPlayerDataPoint mrpdPoint) //INCREMENT
    {

    }

    ParseObject(ParseType type, MultiRoomDataPoint mrdPoint) //DWH
    {

    }

    ParseObject(ParseType type, DataPoint point)
    {

    }
}
