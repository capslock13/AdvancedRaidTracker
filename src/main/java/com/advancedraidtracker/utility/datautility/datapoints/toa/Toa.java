package com.advancedraidtracker.utility.datautility.datapoints.toa;

import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.datautility.datapoints.LogEntry;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;

import java.nio.file.Path;
import java.util.List;

import static com.advancedraidtracker.constants.RaidRoom.*;

public class Toa extends Raid
{
    private ApmekenParser apmekenParser = new ApmekenParser();
    private BabaParser babaParser = new BabaParser();
    private ScabarasParser scabarasParser = new ScabarasParser();
    private KephriParser kephriParser = new KephriParser();
    private HetParser hetParser = new HetParser();
    private AkkhaParser akkhaParser = new AkkhaParser();
    private CrondisParser crondisParser = new CrondisParser();
    private ZebakParser zebakParser = new ZebakParser();
    private WardensParser wardensParser = new WardensParser();

    public Toa(Path filepath, List<LogEntry> raidData)
    {
        super(filepath, raidData);
        roomParsers.put(APMEKEN, apmekenParser);
        roomParsers.put(BABA, babaParser);
        roomParsers.put(SCABARAS, scabarasParser);
        roomParsers.put(KEPHRI, kephriParser);
        roomParsers.put(HET, hetParser);
        roomParsers.put(AKKHA, akkhaParser);
        roomParsers.put(CRONDIS, crondisParser);
        roomParsers.put(ZEBAK, zebakParser);
        roomParsers.put(WARDENS, wardensParser);
    }

    @Override
    public String getRoomStatus()
    {
        return roomStatus;
    }

    @Override
    public RaidType getRaidType()
    {
        return RaidType.TOA;
    }
}
