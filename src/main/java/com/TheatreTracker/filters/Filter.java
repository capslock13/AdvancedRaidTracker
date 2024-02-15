package com.TheatreTracker.filters;

import lombok.Getter;

@Getter
public class Filter
{
    private final String name;
    private final String[] filters;

    public Filter(String name, String[] filters)
    {
        this.filters = filters;
        this.name = name;
    }
}
