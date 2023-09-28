package com.spellbladenext.client.item.model;

import com.spellbladenext.Spellblades;
import com.spellbladenext.items.armor.MagisterArmor;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spell_power.api.MagicSchool;

public class MagisterArmorModel extends GeoModel<MagisterArmor> {


    @Override
    public Identifier getModelResource(MagisterArmor animatable) {
        if(animatable.getSlotType() == EquipmentSlot.HEAD){
            if(animatable.getMagicschool().contains(MagicSchool.ARCANE)){
                return new Identifier(Spellblades.MOD_ID,"geo/magebane.geo.json");



            }
            if(animatable.getMagicschool().contains(MagicSchool.FIRE)){
                return new Identifier(Spellblades.MOD_ID,"geo/magebreaker.geo.json");


            }
            if(animatable.getMagicschool().contains(MagicSchool.FROST)){
                return new Identifier(Spellblades.MOD_ID,"geo/mageseeker.geo.json");

            }

        }
        return new Identifier(Spellblades.MOD_ID,"geo/inquisitor.geo.json");
    }

    @Override
    public Identifier getTextureResource(MagisterArmor animatable) {
        if(animatable.getSlotType() == EquipmentSlot.HEAD){
            if(animatable.getMagicschool().contains(MagicSchool.ARCANE)){

                return new Identifier(Spellblades.MOD_ID,"textures/armor/magebane_crown.png");

            }
            if(animatable.getMagicschool().contains(MagicSchool.FROST)){

                return new Identifier(Spellblades.MOD_ID,"textures/armor/mageseeker_hat.png");

            }
            if(animatable.getMagicschool().contains(MagicSchool.FIRE)){

                return new Identifier(Spellblades.MOD_ID,"textures/armor/magebreaker_helmet.png");


            }

        }
        if(animatable.getMagicschool().contains(MagicSchool.ARCANE)){

            return new Identifier(Spellblades.MOD_ID,"textures/armor/aetherfire.png");
        }
        if(animatable.getMagicschool().contains(MagicSchool.FIRE)){

            return new Identifier(Spellblades.MOD_ID,"textures/armor/rimeblaze.png");

        }
        return new Identifier(Spellblades.MOD_ID,"textures/armor/deathchill.png");
    }

    @Override
    public Identifier getAnimationResource(MagisterArmor animatable) {
        return null;
    }
}
