package com.cTimers.ui;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import com.cTimers.cRoomData;
import com.cTimers.filters.*;
import com.cTimers.panelcomponents.cFilteredRaidsFrame;
import com.cTimers.cTimersPlugin;
import com.cTimers.jdatepicker.*;
import com.cTimers.panelcomponents.cLoadFilterFrame;
import com.cTimers.panelcomponents.cSaveFilterFrame;
import com.cTimers.utility.cDebugHelper;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
@Slf4j
public class cTimersPanel extends PluginPanel
{
    private final cTimersPlugin plugin;

    private cFilteredRaidsFrame raids;
    private cLoadFilterFrame loadFilterFrame;
    private cSaveFilterFrame saveFilterFrame;
    private boolean raidsOpen;

    @Inject
    cTimersPanel(cTimersPlugin plugin)
    {
        this.plugin = plugin;
    }

    private final JPanel panelContainer = new JPanel();
    private final JPanel addFiltersContainer = new JPanel();
    private final JPanel activeFiltersContainer = new JPanel();

    private ArrayList<cFilterCondition> filters;

    public void init()
    {
        filters = new ArrayList<cFilterCondition>();
        raids = new cFilteredRaidsFrame();
        raids.getContentPane().setBackground(Color.BLACK);
        loadFilterFrame = new cLoadFilterFrame(activeFiltersContainer);
        saveFilterFrame = new cSaveFilterFrame();
        raidsOpen = false;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5, 5, 5, 5));

        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));

        panelContainer.add(buildAddFiltersContainer());
        panelContainer.add(buildActiveFiltersContainer());

        add(panelContainer, BorderLayout.NORTH);
    }

    private int gridHeight = 1;

    public ArrayList<cRoomData> getAllFakeRaids()
    {
        ArrayList<cRoomData> data = new ArrayList<>();
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(1));
        data.add(new cRoomData(2));
        data.add(new cRoomData(3));
        return data;
    }
    public ArrayList<cRoomData> getAllRaids()
    {
        ArrayList<cRoomData> raids = new ArrayList<>();
        try
        {
            String path = (cDebugHelper.raidDataSource == cDebugHelper.PRIMARY_FILE) ? "/.runelite/logs/tobdata.log" : "/.runelite/logs/altdata.log";
            File logFile = new File(System.getProperty("user.home").replace("\\", "/") + path);
            Scanner logReader = new Scanner(new FileInputStream(logFile));
            ArrayList<String> raid = new ArrayList<>();
            boolean raidActive = false;
            while(logReader.hasNextLine())
            {
                String line = logReader.nextLine();
                String[] lineSplit = line.split(",");
                if(!raidActive)
                {
                    if(lineSplit.length > 3)
                    {
                        if(Integer.parseInt(lineSplit[3]) == 0)
                        {
                            raid.add(line);
                            raidActive = true;
                        }
                    }
                }
                else
                {
                    if(lineSplit.length > 3)
                    {
                        if(Integer.parseInt(lineSplit[3]) == 99)
                        {
                            raid.add(line);
                        }
                        else if(Integer.parseInt(lineSplit[3]) == 4)
                        {
                            raid.add(line);
                            raidActive = false;
                            raids.add(new cRoomData(raid.toArray(new String[raid.size()])));
                            raid.clear();
                        }
                        else
                        {
                            raid.add(line);
                        }
                    }
                }
            }
            logReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return raids;
    }

    public boolean evaluateAllFilters(cRoomData data)
    {
        for(cFilterCondition filter : filters)
        {
            if(!filter.evaluate(data))
            {
               return false;
            }
        }
        return true;
    }

    public ArrayList<cRoomData> getActiveFilteredRaids()
    {
        ArrayList<cRoomData> filteredRaids = new ArrayList<>();
        ArrayList<cRoomData> allRaids = (cDebugHelper.raidDataSource == cDebugHelper.GENERATED) ? getAllFakeRaids() : getAllRaids();
        for(cRoomData data : allRaids)
        {
            if(evaluateAllFilters(data))
            {
                filteredRaids.add(data);
            }
        }
        return filteredRaids;
    }

    private JPanel buildActiveFiltersContainer()
    {
        GridLayout gl = new GridLayout(gridHeight, 0);
        gl.setVgap(5);
        activeFiltersContainer.setLayout(gl);
        activeFiltersContainer.setBorder(BorderFactory.createTitledBorder("Active Filters"));

        GridLayout gl2 = new GridLayout(2, 2);
        JPanel holder = new JPanel(gl2);
        JButton save = new JButton("Save");
        JButton clear = new JButton("Clear");
        JButton view = new JButton("View");
        JButton load = new JButton("Load");

        view.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                raids.getContentPane().removeAll();
                raids.updateFrameData(getActiveFilteredRaids());
                raids.getContentPane().setBackground(Color.BLACK);
                raids.repaint();
                raids.open();
            }
        });

        clear.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ArrayList<Component> components = new ArrayList<>();
                for(Component c : activeFiltersContainer.getComponents())
                {
                    if(c instanceof JLabel)
                    {
                        components.add(c);
                    }
                }
                for(Component c : components)
                {
                    activeFiltersContainer.remove(c);
                }
                activeFiltersContainer.setLayout(new GridLayout(1, 1));
                filters.clear();

                activeFiltersContainer.revalidate();
                activeFiltersContainer.repaint();
            }
        });

        save.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ArrayList<String> filtersToSave = new ArrayList<>();
                for(Component label : activeFiltersContainer.getComponents())
                {
                    if(label instanceof JLabel)
                    {
                        filtersToSave.add(((JLabel) label).getText());
                    }
                }
                saveFilterFrame.updateFilterFrame(filtersToSave.toArray(new String[filtersToSave.size()]));
                saveFilterFrame.open();
            }
        });

        load.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new cLoadFilterFrame(activeFiltersContainer).open();
            }
        });

        holder.add(save);
        holder.add(clear);
        holder.add(view);
        holder.add(load);

        activeFiltersContainer.add(holder);
        return activeFiltersContainer;
    }

    private String validateTime(String text) //TODO ERROR HANDLE + Below
    {
        if(text.startsWith(":"))
        {

        }
        return text;
    }

    private int getTimeFromString(String text) //TODO ERROR HANDLE
    {
        int ticks = 0;
        String sub = text;
        if(sub.contains(":"))
        {
            ticks += 100 * Integer.parseInt(sub.substring(0, sub.indexOf(":")));
            sub = text.substring(sub.indexOf(":")+1);
        }
        ticks += (Double.parseDouble(sub)/0.6);
        return ticks;
    }

    private JPanel buildAddFiltersContainer()
    {
        GridLayout gl = new GridLayout(22, 0);
        gl.setVgap(5);
        addFiltersContainer.setLayout(gl);

        addFiltersContainer.setBorder(BorderFactory.createTitledBorder("Add Filters"));

        JLabel label1 = new JLabel("Filter by room or split time");
        label1.setHorizontalAlignment(JLabel.CENTER);

        JLabel label2 = new JLabel("Filter by players in raid");
        label2.setHorizontalAlignment(JLabel.CENTER);

        JLabel label3 = new JLabel("Filter by date");
        label3.setHorizontalAlignment(JLabel.CENTER);

        JLabel label4 = new JLabel("Filter by other condition (int)");
        label4.setHorizontalAlignment(JLabel.CENTER);

        JLabel label5 = new JLabel("Filter by other condition (bool)");
        label5.setHorizontalAlignment(JLabel.CENTER);

        JButton buttonAddTimeFilter = new JButton("Add filter");

        JButton buttonAddPlayerFilter = new JButton("Add filter");

        JButton buttonAddDateFilter = new JButton("Add filter");

        JButton buttonAddFilterOtherInt = new JButton("Add filter");

        JButton buttonAddFilterOtherBool = new JButton("Add filter");

        String[] choicesRoom = {
                "Maiden total time", "Maiden 70 split", "Maiden 50 split", "Maiden 30 split", "Maiden 70-50 split", "Maiden 50-30 split", "Maiden skip split",
                "Bloat total time", "Bloat first down split",
                "Nylocas total time", "Nylo boss spawn", "Nylo boss duration", "Nylo last wave", "Nylo cleanup",
                "Sotetseg total time", "Sote 66 split", "Sote 33 split", "Sote first maze length", "Sote second maze length", "Sote average maze length",
                "Xarpus total time", "Xarpus screech", "Xarpus post screech",
                "Verzik total time", "Verzik p1 split", "Verzik p2 split", "Verzik p2 duration", "Verzik p3 duration"
        };

        String[] choicesOtherInt = {
                "Maiden bloods spawned",
                "Maiden crabs leaked",
                "Maiden defense reduction",
                "Maiden deaths",
                "Bloat Downs",
                "Bloat 1st walk deaths",
                "Bloat first walk BGS",
                "Bloat deaths",
                "Nylo stalls pre 20",
                "Nylo stalls post 20",
                "Nylo total stalls",
                "Nylo range splits",
                "Nylo mage splits",
                "Nylo melee splits",
                "Nylo range rotations",
                "Nylo mage rotations",
                "Nylo melee roations",
                "Nylo defense reduction",
                "Nylo deaths",
                "Sote specs p1",
                "Sote specs p2",
                "Sote specs p3",
                "Sote deaths",
                "Sote total specs hit",
                "Xarp defense reduction",
                "Xarp deaths",
                "Xarpus healing",
                "Verzik bounces",
                "Verzik deaths",
                "Raid team size"
        };

        String[] choicesOtherBool = {
                "Maiden skip successful",
                "Reset after maiden",
                "Maiden wipe",
                "Reset after bloat",
                "Bloat wipe",
                "Reset after nylo",
                "Nylo wipe",
                "Reset after sote",
                "Sote wipe",
                "Reset after xarp",
                "Xarp wipe",
                "Verzik  wipe"
        };

        JComboBox<String> rooms = new JComboBox<String>(choicesRoom);

        String[] choicesDate = {
                "on",
                "before",
                "after"
        };

        String[] qualifierOtherBool = {
                "True",
                "False"
        };
        JComboBox<String> otherInt = new JComboBox<>(choicesOtherInt);
        JComboBox<String> otherBool = new JComboBox<>(choicesOtherBool);


        UtilDateModel model = new UtilDateModel();
        JDatePanel datePanel = new JDatePanel(model); //TODO what is this and do we need it
        JDatePicker datePicker = new JDatePicker(model);

        JTextField players = new JTextField();

        String[] playersQualifier = {
                "contains exactly",
                "includes",
                "includes any of",
                "excludes",
                "excludes all of"
        };

        JComboBox<String> dateSelector = new JComboBox<String>(choicesDate);

        JComboBox<String> pQualifiers = new JComboBox<String>(playersQualifier);

        String[] choicesQualifier = {
                "equal to",
                "less than",
                "greater than",
                "less than or equal to",
                "greater than or equal to"
        };

        JComboBox<String> qualifiers = new JComboBox<String>(choicesQualifier);

        JComboBox<String> qualifiersOtherInt = new JComboBox<>(choicesQualifier);

        JComboBox<String> qualifiersOtherBool = new JComboBox<>(qualifierOtherBool);

        JTextField text2 = new JTextField();


        JTextField text = new JTextField(8);
        /*cAutoFill autoFill = new cAutoFill(text, this, null, Color.WHITE.brighter(), Color.white, Color.red, 0.75f)
        {
            @Override
            boolean wordTyped(String typedWord)
            {
                ArrayList<String> words = new ArrayList<>();
                words.add("=");
                words.add("<");
                words.add(">");
                words.add("<=");
                words.add(">=");
                setDictionary(words);
                return super.wordTyped(typedWord);
            }
        };*/

        buttonAddTimeFilter.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String time = validateTime(text.getText());
                if(time.equals(""))
                {
                    return;
                }
                gridHeight++;
                GridLayout gl = new GridLayout(gridHeight, 0);
                activeFiltersContainer.setLayout(gl);
                JLabel label = new JLabel(rooms.getSelectedItem().toString() + " " + qualifiers.getSelectedItem().toString() + " " + time + " ");
                filters.add(new cFilterTime(rooms.getSelectedIndex(), qualifiers.getSelectedIndex(), getTimeFromString(time)));

                activeFiltersContainer.add(label);
                activeFiltersContainer.revalidate();
                activeFiltersContainer.repaint();
            }
        });

        buttonAddPlayerFilter.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                gridHeight++;
                GridLayout gl = new GridLayout(gridHeight, 0);
                activeFiltersContainer.setLayout(gl);
                JLabel label = new JLabel("Raid " + pQualifiers.getSelectedItem().toString() + " " + players.getText());
                filters.add(new cFilterPlayers(players.getText(), pQualifiers.getSelectedIndex()));
                activeFiltersContainer.add(label);
                activeFiltersContainer.revalidate();
                activeFiltersContainer.repaint();
            }
        });

        buttonAddDateFilter.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                gridHeight++;
                GridLayout gl = new GridLayout(gridHeight, 0);
                activeFiltersContainer.setLayout(gl);
                JLabel label = new JLabel("Raid was " + dateSelector.getSelectedItem().toString() + " " + datePicker.getModel().getValue().toString());
                filters.add(new cFilterDate((Date)datePicker.getModel().getValue(), dateSelector.getSelectedIndex())); //TODO maybe
                activeFiltersContainer.add(label);
                activeFiltersContainer.revalidate();
                activeFiltersContainer.repaint();
            }
        });

        buttonAddFilterOtherInt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                gridHeight++;
                GridLayout gl = new GridLayout(gridHeight, 0);
                activeFiltersContainer.setLayout(gl);
                JLabel label = new JLabel(otherInt.getSelectedItem().toString() + " " + qualifiersOtherInt.getSelectedItem().toString() + " " + text2.getText() + " ");
                filters.add(new cFilterOtherInt(otherInt.getSelectedIndex(), qualifiersOtherInt.getSelectedIndex(), Integer.parseInt(text2.getText())));
                activeFiltersContainer.add(label);
                activeFiltersContainer.revalidate();
                activeFiltersContainer.repaint();
            }
        });

        buttonAddFilterOtherBool.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                gridHeight++;
                GridLayout gl = new GridLayout(gridHeight, 0);
                activeFiltersContainer.setLayout(gl);
                JLabel label = new JLabel(otherBool.getSelectedItem().toString() + " " + qualifiersOtherBool.getSelectedItem().toString());
                filters.add(new cFilterOtherBool(otherBool.getSelectedIndex(), qualifiersOtherBool.getSelectedIndex() == 0)); //TODO maybe
                activeFiltersContainer.add(label);
                activeFiltersContainer.revalidate();
                activeFiltersContainer.repaint();
            }
        });


        addFiltersContainer.add(label1);
        addFiltersContainer.add(rooms);
        addFiltersContainer.add(qualifiers);
        addFiltersContainer.add(text);
        addFiltersContainer.add(buttonAddTimeFilter);

        addFiltersContainer.add(label2);
        addFiltersContainer.add(pQualifiers);
        addFiltersContainer.add(players);
        addFiltersContainer.add(buttonAddPlayerFilter);

        addFiltersContainer.add(label3);
        addFiltersContainer.add(dateSelector);
        addFiltersContainer.add(datePicker);
        addFiltersContainer.add(buttonAddDateFilter);

        addFiltersContainer.add(label4);
        addFiltersContainer.add(otherInt);
        addFiltersContainer.add(qualifiersOtherInt);
        addFiltersContainer.add(text2);
        addFiltersContainer.add(buttonAddFilterOtherInt);

        addFiltersContainer.add(label5);
        addFiltersContainer.add(otherBool);
        addFiltersContainer.add(qualifiersOtherBool);
        addFiltersContainer.add(buttonAddFilterOtherBool);

        return addFiltersContainer;
    }
}
