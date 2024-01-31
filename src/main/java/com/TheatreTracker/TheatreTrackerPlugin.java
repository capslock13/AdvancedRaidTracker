package com.TheatreTracker;

import com.TheatreTracker.constants.NpcIDs;
import com.TheatreTracker.constants.TOBRoom;
import com.TheatreTracker.panelcomponents.LiveChartFrame;
import com.TheatreTracker.ui.RaidTrackerPanelPrimary;
import com.TheatreTracker.utility.*;
import com.TheatreTracker.utility.thrallvengtracking.*;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.kit.KitType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.PartyChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.party.PartyMember;
import net.runelite.client.party.PartyService;
import net.runelite.client.party.WSClient;
import net.runelite.client.party.events.UserJoin;
import net.runelite.client.party.events.UserPart;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;
import com.TheatreTracker.rooms.*;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.plugins.specialcounter.SpecialCounterUpdate;
import net.runelite.client.plugins.specialcounter.SpecialWeapon;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.TheatreTracker.constants.LogID.*;
import static com.TheatreTracker.constants.NpcIDs.*;
import static com.TheatreTracker.constants.TOBRoom.*;



@Slf4j
@PluginDescriptor(
        name = "Theatre of Blood Tracker",
        description = "Tracking and statistics for Theatre of Blood",
        tags = {"timers", "tob", "tracker", "time", "theatre", "analytics"}
)
public class TheatreTrackerPlugin extends Plugin
{
    private NavigationButton navButtonPrimary;
    public DataWriter clog;

    private boolean partyIntact = false;

    @Inject
    private TheatreTrackerConfig config;

    @Inject
    private ItemManager itemManager;

    public TheatreTrackerPlugin() {
    }

