package com.advancedraidtracker.ui;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.utility.datautility.DataPoint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

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
        JMenu topLevelMenuAll = new JMenu("All");
        topLevelMenuAll.setBackground(Color.BLACK);
        topLevelMenuAll.setOpaque(true);
        JMenu topLevelMenuToA = new JMenu("ToA");
        topLevelMenuToA.setBackground(Color.BLACK);
        topLevelMenuToA.setOpaque(true);
        JMenu topLevelMenuToB = new JMenu("ToB");
        topLevelMenuToB.setBackground(Color.BLACK);
        topLevelMenuToB.setOpaque(true);
        JMenu topLevelMenuCoX = new JMenu("CoX");
        topLevelMenuCoX.setBackground(Color.BLACK);
        topLevelMenuCoX.setOpaque(true);
        JMenu topLevelMenuCol = new JMenu("Colosseum");
        topLevelMenuCol.setBackground(Color.BLACK);
        topLevelMenuCol.setOpaque(true);
        JMenu topLevelMenuInferno = new JMenu("Inferno");
        topLevelMenuInferno.setBackground(Color.BLACK);
        topLevelMenuInferno.setOpaque(true);

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
        topLevelMenuInferno.add(men);
        topLevelMenuInferno.add(men2);
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
            if(RaidRoom.getRoom(category).isTOA())
            {
                topLevelMenuToA.add(menu);
            }
            else if(RaidRoom.getRoom(category).isTOB())
            {
                topLevelMenuToB.add(menu);
            }
            else if(RaidRoom.getRoom(category).isCOX())
            {
                topLevelMenuCoX.add(menu);
            }
            else if(RaidRoom.getRoom(category).isColo())
            {
                topLevelMenuCol.add(menu);
            }
            else
            {
                topLevelMenuAll.add(menu);
            }
        }
        popmenu.add(topLevelMenuAll);
        popmenu.add(topLevelMenuToA);
        popmenu.add(topLevelMenuToB);
        popmenu.add(topLevelMenuCoX);
        popmenu.add(topLevelMenuCol);
        popmenu.add(topLevelMenuInferno);
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
    }

    private JMenuItem createMenuItem(final String name)
    {
        JMenuItem item = new JMenuItem(name);
        item.setBackground(Color.BLACK);
        item.setOpaque(true);
        item.addActionListener(event -> setComboSelection(name));
        return item;
    }

    private void setComboSelection(String name)
    {
        window.setComboBox(name);
        window.update();
    }
}
