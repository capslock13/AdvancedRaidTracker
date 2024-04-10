package com.advancedraidtracker.ui;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.constants.RaidType;
import com.advancedraidtracker.utility.datautility.DataPoint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.advancedraidtracker.constants.RaidRoom.ALL;
import static com.advancedraidtracker.rooms.inf.InfernoHandler.roomMap;
import static com.advancedraidtracker.utility.UISwingUtility.getThemedMenu;
import static com.advancedraidtracker.utility.UISwingUtility.getThemedMenuItem;

@Slf4j
public class DataPointMenu
{
    @Getter
    private JComboBox<String> comboBox;
    private UpdateableWindow window;

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
            JMenu menu = createMenu(raidType.name);
            Map<String, JMenu> infernoSubMenus = new LinkedHashMap<>();
            Map<RaidRoom, JMenu> roomSubMenus = new LinkedHashMap<>();
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
}
