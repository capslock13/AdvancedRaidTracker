package com.cTimers.utility;

import com.cTimers.cRoomData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class cStatisticGatherer
{

    public static double getOverallTimeAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(cRoomData::getOverallTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.getTimeSum();
            count++;
        }
        return total/count;
    }

    public static double getOverallMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(cRoomData::getOverallTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.getTimeSum());
        }
        Collections.sort(values);
        if(values.size() % 2 == 0)
        {
            median = ((double)values.get(values.size()/2) + (double)values.get(values.size()/2-1))/2.0;
        }
        else
        {
            median = (double) values.get(values.size()/2);
        }
        return median;
    }

    public static double getOverallTimeMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(cRoomData::getOverallTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.getTimeSum();
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getOverallMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(cRoomData::getOverallTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.getTimeSum();
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getGenericAverage(ArrayList<Integer> data)
    {
        double total = 0;
        double count = 0;
        for(int d : data)
        {
            if(d != -1)
            {
                total += d;
                count++;
            }
        }
        return total/count;
    }

    public static double getGenericAverage(ArrayList<cRoomData> data, cDataPoint parameter)
    {
        double total = 0;
        double count = 0;
        for(cRoomData room : data)
        {
            if(!room.getTimeAccurate(parameter))
            {
                continue;
            }
            int d = room.getValue(parameter);
            if(d != -1)
            {
                total += d;
                count++;
            }
        }
        return total/count;
    }

    public static double getGenericMedian(ArrayList<Integer> data)
    {
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(int d : data)
        {
            if(d != -1)
                values.add((double) d);
        }
        Collections.sort(values);
        if(values.size() % 2 == 0)
        {
            median = ((double)values.get(values.size()/2) + (double)values.get(values.size()/2-1))/2.0;
        }
        else
        {
            median = (double) values.get(values.size()/2);
        }
        return median;
    }

    public static double getGenericMedian(ArrayList<cRoomData> data, cDataPoint param)
    {
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData room : data)
        {
            if(!room.getTimeAccurate(param))
            {
                continue;
            }
            int d = room.getValue(param);
            if(d != -1)
                values.add((double) d);
        }
        Collections.sort(values);
        if(values.size() % 2 == 0)
        {
            median = ((double)values.get(values.size()/2) + (double)values.get(values.size()/2-1))/2.0;
        }
        else
        {
            median = (double) values.get(values.size()/2);
        }
        return median;
    }

    public static double getGenericMin(ArrayList<Integer> data)
    {
        int minValue = Integer.MAX_VALUE;
        for(int d : data)
        {
            if(d < minValue && d != -1)
            {
                minValue = d;
            }
        }
        return minValue;
    }

    public static double getGenericMin(ArrayList<cRoomData> data, cDataPoint parameter)
    {
        int minValue = Integer.MAX_VALUE;
        for(cRoomData room : data)
        {
            if(!room.getTimeAccurate(parameter))
            {
                continue;
            }
            int d = room.getValue(parameter);
            if(d < minValue && d != -1)
            {
                minValue = d;
            }
        }
        return minValue;
    }


    public static double getGenericMax(ArrayList<Integer> data)
    {
        int maxValue = 0;
        for(int d : data)
        {
            if(d > maxValue)
            {
                maxValue = d;
            }
        }
        return maxValue;
    }

    public static double getGenericMax(ArrayList<cRoomData> data, cDataPoint parameter)
    {
        int maxValue = 0;
        for(cRoomData room : data)
        {
            if(!room.getTimeAccurate(parameter))
            {
                continue;
            }
            int d = room.getValue(parameter);
            if(d > maxValue)
            {
                maxValue = d;
            }
        }
        return maxValue;
    }

    public static double getGenericMode(ArrayList<cRoomData> data, cDataPoint parameter)
    {
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            if(!data.get(i).getTimeAccurate(parameter))
            {
                continue;
            }
            int iv = data.get(i).getValue(parameter);
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(!data.get(j).getTimeAccurate(parameter))
                {
                    continue;
                }
                int jv = data.get(j).getValue(parameter);
                if(jv != -1 && iv != -1)
                {
                    if (Objects.equals(data.get(j), data.get(i)))
                    {
                        count++;
                    }
                }
            }
            if(count > maxCount)
            {
                maxValue = iv;
            }
        }
        return maxValue;
    }
    public static double getGenericMode(ArrayList<Integer> data)
    {
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j) != -1 && data.get(i) != -1) {
                    if (Objects.equals(data.get(j), data.get(i))) {
                        count++;
                    }
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i);
            }
        }
        return maxValue;
    }


}
