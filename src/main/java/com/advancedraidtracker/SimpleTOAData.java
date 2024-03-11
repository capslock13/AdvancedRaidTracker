package com.advancedraidtracker;

import com.advancedraidtracker.constants.LogID;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.datautility.DataManager;
import com.advancedraidtracker.utility.datautility.DataPoint;
import com.advancedraidtracker.utility.wrappers.PlayerCorrelatedPointData;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static com.advancedraidtracker.utility.datautility.DataPoint.*;

@Slf4j
public class SimpleTOAData extends SimpleRaidDataBase
{
    public final String filePath;
    public final String fileName;
    private final DataManager dataManager;
    private ArrayList<String> globalData;
    private String raidStatus = "";
    private boolean hasExited = false;
    private int lastStartTime = 0;
    private int raidExitTime = 0;
    private int raidStartTime = 0;
    public List<Integer> zebakWaterfalls = new ArrayList<>();
    public List<Integer> zebakBoulders = new ArrayList<>();
    public List<Integer> wardenCoreStarts = new ArrayList<>();
    public List<Integer> wardenCoreEnds = new ArrayList<>();
    public List<Integer> hetDowns = new ArrayList<>();
    public List<Integer> dungThrows = new ArrayList<>();

    public SimpleTOAData(String[] parameters, String filePath, String fileName)
    {
        raidType = RaidType.TOA;
        this.filePath = filePath;
        this.fileName = fileName;
        dataManager = new DataManager(RaidType.TOA);
        globalData = new ArrayList<>(Arrays.asList(parameters));
        Date endTime = null;
        parseRoomAgnostic();
        int currentRoomIndex = -1;
        int currentIndex = 0;
        for (String s : globalData)
        {
            String[] subData = s.split(",");
            if (subData.length > 3)
            {
                int key = Integer.parseInt(subData[3]);
                if (LogID.valueOf(key).equals(LogID.LEFT_TOA))
                {
                    endTime = new Date(Long.parseLong(subData[1]));
                } else if (LogID.valueOf(key).equals(LogID.ENTERED_NEW_TOA_REGION))
                {
                    if (!subData[4].equals("TOA Nexus"))
                    {
                        currentRoomIndex = currentIndex;
                        break;
                    }
                }
            }
            currentIndex++;
        }
        if (currentRoomIndex == -1)
        {
            return;
        }
        String nextRoom = globalData.get(currentRoomIndex).split(",")[4];
        globalData = new ArrayList<>(globalData.subList(currentRoomIndex + 1, globalData.size()));
        parseNextRoom(nextRoom); //Path 1
        if (!hasExited && !globalData.isEmpty())
        {
            parseNextRoom(globalData.get(0).split(",")[4]); //Path 2
            if (!hasExited && !globalData.isEmpty())
            {
                parseNextRoom(globalData.get(0).split(",")[4]); //Path 3
                if (!hasExited && !globalData.isEmpty())
                {
                    parseNextRoom(globalData.get(0).split(",")[4]); //Path 4
                    if (!hasExited && !globalData.isEmpty())
                    {
                        parseNextRoom(globalData.get(0).split(",")[4]); //Wardens
                    }
                }
            }
        }
        dataManager.set(CHALLENGE_TIME, getTimeSum());
        dataManager.set(OVERALL_TIME, getOverallTime());
    }

    private void addColorToStatus(String color)
    {
        String lastLetter = raidStatus.substring(raidStatus.length() - 1);
        raidStatus = raidStatus.substring(0, raidStatus.length() - 1) + color + lastLetter;
    }

