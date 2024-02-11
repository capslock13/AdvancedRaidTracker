package com.TheatreTracker.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeaponDecider
{
    public static WeaponAttack getWeapon(String animationS, String graphics, String projectileS, String weaponS)
    {
        WeaponAttack weaponUsed = WeaponAttack.UNDECIDED;
        List<String> spotAnims = Arrays.asList(graphics.split(":"));
        int projectile;
        int weapon;
        int animation;
        try
        {
            projectile = Integer.parseInt(projectileS);
            weapon = Integer.parseInt(weaponS);
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
                weaponUsed = WeaponAttack.FREEZE; //add lowercase if bad weapon
                break;
            case 8056:
                weaponUsed = WeaponAttack.SCYTHE;
                break;
            case 7511:
                //if(spotAnims.stream().anyMatch(p->p.contains("1336") || p.contains("2623")))
                //{
                //  weaponUsed = WeaponAttack.DINHS_SPEC;
                //}
                //else
                //{
                weaponUsed = WeaponAttack.DINHS_SPEC;
                //}
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
                weaponUsed = WeaponAttack.HAMMER;
                break;
            case 440:
                weaponUsed = WeaponAttack.CHALLY_WHACK;
                break;
            case 1203:
                weaponUsed = WeaponAttack.CHALLY_SPEC;
                break;
            case 8288:
            case 390:
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
                if(weapon == 20997)
                {
                    weaponUsed = WeaponAttack.TBOW;
                }
                else if(weapon == 27655)
                {
                    weaponUsed = WeaponAttack.WEB_WEAVER;
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
        }
        return weaponUsed;
    }
}
//graphics todo recieve veng 725, sbs 1062, veng self 726, magic imbue 141, humidify 1061