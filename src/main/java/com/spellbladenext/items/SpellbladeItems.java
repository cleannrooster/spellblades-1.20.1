package com.spellbladenext.items;

import com.spellbladenext.Spellblades;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import java.util.HashMap;

public class SpellbladeItems {
    public static final HashMap<String, Item> entries;
    static {
        entries = new HashMap<>();
        for(var weaponEntry: Items.entries) {
            entries.put(weaponEntry.id().toString(), weaponEntry.item());
        }
        entries.put("spellblades:whirlwindoil", Spellblades.whirlwindOil);
        entries.put("spellblades:spelloil", Spellblades.spellOil);
        entries.put("spellblades:smiteoil", Spellblades.smiteOil);
        entries.put("spellblades:monkeystaff", Spellblades.MONKEYSTAFF);



    }
}