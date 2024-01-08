package com.TheatreTracker.filters;

import lombok.Getter;

public class Filter {
    @Getter
    private final String name;
    @Getter
    private final String[] filters;

    public Filter(String name, String[] filters) {
        this.filters = filters;
        this.name = name;
    }
}
