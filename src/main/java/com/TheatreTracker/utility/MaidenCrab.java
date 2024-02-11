package com.TheatreTracker.utility;

import lombok.Getter;
import net.runelite.api.NPC;

public class MaidenCrab
{
        public NPC crab;
        public int maxHealth;
        public int health;
        public String description;

        public MaidenCrab(NPC crab, int scale, String description)
        {
            switch (scale)
            {
                case 5:
                    maxHealth = 100;
                    break;
                case 4:
                    maxHealth = 87;
                    break;
                default:
                    maxHealth = 75;
                    break;
            }
            this.crab = crab;
            health = maxHealth;
            this.description = description;
        }
}
