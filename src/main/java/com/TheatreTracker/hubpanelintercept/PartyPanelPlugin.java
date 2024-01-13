package com.TheatreTracker.hubpanelintercept;

import java.awt.image.BufferedImage;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.PartyChanged;
import net.runelite.client.events.PartyMemberAvatar;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.party.PartyService;
import net.runelite.client.party.WSClient;
import net.runelite.client.party.events.UserPart;
import net.runelite.client.party.messages.UserSync;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;


@Slf4j
@PluginDescriptor(
        name = "Hub Party Panel"
)
public class PartyPanelPlugin extends Plugin
{
    private static final BufferedImage ICON = ImageUtil.loadImageResource(PartyPanelPlugin.class, "icon.png");

    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private ClientToolbar clientToolbar;


    @Inject
    private PartyService partyService;

    @Inject
    private PluginManager pluginManager;

    @Inject
    SpriteManager spriteManager;

    @Inject
    ItemManager itemManager;

    @Inject
    private WSClient wsClient;


    @Getter
    private final Map<Long, PartyPlayer> partyMembers = new HashMap<>();

    @Getter
    private PartyPlayer myPlayer = null;

    private NavigationButton navButton;
    private boolean addedButton = false;

    private PartyPanel panel;
    private Instant lastLogout;

    // All events should be deferred to the next game tick
    private PartyBatchedChange currentChange = new PartyBatchedChange();

    @Provides
    PartyPanelConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(PartyPanelConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {

    }

    @Override
    protected void shutDown() throws Exception
    {

    }

    @Subscribe
    protected void onConfigChanged(final ConfigChanged c)
    {

    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged c)
    {

    }

    public boolean isInParty()
    {
        return partyService.isInParty();
    }

    public boolean isLocalPlayer(long id)
    {
        return partyService.getLocalMember() != null && partyService.getLocalMember().getMemberId() == id;
    }

    @Subscribe
    public void onUserPart(final UserPart event)
    {

    }

    @Subscribe
    public void onUserSync(final UserSync event)
    {

    }

    @Subscribe
    public void onPartyChanged(final PartyChanged event)
    {

    }

    @Subscribe
    public void onGameTick(final GameTick tick)
    {

    }

    @Subscribe
    public void onStatChanged(final StatChanged event)
    {

    }

    @Subscribe
    public void onItemContainerChanged(final ItemContainerChanged c)
    {

    }

    @Subscribe
    public void onVarbitChanged(final VarbitChanged event)
    {

    }

    @Subscribe
    public void onPartyBatchedChange(PartyBatchedChange e)
    {

    }

    @Subscribe
    public void onPartyMemberAvatar(PartyMemberAvatar e)
    {

    }

    public void changeParty(String passphrase)
    {

    }

    public void createParty()
    {

    }

    public String getPartyPassphrase()
    {
        return null;
    }

    public void leaveParty()
    {

    }

    private int[] convertItemsToArray(Item[] items)
    {
     return null;
    }

    private int[] convertGameItemsToArray(GameItem[] items)
    {
      return null;
    }

    public PartyBatchedChange partyPlayerAsBatchedChange()
    {
       return null;
    }

    @Schedule(
            period = 10,
            unit = ChronoUnit.SECONDS
    )
    public void checkIdle()
    {

    }

    private static int messageFreq(int partySize)
    {
        return 0;
    }
}