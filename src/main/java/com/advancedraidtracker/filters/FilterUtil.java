package com.advancedraidtracker.filters;

public class FilterUtil
{
    public static boolean compare(int operator, int value, int compareValue)
    {
        switch (operator)
        {
            case 0:
                return compareValue == value;
            case 1:
                return compareValue < value;
            case 2:
                return compareValue > value;
            case 3:
                return compareValue <= value;
            case 4:
                return compareValue >= value;
            default:
                return false;
        }
    }
}
