package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import static com.advancedraidtracker.utility.UISwingUtility.getThemedLabel;
import com.advancedraidtracker.utility.datautility.datapoints.Raid;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.JPanel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChartSpecCalculatorPanel extends JPanel implements ChartListener
{
	List<OutlineBox> attacks = new ArrayList<>();
	@Override
	public void onChartChanged(ChartChangedEvent event)
	{
		if(event.objectType == ChartObjectType.ATTACK)
		{
			if(event.actionType == ChartActionType.ADD_ELEMENT)
			{
				for(Object obj : event.chartObjects)
				{
					attacks.add((OutlineBox) obj);
				}
			}
			else if(event.actionType == ChartActionType.REMOVE_ELEMENT)
			{
				for(Object obj : event.chartObjects)
				{
					attacks.remove((OutlineBox) obj);
				}
			}
		}
		updateSpecs();
	}
	private JLabel label;
	public ChartSpecCalculatorPanel(AdvancedRaidTrackerConfig config)
	{
		setBackground(config.primaryDark());
		setForeground(config.fontColor());
		setOpaque(true);
		label = getThemedLabel();
		setLayout(new BorderLayout());
		add(label, BorderLayout.CENTER);
	}

	public void updateSpecs()
	{
		Multimap<String, OutlineBox> playerAttacks = ArrayListMultimap.create();
		for(OutlineBox box : attacks)
		{
			playerAttacks.put(box.player, box);
		}

		for(String player : playerAttacks.keySet())
		{
			int spec = 100;
			List<OutlineBox> orderedAttacks = playerAttacks.get(player).stream().sorted(Comparator.comparing(OutlineBox::getTick)).collect(Collectors.toList());
			for(OutlineBox box : orderedAttacks)
			{
				spec -= box.playerAnimation.spec;
				if(spec < 0)
				{
					log.info(player + " ran out of spec on tick " + box.tick);
				}
			}
			log.info(player + " ended with " + spec + " spec");
		}
	}
}