    public void parseRoomAgnostic()
    {
        for (String s : globalData)
        {
            String[] subData = s.split(",");
            if (subData.length > 3)
            {
                LogID key = LogID.valueOf(Integer.parseInt(subData[3]));
                switch (key)
                {
                    case TOA_PARTY_MEMBERS:
                        dataManager.increment(TOA_PARTY_SIZE);
                        for (int i = 4; i < subData.length; i++)
                        {
                            if (!subData[i].isEmpty())
                            {
                                players.put(subData[i], 0);
                            }
                        }
                        break;
                    case ENTERED_TOA:
                        raidStarted = new Date(Long.parseLong(subData[1]));
                        break;
                    case INVOCATION_LEVEL:
                        dataManager.set(TOA_INVOCATION_LEVEL, Integer.parseInt(subData[4]));
                        break;
                    case RAID_TIMER_START:
                        raidStartTime = Integer.parseInt(subData[4]);
                        break;
                }
            }
        }
    }

    public void parseNextRoom(String room)
    {
        switch (room)
        {
            case "Crondis":
                parseCrondisPath();
                break;
            case "Scabaras":
                parseScabarasPath();
                break;
            case "Apmeken":
                parseApmekenPath();
                break;
            case "Het":
                parseHetPath();
                break;
            case "Wardens":
                parseWardens();
                break;
        }
    }

    public void parseCrondisPath()
    {
        int pathStartTick = 0;
        raidStatus += "Z";
        int activeIndex = 0;
        boolean hasSeenNexus = false;
        for (String s : globalData)
        {
            try
            {
                if (activeIndex == 0 && !LogID.valueOf(Integer.parseInt(s.split(",")[3])).equals(LogID.LEFT_TOA))
                {
                    activeIndex++;
                    continue;
                }
                String[] subData = s.split(",");
                switch (LogID.valueOf(Integer.parseInt(subData[3])))
                {
                    case LEFT_TOA:
                        hasExited = true;
                        if (hasSeenNexus)
                        {
                            addColorToStatus(orange);
                        } else
                        {
                            addColorToStatus(red);
                        }
                        raidExitTime = Integer.parseInt(subData[4]);
                        return;
                    case ENTERED_NEW_TOA_REGION:
                        if (subData[4].equals("TOA Nexus"))
                        {
                            hasSeenNexus = true;
                        } else if (!subData[4].equals("Zebak"))
                        {
                            addColorToStatus(green);
                            globalData = new ArrayList<>(globalData.subList(activeIndex, globalData.size()));
                            return;
                        }
                        break;
                    case TOA_CRONDIS_START:
                    case TOA_ZEBAK_START:
                        lastStartTime = Integer.parseInt(subData[4]);
                        break;
                    case TOA_CRONDIS_FINISHED:
                        dataManager.set(CRONDIS_TIME, Integer.parseInt(subData[4]));
                        break;
                    case TOA_ZEBAK_FINISHED:
                        dataManager.set(ZEBAK_TIME, Integer.parseInt(subData[4]));
                        dataManager.set(ZEBAK_ENRAGED_DURATION, dataManager.get(ZEBAK_TIME)-dataManager.get(ZEBAK_ENRAGED_SPLIT));
                        break;
                    case TOA_ZEBAK_ENRAGED:
                        dataManager.set(ZEBAK_ENRAGED_SPLIT, Integer.parseInt(subData[4]));
                        break;
                    case TOA_CRONDIS_CROC_DAMAGE:
                        dataManager.increment(CRONDIS_CROCODILE_DAMAGE, Integer.parseInt(subData[4]));
                        break;
                    case TOA_CRONDIS_WATER:
                        int amount = Integer.parseInt(subData[4]);
                        if (amount == 100)
                        {
                            dataManager.increment(CRONDIS_HEALS_100);
                        } else if (amount == 50)
                        {
                            dataManager.increment(CRONDIS_HEALS_50);
                        } else if (amount == 25)
                        {
                            dataManager.increment(CRONDIS_HEALS_25);
                        }
                        break;
                    case TOA_ZEBAK_JUG_PUSHED:
                        dataManager.increment(ZEBAK_JUGS_PUSHED);
                        break;
                    case TOA_ZEBAK_BOULDER_ATTACK:
                        zebakBoulders.add(Integer.parseInt(subData[4]));
                        dataManager.increment(ZEBAK_BOULDER_ATTACKS);
                        break;
                    case TOA_ZEBAK_WATERFALL_ATTACK:
                        zebakWaterfalls.add(Integer.parseInt(subData[4]));
                        dataManager.increment(ZEBAK_WATERFALL_ATTACKS);
                        break;
                }
                activeIndex++;
            } catch (Exception exception)
            {

            }
        }
    }

