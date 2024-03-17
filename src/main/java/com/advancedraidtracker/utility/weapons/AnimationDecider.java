package com.advancedraidtracker.utility.weapons;

import java.util.Arrays;
import java.util.List;

public class AnimationDecider
{
    public static PlayerAnimation getWeapon(String animationS, String graphics, String projectileS, int weapon)
    { //todo redo all of this ** magic numbers will be fixed then too
        PlayerAnimation weaponUsed = PlayerAnimation.UNDECIDED;
        List<String> spotAnims = Arrays.asList(graphics.split(":"));
        int projectile;
        int animation;
        try
        {
            projectile = Integer.parseInt(projectileS);
            animation = Integer.parseInt(animationS);
        } catch (Exception e)
        {
            return weaponUsed;
        }
        switch (animation)
        {
            case 5061:
            case 10656:
                if (projectile == 1043 || projectile == 2599)
                {
                    weaponUsed = PlayerAnimation.BLOWPIPE_SPEC;
                } else
                {
                    weaponUsed = PlayerAnimation.BLOWPIPE;
                }
                break;
            case 1167:
                if (spotAnims.stream().anyMatch(p -> p.equalsIgnoreCase("1540")))
                {
                    weaponUsed = PlayerAnimation.SANG;
                } else
                {
                    if (weapon == 22516)
                    {
                        if (projectile == 1547)
                        {
                            weaponUsed = PlayerAnimation.DAWN_SPEC;
                        } else
                        {
                            weaponUsed = PlayerAnimation.DAWN_AUTO;
                        }
                    } else
                    {
                        weaponUsed = PlayerAnimation.SANG;
                    }
                }
                break;
            case 1979:
                weaponUsed = PlayerAnimation.FREEZE; //todo add lowercase if bad weapon
                break;
            case 8056:
                weaponUsed = PlayerAnimation.SCYTHE;
                break;
            case 7511:
                weaponUsed = PlayerAnimation.DINHS_SPEC;
                break;
            case 7618:
                weaponUsed = PlayerAnimation.CHIN;
                break;
            case 1658:
                weaponUsed = PlayerAnimation.WHIP;
                break;
            case 401:
                weaponUsed = PlayerAnimation.HAMMER_BOP;
                break;
            case 1378:
                if (weapon == 27690)
                {
                    weaponUsed = PlayerAnimation.VOID_WAKER_SPEC;
                    break;
                } else
                {
                    weaponUsed = PlayerAnimation.HAMMER;
                }
                break;
            case 428:
            case 419:
                if (weapon == 25981)
                {
                    weaponUsed = PlayerAnimation.KERIS_BREACHING;
                    break;
                } else if (weapon == 27291)
                {
                    weaponUsed = PlayerAnimation.KERIS_SUN;
                    break;
                } else if (weapon == 27287)
                {
                    weaponUsed = PlayerAnimation.KERIS_CORRUPTION;
                    break;
                }
            case 440:
                if (weapon == 12904)
                {
                    weaponUsed = PlayerAnimation.TSOTD;
                    break;
                }
                weaponUsed = PlayerAnimation.CHALLY_WHACK;
                break;
            case 1203:
                weaponUsed = PlayerAnimation.CHALLY_SPEC;
                break;
            case 390:
                if (weapon == 27690)
                {
                    weaponUsed = PlayerAnimation.VOID_WAKER;
                    break;
                }
            case 9471:
                if (weapon == 26219 || weapon == 27246)
                {
                    weaponUsed = PlayerAnimation.FANG;
                    break;
                }
            case 8288:
            case 386:
                if (weapon == 23995 || weapon == 24551)
                {
                    weaponUsed = PlayerAnimation.BLADE_OF_SAELDOR;
                    break;
                }
                if (weapon == 27690)
                {
                    weaponUsed = PlayerAnimation.VOID_WAKER;
                    break;
                }
                weaponUsed = PlayerAnimation.SWIFT_BLADE;
                break;
            case 7642:
            case 7643:
                weaponUsed = PlayerAnimation.BGS_SPEC;
                break;
            case 7045:
                weaponUsed = PlayerAnimation.BGS_WHACK;
                break;
            case 426:
                if (weapon == 20997)
                {
                    weaponUsed = PlayerAnimation.TBOW;
                } else if (weapon == 27655)
                {
                    weaponUsed = PlayerAnimation.WEB_WEAVER;
                } else
                {
                    weaponUsed = PlayerAnimation.TBOW;
                }
                break;
            case 9168:
                if (projectile == 1468)
                {
                    weaponUsed = PlayerAnimation.ZCB_AUTO;
                } else if (projectile == 1995)
                {
                    weaponUsed = PlayerAnimation.ZCB_SPEC;
                }
                break;
            case 393:
                weaponUsed = PlayerAnimation.CLAW_SCRATCH;
                break;
            case 7514:
                weaponUsed = PlayerAnimation.CLAW_SPEC;
                break;
            case 9493:
                weaponUsed = PlayerAnimation.SHADOW;
                break;
            case 7554:
                weaponUsed = PlayerAnimation.DART;
                break;
            case 6299:
                weaponUsed = PlayerAnimation.SBS;
                break;
            case 100000:
                weaponUsed = PlayerAnimation.BOUNCE;
                break;
            case 4411:
                weaponUsed = PlayerAnimation.AID_OTHER;
                break;
            case 8316:
                weaponUsed = PlayerAnimation.VENG_SELF;
                break;
            case 6294:
                weaponUsed = PlayerAnimation.HUMIDIFY;
                break;
            case 722:
                weaponUsed = PlayerAnimation.MAGIC_IMBUE;
                break;
            case 10629:
            case 836:
                weaponUsed = PlayerAnimation.DEATH;
                break;
            case 8070:
            case 1816:
                weaponUsed = PlayerAnimation.TELEPORT;
                break;
            case 4409:
                weaponUsed = PlayerAnimation.HEAL_GROUP;
                break;
            case 9479:
                weaponUsed = PlayerAnimation.MINING;
                break;
            case 376:
            case 377:
                weaponUsed = PlayerAnimation.DDS_POKE;
                break;
            case 1062:
                weaponUsed = PlayerAnimation.DDS_SPEC;
                break;
            case 381:
                if (weapon == 25981)
                {
                    weaponUsed = PlayerAnimation.KERIS_BREACHING;
                    break;
                } else if (weapon == 27291)
                {
                    weaponUsed = PlayerAnimation.KERIS_SUN;
                    break;
                } else if (weapon == 27287)
                {
                    weaponUsed = PlayerAnimation.KERIS_CORRUPTION;
                    break;
                }
            case 827:
                weaponUsed = PlayerAnimation.PICKUP;
                break;
            case 9546:
                weaponUsed = PlayerAnimation.KERIS_SUN_SPEC;
                break;
            case 9544:
                weaponUsed = PlayerAnimation.KERIS_CORRUPTION_SPEC;
                break;
            case 832:
                weaponUsed = PlayerAnimation.PUSH_JUG;
                break;
            case 4503:
            case 400:
                weaponUsed = PlayerAnimation.INQ_MACE;
                break;
        }
        return weaponUsed;
    }
}