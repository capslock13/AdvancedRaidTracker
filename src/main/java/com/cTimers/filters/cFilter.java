package com.cTimers.filters;

import lombok.Getter;

public class cFilter
{
    @Getter
    private String name;
    @Getter
    private String[] filters;

    public cFilter(String name, String[] filters)
    {
        this.filters = filters;
        this.name = name;
    }
}
