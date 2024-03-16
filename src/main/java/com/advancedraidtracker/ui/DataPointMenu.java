package com.advancedraidtracker.ui;

import com.advancedraidtracker.constants.RaidRoom;
import com.advancedraidtracker.ui.comparisonview.ComparisonViewPanel;
import com.advancedraidtracker.utility.datautility.DataPoint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
@Slf4j
public class DataPointMenu
{
    private List<String> flatData;
    @Getter
    private JComboBox<String> comboBox;
    private JPopupMenu menu;
    private Raids window;
    public DataPointMenu(List<String> allComboValues, Map<String, String[]> popupData, List<String> flatData, JPopupMenu popmenu, JComboBox<String> box, Raids window)
    {
        this.window = window;
        this.menu = popmenu;
        this.comboBox = box;
        this.flatData = flatData;
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
            else
            {
                topLevelMenuAll.add(menu);
            }
        }
        popmenu.add(topLevelMenuAll);
        popmenu.add(topLevelMenuToA);
        popmenu.add(topLevelMenuToB);
        popmenu.add(topLevelMenuCoX);
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
        //Vector<String> items = new Vector<>();

        //ComparisonViewPanel.addComboItems(name, items, flatData, comboBox);
        //comboBox.addItem(name);
        //comboBox.setSelectedItem(name);
        window.example.addItem(name);
        window.example.setSelectedItem(name);
        window.updateTable();
    }

    public void invertVisible()
    {
        if(menu.isVisible())
        {
            menu.setVisible(false);
        }
        else
        {
            menu.show(comboBox, 0, comboBox.getSize().height);
        }
    }

    public String getActive()
    {
        return comboBox.getSelectedItem().toString();
    }
}
