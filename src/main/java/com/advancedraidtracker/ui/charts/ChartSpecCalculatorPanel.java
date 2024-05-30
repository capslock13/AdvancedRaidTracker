package com.advancedraidtracker.ui.charts;

import com.advancedraidtracker.AdvancedRaidTrackerConfig;
import com.advancedraidtracker.ui.charts.chartelements.OutlineBox;
import static com.advancedraidtracker.utility.UISwingUtility.getThemedLabel;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
			int firstReduction = -1;
			List<OutlineBox> orderedAttacks = playerAttacks.get(player).stream().sorted(Comparator.comparing(OutlineBox::getTick)).collect(Collectors.toList());
			for(OutlineBox box : orderedAttacks)
			{
				spec -= box.playerAnimation.spec;
				if(firstReduction < 0 && spec < 100)
				{
					firstReduction = box.tick;
				}
				if(spec == -5)
				{
					//todo future use
					//log.info(player + " needs to preach and start between tick " + (157-box.tick) + " and " + (157-firstReduction));
				}
				else if(spec < 0)
				{
					//todo future use
					//log.info(player + "went below 0 spec on tick " + box.tick);
				}
			}
			//todo future use
			//log.info(player + " ended with " + spec + " spec");
		}
	}
}
