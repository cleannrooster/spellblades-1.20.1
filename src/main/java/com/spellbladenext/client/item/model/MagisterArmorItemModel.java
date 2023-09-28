package com.spellbladenext.client.item.model;

import com.spellbladenext.Spellblades;
import com.spellbladenext.items.armor.MagisterArmor;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spell_power.api.MagicSchool;

public class MagisterArmorItemModel extends GeoModel<MagisterArmor> {
    @Override
    public Identifier getModelResource(MagisterArmor orb) {
        if(orb.getSlotType() == EquipmentSlot.HEAD){
            if(orb.getMagicschool().contains(MagicSchool.ARCANE)){

                    return new Identifier(Spellblades.MOD_ID,"geo/magebane.geo.json");


            }
            if(orb.getMagicschool().contains(MagicSchool.FROST)){
                return new Identifier(Spellblades.MOD_ID,"geo/mageseeker.geo.json");

            }
            return new Identifier(Spellblades.MOD_ID,"geo/magebreaker.geo.json");

        }
        if(orb.getSlotType() == EquipmentSlot.CHEST){
            return new Identifier(Spellblades.MOD_ID,"geo/inquisitor_chest.json");

        }
        if(orb.getSlotType() == EquipmentSlot.FEET){
            return new Identifier(Spellblades.MOD_ID,"geo/inquisitor_feet.json");

        }
            return new Identifier(Spellblades.MOD_ID,"geo/inquisitor_legs.json");


    }

    @Override
    public Identifier getTextureResource(MagisterArmor orb) {
        if(orb.getSlotType() == EquipmentSlot.HEAD){
            if(orb.getMagicschool().contains(MagicSchool.ARCANE)){
                return new Identifier(Spellblades.MOD_ID,"textures/armor/magebane_crown.png");


            }
            if(orb.getMagicschool().contains(MagicSchool.FROST)){
                return new Identifier(Spellblades.MOD_ID,"textures/armor/mageseeker_hat.png");

            }
            return new Identifier(Spellblades.MOD_ID,"textures/armor/magebreaker_helmet.png");

        }
        if(orb.getMagicschool().contains(MagicSchool.ARCANE)){

            return new Identifier(Spellblades.MOD_ID,"textures/armor/aetherfire.png");
        }
        if(orb.getMagicschool().contains(MagicSchool.FIRE)){
            return new Identifier(Spellblades.MOD_ID,"textures/armor/rimeblaze.png");

        }
            return new Identifier(Spellblades.MOD_ID,"textures/armor/deathchill.png");

    }


    @Override
    public Identifier getAnimationResource(MagisterArmor orb) {
        return null;
    }

}
