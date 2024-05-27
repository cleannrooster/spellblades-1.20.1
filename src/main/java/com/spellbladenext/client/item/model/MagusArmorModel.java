package com.spellbladenext.client.item.model;

import com.spellbladenext.Spellblades;
import com.spellbladenext.items.armor.MagisterArmor;
import com.spellbladenext.items.armor.MagusArmor;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;

public class MagusArmorModel extends GeoModel<MagusArmor> {


    @Override
    public Identifier getModelResource(MagusArmor orb) {

        return new Identifier(Spellblades.MOD_ID,"geo/robes.geo.json");

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
