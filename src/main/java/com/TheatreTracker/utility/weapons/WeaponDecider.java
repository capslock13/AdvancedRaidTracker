package com.TheatreTracker.utility.weapons;

import java.util.Arrays;
import java.util.List;

public class WeaponDecider
{
    public static WeaponAttack getWeapon(String animationS, String graphics, String projectileS, int weapon)
    { //todo redo all of this once the incorrect weapon worn if same tick swap is fixed, magic numbers will be fixed then too
        WeaponAttack weaponUsed = WeaponAttack.UNDECIDED;
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
                    weaponUsed = WeaponAttack.BLOWPIPE_SPEC;
                } else
                {
                    weaponUsed = WeaponAttack.BLOWPIPE;
                }
                break;
            case 1167:
                if (spotAnims.stream().anyMatch(p -> p.equalsIgnoreCase("1540")))
                {
                    weaponUsed = WeaponAttack.SANG;
                } else
                {
                    if (weapon == 22516)
                    {
                        if (projectile == 1547)
                        {
                            weaponUsed = WeaponAttack.DAWN_SPEC;
                        } else
                        {
                            weaponUsed = WeaponAttack.DAWN_AUTO;
                        }
                    } else
                    {
                        weaponUsed = WeaponAttack.SANG;
                    }
                }
                break;
            case 1979:
                weaponUsed = WeaponAttack.FREEZE; //todo add lowercase if bad weapon
                break;
            case 8056:
                weaponUsed = WeaponAttack.SCYTHE;
                break;
            case 7511:
                weaponUsed = WeaponAttack.DINHS_SPEC;
                break;
            case 7618:
                weaponUsed = WeaponAttack.CHIN;
                break;
            case 1658:
                weaponUsed = WeaponAttack.WHIP;
                break;
            case 401:
                weaponUsed = WeaponAttack.HAMMER_BOP;
                break;
            case 1378:
                if (weapon == 27690)
                {
                    weaponUsed = WeaponAttack.VOID_WAKER_SPEC;
                    break;
                } else
                {
                    weaponUsed = WeaponAttack.HAMMER;
                }
                break;
            case 428:
            case 419:
                if (weapon == 25981)
                {
                    weaponUsed = WeaponAttack.KERIS_BREACHING;
                    break;
                } else if (weapon == 27291)
                {
                    weaponUsed = WeaponAttack.KERIS_SUN;
                    break;
                } else if (weapon == 27287)
                {
                    weaponUsed = WeaponAttack.KERIS_CORRUPTION;
                    break;
                }
            case 440:
                if (weapon == 12904)
                {
                    weaponUsed = WeaponAttack.TSOTD;
                    break;
                }
                weaponUsed = WeaponAttack.CHALLY_WHACK;
                break;
            case 1203:
                weaponUsed = WeaponAttack.CHALLY_SPEC;
                break;
            case 390:
                if (weapon == 27690)
                {
                    weaponUsed = WeaponAttack.VOID_WAKER;
                    break;
                }
            case 9471:
                if (weapon == 26219 || weapon == 27246)
                {
                    weaponUsed = WeaponAttack.FANG;
                    break;
                }
            case 8288:
            case 386:
                if (weapon == 23995 || weapon == 24551)
                {
                    weaponUsed = WeaponAttack.BLADE_OF_SAELDOR;
                    break;
                }
                if (weapon == 27690)
                {
                    weaponUsed = WeaponAttack.VOID_WAKER;
                    break;
                }
                weaponUsed = WeaponAttack.SWIFT_BLADE;
                break;
            case 7642:
            case 7643:
                weaponUsed = WeaponAttack.BGS_SPEC;
                break;
            case 7045:
                weaponUsed = WeaponAttack.BGS_WHACK;
                break;
            case 426:
                if (weapon == 20997)
                {
                    weaponUsed = WeaponAttack.TBOW;
                } else if (weapon == 27655)
                {
                    weaponUsed = WeaponAttack.WEB_WEAVER;
                } else
                {
                    weaponUsed = WeaponAttack.TBOW;
                }
                break;
            case 9168:
                if (projectile == 1468)
                {
                    weaponUsed = WeaponAttack.ZCB_AUTO;
                } else if (projectile == 1995)
                {
                    weaponUsed = WeaponAttack.ZCB_SPEC;
                }
                break;
            case 393:
                weaponUsed = WeaponAttack.CLAW_SCRATCH;
                break;
            case 7514:
                weaponUsed = WeaponAttack.CLAW_SPEC;
                break;
            case 9493:
                weaponUsed = WeaponAttack.SHADOW;
                break;
            case 7554:
                weaponUsed = WeaponAttack.DART;
                break;
            case 6299:
                weaponUsed = WeaponAttack.SBS;
                break;
            case 100000:
                weaponUsed = WeaponAttack.BOUNCE;
                break;
            case 4411:
                weaponUsed = WeaponAttack.AID_OTHER;
                break;
            case 8316:
                weaponUsed = WeaponAttack.VENG_SELF;
                break;
            case 6294:
                weaponUsed = WeaponAttack.HUMIDIFY;
                break;
            case 722:
                weaponUsed = WeaponAttack.MAGIC_IMBUE;
                break;
            case 10629:
            case 836:
                weaponUsed = WeaponAttack.DEATH;
                break;
            case 8070:
            case 1816:
                weaponUsed = WeaponAttack.TELEPORT;
                break;
            case 4409:
                weaponUsed = WeaponAttack.HEAL_GROUP;
                break;
            case 9479:
                weaponUsed = WeaponAttack.MINING;
                break;
            case 376:
            case 377:
                weaponUsed = WeaponAttack.DDS_POKE;
                break;
            case 1062:
                weaponUsed = WeaponAttack.DDS_SPEC;
                break;
            case 381:
                if (weapon == 25981)
                {
                    weaponUsed = WeaponAttack.KERIS_BREACHING;
                    break;
                } else if (weapon == 27291)
                {
                    weaponUsed = WeaponAttack.KERIS_SUN;
                    break;
                } else if (weapon == 27287)
                {
                    weaponUsed = WeaponAttack.KERIS_CORRUPTION;
                    break;
                }
            case 827:
                weaponUsed = WeaponAttack.PICKUP;
                break;
            case 9546:
                weaponUsed = WeaponAttack.KERIS_SUN_SPEC;
                break;
            case 9544:
                weaponUsed = WeaponAttack.KERIS_CORRUPTION_SPEC;
                break;
            case 832:
                weaponUsed = WeaponAttack.PUSH_JUG;
                break;
        }
        return weaponUsed;
    }
}
//graphics todo recieve veng 725, sbs 1062, veng self 726, magic imbue 141, humidify 1061