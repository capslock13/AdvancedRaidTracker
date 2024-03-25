package com.advancedraidtracker.ui.charts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChartTheme
{
    DEFAULT("Default", "#141414", "#1e1e1e", "#282828", "#262626", "#dddddd", "#787878", "#ff0000", "#ffffff"),
    BLERT_IO("Blert.io", "#1b1c25", "#1e1f28", "#23242f", "#23242f", "#dddddd", "#4C4E54", "#6F7277", "#23242f"),
    HIGH_CONTRAST("High Contrast", 	"#000000", "#080808", "#141414", "#141414", "#ffffff", "#787878", "#ff0000", "#ffffff"),
    JAGEX("Jagex", "#3e3529", "#4b3e32", "#554B41", "#554B41", "#ff981f", "#000000", "#ffffff", "#554b41"),
    FOREST("Forest", "#001d0e", "#044423", "#066136", "#016134", "#dddddd", "#787878", "#ff0000", "#066136"),
    LIGHT_MODE("Light Mode", "#cccccc", "#aaaaaa", "#787878", "#787878", "#000000", "#000000", "#000000", "#ffffff"),
    SOLARIZED_LIGHT("Solarized Light", "#fdf6e3", "#93a1a1", "#eee8d5", "#657b83", "#859900", "#6c71c4", "#b58900", "#ffffff"),
    SOLARIZED_DARK("Solarized Dark", "#002b36", "#073642", "#073642", "#0e414a", "#b58900", "#6c71c4", "#859900", "#ffffff"),
    CAKE("Cake", "#2E231B", "#4D3829", "#4D3829", "#5d4033", "#c092ce", "#d9956f", "#ea88b9", "#ffffff"),
    ONE_DARK("One Dark", "#282c34", "#363342" , "#363342", "#423948","#e5c07b", "#61afef", "#c678dd", "#ffffff"),
    NIGHT_OWL("Night Owl", "#011627", "#1d3b53", "#1d3b53", "#2f4c66", "#ecc48d", "#d6deeb", "#addb67", "#ffffff"),
    ;


    @Override
    public String toString()
    {
        return name;
    }

    private final String name;
    private final String primaryDark;
    private final String primaryMiddle;
    private final String primaryLight;
    private final String idleTick;
    private final String fontColor;
    private final String boxColor;
    private final String markerColor;
    private final String attackBoxColor;
}
