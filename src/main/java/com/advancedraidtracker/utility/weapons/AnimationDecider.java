package com.advancedraidtracker.utility.weapons;

import java.util.Arrays;
import java.util.List;

public class AnimationDecider
{
    public static PlayerAnimation getWeapon(String animationS, String graphicsS, String projectileS, int weapon)
    {
        PlayerAnimation event = PlayerAnimation.UNDECIDED;
        List<String> spotAnims = Arrays.asList(graphicsS.split(":"));
        int projectile = 0;
        int animation = 0;

        try
        {
            if (!projectileS.isEmpty())
            {
                projectile = Integer.parseInt(projectileS);
            }
            if (!animationS.isEmpty())
            {
                animation = Integer.parseInt(animationS);
            }
        } catch (Exception e)
        {
            return event;
        }

        int maxMatches = 0;
        int minPopulatedArrays = Integer.MAX_VALUE;
        int animationMatches = 0;

        for (PlayerAnimation playerAnimation : PlayerAnimation.values())
        {
            int matches = 0;
            int populatedArrays = 0;

            int finalAnimation = animation;
            if (playerAnimation.animations.length > 0)
            {
                populatedArrays++;
                if (Arrays.stream(playerAnimation.animations).anyMatch(a -> a == finalAnimation))
                {
                    matches++;
                    animationMatches++;
                }
            }

            if (playerAnimation.graphics.length > 0)
            {
                populatedArrays++;
                boolean graphicMatch = Arrays.stream(playerAnimation.graphics)
                        .anyMatch(g -> spotAnims.contains(String.valueOf(g)));
                if (graphicMatch)
                {
                    matches++;
                }
            }

            if (playerAnimation.weaponIDs.length > 0)
            {
                populatedArrays++;
                if (Arrays.stream(playerAnimation.weaponIDs).anyMatch(w -> w == weapon))
                {
                    matches++;
                }
            }

            int finalProjectile = projectile;
            if (playerAnimation.projectiles.length > 0)
            {
                populatedArrays++;
                if (Arrays.stream(playerAnimation.projectiles).anyMatch(p -> p == finalProjectile))
                {
                    matches++;
                }
            }

            if (populatedArrays == 1 && playerAnimation.animations.length > 0 &&
                    Arrays.stream(playerAnimation.animations).anyMatch(a -> a == finalAnimation))
            {
                event = playerAnimation;
                break;
            }

            if (matches > maxMatches || (matches == maxMatches && populatedArrays < minPopulatedArrays))
            {
                maxMatches = matches;
                minPopulatedArrays = populatedArrays;
                event = playerAnimation;
            }
        }
        if (animationMatches == 0)
        {
            return PlayerAnimation.UNDECIDED;
        }

        return event;
    }
}