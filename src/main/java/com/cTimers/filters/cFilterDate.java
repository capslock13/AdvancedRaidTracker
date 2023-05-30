package com.cTimers.filters;

import com.cTimers.cRoomData;

import java.util.Calendar;
import java.util.Date;

public class cFilterDate extends cFilterCondition
{
    private Date date;
    private int qualifier;
    private String stringValue;

    public cFilterDate(Date date, int qualifier, String val)
    {
        this.date = date;
        this.qualifier = qualifier;
        stringValue = val;
    }
    @Override
    public String toString()
    {
        return stringValue;
    }
    @Override
    public boolean evaluate(cRoomData data)
    {
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal.setTime(data.raidStarted);
        cal2.setTime(date);
        switch(qualifier)
        {
            case 0:
                return (cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                        cal.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) &&
                        cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR));
            case 1:
                return data.raidStarted.before(date);
            case 2:
                return data.raidStarted.after(date);
            default:
                throw new IllegalStateException("Unexpected value: " + qualifier);
        }
    }

    public String getFilterCSV()
    {
        return "4-" + date.getTime() + "-" + qualifier + "-" + stringValue;
    }
}
