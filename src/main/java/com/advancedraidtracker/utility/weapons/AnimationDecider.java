package com.advancedraidtracker.utility.weapons;

import java.util.Arrays;
import java.util.List;

public class AnimationDecider
{

    /**
     *
     * @param animationS
     * @param graphics
     * @param projectileS
     * @param weapon
     * @return
     */
    public static PlayerAnimation getWeapon(String animationS, String graphics, String projectileS, int weapon)
    { //todo redo all of this ** magic numbers will be fixed then too
        PlayerAnimation event = PlayerAnimation.UNDECIDED;
        List<String> spotAnims = Arrays.asList(graphics.split(":"));
        int projectile;
        int animation;
        try
        {
            projectile = Integer.parseInt(projectileS);
            animation = Integer.parseInt(animationS);
        } catch (Exception e)
        {
            return event;
        }
        switch (animation)
        {
            case 10893:
                event = PlayerAnimation.COLOSSEUM_SPECIAL;
                break;
            case 10847:
            case 10859:
            case 10850:
            case 10853:
            case 10856:
            case 10892:
                event = PlayerAnimation.COLOSSEUM_AUTO;
                break;
            case 5061:
            case 10656:
                if (projectile == 1043 || projectile == 2599)
                {
                    event = PlayerAnimation.BLOWPIPE_SPEC;
                } else
                {
                    event = PlayerAnimation.BLOWPIPE;
                }
                break;
            case 1167:
                if (spotAnims.stream().anyMatch(p -> p.equalsIgnoreCase("1540")))
                {
                    event = PlayerAnimation.SANG;
                } else
                {
                    if (weapon == 22516)
                    {
                        if (projectile == 1547)
                        {
                            event = PlayerAnimation.DAWN_SPEC;
                        } else
                        {
                            event = PlayerAnimation.DAWN_AUTO;
                        }
                    } else
                    {
                        event = PlayerAnimation.SANG;
                    }
                }
                break;
            case 1979:
                event = PlayerAnimation.FREEZE; //todo add lowercase if bad weapon
                break;
            case 8056:
                event = PlayerAnimation.SCYTHE;
                break;
            case 7511:
                event = PlayerAnimation.DINHS_SPEC;
                break;
            case 7618:
                event = PlayerAnimation.CHIN;
                break;
            case 1658:
                event = PlayerAnimation.WHIP;
                break;
            case 401:
                event = PlayerAnimation.HAMMER_BOP;
                break;
            case 1378:
                if (weapon == 27690)
                {
                    event = PlayerAnimation.VOID_WAKER_SPEC;
                    break;
                } else
                {
                    event = PlayerAnimation.HAMMER;
                }
                break;
            case 428:
            case 419:
                if (weapon == 25981)
                {
                    event = PlayerAnimation.KERIS_BREACHING;
                    break;
                } else if (weapon == 27291)
                {
                    event = PlayerAnimation.KERIS_SUN;
                    break;
                } else if (weapon == 27287)
                {
                    event = PlayerAnimation.KERIS_CORRUPTION;
                    break;
                }
            case 440:
                if (weapon == 12904)
                {
                    event = PlayerAnimation.TSOTD;
                    break;
                }
                event = PlayerAnimation.CHALLY_WHACK;
                break;
            case 1203:
                event = PlayerAnimation.CHALLY_SPEC;
                break;
            case 390:
                if (weapon == 27690)
                {
                    event = PlayerAnimation.VOID_WAKER;
                    break;
                }
            case 9471:
                if (weapon == 26219 || weapon == 27246)
                {
                    event = PlayerAnimation.FANG;
                    break;
                }
            case 8288:
            case 386:
                if (weapon == 23995 || weapon == 24551)
                {
                    event = PlayerAnimation.BLADE_OF_SAELDOR;
                    break;
                }
                if (weapon == 27690)
                {
                    event = PlayerAnimation.VOID_WAKER;
                    break;
                }
                event = PlayerAnimation.SWIFT_BLADE;
                break;
            case 7642:
            case 7643:
                event = PlayerAnimation.BGS_SPEC;
                break;
            case 7045:
                event = PlayerAnimation.BGS_WHACK;
                break;
            case 426:
                if (weapon == 20997)
                {
                    event = PlayerAnimation.TBOW;
                } else if (weapon == 27655)
                {
                    event = PlayerAnimation.WEB_WEAVER;
                } else
                {
                    event = PlayerAnimation.TBOW;
                }
                break;
            case 9168:
                if (projectile == 1468)
                {
                    event = PlayerAnimation.ZCB_AUTO;
                } else if (projectile == 1995)
                {
                    event = PlayerAnimation.ZCB_SPEC;
                }
                break;
            case 393:
                event = PlayerAnimation.CLAW_SCRATCH;
                break;
            case 7514:
                event = PlayerAnimation.CLAW_SPEC;
                break;
            case 9493:
                event = PlayerAnimation.SHADOW;
                break;
            case 7554:
                event = PlayerAnimation.DART;
                break;
            case 6299:
                event = PlayerAnimation.SBS;
                break;
            case 100000:
                event = PlayerAnimation.BOUNCE;
                break;
            case 4411:
                event = PlayerAnimation.AID_OTHER;
                break;
            case 8316:
                event = PlayerAnimation.VENG_SELF;
                break;
            case 6294:
                event = PlayerAnimation.HUMIDIFY;
                break;
            case 722:
                event = PlayerAnimation.MAGIC_IMBUE;
                break;
            case 10629:
            case 836:
                event = PlayerAnimation.DEATH;
                break;
            case 8070:
            case 1816:
                event = PlayerAnimation.TELEPORT;
                break;
            case 4409:
                event = PlayerAnimation.HEAL_GROUP;
                break;
            case 9479:
                event = PlayerAnimation.MINING;
                break;
            case 376:
            case 377:
                event = PlayerAnimation.DDS_POKE;
                break;
            case 1062:
                event = PlayerAnimation.DDS_SPEC;
                break;
            case 381:
                if (weapon == 25981)
                {
                    event = PlayerAnimation.KERIS_BREACHING;
                    break;
                } else if (weapon == 27291)
                {
                    event = PlayerAnimation.KERIS_SUN;
                    break;
                } else if (weapon == 27287)
                {
                    event = PlayerAnimation.KERIS_CORRUPTION;
                    break;
                }
            case 827:
                event = PlayerAnimation.PICKUP;
                break;
            case 9546:
                event = PlayerAnimation.KERIS_SUN_SPEC;
                break;
            case 9544:
                event = PlayerAnimation.KERIS_CORRUPTION_SPEC;
                break;
            case 832:
                event = PlayerAnimation.PUSH_JUG;
                break;
            case 4503:
            case 400:
                event = PlayerAnimation.INQ_MACE;
                break;
        }
        return event;
    }
}