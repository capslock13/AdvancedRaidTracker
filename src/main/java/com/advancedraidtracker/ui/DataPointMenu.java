package com.advancedraidtracker.ui;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.RoomUtil;
import com.advancedraidtracker.utility.datautility.DataPoint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.advancedraidtracker.constants.RaidRoom.ALL;
import static com.advancedraidtracker.constants.RaidType.COLOSSEUM;
import static com.advancedraidtracker.constants.RaidType.INFERNO;
import static com.advancedraidtracker.rooms.inf.InfernoHandler.roomMap;
import static com.advancedraidtracker.utility.UISwingUtility.getThemedMenu;
import static com.advancedraidtracker.utility.UISwingUtility.getThemedMenuItem;

@Slf4j
public class DataPointMenu
{
    @Getter
    private JComboBox<String> comboBox;
    private final UpdateableWindow window;

    public DataPointMenu(JPopupMenu popmenu, JComboBox<String> box, UpdateableWindow window)
    { //todo handle edge cases differently
        this.window = window;
        this.comboBox = box;

        Map<RaidType, JMenu> menus = new LinkedHashMap<>();

        for (RaidType raidType : RaidType.values())
        {
            if (raidType.equals(RaidType.ALL))
            {
                JMenu menu = createMenu(raidType.name);
				JMenu timeCategoryAll = createMenu("Time");
				timeCategoryAll.add(createMenuItem("Challenge Time"));
				timeCategoryAll.add(createMenuItem("Overall Time"));
				timeCategoryAll.add(createMenuItem("Time Outside Rooms"));
				menu.add(timeCategoryAll);
                Map<DataPoint.MenuType, JMenu> categoryMenus = new LinkedHashMap<>();
                for (DataPoint.MenuType menuType : DataPoint.MenuType.values())
                {
                    if (!menuType.excluded)
                    {
                        categoryMenus.put(menuType, createMenu(menuType.name));
                    }
                }
                for (DataPoint point : DataPoint.getRoomPoints(ALL))
                {
                    if (!point.menuType.equals(DataPoint.MenuType.EXCLUDED))
                    {
                        if (point.room.equals(ALL))
                        {
                            categoryMenus.get(point.menuType).add(createMenuItem("Total " + point.name));
                        } else
                        {
                            categoryMenus.get(point.menuType).add(createMenuItem(point.name));
                        }
                    }
                }
                for (JMenu categoryMenu : categoryMenus.values())
                {
                    if (categoryMenu.getMenuComponents().length != 0)
                    {
                        menu.add(categoryMenu);
                    }
                }
                menus.put(RaidType.ALL, menu);
                continue;
            }
            JMenu menu;
            Map<RaidRoom, JMenu> roomSubMenus = new LinkedHashMap<>();
            if(raidType == INFERNO || raidType == RaidType.COLOSSEUM)
            {
                if(raidType == INFERNO)
                {
                    menu = getWaveSubMenus("Inferno", "Inf", infernoSplits, 69);
                }
                else
                {
                    menu = getWaveSubMenus("Colosseum", "Col", colosseumSplits, 12);
                }
            }
            else
            {
                menu = createMenu(raidType.name);
                for (RaidRoom room : RaidRoom.getRaidRoomsForRaidType(raidType))
                {
                    JMenu roomMenu = createMenu(room.name);
                    Map<DataPoint.MenuType, JMenu> categoryMenus = new LinkedHashMap<>();
                    for (DataPoint.MenuType menuType : DataPoint.MenuType.values())
                    {
                        if (!menuType.excluded)
                        {
                            categoryMenus.put(menuType, createMenu(menuType.name));
                        }
                    }
                    for (DataPoint point : DataPoint.getRoomPoints(room))
                    {
                        if (!point.menuType.equals(DataPoint.MenuType.EXCLUDED))
                        {
                            if (point.room.equals(ALL))
                            {
                                categoryMenus.get(point.menuType).add(createMenuItem(room.name + " " + point.name));
                            } else
                            {
                                categoryMenus.get(point.menuType).add(createMenuItem(point.name));
                            }
                        }
                    }
                    for (JMenu categoryMenu : categoryMenus.values())
                    {
                        if (categoryMenu.getMenuComponents().length != 0)
                        {
                            roomMenu.add(categoryMenu);
                        }
                    }
                    roomSubMenus.put(room, roomMenu);
                }
            }
            for (JMenu roomMenu : roomSubMenus.values())
            {
                if (roomMenu.getMenuComponents().length != 0)
                {
                    menu.add(roomMenu);
                }
            }
            menus.put(raidType, menu);
        }
        for (JMenu menu : menus.values())
        {
            if (menu.getMenuComponents().length != 0)
            {
                popmenu.add(menu);
            }
        }
    }

