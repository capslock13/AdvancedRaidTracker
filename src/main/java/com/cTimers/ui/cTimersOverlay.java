package com.cTimers.ui;

import com.google.inject.Inject;
import net.runelite.api.Client;
import com.cTimers.cTimersConfig;
import com.cTimers.cTimersPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.*;

public class cTimersOverlay extends Overlay
{
    private final Client client;
    private final cTimersPlugin plugin;
    private final cTimersConfig config;

    @Inject
    private cTimersOverlay(Client client, cTimersPlugin plugin, cTimersConfig config)
    {
        super(plugin);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        return null;
    }
}
