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

@Slf4j
public class DataPointMenu
{
    @Getter
    private JComboBox<String> comboBox;
    private UpdateableWindow window;
    public DataPointMenu(List<String> allComboValues, Map<String, String[]> popupData, List<String> flatData, JPopupMenu popmenu, JComboBox<String> box, UpdateableWindow window)
    { //todo handle edge cases differently
        this.window = window;
        this.comboBox = box;

        Map<RaidType, JMenu> menus = new LinkedHashMap<>();
        for(RaidType raidType : RaidType.values())
        {
            if(raidType.equals(RaidType.ALL))
            {
                JMenu menu = createMenu(raidType.name);
                Map<DataPoint.MenuType, JMenu> categoryMenus = new LinkedHashMap<>();
                for(DataPoint.MenuType menuType : DataPoint.MenuType.values())
                {
                    if(!menuType.excluded)
                    {
                        categoryMenus.put(menuType, createMenu(menuType.name));
                    }
                }
                for(DataPoint point : DataPoint.getRoomPoints(ALL))
                {
                    if(!point.menuType.equals(DataPoint.MenuType.EXCLUDED))
                    {
                        if(point.room.equals(ALL))
                        {
                            categoryMenus.get(point.menuType).add(createMenuItem("Total " + point.name));
                        }
                        else
                        {
                            categoryMenus.get(point.menuType).add(createMenuItem(point.name));
                        }
                    }
                }
                for(JMenu categoryMenu : categoryMenus.values())
                {
                    if(categoryMenu.getMenuComponents().length != 0)
                    {
                        menu.add(categoryMenu);
                    }
                }
                menus.put(RaidType.ALL, menu);
                continue;
            }
            JMenu menu = createMenu(raidType.name);

            Map<RaidRoom, JMenu> roomSubMenus = new LinkedHashMap<>();
            for(RaidRoom room : RaidRoom.getRaidRoomsForRaidType(raidType))
            {
                JMenu roomMenu = createMenu(room.name);
                Map<DataPoint.MenuType, JMenu> categoryMenus = new LinkedHashMap<>();
                for(DataPoint.MenuType menuType : DataPoint.MenuType.values())
                {
                    if(!menuType.excluded)
                    {
                        categoryMenus.put(menuType, createMenu(menuType.name));
                    }
                }
                for(DataPoint point : DataPoint.getRoomPoints(room))
                {
                    if(!point.menuType.equals(DataPoint.MenuType.EXCLUDED))
                    {
                        if(point.room.equals(ALL))
                        {
                            categoryMenus.get(point.menuType).add(createMenuItem(room.name + " " + point.name));
                        }
                        else
                        {
                            categoryMenus.get(point.menuType).add(createMenuItem(point.name));
                        }
                    }
                }
                for(JMenu categoryMenu : categoryMenus.values())
                {
                    if(categoryMenu.getMenuComponents().length != 0)
                    {
                        roomMenu.add(categoryMenu);
                    }
                }
                roomSubMenus.put(room, roomMenu);
            }
            for(JMenu roomMenu : roomSubMenus.values())
            {
                if(roomMenu.getMenuComponents().length != 0)
                {
                    menu.add(roomMenu);
                }
            }
            menus.put(raidType, menu);
        }
        for(JMenu menu : menus.values())
        {
            if(menu.getMenuComponents().length != 0)
            {
                popmenu.add(menu);
            }
        }
        /*
        JMenu men = new JMenu("Time");
        men.setBackground(Color.BLACK);
        men.setOpaque(true);
        JMenu men2 = new JMenu("Misc");
        men2.setBackground(Color.BLACK);
        men2.setOpaque(true);
        for(Integer val : roomMap.keySet())
        {
            JMenu subMenu = new JMenu(roomMap.get(val));
            subMenu.setOpaque(true);
            subMenu.setBackground(Color.BLACK);
            String valS = roomMap.get(val);
            if(valS.contains("-"))
            {
                String[] ss = valS.split(" ");
                if(ss.length == 2)
                {
                    String[] ss2 = ss[1].split("-");
                    if(ss2.length == 2)
                    {
                        int upper = Integer.parseInt(ss2[1])+1;
                        int lower = Integer.parseInt(ss2[0])+1;
                        for(int i = lower-1; i < upper; i++)
                        {
                            subMenu.add(createMenuItem("Wave " + i + " Split"));
                            subMenu.add(createMenuItem("Wave " + i + " Duration"));
                        }
                    }
                }
            }
            men.add(subMenu);
        }
        //topLevelMenuInferno.add(men);
        //topLevelMenuInferno.add(men2);
        for (String category : allComboValues)
        {
            JMenu menu = new JMenu(category);
            menu.setBackground(Color.BLACK);
            menu.setOpaque(true);
            if (!category.equals("Room Times") && !category.equals("Any"))
            {
                JMenu timeMenu = new JMenu("Time");
                timeMenu.setBackground(Color.BLACK);
                timeMenu.setOpaque(true);
                for (String itemName : DataPoint.filterTimes(popupData.get(category)))
                {
                    timeMenu.add(createMenuItem(itemName));
                    flatData.add(itemName);
                }
                JMenu countMenu = new JMenu("Misc");
                countMenu.setBackground(Color.BLACK);
                countMenu.setOpaque(true);
                for (String itemName : DataPoint.filterInt(popupData.get(category)))
                {
                    countMenu.add(createMenuItem(itemName));
                    flatData.add(itemName);
                }
                JMenu thrallMenu = new JMenu("Thrall");
                thrallMenu.setBackground(Color.BLACK);
                thrallMenu.setOpaque(true);
                for (String itemName : DataPoint.filterThrall(popupData.get(category)))
                {
                    thrallMenu.add(createMenuItem(itemName));
                    flatData.add(itemName);
                }
                JMenu vengMenu = new JMenu("Veng");
                vengMenu.setBackground(Color.BLACK);
                vengMenu.setOpaque(true);
                for (String itemName : DataPoint.filterVeng(popupData.get(category)))
                {
                    vengMenu.add(createMenuItem(itemName));
                    flatData.add(itemName);
                }

                JMenu specMenu = new JMenu("Spec");
                specMenu.setBackground(Color.BLACK);
                specMenu.setOpaque(true);
                for (String itemName : DataPoint.filterSpecs(popupData.get(category)))
                {
                    specMenu.add(createMenuItem(itemName));
                    flatData.add(itemName);
                }

                menu.add(timeMenu);
                menu.add(countMenu);
                menu.add(thrallMenu);
                menu.add(vengMenu);
                menu.add(specMenu);
            } else
            {
                for (String itemName : popupData.get(category))
                {
                    menu.add(createMenuItem(itemName));
                    flatData.add(itemName);
                }
            }
            menus.get(RaidRoom.getRoom(category).getRaidType()).add(menu);
        }

        for(JMenu menu : menus.values())
        {
            popmenu.add(menu);
        }

        JMenu playerSpecificMenu = new JMenu("Player Specific");
        playerSpecificMenu.setBackground(Color.BLACK);
        playerSpecificMenu.setOpaque(true);
        String[] qualifiers = new String[]{"Maiden", "Bloat", "Nylo", "Sote", "Xarp", "Verz", "deaths"};

        for (String s : qualifiers)
        {
            JMenu room = new JMenu(s);
            room.setBackground(Color.BLACK);
            room.setOpaque(true);
            for (String qualified : DataPoint.getPlayerSpecific())
            {
                if (qualified.contains(s))
                {
                    room.add(createMenuItem("Player: " + qualified));
                    flatData.add("Player: " + qualified);
                }
            }
            playerSpecificMenu.add(room);
        }
        JMenu room = new JMenu("Other");
        room.setBackground(Color.BLACK);
        room.setOpaque(true);
        for (String qualified : DataPoint.getPlayerSpecific())
        {
            boolean anyFlagged = false;
            for (String s : qualifiers)
            {
                if (qualified.contains(s))
                {
                    anyFlagged = true;
                }
            }
            if (!anyFlagged)
            {
                room.add(createMenuItem("Player: " + qualified));
                flatData.add("Player: " + qualified);
            }
        }
        playerSpecificMenu.add(room);

        popmenu.add(playerSpecificMenu);

         */
    }

    private JMenuItem createMenuItem(final String name)
    {
        JMenuItem item = new JMenuItem(name);
        item.setBackground(Color.BLACK);
        item.setOpaque(true);
        item.addActionListener(event -> setComboSelection(name));
        return item;
    }

    private JMenu createMenu(final String name)
    {
        JMenu menu = new JMenu(name);
        menu.setBackground(Color.BLACK);
        menu.setOpaque(true);;
        return menu;
    }

    private void setComboSelection(String name)
    {
        window.setComboBox(name);
        window.update();
    }
}
