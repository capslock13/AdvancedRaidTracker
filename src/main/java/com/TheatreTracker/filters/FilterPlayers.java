package com.TheatreTracker.filters;

import com.TheatreTracker.SimpleRaidData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

@Slf4j
public class FilterPlayers extends FilterCondition
{
    private final ArrayList<String> players;
    private final int operator;
    private final String stringValue;

    public FilterPlayers(String players, int operator, String val)
    {
        this.players = new ArrayList<String>();
        String[] playerNames = players.split(",");
        for (String playerName : playerNames)
        {
            this.players.add(playerName.replaceAll(String.valueOf((char) 160), String.valueOf((char) 32)).toLowerCase());
        }
        this.operator = operator;
        stringValue = val;
    }

    private boolean cleanContains(SimpleRaidData data, String player)
    {
        for (String p : data.players.keySet())
        {
            if (p.replaceAll(String.valueOf((char) 160), String.valueOf((char) 32)).toLowerCase().equals(player))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString()
    {
        return stringValue;
    }

    @Override
    public boolean evaluate(SimpleRaidData data)
    {
        switch (operator)
        {
            case 0:
                if (data.players.size() == players.size())
                {
                    for (String p : players)
                    {
                        if (!cleanContains(data, p))
                        {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            case 1:
                boolean flag = true;
                for (String p : players)
                {
                    if (!cleanContains(data, p))
                    {
                        flag = false;
                    }
                }
                return flag;
            case 2:
                for (String p : players)
                {
                    if (cleanContains(data, p))
                    {
                        return true;
                    }
                }
                return false;
            case 3:
                for (String p : players)
                {
                    if (!cleanContains(data, p))
                    {
                        return true;
                    }
                }
                return false;
            case 4:
                for (String p : players)
                {
                    if (cleanContains(data, p))
                    {
                        return false;
                    }
                }
                return true;
        }
        return false;
    }

    public String getFilterCSV()
    {
        StringBuilder playerStr = new StringBuilder();
        for (String s : players)
        {
            playerStr.append(s);
            playerStr.append(",");
        }
        playerStr = new StringBuilder(StringUtils.substring(playerStr.toString(), 0, playerStr.length() - 1));
        return "2-" + operator + "-" + playerStr + "-" + stringValue;
    }
}
