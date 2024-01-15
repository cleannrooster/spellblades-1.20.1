package com.spellbladenext.client.item.model;

import com.spellbladenext.Spellblades;
import com.spellbladenext.items.Orb;
import mod.azure.azurelib.model.GeoModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.spell_power.api.MagicSchool;
public class OrbModel extends GeoModel<Orb> {


    @Override
    public Identifier getModelResource(Orb orb) {
        return new Identifier(Spellblades.MOD_ID,"geo/orb.geo.json");
    }

    @Override
    public Identifier getTextureResource(Orb orb) {
        if(orb.getSchool() == MagicSchool.FIRE) {
            return new Identifier(Spellblades.MOD_ID, "textures/item/orb_fire.png");
        }
        if(orb.getSchool() == MagicSchool.FROST) {
            return new Identifier(Spellblades.MOD_ID, "textures/item/orb_frost.png");
        }

        return new Identifier(Spellblades.MOD_ID, "textures/item/orb_arcane.png");
    }

    @Override
    public Identifier getAnimationResource(Orb orb) {
        return new Identifier(Spellblades.MOD_ID,"animations/orb.animations.json");
    }
}