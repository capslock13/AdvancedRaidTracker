package com.TheatreTracker.utility;

public class PlayerWornItems
{
    public int helmet = 0;
    public int cape = 0;
    public int amulet = 0;
    public int weapon = 0;
    public int torso = 0;
    public int shield = 0;
    public int legs = 0;
    public int gloves = 0;
    public int boots = 0;
    public PlayerWornItems(String s)
    {
        String[] items = s.split(",");
        if(items.length == 9)
        {
            helmet = Integer.parseInt(items[0]);
            cape = Integer.parseInt(items[0]);
            amulet = Integer.parseInt(items[0]);
            weapon = Integer.parseInt(items[0]);
            torso = Integer.parseInt(items[0]);
            shield = Integer.parseInt(items[0]);
            legs = Integer.parseInt(items[0]);
            gloves = Integer.parseInt(items[0]);
            boots = Integer.parseInt(items[0]);
        }
    }

    public String getHelmet()
    {
        return ItemReference.getNameFromID(helmet);
    }
    public String getCape()
    {
        return ItemReference.getNameFromID(cape);
    }
    public String getAmulet()
    {
        return ItemReference.getNameFromID(amulet);
    }
    public String getWeapon()
    {
        return ItemReference.getNameFromID(weapon);
    }
    public String getTorso()
    {
        return ItemReference.getNameFromID(torso);
    }
    public String getShield()
    {
        return ItemReference.getNameFromID(shield);
    }
    public String getLegs()
    {
        return ItemReference.getNameFromID(legs);
    }
    public String getGloves()
    {
        return ItemReference.getNameFromID(gloves);
    }
    public String getBoots()
    {
        return ItemReference.getNameFromID(boots);
    }

    public String[] getAll()
    {
        return new String[]
                {
                  getHelmet(), getCape(), getAmulet(), getWeapon(), getTorso(), getShield(), getLegs(), getGloves(), getBoots()
                };
    }
}
