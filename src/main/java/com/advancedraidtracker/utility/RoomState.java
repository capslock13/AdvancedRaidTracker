package com.advancedraidtracker.utility;

public class RoomState
{
    public enum MaidenRoomState
    {
        NOT_STARTED,
        PHASE_1,
        PHASE_2,
        PHASE_3,
        PHASE_4,
        FINISHED
    }

    public enum BloatRoomState
    {
        NOT_STARTED,
        WALKING,
        DOWN,
        FINISHED
    }

    public enum NyloRoomState
    {
        NOT_STARTED,
        WAVES,
        CLEANUP,
        WAITING_FOR_BOSS,
        BOSS,
        FINISHED
    }

    public enum SotetsegRoomState
    {
        NOT_STARTED,
        PHASE_1,
        MAZE_1,
        PHASE_2,
        MAZE_2,
        PHASE_3,
        FINISHED
    }

    public enum XarpusRoomState
    {
        NOT_STARTED,
        EXHUMEDS,
        PRESCREECH,
        POSTSCREECH,
        FINISHED
    }

    public enum VerzikRoomState
    {
        NOT_STARTED,
        PHASE_1,
        PHASE_2,
        PHASE_2_REDS,
        PHASE_3,
        FINISHED
    }

    public enum AkkhaRoomState
    {
        NOT_STARTED,
        PHASE_1,
        SHADOW_1,
        PHASE_2,
        SHADOW_2,
        PHASE_3,
        SHADOW_3,
        PHASE_4,
        SHADOW_4,
        PHASE_5,
        FINAL_PHASE,
        FINISHED
    }

    public enum BabaRoomState
    {
        NOT_STARTED,
        PHASE_1,
        BOULDER_1,
        PHASE_2,
        BOULDER_2,
        PHASE_3,
        FINISHED
    }

    public enum KephriRoomState
    {
        NOT_STARTED,
        PHASE_1,
        SWARM_1,
        PHASE_2,
        SWARM_2,
        PHASE_3,
        FINISHED
    }

    public enum ZebakRoomState
    {
        NOT_STARTED,
        PHASE_1,
        FINISHED
    }

    public enum WardenRoomState
    {
        NOT_STARTED,
        PHASE_1,
        PHASE_2,
        PHASE_3,
        ENRAGED,
        FINISHED
    }
}
