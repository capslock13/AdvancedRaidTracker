package com.advancedraidtracker.ui.charts.chartcreator;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import static com.advancedraidtracker.utility.UISwingUtility.*;

public class ChartTopMenuPanel extends JPanel
{
    JCheckBox weaponCD = getThemedCheckBox("Enforce Weapon CD?");
    JTextField lineText = getThemedTextField();

    public ChartTopMenuPanel(ChartCreatorFrame parent, AdvancedRaidTrackerConfig config)
    {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(config.primaryDark());
        setOpaque(true);
        JComboBox<Integer> options = getThemedComboBox(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        options.addActionListener(al ->
        {
            parent.setPlayerCount(options.getSelectedIndex() + 1);
        });

        JSpinner startTick = getThemedSpinner(new SpinnerNumberModel(1, 0, 500, 1));

        JSpinner endTick = getThemedSpinner(new SpinnerNumberModel(50, 1, 500, 1));

        startTick.addChangeListener(cl ->
        {
            endTick.setModel(new SpinnerNumberModel((int) endTick.getValue(), (int) startTick.getValue() + 1, 500, 1));
            parent.setStartTick((int) startTick.getValue());
        });

        endTick.addChangeListener(cl ->
        {
            startTick.setModel(new SpinnerNumberModel((int) startTick.getValue(), 0, (int) endTick.getValue() - 1, 1));
            parent.setEndTick((int) endTick.getValue());
        });

        add(getThemedLabel("Players: "));
        add(options);
        add(Box.createRigidArea(new Dimension(20, 0)));

        add(getThemedLabel("Start Tick: "));
        add(startTick);
        add(Box.createRigidArea(new Dimension(20, 0)));

        add(getThemedLabel("End Tick: "));
        add(endTick);
        add(Box.createRigidArea(new Dimension(30, 0)));

        lineText.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                changed();
            }

            public void changed()
            {
                parent.changeLineText(lineText.getText());
            }
        });
        add(getThemedLabel("Line Text: "));
        add(lineText);
        add(Box.createRigidArea(new Dimension(20, 0)));


        weaponCD.setSelected(true);
        parent.setEnforceCD(true);
        add(weaponCD);
        add(Box.createRigidArea(new Dimension(250, 0)));

    }
}
