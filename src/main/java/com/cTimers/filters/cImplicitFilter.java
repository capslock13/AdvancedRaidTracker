package com.cTimers.filters;

import com.cTimers.cRoomData;
import com.cTimers.utility.cDataPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class cImplicitFilter
{
    private cFilterCondition filter;

    public cImplicitFilter(cFilterDate filterDate)
    {
        filter = filterDate;
    }
    public cImplicitFilter(cFilterTime filterTime)
    {
        filter = filterTime;
    }
    public cImplicitFilter(cFilterPlayers filterPlayers)
    {
        filter = filterPlayers;
    }
    public cImplicitFilter(cFilterOtherBool filterBool)
    {
        filter = filterBool;
    }
    public cImplicitFilter(cFilterOtherInt filterInt)
    {
        filter = filterInt;
    }

    public cImplicitFilter(String s)
    {
        filter = null;
        if(s.length() != 0)
        {
            int filterType = Integer.parseInt(s.substring(0,1));
            ArrayList<String> parse = new ArrayList<String>(Arrays.asList(s.split("-")));
            switch(filterType)
            {
                case 0:
                    if(parse.size() == 5)
                    {
                        filter = new cFilterTime(
                                cDataPoint.getValue(parse.get(1)),
                                Integer.parseInt(parse.get(2)),
                                Integer.parseInt(parse.get(3)),
                                parse.get(4));
                    }
                    break;
                case 1:
                    if(parse.size() == 5)
                    {
                        filter = new cFilterOtherInt(
                                cDataPoint.getValue(parse.get(1)),
                                Integer.parseInt(parse.get(2)),
                                Integer.parseInt(parse.get(3)),
                                parse.get(4));
                    }
                    break;
                case 2:
                    if(parse.size() == 4)
                    {
                        filter = new cFilterPlayers(
                                parse.get(2),
                                Integer.parseInt(parse.get(1)),
                                parse.get(3));
                    }
                    break;
                case 3:
                    if(parse.size() == 4)
                    {
                        filter = new cFilterOtherBool(
                                Integer.parseInt(parse.get(1)),
                                (Integer.parseInt(parse.get(2)) == 1),
                                parse.get(3));
                    }
                    break;
                case 4:
                    if(parse.size() == 4)
                    {
                        filter = new cFilterDate(
                                new Date(Long.parseLong(parse.get(1))),
                                Integer.parseInt(parse.get(2)),
                                parse.get(3));
                    }
            }
        }
    }

    public String getFilterCSV() { return filter.getFilterCSV();}
    public String getFilterDescription()
    {
        return filter.toString();
    }

    public boolean evaluate(cRoomData data)
    {
        return filter.evaluate(data);
    }
}