    public void parseScabarasPath()
    {
        raidStatus += "K";
        int activeIndex = 0;
        boolean hasSeenNexus = false;
        for (String s : globalData)
        {
            if (activeIndex == 0 && !LogID.valueOf(Integer.parseInt(s.split(",")[3])).equals(LogID.LEFT_TOA))
            {
                activeIndex++;
                continue;
            }
            String[] subData = s.split(",");
            switch (LogID.valueOf(Integer.parseInt(subData[3])))
            {
                case LEFT_TOA:
                    hasExited = true;
                    if (hasSeenNexus)
                    {
                        addColorToStatus(orange);
                    } else
                    {
                        addColorToStatus(red);
                    }
                    raidExitTime = Integer.parseInt(subData[4]);
                    return;
                case ENTERED_NEW_TOA_REGION:
                    if (subData[4].equals("TOA Nexus"))
                    {
                        hasSeenNexus = true;
                    } else if (!subData[4].equals("Kephri"))
                    {
                        addColorToStatus(green);
                        globalData = new ArrayList<>(globalData.subList(activeIndex, globalData.size()));
                        return;
                    }
                    break;
                case TOA_SCABARAS_FINISHED:
                    dataManager.set(SCABARAS_TIME, Integer.parseInt(subData[4]));
                    break;
                case TOA_KEPHRI_PHASE_1_END:
                    dataManager.set(KEPHRI_P1_DURATION, Integer.parseInt(subData[4]));
                    break;
                case TOA_KEPHRI_SWARM_1_END:
                    dataManager.set(KEPHRI_P2_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(KEPHRI_SWARM1_DURATION, dataManager.get(KEPHRI_P2_SPLIT) - dataManager.get(KEPHRI_P1_DURATION));
                    break;
                case TOA_KEPHRI_PHASE_2_END:
                    dataManager.set(KEPHRI_SWARM2_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(KEPHRI_P2_DURATION, dataManager.get(KEPHRI_SWARM2_SPLIT) - dataManager.get(KEPHRI_P2_SPLIT));
                    break;
                case TOA_KEPHRI_SWARM_2_END:
                    dataManager.set(KEPHRI_P3_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(KEPHRI_SWARM2_DURATION, dataManager.get(KEPHRI_P3_SPLIT) - dataManager.get(KEPHRI_SWARM2_SPLIT));
                    break;
                case TOA_KEPHRI_FINISHED:
                    dataManager.set(KEPHRI_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(KEPHRI_P3_DURATION, dataManager.get(KEPHRI_TIME) - dataManager.get(KEPHRI_P3_SPLIT));
                    break;
                case TOA_KEPHRI_DUNG_THROWN:
                    dataManager.increment(KEPHRI_DUNG_THROWN);
                    dungThrows.add(Integer.parseInt(subData[4]));
                    break;
                case TOA_KEPHRI_MELEE_HEAL:
                    dataManager.increment(KEPHRI_MELEE_SCARAB_HEALS);
                    dataManager.decrement(KEPHRI_SWARMS_HEALED);
                    break;
                case TOA_KEPHRI_HEAL:
                    dataManager.increment(KEPHRI_SWARMS_HEALED);
                    break;
                case TOA_KEPHRI_SWARM_SPAWN:
                    dataManager.increment(KEPHRI_SWARMS_TOTAL);
                    break;
                case TOA_KEPHRI_START:
                case TOA_SCABARAS_START:
                    lastStartTime = Integer.parseInt(subData[4]);
                    break;
            }
            activeIndex++;
        }
    }

    public void parseApmekenPath()
    {
        raidStatus += "B";
        int activeIndex = 0;
        boolean hasSeenNexus = false;
        for (String s : globalData)
        {
            if (activeIndex == 0 && !LogID.valueOf(Integer.parseInt(s.split(",")[3])).equals(LogID.LEFT_TOA))
            {
                activeIndex++;
                continue;
            }
            String[] subData = s.split(",");
            switch (LogID.valueOf(Integer.parseInt(subData[3])))
            {
                case LEFT_TOA:
                    hasExited = true;
                    if (hasSeenNexus)
                    {
                        addColorToStatus(orange);
                    } else
                    {
                        addColorToStatus(red);
                    }
                    raidExitTime = Integer.parseInt(subData[4]);
                    return;
                case ENTERED_NEW_TOA_REGION:
                    if (subData[4].equals("TOA Nexus"))
                    {
                        hasSeenNexus = true;
                    } else if (!subData[4].equals("Baba"))
                    {
                        addColorToStatus(green);
                        globalData = new ArrayList<>(globalData.subList(activeIndex, globalData.size()));
                        return;
                    }
                    break;
                case TOA_APMEKEN_FINISHED:
                    dataManager.set(APMEKEN_TIME, Integer.parseInt(subData[4]));
                    break;
                case TOA_BABA_PHASE_1_END:
                    dataManager.set(BABA_P1_DURATION, Integer.parseInt(subData[4]));
                    break;
                case TOA_BABA_BOULDER_1_END:
                    dataManager.set(BABA_P2_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(BABA_BOULDER_1_DURATION, dataManager.get(BABA_P2_SPLIT) - dataManager.get(BABA_P1_DURATION));
                    break;
                case TOA_BABA_PHASE_2_END:
                    dataManager.set(BABA_BOULDER_2_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(BABA_P2_DURATION, dataManager.get(BABA_BOULDER_2_SPLIT) - dataManager.get(BABA_P2_SPLIT));
                    break;
                case TOA_BABA_BOULDER_2_END:
                    dataManager.set(BABA_P3_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(BABA_BOULDER_2_DURATION, dataManager.get(BABA_P3_SPLIT) - dataManager.get(BABA_BOULDER_2_SPLIT));
                    break;
                case TOA_BABA_FINISHED:
                    dataManager.set(BABA_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(BABA_P3_DURATION, dataManager.get(BABA_TIME) - dataManager.get(BABA_P3_SPLIT));
                    break;
                case TOA_APMEKEN_CURSED_SPAWN:
                    dataManager.increment(APMEKEN_CURSED_COUNT);
                    break;
                case TOA_APMEKEN_SHAMAN_SPAWN:
                    dataManager.increment(APMEKEN_SHAMAN_COUNT);
                    break;
                case TOA_APMEKEN_VOLATILE_SPAWN:
                    dataManager.increment(APMEKEN_VOLATILE_COUNT);
                    break;
                case TOA_BABA_BOULDER_THROW:
                    dataManager.increment(BABA_BOULDERS_THROWN);
                    break;
                case TOA_BABA_BOULDER_BROKEN:
                    dataManager.increment(BABA_BOULDERS_BROKEN);
                    break;
                case TOA_BABA_START:
                case TOA_APMEKEN_START:
                    lastStartTime = Integer.parseInt(subData[4]);
                    break;
            }
            activeIndex++;
        }
    }

    public void parseHetPath()
    {
        raidStatus += "A";
        int activeIndex = 0;
        boolean hasSeenNexus = false;
        for (String s : globalData)
        {
            if (activeIndex == 0 && !LogID.valueOf(Integer.parseInt(s.split(",")[3])).equals(LogID.LEFT_TOA))
            {
                activeIndex++;
                continue;
            }
            String[] subData = s.split(",");
            switch (LogID.valueOf(Integer.parseInt(subData[3])))
            {
                case LEFT_TOA:
                    hasExited = true;
                    if (hasSeenNexus)
                    {
                        addColorToStatus(orange);
                    } else
                    {
                        addColorToStatus(red);
                    }
                    raidExitTime = Integer.parseInt(subData[4]);
                    return;
                case ENTERED_NEW_TOA_REGION:
                    if (subData[4].equals("TOA Nexus"))
                    {
                        hasSeenNexus = true;
                    } else if (!subData[4].equals("Akkha"))
                    {
                        addColorToStatus(green);
                        globalData = new ArrayList<>(globalData.subList(activeIndex, globalData.size()));
                        return;
                    }
                    break;
                case TOA_HET_FINISHED:
                    dataManager.set(HET_TIME, Integer.parseInt(subData[4]));
                    break;
                case TOA_HET_DOWN:
                    dataManager.increment(HET_DOWNS);
                    hetDowns.add(Integer.parseInt(subData[4]));
                    break;
                case TOA_AKKHA_PHASE_1_END:
                    dataManager.set(AKKHA_P1_DURATION, Integer.parseInt(subData[4]));
                    break;
                case TOA_AKKHA_SHADOW_1_END:
                    dataManager.set(AKKHA_P2_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(AKKHA_SHADOW_1_DURATION, dataManager.get(AKKHA_P2_SPLIT) - dataManager.get(AKKHA_P1_DURATION));
                    break;
                case TOA_AKKHA_PHASE_2_END:
                    dataManager.set(AKKHA_SHADOW_2_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(AKKHA_P2_DURATION, dataManager.get(AKKHA_SHADOW_2_SPLIT) - dataManager.get(AKKHA_P2_SPLIT));
                    break;
                case TOA_AKKHA_SHADOW_2_END:
                    dataManager.set(AKKHA_P3_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(AKKHA_SHADOW_2_DURATION, dataManager.get(AKKHA_P3_SPLIT) - dataManager.get(AKKHA_SHADOW_2_SPLIT));
                    break;
                case TOA_AKKHA_PHASE_3_END:
                    dataManager.set(AKKHA_SHADOW_3_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(AKKHA_P3_DURATION, dataManager.get(AKKHA_SHADOW_3_SPLIT) - dataManager.get(AKKHA_P3_SPLIT));
                    break;
                case TOA_AKKHA_SHADOW_3_END:
                    dataManager.set(AKKHA_P4_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(AKKHA_SHADOW_3_DURATION, dataManager.get(AKKHA_P4_SPLIT) - dataManager.get(AKKHA_SHADOW_3_SPLIT));
                    break;
                case TOA_AKKHA_PHASE_4_END:
                    dataManager.set(AKKHA_SHADOW_4_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(AKKHA_P4_DURATION, dataManager.get(AKKHA_SHADOW_4_SPLIT) - dataManager.get(AKKHA_P4_SPLIT));
                    break;
                case TOA_AKKHA_SHADOW_4_END:
                    dataManager.set(AKKHA_P5_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(AKKHA_SHADOW_4_DURATION, dataManager.get(AKKHA_P5_SPLIT) - dataManager.get(AKKHA_SHADOW_4_SPLIT));
                    break;
                case TOA_AKKHA_PHASE_5_END:
                    dataManager.set(AKKHA_FINAL_PHASE_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(AKKHA_P5_DURATION, dataManager.get(AKKHA_FINAL_PHASE_SPLIT) - dataManager.get(AKKHA_P5_SPLIT));
                    break;
                case TOA_AKKHA_FINISHED:
                    dataManager.set(AKKHA_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(AKKHA_FINAL_PHASE_DURATION, dataManager.get(AKKHA_TIME) - dataManager.get(AKKHA_FINAL_PHASE_SPLIT));
                    break;
                case TOA_AKKHA_START:
                case TOA_HET_START:
                    lastStartTime = Integer.parseInt(subData[4]);
                    break;
                case TOA_AKKHA_NULLED_HIT_ON_AKKHA:
                case TOA_AKKHA_NULLED_HIT_ON_SHADOW:
                    dataManager.increment(AKKHA_NULL_HIT);
                    dataManager.incrementPlayerSpecific(AKKHA_NULL_HIT, subData[4]);
                    break;
            }
            activeIndex++;
        }
    }

    public void parseWardens()
    {
        int lastSkullCount = 1;
        raidStatus += "W";
        int activeIndex = 0;
        for (String s : globalData)
        {
            if (activeIndex == 0)
            {
                activeIndex++;
                continue;
            }
            String[] subData = s.split(",");
            switch (LogID.valueOf(Integer.parseInt(subData[3])))
            {
                case LEFT_TOA:
                    hasExited = true;
                    if (dataManager.get(WARDENS_TIME) > 0)
                    {
                        addColorToStatus(green);
                    } else
                    {
                        addColorToStatus(red);
                    }
                    raidExitTime = Integer.parseInt(subData[4]);
                    return;
                case ENTERED_NEW_TOA_REGION:
                    if (subData[4].equals("Tomb"))
                    {
                        addColorToStatus(green);
                    }
                    globalData = new ArrayList<>(globalData.subList(activeIndex, globalData.size()));
                    return;
                case TOA_WARDENS_P1_END:
                    dataManager.set(WARDENS_P1_DURATION, Integer.parseInt(subData[4]));
                    break;
                case TOA_WARDENS_P2_END:
                    dataManager.set(WARDENS_P3_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(WARDENS_P2_DURATION, dataManager.get(WARDENS_P3_SPLIT) - dataManager.get(WARDENS_P1_DURATION));
                    break;
                case TOA_WARDENS_ENRAGED:
                    dataManager.set(WARDENS_ENRAGED_SPLIT, Integer.parseInt(subData[4]));
                    dataManager.set(WARDENS_UNTIL_ENRAGED_DURATION, Integer.parseInt(subData[4]) - dataManager.get(WARDENS_P3_SPLIT));
                    break;
                case TOA_WARDENS_FINISHED:
                    dataManager.set(WARDENS_TIME, Integer.parseInt(subData[4]));
                    dataManager.set(WARDENS_ENRAGED_DURATION, dataManager.get(WARDENS_TIME) - dataManager.get(WARDENS_ENRAGED_SPLIT));
                    dataManager.set(WARDENS_P3_DURATION, dataManager.get(WARDENS_TIME) - dataManager.get(WARDENS_P3_SPLIT));
                    break;
                case TOA_WARDENS_SKULLS_STARTED:
                    dataManager.set(DataPoint.getValue("Wardens Skull " + lastSkullCount + " Split"), Integer.parseInt(subData[4])-dataManager.get(WARDENS_P3_SPLIT));
                    break;
                case TOA_WARDENS_SKULLS_ENDED:
                    dataManager.set(DataPoint.getValue("Wardens Skull " + lastSkullCount + " Duration"), Integer.parseInt(subData[4]) - dataManager.get("Wardens Skull " + lastSkullCount + " Split") - dataManager.get(WARDENS_P3_SPLIT));
                    lastSkullCount++;
                    break;
                case TOA_WARDENS_CORE_SPAWNED:
                    if(subData.length > 4)
                    {
                        wardenCoreStarts.add(Integer.parseInt(subData[4]));
                    }
                    break;
                case TOA_WARDENS_CORE_DESPAWNED:
                    if(subData.length > 4)
                    {
                        wardenCoreEnds.add(Integer.parseInt(subData[4]));
                        dataManager.increment(WARDENS_P2_DOWNS);
                    }
                    break;
                case TOA_WARDENS_START:
                    lastStartTime = Integer.parseInt(subData[4]);
                    break;
            }
            activeIndex++;
        }
    }

    @Override
    public String getRaidType()
    {
        return yellow + raidType.name;
    }

    @Override
    public String getFileName()
    {
        return this.fileName;
    }

    @Override
    public String getFilePath()
    {
        return this.filePath;
    }

    @Override
    public int getScale()
    {
        return players.size();
    }

    @Override
    public Date getDate()
    {
        return raidStarted;
    }

    @Override
    public int getValue(String name)
    {
        return dataManager.get(name);
    }

    @Override
    public int getValue(DataPoint point)
    {
        return dataManager.get(point);
    }

    @Override
    public String getScaleString()
    {
        String scale;
        if (players.size() == 1)
        {
            scale = "Solo";
        } else if (players.size() == 2)
        {
            scale = "Duo";
        } else if (players.size() == 3)
        {
            scale = "Trio";
        } else
        {
            scale = players.size() + " Man";
        }
        return scale + " (" + dataManager.get(TOA_INVOCATION_LEVEL) + ")";
    }

    @Override
    public String getRoomStatus()
    {
        return (raidStatus.isEmpty()) ? orange + "Nexus Reset" : raidStatus;
    }

    @Override
    public void setIndex(int index)
    {
        dataManager.set(RAID_INDEX, index);
    }

    @Override
    public int getIndex()
    {
        return dataManager.get(RAID_INDEX);
    }

    @Override
    public String getPlayers()
    {
        StringBuilder playerString = new StringBuilder();
        for (String s : players.keySet())
        {
            playerString.append(s).append(", ");
        }
        return (playerString.length() > 2) ? playerString.substring(0, playerString.length() - 2) : "";
    }

    @Override
    public ArrayList<String> getPlayersArray()
    {
        return new ArrayList<>(players.keySet());
    }

    public String[] getCompletePlayerArray()
    {
        String[] playerArray = new String[8];
        ArrayList<String> arrayListPlayers = getPlayersArray();
        for (int i = 0; i < 8; i++)
        {
            if (i < arrayListPlayers.size())
            {
                playerArray[i] = arrayListPlayers.get(i);
            } else
            {
                playerArray[i] = "";
            }
        }
        return playerArray;
    }

    @Override
    public String getPlayerList(ArrayList<Map<String, ArrayList<String>>> aliases)
    {
        return null;
    }

    @Override
    public String getPlayerList()
    {
        return null;
    }

    @Override
    public int getSpecificTime()
    {
        return 0;
    }

    @Override
    public boolean getOverallTimeAccurate()
    {
        return true;
    }

    @Override
    public PlayerCorrelatedPointData getSpecificTimeInactiveCorrelated(String inactive)
    {
        return null;
    }

    @Override
    public int getSpecificTimeInactive(String inactive)
    {
        return getValue(DataPoint.getValue(inactive));
    }

    @Override
    public boolean getTimeAccurate(DataPoint key)
    {
        switch (key.room)
        {
            case SCABARAS:
                return dataManager.get(SCABARAS_TIME) > 0;
            case APMEKEN:
                return dataManager.get(APMEKEN_TIME) > 0;
            case CRONDIS:
                return dataManager.get(CRONDIS_TIME) > 0;
            case HET:
                return dataManager.get(HET_TIME) > 0;
            case KEPHRI:
                return dataManager.get(KEPHRI_TIME) > 0;
            case BABA:
                return dataManager.get(BABA_TIME) > 0;
            case ZEBAK:
                return dataManager.get(ZEBAK_TIME) > 0;
            case AKKHA:
                return dataManager.get(AKKHA_TIME) > 0;
            case WARDENS:
                return dataManager.get(WARDENS_TIME) > 0;
        }
        return true;
    }

    @Override
    public int getTimeSum()
    {
        return dataManager.get(SCABARAS_TIME)
                + dataManager.get(KEPHRI_TIME)
                + dataManager.get(APMEKEN_TIME)
                + dataManager.get(BABA_TIME)
                + dataManager.get(CRONDIS_TIME)
                + dataManager.get(ZEBAK_TIME)
                + dataManager.get(HET_TIME)
                + dataManager.get(AKKHA_TIME)
                + dataManager.get(WARDENS_TIME);
    }

    public int getOverallTime()
    {
        return (raidExitTime + lastStartTime - raidStartTime);
    }
}
