package com.advancedraidtracker.utility.datautility.datapoints.inf;

import lombok.Data;
import lombok.Value;


@Value
public class InfernoSpawn
{
    int id;
    int regionX;
    int regionY;
    boolean respawned;
}
