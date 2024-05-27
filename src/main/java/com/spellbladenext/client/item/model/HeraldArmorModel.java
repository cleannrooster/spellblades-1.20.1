package com.spellbladenext.client.item.model;

import com.spellbladenext.Spellblades;
import com.spellbladenext.items.armor.HeraldArmor;
import com.spellbladenext.items.armor.MagisterArmor;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchools;

public class HeraldArmorModel extends GeoModel<HeraldArmor> {


    @Override
    public Identifier getModelResource(HeraldArmor animatable) {
        if(animatable.getMagicschool().contains(SpellSchools.ARCANE)) {

            return new Identifier(Spellblades.MOD_ID, "geo/enigmaherald.geo.json");
        }
        if(animatable.getMagicschool().contains(SpellSchools.FIRE)) {

            return new Identifier(Spellblades.MOD_ID, "geo/ashenherald.geo.json");
        }
        if(animatable.getMagicschool().contains(SpellSchools.FROST)) {

            return new Identifier(Spellblades.MOD_ID, "geo/frigidherald.geo.json");
        }
        return new Identifier(Spellblades.MOD_ID, "geo/enigmaherald.geo.json");

    }

    @Override
    public Identifier getTextureResource(HeraldArmor animatable) {

        if(animatable.getMagicschool().contains(SpellSchools.ARCANE)){

            return new Identifier(Spellblades.MOD_ID,"textures/armor/enigmaherald.png");
        }
        if(animatable.getMagicschool().contains(SpellSchools.FROST)){

            return new Identifier(Spellblades.MOD_ID,"textures/armor/frigidherald.png");
        }
        if(animatable.getMagicschool().contains(SpellSchools.FIRE)){

            return new Identifier(Spellblades.MOD_ID,"textures/armor/ashenherald.png");
        }
    /*    if(animatable.getMagicschool().contains(MagicSchool.FIRE)){

            return new Identifier(Spellblades.MOD_ID,"textures/armor/rimeblaze.png");

        }*/
        return new Identifier(Spellblades.MOD_ID,"textures/armor/enigmaherald.png");
    }

    @Override
    public Identifier getAnimationResource(HeraldArmor animatable) {
        return null;
    }
}
