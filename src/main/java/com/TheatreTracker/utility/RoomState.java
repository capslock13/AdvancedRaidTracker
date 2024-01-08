package com.TheatreTracker.utility;

public class RoomState {
    public enum MaidenRoomState {
        NOT_STARTED,
        PHASE_1,
        PHASE_2,
        PHASE_3,
        PHASE_4,
        FINISHED
    }

    public enum BloatRoomState {
        NOT_STARTED,
        WALKING,
        DOWN,
        FINISHED
    }

    public enum NyloRoomState {
        NOT_STARTED,
        WAVES,
        CLEANUP,
        WAITING_FOR_BOSS,
        BOSS,
        FINISHED
    }

    public enum SotetsegRoomState {
        NOT_STARTED,
        PHASE_1,
        MAZE_1,
        PHASE_2,
        MAZE_2,
        PHASE_3,
        FINISHED
    }

    public enum XarpusRoomState {
        NOT_STARTED,
        EXHUMEDS,
        PRESCREECH,
        POSTSCREECH,
        FINISHED
    }

    public enum VerzikRoomState {
        NOT_STARTED,
        PHASE_1,
        PHASE_2,
        PHASE_2_REDS,
        PHASE_3,
        FINISHED
    }
}
