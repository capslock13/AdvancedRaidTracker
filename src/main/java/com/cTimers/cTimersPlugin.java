package com.cTimers;

import com.cTimers.constants.NpcIDs;
import com.cTimers.ui.cTimersPanelPrimary;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.PartyChanged;
import net.runelite.client.party.PartyMember;
import net.runelite.client.party.PartyService;
import net.runelite.client.party.events.UserJoin;
import net.runelite.client.party.events.UserPart;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;
import com.cTimers.rooms.*;
import com.cTimers.utility.cLogger;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.plugins.specialcounter.SpecialCounterUpdate;
import net.runelite.client.plugins.specialcounter.SpecialWeapon;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.cTimers.constants.LogID.*;
import static com.cTimers.constants.TOBRoom.*;


@Slf4j
@PluginDescriptor(
        name = "cTimers",
        description = "Timers and statistics for Theatre of Blood",
        tags = {"timers", "tob", "tracker", "time", "theatre"}
)
public class cTimersPlugin extends Plugin
{
    private NavigationButton navButtonPrimary;
    private cLogger clog;

    private boolean partyIntact = false;

    @Inject
    private cTimersConfig config;

    public cTimersPlugin() {
    }

    @Provides
    cTimersConfig getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(cTimersConfig.class);
    }
    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private ClientThread clientThread;

    @Inject
    private PartyService party;

    @Inject
    private Client client;

    private cLobby lobby;
    private cMaiden maiden;
    private cBloat bloat;
    private cNylo nylo;
    private cSotetseg sote;
    private cXarpus xarpus;
    private cVerzik verzik;


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
    private cRoom currentRoom;
    int deferredTick;
    private ArrayList<String> currentPlayers;
    private boolean checkDefer = false;
    public static int scale = -1;

    @Inject
    private PluginManager pluginManager;

    @Inject
    private EventBus eventBus;

    @Override
    protected void shutDown()
    {
        partyIntact = false;
        clientToolbar.removeNavigation(navButtonPrimary);
    }

    @Override
    protected void startUp() throws Exception
    {
        super.startUp();

        cTimersPanelPrimary timersPanelPrimary = injector.getInstance(cTimersPanelPrimary.class);
        partyIntact = false;


        final BufferedImage icon = ImageUtil.loadImageResource(DevToolsPlugin.class, "devtools_icon.png");
        navButtonPrimary = NavigationButton.builder().tooltip("cTimersPrimary").icon(icon).priority(10).panel(timersPanelPrimary).build();

        clientToolbar.addNavigation(navButtonPrimary);

        clog = new cLogger(client, config);
        lobby = new cLobby(client, clog, config);
        maiden = new cMaiden(client, clog, config);
        bloat = new cBloat(client, clog, config);
        nylo = new cNylo(client, clog, config);
        sote = new cSotetseg(client, clog, config);
        xarpus = new cXarpus(client, clog, config);
        verzik = new cVerzik(client, clog, config);
        inTheatre = false;
        wasInTheatre = false;
        deferredTick = 0;
        currentPlayers = new ArrayList<>();
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
        cRoom previous = currentRoom;
        int currentRegion = getRoom();
        boolean activeState = false;
        if(inRegion(LOBBY_REGION))
        {
            currentRoom = lobby;
        }
        else if (previous == lobby && !inRegion(MAIDEN_REGION)) //TODO faulty logic
        {
            deferredTick = client.getTickCount()+2;
            clog.write(ENTERED_TOB);
            clog.write(SPECTATE);
            clog.write(LATE_START, String.valueOf(currentRegion));
        }
        if(inRegion(MAIDEN_REGION))
        {
            if(previous != maiden)
            {
                currentRoom = maiden;
                enteredMaiden(previous);
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

    private void enteredMaiden(cRoom old)
    {
        clog.write(ENTERED_TOB);
        deferredTick = client.getTickCount()+2;
        maiden.reset();
    }

    private void enteredBloat(cRoom old)
    {
        clog.write(ENTERED_NEW_TOB_REGION, BLOAT.ordinal());
        maiden.reset();
        bloat.reset();
    }

    private void enteredNylo(cRoom old)
    {
        clog.write(ENTERED_NEW_TOB_REGION, NYLO.ordinal());
        bloat.reset();
        nylo.reset();
    }

    private void enteredSote(cRoom old)
    {
        clog.write(ENTERED_NEW_TOB_REGION, SOTE.ordinal());
        nylo.reset();
        sote.reset();
    }

    private void enteredXarpus(cRoom old)
    {
        clog.write(ENTERED_NEW_TOB_REGION, XARPUS.ordinal());
        sote.reset();
        xarpus.reset();
    }

    private void enteredVerzik(cRoom old)
    {
        clog.write(ENTERED_NEW_TOB_REGION, VERZIK.ordinal());
        xarpus.reset();
        verzik.reset();
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
                    clog.write(BGS, name, ""+event.getHit());
                }
                if(event.getWeapon().equals(SpecialWeapon.DRAGON_WARHAMMER))
                {
                    clog.write(DWH, name);
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

    @Subscribe
    public void onGameTick(GameTick event) throws PluginInstantiationException
    {
        updateRoom();
        if(inTheatre)
        {
            wasInTheatre = true;
            currentRoom.updateGameTick(event);

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
                //TODO better way of doing this
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
    }

    @Subscribe
    public void onActorDeath(ActorDeath event)
    {
        if(inTheatre)
        {
            Actor a = event.getActor();
            if(a instanceof Player)
            {
                clog.write(PLAYER_DIED, event.getActor().getName());
            }
        }
    }

    @Subscribe
    public void onGraphicChanged(GraphicChanged event)
    {
        if(inTheatre)
        {
            currentRoom.updateGraphicChanged(event);
        }
    }

    @Subscribe
    public void onProjectileMoved(ProjectileMoved event)
    {
        if(inTheatre)
        {
            currentRoom.updateProjectileMoved(event);
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event)
    {
        if(inTheatre)
        {
            currentRoom.updateAnimationChanged(event);
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

    @Subscribe
    public void onNpcSpawned(NpcSpawned event)
    {
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
            currentRoom.updateHitsplatApplied(event);
        }
    }

    @Subscribe
    public void onOverheadTextChanged(OverheadTextChanged event)
    {
        if(currentRoom instanceof cXarpus)
        {
            xarpus.updateOverheadText(event);
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
