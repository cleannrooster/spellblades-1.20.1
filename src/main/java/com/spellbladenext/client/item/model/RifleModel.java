package com.spellbladenext.client.item.model;

import com.spellbladenext.Spellblades;
import com.spellbladenext.items.Orb;
import com.spellbladenext.items.Rifle;
import mod.azure.azurelib.model.GeoModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.spell_power.api.MagicSchool;

@Environment(EnvType.CLIENT)
public class RifleModel extends GeoModel<Rifle> {


    @Override
    public Identifier getModelResource(Rifle orb) {
        return new Identifier(Spellblades.MOD_ID,"geo/rifle.geo.json");
    }

    @Override
    public Identifier getTextureResource(Rifle orb) {

        return new Identifier(Spellblades.MOD_ID, "textures/item/rifle.png");
    }

    @Override
    public Identifier getAnimationResource(Rifle orb) {
        return null;
    }
}