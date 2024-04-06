package com.advancedraidtracker.utility.datautility.datapoints;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.datapoints.toa.*;
import com.advancedraidtracker.utility.datautility.datapoints.tob.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineManager
{
    Map<RaidRoom, RoomParser> parsers;
    final Raid raid;

    public LineManager(Raid raid)
    {
        this.raid = raid;
        parsers = new HashMap<>();
        parsers.put(RaidRoom.MAIDEN, new MaidenParser(raid));
        parsers.put(RaidRoom.BLOAT, new BloatParser(raid));
        parsers.put(RaidRoom.NYLOCAS, new NylocasParser(raid));
        parsers.put(RaidRoom.SOTETSEG, new SotetsegParser(raid));
        parsers.put(RaidRoom.XARPUS, new XarpusParser(raid));
        parsers.put(RaidRoom.VERZIK, new VerzikParser(raid));
        parsers.put(RaidRoom.CRONDIS, new CrondisParser(raid));
        parsers.put(RaidRoom.ZEBAK, new ZebakParser(raid));
        parsers.put(RaidRoom.SCABARAS, new ScabarasParser(raid));
        parsers.put(RaidRoom.KEPHRI, new KephriParser(raid));
        parsers.put(RaidRoom.APMEKEN, new ApmekenParser(raid));
        parsers.put(RaidRoom.BABA, new BabaParser(raid));
        parsers.put(RaidRoom.HET, new HetParser(raid));
        parsers.put(RaidRoom.AKKHA, new AkkhaParser(raid));
        parsers.put(RaidRoom.WARDENS, new WardensParser(raid));
    }

    public Map<Integer, String> getLines(RaidRoom room)
    {
        return parsers.getOrDefault(room, new UnknownParser(raid)).getLines();
    }

    public Map<Integer, String> getRoomSpecificData(RaidRoom room)
    {
        return parsers.getOrDefault(room, new UnknownParser(raid)).getRoomSpecificMarkers();
    }

    public String getRoomSpecificText(RaidRoom room)
    {
        return parsers.getOrDefault(room, new UnknownParser(raid)).getRoomSpecificMarkerName();
    }

    public List<Integer> getRoomSpecificAutos(RaidRoom room)
    {
        return parsers.getOrDefault(room, new UnknownParser(raid)).getRoomAutos();
    }
}
