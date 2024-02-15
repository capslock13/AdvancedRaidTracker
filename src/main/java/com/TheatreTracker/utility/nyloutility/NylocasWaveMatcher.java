package com.TheatreTracker.utility.nyloutility;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public class NylocasWaveMatcher
{
    private static boolean wave17Matched = false;
    private static boolean wave26Matched = false;
    private static boolean wave28Matched = false;
    private static NylocasWave lastWave = null;

    public static boolean isWave(ArrayList<NylocasShell> nylos)
    {
        ArrayList<NylocasWave> potentialWaves = (ArrayList<NylocasWave>) Arrays.stream(NylocasWave.waves).filter(c -> c.count() == nylos.size()).collect(Collectors.toList());
        if (!potentialWaves.isEmpty())
        {
            for (NylocasWave w : potentialWaves)
            {
                boolean waveFlag = true;
                for (NylocasData c : w.getNylos())
                {
                    if (waveFlag && !containsNylo(c, nylos))
                    {
                        waveFlag = false;
                    }
                }
                if (waveFlag) //waves 17, 26, and 28 are comprised of the exact same spawns
                {
                    if (wave17Matched && w.getWave() == 17)
                    {
                        waveFlag = false;
                    } else if (wave26Matched && w.getWave() == 26)
                    {
                        waveFlag = false;

                    } else if (wave28Matched && w.getWave() == 28)
                    {
                        waveFlag = false;
                    } else if (w.getWave() == 17)
                    {
                        wave17Matched = true;
                        wave26Matched = false;
                        wave28Matched = false;
                    } else if (w.getWave() == 26)
                    {
                        wave26Matched = true;
                        wave28Matched = false;
                    } else if (w.getWave() == 28)
                    {
                        wave28Matched = true;
                    } else if (w.getWave() < 17)
                    {
                        wave17Matched = false;
                        wave26Matched = false;
                        wave28Matched = false;
                    } else if (w.getWave() < 26)
                    {
                        wave26Matched = false;
                        wave28Matched = false;
                    } else if (w.getWave() == 27)
                    {
                        wave28Matched = false;
                    }
                }
                if (waveFlag)
                {
                    lastWave = w;
                    return true;
                }
            }
        }
        return false;
    }

    public static NylocasWave getWave()
    {
        return lastWave;
    }

    public static boolean containsNylo(NylocasData c, ArrayList<NylocasShell> ns)
    {
        boolean flag = false;
        for (NylocasShell n : ns)
        {
            if (matchesNylo(c, n))
            {
                flag = true;
            }
        }
        return flag;
    }

    public static boolean matchesNylo(NylocasData c1, NylocasShell c2)
    {
        return (c1.getSpawnStyle() == c2.style && c1.position == c2.position);
    }

}