    private JMenuItem createMenuItem(final String name)
    {
        JMenuItem item = getThemedMenuItem(name);
        item.addActionListener(event -> setComboSelection(name));
        return item;
    }

    private JMenu createMenu(final String name)
    {
        JMenu menu = getThemedMenu(name);
        return menu;
    }

    private void setComboSelection(String name)
    {
        window.setComboBox(name);
        window.update();
    }

    public JMenu getWaveSubMenus(String contentName, String prefix, List<String> relevantSplits, int highestWave)
    {
        JMenu menu = createMenu(contentName);

        JMenu allWaves = createMenu("All Waves");
        for(int i = 1; i < highestWave+1; i++)
        {
            allWaves.add(getSubMenuForWave(contentName, prefix+ " Wave " + i));
        }
        menu.add(allWaves);

        for(String splitSubMenu : relevantSplits)
        {
            JMenu splitMenu = createMenu(splitSubMenu);
            splitMenu.add(getSubMenuForWave(contentName, prefix + " " + splitSubMenu));
            menu.add(splitMenu);
        }
        return menu;
    }

    public JMenu getSubMenuForWave(String contentName, String waveName)
    {
        JMenu wave = createMenu(waveName);
        Map<DataPoint.MenuType, JMenu> categoryMenus = new LinkedHashMap<>();
        for (DataPoint.MenuType menuType : DataPoint.MenuType.values())
        {
            if (!menuType.excluded)
            {
                categoryMenus.put(menuType, createMenu(menuType.name));
            }
        }
        List<RaidRoom> roomsToAdd = RoomUtil.getVariations(waveName);
        roomsToAdd.add(RaidRoom.getRoom(contentName));
        for (DataPoint point : DataPoint.getRoomPoints(RaidRoom.getRoom(contentName), RaidRoom.getRoom(waveName)))
        {
            if (!point.menuType.equals(DataPoint.MenuType.EXCLUDED))
            {
                if (point.room.equals(ALL))
                {
                    categoryMenus.get(point.menuType).add(createMenuItem(waveName + " " + point.name));
                } else
                {
                    categoryMenus.get(point.menuType).add(createMenuItem(point.name));
                }
            }
        }
        for (DataPoint.MenuType menuType : categoryMenus.keySet())
        {
            if(menuType == DataPoint.MenuType.TIME && (contentName.equals("Inferno") || waveName.contains("-")))
            {
                categoryMenus.get(menuType).add(createMenuItem(waveName + " Duration"));
            }
            if (categoryMenus.get(menuType).getMenuComponents().length != 0)
            {
                wave.add(categoryMenus.get(menuType));
            }
        }
        return wave;
    }

    private static final List<String> infernoSplits = List.of("Wave 1-8", "Wave 9-17", "Wave 18-24", "Wave 25-34", "Wave 35-41", "Wave 42-49", "Wave 50-56", "Wave 57-59", "Wave 60-62", "Wave 63-65", "Wave 66", "Wave 67", "Wave 68", "Wave 69");
    private static final List<String> colosseumSplits = List.of("Wave 1-6", "Wave 7-11", "Wave 1-11");
}
