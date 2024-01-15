package com.spellbladenext.client.entity;

import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.CycloneEntity;
import com.spellbladenext.entity.RedLaserEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class RedLaserModel<T extends RedLaserEntity> extends GeoModel<RedLaserEntity> {

    @Override
    public Identifier getModelResource(RedLaserEntity reaver) {

        return new Identifier(Spellblades.MOD_ID,"geo/redbeam.geo.json");
    }
    @Override
    public Identifier getTextureResource(RedLaserEntity reaver) {
            return new Identifier(Spellblades.MOD_ID, "textures/mob/redbeam.png");

    }

    @Override
    public Identifier getAnimationResource(RedLaserEntity reaver) {
        return new Identifier(Spellblades.MOD_ID,"animations/redbeam.animation.json");
    }

}
