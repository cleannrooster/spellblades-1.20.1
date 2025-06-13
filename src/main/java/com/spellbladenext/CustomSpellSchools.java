package com.spellbladenext;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;

public class CustomSpellSchools {
    public static SpellSchool ARMOR;
    static{
        ARMOR = new SpellSchool(SpellSchool.Archetype.MELEE, Identifier.of("spell_power", "armor"), 11776947, DamageTypes.PLAYER_ATTACK, EntityAttributes.GENERIC_ARMOR);
    }
    public static void register(){
        ARMOR.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD, queryArgs ->{
            return (double) queryArgs.entity().getArmor();
        });
        SpellSchools.register(ARMOR);

    }
}
