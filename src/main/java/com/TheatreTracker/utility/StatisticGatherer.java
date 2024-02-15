package com.TheatreTracker.utility;

import com.TheatreTracker.RoomData;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class StatisticGatherer
{
    public static double getOverallTimeAverage(ArrayList<RoomData> data)
    {
        data = data.stream().filter(RoomData::getOverallTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if (data.isEmpty())
        {
            return -1;
        }
        double total = 0;
        double count = 0;
        for (RoomData d : data)
        {
            total += d.getTimeSum();
            count++;
        }
        return total / count;
    }

    public static double getOverallMedian(ArrayList<RoomData> data)
    {
        data = data.stream().filter(RoomData::getOverallTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        if (data.isEmpty())
        {
            return -1;
        }
        double median;
        List<Double> values = new ArrayList<>();
        for (RoomData d : data)
        {
            values.add((double) d.getTimeSum());
        }
        Collections.sort(values);
        if (values.size() % 2 == 0)
        {
            median = ((double) values.get(values.size() / 2) + values.get(values.size() / 2 - 1)) / 2.0;
        } else
        {
            median = values.get(values.size() / 2);
        }
        return median;
    }

    public static double getOverallTimeMin(ArrayList<RoomData> data)
    {
        data = data.stream().filter(RoomData::getOverallTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int minValue = Integer.MAX_VALUE;
        for (RoomData d : data)
        {
            int split = d.getTimeSum();
            if (split < minValue)
            {
                minValue = split;
            }
        }
        return minValue;
    }

    public static double getOverallMax(ArrayList<RoomData> data)
    {
        data = data.stream().filter(RoomData::getOverallTimeAccurate).collect(Collectors.toCollection(ArrayList::new));
        int maxValue = 0;
        for (RoomData d : data)
        {
            int split = d.getTimeSum();
            if (split > maxValue)
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
        for (int d : data)
        {
            if (d != -1)
            {
                total += d;
                count++;
            }
        }
        return total / count;
    }

    public static double getGenericAverage(ArrayList<RoomData> data, DataPoint parameter)
    {
        double total = 0;
        double count = 0;
        for (RoomData room : data)
        {
            if (!room.getTimeAccurate(parameter))
            {
                continue;
            }
            int d = room.getValue(parameter);
            if (d != -1)
            {
                if (parameter.type != DataPoint.types.TIME || d != 0)
                {
                    total += d;
                    count++;
                }
            }
        }
        return total / count;
    }

    public static double getGenericMedian(ArrayList<Integer> data)
    {
        if (data.isEmpty())
        {
            return -1;
        }
        double median;
        List<Double> values = new ArrayList<>();
        for (int d : data)
        {
            if (d != -1)
                values.add((double) d);
        }
        Collections.sort(values);
        if (values.size() % 2 == 0)
        {
            median = ((double) values.get(values.size() / 2) + (double) values.get(values.size() / 2 - 1)) / 2.0;
        } else
        {
            median = (double) values.get(values.size() / 2);
        }
        return median;
    }

    public static double getGenericMedian(ArrayList<RoomData> data, DataPoint param)
    {
        if (data.isEmpty())
        {
            return -1;
        }
        double median;
        List<Double> values = new ArrayList<>();
        for (RoomData room : data)
        {
            if (!room.getTimeAccurate(param))
            {
                continue;
            }
            int d = room.getValue(param);
            if (d != -1)
                values.add((double) d);
        }
        Collections.sort(values);
        if (!values.isEmpty())
        {
            if (values.size() % 2 == 0)
            {
                median = (values.get(values.size() / 2) + (double) values.get(values.size() / 2 - 1)) / 2.0;
            } else
            {
                median = values.get(values.size() / 2);
            }
            return median;
        }
        return -1;
    }

    public static double getGenericMin(ArrayList<Integer> data, boolean isTime)
    {
        int minValue = Integer.MAX_VALUE;
        for (int d : data)
        {
            if (d < minValue && d != -1 && (!isTime || d != 0))
            {
                minValue = d;
            }
        }
        return minValue;
    }

    public static double getGenericMin(ArrayList<Integer> data)
    {
        return getGenericMin(data, false);
    }

    public static double getGenericMin(ArrayList<RoomData> data, DataPoint parameter)
    {
        int minValue = Integer.MAX_VALUE;
        for (RoomData room : data)
        {
            if (!room.getTimeAccurate(parameter))
            {
                continue;
            }
            int d = room.getValue(parameter);
            if (d < minValue && d != -1)
            {
                if (parameter.type != DataPoint.types.TIME || room.getValue(parameter) != 0)
                {
                    minValue = d;
                }
            }
        }
        return minValue;
    }


    public static double getGenericMax(ArrayList<Integer> data)
    {
        int maxValue = 0;
        for (int d : data)
        {
            if (d > maxValue)
            {
                maxValue = d;
            }
        }
        return maxValue;
    }

    public static double getGenericMax(ArrayList<RoomData> data, DataPoint parameter)
    {
        int maxValue = 0;
        for (RoomData room : data)
        {
            if (!room.getTimeAccurate(parameter))
            {
                continue;
            }
            int d = room.getValue(parameter);
            if (d > maxValue)
            {
                maxValue = d;
            }
        }
        return maxValue;
    }

    public static double getGenericMode(ArrayList<RoomData> data, DataPoint parameter)
    {
        int maxCount = 0;
        int maxValue = 0;
        for (int i = 0; i < data.size(); i++)
        {
            if (!data.get(i).getTimeAccurate(parameter))
            {
                continue;
            }
            int iv = data.get(i).getValue(parameter);
            int count = 0;
            for (RoomData datum : data)
            {
                if (!datum.getTimeAccurate(parameter))
                {
                    continue;
                }
                int jv = datum.getValue(parameter);
                if (jv != -1 && iv != -1)
                {
                    if (Objects.equals(datum, data.get(i)))
                    {
                        count++;
                    }
                }
            }
            if (count > maxCount)
            {
                maxValue = iv;
                maxCount = count;
            }
        }
        if (maxCount > 1)
        {
            return maxValue;
        } else
        {
            return -1;
        }
    }

    public static double getGenericMode(ArrayList<Integer> data)
    {
        int maxCount = 0;
        int maxValue = 0;
        for (int i = 0; i < data.size(); i++)
        {
            int count = 0;
            for (Integer datum : data)
            {
                if (datum != -1 && data.get(i) != -1)
                {
                    if (Objects.equals(datum, data.get(i)))
                    {
                        count++;
                    }
                }
            }
            if (count > maxCount)
            {
                maxValue = data.get(i);
                maxCount = count;
            }
        }
        if (maxCount > 1)
        {
            return maxValue;
        } else
        {
            return -1;
        }
    }


    public static double getGenericPercent(ArrayList<Integer> arrayForStatistics, int threshold)
    {
        double count = 0;
        double total = arrayForStatistics.size();
        for (Integer i : arrayForStatistics)
        {
            if (i <= threshold)
            {
                count++;
            }
        }
        double percent = count / total;
        int percentRounded = (int) (percent * 1000);
        percentRounded /= 10;
        return percentRounded;
    }
}
