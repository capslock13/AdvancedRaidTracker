package com.cTimers.filters;

import com.cTimers.cRoomData;

public class cFilterOtherInt extends cFilterCondition
{
    int param;
    int qualifier;
    int value;
    public cFilterOtherInt(int param, int qualifier, int value)
    {
        this.param = param;
        this.qualifier = qualifier;
        this.value = value;
    }

    @Override
    public boolean evaluate(cRoomData data)
    {
        int conditionValue = -1;
        switch(conditionValue)
        {
            case 0:
                conditionValue = data.maidenBloodsSpawned;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
        }
        return false;
    }
}
