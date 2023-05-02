package com.cTimers.filters;

import lombok.Getter;

public class cFilter
{
    @Getter
    private final String name;
    @Getter
    private final String[] filters;

    public cFilter(String name, String[] filters)
    {
        this.filters = filters;
        this.name = name;
    }
}
