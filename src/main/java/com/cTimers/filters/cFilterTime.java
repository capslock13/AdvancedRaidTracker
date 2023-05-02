package com.cTimers.filters;

import lombok.extern.slf4j.Slf4j;
import com.cTimers.cRoomData;
@Slf4j
public class cFilterTime extends cFilterCondition
{
    private int split;
    private int compare;
    private int time;

    private String stringValue;
    public cFilterTime(int split1, int compare1, int time1)
    {
        this.split = split1;
        this.compare = compare1;
        this.time = time1;
    }
    public cFilterTime(int split1, int compare1, int time1, String str)
    {
        this.split = split1;
        this.compare = compare1;
        this.time = time1;
        stringValue = str;
    }

    @Override
    public String toString()
    {
        return stringValue;
    }

    @Override
    public boolean evaluate(cRoomData data)
    {
        int splitTime = 0;
        switch(split)
        {
            case 0:
                splitTime = data.maidenTime;
                break;
            case 1:
                splitTime = data.maiden70Split;
                break;
            case 2:
                splitTime = data.maiden50Split;
                break;
            case 3:
                splitTime = data.maiden30Split;
                break;
            case 4:
                splitTime = (data.maiden50Split == 0 || data.maiden70Split == 0) ? 0 : data.maiden50Split-data.maiden70Split;
                break;
            case 5:
                splitTime = (data.maiden30Split == 0 || data.maiden50Split == 0) ? 0 : data.maiden30Split-data.maiden50Split;
                break;
            case 6:
                splitTime = (data.maidenTime == 0 || data.maiden30Split == 0) ? 0 : data.maidenTime-data.maiden30Split;
                break;
            case 7:
                splitTime = data.bloatTime;
                break;
            case 8:
                splitTime = data.bloatFirstDownSplit;
            case 9:
                splitTime = data.nyloTime;
                break;
            case 10:
                splitTime = data.nyloBossSpawn;
                break;
            case 11:
                splitTime = (data.nyloTime == 0 || data.nyloBossSpawn == 0) ? 0 : data.nyloTime-data.nyloBossSpawn;
                break;
            case 12:
                splitTime = data.nyloLastWave;
                break;
            case 13:
                //TODO NYLO CLEANUP
                break;
            case 14:
                splitTime = data.soteTime;
                break;
            case 15:
                splitTime = data.soteFirstMazeStartSplit;
                break;
            case 16:
                splitTime = data.soteSecondMazeStartSplit;
                break;
            case 17:
                splitTime = (data.soteFirstMazeEndSplit == 0 || data.soteFirstMazeStartSplit == 0) ? 0 : data.soteFirstMazeEndSplit-data.soteFirstMazeStartSplit;
                break;
            case 18:
                splitTime = (data.soteSecondMazeStartSplit == 0 || data.soteSecondMazeEndSplit == 0) ? 0 : data.soteSecondMazeEndSplit-data.soteSecondMazeStartSplit;
                break;
            case 19:
                //TODO AVERAGE MAZE LENGTH
                break;
            case 20:
                splitTime = data.xarpTime;
                break;
            case 21:
                splitTime = data.xarpScreechSplit;
                break;
            case 22:
                splitTime = (data.xarpTime == 0 || data.xarpScreechSplit == 0) ? 0 : data.xarpTime-data.xarpScreechSplit;
                break;
            case 23:
                splitTime = data.verzikTime;
                break;
            case 24:
                splitTime = data.verzikP1Split;
                break;
            case 25:
                splitTime = (data.verzikP1Split == 0 || data.verzikP2Split == 0) ? 0 : data.verzikP2Split-data.verzikP1Split;
                break;
            case 26:
                splitTime = (data.verzikP2Split == 0 || data.verzikTime == 0) ? 0 : data.verzikTime-data.verzikP2Split;
                break;
            default:
                splitTime = 0;
        }
        if(splitTime == 0)
        {
            return false;
        }
        else
        {
            switch(compare)
            {
                case 0:
                    if(time == splitTime)
                    {
                        return true;
                    }
                    break;
                case 1:
                    if(splitTime < time)
                    {
                        return true;
                    }
                    break;
                case 2:
                    if(splitTime > time)
                    {
                        return true;
                    }
                    break;
                case 3:
                    if(splitTime <= time)
                    {
                        return true;
                    }
                    break;
                case 4:
                    if(splitTime >= time)
                    {
                        return true;
                    }
                    break;
            }
            return false;
        }
    }
}
