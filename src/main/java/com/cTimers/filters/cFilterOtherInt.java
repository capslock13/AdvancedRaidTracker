package com.cTimers.filters;

import com.cTimers.cRoomData;

public class cFilterOtherInt extends cFilterCondition
{
    int param;
    int qualifier;
    int value;
    String stringValue;
    public cFilterOtherInt(int param, int qualifier, int value)
    {
        this.param = param;
        this.qualifier = qualifier;
        this.value = value;
    }

    public cFilterOtherInt(int param, int qualifier, int value, String val)
    {
        this.param = param;
        this.qualifier = qualifier;
        this.value = value;
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
        int conditionValue = -1;
        boolean roomAccurate = true;
        if(param < 4)
        {
            if(!data.maidenStartAccurate || !data.maidenEndAccurate)
            {
                roomAccurate = false;
            }
        }
        else if(param < 8)
        {
            if(!data.bloatStartAccurate || !data.bloatEndAccurate)
            {
                roomAccurate = false;
            }
        }
        else if(param < 19)
        {
            if(!data.nyloStartAccurate || !data.nyloEndAccurate)
            {
                roomAccurate = false;
            }
        }
        else if(param < 24)
        {
            if(!data.soteStartAccurate || !data.soteEndAccurate)
            {
                roomAccurate = false;
            }
        }
        else if(param < 27)
        {
            if(!data.xarpStartAccurate || !data.xarpEndAccurate)
            {
                roomAccurate = false;
            }
        }
        else if(param < 29)
        {
            if(!data.verzikStartAccurate || !data.verzikEndAccurate)
            {
                roomAccurate = false;
            }
        }
        if(roomAccurate)
        {
            switch (param)
            {
                case 0:
                    conditionValue = data.maidenBloodsSpawned;
                    break;
                case 1:
                    conditionValue = data.maidenCrabsLeaked;
                    break;
                case 2:
                    conditionValue = (data.maidenDefenseAccurate) ? data.maidenDefense : -1;
                    break;
                case 3:
                    conditionValue = data.maidenDeaths;
                    break;
                case 4:
                    conditionValue = data.bloatDowns;
                    break;
                case 5:
                    conditionValue = data.bloatFirstWalkDeaths;
                    break;
                case 6:
                    conditionValue = (data.bloatDefenseAccurate) ? data.bloatfirstWalkDefense : -1;
                    break;
                case 7:
                    conditionValue = data.bloatDeaths;
                    break;
                case 8:
                    conditionValue = data.nyloStallsPre20;
                    break;
                case 9:
                    conditionValue = data.nyloStallsPost20;
                    break;
                case 10:
                    conditionValue = data.nyloStallsTotal;
                    break;
                case 11:
                    conditionValue = data.nyloRangeSplits;
                    break;
                case 12:
                    conditionValue = data.nyloMageSplits;
                    break;
                case 13:
                    conditionValue = data.nyloMeleeSplits;
                    break;
                case 14:
                    conditionValue = data.nyloRangeRotations;
                    break;
                case 15:
                    conditionValue = data.nyloMageRotations;
                    break;
                case 16:
                    conditionValue = data.nyloMeleeRotations;
                    break;
                case 17:
                    conditionValue = (data.nyloDefenseAccurate) ? data.nyloDefenseReduction : -1;
                    break;
                case 18:
                    conditionValue = (data.nyloDeaths);
                    break;
                case 19:
                    conditionValue = (data.soteSpecsP1);
                    break;
                case 20:
                    conditionValue = (data.soteSpecsP2);
                    break;
                case 21:
                    conditionValue = (data.soteSpecsP3);
                    break;
                case 22:
                    conditionValue = data.soteDeaths;
                    break;
                case 23:
                    conditionValue = data.soteSpecsTotal;
                    break;
                case 24:
                    conditionValue = (data.xarpDefenseAccurate) ? data.xarpDefense : -1;
                    break;
                case 25:
                    conditionValue = data.xarpDeaths;
                    break;
                case 26:
                    conditionValue = data.xarpHealing;
                    break;
                case 27:
                    conditionValue = data.verzikBounces;
                    break;
                case 28:
                    conditionValue = data.verzikDeaths;
                    break;
                case 29:
                    conditionValue = data.raidTeamSize;
                    break;
            }
        }
        if(conditionValue == -1)
        {
            return false;
        }
        else
        {
            switch(qualifier)
            {
                case 0:
                    return conditionValue == value;
                case 1:
                    return conditionValue < value;
                case 2:
                    return conditionValue > value;
                case 3:
                    return conditionValue <= value;
                case 4:
                    return conditionValue >= value;
                default:
                    return false;
            }
        }
    }
}
