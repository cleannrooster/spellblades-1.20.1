package com.spellbladenext.client.item.model;

import com.spellbladenext.Spellblades;
import com.spellbladenext.items.Spellblade;
import com.spellbladenext.items.armor.MagisterArmor;
import com.spellbladenext.items.armor.MagusArmor;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spell_power.api.enchantment.Enchantments_SpellPowerMechanics;
import net.spell_power.api.enchantment.SpellPowerEnchanting;
import net.spell_power.internals.SchoolFilteredEnchantment;

public class MagusArmorItemModel extends GeoModel<MagusArmor> {
    @Override
    public Identifier getModelResource(MagusArmor orb) {
        if(orb.getSlotType() == EquipmentSlot.CHEST){
            return new Identifier(Spellblades.MOD_ID,"geo/robeitem.geo.json");

        }
        if(orb.getSlotType() == EquipmentSlot.FEET){
            return new Identifier(Spellblades.MOD_ID,"geo/bootsitem.geo.json");

        }
        if(orb.getSlotType() == EquipmentSlot.LEGS){
            return new Identifier(Spellblades.MOD_ID,"geo/pantsitem.geo.json");

        }
        return new Identifier(Spellblades.MOD_ID,"geo/hooditem.json");
    }


    @Override
    public Identifier getTextureResource(MagusArmor orb) {

        return new Identifier(Spellblades.MOD_ID, "textures/armor/robestexture_default.png");
    }

    @Override
    public Identifier getAnimationResource(MagusArmor orb) {
        return null;
    }

}