    @Provides
    TheatreTrackerConfig getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(TheatreTrackerConfig.class);
    }
    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private ClientThread clientThread;

    @Inject
    private PartyService party;

    @Inject
    private Client client;

    private LobbyHandler lobby;
    private MaidenHandler maiden;
    private BloatHandler bloat;
    private NyloHandler nylo;
    private SotetsegHandler sote;
    private XarpusHandler xarpus;
    private VerzikHandler verzik;

    private ArrayList<DamageQueueShell> queuedThrallDamage;
    private ArrayList<ThrallCurrentDamage> currentThrallDamage;

    private ArrayList<QueuedPlayerAttackLessProjectiles> playersAttacked;


    private final int LOBBY_REGION = 14642;
    private final int MAIDEN_REGION = 12613;
    private final int BLOAT_REGION = 13125;
    private final int NYLO_REGION = 13122;
    private final int SOTETSEG_REGION = 13123;
    private final int SOTETSEG_UNDER_REGION = 13379;
    private final int XARPUS_REGION = 12612;
    private final int VERZIK_REGION = 12611;

    private boolean inTheatre;
    private boolean wasInTheatre;
    private RoomHandler currentRoom;
    int deferredTick;
    private ArrayList<String> currentPlayers;
    private boolean checkDefer = false;
    public static int scale = -1;
    public boolean verzShieldActive = false;

    private ThrallTracker thrallTracker;
    private VengTracker vengTracker;
    private List<PlayerShell> localPlayers;
    private List<ProjectileQueue> activeProjectiles;
    private ArrayList<Projectile> currentTickProjectiles;

    private List<VengDamageQueue> activeVenges;

    @Inject
    private PluginManager pluginManager;

    @Inject
    private EventBus eventBus;

    @Inject
    private WSClient wsClient;

    Map<Player, Integer> activelyPiping;
    public LiveChartFrame liveFrame;

    @Override
    protected void shutDown()
    {
        partyIntact = false;
        clientToolbar.removeNavigation(navButtonPrimary);
    }

    public void addToProjectileQueue(ProjectileQueue queueItem)
    {
        activeProjectiles.add(queueItem);
    }

    public void openLiveFrame()
    {
        liveFrame.open();
    }

    public int getTick()
    {
        return client.getTickCount();
    }
    public boolean isVerzP2()
    {
        if(currentRoom instanceof VerzikHandler)
        {
            VerzikHandler room = (VerzikHandler) currentRoom;
            if(room.roomState == RoomState.VerzikRoomState.PHASE_2 || room.roomState == RoomState.VerzikRoomState.PHASE_2_REDS)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void startUp() throws Exception
    {
        super.startUp();
        localPlayers = new ArrayList<>();
        thrallTracker = new ThrallTracker(this);
        vengTracker = new VengTracker(this);
        activeProjectiles = new ArrayList<>();
        activeVenges = new ArrayList<>();
        queuedThrallDamage = new ArrayList<>();
        currentThrallDamage = new ArrayList<>();
        RaidTrackerPanelPrimary timersPanelPrimary = injector.getInstance(RaidTrackerPanelPrimary.class);
        partyIntact = false;
        activelyPiping = new LinkedHashMap<>();
        liveFrame = new LiveChartFrame();
        playersTextChanged = new ArrayList<>();
        File dirMain = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/primary/");
        File dirFilters = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/filters/");

        File dirRaids = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/raids/");
        if(!dirRaids.exists()) dirRaids.mkdirs();

        if(!dirMain.exists()) dirMain.mkdirs();
        if(!dirFilters.exists()) dirFilters.mkdirs();

        File logFile = new File(System.getProperty("user.home").replace("\\", "/") + "/.runelite/theatretracker/primary/tobdata.log");
        if (!logFile.exists())
        {
            logFile.createNewFile();
        }

        final BufferedImage icon = ImageUtil.loadImageResource(DevToolsPlugin.class, "devtools_icon.png");
        navButtonPrimary = NavigationButton.builder().tooltip("RaidTrackerPanelPrimary").icon(icon).priority(10).panel(timersPanelPrimary).build();

        clientToolbar.addNavigation(navButtonPrimary);

        clog = new DataWriter(client, config);
        lobby = new LobbyHandler(client, clog, config);
        maiden = new MaidenHandler(client, clog, config, this);
        bloat = new BloatHandler(client, clog, config, this);
        nylo = new NyloHandler(client, clog, config, this);
        sote = new SotetsegHandler(client, clog, config, this);
        xarpus = new XarpusHandler(client, clog, config, this);
        verzik = new VerzikHandler(client, clog, config, this);
        inTheatre = false;
        wasInTheatre = false;
        deferredTick = 0;
        currentPlayers = new ArrayList<>();
        currentTickProjectiles = new ArrayList<>();
        playersAttacked = new ArrayList<>();
    }

    /**
     * @return Room as int if inside TOB (0 indexed), -1 otherwise
     */
    private int getRoom() {
        if (inRegion(LOBBY_REGION))
            return -1;
        else if (inRegion(MAIDEN_REGION))
            return 0;
        else if (inRegion(BLOAT_REGION))
            return 1;
        else if (inRegion(NYLO_REGION))
            return 2;
        else if (inRegion(SOTETSEG_REGION) || inRegion(SOTETSEG_UNDER_REGION))
            return 3;
        else if (inRegion(XARPUS_REGION))
            return 4;
        else if (inRegion(VERZIK_REGION))
            return 5;
        return -1;
    }

    private void updateRoom()
    {
        RoomHandler previous = currentRoom;
        int currentRegion = getRoom();
        boolean activeState = false;
        if(inRegion(LOBBY_REGION))
        {
            currentRoom = lobby;
        }
        else if (previous == lobby && inRegion(BLOAT_REGION, NYLO_REGION, SOTETSEG_REGION, XARPUS_REGION, VERZIK_REGION))
        {
            deferredTick = client.getTickCount()+2; //Check two ticks from now for player names in orbs
            clog.write(ENTERED_TOB);
            clog.write(SPECTATE);
            clog.write(LATE_START, String.valueOf(currentRegion));
            liveFrame.resetAll();
        }
        if(inRegion(MAIDEN_REGION))
        {
            if(previous != maiden)
            {
                currentRoom = maiden;
                enteredMaiden(previous);
                liveFrame.resetAll();
            }
            activeState = true;
        }
        else if(inRegion(BLOAT_REGION))
        {
            if(previous != bloat)
            {
                currentRoom = bloat;
                enteredBloat(previous);
            }
            activeState = true;
        }
        else if(inRegion(NYLO_REGION))
        {
            if(previous != nylo)
            {
                currentRoom = nylo;
                enteredNylo(previous);
            }
            activeState = true;
        }
        else if(inRegion(SOTETSEG_REGION, SOTETSEG_UNDER_REGION))
        {
            if(previous != sote)
            {
                currentRoom = sote;
                enteredSote(previous);
            }
            activeState = true;
        }
        else if(inRegion(XARPUS_REGION))
        {
            if(previous != xarpus)
            {
                currentRoom = xarpus;
                enteredXarpus(previous);
            }
            activeState = true;
        }
        else if(inRegion(VERZIK_REGION))
        {
            if(previous != verzik)
            {
                currentRoom = verzik;
                enteredVerzik(previous);
            }
            activeState = true;
        }
        inTheatre = activeState;
    }

    private void enteredMaiden(RoomHandler old)
    {
        clog.write(ENTERED_TOB);
        deferredTick = client.getTickCount()+2;
        maiden.reset();
        liveFrame.tabbedPane.setSelectedIndex(0);
    }

    private void enteredBloat(RoomHandler old)
    {
        clog.write(ENTERED_NEW_TOB_REGION, TOBRoom.BLOAT.ordinal());
        maiden.reset();
        bloat.reset();
        liveFrame.tabbedPane.setSelectedIndex(1);
    }

    private void enteredNylo(RoomHandler old)
    {
        clog.write(ENTERED_NEW_TOB_REGION, NYLO.ordinal());
        bloat.reset();
        nylo.reset();
        liveFrame.tabbedPane.setSelectedIndex(2);
    }

    private void enteredSote(RoomHandler old)
    {
        clog.write(ENTERED_NEW_TOB_REGION, SOTE.ordinal());
        nylo.reset();
        sote.reset();
        liveFrame.tabbedPane.setSelectedIndex(3);
    }

    private void enteredXarpus(RoomHandler old)
    {
        clog.write(ENTERED_NEW_TOB_REGION, XARPUS.ordinal());
        sote.reset();
        xarpus.reset();
        liveFrame.tabbedPane.setSelectedIndex(4);
    }

    private void enteredVerzik(RoomHandler old)
    {
        clog.write(ENTERED_NEW_TOB_REGION, VERZIK.ordinal());
        xarpus.reset();
        verzik.reset();
        liveFrame.tabbedPane.setSelectedIndex(5);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        if(event.getGameState() == GameState.LOGGED_IN)
        {
            checkDefer = true;
        }
    }

    @Subscribe
    public void onSpecialCounterUpdate(SpecialCounterUpdate event)
    {
        if(inTheatre)
        {
            String name = party.getMemberById(event.getMemberId()).getDisplayName();
            if (name == null) {
                return;
            }
            boolean playerInRaid = false;
            // Ensures correct names across encodings
            for(String player : currentPlayers)
            {
                if (name.equals(player.replaceAll(String.valueOf((char) 160), String.valueOf((char) 32)))) {
                    playerInRaid = true;
                    break;
                }
            }
            if(playerInRaid)
            {
                if(event.getWeapon().equals(SpecialWeapon.BANDOS_GODSWORD))
                {
                    clog.write(BGS, name, ""+event.getHit(), String.valueOf(client.getTickCount()-currentRoom.roomStartTick));
                }
                if(event.getWeapon().equals(SpecialWeapon.DRAGON_WARHAMMER))
                {
                    clog.write(DWH, name, String.valueOf(client.getTickCount()-currentRoom.roomStartTick));
                }
            }
        }
    }

    private String cleanString(String s1)
    {
        return s1.replaceAll(String.valueOf((char) 160), String.valueOf((char) 32));
    }

    private boolean isPartyComplete()
    {
        if(currentPlayers.size() > party.getMembers().size())
        {
            return false;
        }
        for(String raidPlayer : currentPlayers)
        {
            boolean currentPlayerMatched = false;
            for(PartyMember partyPlayer : party.getMembers())
            {
                if(cleanString(raidPlayer).equals(partyPlayer.getDisplayName()))
                {
                    currentPlayerMatched = true;
                }
            }
            if(!currentPlayerMatched)
            {
                return false;
            }
        }
        return true;
    }

    private void checkPartyUpdate()
    {
        checkPartyUpdate(false);
    }
    private void checkPartyUpdate(boolean preMaiden)
    {
        if(inTheatre)
        {
            if(partyIntact)
            {
                if (!isPartyComplete())
                {
                    partyIntact = false;
                    clog.write(PARTY_INCOMPLETE);
                }
            }
            else
            {
                if(isPartyComplete())
                {
                    partyIntact = true;
                    clog.write(PARTY_COMPLETE);
                }
            }
        }

    }

    @Subscribe
    public void onPartyChanged(final PartyChanged party)
    {

        checkPartyUpdate();
    }

    @Subscribe
    public void onUserPart(final UserPart event)
    {
        checkPartyUpdate();
    }

    @Subscribe
    public void onUserJoin(final UserJoin event)
    {
        checkPartyUpdate();
    }

    public void addQueuedThrallDamage(int targetIndex, int sourceIndex, int offset, String source)
    {
        queuedThrallDamage.add(new DamageQueueShell(targetIndex, sourceIndex, offset, source, client.getTickCount()));
    }

    public void removeDeadProjectiles()
    {
        activeProjectiles.removeIf(projectileQueue -> projectileQueue.finalTick <= client.getTickCount());
    }

    public void removeDeadVenges()
    {
        activeVenges.removeIf(vengDamageQueue -> vengDamageQueue.appliedTick <= client.getTickCount());
    }

    public void addLiveLine(int room, int value, String description)
    {
        switch(room)
        {
            case 0:
                liveFrame.addMaidenLine(value, description);
                break;
            case 1:
                liveFrame.addBloatLine(value, description);
                break;
            case 2:
                liveFrame.addNyloLine(value, description);
                break;
            case 3:
                liveFrame.addSoteLine(value, description);
                break;
            case 4:
                liveFrame.addXarpLine(value, description);
                break;
            case 5:
                liveFrame.addVerzikLine(value, description);
                break;
        }
    }

    public void thrallAttackedP2VerzikShield(int tickOffset)
    {
        if(currentRoom instanceof VerzikHandler)
        {
            VerzikHandler room = (VerzikHandler) currentRoom;
            room.thrallAttackedShield(client.getTickCount()+tickOffset);
        }
    }

    public void addThrallOutlineBox(ThrallOutlineBox outlineBox)
    {
        clog.write(THRALL_SPAWN, outlineBox.owner, String.valueOf(outlineBox.spawnTick), String.valueOf(outlineBox.id));
        liveFrame.getPanel(currentRoom.getName()).addThrallOutlineBox(outlineBox);
    }

    public int getRoomTick()
    {
        return client.getTickCount()-currentRoom.roomStartTick;
    }


    @Subscribe
    public void onGameTick(GameTick event) throws PluginInstantiationException
    {
        for(Player p : activelyPiping.keySet())
        {
            if(client.getTickCount() > (activelyPiping.get(p)+1) && ((client.getTickCount()-activelyPiping.get(p)-1)%2)==0)
            {//
                int interactedIndex = -1;
                int interactedID = -1;
                Actor interacted = p.getInteracting();
                if(interacted instanceof NPC)
                {
                    NPC npc = (NPC) interacted;
                    interactedID = npc.getId();
                    interactedIndex = npc.getIndex();
                }
                clog.write(PLAYER_ATTACK,
                        p.getName()+":"+(client.getTickCount() - currentRoom.roomStartTick-1),
                        String.valueOf(p.getAnimation()),
                        "",
                        p.getPlayerComposition().getEquipmentId(KitType.WEAPON)+":"+interactedIndex+":"+interactedID,
                        "-1");
                liveFrame.addAttack(new PlayerDidAttack(
                        String.valueOf(p.getName()),
                        String.valueOf(p.getAnimation()),
                        -1,
                        String.valueOf(p.getPlayerComposition().getEquipmentId(KitType.WEAPON)),
                        "-1",
                        "",
                        interactedIndex,
                        interactedID
                ), currentRoom.getName());
            }
        }

        for(QueuedPlayerAttackLessProjectiles playerAttackQueuedItem : playersAttacked)
        {
            playerAttackQueuedItem.tick--;
            if(playerAttackQueuedItem.tick == 0)
            {
                for(Projectile projectile : client.getProjectiles())
                {
                    int offset = 41; //zcb
                    if(projectile.getId() == 1544 || projectile.getId() == 1547)
                    {
                        offset = 51; //dawnbringer
                    }
                    if(projectile.getStartCycle() == client.getGameCycle()+offset)
                    {
                        WorldPoint position = WorldPoint.fromLocal(client, new LocalPoint(projectile.getX1(), projectile.getY1()));
                        if (position.distanceTo(playerAttackQueuedItem.player.getWorldLocation()) == 0)
                        {
                            if(projectile.getId() == 1547 || projectile.getId() == 1544)
                            {
                                int projectileHitTick = projectile.getRemainingCycles();
                                projectileHitTick = (projectileHitTick / 30);
                                clog.write(DAWN_SPEC, playerAttackQueuedItem.player.getName(), String.valueOf(client.getTickCount()-currentRoom.roomStartTick+projectileHitTick+1));

                            }
                            if (projectile.getId() == 1547 || projectile.getId() == 1995 || projectile.getId() == 1468 || projectile.getId() == 1544)
                            {
                                int interactedIndex = -1;
                                int interactedID = -1;
                                Actor interacted = playerAttackQueuedItem.player.getInteracting();
                                if(interacted instanceof NPC)
                                {
                                    NPC npc = (NPC) interacted;
                                    interactedID = npc.getId();
                                    interactedIndex = npc.getIndex();
                                }
                                clog.write(PLAYER_ATTACK,
                                        playerAttackQueuedItem.player.getName() + ":" + (client.getTickCount() - currentRoom.roomStartTick),
                                        playerAttackQueuedItem.animation,
                                        playerAttackQueuedItem.spotAnims,
                                        playerAttackQueuedItem.weapon+":"+interactedIndex+":"+interactedID,
                                        String.valueOf(projectile.getId()));
                                liveFrame.addAttack(new PlayerDidAttack(
                                        playerAttackQueuedItem.player.getName(),
                                        playerAttackQueuedItem.animation,
                                        0,
                                        playerAttackQueuedItem.weapon,
                                        String.valueOf(projectile.getId()),
                                        playerAttackQueuedItem.spotAnims,
                                        interactedIndex,
                                        interactedID)
                                , currentRoom.getName());
                            }
                        }
                    }
                }
            }
        }
        playersAttacked.removeIf(p->p.tick ==0);
        removeDeadProjectiles();
        removeDeadVenges();
        playersTextChanged.clear();
        localPlayers.clear();
        for(Player p : client.getPlayers())
        {
            localPlayers.add(new PlayerShell(p.getWorldLocation(), p.getName()));
            thrallTracker.updatePlayerInteracting(p.getName(), p.getInteracting());
        }
        for(DamageQueueShell damage : queuedThrallDamage)
        {
            damage.offset--;
        }
        thrallTracker.updateTick();
        vengTracker.updateTick();
        updateRoom();
        if(inTheatre)
        {
            wasInTheatre = true;
            currentRoom.updateGameTick(event);

            if(currentRoom.isActive())
            {
                liveFrame.incrementTick(currentRoom.getName());
            }

            if(client.getTickCount() == deferredTick)
            {
                String[] players = {"", "", "", "", ""};
                int varcStrID = 330; // Widget for player names
                for(int i = varcStrID; i < varcStrID+5; i++)
                {
                    if(client.getVarcStrValue(i) != null && !client.getVarcStrValue(i).equals(""))
                    {
                        players[i-varcStrID] = Text.escapeJagex(client.getVarcStrValue(i));
                    }
                }
                for(String s : players)
                {
                    if(!s.equals(""))
                    {
                        currentPlayers.add(s);
                    }
                }
                liveFrame.setPlayers(currentPlayers);
                checkPartyUpdate(true);
                boolean flag = false;
                for(String p : players)
                {
                    if(p.replaceAll(String.valueOf((char) 160), String.valueOf((char) 32)).equals(client.getLocalPlayer().getName().replaceAll(String.valueOf((char) 160), String.valueOf((char) 32))))
                    {
                        flag = true;
                    }
                }
                deferredTick = 0;
                if(!flag)
                {
                    clog.write(SPECTATE);
                }
                clog.write(PARTY_MEMBERS, players[0], players[1], players[2], players[3], players[4]);
                maiden.setScale(Arrays.stream(players).filter(x -> !x.equals("")).collect(Collectors.toList()).size());
                scale = currentPlayers.size();
            }
        }
        else
        {
            if(wasInTheatre)
            {
                leftRaid();
                wasInTheatre = false;
            }
        }
    }

    public void leftRaid()
    {
        partyIntact = false;
        currentPlayers.clear();
        clog.write(LEFT_TOB); //todo add region
        currentRoom = null;
        activelyPiping.clear();
    }

    @Subscribe
    public void onActorDeath(ActorDeath event)
    {
        if(inTheatre)
        {
            Actor a = event.getActor();
            if(a instanceof Player)
            {
                clog.write(PLAYER_DIED, event.getActor().getName(), String.valueOf(client.getTickCount()-currentRoom.roomStartTick));
            }
        }
    }

    @Subscribe
    public void onGraphicChanged(GraphicChanged event)
    {
        if(event.getActor() instanceof Player)
        {
            int id = -1;
            if(event.getActor().hasSpotAnim(THRALL_CAST_GRAPHIC_MAGE))
            {
                id = THRALL_CAST_GRAPHIC_MAGE;
            }
            else if(event.getActor().hasSpotAnim(THRALL_CAST_GRAPHIC_MELEE))
            {
                id = THRALL_CAST_GRAPHIC_MELEE;
            }
            else if(event.getActor().hasSpotAnim(THRALL_CAST_GRAPHIC_RANGE))
            {
                id = THRALL_CAST_GRAPHIC_RANGE;
            }
            else if(event.getActor().hasSpotAnim(VENG_GRAPHIC))
            {
                vengTracker.vengSelfGraphicApplied((Player)event.getActor());
            }
            else if(event.getActor().hasSpotAnim(VENG_OTHER_GRAPHIC))
            {
                vengTracker.vengOtherGraphicApplied((Player)event.getActor());
            }
            if(id != -1)
            {
                thrallTracker.playerHasThrallCastSpotAnim((Player) event.getActor(), id);
            }

        }
        if(inTheatre)
        {
            currentRoom.updateGraphicChanged(event);
        }
    }

    @Subscribe
    public void onGraphicsObjectCreated(GraphicsObjectCreated event)
    {
        if(inTheatre)
        {
            currentRoom.updateGraphicsObjectCreated(event);
        }
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event)
    {
        if(inTheatre)
        {
            currentRoom.updateGameObjectSpawned(event);
        }
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event)
    {
        if(inTheatre)
        {
            currentRoom.updateGameObjectDespawned(event);
        }
    }

    @Subscribe
    public void onItemSpawned(ItemSpawned event)
    {
        if(inTheatre)
        {
            currentRoom.updateItemSpawned(event);
        }
    }

    @Subscribe
    public void onProjectileMoved(ProjectileMoved event)
    {
        if(inTheatre)
        {
            int id = event.getProjectile().getId();
            if (id == THRALL_PROJECTILE_RANGE || id == THRALL_PROJECTILE_MAGE)
            {
                if (event.getProjectile().getStartCycle() == client.getGameCycle()) {
                    thrallTracker.projectileCreated(event.getProjectile(), WorldPoint.fromLocal(client, new LocalPoint(event.getProjectile().getX1(), event.getProjectile().getY1())), event.getProjectile().getInteracting().getWorldLocation(), client.getTickCount());
                }
            }
            //Thrall hitsplats come before damage hitsplits unless the source is a projectile that was spawned on a tick before the thrall projectile spawned
            else if (event.getProjectile().getStartCycle() == client.getGameCycle())
            { //Thrall projectiles move slower and the only time this situation occurs in TOB is max distance TBOW/ZCB during maiden
                if (id == TBOW_PROJECTILE || id == ZCB_PROJECTILE || id == ZCB_SPEC_PROJECTILE)
                { //Not sure why 10 is correct instead of 19 (60 - 41 tick delay) but extensive trial and error shows this to be accurate
                    int projectileHitTick = 10 + event.getProjectile().getRemainingCycles();
                    projectileHitTick = (projectileHitTick / 30);
                    if (event.getProjectile().getInteracting() instanceof NPC) {
                        int index = ((NPC) event.getProjectile().getInteracting()).getIndex();
                        activeProjectiles.add(new ProjectileQueue(client.getTickCount(), projectileHitTick + client.getTickCount(), index));
                    }
                }
            }
            if (inTheatre)
            {
                currentRoom.updateProjectileMoved(event);
            }
        }
    }

    private void sendChatMessage(String msg)
    {
        clientThread.invoke(() -> client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", msg, null, false));
    }


    
    @Subscribe
    public void onAnimationChanged(AnimationChanged event)
    {
        Player p;
        if (event.getActor() instanceof Player && inTheatre)
        {
            p = (Player) event.getActor();
            if(p.getPlayerComposition() != null)
            {
                int id = p.getPlayerComposition().getEquipmentId(KitType.WEAPON);
                if (event.getActor().getAnimation() == 8056)
                {
                    if (id == 22486 || id == 25738 || id == 25741)
                    {
                        if(config.showMistakesInChat())
                        {
                            sendChatMessage(event.getActor().getName() + " is using an uncharged scythe");
                        }
                    }
                } else if (event.getActor().getAnimation() == 401)
                {
                    if (id == 13576 || id == 20785)
                    {
                        if(config.showMistakesInChat())
                        {
                            sendChatMessage(event.getActor().getName() + " hammer bopped (bad rng)");
                        }
                        clog.write(DWH_BOP, event.getActor().getName());
                    }
                } else if (event.getActor().getAnimation() == 414)
                {
                    if (id == 21006 || id == 23626)
                    {
                        if(config.showMistakesInChat())
                        {
                            sendChatMessage(event.getActor().getName() + " kodai bopped (nothing they could've done to prevent it)");
                        }
                        clog.write(KODAI_BOP, event.getActor().getName());
                    }
                }
                else if (event.getActor().getAnimation() == 440)
                {
                    if (id == 23987)
                    {
                        if(config.showMistakesInChat())
                        {
                            sendChatMessage(event.getActor().getName() + " chally poked");
                        }
                        clog.write(CHALLY_POKE, event.getActor().getName());
                    }
                } else if (event.getActor().getAnimation() == 7045)
                {
                    if(config.showMistakesInChat())
                    {
                        sendChatMessage(event.getActor().getName() + " swung BGS without speccing");
                    }
                    clog.write(BGS_WHACK, event.getActor().getName());
                }
                String animations = "";
                for(ActorSpotAnim anim : p.getSpotAnims())
                {
                    animations += String.valueOf(anim.getId());
                    animations += ":";
                }
                if(p.getAnimation() == 1167 || p.getAnimation() == 9168)
                {
                    if(p.getAnimation() != 1167 || p.getPlayerComposition().getEquipmentId(KitType.WEAPON) == 22516)
                    {
                        WorldPoint worldPoint = p.getWorldLocation();
                        playersAttacked.add(new QueuedPlayerAttackLessProjectiles(p, worldPoint, 1, animations, String.valueOf(p.getPlayerComposition().getEquipmentId(KitType.WEAPON)), String.valueOf(p.getAnimation())));
                    }
                    else
                    {
                        int interactedIndex = -1;
                        int interactedID = -1;
                        Actor interacted = p.getInteracting();
                        if(interacted instanceof NPC)
                        {
                            NPC npc = (NPC) interacted;
                            interactedID = npc.getId();
                            interactedIndex = npc.getIndex();
                        }
                        clog.write(PLAYER_ATTACK,
                                p.getName()+":"+(client.getTickCount() - currentRoom.roomStartTick),
                                String.valueOf(p.getAnimation()),
                                animations,
                                p.getPlayerComposition().getEquipmentId(KitType.WEAPON)+":"+interactedIndex+":"+interactedID,
                                "-1");
                        liveFrame.addAttack(new PlayerDidAttack(
                                String.valueOf(p.getName()),
                                String.valueOf(p.getAnimation()),
                                0,
                                String.valueOf(p.getPlayerComposition().getEquipmentId(KitType.WEAPON)),
                                "-1",
                                animations,
                                interactedIndex,
                                interactedID
                        ), currentRoom.getName());
                    }
                }
                else if(p.getAnimation() != -1)
                {
                    int interactedIndex = -1;
                    int interactedID = -1;
                    Actor interacted = p.getInteracting();
                    if(interacted instanceof NPC)
                    {
                        NPC npc = (NPC) interacted;
                        interactedID = npc.getId();
                        interactedIndex = npc.getIndex();
                    }
                    clog.write(PLAYER_ATTACK,
                            p.getName()+":"+(client.getTickCount() - currentRoom.roomStartTick),
                            String.valueOf(p.getAnimation()),
                            animations,
                            p.getPlayerComposition().getEquipmentId(KitType.WEAPON)+":"+interactedIndex+":"+interactedID,
                            "-1");
                    liveFrame.addAttack(new PlayerDidAttack(
                            String.valueOf(p.getName()),
                            String.valueOf(p.getAnimation()),
                            0,
                            String.valueOf(p.getPlayerComposition().getEquipmentId(KitType.WEAPON)),
                            "-1",
                            animations,
                            interactedIndex,
                            interactedID
                    ), currentRoom.getName());
                    if(p.getAnimation() == 5061 || p.getAnimation() == 10656)
                    {
                        activelyPiping.put(p, client.getTickCount());
                    }
                }
                else
                {
                    activelyPiping.remove(p);
                }

            }
        }
        if(inTheatre)
        {
            int id = event.getActor().getAnimation();
            if (event.getActor().getAnimation() == THRALL_CAST_ANIMATION)
            {
                thrallTracker.castThrallAnimation((Player) event.getActor());
            } else if (event.getActor().getAnimation() == MELEE_THRALL_ATTACK_ANIMATION && event.getActor() instanceof NPC)
            {
                thrallTracker.meleeThrallAttacked((NPC) event.getActor());
            } else if (event.getActor().getAnimation() == VENG_CAST)
            {
                vengTracker.vengSelfCast((Player) event.getActor());
            } else if (event.getActor().getAnimation() == VENG_OTHER_CAST)
            {
                vengTracker.vengOtherCast((Player) event.getActor());
            } else if (id == DWH_SPEC)
            {
                clog.write(HAMMER_ATTEMPTED, event.getActor().getName());
            } else if (event.getActor().getName() != null && event.getActor().getName().contains("Maiden") && id == MAIDEN_BLOOD_THROW_ANIM)
            {
                clog.write(BLOOD_THROWN);
            }
            if (inTheatre)
            {
                currentRoom.updateAnimationChanged(event);
            }
        }
    }

    @Subscribe
    public void onInteractingChanged(InteractingChanged event)
    {
        if(inTheatre)
        {
            currentRoom.updateInteractingChanged(event);
        }
    }

    @Subscribe
    public void onNpcChanged(NpcChanged event)
    {
        if(inTheatre)
        {
            currentRoom.handleNPCChanged(event.getNpc().getId());
        }
    }

    private void handleThrallSpawn(NPC npc)
    {
        ArrayList<PlayerShell> potentialPlayers = new ArrayList<>();
        for(PlayerShell p : localPlayers)
        {
            if(p.worldLocation.distanceTo(npc.getWorldLocation()) == 1)
            {
                potentialPlayers.add(p);
            }
        }
        thrallTracker.thrallSpawned(npc, potentialPlayers);
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event)
    {
        int id = event.getNpc().getId();
        if(id == MELEE_THRALL || id == RANGE_THRALL || id == MAGE_THRALL)
        {
            handleThrallSpawn(event.getNpc());
        }
        switch(event.getNpc().getId())
        {
            case NpcIDs.MAIDEN_P0:
            case NpcIDs.MAIDEN_P1:
            case NpcIDs.MAIDEN_P2:
            case NpcIDs.MAIDEN_P3:
            case NpcIDs.MAIDEN_PRE_DEAD:
            case NpcIDs.MAIDEN_DEAD:
            case NpcIDs.MAIDEN_MATOMENOS:
            case NpcIDs.MAIDEN_P0_HM:
            case NpcIDs.MAIDEN_P1_HM:
            case NpcIDs.MAIDEN_P2_HM:
            case NpcIDs.MAIDEN_P3_HM:
            case NpcIDs.MAIDEN_PRE_DEAD_HM:
            case NpcIDs.MAIDEN_DEAD_HM:
            case NpcIDs.MAIDEN_MATOMENOS_HM:
            case NpcIDs.MAIDEN_P0_SM:
            case NpcIDs.MAIDEN_P1_SM:
            case NpcIDs.MAIDEN_P2_SM:
            case NpcIDs.MAIDEN_P3_SM:
            case NpcIDs.MAIDEN_PRE_DEAD_SM:
            case NpcIDs.MAIDEN_DEAD_SM:
            case NpcIDs.MAIDEN_MATOMENOS_SM:
            case NpcIDs.MAIDEN_BLOOD:
            case NpcIDs.MAIDEN_BLOOD_HM:
            case NpcIDs.MAIDEN_BLOOD_SM:
            {
                maiden.updateNpcSpawned(event);
            }
                break;
            case NpcIDs.BLOAT:
            case NpcIDs.BLOAT_HM:
            case NpcIDs.BLOAT_SM:
                bloat.updateNpcSpawned(event);
                break;
            case NpcIDs.NYLO_MELEE_SMALL:
            case NpcIDs.NYLO_MELEE_SMALL_AGRO:
            case NpcIDs.NYLO_RANGE_SMALL:
            case NpcIDs.NYLO_RANGE_SMALL_AGRO:
            case NpcIDs.NYLO_MAGE_SMALL:
            case NpcIDs.NYLO_MAGE_SMALL_AGRO:
            case NpcIDs.NYLO_MELEE_BIG:
            case NpcIDs.NYLO_MELEE_BIG_AGRO:
            case NpcIDs.NYLO_RANGE_BIG:
            case NpcIDs.NYLO_RANGE_BIG_AGRO:
            case NpcIDs.NYLO_MAGE_BIG:
            case NpcIDs.NYLO_MAGE_BIG_AGRO:
            case NpcIDs.NYLO_MELEE_SMALL_HM:
            case NpcIDs.NYLO_MELEE_SMALL_AGRO_HM:
            case NpcIDs.NYLO_RANGE_SMALL_HM:
            case NpcIDs.NYLO_RANGE_SMALL_AGRO_HM:
            case NpcIDs.NYLO_MAGE_SMALL_HM:
            case NpcIDs.NYLO_MAGE_SMALL_AGRO_HM:
            case NpcIDs.NYLO_MELEE_BIG_HM:
            case NpcIDs.NYLO_MELEE_BIG_AGRO_HM:
            case NpcIDs.NYLO_RANGE_BIG_HM:
            case NpcIDs.NYLO_RANGE_BIG_AGRO_HM:
            case NpcIDs.NYLO_MAGE_BIG_HM:
            case NpcIDs.NYLO_MAGE_BIG_AGRO_HM:
            case NpcIDs.NYLO_MELEE_SMALL_SM:
            case NpcIDs.NYLO_MELEE_SMALL_AGRO_SM:
            case NpcIDs.NYLO_RANGE_SMALL_SM:
            case NpcIDs.NYLO_RANGE_SMALL_AGRO_SM:
            case NpcIDs.NYLO_MAGE_SMALL_SM:
            case NpcIDs.NYLO_MAGE_SMALL_AGRO_SM:
            case NpcIDs.NYLO_MELEE_BIG_SM:
            case NpcIDs.NYLO_MELEE_BIG_AGRO_SM:
            case NpcIDs.NYLO_RANGE_BIG_SM:
            case NpcIDs.NYLO_RANGE_BIG_AGRO_SM:
            case NpcIDs.NYLO_MAGE_BIG_SM:
            case NpcIDs.NYLO_MAGE_BIG_AGRO_SM:
            case NpcIDs.NYLO_BOSS_DROPPING:
            case NpcIDs.NYLO_BOSS_DROPPING_HM:
            case NpcIDs.NYLO_BOSS_DROPING_SM:
            case NpcIDs.NYLO_BOSS_MELEE:
            case NpcIDs.NYLO_BOSS_MELEE_HM:
            case NpcIDs.NYLO_BOSS_MELEE_SM:
            case NpcIDs.NYLO_BOSS_MAGE:
            case NpcIDs.NYLO_BOSS_MAGE_HM:
            case NpcIDs.NYLO_BOSS_MAGE_SM:
            case NpcIDs.NYLO_BOSS_RANGE:
            case NpcIDs.NYLO_BOSS_RANGE_HM:
            case NpcIDs.NYLO_BOSS_RANGE_SM:
            case NpcIDs.NYLO_PRINKIPAS_DROPPING:
            case NpcIDs.NYLO_PRINKIPAS_MELEE:
            case NpcIDs.NYLO_PRINKIPAS_MAGIC:
            case NpcIDs.NYLO_PRINKIPAS_RANGE:
                nylo.updateNpcSpawned(event);
                break;
            case NpcIDs.SOTETSEG_ACTIVE:
            case NpcIDs.SOTETSEG_ACTIVE_HM:
            case NpcIDs.SOTETSEG_ACTIVE_SM:
            case NpcIDs.SOTETSEG_INACTIVE:
            case NpcIDs.SOTETSEG_INACTIVE_HM:
            case NpcIDs.SOTETSEG_INACTIVE_SM:
                sote.updateNpcSpawned(event);
                break;
            case NpcIDs.XARPUS_INACTIVE:
            case NpcIDs.XARPUS_P1:
            case NpcIDs.XARPUS_P23:
            case NpcIDs.XARPUS_DEAD:
            case NpcIDs.XARPUS_INACTIVE_HM:
            case NpcIDs.XARPUS_P1_HM:
            case NpcIDs.XARPUS_P23_HM:
            case NpcIDs.XARPUS_DEAD_HM:
            case NpcIDs.XARPUS_INACTIVE_SM:
            case NpcIDs.XARPUS_P1_SM:
            case NpcIDs.XARPUS_P23_SM:
            case NpcIDs.XARPUS_DEAD_SM:
                xarpus.updateNpcSpawned(event);
                break;
            case NpcIDs.VERZIK_P1_INACTIVE:
            case NpcIDs.VERZIK_P1:
            case NpcIDs.VERZIK_P2_INACTIVE:
            case NpcIDs.VERZIK_P2:
            case NpcIDs.VERZIK_P3_INACTIVE:
            case NpcIDs.VERZIK_P3:
            case NpcIDs.VERZIK_DEAD:
            case NpcIDs.VERZIK_P1_INACTIVE_HM:
            case NpcIDs.VERZIK_P1_HM:
            case NpcIDs.VERZIK_P2_INACTIVE_HM:
            case NpcIDs.VERZIK_P2_HM:
            case NpcIDs.VERZIK_P3_INACTIVE_HM:
            case NpcIDs.VERZIK_P3_HM:
            case NpcIDs.VERZIK_DEAD_HM:
            case NpcIDs.VERZIK_P1_INACTIVE_SM:
            case NpcIDs.VERZIK_P1_SM:
            case NpcIDs.VERZIK_P2_INACTIVE_SM:
            case NpcIDs.VERZIK_P2_SM:
            case NpcIDs.VERZIK_P3_INACTIVE_SM:
            case NpcIDs.VERZIK_P3_SM:
            case NpcIDs.VERZIK_DEAD_SM:
                verzik.updateNpcSpawned(event);
                break;
            default:
                if(currentRoom != null)
                {
                    currentRoom.updateNpcSpawned(event);
                }
                break;
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event)
    {
        int id = event.getNpc().getId();
        if(id == MELEE_THRALL || id == RANGE_THRALL || id == MAGE_THRALL)
        {
            thrallTracker.removeThrall(event.getNpc());
        }
        if(inTheatre)
        {
            currentRoom.updateNpcDespawned(event);
        }
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied event)
    {
        if(inTheatre)
        {
            if (event.getActor() instanceof Player && inTheatre)
            {
                playersTextChanged.add(new vengpair(event.getActor().getName(), event.getHitsplat().getAmount()));
            }
            queuedThrallDamage.sort(Comparator.comparing(DamageQueueShell::getSourceIndex));
            int index = -1;
            if (event.getActor() instanceof NPC && event.getHitsplat().getHitsplatType() != HitsplatID.HEAL)
            {
                for (int i = 0; i < queuedThrallDamage.size(); i++)
                {
                    int altIndex = 0;
                    int matchedIndex = -1;
                    boolean postponeThrallHit = false;
                    for (ProjectileQueue projectile : activeProjectiles)
                    {
                        if (projectile.targetIndex == ((NPC) event.getActor()).getIndex())
                        {
                            if (client.getTickCount() == projectile.finalTick)
                            {
                                if (projectile.originTick < queuedThrallDamage.get(i).originTick)
                                {
                                    postponeThrallHit = true;
                                    matchedIndex = altIndex;
                                }
                            }
                        }
                        altIndex++;
                    }
                    if (queuedThrallDamage.get(i).offset == 0 && queuedThrallDamage.get(i).targetIndex == ((NPC) event.getActor()).getIndex())
                    {
                        if (postponeThrallHit)
                        {
                            activeProjectiles.remove(matchedIndex);
                        } else
                        {
                            if (event.getHitsplat().getAmount() > 3)
                            {
                            }
                            else
                            {
                                index = i;
                                clog.write(THRALL_DAMAGED, queuedThrallDamage.get(i).source, String.valueOf(event.getHitsplat().getAmount()));

                            }
                        }
                        if (index != -1)
                        {
                            queuedThrallDamage.remove(index);
                        }
                        if (inTheatre)
                        {
                            currentRoom.updateHitsplatApplied(event);
                        }
                        return;
                    }
                }
                for (VengDamageQueue veng : activeVenges)
                {
                    int expectedDamage = (int) (0.75 * veng.damage);
                    if (event.getHitsplat().getAmount() == expectedDamage)
                    {
                        clog.write(VENG_WAS_PROCCED, veng.target, String.valueOf(expectedDamage));
                        if (inTheatre)
                        {
                            currentRoom.updateHitsplatApplied(event);
                        }
                        return;
                    }
                }
            }

            if (inTheatre)
            {
                currentRoom.updateHitsplatApplied(event);
            }
        }
    }

    private ArrayList<vengpair> playersTextChanged;

    @Subscribe
    public void onOverheadTextChanged(OverheadTextChanged event)
    {
        if(inTheatre)
        {
            if (event.getOverheadText().equals("Taste vengeance!"))
            {
                for (vengpair vp : playersTextChanged)
                {
                    if (vp.player.equals(event.getActor().getName()))
                    {
                        vengTracker.vengProcced(vp);
                        activeVenges.add(new VengDamageQueue(vp.player, vp.hitsplat, client.getTickCount() + 1));
                    }
                }
            }
            if (currentRoom instanceof XarpusHandler)
            {
                xarpus.updateOverheadText(event);
            }
        }
    }

    public boolean inRegion(int... regions)
    {
        if (client.getMapRegions() != null)
        {
            for (int currentRegion : client.getMapRegions())
            {
                for (int region : regions)
                {
                    if (currentRegion == region)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
