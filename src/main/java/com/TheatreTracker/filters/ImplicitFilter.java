package com.TheatreTracker.filters;

import com.TheatreTracker.RoomData;
import com.TheatreTracker.utility.datautility.DataPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ImplicitFilter
{
    private FilterCondition filter;

    public ImplicitFilter(FilterDate filterDate)
    {
        filter = filterDate;
    }

    public ImplicitFilter(FilterTime filterTime)
    {
        filter = filterTime;
    }

    public ImplicitFilter(FilterPlayers filterPlayers)
    {
        filter = filterPlayers;
    }

    public ImplicitFilter(FilterOtherBool filterBool)
    {
        filter = filterBool;
    }

    public ImplicitFilter(FilterOtherInt filterInt)
    {
        filter = filterInt;
    }

    public ImplicitFilter(String s)
    {
        filter = null;
        if (!s.isEmpty())
        {
            int filterType = Integer.parseInt(s.substring(0, 1));
            ArrayList<String> parse = new ArrayList<String>(Arrays.asList(s.split("-")));
            switch (filterType)
            {
                case 0:
                    if (parse.size() == 5)
                    {
                        filter = new FilterTime(
                                DataPoint.getValue(parse.get(1)),
                                Integer.parseInt(parse.get(2)),
                                Integer.parseInt(parse.get(3)),
                                parse.get(4));
                    }
                    break;
                case 1:
                    if (parse.size() == 5)
                    {
                        filter = new FilterOtherInt(
                                DataPoint.getValue(parse.get(1)),
                                Integer.parseInt(parse.get(2)),
                                Integer.parseInt(parse.get(3)),
                                parse.get(4));
                    }
                    break;
                case 2:
                    if (parse.size() == 4)
                    {
                        filter = new FilterPlayers(
                                parse.get(2),
                                Integer.parseInt(parse.get(1)),
                                parse.get(3));
                    }
                    break;
                case 3:
                    if (parse.size() == 4)
                    {
                        filter = new FilterOtherBool(
                                Integer.parseInt(parse.get(1)),
                                (Integer.parseInt(parse.get(2)) == 1),
                                parse.get(3));
                    }
                    break;
                case 4:
                    if (parse.size() == 4)
                    {
                        filter = new FilterDate(
                                new Date(Long.parseLong(parse.get(1))),
                                Integer.parseInt(parse.get(2)),
                                parse.get(3));
                    }
            }
        }
    }

    public String getFilterCSV()
    {
        return filter.getFilterCSV();
    }

    public String getFilterDescription()
    {
        return filter.toString();
    }

    public boolean evaluate(RoomData data)
    {
        return filter.evaluate(data);
    }
}
