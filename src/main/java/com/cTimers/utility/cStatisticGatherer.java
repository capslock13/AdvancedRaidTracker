package com.cTimers.utility;

import com.cTimers.cRoomData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class cStatisticGatherer
{
    public static double getMaiden70SplitAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));

        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.maiden70Split;
            count++;
        }
        return total/count;
    }

    public static double getMaiden7050SplitAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += (d.maiden50Split-d.maiden70Split);
            count++;
        }
        return total/count;
    }

    public static double getMaiden50SplitAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.maiden50Split;
            count++;
        }
        return total/count;
    }

    public static double getMaiden5030SplitAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += (d.maiden30Split-d.maiden50Split);
            count++;
        }
        return total/count;
    }

    public static double getMaiden30SplitAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.maiden30Split;
            count++;
        }
        return total/count;
    }

    public static double getMaidenSkipSplitAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += (d.maidenTime-d.maiden30Split);
            count++;
        }
        return total/count;
    }

    public static double getMaidenTimeAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.maidenTime;
            count++;
        }
        return total/count;
    }

    public static double getMaiden70SplitMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) (d.maiden70Split));
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

    public static double getMaiden7050SplitMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) (d.maiden50Split-d.maiden70Split));
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

    public static double getMaiden50SplitMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.maiden50Split);
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

    public static double getMaiden5030SplitMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) (d.maiden30Split-d.maiden50Split));
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

    public static double getMaiden30SplitMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.maiden30Split);
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

    public static double getMaidenSkipSplitMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.maidenTime-d.maiden30Split);
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

    public static double getMaidenTimeMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.maidenTime);
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

    public static int getMaiden70SplitMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).maiden70Split == data.get(i).maiden70Split)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).maiden70Split;
            }
        }
        return maxValue;
    }

    public static int getMaiden7050SplitMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if((data.get(j).maiden50Split-data.get(j).maiden70Split) == (data.get(i).maiden50Split-data.get(i).maiden70Split))
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = (data.get(i).maiden50Split-data.get(i).maiden70Split);
            }
        }
        return maxValue;
    }

    public static int getMaiden50SplitMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).maiden50Split == data.get(i).maiden50Split)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).maiden50Split;
            }
        }
        return maxValue;
    }

    public static int getMaiden5030SplitMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if((data.get(j).maiden30Split-data.get(j).maiden50Split) == (data.get(i).maiden30Split-data.get(i).maiden50Split))
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = (data.get(i).maiden30Split-data.get(i).maiden50Split);
            }
        }
        return maxValue;
    }

    public static int getMaiden30SplitMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).maiden30Split == data.get(i).maiden30Split)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).maiden70Split;
            }
        }
        return maxValue;
    }

    public static int getMaidenSkipSplitMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if((data.get(j).maidenTime-data.get(j).maiden30Split) == (data.get(i).maidenTime-data.get(i).maiden30Split))
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = (data.get(i).maidenTime-data.get(i).maiden30Split);
            }
        }
        return maxValue;
    }
 //TODO mode wrong
    public static int getMaidenTimeMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).maidenTime == data.get(i).maidenTime)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).maidenTime;
            }
        }
        return maxValue;
    }

    public static int getMaiden70SplitMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.maiden70Split;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static int getMaiden7050SplitMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.maiden50Split-d.maiden70Split;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static int getMaiden50SplitMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.maiden50Split;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static int getMaiden5030SplitMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.maiden30Split-d.maiden50Split;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static int getMaiden30SplitMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.maiden30Split;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static int getMaidenSkipSplitMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.maidenTime-d.maiden30Split;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static int getMaidenTimeMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.maidenTime;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static int getMaiden70SplitMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.maiden70Split;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static int getMaiden7050SplitMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.maiden50Split-d.maiden70Split;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static int getMaiden50SplitMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.maiden50Split;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static int getMaiden5030SplitMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.maiden30Split-d.maiden50Split;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static int getMaiden30SplitMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.maiden30Split;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static int getMaidenSkipSplitMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.maidenTime-d.maiden30Split;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static int getMaidenTimeMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.maidenTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.maidenTime;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getBloatFirstDownAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.bloatTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.bloatFirstDownSplit;
            count++;
        }
        return total/count;
    }

    public static double getBloatTimeAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.bloatTimeAccurate).collect(Collectors.toCollection(ArrayList::new));

        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.bloatTime;
            count++;
        }
        return total/count;
    }

    public static double getBloatFirstDownMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.bloatTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.bloatFirstDownSplit);
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

    public static double getBloatTimeMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.bloatTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.bloatTime);
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

    public static int getBloatFirstDownMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.bloatTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).bloatFirstDownSplit == data.get(i).bloatFirstDownSplit)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).bloatFirstDownSplit;
            }
        }
        return maxValue;
    }

    public static int getBloatTimeMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.bloatTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).bloatTime == data.get(i).bloatTime)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).bloatTime;
            }
        }
        return maxValue;
    }

    public static int getBloatFirstDownMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.bloatTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.bloatFirstDownSplit;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static int getBloatTimeMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.bloatTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.bloatTime;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static int getBloatFirstDownMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.bloatTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.bloatFirstDownSplit;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static int getBloatTimeMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.bloatTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.bloatTime;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getNyloLastWaveAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.nyloLastWave;
            count++;
        }
        return total/count;
    }

    public static double getNyloBossSpawnAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.nyloBossSpawn;
            count++;
        }
        return total/count;
    }

    public static double getNyloBossSplitAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += (d.nyloTime-d.nyloBossSpawn);
            count++;
        }
        return total/count;
    }

    public static double getNyloTimeAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.nyloTime;
            count++;
        }
        return total/count;
    }

    public static double getNyloLastWaveMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.nyloLastWave);
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

    public static double getNyloBossSpawnMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.nyloBossSpawn);
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

    public static double getNyloBossSplitMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) (d.nyloTime-d.nyloBossSpawn));
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

    public static double getNyloTimeMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.nyloTime);
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

    public static double getNyloLastWaveMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).nyloLastWave == data.get(i).nyloLastWave)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).nyloLastWave;
            }
        }
        return maxValue;
    }

    public static double getNyloBossSpawnMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).nyloBossSpawn == data.get(i).nyloBossSpawn)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).nyloBossSpawn;
            }
        }
        return maxValue;
    }

    public static double getNyloBossSplitMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if((data.get(j).nyloTime-data.get(j).nyloBossSpawn) == (data.get(i).nyloTime-data.get(i).nyloBossSpawn))
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = (data.get(i).nyloTime-data.get(i).nyloBossSpawn);
            }
        }
        return maxValue;
    }

    public static double getNyloTimeMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).nyloTime == data.get(i).nyloTime)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).nyloTime;
            }
        }
        return maxValue;
    }

    public static double getNyloLastWaveMinimum(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.nyloLastWave;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getNyloBossSpawnMinimum(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.nyloBossSpawn;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getNyloBossSplitMinimum(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.nyloTime-d.nyloBossSpawn;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getNyloTimeMinimum(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.nyloTime;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getNyloLastWaveMaximum(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.nyloLastWave;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getNyloBossSpawnMaximum(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.nyloBossSpawn;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getNyloBossSplitMaximum(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.nyloTime-d.nyloBossSpawn;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getNyloTimeMaximum(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.nyloTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.nyloTime;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getSoteP1Average(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.soteFirstMazeStartSplit;
            count++;
        }
        return total/count;
    }

    public static double getSoteP2Average(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.soteSecondMazeStartSplit-d.soteFirstMazeEndSplit;
            count++;
        }
        return total/count;
    }

    public static double getSoteP3Average(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.soteTime-d.soteSecondMazeEndSplit;
            count++;
        }
        return total/count;
    }

    public static double getSoteM1Average(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.soteFirstMazeEndSplit-d.soteFirstMazeStartSplit;
            count++;
        }
        return total/count;
    }

    public static double getSoteM2Average(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.soteSecondMazeEndSplit-d.soteSecondMazeStartSplit;
            count++;
        }
        return total/count;
    }

    public static double getSoteTimeAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.soteTime;
            count++;
        }
        return total/count;
    }

    public static double getSoteP1Median(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.soteFirstMazeStartSplit);
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

    public static double getSoteP2Median(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) (d.soteSecondMazeStartSplit-d.soteFirstMazeEndSplit));
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

    public static double getSoteP3Median(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) (d.soteTime-d.soteSecondMazeEndSplit));
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

    public static double getSoteM1Median(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) (d.soteFirstMazeEndSplit-d.soteFirstMazeStartSplit));
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

    public static double getSoteM2Median(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) (d.soteSecondMazeEndSplit-d.soteSecondMazeStartSplit));
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

    public static double getSoteTimeMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.soteTime);
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

    public static double getSoteP1Mode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).soteFirstMazeStartSplit == data.get(i).soteFirstMazeStartSplit)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).soteFirstMazeStartSplit;
            }
        }
        return maxValue;
    }

    public static double getSoteP2Mode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).soteSecondMazeStartSplit-data.get(j).soteFirstMazeEndSplit == data.get(i).soteSecondMazeStartSplit-data.get(j).soteFirstMazeEndSplit)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).soteSecondMazeStartSplit-data.get(i).soteFirstMazeEndSplit;
            }
        }
        return maxValue;
    }

    public static double getSoteP3Mode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).soteTime-data.get(j).soteSecondMazeEndSplit == data.get(i).soteTime-data.get(j).soteSecondMazeEndSplit)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).soteTime-data.get(i).soteSecondMazeEndSplit;
            }
        }
        return maxValue;
    }

    public static double getSoteM1Mode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).soteFirstMazeEndSplit-data.get(j).soteFirstMazeStartSplit == data.get(i).soteFirstMazeEndSplit-data.get(j).soteFirstMazeStartSplit)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).soteFirstMazeEndSplit-data.get(i).soteFirstMazeStartSplit;
            }
        }
        return maxValue;
    }

    public static double getSoteM2Mode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).soteSecondMazeEndSplit-data.get(j).soteSecondMazeStartSplit == data.get(i).soteSecondMazeEndSplit-data.get(j).soteSecondMazeStartSplit)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).soteSecondMazeEndSplit-data.get(i).soteSecondMazeStartSplit;
            }
        }
        return maxValue;
    }
    public static double getSoteTimeMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).soteTime == data.get(i).soteTime)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).soteTime;
            }
        }
        return maxValue;
    }

    public static double getSoteP1Min(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.soteFirstMazeStartSplit;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getSoteP2Min(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.soteSecondMazeStartSplit-d.soteFirstMazeEndSplit;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getSoteP3Min(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.soteTime-d.soteSecondMazeEndSplit;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getSoteM1Min(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.soteFirstMazeEndSplit-d.soteFirstMazeStartSplit;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getSoteM2Min(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.soteSecondMazeEndSplit-d.soteSecondMazeStartSplit;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getSoteTimeMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.soteTime;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getSoteP1Max(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.soteFirstMazeStartSplit;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getSoteP2Max(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.soteSecondMazeStartSplit-d.soteFirstMazeEndSplit;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getSoteP3Max(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.soteTime-d.soteSecondMazeEndSplit;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getSoteM1Max(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.soteFirstMazeEndSplit-d.soteFirstMazeStartSplit;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getSoteM2Max(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.soteSecondMazeEndSplit-d.soteSecondMazeStartSplit;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getSoteTimeMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.soteTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.soteTime;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getXarpScreechAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.xarpTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.xarpScreechSplit;
            count++;
        }
        return total/count;
    }

    public static double getXarpTimeAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.xarpTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.xarpTime;
            count++;
        }
        return total/count;
    }

    public static double getXarpScreechMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.xarpTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.xarpScreechSplit);
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

    public static double getXarpTimeMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.xarpTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.xarpTime);
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

    public static double getXarpScreechMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.xarpTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).xarpScreechSplit == data.get(i).xarpScreechSplit)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).xarpScreechSplit;
            }
        }
        return maxValue;
    }

    public static double getXarpTimeMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.xarpTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).xarpTime == data.get(i).xarpTime)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).xarpTime;
            }
        }
        return maxValue;
    }

    public static double getXarpScreechMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.xarpTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.xarpScreechSplit;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getXarpTimeMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.xarpTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.xarpTime;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getXarpScreechMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.xarpTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.xarpScreechSplit;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getXarpTimeMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.xarpTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.xarpTime;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getVerzikP1Average(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.verzikP1Split;
            count++;
        }
        return total/count;
    }

    public static double getVerzikP2Average(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += (d.verzikP2Split-d.verzikP1Split);
            count++;
        }
        return total/count;
    }

    public static double getVerzikP3EntryAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.verzikP2Split;
            count++;
        }
        return total/count;
    }

    public static double getVerzikP3Average(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += (d.verzikTime-d.verzikP2Split);
            count++;
        }
        return total/count;
    }

    public static double getVerzikTimeAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.verzikTime;
            count++;
        }
        return total/count;
    }

    public static double getVerzikP1Median(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.verzikP1Split);
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

    public static double getVerzikP2Median(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.verzikP2Split-d.verzikP1Split);
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

    public static double getVerzikP3EntryMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.verzikP2Split);
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

    public static double getVerzikP3Median(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.verzikTime-d.verzikP2Split);
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

    public static double getVerzikTimeMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.verzikTime);
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

    public static double getVerzikP1Mode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).verzikP1Split == data.get(i).verzikP1Split)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).verzikP1Split;
            }
        }
        return maxValue;
    }

    public static double getVerzikP2Mode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).verzikP2Split-data.get(j).verzikP1Split == data.get(i).verzikP2Split-data.get(i).verzikP1Split)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).verzikP2Split-data.get(i).verzikP1Split;
            }
        }
        return maxValue;
    }

    public static double getVerzikP3EntryMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).verzikP2Split == data.get(i).verzikP2Split)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).verzikP2Split;
            }
        }
        return maxValue;
    }

    public static double getVerzikP3Mode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).verzikTime-data.get(j).verzikP2Split == data.get(i).verzikTime-data.get(i).verzikP2Split)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).verzikTime-data.get(i).verzikP2Split;
            }
        }
        return maxValue;
    }

    public static double getVerzikTimeMode(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxCount = 0;
        int maxValue = 0;
        for(int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for(int j = 0; j < data.size(); j++)
            {
                if(data.get(j).verzikTime == data.get(i).verzikTime)
                {
                    count++;
                }
            }
            if(count > maxCount)
            {
                maxValue = data.get(i).verzikTime;
            }
        }
        return maxValue;
    }

    public static double getVerzikP1Min(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.verzikP1Split;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getVerzikP2Min(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.verzikP2Split-d.verzikP1Split;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getVerzikP3EntryMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.verzikP2Split;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getVerzikP3Min(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.verzikTime-d.verzikP2Split;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getVerzikTimeMin(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.verzikTime;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getVerzikP1Max(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.verzikP1Split;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getVerzikP2Max(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.verzikP2Split-d.verzikP1Split;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getVerzikP3EntryMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.verzikP2Split;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getVerzikP3Max(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = (d.verzikTime-d.verzikP2Split);
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getVerzikTimeMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.verzikTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.verzikTime;
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getOverallTimeAverage(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.getOverallTimeAccurate()).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0)
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for(cRoomData d : data)
        {
            total += d.getOverallTime();
            count++;
        }
        return total/count;
    }

    public static double getOverallMedian(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.getOverallTimeAccurate()).collect(Collectors.toCollection(ArrayList::new));
        if(data.size() == 0) { return -1; }
        double median;
        List<Double> values = new ArrayList<>();
        for(cRoomData d : data)
        {
            values.add((double) d.getOverallTime());
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
        data = data.stream().filter(d -> d.getOverallTimeAccurate()).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for(cRoomData d : data)
        {
            int split = d.getOverallTime();
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getOverallMax(ArrayList<cRoomData> data)
    {
        data = data.stream().filter(d -> d.getOverallTimeAccurate()).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for(cRoomData d : data)
        {
            int split = d.getOverallTime();
            if(split > maxValue)
            {
                maxValue = split;
            }
        }
        return maxValue;
    }

    public static double getGenericTimeAverage(ArrayList<Integer> data)
    {
        double total = 0;
        double count = 0;
        for(int d : data)
        {
            total += d;
            count++;
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

    public static double getGenericTimeMin(ArrayList<Integer> data)
    {
        int minValue = Integer.MAX_VALUE;
        for(int d : data)
        {
            int split = d;
            if(split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getGenericMax(ArrayList<Integer> data)
    {
        int maxValue = 0;
        for(int d : data)
        {
            int split = d;
            if(split > maxValue)
            {
                maxValue = split;
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
                if(Objects.equals(data.get(j), data.get(i)))
                {
                    count++;
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
